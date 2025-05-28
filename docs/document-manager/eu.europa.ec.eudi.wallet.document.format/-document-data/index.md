//[document-manager](../../../index.md)/[eu.europa.ec.eudi.wallet.document.format](../index.md)/[DocumentData](index.md)

# DocumentData

sealed interface [DocumentData](index.md)

Represents the claims of a document.

#### Inheritors

| |
|---|
| [MsoMdocData](../-mso-mdoc-data/index.md) |
| [SdJwtVcData](../-sd-jwt-vc-data/index.md) |

## Properties

| Name | Summary |
|---|---|
| [claims](claims.md) | [androidJvm]<br>abstract val [claims](claims.md): [List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin.collections/-list/index.html)&lt;[DocumentClaim](../-document-claim/index.md)&gt;<br>The list of claims. |
| [format](format.md) | [androidJvm]<br>abstract val [format](format.md): [DocumentFormat](../-document-format/index.md)<br>The format of the document. |
| [issuerMetadata](issuer-metadata.md) | [androidJvm]<br>abstract val [issuerMetadata](issuer-metadata.md): [IssuerMetadata](../../eu.europa.ec.eudi.wallet.document.metadata/-issuer-metadata/index.md)?<br>The metadata of the document provided by the issuer. |
