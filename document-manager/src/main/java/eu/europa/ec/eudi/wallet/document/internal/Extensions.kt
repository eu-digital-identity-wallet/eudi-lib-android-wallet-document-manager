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

import com.android.identity.cbor.Cbor
import com.android.identity.crypto.EcPublicKey
import java.security.SecureRandom

internal val Int.randomBytes: ByteArray
    get() {
        val secureRandom = SecureRandom()
        val randomBytes = ByteArray(this)
        secureRandom.nextBytes(randomBytes)
        return randomBytes
    }

internal val EcPublicKey.toCoseBytes: ByteArray
    get() = Cbor.encode(toDataItem())

internal val ByteArray.toEcPublicKey: EcPublicKey
    get() = EcPublicKey.fromDataItem(Cbor.decode(this))

internal val ByteArray.sdJwtVcString: String
    get() = String(this, charset = Charsets.US_ASCII)