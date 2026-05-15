# Cross-Platform Localization Spec

**Version:** 1.2
**Status:** Approved (validated on Android and iOS)
**Audience:** Platform engineering leads — iOS, Web, Flutter, future platforms

---

## 1. String resolution priority chain

Every user-facing string in the UIKit MUST resolve through a fixed priority chain. The system evaluates each level in order and returns the first non-empty value.

| Priority | Level | Provider | Description |
|----------|-------|----------|-------------|
| 1 (highest) | Config text | Remote server (via config.json) | Per-element text override fetched remotely. Enables on-the-fly updates without app release. |
| 2 | Programmatic override | SDK consumer (at runtime) | Key-value pairs set in code. Useful for branding or A/B testing. |
| 3 | Locale bundle | SDK consumer (at SDK init or runtime) | A complete or partial set of translations for a specific locale. |
| 4 | Library default | SDK itself (shipped with SDK) | English strings bundled in the SDK. Always present. |
| 5 (lowest) | Key name fallback | Automatic | Returns the raw key string (e.g., `"amity_social_create_post"`). |

### Level 1: Config text (remote override)

Config.json is a JSON configuration file fetched from the Amity server. Each UI element MAY have a `"text"` field. When present and non-empty, this text is used directly, bypassing all other levels.

- **Who provides it:** Amity dashboard or remote config service.
- **When it applies:** Only for UI elements that have a corresponding entry in config.json. Most strings do NOT have config entries — they resolve through levels 2–5.
- **When absent:** The system proceeds to level 2.
- **Update behavior:** When config.json is re-fetched, new text values take effect on the next UI render without app restart.

### Level 2: Programmatic override

The SDK consumer registers key-value pairs at runtime via an override API. These override everything below them.

- **Who provides it:** SDK consumer.
- **When it applies:** When the consumer has called the override API for a given key.
- **Merge behavior:** Multiple calls to set overrides MUST merge (not replace). If the consumer sets key A in one call and key B in another, both A and B are active. If the same key is set twice, the later value wins.
- **When absent:** The system proceeds to level 3.
- **Clearing:** The consumer can clear all overrides, which causes all keys to fall through to level 3+.

### Level 3: Locale bundle

The SDK consumer registers a dictionary of string key → translated value for a specific locale identifier. This is the primary mechanism for adding translations.

- **Who provides it:** SDK consumer (the app developer).
- **When it applies:** When a locale bundle is active and contains the requested key.
- **Replace behavior:** Setting a locale bundle MUST replace the entire bundle for that locale (not merge). If the consumer sets locale "ja" with keys A and B, then sets "ja" again with only key A, key B is lost.
- **Partial bundles are valid:** The consumer does not need to provide translations for every key. Missing keys fall through to level 4.
- **When absent:** The system proceeds to level 4.
- **Clearing:** The consumer can deactivate the locale (without necessarily deleting the cached bundle), which causes all keys to fall through to level 4.

### Level 4: Library default

The SDK ships with a complete set of English strings for every user-facing key.

- **Who provides it:** The SDK itself.
- **When it applies:** When levels 1–3 have no value for the requested key.
- **Guarantee:** Every key that the SDK uses internally MUST have a library default. This ensures the UI is always readable even with zero developer configuration.

### Level 5: Key name fallback

If a key has no value at any level (including no library default), the raw key string is returned.

- **When it applies:** Only when the key is not registered in the library defaults. This should never happen for SDK-internal keys — it exists as a safety net for dynamically constructed keys or developer-provided keys.
- **Diagnostic value:** Seeing a raw key in the UI indicates a bug (missing library default) or an incorrectly constructed key.

### Resolution pseudocode

```
function resolveString(elementConfig, key, formatArgs):
    // Level 1: Config text
    if elementConfig exists AND elementConfig.text is non-empty:
        return elementConfig.text

    // Level 2: Programmatic override
    if overrides[key] exists:
        value = overrides[key]
        return format(value, formatArgs)

    // Level 3: Active locale bundle
    if activeLocale is set AND localeBundle[activeLocale][key] exists:
        value = localeBundle[activeLocale][key]
        return format(value, formatArgs)

    // Level 4: Library default
    if libraryDefaults[key] exists:
        value = libraryDefaults[key]
        return format(value, formatArgs)

    // Level 5: Key name fallback
    return key
```

