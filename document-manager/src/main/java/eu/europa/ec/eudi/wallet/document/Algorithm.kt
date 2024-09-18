/*
 * Copyright (c) 2023-2024 European Commission
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

package eu.europa.ec.eudi.wallet.document

import androidx.annotation.StringDef
import com.android.identity.crypto.Algorithm as BaseAlgorith

/**
 * Algorithm used to sign the document
 */
@Retention(AnnotationRetention.SOURCE)
@StringDef(value = [Algorithm.SHA256withECDSA, Algorithm.SHA384withECDSA, Algorithm.SHA512withECDSA])
annotation class Algorithm {
    /**
     * Supported algorithms
     * @property SHA512withECDSA
     * @property SHA384withECDSA
     * @property SHA256withECDSA
     */
    companion object {
        const val SHA512withECDSA = "SHA512withECDSA"
        const val SHA384withECDSA = "SHA384withECDSA"
        const val SHA256withECDSA = "SHA256withECDSA"
    }
}

internal val String.algorithm: BaseAlgorith
    get() = when (this) {
        Algorithm.SHA256withECDSA -> BaseAlgorith.ES256
        Algorithm.SHA384withECDSA -> BaseAlgorith.ES384
        Algorithm.SHA512withECDSA -> BaseAlgorith.ES512
        else -> throw IllegalArgumentException("Unknown algorithm: $this")
    }