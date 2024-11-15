//[document-manager](../../../index.md)/[eu.europa.ec.eudi.wallet.document](../index.md)/[CreateDocumentSettings](index.md)

# CreateDocumentSettings

interface [CreateDocumentSettings](index.md)

Interface that defines the required settings when creating a document
with [DocumentManager.createDocument](../-document-manager/create-document.md). Implementors
of [DocumentManager](../-document-manager/index.md) may introduce custom requirements for creating a
document.

#### See also

|                                                                          |                |
|--------------------------------------------------------------------------|----------------|
| [CreateDocumentSettingsImpl](../-create-document-settings-impl/index.md) | implementation |

#### Inheritors

|                                                                          |
|--------------------------------------------------------------------------|
| [CreateDocumentSettingsImpl](../-create-document-settings-impl/index.md) |

## Types

| Name                             | Summary                                                 |
|----------------------------------|---------------------------------------------------------|
| [Companion](-companion/index.md) | [androidJvm]<br>object [Companion](-companion/index.md) |

## Properties

| Name                                              | Summary                                                                                                                                                          |
|---------------------------------------------------|------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| [createKeySettings](create-key-settings.md)       | [androidJvm]<br>abstract val [createKeySettings](create-key-settings.md): CreateKeySettings                                                                      |
| [secureAreaIdentifier](secure-area-identifier.md) | [androidJvm]<br>abstract val [secureAreaIdentifier](secure-area-identifier.md): [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html) |
