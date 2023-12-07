/*
 * Copyright (c) 2023 European Commission
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
package eu.europa.ec.eudi.wallet.document.sample

import COSE.AlgorithmID.ECDSA_256
import COSE.HeaderKeys.Algorithm
import COSE.Sign1Message
import android.content.Context
import com.android.identity.mdoc.mso.MobileSecurityObjectGenerator
import com.android.identity.util.Timestamp
import com.upokecenter.cbor.CBORObject
import eu.europa.ec.eudi.wallet.document.AddDocumentResult
import eu.europa.ec.eudi.wallet.document.CreateIssuanceRequestResult
import eu.europa.ec.eudi.wallet.document.DocumentManager
import eu.europa.ec.eudi.wallet.document.internal.docTypeName
import eu.europa.ec.eudi.wallet.document.internal.getEmbeddedCBORObject
import eu.europa.ec.eudi.wallet.document.internal.issuerCertificate
import eu.europa.ec.eudi.wallet.document.internal.issuerPrivateKey
import eu.europa.ec.eudi.wallet.document.internal.oneKey
import eu.europa.ec.eudi.wallet.document.internal.supportsStrongBox
import eu.europa.ec.eudi.wallet.document.internal.withTag24
import java.security.MessageDigest
import java.security.PublicKey

/**
 * A [SampleDocumentManager] implementation that composes a [DocumentManager] and provides methods to load sample data.
 *
 * @property hardwareBacked Indicates that hardware-backed keys should be used. Default is true if device supports it, false otherwise.
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
    var hardwareBacked: Boolean = context.supportsStrongBox

    /**
     * Instructs the document manager to use hardware-backed keys for the sample documents.
     *
     * @param flag true if hardware-backed keys should be used, false otherwise
     */
    fun hardwareBacked(flag: Boolean) = apply { hardwareBacked = flag }

    /**
     * Loads the sample data into the document manager.
     *
     * The sample data is a CBOR bytearray that has the following structure:
     *
     * ```cddl
     * Data = {
     *  "documents" : [+Document], ; Returned documents
     * }
     * Document = {
     *  "docType" : DocType, ; Document type returned
     *  "issuerSigned" : IssuerSigned, ; Returned data elements signed by the issuer
     * }
     * IssuerSigned = {
     *  "nameSpaces" : IssuerNameSpaces, ; Returned data elements
     * }
     * IssuerNameSpaces = { ; Returned data elements for each namespace
     *  + NameSpace => [ + IssuerSignedItemBytes ]
     * }
     * IssuerSignedItem = {
     *  "digestID" : uint, ; Digest ID for issuer data authentication
     *  "random" : bstr, ; Random value for issuer data authentication
     *  "elementIdentifier" : DataElementIdentifier, ; Data element identifier
     *  "elementValue" : DataElementValue ; Data element value
     * }
     * ```
     *
     * @param sampleData
     * @return [LoadSampleResult.Success] if the sample data has been loaded successfully. Otherwise, returns [LoadSampleResult.Error], with the error message.
     */
    override fun loadSampleData(sampleData: ByteArray): LoadSampleResult {
        try {
            val cbor = CBORObject.DecodeFromBytes(sampleData)
            val documents = cbor.get("documents")
            documents.values.forEach { documentCbor ->
                val docType = documentCbor["docType"].AsString()

                val issuerSigned = documentCbor["issuerSigned"]
                val nameSpaces = issuerSigned["nameSpaces"]

                when (val requestResult = createIssuanceRequest(docType, hardwareBacked)) {
                    is CreateIssuanceRequestResult.Failure -> {
                        return LoadSampleResult.Error(requestResult.throwable)
                    }

                    is CreateIssuanceRequestResult.Success -> {
                        val request = requestResult.issuanceRequest
                        request.name = context.docTypeName(docType) ?: docType
                        val authKey = request.publicKey

                        val mso = generateMso(docType, authKey, nameSpaces)
                        val issuerAuth = signMso(mso)
                        val data = generateData(docType, nameSpaces, issuerAuth)

                        when (val addResult = addDocument(request, data)) {
                            is AddDocumentResult.Failure -> return LoadSampleResult.Error(addResult.throwable)

                            is AddDocumentResult.Success -> {
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

        private fun generateMso(
            docType: String,
            authKey: PublicKey,
            nameSpaces: CBORObject,
        ) =
            MobileSecurityObjectGenerator(DIGEST_ALG, docType, authKey)
                .apply {
                    val now = Timestamp.now().toEpochMilli()
                    val signed = Timestamp.ofEpochMilli(now)
                    val validFrom = Timestamp.ofEpochMilli(now)
                    val validUntil = Timestamp.ofEpochMilli(now + 1000L * 60L * 60L * 24L * 365L)
                    setValidityInfo(signed, validFrom, validUntil, null)

                    nameSpaces.entries.forEach { (nameSpace, issuerSignedItems) ->
                        val digestIds = calculateDigests(issuerSignedItems)
                        addDigestIdsForNamespace(nameSpace.AsString(), digestIds)
                    }
                }
                .generate()

        private fun calculateDigests(issuerSignedItems: CBORObject): Map<Long, ByteArray> {
            return issuerSignedItems.values.associate { issuerSignedItemBytes ->
                val issuerSignedItem = issuerSignedItemBytes.getEmbeddedCBORObject()
                val digest = MessageDigest.getInstance(DIGEST_ALG)
                    .digest(issuerSignedItemBytes.EncodeToBytes())
                issuerSignedItem["digestID"].AsInt32().toLong() to digest
            }
        }

        private fun signMso(mso: ByteArray) = Sign1Message(false, true).apply {
            protectedAttributes.Add(Algorithm.AsCBOR(), ECDSA_256.AsCBOR())
            unprotectedAttributes.Add(33L, issuerCertificate.encoded)
            SetContent(mso.withTag24())
            sign(issuerPrivateKey.oneKey)
        }.EncodeToCBORObject()

        private fun generateData(
            docType: String,
            issuerNameSpaces: CBORObject,
            issuerAuth: CBORObject,
        ): ByteArray {
            return mapOf(
                "documents" to arrayOf(
                    mapOf(
                        "docType" to docType,
                        "issuerSigned" to mapOf(
                            "nameSpaces" to issuerNameSpaces,
                            "issuerAuth" to issuerAuth,
                        ),
                    ),
                ),
            ).let { CBORObject.FromObject(it).EncodeToBytes() }
        }
    }
}
