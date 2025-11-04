package sk.o2.scratchcard.presentation.screens.activation

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
 * Activation Screen - Activate scratch card.
 *
 * TODO: Full implementation in Stories 1.5, 1.6, 1.7
 * This is a navigation placeholder that will be enhanced with:
 * - ViewModel integration for activation API call
 * - Loading indicator during API call
 * - Success/failure handling
 * - Error modal display
 * - Non-cancellable operation behavior
 *
 * @param onNavigateBack Callback to return to previous screen
 */
@Composable
fun ActivationScreen(
    onNavigateBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(O2Spacing.md),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(text = "Activation Screen")

        Spacer(modifier = Modifier.height(O2Spacing.xl))

        // TODO Story 1.5: Add activation button with API call
        // TODO Story 1.6: Add validation logic
        // TODO Story 1.7: Add error modal handling
        Button(onClick = { /* TODO: Start activation */ }) {
            Text("Activate")
        }

        Spacer(modifier = Modifier.height(O2Spacing.lg))

        Button(onClick = onNavigateBack) {
            Text("Back")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ActivationScreenPreview() {
    O2Theme {
        ActivationScreen(onNavigateBack = {})
    }
}
