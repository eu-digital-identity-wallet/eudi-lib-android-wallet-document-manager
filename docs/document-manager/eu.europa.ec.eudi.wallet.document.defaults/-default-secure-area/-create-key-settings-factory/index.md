//[document-manager](../../../../index.md)/[eu.europa.ec.eudi.wallet.document.defaults](../../index.md)/[DefaultSecureArea](../index.md)/[CreateKeySettingsFactory](index.md)

# CreateKeySettingsFactory

[androidJvm]\
class [CreateKeySettingsFactory](index.md)(
context: [Context](https://developer.android.com/reference/kotlin/android/content/Context.html)) : [CreateKeySettingsFactory](../../../eu.europa.ec.eudi.wallet.document/-create-key-settings-factory/index.md)

## Constructors

|                                                             |                                                                                                                              |
|-------------------------------------------------------------|------------------------------------------------------------------------------------------------------------------------------|
| [CreateKeySettingsFactory](-create-key-settings-factory.md) | [androidJvm]<br>constructor(context: [Context](https://developer.android.com/reference/kotlin/android/content/Context.html)) |

## Properties

| Name                                                      | Summary                                                                                                                                                                     |
|-----------------------------------------------------------|-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| [attestationChallenge](attestation-challenge.md)          | [androidJvm]<br>var [attestationChallenge](attestation-challenge.md): [ByteArray](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-byte-array/index.html)               |
| [curve](curve.md)                                         | [androidJvm]<br>var [curve](curve.md): EcCurve                                                                                                                              |
| [keyPurposes](key-purposes.md)                            | [androidJvm]<br>var [keyPurposes](key-purposes.md): [Set](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-set/index.html)&lt;KeyPurpose&gt;                |
| [userAuth](user-auth.md)                                  | [androidJvm]<br>var [userAuth](user-auth.md): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)                                            |
| [userAuthTimeoutInMillis](user-auth-timeout-in-millis.md) | [androidJvm]<br>var [userAuthTimeoutInMillis](user-auth-timeout-in-millis.md): [Long](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-long/index.html)                 |
| [userAuthType](user-auth-type.md)                         | [androidJvm]<br>var [userAuthType](user-auth-type.md): [Set](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-set/index.html)&lt;UserAuthenticationType&gt; |
| [useStrongBox](use-strong-box.md)                         | [androidJvm]<br>var [useStrongBox](use-strong-box.md): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)                                   |

## Functions

| Name                                        | Summary                                                                                            |
|---------------------------------------------|----------------------------------------------------------------------------------------------------|
| [createKeySettings](create-key-settings.md) | [androidJvm]<br>open override fun [createKeySettings](create-key-settings.md)(): CreateKeySettings |
