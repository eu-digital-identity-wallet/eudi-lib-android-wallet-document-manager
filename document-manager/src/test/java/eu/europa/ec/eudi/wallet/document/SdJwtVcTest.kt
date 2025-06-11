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

import android.util.Log
import eu.europa.ec.eudi.sdjwt.DefaultSdJwtOps
import eu.europa.ec.eudi.sdjwt.DefaultSdJwtOps.recreateClaimsAndDisclosuresPerClaim
import eu.europa.ec.eudi.sdjwt.vc.SelectPath.Default.select
import eu.europa.ec.eudi.wallet.document.credential.IssuerProvidedCredential
import eu.europa.ec.eudi.wallet.document.format.MutableSdJwtClaim
import eu.europa.ec.eudi.wallet.document.format.SdJwtVcData
import eu.europa.ec.eudi.wallet.document.format.SdJwtVcFormat
import eu.europa.ec.eudi.wallet.document.internal.parse
import io.mockk.clearAllMocks
import io.mockk.clearMocks
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkObject
import io.mockk.mockkStatic
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runTest
import org.multipaz.securearea.SecureArea
import org.multipaz.securearea.SecureAreaRepository
import org.multipaz.securearea.software.SoftwareCreateKeySettings
import org.multipaz.securearea.software.SoftwareSecureArea
import org.multipaz.storage.Storage
import org.multipaz.storage.ephemeral.EphemeralStorage
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Ignore
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertIs
import kotlin.test.assertTrue

class SdJwtVcTest {

    @BeforeTest
    fun setup() {
        mockkStatic(Log::class)
        every { Log.v(any(), any()) } returns 0
        every { Log.v(any(), any(), any()) } returns 0
        every { Log.d(any(), any()) } returns 0
        every { Log.d(any(), any(), any()) } returns 0
        every { Log.i(any(), any()) } returns 0
        every { Log.i(any(), any(), any()) } returns 0
        every { Log.e(any(), any()) } returns 0
        every { Log.e(any(), any(), any()) } returns 0
        every { Log.w(any(), any(), any()) } returns 0
    }

    @AfterTest
    fun tearDown() {
        clearAllMocks()
        documentManager.getDocuments().forEach { documentManager.deleteDocumentById(it.id) }
    }

    @Test
    @Ignore("This test is failing because of invalid sd jwt vc, need to fix the test")
    fun `validate sd-jwt vc`() {
        runBlocking {
            val sdJwtVcString = getResourceAsText("sample_sd_jwt_vc.txt")
                .replace("\n", "")
                .replace("\r", "")
            val verifier = DefaultSdJwtOps.SdJwtVcVerifier.usingX5cOrIssuerMetadata(
                httpClientFactory = { mockk(relaxed = true) },
                x509CertificateTrust = { _ -> true },
            )
            val result = verifier.verify(sdJwtVcString)
            assertTrue(result.isSuccess)
        }
    }

    @Test
    @Ignore("This is test for parsing the sd-jwt vc. See eu.europa.ec.eudi.wallet.document.format.SdJwtVcDataTest for the actual test")
    fun `parse sd-jwt vc`() {

        val sdJwtVcString = getResourceAsText("sample_sd_jwt_vc.txt")
            .replace("\n", "")
            .replace("\r", "")

        val sdJwtVc = DefaultSdJwtOps.unverifiedIssuanceFrom(sdJwtVcString).getOrNull()
        assertTrue(sdJwtVc != null)

        val (claims, disclosuresPerClaim) = sdJwtVc.recreateClaimsAndDisclosuresPerClaim()
        val excluded = arrayOf("iss")

        val claimValueList = disclosuresPerClaim.map {
            println(
                "Path: ${it.key}, Value: ${
                    claims.select(it.key).getOrNull()
                }, SelectivelyDisclosable: ${it.value.isNotEmpty()}"
            )
            Triple(it.key, claims.select(it.key).getOrNull(), it.value.isNotEmpty())
        }.filterNot { it.first.head().toString() in excluded }

        val sdJwtVcClaims = mutableListOf<MutableSdJwtClaim>()
        for ((path, value, selectivelyDisclosable) in claimValueList) {
            var current = sdJwtVcClaims
            for (key in path.value) {
                val existingNode = current.find { it.identifier == key.toString() }
                if (existingNode != null) {
                    current = existingNode.children
                } else {
                    val newClaim = MutableSdJwtClaim(
                        identifier = key.toString(),
                        value = value?.parse(),
                        rawValue = value?.toString() ?: "",
                        selectivelyDisclosable = selectivelyDisclosable,
                        metadata = null
                    )
                    current.add(newClaim)
                    current = newClaim.children
                }
            }
        }

