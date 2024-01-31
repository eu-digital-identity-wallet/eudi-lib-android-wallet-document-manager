//[document-manager](../../../../index.md)/[eu.europa.ec.eudi.wallet.document](../../index.md)/[AddDocumentResult](../index.md)/[Failure](index.md)

# Failure

data class [Failure](index.md)(val throwable: [Throwable](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-throwable/index.html)) : [AddDocumentResult](../index.md)

Failure while adding the document. Contains the throwable that caused the failure

#### Parameters

androidJvm

| | |
|---|---|
| throwable | throwable that caused the failure |

## Constructors

| | |
|---|---|
| [Failure](-failure.md) | [androidJvm]<br>constructor(throwable: [Throwable](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-throwable/index.html)) |

## Functions

| Name | Summary |
|---|---|
| [onFailure](../on-failure.md) | [androidJvm]<br>open fun [onFailure](../on-failure.md)(block: ([Throwable](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-throwable/index.html)) -&gt; [Unit](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)): [AddDocumentResult](../index.md)<br>Failure while adding the document. Contains the throwable that caused the failure |
| [onSuccess](../on-success.md) | [androidJvm]<br>open fun [onSuccess](../on-success.md)(block: ([DocumentId](../../index.md#659369697%2FClasslikes%2F1351694608), [ByteArray](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-byte-array/index.html)) -&gt; [Unit](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)): [AddDocumentResult](../index.md)<br>Success result containing the documentId and the proof of provisioning if successful |

## Properties

| Name | Summary |
|---|---|
| [throwable](throwable.md) | [androidJvm]<br>val [throwable](throwable.md): [Throwable](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-throwable/index.html)<br>throwable that caused the failure |
