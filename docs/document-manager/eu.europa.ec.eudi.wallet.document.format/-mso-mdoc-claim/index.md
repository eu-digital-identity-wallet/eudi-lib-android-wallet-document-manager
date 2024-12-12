//[document-manager](../../../index.md)/[eu.europa.ec.eudi.wallet.document.format](../index.md)/[MsoMdocClaim](index.md)

# MsoMdocClaim

[androidJvm]\
data class [MsoMdocClaim](index.md)(val
nameSpace: [NameSpace](../../eu.europa.ec.eudi.wallet.document/-name-space/index.md), val
identifier: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html), val
value: [Any](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/index.html)?, val
rawValue: [ByteArray](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-byte-array/index.html)) : [DocumentClaim](../-document-claim/index.md)

Represents a claim of a document in the MsoMdoc format.

## Constructors

|                                    |                                                                                                                                                                                                                                                                                                                                                                                                        |
|------------------------------------|--------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| [MsoMdocClaim](-mso-mdoc-claim.md) | [androidJvm]<br>constructor(nameSpace: [NameSpace](../../eu.europa.ec.eudi.wallet.document/-name-space/index.md), identifier: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html), value: [Any](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/index.html)?, rawValue: [ByteArray](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-byte-array/index.html)) |

## Properties

| Name                        | Summary                                                                                                                                                                                     |
|-----------------------------|---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| [identifier](identifier.md) | [androidJvm]<br>open override val [identifier](identifier.md): [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)<br>The identifier of the claim.             |
| [nameSpace](name-space.md)  | [androidJvm]<br>val [nameSpace](name-space.md): [NameSpace](../../eu.europa.ec.eudi.wallet.document/-name-space/index.md)<br>The name-space of the claim.                                   |
| [rawValue](raw-value.md)    | [androidJvm]<br>open override val [rawValue](raw-value.md): [ByteArray](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-byte-array/index.html)<br>The raw value of the claim in bytes. |
| [value](value.md)           | [androidJvm]<br>open override val [value](value.md): [Any](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/index.html)?<br>The value of the claim.                                 |
