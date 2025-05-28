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

// Added imports
import eu.europa.ec.eudi.wallet.document.credential.IssuerProvidedCredential
import eu.europa.ec.eudi.wallet.document.format.MsoMdocData
import eu.europa.ec.eudi.wallet.document.format.MsoMdocFormat
import eu.europa.ec.eudi.wallet.document.metadata.IssuerMetadata
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.multipaz.cbor.Cbor
import org.multipaz.credential.SecureAreaBoundCredential
import org.multipaz.securearea.SecureArea
import org.multipaz.securearea.SecureAreaRepository
import org.multipaz.securearea.software.SoftwareCreateKeySettings
import org.multipaz.securearea.software.SoftwareSecureArea
import org.multipaz.storage.Storage
import org.multipaz.storage.ephemeral.EphemeralStorage
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertIs
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import kotlin.test.assertTrue

class DocumentManagerImplTest {

    lateinit var documentManager: DocumentManagerImpl
    lateinit var storage: Storage
    lateinit var secureArea: SecureArea
    lateinit var secureAreaRepository: SecureAreaRepository

    @BeforeTest
    fun setUp() {
        storage = EphemeralStorage()
        secureArea = runBlocking { SoftwareSecureArea.create(storage) }
        secureAreaRepository = SecureAreaRepository.build {
            add(SoftwareSecureArea.create(storage))
        }
        documentManager = DocumentManagerImpl(
            identifier = "document_manager",
            storage = EphemeralStorage(),
            secureAreaRepository = secureAreaRepository,
        )
    }

    @AfterTest
    fun tearDown() {
        documentManager.getDocuments().forEach { documentManager.deleteDocumentById(it.id) }
    }

    @OptIn(ExperimentalStdlibApi::class)
    @Test
    fun nameSpacedData() {
        val issuerData = getResourceAsText("eu_pid.hex").hexToByteArray(HexFormat.Default)

        Cbor.decode(issuerData)
    }

    @OptIn(ExperimentalStdlibApi::class)
    @Test
    fun `should create document and store issued document`() {
        // set checkDevicePublicKey to false to avoid checking the MSO key
        // since we are using fixed issuer data
        documentManager.checkDevicePublicKey = false
        val createKeySettings = SoftwareCreateKeySettings.Builder().build()
        val createDocumentSettings = CreateDocumentSettings(
            secureAreaIdentifier = secureArea.identifier,
            createKeySettings = createKeySettings,
            numberOfCredentials = 3,
        )
        val createDocumentResult = documentManager.createDocument(
            format = MsoMdocFormat(docType = "eu.europa.ec.eudi.pid.1"),
            createSettings = createDocumentSettings
        )
        assertTrue(createDocumentResult.isSuccess)
        val unsignedDocument = createDocumentResult.getOrThrow()
        assertFalse(unsignedDocument.isCertified)

        // change document name
        unsignedDocument.name = "EU PID"

        assertIs<MsoMdocFormat>(unsignedDocument.format)
        val documentFormat = unsignedDocument.format as MsoMdocFormat
        assertEquals("eu.europa.ec.eudi.pid.1", documentFormat.docType)
        // Replace deprecated isKeyInvalidated check
        runBlocking {
            assertFalse(
                unsignedDocument.getPoPSigners().isEmpty(),
                "Expected document to have valid key (PoP signers)"
            )
        }
        assertEquals(documentManager.identifier, unsignedDocument.documentManagerId)

        val issuerData = getResourceAsText("eu_pid.hex").hexToByteArray(HexFormat.Default)


        val issuerProvidedData = runBlocking {
            unsignedDocument.baseDocument.getPendingCredentials()
                .filterIsInstance<SecureAreaBoundCredential>()
                .map {
                    IssuerProvidedCredential(
                        publicKeyAlias = it.alias,
                        data = issuerData
                    )
                }
        }

        val storeDocumentResult =
            documentManager.storeIssuedDocument(unsignedDocument, issuerProvidedData)
        assertTrue(storeDocumentResult.isSuccess)
        val issuedDocument = storeDocumentResult.getOrThrow()


        assertEquals("EU PID", issuedDocument.name)
        assertEquals(documentManager.identifier, issuedDocument.documentManagerId)

        runBlocking { // Added runBlocking
            assertTrue(issuedDocument.isCertified()) // Replaced deprecated property access
            assertEquals(3, issuedDocument.getCredentials().size)

            val claims = issuedDocument.data // Replaced deprecated property access
            assertIs<MsoMdocData>(claims)

            assertEquals(1, claims.nameSpaces.keys.size)
            assertEquals(33, claims.nameSpaces.entries.first().value.size)
        }
    }

