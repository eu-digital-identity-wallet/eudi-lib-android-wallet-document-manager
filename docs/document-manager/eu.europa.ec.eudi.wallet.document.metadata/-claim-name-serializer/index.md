//[document-manager](../../../index.md)/[eu.europa.ec.eudi.wallet.document.metadata](../index.md)/[ClaimNameSerializer](index.md)

# ClaimNameSerializer

[androidJvm]\
object [ClaimNameSerializer](index.md) :
KSerializer&lt;[DocumentMetaData.ClaimName](../-document-meta-data/-claim-name/index.md)&gt;

## Properties

| Name                        | Summary                                                                         |
|-----------------------------|---------------------------------------------------------------------------------|
| [descriptor](descriptor.md) | [androidJvm]<br>open override val [descriptor](descriptor.md): SerialDescriptor |

## Functions

| Name                          | Summary                                                                                                                                                         |
|-------------------------------|-----------------------------------------------------------------------------------------------------------------------------------------------------------------|
| [deserialize](deserialize.md) | [androidJvm]<br>open override fun [deserialize](deserialize.md)(decoder: Decoder): [DocumentMetaData.ClaimName](../-document-meta-data/-claim-name/index.md)    |
| [serialize](serialize.md)     | [androidJvm]<br>open override fun [serialize](serialize.md)(encoder: Encoder, value: [DocumentMetaData.ClaimName](../-document-meta-data/-claim-name/index.md)) |
