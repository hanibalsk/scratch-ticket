# GitHub Actions Workflows

This document describes the CI/CD workflows configured for the O2 Scratch Card project.

## Overview

All workflows use the **latest stable versions** of GitHub Actions (as of 2025):
- `actions/checkout@v5` - Code checkout
- `actions/setup-java@v5` - Java/JDK setup
- `gradle/actions/setup-gradle@v5` - Gradle build caching
- `actions/upload-artifact@v4` - Artifact uploads
- `github/codeql-action@v3` - Security scanning

**Cost**: All workflows are **100% FREE** for public repositories.

---

## Workflows

### 1. Android CI (`android-ci.yml`)

**Triggers**: Push to `main`/`develop`, Pull Requests

**Purpose**: Main build, test, and coverage pipeline

**Steps**:
1. ✅ Checkout code
2. ☕ Setup JDK 17 (Temurin distribution)
3. 🐘 Setup Gradle with caching
4. 🎨 Run ktlint code formatting checks
5. 🔨 Build debug APK
6. 🧪 Run unit tests (197 tests)
7. 📊 Generate Kover coverage reports (XML + HTML)
8. ✔️ Verify ≥50% coverage threshold
9. 📤 Upload test results, coverage reports, and APK as artifacts

**Timeout**: 30 minutes

**Artifacts**:
- `test-results` - JUnit test reports
- `coverage-reports` - Kover HTML/XML reports
- `app-debug` - Debug APK

**Gradle Cache**: Read-only for non-main branches (faster PR builds)

---

### 2. Dependency Review (`dependency-review.yml`)

**Triggers**: Pull Requests to `main`

**Purpose**: Security scanning for vulnerable dependencies

**Steps**:
1. ✅ Checkout code
2. 🔍 Scan dependencies for known vulnerabilities
3. 💬 Comment PR with findings
4. ❌ Fail build if moderate+ severity vulnerabilities found

**Free for**: All public repositories

**Reports**: Inline PR comments with vulnerability details

---

### 3. CodeQL Analysis (`codeql.yml`)

**Triggers**:
- Push to `main`
- Pull Requests to `main`
- Weekly schedule (Monday 00:00 UTC)

**Purpose**: Static security and quality analysis

**Steps**:
1. ✅ Checkout code
2. 🔧 Initialize CodeQL with `security-and-quality` queries
3. ☕ Setup JDK 17
4. 🔨 Build project
5. 🔍 Perform analysis for Java/Kotlin

**Languages**: `java-kotlin`

**Queries**: Security vulnerabilities + code quality issues

**Results**: Visible in GitHub Security tab

**Free for**: All public repositories

---

### 4. PR Labeler (`pr-labeler.yml`)

**Triggers**: PR opened or updated

**Purpose**: Auto-label PRs based on changed files

**Configuration**: `.github/labeler.yml`

**Labels**:
- `domain` - Domain layer changes
- `data` - Data layer changes
- `presentation` - UI/Presentation changes
- `tests` - Test file changes
- `documentation` - Markdown file changes
- `dependencies` - Gradle dependency changes
- `ci` - Workflow changes

**Benefits**: Better PR organization and filtering

---

## Workflow Features

### Gradle Build Cache

- **Enabled**: Automatic caching via `gradle/actions/setup-gradle@v5`
- **Strategy**: Read-only for non-main branches (prevents cache pollution)
- **Savings**: ~50% faster repeat builds

### Artifact Retention

- **Default**: 90 days for test results and coverage reports
- **Purpose**: Historical analysis and debugging

### Permissions

All workflows use **minimal permissions** following the principle of least privilege:
- `contents: read` - Read repository code
- `pull-requests: write` - Comment on PRs (where needed)
- `security-events: write` - Upload CodeQL results (CodeQL only)

---

## CI/CD Status Badges

Add these badges to your README:

```markdown
![Android CI](https://github.com/hanibalsk/scratch-ticket/workflows/Android%20CI/badge.svg)
![CodeQL](https://github.com/hanibalsk/scratch-ticket/workflows/CodeQL%20Analysis/badge.svg)
```

---

## Local Testing

Test workflows locally before pushing:

```bash
# Install act (GitHub Actions local runner)
brew install act  # macOS
# or: https://github.com/nektos/act

# Run Android CI workflow locally
act -j build-and-test
```

---

## Adding New Workflows

1. Create `.github/workflows/your-workflow.yml`
2. Use latest action versions from [GitHub Marketplace](https://github.com/marketplace?type=actions)
3. Test locally with `act` (optional)
4. Commit and push to trigger

---

## Resources

- [GitHub Actions Documentation](https://docs.github.com/en/actions)
- [Gradle Actions](https://github.com/gradle/actions)
- [Android CI Best Practices](https://developer.android.com/studio/projects/continuous-integration)
- [Free CI/CD Minutes](https://docs.github.com/en/billing/managing-billing-for-github-actions/about-billing-for-github-actions#included-storage-and-minutes)

---

**Built with ❤️ for O2 Slovakia**
