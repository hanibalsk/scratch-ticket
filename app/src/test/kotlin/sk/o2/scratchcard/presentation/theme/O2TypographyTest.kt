package sk.o2.scratchcard.presentation.theme

import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

/**
 * Unit tests for O2 typography system.
 *
 * Validates:
 * - Font sizes, weights, and line heights are correct
 * - Letter spacing adjustments are applied
 * - Typography follows Material Design 3 scale
 * - All text styles use default font family (Roboto)
 */
class O2TypographyTest {
    @Test
    fun `displayLarge has correct properties`() {
        val style = O2Typography.displayLarge

        assertEquals(FontFamily.Default, style.fontFamily)
        assertEquals(FontWeight.Bold, style.fontWeight)
        assertEquals(40.sp, style.fontSize)
        assertEquals(0.2.sp, style.letterSpacing)
        assertEquals(48.sp, style.lineHeight)
    }

    @Test
    fun `displayMedium has correct properties`() {
        val style = O2Typography.displayMedium

        assertEquals(FontFamily.Default, style.fontFamily)
        assertEquals(FontWeight.Bold, style.fontWeight)
        assertEquals(36.sp, style.fontSize)
        assertEquals(0.2.sp, style.letterSpacing)
        assertEquals(44.sp, style.lineHeight)
    }

    @Test
    fun `displaySmall has correct properties`() {
        val style = O2Typography.displaySmall

        assertEquals(FontFamily.Default, style.fontFamily)
        assertEquals(FontWeight.Bold, style.fontWeight)
        assertEquals(32.sp, style.fontSize)
        assertEquals(0.2.sp, style.letterSpacing)
        assertEquals(40.sp, style.lineHeight)
    }

    @Test
    fun `headlineLarge has correct properties`() {
        val style = O2Typography.headlineLarge

        assertEquals(FontFamily.Default, style.fontFamily)
        assertEquals(FontWeight.Bold, style.fontWeight)
        assertEquals(28.sp, style.fontSize)
        assertEquals(0.2.sp, style.letterSpacing)
        assertEquals(36.sp, style.lineHeight)
    }

    @Test
    fun `headlineMedium has correct properties`() {
        val style = O2Typography.headlineMedium

        assertEquals(FontFamily.Default, style.fontFamily)
        assertEquals(FontWeight.SemiBold, style.fontWeight)
        assertEquals(24.sp, style.fontSize)
        assertEquals(0.2.sp, style.letterSpacing)
        assertEquals(32.sp, style.lineHeight)
    }

    @Test
    fun `headlineSmall has correct properties`() {
        val style = O2Typography.headlineSmall

        assertEquals(FontFamily.Default, style.fontFamily)
        assertEquals(FontWeight.SemiBold, style.fontWeight)
        assertEquals(22.sp, style.fontSize)
        assertEquals(0.sp, style.letterSpacing)
        assertEquals(28.sp, style.lineHeight)
    }

    @Test
    fun `titleLarge has correct properties`() {
        val style = O2Typography.titleLarge

        assertEquals(FontFamily.Default, style.fontFamily)
        assertEquals(FontWeight.SemiBold, style.fontWeight)
        assertEquals(18.sp, style.fontSize)
        assertEquals(0.sp, style.letterSpacing)
        assertEquals(24.sp, style.lineHeight)
    }

    @Test
    fun `titleMedium has correct properties`() {
        val style = O2Typography.titleMedium

        assertEquals(FontFamily.Default, style.fontFamily)
        assertEquals(FontWeight.Medium, style.fontWeight)
        assertEquals(16.sp, style.fontSize)
        assertEquals(0.sp, style.letterSpacing)
        assertEquals(22.sp, style.lineHeight)
    }

    @Test
    fun `titleSmall has correct properties`() {
        val style = O2Typography.titleSmall

        assertEquals(FontFamily.Default, style.fontFamily)
        assertEquals(FontWeight.Medium, style.fontWeight)
        assertEquals(14.sp, style.fontSize)
        assertEquals(0.sp, style.letterSpacing)
        assertEquals(20.sp, style.lineHeight)
    }

    @Test
    fun `bodyLarge has correct properties`() {
        val style = O2Typography.bodyLarge

        assertEquals(FontFamily.Default, style.fontFamily)
        assertEquals(FontWeight.Normal, style.fontWeight)
        assertEquals(16.sp, style.fontSize)
        assertEquals(0.sp, style.letterSpacing)
        assertEquals(24.sp, style.lineHeight)
    }

    @Test
    fun `bodyMedium has correct properties`() {
        val style = O2Typography.bodyMedium

        assertEquals(FontFamily.Default, style.fontFamily)
        assertEquals(FontWeight.Normal, style.fontWeight)
        assertEquals(14.sp, style.fontSize)
        assertEquals(0.sp, style.letterSpacing)
        assertEquals(20.sp, style.lineHeight)
    }

