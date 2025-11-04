package sk.o2.scratchcard.presentation.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview

/**
 * O2 Theme composable.
 *
 * Applies O2 Corporate Identity design system to the entire application.
 * Wraps Material 3 theme with O2-specific colors, typography, and shapes.
 *
 * Features:
 * - Automatic dark mode based on system preference
 * - Complete O2 brand color palette
 * - O2 typography with Roboto fallback
 * - Consistent corner radii and spacing
 *
 * Usage:
 * ```kotlin
 * setContent {
 *     O2Theme {
 *         // Your app content
 *     }
 * }
 * ```
 *
 * @param darkTheme Whether to use dark theme (default: follows system)
 * @param content Composable content to theme
 */
@Composable
fun O2Theme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) {
        DarkColorScheme
    } else {
        LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = O2Typography,
        shapes = O2Shapes,
        content = content
    )
}

// ═══════════════════════════════════════════════════════════
// Preview Functions
// ═══════════════════════════════════════════════════════════

/**
 * Preview O2Theme in light mode.
 */
@Preview(name = "O2 Theme - Light", showBackground = true)
@Composable
fun O2ThemeLightPreview() {
    O2Theme(darkTheme = false) {
        ThemePreviewContent()
    }
}

/**
 * Preview O2Theme in dark mode.
 */
@Preview(name = "O2 Theme - Dark", showBackground = true)
@Composable
fun O2ThemeDarkPreview() {
    O2Theme(darkTheme = true) {
        ThemePreviewContent()
    }
}

/**
 * Sample content for theme previews.
 * Shows colors, typography, and shapes in action.
 */
@Composable
private fun ThemePreviewContent() {
    androidx.compose.foundation.layout.Column(
        modifier = androidx.compose.ui.Modifier
            .androidx.compose.foundation.background(MaterialTheme.colorScheme.background)
            .padding(O2Spacing.md)
    ) {
        // Display text
        androidx.compose.material3.Text(
            text = "O2 Theme",
            style = MaterialTheme.typography.displayLarge,
            color = MaterialTheme.colorScheme.primary
        )

        androidx.compose.foundation.layout.Spacer(
            modifier = androidx.compose.ui.Modifier.height(O2Spacing.sm)
        )

        // Headline
        androidx.compose.material3.Text(
            text = "Design System",
            style = MaterialTheme.typography.headlineLarge,
            color = MaterialTheme.colorScheme.onBackground
        )

        androidx.compose.foundation.layout.Spacer(
            modifier = androidx.compose.ui.Modifier.height(O2Spacing.md)
        )

        // Body text
        androidx.compose.material3.Text(
            text = "O2 Corporate Identity applied to Android with Material 3",
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurface
        )

        androidx.compose.foundation.layout.Spacer(
            modifier = androidx.compose.ui.Modifier.height(O2Spacing.lg)
        )

        // Primary button preview
        androidx.compose.material3.Button(
            onClick = {},
            shape = O2Shapes.small
        ) {
            androidx.compose.material3.Text("Primary Button")
        }
    }
}
