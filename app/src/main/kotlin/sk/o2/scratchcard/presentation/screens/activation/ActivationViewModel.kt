package sk.o2.scratchcard.presentation.screens.activation

import androidx.annotation.StringRes
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import sk.o2.scratchcard.R
import sk.o2.scratchcard.data.di.IoDispatcher
import sk.o2.scratchcard.domain.model.DomainException
import sk.o2.scratchcard.domain.model.ScratchCardState
import sk.o2.scratchcard.domain.repository.ScratchCardRepository
import sk.o2.scratchcard.domain.usecase.ActivateCardUseCase
import timber.log.Timber
import javax.inject.Inject

/**
 * ViewModel for Activation Screen.
 *
 * Manages card activation with non-cancellable coroutine (GlobalScope + IO dispatcher).
 * Maps domain exceptions to user-friendly error types for UI display.
 *
 * Key Features:
 * - Non-cancellable activation (FR014) - operation completes even if user navigates away
 * - Error type categorization (FR017) for appropriate error messages
 * - State observation from repository for success feedback
 *
 * @property activateCardUseCase Use case for activation operation
 * @property repository Repository for observing card state
 * @property ioDispatcher IO dispatcher for background work
 */
@HiltViewModel
class ActivationViewModel
    @Inject
    constructor(
        private val activateCardUseCase: ActivateCardUseCase,
        repository: ScratchCardRepository,
        @IoDispatcher private val ioDispatcher: CoroutineDispatcher,
    ) : ViewModel() {
        /**
         * Observe card state from repository.
         * Used to show success when state transitions to Activated.
         */
        val cardState: StateFlow<ScratchCardState> =
            repository.cardState
                .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), ScratchCardState.Unscratched)

        /**
         * UI state for activation screen.
         */
        private val _uiState = MutableStateFlow<ActivationUiState>(ActivationUiState.Idle)
        val uiState: StateFlow<ActivationUiState> = _uiState.asStateFlow()

        /**
         * Activate card with non-cancellable operation.
         *
         * Uses GlobalScope + IO dispatcher to ensure operation completes even
         * if user navigates away (FR014). This is critical for activation to
         * reach the server and update state correctly.
         *
         * Error Handling (FR017):
         * - Network errors → "No internet connection" or "Request timed out"
         * - HTTP errors → "Service unavailable, try again later"
         * - Parsing errors → "Service temporarily unavailable"
         * - Validation errors → "This scratch card could not be activated"
         *
         * State Updates:
         * - Loading state shown immediately
         * - Success state when android > 277028
         * - Error state with categorized error type
         *
         * @param code UUID code to activate (from Scratched state)
         */
        @OptIn(DelicateCoroutinesApi::class)
        fun activateCard(code: String) {
            _uiState.value = ActivationUiState.Loading
            Timber.d("Initiating activation for code: $code")

            // Non-cancellable: Use GlobalScope to survive ViewModel clear (FR014)
            GlobalScope.launch(ioDispatcher) {
                try {
                    val result = activateCardUseCase(code)

                    result.fold(
                        onSuccess = { activated ->
                            if (activated) {
                                // Should never reach here since repository now uses ValidationException
                                _uiState.value = ActivationUiState.Success
                                Timber.i("Activation succeeded")
                            } else {
                                // Legacy path - should be ValidationException now
                                _uiState.value =
                                    ActivationUiState.Error(
                                        ErrorType.Validation(
                                            messageRes = R.string.error_activation_failed_message,
                                        ),
                                    )
                                Timber.w("Activation returned false (validation failed)")
                            }
                        },
                        onFailure = { error ->
                            val errorType = mapErrorToType(error)
                            _uiState.value = ActivationUiState.Error(errorType)
                            Timber.e(error, "Activation failed with error type: ${errorType::class.simpleName}")
                        },
                    )
                } catch (e: Exception) {
                    // Catch any unexpected exceptions
                    _uiState.value =
                        ActivationUiState.Error(
                            ErrorType.Unknown(messageRes = R.string.error_unknown_message),
                        )
                    Timber.e(e, "Unexpected error in activation ViewModel")
                }
            }
        }

        /**
         * Map domain exceptions to UI error types (FR017).
         *
         * Categorizes errors for appropriate user messaging:
         * - Network: Connection issues
         * - Server: API unavailability
         * - Parsing: Response format issues
         * - Validation: Business rule failures
         * - Unknown: Unexpected errors
         *
         * @param error Exception from use case
         * @return ErrorType for UI display
         */
        private fun mapErrorToType(error: Throwable): ErrorType =
            when (error) {
                is DomainException.ValidationException ->
                    ErrorType.Validation(
                        messageRes = R.string.error_activation_failed_message,
                        androidVersion = error.androidVersion,
                    )
                is DomainException.NetworkException.NoConnection ->
                    ErrorType.Network(
                        messageRes = R.string.error_no_internet,
                        isTimeout = false,
                    )
                is DomainException.NetworkException.Timeout ->
                    ErrorType.Network(
                        messageRes = R.string.error_request_timeout,
                        isTimeout = true,
                    )
                is DomainException.HttpException.ClientError ->
                    ErrorType.Server(
                        messageRes = R.string.error_service_unavailable_short,
                        statusCode = error.statusCode,
                    )
                is DomainException.HttpException.ServerError ->
                    ErrorType.Server(
                        messageRes = R.string.error_service_unavailable_short,
                        statusCode = error.statusCode,
                    )
                is DomainException.ParsingException ->
                    ErrorType.Parsing(
                        messageRes = R.string.error_service_unavailable,
                    )
                else ->
                    ErrorType.Unknown(
                        messageRes = R.string.error_unknown_message,
                    )
            }

        /**
         * Clear error state and return to idle.
         *
         * Called when user dismisses error modal.
         */
        fun clearError() {
            _uiState.value = ActivationUiState.Idle
            Timber.d("Error cleared, returning to idle state")
        }

        /**
         * Retry activation after error.
         *
         * Convenience method that clears error and retries with same code.
         *
         * @param code UUID code to retry activation
         */
        fun retryActivation(code: String) {
            clearError()
            activateCard(code)
        }
    }

