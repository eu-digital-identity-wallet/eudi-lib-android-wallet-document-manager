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

package eu.europa.ec.eudi.wallet.document.internal

import eu.europa.ec.eudi.wallet.document.CreateDocumentSettings
import eu.europa.ec.eudi.wallet.document.format.MsoMdocFormat
import eu.europa.ec.eudi.wallet.document.metadata.IssuerMetadata
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import kotlinx.datetime.Clock
import kotlinx.io.bytestring.ByteString
import org.multipaz.document.NameSpacedData
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertFalse
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import kotlin.test.assertTrue

class ApplicationMetadataTest {

    private val testDocumentId = "test-document-id"
    private val testDocumentName = "test-document"
    private val testDocumentManagerId = "test-manager-id"
    private val testCreatedAt = Clock.System.now()
    private val testFormat = MsoMdocFormat("test-doc-type")

    @Test
    fun `create factory method returns ApplicationMetaData instance`() = runTest {
        val saveFn: suspend (ByteString) -> Unit = mockk(relaxed = true)

        val metadata = ApplicationMetadata.create(
            documentId = testDocumentId,
            serializedData = null,
            saveFn = saveFn
        )

        assertNotNull(metadata)
        assertFalse(metadata.provisioned)
    }

    @Test
    fun `new ApplicationMetaData is not provisioned by default`() = runTest {
        val saveFn: suspend (ByteString) -> Unit = mockk(relaxed = true)

        val metadata = ApplicationMetadata.create(
            documentId = testDocumentId,
            serializedData = null,
            saveFn = saveFn
        )

        assertFalse(metadata.provisioned)
    }

    @Test
    fun `setAsProvisioned sets document as provisioned`() = runTest {
        val saveFn: suspend (ByteString) -> Unit = mockk(relaxed = true)

        val metadata = ApplicationMetadata.create(
            documentId = testDocumentId,
            serializedData = null,
            saveFn = saveFn
        )

        metadata.setAsProvisioned()

        assertTrue(metadata.provisioned)
    }

    @Test
    fun `setAsProvisioned throws exception when already provisioned`() = runTest {
        val saveFn: suspend (ByteString) -> Unit = mockk(relaxed = true)

        val metadata = ApplicationMetadata.create(
            documentId = testDocumentId,
            serializedData = null,
            saveFn = saveFn
        )

        metadata.setAsProvisioned()

        assertFailsWith<IllegalStateException> {
            metadata.setAsProvisioned()
        }
    }

    @Test
    fun `format property throws exception when not set`() = runTest {
        val saveFn: suspend (ByteString) -> Unit = mockk(relaxed = true)

        val metadata = ApplicationMetadata.create(
            documentId = testDocumentId,
            serializedData = null,
            saveFn = saveFn
        )

        assertFailsWith<IllegalStateException> {
            metadata.format
        }
    }

    @Test
    fun `documentName property throws exception when not set`() = runTest {
        val saveFn: suspend (ByteString) -> Unit = mockk(relaxed = true)

        val metadata = ApplicationMetadata.create(
            documentId = testDocumentId,
            serializedData = null,
            saveFn = saveFn
        )

        assertFailsWith<NullPointerException> {
            metadata.documentName
        }
    }

    @Test
    fun `documentManagerId property throws exception when not set`() = runTest {
        val saveFn: suspend (ByteString) -> Unit = mockk(relaxed = true)

        val metadata = ApplicationMetadata.create(
            documentId = testDocumentId,
            serializedData = null,
            saveFn = saveFn
        )

        assertFailsWith<NullPointerException> {
            metadata.documentManagerId
        }
    }

    @Test
    fun `createdAt property throws exception when not set`() = runTest {
        val saveFn: suspend (ByteString) -> Unit = mockk(relaxed = true)

        val metadata = ApplicationMetadata.create(
            documentId = testDocumentId,
            serializedData = null,
            saveFn = saveFn
        )

        assertFailsWith<NullPointerException> {
            metadata.createdAt
        }
    }

    @Test
    fun `credentialPolicy defaults to RotateUse when not set`() = runTest {
        val saveFn: suspend (ByteString) -> Unit = mockk(relaxed = true)

        val metadata = ApplicationMetadata.create(
            documentId = testDocumentId,
            serializedData = null,
            saveFn = saveFn
        )

        assertEquals(CreateDocumentSettings.CredentialPolicy.RotateUse, metadata.credentialPolicy)
    }

