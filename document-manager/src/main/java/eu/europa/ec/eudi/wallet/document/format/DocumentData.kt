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

package eu.europa.ec.eudi.wallet.document.format

import eu.europa.ec.eudi.sdjwt.DefaultSdJwtOps
import eu.europa.ec.eudi.sdjwt.DefaultSdJwtOps.recreateClaimsAndDisclosuresPerClaim
import eu.europa.ec.eudi.sdjwt.vc.SelectPath.Default.select
import eu.europa.ec.eudi.wallet.document.NameSpace
import eu.europa.ec.eudi.wallet.document.NameSpacedValues
import eu.europa.ec.eudi.wallet.document.NameSpaces
import eu.europa.ec.eudi.wallet.document.internal.parse
import eu.europa.ec.eudi.wallet.document.internal.toObject
import eu.europa.ec.eudi.wallet.document.metadata.IssuerMetadata
import org.multipaz.cbor.Cbor
import org.multipaz.document.NameSpacedData

/**
 * Represents the claims of a document.
 * @property format The format of the document.
 * @property claims The list of claims.
 * @property issuerMetadata The metadata of the document provided by the issuer.
 */
sealed interface DocumentData {
    val format: DocumentFormat
    val claims: List<DocumentClaim>
    val issuerMetadata: IssuerMetadata?
}

/**
 * Represents a claim of a document.
 * @property identifier The identifier of the claim.
 * @property value The value of the claim.
 * @property rawValue The raw value of the claim.
 * @property issuerMetadata The metadata of the claim provided by the issuer.
 */
sealed class DocumentClaim(
    open val identifier: String,
    open val value: Any?,
    open val rawValue: Any?,
    open val issuerMetadata: IssuerMetadata.Claim? = null
)

/**
 * Represents the claims of a document in the MsoMdoc format.
 * @property format The MsoMdoc format containing the docType
 * @property nameSpacedData The name-spaced data.
 * @property claims The list of claims.
 * @property issuerMetadata The metadata of the document provided by the issuer.
 * @property nameSpacedDataInBytes The name-spaced data in bytes.
 * @property nameSpacedDataDecoded The name-spaced data decoded.
 * @property nameSpaces The name-spaces.
 *
 */
