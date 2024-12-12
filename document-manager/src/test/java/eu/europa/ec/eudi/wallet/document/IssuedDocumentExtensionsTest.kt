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

import com.android.identity.document.NameSpacedData
import com.upokecenter.cbor.CBORObject
import eu.europa.ec.eudi.wallet.document.format.MsoMdocClaims
import io.mockk.mockk
import kotlinx.datetime.Clock
import kotlinx.datetime.toJavaInstant
import org.junit.Assert
import kotlin.test.Test
import kotlin.time.Duration.Companion.days


class TestIssuedDocumentExtensions {
    @Test
    fun testDocumentCborToJson() {
        val nameSpacedData = NameSpacedData.Builder()
            .putEntryNumber("namespace1", "element1", 1)
            .putEntryString("namespace1", "element2", "AQID")
            .putEntryString("namespace2", "element3", "value3")
            .putEntry("namespace2", "element4", CBORObject.NewMap().apply {
                Add("subelement1", CBORObject.FromObjectAndTag("2023-11-09T00:01:02Z", 0))
                Add("subelement2", 5.4f)
            }.EncodeToBytes())
            .putEntry("namespace3", "element1", CBORObject.NewArray().apply {
                Add(1)
                Add("string")
                Add("AQID")
            }.EncodeToBytes())
            .build()

        val issuedDocument = IssuedDocument(
            id = "id",
            name = "name",
            format = mockk(),
            isCertified = true,
            keyAlias = "keyAlias",
            secureArea = mockk(),
            documentManagerId = "DocumentManagerId",
            createdAt = Clock.System.now().toJavaInstant(),
            issuedAt = Clock.System.now().toJavaInstant(),
            validFrom = Clock.System.now().toJavaInstant(),
            validUntil = Clock.System.now().plus(10.days).toJavaInstant(),
            claims = MsoMdocClaims(nameSpacedData = nameSpacedData),
            issuerProvidedData = byteArrayOf(),
        )

        val json = issuedDocument.nameSpacedDataJSONObject

        Assert.assertEquals(3, json.keys().asSequence().toList().size)
        Assert.assertEquals(2, json.getJSONObject("namespace1").keys().asSequence().toList().size)
        Assert.assertEquals(2, json.getJSONObject("namespace2").keys().asSequence().toList().size)
        Assert.assertEquals(1, json.getJSONObject("namespace3").keys().asSequence().toList().size)

        Assert.assertEquals(1, json.getJSONObject("namespace1").getInt("element1"))
        Assert.assertEquals("AQID", json.getJSONObject("namespace1").getString("element2"))

        Assert.assertEquals("value3", json.getJSONObject("namespace2").getString("element3"))
        Assert.assertEquals(
            "2023-11-09T00:01:02Z",
            json.getJSONObject("namespace2").getJSONObject("element4").getString("subelement1")
        )
        Assert.assertEquals(
            5.4f,
            json.getJSONObject("namespace2").getJSONObject("element4").getDouble("subelement2")
                .toFloat(),
            0.0001f
        )

        Assert.assertEquals(1, json.getJSONObject("namespace3").getJSONArray("element1")[0])
        Assert.assertEquals("string", json.getJSONObject("namespace3").getJSONArray("element1")[1])
        Assert.assertEquals("AQID", json.getJSONObject("namespace3").getJSONArray("element1")[2])

    }
}
