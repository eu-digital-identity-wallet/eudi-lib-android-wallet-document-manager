//[document-manager](../../../index.md)/[eu.europa.ec.eudi.wallet.document](../index.md)/[Document](index.md)

# Document

sealed interface [Document](index.md)

Document interface representing a document

#### Inheritors

|                                                    |
|----------------------------------------------------|
| [IssuedDocument](../-issued-document/index.md)     |
| [UnsignedDocument](../-unsigned-document/index.md) |

## Properties

| Name                                           | Summary                                                                                                                                                                                                                                                     |
|------------------------------------------------|-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| [createdAt](created-at.md)                     | [androidJvm]<br>abstract val [createdAt](created-at.md): [Instant](https://developer.android.com/reference/kotlin/java/time/Instant.html)<br>the creation date                                                                                              |
| [documentManagerId](document-manager-id.md)    | [androidJvm]<br>abstract val [documentManagerId](document-manager-id.md): [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)<br>the [DocumentManager.identifier](../-document-manager/identifier.md) related to this document |
| [format](format.md)                            | [androidJvm]<br>abstract val [format](format.md): [DocumentFormat](../../eu.europa.ec.eudi.wallet.document.format/-document-format/index.md)<br>the document format                                                                                         |
| [id](id.md)                                    | [androidJvm]<br>abstract val [id](id.md): [DocumentId](../-document-id/index.md)<br>the document id                                                                                                                                                         |
| [isCertified](is-certified.md)                 | [androidJvm]<br>abstract val [isCertified](is-certified.md): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)<br>whether the document is certified                                                                        |
| [isKeyInvalidated](is-key-invalidated.md)      | [androidJvm]<br>open val [isKeyInvalidated](is-key-invalidated.md): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)<br>whether the key is invalidated                                                                    |
| [keyAlias](key-alias.md)                       | [androidJvm]<br>abstract val [keyAlias](key-alias.md): [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)<br>the key alias                                                                                                    |
| [keyInfo](key-info.md)                         | [androidJvm]<br>open val [keyInfo](key-info.md): KeyInfo<br>the key info                                                                                                                                                                                    |
| [name](name.md)                                | [androidJvm]<br>abstract val [name](name.md): [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)<br>the document name                                                                                                         |
| [publicKeyCoseBytes](public-key-cose-bytes.md) | [androidJvm]<br>open val [publicKeyCoseBytes](public-key-cose-bytes.md): [ByteArray](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-byte-array/index.html)<br>the public key cose bytes                                                               |
| [secureArea](secure-area.md)                   | [androidJvm]<br>abstract val [secureArea](secure-area.md): SecureArea<br>the secure area                                                                                                                                                                    |

## Functions

| Name                             | Summary                                                                                                                                                                                                                                                                                                                                                   |
|----------------------------------|-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| [keyAgreement](key-agreement.md) | [androidJvm]<br>open fun [keyAgreement](key-agreement.md)(otherPublicKey: [ByteArray](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-byte-array/index.html), keyUnlockData: KeyUnlockData? = null): [Outcome](../-outcome/index.md)&lt;[SharedSecret](../-shared-secret/index.md)&gt;<br>Creates a shared secret given the other party's public key |
| [sign](sign.md)                  | [androidJvm]<br>open fun [sign](sign.md)(dataToSign: [ByteArray](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-byte-array/index.html), algorithm: Algorithm = Algorithm.ES256, keyUnlockData: KeyUnlockData? = null): [Outcome](../-outcome/index.md)&lt;EcSignature&gt;<br>Sign the data with the document key                                    |
