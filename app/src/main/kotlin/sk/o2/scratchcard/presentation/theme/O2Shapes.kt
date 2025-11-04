package sk.o2.scratchcard.presentation.theme

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Shapes
import androidx.compose.ui.unit.dp

/**
 * O2 Shape System.
 *
 * Defines corner radii for different component types following O2 design guidelines:
 * - Small (12dp): Buttons, chips, badges
 * - Medium (16dp): Cards, containers
 * - Large (24dp): Dialogs, bottom sheets, modals
 *
 * All shapes use RoundedCornerShape for consistent rounded corners.
 *
 * Reference: docs/o2-design-system.md#containers
 */
val O2Shapes =
    Shapes(
        /**
         * Extra Small - 8dp radius.
         * Use for: Small chips, tight corners.
         */
        extraSmall = RoundedCornerShape(8.dp),
        /**
         * Small - 12dp radius.
         * Use for: Buttons, chips, badges, input fields.
         */
        small = RoundedCornerShape(12.dp),
        /**
         * Medium - 16dp radius.
         * Use for: Cards, containers, elevated surfaces.
         */
        medium = RoundedCornerShape(16.dp),
        /**
         * Large - 24dp radius.
         * Use for: Dialogs, bottom sheets, modals, large cards.
         */
        large = RoundedCornerShape(24.dp),
        /**
         * Extra Large - 28dp radius.
         * Use for: Hero cards, large modals.
         */
        extraLarge = RoundedCornerShape(28.dp),
    )
