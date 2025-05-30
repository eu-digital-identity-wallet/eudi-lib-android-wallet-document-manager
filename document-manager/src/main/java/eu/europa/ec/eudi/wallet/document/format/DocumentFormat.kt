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

package eu.europa.ec.eudi.wallet.document.format

import eu.europa.ec.eudi.wallet.document.DocType
import eu.europa.ec.eudi.wallet.document.Vct

/**
 * Represents a Document Format
 */
sealed interface DocumentFormat {
    companion object
}

/**
 * Represents a MsoMdoc Format for a [eu.europa.ec.eudi.wallet.document.Document]
 * @property docType the document type
 */
data class MsoMdocFormat(val docType: DocType) : DocumentFormat

/**
 * Represents a SdJwtVc Format for the [eu.europa.ec.eudi.wallet.document.Document]
 * @property vct the Vct of the document
 */
data class SdJwtVcFormat(val vct: Vct) : DocumentFormat