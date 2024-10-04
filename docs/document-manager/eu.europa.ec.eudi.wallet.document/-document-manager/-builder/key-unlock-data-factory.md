//[document-manager](../../../../index.md)/[eu.europa.ec.eudi.wallet.document](../../index.md)/[DocumentManager](../index.md)/[Builder](index.md)/[keyUnlockDataFactory](key-unlock-data-factory.md)

# keyUnlockDataFactory

[androidJvm]\
fun [keyUnlockDataFactory](key-unlock-data-factory.md)(
keyUnlockDataFactory: [KeyUnlockDataFactory](../../-key-unlock-data-factory/index.md)): [DocumentManager.Builder](index.md)

Sets the factory to create KeyUnlockData for document keys. By default, this is set
to [DefaultSecureArea.KeyUnlockDataFactory](../../../eu.europa.ec.eudi.wallet.document.defaults/-default-secure-area/-companion/-key-unlock-data-factory.md).
This factory is used to create KeyUnlockData that is used to unlock the keys in the secure area.

#### Return

#### Parameters

androidJvm

|                      |
|----------------------|
| keyUnlockDataFactory |

#### See also

|               |
|---------------|
| KeyUnlockData |

[androidJvm]\
var [keyUnlockDataFactory](key-unlock-data-factory.md): [KeyUnlockDataFactory](../../-key-unlock-data-factory/index.md)?
