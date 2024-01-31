//[document-manager](../../index.md)/[eu.europa.ec.eudi.wallet.document](index.md)

# Package-level declarations

## Types

| Name | Summary |
|---|---|
| [AddDocumentResult](-add-document-result/index.md) | [androidJvm]<br>interface [AddDocumentResult](-add-document-result/index.md)<br>Add document result sealed interface |
| [Algorithm](-algorithm/index.md) | [androidJvm]<br>annotation class [Algorithm](-algorithm/index.md) |
| [CreateIssuanceRequestResult](-create-issuance-request-result/index.md) | [androidJvm]<br>interface [CreateIssuanceRequestResult](-create-issuance-request-result/index.md)<br>Create issuance request result sealed interface |
| [DeleteDocumentResult](-delete-document-result/index.md) | [androidJvm]<br>interface [DeleteDocumentResult](-delete-document-result/index.md)<br>Delete document result sealed interface |
| [Document](-document/index.md) | [androidJvm]<br>data class [Document](-document/index.md)(val id: [DocumentId](index.md#659369697%2FClasslikes%2F1351694608), val docType: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html), val name: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html), val hardwareBacked: [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html), val createdAt: [Instant](https://developer.android.com/reference/kotlin/java/time/Instant.html), val requiresUserAuth: [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html), val nameSpacedData: [Map](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-map/index.html)&lt;[NameSpace](index.md#1862659344%2FClasslikes%2F1351694608), [Map](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-map/index.html)&lt;[ElementIdentifier](index.md#-190936378%2FClasslikes%2F1351694608), [ByteArray](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-byte-array/index.html)&gt;&gt;)<br>Data class that represents a document. |
| [DocumentId](index.md#659369697%2FClasslikes%2F1351694608) | [androidJvm]<br>typealias [DocumentId](index.md#659369697%2FClasslikes%2F1351694608) = [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html) |
| [DocumentManager](-document-manager/index.md) | [androidJvm]<br>interface [DocumentManager](-document-manager/index.md)<br>Document manager object is the entry point to access documents. |
| [DocumentManagerImpl](-document-manager-impl/index.md) | [androidJvm]<br>class [DocumentManagerImpl](-document-manager-impl/index.md)(context: [Context](https://developer.android.com/reference/kotlin/android/content/Context.html), storageEngine: StorageEngine, secureArea: AndroidKeystoreSecureArea) : [DocumentManager](-document-manager/index.md)<br>A [DocumentManager](-document-manager/index.md) implementation that uses StorageEngine to store documents and AndroidKeystoreSecureArea for key management. |
| [ElementIdentifier](index.md#-190936378%2FClasslikes%2F1351694608) | [androidJvm]<br>typealias [ElementIdentifier](index.md#-190936378%2FClasslikes%2F1351694608) = [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html) |
| [IssuanceRequest](-issuance-request/index.md) | [androidJvm]<br>interface [IssuanceRequest](-issuance-request/index.md)<br>Issuance request class. Contains the necessary information to issue a document. Use the DocumentManager::createIssuanceRequest method to create an issuance request. |
| [NameSpace](index.md#1862659344%2FClasslikes%2F1351694608) | [androidJvm]<br>typealias [NameSpace](index.md#1862659344%2FClasslikes%2F1351694608) = [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html) |
| [SignedWithAuthKeyResult](-signed-with-auth-key-result/index.md) | [androidJvm]<br>interface [SignedWithAuthKeyResult](-signed-with-auth-key-result/index.md) |

## Properties

| Name | Summary |
|---|---|
| [nameSpacedDataJSONObject](name-spaced-data-j-s-o-n-object.md) | [androidJvm]<br>@get:[JvmName](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.jvm/-jvm-name/index.html)(name = &quot;nameSpacedDataAsJSONObject&quot;)<br>val [Document](-document/index.md).[nameSpacedDataJSONObject](name-spaced-data-j-s-o-n-object.md): [JSONObject](https://developer.android.com/reference/kotlin/org/json/JSONObject.html)<br>Extension function to convert [Document](-document/index.md)'s nameSpacedData to [JSONObject](https://developer.android.com/reference/kotlin/org/json/JSONObject.html) |
