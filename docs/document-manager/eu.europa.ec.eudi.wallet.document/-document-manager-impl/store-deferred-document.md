//[document-manager](../../../index.md)/[eu.europa.ec.eudi.wallet.document](../index.md)/[DocumentManagerImpl](index.md)/[storeDeferredDocument](store-deferred-document.md)

# storeDeferredDocument

[androidJvm]\
open override fun [storeDeferredDocument](store-deferred-document.md)(
unsignedDocument: [UnsignedDocument](../-unsigned-document/index.md),
relatedData: [ByteArray](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-byte-array/index.html)): [StoreDocumentResult](../-store-document-result/index.md)

Stores a [UnsignedDocument](../-unsigned-document/index.md)
as [DeferredDocument](../-deferred-document/index.md). The document can be retrieved using
the [DocumentManager.getDocumentById](../-document-manager/get-document-by-id.md) method. Also, the
relatedData can be used later for the issuance process.

#### Return

[StoreDocumentResult.Success](../-store-document-result/-success/index.md) containing the documentId
if successful, [StoreDocumentResult.Failure](../-store-document-result/-failure/index.md) otherwise

#### Parameters

androidJvm

|                  |                                                                                                                |
|------------------|----------------------------------------------------------------------------------------------------------------|
| unsignedDocument | [UnsignedDocument](../-unsigned-document/index.md) containing necessary information of the issued the document |
| relatedData      | related data to deferred process to be stored with the document                                                |
