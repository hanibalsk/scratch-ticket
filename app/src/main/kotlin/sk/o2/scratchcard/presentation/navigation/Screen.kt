package sk.o2.scratchcard.presentation.navigation

/**
 * Sealed interface defining all navigation destinations in the app.
 *
 * Provides type-safe navigation with compile-time route validation.
 * Each screen is a singleton object with a unique route string.
 *
 * Usage:
 * ```kotlin
 * navController.navigate(Screen.Scratch.route)
 * ```
 *
 * Benefits:
 * - Type safety: No string typos
 * - Exhaustive when expressions
 * - Centralized route management
 * - Easy to add deep linking in future
 */
sealed interface Screen {
    val route: String

    /**
     * Main screen - app entry point.
     *
     * Shows current scratch card state and navigation buttons.
     * Start destination for the app.
     */
    data object Main : Screen {
        override val route = "main"
    }

    /**
     * Scratch screen - perform scratch operation.
     *
     * Allows user to initiate 2-second scratch operation to reveal UUID code.
     * Operation is cancellable if user navigates back before completion.
     */
    data object Scratch : Screen {
        override val route = "scratch"
    }

    /**
     * Activation screen - activate scratch card.
     *
     * Calls O2 API to validate and activate the revealed code.
     * Operation is non-cancellable - continues even if user navigates away.
     */
    data object Activation : Screen {
        override val route = "activation"
    }
}
