//[document-manager](../../../index.md)/[eu.europa.ec.eudi.wallet.document.defaults](../index.md)/[DefaultSecureArea](index.md)

# DefaultSecureArea

[androidJvm]\
class [DefaultSecureArea](index.md)(
context: [Context](https://developer.android.com/reference/kotlin/android/content/Context.html),
storageEngine: StorageEngine) : SecureArea

## Constructors

|                                              |                                                                                                                                                            |
|----------------------------------------------|------------------------------------------------------------------------------------------------------------------------------------------------------------|
| [DefaultSecureArea](-default-secure-area.md) | [androidJvm]<br>constructor(context: [Context](https://developer.android.com/reference/kotlin/android/content/Context.html), storageEngine: StorageEngine) |

## Types

| Name                                                              | Summary                                                                                                                                                                                                                                                                                              |
|-------------------------------------------------------------------|------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| [Companion](-companion/index.md)                                  | [androidJvm]<br>object [Companion](-companion/index.md)                                                                                                                                                                                                                                              |
| [CreateKeySettingsFactory](-create-key-settings-factory/index.md) | [androidJvm]<br>class [CreateKeySettingsFactory](-create-key-settings-factory/index.md)(context: [Context](https://developer.android.com/reference/kotlin/android/content/Context.html)) : [CreateKeySettingsFactory](../../eu.europa.ec.eudi.wallet.document/-create-key-settings-factory/index.md) |

## Properties

| Name                                                         | Summary                                                                                                                                                                          |
|--------------------------------------------------------------|----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| [createKeySettingsFactory](create-key-settings-factory.md)   | [androidJvm]<br>val [createKeySettingsFactory](create-key-settings-factory.md): [DefaultSecureArea.CreateKeySettingsFactory](-create-key-settings-factory/index.md)              |
| [displayName](index.md#2128116500%2FProperties%2F1351694608) | [androidJvm]<br>open override val [displayName](index.md#2128116500%2FProperties%2F1351694608): [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html) |
| [identifier](index.md#-101639792%2FProperties%2F1351694608)  | [androidJvm]<br>open override val [identifier](index.md#-101639792%2FProperties%2F1351694608): [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)  |

## Functions

| Name                                                               | Summary                                                                                                                                                                                                                                                                                                                                                          |
|--------------------------------------------------------------------|------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| [createKey](index.md#1665127657%2FFunctions%2F1351694608)          | [androidJvm]<br>open override fun [createKey](index.md#1665127657%2FFunctions%2F1351694608)(alias: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html), createKeySettings: CreateKeySettings)                                                                                                                                       |
| [deleteKey](index.md#-1311353225%2FFunctions%2F1351694608)         | [androidJvm]<br>open override fun [deleteKey](index.md#-1311353225%2FFunctions%2F1351694608)(alias: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html))                                                                                                                                                                            |
| [getKeyInfo](index.md#1261057474%2FFunctions%2F1351694608)         | [androidJvm]<br>open override fun [getKeyInfo](index.md#1261057474%2FFunctions%2F1351694608)(alias: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)): KeyInfo                                                                                                                                                                   |
| [getKeyInvalidated](index.md#-1117988765%2FFunctions%2F1351694608) | [androidJvm]<br>open override fun [getKeyInvalidated](index.md#-1117988765%2FFunctions%2F1351694608)(alias: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)                                                                                |
| [keyAgreement](index.md#1875729821%2FFunctions%2F1351694608)       | [androidJvm]<br>open override fun [keyAgreement](index.md#1875729821%2FFunctions%2F1351694608)(alias: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html), otherKey: EcPublicKey, keyUnlockData: KeyUnlockData?): [ByteArray](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-byte-array/index.html)                           |
| [sign](index.md#-1171361423%2FFunctions%2F1351694608)              | [androidJvm]<br>open override fun [sign](index.md#-1171361423%2FFunctions%2F1351694608)(alias: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html), signatureAlgorithm: Algorithm, dataToSign: [ByteArray](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-byte-array/index.html), keyUnlockData: KeyUnlockData?): EcSignature |
