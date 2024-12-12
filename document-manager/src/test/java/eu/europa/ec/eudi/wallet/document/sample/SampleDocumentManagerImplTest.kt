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

package eu.europa.ec.eudi.wallet.document.sample

import com.android.identity.securearea.SecureAreaRepository
import com.android.identity.securearea.software.SoftwareCreateKeySettings
import com.android.identity.securearea.software.SoftwareSecureArea
import com.android.identity.storage.EphemeralStorageEngine
import eu.europa.ec.eudi.wallet.document.CreateDocumentSettings
import eu.europa.ec.eudi.wallet.document.DocumentManagerImpl
import eu.europa.ec.eudi.wallet.document.IssuedDocument
import eu.europa.ec.eudi.wallet.document.MsoMdocIssuedDocument
import eu.europa.ec.eudi.wallet.document.format.MsoMdocFormat
import eu.europa.ec.eudi.wallet.document.getResourceAsText
import eu.europa.ec.eudi.wallet.document.secureAreaFixture
import org.junit.AfterClass
import org.junit.BeforeClass
import kotlin.io.encoding.Base64
import kotlin.io.encoding.ExperimentalEncodingApi
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs
import kotlin.test.assertTrue

class SampleDocumentManagerImplTest {

    companion object {
        lateinit var documentManager: SampleDocumentManagerImpl

        @OptIn(ExperimentalEncodingApi::class)
        val sampleDocuments
            get() = getResourceAsText("sample_documents.txt").let {
                Base64.Default.decode(it)
            }

        @BeforeClass
        @JvmStatic
        fun setUp() {
            val secureArea = SoftwareSecureArea(EphemeralStorageEngine())
            val secureAreaRepository = SecureAreaRepository()
                .apply { addImplementation(secureArea) }
            documentManager = SampleDocumentManagerImpl(
                DocumentManagerImpl(
                    identifier = SampleDocumentManagerImpl::class.simpleName!!,
                    storageEngine = EphemeralStorageEngine(),
                    secureAreaRepository = secureAreaRepository
                )
            )
            val createKeySettings = SoftwareCreateKeySettings.Builder().build()
            val loadResult = documentManager.loadMdocSampleDocuments(
                sampleData = sampleDocuments,
                createSettings = CreateDocumentSettings(
                    secureAreaIdentifier = secureArea.identifier,
                    createKeySettings = createKeySettings,
                ),
                documentNamesMap = mapOf(
                    "eu.europa.ec.eudi.pid.1" to "EU PID",
                    "org.iso.18013.5.1.mDL" to "mDL"
                )
            )
            assertTrue(loadResult.isSuccess)
        }

        @AfterClass
        @JvmStatic
        fun tearDown() {
            documentManager.getDocuments().forEach { documentManager.deleteDocumentById(it.id) }
        }
    }

    @Test
    fun `getDocuments should return the two sample documents`() {
        val documents = documentManager.getDocuments()
        assertEquals(2, documents.size)
    }

    @Test
    fun `documents should have their names set according to namesMap given to load method`() {
        val documents = documentManager.getDocuments()
        assertTrue(documents.any { it.name == "EU PID" })
        assertTrue(documents.any { it.name == "mDL" })
    }

    @Test
    fun `getDocuments using query with docType should return the appropriate document`() {
        val documents = documentManager.getDocuments { document ->
            (document.format as MsoMdocFormat).docType == "eu.europa.ec.eudi.pid.1"
        }
        assertEquals(1, documents.size)
        val document = documents.first()
        assertEquals("EU PID", document.name)
    }

    @Test
    fun `deleteDocumentById should delete the document`() {
        val storageEngine = EphemeralStorageEngine()
        val secureAreaRepository = SecureAreaRepository()
            .apply { addImplementation(SoftwareSecureArea(storageEngine)) }
        val documentManager = SampleDocumentManagerImpl(
            DocumentManagerImpl(
                identifier = SampleDocumentManagerImpl::class.simpleName!!,
                storageEngine = storageEngine,
                secureAreaRepository = secureAreaRepository
            )
        )
        val createKeySettings = SoftwareCreateKeySettings.Builder().build()
        documentManager.loadMdocSampleDocuments(
            sampleData = sampleDocuments,
            createSettings = CreateDocumentSettings(
                secureAreaIdentifier = secureAreaFixture.identifier,
                createKeySettings = createKeySettings,
            ),
            documentNamesMap = mapOf(
                "eu.europa.ec.eudi.pid.1" to "EU PID",
                "org.iso.18013.5.1.mDL" to "mDL"
            )
        )

        val document = documentManager.getDocuments().first()
        val documentId = document.id

        val deleteResult = documentManager.deleteDocumentById(documentId)

        assertTrue(deleteResult.isSuccess)
        val documents = documentManager.getDocuments()
        assertEquals(1, documents.size)

    }

    @Test
    fun `issued document nameSpacedDataValues parses correctly the cbor values`() {
        val document = documentManager.getDocuments {
            (it.format as MsoMdocFormat).docType == "org.iso.18013.5.1.mDL"
        }.first()

        assertIs<IssuedDocument>(document)
        val data = (document as MsoMdocIssuedDocument).nameSpacedDataDecoded
        assertEquals(1, data.size)
        assertEquals("org.iso.18013.5.1", data.keys.first())
    }

    @Test
    fun `getDocumentById should return the document`() {
        val documents = documentManager.getDocuments()
        val firstDocument = documents.first()
        val documentId = firstDocument.id
        val document = documentManager.getDocumentById(documentId)

        assertEquals(firstDocument, document)
    }
}