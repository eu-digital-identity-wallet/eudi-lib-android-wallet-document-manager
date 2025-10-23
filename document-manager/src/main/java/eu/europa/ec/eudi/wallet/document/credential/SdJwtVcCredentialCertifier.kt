/*
 * Copyright (c) 2025 European Commission
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

package eu.europa.ec.eudi.wallet.document.credential

import com.nimbusds.jose.jwk.JWK
import com.nimbusds.jose.jwk.KeyConverter
import eu.europa.ec.eudi.sdjwt.DefaultSdJwtOps
import eu.europa.ec.eudi.sdjwt.vc.KtorHttpClientFactory
import eu.europa.ec.eudi.wallet.document.internal.sdJwtVcString
import io.ktor.client.HttpClient
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.jsonPrimitive
import kotlinx.serialization.json.longOrNull
import org.multipaz.credential.SecureAreaBoundCredential
import org.multipaz.crypto.javaPublicKey
import org.multipaz.util.Logger
import kotlin.time.Clock
import kotlin.time.Duration.Companion.days
import kotlin.time.Instant

class SdJwtVcCredentialCertifier(
    var ktorHttpClientFactory: KtorHttpClientFactory = { HttpClient() }
) : CredentialCertification {
    override suspend fun certifyCredential(
        credential: SecureAreaBoundCredential,
        issuedCredential: IssuerProvidedCredential,
        forceKeyCheck: Boolean
    ) {
        val data = issuedCredential.data
        DefaultSdJwtOps.SdJwtVcVerifier.usingX5cOrIssuerMetadata(
            httpClientFactory = ktorHttpClientFactory,
            x509CertificateTrust = { _ ->
                // TODO Check the certificate path
                true
            }
        ).verify(data.sdJwtVcString).onFailure {
            Logger.w("SdJwtVcVerifier", "Invalid SD-JWT VC with error: ${it.message}", it)
        }

        val sdJwt = DefaultSdJwtOps.unverifiedIssuanceFrom(data.sdJwtVcString).getOrElse {
            throw IllegalArgumentException("Invalid SD-JWT VC", it)
        }

        val (_, claims) = sdJwt.jwt

        claims["cnf"]?.let {
            val jwk = JWK.parse(Json.Default.decodeFromString<JsonObject>(it.toString())["jwk"].toString())
            val sdjwtVcPk = KeyConverter.toJavaKeys(listOf(jwk)).first()
                ?: throw IllegalArgumentException("Invalid SD-JWT VC")
            if (credential.secureArea.getKeyInfo(credential.alias).publicKey.javaPublicKey != sdjwtVcPk && forceKeyCheck) {
                throw IllegalArgumentException("Public key in SD-JWT VC does not match the one in the request")
            }
        }

        // TODO what to do with validFrom and validUntil if they are not present in the SD-JWT VC
        //  in nbf (or iat if no nbf) and exp claims that are optional

        val nbf = claims["nbf"]?.jsonPrimitive?.longOrNull?.let { Instant.fromEpochSeconds(it) }
        val iat = claims["iat"]?.jsonPrimitive?.longOrNull?.let { Instant.fromEpochSeconds(it) }
        val exp = claims["exp"]?.jsonPrimitive?.longOrNull?.let { Instant.fromEpochSeconds(it) }
        val validFrom = nbf ?: iat ?: Clock.System.now()
        val validUntil = exp ?: validFrom.plus(30.days)

        credential.certify(data, validFrom, validUntil)
    }
}
