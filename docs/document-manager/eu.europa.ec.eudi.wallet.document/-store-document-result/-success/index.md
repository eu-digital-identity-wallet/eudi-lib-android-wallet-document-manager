//[document-manager](../../../../index.md)/[eu.europa.ec.eudi.wallet.document](../../index.md)/[StoreDocumentResult](../index.md)/[Success](index.md)

# Success

data class [Success](index.md)(val documentId: [DocumentId](../../index.md#659369697%2FClasslikes%2F1351694608), val
proofOfProvisioning: [ByteArray](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-byte-array/index.html)? =
null) : [StoreDocumentResult](../index.md)

Success result containing the documentId. DocumentId can be then used to retrieve the document from
the [DocumentManager.getDocumentById](../../-document-manager/get-document-by-id.md) method

#### Parameters

androidJvm

|                     |                              |
|---------------------|------------------------------|
| documentId          | document's unique identifier |
| proofOfProvisioning | proof of provisioning        |

## Constructors

|                        |                                                                                                                                                                                                                                 |
|------------------------|---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| [Success](-success.md) | [androidJvm]<br>constructor(documentId: [DocumentId](../../index.md#659369697%2FClasslikes%2F1351694608), proofOfProvisioning: [ByteArray](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-byte-array/index.html)? = null) |

## Functions

| Name                          | Summary                                                                                                                                                                                                                                                                                                                                                                                                                                    |
|-------------------------------|--------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| [equals](equals.md)           | [androidJvm]<br>open operator override fun [equals](equals.md)(other: [Any](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/index.html)?): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)                                                                                                                                                                                                     |
| [hashCode](hash-code.md)      | [androidJvm]<br>open override fun [hashCode](hash-code.md)(): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)                                                                                                                                                                                                                                                                                                   |
| [onFailure](../on-failure.md) | [androidJvm]<br>open fun [onFailure](../on-failure.md)(block: ([Throwable](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-throwable/index.html)) -&gt; [Unit](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)): [StoreDocumentResult](../index.md)<br>Failure while adding the document. Contains the throwable that caused the failure                                                                        |
| [onSuccess](../on-success.md) | [androidJvm]<br>open fun [onSuccess](../on-success.md)(block: ([DocumentId](../../index.md#659369697%2FClasslikes%2F1351694608), [ByteArray](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-byte-array/index.html)?) -&gt; [Unit](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)): [StoreDocumentResult](../index.md)<br>Success result containing the documentId and the proof of provisioning if successful |

## Properties

| Name                                            | Summary                                                                                                                                                                                       |
|-------------------------------------------------|-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| [documentId](document-id.md)                    | [androidJvm]<br>val [documentId](document-id.md): [DocumentId](../../index.md#659369697%2FClasslikes%2F1351694608)<br>document's unique identifier                                            |
| [proofOfProvisioning](proof-of-provisioning.md) | [androidJvm]<br>val [proofOfProvisioning](proof-of-provisioning.md): [ByteArray](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-byte-array/index.html)? = null<br>proof of provisioning |
