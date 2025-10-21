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
import eu.europa.ec.eudi.wallet.document.format.SdJwtVcFormat
import eu.europa.ec.eudi.wallet.document.metadata.IssuerMetadata
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.spyk
import kotlinx.coroutines.test.runTest
import kotlinx.io.bytestring.ByteString
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertFalse
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import kotlin.test.assertTrue
import kotlin.time.Clock

class ApplicationMetadataTest {

    private val testDocumentId = "test-document-id"
    private val testDocumentName = "test-document"
    private val testDocumentManagerId = "test-manager-id"
    private val testCreatedAt = Clock.System.now()
    private val testFormat = MsoMdocFormat("test-doc-type")
    private val testSdJwtVcFormat = SdJwtVcFormat("test-vct")
    private val testKeyAttestation = "test-key-attestation"

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

        metadata.markAsProvisioned()

        assertTrue(metadata.provisioned)
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

        assertFailsWith<IllegalStateException> {
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

        assertFailsWith<IllegalStateException> {
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

        assertFailsWith<IllegalStateException> {
            metadata.createdAt
        }
    }

    @Test
    fun `initialCredentialsCount property throws exception when not set`() = runTest {
        val saveFn: suspend (ByteString) -> Unit = mockk(relaxed = true)

        val metadata = ApplicationMetadata.create(
            documentId = testDocumentId,
            serializedData = null,
            saveFn = saveFn
        )

        assertFailsWith<IllegalStateException> {
            metadata.initialCredentialsCount
        }
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
            initialCredentialsCount = 2,
            credentialPolicy = CreateDocumentSettings.CredentialPolicy.RotateUse,
            keyAttestation = testKeyAttestation
        )

        assertEquals(testFormat, metadata.format)
        assertEquals(testDocumentName, metadata.documentName)
        assertEquals(testDocumentManagerId, metadata.documentManagerId)
        assertEquals(testCreatedAt, metadata.createdAt)
        assertEquals(issuerMetadata, metadata.issuerMetadata)
        assertEquals(2, metadata.initialCredentialsCount)
        assertEquals(CreateDocumentSettings.CredentialPolicy.RotateUse, metadata.credentialPolicy)

        coVerify { saveFn(any()) }
    }

    @Test
    fun `initialize with SdJwtVcFormat sets correct format`() = runTest {
        val saveFn: suspend (ByteString) -> Unit = mockk(relaxed = true)

        val metadata = ApplicationMetadata.create(
            documentId = testDocumentId,
            serializedData = null,
            saveFn = saveFn
        )

        metadata.initialize(
            format = testSdJwtVcFormat,
            documentName = testDocumentName,
            documentManagerId = testDocumentManagerId,
            createdAt = testCreatedAt,
            initialCredentialsCount = 1,
            credentialPolicy = CreateDocumentSettings.CredentialPolicy.RotateUse,
            issuerMetadata = null,
            keyAttestation = null
        )

        assertEquals(testSdJwtVcFormat, metadata.format)
        assertEquals(testDocumentName, metadata.documentName)
        assertEquals(testDocumentManagerId, metadata.documentManagerId)
        assertEquals(testCreatedAt, metadata.createdAt)
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
            createdAt = testCreatedAt,
            initialCredentialsCount = 1,
            credentialPolicy = CreateDocumentSettings.CredentialPolicy.RotateUse,
            issuerMetadata = null,
            keyAttestation = null
        )

