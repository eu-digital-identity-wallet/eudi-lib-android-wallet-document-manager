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
@file:JvmMultifileClass

package eu.europa.ec.eudi.wallet.document

import android.content.Context
import com.android.identity.android.securearea.AndroidKeystoreSecureArea
import com.android.identity.android.storage.AndroidStorageEngine
import com.android.identity.storage.StorageEngine
import eu.europa.ec.eudi.wallet.document.internal.isDeviceSecure
import java.io.File
import java.security.cert.X509Certificate

/**
 * Document manager object is the entry point to access documents.
 *
 * It is used to add, retrieve and delete documents.
 *
 * A default implementation of this interface is implemented by [DocumentManagerImpl].
 * To instantiate it, use the [eu.europa.ec.eudi.wallet.document.DocumentManager.Builder] class.
 */
interface DocumentManager {
    /**
     * Retrieve all documents
     *
     * @return list of documents
     */
    fun getDocuments(): List<Document>

    /**
     * Get document by id
     *
     * @param documentId document's unique identifier
     * @return [Document] if exists, null otherwise
     */
    fun getDocumentById(documentId: DocumentId): Document?

    /**
     * Delete document by id
     *
     * @param documentId document's unique identifier
     * @return [DeleteDocumentResult.Success] containing the proof of deletion if successful, [DeleteDocumentResult.Failure] otherwise
     */
    fun deleteDocumentById(documentId: DocumentId): DeleteDocumentResult

    /**
     * Create an issuance request for a given docType. The issuance request can be then used to issue the document
     * from the issuer. The issuance request contains the certificate that must be sent to the issuer.
     *
     * @param docType document's docType (example: "eu.europa.ec.eudiw.pid.1")
     * @param hardwareBacked whether the document should be stored in hardware backed storage
     * @param attestationChallenge optional attestationChallenge to check provided by the issuer
     * @return [CreateIssuanceRequestResult.Success] containing the issuance request if successful, [CreateIssuanceRequestResult.Failure] otherwise
     */
    fun createIssuanceRequest(
        docType: String,
        hardwareBacked: Boolean,
        attestationChallenge: ByteArray? = null,
    ): CreateIssuanceRequestResult

    /**
     * Add document to the document manager.
     *
     * Expected data format is CBOR. The CBOR data must be in the following structure:
     *
     * ```cddl
     * Data = {
     *   "documents" : [+Document], ; Returned documents
     * }
     * Document = {
     *   "docType" : DocType, ; Document type returned
     *   "issuerSigned" : IssuerSigned, ; Returned data elements signed by the issuer
     * }
     * IssuerSigned = {
     *   "nameSpaces" : IssuerNameSpaces, ; Returned data elements
     *   "issuerAuth" : IssuerAuth ; Contains the mobile security object (MSO) for issuer data authentication
     * }
     * IssuerNameSpaces = { ; Returned data elements for each namespace
     *   + NameSpace => [ + IssuerSignedItemBytes ]
     * }
     * IssuerSignedItemBytes = #6.24(bstr .cbor IssuerSignedItem)
     * IssuerSignedItem = {
     *   "digestID" : uint, ; Digest ID for issuer data authentication
     *   "random" : bstr, ; Random value for issuer data authentication
     *   "elementIdentifier" : DataElementIdentifier, ; Data element identifier
     *   "elementValue" : DataElementValue ; Data element value
     * }
     * IssuerAuth = COSE_Sign1 ; The payload is MobileSecurityObjectBytes
     * ```
     *
     * The document is added in the storage and can be retrieved using the
     * [DocumentManager::getDocumentById] method.
     *
     * @param request [IssuanceRequest] containing necessary information of the issued the document
     * @param data in CBOR format containing the document's data
     * @return [AddDocumentResult.Success] containing the documentId and the proof of provisioning if successful, [AddDocumentResult.Failure] otherwise
     */
    fun addDocument(request: IssuanceRequest, data: ByteArray): AddDocumentResult

    /**
     * Builder class to instantiate the default DocumentManager implementation.
     *
     * example:
     * ```
     * val documentManager = DocumentManager.Builder(context)
     *    .useEncryption(true)
     *    .storageDir(context.noBackupFilesDir)
     *    .enableUserAuth(true)
     *    .userAuthTimeout(30000)
     *    .build()
     * ```
     *
     * @property useEncryption whether to encrypt the values stored on disk. Note that keys are not encrypted, only values. By default this is set to true.
     * @property storageDir the directory to store data files in. By default the [Context.getNoBackupFilesDir] is used.
     * @property userAuth flag that indicates if the document requires user authentication to be accessed. By default this is set to true if the device is secured with a PIN, password or pattern.
     * @property userAuthTimeoutInMillis timeout in milliseconds for user authentication. By default this is set to 30 seconds.
     *
     * @constructor
     *
     * @param context [Context] used to instantiate the DocumentManager
     */
    class Builder(context: Context) {
        private val _context = context.applicationContext
        var useEncryption: Boolean = true
        var storageDir: File = _context.noBackupFilesDir
        var userAuth: Boolean = context.isDeviceSecure
        var userAuthTimeoutInMillis: Long = DocumentManagerImpl.AUTH_TIMEOUT

