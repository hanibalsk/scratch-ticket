package sk.o2.scratchcard.domain.model

/**
 * Domain-level exceptions for the O2 Scratch Card app.
 *
 * Provides typed exception hierarchy for categorizing errors (FR017).
 * Enables UI layer to display appropriate error messages based on error type.
 *
 * Exception Categories:
 * - NetworkException: Connection issues (no internet, timeout)
 * - HttpException: API response errors (4xx, 5xx)
 * - ParsingException: Response parsing/serialization errors
 * - ValidationException: Business logic validation failures (android version below threshold)
 *
 * Usage:
 * ```kotlin
 * catch (e: DomainException.NetworkException.NoConnection) {
 *     showError("No internet connection")
 * }
 * ```
 */
sealed class DomainException(
    message: String,
    cause: Throwable? = null,
) : Exception(message, cause) {
    /**
     * Network-related exceptions.
     *
     * Thrown when network connectivity issues prevent API communication.
     */
    sealed class NetworkException(
        message: String,
        cause: Throwable,
    ) : DomainException(message, cause) {
        /**
         * No internet connection available.
         *
         * Thrown when device has no network connectivity.
         * UI should display: "No internet connection"
         */
        class NoConnection(
            cause: Throwable,
        ) : NetworkException("No internet connection", cause)

        /**
         * Request timed out.
         *
         * Thrown when API request exceeds 10-second timeout (NFR002).
         * UI should display: "Request timed out"
         */
        class Timeout(
            cause: Throwable,
        ) : NetworkException("Request timed out", cause)
    }

    /**
     * HTTP response exceptions.
     *
     * Thrown when API returns non-success status codes.
     */
    sealed class HttpException(
        message: String,
        val statusCode: Int,
        cause: Throwable,
    ) : DomainException(message, cause) {
        /**
         * Client error (4xx status codes).
         *
         * Thrown for 400-499 HTTP status codes.
         * UI should display: "Service unavailable, try again later"
         */
        class ClientError(
            statusCode: Int,
            cause: Throwable,
        ) : HttpException("HTTP client error: $statusCode", statusCode, cause)

        /**
         * Server error (5xx status codes).
         *
         * Thrown for 500-599 HTTP status codes.
         * UI should display: "Service unavailable, try again later"
         */
        class ServerError(
            statusCode: Int,
            cause: Throwable,
        ) : HttpException("HTTP server error: $statusCode", statusCode, cause)
    }

    /**
     * JSON parsing exception.
     *
     * Thrown when API response cannot be parsed or is malformed.
     * UI should display: "Service temporarily unavailable. Please try again later."
     */
    class ParsingException(
        cause: Throwable,
    ) : DomainException("Failed to parse API response", cause)

    /**
     * Business logic validation exception.
     *
     * Thrown when activation fails due to android ≤ 277028.
     * This is not an error in the technical sense - the API call succeeded
     * but the business rule validation failed.
     *
     * UI should display: "This scratch card could not be activated. Please try again or contact support."
     */
    class ValidationException(
        val androidVersion: Int,
    ) : DomainException("Activation validation failed: android version $androidVersion ≤ 277028")
}
