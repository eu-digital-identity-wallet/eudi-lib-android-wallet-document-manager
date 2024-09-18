//[document-manager](../../../index.md)/[eu.europa.ec.eudi.wallet.document](../index.md)/[DocumentManager](index.md)/[createDocument](create-document.md)

# createDocument

[androidJvm]\
abstract fun [createDocument](create-document.md)(
docType: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html),
useStrongBox: [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html),
attestationChallenge: [ByteArray](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-byte-array/index.html)? =
null): [CreateDocumentResult](../-create-document-result/index.md)

Creates a [UnsignedDocument](../-unsigned-document/index.md) for a given docType which can be then
used to issue the document from the issuer. The [UnsignedDocument](../-unsigned-document/index.md)
contains the certificate that must be sent to the issuer and
implements [UnsignedDocument.signWithAuthKey](../-unsigned-document/sign-with-auth-key.md) to sign
the proof of possession if needed by the issuer.

#### Return

[CreateDocumentResult.Success](../-create-document-result/-success/index.md) containing the issuance
request if successful, [CreateDocumentResult.Failure](../-create-document-result/-failure/index.md)
otherwise

#### Parameters

androidJvm

|                      |                                                                   |
|----------------------|-------------------------------------------------------------------|
| docType              | document's docType (example: &quot;eu.europa.ec.eudi.pid.1&quot;) |
| useStrongBox         | whether the document should be stored in hardware backed storage  |
| attestationChallenge | optional attestationChallenge to check provided by the issuer     |
