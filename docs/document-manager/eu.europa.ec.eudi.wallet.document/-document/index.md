//[document-manager](../../../index.md)/[eu.europa.ec.eudi.wallet.document](../index.md)/[Document](index.md)

# Document

interface [Document](index.md)

A document.

#### Inheritors

|                                                    |
|----------------------------------------------------|
| [DeferredDocument](../-deferred-document/index.md) |
| [IssuedDocument](../-issued-document/index.md)     |
| [UnsignedDocument](../-unsigned-document/index.md) |

## Types

| Name                             | Summary                                                                                                                                                                                              |
|----------------------------------|------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| [Companion](-companion/index.md) | [androidJvm]<br>object [Companion](-companion/index.md)                                                                                                                                              |
| [State](-state/index.md)         | [androidJvm]<br>enum [State](-state/index.md) : [Enum](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-enum/index.html)&lt;[Document.State](-state/index.md)&gt; <br>The state of the document. |

## Properties

| Name                                      | Summary                                                                                                                                                                                                         |
|-------------------------------------------|-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| [createdAt](created-at.md)                | [androidJvm]<br>abstract val [createdAt](created-at.md): [Instant](https://developer.android.com/reference/kotlin/java/time/Instant.html)<br>the creation date of the document                                  |
| [docType](doc-type.md)                    | [androidJvm]<br>abstract val [docType](doc-type.md): [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)<br>the document type                                                      |
| [id](id.md)                               | [androidJvm]<br>abstract val [id](id.md): [DocumentId](../index.md#659369697%2FClasslikes%2F1351694608)<br>the identifier of the document                                                                       |
| [isDeferred](is-deferred.md)              | [androidJvm]<br>open val [isDeferred](is-deferred.md): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)<br>whether the document is deferred                                   |
| [isIssued](is-issued.md)                  | [androidJvm]<br>open val [isIssued](is-issued.md): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)<br>whether the document is issued                                         |
| [isUnsigned](is-unsigned.md)              | [androidJvm]<br>open val [isUnsigned](is-unsigned.md): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)<br>whether the document is unsigned                                   |
| [name](name.md)                           | [androidJvm]<br>abstract val [name](name.md): [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)<br>the name of the document                                                      |
| [requiresUserAuth](requires-user-auth.md) | [androidJvm]<br>abstract val [requiresUserAuth](requires-user-auth.md): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)<br>whether the document requires user authentication |
| [state](state.md)                         | [androidJvm]<br>abstract val [state](state.md): [Document.State](-state/index.md)<br>the state of the document                                                                                                  |
| [usesStrongBox](uses-strong-box.md)       | [androidJvm]<br>abstract val [usesStrongBox](uses-strong-box.md): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)<br>whether the document's keys are in strongBox            |
