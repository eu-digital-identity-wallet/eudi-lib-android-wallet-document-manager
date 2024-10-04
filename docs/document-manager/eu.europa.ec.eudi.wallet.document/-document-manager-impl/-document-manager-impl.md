//[document-manager](../../../index.md)/[eu.europa.ec.eudi.wallet.document](../index.md)/[DocumentManagerImpl](index.md)/[DocumentManagerImpl](-document-manager-impl.md)

# DocumentManagerImpl

[androidJvm]\
constructor(storageEngine: StorageEngine, secureArea: SecureArea,
createKeySettingsFactory: [CreateKeySettingsFactory](../-create-key-settings-factory/index.md),
keyUnlockDataFactory: [KeyUnlockDataFactory](../-key-unlock-data-factory/index.md),
checkPublicKeyBeforeAdding: [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) =
true)

#### Parameters

androidJvm

|                            |                                                                                                                                  |
|----------------------------|----------------------------------------------------------------------------------------------------------------------------------|
| storageEngine              | storage engine used to store documents                                                                                           |
| secureArea                 | secure area used to store documents' keys                                                                                        |
| createKeySettingsFactory   | factory to create CreateKeySettings for document keys                                                                            |
| checkPublicKeyBeforeAdding | flag that indicates if the public key in the [UnsignedDocument](../-unsigned-document/index.md) must match the public key in MSO |
