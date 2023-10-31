//[document-manager](../../../../index.md)/[eu.europa.ec.eudi.wallet.document](../../index.md)/[AddDocumentResult](../index.md)/[Success](index.md)

# Success

class [Success](index.md)(val documentId: [DocumentId](../../index.md#659369697%2FClasslikes%2F1351694608), val proofOfProvisioning: [ByteArray](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-byte-array/index.html)) : [AddDocumentResult](../index.md)

Success result containing the documentId. DocumentId can be then used to retrieve the document from the DocumentManager::getDocumentById method

#### Parameters

androidJvm

| | |
|---|---|
| documentId | document's unique identifier |
| proofOfProvisioning | proof of provisioning |

## Constructors

| | |
|---|---|
| [Success](-success.md) | [androidJvm]<br>constructor(documentId: [DocumentId](../../index.md#659369697%2FClasslikes%2F1351694608), proofOfProvisioning: [ByteArray](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-byte-array/index.html)) |

## Properties

| Name | Summary |
|---|---|
| [documentId](document-id.md) | [androidJvm]<br>val [documentId](document-id.md): [DocumentId](../../index.md#659369697%2FClasslikes%2F1351694608)<br>document's unique identifier |
| [proofOfProvisioning](proof-of-provisioning.md) | [androidJvm]<br>val [proofOfProvisioning](proof-of-provisioning.md): [ByteArray](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-byte-array/index.html)<br>proof of provisioning |
