//[document-manager](../../../index.md)/[eu.europa.ec.eudi.wallet.document](../index.md)/[IssuanceRequest](index.md)/[signWithAuthKey](sign-with-auth-key.md)

# signWithAuthKey

[androidJvm]\
abstract fun [signWithAuthKey](sign-with-auth-key.md)(data: [ByteArray](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-byte-array/index.html), alg: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html) = Algorithm.SHA256withECDSA): [SignedWithAuthKeyResult](../-signed-with-auth-key-result/index.md)

Sign given data with authentication key

Available algorithms are:

- 
   [Algorithm.SHA256withECDSA](../-algorithm/-companion/-s-h-a256with-e-c-d-s-a.md)

#### Return

[SignedWithAuthKeyResult.Success](../-signed-with-auth-key-result/-success/index.md) containing the signature if successful, [SignedWithAuthKeyResult.UserAuthRequired](../-signed-with-auth-key-result/-user-auth-required/index.md) if user authentication is required to sign data, [SignedWithAuthKeyResult.Failure](../-signed-with-auth-key-result/-failure/index.md) if an error occurred while signing the data

#### Parameters

androidJvm

| | |
|---|---|
| data | to be signed |
| alg | algorithm to be used for signing the data (example: &quot;SHA256withECDSA&quot;) |
