/*
 *  Copyright (c) 2023-2024 European Commission
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

import COSE.Message
import COSE.MessageTag
import COSE.Sign1Message
import android.content.Context
import android.util.Log
import com.android.identity.android.securearea.AndroidKeystoreSecureArea
import com.android.identity.android.securearea.AndroidKeystoreSecureArea.*
import com.android.identity.credential.CredentialStore
import com.android.identity.mdoc.mso.MobileSecurityObjectParser
import com.android.identity.mdoc.mso.StaticAuthDataGenerator
import com.android.identity.securearea.SecureAreaRepository
import com.android.identity.storage.StorageEngine
import com.upokecenter.cbor.CBORObject
import eu.europa.ec.eudi.wallet.document.internal.*
import java.security.SecureRandom
import java.time.Instant
import java.util.*


/**
 * A [DocumentManager] implementation that uses [StorageEngine] to store documents and [AndroidKeystoreSecureArea] for key management.
 *
 * Features:
 * - Enforces user authentication to access documents, if supported by the device
 * - Enforces hardware backed keys, if supported by the device
 * - P256 curve and Sign1 support for document keys
 *
 * To instantiate it, use the [eu.europa.ec.eudi.wallet.document.DocumentManager.Builder] class.
 *
 * @property storageEngine storage engine used to store documents
 * @property secureArea secure area used to store documents' keys
 * @property userAuth flag that indicates if the document requires user authentication to be accessed. If the device is not secured, this will be set to false.
 * @property userAuthTimeoutInMillis timeout in milliseconds for user authentication
 * @property checkPublicKeyBeforeAdding flag that indicates if the public key in the [UnsignedDocument] must match the public key in MSO
 *
 * @constructor
 * @param context
 * @param storageEngine storage engine used to store documents
 * @param secureArea secure area used to store documents' keys
 */
