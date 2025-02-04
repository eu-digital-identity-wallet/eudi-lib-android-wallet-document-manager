//[document-manager](../../../index.md)/[eu.europa.ec.eudi.wallet.document](../index.md)/[DeferredDocument](index.md)

# DeferredDocument

[androidJvm]\
class [DeferredDocument](index.md)(val id: [DocumentId](../-document-id/index.md), var name: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-string/index.html), val format: [DocumentFormat](../../eu.europa.ec.eudi.wallet.document.format/-document-format/index.md), val documentManagerId: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-string/index.html), val isCertified: [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-boolean/index.html), val keyAlias: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-string/index.html), val secureArea: SecureArea, val createdAt: [Instant](https://developer.android.com/reference/kotlin/java/time/Instant.html), documentMetaData: [DocumentMetaData](../../eu.europa.ec.eudi.wallet.document.metadata/-document-meta-data/index.md)?, val relatedData: [ByteArray](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-byte-array/index.html)) : [UnsignedDocument](../-unsigned-document/index.md)

Represents a Deferred Document. A Deferred Document is also an [UnsignedDocument](../-unsigned-document/index.md), since it is not yet signed by the issuer.

## Constructors

| | |
|---|---|
| [DeferredDocument](-deferred-document.md) | [androidJvm]<br>constructor(id: [DocumentId](../-document-id/index.md), name: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-string/index.html), format: [DocumentFormat](../../eu.europa.ec.eudi.wallet.document.format/-document-format/index.md), documentManagerId: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-string/index.html), isCertified: [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-boolean/index.html), keyAlias: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-string/index.html), secureArea: SecureArea, createdAt: [Instant](https://developer.android.com/reference/kotlin/java/time/Instant.html), documentMetaData: [DocumentMetaData](../../eu.europa.ec.eudi.wallet.document.metadata/-document-meta-data/index.md)?, relatedData: [ByteArray](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-byte-array/index.html)) |

## Properties

| Name | Summary |
|---|---|
| [createdAt](../-unsigned-document/created-at.md) | [androidJvm]<br>open override val [createdAt](../-unsigned-document/created-at.md): [Instant](https://developer.android.com/reference/kotlin/java/time/Instant.html)<br>the creation date |
| [documentManagerId](../-unsigned-document/document-manager-id.md) | [androidJvm]<br>open override val [documentManagerId](../-unsigned-document/document-manager-id.md): [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-string/index.html)<br>the [DocumentManager.identifier](../-document-manager/identifier.md) related to this document |
| [format](../-unsigned-document/format.md) | [androidJvm]<br>open override val [format](../-unsigned-document/format.md): [DocumentFormat](../../eu.europa.ec.eudi.wallet.document.format/-document-format/index.md)<br>the document format |
| [id](../-unsigned-document/id.md) | [androidJvm]<br>open override val [id](../-unsigned-document/id.md): [DocumentId](../-document-id/index.md)<br>the document id |
| [isCertified](../-unsigned-document/is-certified.md) | [androidJvm]<br>open override val [isCertified](../-unsigned-document/is-certified.md): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-boolean/index.html)<br>whether the document is certified |
| [isKeyInvalidated](../-document/is-key-invalidated.md) | [androidJvm]<br>open val [isKeyInvalidated](../-document/is-key-invalidated.md): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-boolean/index.html)<br>whether the key is invalidated |
| [keyAlias](../-unsigned-document/key-alias.md) | [androidJvm]<br>open override val [keyAlias](../-unsigned-document/key-alias.md): [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-string/index.html)<br>the key alias |
| [keyInfo](../-document/key-info.md) | [androidJvm]<br>open val [keyInfo](../-document/key-info.md): KeyInfo<br>the key info |
| [metadata](../-unsigned-document/metadata.md) | [androidJvm]<br>open override val [metadata](../-unsigned-document/metadata.md): [DocumentMetaData](../../eu.europa.ec.eudi.wallet.document.metadata/-document-meta-data/index.md)?<br>the document metadata |
| [name](../-unsigned-document/name.md) | [androidJvm]<br>open override var [name](../-unsigned-document/name.md): [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-string/index.html)<br>the document name |
| [publicKeyCoseBytes](../-document/public-key-cose-bytes.md) | [androidJvm]<br>open val [publicKeyCoseBytes](../-document/public-key-cose-bytes.md): [ByteArray](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-byte-array/index.html)<br>the public key cose bytes |
| [relatedData](related-data.md) | [androidJvm]<br>val [relatedData](related-data.md): [ByteArray](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-byte-array/index.html)<br>the related data |
| [secureArea](../-unsigned-document/secure-area.md) | [androidJvm]<br>open override val [secureArea](../-unsigned-document/secure-area.md): SecureArea<br>the secure area |

## Functions

| Name | Summary |
|---|---|
| [keyAgreement](../-document/key-agreement.md) | [androidJvm]<br>open fun [keyAgreement](../-document/key-agreement.md)(otherPublicKey: [ByteArray](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-byte-array/index.html), keyUnlockData: KeyUnlockData? = null): [Outcome](../-outcome/index.md)&lt;[SharedSecret](../-shared-secret/index.md)&gt;<br>Creates a shared secret given the other party's public key |
| [sign](../-document/sign.md) | [androidJvm]<br>open fun [sign](../-document/sign.md)(dataToSign: [ByteArray](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-byte-array/index.html), algorithm: Algorithm = Algorithm.ES256, keyUnlockData: KeyUnlockData? = null): [Outcome](../-outcome/index.md)&lt;EcSignature&gt;<br>Sign the data with the document key |
