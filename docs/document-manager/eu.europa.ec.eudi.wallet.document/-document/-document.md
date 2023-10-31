//[document-manager](../../../index.md)/[eu.europa.ec.eudi.wallet.document](../index.md)/[Document](index.md)/[Document](-document.md)

# Document

[androidJvm]\
constructor(id: [DocumentId](../index.md#659369697%2FClasslikes%2F1351694608), docType: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html), name: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html), hardwareBacked: [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html), createdAt: [Instant](https://developer.android.com/reference/kotlin/java/time/Instant.html), requiresUserAuth: [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html), nameSpacedData: [Map](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-map/index.html)&lt;[NameSpace](../index.md#1862659344%2FClasslikes%2F1351694608), [Map](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-map/index.html)&lt;[ElementIdentifier](../index.md#-190936378%2FClasslikes%2F1351694608), [ByteArray](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-byte-array/index.html)&gt;&gt;)

Creates a document

#### Parameters

androidJvm

| | |
|---|---|
| id | document's unique identifier |
| docType | document's docType (example: &quot;eu.europa.ec.eudiw.pid.1&quot;) |
| name | document's name. This is a human readable name. |
| hardwareBacked | document's storage is hardware backed |
| createdAt | document's creation date |
| requiresUserAuth | flag that indicates if the document requires user authentication to be accessed |
| nameSpacedData | retrieves the document's data, grouped by nameSpace. Values are in CBOR bytes |
