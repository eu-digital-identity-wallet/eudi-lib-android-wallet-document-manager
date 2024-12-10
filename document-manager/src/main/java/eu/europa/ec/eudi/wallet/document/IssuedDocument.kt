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

import com.android.identity.cbor.Cbor
import com.android.identity.document.NameSpacedData
import com.android.identity.securearea.SecureArea
import eu.europa.ec.eudi.wallet.document.format.MsoMdocFormat
import eu.europa.ec.eudi.wallet.document.format.SdJwtVcFormat
import eu.europa.ec.eudi.wallet.document.internal.toObject
import kotlinx.serialization.json.JsonElement
import java.time.Instant

sealed interface IssuedDocument : Document {
    val validFrom: Instant
    val validUntil: Instant
    val issuedAt: Instant
    val issuerProvidedData: ByteArray
}

data class SdJwtVcIssuedDocument(
    override val id: DocumentId,
    override val name: String,
    override val format: SdJwtVcFormat,
    override val documentManagerId: String,
    override val isCertified: Boolean,
    override val keyAlias: String,
    override val secureArea: SecureArea,
    override val createdAt: Instant,
    override val validFrom: Instant,
    override val validUntil: Instant,
    override val issuedAt: Instant,
    override val issuerProvidedData: ByteArray,
    val claims: Claims
) : IssuedDocument {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as SdJwtVcIssuedDocument

        if (id != other.id) return false
        if (name != other.name) return false
        if (format != other.format) return false
        if (documentManagerId != other.documentManagerId) return false
        if (isCertified != other.isCertified) return false
        if (keyAlias != other.keyAlias) return false
        if (secureArea != other.secureArea) return false
        if (createdAt != other.createdAt) return false
        if (validFrom != other.validFrom) return false
        if (validUntil != other.validUntil) return false
        if (issuedAt != other.issuedAt) return false
        if (!issuerProvidedData.contentEquals(other.issuerProvidedData)) return false
        return true
    }

    override fun hashCode(): Int {
        var result = id.hashCode()
        result = 31 * result + name.hashCode()
        result = 31 * result + format.hashCode()
        result = 31 * result + documentManagerId.hashCode()
        result = 31 * result + isCertified.hashCode()
        result = 31 * result + keyAlias.hashCode()
        result = 31 * result + secureArea.hashCode()
        result = 31 * result + createdAt.hashCode()
        result = 31 * result + validFrom.hashCode()
        result = 31 * result + validUntil.hashCode()
        result = 31 * result + issuedAt.hashCode()
        result = 31 * result + issuerProvidedData.contentHashCode()
        return result
    }
}

class Claim(val name: String, val value: JsonElement, val selectivelyDisclosable: Boolean = false)
typealias Claims = List<Claim>

/**
 * Represents an Issued Document
 * @property id the document id
 * @property name the document name
 * @property format the document format
 * @property documentManagerId the [DocumentManager.identifier] related to this document
 * @property isCertified whether the document is certified
 * @property keyAlias the key alias
 * @property secureArea the secure area
 * @property createdAt the creation date
 * @property issuedAt the issuance date
 * @property nameSpacedData the name spaced data
 * @property issuerProvidedData the issuer provided data
 * @property nameSpacedDataInBytes the name spaced data represented as a map
 * @property nameSpacedDataDecoded the name spaced data represented as a map with decoded values
 * @property nameSpaces the name spaces and their element identifiers
 */
data class MsoMdocIssuedDocument(
    override val id: DocumentId,
    override val name: String,
    override val format: MsoMdocFormat,
    override val documentManagerId: String,
    override val isCertified: Boolean,
    override val keyAlias: String,
    override val secureArea: SecureArea,
    override val createdAt: Instant,
    override val validFrom: Instant,
    override val validUntil: Instant,
    override val issuedAt: Instant,
    override val issuerProvidedData: ByteArray,
    val nameSpacedData: NameSpacedData
    ) : IssuedDocument {

    /**
     * Check if the document is valid at a given time, based on the validFrom and validUntil fields
     * @param time the time to check
     * @return true if the document is valid at the given time, false otherwise
     */
    fun isValidAt(time: Instant): Boolean {
        return time in validFrom..validUntil
    }

    val nameSpacedDataInBytes: NameSpacedValues<ByteArray>
        get() = nameSpacedData.nameSpaceNames.associateWith { nameSpace ->
            nameSpacedData.getDataElementNames(nameSpace)
                .associateWith { elementIdentifier ->
                    nameSpacedData.getDataElement(nameSpace, elementIdentifier)
                }
        }

    val nameSpacedDataDecoded: NameSpacedValues<Any?>
        get() = nameSpacedDataInBytes.mapValues {
            it.value.mapValues {
                it.value.toObject()
            }
        }

    val nameSpaces: NameSpaces
        get() = nameSpacedDataInBytes.mapValues { it.value.keys.toList() }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is MsoMdocIssuedDocument) return false

        if (id != other.id) return false
        if (name != other.name) return false
        if (format != other.format) return false
        if (documentManagerId != other.documentManagerId) return false
        if (isCertified != other.isCertified) return false
        if (keyAlias != other.keyAlias) return false
        if (secureArea != other.secureArea) return false
        if (createdAt != other.createdAt) return false
        if (validFrom != other.validFrom) return false
        if (validUntil != other.validUntil) return false
        if (issuedAt != other.issuedAt) return false
        if (!Cbor.encode(nameSpacedData.toDataItem())
                .contentEquals(Cbor.encode(other.nameSpacedData.toDataItem()))
        ) return false
        if (!issuerProvidedData.contentEquals(other.issuerProvidedData)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id.hashCode()
        result = 31 * result + name.hashCode()
        result = 31 * result + format.hashCode()
        result = 31 * result + documentManagerId.hashCode()
        result = 31 * result + isCertified.hashCode()
        result = 31 * result + keyAlias.hashCode()
        result = 31 * result + secureArea.hashCode()
        result = 31 * result + createdAt.hashCode()
        result = 31 * result + validFrom.hashCode()
        result = 31 * result + validUntil.hashCode()
        result = 31 * result + issuedAt.hashCode()
        result = 31 * result + Cbor.encode(nameSpacedData.toDataItem()).contentHashCode()
        result = 31 * result + issuerProvidedData.contentHashCode()
        return result
    }
}