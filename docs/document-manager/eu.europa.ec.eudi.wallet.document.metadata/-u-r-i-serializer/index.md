//[document-manager](../../../index.md)/[eu.europa.ec.eudi.wallet.document.metadata](../index.md)/[URISerializer](index.md)

# URISerializer

[androidJvm]\
object [URISerializer](index.md) :
KSerializer&lt;[URI](https://developer.android.com/reference/kotlin/java/net/URI.html)&gt;

## Properties

| Name                        | Summary                                                                         |
|-----------------------------|---------------------------------------------------------------------------------|
| [descriptor](descriptor.md) | [androidJvm]<br>open override val [descriptor](descriptor.md): SerialDescriptor |

## Functions

| Name                          | Summary                                                                                                                                                       |
|-------------------------------|---------------------------------------------------------------------------------------------------------------------------------------------------------------|
| [deserialize](deserialize.md) | [androidJvm]<br>open override fun [deserialize](deserialize.md)(decoder: Decoder): [URI](https://developer.android.com/reference/kotlin/java/net/URI.html)    |
| [serialize](serialize.md)     | [androidJvm]<br>open override fun [serialize](serialize.md)(encoder: Encoder, value: [URI](https://developer.android.com/reference/kotlin/java/net/URI.html)) |
