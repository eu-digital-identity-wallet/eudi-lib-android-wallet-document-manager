//[document-manager](../../../index.md)/[eu.europa.ec.eudi.wallet.document.internal](../index.md)/[SdJwtVcCredential](index.md)

# SdJwtVcCredential

[androidJvm]\
class [SdJwtVcCredential](index.md) : SecureAreaBoundCredential

A SD-JWT VC credential, according to draft-ietf-oauth-sd-jwt-vc-03 (https://datatracker.ietf.org/doc/draft-ietf-oauth-sd-jwt-vc/).

## Constructors

| | |
|---|---|
| [SdJwtVcCredential](-sd-jwt-vc-credential.md) | [androidJvm]<br>constructor(document: Document, asReplacementFor: Credential?, domain: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-string/index.html), secureArea: SecureArea, createKeySettings: CreateKeySettings, vct: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-string/index.html))<br>Constructs a new [SdJwtVcCredential](index.md).<br>constructor(document: Document, dataItem: DataItem)<br>Constructs a Credential from serialized data. |

## Types

| Name | Summary |
|---|---|
| [Companion](-companion/index.md) | [androidJvm]<br>object [Companion](-companion/index.md) |

## Properties

| Name | Summary |
|---|---|
| [alias](index.md#920612639%2FProperties%2F1351694608) | [androidJvm]<br>val [alias](index.md#920612639%2FProperties%2F1351694608): [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-string/index.html) |
| [applicationData](index.md#-1471606521%2FProperties%2F1351694608) | [androidJvm]<br>val [applicationData](index.md#-1471606521%2FProperties%2F1351694608): ApplicationData |
| [attestation](index.md#-1818261217%2FProperties%2F1351694608) | [androidJvm]<br>val [attestation](index.md#-1818261217%2FProperties%2F1351694608): KeyAttestation |
| [credentialCounter](index.md#554104284%2FProperties%2F1351694608) | [androidJvm]<br>var [credentialCounter](index.md#554104284%2FProperties%2F1351694608): [Long](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-long/index.html) |
| [document](index.md#-60990882%2FProperties%2F1351694608) | [androidJvm]<br>val [document](index.md#-60990882%2FProperties%2F1351694608): Document |
| [domain](index.md#-1465860651%2FProperties%2F1351694608) | [androidJvm]<br>val [domain](index.md#-1465860651%2FProperties%2F1351694608): [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-string/index.html) |
| [identifier](index.md#2002686864%2FProperties%2F1351694608) | [androidJvm]<br>val [identifier](index.md#2002686864%2FProperties%2F1351694608): [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-string/index.html) |
| [isCertified](index.md#1742040740%2FProperties%2F1351694608) | [androidJvm]<br>var [isCertified](index.md#1742040740%2FProperties%2F1351694608): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-boolean/index.html) |
| [isInvalidated](index.md#1486877520%2FProperties%2F1351694608) | [androidJvm]<br>open override val [isInvalidated](index.md#1486877520%2FProperties%2F1351694608): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-boolean/index.html) |
| [issuerProvidedData](index.md#1722101075%2FProperties%2F1351694608) | [androidJvm]<br>val [issuerProvidedData](index.md#1722101075%2FProperties%2F1351694608): [ByteArray](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-byte-array/index.html) |
| [replacement](index.md#464338191%2FProperties%2F1351694608) | [androidJvm]<br>val [replacement](index.md#464338191%2FProperties%2F1351694608): Credential? |
| [replacementFor](index.md#-1482031966%2FProperties%2F1351694608) | [androidJvm]<br>val [replacementFor](index.md#-1482031966%2FProperties%2F1351694608): Credential? |
| [secureArea](index.md#573615911%2FProperties%2F1351694608) | [androidJvm]<br>val [secureArea](index.md#573615911%2FProperties%2F1351694608): SecureArea |
| [usageCount](index.md#-1617573909%2FProperties%2F1351694608) | [androidJvm]<br>var [usageCount](index.md#-1617573909%2FProperties%2F1351694608): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html) |
| [validFrom](index.md#-1195796261%2FProperties%2F1351694608) | [androidJvm]<br>val [validFrom](index.md#-1195796261%2FProperties%2F1351694608): Instant |
| [validUntil](index.md#-493698665%2FProperties%2F1351694608) | [androidJvm]<br>val [validUntil](index.md#-493698665%2FProperties%2F1351694608): Instant |
| [vct](vct.md) | [androidJvm]<br>val [vct](vct.md): [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-string/index.html)<br>The Verifiable Credential Type - or `vct` - as defined in section 3.2.2.1.1 of draft-ietf-oauth-sd-jwt-vc-03 (https://datatracker.ietf.org/doc/draft-ietf-oauth-sd-jwt-vc/) |

## Functions

| Name | Summary |
|---|---|
| [addSerializedData](add-serialized-data.md) | [androidJvm]<br>open override fun [addSerializedData](add-serialized-data.md)(builder: MapBuilder&lt;CborBuilder&gt;) |
| [certify](index.md#1121961381%2FFunctions%2F1351694608) | [androidJvm]<br>fun [certify](index.md#1121961381%2FFunctions%2F1351694608)(issuerProvidedAuthenticationData: [ByteArray](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-byte-array/index.html), validFrom: Instant, validUntil: Instant) |
| [delete](index.md#-1693609216%2FFunctions%2F1351694608) | [androidJvm]<br>open override fun [delete](index.md#-1693609216%2FFunctions%2F1351694608)() |
| [increaseUsageCount](index.md#-1162412471%2FFunctions%2F1351694608) | [androidJvm]<br>fun [increaseUsageCount](index.md#-1162412471%2FFunctions%2F1351694608)() |
| [toCbor](index.md#-2144436964%2FFunctions%2F1351694608) | [androidJvm]<br>fun [toCbor](index.md#-2144436964%2FFunctions%2F1351694608)(): DataItem |
