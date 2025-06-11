//[document-manager](../../../index.md)/[eu.europa.ec.eudi.wallet.document](../index.md)/[UnsignedDocument](index.md)/[credentialsCount](credentials-count.md)

# credentialsCount

[androidJvm]\
open suspend override
fun [credentialsCount](credentials-count.md)(): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html)

Returns the number of valid credentials associated with this document.

For UnsignedDocument, this represents the number of proof-of-possession signers that can be created from the document's pending credentials. Only credentials that are not invalidated and belong to the current document manager are counted.

#### Return

The number of valid credentials available for this document

#### See also

| |
|---|
| [UnsignedDocument.getPoPSigners](get-po-p-signers.md) |
