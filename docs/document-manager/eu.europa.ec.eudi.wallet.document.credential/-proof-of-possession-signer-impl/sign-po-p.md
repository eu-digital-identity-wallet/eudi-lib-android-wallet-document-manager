//[document-manager](../../../index.md)/[eu.europa.ec.eudi.wallet.document.credential](../index.md)/[ProofOfPossessionSignerImpl](index.md)/[signPoP](sign-po-p.md)

# signPoP

[androidJvm]\
open suspend override fun [signPoP](sign-po-p.md)(dataToSign: [ByteArray](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-byte-array/index.html), keyUnlockData: KeyUnlockData?): EcSignature

Creates a Proof of Possession signature by delegating to the secure area associated with the credential.

#### Return

An EcSignature containing the signature data.

#### Parameters

androidJvm

| | |
|---|---|
| dataToSign | The data bytes to be signed. |
| keyUnlockData | Optional data required to unlock the key for signing operations. |
