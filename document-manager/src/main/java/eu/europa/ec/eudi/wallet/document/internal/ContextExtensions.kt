/*
 * Copyright (c) 2023 European Commission
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

import android.app.KeyguardManager
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import eu.europa.ec.eudi.wallet.document.R

@get:JvmSynthetic
internal val Context.isDeviceSecure: Boolean
    get() = getSystemService(KeyguardManager::class.java).isDeviceSecure

@get:JvmSynthetic
internal val Context.supportsStrongBox: Boolean
    get() = Build.VERSION.SDK_INT >= Build.VERSION_CODES.P &&
            packageManager.hasSystemFeature(PackageManager.FEATURE_STRONGBOX_KEYSTORE)

private const val EU_PID_DOCTYPE = "eu.europa.ec.eudi.pid.1"
private const val MDL_DOCTYPE = "org.iso.18013.5.1.mDL"

@JvmSynthetic
internal fun Context.getDocumentNameFromResourcesOrDocType(docType: String): String =
    when (docType) {
        EU_PID_DOCTYPE -> resources.getString(R.string.eu_pid_doctype_name)
        MDL_DOCTYPE -> resources.getString(R.string.mdl_doctype_name)
        else -> docType
    }
