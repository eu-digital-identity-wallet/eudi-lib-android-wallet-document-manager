//[document-manager](../../../../index.md)/[eu.europa.ec.eudi.wallet.document](../../index.md)/[StoreDocumentResult](../index.md)/[Failure](index.md)

# Failure

data class [Failure](index.md)(val
throwable: [Throwable](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-throwable/index.html)) : [StoreDocumentResult](../index.md)

Failure while adding the document. Contains the throwable that caused the failure

#### Parameters

androidJvm

|           |                                   |
|-----------|-----------------------------------|
| throwable | throwable that caused the failure |

## Constructors

|                        |                                                                                                                                |
|------------------------|--------------------------------------------------------------------------------------------------------------------------------|
| [Failure](-failure.md) | [androidJvm]<br>constructor(throwable: [Throwable](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-throwable/index.html)) |

## Properties

| Name                      | Summary                                                                                                                                                                    |
|---------------------------|----------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| [throwable](throwable.md) | [androidJvm]<br>val [throwable](throwable.md): [Throwable](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-throwable/index.html)<br>throwable that caused the failure |

## Functions

| Name                          | Summary                                                                                                                                                                                                                                                                                                                                                                                                             |
|-------------------------------|---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| [onFailure](../on-failure.md) | [androidJvm]<br>open fun [onFailure](../on-failure.md)(block: ([Throwable](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-throwable/index.html)) -&gt; [Unit](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)): [StoreDocumentResult](../index.md)<br>Failure while adding the document. Contains the throwable that caused the failure                                                 |
| [onSuccess](../on-success.md) | [androidJvm]<br>open fun [onSuccess](../on-success.md)(block: ([DocumentId](../../-document-id/index.md), [ByteArray](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-byte-array/index.html)?) -&gt; [Unit](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)): [StoreDocumentResult](../index.md)<br>Success result containing the documentId and the proof of provisioning if successful |