### Two resolution entry points

Not all strings have a config.json entry. The SDK MUST provide two resolution functions:

1. **Config-aware resolution** — Checks config.json text first, then falls through levels 2–5. Used for UI elements that have a config.json customization entry.
2. **Standard resolution** — Skips config.json, starts at level 2. Used for strings that are not configurable via config.json.

UI code chooses the appropriate entry point based on whether the element has a config.json entry.

---

## 2. Config.json string override behavior

### Structure

Config.json is a JSON document with this top-level structure:

```
{
    "preferred_theme": "light" | "dark",
    "theme": { ... },
    "excludes": [ ... ],
    "message_reactions": [ ... ],
    "social_reactions": [ ... ],
    "customizations": { ... }
}
```

### Per-element text overrides

The `customizations` object contains entries keyed by a hierarchical path representing the UI element:

```
"customizations": {
    "page_id/component_id/element_id": {
        "text": "Custom label",
        "background_color": "#1054DE",
        "icon": "icon_name"
    }
}
```

The `"text"` field is optional. When present and non-empty, it is the highest-priority source for that element's display text.

### On-the-fly update contract

- Config.json MAY be re-fetched from the server at runtime.
- When a new config is received, the SDK MUST apply updated text values on the next UI render cycle.
- The SDK MUST NOT require an app restart or screen navigation to reflect config text changes.
- Each platform SHOULD implement a config change notification mechanism so that UI components re-render when config values change.

### Non-text config fields

Config.json also contains non-text fields (colors, icons, feature flags, layout options). These are NOT part of the localization system. The localization spec covers only the `"text"` field and reaction string behavior.

### Reaction strings

Config.json defines two reaction arrays:

```
"message_reactions": [
    { "name": "heart", "image": "reaction_heart_icon" },
    { "name": "like", "image": "reaction_like_icon" }
],
"social_reactions": [
    { "name": "like", "image": "reaction_like_icon" },
    { "name": "love", "image": "reaction_love_icon" }
]
```

Each reaction has a `name` (string identifier) and an `image` (icon reference). The `name` field serves dual purpose:

1. **API identifier** — Sent to the Amity backend for add/remove reaction calls. MUST NOT be translated when used as an API parameter.
2. **Display label source** — Used to derive the user-facing display name shown in reaction tooltips and pickers.

#### Reaction display name resolution

To localize reaction display names, the SDK MUST:

1. Construct a localization key from the reaction name using the convention: `amity_common_reaction_{name}` (e.g., `"like"` → `"amity_common_reaction_like"`).
2. Resolve that key through the standard string resolution chain (levels 2–5; config text does not apply to reaction display names).
3. If the resolution returns the raw key (indicating no translation was found at any level), fall back to title-casing the reaction name (e.g., `"like"` → `"Like"`).

This approach supports both built-in reactions (which have library defaults) and custom reactions defined by third-party developers via config.json. Developers localize custom reactions by adding the corresponding `amity_common_reaction_{name}` key to their locale bundles.

#### Built-in reaction display name keys

The SDK MUST ship library defaults for these reaction display names:

| Key | Default (English) |
|-----|-------------------|
| `amity_common_reaction_like` | Like |
| `amity_common_reaction_love` | Love |
| `amity_common_reaction_fire` | Fire |
| `amity_common_reaction_happy` | Happy |
| `amity_common_reaction_sad` | Sad |
| `amity_common_reaction_heart` | Heart |
| `amity_common_reaction_grinning` | Grinning |

---

## 3. Developer-facing localization API

This section defines the behavioral contract. Each platform MUST implement these capabilities using its idiomatic patterns.

### Capability 1: Register a locale bundle

The SDK consumer provides a locale identifier and a dictionary of string key → translated value.

**Behavioral requirements:**

- The SDK MUST accept a locale identifier (e.g., `"th"`, `"ja"`, `"ko"`) and a dictionary.
- Setting a locale MUST replace the entire bundle for that locale identifier. It is NOT incremental.
- Setting a locale MUST activate that locale immediately (subsequent string resolutions use it).
- The consumer can set different locales for different modules independently (social, common, chat).
- Partial dictionaries are valid. Keys not present in the bundle fall through to library defaults.

**Platform-specific locale bundle mechanisms:**