    @Test
    fun `should return failure result when document is not found when storing issued document`() {
        val mockDocument = mockk<UnsignedDocument>(relaxed = true) { // Added relaxed = true
            every { id } returns "non_existent_document_id_123"
        }
        // issuerProvidedData needs to be List<IssuerProvidedCredential>
        val dummyIssuerProvidedData = listOf(
            IssuerProvidedCredential("dummyAlias", byteArrayOf(0x01, 0x02))
        )
        val storeDocumentResult = documentManager.storeIssuedDocument(
            unsignedDocument = mockDocument,
            issuerProvidedData = dummyIssuerProvidedData
        )
        assertTrue(storeDocumentResult.isFailure)
        // The original test expected IllegalArgumentException. This might vary based on implementation.
        // If the document is not found in an internal map/list by its ID,
        // NoSuchElementException or a custom domain exception might also be plausible.
        // Sticking to IllegalArgumentException as per the original commented test's intent.
        assertIs<IllegalArgumentException>(storeDocumentResult.exceptionOrNull())
    }

    @OptIn(ExperimentalStdlibApi::class)
    @Test
    fun `should return failure result when public keys of document and mso don't match`() {
        documentManager.checkDevicePublicKey = true // Ensure this is enabled for the test

        val createKeySettings = SoftwareCreateKeySettings.Builder().build()
        val createDocumentSettings = CreateDocumentSettings(
            secureAreaIdentifier = secureArea.identifier,
            createKeySettings = createKeySettings,
            numberOfCredentials = 1
        )
        val createDocumentResult = documentManager.createDocument(
            format = MsoMdocFormat(docType = "eu.europa.ec.eudi.pid.1"),
            createSettings = createDocumentSettings
        )
        assertTrue(createDocumentResult.isSuccess)
        val unsignedDocument = createDocumentResult.getOrThrow()

        // Use issuer data that is known to not match the document's generated key.
        // "eu_pid.hex" is used here, assuming it contains a key different from the dynamically generated one.
        val mismatchedIssuerDataBytes =
            getResourceAsText("eu_pid.hex").hexToByteArray(HexFormat.Default)

        // Construct IssuerProvidedCredential list using the alias from the unsignedDocument
        // but with the mismatched MSO data.
        val issuerProvidedCredentials = runBlocking {
            unsignedDocument.baseDocument.getPendingCredentials()
                .filterIsInstance<SecureAreaBoundCredential>()
                .map {
                    IssuerProvidedCredential(
                        publicKeyAlias = it.alias,
                        data = mismatchedIssuerDataBytes
                    )
                }
        }

        // Ensure there's at least one credential to provide, otherwise the test might not hit the intended logic.
        assertTrue(
            issuerProvidedCredentials.isNotEmpty(),
            "Document should have pending credentials to test MSO mismatch."
        )

        val storeDocumentResult =
            documentManager.storeIssuedDocument(unsignedDocument, issuerProvidedCredentials)
        assertTrue(storeDocumentResult.isFailure)
        // The specific exception for a key mismatch might be more specific,
        // but IllegalArgumentException is a common fallback.
        assertIs<IllegalArgumentException>(storeDocumentResult.exceptionOrNull())
    }

    @Test
    fun `should create document with null issuerMetaData if not provided`() {
        val createKeySettings = SoftwareCreateKeySettings.Builder().build()
        val createSettings = CreateDocumentSettings(
            secureAreaIdentifier = secureArea.identifier,
            createKeySettings = createKeySettings,
            numberOfCredentials = 1
        )
        val result = documentManager.createDocument(
            format = MsoMdocFormat(docType = "eu.europa.ec.eudi.pid.1"),
            createSettings = createSettings
            // Assuming issuerMetadata is not passed here and UnsignedDocument.issuerMetadata defaults to null
        )

        assertTrue(result.isSuccess)
        val document = result.getOrThrow()
        // This assertion assumes UnsignedDocument has an 'issuerMetadata' property that is nullable
        // and defaults to null or is not set by the createDocument method without explicit input.
        assertNull(document.issuerMetadata)
    }

