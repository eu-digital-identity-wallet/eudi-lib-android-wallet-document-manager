/*
 *  Copyright (c) 2024 European Commission
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package eu.europa.ec.eudi.wallet.document.internal

import com.android.identity.android.securearea.AndroidKeystoreSecureArea
import com.android.identity.credential.Credential
import eu.europa.ec.eudi.wallet.document.Document
import java.time.Instant

internal var Credential.documentName: String
    @JvmSynthetic
    get() = applicationData.getString("name")
    @JvmSynthetic
    set(value) {
        applicationData.setString("name", value)
    }

internal var Credential.state: Document.State
    @JvmSynthetic
    get() = try {
        applicationData.getNumber("state").let { Document.State.values()[it.toInt()] }
    } catch (_: Throwable) {
        // handle missing state field
        // since the state field was not present in the earlier versions of the app
        if (nameSpacedData.nameSpaceNames.isEmpty()) Document.State.UNSIGNED
        else Document.State.ISSUED
    }
    @JvmSynthetic
    set(value) {
        applicationData.setNumber("state", value.value)
    }

internal var Credential.docType: String
    @JvmSynthetic
    get() = applicationData.getString("docType")
    @JvmSynthetic
    set(value) {
        applicationData.setString("docType", value)
    }

internal var Credential.createdAt: Instant
    @JvmSynthetic
    get() = applicationData.getNumber("createdAt").let { Instant.ofEpochMilli(it) }
    @JvmSynthetic
    set(value) {
        applicationData.setNumber("createdAt", value.toEpochMilli())
    }

internal var Credential.issuedAt: Instant
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


internal val Credential.usesStrongBox: Boolean
    @JvmSynthetic
    get() = when (state) {
        Document.State.UNSIGNED, Document.State.DEFERRED -> pendingAuthenticationKeys.firstOrNull()?.alias
        Document.State.ISSUED -> authenticationKeys.firstOrNull()?.alias
    }?.let {
        (credentialSecureArea.getKeyInfo(it) as AndroidKeystoreSecureArea.KeyInfo)
    }?.isStrongBoxBacked ?: false

internal val Credential.requiresUserAuth: Boolean
    @JvmSynthetic
    get() = when (state) {
        Document.State.UNSIGNED, Document.State.DEFERRED -> pendingAuthenticationKeys.firstOrNull()?.alias
        Document.State.ISSUED -> authenticationKeys.firstOrNull()?.alias
    }?.let {
        (credentialSecureArea.getKeyInfo(it) as AndroidKeystoreSecureArea.KeyInfo)
    }?.isUserAuthenticationRequired ?: false


internal var Credential.attestationChallenge: ByteArray
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

internal var Credential.deferredRelatedData: ByteArray
    @JvmSynthetic
    get() = applicationData.getData("deferredRelatedData")
    @JvmSynthetic
    set(value) {
        applicationData.setData("deferredRelatedData", value)
    }

@JvmSynthetic
internal fun Credential.clearDeferredRelatedData() = applicationData.setData("deferredRelatedData", null)
