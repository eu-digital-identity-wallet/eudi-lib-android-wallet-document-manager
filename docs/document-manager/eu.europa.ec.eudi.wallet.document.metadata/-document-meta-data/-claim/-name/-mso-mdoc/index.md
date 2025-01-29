//[document-manager](../../../../../../index.md)/[eu.europa.ec.eudi.wallet.document.metadata](../../../../index.md)/[DocumentMetaData](../../../index.md)/[Claim](../../index.md)/[Name](../index.md)/[MsoMdoc](index.md)

# MsoMdoc

[androidJvm]\
@Serializable

data class [MsoMdoc](index.md)(val name: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-string/index.html), val nameSpace: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-string/index.html)) : [DocumentMetaData.Claim.Name](../index.md)

MsoMdoc claim name.

## Constructors

| | |
|---|---|
| [MsoMdoc](-mso-mdoc.md) | [androidJvm]<br>constructor(name: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-string/index.html), nameSpace: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-string/index.html)) |

## Properties

| Name | Summary |
|---|---|
| [name](name.md) | [androidJvm]<br>open override val [name](name.md): [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-string/index.html)<br>the name of the claim |
| [nameSpace](name-space.md) | [androidJvm]<br>val [nameSpace](name-space.md): [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-string/index.html)<br>the namespace of the claim |
