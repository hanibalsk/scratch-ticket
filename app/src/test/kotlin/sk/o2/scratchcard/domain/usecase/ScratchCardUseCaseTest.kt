package sk.o2.scratchcard.domain.usecase

import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import sk.o2.scratchcard.domain.repository.ScratchCardRepository

/**
 * Unit tests for ScratchCardUseCase.
 *
 * Validates:
 * - Use case correctly delegates to repository
 * - Result propagation works correctly
 * - Error handling is transparent
 */
class ScratchCardUseCaseTest {

    private val mockRepository = mockk<ScratchCardRepository>()
    private val useCase = ScratchCardUseCase(mockRepository)

    @Test
    fun `invoke delegates to repository scratchCard`() = runTest {
        val expectedCode = "test-uuid-12345"
        coEvery { mockRepository.scratchCard() } returns Result.success(expectedCode)

        val result = useCase()

        assertTrue(result.isSuccess)
        assertEquals(expectedCode, result.getOrNull())
        coVerify(exactly = 1) { mockRepository.scratchCard() }
    }

    @Test
    fun `invoke propagates repository success result`() = runTest {
        val testCode = "550e8400-e29b-41d4-a716-446655440000"
        coEvery { mockRepository.scratchCard() } returns Result.success(testCode)

        val result = useCase()

        assertTrue(result.isSuccess)
        assertEquals(testCode, result.getOrNull())
    }

    @Test
    fun `invoke propagates repository failure result`() = runTest {
        val testException = RuntimeException("Test error")
        coEvery { mockRepository.scratchCard() } returns Result.failure(testException)

        val result = useCase()

        assertTrue(result.isFailure)
        assertEquals(testException, result.exceptionOrNull())
    }

    @Test
    fun `use case has no Android dependencies`() {
        // This test verifies the class can be instantiated with pure Kotlin
        // If there were Android dependencies, this test would fail to compile
        val repository = mockk<ScratchCardRepository>()
        val instance = ScratchCardUseCase(repository)

        assertNotNull(instance)
    }
}
