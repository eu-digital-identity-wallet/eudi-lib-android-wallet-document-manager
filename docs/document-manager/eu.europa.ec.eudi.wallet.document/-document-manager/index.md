//[document-manager](../../../index.md)/[eu.europa.ec.eudi.wallet.document](../index.md)/[DocumentManager](index.md)

# DocumentManager

interface [DocumentManager](index.md)

Document manager object is the entry point to access documents.

It is used to add, retrieve and delete documents.

A default implementation of this interface is implemented by [DocumentManagerImpl](../-document-manager-impl/index.md). To instantiate it, use the [eu.europa.ec.eudi.wallet.document.DocumentManager.Builder](-builder/index.md) class.

#### Inheritors

| |
|---|
| [DocumentManagerImpl](../-document-manager-impl/index.md) |
| [SampleDocumentManager](../../eu.europa.ec.eudi.wallet.document.sample/-sample-document-manager/index.md) |
| [SampleDocumentManagerImpl](../../eu.europa.ec.eudi.wallet.document.sample/-sample-document-manager-impl/index.md) |

## Types

| Name | Summary |
|---|---|
| [Builder](-builder/index.md) | [androidJvm]<br>class [Builder](-builder/index.md)(context: [Context](https://developer.android.com/reference/kotlin/android/content/Context.html))<br>Builder class to instantiate the default DocumentManager implementation. |

## Functions

| Name | Summary |
|---|---|
| [addDocument](add-document.md) | [androidJvm]<br>abstract fun [addDocument](add-document.md)(request: [IssuanceRequest](../-issuance-request/index.md), data: [ByteArray](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-byte-array/index.html)): [AddDocumentResult](../-add-document-result/index.md)<br>Add document to the document manager. |
| [createIssuanceRequest](create-issuance-request.md) | [androidJvm]<br>abstract fun [createIssuanceRequest](create-issuance-request.md)(docType: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html), hardwareBacked: [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html), attestationChallenge: [ByteArray](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-byte-array/index.html)? = null): [CreateIssuanceRequestResult](../-create-issuance-request-result/index.md)<br>Create an issuance request for a given docType. The issuance request can be then used to issue the document from the issuer. The issuance request contains the certificate that must be sent to the issuer. |
| [deleteDocumentById](delete-document-by-id.md) | [androidJvm]<br>abstract fun [deleteDocumentById](delete-document-by-id.md)(documentId: [DocumentId](../index.md#659369697%2FClasslikes%2F1351694608)): [DeleteDocumentResult](../-delete-document-result/index.md)<br>Delete document by id |
| [getDocumentById](get-document-by-id.md) | [androidJvm]<br>abstract fun [getDocumentById](get-document-by-id.md)(documentId: [DocumentId](../index.md#659369697%2FClasslikes%2F1351694608)): [Document](../-document/index.md)?<br>Get document by id |
| [getDocuments](get-documents.md) | [androidJvm]<br>abstract fun [getDocuments](get-documents.md)(): [List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)&lt;[Document](../-document/index.md)&gt;<br>Retrieve all documents |
