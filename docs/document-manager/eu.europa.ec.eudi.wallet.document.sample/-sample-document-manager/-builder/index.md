//[document-manager](../../../../index.md)/[eu.europa.ec.eudi.wallet.document.sample](../../index.md)/[SampleDocumentManager](../index.md)/[Builder](index.md)

# Builder

class [Builder](index.md)(context: [Context](https://developer.android.com/reference/kotlin/android/content/Context.html))

Builder class to instantiate a SampleDocumentManager.

#### Parameters

androidJvm

| |
|---|
| context |

## Constructors

| | |
|---|---|
| [Builder](-builder.md) | [androidJvm]<br>constructor(context: [Context](https://developer.android.com/reference/kotlin/android/content/Context.html)) |

## Functions

| Name | Summary |
|---|---|
| [build](build.md) | [androidJvm]<br>fun [build](build.md)(): [SampleDocumentManager](../index.md)<br>Builds the SampleDocumentManager. |
| [hardwareBacked](hardware-backed.md) | [androidJvm]<br>fun [hardwareBacked](hardware-backed.md)(flag: [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)): [SampleDocumentManager.Builder](index.md)<br>Sets the flag to indicate that the documents' keys should be stored in hardware backed keystore if supported by the device. |

## Properties

| Name | Summary |
|---|---|
| [documentManager](document-manager.md) | [androidJvm]<br>var [documentManager](document-manager.md): [DocumentManager](../../../eu.europa.ec.eudi.wallet.document/-document-manager/index.md) |
| [hardwareBacked](hardware-backed.md) | [androidJvm]<br>var [hardwareBacked](hardware-backed.md): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)<br>Flag to indicate that the documents' keys should be stored in hardware backed keystore if supported by the device. |
