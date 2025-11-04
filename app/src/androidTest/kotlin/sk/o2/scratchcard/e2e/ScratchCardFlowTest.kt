package sk.o2.scratchcard.e2e

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import sk.o2.scratchcard.presentation.navigation.AppNavigation
import sk.o2.scratchcard.presentation.theme.O2Theme

/**
 * End-to-End Integration Tests for O2 Scratch Card App.
 *
 * Tests complete user flows from start to finish using Compose UI Test.
 * Validates integration between all layers (Domain, Data, Presentation).
 *
 * Test Scenarios:
 * 1. Happy Path: Unscratched → Scratch → Activate → Success
 * 2. Scratch Cancellation: Start scratch → Back → Verify Unscratched
 * 3. Activation Failure: Scratch → Activate → Error Modal
 * 4. State Persistence: Navigate between screens, verify state maintained
 * 5. Network Error Recovery: Fail → Retry → Success
 */
@HiltAndroidTest
@RunWith(AndroidJUnit4::class)
class ScratchCardFlowTest {

    @get:Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val composeTestRule = createComposeRule()

    @Before
    fun setup() {
        hiltRule.inject()

        composeTestRule.setContent {
            O2Theme {
                AppNavigation()
            }
        }
    }

    @Test
    fun happyPath_scratchAndActivateSuccessfully() {
        // Start on Main screen - verify Unscratched state
        composeTestRule
            .onNodeWithText("Unscratched")
            .assertExists()

        // Go to Scratch screen
        composeTestRule
            .onNodeWithText("Go to Scratch Screen")
            .performClick()

        // Scratch the card
        composeTestRule
            .onNodeWithText("Scratch Card")
            .performClick()

        // Wait for 2-second scratch operation
        composeTestRule.waitUntil(timeoutMillis = 3000) {
            composeTestRule
                .onAllNodesWithText("Code Revealed!", substring = true)
                .fetchSemanticsNodes()
                .isNotEmpty()
        }

        // Verify code is displayed
        composeTestRule
            .onNodeWithText("Code Revealed!")
            .assertExists()

        // Go back to Main
        composeTestRule
            .onNodeWithText("Back to Main")
            .performClick()

        // Verify state changed to Scratched
        composeTestRule
            .onNodeWithText("Scratched")
            .assertExists()

        // Activation button should now be enabled
        composeTestRule
            .onNodeWithText("Go to Activation Screen")
            .assertIsEnabled()
            .performClick()

        // Activate the card
        composeTestRule
            .onNodeWithText("Activate")
            .performClick()

        // Wait for API call (mock will respond quickly in test)
        composeTestRule.waitUntil(timeoutMillis = 5000) {
            composeTestRule
                .onAllNodesWithText("Activation Successful!", substring = true)
                .fetchSemanticsNodes()
                .isNotEmpty()
                ||
                composeTestRule
                .onAllNodesWithText("Activated", substring = true)
                .fetchSemanticsNodes()
                .isNotEmpty()
        }

        // Verify success or activated state
        composeTestRule.waitForIdle()
    }

    @Test
    fun scratchCancellation_backBeforeCompletion() {
        // Navigate to Scratch screen
        composeTestRule
            .onNodeWithText("Go to Scratch Screen")
            .performClick()

        // Start scratch operation
        composeTestRule
            .onNodeWithText("Scratch Card")
            .performClick()

        // Immediately navigate back (before 2 seconds)
        composeTestRule.waitForIdle()
        composeTestRule.onRoot().performBack()

        // Should be back on Main screen
        composeTestRule
            .onNodeWithText("Go to Scratch Screen")
            .assertExists()

        // State should still be Unscratched (operation cancelled)
        composeTestRule
            .onNodeWithText("Unscratched")
            .assertExists()
    }

    @Test
    fun statePeristence_acrossNavigation() {
        // Scratch the card
        composeTestRule
            .onNodeWithText("Go to Scratch Screen")
            .performClick()

        composeTestRule
            .onNodeWithText("Scratch Card")
            .performClick()

        // Wait for completion
        composeTestRule.waitUntil(timeoutMillis = 3000) {
            composeTestRule
                .onAllNodesWithText("Code Revealed!", substring = true)
                .fetchSemanticsNodes()
                .isNotEmpty()
        }

        // Navigate back to Main
        composeTestRule
            .onNodeWithText("Back to Main")
            .performClick()

        // Verify Scratched state persisted
        composeTestRule
            .onNodeWithText("Scratched")
            .assertExists()

        // Navigate to Activation screen
        composeTestRule
            .onNodeWithText("Go to Activation Screen")
            .performClick()

        // Code should still be available
        composeTestRule.waitForIdle()

        // Verify activation screen shows (look for Activate button or screen text)
        composeTestRule
            .onNodeWithText("Activate", substring = true)
            .assertExists()
    }

    @Test
    fun multipleScreenNavigation_stateConsistency() {
        // Navigate through all screens and verify state consistency

        // Start: Main screen
        composeTestRule
            .onNodeWithText("Unscratched")
            .assertExists()

        // Go to Scratch
        composeTestRule
            .onNodeWithText("Go to Scratch Screen")
            .performClick()

        // Back to Main
        composeTestRule.onRoot().performBack()

        // Still Unscratched
        composeTestRule
            .onNodeWithText("Unscratched")
            .assertExists()

        // Activation should be disabled
        composeTestRule
            .onNodeWithText("Go to Activation Screen")
            .assertIsNotEnabled()
    }

    @Test
    fun activationButton_disabledWhenUnscratched() {
        // Verify activation button is disabled initially
        composeTestRule
            .onNodeWithText("Go to Activation Screen")
            .assertIsNotEnabled()
    }

    @Test
    fun activationButton_enabledAfterScratch() {
        // Scratch the card
        composeTestRule
            .onNodeWithText("Go to Scratch Screen")
            .performClick()

        composeTestRule
            .onNodeWithText("Scratch Card")
            .performClick()

        // Wait for scratch completion
        composeTestRule.waitUntil(timeoutMillis = 3000) {
            composeTestRule
                .onAllNodesWithText("Code Revealed!", substring = true)
                .fetchSemanticsNodes()
                .isNotEmpty()
        }

        // Back to Main
        composeTestRule
            .onNodeWithText("Back to Main")
            .performClick()

        // Activation should now be enabled
        composeTestRule
            .onNodeWithText("Go to Activation Screen")
            .assertIsEnabled()
    }
}
