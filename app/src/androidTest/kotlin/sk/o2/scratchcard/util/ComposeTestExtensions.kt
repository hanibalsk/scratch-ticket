package sk.o2.scratchcard.util

import androidx.compose.ui.test.SemanticsNodeInteraction
import androidx.compose.ui.test.junit4.ComposeContentTestRule
import androidx.test.espresso.Espresso

/**
 * Extension function to simulate back button press in Compose UI tests.
 *
 * This function performs a back navigation by clicking the system back button.
 * It's commonly used in navigation testing scenarios.
 *
 * Usage:
 * ```kotlin
 * composeTestRule.onRoot().performBack()
 * ```
 */
fun SemanticsNodeInteraction.performBack(): SemanticsNodeInteraction {
    // Use Espresso's pressBack() to simulate back button
    Espresso.pressBack()
    return this
}

/**
 * Alternative: Wait for idle and perform back navigation.
 *
 * Usage:
 * ```kotlin
 * composeTestRule.performBack()
 * ```
 */
fun ComposeContentTestRule.performBack() {
    waitForIdle()
    Espresso.pressBack()
    waitForIdle()
}
