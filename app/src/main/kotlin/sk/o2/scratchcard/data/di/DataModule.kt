package sk.o2.scratchcard.data.di

import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.ktor.client.HttpClient
import io.ktor.client.engine.android.Android
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.serialization.kotlinx.json.json
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.serialization.json.Json
import sk.o2.scratchcard.data.remote.O2ApiService
import sk.o2.scratchcard.data.repository.ScratchCardRepositoryImpl
import sk.o2.scratchcard.domain.repository.ScratchCardRepository
import timber.log.Timber
import javax.inject.Qualifier
import javax.inject.Singleton

/**
 * Hilt module providing data layer dependencies.
 *
 * Provides:
 * - HttpClient configured with Ktor Android engine
 * - O2ApiService for API communication
 * - IO CoroutineDispatcher for background operations
 * - Repository implementation binding
 *
 * All dependencies are singletons to ensure consistent state management.
 */
@Module
@InstallIn(SingletonComponent::class)
object DataModule {

    /**
     * Provides configured Ktor HttpClient.
     *
     * Configuration:
     * - Engine: Android (uses Android's HttpURLConnection)
     * - ContentNegotiation: kotlinx.serialization with JSON
     * - HttpTimeout: 10 seconds for all timeout types
     * - Logging: Debug logs in debug builds only
     *
     * @return Configured HttpClient instance
     */
    @Provides
    @Singleton
    fun provideHttpClient(): HttpClient = HttpClient(Android) {
        // Content Negotiation with kotlinx.serialization
        install(ContentNegotiation) {
            json(Json {
                ignoreUnknownKeys = true
                isLenient = true
                prettyPrint = false
                encodeDefaults = true
            })
        }

        // Timeout configuration (NFR002: 10-second timeout)
        install(HttpTimeout) {
            requestTimeoutMillis = 10_000
            connectTimeoutMillis = 10_000
            socketTimeoutMillis = 10_000
        }

        // Logging (debug builds only)
        install(Logging) {
            logger = object : Logger {
                override fun log(message: String) {
                    Timber.d("Ktor: $message")
                }
            }
            level = LogLevel.INFO
        }
    }

    /**
     * Provides O2ApiService instance.
     *
     * @param httpClient Configured Ktor HttpClient
     * @return O2ApiService singleton
     */
    @Provides
    @Singleton
    fun provideO2ApiService(httpClient: HttpClient): O2ApiService =
        O2ApiService(httpClient)

    /**
     * Provides IO CoroutineDispatcher for background operations.
     *
     * Used by repository for scratch and activation operations to ensure
     * they don't block the main thread.
     *
     * @return Dispatchers.IO dispatcher
     */
    @Provides
    @IoDispatcher
    fun provideIoDispatcher(): CoroutineDispatcher = Dispatchers.IO
}

/**
 * Hilt module for binding interfaces to implementations.
 *
 * Separate module using @Binds for more efficient code generation.
 * @Binds is preferred over @Provides for simple interface-to-implementation bindings.
 */
@Module
@InstallIn(SingletonComponent::class)
interface DataBindingsModule {

    /**
     * Binds ScratchCardRepository interface to its implementation.
     *
     * This binding allows domain layer to depend on the interface while
     * the data layer provides the concrete implementation.
     *
     * @param impl Repository implementation
     * @return Repository interface
     */
    @Binds
    @Singleton
    fun bindScratchCardRepository(
        impl: ScratchCardRepositoryImpl
    ): ScratchCardRepository
}

/**
 * Qualifier annotation for IO Dispatcher.
 *
 * Distinguishes IO dispatcher from other dispatcher types (Main, Default).
 * Used for background data operations.
 */
@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class IoDispatcher
