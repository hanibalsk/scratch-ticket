package sk.o2.scratchcard.presentation.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
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
 * - Top half: O2 logo on blue gradient (using drawable resource)
 * - Bottom half: Gradient continuation to bottom of screen
 * - Content: Displayed in white elevated card overlaying the background
 *
 * Design Pattern:
 * - Branded splash/landing pages
 * - Full-screen branded experiences
 * - Marketing/onboarding flows
 *
 * Visual Hierarchy:
 * - Background: O2 blue gradient (BlueDeep -> BluePrimary -> lighter)
 * - Content: White elevated card with rounded corners
 * - Contrast: High contrast for accessibility
 *
 * @param modifier Optional modifier for custom layout behavior
 * @param content Composable content to display in the white card overlay
 */
@Composable
fun O2BrandedBackground(
    modifier: Modifier = Modifier,
    content: @Composable BoxScope.() -> Unit,
) {
    Box(
        modifier =
            modifier
                .fillMaxSize()
                .background(
                    brush =
                        Brush.verticalGradient(
                            colors =
                                listOf(
                                    O2Colors.BlueDeep, // Dark blue at top
                                    O2Colors.BluePrimary, // O2 primary blue
                                    Color(0xFF0070FF), // Lighter blue at bottom
                                ),
                        ),
                ),
    ) {
        // Background logo/gradient image in top half
        // TODO: Replace R.drawable.bg_o2_gradient with actual logo image (bg_o2_logo.webp or bg_o2_logo.png)
        // The gradient drawable serves as a placeholder until the actual image is added
        Image(
            painter = painterResource(id = R.drawable.bg_o2_gradient),
            contentDescription = null, // Decorative background
            modifier =
                Modifier
                    .fillMaxWidth()
                    .align(Alignment.TopCenter),
            contentScale = ContentScale.Crop,
            alpha = 0.9f, // Slight transparency for subtle effect
        )

        // Content provided by the caller
        content()
    }
}

/**
 * O2 Content Card - White elevated card for content overlay.
 *
 * Standard container for content displayed over O2BrandedBackground.
 * Provides consistent styling, elevation, and spacing.
 *
 * Features:
 * - White background with elevation shadow
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
                containerColor = O2Colors.White,
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
