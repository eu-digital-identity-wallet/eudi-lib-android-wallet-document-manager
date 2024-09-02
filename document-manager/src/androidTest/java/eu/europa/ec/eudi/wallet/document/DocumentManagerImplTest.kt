/*
 *  Copyright (c) 2023-2024 European Commission
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package eu.europa.ec.eudi.wallet.document

import android.content.Context
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.android.identity.android.securearea.AndroidKeystoreSecureArea
import com.android.identity.storage.EphemeralStorageEngine
import com.android.identity.storage.StorageEngine
import eu.europa.ec.eudi.wallet.document.internal.isDeviceSecure
import eu.europa.ec.eudi.wallet.document.test.R
import org.bouncycastle.util.encoders.Hex
import org.junit.*
import org.junit.Assert.*
import org.junit.Assume.assumeTrue
import org.junit.runner.RunWith
import java.io.IOException
import java.security.Signature
import java.time.Instant
import java.util.*
import kotlin.random.Random

@RunWith(AndroidJUnit4::class)
class DocumentManagerImplTest {


    private val context: Context
        get() = InstrumentationRegistry.getInstrumentation().targetContext
    private lateinit var secureArea: AndroidKeystoreSecureArea
    private lateinit var storageEngine: StorageEngine
    private lateinit var documentManager: DocumentManagerImpl

    @Before
    @Throws(IOException::class)
    fun setUp() {

        storageEngine = EphemeralStorageEngine()
            .apply {
                deleteAll()
            }
        secureArea = AndroidKeystoreSecureArea(context, storageEngine)
        documentManager = DocumentManagerImpl(context, storageEngine, secureArea)
            .userAuth(false)
    }

    @After
    fun tearDown() {
        storageEngine.deleteAll()
    }

    @Test
    fun test_getDocuments_returns_empty_list() {
        val documents = documentManager.getDocuments()
        assertTrue(documents.isEmpty())
    }

    @Test
    fun test_getDocumentById_returns_null() {
        val documentId = "${UUID.randomUUID()}"
        val document = documentManager.getDocumentById(documentId)
        assertNull(document)
    }

    @Test
    fun test_createDocument() {
        val docType = "eu.europa.ec.eudi.pid.1"
        val requestResult = documentManager.createDocument(docType, false)
        assertTrue(requestResult is CreateDocumentResult.Success)

        val toBeIssued = (requestResult as CreateDocumentResult.Success).unsignedDocument
        val documentId = toBeIssued.id
        assertEquals(docType, toBeIssued.docType)
        assertFalse(toBeIssued.usesStrongBox)
        assertTrue(documentId.isNotBlank())
        assertEquals(docType, toBeIssued.name)

        // assert that document manager never returns an incomplete document
        val document = documentManager.getDocumentById(documentId) as Document
        assertTrue(document.isUnsigned)
        assertEquals(toBeIssued.id, document.id)

        val documents = documentManager.getDocuments()
        assertEquals(1, documents.size)

        val data = Random.nextBytes(32)
        val proofResult = toBeIssued.signWithAuthKey(data)
        assertTrue(proofResult is SignedWithAuthKeyResult.Success)

        proofResult as SignedWithAuthKeyResult.Success

        val proof = proofResult.signature
        val publicKey = toBeIssued.publicKey

        val sig = Signature.getInstance(Algorithm.SHA256withECDSA).apply {
            initVerify(publicKey)
            update(data)
        }
        assertTrue(sig.verify(proof))
    }

    @Test
    fun test_storeIssuedDocument() {
        val docType = "eu.europa.ec.eudi.pid.1"
        val data = context.resources.openRawResource(R.raw.eu_pid).use { raw ->
            Hex.decode(raw.readBytes())
        }
        documentManager.checkPublicKeyBeforeAdding(false)
        val toBeIssued = documentManager.createDocument(docType, false)
            .getOrThrow()
        val retrievedToBeIssued = documentManager.getDocumentById(toBeIssued.id) as UnsignedDocument
        val result = documentManager.storeIssuedDocument(retrievedToBeIssued, data)
        assertTrue(result is StoreDocumentResult.Success)
        assertEquals(toBeIssued.id, (result as StoreDocumentResult.Success).documentId)
    }

    @Test
    fun test_checkPublicKeyInMso() {
        val docType = "eu.europa.ec.eudi.pid.1"
        // some random data with mismatching public key
        val data = context.resources.openRawResource(R.raw.eu_pid).use { raw ->
            Hex.decode(raw.readBytes())
        }
        val expiresAt = Instant.now().plusSeconds(300).toEpochMilli()
        documentManager.checkPublicKeyBeforeAdding(true)
        val toBeIssued = documentManager.createDocument(docType, false)
            .getOrThrow()
        val result = documentManager.storeIssuedDocument(toBeIssued, data)
        assertTrue(result is StoreDocumentResult.Failure)
        result as StoreDocumentResult.Failure
        assertTrue(result.throwable is IllegalArgumentException)
        assertEquals(
            "Public key in MSO does not match the one in the request",
            result.throwable.message
        )
    }

    @Test
    fun test_unsignedDocument_signWithAuthKey_throws_when_userAuth_is_set() {
        assumeTrue(context.isDeviceSecure)
        documentManager
            .userAuth(true)
            .userAuthTimeout(10_000L)
        val doc1 = documentManager.createDocument("doc1", false).getOrThrow()

        assertTrue(doc1.requiresUserAuth)

        val doc1SignResult = doc1.signWithAuthKey(byteArrayOf(1))
        assertTrue(doc1SignResult is SignedWithAuthKeyResult.UserAuthRequired)
    }
}