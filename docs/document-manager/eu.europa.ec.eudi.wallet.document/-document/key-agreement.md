//[document-manager](../../../index.md)/[eu.europa.ec.eudi.wallet.document](../index.md)/[Document](index.md)/[keyAgreement](key-agreement.md)

# keyAgreement

[androidJvm]\
open fun [keyAgreement](key-agreement.md)(otherPublicKey: [ByteArray](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-byte-array/index.html), keyUnlockData: KeyUnlockData? = null): [Outcome](../-outcome/index.md)&lt;[SharedSecret](../-shared-secret/index.md)&gt;

Creates a shared secret given the other party's public key

If the key is locked, the key unlock data must be provided to unlock the key before creating the shared secret. Otherwise, the method will return [Outcome](../-outcome/index.md) with the [SharedSecret](../-shared-secret/index.md).

#### Return

the shared secret result containing the shared secret or the failure

#### Parameters

androidJvm

| | |
|---|---|
| otherPublicKey | the other party's public key |
| keyUnlockData | the key unlock data needed to unlock the key |
