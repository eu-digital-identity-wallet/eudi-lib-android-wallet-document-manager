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

import com.android.identity.document.Document
import com.android.identity.document.NameSpacedData
import com.upokecenter.cbor.CBORObject
import io.mockk.every
import io.mockk.mockk
import org.junit.Assert
import org.junit.Test


class TestIssuedDocumentExtensions {
    @Test
    fun testDocumentCborToJson() {
        val mockNameSpacedData = mockk<NameSpacedData>(relaxed = true) {
            every { nameSpaceNames } returns listOf("namespace1", "namespace2", "namespace3")
            every { getDataElementNames("namespace1") } returns listOf("element1", "element2")
            every { getDataElement("namespace1", "element1") } returns CBORObject.FromObject(1)
                .EncodeToBytes()
            every { getDataElement("namespace1", "element2") } returns CBORObject.FromObjectAndTag(
                byteArrayOf(0x01, 0x02, 0x03),
                24
            ).EncodeToBytes()

            every { getDataElementNames("namespace2") } returns listOf("element3", "element4")
            every {
                getDataElement(
                    "namespace2",
                    "element3"
                )
            } returns CBORObject.FromObject("value3")
                .EncodeToBytes()
            every { getDataElement("namespace2", "element4") } returns CBORObject.FromObject(
                mapOf(
                    "subelement1" to CBORObject.FromObjectAndTag("2023-11-09T00:01:02Z", 0),
                    "subelement2" to CBORObject.FromObject(5.4f)
                )
            ).EncodeToBytes()

            every { getDataElementNames("namespace3") } returns listOf("element1")
            every { getDataElement("namespace3", "element1") } returns CBORObject.FromObject(
                arrayOf(
                    1,
                    "string",
                    byteArrayOf(0x01, 0x02, 0x03)
                )
            ).EncodeToBytes()
        }
        val mockBaseDocument = mockk<Document>(relaxed = true) {
            every { applicationData.getNameSpacedData("nameSpacedData") } returns mockNameSpacedData
        }

        val issuedDocument = IssuedDocument(mockBaseDocument)

        val json = issuedDocument.nameSpacedDataJSONObject
        println(json.toString(2))
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
