//[document-manager](../../../index.md)/[eu.europa.ec.eudi.wallet.document.format](../index.md)/[MsoMdocClaim](index.md)

# MsoMdocClaim

[androidJvm]\
data class [MsoMdocClaim](index.md)(val nameSpace: [NameSpace](../../eu.europa.ec.eudi.wallet.document/-name-space/index.md), val identifier: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-string/index.html), val value: [Any](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-any/index.html)?, val rawValue: [ByteArray](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-byte-array/index.html), val metadata: [DocumentMetaData.Claim](../../eu.europa.ec.eudi.wallet.document.metadata/-document-meta-data/-claim/index.md)?) : [DocumentClaim](../-document-claim/index.md)

Represents a claim of a document in the MsoMdoc format.

## Constructors

| | |
|---|---|
| [MsoMdocClaim](-mso-mdoc-claim.md) | [androidJvm]<br>constructor(nameSpace: [NameSpace](../../eu.europa.ec.eudi.wallet.document/-name-space/index.md), identifier: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-string/index.html), value: [Any](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-any/index.html)?, rawValue: [ByteArray](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-byte-array/index.html), metadata: [DocumentMetaData.Claim](../../eu.europa.ec.eudi.wallet.document.metadata/-document-meta-data/-claim/index.md)?) |

## Properties

| Name | Summary |
|---|---|
| [identifier](identifier.md) | [androidJvm]<br>open override val [identifier](identifier.md): [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-string/index.html)<br>The identifier of the claim. |
| [metadata](metadata.md) | [androidJvm]<br>open override val [metadata](metadata.md): [DocumentMetaData.Claim](../../eu.europa.ec.eudi.wallet.document.metadata/-document-meta-data/-claim/index.md)?<br>The metadata of the claim. |
| [nameSpace](name-space.md) | [androidJvm]<br>val [nameSpace](name-space.md): [NameSpace](../../eu.europa.ec.eudi.wallet.document/-name-space/index.md)<br>The name-space of the claim. |
| [rawValue](raw-value.md) | [androidJvm]<br>open override val [rawValue](raw-value.md): [ByteArray](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-byte-array/index.html)<br>The raw value of the claim in bytes. |
| [value](value.md) | [androidJvm]<br>open override val [value](value.md): [Any](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-any/index.html)?<br>The value of the claim. |

## Functions

| Name                     | Summary                                                                                                                                                                                                                                                            |
|--------------------------|--------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| [equals](equals.md)      | [androidJvm]<br>open operator override fun [equals](equals.md)(other: [Any](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-any/index.html)?): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-boolean/index.html) |
| [hashCode](hash-code.md) | [androidJvm]<br>open override fun [hashCode](hash-code.md)(): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html)                                                                                                             |
