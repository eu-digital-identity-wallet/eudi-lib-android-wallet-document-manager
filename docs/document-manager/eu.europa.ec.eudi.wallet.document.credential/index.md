//[document-manager](../../index.md)/[eu.europa.ec.eudi.wallet.document.credential](index.md)

# Package-level declarations

## Types

| Name | Summary |
|---|---|
| [CredentialCertification](-credential-certification/index.md) | [androidJvm]<br>interface [CredentialCertification](-credential-certification/index.md) |
| [CredentialFactory](-credential-factory/index.md) | [androidJvm]<br>interface [CredentialFactory](-credential-factory/index.md) |
| [IssuerProvidedCredential](-issuer-provided-credential/index.md) | [androidJvm]<br>data class [IssuerProvidedCredential](-issuer-provided-credential/index.md)(val publicKeyAlias: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-string/index.html), val data: [ByteArray](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-byte-array/index.html)) |
| [MdocCredentialFactory](-mdoc-credential-factory/index.md) | [androidJvm]<br>class [MdocCredentialFactory](-mdoc-credential-factory/index.md)(val domain: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-string/index.html)) : [CredentialFactory](-credential-factory/index.md) |
| [MsoMdocCredentialCertifier](-mso-mdoc-credential-certifier/index.md) | [androidJvm]<br>class [MsoMdocCredentialCertifier](-mso-mdoc-credential-certifier/index.md) : [CredentialCertification](-credential-certification/index.md) |
| [ProofOfPossessionSigner](-proof-of-possession-signer/index.md) | [androidJvm]<br>interface [ProofOfPossessionSigner](-proof-of-possession-signer/index.md)<br>Interface for creating Proof of Possession (PoP) signatures. |
| [ProofOfPossessionSignerImpl](-proof-of-possession-signer-impl/index.md) | [androidJvm]<br>class [ProofOfPossessionSignerImpl](-proof-of-possession-signer-impl/index.md)(credential: SecureAreaBoundCredential) : [ProofOfPossessionSigner](-proof-of-possession-signer/index.md)<br>Default implementation of [ProofOfPossessionSigner](-proof-of-possession-signer/index.md) that uses a SecureAreaBoundCredential. |
| [ProofOfPossessionSigners](-proof-of-possession-signers/index.md) | [androidJvm]<br>class [ProofOfPossessionSigners](-proof-of-possession-signers/index.md)(list: [List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin.collections/-list/index.html)&lt;[ProofOfPossessionSigner](-proof-of-possession-signer/index.md)&gt;) : [Collection](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin.collections/-collection/index.html)&lt;[ProofOfPossessionSigner](-proof-of-possession-signer/index.md)&gt; <br>A collection of [ProofOfPossessionSigner](-proof-of-possession-signer/index.md) instances. |
| [SdJwtVcCredential](-sd-jwt-vc-credential/index.md) | [androidJvm]<br>class [SdJwtVcCredential](-sd-jwt-vc-credential/index.md) : SecureAreaBoundCredential, SdJwtVcCredential<br>A SD-JWT VC credential, according to draft-ietf-oauth-sd-jwt-vc-03 (https://datatracker.ietf.org/doc/draft-ietf-oauth-sd-jwt-vc/). |
| [SdJwtVcCredentialCertifier](-sd-jwt-vc-credential-certifier/index.md) | [androidJvm]<br>class [SdJwtVcCredentialCertifier](-sd-jwt-vc-credential-certifier/index.md)(var ktorHttpClientFactory: KtorHttpClientFactory = { HttpClient() }) : [CredentialCertification](-credential-certification/index.md) |
| [SdJwtVcCredentialFactory](-sd-jwt-vc-credential-factory/index.md) | [androidJvm]<br>class [SdJwtVcCredentialFactory](-sd-jwt-vc-credential-factory/index.md)(val domain: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-string/index.html)) : [CredentialFactory](-credential-factory/index.md) |
