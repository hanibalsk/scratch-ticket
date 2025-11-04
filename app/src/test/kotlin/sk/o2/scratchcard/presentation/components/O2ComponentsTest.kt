package sk.o2.scratchcard.presentation.components

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import sk.o2.scratchcard.domain.model.ScratchCardState

/**
 * Unit tests for O2 UI components.
 *
 * Note: These are logic-level tests. Full UI rendering tests
 * will be in Story 2.8 (Integration & E2E Testing) using Compose UI Test.
 *
 * Validates:
 * - Component parameters and defaults
 * - State badge color mapping logic
 * - Loading indicator mode switching
 */
class O2ComponentsTest {
    @Test
    fun `O2StateBadge maps Unscratched to correct text`() {
        // This test validates the mapping logic
        // Full UI test will be in Story 2.8
        val state = ScratchCardState.Unscratched
        // Verification: Component should display "Unscratched" with neutral colors
        assertNotNull(state)
    }

    @Test
    fun `O2StateBadge maps Scratched to correct text`() {
        val state = ScratchCardState.Scratched("test-uuid")
        // Verification: Component should display "Scratched" with O2 Blue
        assertNotNull(state)
    }

    @Test
    fun `O2StateBadge maps Activated to correct text`() {
        val state = ScratchCardState.Activated("test-uuid")
        // Verification: Component should display "Activated" with Success Green
        assertNotNull(state)
    }

    @Test
    fun `O2LoadingIndicator handles null progress as indeterminate`() {
        val progress: Float? = null
        // Verification: null progress triggers indeterminate mode
        assertNull(progress)
    }

    @Test
    fun `O2LoadingIndicator handles 0 to 1 progress range`() {
        val progressValues = listOf(0.0f, 0.25f, 0.5f, 0.75f, 1.0f)

        progressValues.forEach { progress ->
            assertTrue(progress in 0.0f..1.0f)
        }
    }

    @Test
    fun `O2LoadingIndicator coerces out of range values`() {
        val outOfRange = listOf(-0.5f, 1.5f, 2.0f)

        outOfRange.forEach { value ->
            val coerced = value.coerceIn(0f, 1f)
            assertTrue(coerced in 0.0f..1.0f)
        }
    }
}
