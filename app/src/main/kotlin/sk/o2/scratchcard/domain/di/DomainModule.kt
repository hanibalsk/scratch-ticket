package sk.o2.scratchcard.domain.di

import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

/**
 * Hilt module for domain layer dependencies.
 *
 * This module provides bindings for domain layer components. Currently,
 * use cases use constructor injection (@Inject) so no explicit @Provides
 * methods are needed.
 *
 * The module is installed in SingletonComponent to ensure domain layer
 * components have app-wide lifecycle.
 *
 * **Note:** As the domain layer follows Clean Architecture principles,
 * this module has minimal Android/Hilt dependencies. Use cases themselves
 * are pure Kotlin with only @Inject from javax.inject (not Android-specific).
 *
 * Future additions to this module might include:
 * - Custom domain validators
 * - Complex domain services
 * - Domain event buses
 */
@Module
@InstallIn(SingletonComponent::class)
object DomainModule {
    // Use cases use constructor injection (@Inject) - no @Provides needed
    // Add @Provides methods here if complex domain dependencies arise
}
