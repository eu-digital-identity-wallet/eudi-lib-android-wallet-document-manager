//[document-manager](../../../index.md)/[eu.europa.ec.eudi.wallet.document](../index.md)/[Outcome](index.md)

# Outcome

[androidJvm]\
class [Outcome](index.md)&lt;out [T](index.md)&gt;

Outcome for encapsulating success or failure of a computation for document manager operations. Wraps
a [Result](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-result/index.html) instance to
provide Java interop.

## Types

| Name                             | Summary                                                 |
|----------------------------------|---------------------------------------------------------|
| [Companion](-companion/index.md) | [androidJvm]<br>object [Companion](-companion/index.md) |

## Properties

| Name                       | Summary                                                                                                                                                                                                                                                                                                                  |
|----------------------------|--------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| [isFailure](is-failure.md) | [androidJvm]<br>val [isFailure](is-failure.md): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)<br>Returns `true` if [result](result.md) instance represents a failed outcome. In this case [eu.europa.ec.eudi.wallet.document.Outcome.isSuccess](is-success.md) returns `false`.     |
| [isSuccess](is-success.md) | [androidJvm]<br>val [isSuccess](is-success.md): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)<br>Returns `true` if [result](result.md) instance represents a successful outcome. In this case [eu.europa.ec.eudi.wallet.document.Outcome.isFailure](is-failure.md) return `false` . |
| [result](result.md)        | [androidJvm]<br>val [result](result.md): [Result](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-result/index.html)&lt;[T](index.md)&gt;                                                                                                                                                                           |

## Functions

| Name                                    | Summary                                                                                                                                                                                                                                                                |
|-----------------------------------------|------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| [exceptionOrNull](exception-or-null.md) | [androidJvm]<br>fun [exceptionOrNull](exception-or-null.md)(): [Throwable](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-throwable/index.html)?<br>Returns the encapsulated exception if this instance represents a failure outcome or `null` if it is success. |
| [getOrNull](get-or-null.md)             | [androidJvm]<br>fun [getOrNull](get-or-null.md)(): [T](index.md)?<br>Returns the encapsulated value if this instance represents a successful outcome or `null` if it is failure.                                                                                       |
| [getOrThrow](get-or-throw.md)           | [androidJvm]<br>fun [getOrThrow](get-or-throw.md)(): [T](index.md)<br>Returns the encapsulated value if this instance represents a successful outcome or throws the encapsulated exception if it is failure.                                                           |
| [toString](to-string.md)                | [androidJvm]<br>open override fun [toString](to-string.md)(): [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)                                                                                                                         |
