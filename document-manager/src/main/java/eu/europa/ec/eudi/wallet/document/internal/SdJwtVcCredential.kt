package eu.europa.ec.eudi.wallet.document.internal

import org.multipaz.cbor.CborBuilder
import org.multipaz.cbor.DataItem
import org.multipaz.cbor.MapBuilder
import org.multipaz.claim.VcClaim
import org.multipaz.credential.SecureAreaBoundCredential
import org.multipaz.document.Document
import org.multipaz.documenttype.DocumentTypeRepository
import org.multipaz.securearea.CreateKeySettings
import org.multipaz.securearea.SecureArea

/**
 * A SD-JWT VC credential, according to [draft-ietf-oauth-sd-jwt-vc-03]
 * (https://datatracker.ietf.org/doc/draft-ietf-oauth-sd-jwt-vc/).
 */
class SdJwtVcCredential : SecureAreaBoundCredential, org.multipaz.sdjwt.credential.SdJwtVcCredential {
    companion object {

        suspend fun create(
            document: Document,
            asReplacementForIdentifier: String?,
            domain: String,
            secureArea: SecureArea,
            vct: String,
            createKeySettings: CreateKeySettings
        ): SdJwtVcCredential {
            return SdJwtVcCredential(
                document,
                asReplacementForIdentifier,
                domain,
                secureArea,
                vct
            ).apply {
                generateKey(createKeySettings)
            }
        }
    }

    /**
     * The Verifiable Credential Type - or `vct` - as defined in section 3.2.2.1.1 of
     * [draft-ietf-oauth-sd-jwt-vc-03]
     * (https://datatracker.ietf.org/doc/draft-ietf-oauth-sd-jwt-vc/)
     */
    override lateinit var vct: String
        private set

    /**
     * Constructs a new [SdJwtVcCredential].
     *
     * @param document the document to add the credential to.
     * @param asReplacementForIdentifier the credential this credential will replace, if not null
     * @param domain the domain of the credential
     * @param secureArea the secure area for the authentication key associated with this credential.
     * @param vct the Verifiable Credential Type.
     */
    constructor(
        document: Document,
        asReplacementForIdentifier: String?,
        domain: String,
        secureArea: SecureArea,
        vct: String,
    ) : super(document, asReplacementForIdentifier, domain, secureArea) {
        this.vct = vct
    }

    /**
     * Constructs a Credential from serialized data.
     *
     * @param document the [Document] that the credential belongs to.
     */
    constructor(
        document: Document,
    ) : super(document)

    override suspend fun deserialize(dataItem: DataItem) {
        super.deserialize(dataItem)
        vct = dataItem["vct"].asTstr
    }

    override fun addSerializedData(builder: MapBuilder<CborBuilder>) {
        super.addSerializedData(builder)
        builder.put("vct", vct)
    }

    override fun getClaims(documentTypeRepository: DocumentTypeRepository?): List<VcClaim> {
        return getClaimsImpl(documentTypeRepository)
    }
}