    @OptIn(ExperimentalStdlibApi::class) // Added OptIn
    @Test
    fun `should store and retrieve document with issuer metadata`() {
        // Given: Define a sample IssuerMetaData.
        val expectedIssuerMetadata = IssuerMetadata(
            documentConfigurationIdentifier = "pid_test_config_v1",
            display = emptyList(), // Placeholder: Provide actual List<Display> if needed for the test
            claims = null, // Placeholder: Provide actual List<Claim>? if needed
            credentialIssuerIdentifier = "test_issuer_123",
            issuerDisplay = emptyList() // Placeholder: Provide actual List<IssuerDisplay>? if needed
        )

        // Use a local DocumentManager for test isolation if preferred, or the class-level one.
        // The original test created a new one.
        val localDocumentManager = DocumentManagerImpl(
            identifier = "metadata_test_doc_manager",
            storage = EphemeralStorage(), // Fresh storage for isolation
            secureAreaRepository = secureAreaRepository
        )
        // Disable public key check if using dummy MSO data to simplify issuance for this metadata test
        localDocumentManager.checkDevicePublicKey = false

        val createKeySettings = SoftwareCreateKeySettings.Builder().build()
        val createSettings = CreateDocumentSettings(
            secureAreaIdentifier = secureArea.identifier,
            createKeySettings = createKeySettings,
            numberOfCredentials = 1
        )

        // When: Create an unsigned document, now passing issuerMetadata
        val createResult = localDocumentManager.createDocument(
            format = MsoMdocFormat(docType = "eu.europa.ec.eudi.pid.1"),
            createSettings = createSettings,
            issuerMetadata = expectedIssuerMetadata // Pass metadata here
        )
        assertTrue(
            createResult.isSuccess,
            "Failed to create document: ${createResult.exceptionOrNull()?.message}"
        )
        val unsignedDocument = createResult.getOrThrow()

        // // Set the issuerMetadata on the unsigned document - This is removed as issuerMetadata is val
        // // This assumes UnsignedDocument has a mutable 'issuerMetadata' property.
        // unsignedDocument.issuerMetadata = expectedIssuerMetaData

        // To properly test retrieval, the document (now with metadata) needs to be stored.
        // Use dummy issuer data for the MSO part, as the focus is on metadata.
        val dummyMsoData =
            getResourceAsText("eu_pid.hex").hexToByteArray(HexFormat.Default) // Or a simpler valid MSO
        val issuerProvidedCredentials = runBlocking {
            unsignedDocument.baseDocument.getPendingCredentials()
                .filterIsInstance<SecureAreaBoundCredential>()
                .map {
                    IssuerProvidedCredential(
                        publicKeyAlias = it.alias,
                        data = dummyMsoData
                    )
                }
        }
        assertTrue(
            issuerProvidedCredentials.isNotEmpty(),
            "Document should have pending credentials for issuance."
        )

        val storeResult =
            localDocumentManager.storeIssuedDocument(unsignedDocument, issuerProvidedCredentials)
        assertTrue(
            storeResult.isSuccess,
            "Failed to store issued document: ${storeResult.exceptionOrNull()?.message}"
        )
        val issuedDocument = storeResult.getOrThrow()

        // Then: Retrieve the document by ID and check its metadata
        val retrievedDocument = localDocumentManager.getDocumentById(issuedDocument.id)
        assertNotNull(retrievedDocument, "Retrieved document should not be null.")
        // This assumes that IssuedDocument also carries the IssuerMetaData.
        assertEquals(expectedIssuerMetadata, retrievedDocument.issuerMetadata)
    }

    @OptIn(ExperimentalStdlibApi::class)
    @Test
    fun `should store deferred document successfully`() {
        // Prepare an unsigned document first
        documentManager.checkDevicePublicKey = false
        val createKeySettings = SoftwareCreateKeySettings.Builder().build()
        val createDocumentSettings = CreateDocumentSettings(
            secureAreaIdentifier = secureArea.identifier,
            createKeySettings = createKeySettings,
            numberOfCredentials = 1,
        )
        val createDocumentResult = documentManager.createDocument(
            format = MsoMdocFormat(docType = "eu.europa.ec.eudi.pid.1"),
            createSettings = createDocumentSettings
        )
        assertTrue(createDocumentResult.isSuccess)
        val unsignedDocument = createDocumentResult.getOrThrow()
        unsignedDocument.name = "Deferred Document Test"

        // Sample related data for deferred issuance
        val deferredRelatedData = "Sample deferred issuance data".encodeToByteArray()

        // Store the document for deferred issuance
        val storeDeferredResult = documentManager.storeDeferredDocument(
            unsignedDocument = unsignedDocument,
            relatedData = deferredRelatedData
        )

        // Validate result
        assertTrue(storeDeferredResult.isSuccess)
        val deferredDocument = storeDeferredResult.getOrThrow()

        // Verify properties of the deferred document
        assertEquals(unsignedDocument.id, deferredDocument.id)
        assertEquals(unsignedDocument.name, deferredDocument.name)
        assertEquals(documentManager.identifier, deferredDocument.documentManagerId)

        // Verify document is in deferred state
        assertFalse(deferredDocument.isCertified)

        // Verify we can retrieve the document from the manager
        val retrievedDocument = documentManager.getDocumentById(deferredDocument.id)
        assertNotNull(retrievedDocument)
        assertIs<DeferredDocument>(retrievedDocument)
    }

    @Test
    fun `should return failure when document not found for deferred issuance`() {
        val mockDocument = mockk<UnsignedDocument>(relaxed = true) {
            every { id } returns "non_existent_document_id_456"
        }

        // Sample related data
        val dummyRelatedData = "Test related data".toByteArray()

        // Attempt to store a deferred document with non-existent ID
        val storeDeferredResult = documentManager.storeDeferredDocument(
            unsignedDocument = mockDocument,
            relatedData = dummyRelatedData
        )

        // Validate failure
        assertTrue(storeDeferredResult.isFailure)
        assertIs<IllegalArgumentException>(storeDeferredResult.exceptionOrNull())
    }
}
