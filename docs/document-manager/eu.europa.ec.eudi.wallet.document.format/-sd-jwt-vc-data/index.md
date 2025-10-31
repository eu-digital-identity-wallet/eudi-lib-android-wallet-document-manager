//[document-manager](../../../index.md)/[eu.europa.ec.eudi.wallet.document.format](../index.md)/[SdJwtVcData](index.md)

# SdJwtVcData

[androidJvm]\
data class [SdJwtVcData](index.md)(val format: [SdJwtVcFormat](../-sd-jwt-vc-format/index.md), val issuerMetadata: [IssuerMetadata](../../eu.europa.ec.eudi.wallet.document.metadata/-issuer-metadata/index.md)?, val sdJwtVc: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-string/index.html)) : [DocumentData](../-document-data/index.md)

Represents the claims of a document in the SdJwtVc format.

## Constructors

| | |
|---|---|
| [SdJwtVcData](-sd-jwt-vc-data.md) | [androidJvm]<br>constructor(format: [SdJwtVcFormat](../-sd-jwt-vc-format/index.md), issuerMetadata: [IssuerMetadata](../../eu.europa.ec.eudi.wallet.document.metadata/-issuer-metadata/index.md)?, sdJwtVc: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-string/index.html)) |

## Types

| Name | Summary |
|---|---|
| [Companion](-companion/index.md) | [androidJvm]<br>object [Companion](-companion/index.md) |

## Properties

| Name | Summary |
|---|---|
| [claims](claims.md) | [androidJvm]<br>open override val [claims](claims.md): [List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin.collections/-list/index.html)&lt;[SdJwtVcClaim](../-sd-jwt-vc-claim/index.md)&gt;<br>The list of claims. |
| [format](format.md) | [androidJvm]<br>open override val [format](format.md): [SdJwtVcFormat](../-sd-jwt-vc-format/index.md)<br>The SdJwtVc format containing the vct |
| [issuerMetadata](issuer-metadata.md) | [androidJvm]<br>open override val [issuerMetadata](issuer-metadata.md): [IssuerMetadata](../../eu.europa.ec.eudi.wallet.document.metadata/-issuer-metadata/index.md)?<br>The metadata of the document provided by the issuer. |
| [sdJwtVc](sd-jwt-vc.md) | [androidJvm]<br>val [sdJwtVc](sd-jwt-vc.md): [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-string/index.html)<br>The SdJwtVc. |
