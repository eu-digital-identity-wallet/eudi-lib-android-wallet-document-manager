/*
 *  Copyright (c) 2024 European Commission
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package eu.europa.ec.eudi.wallet.document

import com.android.identity.android.securearea.AndroidKeystoreSecureArea
import com.android.identity.credential.Credential
import com.android.identity.securearea.SecureArea
import eu.europa.ec.eudi.wallet.document.Document.State
import eu.europa.ec.eudi.wallet.document.internal.*
import java.security.PublicKey
import java.security.cert.X509Certificate
import java.time.Instant

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
    internal var credential: Credential? = null

    override var name: String = name
        set(value) {
            field = value
            credential?.let { it.documentName = value }
        }

    override val state: State
        get() = credential?.state ?: State.UNSIGNED

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
        return when (val cred = credential) {
            null -> SignedWithAuthKeyResult.Failure(Exception("Not initialized correctly. Use DocumentManager.createDocument method."))
            else -> {
                val pendingAuthKey = cred.pendingAuthenticationKeys.first()
                val keyUnlockData =
                    AndroidKeystoreSecureArea.KeyUnlockData(pendingAuthKey.alias)
                try {
                    cred.credentialSecureArea.sign(
                        pendingAuthKey.alias,
                        alg.algorithm,
                        data,
                        keyUnlockData
                    ).let {
                        SignedWithAuthKeyResult.Success(it)
                    }
                } catch (e: Exception) {
                    when (e) {
                        is SecureArea.KeyLockedException -> SignedWithAuthKeyResult.UserAuthRequired(
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
        operator fun invoke(credential: Credential) = UnsignedDocument(
            id = credential.name,
            name = credential.documentName,
            docType = credential.docType,
            usesStrongBox = credential.usesStrongBox,
            requiresUserAuth = credential.requiresUserAuth,
            createdAt = credential.createdAt,
            certificatesNeedAuth = credential.pendingAuthenticationKeys.first().attestation
        ).also { it.credential = credential }
    }


}