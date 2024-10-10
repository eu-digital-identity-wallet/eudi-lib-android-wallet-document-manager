package eu.europa.ec.eudi.wallet.document

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.android.identity.crypto.Algorithm
import com.android.identity.document.DocumentRequest
import com.android.identity.document.NameSpacedData
import com.android.identity.mdoc.response.DeviceResponseGenerator
import com.android.identity.mdoc.response.DeviceResponseParser
import com.android.identity.mdoc.response.DocumentGenerator
import com.android.identity.mdoc.util.MdocUtil
import com.android.identity.securearea.SecureArea
import com.android.identity.storage.StorageEngine
import com.android.identity.util.Constants
import com.android.identity.util.fromHex
import com.upokecenter.cbor.CBORObject
import eu.europa.ec.eudi.wallet.document.defaults.DefaultSecureArea
import eu.europa.ec.eudi.wallet.document.defaults.DefaultStorageEngine
import eu.europa.ec.eudi.wallet.document.test.R
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import kotlin.collections.component1
import kotlin.collections.component2

@RunWith(AndroidJUnit4::class)
class DigestsMatchTest {

    val context
        get() = InstrumentationRegistry.getInstrumentation().targetContext

    lateinit var storageEngine: StorageEngine

    lateinit var secureArea: SecureArea

    lateinit var documentManager: DocumentManager

    val issuerData: ByteArray
        get() = context.resources.openRawResource(R.raw.issuer_data_pid).use { raw ->
            String(raw.readBytes()).fromHex()
        }

    @Before
    fun setup() {
        storageEngine = DefaultStorageEngine(context, useEncryption = true)
        secureArea = DefaultSecureArea(context, storageEngine)
        val keyUnlockDataFactory = DefaultSecureArea.KeyUnlockDataFactory
        val createKeySettingsFactory = DefaultSecureArea.CreateKeySettingsFactory(context)
        documentManager = DocumentManagerImpl(
            storageEngine = storageEngine,
            secureArea = secureArea,
            createKeySettingsFactory = createKeySettingsFactory,
            keyUnlockDataFactory = keyUnlockDataFactory,
            checkPublicKeyBeforeAdding = false,
        )
    }

    @After
    fun tearDown() {
        storageEngine.deleteAll()
    }

    @Test
    fun test_digest_matching_on_issuer_data() {
        val unsignedDocumentResult =
            documentManager.createDocument("eu.europa.ec.eudi.pid.1", false)

        assertTrue(unsignedDocumentResult is CreateDocumentResult.Success)
        val unsignedDocument =
            (unsignedDocumentResult as CreateDocumentResult.Success).unsignedDocument

        val storeResult = documentManager.storeIssuedDocument(unsignedDocument, issuerData)
        assertTrue(storeResult is StoreDocumentResult.Success)

        val documentId = (storeResult as StoreDocumentResult.Success).documentId

        val document = documentManager.getDocumentById(documentId) as IssuedDocument
        assertNotNull(document)

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

        val issuerData = getStaticAuthDataFromDocument(document, secureArea, storageEngine)
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
        assertEquals(
            "Digest Matching",
            0,
            responseObj.documents[0].numIssuerEntryDigestMatchFailures,
        )
    }
}