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

package eu.europa.ec.eudi.wallet.document.metadata

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.json.JsonClassDiscriminator
import java.net.URI
import java.util.Locale

/**
 * Document metadata domain object for storage.
 */
@Serializable
data class DocumentMetaData(
    val display: List<Display>,
    val claims: Map<out ClaimName, Claim>?
) {

    @OptIn(ExperimentalSerializationApi::class)
    @Serializable
    @JsonClassDiscriminator("type")
    sealed interface ClaimName {
        val name: String

        @Serializable
        data class MsoMdoc(
            override val name: String,
            val nameSpace: String
        ) : ClaimName

        @Serializable
        data class SdJwtVc(
            override val name: String
        ) : ClaimName
    }

    /**
     * Display properties of a supported credential type for a certain language.
     */
    @Serializable
    data class Display(
        val name: String,
        @Serializable(with = LocaleSerializer::class)
        val locale: Locale? = null,
        val logo: Logo? = null,
        val description: String? = null,
        val backgroundColor: String? = null,
        val textColor: String? = null,
    ) {

        /**
         * Logo information.
         */
        @Serializable
        data class Logo(
            @Serializable(with = URISerializer::class)
            val uri: URI? = null,
            val alternativeText: String? = null,
        ) : java.io.Serializable
    }

    @Serializable
    data class Claim(
        @SerialName("mandatory") val mandatory: Boolean? = false,
        @SerialName("value_type") val valueType: String? = null,
        @SerialName("display") val display: List<Display> = emptyList(),
    ) : java.io.Serializable {

        /**
         * Display properties of a Claim.
         */
        @Serializable
        data class Display(
            @SerialName("name") val name: String? = null,
            @Serializable(LocaleSerializer::class)
            @SerialName("locale") val locale: Locale? = null,
        ) : java.io.Serializable
    }
}


internal object LocaleSerializer : KSerializer<Locale> {

    override val descriptor: SerialDescriptor =
        PrimitiveSerialDescriptor("Locale", PrimitiveKind.STRING)

    override fun deserialize(decoder: Decoder): Locale =
        Locale.forLanguageTag(decoder.decodeString())

    override fun serialize(encoder: Encoder, value: Locale) =
        encoder.encodeString(value.toString())
}

object URISerializer : KSerializer<URI> {
    override val descriptor: SerialDescriptor =
        PrimitiveSerialDescriptor("URI", PrimitiveKind.STRING)

    override fun serialize(encoder: Encoder, value: URI) {
        encoder.encodeString(value.toString())
    }

    override fun deserialize(decoder: Decoder): URI {
        return URI.create(decoder.decodeString())
    }
}

//
//object ClaimNameSerializer : KSerializer<DocumentMetaData.ClaimName> {
//    override val descriptor: SerialDescriptor =
//        PrimitiveSerialDescriptor("ClaimName", PrimitiveKind.STRING)
//
//    override fun serialize(encoder: Encoder, value: DocumentMetaData.ClaimName) {
//        val key = when (value) {
//            is DocumentMetaData.ClaimName.MsoMdoc -> "Mso:${value.nameSpace}:${value.name}"
//            is DocumentMetaData.ClaimName.SdJwtVc -> "SdJwt:${value.name}"
//            else -> throw IllegalArgumentException("Unknown ClaimName type")
//        }
//        encoder.encodeString(key)
//    }
//
//    override fun deserialize(decoder: Decoder): DocumentMetaData.ClaimName {
//        val key = decoder.decodeString()
//        return when {
//            key.startsWith("Mso:") -> {
//                val (_, nameSpace, identifier) = key.split(":", limit = 3)
//                DocumentMetaData.ClaimName.MsoMdoc(name = identifier, nameSpace = nameSpace)
//            }
//
//            key.startsWith("SdJwt:") -> {
//                val (_, identifier) = key.split(":", limit = 2)
//                DocumentMetaData.ClaimName.SdJwtVc(name = identifier)
//            }
//
//            else -> throw IllegalArgumentException("Unknown ClaimName key format")
//        }
//    }
//}