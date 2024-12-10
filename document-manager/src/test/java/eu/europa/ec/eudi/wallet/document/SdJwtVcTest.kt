package eu.europa.ec.eudi.wallet.document

import com.android.identity.securearea.SecureArea
import com.android.identity.securearea.SecureAreaRepository
import com.android.identity.securearea.software.SoftwareCreateKeySettings
import com.android.identity.securearea.software.SoftwareSecureArea
import com.android.identity.storage.EphemeralStorageEngine
import com.android.identity.storage.StorageEngine
import eu.europa.ec.eudi.sdjwt.SdJwt
import eu.europa.ec.eudi.sdjwt.unverifiedIssuanceFrom
import eu.europa.ec.eudi.sdjwt.vc.SdJwtVcVerifier
import eu.europa.ec.eudi.wallet.document.format.SdJwtVcFormat
import io.mockk.InternalPlatformDsl.toStr
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertIs
import kotlin.test.assertTrue

class SdJwtVcTest {

    @Test
    fun `validate sd-jwt vc`() {
        runBlocking {
            val sdJwtVcString = getResourceAsText("sample_sd_jwt_vc.txt")
                .replace("\n", "")
                .replace("\r", "")

            val verifier = SdJwtVcVerifier.usingX5cOrIssuerMetadata(
                httpClientFactory = { mockk(relaxed = true) },
                x509CertificateTrust = { _ -> true },
            )
            val result = verifier.verifyIssuance(sdJwtVcString)
            assertTrue(result.isSuccess)
        }
    }

    @Test
    fun `parse sd-jwt vc`() {

        val sdJwtVcString = getResourceAsText("sample_sd_jwt_vc.txt")
            .replace("\n", "")
            .replace("\r", "")

        val sdJwtVc = SdJwt.unverifiedIssuanceFrom(sdJwtVcString).getOrNull()
        assertTrue(sdJwtVc != null)

        val (_, claims) = sdJwtVc.jwt
        val nonSelectivelyDisclosable = claims.filter {
            !it.value.toStr().contains("_sd") &&
                    !it.key.toStr().contains("_sd")
        }.map { it.key to it.value }

        val selectivelyDisclosable = sdJwtVc.disclosures.filter {
            !it.claim().second.toStr().contains("_sd")
        }.map { it.claim().first to it.claim().second }

        val parsedClaims = (nonSelectivelyDisclosable + selectivelyDisclosable).map {
            Claim(it.first.toStr(), it.second, selectivelyDisclosable.contains(it))
        }

        assertEquals(5, nonSelectivelyDisclosable.size)
        assertEquals(23, selectivelyDisclosable.size)
        assertEquals(28, parsedClaims.size)
    }

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
            identifier = "document_manager_1",
            storageEngine = EphemeralStorageEngine(),
            secureAreaRepository = secureAreaRepository,
            ktorHttpClientFactory = { mockk(relaxed = true) }
        ).apply {
            checkDevicePublicKey = false
        }
    }

    @AfterTest
    fun tearDown() {
        storageEngine.deleteAll()
        documentManager.getDocuments().forEach { documentManager.deleteDocumentById(it.id) }
    }


    @Test
    fun `store sd-jwt vc`() {
        // set checkDevicePublicKey to false to avoid checking the MSO key
        // since we are using fixed issuer data
        documentManager.checkDevicePublicKey = false

        val createKeySettings = SoftwareCreateKeySettings.Builder().build()

        val createDocumentResult = documentManager.createDocument(
            format = SdJwtVcFormat("urn:eu.europa.ec.eudi.pid.1"),
            createSettings = CreateDocumentSettings(
                secureAreaIdentifier = secureArea.identifier,
                createKeySettings = createKeySettings
            )
        )
        assertTrue(createDocumentResult.isSuccess)
        val unsignedDocument = createDocumentResult.getOrThrow()
        assertFalse(unsignedDocument.isCertified)

        // change document name
        unsignedDocument.name = "EU PID SD-JWT VC"

        assertIs<SdJwtVcFormat>(unsignedDocument.format)
        val documentFormat = unsignedDocument.format as SdJwtVcFormat
        assertEquals("urn:eu.europa.ec.eudi.pid.1", documentFormat.vct)
        assertFalse(unsignedDocument.isKeyInvalidated)
        assertEquals(documentManager.identifier, unsignedDocument.documentManagerId)

        val sdjwtVcData = getResourceAsText("sample_sd_jwt_vc.txt")
            .replace("\n", "")
            .replace("\r", "")

        val storeDocumentResult = documentManager.storeIssuedDocument(unsignedDocument, sdjwtVcData.toByteArray(Charsets.US_ASCII))
        assertTrue(storeDocumentResult.isSuccess)
        val issuedDocument = storeDocumentResult.getOrThrow()

        // assert that unsigned document remains unsigned
        assertEquals("EU PID SD-JWT VC", issuedDocument.name)
        assertEquals(documentManager.identifier, issuedDocument.documentManagerId)

        assertTrue(issuedDocument.isCertified)
        assertTrue(issuedDocument.issuerProvidedData.isNotEmpty())

        assertEquals(28, (issuedDocument as SdJwtVcIssuedDocument).claims.size)

        val documents = documentManager.getDocuments()
        assertEquals(1, documents.size)
        assertTrue(documents.first() is SdJwtVcIssuedDocument)
    }
}