//[document-manager](../../../../index.md)/[eu.europa.ec.eudi.wallet.document.metadata](../../index.md)/[IssuerMetadata](../index.md)/[Display](index.md)

# Display

@Serializable

data class [Display](index.md)(val name: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-string/index.html), val locale: [Locale](https://developer.android.com/reference/kotlin/java/util/Locale.html)? = null, val logo: [IssuerMetadata.Logo](../-logo/index.md)? = null, val description: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-string/index.html)? = null, val backgroundColor: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-string/index.html)? = null, val textColor: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-string/index.html)? = null, val backgroundImageUri: [URI](https://developer.android.com/reference/kotlin/java/net/URI.html)? = null)

Display properties of a supported credential type for a certain language.

#### See also

| |
|---|
| [IssuerMetadata.Logo](../-logo/index.md) |
| [Locale](https://developer.android.com/reference/kotlin/java/util/Locale.html) |

## Constructors

| | |
|---|---|
| [Display](-display.md) | [androidJvm]<br>constructor(name: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-string/index.html), locale: [Locale](https://developer.android.com/reference/kotlin/java/util/Locale.html)? = null, logo: [IssuerMetadata.Logo](../-logo/index.md)? = null, description: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-string/index.html)? = null, backgroundColor: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-string/index.html)? = null, textColor: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-string/index.html)? = null, backgroundImageUri: [URI](https://developer.android.com/reference/kotlin/java/net/URI.html)? = null) |

## Properties

| Name | Summary |
|---|---|
| [backgroundColor](background-color.md) | [androidJvm]<br>val [backgroundColor](background-color.md): [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-string/index.html)? = null<br>the background color of the document |
| [backgroundImageUri](background-image-uri.md) | [androidJvm]<br>@Serializable(with = URISerializer::class)<br>@SerialName(value = &quot;backgroundImageUri&quot;)<br>val [backgroundImageUri](background-image-uri.md): [URI](https://developer.android.com/reference/kotlin/java/net/URI.html)? = null<br>the URI of the background image |
| [description](description.md) | [androidJvm]<br>val [description](description.md): [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-string/index.html)? = null<br>the description of the document |
| [locale](locale.md) | [androidJvm]<br>@Serializable(with = LocaleSerializer::class)<br>val [locale](locale.md): [Locale](https://developer.android.com/reference/kotlin/java/util/Locale.html)? = null<br>the locale of the current display |
| [logo](logo.md) | [androidJvm]<br>val [logo](logo.md): [IssuerMetadata.Logo](../-logo/index.md)? = null<br>the logo of the document |
| [name](name.md) | [androidJvm]<br>val [name](name.md): [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-string/index.html)<br>the name of the document |
| [textColor](text-color.md) | [androidJvm]<br>val [textColor](text-color.md): [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-string/index.html)? = null<br>the text color of the document |