In addition to the programmatic API, platforms MAY support native resource-based locale bundles:

- **iOS:** Consumers can place `{table}.strings` files (e.g., `AmityLocalizable.strings`) inside their app's `.lproj` directories (e.g., `th.lproj/AmityLocalizable.strings`). The framework checks `Bundle.main` (the consumer's app bundle) before its own framework bundle, allowing consumers to provide translations without calling any API. This is the idiomatic iOS pattern for framework localization and works alongside the programmatic `setLocale()` API.
- **Android:** Consumers can provide translations via Android string resources in locale-qualified `values-{lang}` directories, or via the programmatic API.
- **Web/Flutter:** Consumers use the programmatic API with dictionaries loaded from JSON, ARB, or other formats.

### Capability 2: Override specific strings

The SDK consumer provides a dictionary of key-value overrides that take priority over locale bundles.

**Behavioral requirements:**

- Multiple calls to set overrides MUST merge (accumulate). They do not replace previous overrides.
- An override with an empty string value (`""`) is a valid override. It MUST NOT be treated as absent.
- Overrides are module-scoped (social overrides do not affect common or chat).
- The consumer can clear all overrides at once.

### Capability 3: Deactivate locale

The SDK consumer can deactivate the current locale, causing all strings to resolve from library defaults.

**Behavioral requirements:**

- Deactivating the locale MUST NOT delete the cached bundle. The consumer can re-activate it later.
- After deactivation, string resolution skips level 3 and proceeds to level 4 (library defaults).

### Initialization point

- The string resolution system MUST be initialized before any UI rendering occurs.
- The recommended initialization point is during SDK setup, after the main SDK initialization call.
- Locale bundles and overrides CAN be set at any time after initialization (including at runtime for language switching).
- Setting a locale or override after UI is rendered MUST cause subsequent renders to use the new values. The platform SHOULD trigger re-rendering of visible UI.

### Per-module independence

The SDK has three string resolution modules:

| Module | Scope |
|--------|-------|
| Social | Posts, comments, communities, events, stories, livestream, clips, polls, feeds, notifications |
| Common | Reactions, ads, time labels, shared dialogs, error states, empty states |
| Chat | Messages, channels, message input, chat-specific features |

Each module has its own:
- String provider instance
- Override map
- Locale bundle storage
- Library defaults

The SDK consumer registers locale bundles and overrides separately per module. This allows translating social features without touching chat, or vice versa.

---

## 4. String key naming convention

### Pattern

All string keys follow the pattern:

```
amity_{module}_{descriptive_name}
```

### Module prefixes

| Prefix | Module | Examples |
|--------|--------|----------|
| `amity_social_` | Social | `amity_social_create_post`, `amity_social_leave_community` |
| `amity_common_` | Common | `amity_common_cancel`, `amity_common_delete` |
| `amity_chat_` | Chat | `amity_chat_send_message`, `amity_chat_message_deleted` |

### Special prefixes

| Prefix | Purpose | Examples |
|--------|---------|----------|
| `amity_common_reaction_` | Reaction display names | `amity_common_reaction_like`, `amity_common_reaction_fire` |
| `amity_common_time_` | Time-related strings | `amity_common_time_days_suffix`, `amity_common_time_hours_suffix` |
| `amity_common_ad_` | Advertisement system | `amity_common_ad_about_title`, `amity_common_ad_why_title` |

### Legacy keys

The Android implementation contains some keys without a module prefix (e.g., `amity_comments`, `amity_delete`, `amity_cancel`). These are legacy keys from before the module-scoped naming convention was established.

**Guidance for new platforms:**

- New platforms SHOULD use the module-prefixed convention for all new keys.
- If a platform needs to maintain API compatibility with existing developer-provided locale bundles, it MAY support legacy key names alongside the new convention.
- New keys MUST always use the module prefix.

### Descriptive name guidelines

- Use lowercase with underscores: `amity_social_leave_community` not `amity_social_leaveCommunity`.
- Be descriptive enough that a translator can understand the context: `amity_social_discard_post_title` not `amity_social_dp_t`.
- Group related keys with shared sub-prefixes: `amity_social_comment_reported_toast_message`, `amity_social_comment_deleted_toast_message`.
- For format strings, use the key name alone — the format specifiers are in the value, not the key.

### Rules for adding new keys

