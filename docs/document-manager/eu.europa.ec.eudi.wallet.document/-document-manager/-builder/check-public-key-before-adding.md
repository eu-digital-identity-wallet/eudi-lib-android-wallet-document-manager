//[document-manager](../../../../index.md)/[eu.europa.ec.eudi.wallet.document](../../index.md)/[DocumentManager](../index.md)/[Builder](index.md)/[checkPublicKeyBeforeAdding](check-public-key-before-adding.md)

# checkPublicKeyBeforeAdding

[androidJvm]\
fun [checkPublicKeyBeforeAdding](check-public-key-before-adding.md)(checkPublicKeyBeforeAdding: [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)): [DocumentManager.Builder](index.md)

Sets whether to check public key in MSO before adding document to storage. By default this is set to true. This check is done to prevent adding documents with public key that is not in MSO. The public key from the [IssuanceRequest](../../-issuance-request/index.md) must match the public key in MSO.

#### Parameters

androidJvm

| |
|---|
| checkPublicKeyBeforeAdding |

#### See also

| |
|---|
| [DocumentManager.addDocument](../add-document.md) |

[androidJvm]\
var [checkPublicKeyBeforeAdding](check-public-key-before-adding.md): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)
