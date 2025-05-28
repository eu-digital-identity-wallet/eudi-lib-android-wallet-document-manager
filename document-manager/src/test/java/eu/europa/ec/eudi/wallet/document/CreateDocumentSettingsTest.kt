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

import io.mockk.mockk
import org.multipaz.securearea.CreateKeySettings
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertIs
import kotlin.test.assertSame

class CreateDocumentSettingsTest {

    private val secureAreaIdentifier = "secureAreaIdentifier"
    private val createKeySettings = mockk<CreateKeySettings>()

    @Test
    fun `test operator invoke returns an instance of CreateDocumentSettingsImpl`() {
        val createDocumentSettings = CreateDocumentSettings(
            secureAreaIdentifier = secureAreaIdentifier,
            createKeySettings = createKeySettings,
            numberOfCredentials = 1
        )
        assertIs<CreateDocumentSettingsImpl>(createDocumentSettings)
    }

    @Test
    fun `test all parameters are properly set`() {
        val numberOfCredentials = 3
        val credentialPolicy = CreateDocumentSettings.CredentialPolicy.OneTimeUse
        val createDocumentSettings = CreateDocumentSettings(
            secureAreaIdentifier = secureAreaIdentifier,
            createKeySettings = createKeySettings,
            numberOfCredentials = numberOfCredentials,
            credentialPolicy = credentialPolicy
        )

        assertEquals(secureAreaIdentifier, createDocumentSettings.secureAreaIdentifier)
        assertSame(createKeySettings, createDocumentSettings.createKeySettings)
        assertEquals(numberOfCredentials, createDocumentSettings.numberOfCredentials)
        assertSame(credentialPolicy, createDocumentSettings.credentialPolicy)
    }

    @Test
    fun `test default values are applied when not specified`() {
        val createDocumentSettings = CreateDocumentSettings(
            secureAreaIdentifier = secureAreaIdentifier,
            createKeySettings = createKeySettings
        )

        assertEquals(
            1,
            createDocumentSettings.numberOfCredentials,
            "Default numberOfCredentials should be 1"
        )
        assertIs<CreateDocumentSettings.CredentialPolicy.RotateUse>(
            createDocumentSettings.credentialPolicy,
            "Default credentialPolicy should be RotateUse"
        )
    }

    @Test
    fun `test throws IllegalArgumentException when numberOfCredentials is zero`() {
        val exception = assertFailsWith<IllegalArgumentException> {
            CreateDocumentSettings(
                secureAreaIdentifier = secureAreaIdentifier,
                createKeySettings = createKeySettings,
                numberOfCredentials = 0
            )
        }

        assertEquals("Number of credentials must be greater than 0", exception.message)
    }

    @Test
    fun `test throws IllegalArgumentException when numberOfCredentials is negative`() {
        val exception = assertFailsWith<IllegalArgumentException> {
            CreateDocumentSettings(
                secureAreaIdentifier = secureAreaIdentifier,
                createKeySettings = createKeySettings,
                numberOfCredentials = -1
            )
        }

        assertEquals("Number of credentials must be greater than 0", exception.message)
    }
}

