package sk.o2.scratchcard

import android.app.Application
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber

/**
 * Application class for O2 Scratch Card app
 * Annotated with @HiltAndroidApp to trigger Hilt code generation
 */
@HiltAndroidApp
class ScratchCardApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        // Initialize Timber for logging in debug builds only
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
            Timber.d("ScratchCardApplication initialized - Debug mode")
        }
    }
}
