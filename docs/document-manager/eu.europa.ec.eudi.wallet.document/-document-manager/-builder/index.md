//[document-manager](../../../../index.md)/[eu.europa.ec.eudi.wallet.document](../../index.md)/[DocumentManager](../index.md)/[Builder](index.md)

# Builder

class [Builder](index.md)(context: [Context](https://developer.android.com/reference/kotlin/android/content/Context.html))

Builder class to instantiate the default DocumentManager implementation.

example:

```kotlin
val documentManager = DocumentManager.Builder(context)
   .useEncryption(true)
   .storageDir(context.noBackupFilesDir)
   .enableUserAuth(true)
   .userAuthTimeout(30000)
   .build()
```

#### Parameters

androidJvm

| | |
|---|---|
| context | [Context](https://developer.android.com/reference/kotlin/android/content/Context.html) used to instantiate the DocumentManager |

## Constructors

| | |
|---|---|
| [Builder](-builder.md) | [androidJvm]<br>constructor(context: [Context](https://developer.android.com/reference/kotlin/android/content/Context.html)) |

## Functions

| Name | Summary |
|---|---|
| [build](build.md) | [androidJvm]<br>fun [build](build.md)(): [DocumentManager](../index.md)<br>Build the DocumentManager |
| [checkPublicKeyBeforeAdding](check-public-key-before-adding.md) | [androidJvm]<br>fun [checkPublicKeyBeforeAdding](check-public-key-before-adding.md)(checkPublicKeyBeforeAdding: [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)): [DocumentManager.Builder](index.md)<br>Sets whether to check public key in MSO before adding document to storage. By default this is set to true. This check is done to prevent adding documents with public key that is not in MSO. The public key from the [IssuanceRequest](../../-issuance-request/index.md) must match the public key in MSO. |
| [enableUserAuth](enable-user-auth.md) | [androidJvm]<br>fun [enableUserAuth](enable-user-auth.md)(enable: [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)): [DocumentManager.Builder](index.md)<br>Sets whether to require user authentication to access the document. |
| [storageDir](storage-dir.md) | [androidJvm]<br>fun [storageDir](storage-dir.md)(storageDir: [File](https://developer.android.com/reference/kotlin/java/io/File.html)): [DocumentManager.Builder](index.md)<br>The directory to store data files in. By default the [Context.getNoBackupFilesDir](https://developer.android.com/reference/kotlin/android/content/Context.html#getnobackupfilesdir) is used. |
| [useEncryption](use-encryption.md) | [androidJvm]<br>fun [useEncryption](use-encryption.md)(useEncryption: [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)): [DocumentManager.Builder](index.md)<br>Sets whether to encrypt the values stored on disk. Note that keys are not encrypted, only values. By default this is set to true. |
| [userAuthTimeout](user-auth-timeout.md) | [androidJvm]<br>fun [userAuthTimeout](user-auth-timeout.md)(timeoutInMillis: [Long](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-long/index.html)): [DocumentManager.Builder](index.md)<br>Sets the timeout in milliseconds for user authentication. |

## Properties

| Name | Summary |
|---|---|
| [checkPublicKeyBeforeAdding](check-public-key-before-adding.md) | [androidJvm]<br>var [checkPublicKeyBeforeAdding](check-public-key-before-adding.md): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)<br>flag that indicates if the public key from the [IssuanceRequest](../../-issuance-request/index.md) must match the public key in MSO. By default this is set to true. |
| [storageDir](storage-dir.md) | [androidJvm]<br>var [storageDir](storage-dir.md): [File](https://developer.android.com/reference/kotlin/java/io/File.html)<br>the directory to store data files in. By default the [Context.getNoBackupFilesDir](https://developer.android.com/reference/kotlin/android/content/Context.html#getnobackupfilesdir) is used. |
| [useEncryption](use-encryption.md) | [androidJvm]<br>var [useEncryption](use-encryption.md): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)<br>whether to encrypt the values stored on disk. Note that keys are not encrypted, only values. By default this is set to true. |
| [userAuth](user-auth.md) | [androidJvm]<br>var [userAuth](user-auth.md): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)<br>flag that indicates if the document requires user authentication to be accessed. By default this is set to true if the device is secured with a PIN, password or pattern. |
| [userAuthTimeoutInMillis](user-auth-timeout-in-millis.md) | [androidJvm]<br>var [userAuthTimeoutInMillis](user-auth-timeout-in-millis.md): [Long](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-long/index.html)<br>timeout in milliseconds for user authentication. By default this is set to 30 seconds. |
