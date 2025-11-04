package sk.o2.scratchcard.presentation.theme

import androidx.compose.foundation.shape.RoundedCornerShape
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

/**
 * Unit tests for O2 shape system.
 *
 * Validates:
 * - Shape values are correctly defined
 * - All shapes are RoundedCornerShape
 * - Shapes are accessible
 */
class O2ShapesTest {
    @Test
    fun `all shapes are RoundedCornerShape`() {
        assertTrue(O2Shapes.extraSmall is RoundedCornerShape)
        assertTrue(O2Shapes.small is RoundedCornerShape)
        assertTrue(O2Shapes.medium is RoundedCornerShape)
        assertTrue(O2Shapes.large is RoundedCornerShape)
        assertTrue(O2Shapes.extraLarge is RoundedCornerShape)
    }

    @Test
    fun `all shapes have uniform corners`() {
        val extraSmall = O2Shapes.extraSmall as RoundedCornerShape
        assertEquals(extraSmall.topStart, extraSmall.topEnd)
        assertEquals(extraSmall.topStart, extraSmall.bottomStart)
        assertEquals(extraSmall.topStart, extraSmall.bottomEnd)

        val small = O2Shapes.small as RoundedCornerShape
        assertEquals(small.topStart, small.topEnd)
        assertEquals(small.topStart, small.bottomStart)
        assertEquals(small.topStart, small.bottomEnd)

        val medium = O2Shapes.medium as RoundedCornerShape
        assertEquals(medium.topStart, medium.topEnd)
        assertEquals(medium.topStart, medium.bottomStart)
        assertEquals(medium.topStart, medium.bottomEnd)

        val large = O2Shapes.large as RoundedCornerShape
        assertEquals(large.topStart, large.topEnd)
        assertEquals(large.topStart, large.bottomStart)
        assertEquals(large.topStart, large.bottomEnd)

        val extraLarge = O2Shapes.extraLarge as RoundedCornerShape
        assertEquals(extraLarge.topStart, extraLarge.topEnd)
        assertEquals(extraLarge.topStart, extraLarge.bottomStart)
        assertEquals(extraLarge.topStart, extraLarge.bottomEnd)
    }

    @Test
    fun `all shapes are accessible`() {
        assertNotNull(O2Shapes.extraSmall)
        assertNotNull(O2Shapes.small)
        assertNotNull(O2Shapes.medium)
        assertNotNull(O2Shapes.large)
        assertNotNull(O2Shapes.extraLarge)
    }

    @Test
    fun `shapes object has all required shape sizes`() {
        // Verify all shape sizes are defined in the Shapes object
        val shapes = O2Shapes

        assertNotNull(shapes.extraSmall)
        assertNotNull(shapes.small)
        assertNotNull(shapes.medium)
        assertNotNull(shapes.large)
        assertNotNull(shapes.extraLarge)
    }
}
