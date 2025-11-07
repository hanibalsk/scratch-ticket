package sk.o2.scratchcard.presentation.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import sk.o2.scratchcard.R
import sk.o2.scratchcard.domain.model.ScratchCardState
import sk.o2.scratchcard.presentation.theme.O2Colors
import sk.o2.scratchcard.presentation.theme.O2Theme

/**
 * O2 State Badge - Displays current scratch card state.
 *
 * Visual Mapping:
 * - Unscratched: Gray/Neutral with gray border
 * - Scratched: O2 Blue with blue border
 * - Activated: Success Green with green border
 *
 * Specs:
 * - Corner Radius: 12dp (chip style)
 * - Border: 1dp matching content color
 * - Background: Content color at 10% alpha
 * - Padding: 12dp horizontal, 6dp vertical
 *
 * Accessibility:
 * - Semantic colors provide meaning
 * - Sufficient contrast for readability
 * - Works in light and dark modes
 *
 * @param state Current scratch card state
 * @param modifier Optional modifier
 */
@Composable
fun O2StateBadge(
    state: ScratchCardState,
    modifier: Modifier = Modifier,
) {
    val (text, color) =
        when (state) {
            is ScratchCardState.Unscratched -> stringResource(R.string.state_unscratched) to O2Colors.Neutral600
            is ScratchCardState.Scratched ->
                stringResource(R.string.state_scratched) to
                    MaterialTheme.colorScheme.primary
            is ScratchCardState.Activated -> stringResource(R.string.state_activated) to O2Colors.Success
        }

    Surface(
        modifier = modifier,
        shape = MaterialTheme.shapes.small, // 12dp radius
        color = color.copy(alpha = 0.1f),
        border = BorderStroke(1.dp, color),
        contentColor = color,
    ) {
        Text(
            text = text,
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
            style = MaterialTheme.typography.labelLarge,
            color = color,
        )
    }
}

/**
 * Generic O2 Chip Component - Reusable chip for labels and tags.
 *
 * Visual Specs:
 * - Background: Sky-05 (#E5F2FA) in light mode
 * - Content: Deep Blue (#0112AA)
 * - Corner Radius: 12dp
 * - Padding: 12dp horizontal, 6dp vertical
 *
 * @param text Chip label
 * @param modifier Optional modifier
 */
@Composable
fun O2Chip(
    text: String,
    modifier: Modifier = Modifier,
) {
    val isDarkTheme = isSystemInDarkTheme()

    Surface(
        modifier = modifier,
        shape = MaterialTheme.shapes.small,
        color =
            if (isDarkTheme) {
                O2Colors.BlueDeep.copy(alpha = 0.2f) // Dark mode
            } else {
                O2Colors.Sky05 // Light mode
            },
        contentColor =
            if (isDarkTheme) {
                MaterialTheme.colorScheme.primary // Brighter blue for dark mode
            } else {
                O2Colors.BlueDeep // Dark blue for light mode
            },
    ) {
        Text(
            text = text,
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
            style = MaterialTheme.typography.labelLarge,
        )
    }
}

// ═══════════════════════════════════════════════════════════
// Previews
// ═══════════════════════════════════════════════════════════

@Preview(name = "State Badge - All States - Light", showBackground = true)
@Composable
fun O2StateBadgeAllStatesLightPreview() {
    O2Theme(darkTheme = false) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            O2StateBadge(state = ScratchCardState.Unscratched)
            O2StateBadge(state = ScratchCardState.Scratched("550e8400-e29b-41d4-a716-446655440000"))
            O2StateBadge(state = ScratchCardState.Activated("550e8400-e29b-41d4-a716-446655440000"))
        }
    }
}

@Preview(name = "State Badge - All States - Dark", showBackground = true)
@Composable
fun O2StateBadgeAllStatesDarkPreview() {
    O2Theme(darkTheme = true) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            O2StateBadge(state = ScratchCardState.Unscratched)
            O2StateBadge(state = ScratchCardState.Scratched("550e8400-e29b-41d4-a716-446655440000"))
            O2StateBadge(state = ScratchCardState.Activated("550e8400-e29b-41d4-a716-446655440000"))
        }
    }
}

@Preview(name = "O2 Chip - Light", showBackground = true)
@Composable
fun O2ChipLightPreview() {
    O2Theme(darkTheme = false) {
        Row(
            modifier = Modifier.padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            O2Chip(text = "Tag 1")
            O2Chip(text = "Tag 2")
        }
    }
}

@Preview(name = "O2 Chip - Dark", showBackground = true)
@Composable
fun O2ChipDarkPreview() {
    O2Theme(darkTheme = true) {
        Row(
            modifier = Modifier.padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            O2Chip(text = "Tag 1")
            O2Chip(text = "Tag 2")
        }
    }
}
