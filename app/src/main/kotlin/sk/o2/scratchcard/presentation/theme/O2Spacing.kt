package sk.o2.scratchcard.presentation.theme

import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * O2 Spacing System.
 *
 * Based on 8dp base grid with 4dp sub-grid for fine alignment.
 * Provides consistent spacing values throughout the app.
 *
 * Usage:
 * ```kotlin
 * Column(modifier = Modifier.padding(O2Spacing.md)) {
 *     Text("Title")
 *     Spacer(modifier = Modifier.height(O2Spacing.sm))
 *     Text("Body")
 * }
 * ```
 *
 * Reference: docs/o2-design-system.md#spacing-scale
 */
object O2Spacing {
    /**
     * Extra Small - 4dp.
     * Use for: Tight spacing within components, fine alignment.
     */
    val xs: Dp = 4.dp

    /**
     * Small - 8dp (base grid).
     * Use for: Base spacing between elements, component padding.
     */
    val sm: Dp = 8.dp

    /**
     * Medium - 16dp.
     * Use for: Section spacing, card padding, standard margins.
     */
    val md: Dp = 16.dp

    /**
     * Large - 24dp.
     * Use for: Large section spacing, screen padding top/bottom.
     */
    val lg: Dp = 24.dp

    /**
     * Extra Large - 32dp.
     * Use for: Screen margins, major section dividers.
     */
    val xl: Dp = 32.dp

    /**
     * XXL - 40dp.
     * Use for: Major spacing, hero sections.
     */
    val xxl: Dp = 40.dp

    /**
     * XXXL - 48dp.
     * Use for: Extra large spacing, splash screens.
     */
    val xxxl: Dp = 48.dp
}
