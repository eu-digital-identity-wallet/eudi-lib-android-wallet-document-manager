/*
 *  Copyright (c) 2023-2024 European Commission
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
@file:JvmMultifileClass

package eu.europa.ec.eudi.wallet.document

import com.android.identity.credential.Credential
import eu.europa.ec.eudi.wallet.document.internal.issuedAt
import eu.europa.ec.eudi.wallet.document.internal.toObject
import java.time.Instant

/**
 * An [IssuedDocument] is a document that has been issued. It contains the data that was issued.
 * To store the [IssuedDocument], use the [DocumentManager.storeIssuedDocument] method.
 *
 * @property issuedAt document's issuance date
 * @property requiresUserAuth flag that indicates if the document requires user authentication to be accessed
 * @property nameSpacedData retrieves the document's data, grouped by nameSpace. Values are in CBOR bytes
 * @property nameSpaces retrieves the document's nameSpaces and elementIdentifiers
 * @property nameSpacedDataValues retrieves the document's data, grouped by nameSpace. Values are in their original type
 */
class IssuedDocument internal constructor(
    private val credential: Credential,
) : Document by DocumentImpl(credential) {

    val issuedAt: Instant
        get() = credential.issuedAt

    val nameSpacedData: Map<NameSpace, Map<ElementIdentifier, ByteArray>>
        get() = credential.nameSpacedData.nameSpaceNames.associateWith { nameSpace ->
            credential.nameSpacedData.getDataElementNames(nameSpace)
                .associateWith { elementIdentifier ->
                    credential.nameSpacedData.getDataElement(nameSpace, elementIdentifier)
                }
        }

    val nameSpaces: Map<NameSpace, List<ElementIdentifier>>
        get() = nameSpacedData.mapValues { it.value.keys.toList() }

    val nameSpacedDataValues: Map<NameSpace, Map<ElementIdentifier, Any?>>
        get() {
            val map = mutableMapOf<String, Map<ElementIdentifier, Any?>>()
            for ((namespace, data) in nameSpacedData) {
                val namespaceMap = mutableMapOf<String, Any?>()
                for ((elementIdentifier, value) in data) {
                    namespaceMap[elementIdentifier] = value.toObject()
                }
                map[namespace] = namespaceMap
            }
            return map.toMap()
        }
}

