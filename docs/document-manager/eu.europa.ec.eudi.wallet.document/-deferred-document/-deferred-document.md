//[document-manager](../../../index.md)/[eu.europa.ec.eudi.wallet.document](../index.md)/[DeferredDocument](index.md)/[DeferredDocument](-deferred-document.md)

# DeferredDocument

[androidJvm]\
constructor(id: [DocumentId](../-document-id/index.md),
name: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html),
docType: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html),
usesStrongBox: [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html),
requiresUserAuth: [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html),
createdAt: [Instant](https://developer.android.com/reference/kotlin/java/time/Instant.html),
certificatesNeedAuth: [List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)
&lt;[X509Certificate](https://developer.android.com/reference/kotlin/java/security/cert/X509Certificate.html)
&gt;, keyUnlockDataFactory: [KeyUnlockDataFactory](../-key-unlock-data-factory/index.md),
relatedData: [ByteArray](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-byte-array/index.html))