    @Test
    fun `bodySmall has correct properties`() {
        val style = O2Typography.bodySmall

        assertEquals(FontFamily.Default, style.fontFamily)
        assertEquals(FontWeight.Normal, style.fontWeight)
        assertEquals(12.sp, style.fontSize)
        assertEquals(0.sp, style.letterSpacing)
        assertEquals(16.sp, style.lineHeight)
    }

    @Test
    fun `labelLarge has correct properties`() {
        val style = O2Typography.labelLarge

        assertEquals(FontFamily.Default, style.fontFamily)
        assertEquals(FontWeight.Medium, style.fontWeight)
        assertEquals(14.sp, style.fontSize)
        assertEquals(0.sp, style.letterSpacing)
        assertEquals(20.sp, style.lineHeight)
    }

    @Test
    fun `labelMedium has correct properties`() {
        val style = O2Typography.labelMedium

        assertEquals(FontFamily.Default, style.fontFamily)
        assertEquals(FontWeight.Medium, style.fontWeight)
        assertEquals(12.sp, style.fontSize)
        assertEquals(0.sp, style.letterSpacing)
        assertEquals(16.sp, style.lineHeight)
    }

    @Test
    fun `labelSmall has correct properties`() {
        val style = O2Typography.labelSmall

        assertEquals(FontFamily.Default, style.fontFamily)
        assertEquals(FontWeight.Medium, style.fontWeight)
        assertEquals(10.sp, style.fontSize)
        assertEquals(0.sp, style.letterSpacing)
        assertEquals(14.sp, style.lineHeight)
    }

    @Test
    fun `all text styles use default font family`() {
        assertEquals(FontFamily.Default, O2Typography.displayLarge.fontFamily)
        assertEquals(FontFamily.Default, O2Typography.headlineLarge.fontFamily)
        assertEquals(FontFamily.Default, O2Typography.titleLarge.fontFamily)
        assertEquals(FontFamily.Default, O2Typography.bodyLarge.fontFamily)
        assertEquals(FontFamily.Default, O2Typography.labelLarge.fontFamily)
    }

    @Test
    fun `display and headline styles have letter spacing`() {
        assertTrue(O2Typography.displayLarge.letterSpacing.value > 0)
        assertTrue(O2Typography.displayMedium.letterSpacing.value > 0)
        assertTrue(O2Typography.displaySmall.letterSpacing.value > 0)
        assertTrue(O2Typography.headlineLarge.letterSpacing.value > 0)
        assertTrue(O2Typography.headlineMedium.letterSpacing.value > 0)
    }

    @Test
    fun `font sizes follow descending order within categories`() {
        // Display
        assertTrue(O2Typography.displayLarge.fontSize > O2Typography.displayMedium.fontSize)
        assertTrue(O2Typography.displayMedium.fontSize > O2Typography.displaySmall.fontSize)

        // Headline
        assertTrue(O2Typography.headlineLarge.fontSize > O2Typography.headlineMedium.fontSize)
        assertTrue(O2Typography.headlineMedium.fontSize > O2Typography.headlineSmall.fontSize)

        // Title
        assertTrue(O2Typography.titleLarge.fontSize > O2Typography.titleMedium.fontSize)
        assertTrue(O2Typography.titleMedium.fontSize > O2Typography.titleSmall.fontSize)

        // Body
        assertTrue(O2Typography.bodyLarge.fontSize > O2Typography.bodyMedium.fontSize)
        assertTrue(O2Typography.bodyMedium.fontSize > O2Typography.bodySmall.fontSize)

        // Label
        assertTrue(O2Typography.labelLarge.fontSize > O2Typography.labelMedium.fontSize)
        assertTrue(O2Typography.labelMedium.fontSize > O2Typography.labelSmall.fontSize)
    }

    @Test
    fun `all text styles are accessible`() {
        assertNotNull(O2Typography.displayLarge)
        assertNotNull(O2Typography.displayMedium)
        assertNotNull(O2Typography.displaySmall)
        assertNotNull(O2Typography.headlineLarge)
        assertNotNull(O2Typography.headlineMedium)
        assertNotNull(O2Typography.headlineSmall)
        assertNotNull(O2Typography.titleLarge)
        assertNotNull(O2Typography.titleMedium)
        assertNotNull(O2Typography.titleSmall)
        assertNotNull(O2Typography.bodyLarge)
        assertNotNull(O2Typography.bodyMedium)
        assertNotNull(O2Typography.bodySmall)
        assertNotNull(O2Typography.labelLarge)
        assertNotNull(O2Typography.labelMedium)
        assertNotNull(O2Typography.labelSmall)
    }
}
