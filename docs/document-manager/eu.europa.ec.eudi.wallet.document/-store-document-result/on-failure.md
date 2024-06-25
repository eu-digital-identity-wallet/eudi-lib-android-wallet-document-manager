//[document-manager](../../../index.md)/[eu.europa.ec.eudi.wallet.document](../index.md)/[StoreDocumentResult](index.md)/[onFailure](on-failure.md)

# onFailure

[androidJvm]\
open fun [onFailure](on-failure.md)(
block: ([Throwable](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-throwable/index.html))
-&gt; [Unit](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)): [StoreDocumentResult](index.md)

Failure while adding the document. Contains the throwable that caused the failure

#### Return

[StoreDocumentResult](index.md)

#### Parameters

androidJvm

|       |                                                 |
|-------|-------------------------------------------------|
| block | block to be executed if the result is a failure |
