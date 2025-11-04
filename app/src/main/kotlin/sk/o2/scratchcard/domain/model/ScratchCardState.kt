package sk.o2.scratchcard.domain.model

/**
 * Sealed class representing all possible states of a scratch card.
 *
 * This immutable state model follows Clean Architecture principles with zero Android dependencies.
 * State transitions flow: Unscratched → Scratched → Activated
 *
 * @see Unscratched Initial state when card hasn't been scratched
 * @see Scratched State after 2-second scratch operation reveals UUID code
 * @see Activated Final state after successful API validation
 */
sealed class ScratchCardState {

    /**
     * Initial state - card has not been scratched yet.
     *
     * This is a singleton object representing the default state.
     * No code has been revealed and no activation has occurred.
     */
    data object Unscratched : ScratchCardState()

    /**
     * Card has been scratched and code revealed.
     *
     * After the 2-second scratch operation completes, a UUID code is generated
     * and the card transitions to this state. The code is ready for activation.
     *
     * @property code Unique UUID string generated during scratch operation
     */
    data class Scratched(val code: String) : ScratchCardState()

    /**
     * Card has been successfully activated with the API.
     *
     * Final state reached after API validation confirms android version > 277028.
     * The card cannot be activated again once in this state.
     *
     * @property code The UUID code that was successfully activated
     */
    data class Activated(val code: String) : ScratchCardState()
}
