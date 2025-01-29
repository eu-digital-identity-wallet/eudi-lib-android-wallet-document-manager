//[document-manager](../../../index.md)/[eu.europa.ec.eudi.wallet.document.sample](../index.md)/[SampleDocumentManager](index.md)/[loadMdocSampleDocuments](load-mdoc-sample-documents.md)

# loadMdocSampleDocuments

[androidJvm]\
abstract fun [loadMdocSampleDocuments](load-mdoc-sample-documents.md)(sampleData: [ByteArray](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-byte-array/index.html), createSettings: [CreateDocumentSettings](../../eu.europa.ec.eudi.wallet.document/-create-document-settings/index.md), documentNamesMap: [Map](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin.collections/-map/index.html)&lt;[DocType](../../eu.europa.ec.eudi.wallet.document/-doc-type/index.md), [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-string/index.html)&gt;? = null): [Outcome](../../eu.europa.ec.eudi.wallet.document/-outcome/index.md)&lt;[List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin.collections/-list/index.html)&lt;[DocumentId](../../eu.europa.ec.eudi.wallet.document/-document-id/index.md)&gt;&gt;

Loads the sample documents that are in mdoc format into the document manager.

#### Return

returns the documentIds if successfully loaded, otherwise a error

Expected sampleData format is CBOR. The CBOR data must be in the following structure:

```cddl
SampleData = {
  "documents" : [+Document], ; Returned documents
}
Document = {
  "docType" : DocType, ; Document type returned
  "issuerSigned" : IssuerSigned, ; Returned data elements signed by the issuer
}
IssuerSigned = {
  "nameSpaces" : IssuerNameSpaces, ; Returned data elements
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

#### Parameters

androidJvm

| | |
|---|---|
| sampleData | the sample data in mdoc format to be loaded in cbor format |
| createSettings | the settings for creating new documents for the sample |
| documentNamesMap | the names of the documents per docType |
