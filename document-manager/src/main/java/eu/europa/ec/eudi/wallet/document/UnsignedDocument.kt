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
import com.android.identity.crypto.javaPublicKey
import com.android.identity.crypto.javaX509Certificates
import com.android.identity.crypto.toDer
import com.android.identity.securearea.KeyLockedException
import eu.europa.ec.eudi.wallet.document.Document.State
import eu.europa.ec.eudi.wallet.document.internal.createdAt
import eu.europa.ec.eudi.wallet.document.internal.docType
import eu.europa.ec.eudi.wallet.document.internal.documentName
import eu.europa.ec.eudi.wallet.document.internal.requiresUserAuth
import eu.europa.ec.eudi.wallet.document.internal.state
import eu.europa.ec.eudi.wallet.document.internal.toCoseBytes
import eu.europa.ec.eudi.wallet.document.internal.toEcPublicKey
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
 * @property id the identifier of the document
 * @property name the name of the document. This name can be updated before the document is issued
 * @property docType the document type
 * @property usesStrongBox whether the document's keys are in strongBox
 * @property requiresUserAuth whether the document requires user authentication
 * @property createdAt the creation date of the document
 * @property publicKey public key of the first certificate in [certificatesNeedAuth] list to be included in mobile security object that it will be signed from issuer
 * @property certificatesNeedAuth list of certificates that will be used for issuing the document. This list may be empty
 */

open class UnsignedDocument(
    override val id: DocumentId,
    override val docType: String,
    override var name: String = docType,
    override val usesStrongBox: Boolean,
    override val requiresUserAuth: Boolean,
    override val createdAt: Instant,
    val publicKeyCoseBytes: ByteArray,
    val certificatesNeedAuth: List<X509Certificate>
) : Document {

    val publicKey: PublicKey
        get() = publicKeyCoseBytes.toEcPublicKey.javaPublicKey

    override var state: State = State.UNSIGNED
        protected set

    open fun signWithAuthKey(
        data: ByteArray,
        @Algorithm alg: String = Algorithm.SHA256withECDSA
    ): SignedWithAuthKeyResult {
        return SignedWithAuthKeyResult.Failure(Exception("Not implemented"))
    }

    internal companion object {
        @JvmSynthetic
        operator fun invoke(
            baseDocument: BaseDocument,
            keyUnlockDataFactory: KeyUnlockDataFactory
        ): UnsignedDocument = UnsignedDocumentImpl(
            baseDocument = baseDocument,
            keyUnlockDataFactory = keyUnlockDataFactory
        )
    }
}

internal class UnsignedDocumentImpl internal constructor(
    private val baseDocument: BaseDocument,
    private val keyUnlockDataFactory: KeyUnlockDataFactory
) : UnsignedDocument(
    id = baseDocument.name,
    docType = baseDocument.docType,
    name = baseDocument.documentName,
    usesStrongBox = baseDocument.usesStrongBox,
    requiresUserAuth = baseDocument.requiresUserAuth,
    createdAt = baseDocument.createdAt,
    publicKeyCoseBytes = baseDocument.pendingCredentials
        .filterIsInstance<SecureAreaBoundCredential>()
        .first()
        .attestation
        .publicKey
        .toCoseBytes,
    certificatesNeedAuth = baseDocument.pendingCredentials
        .filterIsInstance<SecureAreaBoundCredential>()
        .firstOrNull()
        ?.attestation
        ?.certChain
        ?.javaX509Certificates ?: emptyList(),
) {
    init {
        state = baseDocument.state
    }

    override var name: String
        get() = baseDocument.documentName
        set(value) {
            baseDocument.documentName = value
        }

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
    override fun signWithAuthKey(
        data: ByteArray,
        @Algorithm alg: String
    ): SignedWithAuthKeyResult {
        return baseDocument.pendingCredentials
            .filterIsInstance<SecureAreaBoundCredential>()
            .firstOrNull()
            ?.let { cred ->
                val keyUnlockData =
                    keyUnlockDataFactory.createKeyUnlockData(cred.secureArea, cred.alias)
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
                            when (keyUnlockData) {
                                is AndroidKeystoreKeyUnlockData -> keyUnlockData.getCryptoObjectForSigning(
                                    alg.algorithm
                                )

                                else -> null
                            }
                        )

                        else -> SignedWithAuthKeyResult.Failure(e)
                    }
                }


            }
            ?: SignedWithAuthKeyResult.Failure(Exception("Not initialized correctly. Use DocumentManager.createDocument method."))
    }

    override fun toString(): String {
        return "UnsignedDocument(id=$id, docType='$docType', usesStrongBox=$usesStrongBox, requiresUserAuth=$requiresUserAuth, createdAt=$createdAt, state=$state, certificatesNeedAuth=$certificatesNeedAuth, name='$name')"
    }
}