//[document-manager](../../../../index.md)/[eu.europa.ec.eudi.wallet.document.credential](../../index.md)/[CredentialFactory](../index.md)/[Companion](index.md)/[invoke](invoke.md)

# invoke

[androidJvm]\
operator fun [invoke](invoke.md)(domain: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-string/index.html), format: [DocumentFormat](../../../eu.europa.ec.eudi.wallet.document.format/-document-format/index.md)): [CredentialFactory](../index.md)

Creates an appropriate credential factory implementation based on the document format.

#### Return

a credential factory implementation specific to the document format

#### Parameters

androidJvm

| | |
|---|---|
| domain | the domain for the credentials |
| format | the document format that determines which factory implementation to use |
