package sk.o2.scratchcard.domain.repository

import kotlinx.coroutines.flow.StateFlow
import sk.o2.scratchcard.domain.model.ScratchCardState

/**
 * Repository abstraction for scratch card operations.
 *
 * This interface defines the contract for managing scratch card state and operations.
 * Implementations must be thread-safe and maintain state consistency across all operations.
 *
 * The repository follows the Single Source of Truth pattern - the cardState StateFlow
 * is the authoritative source for the current card state throughout the application.
 *
 * **Clean Architecture:** This interface belongs to the domain layer and has zero
 * Android or framework dependencies. Implementations in the data layer handle
 * Android-specific concerns.
 */
interface ScratchCardRepository {
    /**
     * Observable card state - emits current state and all subsequent changes.
     *
     * This StateFlow provides a reactive stream of state updates. All screens
     * and ViewModels should observe this flow to stay synchronized with the
     * current scratch card state.
     *
     * The StateFlow is hot and always has a current value (initially Unscratched).
     * New subscribers immediately receive the current state.
     *
     * @return StateFlow emitting current [ScratchCardState] and all updates
     */
    val cardState: StateFlow<ScratchCardState>

    /**
     * Perform scratch operation (2-second delay, generate UUID).
     *
     * This operation simulates scratching a physical scratch card:
     * 1. Delays for exactly 2000 milliseconds (NFR002)
     * 2. Generates a random UUID code
     * 3. Transitions state from Unscratched to Scratched(code)
     *
     * The operation is cancellable - if the coroutine scope is cancelled
     * (e.g., user navigates back), the operation stops and state remains Unscratched.
     *
     * @return Result containing the revealed UUID code on success, or exception on failure
     */
    suspend fun scratchCard(): Result<String>

    /**
     * Activate card with API call.
     *
     * Calls the O2 API endpoint to validate the scratch code:
     * - Endpoint: GET https://api.o2.sk/version?code={uuid}
     * - Validation: Success if response.android > 277028
     *
     * This operation is non-cancellable - once initiated, it will complete
     * even if the user navigates away (FR014).
     *
     * On success, transitions state from Scratched(code) to Activated(code).
     * On validation failure (android ≤ 277028), state remains Scratched.
     *
     * @param code UUID code to activate (from Scratched state)
     * @return Result containing true if activation successful (android > 277028),
     *         false if validation failed (android ≤ 277028),
     *         or exception for network/parsing errors
     */
    suspend fun activateCard(code: String): Result<Boolean>
}
