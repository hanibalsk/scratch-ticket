package sk.o2.scratchcard.data.repository

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.withContext
import sk.o2.scratchcard.data.di.IoDispatcher
import sk.o2.scratchcard.data.remote.O2ApiService
import sk.o2.scratchcard.domain.model.ScratchCardState
import sk.o2.scratchcard.domain.repository.ScratchCardRepository
import timber.log.Timber
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Concrete implementation of ScratchCardRepository.
 *
 * This singleton repository manages scratch card state and coordinates operations:
 * - Maintains authoritative state via MutableStateFlow
 * - Implements 2-second scratch operation with UUID generation
 * - Handles API calls for card activation
 * - Manages state transitions based on API responses
 *
 * Thread Safety: All state mutations happen on the IO dispatcher to ensure
 * thread-safe access to MutableStateFlow.
 *
 * @property apiService Service for O2 API communication
 * @property ioDispatcher Dispatcher for background operations
 */
@Singleton
class ScratchCardRepositoryImpl @Inject constructor(
    private val apiService: O2ApiService,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher
) : ScratchCardRepository {

    /**
     * Mutable internal state - only this repository can modify it.
     * External observers get read-only StateFlow via cardState property.
     */
    private val _cardState = MutableStateFlow<ScratchCardState>(ScratchCardState.Unscratched)

    /**
     * Public read-only StateFlow for observing card state.
     * All ViewModels and screens observe this for reactive UI updates.
     */
    override val cardState: StateFlow<ScratchCardState> = _cardState.asStateFlow()

    /**
     * Perform scratch operation with exact 2-second timing.
     *
     * Implementation:
     * 1. Delays for exactly 2000 milliseconds (NFR002)
     * 2. Generates random UUID using java.util.UUID
     * 3. Updates internal state to Scratched(uuid)
     * 4. Returns Result.success(uuid)
     *
     * Cancellation: This operation uses withContext(ioDispatcher) which
     * is cancellable. If the calling coroutine is cancelled, delay() throws
     * CancellationException and state remains unchanged.
     *
     * @return Result containing revealed UUID code, or exception on failure
     */
    override suspend fun scratchCard(): Result<String> = withContext(ioDispatcher) {
        return@withContext try {
            Timber.d("Starting scratch operation...")

            // Exact 2-second delay as per NFR002
            delay(2000)

            // Generate random UUID code
            val uuid = UUID.randomUUID().toString()
            Timber.d("Scratch complete - UUID generated: $uuid")

            // Transition to Scratched state
            _cardState.value = ScratchCardState.Scratched(uuid)

            Result.success(uuid)
        } catch (e: Exception) {
            Timber.e(e, "Scratch operation failed")
            Result.failure(e)
        }
    }

    /**
     * Activate card by calling O2 API.
     *
     * Implementation:
     * 1. Calls apiService.validateCode(code)
     * 2. Checks if response.android > 277028
     * 3. If valid: transitions to Activated state, returns true
     * 4. If invalid: state remains Scratched, returns false
     * 5. On exception: propagates error, state unchanged
     *
     * State Transitions:
     * - Success (android > 277028): Scratched(code) → Activated(code)
     * - Failure (android ≤ 277028): Scratched(code) remains
     * - Error: Scratched(code) remains
     *
     * @param code UUID code to validate (from Scratched state)
     * @return Result<Boolean> - true if activated, false if validation failed,
     *         or exception for network/parsing errors
     */
    override suspend fun activateCard(code: String): Result<Boolean> = withContext(ioDispatcher) {
        return@withContext try {
            Timber.d("Starting activation for code: $code")

            // Call O2 API to validate code
            val response = apiService.validateCode(code)
            Timber.d("API response received - android version: ${response.android}")

            // Validate threshold: success if android > 277028
            val isValid = response.android > 277028

            if (isValid) {
                // Validation successful - transition to Activated
                _cardState.value = ScratchCardState.Activated(code)
                Timber.i("Activation successful - card activated")
                Result.success(true)
            } else {
                // Validation failed - state remains Scratched (FR017)
                Timber.w("Validation failed - android version ${response.android} ≤ 277028")
                Result.failure(
                    sk.o2.scratchcard.domain.model.DomainException.ValidationException(response.android)
                )
            }
        } catch (e: java.net.UnknownHostException) {
            // Network error: No internet connection
            Timber.e(e, "Network error: No internet connection")
            Result.failure(sk.o2.scratchcard.domain.model.DomainException.NetworkException.NoConnection(e))
        } catch (e: java.net.SocketTimeoutException) {
            // Network error: Request timed out
            Timber.e(e, "Network error: Request timed out after 10 seconds")
            Result.failure(sk.o2.scratchcard.domain.model.DomainException.NetworkException.Timeout(e))
        } catch (e: io.ktor.client.plugins.ClientRequestException) {
            // HTTP error: 4xx status codes
            Timber.e(e, "HTTP client error: ${e.response.status.value}")
            Result.failure(sk.o2.scratchcard.domain.model.DomainException.HttpException.ClientError(e.response.status.value, e))
        } catch (e: io.ktor.client.plugins.ServerResponseException) {
            // HTTP error: 5xx status codes
            Timber.e(e, "HTTP server error: ${e.response.status.value}")
            Result.failure(sk.o2.scratchcard.domain.model.DomainException.HttpException.ServerError(e.response.status.value, e))
        } catch (e: kotlinx.serialization.SerializationException) {
            // Parsing error: Malformed JSON or missing fields
            Timber.e(e, "Parsing error: Malformed API response")
            Result.failure(sk.o2.scratchcard.domain.model.DomainException.ParsingException(e))
        } catch (e: Exception) {
            // Unknown error
            Timber.e(e, "Unknown error during activation")
            Result.failure(e)
        }
    }
}
