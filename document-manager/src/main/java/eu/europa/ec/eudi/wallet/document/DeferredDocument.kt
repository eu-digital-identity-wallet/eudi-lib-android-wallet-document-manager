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

package eu.europa.ec.eudi.wallet.document

import com.android.identity.credential.Credential
import eu.europa.ec.eudi.wallet.document.Document.State
import eu.europa.ec.eudi.wallet.document.internal.*
import java.security.cert.X509Certificate
import java.time.Instant

/**
 * A [DeferredDocument] is as [UnsignedDocument] with extra [relatedData] that can be used later on
 * by the issuing process. To store the [DeferredDocument] and its related data, use the
 * [DocumentManager.storeDeferredDocument]
 *
 * @property relatedData the related data
 */
class DeferredDocument(
    id: DocumentId,
    name: String,
    docType: String,
    usesStrongBox: Boolean,
    requiresUserAuth: Boolean,
    createdAt: Instant,
    certificatesNeedAuth: List<X509Certificate>,
    val relatedData: ByteArray
) : Document, UnsignedDocument(
    id,
    name,
    docType,
    usesStrongBox,
    requiresUserAuth,
    createdAt,
    certificatesNeedAuth,
) {

    override val state: State
        get() = credential?.state ?: State.DEFERRED

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as DeferredDocument

        if (id != other.id) return false
        if (docType != other.docType) return false
        if (name != other.name) return false
        if (usesStrongBox != other.usesStrongBox) return false
        if (requiresUserAuth != other.requiresUserAuth) return false
        if (createdAt != other.createdAt) return false
        if (state != other.state) return false
        if (!relatedData.contentEquals(other.relatedData)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id.hashCode()
        result = 31 * result + docType.hashCode()
        result = 31 * result + name.hashCode()
        result = 31 * result + usesStrongBox.hashCode()
        result = 31 * result + requiresUserAuth.hashCode()
        result = 31 * result + createdAt.hashCode()
        result = 31 * result + state.hashCode()
        result = 31 * result + relatedData.contentHashCode()
        return result
    }

    override fun toString(): String {
        return "DeferredDocument(id='$id', docType='$docType', name='$name', usesStrongBox=$usesStrongBox, requiresUserAuth=$requiresUserAuth, createdAt=$createdAt, state=$state, relatedData=${relatedData.contentToString()})"
    }

    internal companion object {
        @JvmSynthetic
        operator fun invoke(credential: Credential) = DeferredDocument(
            id = credential.name,
            name = credential.documentName,
            docType = credential.docType,
            usesStrongBox = credential.usesStrongBox,
            requiresUserAuth = credential.requiresUserAuth,
            createdAt = credential.createdAt,
            certificatesNeedAuth = credential.pendingAuthenticationKeys.first().attestation,
            relatedData = credential.deferredRelatedData,
        ).apply {
            this.credential = credential
        }
    }
}