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

import com.android.identity.securearea.SecureArea
import com.android.identity.securearea.SecureAreaRepository
import com.android.identity.securearea.software.SoftwareCreateKeySettings
import com.android.identity.securearea.software.SoftwareSecureArea
import com.android.identity.storage.EphemeralStorageEngine
import com.android.identity.storage.StorageEngine
import eu.europa.ec.eudi.wallet.document.format.MsoMdocData
import eu.europa.ec.eudi.wallet.document.format.MsoMdocFormat
import eu.europa.ec.eudi.wallet.document.metadata.DocumentMetaData
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertIs
import kotlin.test.assertTrue

class IssuedDocumentTest {
    lateinit var documentManager: DocumentManagerImpl
    lateinit var storageEngine: StorageEngine
    lateinit var secureArea: SecureArea
    lateinit var secureAreaRepository: SecureAreaRepository
    lateinit var issuedDocument: IssuedDocument

    @OptIn(ExperimentalStdlibApi::class)
    @BeforeTest
    fun setUp() {
        storageEngine = EphemeralStorageEngine()
        secureArea = SoftwareSecureArea(storageEngine)
        secureAreaRepository = SecureAreaRepository()
            .apply { addImplementation(secureArea) }
        documentManager = DocumentManagerImpl(
            identifier = "document_manager",
            storageEngine = EphemeralStorageEngine(),
            secureAreaRepository = secureAreaRepository,
        )

        val metadata = DocumentMetaData.fromJson(getResourceAsText("eu_pid_metadata.json"))
        // set checkDevicePublicKey to false to avoid checking the MSO key
        // since we are using fixed issuer data
        documentManager.checkDevicePublicKey = false
        val createKeySettings = SoftwareCreateKeySettings.Builder().build()
        val createDocumentResult = documentManager.createDocument(
            format = MsoMdocFormat(docType = "eu.europa.ec.eudi.pid.1"),
            createSettings = CreateDocumentSettings(
                secureAreaIdentifier = secureArea.identifier,
                createKeySettings = createKeySettings
            ),
            documentMetaData = metadata
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
        issuedDocument = storeDocumentResult.getOrThrow()
    }

    @AfterTest
    fun tearDown() {
        storageEngine.deleteAll()
        documentManager.getDocuments().forEach { documentManager.deleteDocumentById(it.id) }
    }

    @Test
    fun `assert metadata for claim is available`() {
        assertTrue(issuedDocument.data.claims.any { it.metadata != null })

        // pick given_name

        val givenName = (issuedDocument.data as MsoMdocData)
            .claims
            .first { it.nameSpace == "eu.europa.ec.eudi.pid.1" && it.identifier == "given_name" }

        assertTrue(
            givenName.metadata?.path == listOf(
                givenName.nameSpace,
                givenName.identifier,
            )
        )
    }
}