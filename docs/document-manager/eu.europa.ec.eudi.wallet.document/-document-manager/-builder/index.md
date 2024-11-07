//[document-manager](../../../../index.md)/[eu.europa.ec.eudi.wallet.document](../../index.md)/[DocumentManager](../index.md)/[Builder](index.md)

# Builder

[androidJvm]\
class [Builder](index.md)

Builder class to create a [DocumentManager](../index.md) instance.

## Constructors

|                        |                               |
|------------------------|-------------------------------|
| [Builder](-builder.md) | [androidJvm]<br>constructor() |

## Properties

| Name                                              | Summary                                                                                                                                                                      |
|---------------------------------------------------|------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| [identifier](identifier.md)                       | [androidJvm]<br>var [identifier](identifier.md): [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)?<br>the identifier of the document manager |
| [secureAreaRepository](secure-area-repository.md) | [androidJvm]<br>var [secureAreaRepository](secure-area-repository.md): SecureAreaRepository                                                                                  |
| [storageEngine](storage-engine.md)                | [androidJvm]<br>var [storageEngine](storage-engine.md): StorageEngine?<br>the storage engine to use for storing/retrieving documents                                         |

## Functions

| Name                                                     | Summary                                                                                                                                                                                                                                                                          |
|----------------------------------------------------------|----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| [addSecureArea](add-secure-area.md)                      | [androidJvm]<br>fun [addSecureArea](add-secure-area.md)(secureArea: SecureArea): [DocumentManager.Builder](index.md)<br>Adds a SecureArea implementation to [secureAreaRepository](secure-area-repository.md)                                                                    |
| [build](build.md)                                        | [androidJvm]<br>fun [build](build.md)(): [DocumentManager](../index.md)<br>Build a [DocumentManager](../index.md) instance.                                                                                                                                                      |
| [setIdentifier](set-identifier.md)                       | [androidJvm]<br>fun [setIdentifier](set-identifier.md)(identifier: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)): [DocumentManager.Builder](index.md)<br>Set the identifier of the document manager.                                         |
| [setSecureAreaRepository](set-secure-area-repository.md) | [androidJvm]<br>fun [setSecureAreaRepository](set-secure-area-repository.md)(secureAreaRepository: SecureAreaRepository): [DocumentManager.Builder](index.md)<br>Sets the [secureAreaRepository](set-secure-area-repository.md) that will be used for documents' keys management |
| [setStorageEngine](set-storage-engine.md)                | [androidJvm]<br>fun [setStorageEngine](set-storage-engine.md)(storageEngine: StorageEngine): [DocumentManager.Builder](index.md)<br>Set the storage engine to use for storing/retrieving documents.                                                                              |
