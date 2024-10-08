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
import com.android.identity.android.storage.AndroidStorageEngine
import com.android.identity.storage.StorageEngine
import kotlinx.io.files.Path
import java.io.File

class DefaultStorageEngine(
    context: Context,
    storageFile: File = File(context.noBackupFilesDir, "eudi-identity.bin"),
    useEncryption: Boolean = true
) : StorageEngine by createDefault(context, storageFile, useEncryption) {

    private companion object {
        fun createDefault(
            context: Context,
            storageFile: File,
            useEncryption: Boolean
        ): AndroidStorageEngine {
            return AndroidStorageEngine.Builder(
                context = context,
                storageFile = Path(
                    when {
                        storageFile.isDirectory ->
                            File(storageFile, "eudi-identity.bin")

                        else -> storageFile
                    }.path
                )
            ).apply {
                setUseEncryption(useEncryption)
            }.build()
        }
    }
}