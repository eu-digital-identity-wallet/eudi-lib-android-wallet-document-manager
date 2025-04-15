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

import eu.europa.ec.eudi.wallet.document.format.DocumentFormat
import eu.europa.ec.eudi.wallet.document.internal.toCoseBytes
import eu.europa.ec.eudi.wallet.document.internal.toEcPublicKey
import eu.europa.ec.eudi.wallet.document.metadata.IssuerMetaData
import kotlinx.coroutines.runBlocking
import org.multipaz.crypto.EcSignature
import org.multipaz.securearea.KeyInfo
import org.multipaz.securearea.KeyUnlockData
import org.multipaz.securearea.SecureArea
import java.time.Instant

/**
 * Document interface representing a document
 * @property id the document id
 * @property name the document name
 * @property format the document format
 * @property documentManagerId the [DocumentManager.identifier] related to this document
 * @property keyAlias the key alias
 * @property secureArea the secure area
 * @property createdAt the creation date
 * @property isCertified whether the document is certified
 * @property keyInfo the key info
 * @property publicKeyCoseBytes the public key cose bytes
 * @property isKeyInvalidated whether the key is invalidated
 * @property issuerMetaData the issuer metadata
 */
sealed interface Document {
    val id: DocumentId
    val name: String
    val format: DocumentFormat
    val documentManagerId: String
    val keyAlias: String
    val secureArea: SecureArea
    val createdAt: Instant
    val isCertified: Boolean

    val keyInfo: KeyInfo
        get() = runBlocking { secureArea.getKeyInfo(keyAlias) }

    val publicKeyCoseBytes: ByteArray
        get() = keyInfo.publicKey.toCoseBytes

    val isKeyInvalidated: Boolean
        get() = runBlocking { secureArea.getKeyInvalidated(keyAlias) }

    val issuerMetaData: IssuerMetaData?

    /**
     * Sign the data with the document key
     *
     * If the key is locked, the key unlock data must be provided to unlock the key
     * before signing the data. Otherwise, the method will return [Outcome] with the [EcSignature].
     *
     * @param dataToSign the data to sign
     * @param keyUnlockData the key unlock data needed to unlock the key
     * @return the sign result containing the signature or the failure
     */
    fun sign(
        dataToSign: ByteArray,
        keyUnlockData: KeyUnlockData? = null
    ): Outcome<EcSignature> {
        return runBlocking {
            try {
                val signature = secureArea.sign(
                    keyAlias,
                    dataToSign,
                    keyUnlockData
                )
                Outcome.success(signature)
            } catch (e: Throwable) {
                Outcome.failure(e)
            }
        }
    }

    /**
     * Creates a shared secret given the other party's public key
     *
     * If the key is locked, the key unlock data must be provided to unlock the key
     * before creating the shared secret. Otherwise, the method will return [Outcome] with the [SharedSecret].
     *
     * @param otherPublicKey the other party's public key
     * @param keyUnlockData the key unlock data needed to unlock the key
     * @return the shared secret result containing the shared secret or the failure
     */
    fun keyAgreement(
        otherPublicKey: ByteArray,
        keyUnlockData: KeyUnlockData? = null
    ): Outcome<SharedSecret> {
        return runBlocking {
            try {
                val sharedSecret = secureArea.keyAgreement(
                    keyAlias,
                    otherPublicKey.toEcPublicKey,
                    keyUnlockData
                )
                Outcome.success(sharedSecret)
            } catch (e: Throwable) {
                Outcome.failure(e)
            }
        }
    }
}