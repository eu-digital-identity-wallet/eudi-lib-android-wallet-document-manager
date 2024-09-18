//[document-manager](../../../index.md)/[eu.europa.ec.eudi.wallet.document](../index.md)/[DocumentManagerImpl](index.md)/[storeIssuedDocument](store-issued-document.md)

# storeIssuedDocument

[androidJvm]\
open override fun [storeIssuedDocument](store-issued-document.md)(
unsignedDocument: [UnsignedDocument](../-unsigned-document/index.md),
issuerDocumentData: [ByteArray](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-byte-array/index.html)): [StoreDocumentResult](../-store-document-result/index.md)

Add document to the document manager.

Expected data format is CBOR. The CBOR data must be in the following structure:

```cddl
IssuerSigned = {
  ?"nameSpaces" : IssuerNameSpaces, ; Returned data elements
  "issuerAuth" : IssuerAuth ; Contains the mobile security object (MSO) for issuer data authentication
}
IssuerNameSpaces = { ; Returned data elements for each namespace
  + NameSpace => [ + IssuerSignedItemBytes ]
}
IssuerSignedItemBytes = #6.24(bstr .cbor IssuerSignedItem)
IssuerSignedItem = {
  "digestID" : uint, ; Digest ID for issuer data authentication
  "random" : bstr, ; Random value for issuer data authentication
  "elementIdentifier" : DataElementIdentifier, ; Data element identifier
  "elementValue" : DataElementValue ; Data element value
}
IssuerAuth = COSE_Sign1 ; The payload is MobileSecurityObjectBytes
```

**Important** Currently `nameSpaces` field should exist and must not be empty.

The document is added in the storage and can be retrieved using
the [DocumentManager.getDocumentById](../-document-manager/get-document-by-id.md) method.

#### Return

[StoreDocumentResult.Success](../-store-document-result/-success/index.md) containing the documentId
and the proof of provisioning if
successful, [StoreDocumentResult.Failure](../-store-document-result/-failure/index.md) otherwise

#### Parameters

androidJvm

|                    |                                                                                                                |
|--------------------|----------------------------------------------------------------------------------------------------------------|
| unsignedDocument   | [UnsignedDocument](../-unsigned-document/index.md) containing necessary information of the issued the document |
| issuerDocumentData | in CBOR format containing the document's data                                                                  |
