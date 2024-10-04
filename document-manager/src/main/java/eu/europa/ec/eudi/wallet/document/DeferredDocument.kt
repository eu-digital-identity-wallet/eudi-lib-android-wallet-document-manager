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

import com.android.identity.credential.SecureAreaBoundCredential
import com.android.identity.crypto.javaX509Certificates
import eu.europa.ec.eudi.wallet.document.Document.State
import eu.europa.ec.eudi.wallet.document.internal.createdAt
import eu.europa.ec.eudi.wallet.document.internal.deferredRelatedData
import eu.europa.ec.eudi.wallet.document.internal.docType
import eu.europa.ec.eudi.wallet.document.internal.documentName
import eu.europa.ec.eudi.wallet.document.internal.requiresUserAuth
import eu.europa.ec.eudi.wallet.document.internal.state
import eu.europa.ec.eudi.wallet.document.internal.usesStrongBox
import java.security.cert.X509Certificate
import java.time.Instant
import com.android.identity.document.Document as BaseDocument

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
    keyUnlockDataFactory: KeyUnlockDataFactory,
    val relatedData: ByteArray
) : Document, UnsignedDocument(
    id,
    name,
    docType,
    usesStrongBox,
    requiresUserAuth,
    createdAt,
    certificatesNeedAuth,
    keyUnlockDataFactory,
) {

    override val state: State
        get() = base?.state ?: State.DEFERRED

    override fun toString(): String {
        return "DeferredDocument(id='$id', docType='$docType', name='$name', usesStrongBox=$usesStrongBox, requiresUserAuth=$requiresUserAuth, createdAt=$createdAt, state=$state, relatedData=${relatedData.contentToString()})"
    }

    internal companion object {
        @JvmSynthetic
        operator fun invoke(
            baseDocument: BaseDocument,
            keyUnlockDataFactory: KeyUnlockDataFactory
        ) = DeferredDocument(
            id = baseDocument.name,
            name = baseDocument.documentName,
            docType = baseDocument.docType,
            usesStrongBox = baseDocument.usesStrongBox,
            requiresUserAuth = baseDocument.requiresUserAuth,
            createdAt = baseDocument.createdAt,
            certificatesNeedAuth = baseDocument.pendingCredentials
                .filterIsInstance<SecureAreaBoundCredential>()
                .firstOrNull()
                ?.attestation
                ?.certChain
                ?.javaX509Certificates
                ?: emptyList(),
            relatedData = baseDocument.deferredRelatedData,
            keyUnlockDataFactory = keyUnlockDataFactory
        ).apply {
            this.base = baseDocument
        }
    }
}