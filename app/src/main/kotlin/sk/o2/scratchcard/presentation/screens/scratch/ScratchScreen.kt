package sk.o2.scratchcard.presentation.screens.scratch

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
 * Scratch Screen - Perform scratch operation.
 *
 * TODO: Full implementation in Story 1.4
 * This is a navigation placeholder that will be enhanced with:
 * - ViewModel integration for scratch operation
 * - 2-second loading indicator with progress
 * - UUID code display after completion
 * - Cancellation handling on back navigation
 *
 * @param onNavigateBack Callback to return to previous screen
 */
@Composable
fun ScratchScreen(
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
        Text(text = "Scratch Screen")

        Spacer(modifier = Modifier.height(O2Spacing.xl))

        // TODO Story 1.4: Add scratch button with loading and code display
        Button(onClick = { /* TODO: Start scratch operation */ }) {
            Text("Scratch Card")
        }

        Spacer(modifier = Modifier.height(O2Spacing.lg))

        Button(onClick = onNavigateBack) {
            Text("Back")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ScratchScreenPreview() {
    O2Theme {
        ScratchScreen(onNavigateBack = {})
    }
}
