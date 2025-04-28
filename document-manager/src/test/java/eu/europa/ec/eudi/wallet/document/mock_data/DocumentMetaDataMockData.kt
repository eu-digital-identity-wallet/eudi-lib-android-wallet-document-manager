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
                logo = DocumentMetaData.Logo(
                    uri = URI.create("https://example.com/logo.png"),
                    alternativeText = "Example Logo"
                ),
                description = "This is a sample description",
                backgroundColor = "#FFFFFF",
                textColor = "#000000"
            )
        )

        // Create claims
        val msoClaimName = listOf(
            "namespace.mso",
            "MsoClaim"

        )
        val sdJwtClaimName = listOf(
            "SdJwtClaim"
        )

        val claims: List<DocumentMetaData.Claim> = listOf(
            DocumentMetaData.Claim(
                path = msoClaimName,
                mandatory = true,
                display = listOf(
                    DocumentMetaData.Claim.Display(
                        name = "Mso Claim Display",
                        locale = Locale.ENGLISH
                    )
                )
            ),
            DocumentMetaData.Claim(
                path = sdJwtClaimName,
                mandatory = false,
                display = listOf(
                    DocumentMetaData.Claim.Display(
                        name = "SdJwt Claim Display",
                        locale = Locale.FRENCH
                    )
                )
            )
        )

        val issuerDisplay: List<DocumentMetaData.IssuerDisplay> = listOf(
            DocumentMetaData.IssuerDisplay(
                name = "Greek Goverment",
                locale = Locale.ENGLISH
            )
        )

        // Create and return DocumentMetaData
        return DocumentMetaData(
            display = displays,
            claims = claims,
            issuerDisplay = issuerDisplay,
            documentConfigurationIdentifier = "docId",
            credentialIssuerIdentifier = "credId"
        )
    }
}