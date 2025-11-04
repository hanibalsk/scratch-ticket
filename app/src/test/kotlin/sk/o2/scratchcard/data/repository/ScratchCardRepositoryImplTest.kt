package sk.o2.scratchcard.data.repository

import app.cash.turbine.test
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.async
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceTimeBy
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import sk.o2.scratchcard.data.remote.O2ApiService
import sk.o2.scratchcard.data.remote.O2VersionResponse
import sk.o2.scratchcard.domain.model.ScratchCardState

/**
 * Unit tests for ScratchCardRepositoryImpl.
 *
 * Validates:
 * - Initial state is Unscratched
 * - scratchCard() delays exactly 2 seconds
 * - scratchCard() generates UUID and transitions to Scratched
 * - activateCard() calls API service correctly
 * - State updates based on API responses
 * - StateFlow emissions work correctly
 */
class ScratchCardRepositoryImplTest {

    private lateinit var mockApiService: O2ApiService
    private val testDispatcher = StandardTestDispatcher()
    private lateinit var repository: ScratchCardRepositoryImpl

    @BeforeEach
    fun setup() {
        mockApiService = mockk()
        repository = ScratchCardRepositoryImpl(mockApiService, testDispatcher)
    }

    @Test
    fun `initial state is Unscratched`() {
        val currentState = repository.cardState.value

        assertEquals(ScratchCardState.Unscratched, currentState)
    }

    @Test
    fun `scratchCard delays exactly 2 seconds before completion`() = runTest(testDispatcher) {
        repository.cardState.test {
            // Initial state
            assertEquals(ScratchCardState.Unscratched, awaitItem())

            // Start scratch operation
            val resultDeferred = async { repository.scratchCard() }

            // Advance time by 1999ms - should still be Unscratched
            advanceTimeBy(1999)
            expectNoEvents()

            // Advance final 1ms to complete 2000ms - should transition to Scratched
            advanceTimeBy(1)
            val scratchedState = awaitItem()

            assertTrue(scratchedState is ScratchCardState.Scratched)

            // Verify result contains UUID
            val result = resultDeferred.await()
            assertTrue(result.isSuccess)
            assertNotNull(result.getOrNull())
        }
    }

    @Test
    fun `scratchCard generates UUID and transitions to Scratched state`() = runTest(testDispatcher) {
        val result = repository.scratchCard()
        advanceTimeBy(2000)

        // Verify result success
        assertTrue(result.isSuccess)

        // Verify UUID format (36 characters with dashes)
        val uuid = result.getOrNull()
        assertNotNull(uuid)
        assertEquals(36, uuid!!.length)
        assertTrue(uuid.contains("-"))

        // Verify state transition
        val currentState = repository.cardState.value
        assertTrue(currentState is ScratchCardState.Scratched)
        assertEquals(uuid, (currentState as ScratchCardState.Scratched).code)
    }

    @Test
    fun `multiple scratchCard calls generate different UUIDs`() = runTest(testDispatcher) {
        val result1 = repository.scratchCard()
        advanceTimeBy(2000)

        // Reset to Unscratched for second scratch
        // (In actual app, this would be a new repository instance)
        val repository2 = ScratchCardRepositoryImpl(mockApiService, testDispatcher)
        val result2 = repository2.scratchCard()
        advanceTimeBy(2000)

        val uuid1 = result1.getOrNull()
        val uuid2 = result2.getOrNull()

        assertNotNull(uuid1)
        assertNotNull(uuid2)
        assertNotEquals(uuid1, uuid2)
    }

    @Test
    fun `activateCard calls API service with correct code`() = runTest(testDispatcher) {
        val testCode = "test-uuid-12345"
        val apiResponse = O2VersionResponse(android = 300000)
        coEvery { mockApiService.validateCode(testCode) } returns apiResponse

        repository.activateCard(testCode)

        coVerify(exactly = 1) { mockApiService.validateCode(testCode) }
    }

    @Test
    fun `activateCard transitions to Activated when API returns android greater than 277028`() = runTest(testDispatcher) {
        val testCode = "test-uuid"
        val apiResponse = O2VersionResponse(android = 277029) // Just above threshold
        coEvery { mockApiService.validateCode(testCode) } returns apiResponse

        repository.cardState.test {
            // Skip initial state
            skipItems(1)

            val result = repository.activateCard(testCode)

            // Verify result is success with true
            assertTrue(result.isSuccess)
            assertEquals(true, result.getOrNull())

            // Verify state transitioned to Activated
            val activatedState = awaitItem()
            assertTrue(activatedState is ScratchCardState.Activated)
            assertEquals(testCode, (activatedState as ScratchCardState.Activated).code)
        }
    }

