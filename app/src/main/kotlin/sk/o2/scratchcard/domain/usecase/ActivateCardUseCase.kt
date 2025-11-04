package sk.o2.scratchcard.domain.usecase

import sk.o2.scratchcard.domain.repository.ScratchCardRepository
import javax.inject.Inject

/**
 * Use case for activating a scratch card.
 *
 * Encapsulates the business logic for card activation:
 * - Calls O2 API to validate the scratch code
 * - Validates response: success if android version > 277028
 * - State transition from Scratched to Activated on success
 *
 * This use case follows the Single Responsibility Principle - it has one
 * clear purpose and delegates implementation details to the repository.
 *
 * **Non-Cancellable:** Unlike the scratch operation, activation is non-cancellable.
 * Once initiated, it will complete even if the user navigates away (FR014).
 * This ensures the activation request reaches the server and state updates correctly.
 *
 * @property repository The scratch card repository for data operations
 */
class ActivateCardUseCase @Inject constructor(
    private val repository: ScratchCardRepository
) {

    /**
     * Execute the card activation operation.
     *
     * Calls the O2 API with the provided code and validates the response.
     * The operation continues even if the calling scope is cancelled.
     *
     * Usage:
     * ```kotlin
     * GlobalScope.launch(Dispatchers.IO) {
     *     activateCardUseCase(code).onSuccess { isActivated ->
     *         if (isActivated) {
     *             // Card activated successfully
     *         } else {
     *             // Validation failed (android ≤ 277028)
     *         }
     *     }.onFailure { error ->
     *         // Handle network/parsing error
     *     }
     * }
     * ```
     *
     * @param code The UUID code to activate (obtained from scratch operation)
     * @return Result containing:
     *         - true if activation successful (android > 277028)
     *         - false if validation failed (android ≤ 277028)
     *         - exception for network, timeout, or parsing errors
     */
    suspend operator fun invoke(code: String): Result<Boolean> {
        return repository.activateCard(code)
    }
}
