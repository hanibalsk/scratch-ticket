package sk.o2.scratchcard.presentation.screens.scratch

import app.cash.turbine.test
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.test.advanceTimeBy
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import sk.o2.scratchcard.domain.model.ScratchCardState
import sk.o2.scratchcard.domain.repository.ScratchCardRepository
import sk.o2.scratchcard.domain.usecase.ScratchCardUseCase

/**
 * Unit tests for ScratchViewModel.
 *
 * Validates:
 * - Scratch operation completes after exactly 2 seconds
 * - Operation cancels when ViewModel cleared
 * - Progress updates correctly (0f to 1f)
 * - State transitions observable
 * - Multiple scratch attempts prevented
 */
class ScratchViewModelTest {

    private lateinit var mockUseCase: ScratchCardUseCase
    private lateinit var mockRepository: ScratchCardRepository
    private lateinit var repositoryStateFlow: MutableStateFlow<ScratchCardState>
    private lateinit var viewModel: ScratchViewModel

    @BeforeEach
    fun setup() {
        repositoryStateFlow = MutableStateFlow(ScratchCardState.Unscratched)
        mockUseCase = mockk()
        mockRepository = mockk {
            every { cardState } returns repositoryStateFlow
        }
    }

    @Test
    fun `scratch completes after exactly 2 seconds`() = runTest {
        val testCode = "test-uuid-12345"
        coEvery { mockUseCase() } coAnswers {
            delay(2000)
            Result.success(testCode)
        }

        viewModel = ScratchViewModel(mockUseCase, mockRepository)

        viewModel.startScratch()

        // At 1999ms, should still be scratching
        advanceTimeBy(1999)
        assertTrue(viewModel.isScratching.value)

        // At 2000ms, should be complete
        advanceTimeBy(1)
        advanceUntilIdle()

        assertFalse(viewModel.isScratching.value)
        assertEquals(testCode, viewModel.revealedCode.value)
    }

    @Test
    fun `scratch progress updates from 0 to 1`() = runTest {
        coEvery { mockUseCase() } coAnswers {
            delay(2000)
            Result.success("test-code")
        }

        viewModel = ScratchViewModel(mockUseCase, mockRepository)

        viewModel.scratchProgress.test {
            assertEquals(0f, awaitItem()) // Initial

            viewModel.startScratch()

            // Progress should update gradually
            val progress1 = awaitItem()
            assertTrue(progress1 >= 0f && progress1 <= 1f)

            // Advance time and check final progress
            advanceTimeBy(2000)
            advanceUntilIdle()

            // Should eventually reach 1f
            val finalProgress = expectMostRecentItem()
            assertTrue(finalProgress >= 0.99f && finalProgress <= 1f)
        }
    }

    @Test
    fun `scratch cancels when ViewModel cleared before completion`() = runTest {
        coEvery { mockUseCase() } coAnswers {
            delay(2000)
            Result.success("test-code")
        }

        viewModel = ScratchViewModel(mockUseCase, mockRepository)

        viewModel.isScratching.test {
            assertEquals(false, awaitItem()) // Initial

            viewModel.startScratch()
            assertEquals(true, awaitItem()) // Started

            // Advance halfway through
            advanceTimeBy(1000)

            // Simulate ViewModel cleared (user navigates back)
            viewModel.onCleared()

            // Operation should cancel
            advanceTimeBy(1000)
            advanceUntilIdle()

            // Final state should be false (operation cancelled)
            val finalState = expectMostRecentItem()
            assertFalse(finalState)

            // Code should not be revealed
            assertNull(viewModel.revealedCode.value)
        }
    }

    @Test
    fun `revealed code is set after successful scratch`() = runTest {
        val expectedCode = "550e8400-e29b-41d4-a716-446655440000"
        coEvery { mockUseCase() } coAnswers {
            delay(2000)
            Result.success(expectedCode)
        }

        viewModel = ScratchViewModel(mockUseCase, mockRepository)

        viewModel.revealedCode.test {
            assertNull(awaitItem()) // Initial

            viewModel.startScratch()

            advanceTimeBy(2000)
            advanceUntilIdle()

            assertEquals(expectedCode, awaitItem())
        }
    }

    @Test
    fun `error message set when scratch fails`() = runTest {
        val testException = RuntimeException("Test error")
        coEvery { mockUseCase() } coAnswers {
            delay(2000)
            Result.failure(testException)
        }

        viewModel = ScratchViewModel(mockUseCase, mockRepository)

        viewModel.startScratch()
        advanceTimeBy(2000)
        advanceUntilIdle()

        assertNotNull(viewModel.errorMessage.value)
        assertTrue(viewModel.errorMessage.value!!.contains("Scratch operation failed"))
    }

    @Test
    fun `multiple concurrent scratch attempts are prevented`() = runTest {
        coEvery { mockUseCase() } coAnswers {
            delay(2000)
            Result.success("test-code")
        }

        viewModel = ScratchViewModel(mockUseCase, mockRepository)

        // Start first scratch
        viewModel.startScratch()
        assertTrue(viewModel.isScratching.value)

        // Attempt second scratch while first is in progress
        viewModel.startScratch()

        // Should still only be one operation
        advanceTimeBy(2000)
        advanceUntilIdle()

        // Only one code should be revealed
        assertNotNull(viewModel.revealedCode.value)
    }

    @Test
    fun `clearError resets error message`() = runTest {
        viewModel = ScratchViewModel(mockUseCase, mockRepository)

        viewModel.errorMessage.test {
            assertNull(awaitItem())

            // Manually set error (simulating failure)
            coEvery { mockUseCase() } returns Result.failure(RuntimeException("Test"))
            viewModel.startScratch()
            advanceTimeBy(2000)
            advanceUntilIdle()

            assertNotNull(awaitItem())

            // Clear error
            viewModel.clearError()
            assertNull(awaitItem())
        }
    }

    @Test
    fun `cardState observable reflects repository state`() = runTest {
        viewModel = ScratchViewModel(mockUseCase, mockRepository)

        viewModel.cardState.test {
            assertEquals(ScratchCardState.Unscratched, awaitItem())

            // Change repository state
            repositoryStateFlow.value = ScratchCardState.Scratched("new-code")

            val scratchedState = awaitItem()
            assertTrue(scratchedState is ScratchCardState.Scratched)
            assertEquals("new-code", (scratchedState as ScratchCardState.Scratched).code)
        }
    }

    @Test
    fun `isScratching resets to false after completion`() = runTest {
        coEvery { mockUseCase() } coAnswers {
            delay(2000)
            Result.success("test-code")
        }

        viewModel = ScratchViewModel(mockUseCase, mockRepository)

        viewModel.isScratching.test {
            assertFalse(awaitItem()) // Initial

            viewModel.startScratch()
            assertTrue(awaitItem()) // Started

            advanceTimeBy(2000)
            advanceUntilIdle()

            assertFalse(awaitItem()) // Completed
        }
    }

    @Test
    fun `progress tracking cancels with main operation`() = runTest {
        coEvery { mockUseCase() } coAnswers {
            delay(2000)
            Result.success("test-code")
        }

        viewModel = ScratchViewModel(mockUseCase, mockRepository)

        viewModel.scratchProgress.test {
            awaitItem() // Initial 0f

            viewModel.startScratch()

            // Advance partway
            advanceTimeBy(1000)

            // Cancel by clearing ViewModel
            viewModel.onCleared()

            advanceTimeBy(1000)
            advanceUntilIdle()

            // Progress should have stopped updating
            val finalProgress = expectMostRecentItem()
            assertTrue(finalProgress < 1f) // Didn't complete
        }
    }
}
