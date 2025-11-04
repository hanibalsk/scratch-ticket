package sk.o2.scratchcard.presentation.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import sk.o2.scratchcard.presentation.theme.O2Spacing
import sk.o2.scratchcard.presentation.theme.O2Theme

/**
 * O2 Card Component - Container for grouped content.
 *
 * Visual Specs:
 * - Background: Surface color (white in light, dark in dark mode)
 * - Border: 1dp with subtle color (outline variant)
 * - Corner Radius: 16dp (medium shape)
 * - Elevation: 1dp
 * - Padding: 16dp internal padding
 *
 * Accessibility:
 * - Sufficient contrast between border and surface
 * - Adapts to dark mode automatically
 *
 * Usage:
 * ```kotlin
 * O2Card {
 *     Text("Card content")
 * }
 * ```
 *
 * @param modifier Optional modifier
 * @param content Card content composable
 */
@Composable
fun O2Card(
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.() -> Unit
) {
    Card(
        modifier = modifier,
        shape = MaterialTheme.shapes.medium, // 16dp radius
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface,
        ),
        border = BorderStroke(
            width = 1.dp,
            color = MaterialTheme.colorScheme.outlineVariant
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 1.dp
        )
    ) {
        Column(
            modifier = Modifier.padding(O2Spacing.md),
            content = content
        )
    }
}

// ═══════════════════════════════════════════════════════════
// Previews
// ═══════════════════════════════════════════════════════════

@Preview(name = "O2 Card - Light", showBackground = true)
@Composable
fun O2CardLightPreview() {
    O2Theme(darkTheme = false) {
        Column(modifier = Modifier.padding(16.dp)) {
            O2Card {
                Text(
                    text = "Card Title",
                    style = MaterialTheme.typography.titleLarge
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Card content goes here with additional details.",
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }
}

@Preview(name = "O2 Card - Dark", showBackground = true)
@Composable
fun O2CardDarkPreview() {
    O2Theme(darkTheme = true) {
        Column(modifier = Modifier.padding(16.dp)) {
            O2Card {
                Text(
                    text = "Card Title",
                    style = MaterialTheme.typography.titleLarge
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Card content goes here with additional details.",
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }
}
