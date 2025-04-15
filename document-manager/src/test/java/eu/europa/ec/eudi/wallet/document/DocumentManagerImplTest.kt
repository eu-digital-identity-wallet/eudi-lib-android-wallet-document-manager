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

import com.upokecenter.cbor.CBORObject
import eu.europa.ec.eudi.wallet.document.format.MsoMdocData
import eu.europa.ec.eudi.wallet.document.format.MsoMdocFormat
import eu.europa.ec.eudi.wallet.document.metadata.IssuerMetaData
import eu.europa.ec.eudi.wallet.document.mock_data.IssuerMetaDataMockData
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.Assert
import org.junit.Assert.assertThrows
import org.multipaz.document.DocumentRequest
import org.multipaz.document.NameSpacedData
import org.multipaz.mdoc.mso.StaticAuthDataParser
import org.multipaz.mdoc.response.DeviceResponseGenerator
import org.multipaz.mdoc.response.DeviceResponseParser
import org.multipaz.mdoc.response.DocumentGenerator
import org.multipaz.mdoc.util.MdocUtil
import org.multipaz.securearea.SecureArea
import org.multipaz.securearea.SecureAreaRepository
import org.multipaz.securearea.software.SoftwareCreateKeySettings
import org.multipaz.securearea.software.SoftwareSecureArea
import org.multipaz.storage.Storage
import org.multipaz.storage.ephemeral.EphemeralStorage
import org.multipaz.util.Constants
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertIs
import kotlin.test.assertNotEquals
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
    fun `should create document and store issued document`() {
        // set checkDevicePublicKey to false to avoid checking the MSO key
        // since we are using fixed issuer data
        documentManager.checkDevicePublicKey = false
        val createKeySettings = SoftwareCreateKeySettings.Builder().build()
        val createDocumentResult = documentManager.createDocument(
            format = MsoMdocFormat(docType = "eu.europa.ec.eudi.pid.1"),
            createSettings = CreateDocumentSettings(
                secureAreaIdentifier = secureArea.identifier,
                createKeySettings = createKeySettings
            )
        )
        assertTrue(createDocumentResult.isSuccess)
        val unsignedDocument = createDocumentResult.getOrThrow()
        assertFalse(unsignedDocument.isCertified)

        // change document name
        unsignedDocument.name = "EU PID"

        assertIs<MsoMdocFormat>(unsignedDocument.format)
        val documentFormat = unsignedDocument.format as MsoMdocFormat
        assertEquals("eu.europa.ec.eudi.pid.1", documentFormat.docType)
        assertFalse(unsignedDocument.isKeyInvalidated)
        assertEquals(documentManager.identifier, unsignedDocument.documentManagerId)

        val issuerData = getResourceAsText("eu_pid.hex").hexToByteArray(HexFormat.Default)

        val storeDocumentResult = documentManager.storeIssuedDocument(unsignedDocument, issuerData)
        assertTrue(storeDocumentResult.isSuccess)
        val issuedDocument = storeDocumentResult.getOrThrow()

        // assert that usnigned document remains unsigned
        assertEquals("EU PID", issuedDocument.name)
        assertEquals(documentManager.identifier, issuedDocument.documentManagerId)

        assertTrue(issuedDocument.isCertified)
        assertTrue(issuedDocument.issuerProvidedData.isNotEmpty())

        val claims = issuedDocument.data
        assertIs<MsoMdocData>(claims)

        assertEquals(1, claims.nameSpaces.keys.size)
        assertEquals(33, claims.nameSpaces.entries.first().value.size)
    }

    @Test
    fun `should return failure result when document is not found when storing issued document`() {
        val storeDocumentResult = documentManager.storeIssuedDocument(
            unsignedDocument = mockk<UnsignedDocument> {
                every { id } returns "123"
            },
            issuerProvidedData = byteArrayOf(0x01)
        )
        assertTrue(storeDocumentResult.isFailure)
        assertIs<IllegalArgumentException>(storeDocumentResult.exceptionOrNull())
    }

    @OptIn(ExperimentalStdlibApi::class)
    @Test
    fun `should return failure result when public keys of document and mso don't match`() {
        val createDocumentResult = documentManager.createDocument(
            format = MsoMdocFormat(docType = "eu.europa.ec.eudi.pid.1"),
            createSettings = CreateDocumentSettings(
                secureAreaIdentifier = secureArea.identifier,
                createKeySettings = SoftwareCreateKeySettings.Builder().build()
            )
        )
        assertTrue(createDocumentResult.isSuccess)
        val document = createDocumentResult.getOrThrow()

        val issuerData = getResourceAsText("eu_pid.hex").hexToByteArray(HexFormat.Default)

        val storeDocumentResult = documentManager.storeIssuedDocument(document, issuerData)
        assertTrue(storeDocumentResult.isFailure)
        assertIs<IllegalArgumentException>(storeDocumentResult.exceptionOrNull())
    }

    @Test
    fun `should not crash when issuerMetaData is null`() {
        // Given
        val format = MsoMdocFormat(docType = "eu.europa.ec.eudi.pid.1")
        val createSettings =
            CreateDocumentSettings(
                secureAreaIdentifier = secureArea.identifier,
                createKeySettings = SoftwareCreateKeySettings.Builder().build()
            )
        // When
        val result = documentManager.createDocument(
            format = format,
            createSettings = createSettings,
            issuerMetaData = null
        )


        // Then
        val document = result.getOrThrow()
        assertNull(document.issuerMetaData)
        assertTrue(result.isSuccess)
    }

    @Test
    fun `Given mocked claims When Creating a document and retrieving it THEN it should have the correct issuer metadata`() {
        // Given
        val issuerMetaDataMock: IssuerMetaData = IssuerMetaDataMockData.getData()
        val documentManager = DocumentManagerImpl(
            identifier = "document_manager_1",
            secureAreaRepository = secureAreaRepository,
            storage = storage
        )
        // When
        val unsignedDocument = documentManager.createDocument(
            format = MsoMdocFormat(docType = "eu.europa.ec.eudi.pid.1"),
            createSettings = CreateDocumentSettings(
                secureAreaIdentifier = secureArea.identifier,
                createKeySettings = SoftwareCreateKeySettings.Builder().build()
            ),
            issuerMetaData = issuerMetaDataMock
        ).getOrThrow()

        // Then
        assertEquals(issuerMetaDataMock, unsignedDocument.issuerMetaData)

        // Then
        val document = documentManager.getDocumentById(unsignedDocument.id)
        assertNotNull(document)
        assertEquals(expected = issuerMetaDataMock, actual = document.issuerMetaData)
    }

    @Test
    fun `should return failure result when document is not found when deleting document by id`() {
        val documentId = "some_document_id"
        val deleteDocumentResult = documentManager.deleteDocumentById(documentId)
        assertTrue(deleteDocumentResult.isFailure)
        assertIs<IllegalArgumentException>(deleteDocumentResult.exceptionOrNull())
    }

    @Test
    fun `should return null when document is not found when getting document by id`() {
        val documentId = "some_document_id"
        val document: Document? = documentManager.getDocumentById(documentId)
        assertNull(document)
    }

    @Test
    fun `should return empty list when no documents are found`() {
        assertTrue(documentManager.getDocuments().isEmpty())
    }

    @Test
    fun `should create an unsigned document and store it as deferred`() {
        val createDocumentResult = documentManager.createDocument(
            format = MsoMdocFormat(docType = "eu.europa.ec.eudi.pid.1"),
            createSettings = CreateDocumentSettings(
                secureAreaIdentifier = secureArea.identifier,
                createKeySettings = SoftwareCreateKeySettings.Builder().build()
            )
        )
        assertTrue(createDocumentResult.isSuccess)
        val document = createDocumentResult.getOrThrow()
        assertFalse(document.isCertified)

        // change document name
        document.name = "EU PID"

        assertIs<MsoMdocFormat>(document.format)
        val documentFormat = document.format as MsoMdocFormat
        assertEquals("eu.europa.ec.eudi.pid.1", documentFormat.docType)
        assertFalse(document.isKeyInvalidated)

        val deferredRelatedData = byteArrayOf(1, 2, 3)
        val deferredResult = documentManager.storeDeferredDocument(document, deferredRelatedData)
        assertTrue(deferredResult.isSuccess)
        val deferredDocument = deferredResult.getOrThrow()
        assertEquals(deferredRelatedData, deferredDocument.relatedData)
    }

    @Test
    fun `should return failure result when store deferred document is called with a document that is not found`() {
        val deferredResult = documentManager.storeDeferredDocument(
            unsignedDocument = mockk<UnsignedDocument> {
                every { id } returns "123"
            },
            relatedData = byteArrayOf(0x01)
        )
        assertTrue(deferredResult.isFailure)
        assertIs<IllegalArgumentException>(deferredResult.exceptionOrNull())
    }


    @OptIn(ExperimentalStdlibApi::class)
    @Test
    fun `should generate the correct digest id for a document given an issuer data`() {
        // set checkDevicePublicKey to false to avoid checking the MSO key
        // since we are using fixed issuer data
        documentManager.checkDevicePublicKey = false
        val createDocumentResult = documentManager.createDocument(
            format = MsoMdocFormat(docType = "eu.europa.ec.eudi.pid.1"),
            createSettings = CreateDocumentSettings(
                secureAreaIdentifier = secureArea.identifier,
                createKeySettings = SoftwareCreateKeySettings.Builder().build()
            )
        )
        assertTrue(createDocumentResult.isSuccess)
        val document = createDocumentResult.getOrThrow()

        val issuerData = getResourceAsText("issuer_data_pid.hex").hexToByteArray(HexFormat.Default)

        val storeResult = documentManager.storeIssuedDocument(document, issuerData)
        assertTrue(storeResult.isSuccess)
        val issuedDocument = storeResult.getOrThrow()
        val docType = (issuedDocument.format as MsoMdocFormat).docType
        val claims = issuedDocument.data
        assertIs<MsoMdocData>(claims)
        val dataElements = claims.nameSpaces.flatMap { (nameSpace, elementIdentifiers) ->
            elementIdentifiers.map { elementIdentifier ->
                DocumentRequest.DataElement(nameSpace, elementIdentifier, false)
            }
        }
        val request = DocumentRequest(dataElements)
        val transcript = CBORObject.FromObject(ByteArray(0)).EncodeToBytes()
        val staticAuthData = StaticAuthDataParser(issuedDocument.issuerProvidedData).parse()
        val mergedIssuerNameSpaces =
            MdocUtil.mergeIssuerNamesSpaces(request, claims.nameSpacedData, staticAuthData)
        val data = runBlocking {
            DocumentGenerator(docType, staticAuthData.issuerAuth, transcript)
                .setIssuerNamespaces(mergedIssuerNameSpaces)
                .setDeviceNamespacesSignature(
                    NameSpacedData.Builder().build(),
                    issuedDocument.secureArea,
                    issuedDocument.keyAlias,
                    null,
                )
                .generate()
        }

        val response =
            DeviceResponseGenerator(Constants.DEVICE_RESPONSE_STATUS_OK).addDocument(data)
                .generate()
        val responseObj = DeviceResponseParser(response, transcript)
            .parse()
        Assert.assertEquals("Documents in response", 1, responseObj.documents.size)
        Assert.assertEquals(
            "Digest Matching",
            0,
            responseObj.documents[0].numIssuerEntryDigestMatchFailures,
        )
    }

    @Test
    fun `createDocument fails if secureArea is not registered`() {
        val invalidIdentifier = "Not Existing SecureArea"
        val result = documentManager.createDocument(
            format = MsoMdocFormat(docType = "eu.europa.ec.eudi.pid.1"),
            createSettings = CreateDocumentSettings(
                secureAreaIdentifier = invalidIdentifier,
                createKeySettings = SoftwareCreateKeySettings.Builder().build()
            )
        )

        assertTrue(result.isFailure)
        val exception = assertThrows(IllegalArgumentException::class.java) {
            result.getOrThrow()
        }
        assertEquals("SecureArea '$invalidIdentifier' not registered", exception.message)
    }

    @Test
    fun `createDocument uses the correct secureArea for document`() {
        val secureArea2 = runBlocking {
            val baseSecureArea = SoftwareSecureArea.create(storage)
            object : SecureArea by baseSecureArea {
                override val identifier: String
                    get() = "${baseSecureArea.identifier}2"
            }
        }
        val secureAreaRepository = SecureAreaRepository.build {
            add(secureArea)
            add(secureArea2)
        }
        val documentManager = DocumentManagerImpl(
            identifier = "document_manager",
            storage = storage,
            secureAreaRepository = secureAreaRepository
        )
        val createKeySettings = SoftwareCreateKeySettings.Builder().build()
        val document1 = documentManager.createDocument(
            format = MsoMdocFormat(docType = "eu.europa.ec.eudi.pid.1"),
            createSettings = CreateDocumentSettings(
                secureAreaIdentifier = secureArea.identifier,
                createKeySettings = createKeySettings,
            )
        ).getOrThrow()

        val document2 = documentManager.createDocument(
            format = MsoMdocFormat(docType = "eu.europa.ec.eudi.pid.1"),
            createSettings = CreateDocumentSettings(
                secureAreaIdentifier = secureArea2.identifier,
                createKeySettings = createKeySettings,
            )
        ).getOrThrow()

        assertEquals(secureArea.identifier, document1.secureArea.identifier)
        assertEquals(secureArea2.identifier, document2.secureArea.identifier)
    }

    @Test
    fun `verify that getDocuments returns only the documents from remaining secureArea after removing a secure area from the repository `() {
        val documentManagerIdentifier = "document_manager"
        val storage = EphemeralStorage()
        val secureArea1 = runBlocking { SoftwareSecureArea.create(storage) }
        val secureArea2 = runBlocking {
            val baseSecureArea = SoftwareSecureArea.create(storage)
            object : SecureArea by baseSecureArea {
                override val identifier: String
                    get() = "${baseSecureArea.identifier}2"
            }
        }
        val documentManagerWithTwoSecureAreas = DocumentManagerImpl(
            identifier = documentManagerIdentifier,
            secureAreaRepository = SecureAreaRepository.build {
                add(secureArea1)
                add(secureArea2)
            },
            storage = storage
        )
        documentManagerWithTwoSecureAreas.createDocument(
            format = MsoMdocFormat(docType = "eu.europa.ec.eudi.pid.1"),
            createSettings = CreateDocumentSettings(
                secureAreaIdentifier = secureArea1.identifier,
                createKeySettings = SoftwareCreateKeySettings.Builder().build()
            )
        )
        documentManagerWithTwoSecureAreas.createDocument(
            format = MsoMdocFormat(docType = "eu.europa.ec.eudi.pid.1"),
            createSettings = CreateDocumentSettings(
                secureAreaIdentifier = secureArea2.identifier,
                createKeySettings = SoftwareCreateKeySettings.Builder().build()
            )
        )

        val documentsFromTwoSecureAreas = documentManagerWithTwoSecureAreas.getDocuments()
        assertEquals(2, documentsFromTwoSecureAreas.size)

        // Create a new DocumentManager with the same identifier
        // having only the one of the previous two secure areas
        val documentManagerWithOneSecureArea = DocumentManagerImpl(
            identifier = documentManagerIdentifier,
            secureAreaRepository = SecureAreaRepository.build {
                add(secureArea1)
            },
            storage = storage
        )
        // We expect to list only the documents that their keys are present in secureArea1
        val documentsFromOneSecureArea = documentManagerWithOneSecureArea.getDocuments()
        assertEquals(1, documentsFromOneSecureArea.size)
    }

    @Test
    fun `verify that each documentManager returns only its documents`() {
        val documentManager1 = DocumentManagerImpl(
            identifier = "document_manager_1",
            secureAreaRepository = SecureAreaRepository.build {
                add(secureArea)
            },
            storage = storage
        )

        val documentManager2 = DocumentManagerImpl(
            identifier = "document_manager_2",
            secureAreaRepository = SecureAreaRepository.build {
                add(secureArea)
            },
            storage = storage
        )

        documentManager1.createDocument(
            format = MsoMdocFormat(docType = "eu.europa.ec.eudi.pid.1"),
            createSettings = CreateDocumentSettings(
                secureAreaIdentifier = secureArea.identifier,
                createKeySettings = SoftwareCreateKeySettings.Builder().build()
            )
        )
        documentManager2.createDocument(
            format = MsoMdocFormat(docType = "eu.europa.ec.eudi.pid.1"),
            createSettings = CreateDocumentSettings(
                secureAreaIdentifier = secureArea.identifier,
                createKeySettings = SoftwareCreateKeySettings.Builder().build()
            )
        )

        assertEquals(1, documentManager1.getDocuments().size)
        assertEquals(1, documentManager2.getDocuments().size)

        val document1 = documentManager1.getDocuments().first()
        val document2 = documentManager2.getDocuments().first()
        assertNotEquals(document1, document2)

        assertEquals(documentManager1.identifier, document1.documentManagerId)
        assertEquals(documentManager2.identifier, document2.documentManagerId)
    }
}