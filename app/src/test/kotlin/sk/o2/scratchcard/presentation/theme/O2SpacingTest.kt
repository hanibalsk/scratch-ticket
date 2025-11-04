package sk.o2.scratchcard.presentation.theme

import androidx.compose.ui.unit.dp
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

/**
 * Unit tests for O2 spacing system.
 *
 * Validates:
 * - Spacing values follow 8dp base grid
 * - Spacing scale is progressive
 * - All spacing values are accessible
 */
class O2SpacingTest {
    @Test
    fun `xs spacing is 4dp`() {
        assertEquals(4.dp, O2Spacing.xs)
    }

    @Test
    fun `sm spacing is 8dp`() {
        assertEquals(8.dp, O2Spacing.sm)
    }

    @Test
    fun `md spacing is 16dp`() {
        assertEquals(16.dp, O2Spacing.md)
    }

    @Test
    fun `lg spacing is 24dp`() {
        assertEquals(24.dp, O2Spacing.lg)
    }

    @Test
    fun `xl spacing is 32dp`() {
        assertEquals(32.dp, O2Spacing.xl)
    }

    @Test
    fun `xxl spacing is 40dp`() {
        assertEquals(40.dp, O2Spacing.xxl)
    }

    @Test
    fun `xxxl spacing is 48dp`() {
        assertEquals(48.dp, O2Spacing.xxxl)
    }

    @Test
    fun `spacing follows progressive scale`() {
        assertTrue(O2Spacing.xs < O2Spacing.sm)
        assertTrue(O2Spacing.sm < O2Spacing.md)
        assertTrue(O2Spacing.md < O2Spacing.lg)
        assertTrue(O2Spacing.lg < O2Spacing.xl)
        assertTrue(O2Spacing.xl < O2Spacing.xxl)
        assertTrue(O2Spacing.xxl < O2Spacing.xxxl)
    }

    @Test
    fun `sm is base grid unit 8dp`() {
        assertEquals(8.dp, O2Spacing.sm)
    }

    @Test
    fun `xs is sub-grid unit 4dp`() {
        assertEquals(4.dp, O2Spacing.xs)
    }

    @Test
    fun `all spacing values are positive`() {
        assertTrue(O2Spacing.xs.value > 0)
        assertTrue(O2Spacing.sm.value > 0)
        assertTrue(O2Spacing.md.value > 0)
        assertTrue(O2Spacing.lg.value > 0)
        assertTrue(O2Spacing.xl.value > 0)
        assertTrue(O2Spacing.xxl.value > 0)
        assertTrue(O2Spacing.xxxl.value > 0)
    }

    @Test
    fun `spacing follows multiples of base grid`() {
        // xs is 4dp (sub-grid)
        assertEquals(4.dp, O2Spacing.xs)

        // All others should be multiples of 8dp
        assertEquals(0, O2Spacing.sm.value.toInt() % 8)
        assertEquals(0, O2Spacing.md.value.toInt() % 8)
        assertEquals(0, O2Spacing.lg.value.toInt() % 8)
        assertEquals(0, O2Spacing.xl.value.toInt() % 8)
        assertEquals(0, O2Spacing.xxl.value.toInt() % 8)
        assertEquals(0, O2Spacing.xxxl.value.toInt() % 8)
    }

    @Test
    fun `all spacing values are accessible`() {
        assertNotNull(O2Spacing.xs)
        assertNotNull(O2Spacing.sm)
        assertNotNull(O2Spacing.md)
        assertNotNull(O2Spacing.lg)
        assertNotNull(O2Spacing.xl)
        assertNotNull(O2Spacing.xxl)
        assertNotNull(O2Spacing.xxxl)
    }
}
