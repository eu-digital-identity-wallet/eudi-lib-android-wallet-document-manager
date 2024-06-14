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
@file:JvmMultifileClass

package eu.europa.ec.eudi.wallet.document.sample

import android.content.Context
import eu.europa.ec.eudi.wallet.document.DocumentManager
import eu.europa.ec.eudi.wallet.document.internal.supportsStrongBox
import eu.europa.ec.eudi.wallet.document.sample.SampleDocumentManager.Builder

/**
 * An extension of [DocumentManager] that provides methods to load sample data.
 *
 * The sample data is a CBOR file that contains the following information:
 * - A list of documents to be loaded in the document manager.
 * - Each document contains:
 *  - The document's docType
 *  - A list of namespaces and element identifiers with their corresponding values and random values and digest values to be used in the hash computation.
 *
 * A default implementation is provided by [SampleDocumentManagerImpl]. To instantiate a [SampleDocumentManagerImpl], use the [Builder] class.
 *
 * ```
 * val documentManager = SampleDocumentManager.Builder(context)
 *      .hardwareBacked(true)
 *      .build()
 * ```
 *
 * You can also use a different [DocumentManager] implementation, by passing it to the [Builder] class.
 * By default, the [DocumentManager] implementation used is [eu.europa.ec.eudi.wallet.document.DocumentManagerImpl].
 *
 * ```
 * val sampleDocumentManager = SampleDocumentManager.Builder(context)
 *     documentManager = MyDocumentManager()
 * }.build()
 * ```
 *
 */
interface SampleDocumentManager : DocumentManager {
    /**
     * Loads the sample data into the document manager.
     *
     * @param sampleData the sample data to be loaded in cbor format
     * @return [LoadSampleResult.Success] if the sample data has been loaded successfully.
     * Otherwise, returns [LoadSampleResult.Error], with the error message.
     *
     * Expected sampleData format is CBOR. The CBOR data must be in the following structure:
     *
     * ```cddl
     * SampleData = {
     *   "documents" : [+Document], ; Returned documents
     * }
     * Document = {
     *   "docType" : DocType, ; Document type returned
     *   "issuerSigned" : IssuerSigned, ; Returned data elements signed by the issuer
     * }
     * IssuerSigned = {
     *   "nameSpaces" : IssuerNameSpaces, ; Returned data elements
     *   "issuerAuth" : IssuerAuth ; Contains the mobile security object (MSO) for issuer data authentication
     * }
     * IssuerNameSpaces = { ; Returned data elements for each namespace
     *   + NameSpace => [ + IssuerSignedItemBytes ]
     * }
     * IssuerSignedItemBytes = #6.24(bstr .cbor IssuerSignedItem)
     * IssuerSignedItem = {
     *   "digestID" : uint, ; Digest ID for issuer data authentication
     *   "random" : bstr, ; Random value for issuer data authentication
     *   "elementIdentifier" : DataElementIdentifier, ; Data element identifier
     *   "elementValue" : DataElementValue ; Data element value
     * }
     * IssuerAuth = COSE_Sign1 ; The payload is MobileSecurityObjectBytes
     * ```
     */
    fun loadSampleData(sampleData: ByteArray): LoadSampleResult

    /**
     * Builder class to instantiate a SampleDocumentManager.
     *
     * @property hardwareBacked if hardware-backed keys should be used. Default is true if device supports it.
     *
     * example:
     * ```
     * val documentManager = SampleDocumentManager.Builder(context)
     *    .hardwareBacked(true)
     *    .build()
     * ```
     *
     * @constructor
     *
     * @param context
     */
    class Builder(context: Context) {
        private val _context = context.applicationContext

        var documentManager: DocumentManager = DocumentManager.Builder(_context).build()

        /**
         * Flag to indicate that the documents' keys should be stored in hardware backed keystore if supported by the device.
         */
        var hardwareBacked: Boolean = _context.supportsStrongBox

        /**
         * Sets the flag to indicate that the documents' keys should be stored in hardware backed keystore if supported by the device.
         *
         */
        fun hardwareBacked(flag: Boolean) = apply { hardwareBacked = flag }

        /**
         * Builds the SampleDocumentManager.
         *
         * @return [SampleDocumentManager]
         */
        fun build(): SampleDocumentManager {
            return SampleDocumentManagerImpl(_context, documentManager).apply {
                this.useStrongBox = this@Builder.hardwareBacked
            }
        }
    }
}

/**
 * [SampleDocumentManager.loadSampleData] result.
 * If the sample data has been loaded successfully, returns [LoadSampleResult.Success].
 * Otherwise, returns [LoadSampleResult.Error], with the error message.
 *
 * @see SampleDocumentManager.loadSampleData
 *
 * @constructor Create empty Load sample result
 */
sealed interface LoadSampleResult {
    /**
     * Success object to return when the sample data has been loaded successfully.
     *
     * @constructor Create empty Success
     */
    object Success : LoadSampleResult {
        override fun toString(): String = "Success"
    }

    /**
     * Error class to return the error message.
     *
     * @property throwable exception that caused the error
     * @property message error message
     * @constructor
     * @param throwable exception that caused the error
     */
    data class Error(val throwable: Throwable) : LoadSampleResult {
        constructor(message: String) : this(Exception(message))

        val message = throwable.message ?: "Error"
    }
}
