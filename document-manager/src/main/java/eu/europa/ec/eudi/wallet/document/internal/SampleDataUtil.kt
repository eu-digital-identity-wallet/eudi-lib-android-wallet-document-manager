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
package eu.europa.ec.eudi.wallet.document.internal

import COSE.AlgorithmID.ECDSA_256
import COSE.HeaderKeys.Algorithm
import COSE.OneKey
import COSE.Sign1Message
import com.android.identity.mdoc.mso.MobileSecurityObjectGenerator
import com.android.identity.util.Timestamp
import com.upokecenter.cbor.CBORObject
import org.bouncycastle.jce.provider.BouncyCastleProvider
import org.bouncycastle.util.io.pem.PemReader
import java.security.KeyFactory
import java.security.MessageDigest
import java.security.PrivateKey
import java.security.PublicKey
import java.security.cert.CertificateFactory
import java.security.cert.X509Certificate
import java.security.spec.PKCS8EncodedKeySpec

private val SAMPLE_ISSUER_PRIVATE_KEY = """
-----BEGIN PRIVATE KEY-----
MIGHAgEAMBMGByqGSM49AgEGCCqGSM49AwEHBG0wawIBAQQgnBYfGAK5qBPHdxzB
Jp6Vot36QHrcobpJgpdjLnPnpJuhRANCAAQUy08Z80ZGi7kfmRM45JQIIZlJgkAJ
snZ6+Wap8GdbTK/KTJwCwFII2LdF+Ah721aLdguMndLerWeWeAB48gfl
-----END PRIVATE KEY-----
""".trimIndent()

private val SAMPLE_ISSUER_DS = """
-----BEGIN CERTIFICATE-----
MIIC8jCCAnmgAwIBAgIUdlvsblwDAwqQ68tR+6/BKp9pZYcwCgYIKoZIzj0EAwIw
XDEeMBwGA1UEAwwVUElEIElzc3VlciBDQSAtIEVVIDAxMS0wKwYDVQQKDCRFVURJ
IFdhbGxldCBSZWZlcmVuY2UgSW1wbGVtZW50YXRpb24xCzAJBgNVBAYTAkVVMB4X
DTI0MDcyNTA5MjEyM1oXDTI1MTAxODA5MjEyMlowYzElMCMGA1UEAwwcUElEIERT
IGZvciBzYW1wbGUgZGF0YSAtIDAwMTEtMCsGA1UECgwkRVVESSBXYWxsZXQgUmVm
ZXJlbmNlIEltcGxlbWVudGF0aW9uMQswCQYDVQQGEwJFVTBZMBMGByqGSM49AgEG
CCqGSM49AwEHA0IABBTLTxnzRkaLuR+ZEzjklAghmUmCQAmydnr5ZqnwZ1tMr8pM
nALAUgjYt0X4CHvbVot2C4yd0t6tZ5Z4AHjyB+WjggEQMIIBDDAfBgNVHSMEGDAW
gBRBi2F24YyB3D+yX1Y//myyBoHgETAWBgNVHSUBAf8EDDAKBggrgQICAAABAjBD
BgNVHR8EPDA6MDigNqA0hjJodHRwczovL3ByZXByb2QucGtpLmV1ZGl3LmRldi9j
cmwvcGlkX0NBX0VVXzAxLmNybDAdBgNVHQ4EFgQUgh5Al9G9ATfrSHVDRibkzf7t
l7cwDgYDVR0PAQH/BAQDAgeAMF0GA1UdEgRWMFSGUmh0dHBzOi8vZ2l0aHViLmNv
bS9ldS1kaWdpdGFsLWlkZW50aXR5LXdhbGxldC9hcmNoaXRlY3R1cmUtYW5kLXJl
ZmVyZW5jZS1mcmFtZXdvcmswCgYIKoZIzj0EAwIDZwAwZAIweeQAWuqYHq6hwPF/
5szZ840aaVFTa1J7FUxvayEQW6QMa50qJ/7HczjkD4OvoSKHAjAuZ/I4rChjdeNF
wibtidg8cLtZ7sCW59rUvdnz5wvo3VXndZi3sj0jf8CAKZZZNTE=
-----END CERTIFICATE-----
""".trimIndent()

private val bc = BouncyCastleProvider()

@get:JvmSynthetic
internal val issuerPrivateKey: PrivateKey = PemReader(SAMPLE_ISSUER_PRIVATE_KEY.reader())
    .use { reader -> reader.readPemObject().content }
    .let { privateKeyBytes ->
        KeyFactory.getInstance("EC", bc)
            .generatePrivate(PKCS8EncodedKeySpec(privateKeyBytes))
    }

@get:JvmSynthetic
internal val issuerCertificate: X509Certificate = PemReader(SAMPLE_ISSUER_DS.reader())
    .use { reader -> reader.readPemObject().content }
    .let { certificateBytes ->
        CertificateFactory.getInstance("X.509", bc)
            .generateCertificate(certificateBytes.inputStream())
    } as X509Certificate

@get:JvmSynthetic
internal val PrivateKey.oneKey
    get() = OneKey(null, this)

@JvmSynthetic
internal fun generateMso(
    digestAlg: String,
    docType: String,
    authKey: PublicKey,
    nameSpaces: CBORObject,
) =
    MobileSecurityObjectGenerator(digestAlg, docType, authKey)
        .apply {
            val now = Timestamp.now().toEpochMilli()
            val signed = Timestamp.ofEpochMilli(now)
            val validFrom = Timestamp.ofEpochMilli(now)
            val validUntil = Timestamp.ofEpochMilli(now + 1000L * 60L * 60L * 24L * 365L)
            setValidityInfo(signed, validFrom, validUntil, null)

            val digestIds = nameSpaces.entries.associate { (nameSpace, issuerSignedItems) ->
                nameSpace.AsString() to calculateDigests(digestAlg, issuerSignedItems)
            }
            digestIds.forEach { (nameSpace, digestIds) ->
                addDigestIdsForNamespace(nameSpace, digestIds)
            }
        }
        .generate()

@JvmSynthetic
internal fun calculateDigests(digestAlg: String, issuerSignedItems: CBORObject): Map<Long, ByteArray> {
    return issuerSignedItems.values.associate { issuerSignedItemBytes ->
        val issuerSignedItem = issuerSignedItemBytes.getEmbeddedCBORObject()
        val digest = MessageDigest.getInstance(digestAlg)
            .digest(issuerSignedItemBytes.EncodeToBytes())
        issuerSignedItem["digestID"].AsInt32().toLong() to digest
    }
}

@JvmSynthetic
internal fun signMso(mso: ByteArray) = Sign1Message(false, true).apply {
    protectedAttributes.Add(Algorithm.AsCBOR(), ECDSA_256.AsCBOR())
    unprotectedAttributes.Add(33L, issuerCertificate.encoded)
    SetContent(mso.withTag24())
    sign(issuerPrivateKey.oneKey)
}.EncodeToCBORObject()

@JvmSynthetic
internal fun generateData(
    issuerNameSpaces: CBORObject,
    issuerAuth: CBORObject,
): ByteArray {
    return mapOf(
        "nameSpaces" to issuerNameSpaces,
        "issuerAuth" to issuerAuth,
    ).let { CBORObject.FromObject(it).EncodeToBytes() }
}
