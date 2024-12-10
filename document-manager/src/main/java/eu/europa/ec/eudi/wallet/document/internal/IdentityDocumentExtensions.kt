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

package eu.europa.ec.eudi.wallet.document.internal

import com.android.identity.credential.SecureAreaBoundCredential
import com.android.identity.document.NameSpacedData
import com.android.identity.mdoc.credential.MdocCredential
import eu.europa.ec.eudi.sdjwt.SdJwt
import eu.europa.ec.eudi.sdjwt.unverifiedIssuanceFrom
import eu.europa.ec.eudi.wallet.document.Claim
import eu.europa.ec.eudi.wallet.document.DeferredDocument
import eu.europa.ec.eudi.wallet.document.Document
import eu.europa.ec.eudi.wallet.document.IssuedDocument
import eu.europa.ec.eudi.wallet.document.MsoMdocIssuedDocument
import eu.europa.ec.eudi.wallet.document.SdJwtVcIssuedDocument
import eu.europa.ec.eudi.wallet.document.UnsignedDocument
import eu.europa.ec.eudi.wallet.document.format.DocumentFormat
import eu.europa.ec.eudi.wallet.document.format.MsoMdocFormat
import eu.europa.ec.eudi.wallet.document.format.SdJwtVcFormat
import kotlinx.datetime.toJavaInstant
import java.time.Instant
import com.android.identity.document.Document as IdentityDocument

/**
 * The document name stored in applicationData
 */
internal var IdentityDocument.documentName: String
    @JvmSynthetic
    get() = applicationData.getString("name")
    @JvmSynthetic
    set(value) {
        applicationData.setString("name", value)
    }

/**
 * The document state based on the presence of certifiedCredentials, pendingCredentials
 * and deferredRelatedData
 */
internal val IdentityDocument.state: DocumentState
    @JvmSynthetic
    get() = when {
        certifiedCredentials.isNotEmpty() -> DocumentState.ISSUED
        pendingCredentials.isNotEmpty() -> when {
            deferredRelatedData.isNotEmpty() -> DocumentState.DEFERRED
            else -> DocumentState.UNSIGNED
        }

        else -> DocumentState.UNSIGNED
    }

/**
 * The creation date of the document stored in applicationData
 */
internal var IdentityDocument.createdAt: Instant
    @JvmSynthetic
    get() = applicationData.getNumber("createdAt").let { Instant.ofEpochMilli(it) }
    @JvmSynthetic
    set(value) {
        applicationData.setNumber("createdAt", value.toEpochMilli())
    }

/**
 * The document manager id stored in applicationData
 */
internal var IdentityDocument.documentManagerId: String
    @JvmSynthetic
    get() = applicationData.getString("documentManagerId")
    @JvmSynthetic
    set(value) {
        applicationData.setString("documentManagerId", value)
    }

/**
 * The issued date of the document stored in applicationData
 */
internal var IdentityDocument.issuedAt: Instant
    @JvmSynthetic
    get() = try {
        applicationData.getNumber("issuedAt").let { Instant.ofEpochMilli(it) }
    } catch (_: Throwable) {
        // handle missing issuedAt field
        // since the issuedAt field was not present in the earlier versions of the app
        createdAt
    }
    @JvmSynthetic
    set(value) {
        applicationData.setNumber("issuedAt", value.toEpochMilli())
    }

/**
 * The name spaced data stored in applicationData under the key "nameSpacedData"
 */
internal var IdentityDocument.nameSpacedData: NameSpacedData
    @JvmSynthetic
    get() = this.applicationData.getNameSpacedData("nameSpacedData")
    @JvmSynthetic
    set(value) {
        applicationData.setNameSpacedData("nameSpacedData", value)
    }

/**
 * Deferred related data stored in applicationData under the key "deferredRelatedData"
 * set by [eu.europa.ec.eudi.wallet.document.DocumentManager.storeDeferredDocument]
 * method
 *
 * If not present, an empty byte array is returned
 */
internal var IdentityDocument.deferredRelatedData: ByteArray
    @JvmSynthetic
    get() = try {
        applicationData.getData("deferredRelatedData")
    } catch (_: Throwable) {
        // handle missing deferredRelatedData field
        byteArrayOf()
    }
    @JvmSynthetic
    set(value) {
        applicationData.setData("deferredRelatedData", value)
    }

