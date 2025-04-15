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

import eu.europa.ec.eudi.wallet.document.DeferredDocument
import eu.europa.ec.eudi.wallet.document.Document
import eu.europa.ec.eudi.wallet.document.IssuedDocument
import eu.europa.ec.eudi.wallet.document.UnsignedDocument
import eu.europa.ec.eudi.wallet.document.format.DocumentFormat
import eu.europa.ec.eudi.wallet.document.format.MsoMdocData
import eu.europa.ec.eudi.wallet.document.format.MsoMdocFormat
import eu.europa.ec.eudi.wallet.document.format.SdJwtVcData
import eu.europa.ec.eudi.wallet.document.format.SdJwtVcFormat
import eu.europa.ec.eudi.wallet.document.metadata.IssuerMetaData
import kotlinx.coroutines.runBlocking
import kotlinx.datetime.Instant
import kotlinx.datetime.toJavaInstant
import kotlinx.serialization.SerializationException
import org.multipaz.credential.SecureAreaBoundCredential
import org.multipaz.document.NameSpacedData
import org.multipaz.mdoc.credential.MdocCredential
import kotlin.jvm.Throws
import org.multipaz.document.Document as IdentityDocument

/**
 * The application metadata
 */
internal val IdentityDocument.applicationMetaData: ApplicationMetaData
    @JvmSynthetic
    get() = metadata as ApplicationMetaData

/**
 * The document id stored in application metadata
 */
internal var IdentityDocument.documentIdentifier: String
    @JvmSynthetic
    get() = applicationMetaData.documentIdentifier
    @JvmSynthetic
    set(value) {
        runBlocking { applicationMetaData.setDocumentIdentifier(value) }
    }

/**
 * The document name stored in application metadata
 */
internal var IdentityDocument.documentName: String
    @JvmSynthetic
    get() = applicationMetaData.documentName
    @JvmSynthetic
    set(value) {
        runBlocking { applicationMetaData.setDocumentName(value) }
    }

/**
 * The document state based on the presence of certifiedCredentials, pendingCredentials
 * and deferredRelatedData
 */
internal val IdentityDocument.state: DocumentState
    @JvmSynthetic
    get() = runBlocking { when {
        getCertifiedCredentials().isNotEmpty() -> DocumentState.ISSUED
        getPendingCredentials().isNotEmpty() -> when {
            deferredRelatedData.isNotEmpty() -> DocumentState.DEFERRED
            else -> DocumentState.UNSIGNED
        }

        else -> DocumentState.UNSIGNED
    }}

/**
 * The issuer metadata stored in application metadata under the key "issuerMetadata"
 */
internal var IdentityDocument.issuerMetaData: IssuerMetaData?
    @JvmSynthetic
    @Throws(IllegalArgumentException::class, SerializationException::class)
    /**
     * Get the metadata from applicationData under the key "issuerMetadata"
     * @return the metadata or null if not found
     * @throws IllegalArgumentException if the metadata is not valid json
     * @throws SerializationException if the metadata fails to deserialize
     */
    get() {
        return runBlocking {
            runCatching { applicationMetaData.issuerMetaData }.getOrNull()
        }
    }
    @JvmSynthetic
    @Throws(IllegalArgumentException::class, SerializationException::class)
    /**
     * Set the issuer metadata in application metadata under the key "issuerMetadata"
     * @param value the issuer metadata to be set
     * @throws IllegalArgumentException if the issuer metadata is not valid json
     * @throws SerializationException if the issuer metadata fails to serialize
     */
    set(value) {
        runBlocking {
            value?.let { applicationMetaData.setIssuerMetaData(it) }
        }
    }

/**
 * The creation date of the document stored in application metadata
 */
internal var IdentityDocument.createdAt: Instant
    @JvmSynthetic
    get() = applicationMetaData.createdAt
    @JvmSynthetic
    set(value) {
        runBlocking { applicationMetaData.setCreatedAt(value) }
    }

/**
 * The document manager id stored in application metadata
 */