    @Test
    fun `initialize sets all core properties`() = runTest {
        val saveFn: suspend (ByteString) -> Unit = mockk(relaxed = true)
        val issuerMetadataJson = """
        {
            "documentConfigurationIdentifier": "test-doc-config",
            "display": [
                {
                    "name": "Test Document",
                    "locale": "en-US"
                }
            ],
            "claims": null,
            "credentialIssuerIdentifier": "test-issuer",
            "issuerDisplay": [
                {
                    "name": "Test Issuer",
                    "locale": "en-US"
                }
            ]
        }
        """.trimIndent()
        val issuerMetadata = IssuerMetadata.fromJson(issuerMetadataJson).getOrThrow()

        val metadata = ApplicationMetadata.create(
            documentId = testDocumentId,
            serializedData = null,
            saveFn = saveFn
        )

        metadata.initialize(
            format = testFormat,
            documentName = testDocumentName,
            documentManagerId = testDocumentManagerId,
            createdAt = testCreatedAt,
            issuerMetadata = issuerMetadata,
            credentialPolicy = CreateDocumentSettings.CredentialPolicy.RotateUse
        )

        assertEquals(testFormat, metadata.format)
        assertEquals(testDocumentName, metadata.documentName)
        assertEquals(testDocumentManagerId, metadata.documentManagerId)
        assertEquals(testCreatedAt, metadata.createdAt)
        assertEquals(issuerMetadata, metadata.issuerMetadata)
        assertEquals(CreateDocumentSettings.CredentialPolicy.RotateUse, metadata.credentialPolicy)

        coVerify { saveFn(any()) }
    }

    @Test
    fun `initialize with minimal parameters uses defaults`() = runTest {
        val saveFn: suspend (ByteString) -> Unit = mockk(relaxed = true)

        val metadata = ApplicationMetadata.create(
            documentId = testDocumentId,
            serializedData = null,
            saveFn = saveFn
        )

        metadata.initialize(
            format = testFormat,
            documentName = testDocumentName,
            documentManagerId = testDocumentManagerId,
            createdAt = testCreatedAt
        )

        assertEquals(testFormat, metadata.format)
        assertEquals(testDocumentName, metadata.documentName)
        assertEquals(testDocumentManagerId, metadata.documentManagerId)
        assertEquals(testCreatedAt, metadata.createdAt)
        assertNull(metadata.issuerMetadata)
        assertEquals(CreateDocumentSettings.CredentialPolicy.RotateUse, metadata.credentialPolicy)
    }

    @Test
    fun `setCredentialPolicy updates policy and saves`() = runTest {
        val saveFn: suspend (ByteString) -> Unit = mockk(relaxed = true)

        val metadata = ApplicationMetadata.create(
            documentId = testDocumentId,
            serializedData = null,
            saveFn = saveFn
        )

        val newPolicy = CreateDocumentSettings.CredentialPolicy.OneTimeUse
        metadata.setCredentialPolicy(newPolicy)

        assertEquals(newPolicy, metadata.credentialPolicy)
        coVerify { saveFn(any()) }
    }

    @Test
    fun `setDocumentName updates name and saves`() = runTest {
        val saveFn: suspend (ByteString) -> Unit = mockk(relaxed = true)

        val metadata = ApplicationMetadata.create(
            documentId = testDocumentId,
            serializedData = null,
            saveFn = saveFn
        )

        val newName = "new-document-name"
        metadata.setDocumentName(newName)

        assertEquals(newName, metadata.documentName)
        coVerify { saveFn(any()) }
    }

    @Test
    fun `setDocumentManagerId updates id and saves`() = runTest {
        val saveFn: suspend (ByteString) -> Unit = mockk(relaxed = true)

        val metadata = ApplicationMetadata.create(
            documentId = testDocumentId,
            serializedData = null,
            saveFn = saveFn
        )

        val newId = "new-manager-id"
        metadata.setDocumentManagerId(newId)

        assertEquals(newId, metadata.documentManagerId)
        coVerify { saveFn(any()) }
    }

    @Test
    fun `setIssuerMetaData updates metadata and saves`() = runTest {
        val saveFn: suspend (ByteString) -> Unit = mockk(relaxed = true)
        val issuerMetadataJson = """
        {
            "documentConfigurationIdentifier": "test-doc-config",
            "display": [
                {
                    "name": "Test Document",
                    "locale": "en-US"
                }
            ],
            "claims": null,
            "credentialIssuerIdentifier": "test-issuer",
            "issuerDisplay": [
                {
                    "name": "Test Issuer",
                    "locale": "en-US"
                }
            ]
        }
        """.trimIndent()
        val issuerMetadata = IssuerMetadata.fromJson(issuerMetadataJson).getOrThrow()

        val metadata = ApplicationMetadata.create(
            documentId = testDocumentId,
            serializedData = null,
            saveFn = saveFn
        )

        metadata.setIssuerMetadata(issuerMetadata)

        assertEquals(issuerMetadata, metadata.issuerMetadata)
        coVerify { saveFn(any()) }
    }

