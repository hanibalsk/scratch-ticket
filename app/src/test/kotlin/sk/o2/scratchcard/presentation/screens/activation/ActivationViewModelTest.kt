package sk.o2.scratchcard.presentation.screens.activation

import app.cash.turbine.test
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import sk.o2.scratchcard.domain.model.DomainException
import sk.o2.scratchcard.domain.model.ScratchCardState
import sk.o2.scratchcard.domain.repository.ScratchCardRepository
import sk.o2.scratchcard.domain.usecase.ActivateCardUseCase

/**
 * Unit tests for ActivationViewModel.
 *
 * Validates:
 * - Non-cancellable activation pattern (GlobalScope + IO dispatcher)
 * - Error categorization (Network, HTTP, Parsing, Validation)
 * - UI state transitions (Idle → Loading → Success/Error)
 * - Retry functionality
 */
class ActivationViewModelTest {

    private lateinit var mockUseCase: ActivateCardUseCase
    private lateinit var mockRepository: ScratchCardRepository
    private lateinit var repositoryStateFlow: MutableStateFlow<ScratchCardState>
    private val testDispatcher = StandardTestDispatcher()
    private lateinit var viewModel: ActivationViewModel

    @BeforeEach
    fun setup() {
        Dispatchers.setMain(testDispatcher)

        repositoryStateFlow = MutableStateFlow(ScratchCardState.Scratched("test-code"))
        mockUseCase = mockk()
        mockRepository = mockk {
            every { cardState } returns repositoryStateFlow
        }

        viewModel = ActivationViewModel(mockUseCase, mockRepository, testDispatcher)
    }

    @Test
    fun `initial UI state is Idle`() = runTest {
        viewModel.uiState.test {
            val state = awaitItem()
            assertTrue(state is ActivationUiState.Idle)
        }
    }

    @Test
    fun `activateCard transitions to Loading then Success`() = runTest(testDispatcher) {
        coEvery { mockUseCase("test-code") } returns Result.success(true)

        viewModel.uiState.test {
            assertEquals(ActivationUiState.Idle, awaitItem())

            viewModel.activateCard("test-code")

            assertEquals(ActivationUiState.Loading, awaitItem())

            advanceUntilIdle()

            val finalState = awaitItem()
            assertTrue(finalState is ActivationUiState.Success)
        }
    }

    @Test
    fun `activateCard maps ValidationException to Validation error`() = runTest(testDispatcher) {
        val validationError = DomainException.ValidationException(277028)
        coEvery { mockUseCase("test-code") } returns Result.failure(validationError)

        viewModel.uiState.test {
            awaitItem() // Idle

            viewModel.activateCard("test-code")
            awaitItem() // Loading

            advanceUntilIdle()

            val errorState = awaitItem() as ActivationUiState.Error
            assertTrue(errorState.errorType is ErrorType.Validation)
            assertEquals(277028, (errorState.errorType as ErrorType.Validation).androidVersion)
        }
    }

    @Test
    fun `activateCard maps NoConnection to Network error`() = runTest(testDispatcher) {
        val networkError = DomainException.NetworkException.NoConnection(
            java.net.UnknownHostException("api.o2.sk")
        )
        coEvery { mockUseCase("test-code") } returns Result.failure(networkError)

        viewModel.uiState.test {
            awaitItem() // Idle

            viewModel.activateCard("test-code")
            awaitItem() // Loading

            advanceUntilIdle()

            val errorState = awaitItem() as ActivationUiState.Error
            assertTrue(errorState.errorType is ErrorType.Network)
            assertEquals("No internet connection", errorState.errorType.message)
            assertFalse((errorState.errorType as ErrorType.Network).isTimeout)
        }
    }

    @Test
    fun `activateCard maps Timeout to Network error with timeout flag`() = runTest(testDispatcher) {
        val timeoutError = DomainException.NetworkException.Timeout(
            java.net.SocketTimeoutException("timeout")
        )
        coEvery { mockUseCase("test-code") } returns Result.failure(timeoutError)

        viewModel.uiState.test {
            awaitItem() // Idle

            viewModel.activateCard("test-code")
            awaitItem() // Loading

            advanceUntilIdle()

            val errorState = awaitItem() as ActivationUiState.Error
            assertTrue(errorState.errorType is ErrorType.Network)
            assertEquals("Request timed out", errorState.errorType.message)
            assertTrue((errorState.errorType as ErrorType.Network).isTimeout)
        }
    }

    @Test
    fun `activateCard maps ServerError to Server error`() = runTest(testDispatcher) {
        val serverError = DomainException.HttpException.ServerError(
            500,
            mockk(relaxed = true)
        )
        coEvery { mockUseCase("test-code") } returns Result.failure(serverError)

        viewModel.uiState.test {
            awaitItem() // Idle

            viewModel.activateCard("test-code")
            awaitItem() // Loading

            advanceUntilIdle()

            val errorState = awaitItem() as ActivationUiState.Error
            assertTrue(errorState.errorType is ErrorType.Server)
            assertEquals(500, (errorState.errorType as ErrorType.Server).statusCode)
        }
    }

    @Test
    fun `activateCard maps ParsingException to Parsing error`() = runTest(testDispatcher) {
        val parsingError = DomainException.ParsingException(
            kotlinx.serialization.SerializationException("Invalid JSON")
        )
        coEvery { mockUseCase("test-code") } returns Result.failure(parsingError)

        viewModel.uiState.test {
            awaitItem() // Idle

            viewModel.activateCard("test-code")
            awaitItem() // Loading

            advanceUntilIdle()

            val errorState = awaitItem() as ActivationUiState.Error
            assertTrue(errorState.errorType is ErrorType.Parsing)
        }
    }

    @Test
    fun `clearError returns to Idle state`() = runTest {
        coEvery { mockUseCase("test-code") } returns Result.failure(
            DomainException.ValidationException(277028)
        )

        viewModel.activateCard("test-code")
        advanceUntilIdle()

        viewModel.uiState.test {
            val errorState = awaitItem()
            assertTrue(errorState is ActivationUiState.Error)

            viewModel.clearError()

            val idleState = awaitItem()
            assertTrue(idleState is ActivationUiState.Idle)
        }
    }

    @Test
    fun `retryActivation clears error and retries`() = runTest(testDispatcher) {
        // First call fails
        coEvery { mockUseCase("test-code") } returns Result.failure(
            DomainException.NetworkException.Timeout(mockk())
        )

        viewModel.activateCard("test-code")
        advanceUntilIdle()

        // Verify error state
        assertTrue(viewModel.uiState.value is ActivationUiState.Error)

        // Second call succeeds
        coEvery { mockUseCase("test-code") } returns Result.success(true)

        viewModel.uiState.test {
            awaitItem() // Current error state

            viewModel.retryActivation("test-code")

            val idleState = awaitItem()
            assertTrue(idleState is ActivationUiState.Idle)

            val loadingState = awaitItem()
            assertTrue(loadingState is ActivationUiState.Loading)

            advanceUntilIdle()

            val successState = awaitItem()
            assertTrue(successState is ActivationUiState.Success)
        }
    }

    @Test
    fun `error type titles are correct`() {
        assertEquals("Activation Failed", ErrorType.Validation("msg").getTitle())
        assertEquals("Connection Error", ErrorType.Network("msg", false).getTitle())
        assertEquals("Service Error", ErrorType.Server("msg").getTitle())
        assertEquals("Service Error", ErrorType.Parsing("msg").getTitle())
        assertEquals("Error", ErrorType.Unknown("msg").getTitle())
    }
}
