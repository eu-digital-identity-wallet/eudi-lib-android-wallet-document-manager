//[document-manager](../../../index.md)/[eu.europa.ec.eudi.wallet.document.format](../index.md)/[SdJwtVcClaims](index.md)

# SdJwtVcClaims

[androidJvm]\
data class [SdJwtVcClaims](index.md)(val sdJwtVc:
SdJwt.Issuance&lt;[Pair](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-pair/index.html)
&lt;[String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html), [Map](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-map/index.html)
&lt;[String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html),
JsonElement&gt;&gt;&gt;) : [DocumentClaims](../-document-claims/index.md)

Represents the claims of a document in the SdJwtVc format.

## Constructors

|                                       |                                                                                                                                                                                                                                                                                                                                                                                                                               |
|---------------------------------------|-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| [SdJwtVcClaims](-sd-jwt-vc-claims.md) | [androidJvm]<br>constructor(sdJwtVc: SdJwt.Issuance&lt;[Pair](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-pair/index.html)&lt;[String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html), [Map](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-map/index.html)&lt;[String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html), JsonElement&gt;&gt;&gt;) |

## Properties

| Name                    | Summary                                                                                                                                                                                                                                                                                                                                                                                                                                              |
|-------------------------|------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| [claims](claims.md)     | [androidJvm]<br>open override val [claims](claims.md): [List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)&lt;[SdJwtVcClaim](../-sd-jwt-vc-claim/index.md)&gt;<br>The list of claims.                                                                                                                                                                                                                           |
| [sdJwtVc](sd-jwt-vc.md) | [androidJvm]<br>val [sdJwtVc](sd-jwt-vc.md): SdJwt.Issuance&lt;[Pair](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-pair/index.html)&lt;[String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html), [Map](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-map/index.html)&lt;[String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html), JsonElement&gt;&gt;&gt;<br>The SdJwtVc. |