1. Determine which module the string belongs to (social, common, or chat).
2. Use the appropriate module prefix.
3. Add the key to the module's library defaults with an English value.
4. Add the key to the module's key registry (however the platform tracks known keys).
5. Verify the key does not conflict with existing keys.
6. If the string is shared across modules, it belongs in `common`.

---

## 5. Default string bundle requirements

### Zero-config guarantee

The SDK MUST work correctly with zero developer configuration. When a developer integrates the SDK without calling any localization API, all UI text MUST render in English using library defaults.

### Coverage requirement

Every string key used by SDK UI code MUST have a library default in English. There MUST be no code path where a user sees a raw key name during normal operation.

### Organization

Library defaults are organized by module:

| Module | Approximate key count (Android reference) | Scope |
|--------|-------------------------------------------|-------|
| Social | ~935 | All social features |
| Common | ~39 | Shared UI elements |
| Chat | Defined in chat module | All chat features |

Each module's defaults are self-contained. The social module does not depend on chat defaults, and vice versa. The common module contains strings used by both social and chat.

### Format strings

Some strings contain positional format placeholders:

| Placeholder | Type | Example (Android / JVM) | Example (iOS / Swift) |
|-------------|------|-------------------------|----------------------|
| `%s` / `%@` | String | `"Be the first to react to this %s!"` | `"Be the first to react to this %@!"` |
| `%d` | Integer | `"Your post exceeds the %1$d character limit"` | `"Your post exceeds the %d character limit"` |

The string resolution system MUST apply format arguments after resolving the raw string from the priority chain. If a developer provides a translation with format placeholders, the same arguments are applied to the translated string.

**Platform-specific decision:** Each platform should use its native string formatting mechanism (e.g., `String.format` in JVM, `String(format:)` in Swift, template literals in JS). The placeholder syntax may differ by platform, but the behavior (positional argument substitution) MUST be consistent.

---

## 6. Bypass detection and enforcement

### Principle

No user-facing string in UIKit UI code may be hardcoded. Every visible string MUST resolve through the string resolution system.

### What constitutes a violation

Any of the following in UI-layer code:

1. **Hardcoded string literals** assigned to text-displaying properties (e.g., `text = "Cancel"`, `Text("Settings")`, `title = "Settings"`).
2. **Direct platform resource access** that bypasses the resolution chain (e.g., `NSLocalizedString` directly, `getString(R.string.x)` directly, instead of going through the provider).
3. **Config text access without fallback** — reading the config.json text field without falling through to the localization chain when the config value is empty (e.g., `config.text ?? ""` instead of `config.text ?? localizedString`).
4. **Hardcoded strings in conditionals** — strings inside if/else or switch/match branches (e.g., `condition ? "Yes" : "No"`, `if (condition) "Yes" else "No"`).
5. **Hardcoded strings in collections** — strings in lists, maps, or arrays that are displayed to users (e.g., tab labels in a list literal).
6. **Hardcoded strings in toast/snackbar/alert calls** — strings passed directly to notification or dialog APIs (e.g., `Toast.showToast(message: "Success!")`).

### What is NOT a violation

The following are intentionally excluded:

| Category | Rationale |
|----------|-----------|
| Accessibility descriptions (content descriptions, semantic labels) | May be handled by a separate accessibility localization system. Platform-specific decision. |
| Debug/logging strings | Not user-facing. |
| Error messages in assertions, preconditions, or throws | Developer-facing, not user-facing. |
| String comparisons and equality checks | Logic, not display. |
| Constants used as identifiers (tags, keys, intent extras) | Technical identifiers, not display text. |
| Date/time format patterns | Platform-specific format strings, not translatable text. |
| Preview/test/example code | Not shipped to production. |
| Strings in localization infrastructure files | The provider implementation itself. |
| HTTP headers and auth tokens | Technical values. |
| Empty strings | No visible content. |

### Known edge cases

These patterns are harder to detect with simple text matching. Each platform SHOULD account for them:

