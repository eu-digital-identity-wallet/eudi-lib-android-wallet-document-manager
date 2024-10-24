//[document-manager](../../index.md)/[eu.europa.ec.eudi.wallet.document.format](index.md)

# Package-level declarations

## Types

| Name                                        | Summary                                                                                                                                                                                                                                                                                                                                         |
|---------------------------------------------|-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| [DocumentFormat](-document-format/index.md) | [androidJvm]<br>sealed interface [DocumentFormat](-document-format/index.md)<br>Represents a Document Format                                                                                                                                                                                                                                    |
| [MsoMdocFormat](-mso-mdoc-format/index.md)  | [androidJvm]<br>data class [MsoMdocFormat](-mso-mdoc-format/index.md)(val docType: [DocType](../eu.europa.ec.eudi.wallet.document/-doc-type/index.md)) : [DocumentFormat](-document-format/index.md)<br>Represents a MsoMdoc Format for a [eu.europa.ec.eudi.wallet.document.Document](../eu.europa.ec.eudi.wallet.document/-document/index.md) |
| [SdJwtVcFormat](-sd-jwt-vc-format/index.md) | [androidJvm]<br>data object [SdJwtVcFormat](-sd-jwt-vc-format/index.md) : [DocumentFormat](-document-format/index.md)<br>Represents a SdJwtVc Format for the [eu.europa.ec.eudi.wallet.document.Document](../eu.europa.ec.eudi.wallet.document/-document/index.md)                                                                              |
