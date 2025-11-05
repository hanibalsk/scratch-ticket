package sk.o2.scratchcard.presentation.screens.main

import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import sk.o2.scratchcard.R
import sk.o2.scratchcard.domain.model.ScratchCardState
import sk.o2.scratchcard.presentation.components.O2BrandedBackground
import sk.o2.scratchcard.presentation.components.O2ContentCard
import sk.o2.scratchcard.presentation.components.O2PrimaryButton
import sk.o2.scratchcard.presentation.components.O2StateBadge
import sk.o2.scratchcard.presentation.theme.O2Spacing
import sk.o2.scratchcard.presentation.theme.O2Theme

/**
 * Main Screen - App entry point displaying scratch card state.
 *
 * Features:
 * - Displays current card state with semantic colors (Unscratched/Scratched/Activated)
 * - Two action buttons for navigation to Scratch and Activation screens
 * - "Go to Activation Screen" button enabled only when card is Scratched or Activated
 * - Real-time state updates via StateFlow observation
 * - O2 design system styling with proper spacing and typography
 *
 * Architecture:
 * - MVVM pattern: MainViewModel provides reactive UI state
 * - State observation: collectAsStateWithLifecycle for lifecycle-aware collection
 * - State hoisting: Navigation callbacks passed from parent
 *
 * @param onNavigateToScratch Callback to navigate to scratch screen (always available)
 * @param onNavigateToActivation Callback to navigate to activation screen (conditional)
 * @param modifier Optional modifier for custom layout behavior
 * @param viewModel MainViewModel instance (provided by Hilt, injectable for testing)
 */
@Composable
fun MainScreen(
    onNavigateToScratch: () -> Unit,
    onNavigateToActivation: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: MainViewModel = hiltViewModel(),
) {
    // Observe UI state with lifecycle awareness
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    MainScreenContent(
        uiState = uiState,
        onNavigateToScratch = onNavigateToScratch,
        onNavigateToActivation = onNavigateToActivation,
        modifier = modifier,
    )
}

/**
 * Main Screen content composable (stateless).
 *
 * Separated from MainScreen for easier testing and preview.
 * Contains only UI logic with no ViewModel dependency.
 *
 * @param uiState Current UI state containing card state and button states
 * @param onNavigateToScratch Callback to navigate to scratch screen
 * @param onNavigateToActivation Callback to navigate to activation screen
 * @param modifier Optional modifier
 */
@Composable
private fun MainScreenContent(
    uiState: MainScreenUiState,
    onNavigateToScratch: () -> Unit,
    onNavigateToActivation: () -> Unit,
    modifier: Modifier = Modifier,
) {
    O2BrandedBackground(modifier = modifier) {
        // Center the content card
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center,
        ) {
            O2ContentCard {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(O2Spacing.md),
                ) {
                    // App title
                    Text(
                        text = stringResource(R.string.app_name),
                        style = MaterialTheme.typography.headlineLarge,
                        color = MaterialTheme.colorScheme.onBackground,
                    )

                    Spacer(modifier = Modifier.height(O2Spacing.sm))

                    // State badge - shows current card state
                    O2StateBadge(state = uiState.cardState)

                    Spacer(modifier = Modifier.height(O2Spacing.md))

                    // Go to Scratch Screen button (always enabled)
                    O2PrimaryButton(
                        text = stringResource(R.string.btn_go_to_scratch),
                        onClick = onNavigateToScratch,
                        enabled = true,
                    )

                    // Go to Activation Screen button (enabled only when Scratched or Activated)
                    O2PrimaryButton(
                        text = stringResource(R.string.btn_go_to_activation),
                        onClick = onNavigateToActivation,
                        enabled = uiState.isActivationEnabled,
                    )
                }
            }
        }
    }
}

// ═══════════════════════════════════════════════════════════
// Previews
// ═══════════════════════════════════════════════════════════

@Preview(name = "Main Screen - Unscratched", showBackground = true)
@Composable
fun MainScreenUnscratchedPreview() {
    O2Theme {
        MainScreenContent(
            uiState =
                MainScreenUiState(
                    cardState = ScratchCardState.Unscratched,
                    isActivationEnabled = false,
                ),
            onNavigateToScratch = {},
            onNavigateToActivation = {},
        )
    }
}

@Preview(name = "Main Screen - Scratched", showBackground = true)
@Composable
fun MainScreenScratchedPreview() {
    O2Theme {
        MainScreenContent(
            uiState =
                MainScreenUiState(
                    cardState = ScratchCardState.Scratched("550e8400-e29b-41d4-a716-446655440000"),
                    isActivationEnabled = true,
                ),
            onNavigateToScratch = {},
            onNavigateToActivation = {},
        )
    }
}

@Preview(name = "Main Screen - Activated", showBackground = true)
@Composable
fun MainScreenActivatedPreview() {
    O2Theme {
        MainScreenContent(
            uiState =
                MainScreenUiState(
                    cardState = ScratchCardState.Activated("550e8400-e29b-41d4-a716-446655440000"),
                    isActivationEnabled = true,
                ),
            onNavigateToScratch = {},
            onNavigateToActivation = {},
        )
    }
}
