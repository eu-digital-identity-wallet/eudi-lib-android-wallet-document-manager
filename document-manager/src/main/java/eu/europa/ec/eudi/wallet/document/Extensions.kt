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
@file:JvmName("Utils")

package eu.europa.ec.eudi.wallet.document

import org.json.JSONObject

/**
 * Extension function to convert [IssuedDocument]'s nameSpacedData to [JSONObject]
 *
 * @return [JSONObject]
 */
@get:JvmName("nameSpacedDataAsJSONObject")
val IssuedDocument.nameSpacedDataJSONObject: JSONObject
    get() = JSONObject(nameSpacedDataValues)

/**
 * Extension function for [DocumentManager] to get documents using
 * reified type parameter
 */
inline fun <reified D : Document> DocumentManager.getDocuments(): List<D> {
    val state = when (D::class) {
        UnsignedDocument::class -> Document.State.UNSIGNED
        IssuedDocument::class -> Document.State.ISSUED
        DeferredDocument::class -> Document.State.DEFERRED
        else -> null
    }
    return getDocuments(state).filterIsInstance<D>()
}