//[document-manager](../../../../index.md)/[eu.europa.ec.eudi.wallet.document](../../index.md)/[CreateDocumentSettings](../index.md)/[Companion](index.md)/[invoke](invoke.md)

# invoke

[androidJvm]\
operator fun [invoke](invoke.md)(secureAreaIdentifier: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html), createKeySettings: CreateKeySettings): [CreateDocumentSettings](../index.md)

Create a new instance of [CreateDocumentSettings](../index.md) for [DocumentManagerImpl.createDocument](../../-document-manager-impl/create-document.md) that uses the com.android.identity.securearea.SecureArea.

#### Return

a new instance of [CreateDocumentSettings](../index.md)

#### Parameters

androidJvm

| | |
|---|---|
| secureAreaIdentifier | the com.android.identity.securearea.SecureArea.identifier where the document's keys should be stored |
| createKeySettings | the CreateKeySettings implementation that accompanies the provided com.android.identity.securearea.SecureArea |
