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
| [Builder](-builder/index.md) | [androidJvm]<br>class [Builder](-builder/index.md)<br>Builder class to instantiate a SampleDocumentManager. |
| [Companion](-companion/index.md) | [androidJvm]<br>object [Companion](-companion/index.md) |

## Properties

| Name | Summary |
|---|---|
| [identifier](../../eu.europa.ec.eudi.wallet.document/-document-manager/identifier.md) | [androidJvm]<br>abstract val [identifier](../../eu.europa.ec.eudi.wallet.document/-document-manager/identifier.md): [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-string/index.html)<br>Unique identifier for this document manager instance |
| [secureAreaRepository](../../eu.europa.ec.eudi.wallet.document/-document-manager/secure-area-repository.md) | [androidJvm]<br>abstract val [secureAreaRepository](../../eu.europa.ec.eudi.wallet.document/-document-manager/secure-area-repository.md): SecureAreaRepository<br>Repository for secure key management and cryptographic operations |
| [storage](../../eu.europa.ec.eudi.wallet.document/-document-manager/storage.md) | [androidJvm]<br>abstract val [storage](../../eu.europa.ec.eudi.wallet.document/-document-manager/storage.md): Storage<br>Storage mechanism for persistent document data |

## Functions

| Name | Summary |
|---|---|
| [createDocument](../../eu.europa.ec.eudi.wallet.document/-document-manager/create-document.md) | [androidJvm]<br>abstract fun [createDocument](../../eu.europa.ec.eudi.wallet.document/-document-manager/create-document.md)(format: [DocumentFormat](../../eu.europa.ec.eudi.wallet.document.format/-document-format/index.md), createSettings: [CreateDocumentSettings](../../eu.europa.ec.eudi.wallet.document/-create-document-settings/index.md), issuerMetadata: [IssuerMetadata](../../eu.europa.ec.eudi.wallet.document.metadata/-issuer-metadata/index.md)? = null): [Outcome](../../eu.europa.ec.eudi.wallet.document/-outcome/index.md)&lt;[UnsignedDocument](../../eu.europa.ec.eudi.wallet.document/-unsigned-document/index.md)&gt;<br>Creates a new document with the specified format and security settings. |
| [deleteDocumentById](../../eu.europa.ec.eudi.wallet.document/-document-manager/delete-document-by-id.md) | [androidJvm]<br>abstract fun [deleteDocumentById](../../eu.europa.ec.eudi.wallet.document/-document-manager/delete-document-by-id.md)(documentId: [DocumentId](../../eu.europa.ec.eudi.wallet.document/-document-id/index.md)): [Outcome](../../eu.europa.ec.eudi.wallet.document/-outcome/index.md)&lt;[ProofOfDeletion](../../eu.europa.ec.eudi.wallet.document/-proof-of-deletion/index.md)?&gt;<br>Deletes a document by its unique identifier. |
| [getDocumentById](../../eu.europa.ec.eudi.wallet.document/-document-manager/get-document-by-id.md) | [androidJvm]<br>abstract fun [getDocumentById](../../eu.europa.ec.eudi.wallet.document/-document-manager/get-document-by-id.md)(documentId: [DocumentId](../../eu.europa.ec.eudi.wallet.document/-document-id/index.md)): [Document](../../eu.europa.ec.eudi.wallet.document/-document/index.md)?<br>Retrieves a document by its unique identifier. |
| [getDocuments](../../eu.europa.ec.eudi.wallet.document/-document-manager/get-documents.md) | [androidJvm]<br>abstract fun [getDocuments](../../eu.europa.ec.eudi.wallet.document/-document-manager/get-documents.md)(predicate: ([Document](../../eu.europa.ec.eudi.wallet.document/-document/index.md)) -&gt; [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-boolean/index.html)? = null): [List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin.collections/-list/index.html)&lt;[Document](../../eu.europa.ec.eudi.wallet.document/-document/index.md)&gt;<br>Retrieves all documents managed by this DocumentManager instance. |
| [getDocuments](../../eu.europa.ec.eudi.wallet.document/get-documents.md) | [androidJvm]<br>inline fun &lt;[T](../../eu.europa.ec.eudi.wallet.document/get-documents.md) : [Document](../../eu.europa.ec.eudi.wallet.document/-document/index.md)&gt; [DocumentManager](../../eu.europa.ec.eudi.wallet.document/-document-manager/index.md).[getDocuments](../../eu.europa.ec.eudi.wallet.document/get-documents.md)(): [List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin.collections/-list/index.html)&lt;[T](../../eu.europa.ec.eudi.wallet.document/get-documents.md)&gt;<br>DocumentManager Extension function that returns a list of documents of type [T](../../eu.europa.ec.eudi.wallet.document/get-documents.md). If [T](../../eu.europa.ec.eudi.wallet.document/get-documents.md) is [IssuedDocument](../../eu.europa.ec.eudi.wallet.document/-issued-document/index.md), then only [IssuedDocument](../../eu.europa.ec.eudi.wallet.document/-issued-document/index.md) will be returned. If [T](../../eu.europa.ec.eudi.wallet.document/get-documents.md) is [UnsignedDocument](../../eu.europa.ec.eudi.wallet.document/-unsigned-document/index.md), then only [UnsignedDocument](../../eu.europa.ec.eudi.wallet.document/-unsigned-document/index.md) will be returned, excluding [DeferredDocument](../../eu.europa.ec.eudi.wallet.document/-deferred-document/index.md). If [T](../../eu.europa.ec.eudi.wallet.document/get-documents.md) is [DeferredDocument](../../eu.europa.ec.eudi.wallet.document/-deferred-document/index.md), then only [DeferredDocument](../../eu.europa.ec.eudi.wallet.document/-deferred-document/index.md) will be returned. |
| [loadMdocSampleDocuments](load-mdoc-sample-documents.md) | [androidJvm]<br>abstract fun [loadMdocSampleDocuments](load-mdoc-sample-documents.md)(sampleData: [ByteArray](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-byte-array/index.html), createSettings: [CreateDocumentSettings](../../eu.europa.ec.eudi.wallet.document/-create-document-settings/index.md), documentNamesMap: [Map](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin.collections/-map/index.html)&lt;[DocType](../../eu.europa.ec.eudi.wallet.document/-doc-type/index.md), [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-string/index.html)&gt;? = null): [Outcome](../../eu.europa.ec.eudi.wallet.document/-outcome/index.md)&lt;[List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin.collections/-list/index.html)&lt;[DocumentId](../../eu.europa.ec.eudi.wallet.document/-document-id/index.md)&gt;&gt;<br>Loads the sample documents that are in mdoc format into the document manager. |
| [storeDeferredDocument](../../eu.europa.ec.eudi.wallet.document/-document-manager/store-deferred-document.md) | [androidJvm]<br>abstract fun [storeDeferredDocument](../../eu.europa.ec.eudi.wallet.document/-document-manager/store-deferred-document.md)(unsignedDocument: [UnsignedDocument](../../eu.europa.ec.eudi.wallet.document/-unsigned-document/index.md), relatedData: [ByteArray](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-byte-array/index.html)): [Outcome](../../eu.europa.ec.eudi.wallet.document/-outcome/index.md)&lt;[DeferredDocument](../../eu.europa.ec.eudi.wallet.document/-deferred-document/index.md)&gt;<br>Stores an unsigned document for deferred issuance processing. |
| [storeIssuedDocument](../../eu.europa.ec.eudi.wallet.document/-document-manager/store-issued-document.md) | [androidJvm]<br>abstract fun [storeIssuedDocument](../../eu.europa.ec.eudi.wallet.document/-document-manager/store-issued-document.md)(unsignedDocument: [UnsignedDocument](../../eu.europa.ec.eudi.wallet.document/-unsigned-document/index.md), issuerProvidedData: [List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin.collections/-list/index.html)&lt;[IssuerProvidedCredential](../../eu.europa.ec.eudi.wallet.document.credential/-issuer-provided-credential/index.md)&gt;): [Outcome](../../eu.europa.ec.eudi.wallet.document/-outcome/index.md)&lt;[IssuedDocument](../../eu.europa.ec.eudi.wallet.document/-issued-document/index.md)&gt;<br>Stores a document that has completed the issuance process with an issuer. |
