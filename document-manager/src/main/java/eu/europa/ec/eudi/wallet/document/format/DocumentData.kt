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

package eu.europa.ec.eudi.wallet.document.format

import com.android.identity.cbor.Cbor
import com.android.identity.document.NameSpacedData
import eu.europa.ec.eudi.sdjwt.DefaultSdJwtOps
import eu.europa.ec.eudi.sdjwt.DefaultSdJwtOps.recreateClaimsAndDisclosuresPerClaim
import eu.europa.ec.eudi.sdjwt.vc.SelectPath.Default.select
import eu.europa.ec.eudi.wallet.document.NameSpace
import eu.europa.ec.eudi.wallet.document.NameSpacedValues
import eu.europa.ec.eudi.wallet.document.NameSpaces
import eu.europa.ec.eudi.wallet.document.internal.parse
import eu.europa.ec.eudi.wallet.document.internal.toObject
import eu.europa.ec.eudi.wallet.document.metadata.DocumentMetaData

/**
 * Represents the claims of a document.
 * @property format The format of the document.
 * @property claims The list of claims.
 * @property metadata The metadata of the document.
 */
sealed interface DocumentData {
    val format: DocumentFormat
    val claims: List<DocumentClaim>
    val metadata: DocumentMetaData?
}

/**
 * Represents a claim of a document.
 * @property identifier The identifier of the claim.
 * @property value The value of the claim.
 * @property rawValue The raw value of the claim.
 * @property metadata The metadata of the claim.
 */
sealed class DocumentClaim(
    open val identifier: String,
    open val value: Any?,
    open val rawValue: Any?,
    open val metadata: DocumentMetaData.Claim? = null
)

/**
 * Represents the claims of a document in the MsoMdoc format.
 * @property format The MsoMdoc format containing the docType
 * @property nameSpacedData The name-spaced data.
 * @property claims The list of claims.
 * @property metadata The metadata of the document.
 * @property nameSpacedDataInBytes The name-spaced data in bytes.
 * @property nameSpacedDataDecoded The name-spaced data decoded.
 * @property nameSpaces The name-spaces.
 *
 */
data class MsoMdocData(
    override val format: MsoMdocFormat,
    override val metadata: DocumentMetaData?,
    val nameSpacedData: NameSpacedData
) : DocumentData {

    override val claims: List<MsoMdocClaim>
        get() = nameSpacedData.nameSpaceNames.flatMap { nameSpace ->
            nameSpacedData.getDataElementNames(nameSpace).map { identifier ->
                val metadataClaimName = DocumentMetaData.Claim.Name.MsoMdoc(
                    nameSpace = nameSpace,
                    name = identifier
                )
                MsoMdocClaim(
                    nameSpace = nameSpace,
                    identifier = identifier,
                    value = nameSpacedData.getDataElement(nameSpace, identifier).toObject(),
                    rawValue = nameSpacedData.getDataElement(nameSpace, identifier),
                    metadata = metadata?.claims?.find { it.name == metadataClaimName }
                )
            }
        }

    val nameSpacedDataInBytes: NameSpacedValues<ByteArray>
        get() = nameSpacedData.nameSpaceNames.associateWith { nameSpace ->
            nameSpacedData.getDataElementNames(nameSpace)
                .associateWith { elementIdentifier ->
                    nameSpacedData.getDataElement(nameSpace, elementIdentifier)
                }
        }

    val nameSpacedDataDecoded: NameSpacedValues<Any?>
        get() = claims.groupBy { it.nameSpace }
            .mapValues { it.value.associate { claim -> claim.identifier to claim.value } }

    val nameSpaces: NameSpaces
        get() = nameSpacedDataInBytes.mapValues { it.value.keys.toList() }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is MsoMdocData) return false

        if (!Cbor.encode(nameSpacedData.toDataItem())
                .contentEquals(Cbor.encode(other.nameSpacedData.toDataItem()))
        ) return false

        return true
    }

    override fun hashCode(): Int {
        var result = Cbor.encode(nameSpacedData.toDataItem()).contentHashCode()
        return result
    }
}

/**
 * Represents a claim of a document in the MsoMdoc format.
 * @property nameSpace The name-space of the claim.
 * @property identifier The identifier of the claim.
 * @property value The value of the claim.
 * @property rawValue The raw value of the claim in bytes.
 * @property metadata The metadata of the claim.
 */
data class MsoMdocClaim(
    val nameSpace: NameSpace,
    override val identifier: String,
    override val value: Any?,
    override val rawValue: ByteArray,
    override val metadata: DocumentMetaData.Claim?,
) : DocumentClaim(identifier, value, rawValue, metadata)

/**
 * Represents the claims of a document in the SdJwtVc format.
 * @property format The SdJwtVc format containing the vct
 * @property sdJwtVc The SdJwtVc.
 * @property claims The list of claims.
 * @property metadata The metadata of the document.
 *
 */
data class SdJwtVcData(
    override val format: SdJwtVcFormat,
    override val metadata: DocumentMetaData?,
    val sdJwtVc: String
) : DocumentData {
    override val claims: List<SdJwtVcClaim> by lazy {
        val (claims, disclosuresPerClaim) = DefaultSdJwtOps.unverifiedIssuanceFrom(sdJwtVc)
            .getOrThrow().recreateClaimsAndDisclosuresPerClaim()

        val sdJwtVcClaims = mutableListOf<SdJwtVcClaim>()
        for ((path, value, selectivelyDisclosable) in disclosuresPerClaim.map {
            Triple(it.key, claims.select(it.key).getOrNull(), it.value.isNotEmpty())
        }) {
            var current = sdJwtVcClaims
            for (key in path.value) {
                val existingNode = current.find { it.identifier == key.toString() }
                if (existingNode != null) {
                    current = existingNode.children
                } else {
                    val metadataClaimName = DocumentMetaData.Claim.Name.SdJwtVc(
                        name = key.toString()
                    )
                    val newClaim = SdJwtVcClaim(
                        identifier = key.toString(),
                        value = value?.parse(),
                        rawValue = value?.toString() ?: "",
                        selectivelyDisclosable = selectivelyDisclosable,
                        metadata = metadata?.claims?.find { it.name == metadataClaimName }
                    )
                    current.add(newClaim)
                    current = newClaim.children
                }
            }
        }
        return@lazy sdJwtVcClaims
    }
}

/**
 * Represents a claim of a document in the SdJwtVc format.
 * @property identifier The identifier of the claim.
 * @property value The value of the claim.
 * @property rawValue The raw value of the claim.
 * @property selectivelyDisclosable Whether the claim is selectively disclosable.
 * @property children The children of the claim.
 * @property metadata The metadata of the claim.
 */
data class SdJwtVcClaim(
    override val identifier: String,
    override val value: Any?,
    override val rawValue: String,
    override val metadata: DocumentMetaData.Claim?,
    val selectivelyDisclosable: Boolean,
    val children: MutableList<SdJwtVcClaim> = mutableListOf()
) : DocumentClaim(identifier, value, rawValue, metadata)