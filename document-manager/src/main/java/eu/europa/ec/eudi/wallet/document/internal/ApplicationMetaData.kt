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

import eu.europa.ec.eudi.wallet.document.metadata.IssuerMetaData
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

internal class ApplicationMetaData private constructor(
    serializedData: ByteString?,
    private val saveFn: suspend (data: ByteString) -> Unit
) : DocumentMetadata {
    private val lock = Mutex()
    private val data: Data = if (serializedData == null || serializedData.isEmpty()) {
        Data()
    } else {
        Data.fromCbor(serializedData.toByteArray())
    }

    override val provisioned get() = data.provisioned
    suspend fun setAsProvisioned() = lock.withLock {
        check(!data.provisioned)
        data.provisioned = true
    }

    override val displayName get() = data.displayName
    override val typeDisplayName get() = data.typeDisplayName
    override val cardArt get() = data.cardArt
    override val issuerLogo get() = data.issuerLogo

    val documentIdentifier get() = data.documentIdentifier!!
    suspend fun setDocumentIdentifier(documentIdentifier: String) =
        lock.withLock {
            data.documentIdentifier = documentIdentifier
            save()
        }

    val documentName get() = data.documentName!!
    suspend fun setDocumentName(documentName: String) =
        lock.withLock {
            data.documentName = documentName
            save()
        }

    val documentManagerId get() = data.documentManagerId!!
    suspend fun setDocumentManagerId(documentManagerId: String) =
        lock.withLock {
            data.documentManagerId = documentManagerId
            save()
        }

    val issuerMetaData get() = data.issuerMetaData
    suspend fun setIssuerMetaData(issuerMetaData: IssuerMetaData) =
        lock.withLock {
            data.issuerMetaData = issuerMetaData
            save()
        }

    val createdAt get() = data.createdAt!!
    suspend fun setCreatedAt(createdAt: Instant) =
        lock.withLock {
            data.createdAt = createdAt
            save()
        }

    val issuedAt get() = data.issuedAt
    suspend fun setIssuedAt(issuedAt: Instant) =
        lock.withLock {
            data.issuedAt = issuedAt
            save()
        }

    val nameSpacedData get() = data.nameSpacedData
    suspend fun setNameSpacedData(nameSpacedData: NameSpacedData) =
        lock.withLock {
            data.nameSpacedData = nameSpacedData
            save()
        }

    val deferredRelatedData get() = data.deferredRelatedData
    suspend fun setDeferredRelatedData(deferredRelatedData: ByteArray) =
        lock.withLock {
            data.deferredRelatedData = deferredRelatedData
            save()
        }

    suspend fun clearDeferredRelatedData() =
        lock.withLock {
            data.deferredRelatedData = null
            save()
        }

    suspend fun initialize(
        documentIdentifier: String,
        documentName: String,
        documentManagerId: String,
        createdAt: Instant,
        issuerMetaData: IssuerMetaData? = null
    ) {
        lock.withLock {
            data.documentIdentifier = documentIdentifier
            data.documentName = documentName
            data.documentManagerId = documentManagerId
            data.createdAt = createdAt
            data.issuerMetaData = issuerMetaData
            save()
        }
    }

    private suspend fun save() {
        check(lock.isLocked)
        saveFn(ByteString(data.toCbor()))
    }

    override suspend fun documentDeleted() {}

    @CborSerializable
    data class Data(
        @Volatile var provisioned: Boolean = false,
        @Volatile var displayName: String? = null,
        @Volatile var typeDisplayName: String? = null,
        @Volatile var cardArt: ByteString? = null,
        @Volatile var issuerLogo: ByteString? = null,
        @Volatile var documentIdentifier: String? = null,
        @Volatile var documentName: String? = null,
        @Volatile var documentManagerId: String? = null,
        @Volatile var issuerMetaData: IssuerMetaData? = null,
        @Volatile var createdAt: Instant? = null,
        @Volatile var issuedAt: Instant? = null,
        @Volatile var nameSpacedData: NameSpacedData? = null,
        @Volatile var deferredRelatedData: ByteArray? = null
    ) {
        companion object
    }

    private fun Data.Companion.fromCbor(data: ByteArray): Data {
        return Data.fromDataItem(Cbor.decode(data))
    }

    private fun Data.toCbor(): ByteArray {
        return Cbor.encode(toDataItem())
    }

    private fun Data.toDataItem(): DataItem {
        val builder = CborMap.builder()
        fun <T> putIfNotNull(key: String, value: T?, transform: (T) -> DataItem) {
            if (value != null) {
                builder.put(key, transform(value))
            }
        }
        builder.put("provisioned", this.provisioned.toDataItem())
        putIfNotNull("displayName", this.displayName) { Tstr(it) }
        putIfNotNull("typeDisplayName", this.typeDisplayName) { Tstr(it) }
        putIfNotNull("cardArt", this.cardArt) { Bstr(it.toByteArray()) }
        putIfNotNull("issuerLogo", this.issuerLogo) { Bstr(it.toByteArray()) }
        putIfNotNull("documentIdentifier", this.documentIdentifier) { Tstr(it) }
        putIfNotNull("documentName", this.documentName) { Tstr(it) }
        putIfNotNull("documentManagerId", this.documentManagerId) { Tstr(it) }
        putIfNotNull("issuerMetaData", this.issuerMetaData) { Tstr(it.toJson()) }
        putIfNotNull("createdAt", this.createdAt) { it.toDataItemDateTimeString() }
        putIfNotNull("issuedAt", this.issuedAt) { it.toDataItemDateTimeString() }
        putIfNotNull("nameSpacedData", this.nameSpacedData) { it.toDataItem() }
        putIfNotNull("deferredRelatedData", this.deferredRelatedData) { Bstr(it) }
        return builder.end().build()
    }

    private fun Data.Companion.fromDataItem(dataItem: DataItem): Data {
        fun <T> getValue(key: String, extractor: (DataItem) -> T?): T? =
            if (dataItem.hasKey(key)) extractor(dataItem[key]) else null

        return Data(
            provisioned = dataItem["provisioned"].asBoolean,
            displayName = getValue("displayName") { it.asTstr },
            typeDisplayName = getValue("typeDisplayName") { it.asTstr },
            cardArt = getValue("cardArt") { ByteString(it.asBstr) },
            issuerLogo = getValue("issuerLogo") { ByteString(it.asBstr) },
            documentIdentifier = getValue("documentIdentifier") { it.asTstr },
            documentName = getValue("documentName") { it.asTstr },
            issuerMetaData = getValue("issuerMetaData") { item ->
                IssuerMetaData.fromJson(item.asTstr).getOrNull()
            },
            documentManagerId = getValue("documentManagerId") { it.asTstr },
            createdAt = getValue("createdAt") { it.asDateTimeString },
            issuedAt = getValue("issuedAt") { it.asDateTimeString },
            nameSpacedData = getValue("nameSpacedData") { NameSpacedData.fromDataItem(it) },
            deferredRelatedData = getValue("deferredRelatedData") { it.asBstr }
        )
    }

    companion object {
        suspend fun create(
            documentId: String,
            serializedData: ByteString?,
            saveFn: suspend (data: ByteString) -> Unit
        ): ApplicationMetaData {
            return ApplicationMetaData(serializedData, saveFn)
        }
    }
}