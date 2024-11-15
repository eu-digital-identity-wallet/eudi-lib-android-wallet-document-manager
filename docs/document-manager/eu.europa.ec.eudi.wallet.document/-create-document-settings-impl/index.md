//[document-manager](../../../index.md)/[eu.europa.ec.eudi.wallet.document](../index.md)/[CreateDocumentSettingsImpl](index.md)

# CreateDocumentSettingsImpl

[androidJvm]\
data class [CreateDocumentSettingsImpl](index.md)(val
secureAreaIdentifier: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html),
val createKeySettings:
CreateKeySettings) : [CreateDocumentSettings](../-create-document-settings/index.md)

Implementation of [CreateDocumentSettings](../-create-document-settings/index.md)

## Constructors

|                                                                 |                                                                                                                                                                           |
|-----------------------------------------------------------------|---------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| [CreateDocumentSettingsImpl](-create-document-settings-impl.md) | [androidJvm]<br>constructor(secureAreaIdentifier: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html), createKeySettings: CreateKeySettings) |

## Properties

| Name                                              | Summary                                                                                                                                                                                                                                        |
|---------------------------------------------------|------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| [createKeySettings](create-key-settings.md)       | [androidJvm]<br>open override val [createKeySettings](create-key-settings.md): CreateKeySettings<br>the create key settings that accompanies the provided secure area                                                                          |
| [secureAreaIdentifier](secure-area-identifier.md) | [androidJvm]<br>open override val [secureAreaIdentifier](secure-area-identifier.md): [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)<br>the secure area identifier where the document's keys should be stored |
