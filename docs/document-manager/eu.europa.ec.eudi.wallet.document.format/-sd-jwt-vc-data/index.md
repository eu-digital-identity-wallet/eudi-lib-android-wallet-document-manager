//[document-manager](../../../index.md)/[eu.europa.ec.eudi.wallet.document.format](../index.md)/[SdJwtVcData](index.md)

# SdJwtVcData

[androidJvm]\
data class [SdJwtVcData](index.md)(val format: [SdJwtVcFormat](../-sd-jwt-vc-format/index.md), val
sdJwtVc:
SdJwt.Issuance&lt;[Pair](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-pair/index.html)
&lt;[String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html), [Map](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-map/index.html)
&lt;[String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html),
JsonElement&gt;&gt;&gt;) : [DocumentData](../-document-data/index.md)

Represents the claims of a document in the SdJwtVc format.

## Constructors

|                                   |                                                                                                                                                                                                                                                                                                                                                                                                                                                                                       |
|-----------------------------------|---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| [SdJwtVcData](-sd-jwt-vc-data.md) | [androidJvm]<br>constructor(format: [SdJwtVcFormat](../-sd-jwt-vc-format/index.md), sdJwtVc: SdJwt.Issuance&lt;[Pair](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-pair/index.html)&lt;[String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html), [Map](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-map/index.html)&lt;[String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html), JsonElement&gt;&gt;&gt;) |

## Properties

| Name                    | Summary                                                                                                                                                                                                                                                                                                                                                                                                                                              |
|-------------------------|------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| [claims](claims.md)     | [androidJvm]<br>open override val [claims](claims.md): [List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)&lt;[SdJwtVcClaim](../-sd-jwt-vc-claim/index.md)&gt;<br>The list of claims.                                                                                                                                                                                                                           |
| [format](format.md)     | [androidJvm]<br>open override val [format](format.md): [SdJwtVcFormat](../-sd-jwt-vc-format/index.md)<br>The SdJwtVc format containing the vct                                                                                                                                                                                                                                                                                                       |
| [sdJwtVc](sd-jwt-vc.md) | [androidJvm]<br>val [sdJwtVc](sd-jwt-vc.md): SdJwt.Issuance&lt;[Pair](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-pair/index.html)&lt;[String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html), [Map](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-map/index.html)&lt;[String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html), JsonElement&gt;&gt;&gt;<br>The SdJwtVc. |
