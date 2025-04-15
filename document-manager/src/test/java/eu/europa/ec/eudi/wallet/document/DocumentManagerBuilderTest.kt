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

import kotlinx.coroutines.runBlocking
import org.multipaz.securearea.SecureAreaRepository
import org.multipaz.securearea.software.SoftwareSecureArea
import org.multipaz.storage.ephemeral.EphemeralStorage
import kotlin.test.Ignore
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertIs
import kotlin.test.assertSame

class DocumentManagerBuilderTest {

    val storage = EphemeralStorage()
    val secureArea = runBlocking { SoftwareSecureArea.create(storage) }
    val secureAreaRepository = SecureAreaRepository.build {
        add(secureArea)
    }

    @Test
    fun `build should throw exception when identifier is not set`() {
        // Given
        val builder = DocumentManager.Builder()
            .setStorage(storage)
            .setSecureAreaRepository(secureAreaRepository)

        // When
        val exception = assertFailsWith<IllegalArgumentException> {
            builder.build()
        }

        // Then
        assertEquals("Identifier is required", exception.message)
    }

    @Test
    fun `build should throw exception when storage is not set`() {
        // Given
        val builder = DocumentManager.Builder()
            .setIdentifier("document_manager")
            .setSecureAreaRepository(secureAreaRepository)

        // When
        val exception = assertFailsWith<IllegalArgumentException> {
            builder.build()
        }

        // Then
        assertEquals("Storage is required", exception.message)
    }

    @Test
    fun `build should throw exception when secureAreaRepository is not set`() {
        // Given
        val builder = DocumentManager.Builder()
            .setIdentifier("document_manager")
            .setStorage(storage)

        // When
        val exception = assertFailsWith<IllegalArgumentException> {
            builder.build()
        }

        // Then
        assertEquals("SecureAreaRepository is required", exception.message)
    }

    @Test
    fun `build should return DocumentManagerImpl with the provided storageEngine and secureArea as dependencies`() {
        val builder = DocumentManager.Builder()
            .setIdentifier("document_manager")
            .setStorage(storage)
            .setSecureAreaRepository(secureAreaRepository)

        val documentManager = builder.build()

        assertIs<DocumentManagerImpl>(documentManager)
        assertEquals("document_manager", documentManager.identifier)
        assertEquals(storage, documentManager.storage)
        assertEquals(secureArea, runBlocking { documentManager.secureAreaRepository.getImplementation(secureArea.identifier)})
    }

    @Test
    fun `verify that companion object operator invoke returns DocumentManagerImpl instance`() {
        // When
        val documentManager = DocumentManager {
            setIdentifier("document_manager")
            setStorage(this@DocumentManagerBuilderTest.storage)
            setSecureAreaRepository(SecureAreaRepository.build {
                add(secureArea)
            })
        }

        // Then
        assertIs<DocumentManagerImpl>(documentManager)
        assertEquals("document_manager", documentManager.identifier)
        assertEquals(storage, documentManager.storage)
        assertEquals(secureArea, runBlocking { documentManager.secureAreaRepository.getImplementation(secureArea.identifier) })
    }

    @Test
    fun `verify that setSecureAreaRepository method overrides the default`() {
        val builder = DocumentManager.Builder()
            .setIdentifier("document_manager")
            .setStorage(storage)
            .setSecureAreaRepository(secureAreaRepository)

        builder.setSecureAreaRepository(secureAreaRepository)

        val documentManager = builder.build() as DocumentManagerImpl

        assertSame(secureAreaRepository, documentManager.secureAreaRepository)
    }
}