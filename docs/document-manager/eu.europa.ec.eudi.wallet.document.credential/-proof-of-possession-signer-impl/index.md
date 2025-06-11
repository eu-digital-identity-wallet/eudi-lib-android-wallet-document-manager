//[document-manager](../../../index.md)/[eu.europa.ec.eudi.wallet.document.credential](../index.md)/[ProofOfPossessionSignerImpl](index.md)

# ProofOfPossessionSignerImpl

[androidJvm]\
class [ProofOfPossessionSignerImpl](index.md)(credential: SecureAreaBoundCredential) : [ProofOfPossessionSigner](../-proof-of-possession-signer/index.md)

Default implementation of [ProofOfPossessionSigner](../-proof-of-possession-signer/index.md) that uses a SecureAreaBoundCredential.

This implementation delegates signing operations to the secure area associated with the credential.

## Constructors

| | |
|---|---|
| [ProofOfPossessionSignerImpl](-proof-of-possession-signer-impl.md) | [androidJvm]<br>constructor(credential: SecureAreaBoundCredential) |

## Properties

| Name                         | Summary                                                                                                                                                                                              |
|------------------------------|------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| [keyAlias](key-alias.md)     | [androidJvm]<br>open override val [keyAlias](key-alias.md): [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-string/index.html)<br>The alias of the key used for signing. |
| [secureArea](secure-area.md) | [androidJvm]<br>open override val [secureArea](secure-area.md): SecureArea<br>The secure area associated with the key.                                                                               |

## Functions

| Name                          | Summary                                                                                                                                                                                                                                                                                                                                      |
|-------------------------------|----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| [getKeyInfo](get-key-info.md) | [androidJvm]<br>open suspend override fun [getKeyInfo](get-key-info.md)(): KeyInfo<br>The alias of the key used for signing.                                                                                                                                                                                                                 |
| [signPoP](sign-po-p.md)       | [androidJvm]<br>open suspend override fun [signPoP](sign-po-p.md)(dataToSign: [ByteArray](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-byte-array/index.html), keyUnlockData: KeyUnlockData?): EcSignature<br>Creates a Proof of Possession signature by delegating to the secure area associated with the credential. |
