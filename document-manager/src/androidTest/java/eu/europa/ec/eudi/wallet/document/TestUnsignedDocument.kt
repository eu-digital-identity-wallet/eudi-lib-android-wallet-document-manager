package eu.europa.ec.eudi.wallet.document

import android.content.Context
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.android.identity.android.securearea.AndroidKeystoreCreateKeySettings
import com.android.identity.android.securearea.AndroidKeystoreSecureArea
import com.android.identity.android.securearea.UserAuthenticationType
import com.android.identity.credential.CredentialFactory
import com.android.identity.crypto.EcCurve
import com.android.identity.crypto.toEcPublicKey
import com.android.identity.document.DocumentStore
import com.android.identity.mdoc.credential.MdocCredential
import com.android.identity.securearea.KeyPurpose
import com.android.identity.securearea.SecureAreaRepository
import com.android.identity.storage.EphemeralStorageEngine
import com.android.identity.util.UUID
import eu.europa.ec.eudi.wallet.document.internal.createdAt
import eu.europa.ec.eudi.wallet.document.internal.docType
import eu.europa.ec.eudi.wallet.document.internal.documentName
import eu.europa.ec.eudi.wallet.document.internal.isDeviceSecure
import eu.europa.ec.eudi.wallet.document.internal.state
import kotlinx.datetime.Clock
import kotlinx.datetime.toJavaInstant
import org.bouncycastle.jce.provider.BouncyCastleProvider
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Assume.assumeTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.security.Security
import java.security.Signature


@RunWith(AndroidJUnit4::class)
class TestUnsignedDocument {

    private val context: Context
        get() = InstrumentationRegistry.getInstrumentation().targetContext
    private lateinit var secureArea: AndroidKeystoreSecureArea
    private lateinit var documentStore: DocumentStore

    @Before
    fun setUp() {
        Security.removeProvider(BouncyCastleProvider.PROVIDER_NAME)
        Security.insertProviderAt(BouncyCastleProvider(), 1)
        val storageEngine = EphemeralStorageEngine()
        secureArea = AndroidKeystoreSecureArea(context, storageEngine)
        val secureAreaRepository = SecureAreaRepository().apply {
            addImplementation(secureArea)
        }
        val credentialFactory = CredentialFactory().apply {
            addCredentialImplementation(MdocCredential::class) { document, dataItem ->
                MdocCredential(document, dataItem)
            }
        }
        documentStore = DocumentStore(storageEngine, secureAreaRepository, credentialFactory)
    }

    @After
    fun tearDown() {
        documentStore.listDocuments().forEach { documentStore.deleteDocument(it) }
    }

    private fun getUnsignedDocument(requireUserAuth: Boolean = false): UnsignedDocument {
        // Given
        val documentId = UUID.randomUUID().toString()
        val baseDocument = documentStore.createDocument(documentId).apply {
            state = Document.State.UNSIGNED
            documentName = "test-document"
            docType = "type.test-document"
            createdAt = Clock.System.now().toJavaInstant()
        }
        val attestationChallenge = "attestation-challenge".toByteArray()
        MdocCredential(
            document = baseDocument,
            asReplacementFor = null,
            domain = "domain",
            secureArea = secureArea,
            createKeySettings = AndroidKeystoreCreateKeySettings.Builder(attestationChallenge)
                .setKeyPurposes(setOf(KeyPurpose.SIGN))
                .setEcCurve(EcCurve.P256)
                .setUseStrongBox(false)
                .setUserAuthenticationRequired(
                    requireUserAuth,
                    10_000L,
                    setOf(UserAuthenticationType.LSKF)
                )
                .build(),
            docType = "type.test-document"
        )
        documentStore.addDocument(baseDocument)
        val unsignedDocument = UnsignedDocument(baseDocument)

        return unsignedDocument
    }

    @Test
    fun signWithAuthKey_returns_Success_when_credential_is_initialized_and_signing_is_successful() {
        // Given
        val unsignedDocument = getUnsignedDocument()
        val dataToSign = "test data".toByteArray()

        // When
        val result =
            unsignedDocument.signWithAuthKey(dataToSign, Algorithm.SHA256withECDSA)

        // Then
        assertTrue(result is SignedWithAuthKeyResult.Success)

        val signature = (result as SignedWithAuthKeyResult.Success).signature
        assertTrue(signature.isNotEmpty())

        Signature.getInstance(Algorithm.SHA256withECDSA).apply {
            initVerify(unsignedDocument.certificatesNeedAuth.first().publicKey)
            update(dataToSign)
        }.verify(signature)
    }

    @Test
    fun signWithAuthKey_returns_UserAuthRequired_when_key_is_locked() {
        // Given
        assumeTrue(context.isDeviceSecure)
        val unsignedDocument = getUnsignedDocument(requireUserAuth = true)

        // When
        val result = unsignedDocument.signWithAuthKey("test data".toByteArray())

        // Then
        assertTrue(result is SignedWithAuthKeyResult.UserAuthRequired)
    }

    @Test
    fun publicKey_returns_the_public_key_of_first_certificate() {
        // Given
        val unsignedDocument = getUnsignedDocument()

        // When
        val publicKey = unsignedDocument.publicKey

        // Then
        assertNotNull(publicKey)
        assertEquals(unsignedDocument.certificatesNeedAuth.first().publicKey, publicKey)
        val baseDocument = unsignedDocument.base
        assertNotNull(baseDocument)
        baseDocument!!
        val attestation = baseDocument.pendingCredentials.firstOrNull() { it is MdocCredential }
            ?.let { it as MdocCredential }
            ?.attestation
        assertNotNull(attestation)
        attestation!!
        assertEquals(publicKey.toEcPublicKey(attestation.publicKey.curve), attestation.publicKey)
    }

    @Test
    fun name_is_updated_correctly_when_setting_a_new_value() {
        // Given
        val unsignedDocument = getUnsignedDocument()
        val newName = "New Document Name"

        // When
        unsignedDocument.name = newName

        // Then
        assertEquals(newName, unsignedDocument.name)
        val baseDocument = unsignedDocument.base
        assertNotNull(baseDocument)
        baseDocument!!
        assertTrue(baseDocument.applicationData.keyExists("name"))
        val documentName = baseDocument.applicationData.getString("name")
        assertEquals(newName, documentName)
    }

    @Test
    fun state_returns_the_state_from_application_data() {
        // Given
        val unsignedDocument = getUnsignedDocument()

        // Then
        assertEquals(Document.State.UNSIGNED, unsignedDocument.state)
        val baseDocument = unsignedDocument.base
        assertNotNull(baseDocument)
        baseDocument!!
        assertTrue(baseDocument.applicationData.keyExists("state"))
        val state = baseDocument.applicationData.getNumber("state").toInt()
        assertEquals(Document.State.UNSIGNED.ordinal, state)
    }

}