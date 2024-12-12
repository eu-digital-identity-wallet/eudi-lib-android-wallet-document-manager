//[document-manager](../../../index.md)/[eu.europa.ec.eudi.wallet.document.format](../index.md)/[DocumentClaims](index.md)

# DocumentClaims

sealed interface [DocumentClaims](index.md)

Represents the claims of a document.

#### Inheritors

|                                                |
|------------------------------------------------|
| [MsoMdocClaims](../-mso-mdoc-claims/index.md)  |
| [SdJwtVcClaims](../-sd-jwt-vc-claims/index.md) |

## Properties

| Name                | Summary                                                                                                                                                                                                               |
|---------------------|-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| [claims](claims.md) | [androidJvm]<br>abstract val [claims](claims.md): [List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)&lt;[DocumentClaim](../-document-claim/index.md)&gt;<br>The list of claims. |
