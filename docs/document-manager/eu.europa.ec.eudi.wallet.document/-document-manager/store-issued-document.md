//[document-manager](../../../index.md)/[eu.europa.ec.eudi.wallet.document](../index.md)/[DocumentManager](index.md)/[storeIssuedDocument](store-issued-document.md)

# storeIssuedDocument

[androidJvm]\
abstract fun [storeIssuedDocument](store-issued-document.md)(
unsignedDocument: [UnsignedDocument](../-unsigned-document/index.md),
issuerProvidedData: [ByteArray](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-byte-array/index.html)): [Outcome](../-outcome/index.md)
&lt;[IssuedDocument](../-issued-document/index.md)&gt;

Store an issued document. This method will store the document with its issuer provided data.

#### Return

the result of the storage. If successful, it will return
the [IssuedDocument](../-issued-document/index.md). If not, it will return an error.

#### Parameters

androidJvm

|                    |                          |
|--------------------|--------------------------|
| unsignedDocument   | the unsigned document    |
| issuerProvidedData | the issuer provided data |
