//[document-manager](../../../../../index.md)/[eu.europa.ec.eudi.wallet.document.metadata](../../../index.md)/[DocumentMetaData](../../index.md)/[Display](../index.md)/[Logo](index.md)

# Logo

[androidJvm]\
@Serializable

data class [Logo](index.md)(val
uri: [URI](https://developer.android.com/reference/kotlin/java/net/URI.html)? = null, val
alternativeText: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)? =
null) : [Serializable](https://developer.android.com/reference/kotlin/java/io/Serializable.html)

Logo information.

## Constructors

|                  |                                                                                                                                                                                                                              |
|------------------|------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| [Logo](-logo.md) | [androidJvm]<br>constructor(uri: [URI](https://developer.android.com/reference/kotlin/java/net/URI.html)? = null, alternativeText: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)? = null) |

## Properties

| Name                                   | Summary                                                                                                                                                          |
|----------------------------------------|------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| [alternativeText](alternative-text.md) | [androidJvm]<br>val [alternativeText](alternative-text.md): [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)? = null             |
| [uri](uri.md)                          | [androidJvm]<br>@Serializable(with = URISerializer::class)<br>val [uri](uri.md): [URI](https://developer.android.com/reference/kotlin/java/net/URI.html)? = null |
