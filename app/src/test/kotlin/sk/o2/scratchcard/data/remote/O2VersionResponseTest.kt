package sk.o2.scratchcard.data.remote

import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.Json
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

/**
 * Unit tests for O2VersionResponse data class.
 *
 * Validates:
 * - Correct deserialization from JSON
 * - Property access and data class behavior
 * - Edge cases with various android version values
 * - JSON serialization with @SerialName annotation
 */
class O2VersionResponseTest {
    private val json =
        Json {
            ignoreUnknownKeys = true
            isLenient = true
        }

    @Test
    fun `deserialize valid JSON response`() {
        val jsonString = """{"android": 287028}"""

        val response = json.decodeFromString<O2VersionResponse>(jsonString)

        assertEquals(287028, response.android)
    }

    @Test
    fun `deserialize response with minimum android version`() {
        val jsonString = """{"android": 0}"""

        val response = json.decodeFromString<O2VersionResponse>(jsonString)

        assertEquals(0, response.android)
    }

    @Test
    fun `deserialize response with maximum android version`() {
        val jsonString = """{"android": ${Int.MAX_VALUE}}"""

        val response = json.decodeFromString<O2VersionResponse>(jsonString)

        assertEquals(Int.MAX_VALUE, response.android)
    }

    @Test
    fun `deserialize response at validation threshold`() {
        val jsonString = """{"android": 277028}"""

        val response = json.decodeFromString<O2VersionResponse>(jsonString)

        assertEquals(277028, response.android)
    }

    @Test
    fun `deserialize response just above validation threshold`() {
        val jsonString = """{"android": 277029}"""

        val response = json.decodeFromString<O2VersionResponse>(jsonString)

        assertEquals(277029, response.android)
    }

    @Test
    fun `deserialize response just below validation threshold`() {
        val jsonString = """{"android": 277027}"""

        val response = json.decodeFromString<O2VersionResponse>(jsonString)

        assertEquals(277027, response.android)
    }

    @Test
    fun `deserialize response ignores unknown fields`() {
        val jsonString =
            """
            {
                "android": 287028,
                "ios": 123456,
                "extra_field": "ignored"
            }
            """.trimIndent()

        val response = json.decodeFromString<O2VersionResponse>(jsonString)

        assertEquals(287028, response.android)
    }

    @Test
    fun `data class equality works correctly`() {
        val response1 = O2VersionResponse(287028)
        val response2 = O2VersionResponse(287028)
        val response3 = O2VersionResponse(300000)

        assertEquals(response1, response2)
        assertNotEquals(response1, response3)
    }

    @Test
    fun `data class copy works correctly`() {
        val original = O2VersionResponse(287028)
        val copy = original.copy()
        val modifiedCopy = original.copy(android = 300000)

        assertEquals(original, copy)
        assertEquals(287028, copy.android)
        assertEquals(300000, modifiedCopy.android)
        assertNotEquals(original, modifiedCopy)
    }

    @Test
    fun `data class toString contains android value`() {
        val response = O2VersionResponse(287028)

        val stringRepresentation = response.toString()

        assertTrue(stringRepresentation.contains("287028"))
        assertTrue(stringRepresentation.contains("android"))
    }

    @Test
    fun `data class hashCode is consistent`() {
        val response1 = O2VersionResponse(287028)
        val response2 = O2VersionResponse(287028)

        assertEquals(response1.hashCode(), response2.hashCode())
    }

    @Test
    fun `serialize response to JSON`() {
        val response = O2VersionResponse(287028)

        val jsonString = json.encodeToString(O2VersionResponse.serializer(), response)

        assertTrue(jsonString.contains("\"android\""))
        assertTrue(jsonString.contains("287028"))
    }

    @Test
    fun `deserialize malformed JSON throws exception`() {
        val jsonString = """{"android": "not_a_number"}"""

        assertThrows(SerializationException::class.java) {
            json.decodeFromString<O2VersionResponse>(jsonString)
        }
    }

    @Test
    fun `deserialize missing required field throws exception`() {
        val jsonString = """{}"""

        assertThrows(SerializationException::class.java) {
            json.decodeFromString<O2VersionResponse>(jsonString)
        }
    }

    @Test
    fun `deserialize null value throws exception`() {
        val jsonString = """{"android": null}"""

        assertThrows(SerializationException::class.java) {
            json.decodeFromString<O2VersionResponse>(jsonString)
        }
    }

    @Test
    fun `component1 destructuring works`() {
        val response = O2VersionResponse(287028)

        val (android) = response

        assertEquals(287028, android)
    }

    @Test
    fun `negative android version can be deserialized`() {
        val jsonString = """{"android": -1}"""

        val response = json.decodeFromString<O2VersionResponse>(jsonString)

        assertEquals(-1, response.android)
    }
}
