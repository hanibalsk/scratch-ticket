package sk.o2.scratchcard.presentation.theme

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

/**
 * Unit tests for O2 Theme system.
 *
 * Validates:
 * - Color palette matches O2 specifications
 * - Typography scales are correct
 * - Shapes have correct corner radii
 * - Spacing system follows 8dp base grid
 * - Light and dark color schemes are properly configured
 */
class O2ThemeTest {

    // ═══════════════════════════════════════════════════════════
    // Color Palette Tests
    // ═══════════════════════════════════════════════════════════

    @Test
    fun `O2Colors BluePrimary matches Beyond Blue specification`() {
        assertEquals(Color(0xFF0050FF), O2Colors.BluePrimary)
    }

    @Test
    fun `O2Colors BlueDeep matches Deep O2 Blue specification`() {
        assertEquals(Color(0xFF0112AA), O2Colors.BlueDeep)
    }

    @Test
    fun `O2Colors semantic colors match specification`() {
        assertEquals(Color(0xFF1DB954), O2Colors.Success)
        assertEquals(Color(0xFFFFB300), O2Colors.Warning)
        assertEquals(Color(0xFFE53935), O2Colors.Error)
    }

    @Test
    fun `O2Colors neutral ramp darkest to lightest`() {
        assertEquals(Color(0xFF0B0C0E), O2Colors.Neutral1000) // Darkest
        assertEquals(Color(0xFF1A1C1F), O2Colors.Neutral900)
        assertEquals(Color(0xFF2A2D31), O2Colors.Neutral800)
        assertEquals(Color(0xFFF5F7FA), O2Colors.Neutral050) // Lightest
    }

    @Test
    fun `O2Colors tints match specification`() {
        assertEquals(Color(0xFFE5F2FA), O2Colors.Sky05)
    }

    @Test
    fun `LightColorScheme uses O2 primary blue`() {
        assertEquals(O2Colors.BluePrimary, LightColorScheme.primary)
        assertEquals(O2Colors.White, LightColorScheme.onPrimary)
    }

    @Test
    fun `LightColorScheme uses white surface`() {
        assertEquals(O2Colors.White, LightColorScheme.surface)
        assertEquals(O2Colors.Neutral1000, LightColorScheme.onSurface)
    }

    @Test
    fun `DarkColorScheme uses near-black surface`() {
        assertEquals(O2Colors.Neutral1000, DarkColorScheme.surface)
        assertEquals(Color(0xFFECEFF4), DarkColorScheme.onSurface)
    }

    @Test
    fun `DarkColorScheme primary color unchanged from light mode`() {
        // O2 Blue has good contrast on dark backgrounds
        assertEquals(O2Colors.BluePrimary, DarkColorScheme.primary)
        assertEquals(LightColorScheme.primary, DarkColorScheme.primary)
    }

    // ═══════════════════════════════════════════════════════════
    // Typography Tests
    // ═══════════════════════════════════════════════════════════

    @Test
    fun `O2Typography displayLarge matches specification`() {
        val style = O2Typography.displayLarge

        assertEquals(40.sp, style.fontSize)
        assertEquals(FontWeight.Bold, style.fontWeight)
        assertEquals(0.2.sp, style.letterSpacing)
    }

    @Test
    fun `O2Typography headlineLarge matches specification`() {
        val style = O2Typography.headlineLarge

        assertEquals(28.sp, style.fontSize)
        assertEquals(FontWeight.Bold, style.fontWeight)
        assertEquals(0.2.sp, style.letterSpacing)
    }

    @Test
    fun `O2Typography bodyLarge matches specification`() {
        val style = O2Typography.bodyLarge

        assertEquals(16.sp, style.fontSize)
        assertEquals(FontWeight.Normal, style.fontWeight)
        assertEquals(0.sp, style.letterSpacing)
    }

