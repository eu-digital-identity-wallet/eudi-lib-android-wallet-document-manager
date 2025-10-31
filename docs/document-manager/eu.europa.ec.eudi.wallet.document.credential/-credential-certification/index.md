//[document-manager](../../../index.md)/[eu.europa.ec.eudi.wallet.document.credential](../index.md)/[CredentialCertification](index.md)

# CredentialCertification

fun interface [CredentialCertification](index.md)

#### Inheritors

| |
|---|
| [MsoMdocCredentialCertifier](../-mso-mdoc-credential-certifier/index.md) |
| [SdJwtVcCredentialCertifier](../-sd-jwt-vc-credential-certifier/index.md) |

## Types

| Name | Summary |
|---|---|
| [Companion](-companion/index.md) | [androidJvm]<br>object [Companion](-companion/index.md) |

## Functions

| Name | Summary |
|---|---|
| [certifyCredential](certify-credential.md) | [androidJvm]<br>abstract suspend fun [certifyCredential](certify-credential.md)(credential: SecureAreaBoundCredential, issuedCredential: [IssuerProvidedCredential](../-issuer-provided-credential/index.md), forceKeyCheck: [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-boolean/index.html)) |
