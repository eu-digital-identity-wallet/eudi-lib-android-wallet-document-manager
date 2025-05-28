/*
 * Copyright (c) 2024-2025 European Commission
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
 * Interface that defines the required creationSettings when creating a document with
 * [DocumentManager.createDocument]. Implementors of [DocumentManager] may
 * introduce custom requirements for creating a document.
 *
 * @see [CreateDocumentSettingsImpl] implementation
 */
interface CreateDocumentSettings {

    /**
     * Identifier for the secure area where document keys will be stored.
     * This should match an existing secure area in the system.
     */
    val secureAreaIdentifier: String

    /**
     * Configuration settings for key creation within the secure area.
     * These settings define properties such as key algorithms, key sizes,
     * and any other parameters required by the underlying secure area implementation.
     */
    val createKeySettings: CreateKeySettings

    /**
     * Specifies the number of credentials to create for this document.
     * Multiple credentials can be used for load balancing or redundancy purposes.
     * Must be greater than 0.
     */
    val numberOfCredentials: Int

    /**
     * Defines the policy for credential usage and lifecycle management.
     * Controls whether credentials are used once and deleted or rotated through multiple uses.
     *
     * @see CredentialPolicy
     */
    val credentialPolicy: CredentialPolicy

    companion object {
        /**
         * Create a new instance of [CreateDocumentSettings] for [DocumentManagerImpl.createDocument]
         * that uses the [org.multipaz.securearea.SecureArea].
         *
         * @param secureAreaIdentifier The identifier from [org.multipaz.securearea.SecureArea]
         * where the document's keys should be stored
         * @param createKeySettings The [CreateKeySettings] implementation that accompanies the provided
         * [org.multipaz.securearea.SecureArea]
         * @param numberOfCredentials The number of credentials to create for this document.
         * Must be greater than 0. Defaults to 1 if not specified.
         * @param credentialPolicy The policy determining how credentials are managed after use.
         * Defaults to [CredentialPolicy.RotateUse] if not specified.
         * @return A new instance of [CreateDocumentSettings]
         * @throws IllegalArgumentException if numberOfCredentials is not greater than 0
         */
        operator fun invoke(
            secureAreaIdentifier: String,
            createKeySettings: CreateKeySettings,
            numberOfCredentials: Int = 1,
            credentialPolicy: CredentialPolicy = CredentialPolicy.RotateUse
        ): CreateDocumentSettings {
            require(numberOfCredentials > 0) {
                "Number of credentials must be greater than 0"
            }
            return CreateDocumentSettingsImpl(
                secureAreaIdentifier = secureAreaIdentifier,
                createKeySettings = createKeySettings,
                numberOfCredentials = numberOfCredentials,
                credentialPolicy = credentialPolicy,
            )
        }
    }

    sealed interface CredentialPolicy {
        /**
         * Policy that deletes the credential after a single use.
         *
         * This policy ensures credentials cannot be reused, providing enhanced security
         * by minimizing the timeframe during which credentials are available in the system.
         * It's particularly suitable for high-security scenarios.
         *
         * @see RotateUse for an alternative policy that allows credential reuse
         */
        data object OneTimeUse : CredentialPolicy

        /**
         * Policy that manages credential rotation by tracking usage count.
         *
         * When a credential is used, its usage count is incremented, allowing the system
         * to distribute load across multiple available credentials. This approach balances
         * security with performance considerations by enabling credential reuse while
         * maintaining usage patterns for auditing and optimization purposes.
         *
         * Appropriate for scenarios requiring frequent authentication where the
         * performance overhead of continuous credential generation would be prohibitive.
         *
         * @see OneTimeUse for a stricter security policy
         */
        data object RotateUse : CredentialPolicy

        companion object
    }
}