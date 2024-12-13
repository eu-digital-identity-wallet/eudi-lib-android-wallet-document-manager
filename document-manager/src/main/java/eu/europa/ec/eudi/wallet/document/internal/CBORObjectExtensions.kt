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

import com.android.identity.document.NameSpacedData
import com.upokecenter.cbor.CBORObject
import com.upokecenter.cbor.CBORType
import eu.europa.ec.eudi.wallet.document.NameSpace
import java.util.Base64


@JvmSynthetic
internal fun ByteArray.getEmbeddedCBORObject(): CBORObject {
    return CBORObject.DecodeFromBytes(this).getEmbeddedCBORObject()
}

@JvmSynthetic
internal fun CBORObject.getEmbeddedCBORObject(): CBORObject {
    return if (HasTag(24)) {
        CBORObject.DecodeFromBytes(GetByteString())
    } else {
        this
    }
}

@JvmSynthetic
internal fun ByteArray.withTag24(): ByteArray {
    return CBORObject.FromObjectAndTag(this, 24).EncodeToBytes()
}

@JvmSynthetic
internal fun ByteArray.toObject(): Any? {
    return CBORObject.DecodeFromBytes(this).parse()
}

private fun CBORObject.parse(): Any? = when {
    isNull -> null
    isTrue -> true
    isFalse -> false
    isNumber -> when {
        CanValueFitInInt32() -> AsInt32Value()
        CanValueFitInInt64() -> AsInt64Value()
        else -> AsDouble()
    }

    else -> when (type) {
        CBORType.ByteString -> when {
            HasMostOuterTag(24) -> GetByteString().toObject()
            else -> Base64.getUrlEncoder().encodeToString(GetByteString())
        }

        CBORType.TextString -> AsString()
        CBORType.Array -> values.map { it.parse() }.toList()
        CBORType.Map -> keys.associate { it.parse() to this[it].parse() }
        else -> null
    }
}

@JvmSynthetic
internal fun CBORObject.toDigestIdMapping(): Map<NameSpace, List<ByteArray>> {
    return entries.associate { (nameSpace, issuerSignedItems) ->
        nameSpace.AsString() to issuerSignedItems.values.map { it.EncodeToBytes() }
    }
}

@JvmSynthetic
internal fun CBORObject.asNameSpacedData(): NameSpacedData {
    val builder = NameSpacedData.Builder()
    keys.forEach { nameSpace ->
        this[nameSpace].values.forEach { v ->
            val el = v.getEmbeddedCBORObject()
            builder.putEntry(
                nameSpace.AsString(),
                el["elementIdentifier"].AsString(),
                el["elementValue"].EncodeToBytes(),
            )
        }
    }
    return builder.build()
}