package sk.o2.scratchcard.util

import androidx.lifecycle.ViewModel

/**
 * Reflection-based extension to call protected onCleared() method in tests.
 *
 * This allows us to test ViewModel cleanup behavior without making onCleared() public.
 *
 * Usage:
 * ```kotlin
 * viewModel.callOnCleared()
 * ```
 *
 * @throws IllegalStateException if onCleared() method cannot be found or invoked
 */
fun ViewModel.callOnCleared() {
    try {
        val onClearedMethod = ViewModel::class.java.getDeclaredMethod("onCleared")
        onClearedMethod.isAccessible = true
        onClearedMethod.invoke(this)
    } catch (e: Exception) {
        throw IllegalStateException("Failed to invoke onCleared() via reflection", e)
    }
}