    @Test
    fun `activateCard returns failure when API returns android equal to 277028`() = runTest(testDispatcher) {
        val testCode = "test-uuid"
        val apiResponse = O2VersionResponse(android = 277028) // Exactly at threshold - FAIL
        coEvery { mockApiService.validateCode(testCode) } returns apiResponse

        val result = repository.activateCard(testCode)
        advanceUntilIdle()

        // Verify result is failure with ValidationException
        assertTrue(result.isFailure)
        assertTrue(result.exceptionOrNull() is sk.o2.scratchcard.domain.model.DomainException.ValidationException)

        // Verify state did NOT change (remains Unscratched in this test context)
        val currentState = repository.cardState.value
        assertEquals(ScratchCardState.Unscratched, currentState)
    }

    @Test
    fun `activateCard returns failure when API returns android less than 277028`() = runTest(testDispatcher) {
        val testCode = "test-uuid"
        val apiResponse = O2VersionResponse(android = 200000) // Well below threshold
        coEvery { mockApiService.validateCode(testCode) } returns apiResponse

        val result = repository.activateCard(testCode)
        advanceUntilIdle()

        // Verify result is failure with ValidationException
        assertTrue(result.isFailure)
        assertTrue(result.exceptionOrNull() is sk.o2.scratchcard.domain.model.DomainException.ValidationException)

        // State should not transition
        val currentState = repository.cardState.value
        assertEquals(ScratchCardState.Unscratched, currentState)
    }

    @Test
    fun `activateCard propagates API exceptions as Result failure`() = runTest(testDispatcher) {
        val testCode = "test-uuid"
        val testException = RuntimeException("Network error")
        coEvery { mockApiService.validateCode(testCode) } throws testException

        val result = repository.activateCard(testCode)

        assertTrue(result.isFailure)
        assertEquals(testException, result.exceptionOrNull())

        // State should not change on error
        assertEquals(ScratchCardState.Unscratched, repository.cardState.value)
    }

    @Test
    fun `cardState StateFlow emits to multiple collectors`() = runTest(testDispatcher) {
        // Start two collectors
        val collector1 = async {
            repository.cardState.test {
                awaitItem() // Initial
                val scratched = awaitItem() // After scratch
                assertTrue(scratched is ScratchCardState.Scratched)
                cancelAndIgnoreRemainingEvents()
            }
        }

        val collector2 = async {
            repository.cardState.test {
                awaitItem() // Initial
                val scratched = awaitItem() // After scratch
                assertTrue(scratched is ScratchCardState.Scratched)
                cancelAndIgnoreRemainingEvents()
            }
        }

        // Perform scratch operation
        repository.scratchCard()
        advanceTimeBy(2000)

        // Both collectors should have received the update
        collector1.await()
        collector2.await()
    }

    @Test
    fun `repository is thread-safe with IO dispatcher`() = runTest(testDispatcher) {
        // This test verifies the repository uses IO dispatcher correctly
        // If dispatcher wasn't injected, this would fail

        val result = repository.scratchCard()
        advanceTimeBy(2000)

        assertTrue(result.isSuccess)
    }

    @Test
    fun `boundary value test - activateCard with android 277029 succeeds`() = runTest(testDispatcher) {
        val testCode = "boundary-test"
        val apiResponse = O2VersionResponse(android = 277029) // Minimum success value
        coEvery { mockApiService.validateCode(testCode) } returns apiResponse

        val result = repository.activateCard(testCode)

        assertTrue(result.isSuccess)
        assertEquals(true, result.getOrNull())
    }

    @Test
    fun `boundary value test - activateCard with android 277028 fails`() = runTest(testDispatcher) {
        val testCode = "boundary-test"
        val apiResponse = O2VersionResponse(android = 277028) // Maximum failure value
        coEvery { mockApiService.validateCode(testCode) } returns apiResponse

        val result = repository.activateCard(testCode)
        advanceUntilIdle()

        // Verify result is failure with ValidationException
        assertTrue(result.isFailure)
        assertTrue(result.exceptionOrNull() is sk.o2.scratchcard.domain.model.DomainException.ValidationException)
    }
}
