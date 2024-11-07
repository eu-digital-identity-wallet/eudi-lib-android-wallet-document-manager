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

import com.android.identity.crypto.Algorithm
import com.android.identity.document.DocumentRequest
import com.android.identity.document.NameSpacedData
import com.android.identity.mdoc.mso.StaticAuthDataParser
import com.android.identity.mdoc.response.DeviceResponseGenerator
import com.android.identity.mdoc.response.DeviceResponseParser
import com.android.identity.mdoc.response.DocumentGenerator
import com.android.identity.mdoc.util.MdocUtil
import com.android.identity.securearea.SecureArea
import com.android.identity.securearea.SecureAreaRepository
import com.android.identity.securearea.software.SoftwareCreateKeySettings
import com.android.identity.securearea.software.SoftwareSecureArea
import com.android.identity.storage.EphemeralStorageEngine
import com.android.identity.storage.StorageEngine
import com.android.identity.util.Constants
import com.upokecenter.cbor.CBORObject
import eu.europa.ec.eudi.wallet.document.format.MsoMdocFormat
import eu.europa.ec.eudi.wallet.document.format.UnsupportedDocumentFormat
import io.mockk.every
import io.mockk.mockk
import io.mockk.spyk
import org.junit.Assert
import org.junit.Assert.assertThrows
import kotlin.test.*

class DocumentManagerImplTest {

    lateinit var documentManager: DocumentManagerImpl
    lateinit var storageEngine: StorageEngine
    lateinit var secureArea: SecureArea
    lateinit var secureAreaRepository: SecureAreaRepository

    @BeforeTest
    fun setUp() {
        storageEngine = EphemeralStorageEngine()
        secureArea = SoftwareSecureArea(storageEngine)
        secureAreaRepository = SecureAreaRepository()
            .apply { addImplementation(secureArea) }
        documentManager = DocumentManagerImpl(
            identifier = "document_manager",
            storageEngine = EphemeralStorageEngine(),
            secureAreaRepository = secureAreaRepository
        )
    }

    @AfterTest
    fun tearDown() {
        storageEngine.deleteAll()
        documentManager.getDocuments().forEach { documentManager.deleteDocumentById(it.id) }
    }

