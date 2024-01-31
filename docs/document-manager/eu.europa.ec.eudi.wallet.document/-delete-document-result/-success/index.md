//[document-manager](../../../../index.md)/[eu.europa.ec.eudi.wallet.document](../../index.md)/[DeleteDocumentResult](../index.md)/[Success](index.md)

# Success

data class [Success](index.md)(val proofOfDeletion: [ByteArray](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-byte-array/index.html)?) : [DeleteDocumentResult](../index.md)

Success result containing the proof of deletion

#### Parameters

androidJvm

| |
|---|
| proofOfDeletion |

## Constructors

| | |
|---|---|
| [Success](-success.md) | [androidJvm]<br>constructor(proofOfDeletion: [ByteArray](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-byte-array/index.html)?) |

## Functions

| Name | Summary |
|---|---|
| [equals](equals.md) | [androidJvm]<br>open operator override fun [equals](equals.md)(other: [Any](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/index.html)?): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) |
| [hashCode](hash-code.md) | [androidJvm]<br>open override fun [hashCode](hash-code.md)(): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html) |
| [onFailure](../on-failure.md) | [androidJvm]<br>open fun [onFailure](../on-failure.md)(block: ([Throwable](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-throwable/index.html)) -&gt; [Unit](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)): [DeleteDocumentResult](../index.md)<br>Execute block if the result is a failure |
| [onSuccess](../on-success.md) | [androidJvm]<br>open fun [onSuccess](../on-success.md)(block: ([ByteArray](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-byte-array/index.html)?) -&gt; [Unit](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)): [DeleteDocumentResult](../index.md)<br>Execute block if the result is successful |

## Properties

| Name | Summary |
|---|---|
| [proofOfDeletion](proof-of-deletion.md) | [androidJvm]<br>val [proofOfDeletion](proof-of-deletion.md): [ByteArray](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-byte-array/index.html)? |
