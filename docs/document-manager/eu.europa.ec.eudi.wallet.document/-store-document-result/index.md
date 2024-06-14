//[document-manager](../../../index.md)/[eu.europa.ec.eudi.wallet.document](../index.md)/[StoreDocumentResult](index.md)

# StoreDocumentResult

interface [StoreDocumentResult](index.md)

The result of [DocumentManager.storeIssuedDocument](../-document-manager/store-issued-document.md)
and [DocumentManager.storeDeferredDocument](../-document-manager/store-deferred-document.md) methods

#### Inheritors

|                              |
|------------------------------|
| [Success](-success/index.md) |
| [Failure](-failure/index.md) |

## Types

| Name                         | Summary                                                                                                                                                                                                                                                                                                                                                                                                                                                                                            |
|------------------------------|----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| [Failure](-failure/index.md) | [androidJvm]<br>data class [Failure](-failure/index.md)(val throwable: [Throwable](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-throwable/index.html)) : [StoreDocumentResult](index.md)<br>Failure while adding the document. Contains the throwable that caused the failure                                                                                                                                                                                                              |
| [Success](-success/index.md) | [androidJvm]<br>data class [Success](-success/index.md)(val documentId: [DocumentId](../index.md#659369697%2FClasslikes%2F1351694608), val proofOfProvisioning: [ByteArray](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-byte-array/index.html)? = null) : [StoreDocumentResult](index.md)<br>Success result containing the documentId. DocumentId can be then used to retrieve the document from the [DocumentManager.getDocumentById](../-document-manager/get-document-by-id.md) method |

## Functions

| Name                       | Summary                                                                                                                                                                                                                                                                                                                                                                                                                           |
|----------------------------|-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| [onFailure](on-failure.md) | [androidJvm]<br>open fun [onFailure](on-failure.md)(block: ([Throwable](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-throwable/index.html)) -&gt; [Unit](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)): [StoreDocumentResult](index.md)<br>Failure while adding the document. Contains the throwable that caused the failure                                                                     |
| [onSuccess](on-success.md) | [androidJvm]<br>open fun [onSuccess](on-success.md)(block: ([DocumentId](../index.md#659369697%2FClasslikes%2F1351694608), [ByteArray](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-byte-array/index.html)?) -&gt; [Unit](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)): [StoreDocumentResult](index.md)<br>Success result containing the documentId and the proof of provisioning if successful |
