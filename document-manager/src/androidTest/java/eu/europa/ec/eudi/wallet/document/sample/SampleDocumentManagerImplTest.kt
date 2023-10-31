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
package eu.europa.ec.eudi.wallet.document.sample

import android.content.Context
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.android.identity.android.securearea.AndroidKeystoreSecureArea
import com.android.identity.android.storage.AndroidStorageEngine
import com.android.identity.credential.CredentialRequest
import com.android.identity.credential.CredentialRequest.DataElement
import com.android.identity.credential.CredentialStore
import com.android.identity.credential.NameSpacedData
import com.android.identity.mdoc.mso.StaticAuthDataParser
import com.android.identity.mdoc.mso.StaticAuthDataParser.StaticAuthData
import com.android.identity.mdoc.response.DeviceResponseGenerator
import com.android.identity.mdoc.response.DeviceResponseParser
import com.android.identity.mdoc.response.DocumentGenerator
import com.android.identity.mdoc.util.MdocUtil
import com.android.identity.securearea.SecureArea
import com.android.identity.securearea.SecureArea.KeyLockedException
import com.android.identity.securearea.SecureAreaRepository
import com.android.identity.storage.StorageEngine
import com.android.identity.util.Constants
import com.upokecenter.cbor.CBORObject
import eu.europa.ec.eudi.wallet.document.Document
import eu.europa.ec.eudi.wallet.document.DocumentManagerImpl
import eu.europa.ec.eudi.wallet.document.test.R
import org.junit.AfterClass
import org.junit.Assert
import org.junit.BeforeClass
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException
import java.util.Base64

@RunWith(AndroidJUnit4::class)
class SampleDocumentManagerImplTest {
    @Test
    fun test_loadSampleData() {
        val documents = documentManager.getDocuments()
        Assert.assertEquals(2, documents.size)
        Assert.assertEquals("eu.europa.ec.eudiw.pid.1", documents[0].docType)
        Assert.assertEquals("org.iso.18013.5.1.mDL", documents[1].docType)
    }

    @Test
    @Throws(KeyLockedException::class)
    fun test_sampleDocuments() {
        val documents = documentManager.getDocuments()
        Assert.assertEquals(2, documents.size)
        for (document in documents) {
            val dataElements = document.nameSpaces.flatMap { (nameSpace, elementIdentifiers) ->
                elementIdentifiers.map { elementIdentifier ->
                    DataElement(nameSpace, elementIdentifier, false)
                }
            }
            val request = CredentialRequest(dataElements)
            val transcript = CBORObject.FromObject(ByteArray(0)).EncodeToBytes()
            val nameSpacedData = NameSpacedData.fromEncodedCbor(
                CBORObject.FromObject(document.nameSpacedData).EncodeToBytes(),
            )
            val issuerData = getStaticAuthDataFromDocument(document)
            val staticAuthData = issuerData.staticAuthData
            val mergedIssuerNameSpaces =
                MdocUtil.mergeIssuerNamesSpaces(request, nameSpacedData, staticAuthData)
            val data = DocumentGenerator(document.docType, staticAuthData.issuerAuth, transcript)
                .setIssuerNamespaces(mergedIssuerNameSpaces)
                .setDeviceNamespacesSignature(
                    NameSpacedData.Builder().build(),
                    secureArea,
                    issuerData.keyAlias,
                    null,
                    SecureArea.ALGORITHM_ES256,
                )
                .generate()
            val response =
                DeviceResponseGenerator(Constants.DEVICE_RESPONSE_STATUS_OK).addDocument(data)
                    .generate()
            val responseObj = DeviceResponseParser()
                .setSessionTranscript(transcript)
                .setDeviceResponse(response)
                .parse()
            Assert.assertEquals("Documents in response", 1, responseObj.documents.size)
            Assert.assertTrue(
                "IssuerSigned authentication",
                responseObj.documents[0].issuerSignedAuthenticated,
            )
            Assert.assertTrue(
                "DeviceSigned authentication",
                responseObj.documents[0].deviceSignedAuthenticated,
            )
            Assert.assertEquals(
                "Digest Matching",
                0,
                responseObj.documents[0].numIssuerEntryDigestMatchFailures,
            )
        }
    }

    internal data class DocumentIssuerData(
        var keyAlias: String,
        var staticAuthData: StaticAuthData,
    )

    companion object {
        private lateinit var context: Context
        private lateinit var secureArea: AndroidKeystoreSecureArea
        private lateinit var storageEngine: StorageEngine
        private lateinit var documentManager: SampleDocumentManagerImpl
        private lateinit var sampleData: ByteArray

        @JvmStatic
        @BeforeClass
        @Throws(IOException::class)
        fun setUp() {
            context = InstrumentationRegistry.getInstrumentation().targetContext
            storageEngine = AndroidStorageEngine.Builder(context, context.cacheDir)
                .setUseEncryption(false)
                .build()
                .apply {
                    deleteAll()
                }
            secureArea = AndroidKeystoreSecureArea(context, storageEngine)
            val delegate = DocumentManagerImpl(context, storageEngine, secureArea)
                .userAuth(false)
            documentManager = SampleDocumentManagerImpl(context, delegate)
            try {
                context.resources.openRawResource(R.raw.sample_data).use { raw ->
                    sampleData = Base64.getDecoder().decode(raw.readBytes())
                }
            } catch (e: Exception) {
                throw IOException(e)
            }
            val result = documentManager.loadSampleData(sampleData)
            Assert.assertTrue(result is LoadSampleResult.Success)
        }

        @JvmStatic
        @AfterClass
        fun tearDown() {
            storageEngine.deleteAll()
        }

        private fun getStaticAuthDataFromDocument(document: Document): DocumentIssuerData {
            val secureAreaRepository = SecureAreaRepository()
            secureAreaRepository.addImplementation(secureArea)
            val credentialStore = CredentialStore(storageEngine, secureAreaRepository)
            val credential = credentialStore.lookupCredential(document.id)
            Assert.assertNotNull(credential)
            val authKey = credential!!.authenticationKeys[0]
            Assert.assertNotNull(authKey)
            return DocumentIssuerData(
                staticAuthData = StaticAuthDataParser(authKey.issuerProvidedData).parse(),
                keyAlias = authKey.alias,
            )
        }
    }
}