        /**
         * Sets whether to encrypt the values stored on disk.
         * Note that keys are not encrypted, only values.
         * By default this is set to true.
         *
         * @param useEncryption
         * @return [DocumentManager.Builder]
         */
        fun useEncryption(useEncryption: Boolean) = apply { this.useEncryption = useEncryption }

        /**
         * The directory to store data files in.
         * By default the [Context.getNoBackupFilesDir] is used.
         *
         * @param storageDir
         * @return [DocumentManager.Builder]
         */
        fun storageDir(storageDir: File) = apply { this.storageDir = storageDir }

        /**
         * Sets whether to require user authentication to access the document.
         *
         * @param enable
         * @return [DocumentManager.Builder]
         */
        fun enableUserAuth(enable: Boolean) = apply { this.userAuth = enable }

        /**
         * Sets the timeout in milliseconds for user authentication.
         *
         * @param timeoutInMillis timeout in milliseconds for user authentication
         * @return [DocumentManager.Builder]
         */
        fun userAuthTimeout(timeoutInMillis: Long) =
            apply { this.userAuthTimeoutInMillis = timeoutInMillis }

        /**
         * Build the DocumentManager
         *
         * @return [DocumentManager]
         */
        fun build(): DocumentManager =
            DocumentManagerImpl(_context, storageEngine, androidSecureArea).apply {
                userAuth(this@Builder.userAuth)
                userAuthTimeout(this@Builder.userAuthTimeoutInMillis)
            }

        private val storageEngine: StorageEngine
            get() = AndroidStorageEngine.Builder(_context, storageDir)
                .setUseEncryption(useEncryption)
                .build()

        private val androidSecureArea: AndroidKeystoreSecureArea
            get() = AndroidKeystoreSecureArea(_context, storageEngine)
    }
}

/**
 * Add document result sealed interface
 */
sealed interface AddDocumentResult {

    /**
     * Success result containing the documentId.
     * DocumentId can be then used to retrieve the document from the [DocumentManager::getDocumentById] method
     *
     * @property documentId document's unique identifier
     * @property proofOfProvisioning proof of provisioning
     * @constructor
     *
     * @param documentId document's unique identifier
     * @param proofOfProvisioning proof of provisioning
     */
    class Success(val documentId: DocumentId, val proofOfProvisioning: ByteArray) :
        AddDocumentResult

    /**
     * Failure while adding the document. Contains the throwable that caused the failure
     *
     * @property throwable throwable that caused the failure
     * @constructor
     * @param throwable throwable that caused the failure
     */
    data class Failure(val throwable: Throwable) : AddDocumentResult
}

/**
 * Issuance request data class. Contains the necessary information to issue a document.
 * Use the [DocumentManager::createIssuanceRequest] method to create an issuance request.
 *
 * @property documentId
 * @property docType
 * @property name
 * @property hardwareBacked
 * @property requiresUserAuth
 * @property certificateNeedAuth
 */
data class IssuanceRequest(
    val documentId: DocumentId,
    val docType: String,
    var name: String,
    val hardwareBacked: Boolean,
    val requiresUserAuth: Boolean,
    val certificateNeedAuth: X509Certificate,
)

/**
 * Create issuance request result sealed interface
 */
sealed interface CreateIssuanceRequestResult {

    /**
     * Success result containing the issuance request. The issuance request can be then used to issue the document
     * from the issuer. The issuance request contains the certificate chain that must be sent to the issuer.
     *
     * @property issuanceRequest
     *
     * @constructor
     * @param issuanceRequest
     */
    data class Success(val issuanceRequest: IssuanceRequest) : CreateIssuanceRequestResult

    /**
     * Failure while creating the issuance request. Contains the throwable that caused the failure
     *
     * @property throwable
     * @constructor Create empty Failure
     */
    data class Failure(val throwable: Throwable) : CreateIssuanceRequestResult
}

/**
 * Delete document result sealed interface
 */
sealed interface DeleteDocumentResult {
    /**
     * Success result containing the proof of deletion
     *
     * @property proofOfDeletion
     * @constructor
     * @param proofOfDeletion
     */
    data class Success(val proofOfDeletion: ByteArray?) : DeleteDocumentResult {
        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (javaClass != other?.javaClass) return false

            other as Success

            if (proofOfDeletion != null) {
                if (other.proofOfDeletion == null) return false
                if (!proofOfDeletion.contentEquals(other.proofOfDeletion)) return false
            } else if (other.proofOfDeletion != null) return false

            return true
        }

        override fun hashCode(): Int {
            return proofOfDeletion?.contentHashCode() ?: 0
        }
    }

    /**
     * Failure while deleting the document. Contains the throwable that caused the failure
     *
     * @property throwable throwable that caused the failure
     * @constructor
     * @param throwable throwable that caused the failure
     */
    data class Failure(val throwable: Throwable) : DeleteDocumentResult
}
