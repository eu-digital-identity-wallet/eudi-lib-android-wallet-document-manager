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
import eu.europa.ec.eudi.wallet.document.internal.*
import java.time.Instant

/**
 * An implementation of [Document].
 * @property credential the credential
 * @constructor Creates a document.
 * @param credential the credential
 */
internal class DocumentImpl(private val credential: Credential) : Document {
    override val id: DocumentId
        get() = credential.name
    override val docType: String
        get() = credential.docType
    override val name: String
        get() = credential.documentName
    override val usesStrongBox: Boolean
        get() = credential.usesStrongBox
    override val requiresUserAuth: Boolean
        get() = credential.requiresUserAuth
    override val createdAt: Instant
        get() = credential.createdAt
    override val state: Document.State
        get() = credential.state

}