| Edge case | Description | Detection guidance |
|-----------|-------------|-------------------|
| Strings in conditional expressions | `if (edited) " (edited)" else ""` or `condition ? "Yes" : "No"` — the hardcoded string is inside a branch, not a simple assignment. | Pattern-match string literals inside if/else, ternary, and switch/match arms. |
| Concatenated strings | `dynamicValue + " suffix"` or `"\(count) posts"` — the hardcoded part is concatenated or interpolated with a dynamic value. | Flag any string literal that starts with a space or lowercase letter when concatenated. Also flag string interpolations containing display text. |
| Strings in utility/helper functions | A utility function returns a hardcoded string that eventually reaches the UI. | Scan utility/helper files in the UI layer, not just direct UI components. |
| Strings in collection values | `["Label One", "Label Two"]`, `mapOf("key1" to "Label")`, or `[TabItem(title: "Feed")]` — tab labels or option lists. | Flag string values in array/dictionary/struct literals within UI-layer files. |
| Strings in closure/callback bodies | A closure passed to a SwiftUI view builder or Jetpack Compose composable contains a hardcoded string. | Scan closure bodies, not just top-level function bodies. |
| Multi-line string blocks | A string spans multiple lines, making single-line pattern matching fail. | Use multi-line or AST-based detection where possible. |
| Enum rawValues used as display text | `enum Tab: String { case clips = "Clips" }` where `tab.rawValue` is rendered in UI. | Flag enum rawValues of String type in UI-layer files. |
| SwiftUI `TextField` placeholder | `TextField("Search...", text: $binding)` — the first argument is display text. | Flag string literals as first argument in `TextField`, `Text`, `Label`, `Button` initializers. |
| Config fallback to empty string | `viewConfig.getText(...) ?? ""` — empty string fallback bypasses localization. | Flag `?? ""` after any `getText`/`getConfig` call. |

### Enforcement mechanism

Each platform MUST implement at least one automated check that runs in CI:

- **Option A: Static analysis / lint rule** — A custom lint rule or static analysis plugin that scans UI-layer source files for violations. This is the most robust approach.
- **Option B: Regex-based script** — A build script that uses pattern matching to detect common violations. Faster to implement, but less precise.
- **Option C: AST-based checker** — Parses source code into an AST and flags string literals in UI-producing code paths. Most precise, but most effort.

**Platform reference implementations:**

- **Android:** Uses Option B (regex-based Gradle script with ~24 detection patterns and ~20 exclusion rules).
- **iOS:** Uses Option B (regex-based shell script `scripts/check-hardcoded-strings.sh` scanning `.swift` files with 9 detection patterns and inline suppression support). The iOS codebase has two localization paths (`AmityStringProvider.resolve()` and `String.localizedString`); the enforcement check verifies both are used consistently.

The check MUST fail the build (or CI pipeline) when violations are found. It MUST NOT be advisory-only.

### Intentional violation suppression

Some string literals are legitimately hardcoded and should be excluded from enforcement. Each platform MUST support an inline suppression mechanism so that intentional exceptions are self-documenting in source code.

**Convention (cross-platform):** A comment on the same line as the violation that says `l10n:ok` followed by a brief reason suppresses the violation for that line.

| Platform | Suppression syntax | Example |
|----------|-------------------|---------|
| iOS (Swift) | `// l10n:ok <reason>` | `Text("*") // l10n:ok required-field indicator` |
| Android (Kotlin) | `// l10n:ok <reason>` | `text = "*" // l10n:ok required-field indicator` |
| Web/Flutter | `// l10n:ok <reason>` | `label: "*" // l10n:ok required-field indicator` |

**Accepted reasons (examples):**

| Reason keyword | When to use |
|----------------|-------------|
| `SwiftUI preview stub` / `preview stub` | In `#Preview {}` blocks — not shipped to production |
| `config-driven` | String rendered from a config value that is not translatable via the localization chain |
| `url placeholder` | URL, deep link, or technical scheme identifier displayed as UI placeholder |
| `internal model id` | Technical identifier used as a model key, not displayed to end users |
| `dev-only` | Unreachable code path only reachable during development |

The enforcement tool MUST skip any line whose suppression comment includes `l10n:ok`. The reason text is mandatory — a bare `l10n:ok` without a reason SHOULD be treated as a violation to prevent lazy suppressions.

---

## 7. Testing requirements

Every platform MUST implement automated tests covering the following scenarios.

### 7.1 Resolution priority order

