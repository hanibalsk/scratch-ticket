package sk.o2.scratchcard.presentation.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import sk.o2.scratchcard.presentation.theme.O2Theme

/**
 * O2 Loading Indicator - Progress feedback for async operations.
 *
 * Supports two modes:
 * 1. **Determinate**: Shows exact progress (0.0 to 1.0) for timed operations
 *    - Use for: 2-second scratch countdown
 *    - Progress updates smoothly from 0% to 100%
 *
 * 2. **Indeterminate**: Spinning indicator for unknown duration
 *    - Use for: API calls, activation
 *    - Continuous animation until operation completes
 *
 * Visual Specs:
 * - Color: O2 Primary Blue (#0050FF)
 * - Size: 48dp diameter
 * - Stroke Width: 4dp
 * - Theme-aware: Adapts to light/dark mode
 *
 * @param progress Optional progress value (0.0 to 1.0). If null, shows indeterminate mode.
 * @param modifier Optional modifier
 */
@Composable
fun O2LoadingIndicator(
    modifier: Modifier = Modifier,
    progress: Float? = null,
) {
    if (progress != null) {
        // Determinate progress (e.g., 2-second scratch countdown)
        CircularProgressIndicator(
            progress = { progress.coerceIn(0f, 1f) },
            modifier = modifier.size(48.dp),
            color = MaterialTheme.colorScheme.primary,
            strokeWidth = 4.dp,
            trackColor = MaterialTheme.colorScheme.surfaceVariant,
        )
    } else {
        // Indeterminate progress (e.g., API calls)
        CircularProgressIndicator(
            modifier = modifier.size(48.dp),
            color = MaterialTheme.colorScheme.primary,
            strokeWidth = 4.dp,
            trackColor = MaterialTheme.colorScheme.surfaceVariant,
        )
    }
}

// ═══════════════════════════════════════════════════════════
// Previews
// ═══════════════════════════════════════════════════════════

@Preview(name = "Loading Indicator - Indeterminate - Light", showBackground = true)
@Composable
fun O2LoadingIndicatorIndeterminateLightPreview() {
    O2Theme(darkTheme = false) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            Text("Indeterminate (API Call)")
            O2LoadingIndicator()
        }
    }
}

@Preview(name = "Loading Indicator - Indeterminate - Dark", showBackground = true)
@Composable
fun O2LoadingIndicatorIndeterminateDarkPreview() {
    O2Theme(darkTheme = true) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            Text("Indeterminate (API Call)")
            O2LoadingIndicator()
        }
    }
}

@Preview(name = "Loading Indicator - Determinate - Light", showBackground = true)
@Composable
fun O2LoadingIndicatorDeterminateLightPreview() {
    O2Theme(darkTheme = false) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            Text("Determinate (Scratch Progress)")
            Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                O2LoadingIndicator(progress = 0.0f)
                O2LoadingIndicator(progress = 0.25f)
                O2LoadingIndicator(progress = 0.5f)
                O2LoadingIndicator(progress = 0.75f)
                O2LoadingIndicator(progress = 1.0f)
            }
        }
    }
}

@Preview(name = "Loading Indicator - Determinate - Dark", showBackground = true)
@Composable
fun O2LoadingIndicatorDeterminateDarkPreview() {
    O2Theme(darkTheme = true) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            Text("Determinate (Scratch Progress)")
            Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                O2LoadingIndicator(progress = 0.0f)
                O2LoadingIndicator(progress = 0.25f)
                O2LoadingIndicator(progress = 0.5f)
                O2LoadingIndicator(progress = 0.75f)
                O2LoadingIndicator(progress = 1.0f)
            }
        }
    }
}
