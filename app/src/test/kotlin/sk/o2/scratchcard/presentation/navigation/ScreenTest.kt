package sk.o2.scratchcard.presentation.navigation

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

/**
 * Unit tests for Screen sealed interface.
 *
 * Validates:
 * - Route strings are unique
 * - Route strings are non-empty
 * - Sealed interface provides type safety
 * - All navigation destinations are defined
 */
class ScreenTest {
    @Test
    fun `Main screen route is main`() {
        assertEquals("main", Screen.Main.route)
    }

    @Test
    fun `Scratch screen route is scratch`() {
        assertEquals("scratch", Screen.Scratch.route)
    }

    @Test
    fun `Activation screen route is activation`() {
        assertEquals("activation", Screen.Activation.route)
    }

    @Test
    fun `all routes are unique`() {
        val routes =
            listOf(
                Screen.Main.route,
                Screen.Scratch.route,
                Screen.Activation.route,
            )

        // Check for duplicate routes
        val uniqueRoutes = routes.toSet()
        assertEquals(routes.size, uniqueRoutes.size, "Routes must be unique")
    }

    @Test
    fun `all routes are non-empty`() {
        val screens =
            listOf(
                Screen.Main,
                Screen.Scratch,
                Screen.Activation,
            )

        screens.forEach { screen ->
            assertTrue(screen.route.isNotEmpty(), "${screen::class.simpleName} route must not be empty")
        }
    }

    @Test
    fun `Main is singleton object`() {
        val screen1 = Screen.Main
        val screen2 = Screen.Main

        assertSame(screen1, screen2)
    }

    @Test
    fun `Scratch is singleton object`() {
        val screen1 = Screen.Scratch
        val screen2 = Screen.Scratch

        assertSame(screen1, screen2)
    }

    @Test
    fun `Activation is singleton object`() {
        val screen1 = Screen.Activation
        val screen2 = Screen.Activation

        assertSame(screen1, screen2)
    }

    @Test
    fun `sealed interface provides exhaustive when expressions`() {
        val screens: List<Screen> =
            listOf(
                Screen.Main,
                Screen.Scratch,
                Screen.Activation,
            )

        screens.forEach { screen ->
            // When expression must be exhaustive for sealed interface
            val description =
                when (screen) {
                    Screen.Main -> "Main screen"
                    Screen.Scratch -> "Scratch screen"
                    Screen.Activation -> "Activation screen"
                }

            assertNotNull(description)
        }
    }

    @Test
    fun `route strings follow lowercase naming convention`() {
        val screens = listOf(Screen.Main, Screen.Scratch, Screen.Activation)

        screens.forEach { screen ->
            assertEquals(
                screen.route.lowercase(),
                screen.route,
                "${screen::class.simpleName} route should be lowercase",
            )
        }
    }

    @Test
    fun `route strings do not contain special characters`() {
        val screens = listOf(Screen.Main, Screen.Scratch, Screen.Activation)
        val validPattern = Regex("^[a-z]+$")

        screens.forEach { screen ->
            assertTrue(
                validPattern.matches(screen.route),
                "${screen::class.simpleName} route should only contain lowercase letters",
            )
        }
    }
}
