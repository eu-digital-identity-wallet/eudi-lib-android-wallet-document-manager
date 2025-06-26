//[document-manager](../../../index.md)/[eu.europa.ec.eudi.wallet.document](../index.md)/[IssuedDocument](index.md)/[getValidUntil](get-valid-until.md)

# getValidUntil

[androidJvm]\
suspend fun [getValidUntil](get-valid-until.md)(): &lt;Error class: unknown class&gt;

Retrieves the end date until which the document's credential is valid.

This method safely retrieves the validity end date from the document's current credential using the credential policy. Like other credential operations, this method applies the document's credential usage policy after accessing the credential.

#### Return

A [Result](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-result/index.html)
containing the validity end date as
an [Instant](https://developer.android.com/reference/kotlin/java/time/Instant.html) if successful,
or an exception if no valid credential is found
