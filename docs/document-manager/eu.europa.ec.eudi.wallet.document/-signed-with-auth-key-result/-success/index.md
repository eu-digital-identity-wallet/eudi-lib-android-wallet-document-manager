//[document-manager](../../../../index.md)/[eu.europa.ec.eudi.wallet.document](../../index.md)/[SignedWithAuthKeyResult](../index.md)/[Success](index.md)

# Success

[androidJvm]\
data class [Success](index.md)(val signature: [ByteArray](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-byte-array/index.html)) : [SignedWithAuthKeyResult](../index.md)

Success result containing the signature of data

## Constructors

| | |
|---|---|
| [Success](-success.md) | [androidJvm]<br>constructor(signature: [ByteArray](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-byte-array/index.html)) |

## Functions

| Name | Summary |
|---|---|
| [equals](equals.md) | [androidJvm]<br>open operator override fun [equals](equals.md)(other: [Any](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/index.html)?): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) |
| [hashCode](hash-code.md) | [androidJvm]<br>open override fun [hashCode](hash-code.md)(): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html) |
| [onFailure](../on-failure.md) | [androidJvm]<br>open fun [onFailure](../on-failure.md)(block: ([Throwable](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-throwable/index.html)) -&gt; [Unit](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)): [SignedWithAuthKeyResult](../index.md)<br>Execute block if the result is a failure |
| [onSuccess](../on-success.md) | [androidJvm]<br>open fun [onSuccess](../on-success.md)(block: ([ByteArray](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-byte-array/index.html)) -&gt; [Unit](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)): [SignedWithAuthKeyResult](../index.md)<br>Execute block if the result is successful |
| [onUserAuthRequired](../on-user-auth-required.md) | [androidJvm]<br>open fun [onUserAuthRequired](../on-user-auth-required.md)(block: ([BiometricPrompt.CryptoObject](https://developer.android.com/reference/kotlin/androidx/biometric/BiometricPrompt.CryptoObject.html)?) -&gt; [Unit](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)): [SignedWithAuthKeyResult](../index.md)<br>Execute block if the result requires user authentication |

## Properties

| Name | Summary |
|---|---|
| [signature](signature.md) | [androidJvm]<br>val [signature](signature.md): [ByteArray](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-byte-array/index.html) |