    @OptIn(ExperimentalStdlibApi::class)
    @Test
    fun `should create document and store issued document`() {
        // set checkMsoKey to false to avoid checking the MSO key
        // since we are using fixed issuer data
        documentManager.checkMsoKey = false
        val createKeySettings = SoftwareCreateKeySettings.Builder().build()
        val createDocumentResult = documentManager.createDocument(
            format = MsoMdocFormat(docType = "eu.europa.ec.eudi.pid.1"),
            createSettings = SecureAreaCreateDocumentSettings(
                secureAreaIdentifier = secureArea.identifier,
                keySettings = createKeySettings
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

        assertEquals(1, issuedDocument.nameSpaces.keys.size)
        assertEquals(33, issuedDocument.nameSpaces.entries.first().value.size)
    }

    @Test
    fun `should return failure result when unsupported format is used to create document`() {
        val createDocumentResult = documentManager.createDocument(
            format = UnsupportedDocumentFormat,
            createSettings = SecureAreaCreateDocumentSettings(
                secureAreaIdentifier = secureArea.identifier,
                keySettings = SoftwareCreateKeySettings.Builder().build()
            )
        )
        assertTrue(createDocumentResult.isFailure)
        assertIs<IllegalArgumentException>(createDocumentResult.exceptionOrNull())
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
            createSettings = SecureAreaCreateDocumentSettings(
                secureAreaIdentifier = secureArea.identifier,
                keySettings = SoftwareCreateKeySettings.Builder().build()
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
            createSettings = SecureAreaCreateDocumentSettings(
                secureAreaIdentifier = secureArea.identifier,
                keySettings = SoftwareCreateKeySettings.Builder().build()
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

    @Test
    fun `should return failure when storing issued document of unsupported format`() {

        val createDocumentResult = documentManager.createDocument(
            format = MsoMdocFormat(docType = "eu.europa.ec.eudi.pid.1"),
            createSettings = SecureAreaCreateDocumentSettings(
                secureAreaIdentifier = secureArea.identifier,
                keySettings = SoftwareCreateKeySettings.Builder().build()
            )
        )
        assertTrue(createDocumentResult.isSuccess)
        val document = spyk(createDocumentResult.getOrThrow()) {
            every { format } returns UnsupportedDocumentFormat
        }

        val storeDocumentResult = documentManager.storeIssuedDocument(document, byteArrayOf(0x01))
        assertTrue(storeDocumentResult.isFailure)
        assertIs<IllegalArgumentException>(storeDocumentResult.exceptionOrNull())
    }

    @OptIn(ExperimentalStdlibApi::class)
    @Test
    fun `should generate the correct digest id for a document given an issuer data`() {
        // set checkMsoKey to false to avoid checking the MSO key
        // since we are using fixed issuer data
        documentManager.checkMsoKey = false
        val createDocumentResult = documentManager.createDocument(
            format = MsoMdocFormat(docType = "eu.europa.ec.eudi.pid.1"),
            createSettings = SecureAreaCreateDocumentSettings(
                secureAreaIdentifier = secureArea.identifier,
                keySettings = SoftwareCreateKeySettings.Builder().build()
            )
        )
        assertTrue(createDocumentResult.isSuccess)
        val document = createDocumentResult.getOrThrow()

        val issuerData = getResourceAsText("issuer_data_pid.hex").hexToByteArray(HexFormat.Default)

        val storeResult = documentManager.storeIssuedDocument(document, issuerData)
        assertTrue(storeResult.isSuccess)
        val issuedDocument = storeResult.getOrThrow()
        val docType = (issuedDocument.format as MsoMdocFormat).docType
        val dataElements = issuedDocument.nameSpaces.flatMap { (nameSpace, elementIdentifiers) ->
            elementIdentifiers.map { elementIdentifier ->
                DocumentRequest.DataElement(nameSpace, elementIdentifier, false)
            }
        }
        val request = DocumentRequest(dataElements)
        val transcript = CBORObject.FromObject(ByteArray(0)).EncodeToBytes()
        val staticAuthData = StaticAuthDataParser(issuedDocument.issuerProvidedData).parse()
        val mergedIssuerNameSpaces =
            MdocUtil.mergeIssuerNamesSpaces(request, issuedDocument.nameSpacedData, staticAuthData)
        val data = DocumentGenerator(docType, staticAuthData.issuerAuth, transcript)
            .setIssuerNamespaces(mergedIssuerNameSpaces)
            .setDeviceNamespacesSignature(
                NameSpacedData.Builder().build(),
                issuedDocument.secureArea,
                issuedDocument.keyAlias,
                null,
                Algorithm.ES256,
            )
            .generate()

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
    fun `createDocument fails if invalid createSettings is provided`() {
        val result = documentManager.createDocument(
            format = MsoMdocFormat(docType = "eu.europa.ec.eudi.pid.1"),
            createSettings = object : CreateDocumentSettings {}
        )

        assertTrue(result.isFailure)
        val exception = assertThrows(IllegalArgumentException::class.java) {
            result.getOrThrow()
        }
        assertEquals(
            "Invalid createSettings. Instance of [class eu.europa.ec.eudi.wallet.document.SecureAreaDocumentSettings] expected",
            exception.message
        )
    }

    @Test
    fun `createDocument fails if secureArea is not registered`() {
        val invalidIdentifier = "Not Existing SecureArea"
        val result = documentManager.createDocument(
            format = MsoMdocFormat(docType = "eu.europa.ec.eudi.pid.1"),
            createSettings = SecureAreaCreateDocumentSettings(
                secureAreaIdentifier = invalidIdentifier,
                keySettings = SoftwareCreateKeySettings.Builder().build()
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
        val secureArea2 = object : SecureArea by SoftwareSecureArea(EphemeralStorageEngine()) {
            override val identifier: String
                get() = "${secureArea.identifier}2"
        }
        val secureAreaRepository = SecureAreaRepository()
            .apply {
                addImplementation(secureArea)
                addImplementation(secureArea2)
            }
        val documentManager = DocumentManagerImpl(
            identifier = "document_manager",
            storageEngine = storageEngine,
            secureAreaRepository = secureAreaRepository
        )
        val createKeySettings = SoftwareCreateKeySettings.Builder().build()
        val document1 = documentManager.createDocument(
            format = MsoMdocFormat(docType = "eu.europa.ec.eudi.pid.1"),
            createSettings = SecureAreaCreateDocumentSettings(
                secureAreaIdentifier = secureArea.identifier,
                keySettings = createKeySettings,
            )
        ).getOrThrow()

        val document2 = documentManager.createDocument(
            format = MsoMdocFormat(docType = "eu.europa.ec.eudi.pid.1"),
            createSettings = SecureAreaCreateDocumentSettings(
                secureAreaIdentifier = secureArea2.identifier,
                keySettings = createKeySettings,
            )
        ).getOrThrow()

        assertEquals(secureArea.identifier, document1.secureArea.identifier)
        assertEquals(secureArea2.identifier, document2.secureArea.identifier)
    }

    @Test
    fun `verify that getDocuments returns only the documents from remaining secureArea after removing a secure area from the repository `() {
        val storage = EphemeralStorageEngine()
        val secureArea1 = SoftwareSecureArea(storage)
        val secureArea2 = object : SecureArea by SoftwareSecureArea(EphemeralStorageEngine()) {
            override val identifier: String
                get() = "${secureArea.identifier}2"
        }
        val documentManager1 = DocumentManagerImpl(
            identifier = "document_manager_1",
            secureAreaRepository = SecureAreaRepository().apply {
                addImplementation(secureArea1)
                addImplementation(secureArea2)
            },
            storageEngine = storage
        )
        documentManager1.createDocument(
            format = MsoMdocFormat(docType = "eu.europa.ec.eudi.pid.1"),
            createSettings = SecureAreaCreateDocumentSettings(
                secureAreaIdentifier = secureArea1.identifier,
                keySettings = SoftwareCreateKeySettings.Builder().build()
            )
        )
        documentManager1.createDocument(
            format = MsoMdocFormat(docType = "eu.europa.ec.eudi.pid.1"),
            createSettings = SecureAreaCreateDocumentSettings(
                secureAreaIdentifier = secureArea2.identifier,
                keySettings = SoftwareCreateKeySettings.Builder().build()
            )
        )

        val documentsFrom1 = documentManager1.getDocuments()
        assertEquals(2, documentsFrom1.size)

        val documentManager2 = DocumentManagerImpl(
            identifier = "document_manager_2",
            secureAreaRepository = SecureAreaRepository().apply {
                addImplementation(secureArea1)
            },
            storageEngine = storage
        )

        val documentsFrom2 = documentManager2.getDocuments()
        assertEquals(1, documentsFrom2.size)
    }
}