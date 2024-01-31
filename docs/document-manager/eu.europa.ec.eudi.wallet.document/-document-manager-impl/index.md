//[document-manager](../../../index.md)/[eu.europa.ec.eudi.wallet.document](../index.md)/[DocumentManagerImpl](index.md)

# DocumentManagerImpl

class [DocumentManagerImpl](index.md)(context: [Context](https://developer.android.com/reference/kotlin/android/content/Context.html), storageEngine: StorageEngine, secureArea: AndroidKeystoreSecureArea) : [DocumentManager](../-document-manager/index.md)

A [DocumentManager](../-document-manager/index.md) implementation that uses StorageEngine to store documents and AndroidKeystoreSecureArea for key management.

Features:

- 
   Enforces user authentication to access documents, if supported by the device
- 
   Enforces hardware backed keys, if supported by the device
- 
   P256 curve and Sign1 support for document keys

To instantiate it, use the [eu.europa.ec.eudi.wallet.document.DocumentManager.Builder](../-document-manager/-builder/index.md) class.

#### Parameters

androidJvm

| |
|---|
| context |
| storageEngine | storage engine used to store documents |
| secureArea | secure area used to store documents' keys |

## Constructors

| | |
|---|---|
| [DocumentManagerImpl](-document-manager-impl.md) | [androidJvm]<br>constructor(context: [Context](https://developer.android.com/reference/kotlin/android/content/Context.html), storageEngine: StorageEngine, secureArea: AndroidKeystoreSecureArea) |

## Types

| Name | Summary |
|---|---|
| [Companion](-companion/index.md) | [androidJvm]<br>object [Companion](-companion/index.md) |

## Functions

| Name | Summary |
|---|---|
| [addDocument](add-document.md) | [androidJvm]<br>open override fun [addDocument](add-document.md)(request: [IssuanceRequest](../-issuance-request/index.md), data: [ByteArray](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-byte-array/index.html)): [AddDocumentResult](../-add-document-result/index.md)<br>Add document to the document manager. |
| [checkPublicKeyBeforeAdding](check-public-key-before-adding.md) | [androidJvm]<br>fun [checkPublicKeyBeforeAdding](check-public-key-before-adding.md)(check: [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)): [DocumentManagerImpl](index.md)<br>Sets whether to check public key in MSO before adding document to storage. By default this is set to true. This check is done to prevent adding documents with public key that is not in MSO. The public key from the [IssuanceRequest](../-issuance-request/index.md) must match the public key in MSO. |
| [createIssuanceRequest](create-issuance-request.md) | [androidJvm]<br>open override fun [createIssuanceRequest](create-issuance-request.md)(docType: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html), hardwareBacked: [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html), attestationChallenge: [ByteArray](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-byte-array/index.html)?): [CreateIssuanceRequestResult](../-create-issuance-request-result/index.md)<br>Create an issuance request for a given docType. The issuance request can be then used to issue the document from the issuer. The issuance request contains the certificate that must be sent to the issuer. |
| [deleteDocumentById](delete-document-by-id.md) | [androidJvm]<br>open override fun [deleteDocumentById](delete-document-by-id.md)(documentId: [DocumentId](../index.md#659369697%2FClasslikes%2F1351694608)): [DeleteDocumentResult](../-delete-document-result/index.md)<br>Delete document by id |
| [getDocumentById](get-document-by-id.md) | [androidJvm]<br>open override fun [getDocumentById](get-document-by-id.md)(documentId: [DocumentId](../index.md#659369697%2FClasslikes%2F1351694608)): [Document](../-document/index.md)?<br>Get document by id |
| [getDocuments](get-documents.md) | [androidJvm]<br>open override fun [getDocuments](get-documents.md)(): [List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)&lt;[Document](../-document/index.md)&gt;<br>Retrieve all documents |
| [userAuth](user-auth.md) | [androidJvm]<br>fun [userAuth](user-auth.md)(enable: [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)): [DocumentManagerImpl](index.md)<br>Sets whether to require user authentication to access the document. |
| [userAuthTimeout](user-auth-timeout.md) | [androidJvm]<br>fun [userAuthTimeout](user-auth-timeout.md)(timeoutInMillis: [Long](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-long/index.html)): [DocumentManagerImpl](index.md)<br>Sets the timeout in milliseconds for user authentication. |

## Properties

| Name | Summary |
|---|---|
| [checkPublicKeyBeforeAdding](check-public-key-before-adding.md) | [androidJvm]<br>var [checkPublicKeyBeforeAdding](check-public-key-before-adding.md): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)<br>flag that indicates if the public key in the [IssuanceRequest](../-issuance-request/index.md) must match the public key in MSO |
| [userAuth](user-auth.md) | [androidJvm]<br>var [userAuth](user-auth.md): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)<br>flag that indicates if the document requires user authentication to be accessed |
| [userAuthTimeoutInMillis](user-auth-timeout-in-millis.md) | [androidJvm]<br>var [userAuthTimeoutInMillis](user-auth-timeout-in-millis.md): [Long](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-long/index.html)<br>timeout in milliseconds for user authentication |
