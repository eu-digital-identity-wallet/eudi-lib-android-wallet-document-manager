//[document-manager](../../../../index.md)/[eu.europa.ec.eudi.wallet.document](../../index.md)/[Outcome](../index.md)/[Companion](index.md)

# Companion

[androidJvm]\
object [Companion](index.md)

## Functions

| Name                  | Summary                                                                                                                                                                                                                                                                                                                                                                                                                                                                   |
|-----------------------|---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| [failure](failure.md) | [androidJvm]<br>@[JvmStatic](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.jvm/-jvm-static/index.html)<br>fun &lt;[T](failure.md)&gt; [failure](failure.md)(throwable: [Throwable](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-throwable/index.html)): [Outcome](../index.md)&lt;[T](failure.md)&gt;<br>Returns an instance that encapsulates the given [Throwable](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-throwable/index.html) as failure. |
| [success](success.md) | [androidJvm]<br>@[JvmStatic](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.jvm/-jvm-static/index.html)<br>fun &lt;[T](success.md)&gt; [success](success.md)(value: [T](success.md)): [Outcome](../index.md)&lt;[T](success.md)&gt;<br>Returns an instance that encapsulates the given [value](success.md) as successful value.                                                                                                                                      |
