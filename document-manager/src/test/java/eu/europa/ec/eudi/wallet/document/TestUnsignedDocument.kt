/*
 *  Copyright (c) 2024 European Commission
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

import com.android.identity.android.securearea.AndroidKeystoreSecureArea
import com.android.identity.android.securearea.AndroidKeystoreSecureArea.KeyUnlockData
import com.android.identity.credential.Credential
import com.android.identity.securearea.SecureArea
import eu.europa.ec.eudi.wallet.document.internal.state
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkConstructor
import io.mockk.verify
import org.junit.Assert.*
import org.junit.Test
import java.security.PublicKey
import java.security.cert.X509Certificate

class UnsignedDocumentTest {

    fun getMockCredential(
        signReturn: ByteArray? = null,
        signException: Throwable? = null,
        attestationPublicKey: PublicKey? = null
    ): Credential =
        mockk<Credential> {
            every { name } returns "Credential"
            every { credentialSecureArea } returns mockk {
                every { getKeyInfo(any()) } returns mockk<AndroidKeystoreSecureArea.KeyInfo> {
                    every { isStrongBoxBacked } returns false
                    every { isUserAuthenticationRequired } returns false
                }
                every { sign(any(), any(), any(), any()) } answers {
                    if (signException != null) {
                        throw signException
                    }
                    signReturn ?: byteArrayOf()
                }
            }
            every { pendingAuthenticationKeys } returns listOf(mockk {
                every { alias } returns "alias"
                every { attestation } returns listOf(mockk<X509Certificate> {
                    every { publicKey } returns attestationPublicKey
                })
            })
            every { applicationData } returns mockk {
                every { getString(any<String>()) } returns ""
                every { getNumber("state") } returns Document.State.UNSIGNED.value
                every { getNumber(any<String>()) } returns 0L
                every { getBoolean(any<String>()) } returns false
                every { setString(any(), any()) } returns this
            }
        }

    @Test
    fun `signWithAuthKey returns Success when credential is initialized and signing is successful`() {

        val data = "test data".toByteArray()
        val expectedSignature = byteArrayOf(1, 2, 3)
        val mockCredential = getMockCredential(expectedSignature)
        val unsignedDocument = UnsignedDocument(mockCredential)

        val result = unsignedDocument.signWithAuthKey(data)

        assertTrue(result is SignedWithAuthKeyResult.Success)
        assertArrayEquals(expectedSignature, (result as SignedWithAuthKeyResult.Success).signature)
    }

    @Test
    fun `signWithAuthKey returns UserAuthRequired when key is locked`() {
        mockkConstructor(AndroidKeystoreSecureArea.KeyUnlockData::class)
        every { anyConstructed<KeyUnlockData>().getCryptoObjectForSigning(any()) } returns mockk()
        val mockCredential = getMockCredential(signException = SecureArea.KeyLockedException())
        val unsignedDocument = UnsignedDocument.invoke(mockCredential)
        val data = "test data".toByteArray()

        val result = unsignedDocument.signWithAuthKey(data)

        assertTrue(result is SignedWithAuthKeyResult.UserAuthRequired)
    }

    @Test
    fun `signWithAuthKey returns Failure when an unexpected error occurs`() {

        val data = "test data".toByteArray()
        val exception = RuntimeException("Unexpected error")
        val mockCredential = getMockCredential(signException = exception)
        val unsignedDocument = UnsignedDocument.invoke(mockCredential)

        val result = unsignedDocument.signWithAuthKey(data)

        assertTrue(result is SignedWithAuthKeyResult.Failure)
        assertEquals(exception, (result as SignedWithAuthKeyResult.Failure).throwable)
    }

    @Test
    fun `publicKey returns the first certificate's public key`() {
        val mockPublicKey = mockk<PublicKey>()
        val mockCredential = getMockCredential(attestationPublicKey = mockPublicKey)
        val unsignedDocument = UnsignedDocument.invoke(mockCredential)

        val publicKey = unsignedDocument.publicKey

        assertSame(mockPublicKey, publicKey)
    }

    @Test
    fun `name is updated correctly when setting a new value`() {
        val mockCredential = getMockCredential()

        val unsignedDocument = UnsignedDocument.invoke(mockCredential)
        val newName = "New Document Name"

        unsignedDocument.name = newName

        assertEquals(newName, unsignedDocument.name)
        val applicationData = mockCredential.applicationData
        verify(exactly = 1) { applicationData.setString("name", newName) }
    }

    @Test
    fun `state returns the credential's state`() {
        val mockCredential = getMockCredential()
        val unsignedDocument = UnsignedDocument.invoke(mockCredential)

        val state = unsignedDocument.state

        assertEquals(mockCredential.state, state)
    }
}