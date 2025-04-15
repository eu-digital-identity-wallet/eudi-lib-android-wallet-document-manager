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

import eu.europa.ec.eudi.wallet.document.format.DocumentFormat
import eu.europa.ec.eudi.wallet.document.format.MsoMdocFormat
import eu.europa.ec.eudi.wallet.document.format.SdJwtVcFormat
import eu.europa.ec.eudi.wallet.document.internal.ApplicationMetaData
import eu.europa.ec.eudi.wallet.document.internal.SdJwtVcCredential
import eu.europa.ec.eudi.wallet.document.internal.clearDeferredRelatedData
import eu.europa.ec.eudi.wallet.document.internal.createCredential
import eu.europa.ec.eudi.wallet.document.internal.deferredRelatedData
import eu.europa.ec.eudi.wallet.document.internal.documentIdentifier
import eu.europa.ec.eudi.wallet.document.internal.documentManagerId
import eu.europa.ec.eudi.wallet.document.internal.documentName
import eu.europa.ec.eudi.wallet.document.internal.issuedAt
import eu.europa.ec.eudi.wallet.document.internal.storeIssuedDocument
import eu.europa.ec.eudi.wallet.document.internal.toDocument
import eu.europa.ec.eudi.wallet.document.metadata.IssuerMetaData
import io.ktor.client.HttpClient
import kotlinx.coroutines.runBlocking
import kotlinx.datetime.Clock
import org.jetbrains.annotations.VisibleForTesting
import org.multipaz.credential.CredentialLoader
import org.multipaz.document.DocumentStore
import org.multipaz.mdoc.credential.MdocCredential
import org.multipaz.securearea.SecureAreaRepository
import org.multipaz.storage.Storage
import org.multipaz.util.Logger
import org.multipaz.util.UUID

/**
 * Document Manager Implementation
 *
 * @property identifier the identifier
 * @property storage the storage to use for storing/retrieving documents
 * @property secureAreaRepository the secure area
 *
 * @constructor
 * @param identifier the identifier of the document manager
 * @param storage the storage
 * @param secureAreaRepository the secure area
 */
