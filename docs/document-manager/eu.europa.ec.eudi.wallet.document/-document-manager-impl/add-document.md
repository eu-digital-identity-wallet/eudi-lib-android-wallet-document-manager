//[document-manager](../../../index.md)/[eu.europa.ec.eudi.wallet.document](../index.md)/[DocumentManagerImpl](index.md)/[addDocument](add-document.md)

# addDocument

[androidJvm]\
open override fun [addDocument](add-document.md)(request: [IssuanceRequest](../-issuance-request/index.md), data: [ByteArray](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-byte-array/index.html)): [AddDocumentResult](../-add-document-result/index.md)

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

The document is added in the storage and can be retrieved using the DocumentManager::getDocumentById method.

#### Return

[AddDocumentResult.Success](../-add-document-result/-success/index.md) containing the documentId and the proof of provisioning if successful, [AddDocumentResult.Failure](../-add-document-result/-failure/index.md) otherwise

#### Parameters

androidJvm

| | |
|---|---|
| request | [IssuanceRequest](../-issuance-request/index.md) containing necessary information of the issued the document |
| data | in CBOR format containing the document's data |
