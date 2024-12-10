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

package eu.europa.ec.eudi.wallet.document.internal

import com.android.identity.crypto.javaPublicKey
import com.android.identity.document.Document
import com.android.identity.securearea.CreateKeySettings
import com.android.identity.securearea.SecureArea
import com.nimbusds.jose.jwk.JWK
import com.nimbusds.jose.jwk.KeyConverter
import eu.europa.ec.eudi.sdjwt.SdJwt
import eu.europa.ec.eudi.sdjwt.unverifiedIssuanceFrom
import eu.europa.ec.eudi.sdjwt.vc.SdJwtVcVerifier
import eu.europa.ec.eudi.wallet.document.UnsignedDocument
import eu.europa.ec.eudi.wallet.document.format.SdJwtVcFormat
import io.ktor.client.HttpClient
import kotlinx.coroutines.runBlocking
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.jsonPrimitive
import kotlinx.serialization.json.longOrNull

@JvmSynthetic
internal fun SdJwtVcFormat.createCredential(
    domain: String,
    identityDocument: Document,
    secureArea: SecureArea,
    createKeySettings: CreateKeySettings,
): SdJwtVcCredential {
    return SdJwtVcCredential(
        document = identityDocument,
        asReplacementFor = null,
        domain = domain,
        secureArea = secureArea,
        createKeySettings = createKeySettings,
        vct = vct
    )
}

@JvmSynthetic
internal fun SdJwtVcFormat.storeIssuedDocument(
    unsignedDocument: UnsignedDocument,
    identityDocument: Document,
    data: ByteArray,
    checkDevicePublicKey: Boolean,
    ktorHttpClientFactory: (() -> HttpClient)? = null
) {
    runBlocking {
        SdJwtVcVerifier(
            { ktorHttpClientFactory?.invoke() ?: HttpClient() },
            { certificateChain ->
                // TODO Check the certificate path
                return@SdJwtVcVerifier true
            }
        ).verifyIssuance(String(data, Charsets.US_ASCII)).getOrElse {
            throw IllegalArgumentException("Invalid SD-JWT VC with error: ${it.message}")
        }

        val sdJwt = SdJwt.unverifiedIssuanceFrom(String(data, Charsets.US_ASCII)).getOrElse {
            throw IllegalArgumentException("Invalid SD-JWT VC")
        }

        val (_, claims) = sdJwt.jwt
        claims["cnf"]?.let {
            val jwk = JWK.parse(Json.decodeFromString<JsonObject>(it.toString())["jwk"].toString())
            val sdjwtVcPk = KeyConverter.toJavaKeys(listOf(jwk)).first()
                ?: throw IllegalArgumentException("Invalid SD-JWT VC")
            if (unsignedDocument.keyInfo.publicKey.javaPublicKey != sdjwtVcPk) {
                if (checkDevicePublicKey) {
                    throw IllegalArgumentException("Public key in SD-JWT VC does not match the one in the request")
                }
            }
        }

        // TODO what to do with validFrom and validUntil if they are not present in the SD-JWT VC
        //  in nbf and exp claims that are optional
        val nbf = claims["nbf"]?.jsonPrimitive?.longOrNull?.let { Instant.fromEpochSeconds(it) }
        val exp = claims["exp"]?.jsonPrimitive?.longOrNull?.let { Instant.fromEpochSeconds(it) }
        val validFrom = nbf ?: Clock.System.now()
        val validUntil = exp ?: Instant.fromEpochMilliseconds(
            validFrom.toEpochMilliseconds() + 1 * 86400 * 1000L)

        identityDocument.pendingCredentials.forEach { credential ->
            credential.certify(data, validFrom, validUntil)
        }
    }
}