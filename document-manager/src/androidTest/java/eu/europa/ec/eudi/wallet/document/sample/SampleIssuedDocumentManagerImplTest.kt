/*
 * Copyright (c) 2023-2024 European Commission
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

import android.security.keystore.UserNotAuthenticatedException
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.android.identity.android.securearea.AndroidKeystoreSecureArea
import com.android.identity.android.storage.AndroidStorageEngine
import com.android.identity.credential.CredentialFactory
import com.android.identity.credential.SecureAreaBoundCredential
import com.android.identity.crypto.Algorithm
import com.android.identity.document.DocumentRequest
import com.android.identity.document.DocumentStore
import com.android.identity.document.NameSpacedData
import com.android.identity.mdoc.credential.MdocCredential
import com.android.identity.mdoc.mso.StaticAuthDataParser
import com.android.identity.mdoc.mso.StaticAuthDataParser.StaticAuthData
import com.android.identity.mdoc.response.DeviceResponseGenerator
import com.android.identity.mdoc.response.DeviceResponseParser
import com.android.identity.mdoc.response.DocumentGenerator
import com.android.identity.mdoc.util.MdocUtil
import com.android.identity.securearea.SecureAreaRepository
import com.android.identity.util.Constants
import com.upokecenter.cbor.CBORObject
import eu.europa.ec.eudi.wallet.document.DocumentManager
import eu.europa.ec.eudi.wallet.document.DocumentManagerImpl
import eu.europa.ec.eudi.wallet.document.IssuedDocument
import eu.europa.ec.eudi.wallet.document.test.R
import kotlinx.io.files.Path
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.File
import java.util.*

@RunWith(AndroidJUnit4::class)
class SampleIssuedDocumentManagerImplTest {

    val context
        get() = InstrumentationRegistry.getInstrumentation().targetContext

    lateinit var storageEngine: AndroidStorageEngine

    lateinit var secureArea: AndroidKeystoreSecureArea

    lateinit var delegate: DocumentManager

    lateinit var documentManager: SampleDocumentManager

    val sampleData
        get() = context.resources.openRawResource(R.raw.sample_data).use { raw ->
            Base64.getDecoder().decode(raw.readBytes())
        }

    @Before
    fun setup() {
        storageEngine = AndroidStorageEngine.Builder(
            context,
            Path(File(context.noBackupFilesDir, "eudi-identity.bin").path)
        )
            .setUseEncryption(false)
            .build()
            .apply {
                deleteAll()
            }
        secureArea = AndroidKeystoreSecureArea(context, storageEngine)

        delegate = DocumentManagerImpl(context, storageEngine, secureArea)
            .userAuth(false)

        documentManager = SampleDocumentManagerImpl(context, delegate)
    }

    @After
    fun tearDown() {
        storageEngine.deleteAll()
    }

    @Test
    fun test_loadSampleData() {
        documentManager.loadSampleData(sampleData)
        val documents = documentManager.getDocuments()
        assertEquals(2, documents.size)
        assertEquals("eu.europa.ec.eudi.pid.1", documents[0].docType)
        assertEquals("org.iso.18013.5.1.mDL", documents[1].docType)
        assertEquals(
            context.getString(eu.europa.ec.eudi.wallet.document.R.string.eu_pid_doctype_name),
            documents[0].name
        )
        assertEquals(
            context.getString(eu.europa.ec.eudi.wallet.document.R.string.mdl_doctype_name),
            documents[1].name
        )
    }

    @Test
    @Throws(UserNotAuthenticatedException::class)
    fun test_sampleDocuments() {
        documentManager.loadSampleData(sampleData)
        val documents = documentManager.getDocuments()
        assertEquals(2, documents.size)
        for (document in documents) {
            document as IssuedDocument
            val dataElements = document.nameSpaces.flatMap { (nameSpace, elementIdentifiers) ->
                elementIdentifiers.map { elementIdentifier ->
                    DocumentRequest.DataElement(nameSpace, elementIdentifier, false)
                }
            }
            val request = DocumentRequest(dataElements)
            val transcript = CBORObject.FromObject(ByteArray(0)).EncodeToBytes()
            val nameSpacedData = NameSpacedData.Builder().apply {
                document.nameSpacedData.forEach { (nameSpace, elements) ->
                    elements.forEach { (elementIdentifier, value) ->
                        putEntry(nameSpace, elementIdentifier, value)
                    }
                }
            }.build()

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
                    Algorithm.ES256,
                )
                .generate()
            val response =
                DeviceResponseGenerator(Constants.DEVICE_RESPONSE_STATUS_OK).addDocument(data)
                    .generate()
            val responseObj = DeviceResponseParser(response, transcript)
                .parse()
            assertEquals("Documents in response", 1, responseObj.documents.size)
            assertTrue(
                "IssuerSigned authentication",
                responseObj.documents[0].issuerSignedAuthenticated,
            )
            assertTrue(
                "DeviceSigned authentication",
                responseObj.documents[0].deviceSignedAuthenticated,
            )
            assertEquals(
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


    private fun getStaticAuthDataFromDocument(issuedDocument: IssuedDocument): DocumentIssuerData {
        val secureAreaRepository = SecureAreaRepository()
        secureAreaRepository.addImplementation(secureArea)
        val credentialFactory = CredentialFactory().apply {
            addCredentialImplementation(MdocCredential::class) { document, dataItem ->
                MdocCredential(document, dataItem)
            }
        }
        val credentialStore = DocumentStore(storageEngine, secureAreaRepository, credentialFactory)
        val baseDocument = credentialStore.lookupDocument(issuedDocument.id)
        assertNotNull(baseDocument)
        val credential = (baseDocument!!.certifiedCredentials[0] as SecureAreaBoundCredential)
        return DocumentIssuerData(
            staticAuthData = StaticAuthDataParser(credential.issuerProvidedData).parse(),
            keyAlias = credential.alias,
        )
    }
}