        assertEquals(testFormat, metadata.format)
        assertEquals(testDocumentName, metadata.documentName)
        assertEquals(testDocumentManagerId, metadata.documentManagerId)
        assertEquals(testCreatedAt, metadata.createdAt)
        assertEquals(1, metadata.initialCredentialsCount) // Default value is 1
        assertNull(metadata.issuerMetadata)
        assertEquals(CreateDocumentSettings.CredentialPolicy.RotateUse, metadata.credentialPolicy)
    }

    @Test
    fun `issue sets issuerProvidedData and marks as provisioned`() = runTest {
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
            createdAt = testCreatedAt,
            initialCredentialsCount = 1,
            credentialPolicy = CreateDocumentSettings.CredentialPolicy.RotateUse,
            issuerMetadata = null,
            keyAttestation = null
        )

        val testData = ByteString("test-data".toByteArray())
        metadata.issue(testData)

        assertNotNull(metadata.issuerProvidedData)
        assertEquals("test-data", metadata.issuerProvidedData?.let { String(it) })
        assertTrue(metadata.provisioned)
        coVerify(exactly = 3) { saveFn(any()) } // 1 from initialize 2 from issue
    }

    @Test
    fun `issue does nothing when already provisioned`() = runTest {
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
            createdAt = testCreatedAt,
            initialCredentialsCount = 1,
            credentialPolicy = CreateDocumentSettings.CredentialPolicy.RotateUse,
            issuerMetadata = null,
            keyAttestation = null
        )

        val testData = ByteString("test-data".toByteArray())
        metadata.issue(testData)

        // Clear verification history to ensure exact count in next verification
        io.mockk.clearMocks(saveFn)

        val newData = ByteString("new-data".toByteArray())
        metadata.issue(newData)

        // Should still have the original data
        assertEquals("test-data", metadata.issuerProvidedData?.let { String(it) })
        coVerify(exactly = 0) { saveFn(any()) }
    }

    @Test
    fun `issue with documentName updates name`() = runTest {
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
            createdAt = testCreatedAt,
            initialCredentialsCount = 1,
            credentialPolicy = CreateDocumentSettings.CredentialPolicy.RotateUse,
            issuerMetadata = null,
            keyAttestation = null
        )

        val testData = ByteString("test-data".toByteArray())
        val newName = "updated-document-name"
        metadata.issue(testData, newName)

        assertEquals(newName, metadata.displayName)
        assertTrue(metadata.provisioned)
    }

    @Test
    fun `issueDeferred sets deferredRelatedData`() = runTest {
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
            createdAt = testCreatedAt,
            initialCredentialsCount = 1,
            credentialPolicy = CreateDocumentSettings.CredentialPolicy.RotateUse,
            issuerMetadata = null,
            keyAttestation = null
        )

        val testData = ByteString("deferred-data".toByteArray())
        metadata.issueDeferred(testData)

        assertNotNull(metadata.deferredRelatedData)
        assertEquals("deferred-data", metadata.deferredRelatedData?.let { String(it) })
        coVerify(exactly = 2) { saveFn(any()) }
    }

    @Test
    fun `issueDeferred does nothing when already provisioned`() = runTest {
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
            createdAt = testCreatedAt,
            initialCredentialsCount = 1,
            credentialPolicy = CreateDocumentSettings.CredentialPolicy.RotateUse,
            issuerMetadata = null,
            keyAttestation = null
        )

        metadata.markAsProvisioned()

        // Clear verification history to ensure exact count in next verification
        io.mockk.clearMocks(saveFn)

        val testData = ByteString("deferred-data".toByteArray())
        metadata.issueDeferred(testData)

        assertNull(metadata.deferredRelatedData)
        coVerify(exactly = 0) { saveFn(any()) }
    }

    @Test
    fun `issueDeferred with documentName updates name`() = runTest {
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
            createdAt = testCreatedAt,
            initialCredentialsCount = 1,
            credentialPolicy = CreateDocumentSettings.CredentialPolicy.RotateUse,
            issuerMetadata = null,
            keyAttestation = null
        )

        val testData = ByteString("deferred-data".toByteArray())
        val newName = "updated-document-name"
        metadata.issueDeferred(testData, newName)

        assertEquals(newName, metadata.displayName)
    }

    @Test
    fun `setKeyAttestation updates keyAttestation and saves`() = runTest {
        val saveFn: suspend (ByteString) -> Unit = mockk(relaxed = true)
        val keyAttestation = "test-attestation"

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
            initialCredentialsCount = 1,
            credentialPolicy = CreateDocumentSettings.CredentialPolicy.RotateUse,
            issuerMetadata = null,
            keyAttestation = null
        )

        metadata.setKeyAttestation(keyAttestation)

        assertEquals(keyAttestation, metadata.keyAttestation)
        coVerify(exactly = 2) { saveFn(any()) }
    }

    @Test
    fun `setKeyAttestation does nothing when already provisioned`() = runTest {
        val saveFn: suspend (ByteString) -> Unit = mockk(relaxed = true)
        val keyAttestation = "test-attestation"

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
            initialCredentialsCount = 1,
            credentialPolicy = CreateDocumentSettings.CredentialPolicy.RotateUse,
            issuerMetadata = null,
            keyAttestation = null
        )

        metadata.markAsProvisioned()

        // Clear verification history to ensure exact count in next verification
        io.mockk.clearMocks(saveFn)

        metadata.setKeyAttestation(keyAttestation)

        assertNull(metadata.keyAttestation)
        coVerify(exactly = 0) { saveFn(any()) }
    }

    @Test
    fun `documentName returns docType for MsoMdocFormat when displayName is null`() = runTest {
        val saveFn: suspend (ByteString) -> Unit = mockk(relaxed = true)
        val format = MsoMdocFormat("test-doc-type")

        val metadata = spyk(
            ApplicationMetadata.create(
                documentId = testDocumentId,
                serializedData = null,
                saveFn = saveFn
            )
        )

        every { metadata.displayName } returns null

        metadata.initialize(
            format = format,
            documentName = testDocumentName,  // This will be overridden by our mock
            documentManagerId = testDocumentManagerId,
            createdAt = testCreatedAt,
            initialCredentialsCount = 1,
            credentialPolicy = CreateDocumentSettings.CredentialPolicy.RotateUse,
            issuerMetadata = null,
            keyAttestation = null
        )

        assertEquals("test-doc-type", metadata.documentName)
    }

    @Test
    fun `documentName returns vct for SdJwtVcFormat when displayName is null`() = runTest {
        val saveFn: suspend (ByteString) -> Unit = mockk(relaxed = true)
        val format = SdJwtVcFormat("test-vct")

        val metadata = spyk(
            ApplicationMetadata.create(
                documentId = testDocumentId,
                serializedData = null,
                saveFn = saveFn
            )
        )

        every { metadata.displayName } returns null

        metadata.initialize(
            format = format,
            documentName = testDocumentName,  // This will be overridden by our mock
            documentManagerId = testDocumentManagerId,
            createdAt = testCreatedAt,
            initialCredentialsCount = 1,
            credentialPolicy = CreateDocumentSettings.CredentialPolicy.RotateUse,
            issuerMetadata = null,
            keyAttestation = null
        )

        assertEquals("test-vct", metadata.documentName)
    }
}

