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
import eu.europa.ec.eudi.wallet.document.DeferredDocument
import eu.europa.ec.eudi.wallet.document.Document
import eu.europa.ec.eudi.wallet.document.IssuedDocument
import eu.europa.ec.eudi.wallet.document.UnsignedDocument
import eu.europa.ec.eudi.wallet.document.format.DocumentFormat
import eu.europa.ec.eudi.wallet.document.format.MsoMdocFormat
import kotlinx.datetime.toJavaInstant
import java.time.Instant
import com.android.identity.document.Document as IdentityDocument

internal var IdentityDocument.documentName: String
    @JvmSynthetic
    get() = applicationData.getString("name")
    @JvmSynthetic
    set(value) {
        applicationData.setString("name", value)
    }

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

internal var IdentityDocument.createdAt: Instant
    @JvmSynthetic
    get() = applicationData.getNumber("createdAt").let { Instant.ofEpochMilli(it) }
    @JvmSynthetic
    set(value) {
        applicationData.setNumber("createdAt", value.toEpochMilli())
    }

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

internal var IdentityDocument.nameSpacedData: NameSpacedData
    @JvmSynthetic
    get() = this.applicationData.getNameSpacedData("nameSpacedData")
    @JvmSynthetic
    set(value) {
        applicationData.setNameSpacedData("nameSpacedData", value)
    }

internal var IdentityDocument.attestationChallenge: ByteArray
    @JvmSynthetic
    get() = try {
        applicationData.getData("attestationChallenge")
    } catch (_: Throwable) {
        // handle missing attestationChallenge field
        // since the attestationChallenge field was not present in the earlier versions of the app
        ByteArray(0)
    }
    @JvmSynthetic
    set(value) {
        applicationData.setData("attestationChallenge", value)
    }

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

@JvmSynthetic
internal fun IdentityDocument.clearDeferredRelatedData() =
    applicationData.setData("deferredRelatedData", null)

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
        else -> throw IllegalArgumentException("Unsupported format type: ${credential::class}")
    }


    return when (state) {

        DocumentState.UNSIGNED -> UnsignedDocument(
            id = name,
            name = documentName,
            createdAt = createdAt,
            secureArea = credential.secureArea,
            format = documentFormat,
            isCertified = credential.isCertified,
            keyAlias = credential.alias,
        )

        DocumentState.ISSUED -> IssuedDocument(
            id = name,
            name = documentName,
            createdAt = createdAt,
            issuedAt = issuedAt,
            secureArea = credential.secureArea,
            format = documentFormat,
            isCertified = credential.isCertified,
            keyAlias = credential.alias,
            validFrom = credential.validFrom.toJavaInstant(),
            validUntil = credential.validUntil.toJavaInstant(),
            nameSpacedData = nameSpacedData,
            issuerProvidedData = credential.issuerProvidedData
        )

        DocumentState.DEFERRED -> DeferredDocument(
            id = name,
            name = documentName,
            createdAt = createdAt,
            secureArea = credential.secureArea,
            format = documentFormat,
            isCertified = credential.isCertified,
            keyAlias = credential.alias,
            relatedData = deferredRelatedData
        )
    } as D
}