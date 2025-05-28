//[document-manager](../../../index.md)/[eu.europa.ec.eudi.wallet.document.credential](../index.md)/[SdJwtVcCredential](index.md)

# SdJwtVcCredential

[androidJvm]\
class [SdJwtVcCredential](index.md) : SecureAreaBoundCredential, SdJwtVcCredential

A SD-JWT VC credential, according to draft-ietf-oauth-sd-jwt-vc-03 (https://datatracker.ietf.org/doc/draft-ietf-oauth-sd-jwt-vc/).

## Constructors

| | |
|---|---|
| [SdJwtVcCredential](-sd-jwt-vc-credential.md) | [androidJvm]<br>constructor(document: Document, asReplacementForIdentifier: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-string/index.html)?, domain: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-string/index.html), secureArea: SecureArea, vct: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-string/index.html))<br>Constructs a new [SdJwtVcCredential](index.md).<br>constructor(document: Document)<br>Constructs a Credential from serialized data. |

## Types

| Name | Summary |
|---|---|
| [Companion](-companion/index.md) | [androidJvm]<br>object [Companion](-companion/index.md) |

## Properties

| Name | Summary |
|---|---|
| [alias](index.md#-670682737%2FProperties%2F1351694608) | [androidJvm]<br>lateinit var [alias](index.md#-670682737%2FProperties%2F1351694608): [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-string/index.html) |
| [document](index.md#-1019198258%2FProperties%2F1351694608) | [androidJvm]<br>val [document](index.md#-1019198258%2FProperties%2F1351694608): Document |
| [domain](index.md#589005893%2FProperties%2F1351694608) | [androidJvm]<br>lateinit var [domain](index.md#589005893%2FProperties%2F1351694608): [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-string/index.html) |
| [identifier](index.md#288399872%2FProperties%2F1351694608) | [androidJvm]<br>val [identifier](index.md#288399872%2FProperties%2F1351694608): [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-string/index.html) |
| [isCertified](index.md#138751540%2FProperties%2F1351694608) | [androidJvm]<br>val [isCertified](index.md#138751540%2FProperties%2F1351694608): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-boolean/index.html) |
| [issuerProvidedData](index.md#-1434858557%2FProperties%2F1351694608) | [androidJvm]<br>val [issuerProvidedData](index.md#-1434858557%2FProperties%2F1351694608): [ByteArray](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-byte-array/index.html) |
| [replacementForIdentifier](index.md#-201283543%2FProperties%2F1351694608) | [androidJvm]<br>var [replacementForIdentifier](index.md#-201283543%2FProperties%2F1351694608): [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-string/index.html)? |
| [secureArea](index.md#336720567%2FProperties%2F1351694608) | [androidJvm]<br>lateinit var [secureArea](index.md#336720567%2FProperties%2F1351694608): SecureArea |
| [usageCount](index.md#963106395%2FProperties%2F1351694608) | [androidJvm]<br>var [usageCount](index.md#963106395%2FProperties%2F1351694608): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html) |
| [validFrom](index.md#-835453845%2FProperties%2F1351694608) | [androidJvm]<br>val [validFrom](index.md#-835453845%2FProperties%2F1351694608): Instant |
| [validUntil](index.md#2086981639%2FProperties%2F1351694608) | [androidJvm]<br>val [validUntil](index.md#2086981639%2FProperties%2F1351694608): Instant |
| [vct](vct.md) | [androidJvm]<br>open lateinit override var [vct](vct.md): [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-string/index.html)<br>The Verifiable Credential Type - or `vct` - as defined in section 3.2.2.1.1 of draft-ietf-oauth-sd-jwt-vc-03 (https://datatracker.ietf.org/doc/draft-ietf-oauth-sd-jwt-vc/) |

## Functions

| Name | Summary |
|---|---|
| [addSerializedData](add-serialized-data.md) | [androidJvm]<br>open override fun [addSerializedData](add-serialized-data.md)(builder: MapBuilder&lt;CborBuilder&gt;) |
| [addToDocument](index.md#1991203098%2FFunctions%2F1351694608) | [androidJvm]<br>suspend fun [addToDocument](index.md#1991203098%2FFunctions%2F1351694608)() |
| [certify](index.md#1333111093%2FFunctions%2F1351694608) | [androidJvm]<br>open suspend fun [certify](index.md#1333111093%2FFunctions%2F1351694608)(issuerProvidedAuthenticationData: [ByteArray](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-byte-array/index.html), validFrom: Instant, validUntil: Instant) |
| [deserialize](deserialize.md) | [androidJvm]<br>open suspend override fun [deserialize](deserialize.md)(dataItem: DataItem) |
| [getAttestation](index.md#-9446527%2FFunctions%2F1351694608) | [androidJvm]<br>suspend fun [getAttestation](index.md#-9446527%2FFunctions%2F1351694608)(): KeyAttestation |
| [getClaims](get-claims.md) | [androidJvm]<br>open override fun [getClaims](get-claims.md)(documentTypeRepository: DocumentTypeRepository?): [List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin.collections/-list/index.html)&lt;VcClaim&gt; |
| [getClaimsImpl](index.md#-1252900650%2FFunctions%2F1351694608) | [androidJvm]<br>open fun [getClaimsImpl](index.md#-1252900650%2FFunctions%2F1351694608)(documentTypeRepository: DocumentTypeRepository?): [List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin.collections/-list/index.html)&lt;VcClaim&gt; |
| [increaseUsageCount](index.md#-24404807%2FFunctions%2F1351694608) | [androidJvm]<br>suspend fun [increaseUsageCount](index.md#-24404807%2FFunctions%2F1351694608)() |
| [isInvalidated](index.md#768951744%2FFunctions%2F1351694608) | [androidJvm]<br>open suspend override fun [isInvalidated](index.md#768951744%2FFunctions%2F1351694608)(): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-boolean/index.html) |
| [replacementForDeleted](index.md#-1712460561%2FFunctions%2F1351694608) | [androidJvm]<br>suspend fun [replacementForDeleted](index.md#-1712460561%2FFunctions%2F1351694608)() |