        printSdJwtVcClaims(sdJwtVcClaims)

        assertEquals(claims.filterNot { it.key in excluded }.size, sdJwtVcClaims.size)
        assertEquals(
            claimValueList.size,
            getSdJwtClaims(sdJwtVcClaims).size
        )
    }

    lateinit var documentManager: DocumentManagerImpl
    lateinit var storage: Storage
    lateinit var secureArea: SecureArea
    lateinit var secureAreaRepository: SecureAreaRepository

    @BeforeTest
    fun setUp() {
        storage = EphemeralStorage()
        secureArea = runBlocking { SoftwareSecureArea.create(storage) }
        secureAreaRepository = SecureAreaRepository.Builder()
            .add(secureArea)
            .build()
        documentManager = DocumentManagerImpl(
            identifier = "document_manager_1",
            storage = EphemeralStorage(),
            secureAreaRepository = secureAreaRepository,
            ktorHttpClientFactory = { mockk(relaxed = true) }
        ).apply {
            checkDevicePublicKey = false
        }
    }

    @Test
    fun `store sd-jwt vc`() = runTest {
        // set checkDevicePublicKey to false to avoid checking the MSO key
        // since we are using fixed issuer data
        documentManager.checkDevicePublicKey = false

        val createKeySettings = SoftwareCreateKeySettings.Builder().build()

        val createDocumentResult = documentManager.createDocument(
            format = SdJwtVcFormat("urn:eu.europa.ec.eudi.pid.1"),
            createSettings = CreateDocumentSettings(
                secureAreaIdentifier = secureArea.identifier,
                createKeySettings = createKeySettings,
                numberOfCredentials = 1
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
        assertEquals(documentManager.identifier, unsignedDocument.documentManagerId)

        val sdjwtVcData = getResourceAsText("sample_sd_jwt_vc.txt")
            .replace("\n", "")
            .replace("\r", "")

        val issuerData = unsignedDocument.getPoPSigners().map {
            IssuerProvidedCredential(
                publicKeyAlias = it.keyAlias,
                data = sdjwtVcData.toByteArray(Charsets.US_ASCII),
            )
        }

        val storeDocumentResult = documentManager.storeIssuedDocument(
            unsignedDocument,
            issuerData
        )
        assertTrue(storeDocumentResult.isSuccess)
        val issuedDocument = storeDocumentResult.getOrThrow()

        assertEquals("EU PID SD-JWT VC", issuedDocument.name)
        assertEquals(documentManager.identifier, issuedDocument.documentManagerId)

        val claims = issuedDocument.data
        assertIs<SdJwtVcData>(claims)
        assertEquals(19, claims.claims.size)

        val documents = documentManager.getDocuments()
        assertEquals(1, documents.size)
        assertIs<SdJwtVcFormat>(documents.first().format)
    }

    private fun printSdJwtVcClaims(sdJwtVcClaims: List<MutableSdJwtClaim>, indent: String = "") {
        for (sdJwtVcClaim in sdJwtVcClaims) {
            println("$indent- Identifier: ${sdJwtVcClaim.identifier}")
            if (sdJwtVcClaim.value != null) println("$indent  Value: ${sdJwtVcClaim.value}")
            if (sdJwtVcClaim.rawValue.isNotEmpty()) println("$indent  Raw Value: ${sdJwtVcClaim.rawValue}")
            println("$indent  Selectively Disclosable: ${sdJwtVcClaim.selectivelyDisclosable}")
            if (sdJwtVcClaim.metadata != null) println("$indent  Metadata: ${sdJwtVcClaim.metadata}")
            if (sdJwtVcClaim.children.isNotEmpty()) {
                println("$indent  Children:")
                printSdJwtVcClaims(sdJwtVcClaim.children, "$indent    ")
            }
        }
    }

    private fun getSdJwtClaims(claims: List<MutableSdJwtClaim>): List<MutableSdJwtClaim> {
        return claims.flatMap { claim ->
            listOf(claim) + getSdJwtClaims(claim.children)
        }
    }
}