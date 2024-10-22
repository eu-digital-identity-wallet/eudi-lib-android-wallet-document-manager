/*
 * Copyright (c) 2024 European Commission
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

// How to instantiate storageEngine and secureArea with
// implementations from the identity-credential library
import com.android.identity.securearea.software.SoftwareCreateKeySettings
import com.android.identity.securearea.software.SoftwareKeyUnlockData
import com.android.identity.securearea.software.SoftwareSecureArea
import com.android.identity.storage.EphemeralStorageEngine
import eu.europa.ec.eudi.wallet.document.Document
import eu.europa.ec.eudi.wallet.document.DocumentId
import eu.europa.ec.eudi.wallet.document.DocumentManager
import eu.europa.ec.eudi.wallet.document.format.MsoMdocFormat
import eu.europa.ec.eudi.wallet.document.sample.SampleDocumentManager

val storageEngine = EphemeralStorageEngine()
val secureArea = SoftwareSecureArea(storageEngine)

// Instantiate the DocumentManager using the DocumentManager.Builder
val builder = DocumentManager.Builder()
    .setStorageEngine(storageEngine)
    .setSecureArea(secureArea)

val documentManager = builder.build()
fun readFileWithSampleData(): ByteArray = TODO()
fun deleteDocument(document: Document) {
    try {
        val documentId = "some_document_id"
        val deleteResult = documentManager.deleteDocumentById(documentId)
        deleteResult.getOrThrow()
    } catch (e: Throwable) {
        // Handle the exception
    }
}

fun loadSampleData() {
    val sampleDocumentManager = SampleDocumentManager.Builder()
        .setDocumentManager(documentManager)
        .build()

    val sampleMdocDocuments: ByteArray = readFileWithSampleData()

    val createKeySettings = SoftwareCreateKeySettings.Builder().build()
    val loadResult = sampleDocumentManager.loadMdocSampleDocuments(
        sampleData = sampleMdocDocuments,
        createKeySettings = createKeySettings,
        documentNamesMap = mapOf(
            "eu.europa.ec.eudi.pid.1" to "EU PID",
            "org.iso.18013.5.1.mDL" to "mDL"
        )
    )
    val documentIds: List<DocumentId> = loadResult.getOrThrow()
}


fun sendToIssuer(publicKeyCoseBytes: ByteArray, signatureCoseBytes: ByteArray): ByteArray = TODO()

fun issueDocument() {
    try {
        // create a new document
        // Construct the createKeySettings that will be used to create the key
        // for the document. Here we use SoftwareCreateKeySettings as an example
        // provided by the identity-credential library
        val createKeySettings = SoftwareCreateKeySettings.Builder().build()
        val createDocumentResult = documentManager.createDocument(
            format = MsoMdocFormat(docType = "eu.europa.ec.eudi.pid.1"),
            createKeySettings = createKeySettings
        )
        val unsignedDocument = createDocumentResult.getOrThrow()
        val publicKeyBytes = unsignedDocument.publicKeyCoseBytes

        // prepare keyUnlockData to unlock the key
        // probably prompt the user to enter the passphrase
        // or use any other method to unlock the key
        // here we use SoftwareKeyUnlockData as an example
        // provided by the identity-credential library
        val keyUnlockData = SoftwareKeyUnlockData(
            passphrase = "passphrase required to unlock the key"
        )
        // proof of key possession
        // Sign the documents public key with the private key
        // before sending it to the issuer
        val signatureResult =
            unsignedDocument.sign(publicKeyBytes, keyUnlockData = keyUnlockData)
        val signature = signatureResult.getOrThrow().toCoseEncoded()

        // send the public key and the signature to the issuer
        // and get the document data
        val documentData = sendToIssuer(
            publicKeyCoseBytes = publicKeyBytes,
            signatureCoseBytes = signature
        )

        // store the issued document with the document data received from the issuer
        val storeResult =
            documentManager.storeIssuedDocument(unsignedDocument, documentData)

        // get the issued document
        val issuedDocument = storeResult.getOrThrow()
    } catch (e: Throwable) {
        // Handle the exception
    }
}