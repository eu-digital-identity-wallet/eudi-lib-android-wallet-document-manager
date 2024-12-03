package eu.europa.ec.eudi.wallet.document.mock_data

import eu.europa.ec.eudi.wallet.document.metadata.DocumentMetaData
import java.net.URI
import java.util.Locale

object DocumentMetaDataMockData {

    fun getData(): DocumentMetaData {
        // Initialize displays
        val displays = listOf(
            DocumentMetaData.Display(
                name = "Example Display",
                locale = Locale.ENGLISH,
                logo = DocumentMetaData.Display.Logo(
                    uri = URI.create("https://example.com/logo.png"),
                    alternativeText = "Example Logo"
                ),
                description = "This is a sample description",
                backgroundColor = "#FFFFFF",
                textColor = "#000000"
            )
        )

        // Create claims
        val msoClaimName = DocumentMetaData.MsoMdocClaimName(
            name = "MsoClaim",
            nameSpace = "namespace.mso"
        )
        val sdJwtClaimName = DocumentMetaData.SdJwtVcsClaimName(
            name = "SdJwtClaim"
        )

        val claims : Map<DocumentMetaData.ClaimName, DocumentMetaData.Claim> = mapOf(
            msoClaimName to DocumentMetaData.Claim(
                mandatory = true,
                valueType = "string",
                display = listOf(
                    DocumentMetaData.Claim.Display(
                        name = "Mso Claim Display",
                        locale = Locale.ENGLISH
                    )
                )
            ),
            sdJwtClaimName to DocumentMetaData.Claim(
                mandatory = false,
                valueType = "integer",
                display = listOf(
                    DocumentMetaData.Claim.Display(
                        name = "SdJwt Claim Display",
                        locale = Locale.FRENCH
                    )
                )
            )
        )

        // Create and return DocumentMetaData
        return DocumentMetaData(
            display = displays,
            claims = claims
        )
    }
}