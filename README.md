# EUDI Wallet Documents Manager library for Android

:heavy_exclamation_mark: **Important!** Before you proceed, please read
the [EUDI Wallet Reference Implementation project description](https://github.com/eu-digital-identity-wallet/.github-private/blob/main/profile/reference-implementation.md)

## Overview

This library provides a set of classes to manage documents in an EUDI Android Wallet.

It defines the interfaces for DocumentManager and Document classes and provides a standard
implementation
of the DocumentManager interface using Android Identity Credential API.

It also provides a sample implementation of the DocumentManager interface that can be used to load
sample documents and test the library.

The library is written in Kotlin and is available for Android.

## :heavy_exclamation_mark: Disclaimer

The released software is a initial development release version: 
-  The initial development release is an early endeavor reflecting the efforts of a short timeboxed period, and by no means can be considered as the final product.  
-  The initial development release may be changed substantially over time, might introduce new features but also may change or remove existing ones, potentially breaking compatibility with your existing code.
-  The initial development release is limited in functional scope.
-  The initial development release may contain errors or design flaws and other problems that could cause system or other failures and data loss.
-  The initial development release has reduced security, privacy, availability, and reliability standards relative to future releases. This could make the software slower, less reliable, or more vulnerable to attacks than mature software.
-  The initial development release is not yet comprehensively documented. 
-  Users of the software must perform sufficient engineering and additional testing in order to properly evaluate their application and determine whether any of the open-sourced components is suitable for use in that application.
-  We strongly recommend not putting this version of the software into production use.
-  Only the latest version of the software will be supported

## Requirements

- Android 8 (API level 26) or higher

### Dependencies

To include the library in your project, add the following dependencies to your app's build.gradle
file.

```groovy
dependencies {
    implementation "eu.europa.ec.eudi:eudi-lib-android-wallet-document-manager:0.1.0-SNAPSHOT"
}
```

## How to Use

Below is a quick overview of how to use the library.

For source code documentation, see in [docs](docs/index.md) directory.

### Instantiating the DocumentManager

The library provides a `DocumentManager` class implementation to manage documents.
To create an instance of
the [DocumentManager](docs/document-manager/eu.europa.ec.eudi.wallet.document/-document-manager/index.md),
library provides
a [Builder](docs/document-manager/eu.europa.ec.eudi.wallet.document/-document-manager/-builder/index.md),
that can be used to get a default implementation of the `DocumentManager`.

```kotlin
import eu.europa.ec.eudi.wallet.document.DocumentManager

val documentManager = DocumentManager.Builder(context)
    .useEncryption(true)
    .storageDir(context.noBackupFilesDir)
    .enableUserAuth(true)
    .userAuthTimeout(30000)
    .build()
```

### Managing documents

Document is an object that contains the following information:

- `id` document's unique identifier
- `docType` document's docType (example: "eu.europa.ec.eudiw.pid.1")
- `name` document's name. This is a human readable name.
- `hardwareBacked` document's storage is hardware backed
- `createdAt` document's creation date
- `requiresUserAuth` flag that indicates if the document requires user authentication to be accessed
- `nameSpacedData` retrieves the document's data, grouped by nameSpace. Values are in CBOR bytes

To retrieve the list of documents, use the `getDocuments` method:

```kotlin
import eu.europa.ec.eudi.wallet.document.Document

val documents: List<Document> = documentManager.getDocuments()
```

To retrieve a document by its id, use the `getDocumentById` method:

```kotlin
import eu.europa.ec.eudi.wallet.document.Document
import eu.europa.ec.eudi.wallet.document.DocumentId

val documentId = "some document id"
val document: Document = documentManager.getDocumentById(documentId)
```

DocumentManager also provides the `deleteDocumentById` method to delete a document by its id:

```kotlin
import eu.europa.ec.eudi.wallet.document.Document
import eu.europa.ec.eudi.wallet.document.DocumentId
import eu.europa.ec.eudi.wallet.document.DeleteDocumentResult

val documentId = "some document id"
val deleteResult: DeleteDocumentResult = documentManager.deleteDocumentById(documentId)

when (deleteResult) {
    is DeleteDocumentResult.Success -> {
        // document deleted successfully
        val proofOfDeletion = deleteResult.proofOfDeletion
    }
    is DeleteDocumentResult.Failure -> {
        // handle error while deleting document
    }
}

```

In order to add a new document in `DocumentManager`, the following steps should be followed:

1. Create an issuance request using the `createIssuanceRequest` method of the `DocumentManager`
   class.
2. Send the issuance request to the issuer.
3. Add the document to the `DocumentManager` using the `addDocument` method.

In order to use with the `addDocument` method, document's data must be in CBOR bytes that has the following structure:

```cddl
Data = {
 "documents" : [+Document], ; Returned documents
}
Document = {
 "docType" : DocType, ; Document type returned
 "issuerSigned" : IssuerSigned, ; Returned data elements signed by the issuer
}
IssuerSigned = {
 "nameSpaces" : IssuerNameSpaces, ; Returned data elements
 "issuerAuth" : IssuerAuth ; Contains the mobile security object (MSO) for issuer data authentication
}
IssuerNameSpaces = { ; Returned data elements for each namespace
 + NameSpace => [ + IssuerSignedItemBytes ]
}
IssuerSignedItemBytes = #6.24(bstr .cbor IssuerSignedItem)
IssuerSignedItem = {
 "digestID" : uint, ; Digest ID for issuer data authentication
 "random" : bstr, ; Random value for issuer data authentication
 "elementIdentifier" : DataElementIdentifier, ; Data element identifier
 "elementValue" : DataElementValue ; Data element value
}
IssuerAuth = COSE_Sign1 ; The payload is MobileSecurityObjectBytes
```

See the code below for an example of how to add a new document in `DocumentManager`:

```kotlin
import eu.europa.ec.eudi.wallet.document.AddDocumentResult
import eu.europa.ec.eudi.wallet.document.CreateIssuanceRequestResult
import eu.europa.ec.eudi.wallet.document.sample.SampleDocumentManager

val docType = "eu.europa.ec.eudiw.pid.1"
val hardwareBacked = false
val attestationChallenge = byteArrayOf(
    // attestation challenge bytes
    // provided by the issuer
)
val requestResult =
    documentManager.createIssuanceRequest(docType, hardwareBacked, attestationChallenge)
when (requestResult) {
    is CreateIssuanceRequestResult.Failure -> {
        val error = requestResult.throwable
        // handle error while creating issuance request
    }
    is CreateIssuanceRequestResult.Success -> {
        val request = requestResult.issuanceRequest
        val docType = request.docType
        // the device certificate that will be used in the signing of the document
        // from the issuer while creating the MSO (Mobile Security Object)
        val certificateNeedAuth = request.certificateNeedAuth

        // ... code that sends docType and certificates to issuer

        // after receiving the MSO from the issuer, the user can start the issuance process
        val issuerData: ByteArray = byteArrayOf(
            // CBOR bytes of the document
        )

        val addResult = documentManager.addDocument(request, issuerData)

        when (addResult) {
            is AddDocumentResult.Failure -> {
                val error = addResult.throwable
                // handle error while adding document
            }
            is AddDocumentResult.Success -> {
                val documentId = addResult.documentId
                // the documentId of the newly added document
                // use the documentId to retrieve the document
                documentManager.getDocumentById(documentId)
            }
        }
    }
}
```

### Working with sample documents

The library, also provides a `SampleDocumentManager` class implementation that can be used to load
sample documents and test the library easily.

To create a new instance of the `SampleDocumentManager` class, use
the `SampleDocumentManager.Builder` class:

```kotlin
import eu.europa.ec.eudi.wallet.document.sample.SampleDocumentManager
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds

val sampleDocumentManager = SampleDocumentManager.Builder(context)
    .documentManager(documentManager) // optional if a DocumentManager instance is already created, else a default DocumentManager instance will be created
    .hardwareBacked(false) // documents' keys should be stored in hardware backed keystore if supported by the device. The default value is true if device supports hardware backed keystore, else false
    .build()
```

After creating an instance of `SampleDocumentManager`, you can load the sample documents using
the `loadSampleData` method like shown below:

```kotlin
import android.util.Base64
import kotlinx.coroutines.runBlocking

// Assuming that the sample data is stored in a file named sample_data.json in the raw resources directory
// in base64 encoded format and context is an instance of android.content.Context
val sampleDocumentsByteArray = context.resources.openRawResource(R.raw.sample_data).use {
    val data = String(it.readBytes())
    Base64.decode(data, Base64.DEFAULT)
}
documentManager.loadSampleData(sampleDocumentsByteArray)
```

Sample documents must be in CBOR format with the following structure:

```cddl
Data = {
 "documents" : [+Document], ; Returned documents
}
Document = {
 "docType" : DocType, ; Document type returned
 "issuerSigned" : IssuerSigned, ; Returned data elements signed by the issuer
}
IssuerSigned = {
 "nameSpaces" : IssuerNameSpaces, ; Returned data elements
}
IssuerNameSpaces = { ; Returned data elements for each namespace
 + NameSpace => [ + IssuerSignedItemBytes ]
}
IssuerSignedItem = {
 "digestID" : uint, ; Digest ID for issuer data authentication
 "random" : bstr, ; Random value for issuer data authentication
 "elementIdentifier" : DataElementIdentifier, ; Data element identifier
 "elementValue" : DataElementValue ; Data element value
}
```

## How to contribute

We welcome contributions to this project. To ensure that the process is smooth for everyone
involved, follow the guidelines found in [CONTRIBUTING.md](CONTRIBUTING.md).

## License

### Third-party component licenses

See [licenses.md](licenses.md) for details.

### License details

Copyright (c) 2023 European Commission

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
