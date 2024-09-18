//[document-manager](../../../index.md)/[eu.europa.ec.eudi.wallet.document](../index.md)/[UnsignedDocument](index.md)

# UnsignedDocument

open class [UnsignedDocument](index.md)(val id: [DocumentId](../-document-id/index.md),
name: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html), val
docType: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html), val
usesStrongBox: [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html),
val
requiresUserAuth: [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html),
val createdAt: [Instant](https://developer.android.com/reference/kotlin/java/time/Instant.html), val
certificatesNeedAuth: [List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)
&lt;[X509Certificate](https://developer.android.com/reference/kotlin/java/security/cert/X509Certificate.html)
&gt;) : [Document](../-document/index.md)

A [UnsignedDocument](index.md) is a document that is in the process of being issued. It contains the
information required to issue the document and can be used to sign the proof of possession required
by the issuers using the [UnsignedDocument.signWithAuthKey](sign-with-auth-key.md) method.

Use the [DocumentManager.createDocument](../-document-manager/create-document.md) method to create
a [UnsignedDocument](index.md)

Once the document is issued and document's data are available by the issuer, use
the [DocumentManager.storeIssuedDocument](../-document-manager/store-issued-document.md) to store
the document. This will transform the [UnsignedDocument](index.md) to
an [IssuedDocument](../-issued-document/index.md)

#### Inheritors

|                                                    |
|----------------------------------------------------|
| [DeferredDocument](../-deferred-document/index.md) |

## Constructors

|                                           |                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                   |
|-------------------------------------------|---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| [UnsignedDocument](-unsigned-document.md) | [androidJvm]<br>constructor(id: [DocumentId](../-document-id/index.md), name: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html), docType: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html), usesStrongBox: [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html), requiresUserAuth: [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html), createdAt: [Instant](https://developer.android.com/reference/kotlin/java/time/Instant.html), certificatesNeedAuth: [List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)&lt;[X509Certificate](https://developer.android.com/reference/kotlin/java/security/cert/X509Certificate.html)&gt;) |

## Properties

| Name                                              | Summary                                                                                                                                                                                                                                                                                                                                             |
|---------------------------------------------------|-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| [certificatesNeedAuth](certificates-need-auth.md) | [androidJvm]<br>val [certificatesNeedAuth](certificates-need-auth.md): [List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)&lt;[X509Certificate](https://developer.android.com/reference/kotlin/java/security/cert/X509Certificate.html)&gt;<br>list of certificates that will be used for issuing the document |
| [createdAt](created-at.md)                        | [androidJvm]<br>open override val [createdAt](created-at.md): [Instant](https://developer.android.com/reference/kotlin/java/time/Instant.html)<br>the creation date of the document                                                                                                                                                                 |
| [docType](doc-type.md)                            | [androidJvm]<br>override val [docType](doc-type.md): [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)<br>the document type                                                                                                                                                                                          |
| [id](id.md)                                       | [androidJvm]<br>open override val [id](id.md): [DocumentId](../-document-id/index.md)<br>the identifier of the document                                                                                                                                                                                                                             |
| [isDeferred](../-document/is-deferred.md)         | [androidJvm]<br>open val [isDeferred](../-document/is-deferred.md): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)<br>whether the document is deferred                                                                                                                                                          |
| [isIssued](../-document/is-issued.md)             | [androidJvm]<br>open val [isIssued](../-document/is-issued.md): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)<br>whether the document is issued                                                                                                                                                                |
| [isUnsigned](../-document/is-unsigned.md)         | [androidJvm]<br>open val [isUnsigned](../-document/is-unsigned.md): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)<br>whether the document is unsigned                                                                                                                                                          |
| [name](name.md)                                   | [androidJvm]<br>open override var [name](name.md): [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)<br>the name of the document. This name can be updated before the document is issued                                                                                                                             |
| [publicKey](public-key.md)                        | [androidJvm]<br>val [publicKey](public-key.md): [PublicKey](https://developer.android.com/reference/kotlin/java/security/PublicKey.html)<br>public key of the first certificate in [certificatesNeedAuth](certificates-need-auth.md) list to be included in mobile security object that it will be signed from issuer                               |
| [requiresUserAuth](requires-user-auth.md)         | [androidJvm]<br>open override val [requiresUserAuth](requires-user-auth.md): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)<br>whether the document requires user authentication                                                                                                                                |
| [state](state.md)                                 | [androidJvm]<br>open override val [state](state.md): [Document.State](../-document/-state/index.md)<br>the state of the document                                                                                                                                                                                                                    |
| [usesStrongBox](uses-strong-box.md)               | [androidJvm]<br>open override val [usesStrongBox](uses-strong-box.md): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)<br>whether the document's keys are in strongBox                                                                                                                                           |

## Functions

| Name                                     | Summary                                                                                                                                                                                                                                                                                                                                                                                        |
|------------------------------------------|------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| [signWithAuthKey](sign-with-auth-key.md) | [androidJvm]<br>fun [signWithAuthKey](sign-with-auth-key.md)(data: [ByteArray](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-byte-array/index.html), alg: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html) = Algorithm.SHA256withECDSA): [SignedWithAuthKeyResult](../-signed-with-auth-key-result/index.md)<br>Sign given data with authentication key |
| [toString](to-string.md)                 | [androidJvm]<br>open override fun [toString](to-string.md)(): [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)                                                                                                                                                                                                                                                 |
