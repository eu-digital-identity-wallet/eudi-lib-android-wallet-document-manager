//[document-manager](../../index.md)/[eu.europa.ec.eudi.wallet.document.sample](index.md)

# Package-level declarations

## Types

| Name | Summary |
|---|---|
| [LoadSampleResult](-load-sample-result/index.md) | [androidJvm]<br>interface [LoadSampleResult](-load-sample-result/index.md)<br>[SampleDocumentManager.loadSampleData](-sample-document-manager/load-sample-data.md) result. If the sample data has been loaded successfully, returns [LoadSampleResult.Success](-load-sample-result/-success/index.md). Otherwise, returns [LoadSampleResult.Error](-load-sample-result/-error/index.md), with the error message. |
| [SampleDocumentManager](-sample-document-manager/index.md) | [androidJvm]<br>interface [SampleDocumentManager](-sample-document-manager/index.md) : [DocumentManager](../eu.europa.ec.eudi.wallet.document/-document-manager/index.md)<br>An extension of [DocumentManager](../eu.europa.ec.eudi.wallet.document/-document-manager/index.md) that provides methods to load sample data. |
| [SampleDocumentManagerImpl](-sample-document-manager-impl/index.md) | [androidJvm]<br>class [SampleDocumentManagerImpl](-sample-document-manager-impl/index.md)(context: [Context](https://developer.android.com/reference/kotlin/android/content/Context.html), documentManager: [DocumentManager](../eu.europa.ec.eudi.wallet.document/-document-manager/index.md)) : [DocumentManager](../eu.europa.ec.eudi.wallet.document/-document-manager/index.md), [SampleDocumentManager](-sample-document-manager/index.md)<br>A [SampleDocumentManager](-sample-document-manager/index.md) implementation that composes a [DocumentManager](../eu.europa.ec.eudi.wallet.document/-document-manager/index.md) and provides methods to load sample data. |
