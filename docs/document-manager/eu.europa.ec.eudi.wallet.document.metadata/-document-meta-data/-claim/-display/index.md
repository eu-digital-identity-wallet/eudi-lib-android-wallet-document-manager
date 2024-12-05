//[document-manager](../../../../../index.md)/[eu.europa.ec.eudi.wallet.document.metadata](../../../index.md)/[DocumentMetaData](../../index.md)/[Claim](../index.md)/[Display](index.md)

# Display

@Serializable

data class [Display](index.md)(val
name: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)? = null, val
locale: [Locale](https://developer.android.com/reference/kotlin/java/util/Locale.html)? = null)

Display properties of a Claim.

#### See also

|                                                                                |
|--------------------------------------------------------------------------------|
| [Locale](https://developer.android.com/reference/kotlin/java/util/Locale.html) |

## Constructors

|                        |                                                                                                                                                                                                                             |
|------------------------|-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| [Display](-display.md) | [androidJvm]<br>constructor(name: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)? = null, locale: [Locale](https://developer.android.com/reference/kotlin/java/util/Locale.html)? = null) |

## Properties

| Name                | Summary                                                                                                                                                                                                                                                          |
|---------------------|------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| [locale](locale.md) | [androidJvm]<br>@Serializable(with = LocaleSerializer::class)<br>@SerialName(value = &quot;locale&quot;)<br>val [locale](locale.md): [Locale](https://developer.android.com/reference/kotlin/java/util/Locale.html)? = null<br>the locale of the current display |
| [name](name.md)     | [androidJvm]<br>@SerialName(value = &quot;name&quot;)<br>val [name](name.md): [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)? = null<br>the name of the claim                                                                  |
