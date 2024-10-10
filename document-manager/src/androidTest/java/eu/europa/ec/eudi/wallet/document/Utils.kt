package eu.europa.ec.eudi.wallet.document

import com.android.identity.credential.CredentialFactory
import com.android.identity.credential.SecureAreaBoundCredential
import com.android.identity.document.DocumentStore
import com.android.identity.mdoc.credential.MdocCredential
import com.android.identity.mdoc.mso.StaticAuthDataParser
import com.android.identity.mdoc.mso.StaticAuthDataParser.StaticAuthData
import com.android.identity.securearea.SecureArea
import com.android.identity.securearea.SecureAreaRepository
import com.android.identity.storage.StorageEngine
import org.junit.Assert.assertNotNull

data class DocumentIssuerData(
    var keyAlias: String,
    var staticAuthData: StaticAuthData,
)

fun getStaticAuthDataFromDocument(
    issuedDocument: IssuedDocument,
    secureArea: SecureArea,
    storageEngine: StorageEngine
): DocumentIssuerData {
    val secureAreaRepository = SecureAreaRepository()
    secureAreaRepository.addImplementation(secureArea)
    val credentialFactory = CredentialFactory().apply {
        addCredentialImplementation(MdocCredential::class) { document, dataItem ->
            MdocCredential(document, dataItem)
        }
    }
    val credentialStore = DocumentStore(storageEngine, secureAreaRepository, credentialFactory)
    val baseDocument = credentialStore.lookupDocument(issuedDocument.id)
    assertNotNull(baseDocument)
    val credential = (baseDocument!!.certifiedCredentials[0] as SecureAreaBoundCredential)
    return DocumentIssuerData(
        staticAuthData = StaticAuthDataParser(credential.issuerProvidedData).parse(),
        keyAlias = credential.alias,
    )
}