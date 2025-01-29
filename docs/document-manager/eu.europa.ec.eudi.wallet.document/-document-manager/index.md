//[document-manager](../../../index.md)/[eu.europa.ec.eudi.wallet.document](../index.md)/[DocumentManager](index.md)

# DocumentManager

interface [DocumentManager](index.md)

The DocumentManager interface is the main entry point to interact with documents. It is a high-level abstraction that provides a simplified API to interact with documents.

It provides methods to:

- 
   Create a new document
- 
   Store a document
- 
   Retrieve a document
- 
   Delete a document
- 
   List all documents

To create a default instance of the DocumentManager, use the companion object or the [Builder](-builder/index.md) class.

#### See also

| |
|---|
| [DocumentManager.Builder](-builder/index.md) |

#### Inheritors

| |
|---|
| [DocumentManagerImpl](../-document-manager-impl/index.md) |
| [SampleDocumentManager](../../eu.europa.ec.eudi.wallet.document.sample/-sample-document-manager/index.md) |
| [SampleDocumentManagerImpl](../../eu.europa.ec.eudi.wallet.document.sample/-sample-document-manager-impl/index.md) |

## Types

| Name | Summary |
|---|---|
| [Builder](-builder/index.md) | [androidJvm]<br>class [Builder](-builder/index.md)<br>Builder class to create a [DocumentManager](index.md) instance. |
| [Companion](-companion/index.md) | [androidJvm]<br>object [Companion](-companion/index.md)<br>Companion object to create a [DocumentManager](index.md) instance. |

## Properties

| Name | Summary |
|---|---|
| [identifier](identifier.md) | [androidJvm]<br>abstract val [identifier](identifier.md): [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-string/index.html)<br>the identifier of the document manager |
| [secureAreaRepository](secure-area-repository.md) | [androidJvm]<br>abstract val [secureAreaRepository](secure-area-repository.md): SecureAreaRepository |
| [storageEngine](storage-engine.md) | [androidJvm]<br>abstract val [storageEngine](storage-engine.md): StorageEngine |

## Functions

| Name | Summary |
|---|---|
| [createDocument](create-document.md) | [androidJvm]<br>abstract fun [createDocument](create-document.md)(format: [DocumentFormat](../../eu.europa.ec.eudi.wallet.document.format/-document-format/index.md), createSettings: [CreateDocumentSettings](../-create-document-settings/index.md), documentMetaData: [DocumentMetaData](../../eu.europa.ec.eudi.wallet.document.metadata/-document-meta-data/index.md)? = null): [Outcome](../-outcome/index.md)&lt;[UnsignedDocument](../-unsigned-document/index.md)&gt;<br>Create a new document. This method will create a new document with the given format and keys settings. If the document is successfully created, it will return an [UnsignedDocument](../-unsigned-document/index.md). This [UnsignedDocument](../-unsigned-document/index.md) contains the keys and the method to proof the ownership of the keys, that can be used with an issuer to retrieve the document's claims. After that the document can be stored using [storeIssuedDocument](store-issued-document.md) or [storeDeferredDocument](store-deferred-document.md). |
| [deleteDocumentById](delete-document-by-id.md) | [androidJvm]<br>abstract fun [deleteDocumentById](delete-document-by-id.md)(documentId: [DocumentId](../-document-id/index.md)): [Outcome](../-outcome/index.md)&lt;[ProofOfDeletion](../-proof-of-deletion/index.md)?&gt;<br>Delete a document by its identifier. |
| [getDocumentById](get-document-by-id.md) | [androidJvm]<br>abstract fun [getDocumentById](get-document-by-id.md)(documentId: [DocumentId](../-document-id/index.md)): [Document](../-document/index.md)?<br>Retrieve a document by its identifier. |
| [getDocuments](get-documents.md) | [androidJvm]<br>abstract fun [getDocuments](get-documents.md)(predicate: ([Document](../-document/index.md)) -&gt; [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-boolean/index.html)? = null): [List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin.collections/-list/index.html)&lt;[Document](../-document/index.md)&gt;<br>Retrieve all documents. |
| [getDocuments](../get-documents.md) | [androidJvm]<br>inline fun &lt;[T](../get-documents.md) : [Document](../-document/index.md)&gt; [DocumentManager](index.md).[getDocuments](../get-documents.md)(): [List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin.collections/-list/index.html)&lt;[T](../get-documents.md)&gt;<br>DocumentManager Extension function that returns a list of documents of type [T](../get-documents.md). If [T](../get-documents.md) is [IssuedDocument](../-issued-document/index.md), then only [IssuedDocument](../-issued-document/index.md) will be returned. If [T](../get-documents.md) is [UnsignedDocument](../-unsigned-document/index.md), then only [UnsignedDocument](../-unsigned-document/index.md) will be returned, excluding [DeferredDocument](../-deferred-document/index.md). If [T](../get-documents.md) is [DeferredDocument](../-deferred-document/index.md), then only [DeferredDocument](../-deferred-document/index.md) will be returned. |
| [storeDeferredDocument](store-deferred-document.md) | [androidJvm]<br>abstract fun [storeDeferredDocument](store-deferred-document.md)(unsignedDocument: [UnsignedDocument](../-unsigned-document/index.md), relatedData: [ByteArray](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-byte-array/index.html)): [Outcome](../-outcome/index.md)&lt;[DeferredDocument](../-deferred-document/index.md)&gt;<br>Store an unsigned document for deferred issuance. This method will store the document with the related to the issuance data. |
| [storeIssuedDocument](store-issued-document.md) | [androidJvm]<br>abstract fun [storeIssuedDocument](store-issued-document.md)(unsignedDocument: [UnsignedDocument](../-unsigned-document/index.md), issuerProvidedData: [ByteArray](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-byte-array/index.html)): [Outcome](../-outcome/index.md)&lt;[IssuedDocument](../-issued-document/index.md)&gt;<br>Store an issued document. This method will store the document with its issuer provided data. |
