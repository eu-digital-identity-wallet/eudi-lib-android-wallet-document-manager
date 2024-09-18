//[document-manager](../../../index.md)/[eu.europa.ec.eudi.wallet.document](../index.md)/[CreateDocumentResult](index.md)

# CreateDocumentResult

sealed interface [CreateDocumentResult](index.md)

The result of [DocumentManager.createDocument](../-document-manager/create-document.md) method

#### Inheritors

|                              |
|------------------------------|
| [Success](-success/index.md) |
| [Failure](-failure/index.md) |

## Types

| Name                         | Summary                                                                                                                                                                                                                                                                                                                                                                                                                                       |
|------------------------------|-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| [Failure](-failure/index.md) | [androidJvm]<br>data class [Failure](-failure/index.md)(val throwable: [Throwable](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-throwable/index.html)) : [CreateDocumentResult](index.md)<br>Failure while creating a document. Contains the throwable that caused the failure                                                                                                                                                        |
| [Success](-success/index.md) | [androidJvm]<br>data class [Success](-success/index.md)(val unsignedDocument: [UnsignedDocument](../-unsigned-document/index.md)) : [CreateDocumentResult](index.md)<br>Success result containing the [UnsignedDocument](../-unsigned-document/index.md), that can be then used to issue the document from the issuer. The [UnsignedDocument](../-unsigned-document/index.md) contains the certificate chain that must be sent to the issuer. |

## Functions

| Name                          | Summary                                                                                                                                                                                                                                                                                                               |
|-------------------------------|-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| [getOrThrow](get-or-throw.md) | [androidJvm]<br>open fun [getOrThrow](get-or-throw.md)(): [UnsignedDocument](../-unsigned-document/index.md)<br>Get [UnsignedDocument](../-unsigned-document/index.md) or throw the throwable that caused the failure                                                                                                 |
| [onFailure](on-failure.md)    | [androidJvm]<br>open fun [onFailure](on-failure.md)(block: ([Throwable](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-throwable/index.html)) -&gt; [Unit](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)): [CreateDocumentResult](index.md)<br>Execute block if the result is a failure |
| [onSuccess](on-success.md)    | [androidJvm]<br>open fun [onSuccess](on-success.md)(block: ([UnsignedDocument](../-unsigned-document/index.md)) -&gt; [Unit](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)): [CreateDocumentResult](index.md)<br>Execute block if the result is successful                                    |
