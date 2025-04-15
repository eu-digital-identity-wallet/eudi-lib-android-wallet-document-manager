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

import eu.europa.ec.eudi.wallet.document.format.MsoMdocData
import eu.europa.ec.eudi.wallet.document.format.MsoMdocFormat
import eu.europa.ec.eudi.wallet.document.internal.toCoseBytes
import kotlinx.coroutines.runBlocking
import kotlinx.datetime.Clock
import kotlinx.datetime.toJavaInstant
import org.multipaz.crypto.Algorithm
import org.multipaz.crypto.Crypto
import org.multipaz.crypto.EcCurve
import org.multipaz.securearea.KeyLockedException
import org.multipaz.securearea.PassphraseConstraints
import org.multipaz.securearea.SecureAreaRepository
import org.multipaz.securearea.software.SoftwareCreateKeySettings
import org.multipaz.securearea.software.SoftwareKeyUnlockData
import org.multipaz.securearea.software.SoftwareSecureArea
import org.multipaz.storage.ephemeral.EphemeralStorage
import kotlin.test.Test
import kotlin.test.assertContentEquals
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertIs
import kotlin.test.assertNotEquals
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

class DocumentTest {

    val storageFixture = EphemeralStorage()
    val secureAreaFixture = runBlocking { SoftwareSecureArea.create(storageFixture) }
    val secureAreaRepositoryFixture = SecureAreaRepository.build {
        add(secureAreaFixture)
    }

    @Test
    fun `verify properties set correctly`() {
        val documentManagerId = "document_manager"
        val documentManager = DocumentManager {
            setIdentifier(documentManagerId)
            setStorage(storageFixture)
            setSecureAreaRepository(secureAreaRepositoryFixture)
        }

        val documentFormat = MsoMdocFormat(docType = "eu.europa.ec.eudi.pid.1")
        val unsignedDocument = documentManager.createDocument(
            format = documentFormat,
            createSettings = CreateDocumentSettings(
                secureAreaIdentifier = secureAreaFixture.identifier,
                createKeySettings = SoftwareCreateKeySettings.Builder().build()
            )
        ).getOrThrow()

        assertEquals(documentManagerId, unsignedDocument.documentManagerId)
        assertEquals(secureAreaFixture, unsignedDocument.secureArea)
        assertEquals(documentFormat, unsignedDocument.format)
        assertFalse(unsignedDocument.isCertified)
        assertFalse(unsignedDocument.isKeyInvalidated)
        assertEquals(
            runBlocking { secureAreaFixture.getKeyInfo(unsignedDocument.keyAlias).publicKey },
            unsignedDocument.keyInfo.publicKey
        )
    }

    @OptIn(ExperimentalStdlibApi::class)
    @Test
    fun `verify issued document properties`() {
        val documentManagerId = "document_manager"
        val documentManager = DocumentManager {
            setIdentifier(documentManagerId)
            setStorage(storageFixture)
            setSecureAreaRepository(secureAreaRepositoryFixture)
        }

        val documentFormat = MsoMdocFormat(docType = "eu.europa.ec.eudi.pid.1")
        val unsignedDocument = documentManager.createDocument(
            format = documentFormat,
            createSettings = CreateDocumentSettings(
                secureAreaIdentifier = secureAreaFixture.identifier,
                createKeySettings = SoftwareCreateKeySettings.Builder().build()
            )
        ).getOrThrow()

        val issuerData = getResourceAsText("eu_pid.hex").hexToByteArray(HexFormat.Default)
        // disable mso check since we are providing sample data
        (documentManager as DocumentManagerImpl).checkDevicePublicKey = false
        val issuedDocument =
            documentManager.storeIssuedDocument(unsignedDocument, issuerData).getOrThrow()

        assertNotEquals(unsignedDocument as Document, issuedDocument as Document)

        assertEquals(unsignedDocument.createdAt, issuedDocument.createdAt)
        assertNotEquals(issuedDocument.createdAt, issuedDocument.issuedAt)
        val claims = issuedDocument.data
        assertIs<MsoMdocData>(claims)
        assertTrue(claims.nameSpaces.isNotEmpty())
        assertTrue(issuedDocument.validFrom.isBefore(issuedDocument.validUntil))
        assertTrue(issuedDocument.isValidAt(Clock.System.now().toJavaInstant()))
        assertTrue(issuedDocument.issuerProvidedData.isNotEmpty())
        assertTrue(
            claims.nameSpacedData.hasDataElement(
                "eu.europa.ec.eudi.pid.1",
                "given_name"
            )
        )
    }

