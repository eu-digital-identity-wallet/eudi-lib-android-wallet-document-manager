/*
 * Copyright (c) 2023-2024 European Commission
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

import android.content.Context
import com.android.identity.securearea.CreateKeySettings
import com.android.identity.securearea.KeyUnlockData
import com.android.identity.securearea.SecureArea
import com.android.identity.storage.StorageEngine
import eu.europa.ec.eudi.wallet.document.defaults.DefaultSecureArea
import eu.europa.ec.eudi.wallet.document.defaults.DefaultStorageEngine

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
     *   .storageEngine(MyStorageEngine())
     *   .secureArea(MySecureArea())
     *   .createKeySettingsFactory(MyCreateKeySettingsFactory())
     *   .checkPublicKeyBeforeAdding(true)
     *   .build()
     * ```
     *
     * @property storageEngine storage engine used to store documents. By default, this is set to [DefaultStorageEngine].
     * @property secureArea secure area used to store documents' keys. By default, this is set to [DefaultSecureArea].
     * @property createKeySettingsFactory factory to create [CreateKeySettings] for document keys. By default, this is set to [DefaultSecureArea.CreateKeySettingsFactory].
     * @property checkPublicKeyBeforeAdding flag that indicates if the public key from the [UnsignedDocument] must match the public key in MSO. By default this is set to true.
     *
     * @constructor
     * @param context [Context] used to instantiate the DocumentManager
     */
    class Builder(context: Context) {
        private val context = context.applicationContext
        var storageEngine: StorageEngine? = null
        var secureArea: SecureArea? = null
        var createKeySettingsFactory: CreateKeySettingsFactory? = null
        var keyUnlockDataFactory: KeyUnlockDataFactory? = null
        var checkPublicKeyBeforeAdding: Boolean = true

        /**
         * Sets the storage engine to store the documents.
         * By default, this is set to [DefaultStorageEngine].
         * The storage engine is used to store the documents in the device's storage.
         */
        fun storageEngine(storageEngine: StorageEngine) =
            apply { this.storageEngine = storageEngine }

        /**
         * Sets the secure area that manages the keys for the documents.
         * By default, this is set to [DefaultSecureArea].
         */
        fun secureArea(secureArea: SecureArea) = apply { this.secureArea = secureArea }

        /**
         * Sets the factory to create [CreateKeySettings] for document keys.
         * By default, this is set to [DefaultSecureArea.CreateKeySettingsFactory].
         * This factory is used to create [CreateKeySettings] for the keys that are created in the secure area.
         * The [CreateKeySettings] can be used to set the key's alias, key's purpose, key's protection level, etc.
         */
        fun createKeySettingsFactory(createKeySettingsFactory: CreateKeySettingsFactory) =
            apply { this.createKeySettingsFactory = createKeySettingsFactory }


        /**
         * Sets the factory to create [KeyUnlockData] for document keys.
         * By default, this is set to [DefaultSecureArea.KeyUnlockDataFactory].
         * This factory is used to create [KeyUnlockData] that is used to unlock the keys in the secure area.
         * @param keyUnlockDataFactory
         * @see [KeyUnlockData]
         * @return
         */
        fun keyUnlockDataFactory(keyUnlockDataFactory: KeyUnlockDataFactory) =
            apply { this.keyUnlockDataFactory = keyUnlockDataFactory }

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
        fun build(): DocumentManager {
            val storageEngineImpl = storageEngine ?: DefaultStorageEngine(context)
            val secureAreaImpl = secureArea ?: DefaultSecureArea(context, storageEngineImpl)
            val createKeySettingsFactory =
                createKeySettingsFactory ?: DefaultSecureArea.CreateKeySettingsFactory(context)
            return DocumentManagerImpl(
                storageEngine = storageEngineImpl,
                secureArea = secureAreaImpl,
                createKeySettingsFactory = createKeySettingsFactory,
                keyUnlockDataFactory = keyUnlockDataFactory
                    ?: DefaultSecureArea.KeyUnlockDataFactory,
                checkPublicKeyBeforeAdding = checkPublicKeyBeforeAdding
            )
        }

    }

    companion object {

        /**
         * Instantiates a DocumentManager using the provided context and the builder block.
         */
        operator fun invoke(context: Context, block: Builder.() -> Unit): DocumentManager {
            return Builder(context).apply(block).build()
        }
    }
}


