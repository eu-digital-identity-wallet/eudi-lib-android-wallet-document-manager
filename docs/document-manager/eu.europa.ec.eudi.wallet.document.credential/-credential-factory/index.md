//[document-manager](../../../index.md)/[eu.europa.ec.eudi.wallet.document.credential](../index.md)/[CredentialFactory](index.md)

# CredentialFactory

interface [CredentialFactory](index.md)

#### Inheritors

| |
|---|
| [MdocCredentialFactory](../-mdoc-credential-factory/index.md) |
| [SdJwtVcCredentialFactory](../-sd-jwt-vc-credential-factory/index.md) |

## Types

| Name | Summary |
|---|---|
| [Companion](-companion/index.md) | [androidJvm]<br>object [Companion](-companion/index.md) |

## Functions

| Name | Summary |
|---|---|
| [createCredentials](create-credentials.md) | [androidJvm]<br>abstract suspend fun [createCredentials](create-credentials.md)(format: [DocumentFormat](../../eu.europa.ec.eudi.wallet.document.format/-document-format/index.md), document: Document, createDocumentSettings: [CreateDocumentSettings](../../eu.europa.ec.eudi.wallet.document/-create-document-settings/index.md), secureArea: SecureArea): [List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin.collections/-list/index.html)&lt;SecureAreaBoundCredential&gt; |
