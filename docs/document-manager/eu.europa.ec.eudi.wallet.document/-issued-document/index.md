//[document-manager](../../../index.md)/[eu.europa.ec.eudi.wallet.document](../index.md)/[IssuedDocument](index.md)

# IssuedDocument

[androidJvm]\
data class [IssuedDocument](index.md)(val id: [DocumentId](../-document-id/index.md), val name: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-string/index.html), val documentManagerId: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-string/index.html), val isCertified: [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-boolean/index.html), val keyAlias: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-string/index.html), val secureArea: SecureArea, val createdAt: [Instant](https://developer.android.com/reference/kotlin/java/time/Instant.html), val validFrom: [Instant](https://developer.android.com/reference/kotlin/java/time/Instant.html), val validUntil: [Instant](https://developer.android.com/reference/kotlin/java/time/Instant.html), val issuedAt: [Instant](https://developer.android.com/reference/kotlin/java/time/Instant.html), val issuerProvidedData: [ByteArray](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-byte-array/index.html), val data: [DocumentData](../../eu.europa.ec.eudi.wallet.document.format/-document-data/index.md)) : [Document](../-document/index.md)

Represents an Issued Document

## Constructors

| | |
|---|---|
| [IssuedDocument](-issued-document.md) | [androidJvm]<br>constructor(id: [DocumentId](../-document-id/index.md), name: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-string/index.html), documentManagerId: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-string/index.html), isCertified: [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-boolean/index.html), keyAlias: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-string/index.html), secureArea: SecureArea, createdAt: [Instant](https://developer.android.com/reference/kotlin/java/time/Instant.html), validFrom: [Instant](https://developer.android.com/reference/kotlin/java/time/Instant.html), validUntil: [Instant](https://developer.android.com/reference/kotlin/java/time/Instant.html), issuedAt: [Instant](https://developer.android.com/reference/kotlin/java/time/Instant.html), issuerProvidedData: [ByteArray](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-byte-array/index.html), data: [DocumentData](../../eu.europa.ec.eudi.wallet.document.format/-document-data/index.md)) |

## Properties

| Name | Summary |
|---|---|
| [createdAt](created-at.md) | [androidJvm]<br>open override val [createdAt](created-at.md): [Instant](https://developer.android.com/reference/kotlin/java/time/Instant.html)<br>the creation date |
| [data](data.md) | [androidJvm]<br>val [data](data.md): [DocumentData](../../eu.europa.ec.eudi.wallet.document.format/-document-data/index.md)<br>the document data (format specific) |
| [documentManagerId](document-manager-id.md) | [androidJvm]<br>open override val [documentManagerId](document-manager-id.md): [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-string/index.html)<br>the [DocumentManager.identifier](../-document-manager/identifier.md) related to this document |
| [format](format.md) | [androidJvm]<br>open override val [format](format.md): [DocumentFormat](../../eu.europa.ec.eudi.wallet.document.format/-document-format/index.md)<br>the document format |
| [id](id.md) | [androidJvm]<br>open override val [id](id.md): [DocumentId](../-document-id/index.md)<br>the document id |
| [isCertified](is-certified.md) | [androidJvm]<br>open override val [isCertified](is-certified.md): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-boolean/index.html)<br>whether the document is certified |
| [isKeyInvalidated](../-document/is-key-invalidated.md) | [androidJvm]<br>open val [isKeyInvalidated](../-document/is-key-invalidated.md): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-boolean/index.html)<br>whether the key is invalidated |
| [issuedAt](issued-at.md) | [androidJvm]<br>val [issuedAt](issued-at.md): [Instant](https://developer.android.com/reference/kotlin/java/time/Instant.html)<br>the issuance date |
| [issuerProvidedData](issuer-provided-data.md) | [androidJvm]<br>val [issuerProvidedData](issuer-provided-data.md): [ByteArray](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-byte-array/index.html)<br>the issuer provided data |
| [keyAlias](key-alias.md) | [androidJvm]<br>open override val [keyAlias](key-alias.md): [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-string/index.html)<br>the key alias |
| [keyInfo](../-document/key-info.md) | [androidJvm]<br>open val [keyInfo](../-document/key-info.md): KeyInfo<br>the key info |
| [metadata](metadata.md) | [androidJvm]<br>open override val [metadata](metadata.md): [DocumentMetaData](../../eu.europa.ec.eudi.wallet.document.metadata/-document-meta-data/index.md)?<br>the document metadata |
| [name](name.md) | [androidJvm]<br>open override val [name](name.md): [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-string/index.html)<br>the document name |
| [nameSpacedDataJSONObject](../name-spaced-data-j-s-o-n-object.md) | [androidJvm]<br>@get:[JvmName](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin.jvm/-jvm-name/index.html)(name = &quot;nameSpacedDataAsJSONObject&quot;)<br>val [IssuedDocument](index.md).[nameSpacedDataJSONObject](../name-spaced-data-j-s-o-n-object.md): [JSONObject](https://developer.android.com/reference/kotlin/org/json/JSONObject.html)<br>Extension function to convert [IssuedDocument](index.md)'s nameSpacedData to [JSONObject](https://developer.android.com/reference/kotlin/org/json/JSONObject.html) Applicable only when [IssuedDocument.data](data.md) is [MsoMdocData](../../eu.europa.ec.eudi.wallet.document.format/-mso-mdoc-data/index.md) |
| [publicKeyCoseBytes](../-document/public-key-cose-bytes.md) | [androidJvm]<br>open val [publicKeyCoseBytes](../-document/public-key-cose-bytes.md): [ByteArray](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-byte-array/index.html)<br>the public key cose bytes |
| [secureArea](secure-area.md) | [androidJvm]<br>open override val [secureArea](secure-area.md): SecureArea<br>the secure area |
| [validFrom](valid-from.md) | [androidJvm]<br>val [validFrom](valid-from.md): [Instant](https://developer.android.com/reference/kotlin/java/time/Instant.html) |
| [validUntil](valid-until.md) | [androidJvm]<br>val [validUntil](valid-until.md): [Instant](https://developer.android.com/reference/kotlin/java/time/Instant.html) |

## Functions

| Name | Summary |
|---|---|
| [equals](equals.md) | [androidJvm]<br>open operator override fun [equals](equals.md)(other: [Any](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-any/index.html)?): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-boolean/index.html) |
| [hashCode](hash-code.md) | [androidJvm]<br>open override fun [hashCode](hash-code.md)(): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html) |
| [isValidAt](is-valid-at.md) | [androidJvm]<br>fun [isValidAt](is-valid-at.md)(time: [Instant](https://developer.android.com/reference/kotlin/java/time/Instant.html)): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-boolean/index.html)<br>Check if the document is valid at a given time, based on the validFrom and validUntil fields |
| [keyAgreement](../-document/key-agreement.md) | [androidJvm]<br>open fun [keyAgreement](../-document/key-agreement.md)(otherPublicKey: [ByteArray](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-byte-array/index.html), keyUnlockData: KeyUnlockData? = null): [Outcome](../-outcome/index.md)&lt;[SharedSecret](../-shared-secret/index.md)&gt;<br>Creates a shared secret given the other party's public key |
| [sign](../-document/sign.md) | [androidJvm]<br>open fun [sign](../-document/sign.md)(dataToSign: [ByteArray](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-byte-array/index.html), algorithm: Algorithm = Algorithm.ES256, keyUnlockData: KeyUnlockData? = null): [Outcome](../-outcome/index.md)&lt;EcSignature&gt;<br>Sign the data with the document key |
