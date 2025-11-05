# O2 Scratch Card - Android Application

A native Android application demonstrating Clean Architecture, MVVM pattern, and modern Android development best practices using Jetpack Compose.

## Project Overview

This app implements a scratch card feature with three states (Unscratched, Scratched, Activated) and integrates with the O2 API for validation. Built as a technical assessment for O2 Slovakia's Android development position.

### Key Features

- **Scratch Card Flow**: Interactive scratch operation with 2-second delay and UUID generation
- **API Integration**: Validates scratch codes with O2's version API endpoint
- **State Management**: Persistent state across navigation using StateFlow
- **O2 Design System**: Complete implementation of O2 brand identity
- **Clean Architecture**: Three-layer architecture (Domain/Data/Presentation)
- **Modern Stack**: Kotlin 2.1.0, Jetpack Compose, Hilt, Ktor Client

## Technology Stack

### Core Technologies

| Component | Technology | Version |
|-----------|-----------|---------|
| Language | Kotlin | 2.1.0 |
| UI Framework | Jetpack Compose | BOM 2025.10.01 |
| Dependency Injection | Hilt | 2.57.1 |
| HTTP Client | Ktor Client (Android engine) | 3.0.3 |
| Serialization | kotlinx.serialization | 1.8.0 |
| Async | Kotlin Coroutines | 1.10.2 |
| Navigation | Compose Navigation | 2.8.5 |

### Testing Stack

| Tool | Purpose | Version |
|------|---------|---------|
| JUnit 5 | Unit testing framework | 5.11.4 |
| MockK | Mocking library | 1.13.14 |
| Turbine | Flow testing | 1.2.0 |
| Kover | Code coverage | 0.9.0 |

**Coverage Target**: ≥50% enforced by Kover (focused on domain/data layers)

### Build Configuration

- **Minimum SDK**: API 24 (Android 7.0 Nougat) - 87% device coverage
- **Target SDK**: API 35 (Android 15)
- **Compile SDK**: API 35

## Architecture

### Clean Architecture Layers

```
app/src/main/kotlin/sk/o2/scratchcard/
├── domain/          # Pure Kotlin business logic (zero Android dependencies)
│   ├── model/       # Data models and sealed classes
│   ├── repository/  # Repository interfaces
│   └── usecase/     # Business use cases
├── data/            # Data layer implementation
│   ├── repository/  # Repository implementations
│   ├── remote/      # API services
│   └── local/       # Local data sources
└── presentation/    # UI layer with Jetpack Compose
    ├── screens/     # Screen composables
    ├── components/  # Reusable UI components
    ├── theme/       # O2 design system
    └── navigation/  # Navigation setup
```

### Design Patterns

- **MVVM**: ViewModels with StateFlow for reactive UI
- **Repository Pattern**: Abstract data sources
- **Use Case Pattern**: Single-responsibility business operations
- **Dependency Inversion**: Interfaces in domain, implementations in data

## Prerequisites

### Required

- **Android Studio**: Ladybug 2024.2.1 or later
- **JDK**: Java 17 or later
- **Kotlin**: 2.1.0 (via Gradle plugin)
- **Gradle**: 8.7+ (via wrapper)

### Recommended

- **Android Emulator**: API 24+ for testing
- **Physical Device**: For performance and UX testing

## Build Instructions

### Clone Repository

```bash
git clone https://github.com/hanibalsk/scratch-ticket.git
cd scratch-ticket
```

### Build Project

```bash
# Clean build
./gradlew clean build

# Build debug APK
./gradlew assembleDebug

# Build release APK
./gradlew assembleRelease
```

### Install on Device/Emulator

```bash
# Install debug build
./gradlew installDebug

# Install and run
./gradlew installDebug && adb shell am start -n sk.o2.scratchcard/.presentation.MainActivity
```

## Running Tests

### Unit Tests

```bash
# Run all unit tests
./gradlew test

# Run unit tests with coverage
./gradlew koverHtmlReport

# Open coverage report
open app/build/reports/kover/html/index.html
```

### Android Instrumented Tests

```bash
# Run on connected device/emulator
./gradlew connectedAndroidTest
```

### Coverage Requirements

- **Overall Coverage**: ≥50% (enforced by Kover, focused on business logic)
- **Unit Tests**: Target ≥90% for domain and data layers
- **UI Tests**: Integration and E2E tests for critical flows
- **Note**: UI components excluded from coverage as they require instrumented tests

## Code Quality

### Linting

```bash
# Run Kotlin lint checks
./gradlew lint

# View lint report
open app/build/reports/lint-results.html
```

### Code Formatting

- **Style**: Kotlin official coding conventions
- **Line Length**: 120 characters
- **Indentation**: 4 spaces

## Project Structure

```
scratch-ticket/
├── app/
│   ├── src/
│   │   ├── main/
│   │   │   ├── kotlin/sk/o2/scratchcard/  # Source code
│   │   │   ├── res/                        # Android resources
│   │   │   └── AndroidManifest.xml
│   │   ├── test/                           # Unit tests
│   │   └── androidTest/                    # Instrumented tests
│   ├── build.gradle.kts
│   └── proguard-rules.pro
├── gradle/
│   └── libs.versions.toml    # Version catalog
├── build.gradle.kts           # Root build config
├── settings.gradle.kts        # Project settings
└── README.md
```

## Development Workflow

### Story-Driven Development

This project follows a structured story-driven workflow:

1. **Epic 1**: Core Scratch Card Flow (9 stories)
2. **Epic 2**: O2 Design System & QA (7 stories)

### Implementation Order

1. Project Infrastructure (Story 1.0) ✓
2. Domain Layer (Story 1.3a)
3. Data Layer (Story 1.3b)
4. O2 Theme (Story 2.1)
5. Navigation (Story 1.2)
6. Feature Screens (Stories 1.1, 1.4, 2.2)
7. API Integration (Stories 1.5, 1.6, 1.7)
8. Testing & Quality (Stories 2.3-2.8)

## API Integration

### O2 Version API

- **Endpoint**: `GET https://api.o2.sk/version`
- **Query Parameter**: `code` (UUID from scratch operation)
- **Response**: `{ "android": <version_number> }`
- **Validation**: Success if `android > ANDROID_VERSION_THRESHOLD` (277028)
- **Constant**: `O2ApiService.ANDROID_VERSION_THRESHOLD`

### Error Handling

- **Network Errors**: Timeout, no connection
- **HTTP Errors**: 4xx, 5xx status codes
- **Parsing Errors**: Invalid JSON structure
- **Validation Errors**: android ≤ ANDROID_VERSION_THRESHOLD

## Contributing

### Code Style

- Follow Kotlin coding conventions
- Use meaningful variable and function names
- Document public APIs with KDoc
- Write tests for all business logic

### Pull Request Process

1. Create feature branch from `main`
2. Implement story with tests
3. Ensure all tests pass
4. Verify ≥50% code coverage (Kover check)
5. Create PR with story reference

## License

This project is created as a technical assessment for O2 Slovakia.

## Contact

For questions or issues, please contact the development team.

---

**Built with ❤️ for O2 Slovakia**