    @Test
    fun `verify deferred document properties`() {
        val documentManagerId = "document_manager"
        val documentManager = DocumentManager {
            setIdentifier(documentManagerId)
            setStorage(storageFixture)
            setSecureAreaRepository(secureAreaRepositoryFixture)
        }

        val documentFormat = MsoMdocFormat(docType = "eu.europa.ec.eudi.pid.1")
        val unsignedDocument = documentManager.createDocument(
            format = documentFormat,
            createSettings = CreateDocumentSettings(
                secureAreaIdentifier = secureAreaFixture.identifier,
                createKeySettings = SoftwareCreateKeySettings.Builder().build()
            )
        ).getOrThrow()

        val deferredRelatedData = byteArrayOf(0x01, 0x02, 0x03)

        val deferredDocument = documentManager.storeDeferredDocument(
            unsignedDocument,
            deferredRelatedData
        ).getOrThrow()

        assertNotEquals(unsignedDocument as Document, deferredDocument as Document)
        assertEquals(unsignedDocument.createdAt, deferredDocument.createdAt)
        assertEquals(unsignedDocument.documentManagerId, deferredDocument.documentManagerId)
        assertEquals(unsignedDocument.format, deferredDocument.format)
        assertFalse(deferredDocument.isCertified)
        assertContentEquals(deferredRelatedData, deferredDocument.relatedData)
    }

    @Test
    fun `document sign method should return key locked result if key usage requires unlocking`() {
        val documentManagerId = "document_manager"
        val documentManager = DocumentManager {
            setIdentifier(documentManagerId)
            setStorage(storageFixture)
            setSecureAreaRepository(secureAreaRepositoryFixture)
        }

        val documentFormat = MsoMdocFormat(docType = "eu.europa.ec.eudi.pid.1")
        val unsignedDocument = documentManager.createDocument(
            format = documentFormat,
            createSettings = CreateDocumentSettings(
                secureAreaIdentifier = secureAreaFixture.identifier,
                createKeySettings = SoftwareCreateKeySettings.Builder()
                    .setPassphraseRequired(true, "1234", PassphraseConstraints.PIN_FOUR_DIGITS)
                    .build()
            )
        ).getOrThrow()

        val dataToSign = byteArrayOf(1, 2, 3)
        val signResult = unsignedDocument.sign(dataToSign)
        assertTrue(signResult.isFailure)
        val failure = signResult.exceptionOrNull()
        assertIs<KeyLockedException>(failure)
    }

    @Test
    fun `document sign method should return failure when fails to sign`() {
        val documentManagerId = "document_manager"
        val documentManager = DocumentManager {
            setIdentifier(documentManagerId)
            setStorage(storageFixture)
            setSecureAreaRepository(secureAreaRepositoryFixture)
        }
        val documentFormat = MsoMdocFormat(docType = "eu.europa.ec.eudi.pid.1")
        val unsignedDocument = documentManager.createDocument(
            format = documentFormat,
            createSettings = CreateDocumentSettings(
                secureAreaIdentifier = secureAreaFixture.identifier,
                createKeySettings = SoftwareCreateKeySettings.Builder()
                    .setAlgorithm(Algorithm.ECDH_P256)
                    .build()
            )
        ).getOrThrow()

        val dataToSign = byteArrayOf(1, 2, 3)
        val signResult = unsignedDocument.sign(dataToSign)
        assertTrue(signResult.isFailure)
    }

