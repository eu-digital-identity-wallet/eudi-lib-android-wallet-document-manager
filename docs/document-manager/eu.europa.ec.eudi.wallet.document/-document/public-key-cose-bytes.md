//[document-manager](../../../index.md)/[eu.europa.ec.eudi.wallet.document](../index.md)/[Document](index.md)/[publicKeyCoseBytes](public-key-cose-bytes.md)

# publicKeyCoseBytes

[androidJvm]\
abstract val [~~publicKeyCoseBytes~~](public-key-cose-bytes.md): [ByteArray](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-byte-array/index.html)

---

### Deprecated

For UnsignedDocument, use getPoPSigners().first().getKeyInfo().publicKey.toCoseBytes. For IssuedDocument, use findCredential()?.secureArea.getKeyInfo().publicKey.toCoseBytes

---
