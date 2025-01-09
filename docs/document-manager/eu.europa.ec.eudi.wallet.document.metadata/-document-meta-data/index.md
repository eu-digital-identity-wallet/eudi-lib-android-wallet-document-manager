//[document-manager](../../../index.md)/[eu.europa.ec.eudi.wallet.document.metadata](../index.md)/[DocumentMetaData](index.md)

# DocumentMetaData

[androidJvm]\
@Serializable

data class [DocumentMetaData](index.md)(val documentConfigurationIdentifier: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html), val display: [List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)&lt;[DocumentMetaData.Display](-display/index.md)&gt;, val claims: [List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)&lt;[DocumentMetaData.Claim](-claim/index.md)&gt;?, val credentialIssuerIdentifier: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html), val issuerDisplay: [List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)&lt;[DocumentMetaData.Display](-display/index.md)&gt;?)

Document metadata domain object for storage.

## Constructors

| | |
|---|---|
| [DocumentMetaData](-document-meta-data.md) | [androidJvm]<br>constructor(documentConfigurationIdentifier: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html), display: [List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)&lt;[DocumentMetaData.Display](-display/index.md)&gt;, claims: [List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)&lt;[DocumentMetaData.Claim](-claim/index.md)&gt;?, credentialIssuerIdentifier: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html), issuerDisplay: [List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)&lt;[DocumentMetaData.Display](-display/index.md)&gt;?) |

## Types

| Name | Summary |
|---|---|
| [Claim](-claim/index.md) | [androidJvm]<br>@Serializable<br>data class [Claim](-claim/index.md)(val name: [DocumentMetaData.Claim.Name](-claim/-name/index.md), val mandatory: [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)? = false, val valueType: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)? = null, val display: [List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)&lt;[DocumentMetaData.Claim.Display](-claim/-display/index.md)&gt; = emptyList())<br>Claim properties. |
| [Companion](-companion/index.md) | [androidJvm]<br>object [Companion](-companion/index.md) |
| [Display](-display/index.md) | [androidJvm]<br>@Serializable<br>data class [Display](-display/index.md)(val name: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html), val locale: [Locale](https://developer.android.com/reference/kotlin/java/util/Locale.html)? = null, val logo: [DocumentMetaData.Display.Logo](-display/-logo/index.md)? = null, val description: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)? = null, val backgroundColor: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)? = null, val textColor: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)? = null)<br>Display properties of a supported credential type for a certain language. |

## Properties

| Name | Summary |
|---|---|
| [claims](claims.md) | [androidJvm]<br>val [claims](claims.md): [List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)&lt;[DocumentMetaData.Claim](-claim/index.md)&gt;?<br>metadata for the claims of the document |
| [credentialIssuerIdentifier](credential-issuer-identifier.md) | [androidJvm]<br>val [credentialIssuerIdentifier](credential-issuer-identifier.md): [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)<br>the credential issuer identifier |
| [display](display.md) | [androidJvm]<br>val [display](display.md): [List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)&lt;[DocumentMetaData.Display](-display/index.md)&gt;<br>the display properties of the document |
| [documentConfigurationIdentifier](document-configuration-identifier.md) | [androidJvm]<br>val [documentConfigurationIdentifier](document-configuration-identifier.md): [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)<br>the document configuration identifier |
| [issuerDisplay](issuer-display.md) | [androidJvm]<br>val [issuerDisplay](issuer-display.md): [List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)&lt;[DocumentMetaData.Display](-display/index.md)&gt;?<br>the display properties of the issuer that issued the document |

## Functions

| Name | Summary |
|---|---|
| [toJson](to-json.md) | [androidJvm]<br>fun [toJson](to-json.md)(): [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)<br>Convert the object to a JSON string. |
