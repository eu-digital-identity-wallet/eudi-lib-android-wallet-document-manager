//[document-manager](../../../../index.md)/[eu.europa.ec.eudi.wallet.document.credential](../../index.md)/[CredentialIssuedData](../index.md)/[SdJwtVc](index.md)

# SdJwtVc

[androidJvm]\
data class [SdJwtVc](index.md)(val issuedSdJwt: SdJwt&lt;JwtAndClaims&gt;) : [CredentialIssuedData](../index.md)

Represents a Selective Disclosure JWT Verifiable Credential format.

## Constructors

| | |
|---|---|
| [SdJwtVc](-sd-jwt-vc.md) | [androidJvm]<br>constructor(issuedSdJwt: SdJwt&lt;JwtAndClaims&gt;) |

## Properties

| Name | Summary |
|---|---|
| [issuedSdJwt](issued-sd-jwt.md) | [androidJvm]<br>val [issuedSdJwt](issued-sd-jwt.md): SdJwt&lt;JwtAndClaims&gt;<br>The SD-JWT with claims as issued by the credential provider |
