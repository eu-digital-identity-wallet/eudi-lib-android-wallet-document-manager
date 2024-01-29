/*
 * Copyright (c) 2023 European Commission
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

import COSE.Message
import COSE.MessageTag
import COSE.Sign1Message
import android.content.Context
import android.util.Log
import com.android.identity.android.securearea.AndroidKeystoreSecureArea
import com.android.identity.android.securearea.AndroidKeystoreSecureArea.KEY_PURPOSE_SIGN
import com.android.identity.android.securearea.AndroidKeystoreSecureArea.USER_AUTHENTICATION_TYPE_BIOMETRIC
import com.android.identity.android.securearea.AndroidKeystoreSecureArea.USER_AUTHENTICATION_TYPE_LSKF
import com.android.identity.credential.Credential
import com.android.identity.credential.CredentialStore
import com.android.identity.credential.NameSpacedData
import com.android.identity.mdoc.mso.MobileSecurityObjectParser
import com.android.identity.mdoc.mso.StaticAuthDataGenerator
import com.android.identity.securearea.SecureArea
import com.android.identity.securearea.SecureAreaRepository
import com.android.identity.storage.StorageEngine
import com.upokecenter.cbor.CBORObject
import eu.europa.ec.eudi.wallet.document.internal.getEmbeddedCBORObject
import eu.europa.ec.eudi.wallet.document.internal.isDeviceSecure
import eu.europa.ec.eudi.wallet.document.internal.supportsStrongBox
import eu.europa.ec.eudi.wallet.document.internal.withTag24
import java.time.Instant
import java.util.UUID
import kotlin.random.Random

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
 * @property userAuth flag that indicates if the document requires user authentication to be accessed
 * @property userAuthTimeoutInMillis timeout in milliseconds for user authentication
 * @property checkPublicKeyBeforeAdding flag that indicates if the public key in the [IssuanceRequest] must match the public key in MSO
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
     * The public key from the [IssuanceRequest] must match the public key in MSO.
     *
     * @see [DocumentManager.addDocument]
     *
     * @param check
     */
    fun checkPublicKeyBeforeAdding(check: Boolean) =
        apply { this.checkPublicKeyBeforeAdding = check }

    override fun getDocuments(): List<Document> =
        credentialStore.listCredentials().mapNotNull { credentialName ->
            credentialStore.lookupCredential(credentialName)?.asDocument
        }

    override fun getDocumentById(documentId: DocumentId): Document? =
        credentialStore.lookupCredential(documentId)?.asDocument

    override fun deleteDocumentById(documentId: DocumentId): DeleteDocumentResult {
        return try {
            val proofOfDeletion = byteArrayOf()
            credentialStore.deleteCredential(documentId)
            DeleteDocumentResult.Success(proofOfDeletion)
        } catch (e: Exception) {
            DeleteDocumentResult.Failure(e)
        }
    }

    override fun createIssuanceRequest(
        docType: String,
        hardwareBacked: Boolean,
        attestationChallenge: ByteArray?,
    ): CreateIssuanceRequestResult = try {
        val documentId = "${UUID.randomUUID()}"
        val strongBoxed = hardwareBacked && context.supportsStrongBox
        val nonEmptyChallenge = attestationChallenge
            ?.takeUnless { it.isEmpty() }
            ?: Random.nextBytes(10)
        val keySettings = createKeySettings(nonEmptyChallenge, strongBoxed)
        val credential = credentialStore.createCredential(documentId, keySettings)
        val request = IssuanceRequest(docType, credential, keySettings)
        CreateIssuanceRequestResult.Success(request)
    } catch (e: Exception) {
        CreateIssuanceRequestResult.Failure(e)
    }

    override fun addDocument(request: IssuanceRequest, data: ByteArray): AddDocumentResult {
        try {
            val credential = credentialStore.lookupCredential(request.documentId)
                ?: return AddDocumentResult.Failure(IllegalArgumentException("No credential found for ${request.documentId}"))
            val cbor = CBORObject.DecodeFromBytes(data)

            val documentCbor =
                cbor["documents"].values.firstOrNull { it["docType"].AsString() == request.docType }
                    ?: return AddDocumentResult.Failure(
                        IllegalArgumentException("No document found for ${request.docType}"),
                    )

            credential.apply {
                val issuerSigned = documentCbor["issuerSigned"]
                val issuerAuthBytes = issuerSigned["issuerAuth"].EncodeToBytes()
                val issuerAuth = Message
                    .DecodeFromBytes(issuerAuthBytes, MessageTag.Sign1) as Sign1Message

                val msoBytes = issuerAuth.GetContent().getEmbeddedCBORObject().EncodeToBytes()

                val mso = MobileSecurityObjectParser()
                    .setMobileSecurityObject(msoBytes)
                    .parse()

                if (mso.deviceKey != request.publicKey) {
                    val msg = "Public key in MSO does not match the one in the request"
                    Log.d(TAG, msg)
                    if (checkPublicKeyBeforeAdding) {
                        return AddDocumentResult.Failure(IllegalArgumentException(msg))
                    }
                }

                applicationData.setString(DOCUMENT_NAME, request.name)
                applicationData.setNumber(DOCUMENT_CREATED_AT, Instant.now().toEpochMilli())
                applicationData.setString(DOCUMENT_DOC_TYPE, mso.docType)
                applicationData.setBoolean(DOCUMENT_REQUIRES_USER_AUTH, userAuth)

                val nameSpaces = issuerSigned["nameSpaces"]
                val digestIdMapping = nameSpaces.toDigestIdMapping()
                val staticAuthData = StaticAuthDataGenerator(digestIdMapping, issuerAuthBytes)
                    .generate()
                credential.pendingAuthenticationKeys.forEach { key ->
                    key.certify(staticAuthData, mso.validFrom, mso.validUntil)
                }

                nameSpacedData = nameSpaces.asNameSpacedData()
            }
            return AddDocumentResult.Success(credential.name, byteArrayOf())
        } catch (e: Exception) {
            return AddDocumentResult.Failure(e)
        }
    }

    private fun createKeySettings(
        challenge: ByteArray,
        hardwareBacked: Boolean,
    ) = AndroidKeystoreSecureArea.CreateKeySettings.Builder(challenge)
        .setEcCurve(SecureArea.EC_CURVE_P256)
        .setUseStrongBox(hardwareBacked)
        .setUserAuthenticationRequired(userAuth, userAuthTimeoutInMillis, AUTH_TYPE)
        .setKeyPurposes(KEY_PURPOSE_SIGN)
        .build()

    private fun CBORObject.toDigestIdMapping(): Map<String, List<ByteArray>> = keys.associate {
        it.AsString() to this[it].values.map { v ->
            val el = v.getEmbeddedCBORObject()
            CBORObject.NewMap()
                .Add("digestID", el["digestID"])
                .Add("random", el["random"])
                .Add("elementIdentifier", el["elementIdentifier"])
                .Add("elementValue", CBORObject.Null)
                .EncodeToBytes()
                .withTag24()
        }
    }

    companion object {
        private const val TAG = "DocumentManagerImpl"

        @JvmStatic
        val AUTH_TIMEOUT = 30_000L
        private const val AUTH_TYPE =
            USER_AUTHENTICATION_TYPE_BIOMETRIC or USER_AUTHENTICATION_TYPE_LSKF

        private const val DOCUMENT_DOC_TYPE = "docType"
        private const val DOCUMENT_NAME = "name"
        private const val DOCUMENT_CREATED_AT = "createdAt"
        private const val DOCUMENT_REQUIRES_USER_AUTH = "requiresUserAuth"

        private fun CBORObject.asNameSpacedData(): NameSpacedData {
            val builder = NameSpacedData.Builder()
            keys.forEach { nameSpace ->
                this[nameSpace].values.forEach { v ->
                    val el = v.getEmbeddedCBORObject()
                    builder.putEntry(
                        nameSpace.AsString(),
                        el["elementIdentifier"].AsString(),
                        el["elementValue"].EncodeToBytes(),
                    )
                }
            }
            return builder.build()
        }
    }

    private val Credential.asDocument: Document?
        get() {
            if (this.pendingAuthenticationKeys.isNotEmpty()) return null

            return Document(
                id = name,
                docType = applicationData.getString(DOCUMENT_DOC_TYPE),
                name = applicationData.getString(DOCUMENT_NAME),
                hardwareBacked = authenticationKeys.firstOrNull()?.alias?.let {
                    credentialSecureArea.getKeyInfo(it).isHardwareBacked
                } ?: false,
                createdAt = Instant.ofEpochMilli(applicationData.getNumber(DOCUMENT_CREATED_AT)),
                requiresUserAuth = applicationData.getBoolean(DOCUMENT_REQUIRES_USER_AUTH),
                nameSpacedData = nameSpacedData.nameSpaceNames.associateWith { nameSpace ->
                    nameSpacedData.getDataElementNames(nameSpace)
                        .associateWith { elementIdentifier ->
                            nameSpacedData.getDataElement(nameSpace, elementIdentifier)
                        }
                },
            )
        }
}
