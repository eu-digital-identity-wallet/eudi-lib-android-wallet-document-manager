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
 * The result of [DocumentManager.deleteDocumentById] method
 */
sealed interface DeleteDocumentResult {
    /**
     * Success result containing the proof of deletion
     *
     * @property proofOfDeletion
     * @constructor
     * @param proofOfDeletion
     */
    data class Success(val proofOfDeletion: ByteArray?) : DeleteDocumentResult {
        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (javaClass != other?.javaClass) return false

            other as Success

            if (proofOfDeletion != null) {
                if (other.proofOfDeletion == null) return false
                if (!proofOfDeletion.contentEquals(other.proofOfDeletion)) return false
            } else if (other.proofOfDeletion != null) return false

            return true
        }

        override fun hashCode(): Int {
            return proofOfDeletion?.contentHashCode() ?: 0
        }
    }

    /**
     * Failure while deleting the document. Contains the throwable that caused the failure
     *
     * @property throwable throwable that caused the failure
     * @constructor
     * @param throwable throwable that caused the failure
     */
    data class Failure(val throwable: Throwable) : DeleteDocumentResult

    /**
     * Execute block if the result is successful
     *
     * @param block
     * @return [DeleteDocumentResult]
     */
    fun onSuccess(block: (ByteArray?) -> Unit): DeleteDocumentResult = apply {
        if (this is Success) block(proofOfDeletion)
    }

    /**
     * Execute block if the result is a failure
     *
     * @param block
     * @return [DeleteDocumentResult]
     */
    fun onFailure(block: (Throwable) -> Unit): DeleteDocumentResult = apply {
        if (this is Failure) block(throwable)
    }
}