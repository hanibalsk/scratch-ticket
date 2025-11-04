package sk.o2.scratchcard.domain.model

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

/**
 * Unit tests for ScratchCardState sealed class.
 *
 * Validates:
 * - Sealed class behavior and type safety
 * - State equality and uniqueness
 * - Data class properties
 * - Object singleton behavior
 */
class ScratchCardStateTest {

    @Test
    fun `Unscratched is singleton object`() {
        val state1 = ScratchCardState.Unscratched
        val state2 = ScratchCardState.Unscratched

        // Object instances are the same reference
        assertSame(state1, state2)
    }

    @Test
    fun `Scratched equality based on code property`() {
        val code = "550e8400-e29b-41d4-a716-446655440000"
        val state1 = ScratchCardState.Scratched(code)
        val state2 = ScratchCardState.Scratched(code)
        val state3 = ScratchCardState.Scratched("different-uuid")

        // Data classes with same properties are equal
        assertEquals(state1, state2)
        assertEquals(state1.code, state2.code)

        // Different codes mean different states
        assertNotEquals(state1, state3)
    }

    @Test
    fun `Activated equality based on code property`() {
        val code = "550e8400-e29b-41d4-a716-446655440000"
        val state1 = ScratchCardState.Activated(code)
        val state2 = ScratchCardState.Activated(code)
        val state3 = ScratchCardState.Activated("different-uuid")

        assertEquals(state1, state2)
        assertEquals(state1.code, state2.code)
        assertNotEquals(state1, state3)
    }

    @Test
    fun `Scratched and Activated with same code are not equal`() {
        val code = "550e8400-e29b-41d4-a716-446655440000"
        val scratched = ScratchCardState.Scratched(code)
        val activated = ScratchCardState.Activated(code)

        // Different sealed class types, even with same code
        assertNotEquals(scratched, activated)
    }

    @Test
    fun `sealed class provides type safety with when expressions`() {
        val states = listOf(
            ScratchCardState.Unscratched,
            ScratchCardState.Scratched("test-uuid"),
            ScratchCardState.Activated("test-uuid")
        )

        states.forEach { state ->
            // When expression must be exhaustive for sealed class
            val description = when (state) {
                is ScratchCardState.Unscratched -> "Card is unscratched"
                is ScratchCardState.Scratched -> "Card is scratched with code: ${state.code}"
                is ScratchCardState.Activated -> "Card is activated with code: ${state.code}"
            }

            assertNotNull(description)
        }
    }

    @Test
    fun `Unscratched state has no properties`() {
        val state = ScratchCardState.Unscratched

        // Unscratched is an object with no properties
        assertEquals("Unscratched", state.toString())
    }

    @Test
    fun `Scratched state stores code correctly`() {
        val code = "test-uuid-12345"
        val state = ScratchCardState.Scratched(code)

        assertEquals(code, state.code)
        assertTrue(state.code.isNotEmpty())
    }

    @Test
    fun `Activated state stores code correctly`() {
        val code = "test-uuid-67890"
        val state = ScratchCardState.Activated(code)

        assertEquals(code, state.code)
        assertTrue(state.code.isNotEmpty())
    }

    @Test
    fun `state hierarchy is correctly structured`() {
        val states: List<ScratchCardState> = listOf(
            ScratchCardState.Unscratched,
            ScratchCardState.Scratched("code1"),
            ScratchCardState.Activated("code2")
        )

        // All instances are ScratchCardState
        states.forEach { state ->
            assertTrue(state is ScratchCardState)
        }

        // But each has unique type
        assertTrue(states[0] is ScratchCardState.Unscratched)
        assertTrue(states[1] is ScratchCardState.Scratched)
        assertTrue(states[2] is ScratchCardState.Activated)
    }
}
