package sk.o2.scratchcard.presentation.screens.scratch

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import sk.o2.scratchcard.domain.model.ScratchCardState
import sk.o2.scratchcard.domain.repository.ScratchCardRepository
import sk.o2.scratchcard.domain.usecase.ScratchCardUseCase
import timber.log.Timber
import javax.inject.Inject

/**
 * ViewModel for Scratch Screen.
 *
 * Manages scratch operation with cancellable coroutine (viewModelScope).
 * Provides progress tracking for determinate loading indicator.
 *
 * Key Features:
 * - Automatic cancellation when ViewModel cleared (back navigation)
 * - Progress updates every 20ms for smooth UI feedback
 * - State observation from repository
 */
@HiltViewModel
class ScratchViewModel
    @Inject
    constructor(
        private val scratchCardUseCase: ScratchCardUseCase,
        repository: ScratchCardRepository,
    ) : ViewModel() {
        /**
         * Observe card state from repository.
         * Updates automatically when scratch completes.
         */
        val cardState: StateFlow<ScratchCardState> =
            repository.cardState
                .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), ScratchCardState.Unscratched)

        /**
         * Indicates if scratch operation is in progress.
         */
        private val _isScratching = MutableStateFlow(false)
        val isScratching: StateFlow<Boolean> = _isScratching.asStateFlow()

        /**
         * Progress of scratch operation (0f to 1f) for determinate indicator.
         */
        private val _scratchProgress = MutableStateFlow(0f)
        val scratchProgress: StateFlow<Float> = _scratchProgress.asStateFlow()

        /**
         * Revealed UUID code after scratch completion.
         */
        private val _revealedCode = MutableStateFlow<String?>(null)
        val revealedCode: StateFlow<String?> = _revealedCode.asStateFlow()

        /**
         * Error message if scratch operation fails.
         */
        private val _errorMessage = MutableStateFlow<String?>(null)
        val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()

        /**
         * Start scratch operation with progress tracking.
         *
         * Uses viewModelScope for automatic cancellation when user navigates back.
         * Progress is updated every 20ms for smooth animation (2000ms / 100 steps).
         *
         * Cancellation Behavior (FR010):
         * - When user presses back during scratch, ViewModel is cleared
         * - viewModelScope automatically cancels all coroutines
         * - State remains Unscratched (operation didn't complete)
         */
        fun startScratch() {
            if (_isScratching.value) {
                Timber.w("Scratch already in progress, ignoring request")
                return
            }

            viewModelScope.launch {
                try {
                    _isScratching.value = true
                    _scratchProgress.value = 0f
                    _errorMessage.value = null

                    // Launch progress updater
                    val progressJob =
                        launch {
                            for (i in 0..100) {
                                _scratchProgress.value = i / 100f
                                delay(20) // 2000ms / 100 steps = 20ms per step
                            }
                        }

                    // Execute scratch operation (2-second delay)
                    scratchCardUseCase()
                        .onSuccess { code ->
                            _revealedCode.value = code
                            _scratchProgress.value = 1f
                            Timber.d("Scratch completed successfully: $code")
                        }.onFailure { error ->
                            _errorMessage.value = "Scratch operation failed: ${error.message}"
                            Timber.e(error, "Scratch operation failed")
                        }

                    progressJob.cancel()
                } finally {
                    _isScratching.value = false
                }
            }
        }

        /**
         * Clear error message.
         */
        fun clearError() {
            _errorMessage.value = null
        }

        /**
         * Called when ViewModel is cleared (user navigates back).
         * viewModelScope automatically cancels all coroutines.
         */
        override fun onCleared() {
            super.onCleared()
            Timber.d("ScratchViewModel cleared - all coroutines cancelled")
        }
    }
