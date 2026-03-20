# AGENTS.md - Agent Coding Guidelines for Amity-Social-Cloud-UIKit-Android

## Table of Contents

| # | Section | What it covers |
|---|---------|----------------|
| 1 | [Build Commands](#build-commands) | Gradle tasks for building, testing, linting, and per-module builds |
| 2 | [Code Style Guidelines](#code-style-guidelines) | Package/class/variable naming, formatting, imports, null safety, Kotlin idioms |
| 3 | [Agent / Automation Rules](#agent--automation-rules) | Language, commit message format, class reference conventions |
| 4 | [Compose Guidelines](#compose-guidelines) | Component naming, state management (uiState pattern), theming |
| 5 | [Testing Guidelines](#testing-guidelines) | JUnit 4 + MockK, AAA pattern, test location |
| 6 | [Architecture](#architecture) | Module structure, MVVM pattern, snackbar system, cross-page communication |
| 7 | [Dependencies](#dependencies) | Key libraries and build configuration (SDK versions, Gradle, AGP) |
| 8 | [Agent Skills](#agent-skills) | How to create, organize, and reference `.agent/skills/` knowledge docs |

---

## Build Commands

### Full Project Build
```bash
./gradlew assembleDebug          # Build debug APK
./gradlew assembleRelease       # Build release APK
./gradlew build                 # Full build with tests
```

### Running Tests

#### Run All Unit Tests
```bash
./gradlew test                  # Run all unit tests
./gradlew testDebugUnitTest     # Run debug unit tests
```

#### Run Single Test Class
```bash
./gradlew test --tests "com.amity.socialcloud.uikit.logging.AmityLogHelperTest"
```

#### Run Single Test Method
```bash
./gradlew test --tests "com.amity.socialcloud.uikit.logging.AmityLogHelperTest.test_logs"
```

#### Run Tests for Specific Module
```bash
./gradlew :logging:test         # Test only logging module
./gradlew :social:test          # Test only social module
./gradlew :chat:test            # Test only chat module
```

### Linting
```bash
./gradlew lint                  # Run lint on all modules
./gradlew lintDebug            # Run lint on debug builds
./gradlew :module:lint         # Lint specific module
```

### Clean & Rebuild
```bash
./gradlew clean                # Clean build directories
./gradlew clean build          # Clean and rebuild
```

### Module-Specific Builds
```bash
./gradlew :amity-uikit:assembleDebug    # Build UIKit module
./gradlew :social:assembleDebug          # Build social module
./gradlew :chat:assembleDebug            # Build chat module
./gradlew :social-compose:assembleDebug  # Build social-compose module
./gradlew :chat-compose:assembleDebug    # Build chat-compose module
```

---

## Code Style Guidelines

### Package Naming
- Base package: `com.amity.socialcloud.uikit`
- Modules: `com.amity.socialcloud.uikit.<module>.<feature>.<subfeature>`
- Example: `com.amity.socialcloud.uikit.community.newsfeed.fragment`

---

### Agent / Automation Rules

- When referring to a class in code comments, logs, or agent output, mention only the class name (for example `AmityBaseFragment`). Do not include the package in the class mention; put the package name in the import statement instead (for example `import com.amity.socialcloud.uikit.common.base.AmityBaseFragment`).
- All comments, commit messages, notes added by automation or agents must be in English. If the original prompt or content is in another language, translate it to English before inserting it into code, comments, or commit messages.
- Follow this repository's commit message pattern when creating commit notes or descriptions: use conventional commit prefixes (for example `feat:`, `fix:`, `chore:`, `refactor:`, `docs:`, `test:`) and write a short imperative description. Keep the commit summary concise and add a short body if more context is needed. Always write commit messages in English.

Examples and recommendations based on existing repository history:
- Prefix: `fix:`, `feat:`, `chore:`, `refactor:`, `docs:`, `test:` (use the one that best fits the change).
- Format: `type: short imperative description`  — e.g. `fix: Prevent racing condition on cursor position`
- Summary length: aim for a short summary (<= 72 characters preferred); repository uses concise single-line summaries.
- Body: if needed, add 1–2 sentences explaining why or what changed, wrapped at ~72 characters per line.
- Reference: include PR or issue number only if available (for example `Refs #123`).

Example commit messages observed in this repo:
- `fix: Fix fail to remove video from post`
- `feat: Product carousel`
- `chore: Update AGENTS.md`


### Class Naming Conventions
| Type | Convention | Example |
|------|------------|---------|
| Activities | `Amity<Feature>Activity` | `AmityEditMessageActivity` |
| Fragments | `Amity<Feature>Fragment` | `AmityNewsFeedFragment` |
| ViewModels | `Amity<Feature>ViewModel` | `AmityRecentChatViewModel` |
| Adapters | `Amity<Feature>Adapter` | `AmityRecentChatAdapter` |
| ViewHolders | `Amity<Feature>ViewHolder` | `AmityTextMsgSenderViewHolder` |
| Components (Compose) | `Amity<Feature>Component` | `AmityStoryGlobalTabComponent` |
| Behaviors | `Amity<Feature>Behavior` | `AmityCreateStoryPageBehavior` |
| Models/Data | `Amity<Feature>Item` | `AmityBasePostItem` |
| Events | `<Name>Event` | `AmityFeedRefreshEvent` |
| Sealed Classes | `Amity<Name>State` | `AmityFeedLoadState` |

### Variable & Function Naming
- Use **camelCase** for variables and function names
- Use **PascalCase** for class names
- Use **UPPER_SNAKE_CASE** for constants
- Prefix booleans with `is`, `has`, `should`, `can`
- Use descriptive names; avoid single letters except in lambdas

```kotlin
// Good
private var isLoading = false
private val userList = mutableListOf<User>()
fun loadUserFeed() { }

// Avoid
private var loading = false
private var list = mutableListOf<User>()
fun load() { }
```

### Import Organization
Imports should be organized in the following order with blank lines between groups:
1. Android/Kotlin imports (`android.*`, `kotlin.*`)
2. Third-party SDK imports (`com.amity.*`, `io.reactivex.*`)
3. Internal project imports (`com.amity.socialcloud.uikit.*`)
4. Static imports

```kotlin
package com.amity.socialcloud.uikit.community.newsfeed.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.fragment.app.Fragment
import com.amity.socialcloud.uikit.common.base.AmityBaseFragment
import com.amity.socialcloud.uikit.community.R
```

### Code Formatting

#### Spacing
- Use 4 spaces for indentation (no tabs)
- Add space after commas and colons in types
- Use spaces around operators (`=`, `+`, `==`, etc.)

```kotlin
// Good
fun onCreate(savedInstanceState: Bundle?) {
    val list = listOf(1, 2, 3)
    val sum = list.reduce { acc, i -> acc + i }
}

// Bad
fun onCreate(savedInstanceState:Bundle?){
    val list=listOf(1,2,3)
    val sum=list.reduce{acc,i->acc+i}
}
```

#### Line Length
- Maximum line length: **120 characters**
- Break long lines at logical points (operators, commas)

#### Braces
- Use braces even for single-line statements in control flows
- Place opening brace on same line

```kotlin
// Good
if (isLoading) {
    showProgress()
}

// Avoid
if (isLoading)
    showProgress()
```

### Type System

#### Null Safety
- Use nullable types (`?`) when value can be null
- Use safe call operator (`?.`) and Elvis operator (`?:`)
- Prefer `lateinit` over nullable types for properties initialized in lifecycle methods

```kotlin
// Good
private lateinit var binding: AmityFragmentNewsFeedBinding
var userName: String? = null

// Avoid
var userName: String = "" // when it can be null
```

#### Type Annotations
- Explicitly type when inference is unclear
- Use val over var whenever possible

### Kotlin-Specific Guidelines

#### Extension Functions
- Use extension functions for reusable functionality
- Group extensions in dedicated files (e.g., `extensions/ViewExtensions.kt`)

```kotlin
fun View.setSafeOnClickListener(action: (View) -> Unit) {
    setOnClickListener { view ->
        if (isClickEnabled) {
            action(view)
        }
    }
}
```

#### Coroutines
- Use `viewModelScope` for ViewModel coroutines
- Use `lifecycleScope` for Activity/Fragment coroutines
- Handle exceptions with `try-catch` or CoroutineExceptionHandler

#### RxJava
- Always dispose subscriptions in `onCleared()` or use `addDisposable()`
- Use appropriate schedulers (subscribeOn/observeOn)

### Error Handling

#### Exceptions
- Catch specific exceptions, avoid bare `catch (e: Exception)`
- Use custom exception classes for domain-specific errors
- Log exceptions before rethrowing or handling

```kotlin
try {
    userRepository.getUser(userId)
} catch (e: UserNotFoundException) {
    showErrorMessage("User not found")
    Log.e(TAG, "User lookup failed", e)
} catch (e: NetworkException) {
    handleNetworkError(e)
}
```

#### Result Types
- Prefer returning `Result<T>` or sealed classes over throwing exceptions for expected errors
- Use sealed classes for operation outcomes

```kotlin
sealed class FeedLoadResult {
    data class Success(val posts: List<Post>) : FeedLoadResult()
    data class Error(val exception: Throwable) : FeedLoadResult()
    object Loading : FeedLoadResult()
}
```

### Compose Guidelines

#### Component Naming
- Use suffix `Component` for larger composable sections
- Use suffix `Element` for small, reusable UI pieces
- Use suffix `Row`, `Column`, `Box` for layout components

#### State Management
- Use `remember` and `rememberSaveable` for UI state
- Use `mutableStateOf` for observable state
- Extract state hoisting to parent composables
- **Always use uiState from ViewModel to manage UI state** - Do not use local mutableStateOf in composables for state that should be managed by the ViewModel

```kotlin
// Good - Use uiState from ViewModel
@Composable
fun AmityProductTagSelectionComponent(
    viewModel: AmityProductTagSelectionViewModel,
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsState()

    // Use uiState properties
    if (uiState.isLoading) {
        AmityLoadingCircle()
    }

    LazyColumn {
        items(uiState.products) { product ->
            ProductItem(product = product)
        }
    }
}

// Avoid - Using local mutableState in composable
@Composable
fun AmityProductTagSelectionComponent(
    viewModel: AmityProductTagSelectionViewModel,
    modifier: Modifier = Modifier
) {
    var isLoading by remember { mutableStateOf(false) }  // Don't do this
    var products by remember { mutableStateOf(listOf<Product>()) }  // Don't do this
}
```

**ViewModel uiState pattern:**
```kotlin
class AmityProductTagSelectionViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(AmityProductTagSelectionUiState())
    val uiState: StateFlow<AmityProductTagSelectionUiState> = _uiState.asStateFlow()

    fun loadProducts() {
        _uiState.update { it.copy(isLoading = true) }
        // Load products and update uiState
    }
}

data class AmityProductTagSelectionUiState(
    val isLoading: Boolean = false,
    val products: List<Product> = emptyList(),
    val error: String? = null
)
```

#### Theme
- Use theme colors from `MaterialTheme.colors`
- Use `MaterialTheme.typography` for text styles
- Support both light/dark themes

### Testing Guidelines

#### Unit Tests
- Use JUnit 4 with MockK for mocking
- Follow AAA pattern: Arrange, Act, Assert
- Name test methods descriptively: `test<ExpectedBehavior>`

```kotlin
@Test
fun test_logs_debug_message() {
    // Arrange
    mockkStatic(Timber::class)
    every { Timber.d(any<String>()) } answers { answer = "DEBUG" }

    // Act
    LogHelper.d("Debug message")

    // Assert
    assertTrue(answer == "DEBUG")
}
```

#### Test Location
- Unit tests: `src/test/java/`
- Android tests: `src/androidTest/java/`

---

## Architecture

### Module Structure
- **common** - Shared utilities, base classes
- **common-compose** - Shared Compose components
- **social** - Social feed (XML-based)
- **social-compose** - Social feed (Compose-based)
- **chat** - Chat features (XML-based)
- **chat-compose** - Chat features (Compose-based)
- **amity-uikit** - Main UIKit module
- **sample** / **amity-sample-code** - Sample implementations

### Pattern
- **MVVM** for ViewModels
- **Repository Pattern** for data access
- Use **AmityBaseViewModel** as base class
- Use **AmityBaseFragment** for Fragments

### Snackbar / Toast Notification System

The project uses **two mechanisms** for showing user-facing notifications in Compose screens. Pick the right one depending on whether the snackbar needs to survive a page transition.

| Mechanism | When to use | Key file |
|-----------|-------------|----------|
| **`AmityUIKitSnackbar`** (Global Event Bus) | Show a snackbar **across pages** (e.g. emit from a ViewModel before closing the current page, displayed by the next page) or when you have no scope access | `common-compose/.../eventbus/AmityUIKitSnackbar.kt` |
| **Scope-based** (`getPageScope().showSnackbar()`) | Show a snackbar **within the same page** where you have a page or component scope | `common-compose/.../scope/AmityComposeScopeImpl.kt` |

#### Available methods

**Global event bus (`AmityUIKitSnackbar`)**:
```kotlin
// Success snackbar (checkmark icon)
AmityUIKitSnackbar.publishSnackbarMessage(
    message = "Post deleted",
    offsetFromBottom = 52    // optional, default 0
)

// Error snackbar (warning icon)
AmityUIKitSnackbar.publishSnackbarErrorMessage(
    message = "Some products that you've tagged are no longer available."
)
```

**Scope-based** (from inside a composable with page/component scope):
```kotlin
// Success snackbar (checkmark icon)
getPageScope().showSnackbar("Action completed")

// Error snackbar (warning icon)
getPageScope().showErrorSnackbar("Something went wrong")

// Progress snackbar (spinner, indefinite duration)
getPageScope().showProgressSnackbar("Posting...")

// Dismiss current snackbar
getPageScope().dismissSnackbar()
```

#### How it works
1. `AmityUIKitSnackbar` is a singleton that emits messages via `MutableSharedFlow`.
2. `AmityBasePage` and `AmityBaseComponent` host a `SnackbarHost` inside their `Scaffold`.
3. `SnackbarScope` (implemented by page/component scopes) subscribes to the flows in `registerSnackBarEvents()`.
4. Two visual composables: `AmitySnackbar` (icon + text, auto-dismiss) and `AmityProgressSnackbar` (spinner + text, indefinite).

#### Icons
- Success: `R.drawable.amity_ic_snack_bar_success` (white circle-check)
- Error/Warning: `R.drawable.amity_ic_snack_bar_warning` (white circle-warning)

### Cross-Page Communication Patterns

The project does **not** pass data back via Activity result Intents. Instead it uses global singletons and event buses.

| Pattern | Purpose | Key file |
|---------|---------|----------|
| **`AmityPostComposerHelper`** | Holds newly created/updated `AmityPost` objects in memory. Feed pages read from this singleton to render new posts at the top of the list before the next server refresh. | `social-compose/.../post/composer/AmityPostComposerHelper.kt` |
| **`AmityUIKitSnackbar`** | Fires snackbar messages across page boundaries via `SharedFlow`. Any page with a `SnackbarHost` will display the message. | `common-compose/.../eventbus/AmityUIKitSnackbar.kt` |
| **`closePageWithResult()`** | Closes the current Activity with a result code (`RESULT_OK` / `RESULT_CANCELED`). **Does not send Intent extras.** Do not attempt to pass data through the result Intent. | `common-compose/.../utils/AmityComposeExt.kt` |

#### Post creation/update flow
1. ViewModel calls SDK `createPost` / `editPost` returning `Single<AmityPost>`.
2. On success, ViewModel stores the post in `AmityPostComposerHelper` and emits `AmityPostCreationEvent.Success` via `StateFlow`.
3. If a cross-page notification is needed (e.g. product tags became unavailable), ViewModel publishes to `AmityUIKitSnackbar` **before** emitting the success event.
4. Composer page observes the success event and calls `closePageWithResult(RESULT_OK)`.
5. Feed page reads `AmityPostComposerHelper.getCreatedPosts()` and shows any pending snackbar from `AmityUIKitSnackbar`.

> **Deeper dive:** For detailed post composer internals (create/edit paths, product tag handling), see `.agent/skills/uikit/social/post/post-composer-architecture.md`. For SDK model details (`AmityPost`, `AmityProductTag`, etc.), see `.agent/skills/sdk/social/sdk-social-models.md`.

---

## Dependencies

### Key Libraries
- Kotlin 2.2.0
- Jetpack Compose (via Kotlin plugin)
- RxJava 3
- Retrofit 2.9.0
- Media3 (ExoPlayer) 1.1.0
- MockK 1.10.0 (testing)
- Timber (logging)

### Build Configuration
- Min SDK: 24
- Target/Compile SDK: 35
- Java Version: 17
- Gradle: 8.6.1
- AGP: 8.6.1

---

## Agent Skills

### What are skills?

Skills are markdown documents in `.agent/skills/` that capture deep architectural knowledge discovered while exploring the codebase. They serve as reusable reference for **any** AI agent (Claude, Copilot, Cursor, OpenCode, etc.) working on this project.

### When to create a new skill

**Always create a skill when you:**
- Discover non-obvious architecture, patterns, or conventions by exploring the codebase
- Spend significant effort understanding how a feature works end-to-end
- Find information that would be useful for future tasks in the same area
- Learn about SDK classes, APIs, or data structures that have no source code in this repo

**Do NOT create a skill for:**
- Information already covered in this AGENTS.md file
- Trivial or obvious patterns that any developer would know
- Temporary debugging notes

### Directory structure

Skills are organized by domain using subdirectories that mirror the project's module/feature structure:

```
.agent/skills/
├── uikit/                          # UIKit-layer knowledge
│   ├── social/                     # Social module
│   │   ├── post/                   # Post feature
│   │   │   └── post-composer-architecture.md
│   │   ├── feed/                   # Feed feature (example)
│   │   └── comment/                # Comment feature (example)
│   ├── chat/                       # Chat module (example)
│   └── common/                     # Shared UIKit components (example)
├── sdk/                            # SDK-layer knowledge (external deps)
│   ├── social/                     # Social SDK models & APIs
│   │   └── sdk-social-models.md
│   └── chat/                       # Chat SDK models & APIs (example)
└── infra/                          # Infrastructure (example)
    ├── build/                      # Build system, Gradle config
    └── ci/                         # CI/CD pipeline
```

**Rules:**
- Place each skill under the domain path that matches its topic: `<layer>/<module>/<feature>/`
- Layer is one of: `uikit` (UIKit app code), `sdk` (external SDK knowledge), `infra` (build/CI/tooling)
- Use kebab-case for file names: `post-composer-architecture.md`, not `PostComposerArchitecture.md`
- One topic per file — keep skills focused and atomic

### Skill file format

Each skill file is plain markdown with no special frontmatter. Start with:

```markdown
# Title

> One-line description of when to read this document.

## Section 1
...
```

**Guidelines:**
- Begin with a short blockquote explaining **when** an agent should read this file
- Use tables for structured data (method signatures, file lists, enum values)
- Include code snippets showing real usage patterns from the codebase
- Reference key file paths so agents can navigate to the source

### Referencing skills from AGENTS.md

When adding architecture sections to this file (AGENTS.md), keep them concise and link to the corresponding skill for details:

```markdown
> **Deeper dive:** For detailed internals, see `.agent/skills/uikit/social/post/post-composer-architecture.md`.
```

### Existing skills

| Skill file | Topic |
|------------|-------|
| `.agent/skills/uikit/social/post/post-composer-architecture.md` | Post composer internals — create/edit paths, product tags, media uploads, event flow |
| `.agent/skills/sdk/social/sdk-social-models.md` | SDK model reference — `AmityPost`, `AmityProductTag`, `AmityProduct`, and related types |
