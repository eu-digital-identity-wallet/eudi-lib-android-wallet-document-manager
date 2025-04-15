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

package eu.europa.ec.eudi.wallet.document

import org.multipaz.securearea.CreateKeySettings

/**
 * Interface that defines the required settings when creating a document with
 * [DocumentManager.createDocument]. Implementors of [DocumentManager] may
 * introduce custom requirements for creating a document.
 *
 * @see [CreateDocumentSettingsImpl] implementation
 */
interface CreateDocumentSettings {

    val secureAreaIdentifier: String
    val createKeySettings: CreateKeySettings

    companion object {
        /**
         * Create a new instance of [CreateDocumentSettings] for [DocumentManagerImpl.createDocument]
         * that uses the [org.multipaz.securearea.SecureArea].
         *
         * @param secureAreaIdentifier the identifier from [org.multipaz.securearea.SecureArea]
         * where the document's keys should be stored
         * @param createKeySettings the [CreateKeySettings] implementation that accompanies the provided
         * [org.multipaz.securearea.SecureArea]
         * @return a new instance of [CreateDocumentSettings]
         */
        operator fun invoke(
            secureAreaIdentifier: String,
            createKeySettings: CreateKeySettings,
        ): CreateDocumentSettings = CreateDocumentSettingsImpl(
            secureAreaIdentifier = secureAreaIdentifier,
            createKeySettings = createKeySettings,
        )
    }
}