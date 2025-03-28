//[document-manager](../../../index.md)/[eu.europa.ec.eudi.wallet.document](../index.md)/[DocumentManagerImpl](index.md)

# DocumentManagerImpl

class [DocumentManagerImpl](index.md)(val identifier: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-string/index.html), val storageEngine: StorageEngine, val secureAreaRepository: SecureAreaRepository, val ktorHttpClientFactory: () -&gt; HttpClient? = null) : [DocumentManager](../-document-manager/index.md)

Document Manager Implementation

#### Parameters

androidJvm

| | |
|---|---|
| identifier | the identifier of the document manager |
| storageEngine | the storage engine |
| secureAreaRepository | the secure area |

## Constructors

| | |
|---|---|
| [DocumentManagerImpl](-document-manager-impl.md) | [androidJvm]<br>constructor(identifier: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-string/index.html), storageEngine: StorageEngine, secureAreaRepository: SecureAreaRepository, ktorHttpClientFactory: () -&gt; HttpClient? = null) |

## Types

| Name | Summary |
|---|---|
| [Companion](-companion/index.md) | [androidJvm]<br>object [Companion](-companion/index.md) |

## Properties

| Name | Summary |
|---|---|
| [identifier](identifier.md) | [androidJvm]<br>open override val [identifier](identifier.md): [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-string/index.html)<br>the identifier |
| [ktorHttpClientFactory](ktor-http-client-factory.md) | [androidJvm]<br>val [ktorHttpClientFactory](ktor-http-client-factory.md): () -&gt; HttpClient? = null |
| [secureAreaRepository](secure-area-repository.md) | [androidJvm]<br>open override val [secureAreaRepository](secure-area-repository.md): SecureAreaRepository<br>the secure area |
| [storageEngine](storage-engine.md) | [androidJvm]<br>open override val [storageEngine](storage-engine.md): StorageEngine<br>the storage engine |

## Functions

| Name | Summary |
|---|---|
| [createDocument](create-document.md) | [androidJvm]<br>open override fun [createDocument](create-document.md)(format: [DocumentFormat](../../eu.europa.ec.eudi.wallet.document.format/-document-format/index.md), createSettings: [CreateDocumentSettings](../-create-document-settings/index.md), documentMetaData: [DocumentMetaData](../../eu.europa.ec.eudi.wallet.document.metadata/-document-meta-data/index.md)?): [Outcome](../-outcome/index.md)&lt;[UnsignedDocument](../-unsigned-document/index.md)&gt;<br>Create a new document. This method will create a new document with the given format and keys settings. If the document is successfully created, it will return an [UnsignedDocument](../-unsigned-document/index.md). This [UnsignedDocument](../-unsigned-document/index.md) contains the keys and the method to proof the ownership of the keys, that can be used with an issuer to retrieve the document's claims. After that the document can be stored using [storeIssuedDocument](store-issued-document.md) or [storeDeferredDocument](store-deferred-document.md). |
| [deleteDocumentById](delete-document-by-id.md) | [androidJvm]<br>open override fun [deleteDocumentById](delete-document-by-id.md)(documentId: [DocumentId](../-document-id/index.md)): [Outcome](../-outcome/index.md)&lt;[ProofOfDeletion](../-proof-of-deletion/index.md)?&gt;<br>Delete a document by its identifier. |
| [getDocumentById](get-document-by-id.md) | [androidJvm]<br>open override fun [getDocumentById](get-document-by-id.md)(documentId: [DocumentId](../-document-id/index.md)): [Document](../-document/index.md)?<br>Retrieve a document by its identifier. |
| [getDocuments](get-documents.md) | [androidJvm]<br>open override fun [getDocuments](get-documents.md)(predicate: ([Document](../-document/index.md)) -&gt; [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-boolean/index.html)?): [List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin.collections/-list/index.html)&lt;[Document](../-document/index.md)&gt;<br>Retrieve all documents. |
| [getDocuments](../get-documents.md) | [androidJvm]<br>inline fun &lt;[T](../get-documents.md) : [Document](../-document/index.md)&gt; [DocumentManager](../-document-manager/index.md).[getDocuments](../get-documents.md)(): [List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin.collections/-list/index.html)&lt;[T](../get-documents.md)&gt;<br>DocumentManager Extension function that returns a list of documents of type [T](../get-documents.md). If [T](../get-documents.md) is [IssuedDocument](../-issued-document/index.md), then only [IssuedDocument](../-issued-document/index.md) will be returned. If [T](../get-documents.md) is [UnsignedDocument](../-unsigned-document/index.md), then only [UnsignedDocument](../-unsigned-document/index.md) will be returned, excluding [DeferredDocument](../-deferred-document/index.md). If [T](../get-documents.md) is [DeferredDocument](../-deferred-document/index.md), then only [DeferredDocument](../-deferred-document/index.md) will be returned. |
| [storeDeferredDocument](store-deferred-document.md) | [androidJvm]<br>open override fun [storeDeferredDocument](store-deferred-document.md)(unsignedDocument: [UnsignedDocument](../-unsigned-document/index.md), relatedData: [ByteArray](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-byte-array/index.html)): [Outcome](../-outcome/index.md)&lt;[DeferredDocument](../-deferred-document/index.md)&gt;<br>Store an unsigned document for deferred issuance. This method will store the document with the related to the issuance data. |
| [storeIssuedDocument](store-issued-document.md) | [androidJvm]<br>open override fun [storeIssuedDocument](store-issued-document.md)(unsignedDocument: [UnsignedDocument](../-unsigned-document/index.md), issuerProvidedData: [ByteArray](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-byte-array/index.html)): [Outcome](../-outcome/index.md)&lt;[IssuedDocument](../-issued-document/index.md)&gt;<br>Store an issued document. This method will store the document with its issuer provided data. |
