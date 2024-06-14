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
 * The result of [DocumentManager.storeIssuedDocument] and [DocumentManager.storeDeferredDocument]
 * methods
 */
sealed interface StoreDocumentResult {

    /**
     * Success result containing the documentId.
     * DocumentId can be then used to retrieve the document from the [DocumentManager.getDocumentById] method
     *
     * @property documentId document's unique identifier
     * @property proofOfProvisioning proof of provisioning
     * @constructor
     *
     * @param documentId document's unique identifier
     * @param proofOfProvisioning proof of provisioning
     */
    data class Success(val documentId: DocumentId, val proofOfProvisioning: ByteArray? = null) :
        StoreDocumentResult {
        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (javaClass != other?.javaClass) return false

            other as Success

            if (documentId != other.documentId) return false
            if (proofOfProvisioning != null) {
                if (other.proofOfProvisioning == null) return false
                if (!proofOfProvisioning.contentEquals(other.proofOfProvisioning)) return false
            } else if (other.proofOfProvisioning != null) return false

            return true
        }

        override fun hashCode(): Int {
            var result = documentId.hashCode()
            result = 31 * result + (proofOfProvisioning?.contentHashCode() ?: 0)
            return result
        }
    }

    /**
     * Failure while adding the document. Contains the throwable that caused the failure
     *
     * @property throwable throwable that caused the failure
     * @constructor
     * @param throwable throwable that caused the failure
     */
    data class Failure(val throwable: Throwable) : StoreDocumentResult

    /**
     * Success result containing the documentId and the proof of provisioning if successful
     *
     * @param block block to be executed if the result is successful
     * @return [StoreDocumentResult]
     */
    fun onSuccess(block: (DocumentId, ByteArray?) -> Unit): StoreDocumentResult = apply {
        if (this is Success) block(documentId, proofOfProvisioning)
    }

    /**
     * Failure while adding the document. Contains the throwable that caused the failure
     *
     * @param block block to be executed if the result is a failure
     * @return [StoreDocumentResult]
     */
    fun onFailure(block: (Throwable) -> Unit): StoreDocumentResult = apply {
        if (this is Failure) block(throwable)
    }
}