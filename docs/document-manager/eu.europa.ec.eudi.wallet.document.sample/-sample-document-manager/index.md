//[document-manager](../../../index.md)/[eu.europa.ec.eudi.wallet.document.sample](../index.md)/[SampleDocumentManager](index.md)

# SampleDocumentManager

interface [SampleDocumentManager](index.md) : [DocumentManager](../../eu.europa.ec.eudi.wallet.document/-document-manager/index.md)

An extension of [DocumentManager](../../eu.europa.ec.eudi.wallet.document/-document-manager/index.md) that provides methods to load sample data.

The sample data is a CBOR file that contains the following information:

- 
   A list of documents to be loaded in the document manager.
- 
   Each document contains:
- 
   The document's docType
- 
   A list of namespaces and element identifiers with their corresponding values and random values and digest values to be used in the hash computation.

A default implementation is provided by [SampleDocumentManagerImpl](../-sample-document-manager-impl/index.md). To instantiate a [SampleDocumentManagerImpl](../-sample-document-manager-impl/index.md), use the [Builder](-builder/index.md) class.

```kotlin
val documentManager = SampleDocumentManager.Builder(context)
     .hardwareBacked(true)
     .build()
```

You can also use a different [DocumentManager](../../eu.europa.ec.eudi.wallet.document/-document-manager/index.md) implementation, by passing it to the [Builder](-builder/index.md) class. By default, the [DocumentManager](../../eu.europa.ec.eudi.wallet.document/-document-manager/index.md) implementation used is [eu.europa.ec.eudi.wallet.document.DocumentManagerImpl](../../eu.europa.ec.eudi.wallet.document/-document-manager-impl/index.md).

```kotlin
val sampleDocumentManager = SampleDocumentManager.Builder(context)
    documentManager = MyDocumentManager()
}.build()
```

#### Inheritors

| |
|---|
| [SampleDocumentManagerImpl](../-sample-document-manager-impl/index.md) |

## Types

| Name | Summary |
|---|---|
| [Builder](-builder/index.md) | [androidJvm]<br>class [Builder](-builder/index.md)(context: [Context](https://developer.android.com/reference/kotlin/android/content/Context.html))<br>Builder class to instantiate a SampleDocumentManager. |

## Functions

| Name | Summary |
|---|---|
| [addDocument](../../eu.europa.ec.eudi.wallet.document/-document-manager/add-document.md) | [androidJvm]<br>abstract fun [addDocument](../../eu.europa.ec.eudi.wallet.document/-document-manager/add-document.md)(request: [IssuanceRequest](../../eu.europa.ec.eudi.wallet.document/-issuance-request/index.md), data: [ByteArray](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-byte-array/index.html)): [AddDocumentResult](../../eu.europa.ec.eudi.wallet.document/-add-document-result/index.md)<br>Add document to the document manager. |
| [createIssuanceRequest](../../eu.europa.ec.eudi.wallet.document/-document-manager/create-issuance-request.md) | [androidJvm]<br>abstract fun [createIssuanceRequest](../../eu.europa.ec.eudi.wallet.document/-document-manager/create-issuance-request.md)(docType: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html), hardwareBacked: [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html), attestationChallenge: [ByteArray](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-byte-array/index.html)? = null): [CreateIssuanceRequestResult](../../eu.europa.ec.eudi.wallet.document/-create-issuance-request-result/index.md)<br>Create an issuance request for a given docType. The issuance request can be then used to issue the document from the issuer. The issuance request contains the certificate that must be sent to the issuer. |
| [deleteDocumentById](../../eu.europa.ec.eudi.wallet.document/-document-manager/delete-document-by-id.md) | [androidJvm]<br>abstract fun [deleteDocumentById](../../eu.europa.ec.eudi.wallet.document/-document-manager/delete-document-by-id.md)(documentId: [DocumentId](../../eu.europa.ec.eudi.wallet.document/index.md#659369697%2FClasslikes%2F1351694608)): [DeleteDocumentResult](../../eu.europa.ec.eudi.wallet.document/-delete-document-result/index.md)<br>Delete document by id |
| [getDocumentById](../../eu.europa.ec.eudi.wallet.document/-document-manager/get-document-by-id.md) | [androidJvm]<br>abstract fun [getDocumentById](../../eu.europa.ec.eudi.wallet.document/-document-manager/get-document-by-id.md)(documentId: [DocumentId](../../eu.europa.ec.eudi.wallet.document/index.md#659369697%2FClasslikes%2F1351694608)): [Document](../../eu.europa.ec.eudi.wallet.document/-document/index.md)?<br>Get document by id |
| [getDocuments](../../eu.europa.ec.eudi.wallet.document/-document-manager/get-documents.md) | [androidJvm]<br>abstract fun [getDocuments](../../eu.europa.ec.eudi.wallet.document/-document-manager/get-documents.md)(): [List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)&lt;[Document](../../eu.europa.ec.eudi.wallet.document/-document/index.md)&gt;<br>Retrieve all documents |
| [loadSampleData](load-sample-data.md) | [androidJvm]<br>abstract fun [loadSampleData](load-sample-data.md)(sampleData: [ByteArray](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-byte-array/index.html)): [LoadSampleResult](../-load-sample-result/index.md)<br>Loads the sample data into the document manager. |
