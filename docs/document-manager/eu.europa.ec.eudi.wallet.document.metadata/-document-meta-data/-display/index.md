//[document-manager](../../../../index.md)/[eu.europa.ec.eudi.wallet.document.metadata](../../index.md)/[DocumentMetaData](../index.md)/[Display](index.md)

# Display

[androidJvm]\
@Serializable

data class [Display](index.md)(val
name: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html), val
locale: [Locale](https://developer.android.com/reference/kotlin/java/util/Locale.html)? = null, val
logo: [DocumentMetaData.Display.Logo](-logo/index.md)? = null, val
description: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)? =
null, val
backgroundColor: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)? =
null, val
textColor: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)? = null)

Display properties of a supported credential type for a certain language.

## Constructors

|                        |                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                           |
|------------------------|-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| [Display](-display.md) | [androidJvm]<br>constructor(name: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html), locale: [Locale](https://developer.android.com/reference/kotlin/java/util/Locale.html)? = null, logo: [DocumentMetaData.Display.Logo](-logo/index.md)? = null, description: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)? = null, backgroundColor: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)? = null, textColor: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)? = null) |

## Types

| Name                   | Summary                                                                                                                                                                                                                                                                                                                                                                                     |
|------------------------|---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| [Logo](-logo/index.md) | [androidJvm]<br>@Serializable<br>data class [Logo](-logo/index.md)(val uri: [URI](https://developer.android.com/reference/kotlin/java/net/URI.html)? = null, val alternativeText: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)? = null) : [Serializable](https://developer.android.com/reference/kotlin/java/io/Serializable.html)<br>Logo information. |

## Properties

| Name                                   | Summary                                                                                                                                                                          |
|----------------------------------------|----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| [backgroundColor](background-color.md) | [androidJvm]<br>val [backgroundColor](background-color.md): [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)? = null                             |
| [description](description.md)          | [androidJvm]<br>val [description](description.md): [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)? = null                                      |
| [locale](locale.md)                    | [androidJvm]<br>@Serializable(with = LocaleSerializer::class)<br>val [locale](locale.md): [Locale](https://developer.android.com/reference/kotlin/java/util/Locale.html)? = null |
| [logo](logo.md)                        | [androidJvm]<br>val [logo](logo.md): [DocumentMetaData.Display.Logo](-logo/index.md)? = null                                                                                     |
| [name](name.md)                        | [androidJvm]<br>val [name](name.md): [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)                                                            |
| [textColor](text-color.md)             | [androidJvm]<br>val [textColor](text-color.md): [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)? = null                                         |
