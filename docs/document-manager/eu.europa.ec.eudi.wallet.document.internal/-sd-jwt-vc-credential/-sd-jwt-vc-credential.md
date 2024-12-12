//[document-manager](../../../index.md)/[eu.europa.ec.eudi.wallet.document.internal](../index.md)/[SdJwtVcCredential](index.md)/[SdJwtVcCredential](-sd-jwt-vc-credential.md)

# SdJwtVcCredential

[androidJvm]\
constructor(document: Document, asReplacementFor: Credential?,
domain: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html),
secureArea: SecureArea, createKeySettings: CreateKeySettings,
vct: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html))

Constructs a new [SdJwtVcCredential](index.md).

#### Parameters

androidJvm

|                   |                                                                             |
|-------------------|-----------------------------------------------------------------------------|
| document          | the document to add the credential to.                                      |
| asReplacementFor  | the credential this credential will replace, if not null                    |
| domain            | the domain of the credential                                                |
| secureArea        | the secure area for the authentication key associated with this credential. |
| createKeySettings | the settings used to create new credentials.                                |
| vct               | the Verifiable Credential Type.                                             |

[androidJvm]\
constructor(document: Document, dataItem: DataItem)

Constructs a Credential from serialized data.

#### Parameters

androidJvm

|          |                                              |
|----------|----------------------------------------------|
| document | the Document that the credential belongs to. |
| dataItem | the serialized data.                         |
