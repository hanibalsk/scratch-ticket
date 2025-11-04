package sk.o2.scratchcard.presentation.screens.main

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import sk.o2.scratchcard.presentation.theme.O2Spacing
import sk.o2.scratchcard.presentation.theme.O2Theme

/**
 * Main Screen - App entry point.
 *
 * TODO: Full implementation in Story 1.1
 * This is a navigation placeholder that will be enhanced with:
 * - State badge display (Unscratched/Scratched/Activated)
 * - ViewModel integration for state observation
 * - O2 component library integration
 *
 * @param onNavigateToScratch Callback to navigate to scratch screen
 * @param onNavigateToActivation Callback to navigate to activation screen
 */
@Composable
fun MainScreen(
    onNavigateToScratch: () -> Unit,
    onNavigateToActivation: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(O2Spacing.md),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(text = "Main Screen")

        Spacer(modifier = Modifier.height(O2Spacing.lg))

        // TODO Story 1.1: Add state badge here

        Spacer(modifier = Modifier.height(O2Spacing.xl))

        // Go to Scratch Screen (always enabled)
        Button(onClick = onNavigateToScratch) {
            Text("Go to Scratch Screen")
        }

        Spacer(modifier = Modifier.height(O2Spacing.md))

        // Go to Activation Screen (TODO Story 1.1: enable only when Scratched/Activated)
        Button(onClick = onNavigateToActivation) {
            Text("Go to Activation Screen")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun MainScreenPreview() {
    O2Theme {
        MainScreen(
            onNavigateToScratch = {},
            onNavigateToActivation = {}
        )
    }
}
