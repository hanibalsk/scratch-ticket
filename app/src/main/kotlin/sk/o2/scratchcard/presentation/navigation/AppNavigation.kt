package sk.o2.scratchcard.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import sk.o2.scratchcard.presentation.screens.activation.ActivationScreen
import sk.o2.scratchcard.presentation.screens.main.MainScreen
import sk.o2.scratchcard.presentation.screens.scratch.ScratchScreen

/**
 * Main navigation composable for the app.
 *
 * Sets up NavHost with all screen destinations and manages navigation state.
 * Uses type-safe Screen sealed interface for route management.
 *
 * Navigation Flow:
 * - Start: Main screen
 * - Main → Scratch (always available)
 * - Main → Activation (only when Scratched or Activated)
 * - Back navigation: Pops back stack
 *
 * State Management:
 * - State is preserved in ScratchCardRepository (singleton)
 * - All screens observe the same StateFlow
 * - No state loss during navigation
 *
 * Usage:
 * ```kotlin
 * O2Theme {
 *     AppNavigation()
 * }
 * ```
 */
@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Screen.Main.route
    ) {
        // Main Screen - App entry point
        composable(route = Screen.Main.route) {
            MainScreen(
                onNavigateToScratch = {
                    navController.navigate(Screen.Scratch.route)
                },
                onNavigateToActivation = {
                    navController.navigate(Screen.Activation.route)
                }
            )
        }

        // Scratch Screen - Perform scratch operation
        composable(route = Screen.Scratch.route) {
            ScratchScreen(
                onNavigateBack = {
                    navController.popBackStack()
                }
            )
        }

        // Activation Screen - Activate scratch card
        composable(route = Screen.Activation.route) {
            ActivationScreen(
                onNavigateBack = {
                    navController.popBackStack()
                }
            )
        }
    }
}