data class MsoMdocData(
    override val format: MsoMdocFormat,
    override val issuerMetadata: IssuerMetadata?,
    val nameSpacedData: NameSpacedData
) : DocumentData {

    override val claims: List<MsoMdocClaim>
        get() = nameSpacedData.nameSpaceNames.flatMap { nameSpace ->
            nameSpacedData.getDataElementNames(nameSpace).map { identifier ->
                val metadataClaimName = listOf(nameSpace, identifier)
                MsoMdocClaim(
                    nameSpace = nameSpace,
                    identifier = identifier,
                    value = nameSpacedData.getDataElement(nameSpace, identifier).toObject(),
                    rawValue = nameSpacedData.getDataElement(nameSpace, identifier),
                    issuerMetadata = issuerMetadata?.claims?.find { it.path == metadataClaimName }
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
 * @property issuerMetadata The metadata of the claim provided by the issuer.
 */
data class MsoMdocClaim(
    val nameSpace: NameSpace,
    override val identifier: String,
    override val value: Any?,
    override val rawValue: ByteArray,
    override val issuerMetadata: IssuerMetadata.Claim?,
) : DocumentClaim(identifier, value, rawValue, issuerMetadata) {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as MsoMdocClaim

        if (nameSpace != other.nameSpace) return false
        if (identifier != other.identifier) return false
        if (value != other.value) return false
        if (!rawValue.contentEquals(other.rawValue)) return false
        if (issuerMetadata != other.issuerMetadata) return false

        return true
    }

    override fun hashCode(): Int {
        var result = nameSpace.hashCode()
        result = 31 * result + identifier.hashCode()
        result = 31 * result + (value?.hashCode() ?: 0)
        result = 31 * result + rawValue.contentHashCode()
        result = 31 * result + (issuerMetadata?.hashCode() ?: 0)
        return result
    }
}

/**
 * Represents the claims of a document in the SdJwtVc format.
 * @property format The SdJwtVc format containing the vct
 * @property sdJwtVc The SdJwtVc.
 * @property claims The list of claims.
 * @property issuerMetadata The metadata of the document provided by the issuer.
 *
 */
data class SdJwtVcData(
    override val format: SdJwtVcFormat,
    override val issuerMetadata: IssuerMetadata?,
    val sdJwtVc: String
) : DocumentData {
    override val claims: List<SdJwtVcClaim> by lazy {
        val (claims, disclosuresPerClaim) = DefaultSdJwtOps.unverifiedIssuanceFrom(sdJwtVc)
            .getOrThrow().recreateClaimsAndDisclosuresPerClaim()

        // Filter out paths that are excluded from claims
        val filteredDisclosuresPerClaim = disclosuresPerClaim
            .filterNot { (path, _) -> path.head().toString() in ExcludedIdentifiers }

        // create the list of claims that will be returned
        // and populate it with the claims and their children
        mutableListOf<MutableSdJwtClaim>().also { sdJwtVcClaims ->

            for ((path, disclosures) in filteredDisclosuresPerClaim) {
                val value = claims.select(path).getOrNull()
                val selectivelyDisclosable = disclosures.isNotEmpty()

                // start from the root of the list of claims
                var current = sdJwtVcClaims

                for (key in path.value) {
                    // check if the current path element is already present in the current list of claims
                    val existingNode = current.find { it.identifier == key.toString() }

                    // if the path element is already present, move to the children of the existing node
                    if (existingNode != null) {
                        current = existingNode.children
                    } else {
                        // if the path element is not present, create a new claim and add it to the current list of claims
                        val newClaim = MutableSdJwtClaim(
                            identifier = key.toString(),
                            value = value?.parse(),
                            rawValue = value?.toString() ?: "",
                            selectivelyDisclosable = selectivelyDisclosable,
                            metadata = issuerMetadata?.claims?.find { m -> m.path == path.value.map { it.toString() } }
                        )
                        // add the new claim to the current list of claims
                        current.add(newClaim)
                        // set the current list of claims to the children of the new claim
                        current = newClaim.children
                    }
                }
            }
        }.map { it.toSdJwtVcClaim() }
    }

    companion object {
        internal val ExcludedIdentifiers = arrayOf(
            "cnf",
            "iss",
            "vct",
            "aud",
            "status",
            "assurance_level",
        )
    }
}

/**
 * Represents a claim of a document in the SdJwtVc format.
 * @property identifier The identifier of the claim.
 * @property value The value of the claim.
 * @property rawValue The raw value of the claim.
 * @property selectivelyDisclosable Whether the claim is selectively disclosable.
 * @property children The children of the claim.
 * @property issuerMetadata The metadata of the claim provided by the issuer.
 */
data class SdJwtVcClaim(
    override val identifier: String,
    override val value: Any?,
    override val rawValue: String,
    override val issuerMetadata: IssuerMetadata.Claim?,
    val selectivelyDisclosable: Boolean,
    val children: List<SdJwtVcClaim>
) : DocumentClaim(identifier, value, rawValue, issuerMetadata)

/**
 * Internal class for SdJwtVcClaim that can be mutated.
 * Mutation is needed to build the list of claims.
 */
internal class MutableSdJwtClaim(
    val identifier: String,
    val value: Any?,
    val rawValue: String,
    val metadata: IssuerMetadata.Claim?,
    val selectivelyDisclosable: Boolean,
    val children: MutableList<MutableSdJwtClaim> = mutableListOf()
) {
    fun toSdJwtVcClaim(): SdJwtVcClaim {
        return SdJwtVcClaim(
            identifier = identifier,
            value = value,
            rawValue = rawValue,
            issuerMetadata = metadata,
            selectivelyDisclosable = selectivelyDisclosable,
            children = children.map { it.toSdJwtVcClaim() }
        )
    }
}