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

package eu.europa.ec.eudi.wallet.document.defaults

import android.content.Context
import com.android.identity.android.securearea.AndroidKeystoreCreateKeySettings
import com.android.identity.android.securearea.AndroidKeystoreKeyUnlockData
import com.android.identity.android.securearea.AndroidKeystoreSecureArea
import com.android.identity.android.securearea.UserAuthenticationType
import com.android.identity.crypto.EcCurve
import com.android.identity.securearea.CreateKeySettings
import com.android.identity.securearea.KeyPurpose
import com.android.identity.securearea.SecureArea
import com.android.identity.storage.StorageEngine
import eu.europa.ec.eudi.wallet.document.KeyUnlockDataFactory
import eu.europa.ec.eudi.wallet.document.internal.randomBytes
import eu.europa.ec.eudi.wallet.document.CreateKeySettingsFactory as ICreateKeySettingsFactory

class DefaultSecureArea(context: Context, storageEngine: StorageEngine) :
    SecureArea by AndroidKeystoreSecureArea(context, storageEngine) {
    val createKeySettingsFactory by lazy { CreateKeySettingsFactory(context) }

    companion object {
        val KeyUnlockDataFactory = KeyUnlockDataFactory { secureArea, keyAlias ->
            keyAlias?.let { AndroidKeystoreKeyUnlockData(it) }
        }
    }

    class CreateKeySettingsFactory(context: Context) : ICreateKeySettingsFactory {
        private val capabilities by lazy {
            AndroidKeystoreSecureArea.Capabilities(context)
        }
        var attestationChallenge: ByteArray = 10.randomBytes
        var keyPurposes: Set<KeyPurpose> = setOf(KeyPurpose.SIGN)
        var curve: EcCurve = EcCurve.P256
        var useStrongBox: Boolean = capabilities.strongBoxSupported
        var userAuth: Boolean = false
        var userAuthTimeoutInMillis: Long = 30_000L
        var userAuthType: Set<UserAuthenticationType> =
            setOf(UserAuthenticationType.BIOMETRIC, UserAuthenticationType.LSKF)

        override fun createKeySettings(): CreateKeySettings {
            return AndroidKeystoreCreateKeySettings.Builder(attestationChallenge)
                .setEcCurve(EcCurve.P256)
                .setUseStrongBox(useStrongBox && capabilities.strongBoxSupported)
                .setUserAuthenticationRequired(userAuth, userAuthTimeoutInMillis, userAuthType)
                .setKeyPurposes(setOf(KeyPurpose.SIGN))
                .build()
        }
    }
}