//[document-manager](../../../index.md)/[eu.europa.ec.eudi.wallet.document](../index.md)/[SignedWithAuthKeyResult](index.md)

# SignedWithAuthKeyResult

interface [SignedWithAuthKeyResult](index.md)

#### Inheritors

| |
|---|
| [Success](-success/index.md) |
| [UserAuthRequired](-user-auth-required/index.md) |
| [Failure](-failure/index.md) |

## Types

| Name | Summary |
|---|---|
| [Failure](-failure/index.md) | [androidJvm]<br>data class [Failure](-failure/index.md)(val throwable: [Throwable](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-throwable/index.html)) : [SignedWithAuthKeyResult](index.md)<br>Failure while signing the data. Contains the throwable that caused the failure |
| [Success](-success/index.md) | [androidJvm]<br>data class [Success](-success/index.md)(val signature: [ByteArray](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-byte-array/index.html)) : [SignedWithAuthKeyResult](index.md)<br>Success result containing the signature of data |
| [UserAuthRequired](-user-auth-required/index.md) | [androidJvm]<br>data class [UserAuthRequired](-user-auth-required/index.md)(val cryptoObject: [BiometricPrompt.CryptoObject](https://developer.android.com/reference/kotlin/androidx/biometric/BiometricPrompt.CryptoObject.html)?) : [SignedWithAuthKeyResult](index.md)<br>User authentication is required to sign data |

## Functions

| Name | Summary |
|---|---|
| [onFailure](on-failure.md) | [androidJvm]<br>open fun [onFailure](on-failure.md)(block: ([Throwable](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-throwable/index.html)) -&gt; [Unit](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)): [SignedWithAuthKeyResult](index.md)<br>Execute block if the result is a failure |
| [onSuccess](on-success.md) | [androidJvm]<br>open fun [onSuccess](on-success.md)(block: ([ByteArray](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-byte-array/index.html)) -&gt; [Unit](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)): [SignedWithAuthKeyResult](index.md)<br>Execute block if the result is successful |
| [onUserAuthRequired](on-user-auth-required.md) | [androidJvm]<br>open fun [onUserAuthRequired](on-user-auth-required.md)(block: ([BiometricPrompt.CryptoObject](https://developer.android.com/reference/kotlin/androidx/biometric/BiometricPrompt.CryptoObject.html)?) -&gt; [Unit](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)): [SignedWithAuthKeyResult](index.md)<br>Execute block if the result requires user authentication |
