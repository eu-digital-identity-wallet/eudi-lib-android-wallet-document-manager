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

import com.android.identity.credential.Credential
import eu.europa.ec.eudi.wallet.document.Document.State.*
import eu.europa.ec.eudi.wallet.document.internal.state
import java.time.Instant


typealias DocumentId = String
typealias NameSpace = String
typealias ElementIdentifier = String

/**
 * A document.
 * @property id the identifier of the document
 * @property docType the document type
 * @property name the name of the document
 * @property usesStrongBox whether the document's keys are in strongBox
 * @property requiresUserAuth whether the document requires user authentication
 * @property createdAt the creation date of the document
 * @property state the state of the document
 * @property isUnsigned whether the document is unsigned
 * @property isDeferred whether the document is deferred
 * @property isIssued whether the document is issued
 */
sealed interface Document {
    val id: DocumentId
    val docType: String
    val name: String
    val usesStrongBox: Boolean
    val requiresUserAuth: Boolean
    val createdAt: Instant
    val state: State

    val isUnsigned: Boolean
        get() = state == UNSIGNED

    val isDeferred: Boolean
        get() = state == DEFERRED

    val isIssued: Boolean
        get() = state == ISSUED

    /**
     * The state of the document.
     * @property UNSIGNED the document is unsigned
     * @property ISSUED the document is issued
     * @property DEFERRED the document is deferred
     */
    enum class State {
        UNSIGNED, ISSUED, DEFERRED;

        val value
            get() = ordinal.toLong()
    }

    companion object {

        internal operator fun invoke(credential: Credential) = when (credential.state) {
            UNSIGNED -> UnsignedDocument(credential)
            DEFERRED -> DeferredDocument(credential)
            ISSUED -> IssuedDocument(credential)
        }
    }
}