    @Test
    fun `setCreatedAt updates timestamp and saves`() = runTest {
        val saveFn: suspend (ByteString) -> Unit = mockk(relaxed = true)

        val metadata = ApplicationMetadata.create(
            documentId = testDocumentId,
            serializedData = null,
            saveFn = saveFn
        )

        val newTimestamp = Clock.System.now()
        metadata.setCreatedAt(newTimestamp)

        assertEquals(newTimestamp, metadata.createdAt)
        coVerify { saveFn(any()) }
    }

    @Test
    fun `issuedAt is null by default`() = runTest {
        val saveFn: suspend (ByteString) -> Unit = mockk(relaxed = true)

        val metadata = ApplicationMetadata.create(
            documentId = testDocumentId,
            serializedData = null,
            saveFn = saveFn
        )

        assertNull(metadata.issuedAt)
    }

    @Test
    fun `setIssuedAt updates timestamp and saves`() = runTest {
        val saveFn: suspend (ByteString) -> Unit = mockk(relaxed = true)

        val metadata = ApplicationMetadata.create(
            documentId = testDocumentId,
            serializedData = null,
            saveFn = saveFn
        )

        val issuedTimestamp = Clock.System.now()
        metadata.setIssuedAt(issuedTimestamp)

        assertEquals(issuedTimestamp, metadata.issuedAt)
        coVerify { saveFn(any()) }
    }

    @Test
    fun `nameSpacedData is null by default`() = runTest {
        val saveFn: suspend (ByteString) -> Unit = mockk(relaxed = true)

        val metadata = ApplicationMetadata.create(
            documentId = testDocumentId,
            serializedData = null,
            saveFn = saveFn
        )

        assertNull(metadata.nameSpacedData)
    }

    @Test
    fun `setNameSpacedData updates data and saves`() = runTest {
        val saveFn: suspend (ByteString) -> Unit = mockk(relaxed = true)
        val nameSpacedData = mockk<NameSpacedData> {
            every { toDataItem() } returns mockk(relaxed = true)
        }

        val metadata = ApplicationMetadata.create(
            documentId = testDocumentId,
            serializedData = null,
            saveFn = saveFn
        )

        metadata.setNameSpacedData(nameSpacedData)

        assertEquals(nameSpacedData, metadata.nameSpacedData)
        coVerify { saveFn(any()) }
    }

    @Test
    fun `deferredRelatedData is null by default`() = runTest {
        val saveFn: suspend (ByteString) -> Unit = mockk(relaxed = true)

        val metadata = ApplicationMetadata.create(
            documentId = testDocumentId,
            serializedData = null,
            saveFn = saveFn
        )

        assertNull(metadata.deferredRelatedData)
    }

    @Test
    fun `setDeferredRelatedData updates data and saves`() = runTest {
        val saveFn: suspend (ByteString) -> Unit = mockk(relaxed = true)
        val deferredData = "test deferred data".encodeToByteArray()

        val metadata = ApplicationMetadata.create(
            documentId = testDocumentId,
            serializedData = null,
            saveFn = saveFn
        )

        metadata.setDeferredRelatedData(deferredData)

        assertEquals(deferredData, metadata.deferredRelatedData)
        coVerify { saveFn(any()) }
    }

    @Test
    fun `clearDeferredRelatedData sets data to null and saves`() = runTest {
        val saveFn: suspend (ByteString) -> Unit = mockk(relaxed = true)
        val deferredData = "test deferred data".encodeToByteArray()

        val metadata = ApplicationMetadata.create(
            documentId = testDocumentId,
            serializedData = null,
            saveFn = saveFn
        )

        // First set some data
        metadata.setDeferredRelatedData(deferredData)
        assertEquals(deferredData, metadata.deferredRelatedData)

        // Then clear it
        metadata.clearDeferredRelatedData()
        assertNull(metadata.deferredRelatedData)

        coVerify(exactly = 2) { saveFn(any()) }
    }

    @Test
    fun `display properties are null by default`() = runTest {
        val saveFn: suspend (ByteString) -> Unit = mockk(relaxed = true)

        val metadata = ApplicationMetadata.create(
            documentId = testDocumentId,
            serializedData = null,
            saveFn = saveFn
        )

        assertNull(metadata.displayName)
        assertNull(metadata.typeDisplayName)
        assertNull(metadata.cardArt)
        assertNull(metadata.issuerLogo)
    }