| Test | Setup | Assertion |
|------|-------|-----------|
| Override wins over locale | Set override for key K = "A". Set locale bundle with K = "B". | `resolve(K)` returns `"A"`. |
| Locale wins over library default | Set locale bundle with key K = "C". Library default for K = "D". | `resolve(K)` returns `"C"`. |
| Library default used when nothing else set | No overrides, no locale. Library default for K = "E". | `resolve(K)` returns `"E"`. |
| Key fallback when nothing found | No overrides, no locale, no library default for key K. | `resolve(K)` returns `"K"` (raw key). |
| Config text wins over everything | Config has text "X" for element. Override for key = "Y". Locale has key = "Z". | Config-aware resolve returns `"X"`. |

### 7.2 Override behavior

| Test | Setup | Assertion |
|------|-------|-----------|
| Multiple setOverrides calls merge | Set override K1 = "A". Then set override K2 = "B". | Both K1 and K2 resolve from overrides. |
| Later override value wins | Set override K = "A". Then set override K = "B". | `resolve(K)` returns `"B"`. |
| Empty string override is valid | Set override K = "". | `resolve(K)` returns `""` (does NOT fall through). |
| Clear overrides falls through to locale | Set override K = "A" and locale K = "B". Clear overrides. | `resolve(K)` returns `"B"`. |

### 7.3 Locale bundle behavior

| Test | Setup | Assertion |
|------|-------|-----------|
| setLocale replaces entire bundle | Set locale "ja" with K1 = "A", K2 = "B". Set locale "ja" again with only K1 = "C". | `resolve(K1)` returns `"C"`. `resolve(K2)` falls through to library default (not "B"). |
| Partial bundle works | Set locale with K1 only. Library default has K1 and K2. | `resolve(K1)` returns locale value. `resolve(K2)` returns library default. |
| Clear locale falls through to default | Set locale K = "A". Clear locale. | `resolve(K)` returns library default. |

### 7.4 Config removal fallback

| Test | Setup | Assertion |
|------|-------|-----------|
| Config text present | Config has text "X" for element. | Config-aware resolve returns `"X"`. |
| Config text removed | Config had text "X", then config updated with text removed (empty). Locale has key = "Y". | Config-aware resolve returns `"Y"` (falls through to locale). |
| Config text empty string | Config has text = "". Locale has key = "Z". | Config-aware resolve returns `"Z"` (empty config text is treated as absent). |

### 7.5 Format string arguments

| Test | Setup | Assertion |
|------|-------|-----------|
| Format args with override | Override K = "Hello, %s! You have %d items." | `resolve(K, "Alice", 5)` returns `"Hello, Alice! You have 5 items."` |
| Format args with locale | Locale K = "こんにちは、%s！%d件あります。" | `resolve(K, "Alice", 5)` returns `"こんにちは、Alice！5件あります。"` |
| Format args with key fallback | No translation for K. | `resolve(K, "Alice", 5)` returns key name (format not applied to non-format string). |

### 7.6 Bypass enforcement

| Test | Setup | Assertion |
|------|-------|-----------|
| No hardcoded strings in UI layer | Run bypass detection tool on all UI source files. | Zero violations reported. |

### 7.7 Translation coverage

Each platform SHOULD implement a translation coverage check that compares the library's EN source against consumer-provided locale bundles. The check MUST report:

| Category | Definition | Severity |
|----------|-----------|----------|
| Missing | Key exists in EN but absent from locale file | Violation (blocks complete translation) |
| Untranslated | Key present in locale but value identical to EN | Violation (blocks complete translation) |
| Orphan | Key in locale but not in EN | Warning only (stale entry, safe to remove) |

**Platform reference implementations:**

- **Android:** `checkMissingTranslations` Gradle task (`buildscripts/checkMissingTranslations.gradle`) — compares `strings.xml` against Kotlin locale bundles and XML resource overlays.
- **iOS:** `scripts/check-missing-translations.sh` — compares `en.lproj/AmityLocalizable.strings` against all `*.lproj/AmityLocalizable.strings` files found in the consumer app bundle.

**KNOWN_SAME exemption:** Some keys are legitimately identical in all languages (e.g., `"ALT"`, `" *"`). Each platform's tool MUST support an exclusion list for these keys to avoid false positives.

