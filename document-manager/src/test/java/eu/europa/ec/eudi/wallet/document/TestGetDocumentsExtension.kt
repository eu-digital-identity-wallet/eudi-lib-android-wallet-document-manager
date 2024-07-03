/*
 *  Copyright (c) 2024 European Commission
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

package eu.europa.ec.eudi.wallet.document

import io.mockk.every
import io.mockk.mockk
import org.junit.Assert.assertEquals
import org.junit.Assert.assertSame
import org.junit.Test

class TestGetDocumentsExtension {

    @Test
    fun `test getDocuments extension returns the correct documents`() {
        val issued = mockk<IssuedDocument> {
            every { state } returns Document.State.ISSUED
        }
        val deferred = mockk<DeferredDocument> {
            every { state } returns Document.State.DEFERRED
        }
        val unsigned = mockk<UnsignedDocument> {
            every { state } returns Document.State.UNSIGNED
        }
        val documentManager = mockk<DocumentManager>()

        every {
            documentManager.getDocuments(null)
        } returns listOf(issued, deferred, unsigned)
        every {
            documentManager.getDocuments(Document.State.ISSUED)
        } returns listOf(issued)
        every {
            documentManager.getDocuments(Document.State.DEFERRED)
        } returns listOf(deferred)
        every {
            documentManager.getDocuments(Document.State.UNSIGNED)
        } returns listOf(unsigned)


        val issuedResult = documentManager.getDocuments<IssuedDocument>()
        assertEquals(1, issuedResult.size)
        assertSame(issued, issuedResult[0])

        val deferredResult = documentManager.getDocuments<DeferredDocument>()
        assertEquals(1, deferredResult.size)
        assertSame(deferred, deferredResult[0])

        val unsignedResult = documentManager.getDocuments<UnsignedDocument>()
        assertEquals(1, unsignedResult.size)
        assertSame(unsigned, unsignedResult[0])
    }
}