class DocumentManagerImpl(
    override val identifier: String,
    override val storage: Storage,
    override val secureAreaRepository: SecureAreaRepository,
    val ktorHttpClientFactory: (() -> HttpClient)? = null
    // TODO: list trusted certificates
) : DocumentManager {

    @VisibleForTesting
    @get:JvmSynthetic
    internal var checkDevicePublicKey: Boolean = true

    @VisibleForTesting
    @get:JvmSynthetic
    internal val prefix = "Document_${identifier}_"

    @get:VisibleForTesting
    @get:JvmSynthetic
    internal val documentStore by lazy {
        DocumentStore(
            storage = storage,
            secureAreaRepository = secureAreaRepository,
            credentialLoader = CredentialLoader().apply {
                    addCredentialImplementation(MdocCredential::class) { document ->
                        MdocCredential(document)
                    }
                    addCredentialImplementation(SdJwtVcCredential::class) { document ->
                        SdJwtVcCredential(document)
                    }
                },
            documentMetadataFactory = ApplicationMetaData::create
        )
    }

    /**
     * Retrieve a document by its identifier.
     *
     * @param documentId the identifier of the document
     * @return the document or null if not found
     */
    override fun getDocumentById(documentId: DocumentId): Document? {
        return runBlocking {
            try {
                documentStore.lookupDocument(documentId)
                    ?.takeIf { doc -> doc.documentManagerId == identifier }?.toDocument()
            } catch (e: Throwable) {
                Logger.e(TAG, "Failed to lookup document with id $documentId", e)
                null
            }
        }
    }

    /**
     * Retrieve all documents.
     *
     * @param predicate a query to filter the documents
     * @return the list of documents
     */
    override fun getDocuments(predicate: ((Document) -> Boolean)?): List<Document> {
        return runBlocking {
            try {
                documentStore.listDocuments()
                    .mapNotNull {
                        getDocumentById(it)?.takeIf { doc -> doc.documentManagerId == identifier }
                    }
            } catch (e: Throwable) {
                Logger.e(TAG, "Failed to get documents", e)
                emptyList()
            }
        }
    }

    /**
     * Delete a document by its identifier.
     *
     * @param documentId the identifier of the document
     * @return the result of the deletion. If successful, it will return a proof of deletion. If not, it will return an error.
     */
    override fun deleteDocumentById(documentId: DocumentId): Outcome<ProofOfDeletion?> {
        return runBlocking {
            documentStore.lookupDocument(documentId)
                ?.let { identityDocument ->
                    val proofOfDeletion = null
                    documentStore.deleteDocument(identityDocument.identifier)
                    Outcome.success(proofOfDeletion)
                }
                ?: Outcome.failure(IllegalArgumentException("Document with $documentId not found"))
        }
    }

    /**
     * Create a new document. This method will create a new document with the given format and keys settings.
     * If the document is successfully created, it will return an [UnsignedDocument]. This [UnsignedDocument]
     * contains the keys and the method to proof the ownership of the keys, that can be used with an issuer
     * to retrieve the document's claims. After that the document can be stored using [storeIssuedDocument] or [storeDeferredDocument].
     *
     * @param format the format of the document
     * @param createSettings the [CreateDocumentSettings] to use for the new document
     * @param issuerMetaData the [IssuerMetaData] data regarding document display
     * @return the result of the creation. If successful, it will return the document. If not, it will return an error.
     */
    override fun createDocument(
        format: DocumentFormat,
        createSettings: CreateDocumentSettings,
        issuerMetaData: IssuerMetaData?
    ): Outcome<UnsignedDocument> {
        var documentId: String? = null
        return runBlocking {
            try {
                val secureArea =
                    secureAreaRepository.getImplementation(createSettings.secureAreaIdentifier)
                        ?: throw IllegalArgumentException("SecureArea '${createSettings.secureAreaIdentifier}' not registered")
                documentId = "${prefix}${UUID.randomUUID()}"
                val domain = identifier
                val identityDocument = documentStore.createDocument { metadata ->
                    (metadata as ApplicationMetaData).initialize(
                        documentIdentifier = documentId.toString(),
                        documentName = documentId.toString(),
                        documentManagerId = domain,
                        createdAt = Clock.System.now(),
                        issuerMetaData = issuerMetaData,
                    )
                }
                when (format) {
                    is MsoMdocFormat -> {
                        val mdocCredential = format.createCredential(
                            domain, identityDocument, secureArea, createSettings.createKeySettings
                        )
                        identityDocument.documentIdentifier = mdocCredential.document.identifier
                        identityDocument.documentName = format.docType
                    }

                    is SdJwtVcFormat -> {
                        val sdJwtVcCredential = format.createCredential(
                            domain, identityDocument, secureArea, createSettings.createKeySettings
                        )
                        identityDocument.documentIdentifier = sdJwtVcCredential.document.identifier
                        identityDocument.documentName = format.vct
                    }
                }
                Outcome.success(identityDocument.toDocument())
            } catch (e: Throwable) {
                documentId?.let { documentStore.deleteDocument(it) }
                Outcome.failure(e)
            }
        }
    }

    /**
     * Store an issued document. This method will store the document with its issuer provided data.
     *
     * @param unsignedDocument the unsigned document
     * @param issuerProvidedData the issuer provided data
     * @return the result of the storage. If successful, it will return the [IssuedDocument]. If not, it will return an error.
     */
    override fun storeIssuedDocument(
        unsignedDocument: UnsignedDocument,
        issuerProvidedData: ByteArray
    ): Outcome<IssuedDocument> {
        return runBlocking {
            try {
                val identityDocument = documentStore.lookupDocument(unsignedDocument.id)
                    ?: throw IllegalArgumentException("Document with ${unsignedDocument.id} not found")

                when (val format = unsignedDocument.format) {
                    is MsoMdocFormat -> format.storeIssuedDocument(
                        unsignedDocument,
                        identityDocument,
                        issuerProvidedData,
                        checkDevicePublicKey
                    )

                    is SdJwtVcFormat -> format.storeIssuedDocument(
                        unsignedDocument,
                        identityDocument,
                        issuerProvidedData,
                        checkDevicePublicKey,
                        ktorHttpClientFactory
                    )

                    else -> throw IllegalArgumentException("Format ${format::class.simpleName} not supported")
                }
                with(identityDocument) {
                    documentIdentifier = unsignedDocument.id
                    documentName = unsignedDocument.name
                    issuedAt = Clock.System.now()
                    clearDeferredRelatedData()
                }
                Outcome.success(identityDocument.toDocument())
            } catch (e: Throwable) {
                Outcome.failure(e)
            }
        }
    }

    /**
     * Store an unsigned document for deferred issuance. This method will store the document with the related
     * to the issuance data.
     *
     * @param unsignedDocument the unsigned document
     * @param relatedData the related data
     * @return the result of the storage. If successful, it will return the [DeferredDocument]. If not, it will return an error.
     */
    override fun storeDeferredDocument(
        unsignedDocument: UnsignedDocument,
        relatedData: ByteArray
    ): Outcome<DeferredDocument> {
        return runBlocking {
            try {
                val identityDocument = documentStore.lookupDocument(unsignedDocument.id)
                    ?: throw IllegalArgumentException("Document with ${unsignedDocument.id} not found")

                with(identityDocument) {
                    documentName = unsignedDocument.name
                    deferredRelatedData = relatedData
                }
                Outcome.success(identityDocument.toDocument())
            } catch (e: Exception) {
                Outcome.failure(e)
            }
        }
    }

    companion object {
        private const val TAG = "DocumentManagerImpl"
    }
}