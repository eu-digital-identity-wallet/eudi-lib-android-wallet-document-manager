//[document-manager](../../../index.md)/[eu.europa.ec.eudi.wallet.document.sample](../index.md)/[LoadSampleResult](index.md)

# LoadSampleResult

interface [LoadSampleResult](index.md)

[SampleDocumentManager.loadSampleData](../-sample-document-manager/load-sample-data.md) result. If the sample data has been loaded successfully, returns [LoadSampleResult.Success](-success/index.md). Otherwise, returns [LoadSampleResult.Error](-error/index.md), with the error message.

#### See also

| |
|---|
| [SampleDocumentManager.loadSampleData](../-sample-document-manager/load-sample-data.md) |

#### Inheritors

| |
|---|
| [Success](-success/index.md) |
| [Error](-error/index.md) |

## Types

| Name | Summary |
|---|---|
| [Error](-error/index.md) | [androidJvm]<br>data class [Error](-error/index.md)(val throwable: [Throwable](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-throwable/index.html)) : [LoadSampleResult](index.md)<br>Error class to return the error message. |
| [Success](-success/index.md) | [androidJvm]<br>object [Success](-success/index.md) : [LoadSampleResult](index.md)<br>Success object to return when the sample data has been loaded successfully. |
