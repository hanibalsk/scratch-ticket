package sk.o2.scratchcard.presentation.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

/**
 * O2 Typography System.
 *
 * Font Decision: Using Roboto (system default) with adjusted letterSpacing
 * to approximate O2's On Air font aesthetic. On Air font licensing not available
 * for this project.
 *
 * Letter Spacing Adjustment: +0.2sp to +0.4sp tracking added to headings
 * to create the airy, open feel characteristic of O2 brand typography.
 *
 * Reference: docs/o2-design-system.md#typography-scale
 */
val O2Typography = Typography(
    // ═══════════════════════════════════════════════════════════
    // Display (40sp, Bold, +0.2sp) - Hero sections, splash
    // ═══════════════════════════════════════════════════════════
    displayLarge = TextStyle(
        fontFamily = FontFamily.Default, // Roboto
        fontWeight = FontWeight.Bold,
        fontSize = 40.sp,
        letterSpacing = 0.2.sp,
        lineHeight = 48.sp
    ),
    displayMedium = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Bold,
        fontSize = 36.sp,
        letterSpacing = 0.2.sp,
        lineHeight = 44.sp
    ),
    displaySmall = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Bold,
        fontSize = 32.sp,
        letterSpacing = 0.2.sp,
        lineHeight = 40.sp
    ),

    // ═══════════════════════════════════════════════════════════
    // Headline (28sp, Bold, +0.2sp) - Page titles
    // ═══════════════════════════════════════════════════════════
    headlineLarge = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Bold,
        fontSize = 28.sp,
        letterSpacing = 0.2.sp,
        lineHeight = 36.sp
    ),
    headlineMedium = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.SemiBold,
        fontSize = 24.sp,
        letterSpacing = 0.2.sp,
        lineHeight = 32.sp
    ),
    headlineSmall = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.SemiBold,
        fontSize = 22.sp,
        letterSpacing = 0.sp,
        lineHeight = 28.sp
    ),

    // ═══════════════════════════════════════════════════════════
    // Title (18sp, SemiBold) - Card titles, button text
    // ═══════════════════════════════════════════════════════════
    titleLarge = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.SemiBold,
        fontSize = 18.sp,
        letterSpacing = 0.sp,
        lineHeight = 24.sp
    ),
    titleMedium = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Medium,
        fontSize = 16.sp,
        letterSpacing = 0.sp,
        lineHeight = 22.sp
    ),
    titleSmall = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Medium,
        fontSize = 14.sp,
        letterSpacing = 0.sp,
        lineHeight = 20.sp
    ),

    // ═══════════════════════════════════════════════════════════
    // Body (16sp, Regular) - Body text, descriptions
    // ═══════════════════════════════════════════════════════════
    bodyLarge = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        letterSpacing = 0.sp,
        lineHeight = 24.sp
    ),
    bodyMedium = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 14.sp,
        letterSpacing = 0.sp,
        lineHeight = 20.sp
    ),
    bodySmall = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 12.sp,
        letterSpacing = 0.sp,
        lineHeight = 16.sp
    ),

    // ═══════════════════════════════════════════════════════════
    // Label (14sp, Medium) - Labels, metadata
    // ═══════════════════════════════════════════════════════════
    labelLarge = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Medium,
        fontSize = 14.sp,
        letterSpacing = 0.sp,
        lineHeight = 20.sp
    ),
    labelMedium = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Medium,
        fontSize = 12.sp,
        letterSpacing = 0.sp,
        lineHeight = 16.sp
    ),
    labelSmall = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Medium,
        fontSize = 10.sp,
        letterSpacing = 0.sp,
        lineHeight = 14.sp
    ),
)
