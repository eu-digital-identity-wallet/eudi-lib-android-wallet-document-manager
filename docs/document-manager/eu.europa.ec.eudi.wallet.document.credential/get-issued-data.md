//[document-manager](../../index.md)/[eu.europa.ec.eudi.wallet.document.credential](index.md)/[getIssuedData](get-issued-data.md)

# getIssuedData

[androidJvm]\
inline fun &lt;[D](get-issued-data.md) : [CredentialIssuedData](-credential-issued-data/index.md)
&gt;
SecureAreaBoundCredential.[getIssuedData](get-issued-data.md)(): [Result](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-result/index.html)
&lt;[D](get-issued-data.md)&gt;

Extension function to extract issuer provided data from a SecureAreaBoundCredential.

This function processes different types of secure area bound credentials and extracts their corresponding [CredentialIssuedData](-credential-issued-data/index.md) representation.

#### Return

A [Result](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-result/index.html)
containing the extracted data of type [D](get-issued-data.md) or a failure if the credential type is
unsupported or if the conversion to the requested type fails

#### Parameters

androidJvm

| | |
|---|---|
| D | The specific type of [CredentialIssuedData](-credential-issued-data/index.md) expected to be returned |

#### Throws

|                                                                                                                    |                                         |
|--------------------------------------------------------------------------------------------------------------------|-----------------------------------------|
| [IllegalArgumentException](https://developer.android.com/reference/kotlin/java/lang/IllegalArgumentException.html) | if the credential type is not supported |
