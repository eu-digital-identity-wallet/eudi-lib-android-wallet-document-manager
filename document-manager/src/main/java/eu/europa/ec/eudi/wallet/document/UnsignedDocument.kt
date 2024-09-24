/*
 * Copyright (c) 2024 European Commission
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package eu.europa.ec.eudi.wallet.document

import com.android.identity.android.securearea.AndroidKeystoreKeyUnlockData
import com.android.identity.credential.SecureAreaBoundCredential
import com.android.identity.crypto.EcPublicKey
import com.android.identity.crypto.javaX509Certificates
import com.android.identity.crypto.toDer
import com.android.identity.securearea.KeyLockedException
import eu.europa.ec.eudi.wallet.document.Document.State
import eu.europa.ec.eudi.wallet.document.internal.createdAt
import eu.europa.ec.eudi.wallet.document.internal.docType
import eu.europa.ec.eudi.wallet.document.internal.documentName
import eu.europa.ec.eudi.wallet.document.internal.requiresUserAuth
import eu.europa.ec.eudi.wallet.document.internal.state
import eu.europa.ec.eudi.wallet.document.internal.usesStrongBox
import java.security.PublicKey
import java.security.cert.X509Certificate
import java.time.Instant
import com.android.identity.document.Document as BaseDocument

/**
 * A [UnsignedDocument] is a document that is in the process of being issued.
 * It contains the information required to issue the document and can be used to sign the
 * proof of possession required by the issuers using the [UnsignedDocument.signWithAuthKey] method.
 *
 * Use the [DocumentManager.createDocument] method to create a [UnsignedDocument]
 *
 * Once the document is issued and document's data are available by the issuer, use the
 * [DocumentManager.storeIssuedDocument] to store the document. This will transform the [UnsignedDocument] to
 * an [IssuedDocument]
 *
 * @property name the name of the document. This name can be updated before the document is issued
 * @property certificatesNeedAuth list of certificates that will be used for issuing the document
 * @property publicKey public key of the first certificate in [certificatesNeedAuth] list to be included in mobile security object that it will be signed from issuer
 */
open class UnsignedDocument(
    override val id: DocumentId,
    name: String,
    final override val docType: String,
    override val usesStrongBox: Boolean,
    override val requiresUserAuth: Boolean,
    override val createdAt: Instant,
    val certificatesNeedAuth: List<X509Certificate>
) : Document {

    @JvmSynthetic
    internal var base: BaseDocument? = null

    internal val ecPublicKey: EcPublicKey?
        get() = base?.pendingCredentials
            ?.firstOrNull() { it is SecureAreaBoundCredential }
            ?.let { it as SecureAreaBoundCredential }
            ?.attestation
            ?.publicKey

    override var name: String = name
        set(value) {
            field = value
            base?.let { it.documentName = value }
        }

    override val state: State
        get() = base?.state ?: State.UNSIGNED

    val publicKey: PublicKey
        get() = certificatesNeedAuth.first().publicKey

    /**
     * Sign given data with authentication key
     *
     * Available algorithms are:
     * - [Algorithm.SHA256withECDSA]
     *
     * @param data to be signed
     * @param alg algorithm to be used for signing the data (example: "SHA256withECDSA")
     * @return [SignedWithAuthKeyResult.Success] containing the signature if successful,
     * [SignedWithAuthKeyResult.UserAuthRequired] if user authentication is required to sign data,
     * [SignedWithAuthKeyResult.Failure] if an error occurred while signing the data
     */
    fun signWithAuthKey(
        data: ByteArray,
        @Algorithm alg: String = Algorithm.SHA256withECDSA
    ): SignedWithAuthKeyResult {
        return when (val cred = base?.pendingCredentials
            ?.firstOrNull { it is SecureAreaBoundCredential }
            ?.let { it as SecureAreaBoundCredential }) {

            null -> SignedWithAuthKeyResult.Failure(Exception("Not initialized correctly. Use DocumentManager.createDocument method."))
            else -> {
                val keyUnlockData = AndroidKeystoreKeyUnlockData(cred.alias)
                try {
                    cred.secureArea.sign(
                        cred.alias,
                        alg.algorithm,
                        data,
                        keyUnlockData
                    ).let {
                        SignedWithAuthKeyResult.Success(it.toDer())
                    }
                } catch (e: Exception) {
                    when (e) {
                        is KeyLockedException -> SignedWithAuthKeyResult.UserAuthRequired(
                            keyUnlockData.getCryptoObjectForSigning(alg.algorithm)
                        )

                        else -> SignedWithAuthKeyResult.Failure(e)
                    }
                }

            }

        }
    }

    override fun toString(): String {
        return "UnsignedDocument(id=$id, docType='$docType', usesStrongBox=$usesStrongBox, requiresUserAuth=$requiresUserAuth, createdAt=$createdAt, state=$state, certificatesNeedAuth=$certificatesNeedAuth, name='$name')"
    }

    internal companion object {
        @JvmSynthetic
        operator fun invoke(baseDocument: BaseDocument) = UnsignedDocument(
            id = baseDocument.name,
            name = baseDocument.documentName,
            docType = baseDocument.docType,
            usesStrongBox = baseDocument.usesStrongBox,
            requiresUserAuth = baseDocument.requiresUserAuth,
            createdAt = baseDocument.createdAt,
            certificatesNeedAuth = baseDocument.pendingCredentials
                .filterIsInstance<SecureAreaBoundCredential>()
                .firstOrNull()
                ?.attestation
                ?.certChain
                ?.javaX509Certificates
                ?: emptyList(),
        ).also { it.base = baseDocument }
    }
}