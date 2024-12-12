//[document-manager](../../../index.md)/[eu.europa.ec.eudi.wallet.document.format](../index.md)/[MsoMdocClaims](index.md)

# MsoMdocClaims

[androidJvm]\
data class [MsoMdocClaims](index.md)(val nameSpacedData:
NameSpacedData) : [DocumentClaims](../-document-claims/index.md)

Represents the claims of a document in the MsoMdoc format.

## Constructors

|                                      |                                                             |
|--------------------------------------|-------------------------------------------------------------|
| [MsoMdocClaims](-mso-mdoc-claims.md) | [androidJvm]<br>constructor(nameSpacedData: NameSpacedData) |

## Properties

| Name                                                  | Summary                                                                                                                                                                                                                                                                                              |
|-------------------------------------------------------|------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| [claims](claims.md)                                   | [androidJvm]<br>open override val [claims](claims.md): [List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)&lt;[MsoMdocClaim](../-mso-mdoc-claim/index.md)&gt;<br>The list of claims.                                                                            |
| [nameSpacedData](name-spaced-data.md)                 | [androidJvm]<br>val [nameSpacedData](name-spaced-data.md): NameSpacedData<br>The name-spaced data.                                                                                                                                                                                                   |
| [nameSpacedDataDecoded](name-spaced-data-decoded.md)  | [androidJvm]<br>val [nameSpacedDataDecoded](name-spaced-data-decoded.md): [NameSpacedValues](../../eu.europa.ec.eudi.wallet.document/-name-spaced-values/index.md)&lt;[Any](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/index.html)?&gt;<br>The name-spaced data decoded.               |
| [nameSpacedDataInBytes](name-spaced-data-in-bytes.md) | [androidJvm]<br>val [nameSpacedDataInBytes](name-spaced-data-in-bytes.md): [NameSpacedValues](../../eu.europa.ec.eudi.wallet.document/-name-spaced-values/index.md)&lt;[ByteArray](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-byte-array/index.html)&gt;<br>The name-spaced data in bytes. |
| [nameSpaces](name-spaces.md)                          | [androidJvm]<br>val [nameSpaces](name-spaces.md): [NameSpaces](../../eu.europa.ec.eudi.wallet.document/-name-spaces/index.md)<br>The name-spaces.                                                                                                                                                    |

## Functions

| Name                     | Summary                                                                                                                                                                                                                                |
|--------------------------|----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| [equals](equals.md)      | [androidJvm]<br>open operator override fun [equals](equals.md)(other: [Any](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/index.html)?): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) |
| [hashCode](hash-code.md) | [androidJvm]<br>open override fun [hashCode](hash-code.md)(): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)                                                                                               |