internal var IdentityDocument.documentManagerId: String
    @JvmSynthetic
    get() = applicationMetaData.documentManagerId
    @JvmSynthetic
    set(value) {
        runBlocking { applicationMetaData.setDocumentManagerId(value) }
    }

/**
 * The issued date of the document stored in application metadata
 */
internal var IdentityDocument.issuedAt: Instant
    @JvmSynthetic
    get() = try { applicationMetaData.issuedAt!! }
     catch (_: Throwable) {
        // handle missing issuedAt field
        // since the issuedAt field was not present in the earlier versions of the app
        this.createdAt
    }
    @JvmSynthetic
    set(value) {
        runBlocking { applicationMetaData.setIssuedAt(value) }
    }

/**
 * The NameSpacedData stored in application metadata under the key "nameSpacedData"
 */
internal var IdentityDocument.nameSpacedData: NameSpacedData
    @JvmSynthetic
    get() =  applicationMetaData.nameSpacedData ?: NameSpacedData.Builder().build()
    @JvmSynthetic
    set(value) {
        runBlocking { applicationMetaData.setNameSpacedData(value) }
    }

/**
 * Deferred related data stored in application metadata under the key "deferredRelatedData"
 * set by [eu.europa.ec.eudi.wallet.document.DocumentManager.storeDeferredDocument]
 * method
 *
 * If not present, an empty byte array is returned
 */
internal var IdentityDocument.deferredRelatedData: ByteArray
    @JvmSynthetic
    get() =
    try {
        applicationMetaData.deferredRelatedData ?: byteArrayOf()
    }
    catch (_: Throwable) {
        // handle missing deferredRelatedData field
        byteArrayOf()
    }
    @JvmSynthetic
    set(value) {
        runBlocking { applicationMetaData.setDeferredRelatedData(value) }
    }

/**
 * Clear the deferred related data stored in application metadata
 */
@JvmSynthetic
internal fun IdentityDocument.clearDeferredRelatedData() =
    runBlocking { applicationMetaData.clearDeferredRelatedData() }

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
        DocumentState.DEFERRED -> runBlocking { getPendingCredentials() }
            .filterIsInstance<SecureAreaBoundCredential>()
            .first()

        DocumentState.ISSUED -> runBlocking { getCertifiedCredentials() }
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
            id = identifier,
            name = documentName,
            createdAt = createdAt.toJavaInstant(),
            secureArea = credential.secureArea,
            format = documentFormat,
            documentManagerId = documentManagerId,
            isCertified = credential.isCertified,
            keyAlias = credential.alias,
            issuerMetaData = issuerMetaData,
        )

        DocumentState.ISSUED -> IssuedDocument(
            id = identifier,
            name = documentName,
            createdAt = createdAt.toJavaInstant(),
            issuedAt = issuedAt.toJavaInstant(),
            secureArea = credential.secureArea,
            documentManagerId = documentManagerId,
            isCertified = credential.isCertified,
            keyAlias = credential.alias,
            validFrom = credential.validFrom.toJavaInstant(),
            validUntil = credential.validUntil.toJavaInstant(),
            issuerProvidedData = credential.issuerProvidedData,
            data = when (documentFormat) {
                is MsoMdocFormat -> MsoMdocData(
                    format = documentFormat,
                    nameSpacedData = nameSpacedData,
                    issuerMetadata = issuerMetaData,
                )

                is SdJwtVcFormat -> SdJwtVcData(
                    format = documentFormat,
                    sdJwtVc = credential.issuerProvidedData.sdJwtVcString,
                    issuerMetadata = issuerMetaData,
                )
            }
        )

        DocumentState.DEFERRED -> DeferredDocument(
            id = identifier,
            name = documentName,
            createdAt = createdAt.toJavaInstant(),
            secureArea = credential.secureArea,
            format = documentFormat,
            documentManagerId = documentManagerId,
            isCertified = credential.isCertified,
            keyAlias = credential.alias,
            relatedData = deferredRelatedData,
            issuerMetaData = issuerMetaData
        )
    } as D
}
