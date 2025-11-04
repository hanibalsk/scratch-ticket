package sk.o2.scratchcard.data.remote

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Service for O2 API communication.
 *
 * Provides methods for interacting with the O2 Slovakia API endpoints.
 * Uses Ktor Client for HTTP communication with Android engine.
 *
 * API Endpoint: https://api.o2.sk/version
 *
 * @property httpClient Ktor HTTP client configured with Android engine and JSON serialization
 */
@Singleton
class O2ApiService @Inject constructor(
    private val httpClient: HttpClient
) {

    /**
     * Validate scratch card code with O2 API.
     *
     * Calls the O2 version endpoint to check if the scratch code is valid.
     * The API returns an android version number which is compared against
     * threshold value 277028 to determine activation eligibility.
     *
     * API Details:
     * - Method: GET
     * - URL: https://api.o2.sk/version
     * - Query Parameter: code (UUID from scratch operation)
     * - Response: JSON with "android" field containing version number
     * - Timeout: 10 seconds (configured in HttpClient)
     *
     * Error Handling:
     * - Network errors: Throws IOException or HttpRequestException
     * - Timeout errors: Throws HttpRequestTimeoutException
     * - Parsing errors: Throws SerializationException
     * - HTTP errors: Throws ResponseException with status code
     *
     * @param code UUID code to validate (obtained from scratch operation)
     * @return O2VersionResponse containing android version number
     * @throws Exception for network, timeout, parsing, or HTTP errors
     */
    suspend fun validateCode(code: String): O2VersionResponse {
        Timber.d("Calling O2 API - validateCode: $code")

        return try {
            val response: O2VersionResponse = httpClient.get("https://api.o2.sk/version") {
                parameter("code", code)
            }.body()

            Timber.d("O2 API response: android=${response.android}")
            response
        } catch (e: Exception) {
            Timber.e(e, "O2 API call failed for code: $code")
            throw e
        }
    }
}

/**
 * Response model for O2 version API endpoint.
 *
 * Expected JSON structure:
 * ```json
 * {
 *   "android": 287028
 * }
 * ```
 *
 * The "android" field contains a version number that determines
 * activation eligibility. Values > 277028 indicate successful activation.
 *
 * @property android Version number for Android platform (threshold: > 277028 for success)
 */
@Serializable
data class O2VersionResponse(
    @SerialName("android")
    val android: Int
)
