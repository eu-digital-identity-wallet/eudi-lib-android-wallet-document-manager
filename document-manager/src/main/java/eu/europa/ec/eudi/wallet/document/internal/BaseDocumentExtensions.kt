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

import com.android.identity.android.securearea.AndroidKeystoreKeyInfo
import com.android.identity.credential.SecureAreaBoundCredential
import com.android.identity.document.NameSpacedData
import com.android.identity.mdoc.credential.MdocCredential
import eu.europa.ec.eudi.wallet.document.Document
import kotlinx.datetime.Clock
import java.time.Instant
import com.android.identity.document.Document as BaseDocument

internal var BaseDocument.documentName: String
    @JvmSynthetic
    get() = applicationData.getString("name")
    @JvmSynthetic
    set(value) {
        applicationData.setString("name", value)
    }

internal var BaseDocument.state: Document.State
    @JvmSynthetic
    get() = try {
        applicationData.getNumber("state").let { Document.State.entries[it.toInt()] }
    } catch (_: Throwable) {
        // handle missing state field
        // since the state field was not present in the earlier versions of the app
        if (!applicationData.keyExists("nameSpacedData") || applicationData.getNameSpacedData("nameSpacedData").nameSpaceNames.isEmpty()) Document.State.UNSIGNED
        else Document.State.ISSUED
    }
    @JvmSynthetic
    set(value) {
        applicationData.setNumber("state", value.value)
    }

internal var BaseDocument.docType: String
    @JvmSynthetic
    get() = applicationData.getString("docType")
    @JvmSynthetic
    set(value) {
        applicationData.setString("docType", value)
    }

internal var BaseDocument.createdAt: Instant
    @JvmSynthetic
    get() = applicationData.getNumber("createdAt").let { Instant.ofEpochMilli(it) }
    @JvmSynthetic
    set(value) {
        applicationData.setNumber("createdAt", value.toEpochMilli())
    }

internal var BaseDocument.issuedAt: Instant
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

internal var BaseDocument.nameSpacedData: NameSpacedData
    @JvmSynthetic
    get() = this.applicationData.getNameSpacedData("nameSpacedData")
    @JvmSynthetic
    set(value) {
        applicationData.setNameSpacedData("nameSpacedData", value)
    }


internal val BaseDocument.usesStrongBox: Boolean
    @JvmSynthetic
    get() = when (state) {
        Document.State.UNSIGNED, Document.State.DEFERRED -> pendingCredentials
        Document.State.ISSUED -> certifiedCredentials
    }.filterIsInstance<SecureAreaBoundCredential>()
        .firstOrNull()
        ?.let { it.secureArea.getKeyInfo(it.alias) }
        ?.let { it as AndroidKeystoreKeyInfo }
        ?.isStrongBoxBacked ?: false

internal val BaseDocument.requiresUserAuth: Boolean
    @JvmSynthetic
    get() = when (state) {
        Document.State.UNSIGNED, Document.State.DEFERRED -> pendingCredentials
        Document.State.ISSUED -> certifiedCredentials
    }.filterIsInstance<SecureAreaBoundCredential>()
        .firstOrNull()
        ?.let { it.secureArea.getKeyInfo(it.alias) }
        ?.let { it as AndroidKeystoreKeyInfo }
        ?.isUserAuthenticationRequired ?: false


internal var BaseDocument.attestationChallenge: ByteArray
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

internal var BaseDocument.deferredRelatedData: ByteArray
    @JvmSynthetic
    get() = applicationData.getData("deferredRelatedData")
    @JvmSynthetic
    set(value) {
        applicationData.setData("deferredRelatedData", value)
    }

@JvmSynthetic
internal fun BaseDocument.clearDeferredRelatedData() =
    applicationData.setData("deferredRelatedData", null)

@JvmSynthetic
internal fun BaseDocument.findCredential(
    now: kotlinx.datetime.Instant = Clock.System.now()
): MdocCredential? {
    var candidate: MdocCredential? = null
    certifiedCredentials
        .filterIsInstance<MdocCredential>()
        .filter { now >= it.validFrom && now <= it.validUntil }
        .forEach { credential ->
            // If we already have a candidate, prefer this one if its usage count is lower
            candidate?.let { candidateCredential ->
                if (credential.usageCount < candidateCredential.usageCount) {
                    candidate = credential
                }
            } ?: run {
                candidate = credential
            }

        }
    return candidate
}
