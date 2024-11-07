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

import kotlin.test.Test
import kotlin.test.assertContentEquals
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertIs

class DocumentManagerBuilderTest {

    @Test
    fun `build should throw exception when identifier is not set`() {
        // Given
        val builder = DocumentManager.Builder()
            .addSecureArea(secureArea)
            .setStorageEngine(storageEngine)

        // When
        val exception = assertFailsWith<IllegalArgumentException> {
            builder.build()
        }

        // Then
        assertEquals("Identifier is required", exception.message)
    }

    @Test
    fun `build should throw exception when storageEngine is not set`() {
        // Given
        val builder = DocumentManager.Builder()
            .setIdentifier("document_manager")
            .addSecureArea(secureArea)

        // When
        val exception = assertFailsWith<IllegalArgumentException> {
            builder.build()
        }

        // Then
        assertEquals("Storage engine is required", exception.message)
    }

    @Test
    fun `build should throw exception when secureAreaRepository is empty`() {
        // Given
        val builder = DocumentManager.Builder()
            .setIdentifier("document_manager")
            .setStorageEngine(storageEngine)

        // When
        val exception = assertFailsWith<IllegalArgumentException> {
            builder.build()
        }

        // Then
        assertEquals("SecureAreaRepository is empty", exception.message)
    }

    @Test
    fun `build should return DocumentManagerImpl with the provided storageEngine and secureArea as dependencies`() {
        val builder = DocumentManager.Builder()
            .setIdentifier("document_manager")
            .setStorageEngine(storageEngine)
            .addSecureArea(secureArea)

        val documentManager = builder.build()

        assertIs<DocumentManagerImpl>(documentManager)
        assertEquals("document_manager", documentManager.identifier)
        assertEquals(storageEngine, documentManager.storageEngine)
        assertContentEquals(
            listOf(secureArea),
            documentManager.secureAreaRepository.implementations
        )
    }

    @Test
    fun `verify that companion object operator invoke returns DocumentManagerImpl instance`() {
        // When
        val documentManager = DocumentManager {
            setIdentifier("document_manager")
            setStorageEngine(eu.europa.ec.eudi.wallet.document.storageEngine)
            addSecureArea(eu.europa.ec.eudi.wallet.document.secureArea)
        }

        // Then
        assertIs<DocumentManagerImpl>(documentManager)
        assertEquals("document_manager", documentManager.identifier)
        assertEquals(storageEngine, documentManager.storageEngine)
        assertContentEquals(
            listOf(secureArea),
            documentManager.secureAreaRepository.implementations
        )
    }

    @Test
    fun `verify that setSecureAreaRepository method overrides the default`() {
    }
}