    @Test
    fun `document sign method should create a valid signature`() {
        val documentManagerId = "document_manager"
        val documentManager = DocumentManager {
            setIdentifier(documentManagerId)
            setStorage(storageFixture)
            setSecureAreaRepository(secureAreaRepositoryFixture)
        }
        val documentFormat = MsoMdocFormat(docType = "eu.europa.ec.eudi.pid.1")
        val unsignedDocument = documentManager.createDocument(
            format = documentFormat,
            createSettings = CreateDocumentSettings(
                secureAreaIdentifier = secureAreaFixture.identifier,
                createKeySettings = SoftwareCreateKeySettings.Builder().build()
            )
        ).getOrThrow()
        val dataToSign = byteArrayOf(1, 2, 3)
        val signResult = unsignedDocument.sign(dataToSign)
        assertTrue(signResult.isSuccess)
        assertNotNull(signResult.getOrNull())
        val signature = signResult.getOrThrow()

        // Verify the signature
        val publicKey = unsignedDocument.keyInfo.publicKey
        val isValid = runCatching {
            Crypto.checkSignature(publicKey, dataToSign, Algorithm.ES256, signature)
        }.isSuccess
        assertTrue(isValid)
    }

    @Test
    fun `document sign method should return result success when locked key and passing the keyUnlockData`() {
        val documentManagerId = "document_manager"
        val documentManager = DocumentManager {
            setIdentifier(documentManagerId)
            setStorage(storageFixture)
            setSecureAreaRepository(secureAreaRepositoryFixture)
        }
        val documentFormat = MsoMdocFormat(docType = "eu.europa.ec.eudi.pid.1")
        val unsignedDocument = documentManager.createDocument(
            format = documentFormat,
            createSettings = CreateDocumentSettings(
                secureAreaIdentifier = secureAreaFixture.identifier,
                createKeySettings = SoftwareCreateKeySettings.Builder()
                    .setPassphraseRequired(true, "1234", PassphraseConstraints.PIN_FOUR_DIGITS)
                    .build()
            )
        ).getOrThrow()

        val dataToSign = byteArrayOf(1, 2, 3)
        val keyUnlockData = SoftwareKeyUnlockData(passphrase = "1234")
        val signResult = unsignedDocument.sign(dataToSign, keyUnlockData = keyUnlockData)
        assertTrue(signResult.isSuccess)
        assertNotNull(signResult.getOrNull())
        val signature = signResult.getOrThrow()

        // Verify the signature
        val publicKey = unsignedDocument.keyInfo.publicKey
        val isValid = runCatching {
            Crypto.checkSignature(publicKey, dataToSign, Algorithm.ES256, signature)
        }.isSuccess
        assertTrue(isValid)
    }

    @Test
    fun `document keyAgreement method should return key locked result if key usage requires unlocking`() {
        val documentManagerId = "document_manager"
        val documentManager = DocumentManager {
            setIdentifier(documentManagerId)
            setStorage(storageFixture)
            setSecureAreaRepository(secureAreaRepositoryFixture)
        }

        val documentFormat = MsoMdocFormat(docType = "eu.europa.ec.eudi.pid.1")
        val unsignedDocument = documentManager.createDocument(
            format = documentFormat,
            createSettings = CreateDocumentSettings(
                secureAreaIdentifier = secureAreaFixture.identifier,
                createKeySettings = SoftwareCreateKeySettings.Builder()
                    .setPassphraseRequired(true, "1234", PassphraseConstraints.PIN_FOUR_DIGITS)
                    .build()
            )
        ).getOrThrow()

        val otherPrivateKey = Crypto.createEcPrivateKey(EcCurve.P256)
        val otherPublicKey = otherPrivateKey.publicKey.toCoseBytes

        val sharedSecretResult = unsignedDocument.keyAgreement(
            otherPublicKey = otherPublicKey,
            keyUnlockData = null
        )
        assertTrue(sharedSecretResult.isFailure)
        val failure = sharedSecretResult.exceptionOrNull()
        assertIs<KeyLockedException>(failure)
    }

