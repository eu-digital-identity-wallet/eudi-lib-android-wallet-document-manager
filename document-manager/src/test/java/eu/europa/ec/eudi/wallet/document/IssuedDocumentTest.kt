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

import eu.europa.ec.eudi.wallet.document.format.MsoMdocData
import eu.europa.ec.eudi.wallet.document.format.MsoMdocFormat
import eu.europa.ec.eudi.wallet.document.metadata.IssuerMetaData
import kotlinx.coroutines.runBlocking
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
import kotlin.test.assertTrue

class IssuedDocumentTest {
    lateinit var documentManager: DocumentManagerImpl
    lateinit var storage: Storage
    lateinit var secureArea: SecureArea
    lateinit var secureAreaRepository: SecureAreaRepository
    lateinit var issuedDocument: IssuedDocument

    @OptIn(ExperimentalStdlibApi::class)
    @BeforeTest
    fun setUp() {
        storage = EphemeralStorage()
        secureArea = runBlocking { SoftwareSecureArea.create(storage) }
        secureAreaRepository = SecureAreaRepository.build {
            add(secureArea)
        }
        documentManager = DocumentManagerImpl(
            identifier = "document_manager",
            storage = EphemeralStorage(),
            secureAreaRepository = secureAreaRepository,
        )

        val metadata = IssuerMetaData.fromJson(getResourceAsText("eu_pid_metadata_mso_mdoc.json")).getOrNull()
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
            issuerMetaData = metadata
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
        documentManager.getDocuments().forEach { documentManager.deleteDocumentById(it.id) }
    }

    @Test
    fun `assert metadata for claim is available`() {
        assertTrue(issuedDocument.data.claims.any { it.issuerMetadata != null })

        // pick given_name

        val givenName = (issuedDocument.data as MsoMdocData)
            .claims
            .first { it.nameSpace == "eu.europa.ec.eudi.pid.1" && it.identifier == "given_name" }

        // Access the path property instead of name, as the claim metadata's path should contain the namespace and identifier
        assertTrue(
            givenName.issuerMetadata?.display?.firstOrNull()?.name?.isNotBlank() == true ||
                    givenName.issuerMetadata?.path == listOf(
                givenName.nameSpace,
                givenName.identifier,
            )
        )
    }
}