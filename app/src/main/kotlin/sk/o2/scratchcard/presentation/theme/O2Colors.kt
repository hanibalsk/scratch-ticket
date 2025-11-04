package sk.o2.scratchcard.presentation.theme

import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.ui.graphics.Color

/**
 * O2 Brand Color Palette.
 *
 * Complete color system following O2 Corporate Identity guidelines.
 * Includes primary brand colors, semantic colors, and neutral ramp.
 *
 * Reference: docs/o2-design-system.md
 */
object O2Colors {

    // ═══════════════════════════════════════════════════════════
    // Brand Primary Colors
    // ═══════════════════════════════════════════════════════════

    /**
     * Beyond Blue - O2's primary brand color.
     * Use for primary buttons, links, active states.
     */
    val BluePrimary = Color(0xFF0050FF)

    /**
     * Deep O2 Blue - Headers, dark surfaces, logo backgrounds.
     * Use for emphasis and brand-heavy sections.
     */
    val BlueDeep = Color(0xFF0112AA)

    /**
     * Legacy Navy - Optional deep accents for dark mode.
     * Use sparingly for dark mode differentiation.
     */
    val Navy = Color(0xFF000068)

    // ═══════════════════════════════════════════════════════════
    // Tints
    // ═══════════════════════════════════════════════════════════

    /**
     * Sky-05 - Very light blue tint.
     * Use for subtle backgrounds, card tints, dividers.
     */
    val Sky05 = Color(0xFFE5F2FA)

    // ═══════════════════════════════════════════════════════════
    // Semantic Colors
    // ═══════════════════════════════════════════════════════════

    /**
     * Success Green - Success states, activated card status.
     */
    val Success = Color(0xFF1DB954)

    /**
     * Warning Amber - Warnings, important notices.
     */
    val Warning = Color(0xFFFFB300)

    /**
     * Error Red - Errors, validation failures, destructive actions.
     */
    val Error = Color(0xFFE53935)

    // ═══════════════════════════════════════════════════════════
    // Neutral Ramp (Darkest to Lightest)
    // ═══════════════════════════════════════════════════════════

    /**
     * Neutral 1000 - Darkest neutral.
     * Use for dark mode surfaces.
     */
    val Neutral1000 = Color(0xFF0B0C0E)

    /**
     * Neutral 900 - Very dark.
     */
    val Neutral900 = Color(0xFF1A1C1F)

    /**
     * Neutral 800 - Dark borders in dark mode.
     */
    val Neutral800 = Color(0xFF2A2D31)

    /**
     * Neutral 050 - Lightest neutral.
     * Use for light mode backgrounds.
     */
    val Neutral050 = Color(0xFFF5F7FA)

    // Additional neutrals for text and borders
    val Neutral700 = Color(0xFF3A3D41)
    val Neutral600 = Color(0xFF5A5D61)
    val Neutral500 = Color(0xFF7A7D81)
    val Neutral400 = Color(0xFF9A9DA1)
    val Neutral300 = Color(0xFFBABDC1)
    val Neutral200 = Color(0xFFDADDE1)
    val Neutral100 = Color(0xFFE8ECF1)

    // Pure white and black
    val White = Color(0xFFFFFFFF)
    val Black = Color(0xFF000000)
}

/**
 * Light color scheme following O2 design system.
 *
 * Color Usage:
 * - Primary: O2 Beyond Blue for buttons, links, active states
 * - Surface: White for card backgrounds
 * - OnSurface: Dark text on light backgrounds
 * - Error: O2 Error Red for validation failures
 */
val LightColorScheme = lightColorScheme(
    primary = O2Colors.BluePrimary,
    onPrimary = O2Colors.White,
    primaryContainer = O2Colors.Sky05,
    onPrimaryContainer = O2Colors.BlueDeep,

    secondary = O2Colors.BlueDeep,
    onSecondary = O2Colors.White,

    tertiary = O2Colors.Navy,
    onTertiary = O2Colors.White,

    background = O2Colors.White,
    onBackground = O2Colors.Neutral1000,

    surface = O2Colors.White,
    onSurface = O2Colors.Neutral1000,
    surfaceVariant = O2Colors.Neutral050,
    onSurfaceVariant = O2Colors.Neutral700,

    outline = O2Colors.Neutral200,
    outlineVariant = O2Colors.Neutral100,

    error = O2Colors.Error,
    onError = O2Colors.White,
    errorContainer = Color(0xFFFFDAD6),
    onErrorContainer = Color(0xFF410002),

    // Semantic color mappings
    // Success: Use custom O2Colors.Success
    // Warning: Use custom O2Colors.Warning
)

/**
 * Dark color scheme following O2 design system.
 *
 * Dark Mode Adjustments:
 * - Primary: O2 Blue unchanged (good contrast on dark)
 * - Surface: Near-black (#0B0C0E)
 * - OnSurface: Light text (#ECEFF4)
 * - Borders: Increased contrast
 */
val DarkColorScheme = darkColorScheme(
    primary = O2Colors.BluePrimary,
    onPrimary = O2Colors.White,
    primaryContainer = O2Colors.BlueDeep,
    onPrimaryContainer = Color(0xFFB8C7FF),

    secondary = O2Colors.BlueDeep,
    onSecondary = O2Colors.White,

    tertiary = O2Colors.Navy,
    onTertiary = O2Colors.White,

    background = O2Colors.Neutral1000,
    onBackground = Color(0xFFECEFF4),

    surface = O2Colors.Neutral1000,
    onSurface = Color(0xFFECEFF4),
    surfaceVariant = O2Colors.Neutral900,
    onSurfaceVariant = O2Colors.Neutral300,

    outline = O2Colors.Neutral700,
    outlineVariant = O2Colors.Neutral800,

    error = O2Colors.Error,
    onError = O2Colors.White,
    errorContainer = Color(0xFF93000A),
    onErrorContainer = Color(0xFFFFDAD6),
)
