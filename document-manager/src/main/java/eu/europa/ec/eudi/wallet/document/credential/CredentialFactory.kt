/*
 * Copyright (c) 2025 European Commission
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

package eu.europa.ec.eudi.wallet.document.credential

import eu.europa.ec.eudi.wallet.document.CreateDocumentSettings
import eu.europa.ec.eudi.wallet.document.format.DocumentFormat
import eu.europa.ec.eudi.wallet.document.format.MsoMdocFormat
import eu.europa.ec.eudi.wallet.document.format.SdJwtVcFormat
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.withContext
import org.multipaz.credential.SecureAreaBoundCredential
import org.multipaz.document.Document
import org.multipaz.mdoc.credential.MdocCredential
import org.multipaz.securearea.SecureArea

interface CredentialFactory {
    suspend fun createCredentials(
        format: DocumentFormat,
        document: Document,
        createDocumentSettings: CreateDocumentSettings,
        secureArea: SecureArea,
    ): List<SecureAreaBoundCredential>

    companion object {
        operator fun invoke(domain: String, format: DocumentFormat): CredentialFactory {
            return when (format) {
                is MsoMdocFormat -> MdocCredentialFactory(domain)
                is SdJwtVcFormat -> SdJwtVcCredentialFactory(domain)
            }
        }
    }
}

class MdocCredentialFactory(
    val domain: String,
) : CredentialFactory {
    override suspend fun createCredentials(
        format: DocumentFormat,
        document: Document,
        createDocumentSettings: CreateDocumentSettings,
        secureArea: SecureArea
    ): List<MdocCredential> {
        require(format is MsoMdocFormat) {
            "Expected ${MsoMdocFormat::class}"
        }
        return withContext(Dispatchers.IO) {
            (0..<createDocumentSettings.numberOfCredentials).map {
                async {
                    MdocCredential.create(
                        document = document,
                        domain = domain,
                        secureArea = secureArea,
                        docType = format.docType,
                        createKeySettings = createDocumentSettings.createKeySettings,
                        asReplacementForIdentifier = null,
                    )
                }
            }.awaitAll()
        }
    }
}

class SdJwtVcCredentialFactory(val domain: String) :
    CredentialFactory {
    override suspend fun createCredentials(
        format: DocumentFormat,
        document: Document,
        createDocumentSettings: CreateDocumentSettings,
        secureArea: SecureArea
    ): List<SdJwtVcCredential> {
        require(format is SdJwtVcFormat) {
            "Expected ${SdJwtVcFormat::class}"
        }
        return withContext(Dispatchers.IO) {
            (0..<createDocumentSettings.numberOfCredentials).map {
                async {
                    SdJwtVcCredential.Companion.create(
                        document = document,
                        domain = domain,
                        secureArea = secureArea,
                        vct = format.vct,
                        createKeySettings = createDocumentSettings.createKeySettings,
                        asReplacementForIdentifier = null,
                    )
                }
            }.awaitAll()
        }
    }
}


