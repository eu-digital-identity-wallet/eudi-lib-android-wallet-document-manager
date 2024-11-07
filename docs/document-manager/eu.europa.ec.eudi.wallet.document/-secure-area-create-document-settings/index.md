//[document-manager](../../../index.md)/[eu.europa.ec.eudi.wallet.document](../index.md)/[SecureAreaCreateDocumentSettings](index.md)

# SecureAreaCreateDocumentSettings

[androidJvm]\
data class [SecureAreaCreateDocumentSettings](index.md)(val
secureAreaIdentifier: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html),
val keySettings:
CreateKeySettings) : [CreateDocumentSettings](../-create-document-settings/index.md)

[CreateDocumentSettings](../-create-document-settings/index.md) implementation
for [DocumentManagerImpl](../-document-manager-impl/index.md) that uses the
com.android.identity.securearea.SecureArea.

## Constructors

|                                                                              |                                                                                                                                                                     |
|------------------------------------------------------------------------------|---------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| [SecureAreaCreateDocumentSettings](-secure-area-create-document-settings.md) | [androidJvm]<br>constructor(secureAreaIdentifier: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html), keySettings: CreateKeySettings) |

## Properties

| Name                                              | Summary                                                                                                                                                                                                                                                         |
|---------------------------------------------------|-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| [keySettings](key-settings.md)                    | [androidJvm]<br>val [keySettings](key-settings.md): CreateKeySettings<br>the CreateKeySettings implementation that accompanies the provided com.android.identity.securearea.SecureArea                                                                          |
| [secureAreaIdentifier](secure-area-identifier.md) | [androidJvm]<br>val [secureAreaIdentifier](secure-area-identifier.md): [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)<br>the com.android.identity.securearea.SecureArea.identifier where the document's keys should be stored |