| Test | Setup | Assertion |
|------|-------|-----------|
| Missing key detected | EN has key K. Locale file does not. | Tool reports K as MISSING. |
| Untranslated key detected | Locale has key K with same value as EN. | Tool reports K as UNTRANSLATED. |
| Orphan key reported | Locale has key K not in EN. | Tool reports K as ORPHAN (warning). |
| KNOWN_SAME key not flagged | Key K is in exclusion list. Locale value equals EN value. | Tool does NOT flag K. |
| Clean locale passes | Locale matches EN keys and all values differ (or are in KNOWN_SAME). | Tool reports success. |

---

## 8. Acceptance criteria

A platform's localization implementation is complete when ALL of the following are true:

### String resolution

- [ ] The five-level priority chain is implemented and all levels are reachable.
- [ ] Config text (level 1) overrides everything when present and non-empty.
- [ ] Programmatic overrides (level 2) override locale bundles and library defaults.
- [ ] Locale bundles (level 3) override library defaults.
- [ ] Library defaults (level 4) are present for every SDK-internal string key.
- [ ] Key name fallback (level 5) returns the raw key for unknown keys.
- [ ] Empty string `""` in config text is treated as absent (falls through). Empty string in overrides or locale bundles is treated as a valid value (does not fall through).

### Developer API

- [ ] SDK consumers can register a locale bundle with a locale identifier and a key-value dictionary.
- [ ] SDK consumers can register programmatic overrides that merge across multiple calls.
- [ ] SDK consumers can clear overrides and deactivate locale independently.
- [ ] SDK consumers can register locale bundles independently per module (social, common, chat).
- [ ] The SDK works correctly with zero localization configuration (zero-config).
- [ ] Partial locale bundles work — missing keys fall through to library defaults.

### Config.json integration

- [ ] UI elements with config.json entries check config text before the localization chain.
- [ ] UI elements without config.json entries use the localization chain directly.
- [ ] When config.json is re-fetched with updated text values, UI reflects the changes without app restart.
- [ ] When config.json text is removed, strings fall through to locale bundle or library default.

### Reaction localization

- [ ] Reaction display names resolve through `amity_common_reaction_{name}` keys.
- [ ] Built-in reactions (like, love, fire, happy, sad, heart, grinning) have library defaults.
- [ ] Custom reactions (developer-defined via config.json) fall back to title-cased name when no translation exists.
- [ ] SDK consumers can localize custom reaction names by adding keys to their locale bundles.
- [ ] Reaction `name` is never translated when used as an API parameter — only the display label is localized.

### String keys and defaults

- [ ] All string keys follow the `amity_{module}_{descriptive_name}` convention.
- [ ] Every key used in UI code has a library default in English.
- [ ] Format string placeholders work correctly at every resolution level.

### Enforcement

- [ ] An automated check exists that detects hardcoded user-facing strings in UI code.
- [ ] The check runs in CI and fails the build on violations.
- [ ] The check handles known edge cases (conditionals, concatenation, collections, callbacks).
- [ ] The check excludes non-user-facing strings (debug, accessibility, assertions, identifiers).
- [ ] An automated translation coverage check exists that detects missing and untranslated keys per locale.
- [ ] The coverage check supports a KNOWN_SAME exclusion list for keys legitimately identical across languages.

### Testing

- [ ] All test cases from Section 7 pass.
- [ ] Resolution priority order is verified with automated tests.
- [ ] Override and locale bundle behaviors (merge, replace, clear) are verified.
- [ ] Config removal fallback is verified.
- [ ] Format string argument substitution is verified.

---

## Appendix A: Platform-specific decisions

Each platform team MUST make these decisions during implementation. The spec does not prescribe the answer — only that the behavior matches this document.