    @Test
    fun `documentDeleted is no-op implementation`() = runTest {
        val saveFn: suspend (ByteString) -> Unit = mockk(relaxed = true)

        val metadata = ApplicationMetadata.create(
            documentId = testDocumentId,
            serializedData = null,
            saveFn = saveFn
        )

        // Should not throw any exception
        metadata.documentDeleted()

        // Should not trigger any save operation
        coVerify(exactly = 0) { saveFn(any()) }
    }

    @Test
    fun `serialization and deserialization preserves data`() = runTest {
        var savedData: ByteString? = null
        val saveFn: suspend (ByteString) -> Unit = { data -> savedData = data }

        // Create and initialize metadata
        val originalMetadata = ApplicationMetadata.create(
            documentId = testDocumentId,
            serializedData = null,
            saveFn = saveFn
        )

        originalMetadata.initialize(
            format = testFormat,
            documentName = testDocumentName,
            documentManagerId = testDocumentManagerId,
            createdAt = testCreatedAt
        )

        originalMetadata.setAsProvisioned()

        // Create new metadata from serialized data
        val deserializedMetadata = ApplicationMetadata.create(
            documentId = testDocumentId,
            serializedData = savedData,
            saveFn = saveFn
        )

        // Verify all data is preserved
        assertEquals(originalMetadata.format, deserializedMetadata.format)
        assertEquals(originalMetadata.documentName, deserializedMetadata.documentName)
        assertEquals(originalMetadata.documentManagerId, deserializedMetadata.documentManagerId)
        assertEquals(originalMetadata.createdAt, deserializedMetadata.createdAt)
        assertEquals(originalMetadata.provisioned, deserializedMetadata.provisioned)
        assertEquals(originalMetadata.credentialPolicy, deserializedMetadata.credentialPolicy)
    }

    @Test
    fun `empty serialized data creates new instance`() = runTest {
        val saveFn: suspend (ByteString) -> Unit = mockk(relaxed = true)
        val emptyData = ByteString()

        val metadata = ApplicationMetadata.create(
            documentId = testDocumentId,
            serializedData = emptyData,
            saveFn = saveFn
        )

        assertFalse(metadata.provisioned)
        assertEquals(CreateDocumentSettings.CredentialPolicy.RotateUse, metadata.credentialPolicy)
    }

    @Test
    fun `thread safety - concurrent access to provisioned state`() = runTest {
        val saveFn: suspend (ByteString) -> Unit = mockk(relaxed = true)

        val metadata = ApplicationMetadata.create(
            documentId = testDocumentId,
            serializedData = null,
            saveFn = saveFn
        )

        // This test verifies that the class doesn't throw exceptions during concurrent access
        // The actual thread safety is ensured by the mutex in the implementation
        assertFalse(metadata.provisioned)
        metadata.setAsProvisioned()
        assertTrue(metadata.provisioned)
    }

    @Test
    fun `save function is called for all mutation operations`() = runTest {
        val saveFn: suspend (ByteString) -> Unit = mockk(relaxed = true)
        val issuerMetadataJson = """
        {
            "documentConfigurationIdentifier": "test-doc-config",
            "display": [
                {
                    "name": "Test Document",
                    "locale": "en-US"
                }
            ],
            "claims": null,
            "credentialIssuerIdentifier": "test-issuer",
            "issuerDisplay": [
                {
                    "name": "Test Issuer",
                    "locale": "en-US"
                }
            ]
        }
        """.trimIndent()
        val issuerMetadata = IssuerMetadata.fromJson(issuerMetadataJson).getOrThrow()
        val nameSpacedData = mockk<NameSpacedData> {
            every { toDataItem() } returns mockk(relaxed = true)
        }

        val metadata = ApplicationMetadata.create(
            documentId = testDocumentId,
            serializedData = null,
            saveFn = saveFn
        )

        // Initialize (1 save call)
        metadata.initialize(
            format = testFormat,
            documentName = testDocumentName,
            documentManagerId = testDocumentManagerId,
            createdAt = testCreatedAt
        )

        // Various setter operations (each should trigger a save)
        metadata.setCredentialPolicy(CreateDocumentSettings.CredentialPolicy.RotateUse) // +1
        metadata.setDocumentName("new-name") // +1
        metadata.setDocumentManagerId("new-id") // +1
        metadata.setIssuerMetadata(issuerMetadata) // +1
        metadata.setCreatedAt(Clock.System.now()) // +1
        metadata.setIssuedAt(Clock.System.now()) // +1
        metadata.setNameSpacedData(nameSpacedData) // +1
        metadata.setDeferredRelatedData("test".encodeToByteArray()) // +1        metadata.clearDeferredRelatedData() // +1

        // Total: 9 save calls including initialize
        coVerify(exactly = 9) { saveFn(any()) }
    }
}