/**
 * UI state for Activation Screen.
 *
 * Represents all possible UI states during activation flow.
 */
sealed class ActivationUiState {
    /**
     * Idle state - waiting for user to initiate activation.
     */
    data object Idle : ActivationUiState()

    /**
     * Loading state - API call in progress.
     */
    data object Loading : ActivationUiState()

    /**
     * Success state - activation completed successfully.
     */
    data object Success : ActivationUiState()

    /**
     * Error state - activation failed with categorized error.
     *
     * @property errorType Categorized error for UI display
     */
    data class Error(
        val errorType: ErrorType,
    ) : ActivationUiState()
}

/**
 * Error types for UI display (FR017).
 *
 * Each error type has appropriate title and message resource IDs for error modal.
 * Uses resource IDs to support localization and avoid hardcoded strings in ViewModels.
 */
sealed class ErrorType {
    abstract val messageRes: Int

    /**
     * Get error modal title resource ID based on error type.
     */
    @get:StringRes
    val titleRes: Int
        get() =
            when (this) {
                is Validation -> R.string.error_activation_failed_title
                is Network -> R.string.error_connection_title
                is Server, is Parsing -> R.string.error_service_title
                is Unknown -> R.string.error_unknown_title
            }

    /**
     * Network error (no connection or timeout).
     *
     * @property messageRes String resource ID for error message
     * @property isTimeout Whether error was a timeout (vs no connection)
     */
    data class Network(
        @StringRes override val messageRes: Int,
        val isTimeout: Boolean,
    ) : ErrorType()

    /**
     * Server or HTTP error.
     *
     * @property messageRes String resource ID for error message
     * @property statusCode HTTP status code (for debugging)
     */
    data class Server(
        @StringRes override val messageRes: Int,
        val statusCode: Int? = null,
    ) : ErrorType()

    /**
     * Parsing or serialization error.
     *
     * @property messageRes String resource ID for error message
     */
    data class Parsing(
        @StringRes override val messageRes: Int,
    ) : ErrorType()

    /**
     * Validation error (android ≤ 277028).
     *
     * @property messageRes String resource ID for error message
     * @property androidVersion The android version that failed validation
     */
    data class Validation(
        @StringRes override val messageRes: Int,
        val androidVersion: Int? = null,
    ) : ErrorType()

    /**
     * Unknown or unexpected error.
     *
     * @property messageRes String resource ID for error message
     */
    data class Unknown(
        @StringRes override val messageRes: Int,
    ) : ErrorType()
}
