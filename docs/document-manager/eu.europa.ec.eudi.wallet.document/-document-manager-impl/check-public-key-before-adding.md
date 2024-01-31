//[document-manager](../../../index.md)/[eu.europa.ec.eudi.wallet.document](../index.md)/[DocumentManagerImpl](index.md)/[checkPublicKeyBeforeAdding](check-public-key-before-adding.md)

# checkPublicKeyBeforeAdding

[androidJvm]\
fun [checkPublicKeyBeforeAdding](check-public-key-before-adding.md)(check: [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)): [DocumentManagerImpl](index.md)

Sets whether to check public key in MSO before adding document to storage. By default this is set to true. This check is done to prevent adding documents with public key that is not in MSO. The public key from the [IssuanceRequest](../-issuance-request/index.md) must match the public key in MSO.

#### Parameters

androidJvm

| |
|---|
| check |

#### See also

| |
|---|
| [DocumentManager.addDocument](../-document-manager/add-document.md) |

[androidJvm]\
var [checkPublicKeyBeforeAdding](check-public-key-before-adding.md): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)
