package sk.o2.scratchcard.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import sk.o2.scratchcard.presentation.theme.O2Colors
import sk.o2.scratchcard.presentation.theme.O2Theme

/**
 * O2 Primary Button - Main call-to-action button.
 *
 * Visual Specs:
 * - Container: O2 Beyond Blue (#0050FF)
 * - Content: White
 * - Corner Radius: 12dp
 * - Height: Minimum 48dp (accessibility requirement)
 * - Elevation: 1dp default, 3dp when pressed
 *
 * States:
 * - Enabled: Full opacity, interactive
 * - Disabled: 12% container opacity, 38% content opacity
 * - Pressed: Elevated 3dp with ripple effect
 *
 * @param text Button label text
 * @param onClick Click callback
 * @param modifier Optional modifier
 * @param enabled Whether button is interactive
 */
@Composable
fun O2PrimaryButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
) {
    Button(
        onClick = onClick,
        enabled = enabled,
        modifier = modifier.heightIn(min = 48.dp),
        colors =
            ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primary, // O2 Blue
                contentColor = O2Colors.White,
                disabledContainerColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.12f),
                disabledContentColor = O2Colors.White.copy(alpha = 0.38f),
            ),
        shape = MaterialTheme.shapes.small, // 12dp radius
        elevation =
            ButtonDefaults.buttonElevation(
                defaultElevation = 1.dp,
                pressedElevation = 3.dp,
                disabledElevation = 0.dp,
            ),
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.titleLarge,
        )
    }
}

/**
 * O2 Secondary Button - Secondary actions.
 *
 * Visual Specs:
 * - Container: Surface color (white in light, dark in dark mode)
 * - Outline: 1dp O2 Blue border
 * - Content: O2 Blue
 * - Corner Radius: 12dp
 * - Height: Minimum 48dp
 *
 * States:
 * - Enabled: Blue outline and text
 * - Disabled: Muted outline and text
 * - Pressed: Ripple effect with primary color
 *
 * @param text Button label text
 * @param onClick Click callback
 * @param modifier Optional modifier
 * @param enabled Whether button is interactive
 */
@Composable
fun O2SecondaryButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
) {
    OutlinedButton(
        onClick = onClick,
        enabled = enabled,
        modifier = modifier.heightIn(min = 48.dp),
        colors =
            ButtonDefaults.outlinedButtonColors(
                containerColor = MaterialTheme.colorScheme.surface,
                contentColor = MaterialTheme.colorScheme.primary,
                disabledContainerColor = MaterialTheme.colorScheme.surface,
                disabledContentColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.38f),
            ),
        border =
            ButtonDefaults.outlinedButtonBorder(enabled = enabled).copy(
                width = 1.dp,
            ),
        shape = MaterialTheme.shapes.small, // 12dp radius
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.titleLarge,
        )
    }
}

// ═══════════════════════════════════════════════════════════
// Previews
// ═══════════════════════════════════════════════════════════

@Preview(name = "O2 Primary Button - Light", showBackground = true)
@Composable
fun O2PrimaryButtonLightPreview() {
    O2Theme(darkTheme = false) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            O2PrimaryButton(text = "Scratch Card", onClick = {})
            O2PrimaryButton(text = "Activate", onClick = {})
            O2PrimaryButton(text = "Disabled", onClick = {}, enabled = false)
        }
    }
}

@Preview(name = "O2 Primary Button - Dark", showBackground = true)
@Composable
fun O2PrimaryButtonDarkPreview() {
    O2Theme(darkTheme = true) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            O2PrimaryButton(text = "Scratch Card", onClick = {})
            O2PrimaryButton(text = "Activate", onClick = {})
            O2PrimaryButton(text = "Disabled", onClick = {}, enabled = false)
        }
    }
}

@Preview(name = "O2 Secondary Button - Light", showBackground = true)
@Composable
fun O2SecondaryButtonLightPreview() {
    O2Theme(darkTheme = false) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            O2SecondaryButton(text = "Retry", onClick = {})
            O2SecondaryButton(text = "Dismiss", onClick = {})
            O2SecondaryButton(text = "Disabled", onClick = {}, enabled = false)
        }
    }
}

@Preview(name = "O2 Secondary Button - Dark", showBackground = true)
@Composable
fun O2SecondaryButtonDarkPreview() {
    O2Theme(darkTheme = true) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            O2SecondaryButton(text = "Retry", onClick = {})
            O2SecondaryButton(text = "Dismiss", onClick = {})
            O2SecondaryButton(text = "Disabled", onClick = {}, enabled = false)
        }
    }
}
