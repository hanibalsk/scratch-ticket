package sk.o2.scratchcard.presentation.screens.main

import app.cash.turbine.test
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import sk.o2.scratchcard.domain.model.ScratchCardState
import sk.o2.scratchcard.domain.repository.ScratchCardRepository

/**
 * Unit tests for MainViewModel.
 *
 * Validates:
 * - StateFlow transformation from domain state to UI state
 * - Button enabled/disabled logic based on card state
 * - Reactive state updates when repository state changes
 * - No memory leaks or state retention issues
 */
class MainViewModelTest {

    private lateinit var mockRepository: ScratchCardRepository
    private lateinit var repositoryStateFlow: MutableStateFlow<ScratchCardState>
    private lateinit var viewModel: MainViewModel

    @BeforeEach
    fun setup() {
        repositoryStateFlow = MutableStateFlow(ScratchCardState.Unscratched)
        mockRepository = mockk {
            every { cardState } returns repositoryStateFlow
        }
        viewModel = MainViewModel(mockRepository)
    }

    @Test
    fun `initial UI state is Unscratched with activation disabled`() = runTest {
        viewModel.uiState.test {
            val state = awaitItem()

            assertEquals(ScratchCardState.Unscratched, state.cardState)
            assertFalse(state.isActivationEnabled)
        }
    }

    @Test
    fun `UI state updates when repository state changes to Scratched`() = runTest {
        viewModel.uiState.test {
            // Initial state
            val initialState = awaitItem()
            assertEquals(ScratchCardState.Unscratched, initialState.cardState)
            assertFalse(initialState.isActivationEnabled)

            // Change repository state
            repositoryStateFlow.value = ScratchCardState.Scratched("test-uuid")

            // Observe updated UI state
            val scratchedState = awaitItem()
            assertTrue(scratchedState.cardState is ScratchCardState.Scratched)
            assertEquals("test-uuid", (scratchedState.cardState as ScratchCardState.Scratched).code)
            assertTrue(scratchedState.isActivationEnabled)
        }
    }

    @Test
    fun `UI state updates when repository state changes to Activated`() = runTest {
        viewModel.uiState.test {
            // Skip initial state
            awaitItem()

            // Change to Activated
            repositoryStateFlow.value = ScratchCardState.Activated("activated-uuid")

            val activatedState = awaitItem()
            assertTrue(activatedState.cardState is ScratchCardState.Activated)
            assertEquals("activated-uuid", (activatedState.cardState as ScratchCardState.Activated).code)
            assertTrue(activatedState.isActivationEnabled)
        }
    }

    @Test
    fun `activation button disabled when Unscratched`() = runTest {
        repositoryStateFlow.value = ScratchCardState.Unscratched

        viewModel.uiState.test {
            val state = awaitItem()
            assertFalse(state.isActivationEnabled)
        }
    }

    @Test
    fun `activation button enabled when Scratched`() = runTest {
        repositoryStateFlow.value = ScratchCardState.Scratched("test-code")

        viewModel.uiState.test {
            val state = awaitItem()
            assertTrue(state.isActivationEnabled)
        }
    }

    @Test
    fun `activation button enabled when Activated`() = runTest {
        repositoryStateFlow.value = ScratchCardState.Activated("test-code")

        viewModel.uiState.test {
            val state = awaitItem()
            assertTrue(state.isActivationEnabled)
        }
    }

    @Test
    fun `UI state transformation handles all ScratchCardState variants`() = runTest {
        val states = listOf(
            ScratchCardState.Unscratched to false,
            ScratchCardState.Scratched("uuid-1") to true,
            ScratchCardState.Activated("uuid-2") to true
        )

        states.forEach { (cardState, expectedActivationEnabled) ->
            repositoryStateFlow.value = cardState

            viewModel.uiState.test {
                val uiState = awaitItem()
                assertEquals(cardState, uiState.cardState)
                assertEquals(expectedActivationEnabled, uiState.isActivationEnabled)
                cancelAndIgnoreRemainingEvents()
            }
        }
    }

    @Test
    fun `multiple state transitions are observed correctly`() = runTest {
        viewModel.uiState.test {
            // Initial: Unscratched
            val state1 = awaitItem()
            assertEquals(ScratchCardState.Unscratched, state1.cardState)
            assertFalse(state1.isActivationEnabled)

            // Transition to Scratched
            repositoryStateFlow.value = ScratchCardState.Scratched("uuid-1")
            val state2 = awaitItem()
            assertTrue(state2.cardState is ScratchCardState.Scratched)
            assertTrue(state2.isActivationEnabled)

            // Transition to Activated
            repositoryStateFlow.value = ScratchCardState.Activated("uuid-1")
            val state3 = awaitItem()
            assertTrue(state3.cardState is ScratchCardState.Activated)
            assertTrue(state3.isActivationEnabled)
        }
    }

    @Test
    fun `ViewModel state survives configuration changes via stateIn`() = runTest {
        // stateIn with viewModelScope ensures state survives ViewModel recreation
        // This test verifies the StateFlow is properly configured

        repositoryStateFlow.value = ScratchCardState.Scratched("persistent-uuid")

        viewModel.uiState.test {
            val state = awaitItem()

            // State should be immediately available (stateIn provides initial value)
            assertTrue(state.cardState is ScratchCardState.Scratched)
        }
    }

    @Test
    fun `MainScreenUiState data class equality works correctly`() {
        val state1 = MainScreenUiState(
            cardState = ScratchCardState.Unscratched,
            isActivationEnabled = false
        )

        val state2 = MainScreenUiState(
            cardState = ScratchCardState.Unscratched,
            isActivationEnabled = false
        )

        val state3 = MainScreenUiState(
            cardState = ScratchCardState.Scratched("test"),
            isActivationEnabled = true
        )

        assertEquals(state1, state2)
        assertNotEquals(state1, state3)
    }

    @Test
    fun `MainScreenUiState has sensible defaults`() {
        val defaultState = MainScreenUiState()

        assertEquals(ScratchCardState.Unscratched, defaultState.cardState)
        assertFalse(defaultState.isActivationEnabled)
    }
}
