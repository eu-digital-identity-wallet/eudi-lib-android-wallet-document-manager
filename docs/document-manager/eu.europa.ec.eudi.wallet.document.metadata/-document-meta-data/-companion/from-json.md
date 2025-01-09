//[document-manager](../../../../index.md)/[eu.europa.ec.eudi.wallet.document.metadata](../../index.md)/[DocumentMetaData](../index.md)/[Companion](index.md)/[fromJson](from-json.md)

# fromJson

[androidJvm]\
fun [fromJson](from-json.md)(json: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)): [DocumentMetaData](../index.md)

Create a [DocumentMetaData](../index.md) object from a JSON string.

#### Return

the [DocumentMetaData](../index.md) object

#### Parameters

androidJvm

| | |
|---|---|
| json | the JSON string representation of the object |

#### Throws

| | |
|---|---|
| [IllegalArgumentException](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-illegal-argument-exception/index.html) | if the decoded input cannot be represented as a valid instance of [DocumentMetaData](../index.md) |
| SerializationException | if the given JSON string is not a valid JSON input |
