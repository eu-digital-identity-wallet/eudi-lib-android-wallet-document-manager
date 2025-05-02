//[document-manager](../../../index.md)/[eu.europa.ec.eudi.wallet.document.format](../index.md)/[SdJwtVcClaim](index.md)

# SdJwtVcClaim

[androidJvm]\
data class [SdJwtVcClaim](index.md)(val identifier: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-string/index.html), val value: [Any](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-any/index.html)?, val rawValue: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-string/index.html), val metadata: [DocumentMetaData.Claim](../../eu.europa.ec.eudi.wallet.document.metadata/-document-meta-data/-claim/index.md)?, val selectivelyDisclosable: [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-boolean/index.html), val children: [List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin.collections/-list/index.html)&lt;[SdJwtVcClaim](index.md)&gt;) : [DocumentClaim](../-document-claim/index.md)

Represents a claim of a document in the SdJwtVc format.

## Constructors

| | |
|---|---|
| [SdJwtVcClaim](-sd-jwt-vc-claim.md) | [androidJvm]<br>constructor(identifier: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-string/index.html), value: [Any](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-any/index.html)?, rawValue: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-string/index.html), metadata: [DocumentMetaData.Claim](../../eu.europa.ec.eudi.wallet.document.metadata/-document-meta-data/-claim/index.md)?, selectivelyDisclosable: [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-boolean/index.html), children: [List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin.collections/-list/index.html)&lt;[SdJwtVcClaim](index.md)&gt;) |

## Properties

| Name | Summary |
|---|---|
| [children](children.md) | [androidJvm]<br>val [children](children.md): [List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin.collections/-list/index.html)&lt;[SdJwtVcClaim](index.md)&gt;<br>The children of the claim. |
| [identifier](identifier.md) | [androidJvm]<br>open override val [identifier](identifier.md): [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-string/index.html)<br>The identifier of the claim. |
| [metadata](metadata.md) | [androidJvm]<br>open override val [metadata](metadata.md): [DocumentMetaData.Claim](../../eu.europa.ec.eudi.wallet.document.metadata/-document-meta-data/-claim/index.md)?<br>The metadata of the claim. |
| [rawValue](raw-value.md) | [androidJvm]<br>open override val [rawValue](raw-value.md): [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-string/index.html)<br>The raw value of the claim. |
| [selectivelyDisclosable](selectively-disclosable.md) | [androidJvm]<br>val [selectivelyDisclosable](selectively-disclosable.md): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-boolean/index.html)<br>Whether the claim is selectively disclosable. |
| [value](value.md) | [androidJvm]<br>open override val [value](value.md): [Any](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-any/index.html)?<br>The value of the claim. |
