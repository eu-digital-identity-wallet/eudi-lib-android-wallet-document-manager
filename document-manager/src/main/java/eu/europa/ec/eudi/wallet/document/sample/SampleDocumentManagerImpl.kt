/*
 *  Copyright (c) 2023-2024 European Commission
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
package eu.europa.ec.eudi.wallet.document.sample

import android.content.Context
import com.upokecenter.cbor.CBORObject
import eu.europa.ec.eudi.wallet.document.CreateDocumentResult
import eu.europa.ec.eudi.wallet.document.DocumentManager
import eu.europa.ec.eudi.wallet.document.StoreDocumentResult
import eu.europa.ec.eudi.wallet.document.internal.*

/**
 * A [SampleDocumentManager] implementation that composes a [DocumentManager] and provides methods to load sample data.
 *
 * @property useStrongBox Indicates that hardware-backed keys should be used. Default is true if device supports it, false otherwise.
 *
 * @constructor
 * @param context the application context
 * @param documentManager [DocumentManager] implementation to delegate the document management operations
 */
class SampleDocumentManagerImpl(
    context: Context,
    documentManager: DocumentManager,
) : DocumentManager by documentManager, SampleDocumentManager {
    private val context = context.applicationContext
    var useStrongBox: Boolean = context.supportsStrongBox

    /**
     * Instructs the document manager to use strong box for sample document's keys.
     *
     * @param flag true if hardware-backed keys should be used, false otherwise
     */
    fun useStrongBox(flag: Boolean) = apply { useStrongBox = flag }

    override fun loadSampleData(sampleData: ByteArray): LoadSampleResult {
        try {
            val cbor = CBORObject.DecodeFromBytes(sampleData)
            val documents = cbor.get("documents")
            documents.values.forEach { documentCbor ->
                val docType = documentCbor["docType"].AsString()

                val issuerSigned = documentCbor["issuerSigned"]
                val nameSpaces = issuerSigned["nameSpaces"]
                when (val createDocumentResult = createDocument(docType, useStrongBox)) {
                    is CreateDocumentResult.Failure -> {
                        return LoadSampleResult.Error(createDocumentResult.throwable)
                    }

                    is CreateDocumentResult.Success -> {
                        val usignedDocument = createDocumentResult.unsignedDocument
                        usignedDocument.name = context.getDocumentNameFromResourcesOrDocType(docType)
                        val authKey = usignedDocument.publicKey

                        val mso = generateMso(DIGEST_ALG, docType, authKey, nameSpaces)
                        val issuerAuth = signMso(mso)
                        val data = generateData(nameSpaces, issuerAuth)

                        when (val addResult = storeIssuedDocument(usignedDocument, data)) {
                            is StoreDocumentResult.Failure -> return LoadSampleResult.Error(addResult.throwable)

                            is StoreDocumentResult.Success -> {
                                // proceed to next document
                            }
                        }
                    }
                }
            }
            return LoadSampleResult.Success
        } catch (e: Exception) {
            return LoadSampleResult.Error(e.message ?: "Error storing sample documents")
        }
    }

    private companion object {
        private const val DIGEST_ALG = "SHA-256"
    }
}
