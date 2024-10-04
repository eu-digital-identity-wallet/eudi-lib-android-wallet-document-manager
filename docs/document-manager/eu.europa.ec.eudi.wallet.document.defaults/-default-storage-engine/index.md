//[document-manager](../../../index.md)/[eu.europa.ec.eudi.wallet.document.defaults](../index.md)/[DefaultStorageEngine](index.md)

# DefaultStorageEngine

[androidJvm]\
class [DefaultStorageEngine](index.md)(
context: [Context](https://developer.android.com/reference/kotlin/android/content/Context.html),
storageFile: [File](https://developer.android.com/reference/kotlin/java/io/File.html) = File(
context.noBackupFilesDir, &quot;eudi-identity.bin&quot;),
useEncryption: [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) =
true) : StorageEngine

## Constructors

|                                                    |                                                                                                                                                                                                                                                                                                                                                                                               |
|----------------------------------------------------|-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| [DefaultStorageEngine](-default-storage-engine.md) | [androidJvm]<br>constructor(context: [Context](https://developer.android.com/reference/kotlin/android/content/Context.html), storageFile: [File](https://developer.android.com/reference/kotlin/java/io/File.html) = File(context.noBackupFilesDir, &quot;eudi-identity.bin&quot;), useEncryption: [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) = true) |

## Functions

| Name                                                     | Summary                                                                                                                                                                                                                                                                                    |
|----------------------------------------------------------|--------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| [delete](index.md#-705698692%2FFunctions%2F1351694608)   | [androidJvm]<br>open override fun [delete](index.md#-705698692%2FFunctions%2F1351694608)(key: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html))                                                                                                            |
| [deleteAll](index.md#150767537%2FFunctions%2F1351694608) | [androidJvm]<br>open override fun [deleteAll](index.md#150767537%2FFunctions%2F1351694608)()                                                                                                                                                                                               |
| [enumerate](index.md#173220099%2FFunctions%2F1351694608) | [androidJvm]<br>open override fun [enumerate](index.md#173220099%2FFunctions%2F1351694608)(): [Collection](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-collection/index.html)&lt;[String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)&gt; |
| [get](index.md#1583892691%2FFunctions%2F1351694608)      | [androidJvm]<br>open operator override fun [get](index.md#1583892691%2FFunctions%2F1351694608)(key: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)): [ByteArray](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-byte-array/index.html)?            |
| [put](index.md#-1742996401%2FFunctions%2F1351694608)     | [androidJvm]<br>open override fun [put](index.md#-1742996401%2FFunctions%2F1351694608)(key: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html), data: [ByteArray](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-byte-array/index.html))               |
