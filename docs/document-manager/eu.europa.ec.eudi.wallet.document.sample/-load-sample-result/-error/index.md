//[document-manager](../../../../index.md)/[eu.europa.ec.eudi.wallet.document.sample](../../index.md)/[LoadSampleResult](../index.md)/[Error](index.md)

# Error

data class [Error](index.md)(val throwable: [Throwable](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-throwable/index.html)) : [LoadSampleResult](../index.md)

Error class to return the error message.

#### Parameters

androidJvm

| | |
|---|---|
| throwable | exception that caused the error |

## Constructors

| | |
|---|---|
| [Error](-error.md) | [androidJvm]<br>constructor(message: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html))constructor(throwable: [Throwable](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-throwable/index.html)) |

## Properties

| Name | Summary |
|---|---|
| [message](message.md) | [androidJvm]<br>val [message](message.md): [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)<br>error message |
| [throwable](throwable.md) | [androidJvm]<br>val [throwable](throwable.md): [Throwable](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-throwable/index.html)<br>exception that caused the error |
