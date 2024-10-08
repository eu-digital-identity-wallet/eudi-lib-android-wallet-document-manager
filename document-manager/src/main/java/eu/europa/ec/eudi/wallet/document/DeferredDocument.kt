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
import java.security.PublicKey
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
open class DeferredDocument(
    id: DocumentId,
    docType: String,
    name: String,
    usesStrongBox: Boolean,
    requiresUserAuth: Boolean,
    createdAt: Instant,
    publicKeyCoseBytes: ByteArray,
    certificatesNeedAuth: List<X509Certificate>,
    val relatedData: ByteArray
) : Document, UnsignedDocument(
    id = id,
    docType = docType,
    name = name,
    usesStrongBox = usesStrongBox,
    requiresUserAuth = requiresUserAuth,
    createdAt = createdAt,
    publicKeyCoseBytes = publicKeyCoseBytes,
    certificatesNeedAuth = certificatesNeedAuth,
) {

    override var state: State = State.DEFERRED

    internal companion object {
        @JvmSynthetic
        operator fun invoke(
            baseDocument: BaseDocument,
            keyUnlockDataFactory: KeyUnlockDataFactory
        ): DeferredDocument =
            DeferredDocumentImpl(
                baseDocument,
                UnsignedDocument(baseDocument, keyUnlockDataFactory)
            )
    }
}

internal class DeferredDocumentImpl(
    baseDocument: BaseDocument,
    private val unsignedDocument: UnsignedDocument,
) : DeferredDocument(
    id = baseDocument.name,
    docType = baseDocument.docType,
    name = baseDocument.documentName,
    usesStrongBox = baseDocument.usesStrongBox,
    requiresUserAuth = baseDocument.requiresUserAuth,
    createdAt = baseDocument.createdAt,
    publicKeyCoseBytes = unsignedDocument.publicKeyCoseBytes,
    certificatesNeedAuth = unsignedDocument.certificatesNeedAuth,
    relatedData = baseDocument.deferredRelatedData,
) {

    init {
        state = baseDocument.state
    }

    override fun signWithAuthKey(data: ByteArray, alg: String): SignedWithAuthKeyResult {
        return unsignedDocument.signWithAuthKey(data, alg)
    }

    override fun toString(): String {
        return "DeferredDocument(id='$id', docType='$docType', name='$name', usesStrongBox=$usesStrongBox, requiresUserAuth=$requiresUserAuth, createdAt=$createdAt, state=$state, relatedData=${relatedData.contentToString()})"
    }


}