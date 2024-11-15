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

import com.android.identity.securearea.SecureArea
import com.android.identity.securearea.SecureAreaRepository
import com.android.identity.storage.StorageEngine
import eu.europa.ec.eudi.wallet.document.format.DocumentFormat

/**
 * The DocumentManager interface is the main entry point to interact with documents.
 * It is a high-level abstraction that provides a simplified API to interact with documents.
 *
 * It provides methods to:
 * - Create a new document
 * - Store a document
 * - Retrieve a document
 * - Delete a document
 * - List all documents
 *
 * To create a default instance of the DocumentManager, use the companion object or the [Builder] class.
 *
 * @see [Builder]
 *
 * @property identifier the identifier of the document manager
 */
interface DocumentManager {

    val identifier: String
    val storageEngine: StorageEngine
    val secureAreaRepository: SecureAreaRepository

    /**
     * Retrieve a document by its identifier.
     *
     * @param documentId the identifier of the document
     * @return the document or null if not found
     */
    fun getDocumentById(documentId: DocumentId): Document?

    /**
     * Retrieve all documents.
     *
     * @param predicate a query to filter the documents
     * @return the list of documents
     */
    fun getDocuments(predicate: ((Document) -> Boolean)? = null): List<Document>

    /**
     * Delete a document by its identifier.
     *
     * @param documentId the identifier of the document
     * @return the result of the deletion. If successful, it will return a proof of deletion. If not, it will return an error.
     */
    fun deleteDocumentById(documentId: DocumentId): Outcome<ProofOfDeletion?>

    /**
     * Create a new document. This method will create a new document with the given format and keys settings.
     * If the document is successfully created, it will return an [UnsignedDocument]. This [UnsignedDocument]
     * contains the keys and the method to proof the ownership of the keys, that can be used with an issuer
     * to retrieve the document's claims. After that the document can be stored using [storeIssuedDocument] or [storeDeferredDocument].
     *
     * @param format the format of the document
     * @param createSettings the settings to create the document with
     * @return the result of the creation. If successful, it will return the document. If not, it will return an error.
     */
    fun createDocument(
        format: DocumentFormat,
        createSettings: CreateDocumentSettings,
    ): Outcome<UnsignedDocument>

    /**
     * Store an issued document. This method will store the document with its issuer provided data.
     *
     * @param unsignedDocument the unsigned document
     * @param issuerProvidedData the issuer provided data
     * @return the result of the storage. If successful, it will return the [IssuedDocument]. If not, it will return an error.
     */
    fun storeIssuedDocument(
        unsignedDocument: UnsignedDocument,
        issuerProvidedData: ByteArray,
    ): Outcome<IssuedDocument>

    /**
     * Store an unsigned document for deferred issuance. This method will store the document with the related
     * to the issuance data.
     *
     * @param unsignedDocument the unsigned document
     * @param relatedData the related data
     * @return the result of the storage. If successful, it will return the [DeferredDocument]. If not, it will return an error.
     */
    fun storeDeferredDocument(
        unsignedDocument: UnsignedDocument,
        relatedData: ByteArray
    ): Outcome<DeferredDocument>


    /**
     * Builder class to create a [DocumentManager] instance.
     * @property identifier the identifier of the document manager
     * @property storageEngine the storage engine to use for storing/retrieving documents
     * @property secureArea the secure area to use for managing the keys
     */
    class Builder {
        var identifier: String? = null
        var storageEngine: StorageEngine? = null
        var secureAreaRepository = SecureAreaRepository()

        /**
         * Set the identifier of the document manager.
         * @param identifier the identifier
         * @return this builder
         */
        fun setIdentifier(identifier: String): Builder = apply {
            this.identifier = identifier
        }

        /**
         * Set the storage engine to use for storing/retrieving documents.
         * @param storageEngine the storage engine
         * @return this builder
         */
        fun setStorageEngine(storageEngine: StorageEngine): Builder = apply {
            this.storageEngine = storageEngine
        }

        /**
         * Sets the [secureAreaRepository] that will be used for documents' keys management
         * @param secureAreaRepository the secure area repository
         * @return this builder
         */
        fun setSecureAreaRepository(secureAreaRepository: SecureAreaRepository) = apply {
            this.secureAreaRepository = secureAreaRepository
        }

        /**
         * Adds a [SecureArea] implementation to [secureAreaRepository]
         * @param secureArea the secure area
         * @return this builder
         */
        fun addSecureArea(secureArea: SecureArea): Builder = apply {
            this.secureAreaRepository.addImplementation(secureArea)
        }

        /**
         * Build a [DocumentManager] instance.
         * @throws IllegalArgumentException if the storage engine or secure area is not set
         * @return the document manager
         */
        fun build(): DocumentManager {
            requireNotNull(identifier) { "Identifier is required" }
            requireNotNull(storageEngine) { "Storage engine is required" }
            require(secureAreaRepository.implementations.isNotEmpty()) { "SecureAreaRepository is empty" }
            return DocumentManagerImpl(
                identifier = identifier!!,
                storageEngine = storageEngine!!,
                secureAreaRepository = secureAreaRepository
            )
        }
    }

    /**
     * Companion object to create a [DocumentManager] instance.
     */
    companion object {
        /**
         * Create a [DocumentManager] instance.
         * @param configure the builder configuration
         * @throws IllegalArgumentException if the storage engine or secure area is not set
         * @return the document manager
         */
        @JvmStatic
        operator fun invoke(configure: Builder.() -> Unit): DocumentManager {
            return Builder().apply(configure).build()
        }
    }
}