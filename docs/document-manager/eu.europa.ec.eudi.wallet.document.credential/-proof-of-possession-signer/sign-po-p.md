//[document-manager](../../../index.md)/[eu.europa.ec.eudi.wallet.document.credential](../index.md)/[ProofOfPossessionSigner](index.md)/[signPoP](sign-po-p.md)

# signPoP

[androidJvm]\
abstract suspend fun [signPoP](sign-po-p.md)(dataToSign: [ByteArray](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-byte-array/index.html), keyUnlockData: KeyUnlockData?): EcSignature

Signs the provided data to create a Proof of Possession signature.

#### Return

An EcSignature containing the signature data.

#### Parameters

androidJvm

| | |
|---|---|
| dataToSign | The data bytes to be signed. |
| keyUnlockData | Optional data required to unlock the key for signing operations. |
