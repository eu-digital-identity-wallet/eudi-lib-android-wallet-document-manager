//[document-manager](../../../index.md)/[eu.europa.ec.eudi.wallet.document.format](../index.md)/[SdJwtVcClaim](index.md)

# SdJwtVcClaim

[androidJvm]\
data class [SdJwtVcClaim](index.md)(val
identifier: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html), val
value: [Any](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/index.html)?, val
rawValue: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html), val
selectivelyDisclosable: [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)) : [DocumentClaim](../-document-claim/index.md)

Represents a claim of a document in the SdJwtVc format.

## Constructors

|                                     |                                                                                                                                                                                                                                                                                                                                                                                                                       |
|-------------------------------------|-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| [SdJwtVcClaim](-sd-jwt-vc-claim.md) | [androidJvm]<br>constructor(identifier: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html), value: [Any](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/index.html)?, rawValue: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html), selectivelyDisclosable: [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)) |

## Properties

| Name                                                 | Summary                                                                                                                                                                                                       |
|------------------------------------------------------|---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| [identifier](identifier.md)                          | [androidJvm]<br>open override val [identifier](identifier.md): [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)<br>The identifier of the claim.                               |
| [rawValue](raw-value.md)                             | [androidJvm]<br>open override val [rawValue](raw-value.md): [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)<br>The raw value of the claim.                                   |
| [selectivelyDisclosable](selectively-disclosable.md) | [androidJvm]<br>val [selectivelyDisclosable](selectively-disclosable.md): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)<br>Whether the claim is selectively disclosable. |
| [value](value.md)                                    | [androidJvm]<br>open override val [value](value.md): [Any](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/index.html)?<br>The value of the claim.                                                   |