//[document-manager](../../../index.md)/[eu.europa.ec.eudi.wallet.document](../index.md)/[IssuanceRequest](index.md)

# IssuanceRequest

[androidJvm]\
interface [IssuanceRequest](index.md)

Issuance request class. Contains the necessary information to issue a document. Use the DocumentManager::createIssuanceRequest method to create an issuance request.

## Types

| Name | Summary |
|---|---|
| [Companion](-companion/index.md) | [androidJvm]<br>object [Companion](-companion/index.md) |

## Functions

| Name | Summary |
|---|---|
| [signWithAuthKey](sign-with-auth-key.md) | [androidJvm]<br>abstract fun [signWithAuthKey](sign-with-auth-key.md)(data: [ByteArray](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-byte-array/index.html), alg: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html) = Algorithm.SHA256withECDSA): [SignedWithAuthKeyResult](../-signed-with-auth-key-result/index.md)<br>Sign given data with authentication key |

## Properties

| Name | Summary |
|---|---|
| [certificatesNeedAuth](certificates-need-auth.md) | [androidJvm]<br>abstract val [certificatesNeedAuth](certificates-need-auth.md): [List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)&lt;[X509Certificate](https://developer.android.com/reference/kotlin/java/security/cert/X509Certificate.html)&gt;<br>list of certificates that will be used for issuing the document |
| [docType](doc-type.md) | [androidJvm]<br>abstract val [docType](doc-type.md): [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)<br>document's docType (example: &quot;eu.europa.ec.eudiw.pid.1&quot;) |
| [documentId](document-id.md) | [androidJvm]<br>abstract val [documentId](document-id.md): [DocumentId](../index.md#659369697%2FClasslikes%2F1351694608)<br>document's unique identifier |
| [hardwareBacked](hardware-backed.md) | [androidJvm]<br>abstract val [hardwareBacked](hardware-backed.md): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)<br>whether the document's keys should be stored in hardware backed storage |
| [name](name.md) | [androidJvm]<br>abstract var [name](name.md): [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)<br>document's name |
| [publicKey](public-key.md) | [androidJvm]<br>open val [publicKey](public-key.md): [PublicKey](https://developer.android.com/reference/kotlin/java/security/PublicKey.html)<br>Public key of the first certificate in [certificatesNeedAuth](certificates-need-auth.md) list to be included in mobile security object that it will be signed from issuer |
| [requiresUserAuth](requires-user-auth.md) | [androidJvm]<br>abstract val [requiresUserAuth](requires-user-auth.md): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)<br>whether the document requires user authentication to be accessed |