    @Test
    fun `document keyAgreement method should return failure when fails to run`() {
        val documentManagerId = "document_manager"
        val documentManager = DocumentManager {
            setIdentifier(documentManagerId)
            setStorage(storageFixture)
            setSecureAreaRepository(secureAreaRepositoryFixture)
        }
        val documentFormat = MsoMdocFormat(docType = "eu.europa.ec.eudi.pid.1")
        val unsignedDocument = documentManager.createDocument(
            format = documentFormat,
            createSettings = CreateDocumentSettings(
                secureAreaIdentifier = secureAreaFixture.identifier,
                createKeySettings = SoftwareCreateKeySettings.Builder()
                    .build()
            )
        ).getOrThrow()

        val otherPrivateKey = Crypto.createEcPrivateKey(EcCurve.P256)
        val otherPublicKey = otherPrivateKey.publicKey.toCoseBytes

        val sharedSecretResult = unsignedDocument.keyAgreement(
            otherPublicKey = otherPublicKey,
            keyUnlockData = null
        )

        assertTrue(sharedSecretResult.isFailure)
    }

    @Test
    fun `document keyAgreement method should create a valid sharedSecret`() {
        val documentManagerId = "document_manager"
        val documentManager = DocumentManager {
            setIdentifier(documentManagerId)
            setStorage(storageFixture)
            setSecureAreaRepository(secureAreaRepositoryFixture)
        }
        val documentFormat = MsoMdocFormat(docType = "eu.europa.ec.eudi.pid.1")
        val unsignedDocument = documentManager.createDocument(
            format = documentFormat,
            createSettings = CreateDocumentSettings(
                secureAreaIdentifier = secureAreaFixture.identifier,
                createKeySettings = SoftwareCreateKeySettings.Builder()
                    .setAlgorithm(Algorithm.ECDH_P256)
                    .build()
            )
        ).getOrThrow()

        val otherPrivateKey = Crypto.createEcPrivateKey(EcCurve.P256)
        val otherPublicKey = otherPrivateKey.publicKey.toCoseBytes

        val sharedSecret = unsignedDocument.keyAgreement(
            otherPublicKey = otherPublicKey,
            keyUnlockData = null
        ).getOrThrow()

        // Verify the shared secret
        val expectedSharedSecret = Crypto.keyAgreement(
            otherPrivateKey,
            unsignedDocument.keyInfo.publicKey
        )

        assertContentEquals(expectedSharedSecret, sharedSecret)
    }

    @Test
    fun `document keyAgreement method should return result success when locked key and passing the keyUnlockData`() {
        val documentManagerId = "document_manager"
        val documentManager = DocumentManager {
            setIdentifier(documentManagerId)
            setStorage(storageFixture)
            setSecureAreaRepository(secureAreaRepositoryFixture)
        }
        val documentFormat = MsoMdocFormat(docType = "eu.europa.ec.eudi.pid.1")
        val unsignedDocument = documentManager.createDocument(
            format = documentFormat,
            createSettings = CreateDocumentSettings(
                secureAreaIdentifier = secureAreaFixture.identifier,
                createKeySettings = SoftwareCreateKeySettings.Builder()
                    .setAlgorithm(Algorithm.ECDH_P256)
                    .setPassphraseRequired(true, "1234", PassphraseConstraints.PIN_FOUR_DIGITS)
                    .build()
            )
        ).getOrThrow()

        val otherPrivateKey = Crypto.createEcPrivateKey(EcCurve.P256)
        val otherPublicKey = otherPrivateKey.publicKey.toCoseBytes
        val keyUnlockData = SoftwareKeyUnlockData(passphrase = "1234")
        val sharedSecret = unsignedDocument.keyAgreement(
            otherPublicKey = otherPublicKey,
            keyUnlockData = keyUnlockData
        ).getOrThrow()

        // Verify the shared secret
        val expectedSharedSecret = Crypto.keyAgreement(
            otherPrivateKey,
            unsignedDocument.keyInfo.publicKey
        )

        assertContentEquals(expectedSharedSecret, sharedSecret)
    }
}