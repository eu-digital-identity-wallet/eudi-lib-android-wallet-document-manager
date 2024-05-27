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
MIGHAgEAMBMGByqGSM49AgEGCCqGSM49AwEHBG0wawIBAQQgpZ6P3Bl9GVuiTtkf
ireROebEFuNc+PHXO5rdWnKr+oyhRANCAAR8kxP0waSqTrCz62gRpJlOWd5nmWQx
wvOuCI63oQYctli9jDkSbBlZeskN+Z0HjT7zkTujS9ssvGmH0Cfpr538
-----END PRIVATE KEY-----
""".trimIndent()

private val SAMPLE_ISSUER_DS = """
-----BEGIN CERTIFICATE-----
MIICgTCCAiagAwIBAgIJFkrlmQLcBRBkMAoGCCqGSM49BAMCMFgxCzAJBgNVBAYT
AkJFMRwwGgYDVQQKExNFdXJvcGVhbiBDb21taXNzaW9uMSswKQYDVQQDEyJFVSBE
aWdpdGFsIElkZW50aXR5IFdhbGxldCBUZXN0IENBMB4XDTIzMDUzMDEyMzAwMFoX
DTI0MDUyOTEyMzAwMFowZTELMAkGA1UEBhMCQkUxHDAaBgNVBAoTE0V1cm9wZWFu
IENvbW1pc3Npb24xODA2BgNVBAMTL0VVIERpZ2l0YWwgSWRlbnRpdHkgV2FsbGV0
IFRlc3QgRG9jdW1lbnQgU2lnbmVyMFkwEwYHKoZIzj0CAQYIKoZIzj0DAQcDQgAE
fJMT9MGkqk6ws+toEaSZTlneZ5lkMcLzrgiOt6EGHLZYvYw5EmwZWXrJDfmdB40+
85E7o0vbLLxph9An6a+d/KOByzCByDAdBgNVHQ4EFgQU0aSxJDky+0VycplI8lJ9
lVinTS0wHwYDVR0jBBgwFoAUMpHrDhwBHRQOdk9sT+pMljja+wQwDgYDVR0PAQH/
BAQDAgeAMBIGA1UdJQQLMAkGByiBjF0FAQIwHwYDVR0SBBgwFoYUaHR0cDovL3d3
dy5ldWRpdy5kZXYwQQYDVR0fBDowODA2oDSgMoYwaHR0cHM6Ly9zdGF0aWMuZXVk
aXcuZGV2L3BraS9jcmwvaXNvMTgwMTMtZHMuY3JsMAoGCCqGSM49BAMCA0kAMEYC
IQDeX5jnHvZXUhJr8sS4T97fgdJDylexW9MYqrnx6+s/fQIhAP4i9zJrS1dY/xb+
htM6jY0piCFp2gSbWl4sgGqxRwhI
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
