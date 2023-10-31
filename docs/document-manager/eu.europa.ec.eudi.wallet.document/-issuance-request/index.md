//[document-manager](../../../index.md)/[eu.europa.ec.eudi.wallet.document](../index.md)/[IssuanceRequest](index.md)

# IssuanceRequest

[androidJvm]\
data class [IssuanceRequest](index.md)(val documentId: [DocumentId](../index.md#659369697%2FClasslikes%2F1351694608), val docType: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html), var name: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html), val hardwareBacked: [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html), val requiresUserAuth: [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html), val certificateNeedAuth: [X509Certificate](https://developer.android.com/reference/kotlin/java/security/cert/X509Certificate.html))

Issuance request data class. Contains the necessary information to issue a document. Use the DocumentManager::createIssuanceRequest method to create an issuance request.

## Constructors

| | |
|---|---|
| [IssuanceRequest](-issuance-request.md) | [androidJvm]<br>constructor(documentId: [DocumentId](../index.md#659369697%2FClasslikes%2F1351694608), docType: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html), name: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html), hardwareBacked: [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html), requiresUserAuth: [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html), certificateNeedAuth: [X509Certificate](https://developer.android.com/reference/kotlin/java/security/cert/X509Certificate.html)) |

## Properties

| Name | Summary |
|---|---|
| [certificateNeedAuth](certificate-need-auth.md) | [androidJvm]<br>val [certificateNeedAuth](certificate-need-auth.md): [X509Certificate](https://developer.android.com/reference/kotlin/java/security/cert/X509Certificate.html) |
| [docType](doc-type.md) | [androidJvm]<br>val [docType](doc-type.md): [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html) |
| [documentId](document-id.md) | [androidJvm]<br>val [documentId](document-id.md): [DocumentId](../index.md#659369697%2FClasslikes%2F1351694608) |
| [hardwareBacked](hardware-backed.md) | [androidJvm]<br>val [hardwareBacked](hardware-backed.md): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) |
| [name](name.md) | [androidJvm]<br>var [name](name.md): [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html) |
| [requiresUserAuth](requires-user-auth.md) | [androidJvm]<br>val [requiresUserAuth](requires-user-auth.md): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) |
