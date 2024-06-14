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

/**
 * The result of [DocumentManager.createDocument] method
 */
sealed interface CreateDocumentResult {

    /**
     * Success result containing the [UnsignedDocument], that can be then used to issue the document
     * from the issuer. The [UnsignedDocument] contains the certificate chain that must be sent to the issuer.
     *
     * @property unsignedDocument
     *
     * @constructor
     * @param unsignedDocument
     */
    data class Success(val unsignedDocument: UnsignedDocument) : CreateDocumentResult

    /**
     * Failure while creating a document. Contains the throwable that caused the failure
     *
     * @property throwable
     * @constructor Create empty Failure
     */
    data class Failure(val throwable: Throwable) : CreateDocumentResult

    /**
     * Execute block if the result is successful
     *
     * @param block block to be executed if the result is successful
     * @return [CreateDocumentResult]
     */
    fun onSuccess(block: (UnsignedDocument) -> Unit): CreateDocumentResult = apply {
        if (this is Success) block(unsignedDocument)
    }

    /**
     * Execute block if the result is a failure
     *
     * @param block block to be executed if the result is a failure
     * @return [CreateDocumentResult]
     */
    fun onFailure(block: (Throwable) -> Unit): CreateDocumentResult = apply {
        if (this is Failure) block(throwable)
    }

    /**
     * Get [UnsignedDocument] or throw the throwable that caused the failure
     *
     * @return [UnsignedDocument]
     */
    fun getOrThrow(): UnsignedDocument = when (this) {
        is Success -> unsignedDocument
        is Failure -> throw throwable
    }
}