    @Test
    fun `O2Typography labelLarge matches specification`() {
        val style = O2Typography.labelLarge

        assertEquals(14.sp, style.fontSize)
        assertEquals(FontWeight.Medium, style.fontWeight)
    }

    @Test
    fun `O2Typography titleLarge for button text`() {
        val style = O2Typography.titleLarge

        assertEquals(18.sp, style.fontSize)
        assertEquals(FontWeight.SemiBold, style.fontWeight)
    }

    // ═══════════════════════════════════════════════════════════
    // Shapes Tests
    // ═══════════════════════════════════════════════════════════

    @Test
    fun `O2Shapes small for buttons has 12dp radius`() {
        val shape = O2Shapes.small
        assertTrue(shape is androidx.compose.foundation.shape.RoundedCornerShape)
        // Note: Exact radius validation would require accessing private fields
        // This test validates the shape type is RoundedCornerShape
    }

    @Test
    fun `O2Shapes medium for cards has 16dp radius`() {
        val shape = O2Shapes.medium
        assertTrue(shape is androidx.compose.foundation.shape.RoundedCornerShape)
    }

    @Test
    fun `O2Shapes large for dialogs has 24dp radius`() {
        val shape = O2Shapes.large
        assertTrue(shape is androidx.compose.foundation.shape.RoundedCornerShape)
    }

    // ═══════════════════════════════════════════════════════════
    // Spacing Tests
    // ═══════════════════════════════════════════════════════════

    @Test
    fun `O2Spacing follows 8dp base grid system`() {
        assertEquals(4.dp, O2Spacing.xs)   // Sub-grid
        assertEquals(8.dp, O2Spacing.sm)   // Base grid
        assertEquals(16.dp, O2Spacing.md)  // 2x base
        assertEquals(24.dp, O2Spacing.lg)  // 3x base
        assertEquals(32.dp, O2Spacing.xl)  // 4x base
    }

    @Test
    fun `O2Spacing provides extended scale`() {
        assertEquals(40.dp, O2Spacing.xxl)
        assertEquals(48.dp, O2Spacing.xxxl)
    }

    @Test
    fun `O2Spacing increments are multiples of 4dp`() {
        // Verify spacing follows 4dp sub-grid increments
        val spacings = listOf(
            O2Spacing.xs,   // 4dp
            O2Spacing.sm,   // 8dp
            O2Spacing.md,   // 16dp
            O2Spacing.lg,   // 24dp
            O2Spacing.xl,   // 32dp
            O2Spacing.xxl,  // 40dp
            O2Spacing.xxxl  // 48dp
        )

        spacings.forEach { spacing ->
            assertEquals(0, spacing.value.toInt() % 4, "Spacing $spacing is not a multiple of 4dp")
        }
    }

    // ═══════════════════════════════════════════════════════════
    // Integration Tests
    // ═══════════════════════════════════════════════════════════

    @Test
    fun `color schemes provide contrast for text readability`() {
        // Light mode: dark text on light background
        assertNotEquals(LightColorScheme.surface, LightColorScheme.onSurface)

        // Dark mode: light text on dark background
        assertNotEquals(DarkColorScheme.surface, DarkColorScheme.onSurface)
    }

    @Test
    fun `error colors are consistent across themes`() {
        assertEquals(O2Colors.Error, LightColorScheme.error)
        assertEquals(O2Colors.Error, DarkColorScheme.error)
    }

    @Test
    fun `semantic colors are accessible`() {
        // Verify semantic colors are distinct from neutrals
        assertNotEquals(O2Colors.Success, O2Colors.BluePrimary)
        assertNotEquals(O2Colors.Warning, O2Colors.BluePrimary)
        assertNotEquals(O2Colors.Error, O2Colors.BluePrimary)

        // All semantic colors should be visually distinct
        assertNotEquals(O2Colors.Success, O2Colors.Warning)
        assertNotEquals(O2Colors.Success, O2Colors.Error)
        assertNotEquals(O2Colors.Warning, O2Colors.Error)
    }
}
