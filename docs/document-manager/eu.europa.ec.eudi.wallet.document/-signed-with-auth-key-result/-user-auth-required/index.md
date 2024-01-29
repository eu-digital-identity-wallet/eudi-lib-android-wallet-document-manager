//[document-manager](../../../../index.md)/[eu.europa.ec.eudi.wallet.document](../../index.md)/[SignedWithAuthKeyResult](../index.md)/[UserAuthRequired](index.md)

# UserAuthRequired

[androidJvm]\
data class [UserAuthRequired](index.md)(val cryptoObject: [BiometricPrompt.CryptoObject](https://developer.android.com/reference/kotlin/androidx/biometric/BiometricPrompt.CryptoObject.html)?) : [SignedWithAuthKeyResult](../index.md)

User authentication is required to sign data

## Constructors

| | |
|---|---|
| [UserAuthRequired](-user-auth-required.md) | [androidJvm]<br>constructor(cryptoObject: [BiometricPrompt.CryptoObject](https://developer.android.com/reference/kotlin/androidx/biometric/BiometricPrompt.CryptoObject.html)?) |

## Functions

| Name | Summary |
|---|---|
| [onFailure](../on-failure.md) | [androidJvm]<br>open fun [onFailure](../on-failure.md)(block: ([Throwable](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-throwable/index.html)) -&gt; [Unit](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)): [SignedWithAuthKeyResult](../index.md)<br>Execute block if the result is a failure |
| [onSuccess](../on-success.md) | [androidJvm]<br>open fun [onSuccess](../on-success.md)(block: ([ByteArray](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-byte-array/index.html)) -&gt; [Unit](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)): [SignedWithAuthKeyResult](../index.md)<br>Execute block if the result is successful |
| [onUserAuthRequired](../on-user-auth-required.md) | [androidJvm]<br>open fun [onUserAuthRequired](../on-user-auth-required.md)(block: ([BiometricPrompt.CryptoObject](https://developer.android.com/reference/kotlin/androidx/biometric/BiometricPrompt.CryptoObject.html)?) -&gt; [Unit](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)): [SignedWithAuthKeyResult](../index.md)<br>Execute block if the result requires user authentication |

## Properties

| Name | Summary |
|---|---|
| [cryptoObject](crypto-object.md) | [androidJvm]<br>val [cryptoObject](crypto-object.md): [BiometricPrompt.CryptoObject](https://developer.android.com/reference/kotlin/androidx/biometric/BiometricPrompt.CryptoObject.html)? |
