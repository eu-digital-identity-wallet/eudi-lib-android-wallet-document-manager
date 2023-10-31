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
package eu.europa.ec.eudi.wallet.document

import android.content.Context
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.android.identity.android.securearea.AndroidKeystoreSecureArea
import com.android.identity.android.storage.AndroidStorageEngine
import com.android.identity.storage.StorageEngine
import org.junit.AfterClass
import org.junit.Assert
import org.junit.BeforeClass
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException

@RunWith(AndroidJUnit4::class)
class DocumentManagerImplTest {

    companion object {
        private lateinit var context: Context
        private lateinit var secureArea: AndroidKeystoreSecureArea
        private lateinit var storageEngine: StorageEngine
        private lateinit var documentManager: DocumentManagerImpl

        @JvmStatic
        @BeforeClass
        @Throws(IOException::class)
        fun setUp() {
            context = InstrumentationRegistry.getInstrumentation().targetContext
            storageEngine = AndroidStorageEngine.Builder(context, context.cacheDir)
                .setUseEncryption(false)
                .build()
                .apply {
                    deleteAll()
                }
            secureArea = AndroidKeystoreSecureArea(context, storageEngine)
            documentManager = DocumentManagerImpl(context, storageEngine, secureArea)
                .userAuth(false)
        }

        @JvmStatic
        @AfterClass
        fun tearDown() {
            storageEngine.deleteAll()
        }
    }

    @Test
    fun testDocumentManager_userAuth_status() {
        Assert.assertFalse(documentManager.userAuth)
    }
}
