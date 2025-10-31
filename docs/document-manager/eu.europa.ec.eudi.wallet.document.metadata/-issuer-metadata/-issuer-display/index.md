//[document-manager](../../../../index.md)/[eu.europa.ec.eudi.wallet.document.metadata](../../index.md)/[IssuerMetadata](../index.md)/[IssuerDisplay](index.md)

# IssuerDisplay

[androidJvm]\
@Serializable

data class [IssuerDisplay](index.md)(val name: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-string/index.html), val locale: [Locale](https://developer.android.com/reference/kotlin/java/util/Locale.html)? = null, val logo: [IssuerMetadata.Logo](../-logo/index.md)? = null)

Display properties of the issuer that issued the document.

## Constructors

| | |
|---|---|
| [IssuerDisplay](-issuer-display.md) | [androidJvm]<br>constructor(name: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-string/index.html), locale: [Locale](https://developer.android.com/reference/kotlin/java/util/Locale.html)? = null, logo: [IssuerMetadata.Logo](../-logo/index.md)? = null) |

## Properties

| Name | Summary |
|---|---|
| [locale](locale.md) | [androidJvm]<br>@Serializable(with = LocaleSerializer::class)<br>val [locale](locale.md): [Locale](https://developer.android.com/reference/kotlin/java/util/Locale.html)? = null<br>the locale of the current display |
| [logo](logo.md) | [androidJvm]<br>val [logo](logo.md): [IssuerMetadata.Logo](../-logo/index.md)? = null<br>the logo of the issuer |
| [name](name.md) | [androidJvm]<br>val [name](name.md): [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-string/index.html)<br>the name of the issuer |