/**
 * Clear the deferred related data stored in applicationData
 */
@JvmSynthetic
internal fun IdentityDocument.clearDeferredRelatedData() =
    applicationData.setData("deferredRelatedData", null)

/**
 * Create a [Document] based on the [IdentityDocument].
 * According to the [state] of the document, the returned document will be one of the following:
 * - [UnsignedDocument] if the document is unsigned
 * - [IssuedDocument] if the document is issued
 * - [DeferredDocument] if the document is deferred
 */
@JvmSynthetic
internal inline fun <reified D : Document> IdentityDocument.toDocument(): D {

    val credential = when (state) {
        DocumentState.UNSIGNED,
        DocumentState.DEFERRED -> pendingCredentials
            .filterIsInstance<SecureAreaBoundCredential>()
            .first()

        DocumentState.ISSUED -> certifiedCredentials
            .filterIsInstance<SecureAreaBoundCredential>()
            .first()
    }

    val documentFormat: DocumentFormat = when (credential) {
        is MdocCredential -> MsoMdocFormat(credential.docType)
        is SdJwtVcCredential -> SdJwtVcFormat(credential.vct)
        else -> throw IllegalArgumentException("Unsupported format type: ${credential::class}")
    }


    return when (state) {

        DocumentState.UNSIGNED -> UnsignedDocument(
            id = name,
            name = documentName,
            createdAt = createdAt,
            secureArea = credential.secureArea,
            format = documentFormat,
            documentManagerId = documentManagerId,
            isCertified = credential.isCertified,
            keyAlias = credential.alias,
        )

        DocumentState.ISSUED -> {
            when(documentFormat) {
                is MsoMdocFormat -> {
                    MsoMdocIssuedDocument(
                        id = name,
                        name = documentName,
                        createdAt = createdAt,
                        issuedAt = issuedAt,
                        secureArea = credential.secureArea,
                        format = documentFormat,
                        documentManagerId = documentManagerId,
                        isCertified = credential.isCertified,
                        keyAlias = credential.alias,
                        validFrom = credential.validFrom.toJavaInstant(),
                        validUntil = credential.validUntil.toJavaInstant(),
                        nameSpacedData = nameSpacedData,
                        issuerProvidedData = credential.issuerProvidedData
                    )
                }
                is SdJwtVcFormat -> {
                    SdJwtVcIssuedDocument(
                        id = name,
                        name = documentName,
                        createdAt = createdAt,
                        issuedAt = issuedAt,
                        secureArea = credential.secureArea,
                        format = documentFormat,
                        documentManagerId = documentManagerId,
                        isCertified = credential.isCertified,
                        keyAlias = credential.alias,
                        validFrom = credential.validFrom.toJavaInstant(),
                        validUntil = credential.validUntil.toJavaInstant(),
                        issuerProvidedData = credential.issuerProvidedData,
                        claims = credential.issuerProvidedData.getClaims()
                    )
                }
                else -> throw IllegalArgumentException("Unsupported format type: ${documentFormat::class}")
            }
        }

        DocumentState.DEFERRED -> DeferredDocument(
            id = name,
            name = documentName,
            createdAt = createdAt,
            secureArea = credential.secureArea,
            format = documentFormat,
            documentManagerId = documentManagerId,
            isCertified = credential.isCertified,
            keyAlias = credential.alias,
            relatedData = deferredRelatedData
        )
    } as D
}

private fun ByteArray.getClaims(): List<Claim> {

    val sdJwtVc = SdJwt.unverifiedIssuanceFrom(String(this, Charsets.US_ASCII)).getOrElse {
        throw IllegalArgumentException("Invalid SD-JWT VC")
    }
    val (_, claims) = sdJwtVc.jwt
    val nonSelectivelyDisclosable = claims.filter {
        !it.value.toString().contains("_sd") &&
                !it.key.contains("_sd")
    }.map { it.key to it.value }

    val selectivelyDisclosable = sdJwtVc.disclosures.filter {
        !it.claim().second.toString().contains("_sd")
    }.map { it.claim().first to it.claim().second }

    return (nonSelectivelyDisclosable + selectivelyDisclosable).map {
        Claim(it.first, it.second, selectivelyDisclosable.contains(it))
    }
}
