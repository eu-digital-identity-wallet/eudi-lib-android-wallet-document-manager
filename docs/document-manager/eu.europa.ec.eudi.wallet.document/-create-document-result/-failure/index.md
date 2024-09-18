//[document-manager](../../../../index.md)/[eu.europa.ec.eudi.wallet.document](../../index.md)/[CreateDocumentResult](../index.md)/[Failure](index.md)

# Failure

[androidJvm]\
data class [Failure](index.md)(val
throwable: [Throwable](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-throwable/index.html)) : [CreateDocumentResult](../index.md)

Failure while creating a document. Contains the throwable that caused the failure

## Constructors

|                        |                                                                                                                                                        |
|------------------------|--------------------------------------------------------------------------------------------------------------------------------------------------------|
| [Failure](-failure.md) | [androidJvm]<br>constructor(throwable: [Throwable](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-throwable/index.html))<br>Create empty Failure |

## Properties

| Name                      | Summary                                                                                                                               |
|---------------------------|---------------------------------------------------------------------------------------------------------------------------------------|
| [throwable](throwable.md) | [androidJvm]<br>val [throwable](throwable.md): [Throwable](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-throwable/index.html) |

## Functions

| Name                             | Summary                                                                                                                                                                                                                                                                                                                     |
|----------------------------------|-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| [getOrThrow](../get-or-throw.md) | [androidJvm]<br>open fun [getOrThrow](../get-or-throw.md)(): [UnsignedDocument](../../-unsigned-document/index.md)<br>Get [UnsignedDocument](../../-unsigned-document/index.md) or throw the throwable that caused the failure                                                                                              |
| [onFailure](../on-failure.md)    | [androidJvm]<br>open fun [onFailure](../on-failure.md)(block: ([Throwable](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-throwable/index.html)) -&gt; [Unit](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)): [CreateDocumentResult](../index.md)<br>Execute block if the result is a failure |
| [onSuccess](../on-success.md)    | [androidJvm]<br>open fun [onSuccess](../on-success.md)(block: ([UnsignedDocument](../../-unsigned-document/index.md)) -&gt; [Unit](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)): [CreateDocumentResult](../index.md)<br>Execute block if the result is successful                                 |
