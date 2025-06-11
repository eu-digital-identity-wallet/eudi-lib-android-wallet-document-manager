//[document-manager](../../../index.md)/[eu.europa.ec.eudi.wallet.document](../index.md)/[IssuedDocument](index.md)/[getCredentials](get-credentials.md)

# getCredentials

[androidJvm]\
suspend
fun [getCredentials](get-credentials.md)(): [List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin.collections/-list/index.html)
&lt;SecureAreaBoundCredential&gt;

Retrieves all valid credentials associated with this document.

This method filters the document's credentials based on several criteria:

- 
   Only certified credentials bound to a secure area
- 
   Only credentials that are not invalidated
- 
   Only credentials that belong to the current document manager
- 
   For OneTimeUse policy, only credentials that haven't been used (usageCount == 0)
- 
   For RotateUse policy, all valid credentials

#### Return

A list of valid SecureAreaBoundCredential objects