class DocumentManagerImpl(
    context: Context,
    private val storageEngine: StorageEngine,
    private val secureArea: AndroidKeystoreSecureArea,
) : DocumentManager {

    private val context = context.applicationContext
    private val isDeviceSecure: Boolean
        get() = context.isDeviceSecure

    private val secureAreaRepository: SecureAreaRepository by lazy {
        SecureAreaRepository().apply {
            addImplementation(secureArea)
        }
    }

    private val credentialStore: CredentialStore by lazy {
        CredentialStore(storageEngine, secureAreaRepository)
    }

    var userAuth: Boolean = isDeviceSecure
        /**
         * Sets whether to require user authentication to access the document.
         * If the device is not secured, this will be set to false.
         */
        set(value) {
            field = value && isDeviceSecure
        }

    var userAuthTimeoutInMillis: Long = AUTH_TIMEOUT

    var checkPublicKeyBeforeAdding: Boolean = true

    /**
     * Sets whether to require user authentication to access the document.
     *
     * @param enable
     * @return [DocumentManagerImpl]
     */
    fun userAuth(enable: Boolean) = apply { this.userAuth = enable }

    /**
     * Sets the timeout in milliseconds for user authentication.
     *
     * @param timeoutInMillis timeout in milliseconds for user authentication
     * @return [DocumentManagerImpl]
     */
    fun userAuthTimeout(timeoutInMillis: Long) =
        apply { this.userAuthTimeoutInMillis = timeoutInMillis }

    /**
     * Sets whether to check public key in MSO before adding document to storage.
     * By default this is set to true.
     * This check is done to prevent adding documents with public key that is not in MSO.
     * The public key from the [UnsignedDocument] must match the public key in MSO.
     *
     * @see [DocumentManager.storeIssuedDocument]
     *
     * @param check
     */
    fun checkPublicKeyBeforeAdding(check: Boolean) =
        apply { this.checkPublicKeyBeforeAdding = check }

    override fun getDocuments(state: Document.State?): List<Document> {
        val credentials = credentialStore.listCredentials().mapNotNull { credentialName ->
            credentialStore.lookupCredential(credentialName)
        }
        return when (state) {
            null -> credentials
            else -> credentials.filter { it.state == state }
        }.map { Document(it) }
    }

    override fun getDocumentById(documentId: DocumentId): Document? =
        credentialStore.lookupCredential(documentId)?.let { Document(it) }

    override fun deleteDocumentById(documentId: DocumentId): DeleteDocumentResult {
        return try {
            val proofOfDeletion = byteArrayOf()
            credentialStore.deleteCredential(documentId)
            DeleteDocumentResult.Success(proofOfDeletion)
        } catch (e: Exception) {
            DeleteDocumentResult.Failure(e)
        }
    }

    override fun createDocument(
        docType: String,
        useStrongBox: Boolean,
        attestationChallenge: ByteArray?
    ): CreateDocumentResult {
        return try {

            val documentId = "${UUID.randomUUID()}"
            val strongBoxed = useStrongBox && context.supportsStrongBox
            val nonEmptyChallenge = attestationChallenge
                ?.takeUnless { it.isEmpty() }
                ?: generateRandomBytes(10)
            val keySettings = createKeySettings(nonEmptyChallenge, strongBoxed)
            val credential = credentialStore.createCredential(documentId, keySettings).apply {
                state = Document.State.UNSIGNED
                this.docType = docType
                documentName = docType
                createdAt = Instant.now()
                this.attestationChallenge = nonEmptyChallenge

                createPendingAuthenticationKey(keySettings, null)
            }
            val unsignedDocument = UnsignedDocument(credential)
            CreateDocumentResult.Success(unsignedDocument)
        } catch (e: Exception) {
            CreateDocumentResult.Failure(e)
        }
    }

    override fun storeIssuedDocument(
        unsignedDocument: UnsignedDocument,
        issuerDocumentData: ByteArray
    ): StoreDocumentResult {
        try {
            val credential = credentialStore.lookupCredential(unsignedDocument.id)
                ?: return StoreDocumentResult.Failure(IllegalArgumentException("No credential found for ${unsignedDocument.id}"))

            val issuerSigned = CBORObject.DecodeFromBytes(issuerDocumentData)

            with(credential) {
                val issuerAuthBytes = issuerSigned["issuerAuth"].EncodeToBytes()
                val issuerAuth = Message
                    .DecodeFromBytes(issuerAuthBytes, MessageTag.Sign1) as Sign1Message

                val msoBytes = issuerAuth.GetContent().getEmbeddedCBORObject().EncodeToBytes()

                val mso = MobileSecurityObjectParser()
                    .setMobileSecurityObject(msoBytes)
                    .parse()

                if (mso.deviceKey != unsignedDocument.publicKey) {
                    val msg = "Public key in MSO does not match the one in the request"
                    Log.d(TAG, msg)
                    if (checkPublicKeyBeforeAdding) {
                        return StoreDocumentResult.Failure(IllegalArgumentException(msg))
                    }
                }
                state = Document.State.ISSUED
                docType = mso.docType
                issuedAt = Instant.now()
                clearDeferredRelatedData()

                val nameSpaces = issuerSigned["nameSpaces"]
                val digestIdMapping = nameSpaces.toDigestIdMapping()
                val staticAuthData = StaticAuthDataGenerator(digestIdMapping, issuerAuthBytes)
                    .generate()
                pendingAuthenticationKeys.forEach { key ->
                    key.certify(staticAuthData, mso.validFrom, mso.validUntil)
                }

                nameSpacedData = nameSpaces.asNameSpacedData()
            }
            return StoreDocumentResult.Success(credential.name, null)
        } catch (e: Exception) {
            return StoreDocumentResult.Failure(e)
        }
    }

    override fun storeDeferredDocument(
        unsignedDocument: UnsignedDocument,
        relatedData: ByteArray
    ): StoreDocumentResult {
        try {
            val credential = credentialStore.lookupCredential(unsignedDocument.id)
                ?: return StoreDocumentResult.Failure(IllegalArgumentException("No credential found for ${unsignedDocument.id}"))

            with(credential) {
                state = Document.State.DEFERRED
                deferredRelatedData = relatedData
            }
            return StoreDocumentResult.Success(credential.name, byteArrayOf())
        } catch (e: Exception) {
            return StoreDocumentResult.Failure(e)
        }
    }

    private fun createKeySettings(
        challenge: ByteArray,
        useStrongBox: Boolean,
    ) = CreateKeySettings.Builder(challenge)
        .setEcCurve(EC_CURVE_P256)
        .setUseStrongBox(useStrongBox)
        .setUserAuthenticationRequired(userAuth, userAuthTimeoutInMillis, AUTH_TYPE)
        .setKeyPurposes(KEY_PURPOSE_SIGN)
        .build()

    companion object {
        private const val TAG = "DocumentManager"

        const val AUTH_TIMEOUT = 30_000L
        private const val AUTH_TYPE =
            USER_AUTHENTICATION_TYPE_BIOMETRIC or USER_AUTHENTICATION_TYPE_LSKF
    }
}

private fun generateRandomBytes(size: Int): ByteArray {
    val secureRandom = SecureRandom()
    val randomBytes = ByteArray(size)
    secureRandom.nextBytes(randomBytes)
    return randomBytes
}


