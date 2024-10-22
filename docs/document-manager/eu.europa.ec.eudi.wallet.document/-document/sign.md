//[document-manager](../../../index.md)/[eu.europa.ec.eudi.wallet.document](../index.md)/[Document](index.md)/[sign](sign.md)

# sign

[androidJvm]\
open fun [sign](sign.md)(
dataToSign: [ByteArray](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-byte-array/index.html),
algorithm: Algorithm = Algorithm.ES256, keyUnlockData: KeyUnlockData? =
null): [Outcome](../-outcome/index.md)&lt;EcSignature&gt;

Sign the data with the document key

If the key is locked, the key unlock data must be provided to unlock the key before signing the
data. Otherwise, the method will return SignResult.KeyLocked.

#### Return

the sign result containing the signature or the failure

#### Parameters

androidJvm

|               |                                              |
|---------------|----------------------------------------------|
| dataToSign    | the data to sign                             |
| algorithm     | the algorithm to use for signing             |
| keyUnlockData | the key unlock data needed to unlock the key |
