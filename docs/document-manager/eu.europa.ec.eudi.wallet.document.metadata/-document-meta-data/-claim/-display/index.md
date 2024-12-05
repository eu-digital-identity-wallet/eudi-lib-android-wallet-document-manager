//[document-manager](../../../../../index.md)/[eu.europa.ec.eudi.wallet.document.metadata](../../../index.md)/[DocumentMetaData](../../index.md)/[Claim](../index.md)/[Display](index.md)

# Display

[androidJvm]\
@Serializable

data class [Display](index.md)(val
name: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)? = null, val
locale: [Locale](https://developer.android.com/reference/kotlin/java/util/Locale.html)? =
null) : [Serializable](https://developer.android.com/reference/kotlin/java/io/Serializable.html)

Display properties of a Claim.

## Constructors

|                        |                                                                                                                                                                                                                             |
|------------------------|-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| [Display](-display.md) | [androidJvm]<br>constructor(name: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)? = null, locale: [Locale](https://developer.android.com/reference/kotlin/java/util/Locale.html)? = null) |

## Properties

| Name                | Summary                                                                                                                                                                                                                     |
|---------------------|-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| [locale](locale.md) | [androidJvm]<br>@Serializable(with = LocaleSerializer::class)<br>@SerialName(value = &quot;locale&quot;)<br>val [locale](locale.md): [Locale](https://developer.android.com/reference/kotlin/java/util/Locale.html)? = null |
| [name](name.md)     | [androidJvm]<br>@SerialName(value = &quot;name&quot;)<br>val [name](name.md): [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)? = null                                                      |
