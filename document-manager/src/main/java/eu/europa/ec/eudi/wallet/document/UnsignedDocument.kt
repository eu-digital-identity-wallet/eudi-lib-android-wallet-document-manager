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
import java.time.Instant

/**
 * Represents an unsigned document
 * @property id the document id
 * @property name the document name
 * @property format the document format
 * @property isCertified whether the document is certified
 * @property keyAlias the key alias
 * @property secureArea the secure area
 * @property createdAt the creation date
 */
open class UnsignedDocument(
    override val id: DocumentId,
    override var name: String,
    override val format: DocumentFormat,
    override val isCertified: Boolean,
    override val keyAlias: String,
    override val secureArea: SecureArea,
    override val createdAt: Instant,
) : Document