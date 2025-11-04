package sk.o2.scratchcard.presentation.screens.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import sk.o2.scratchcard.domain.model.ScratchCardState
import sk.o2.scratchcard.domain.repository.ScratchCardRepository
import javax.inject.Inject

/**
 * ViewModel for Main Screen.
 *
 * Responsibilities:
 * - Observes card state from repository
 * - Transforms domain state to UI-specific state
 * - Calculates button enabled/disabled states
 * - Provides reactive state updates to UI
 *
 * State Flow:
 * - Repository (domain) → ViewModel (transforms) → UI (observes)
 * - Single source of truth: ScratchCardRepository.cardState
 *
 * @property repository Scratch card repository (injected via Hilt)
 */
@HiltViewModel
class MainViewModel
    @Inject
    constructor(
        repository: ScratchCardRepository,
    ) : ViewModel() {
        /**
         * UI state derived from repository card state.
         *
         * Transformation logic:
         * - Includes current card state
         * - Calculates whether activation button should be enabled
         *
         * The StateFlow is hot and shared across multiple collectors.
         * Uses WhileSubscribed(5000) to keep upstream active for 5 seconds
         * after last collector disappears, allowing quick re-subscription
         * without restarting the flow.
         */
        val uiState: StateFlow<MainScreenUiState> =
            repository.cardState
                .map { cardState ->
                    MainScreenUiState(
                        cardState = cardState,
                        isActivationEnabled =
                            when (cardState) {
                                is ScratchCardState.Unscratched -> false
                                is ScratchCardState.Scratched -> true
                                is ScratchCardState.Activated -> true
                            },
                    )
                }.stateIn(
                    scope = viewModelScope,
                    started = SharingStarted.WhileSubscribed(5000),
                    initialValue = MainScreenUiState(),
                )
    }

/**
 * UI state for Main Screen.
 *
 * Represents the complete UI state derived from domain state.
 * All UI decisions (button states, visibility, etc.) are calculated here
 * to keep the Composable stateless and testable.
 *
 * @property cardState Current scratch card state from domain layer
 * @property isActivationEnabled Whether "Go to Activation Screen" button should be enabled
 */
data class MainScreenUiState(
    val cardState: ScratchCardState = ScratchCardState.Unscratched,
    val isActivationEnabled: Boolean = false,
)
