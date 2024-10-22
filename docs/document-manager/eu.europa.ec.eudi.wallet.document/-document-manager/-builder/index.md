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

| Name                               | Summary                                                                                                                              |
|------------------------------------|--------------------------------------------------------------------------------------------------------------------------------------|
| [secureArea](secure-area.md)       | [androidJvm]<br>var [secureArea](secure-area.md): SecureArea?<br>the secure area to use for managing the keys                        |
| [storageEngine](storage-engine.md) | [androidJvm]<br>var [storageEngine](storage-engine.md): StorageEngine?<br>the storage engine to use for storing/retrieving documents |

## Functions

| Name                                      | Summary                                                                                                                                                                                             |
|-------------------------------------------|-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| [build](build.md)                         | [androidJvm]<br>fun [build](build.md)(): [DocumentManager](../index.md)<br>Build a [DocumentManager](../index.md) instance.                                                                         |
| [setSecureArea](set-secure-area.md)       | [androidJvm]<br>fun [setSecureArea](set-secure-area.md)(secureArea: SecureArea): [DocumentManager.Builder](index.md)<br>Set the secure area to use for managing the keys.                           |
| [setStorageEngine](set-storage-engine.md) | [androidJvm]<br>fun [setStorageEngine](set-storage-engine.md)(storageEngine: StorageEngine): [DocumentManager.Builder](index.md)<br>Set the storage engine to use for storing/retrieving documents. |
