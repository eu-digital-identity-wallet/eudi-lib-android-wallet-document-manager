//[document-manager](../../../index.md)/[eu.europa.ec.eudi.wallet.document](../index.md)/[Document](index.md)

# Document

data class [Document](index.md)(val id: [DocumentId](../index.md#659369697%2FClasslikes%2F1351694608), val docType: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html), val name: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html), val hardwareBacked: [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html), val createdAt: [Instant](https://developer.android.com/reference/kotlin/java/time/Instant.html), val requiresUserAuth: [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html), val nameSpacedData: [Map](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-map/index.html)&lt;[NameSpace](../index.md#1862659344%2FClasslikes%2F1351694608), [Map](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-map/index.html)&lt;[ElementIdentifier](../index.md#-190936378%2FClasslikes%2F1351694608), [ByteArray](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-byte-array/index.html)&gt;&gt;)

Data class that represents a document.

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

## Constructors

| | |
|---|---|
| [Document](-document.md) | [androidJvm]<br>constructor(id: [DocumentId](../index.md#659369697%2FClasslikes%2F1351694608), docType: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html), name: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html), hardwareBacked: [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html), createdAt: [Instant](https://developer.android.com/reference/kotlin/java/time/Instant.html), requiresUserAuth: [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html), nameSpacedData: [Map](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-map/index.html)&lt;[NameSpace](../index.md#1862659344%2FClasslikes%2F1351694608), [Map](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-map/index.html)&lt;[ElementIdentifier](../index.md#-190936378%2FClasslikes%2F1351694608), [ByteArray](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-byte-array/index.html)&gt;&gt;)<br>Creates a document |

## Properties

| Name | Summary |
|---|---|
| [createdAt](created-at.md) | [androidJvm]<br>val [createdAt](created-at.md): [Instant](https://developer.android.com/reference/kotlin/java/time/Instant.html)<br>document's creation date |
| [docType](doc-type.md) | [androidJvm]<br>val [docType](doc-type.md): [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)<br>document's docType (example: &quot;eu.europa.ec.eudiw.pid.1&quot;) |
| [hardwareBacked](hardware-backed.md) | [androidJvm]<br>val [hardwareBacked](hardware-backed.md): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)<br>document's storage is hardware backed |
| [id](id.md) | [androidJvm]<br>val [id](id.md): [DocumentId](../index.md#659369697%2FClasslikes%2F1351694608)<br>document's unique identifier |
| [name](name.md) | [androidJvm]<br>val [name](name.md): [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)<br>document's name. This is a human readable name. |
| [nameSpacedData](name-spaced-data.md) | [androidJvm]<br>val [nameSpacedData](name-spaced-data.md): [Map](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-map/index.html)&lt;[NameSpace](../index.md#1862659344%2FClasslikes%2F1351694608), [Map](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-map/index.html)&lt;[ElementIdentifier](../index.md#-190936378%2FClasslikes%2F1351694608), [ByteArray](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-byte-array/index.html)&gt;&gt;<br>retrieves the document's data, grouped by nameSpace. Values are in CBOR bytes |
| [nameSpacedDataJSONObject](../name-spaced-data-j-s-o-n-object.md) | [androidJvm]<br>@get:[JvmName](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.jvm/-jvm-name/index.html)(name = &quot;nameSpacedDataAsJSONObject&quot;)<br>val [Document](index.md).[nameSpacedDataJSONObject](../name-spaced-data-j-s-o-n-object.md): [JSONObject](https://developer.android.com/reference/kotlin/org/json/JSONObject.html)<br>Extension function to convert [Document](index.md)'s nameSpacedData to [JSONObject](https://developer.android.com/reference/kotlin/org/json/JSONObject.html) |
| [nameSpaces](name-spaces.md) | [androidJvm]<br>val [nameSpaces](name-spaces.md): [Map](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-map/index.html)&lt;[NameSpace](../index.md#1862659344%2FClasslikes%2F1351694608), [List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)&lt;[ElementIdentifier](../index.md#-190936378%2FClasslikes%2F1351694608)&gt;&gt;<br>retrieves the document's nameSpaces and elementIdentifiers |
| [requiresUserAuth](requires-user-auth.md) | [androidJvm]<br>val [requiresUserAuth](requires-user-auth.md): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)<br>flag that indicates if the document requires user authentication to be accessed |
