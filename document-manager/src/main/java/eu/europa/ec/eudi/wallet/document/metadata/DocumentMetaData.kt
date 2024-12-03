package eu.europa.ec.eudi.wallet.document.metadata

import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import java.net.URI
import java.util.Locale

/**
 * Document metadata domain object for storage.
 */
@Serializable
data class DocumentMetaData(
    val display: List<Display>,
    val claims: Map<ClaimName, Claim>?
) {

    @Serializable
    data class MsoMdocClaimName(
        override val name: String,
        val nameSpace: String
    ) : ClaimName

    @Serializable
    data class SdJwtVcsClaimName(
        override val name: String,
    ): ClaimName

    @Serializable(with= ClaimNameSerializer::class)
    interface ClaimName {
        val name: String
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


object ClaimNameSerializer : KSerializer<DocumentMetaData.ClaimName> {
    override val descriptor: SerialDescriptor =
        PrimitiveSerialDescriptor("ClaimName", PrimitiveKind.STRING)

    override fun serialize(encoder: Encoder, value: DocumentMetaData.ClaimName) {
        val key = when (value) {
            is DocumentMetaData.MsoMdocClaimName -> "Mso:${value.nameSpace}:${value.name}"
            is DocumentMetaData.SdJwtVcsClaimName -> "SdJwt:${value.name}"
            else -> throw IllegalArgumentException("Unknown ClaimName type")
        }
        encoder.encodeString(key)
    }

    override fun deserialize(decoder: Decoder): DocumentMetaData.ClaimName {
        val key = decoder.decodeString()
        return when {
            key.startsWith("Mso:") -> {
                val parts = key.split(":")
                DocumentMetaData.MsoMdocClaimName(name = parts[2], nameSpace = parts[1])
            }
            key.startsWith("SdJwt:") -> {
                val parts = key.split(":")
                DocumentMetaData.SdJwtVcsClaimName(name = parts[1])
            }
            else -> throw IllegalArgumentException("Unknown ClaimName key format")
        }
    }
}