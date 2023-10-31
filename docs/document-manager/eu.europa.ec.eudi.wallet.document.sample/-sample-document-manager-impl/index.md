//[document-manager](../../../index.md)/[eu.europa.ec.eudi.wallet.document.sample](../index.md)/[SampleDocumentManagerImpl](index.md)

# SampleDocumentManagerImpl

class [SampleDocumentManagerImpl](index.md)(context: [Context](https://developer.android.com/reference/kotlin/android/content/Context.html), documentManager: [DocumentManager](../../eu.europa.ec.eudi.wallet.document/-document-manager/index.md)) : [DocumentManager](../../eu.europa.ec.eudi.wallet.document/-document-manager/index.md), [SampleDocumentManager](../-sample-document-manager/index.md)

A [SampleDocumentManager](../-sample-document-manager/index.md) implementation that composes a [DocumentManager](../../eu.europa.ec.eudi.wallet.document/-document-manager/index.md) and provides methods to load sample data.

#### Parameters

androidJvm

| | |
|---|---|
| context | the application context |
| documentManager | [DocumentManager](../../eu.europa.ec.eudi.wallet.document/-document-manager/index.md) implementation to delegate the document management operations |

## Constructors

| | |
|---|---|
| [SampleDocumentManagerImpl](-sample-document-manager-impl.md) | [androidJvm]<br>constructor(context: [Context](https://developer.android.com/reference/kotlin/android/content/Context.html), documentManager: [DocumentManager](../../eu.europa.ec.eudi.wallet.document/-document-manager/index.md)) |

## Functions

| Name | Summary |
|---|---|
| [addDocument](../../eu.europa.ec.eudi.wallet.document/-document-manager/add-document.md) | [androidJvm]<br>open override fun [addDocument](../../eu.europa.ec.eudi.wallet.document/-document-manager/add-document.md)(request: [IssuanceRequest](../../eu.europa.ec.eudi.wallet.document/-issuance-request/index.md), data: [ByteArray](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-byte-array/index.html)): [AddDocumentResult](../../eu.europa.ec.eudi.wallet.document/-add-document-result/index.md)<br>Add document to the document manager. |
| [createIssuanceRequest](../../eu.europa.ec.eudi.wallet.document/-document-manager/create-issuance-request.md) | [androidJvm]<br>open override fun [createIssuanceRequest](../../eu.europa.ec.eudi.wallet.document/-document-manager/create-issuance-request.md)(docType: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html), hardwareBacked: [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html), attestationChallenge: [ByteArray](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-byte-array/index.html)?): [CreateIssuanceRequestResult](../../eu.europa.ec.eudi.wallet.document/-create-issuance-request-result/index.md)<br>Create an issuance request for a given docType. The issuance request can be then used to issue the document from the issuer. The issuance request contains the certificate that must be sent to the issuer. |
| [deleteDocumentById](../../eu.europa.ec.eudi.wallet.document/-document-manager/delete-document-by-id.md) | [androidJvm]<br>open override fun [deleteDocumentById](../../eu.europa.ec.eudi.wallet.document/-document-manager/delete-document-by-id.md)(documentId: [DocumentId](../../eu.europa.ec.eudi.wallet.document/index.md#659369697%2FClasslikes%2F1351694608)): [DeleteDocumentResult](../../eu.europa.ec.eudi.wallet.document/-delete-document-result/index.md)<br>Delete document by id |
| [getDocumentById](../../eu.europa.ec.eudi.wallet.document/-document-manager/get-document-by-id.md) | [androidJvm]<br>open override fun [getDocumentById](../../eu.europa.ec.eudi.wallet.document/-document-manager/get-document-by-id.md)(documentId: [DocumentId](../../eu.europa.ec.eudi.wallet.document/index.md#659369697%2FClasslikes%2F1351694608)): [Document](../../eu.europa.ec.eudi.wallet.document/-document/index.md)?<br>Get document by id |
| [getDocuments](../../eu.europa.ec.eudi.wallet.document/-document-manager/get-documents.md) | [androidJvm]<br>open override fun [getDocuments](../../eu.europa.ec.eudi.wallet.document/-document-manager/get-documents.md)(): [List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)&lt;[Document](../../eu.europa.ec.eudi.wallet.document/-document/index.md)&gt;<br>Retrieve all documents |
| [hardwareBacked](hardware-backed.md) | [androidJvm]<br>fun [hardwareBacked](hardware-backed.md)(flag: [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)): [SampleDocumentManagerImpl](index.md)<br>Instructs the document manager to use hardware-backed keys for the sample documents. |
| [loadSampleData](load-sample-data.md) | [androidJvm]<br>open override fun [loadSampleData](load-sample-data.md)(sampleData: [ByteArray](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-byte-array/index.html)): [LoadSampleResult](../-load-sample-result/index.md)<br>Loads the sample data into the document manager. |

## Properties

| Name | Summary |
|---|---|
| [hardwareBacked](hardware-backed.md) | [androidJvm]<br>var [hardwareBacked](hardware-backed.md): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)<br>Indicates that hardware-backed keys should be used. Default is true if device supports it, false otherwise. |
