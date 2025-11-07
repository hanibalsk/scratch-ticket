package sk.o2.scratchcard.presentation.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import sk.o2.scratchcard.R
import sk.o2.scratchcard.presentation.theme.O2Colors
import sk.o2.scratchcard.presentation.theme.O2Spacing
import sk.o2.scratchcard.presentation.theme.O2Theme

/**
 * O2 Branded Background with Logo and Gradient.
 *
 * Creates a branded background following O2 visual identity:
 * - O2 blue gradient (#002E72 to #0072CE) - consistent in all themes
 * - O2 logo centered in gradient area
 * - Content card positioned in bottom 2/3 of screen (theme-aware)
 *
 * Design Pattern:
 * - Branded splash/landing pages
 * - Full-screen branded experiences
 * - Marketing/onboarding flows
 *
 * Visual Hierarchy:
 * - Gradient: O2 branded blue gradient (same in light/dark mode)
 * - Logo: Centered in gradient area
 * - Content: Theme-aware elevated card in bottom 2/3
 *
 * @param modifier Optional modifier for custom layout behavior
 * @param content Composable content to display in the themed card overlay
 */
@Composable
fun O2BrandedBackground(
    modifier: Modifier = Modifier,
    content: @Composable BoxScope.() -> Unit,
) {
    // O2 blue gradient stays the same in both light and dark mode
    val gradientTopColor = O2Colors.GradientDarkBlue
    val gradientBottomColor = O2Colors.GradientLightBlue

    Box(
        modifier = modifier.fillMaxSize(),
    ) {
        // Top area with gradient (above content card)
        Box(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(0.33f) // Top 1/3 of screen
                    .background(
                        brush =
                            Brush.verticalGradient(
                                colors =
                                    listOf(
                                        gradientTopColor,
                                        gradientBottomColor,
                                    ),
                            ),
                    ).align(Alignment.TopCenter),
            contentAlignment = Alignment.Center,
        ) {
            // O2 logo centered in the gradient area
            Image(
                painter = painterResource(id = R.drawable.o2_logo),
                contentDescription = null, // Decorative background
                modifier = Modifier.fillMaxWidth(0.4f),
                contentScale = ContentScale.Fit,
            )
        }

        // Content positioned in bottom 2/3 of screen
        Box(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(0.67f) // Bottom 2/3
                    .background(gradientBottomColor) // Same as bottom gradient color
                    .align(Alignment.BottomCenter)
                    .windowInsetsPadding(WindowInsets.systemBars), // Respect system bars
        ) {
            content()
        }
    }
}

/**
 * O2 Content Card - Theme-aware elevated card for content overlay.
 *
 * Standard container for content displayed over O2BrandedBackground.
 * Provides consistent styling, elevation, and spacing with automatic theme adaptation.
 *
 * Features:
 * - Theme-aware background (white in light mode, dark surface in dark mode)
 * - Elevation shadow
 * - Rounded corners (16dp)
 * - Padding for comfortable content spacing
 * - Full-width with side margins
 *
 * @param modifier Optional modifier for custom layout behavior
 * @param content Content to display inside the card
 */
@Composable
fun O2ContentCard(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit,
) {
    Card(
        modifier =
            modifier
                .fillMaxWidth()
                .padding(horizontal = O2Spacing.lg),
        shape = RoundedCornerShape(16.dp),
        colors =
            CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surface,
            ),
        elevation =
            CardDefaults.cardElevation(
                defaultElevation = 8.dp,
            ),
    ) {
        Box(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(O2Spacing.lg),
        ) {
            content()
        }
    }
}

// ═══════════════════════════════════════════════════════════
// Previews
// ═══════════════════════════════════════════════════════════

@Preview(name = "O2 Branded Background with Content Card", showBackground = true)
@Composable
fun O2BrandedBackgroundPreview() {
    O2Theme {
        O2BrandedBackground {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center,
            ) {
                O2ContentCard {
                    Column {
                        androidx.compose.material3.Text(
                            text = "O2 Scratch Card",
                            style = MaterialTheme.typography.headlineMedium,
                            color = O2Colors.Neutral1000,
                        )
                        androidx.compose.material3.Text(
                            text = "Content in white card overlay",
                            style = MaterialTheme.typography.bodyLarge,
                            color = O2Colors.Neutral700,
                            modifier = Modifier.padding(top = O2Spacing.sm),
                        )
                    }
                }
            }
        }
    }
}
