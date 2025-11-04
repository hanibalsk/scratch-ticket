package sk.o2.scratchcard

import android.app.Application
import android.content.Context
import androidx.test.runner.AndroidJUnitRunner
import dagger.hilt.android.testing.HiltTestApplication

/**
 * Custom test runner for Hilt instrumented tests.
 *
 * This runner replaces the standard application with HiltTestApplication,
 * which is required for @HiltAndroidTest annotated tests to work correctly.
 *
 * Usage in build.gradle.kts:
 * ```
 * android {
 *     defaultConfig {
 *         testInstrumentationRunner = "sk.o2.scratchcard.HiltTestRunner"
 *     }
 * }
 * ```
 */
class HiltTestRunner : AndroidJUnitRunner() {
    override fun newApplication(
        cl: ClassLoader?,
        className: String?,
        context: Context?,
    ): Application = super.newApplication(cl, HiltTestApplication::class.java.name, context)
}
