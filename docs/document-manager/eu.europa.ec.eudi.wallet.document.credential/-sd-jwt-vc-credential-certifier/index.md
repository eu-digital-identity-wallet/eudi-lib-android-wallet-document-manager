//[document-manager](../../../index.md)/[eu.europa.ec.eudi.wallet.document.credential](../index.md)/[SdJwtVcCredentialCertifier](index.md)

# SdJwtVcCredentialCertifier

[androidJvm]\
class [SdJwtVcCredentialCertifier](index.md)(var ktorHttpClientFactory: KtorHttpClientFactory = { HttpClient() }) : [CredentialCertification](../-credential-certification/index.md)

## Constructors

| | |
|---|---|
| [SdJwtVcCredentialCertifier](-sd-jwt-vc-credential-certifier.md) | [androidJvm]<br>constructor(ktorHttpClientFactory: KtorHttpClientFactory = { HttpClient() }) |

## Properties

| Name | Summary |
|---|---|
| [ktorHttpClientFactory](ktor-http-client-factory.md) | [androidJvm]<br>var [ktorHttpClientFactory](ktor-http-client-factory.md): KtorHttpClientFactory |

## Functions

| Name                                       | Summary                                                                                                                                                                                                                                                                                                                             |
|--------------------------------------------|-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| [certifyCredential](certify-credential.md) | [androidJvm]<br>open suspend override fun [certifyCredential](certify-credential.md)(credential: SecureAreaBoundCredential, issuedCredential: [IssuerProvidedCredential](../-issuer-provided-credential/index.md), forceKeyCheck: [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-boolean/index.html)) |
