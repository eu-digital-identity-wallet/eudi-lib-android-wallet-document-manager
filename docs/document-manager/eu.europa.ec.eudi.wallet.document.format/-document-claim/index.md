//[document-manager](../../../index.md)/[eu.europa.ec.eudi.wallet.document.format](../index.md)/[DocumentClaim](index.md)

# DocumentClaim

sealed class [DocumentClaim](index.md)

Represents a claim of a document.

#### Inheritors

| |
|---|
| [MsoMdocClaim](../-mso-mdoc-claim/index.md) |
| [SdJwtVcClaim](../-sd-jwt-vc-claim/index.md) |

## Properties

| Name                                 | Summary                                                                                                                                                                                                                               |
|--------------------------------------|---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| [identifier](identifier.md)          | [androidJvm]<br>open val [identifier](identifier.md): [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-string/index.html)<br>The identifier of the claim.                                                  |
| [issuerMetadata](issuer-metadata.md) | [androidJvm]<br>open val [issuerMetadata](issuer-metadata.md): [IssuerMetadata.Claim](../../eu.europa.ec.eudi.wallet.document.metadata/-issuer-metadata/-claim/index.md)? = null<br>The metadata of the claim provided by the issuer. |
| [rawValue](raw-value.md)             | [androidJvm]<br>open val [rawValue](raw-value.md): [Any](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-any/index.html)?<br>The raw value of the claim.                                                           |
| [value](value.md)                    | [androidJvm]<br>open val [value](value.md): [Any](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-any/index.html)?<br>The value of the claim.                                                                      |
