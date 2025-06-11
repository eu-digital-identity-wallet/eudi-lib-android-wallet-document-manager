//[document-manager](../../../index.md)/[eu.europa.ec.eudi.wallet.document](../index.md)/[Document](index.md)

# Document

sealed interface [Document](index.md)

Base interface for all document types in the EUDI Wallet ecosystem.

The Document interface defines common properties and behaviors shared by all document types: unsigned documents, documents in the process of being issued, and fully issued documents. Documents are identified by a unique ID and have associated metadata and credentials.

#### Inheritors

| |
|---|
| [IssuedDocument](../-issued-document/index.md) |
| [UnsignedDocument](../-unsigned-document/index.md) |

## Properties

| Name                                           | Summary                                                                                                                                                                                                                                      |
|------------------------------------------------|----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| [createdAt](created-at.md)                     | [androidJvm]<br>abstract val [createdAt](created-at.md): [Instant](https://developer.android.com/reference/kotlin/java/time/Instant.html)<br>The timestamp when the document was created in the wallet                                       |
| [documentManagerId](document-manager-id.md)    | [androidJvm]<br>abstract val [documentManagerId](document-manager-id.md): [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-string/index.html)<br>The identifier of the DocumentManager that manages this document |
| [format](format.md)                            | [androidJvm]<br>abstract val [format](format.md): [DocumentFormat](../../eu.europa.ec.eudi.wallet.document.format/-document-format/index.md)<br>The format specification of the document (e.g., MsoMdoc, SdJwtVc)                            |
| [id](id.md)                                    | [androidJvm]<br>abstract val [id](id.md): [DocumentId](../-document-id/index.md)<br>The unique identifier of the document                                                                                                                    |
| [isCertified](is-certified.md)                 | [androidJvm]<br>abstract val [~~isCertified~~](is-certified.md): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-boolean/index.html)                                                                            |
| [isKeyInvalidated](is-key-invalidated.md)      | [androidJvm]<br>abstract val [~~isKeyInvalidated~~](is-key-invalidated.md): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-boolean/index.html)                                                                 |
| [issuerMetadata](issuer-metadata.md)           | [androidJvm]<br>abstract val [issuerMetadata](issuer-metadata.md): [IssuerMetadata](../../eu.europa.ec.eudi.wallet.document.metadata/-issuer-metadata/index.md)?<br>The document metadata provided by the issuer, if available               |
| [keyAlias](key-alias.md)                       | [androidJvm]<br>abstract val [~~keyAlias~~](key-alias.md): [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-string/index.html)                                                                                    |
| [keyInfo](key-info.md)                         | [androidJvm]<br>abstract val [~~keyInfo~~](key-info.md): KeyInfo                                                                                                                                                                             |
| [name](name.md)                                | [androidJvm]<br>abstract val [name](name.md): [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-string/index.html)<br>The human-readable name of the document                                                      |
| [publicKeyCoseBytes](public-key-cose-bytes.md) | [androidJvm]<br>abstract val [~~publicKeyCoseBytes~~](public-key-cose-bytes.md): [ByteArray](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-byte-array/index.html)                                                       |
| [secureArea](secure-area.md)                   | [androidJvm]<br>abstract val [~~secureArea~~](secure-area.md): SecureArea                                                                                                                                                                    |

## Functions

| Name                                     | Summary                                                                                                                                                                                                                                             |
|------------------------------------------|-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| [credentialsCount](credentials-count.md) | [androidJvm]<br>abstract suspend fun [credentialsCount](credentials-count.md)(): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html)<br>Returns the number of valid credentials associated with this document. |
