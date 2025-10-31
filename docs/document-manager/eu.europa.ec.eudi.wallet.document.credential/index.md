//[document-manager](../../index.md)/[eu.europa.ec.eudi.wallet.document.credential](index.md)

# Package-level declarations

## Types

| Name | Summary |
|---|---|
| [CredentialCertification](-credential-certification/index.md) | [androidJvm]<br>fun interface [CredentialCertification](-credential-certification/index.md) |
| [CredentialFactory](-credential-factory/index.md) | [androidJvm]<br>fun interface [CredentialFactory](-credential-factory/index.md)<br>Factory interface for creating credentials based on document format. Provides functionality to create appropriate credential types for different document formats. |
| [CredentialIssuedData](-credential-issued-data/index.md) | [androidJvm]<br>sealed interface [CredentialIssuedData](-credential-issued-data/index.md)<br>Sealed interface representing different types of credential data provided by issuers. This interface acts as a common type for various credential formats used within the EUDI Wallet. |
| [IssuerProvidedCredential](-issuer-provided-credential/index.md) | [androidJvm]<br>data class [IssuerProvidedCredential](-issuer-provided-credential/index.md)(val publicKeyAlias: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-string/index.html), val data: [ByteArray](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-byte-array/index.html)) |
| [MdocCredentialFactory](-mdoc-credential-factory/index.md) | [androidJvm]<br>class [MdocCredentialFactory](-mdoc-credential-factory/index.md)(val domain: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-string/index.html)) : [CredentialFactory](-credential-factory/index.md)<br>Implementation of CredentialFactory for creating ISO/IEC 18013-5 mobile driving license (mDL) credentials. |
| [MsoMdocCredentialCertifier](-mso-mdoc-credential-certifier/index.md) | [androidJvm]<br>class [MsoMdocCredentialCertifier](-mso-mdoc-credential-certifier/index.md) : [CredentialCertification](-credential-certification/index.md) |
| [ProofOfPossessionSigner](-proof-of-possession-signer/index.md) | [androidJvm]<br>interface [ProofOfPossessionSigner](-proof-of-possession-signer/index.md)<br>Interface for creating Proof of Possession (PoP) signatures. |
| [ProofOfPossessionSignerImpl](-proof-of-possession-signer-impl/index.md) | [androidJvm]<br>class [ProofOfPossessionSignerImpl](-proof-of-possession-signer-impl/index.md)(credential: SecureAreaBoundCredential) : [ProofOfPossessionSigner](-proof-of-possession-signer/index.md)<br>Default implementation of [ProofOfPossessionSigner](-proof-of-possession-signer/index.md) that uses a SecureAreaBoundCredential. |
| [ProofOfPossessionSigners](-proof-of-possession-signers/index.md) | [androidJvm]<br>class [ProofOfPossessionSigners](-proof-of-possession-signers/index.md)(list: [List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin.collections/-list/index.html)&lt;[ProofOfPossessionSigner](-proof-of-possession-signer/index.md)&gt;) : [Collection](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin.collections/-collection/index.html)&lt;[ProofOfPossessionSigner](-proof-of-possession-signer/index.md)&gt; <br>A collection of [ProofOfPossessionSigner](-proof-of-possession-signer/index.md) instances. |
| [SdJwtVcCredentialCertifier](-sd-jwt-vc-credential-certifier/index.md) | [androidJvm]<br>class [SdJwtVcCredentialCertifier](-sd-jwt-vc-credential-certifier/index.md)(var ktorHttpClientFactory: KtorHttpClientFactory = { HttpClient() }) : [CredentialCertification](-credential-certification/index.md) |
| [SdJwtVcCredentialFactory](-sd-jwt-vc-credential-factory/index.md) | [androidJvm]<br>class [SdJwtVcCredentialFactory](-sd-jwt-vc-credential-factory/index.md)(val domain: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-string/index.html)) : [CredentialFactory](-credential-factory/index.md)<br>Implementation of CredentialFactory for creating SD-JWT VC (Selective Disclosure JWT Verifiable Credentials) according to RFC 9401. |

## Functions

| Name | Summary |
|---|---|
| [fromIssuerProvidedData](from-issuer-provided-data.md) | [androidJvm]<br>fun NameSpacedData.Companion.[fromIssuerProvidedData](from-issuer-provided-data.md)(issuerProvidedData: [ByteArray](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-byte-array/index.html)): NameSpacedData<br>Creates a NameSpacedData object from raw issuer provided data in CBOR format. |
| [getIssuedData](get-issued-data.md) | [androidJvm]<br>inline fun &lt;[D](get-issued-data.md) : [CredentialIssuedData](-credential-issued-data/index.md)&gt; SecureAreaBoundCredential.[getIssuedData](get-issued-data.md)(): [Result](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-result/index.html)&lt;[D](get-issued-data.md)&gt;<br>Extension function to extract issuer provided data from a SecureAreaBoundCredential. |
