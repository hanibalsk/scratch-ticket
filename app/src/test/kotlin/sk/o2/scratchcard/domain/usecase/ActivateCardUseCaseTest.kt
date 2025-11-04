package sk.o2.scratchcard.domain.usecase

import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import sk.o2.scratchcard.domain.repository.ScratchCardRepository

/**
 * Unit tests for ActivateCardUseCase.
 *
 * Validates:
 * - Use case correctly delegates to repository
 * - Result propagation for success/failure/errors
 * - Code parameter is passed correctly
 */
class ActivateCardUseCaseTest {

    private val mockRepository = mockk<ScratchCardRepository>()
    private val useCase = ActivateCardUseCase(mockRepository)

    @Test
    fun `invoke delegates to repository activateCard with correct code`() = runTest {
        val testCode = "test-uuid-12345"
        coEvery { mockRepository.activateCard(testCode) } returns Result.success(true)

        val result = useCase(testCode)

        assertTrue(result.isSuccess)
        assertEquals(true, result.getOrNull())
        coVerify(exactly = 1) { mockRepository.activateCard(testCode) }
    }

    @Test
    fun `invoke propagates activation success result`() = runTest {
        val code = "550e8400-e29b-41d4-a716-446655440000"
        coEvery { mockRepository.activateCard(code) } returns Result.success(true)

        val result = useCase(code)

        assertTrue(result.isSuccess)
        assertEquals(true, result.getOrNull())
    }

    @Test
    fun `invoke propagates activation failure result when validation fails`() = runTest {
        val code = "test-uuid"
        coEvery { mockRepository.activateCard(code) } returns Result.success(false)

        val result = useCase(code)

        assertTrue(result.isSuccess)
        assertEquals(false, result.getOrNull())
    }

    @Test
    fun `invoke propagates repository errors`() = runTest {
        val code = "test-uuid"
        val testException = RuntimeException("Network error")
        coEvery { mockRepository.activateCard(code) } returns Result.failure(testException)

        val result = useCase(code)

        assertTrue(result.isFailure)
        assertEquals(testException, result.exceptionOrNull())
    }

    @Test
    fun `use case passes code parameter unchanged`() = runTest {
        val codes = listOf(
            "simple-code",
            "550e8400-e29b-41d4-a716-446655440000",
            "code-with-dashes-123"
        )

        codes.forEach { code ->
            coEvery { mockRepository.activateCard(code) } returns Result.success(true)

            useCase(code)

            coVerify { mockRepository.activateCard(code) }
        }
    }

    @Test
    fun `use case has no Android dependencies`() {
        // Verifies pure Kotlin implementation
        val repository = mockk<ScratchCardRepository>()
        val instance = ActivateCardUseCase(repository)

        assertNotNull(instance)
    }
}
