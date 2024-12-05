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

import com.android.identity.securearea.SecureArea
import eu.europa.ec.eudi.wallet.document.format.DocumentFormat
import eu.europa.ec.eudi.wallet.document.metadata.DocumentMetaData
import java.time.Instant

/**
 * Represents a Deferred Document.
 * A Deferred Document is also an [UnsignedDocument], since it is not yet signed by the issuer.
 * @property id the document id
 * @property name the document name
 * @property format the document format
 * @property documentManagerId the [DocumentManager.identifier] related to this document
 * @property isCertified whether the document is certified
 * @property keyAlias the key alias
 * @property secureArea the secure area
 * @property createdAt the creation date
 * @property relatedData the related data
 */
class DeferredDocument(
    id: DocumentId,
    name: String,
    format: DocumentFormat,
    documentManagerId: String,
    isCertified: Boolean,
    keyAlias: String,
    secureArea: SecureArea,
    createdAt: Instant,
    documentMetaData: DocumentMetaData?,
    val relatedData: ByteArray,
) : UnsignedDocument(
    id = id,
    name = name,
    format = format,
    documentManagerId = documentManagerId,
    isCertified = isCertified,
    keyAlias = keyAlias,
    secureArea = secureArea,
    createdAt = createdAt,
    metadata = documentMetaData
)