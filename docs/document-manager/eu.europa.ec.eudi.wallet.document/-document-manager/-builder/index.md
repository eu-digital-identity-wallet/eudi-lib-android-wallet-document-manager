//[document-manager](../../../../index.md)/[eu.europa.ec.eudi.wallet.document](../../index.md)/[DocumentManager](../index.md)/[Builder](index.md)

# Builder

class [Builder](index.md)(context: [Context](https://developer.android.com/reference/kotlin/android/content/Context.html))

Builder class to instantiate the default DocumentManager implementation.

example:

```kotlin
val documentManager = DocumentManager.Builder(context)
  .storageEngine(MyStorageEngine())
  .secureArea(MySecureArea())
  .createKeySettingsFactory(MyCreateKeySettingsFactory())
  .checkPublicKeyBeforeAdding(true)
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

## Properties

| Name                                                            | Summary                                                                                                                                                                                                                                                                                                                                                                                                     |
|-----------------------------------------------------------------|-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| [checkPublicKeyBeforeAdding](check-public-key-before-adding.md) | [androidJvm]<br>var [checkPublicKeyBeforeAdding](check-public-key-before-adding.md): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)<br>flag that indicates if the public key from the [UnsignedDocument](../../-unsigned-document/index.md) must match the public key in MSO. By default this is set to true.                                                           |
| [createKeySettingsFactory](create-key-settings-factory.md)      | [androidJvm]<br>var [createKeySettingsFactory](create-key-settings-factory.md): [CreateKeySettingsFactory](../../-create-key-settings-factory/index.md)?<br>factory to create CreateKeySettings for document keys. By default, this is set to [DefaultSecureArea.CreateKeySettingsFactory](../../../eu.europa.ec.eudi.wallet.document.defaults/-default-secure-area/-create-key-settings-factory/index.md). |
| [keyUnlockDataFactory](key-unlock-data-factory.md)              | [androidJvm]<br>var [keyUnlockDataFactory](key-unlock-data-factory.md): [KeyUnlockDataFactory](../../-key-unlock-data-factory/index.md)?                                                                                                                                                                                                                                                                    |
| [secureArea](secure-area.md)                                    | [androidJvm]<br>var [secureArea](secure-area.md): SecureArea?<br>secure area used to store documents' keys. By default, this is set to [DefaultSecureArea](../../../eu.europa.ec.eudi.wallet.document.defaults/-default-secure-area/index.md).                                                                                                                                                              |
| [storageEngine](storage-engine.md)                              | [androidJvm]<br>var [storageEngine](storage-engine.md): StorageEngine?<br>storage engine used to store documents. By default, this is set to [DefaultStorageEngine](../../../eu.europa.ec.eudi.wallet.document.defaults/-default-storage-engine/index.md).                                                                                                                                                  |

## Functions

| Name                                                            | Summary                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                     |
|-----------------------------------------------------------------|---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| [build](build.md)                                               | [androidJvm]<br>fun [build](build.md)(): [DocumentManager](../index.md)<br>Build the DocumentManager                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                        |
| [checkPublicKeyBeforeAdding](check-public-key-before-adding.md) | [androidJvm]<br>fun [checkPublicKeyBeforeAdding](check-public-key-before-adding.md)(checkPublicKeyBeforeAdding: [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)): [DocumentManager.Builder](index.md)<br>Sets whether to check public key in MSO before adding document to storage. By default, this is set to true. This check is done to prevent adding documents with public key that is not in MSO. The public key from the [UnsignedDocument](../../-unsigned-document/index.md) must match the public key in MSO.                                                                                                                                  |
| [createKeySettingsFactory](create-key-settings-factory.md)      | [androidJvm]<br>fun [createKeySettingsFactory](create-key-settings-factory.md)(createKeySettingsFactory: [CreateKeySettingsFactory](../../-create-key-settings-factory/index.md)): [DocumentManager.Builder](index.md)<br>Sets the factory to create CreateKeySettings for document keys. By default, this is set to [DefaultSecureArea.CreateKeySettingsFactory](../../../eu.europa.ec.eudi.wallet.document.defaults/-default-secure-area/-create-key-settings-factory/index.md). This factory is used to create CreateKeySettings for the keys that are created in the secure area. The CreateKeySettings can be used to set the key's alias, key's purpose, key's protection level, etc. |
| [keyUnlockDataFactory](key-unlock-data-factory.md)              | [androidJvm]<br>fun [keyUnlockDataFactory](key-unlock-data-factory.md)(keyUnlockDataFactory: [KeyUnlockDataFactory](../../-key-unlock-data-factory/index.md)): [DocumentManager.Builder](index.md)<br>Sets the factory to create KeyUnlockData for document keys. By default, this is set to [DefaultSecureArea.KeyUnlockDataFactory](../../../eu.europa.ec.eudi.wallet.document.defaults/-default-secure-area/-companion/-key-unlock-data-factory.md). This factory is used to create KeyUnlockData that is used to unlock the keys in the secure area.                                                                                                                                    |
| [secureArea](secure-area.md)                                    | [androidJvm]<br>fun [secureArea](secure-area.md)(secureArea: SecureArea): [DocumentManager.Builder](index.md)<br>Sets the secure area that manages the keys for the documents. By default, this is set to [DefaultSecureArea](../../../eu.europa.ec.eudi.wallet.document.defaults/-default-secure-area/index.md).                                                                                                                                                                                                                                                                                                                                                                           |
| [storageEngine](storage-engine.md)                              | [androidJvm]<br>fun [storageEngine](storage-engine.md)(storageEngine: StorageEngine): [DocumentManager.Builder](index.md)<br>Sets the storage engine to store the documents. By default, this is set to [DefaultStorageEngine](../../../eu.europa.ec.eudi.wallet.document.defaults/-default-storage-engine/index.md). The storage engine is used to store the documents in the device's storage.                                                                                                                                                                                                                                                                                            |
