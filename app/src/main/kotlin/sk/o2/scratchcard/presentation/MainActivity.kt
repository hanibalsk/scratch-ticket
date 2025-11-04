package sk.o2.scratchcard.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import dagger.hilt.android.AndroidEntryPoint
import sk.o2.scratchcard.presentation.navigation.AppNavigation
import sk.o2.scratchcard.presentation.theme.O2Theme

/**
 * Main Activity for O2 Scratch Card app.
 *
 * Entry point for the application UI. Sets up:
 * - Edge-to-edge display
 * - O2 Theme (Story 2.1)
 * - App navigation (Story 1.2)
 *
 * The activity is annotated with @AndroidEntryPoint to enable Hilt dependency
 * injection in ViewModels used by screen composables.
 */
@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            O2Theme {
                AppNavigation()
            }
        }
    }
}
