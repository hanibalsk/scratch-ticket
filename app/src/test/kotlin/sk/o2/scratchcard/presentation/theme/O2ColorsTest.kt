package sk.o2.scratchcard.presentation.theme

import androidx.compose.ui.graphics.Color
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

/**
 * Unit tests for O2 color system.
 *
 * Validates:
 * - Color values are correctly defined
 * - Brand colors match O2 Corporate Identity
 * - Light and dark color schemes are properly configured
 * - Semantic colors are accessible
 */
class O2ColorsTest {
    @Test
    fun `BluePrimary is O2 brand blue`() {
        assertEquals(Color(0xFF0050FF), O2Colors.BluePrimary)
    }

    @Test
    fun `BlueDeep is correct deep blue`() {
        assertEquals(Color(0xFF0112AA), O2Colors.BlueDeep)
    }

    @Test
    fun `Navy is correct navy color`() {
        assertEquals(Color(0xFF000068), O2Colors.Navy)
    }

    @Test
    fun `Sky05 is correct light blue tint`() {
        assertEquals(Color(0xFFE5F2FA), O2Colors.Sky05)
    }

    @Test
    fun `semantic colors are correctly defined`() {
        assertEquals(Color(0xFF1DB954), O2Colors.Success)
        assertEquals(Color(0xFFFFB300), O2Colors.Warning)
        assertEquals(Color(0xFFE53935), O2Colors.Error)
    }

    @Test
    fun `neutral colors range from darkest to lightest`() {
        // Darkest
        assertEquals(Color(0xFF0B0C0E), O2Colors.Neutral1000)
        assertEquals(Color(0xFF1A1C1F), O2Colors.Neutral900)
        assertEquals(Color(0xFF2A2D31), O2Colors.Neutral800)
        assertEquals(Color(0xFF3A3D41), O2Colors.Neutral700)
        assertEquals(Color(0xFF5A5D61), O2Colors.Neutral600)
        assertEquals(Color(0xFF7A7D81), O2Colors.Neutral500)
        assertEquals(Color(0xFF9A9DA1), O2Colors.Neutral400)
        assertEquals(Color(0xFFBABDC1), O2Colors.Neutral300)
        assertEquals(Color(0xFFDADDE1), O2Colors.Neutral200)
        assertEquals(Color(0xFFE8ECF1), O2Colors.Neutral100)
        // Lightest
        assertEquals(Color(0xFFF5F7FA), O2Colors.Neutral050)
    }

    @Test
    fun `white and black are pure colors`() {
        assertEquals(Color(0xFFFFFFFF), O2Colors.White)
        assertEquals(Color(0xFF000000), O2Colors.Black)
    }

    @Test
    fun `LightColorScheme uses BluePrimary as primary`() {
        assertEquals(O2Colors.BluePrimary, LightColorScheme.primary)
    }

    @Test
    fun `LightColorScheme uses White as onPrimary`() {
        assertEquals(O2Colors.White, LightColorScheme.onPrimary)
    }

    @Test
    fun `LightColorScheme uses White as surface`() {
        assertEquals(O2Colors.White, LightColorScheme.surface)
    }

    @Test
    fun `LightColorScheme uses Error color`() {
        assertEquals(O2Colors.Error, LightColorScheme.error)
    }

    @Test
    fun `DarkColorScheme uses BluePrimary as primary`() {
        assertEquals(O2Colors.BluePrimary, DarkColorScheme.primary)
    }

    @Test
    fun `DarkColorScheme uses Neutral1000 as surface`() {
        assertEquals(O2Colors.Neutral1000, DarkColorScheme.surface)
    }

    @Test
    fun `DarkColorScheme uses Error color`() {
        assertEquals(O2Colors.Error, DarkColorScheme.error)
    }

    @Test
    fun `light and dark schemes have different background colors`() {
        assertNotEquals(LightColorScheme.background, DarkColorScheme.background)
    }

    @Test
    fun `light scheme has light background`() {
        assertEquals(O2Colors.White, LightColorScheme.background)
    }

    @Test
    fun `dark scheme has dark background`() {
        assertEquals(O2Colors.Neutral1000, DarkColorScheme.background)
    }

    @Test
    fun `all color objects are accessible`() {
        // Brand colors
        assertNotNull(O2Colors.BluePrimary)
        assertNotNull(O2Colors.BlueDeep)
        assertNotNull(O2Colors.Navy)

        // Tints
        assertNotNull(O2Colors.Sky05)

        // Semantic
        assertNotNull(O2Colors.Success)
        assertNotNull(O2Colors.Warning)
        assertNotNull(O2Colors.Error)

        // Neutrals
        assertNotNull(O2Colors.Neutral1000)
        assertNotNull(O2Colors.Neutral900)
        assertNotNull(O2Colors.Neutral050)

        // Pure colors
        assertNotNull(O2Colors.White)
        assertNotNull(O2Colors.Black)
    }

    @Test
    fun `color schemes are fully configured`() {
        // Light scheme
        assertNotNull(LightColorScheme.primary)
        assertNotNull(LightColorScheme.onPrimary)
        assertNotNull(LightColorScheme.background)
        assertNotNull(LightColorScheme.onBackground)
        assertNotNull(LightColorScheme.surface)
        assertNotNull(LightColorScheme.onSurface)
        assertNotNull(LightColorScheme.error)
        assertNotNull(LightColorScheme.onError)

        // Dark scheme
        assertNotNull(DarkColorScheme.primary)
        assertNotNull(DarkColorScheme.onPrimary)
        assertNotNull(DarkColorScheme.background)
        assertNotNull(DarkColorScheme.onBackground)
        assertNotNull(DarkColorScheme.surface)
        assertNotNull(DarkColorScheme.onSurface)
        assertNotNull(DarkColorScheme.error)
        assertNotNull(DarkColorScheme.onError)
    }
}
