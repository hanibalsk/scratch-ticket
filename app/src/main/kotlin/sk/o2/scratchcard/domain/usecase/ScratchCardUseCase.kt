package sk.o2.scratchcard.domain.usecase

import sk.o2.scratchcard.domain.repository.ScratchCardRepository
import javax.inject.Inject

/**
 * Use case for performing scratch card operation.
 *
 * Encapsulates the business logic for scratching a card:
 * - Delegates to repository for the actual operation
 * - 2-second delay with UUID generation
 * - State transition from Unscratched to Scratched
 *
 * This use case follows the Single Responsibility Principle - it has one
 * clear purpose and delegates implementation details to the repository.
 *
 * **Cancellation:** The scratch operation is cancellable. If the calling
 * coroutine scope is cancelled (e.g., ViewModel cleared when user navigates back),
 * the operation will stop and the card remains in Unscratched state.
 *
 * @property repository The scratch card repository for data operations
 */
class ScratchCardUseCase
    @Inject
    constructor(
        private val repository: ScratchCardRepository,
    ) {
        /**
         * Execute the scratch card operation.
         *
         * Performs a 2-second scratch operation that generates a UUID code.
         * The operation is observable through the repository's cardState StateFlow.
         *
         * Usage:
         * ```kotlin
         * viewModelScope.launch {
         *     scratchCardUseCase().onSuccess { code ->
         *         // Handle revealed code
         *     }.onFailure { error ->
         *         // Handle error
         *     }
         * }
         * ```
         *
         * @return Result containing the revealed UUID code on success,
         *         or exception on failure (e.g., cancellation, unexpected error)
         */
        suspend operator fun invoke(): Result<String> = repository.scratchCard()
    }
