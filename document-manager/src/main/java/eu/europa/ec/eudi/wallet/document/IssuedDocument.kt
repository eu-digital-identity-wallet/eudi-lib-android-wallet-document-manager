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
import eu.europa.ec.eudi.wallet.document.format.DocumentData
import eu.europa.ec.eudi.wallet.document.format.DocumentFormat
import eu.europa.ec.eudi.wallet.document.metadata.DocumentMetaData
import java.time.Instant

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
 * @property metadata the document metadata
 * @property issuedAt the issuance date
 * @property issuerProvidedData the issuer provided data
 * @property data the document data (format specific)
 */
data class IssuedDocument(
    override val id: DocumentId,
    override val name: String,
    override val documentManagerId: String,
    override val isCertified: Boolean,
    override val keyAlias: String,
    override val secureArea: SecureArea,
    override val createdAt: Instant,
    override val metadata: DocumentMetaData?,
    val validFrom: Instant,
    val validUntil: Instant,
    val issuedAt: Instant,
    val issuerProvidedData: ByteArray,
    val data: DocumentData,
) : Document {

    override val format: DocumentFormat
        get() = data.format

    /**
     * Check if the document is valid at a given time, based on the validFrom and validUntil fields
     * @param time the time to check
     * @return true if the document is valid at the given time, false otherwise
     */
    fun isValidAt(time: Instant): Boolean {
        return time in validFrom..validUntil
    }


    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is IssuedDocument) return false

        if (id != other.id) return false
        if (name != other.name) return false
        if (documentManagerId != other.documentManagerId) return false
        if (isCertified != other.isCertified) return false
        if (keyAlias != other.keyAlias) return false
        if (secureArea != other.secureArea) return false
        if (createdAt != other.createdAt) return false
        if (metadata != other.metadata) return false
        if (validFrom != other.validFrom) return false
        if (validUntil != other.validUntil) return false
        if (issuedAt != other.issuedAt) return false
        if (data != other.data) return false
        if (!issuerProvidedData.contentEquals(other.issuerProvidedData)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id.hashCode()
        result = 31 * result + name.hashCode()
        result = 31 * result + documentManagerId.hashCode()
        result = 31 * result + isCertified.hashCode()
        result = 31 * result + keyAlias.hashCode()
        result = 31 * result + secureArea.hashCode()
        result = metadata?.let { 31 * result + it.hashCode() } ?: result
        result = 31 * result + createdAt.hashCode()
        result = 31 * result + validFrom.hashCode()
        result = 31 * result + validUntil.hashCode()
        result = 31 * result + issuedAt.hashCode()
        result = 31 * result + data.hashCode()
        result = 31 * result + issuerProvidedData.contentHashCode()
        return result
    }
}