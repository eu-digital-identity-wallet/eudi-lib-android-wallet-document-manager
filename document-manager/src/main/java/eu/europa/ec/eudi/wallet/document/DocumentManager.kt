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

import android.content.Context
import com.android.identity.android.securearea.AndroidKeystoreSecureArea
import com.android.identity.android.storage.AndroidStorageEngine
import com.android.identity.storage.StorageEngine
import eu.europa.ec.eudi.wallet.document.internal.isDeviceSecure
import kotlinx.io.files.Path
import java.io.File

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
     * @param state optional state of the document
     * @return list of documents
     */
    fun getDocuments(state: Document.State? = null): List<Document>

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
     * Creates a [UnsignedDocument] for a given docType which can be then used to issue the document
     * from the issuer. The [UnsignedDocument] contains the certificate that must be sent to the issuer
     * and implements [UnsignedDocument.signWithAuthKey] to sign the proof of possession if needed by the
     * issuer.
     *
     * @param docType document's docType (example: "eu.europa.ec.eudi.pid.1")
     * @param useStrongBox whether the document should be stored in hardware backed storage
     * @param attestationChallenge optional attestationChallenge to check provided by the issuer
     * @return [CreateDocumentResult.Success] containing the issuance request if successful, [CreateDocumentResult.Failure] otherwise
     */
    fun createDocument(
        docType: String,
        useStrongBox: Boolean,
        attestationChallenge: ByteArray? = null,
    ): CreateDocumentResult

    /**
     * Add document to the document manager.
     *
     * Expected data format is CBOR. The CBOR data must be in the following structure:
     *
     * ```cddl
     * IssuerSigned = {
     *   ?"nameSpaces" : IssuerNameSpaces, ; Returned data elements
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
     * **Important** Currently `nameSpaces` field should exist and must not be empty.
     *
     * The document is added in the storage and can be retrieved using the
     * [DocumentManager.getDocumentById] method.
     *
     * @param unsignedDocument [UnsignedDocument] containing necessary information of the issued the document
     * @param issuerDocumentData in CBOR format containing the document's data
     * @return [StoreDocumentResult.Success] containing the documentId and the proof of provisioning if successful, [StoreDocumentResult.Failure] otherwise
     */
    fun storeIssuedDocument(
        unsignedDocument: UnsignedDocument,
        issuerDocumentData: ByteArray
    ): StoreDocumentResult

    /**
     * Stores a [UnsignedDocument] as [DeferredDocument]. The document can be retrieved using the [DocumentManager.getDocumentById] method.
     * Also, the relatedData can be used later for the issuance process.
     *
     * @param unsignedDocument [UnsignedDocument] containing necessary information of the issued the document
     * @param relatedData related data to deferred process to be stored with the document
     * @return [StoreDocumentResult.Success] containing the documentId if successful, [StoreDocumentResult.Failure] otherwise
     */
    fun storeDeferredDocument(
        unsignedDocument: UnsignedDocument,
        relatedData: ByteArray
    ): StoreDocumentResult

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
     * @property checkPublicKeyBeforeAdding flag that indicates if the public key from the [UnsignedDocument] must match the public key in MSO. By default this is set to true.
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
        var checkPublicKeyBeforeAdding: Boolean = true

        /**
         * Sets whether to encrypt the values stored on disk.
         * Note that keys are not encrypted, only values.
         * By default, this is set to true.
         *
         * @param useEncryption
         * @return [DocumentManager.Builder]
         */
        fun useEncryption(useEncryption: Boolean) = apply { this.useEncryption = useEncryption }

        /**
         * The directory to store data files in.
         * By default, the [Context.getNoBackupFilesDir] is used.
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
         * Sets whether to check public key in MSO before adding document to storage.
         * By default, this is set to true.
         * This check is done to prevent adding documents with public key that is not in MSO.
         * The public key from the [UnsignedDocument] must match the public key in MSO.
         *
         * @see [DocumentManager.storeIssuedDocument]
         *
         * @param checkPublicKeyBeforeAdding
         */
        fun checkPublicKeyBeforeAdding(checkPublicKeyBeforeAdding: Boolean) =
            apply { this.checkPublicKeyBeforeAdding = checkPublicKeyBeforeAdding }

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
            get() = AndroidStorageEngine.Builder(
                _context,
                Path(File(storageDir.path, "eudi-identity.bin").path)
            )
                .setUseEncryption(useEncryption)
                .build()

        private val androidSecureArea: AndroidKeystoreSecureArea
            get() = AndroidKeystoreSecureArea(_context, storageEngine)
    }
}


