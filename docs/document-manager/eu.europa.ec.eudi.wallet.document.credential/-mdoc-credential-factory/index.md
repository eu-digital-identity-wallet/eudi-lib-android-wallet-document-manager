//[document-manager](../../../index.md)/[eu.europa.ec.eudi.wallet.document.credential](../index.md)/[MdocCredentialFactory](index.md)

# MdocCredentialFactory

[androidJvm]\
class [MdocCredentialFactory](index.md)(val
domain: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-string/index.html)) : [CredentialFactory](../-credential-factory/index.md)

Implementation of CredentialFactory for creating ISO/IEC 18013-5 mobile driving license (mDL) credentials.

## Constructors

|                                                      |                                                                                                                                     |
|------------------------------------------------------|-------------------------------------------------------------------------------------------------------------------------------------|
| [MdocCredentialFactory](-mdoc-credential-factory.md) | [androidJvm]<br>constructor(domain: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-string/index.html)) |

## Properties

| Name                | Summary                                                                                                                                                                   |
|---------------------|---------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| [domain](domain.md) | [androidJvm]<br>val [domain](domain.md): [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-string/index.html)<br>the domain for the credentials |

## Functions

| Name                                       | Summary                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                      |
|--------------------------------------------|----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| [createCredentials](create-credentials.md) | [androidJvm]<br>open suspend override fun [createCredentials](create-credentials.md)(format: [DocumentFormat](../../eu.europa.ec.eudi.wallet.document.format/-document-format/index.md), document: Document, createDocumentSettings: [CreateDocumentSettings](../../eu.europa.ec.eudi.wallet.document/-create-document-settings/index.md), secureArea: SecureArea): [Pair](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-pair/index.html)&lt;[List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin.collections/-list/index.html)&lt;MdocCredential&gt;, JsonObject?&gt;<br>Creates mDL credentials for a document based on MSO mDOC format settings. |
