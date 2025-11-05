package sk.o2.scratchcard.domain.model

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Assertions.assertSame
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import java.io.IOException

/**
 * Unit tests for DomainException sealed class hierarchy.
 *
 * Validates:
 * - Exception type hierarchy and inheritance
 * - Message construction and formatting
 * - Cause propagation
 * - Status code handling for HTTP exceptions
 * - Android version handling for validation exceptions
 */
class DomainExceptionTest {
    @Test
    fun `NetworkException NoConnection creates correct exception`() {
        val cause = IOException("Network unavailable")
        val exception = DomainException.NetworkException.NoConnection(cause)

        assertEquals("No internet connection", exception.message)
        assertSame(cause, exception.cause)
    }

    @Test
    fun `NetworkException Timeout creates correct exception`() {
        val cause = IOException("Connection timed out")
        val exception = DomainException.NetworkException.Timeout(cause)

        assertEquals("Request timed out", exception.message)
        assertSame(cause, exception.cause)
    }

    @Test
    fun `HttpException ClientError creates correct exception with status code`() {
        val cause = RuntimeException("404 Not Found")
        val statusCode = 404
        val exception = DomainException.HttpException.ClientError(statusCode, cause)

        assertEquals("HTTP client error: 404", exception.message)
        assertEquals(404, exception.statusCode)
        assertSame(cause, exception.cause)
    }

    @Test
    fun `HttpException ServerError creates correct exception with status code`() {
        val cause = RuntimeException("500 Internal Server Error")
        val statusCode = 500
        val exception = DomainException.HttpException.ServerError(statusCode, cause)

        assertEquals("HTTP server error: 500", exception.message)
        assertEquals(500, exception.statusCode)
        assertSame(cause, exception.cause)
    }

    @Test
    fun `ParsingException creates correct exception`() {
        val cause = RuntimeException("Malformed JSON")
        val exception = DomainException.ParsingException(cause)

        assertEquals("Failed to parse API response", exception.message)
        assertSame(cause, exception.cause)
    }

    @Test
    fun `ValidationException creates correct exception with android version`() {
        val androidVersion = 250000
        val exception = DomainException.ValidationException(androidVersion)

        assertEquals(
            "Activation validation failed: android version 250000 ≤ 277028",
            exception.message,
        )
        assertEquals(250000, exception.androidVersion)
        assertNull(exception.cause)
    }

    @Test
    fun `ValidationException with boundary android version`() {
        val androidVersion = 277028
        val exception = DomainException.ValidationException(androidVersion)

        assertEquals(277028, exception.androidVersion)
        assertTrue(exception.message!!.contains("277028"))
    }

    @Test
    fun `HttpException preserves status code across hierarchy`() {
        val clientError =
            DomainException.HttpException.ClientError(
                403,
                RuntimeException(),
            )
        val serverError =
            DomainException.HttpException.ServerError(
                503,
                RuntimeException(),
            )

        // Can access statusCode through HttpException type
        assertEquals(403, clientError.statusCode)
        assertEquals(503, serverError.statusCode)

        // Verify through base type
        val baseClientError: DomainException.HttpException = clientError
        val baseServerError: DomainException.HttpException = serverError
        assertEquals(403, baseClientError.statusCode)
        assertEquals(503, baseServerError.statusCode)
    }

    @Test
    fun `exceptions can be distinguished in when expression`() {
        val exceptions =
            listOf(
                DomainException.NetworkException.NoConnection(IOException()),
                DomainException.NetworkException.Timeout(IOException()),
                DomainException.HttpException.ClientError(404, RuntimeException()),
                DomainException.HttpException.ServerError(500, RuntimeException()),
                DomainException.ParsingException(RuntimeException()),
                DomainException.ValidationException(250000),
            )

        exceptions.forEach { exception ->
            val category =
                when (exception) {
                    is DomainException.NetworkException.NoConnection -> "no_connection"
                    is DomainException.NetworkException.Timeout -> "timeout"
                    is DomainException.HttpException.ClientError -> "client_error"
                    is DomainException.HttpException.ServerError -> "server_error"
                    is DomainException.ParsingException -> "parsing"
                    is DomainException.ValidationException -> "validation"
                }

            assertNotNull(category)
        }
    }

    @Test
    fun `nested exceptions maintain cause chain`() {
        val rootCause = IOException("Root cause")
        val networkException = DomainException.NetworkException.NoConnection(rootCause)

        assertEquals(rootCause, networkException.cause)
        assertEquals("Root cause", networkException.cause!!.message)
    }

    @Test
    fun `HttpException with various status codes`() {
        val clientErrors =
            listOf(400, 401, 403, 404, 422).map {
                DomainException.HttpException.ClientError(it, RuntimeException())
            }
        val serverErrors =
            listOf(500, 502, 503, 504).map {
                DomainException.HttpException.ServerError(it, RuntimeException())
            }

        clientErrors.forEach { exception ->
            assertTrue(exception.statusCode in 400..499)
            assertTrue(exception.message!!.contains("HTTP client error"))
        }

        serverErrors.forEach { exception ->
            assertTrue(exception.statusCode in 500..599)
            assertTrue(exception.message!!.contains("HTTP server error"))
        }
    }

    @Test
    fun `ValidationException with edge case android versions`() {
        val testCases =
            mapOf(
                0 to "android version 0 ≤ 277028",
                1 to "android version 1 ≤ 277028",
                277028 to "android version 277028 ≤ 277028",
                Int.MAX_VALUE to "android version ${Int.MAX_VALUE} ≤ 277028",
            )

        testCases.forEach { (version, expectedMessage) ->
            val exception = DomainException.ValidationException(version)
            assertEquals(version, exception.androidVersion)
            assertTrue(
                exception.message!!.contains(expectedMessage),
                "Expected message to contain '$expectedMessage' but was '${exception.message}'",
            )
        }
    }
}
