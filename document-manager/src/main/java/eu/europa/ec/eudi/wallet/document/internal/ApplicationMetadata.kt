/*
 * Copyright (c) 2024-2025 European Commission
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

package eu.europa.ec.eudi.wallet.document.internal

import eu.europa.ec.eudi.wallet.document.CreateDocumentSettings
import eu.europa.ec.eudi.wallet.document.format.DocumentFormat
import eu.europa.ec.eudi.wallet.document.metadata.IssuerMetadata
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.datetime.Instant
import kotlinx.io.bytestring.ByteString
import kotlinx.io.bytestring.isEmpty
import org.multipaz.cbor.Bstr
import org.multipaz.cbor.Cbor
import org.multipaz.cbor.CborMap
import org.multipaz.cbor.DataItem
import org.multipaz.cbor.Tstr
import org.multipaz.cbor.annotation.CborSerializable
import org.multipaz.cbor.toDataItem
import org.multipaz.cbor.toDataItemDateTimeString
import org.multipaz.document.DocumentMetadata
import org.multipaz.document.NameSpacedData

/**
 * Internal implementation of DocumentMetadata for managing application-specific metadata
 * related to wallet documents.
 *
 * This class provides storage and access to various document properties, including:
 * - Document format information
 * - Document provisioning status
 * - Display information (names, artwork)
 * - Timing information (creation, issuance)
 * - Credential policies
 * - Issuer metadata
 * - Document namespaced data
 * - Related deferred data
 *
 * The class ensures thread safety using a mutex lock for all data modifications.
 * Data is serialized to/from CBOR format for persistent storage.
 *
 * @property serializedData Existing serialized CBOR data to initialize from, if available
 * @property saveFn Callback function to persist serialized data when changes occur
 */
