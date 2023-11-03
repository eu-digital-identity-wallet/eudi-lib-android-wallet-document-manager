/*
 * Copyright (c) 2023 European Commission
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

import android.content.Context
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.android.identity.android.securearea.AndroidKeystoreSecureArea
import com.android.identity.storage.EphemeralStorageEngine
import com.android.identity.storage.StorageEngine
import org.junit.AfterClass
import org.junit.Assert
import org.junit.BeforeClass
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException
import java.util.UUID

@RunWith(AndroidJUnit4::class)
class DocumentManagerImplTest {


    companion object {
        private lateinit var context: Context
        private lateinit var secureArea: AndroidKeystoreSecureArea
        private lateinit var storageEngine: StorageEngine
        private lateinit var documentManager: DocumentManagerImpl

        @JvmStatic
        @BeforeClass
        @Throws(IOException::class)
        fun setUp() {
            context = InstrumentationRegistry.getInstrumentation().targetContext
            storageEngine = EphemeralStorageEngine()
                .apply {
                    deleteAll()
                }
            secureArea = AndroidKeystoreSecureArea(context, storageEngine)
            documentManager = DocumentManagerImpl(context, storageEngine, secureArea)
                .userAuth(false)
        }

        @JvmStatic
        @AfterClass
        fun tearDown() {
            storageEngine.deleteAll()
        }
    }

    @Test
    fun test_getDocuments_returns_empty_list() {
        val documents = documentManager.getDocuments()
        Assert.assertTrue(documents.isEmpty())
    }

    @Test
    fun test_getDocumentById_returns_null() {
        val documentId = "${UUID.randomUUID()}"
        val document = documentManager.getDocumentById(documentId)
        Assert.assertNull(document)
    }

    @Test
    fun test_createIssuanceRequest() {
        val docType = "eu.europa.ec.eudiw.pid.1"
        val requestResult = documentManager.createIssuanceRequest(docType, false)
        Assert.assertTrue(requestResult is CreateIssuanceRequestResult.Success)

        val request = (requestResult as CreateIssuanceRequestResult.Success).issuanceRequest
        val documentId = request.documentId
        Assert.assertEquals(docType, request.docType)
        Assert.assertFalse(request.hardwareBacked)
        Assert.assertTrue(documentId.isNotBlank())
        Assert.assertEquals(docType, request.name)
        val stored = storageEngine.enumerate().toSet().filter { it.contains(request.documentId) }
        Assert.assertEquals(3, stored.size)
        Assert.assertNotNull(stored.firstOrNull { it.contains("AuthenticationKey_$documentId") })
        Assert.assertNotNull(stored.firstOrNull { it.contains("Credential_$documentId") })
        Assert.assertNotNull(stored.firstOrNull { it.contains("CredentialKey_$documentId") })

        // assert that document manager never returns an incomplete document
        val document = documentManager.getDocumentById(documentId)
        Assert.assertNull(document)

        val documents = documentManager.getDocuments()
        Assert.assertTrue(documents.isEmpty())
    }
}