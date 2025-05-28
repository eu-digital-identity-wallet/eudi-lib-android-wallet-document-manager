//[document-manager](../../../index.md)/[eu.europa.ec.eudi.wallet.document](../index.md)/[IssuedDocument](index.md)/[credentialsCount](credentials-count.md)

# credentialsCount

[androidJvm]\
open suspend override fun [credentialsCount](credentials-count.md)(): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html)

Returns the number of valid credentials associated with this document.

For UnsignedDocument, this counts credentials that can be used for proof of possession. For IssuedDocument, this counts valid credentials according to the credential policy.

#### Return

The number of valid credentials available for this document
