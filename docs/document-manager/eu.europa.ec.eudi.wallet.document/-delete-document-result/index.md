//[document-manager](../../../index.md)/[eu.europa.ec.eudi.wallet.document](../index.md)/[DeleteDocumentResult](index.md)

# DeleteDocumentResult

interface [DeleteDocumentResult](index.md)

Delete document result sealed interface

#### Inheritors

| |
|---|
| [Success](-success/index.md) |
| [Failure](-failure/index.md) |

## Types

| Name | Summary |
|---|---|
| [Failure](-failure/index.md) | [androidJvm]<br>data class [Failure](-failure/index.md)(val throwable: [Throwable](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-throwable/index.html)) : [DeleteDocumentResult](index.md)<br>Failure while deleting the document. Contains the throwable that caused the failure |
| [Success](-success/index.md) | [androidJvm]<br>data class [Success](-success/index.md)(val proofOfDeletion: [ByteArray](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-byte-array/index.html)?) : [DeleteDocumentResult](index.md)<br>Success result containing the proof of deletion |
