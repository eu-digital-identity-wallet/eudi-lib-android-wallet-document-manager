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

package eu.europa.ec.eudi.wallet.document.internal

import COSE.Message
import COSE.MessageTag
import COSE.Sign1Message
import com.android.identity.mdoc.credential.MdocCredential
import com.android.identity.mdoc.mso.MobileSecurityObjectParser
import com.android.identity.mdoc.mso.StaticAuthDataGenerator
import com.android.identity.securearea.CreateKeySettings
import com.android.identity.securearea.SecureArea
import com.upokecenter.cbor.CBORObject
import eu.europa.ec.eudi.wallet.document.UnsignedDocument
import eu.europa.ec.eudi.wallet.document.format.MsoMdocFormat
import com.android.identity.document.Document as IdentityDocument

@JvmSynthetic
internal fun MsoMdocFormat.createCredential(
    domain: String,
    identityDocument: IdentityDocument,
    secureArea: SecureArea,
    createKeySettings: CreateKeySettings,
): MdocCredential {
    return MdocCredential(
        document = identityDocument,
        asReplacementFor = null,
        domain = domain,
        secureArea = secureArea,
        createKeySettings = createKeySettings,
        docType = docType
    )
}

@JvmSynthetic
internal fun MsoMdocFormat.storeIssuedDocument(
    unsignedDocument: UnsignedDocument,
    identityDocument: IdentityDocument,
    data: ByteArray,
    checkDevicePublicKey: Boolean
) {
    val issuerSigned = CBORObject.DecodeFromBytes(data)
    val issuerAuthBytes = issuerSigned["issuerAuth"].EncodeToBytes()
    val issuerAuth = Message.DecodeFromBytes(issuerAuthBytes, MessageTag.Sign1) as Sign1Message
    val msoBytes = issuerAuth.GetContent().getEmbeddedCBORObject().EncodeToBytes()
    val mso = MobileSecurityObjectParser(msoBytes).parse()
    if (mso.deviceKey != unsignedDocument.keyInfo.publicKey) {
        val msg = "Public key in MSO does not match the one in the request"
        if (checkDevicePublicKey) {
            throw IllegalArgumentException(msg)
        }
    }

    val nameSpaces = issuerSigned["nameSpaces"]
    val digestIdMapping = nameSpaces.toDigestIdMapping()
    val staticAuthData = StaticAuthDataGenerator(digestIdMapping, issuerAuthBytes)
        .generate()
    identityDocument.pendingCredentials.forEach { credential ->
        credential.certify(staticAuthData, mso.validFrom, mso.validUntil)
    }

    identityDocument.nameSpacedData = nameSpaces.asNameSpacedData()
}