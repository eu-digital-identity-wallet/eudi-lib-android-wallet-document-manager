//[document-manager](../../../index.md)/[eu.europa.ec.eudi.wallet.document.format](../index.md)/[DocumentData](index.md)

# DocumentData

sealed interface [DocumentData](index.md)

Represents the claims of a document.

#### Inheritors

|                                            |
|--------------------------------------------|
| [MsoMdocData](../-mso-mdoc-data/index.md)  |
| [SdJwtVcData](../-sd-jwt-vc-data/index.md) |

## Properties

| Name                | Summary                                                                                                                                                                                                               |
|---------------------|-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| [claims](claims.md) | [androidJvm]<br>abstract val [claims](claims.md): [List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)&lt;[DocumentClaim](../-document-claim/index.md)&gt;<br>The list of claims. |
| [format](format.md) | [androidJvm]<br>abstract val [format](format.md): [DocumentFormat](../-document-format/index.md)<br>The format of the document.                                                                                       |