internal class ApplicationMetadata private constructor(
    serializedData: ByteString?,
    private val saveFn: suspend (data: ByteString) -> Unit
) : DocumentMetadata {
    private val lock = Mutex()
    private val data: Data = if (serializedData == null || serializedData.isEmpty()) {
        Data()
    } else {
        Data.fromCbor(serializedData.toByteArray())
    }

    /** Indicates whether the document has been provisioned */
    override val provisioned get() = data.provisioned

    /**
     * Sets the document as provisioned.
     *
     * @throws IllegalStateException If the document is already provisioned
     */
    suspend fun setAsProvisioned() = lock.withLock {
        check(!data.provisioned)
        data.provisioned = true
        save()
    }

    /** The document's display name to show to users */
    override val displayName get() = data.displayName

    /** The document type's display name to show to users */
    override val typeDisplayName get() = data.typeDisplayName

    /** Card art image data to display for this document */
    override val cardArt get() = data.cardArt

    /** Issuer logo image data to display for this document */
    override val issuerLogo get() = data.issuerLogo

    /**
     * The document format (e.g., MsoMdoc or SdJwtVc).
     *
     * @throws IllegalStateException If the format has not been set
     */
    val format: DocumentFormat
        get() = data.format ?: throw IllegalStateException("Document format not set")

    /**
     * The number of credentials that created when this document was created.
     */
    val initialCredentialsCount: Int
        get() = data.initialCredentialsCount
            ?: throw IllegalStateException("Initial credentials count not set")

    /**
     * The credential usage policy for this document.
     * Defaults to RotateUse if not explicitly set.
     */
    val credentialPolicy: CreateDocumentSettings.CredentialPolicy
        get() = data.credentialPolicy ?: CreateDocumentSettings.CredentialPolicy.RotateUse

    /**
     * Sets the credential policy for this document.
     *
     * @param credentialPolicy The policy to set
     */
    suspend fun setCredentialPolicy(credentialPolicy: CreateDocumentSettings.CredentialPolicy) =
        lock.withLock {
            data.credentialPolicy = credentialPolicy
            save()
        }

    /**
     * The document's name identifier.
     *
     * @throws NullPointerException If the document name has not been set
     */
    val documentName get() = data.documentName!!

    /**
     * Sets the document name.
     *
     * @param documentName The name to set for this document
     */
    suspend fun setDocumentName(documentName: String) =
        lock.withLock {
            data.documentName = documentName
            save()
        }

    /**
     * The document manager's unique identifier.
     *
     * @throws NullPointerException If the document manager ID has not been set
     */
    val documentManagerId get() = data.documentManagerId!!

    /**
     * Sets the document manager's identifier.
     *
     * @param documentManagerId The ID to set
     */
    suspend fun setDocumentManagerId(documentManagerId: String) =
        lock.withLock {
            data.documentManagerId = documentManagerId
            save()
        }

    /** Metadata about the document issuer */
    val issuerMetadata get() = data.issuerMetadata

    /**
     * Sets the issuer metadata.
     *
     * @param issuerMetaData The issuer metadata to set
     */
    suspend fun setIssuerMetadata(issuerMetaData: IssuerMetadata) =
        lock.withLock {
            data.issuerMetadata = issuerMetaData
            save()
        }

    val issuerProvidedData: ByteArray?
        get() = data.issuerProvidedData

    suspend fun setIssuerProvidedData(issuerProvidedData: ByteArray) =
        lock.withLock {
            data.issuerProvidedData = issuerProvidedData
            save()
        }

    /**
     * The timestamp when this document was created.
     *
     * @throws NullPointerException If the creation time has not been set
     */
    val createdAt get() = data.createdAt!!

    /**
     * Sets the document creation timestamp.
     *
     * @param createdAt The creation timestamp
     */
    suspend fun setCreatedAt(createdAt: Instant) =
        lock.withLock {
            data.createdAt = createdAt
            save()
        }

    /** The timestamp when this document was issued (may be null for documents not yet issued) */
    val issuedAt get() = data.issuedAt

    /**
     * Sets the document issuance timestamp.
     *
     * @param issuedAt The issuance timestamp
     */
    suspend fun setIssuedAt(issuedAt: Instant) =
        lock.withLock {
            data.issuedAt = issuedAt
            save()
        }

    /** The namespaced data associated with this document */
    val nameSpacedData get() = data.nameSpacedData

    /**
     * Sets the namespaced data for this document.
     *
     * @param nameSpacedData The namespaced data to set
     */
    suspend fun setNameSpacedData(nameSpacedData: NameSpacedData) =
        lock.withLock {
            data.nameSpacedData = nameSpacedData
            save()
        }

    /** Deferred related data associated with this document */
    val deferredRelatedData get() = data.deferredRelatedData

    /**
     * Sets deferred related data for this document.
     *
     * @param deferredRelatedData The deferred data to set
     */
    suspend fun setDeferredRelatedData(deferredRelatedData: ByteArray) =
        lock.withLock {
            data.deferredRelatedData = deferredRelatedData
            save()
        }

    /**
     * Clears any deferred related data associated with this document.
     */
    suspend fun clearDeferredRelatedData() =
        lock.withLock {
            data.deferredRelatedData = null
            save()
        }

    /**
     * Initializes the core document metadata.
     *
     * @param format The document format
     * @param documentName The document name identifier
     * @param documentManagerId The document manager identifier
     * @param createdAt The creation timestamp
     * @param issuerMetadata Optional metadata about the issuer
     * @param credentialPolicy The credential usage policy
     */
    suspend fun initialize(
        format: DocumentFormat,
        documentName: String,
        documentManagerId: String,
        createdAt: Instant,
        issuerMetadata: IssuerMetadata? = null,
        initialCredentialsCount: Int = 1,
        credentialPolicy: CreateDocumentSettings.CredentialPolicy = CreateDocumentSettings.CredentialPolicy.RotateUse
    ) {
        lock.withLock {
            data.format = format
            data.documentName = documentName
            data.documentManagerId = documentManagerId
            data.createdAt = createdAt
            data.issuerMetadata = issuerMetadata
            data.initialCredentialsCount = initialCredentialsCount
            data.credentialPolicy = credentialPolicy
            save()
        }
    }

    /**
     * Saves the current metadata state using the provided save function.
     *
     * @throws IllegalStateException If called without holding the lock
     */
    private suspend fun save() {
        check(lock.isLocked)
        saveFn(ByteString(data.toCbor()))
    }

    /**
     * Called when the associated document is deleted.
     * Currently a no-op implementation of the DocumentMetadata interface.
     */
    override suspend fun documentDeleted() {}

    /**
     * Data class that holds all metadata properties.
     * Uses @CborSerializable to enable CBOR serialization.
     * All properties are marked @Volatile to ensure visibility across threads.
     */
    @CborSerializable
    data class Data(
        @Volatile var format: DocumentFormat? = null,
        @Volatile var provisioned: Boolean = false,
        @Volatile var displayName: String? = null,
        @Volatile var typeDisplayName: String? = null,
        @Volatile var cardArt: ByteString? = null,
        @Volatile var issuerLogo: ByteString? = null,
        @Volatile var documentName: String? = null,
        @Volatile var documentManagerId: String? = null,
        @Volatile var issuerMetadata: IssuerMetadata? = null,
        @Volatile var issuerProvidedData: ByteArray? = null,
        @Volatile var createdAt: Instant? = null,
        @Volatile var issuedAt: Instant? = null,
        @Volatile var nameSpacedData: NameSpacedData? = null,
        @Volatile var deferredRelatedData: ByteArray? = null,
        @Volatile var initialCredentialsCount: Int? = null,
        @Volatile var credentialPolicy: CreateDocumentSettings.CredentialPolicy? = null
    ) {
        companion object
    }

    /**
     * Deserializes Data from CBOR-encoded bytes.
     *
     * @param data CBOR-encoded bytes to deserialize
     * @return Deserialized Data instance
     */
    private fun Data.Companion.fromCbor(data: ByteArray): Data {
        return Data.fromDataItem(Cbor.decode(data))
    }

    /**
     * Serializes Data to CBOR-encoded bytes.
     *
     * @return CBOR-encoded representation of this Data instance
     */
    private fun Data.toCbor(): ByteArray {
        return Cbor.encode(toDataItem())
    }

    /**
     * Converts Data to a CBOR DataItem for serialization.
     * Uses a builder pattern to construct the CBOR map.
     *
     * @return CBOR DataItem representation of this Data
     */
    private fun Data.toDataItem(): DataItem {
        val builder = CborMap.builder()
        fun <T> putIfNotNull(key: String, value: T?, transform: (T) -> DataItem) {
            if (value != null) {
                builder.put(key, transform(value))
            }
        }
        builder.put("provisioned", this.provisioned.toDataItem())
        putIfNotNull("format", this.format) { it.toDataItem() }
        putIfNotNull("displayName", this.displayName) { Tstr(it) }
        putIfNotNull("typeDisplayName", this.typeDisplayName) { Tstr(it) }
        putIfNotNull("cardArt", this.cardArt) { Bstr(it.toByteArray()) }
        putIfNotNull("issuerLogo", this.issuerLogo) { Bstr(it.toByteArray()) }
        putIfNotNull("documentName", this.documentName) { Tstr(it) }
        putIfNotNull("documentManagerId", this.documentManagerId) { Tstr(it) }
        putIfNotNull("issuerMetadata", this.issuerMetadata) { Tstr(it.toJson()) }
        putIfNotNull("issuerProvidedData", this.issuerProvidedData) { Bstr(it) }
        putIfNotNull("createdAt", this.createdAt) { it.toDataItemDateTimeString() }
        putIfNotNull("issuedAt", this.issuedAt) { it.toDataItemDateTimeString() }
        putIfNotNull("nameSpacedData", this.nameSpacedData) { it.toDataItem() }
        putIfNotNull("deferredRelatedData", this.deferredRelatedData) { Bstr(it) }
        putIfNotNull("credentialPolicy", this.credentialPolicy) { it.toDataItem() }
        putIfNotNull("initialCredentialsCount", this.initialCredentialsCount) { it.toDataItem() }
        return builder.end().build()
    }

    /**
     * Deserializes a Data instance from a CBOR DataItem.
     * Extracts each field using appropriate conversions.
     *
     * @param dataItem CBOR DataItem to deserialize
     * @return Deserialized Data instance
     */
    private fun Data.Companion.fromDataItem(dataItem: DataItem): Data {
        fun <T> getValue(key: String, extractor: (DataItem) -> T?): T? =
            if (dataItem.hasKey(key)) extractor(dataItem[key]) else null

        return Data(
            format = getValue("format") {
                DocumentFormat.fromDataItem(it)
            },
            provisioned = dataItem["provisioned"].asBoolean,
            displayName = getValue("displayName") { it.asTstr },
            typeDisplayName = getValue("typeDisplayName") { it.asTstr },
            cardArt = getValue("cardArt") { ByteString(it.asBstr) },
            issuerLogo = getValue("issuerLogo") { ByteString(it.asBstr) },
            documentName = getValue("documentName") { it.asTstr },
            issuerMetadata = getValue("issuerMetadata") { item ->
                IssuerMetadata.fromJson(item.asTstr).getOrNull()
            },
            issuerProvidedData = getValue("issuerProvidedData") { it.asBstr },
            documentManagerId = getValue("documentManagerId") { it.asTstr },
            createdAt = getValue("createdAt") { it.asDateTimeString },
            issuedAt = getValue("issuedAt") { it.asDateTimeString },
            nameSpacedData = getValue("nameSpacedData") { NameSpacedData.fromDataItem(it) },
            deferredRelatedData = getValue("deferredRelatedData") { it.asBstr },
            initialCredentialsCount = getValue("initialCredentialsCount") { it.asNumber.toInt() },
            credentialPolicy = getValue("credentialPolicy") {
                CreateDocumentSettings.CredentialPolicy.fromDataItem(it)
            },
        )
    }

    companion object {
        /**
         * Factory method to create an ApplicationMetaData instance.
         *
         * @param documentId The document identifier
         * @param serializedData Existing serialized data, if available
         * @param saveFn Function to save serialized data
         * @return A new ApplicationMetaData instance
         */
        suspend fun create(
            documentId: String,
            serializedData: ByteString?,
            saveFn: suspend (data: ByteString) -> Unit
        ): ApplicationMetadata {
            return ApplicationMetadata(serializedData, saveFn)
        }
    }
}