| Decision | Context | Options to consider |
|----------|---------|---------------------|
| **Library default storage format** | How are English defaults stored? | Platform resource system (iOS: `.strings` / `.xcstrings`, Android: `strings.xml`, Web: JSON, Flutter: ARB), embedded dictionary, code-generated constants. |
| **Locale bundle format** | What format do SDK consumers provide translations in? | Dictionary/map in code, JSON file, platform resource overlay (iOS: `.lproj/*.strings`, Android: `values-{lang}/strings.xml`), or a combination. |
| **Consumer bundle discovery** | How does the SDK find consumer-provided translations? | iOS: Check `Bundle.main` before framework bundle. Android: Check app resources before library resources. Web/Flutter: Only via programmatic API. |
| **Rerendering on locale change** | How does the UI update when `setLocale()` is called at runtime? | Reactive state (iOS: Combine/`@Published`, Android: Flow, Web: signals), forced re-render, or activity/widget recreation. |
| **Thread safety strategy** | How are the override and locale maps protected from concurrent access? | Locks, concurrent collections, actor isolation (iOS: `@MainActor`), main-thread-only access. |
| **Bypass detection tooling** | What tool detects hardcoded strings? | SwiftLint custom rule, Android Lint/Gradle script, ESLint plugin, Dart analyzer plugin, regex script, or AST-based tool. |
| **Accessibility string handling** | Are content descriptions / accessibility labels part of the localization system or separate? | Same system (included in bypass check) or separate system (excluded from bypass check). |
| **Plural support** | How does the platform handle plural forms (1 comment vs. 5 comments)? | Platform-native pluralization (iOS: `.stringsdict`, Android: `plurals`, Flutter: ICU) or manual if/else in translations. |
| **Config change notification** | How do UI components know when config.json has been updated? | Observer pattern (iOS: `@EnvironmentObject`/`@Published`, Android: `StateFlow`), reactive streams, polling, or callback registration. |
| **Framework bundle packaging** | How are `.strings`/resource files included in distributed SDK? | iOS: SPM `Package.swift` resource declarations with `Bundle.module`, Xcode framework build phase. Android: Gradle `res/` directory. |

### Decisions made by platform

#### Android (reference implementation)

| Decision | Choice |
|----------|--------|
| Library default storage | `strings.xml` in `values/` resource directory |
| Locale bundle format | Programmatic `setLocale()` API with `Map<String, String>` |
| Consumer bundle discovery | App resources override library resources via Android resource merging |
| Rerendering | Jetpack Compose `StateFlow` |
| Thread safety | Main-thread-only access |
| Bypass detection | Regex-based Gradle script (~24 patterns, ~20 exclusion rules) |
| Translation coverage check | `checkMissingTranslations` Gradle task (`buildscripts/checkMissingTranslations.gradle`) |
| Plural support | Manual if/else in translations |
| Config change notification | `StateFlow` observation |

#### iOS

| Decision | Choice |
|----------|--------|
| Library default storage | `.strings` files in `en.lproj/AmityLocalizable.strings` |
| Locale bundle format | Both: (1) programmatic `setLocale()` API with `[String: String]`, and (2) consumer provides `.lproj/AmityLocalizable.strings` in app bundle |
| Consumer bundle discovery | Resolution checks `Bundle.main` before framework bundle (`Bundle(for: AmityUIKit4Manager.self)`) |
| Rerendering | `@Published` version counter on `AmityStringProvider` (Combine) |
| Thread safety | `@MainActor` isolation |
| Bypass detection | Regex-based shell script (`scripts/check-hardcoded-strings.sh`, 9 detection patterns, `// l10n:ok` suppression) |
| Translation coverage check | Python-based shell script (`scripts/check-missing-translations.sh`) — reports missing, untranslated, and orphan keys per locale; KNOWN_SAME exclusion list |
| Plural support | Manual if/else (e.g., `community_member_count_singular` / `community_member_count_plural`) |
| Config change notification | `@EnvironmentObject` with `AmityViewConfigController` |
| Framework bundle packaging | Xcode build phase copies `.lproj` dirs; SPM will use `Bundle.module` resource declarations |

---

## Appendix B: String count reference

For planning and estimation:

### Android (reference implementation)

| Module | String keys | Description |
|--------|-------------|-------------|
| Social | ~935 | Posts, comments, communities, events, stories, livestream, clips, polls, feeds, notifications, moderation |
| Common | ~39 | Reactions, ads, time labels, dialogs, error states, empty states |
| Chat | TBD per platform | Messages, channels, input |
| **Total** | **~974+** | Across social and common; chat is additional |

### iOS

| Module | String keys | Description |
|--------|-------------|-------------|
| Social + Common | ~878 | All social features + shared common strings in a single `.strings` file |
| Chat | TBD | Messages, channels, input |
| **Total** | **~878+** | Chat is additional |

The iOS count is lower than Android due to different UI patterns and some features not yet localized. Both platforms share the same string resolution architecture and key naming conventions. Key counts will converge as both platforms complete localization coverage.
