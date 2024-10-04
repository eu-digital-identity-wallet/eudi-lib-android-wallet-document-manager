//[document-manager](../../../../index.md)/[eu.europa.ec.eudi.wallet.document](../../index.md)/[DocumentManager](../index.md)/[Builder](index.md)/[createKeySettingsFactory](create-key-settings-factory.md)

# createKeySettingsFactory

[androidJvm]\
fun [createKeySettingsFactory](create-key-settings-factory.md)(
createKeySettingsFactory: [CreateKeySettingsFactory](../../-create-key-settings-factory/index.md)): [DocumentManager.Builder](index.md)

Sets the factory to create CreateKeySettings for document keys. By default, this is set
to [DefaultSecureArea.CreateKeySettingsFactory](../../../eu.europa.ec.eudi.wallet.document.defaults/-default-secure-area/-create-key-settings-factory/index.md).
This factory is used to create CreateKeySettings for the keys that are created in the secure area.
The CreateKeySettings can be used to set the key's alias, key's purpose, key's protection level,
etc.

[androidJvm]\
var [createKeySettingsFactory](create-key-settings-factory.md): [CreateKeySettingsFactory](../../-create-key-settings-factory/index.md)?
