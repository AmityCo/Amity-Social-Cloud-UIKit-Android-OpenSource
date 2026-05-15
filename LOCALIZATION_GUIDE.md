# Amity UIKit Android — Localization Guide

This guide explains how to translate the Amity UIKit into any language. The UIKit ships with English defaults that work out of the box — you only need this guide if you want to add translations or override specific strings.

---

## Table of contents

- [Quick start](#quick-start)
- [Architecture overview](#architecture-overview)
- [String resolution priority](#string-resolution-priority)
- [Adding a new language](#adding-a-new-language)
  - [Option A: Kotlin locale bundle (recommended)](#option-a-kotlin-locale-bundle-recommended)
  - [Option B: Android resource overlay](#option-b-android-resource-overlay)
- [Overriding specific strings](#overriding-specific-strings)
- [Remote string updates via config.json](#remote-string-updates-via-configjson)
- [Localizing reaction names](#localizing-reaction-names)
- [String key reference](#string-key-reference)
- [Format strings](#format-strings)
- [Composable vs non-composable usage](#composable-vs-non-composable-usage)
- [Testing your translations](#testing-your-translations)
- [FAQ](#faq)

---

## Quick start

Add Thai translations in three steps:

```kotlin
// 1. Define your translations
object MyThaiStrings {
    val social: Map<String, String> = mapOf(
        "amity_social_post" to "โพสต์",
        "amity_social_comment" to "ความคิดเห็น",
        "amity_social_like" to "ถูกใจ",
        // ... add as many keys as you want to override
    )
    val common: Map<String, String> = mapOf(
        "amity_common_cancel" to "ยกเลิก",
        "amity_common_delete" to "ลบ",
        // ...
    )
}

// 2. Apply in your Application.onCreate()
class MyApp : Application() {
    override fun onCreate() {
        super.onCreate()
        AmityUIKit4Manager.setup(apiKey = "...", endpoint = ...)

        // Apply Thai translations
        DefaultAmitySocialStringProvider.setLocale("th", MyThaiStrings.social)
        DefaultAmityCommonStringProvider.setLocale("th", MyThaiStrings.common)
    }
}

// 3. That's it — all UIKit screens now show your Thai text
```

Any string key you don't include in your map will fall back to the English default from `strings.xml`.

---

## Architecture overview

The UIKit has three independent localization modules:

| Module | Provider class | Composable helper | Scope |
|--------|---------------|-------------------|-------|
| `social-compose` | `DefaultAmitySocialStringProvider` | `amitySocialString("key")` | Posts, comments, communities, events, stories, livestream, clips, polls |
| `common-compose` | `DefaultAmityCommonStringProvider` | `amityCommonString("key")` | Shared UI: reactions, ads, dialogs, time labels, error states |
| `chat-compose` | `DefaultAmityChatStringProvider` | `amityChatString("key")` | Chat messages, channels, message input |

Each module is independent — you can translate social without touching chat, or vice versa.

---

## String resolution priority

When the UIKit needs to display a string, it checks these sources in order and uses the first non-empty value:

```
┌─────────────────────────────────────────────────┐
│ 1. Config.json text (remote override)           │  ← Highest priority
│    Fetched remotely, applied on the fly.        │
│    Useful for A/B testing or emergency text fix. │
├─────────────────────────────────────────────────┤
│ 2. Programmatic overrides (setOverrides)        │
│    Set in code at runtime.                      │
│    Overrides everything below.                  │
├─────────────────────────────────────────────────┤
│ 3. Locale bundle (setLocale)                    │  ← Your translations go here
│    Developer-provided translation map.          │
│    This is what you'll use most often.          │
├─────────────────────────────────────────────────┤
│ 4. strings.xml (Android resource)               │
│    Library-shipped English defaults.            │
│    Always present as a safety net.              │
├─────────────────────────────────────────────────┤
│ 5. Key name fallback                            │
│    Returns the raw key (e.g., "amity_social_x") │
│    Only happens if key is missing from all above│
└─────────────────────────────────────────────────┘
```

**What this means for you:** You only need to provide translations for the strings you want to change. Everything else falls through to the English default.

---

## Adding a new language

### Option A: Kotlin locale bundle (recommended)

This is the cleanest approach — type-safe, IDE-friendly, and works at runtime.

#### Step 1: Create a translations object

```kotlin
package com.example.myapp.localization

object MySocialJapaneseStrings {
    val strings: Map<String, String> = mapOf(
        // Comments
        "amity_comments" to "コメント",
        "amity_delete_comment" to "コメントを削除",
        "amity_edit_comment" to "コメントを編集",

        // Posts
        "amity_social_post" to "投稿",
        "amity_social_create_post" to "投稿を作成",
        "amity_social_edit_post" to "投稿を編集",

        // Community
        "amity_social_community" to "コミュニティ",
        "amity_social_join" to "参加する",
        "amity_social_leave" to "退出する",

        // ... add all keys you want to translate
    )
}

object MyCommonJapaneseStrings {
    val strings: Map<String, String> = mapOf(
        "amity_common_cancel" to "キャンセル",
        "amity_common_delete" to "削除",
        "amity_common_sponsored" to "広告",

        // Reaction display names
        "amity_common_reaction_like" to "いいね",
        "amity_common_reaction_love" to "ラブ",
        "amity_common_reaction_happy" to "嬉しい",
        "amity_common_reaction_sad" to "悲しい",
        // ...
    )
}
```

#### Step 2: Apply at app startup

```kotlin
class MyApp : Application() {
    override fun onCreate() {
        super.onCreate()
        AmityUIKit4Manager.setup(apiKey = "...", endpoint = ...)

        // Detect device locale and apply
        val locale = Locale.getDefault().language
        when (locale) {
            "ja" -> {
                DefaultAmitySocialStringProvider.setLocale("ja", MySocialJapaneseStrings.strings)
                DefaultAmityCommonStringProvider.setLocale("ja", MyCommonJapaneseStrings.strings)
                // If you also use chat-compose:
                // DefaultAmityChatStringProvider.setLocale("ja", MyChatJapaneseStrings.strings)
            }
            "th" -> {
                DefaultAmitySocialStringProvider.setLocale("th", MySocialThaiStrings.strings)
                DefaultAmityCommonStringProvider.setLocale("th", MyCommonThaiStrings.strings)
            }
        }
    }
}
```

#### Step 3: Switch locale at runtime (optional)

```kotlin
// User changes language in your app settings
fun switchToThai() {
    DefaultAmitySocialStringProvider.setLocale("th", MySocialThaiStrings.strings)
    DefaultAmityCommonStringProvider.setLocale("th", MyCommonThaiStrings.strings)
    // Recomposition will pick up the new strings automatically
}

// Revert to English defaults
fun switchToEnglish() {
    DefaultAmitySocialStringProvider.getInstance().clearLocale()
    DefaultAmityCommonStringProvider.getInstance().clearLocale()
}
```

### Option B: Android resource overlay

Use standard Android `values-{locale}/strings.xml` resource folders. This approach integrates with Android's built-in locale system but requires knowing the exact resource names.

#### Step 1: Create a locale-specific strings.xml

In your app module, create `src/main/res/values-ja/strings.xml`:

```xml
<?xml version="1.0" encoding="utf-8"?>
<resources>
    <!-- Social strings -->
    <string name="amity_comments">コメント</string>
    <string name="amity_social_post">投稿</string>
    <string name="amity_social_create_post">投稿を作成</string>

    <!-- Common strings -->
    <string name="amity_common_cancel">キャンセル</string>
    <string name="amity_common_delete">削除</string>
</resources>
```

Android will automatically select the right file based on the device locale.

#### When to use which option

| | Kotlin bundle (Option A) | Android overlay (Option B) |
|---|---|---|
| **Runtime switching** | ✅ Instant, no activity restart | ❌ Requires activity recreation |
| **Partial translations** | ✅ Only include what you translate | ✅ Same |
| **Type safety** | ✅ Compile-time key validation | ❌ String names only |
| **Priority level** | Level 3 (locale bundle) | Level 4 (strings.xml) |
| **Config.json override** | ✅ Config still wins | ✅ Config still wins |
| **IDE support** | ✅ Autocomplete, refactoring | ✅ Android Studio translations editor |

**Recommendation:** Use Option A (Kotlin locale bundle) for most cases. It's higher priority, supports runtime switching, and keeps translations in a single file per language.

---

## Overriding specific strings

You can override individual strings without providing a full translation. This is useful for branding or terminology customization.

```kotlin
// Override just a few strings (works with any locale)
DefaultAmitySocialStringProvider.setOverrides(mapOf(
    "amity_social_community" to "Group",           // Rename "Community" to "Group"
    "amity_social_create_post" to "Share update",   // Custom CTA text
))

// Overrides have higher priority than locale bundles
// So even if Thai is active, these specific strings will show in English
```

To clear overrides:
```kotlin
DefaultAmitySocialStringProvider.getInstance().clearOverrides()
```

---

## Remote string updates via config.json

The UIKit supports remote string overrides via `config.json`, which is fetched from the Amity dashboard. This is the highest priority in the resolution chain.

**How it works:** Each UI element in `config.json` can have a `text` field. If present and non-empty, it overrides everything (locale bundles, strings.xml, etc.).

**Use case:** Update a button label or error message without releasing a new app version.

> **Note:** Most strings do NOT have config.json entries — they go through levels 2–5 only. Config.json text is primarily used for element-specific customizations configured through the Amity dashboard.

---

## Localizing reaction names

Reaction emoji tooltips (shown when long-pressing a reaction) are localized through the standard string resolution chain.

### Default reactions

The UIKit ships with 7 default reaction display names:

| Key | English | Thai | Italian |
|-----|---------|------|---------|
| `amity_common_reaction_like` | Like | ถูกใจ | Mi piace |
| `amity_common_reaction_love` | Love | รัก | Amore |
| `amity_common_reaction_fire` | Fire | ไฟ | Fuoco |
| `amity_common_reaction_happy` | Happy | มีความสุข | Felice |
| `amity_common_reaction_sad` | Sad | เศร้า | Triste |
| `amity_common_reaction_heart` | Heart | หัวใจ | Cuore |
| `amity_common_reaction_grinning` | Grinning | ยิ้มกว้าง | Sorriso |

Include these keys in your `common` locale bundle to translate them.

### Custom reactions

If you've configured custom reactions via `config.json` (e.g., a reaction named `"celebrate"`), you can translate it by adding the corresponding key to your locale bundle:

```kotlin
object MyCommonJapaneseStrings {
    val strings: Map<String, String> = mapOf(
        // Standard reactions
        "amity_common_reaction_like" to "いいね",
        // Custom reaction — must match the name from config.json
        "amity_common_reaction_celebrate" to "お祝い",
    )
}
```

The naming convention is: `amity_common_reaction_{reaction_name}` where `{reaction_name}` matches the `name` field in your config.json reactions array.

If no translation is found for a reaction key, the UIKit falls back to title-casing the key name (e.g., `"celebrate"` → `"Celebrate"`).

---

## String key reference

### Finding all available keys

The complete list of string keys is in these files:

- **Social:** `social-compose/src/main/java/.../localization/AmitySocialStrings.kt` (933 keys)
- **Common:** `common-compose/src/main/java/.../localization/AmityCommonStrings.kt` (38 keys)
- **Chat:** `chat-compose/src/main/java/.../localization/AmityChatStrings.kt`

Each file contains a `buildStringKeyMap()` function that maps every string key to its Android resource ID. The key names match the `strings.xml` entry names.

### Viewing default English values

Check the corresponding `strings.xml` files:

- `social-compose/src/main/res/values/strings.xml` (935 entries)
- `common-compose/src/main/res/values/strings.xml` (39 entries)

### Key naming conventions

| Prefix | Module | Example |
|--------|--------|---------|
| `amity_social_*` | social-compose | `amity_social_create_post` |
| `amity_common_*` | common-compose | `amity_common_cancel` |
| `amity_chat_*` | chat-compose | `amity_chat_send_message` |

Some social-compose keys use legacy prefixes (e.g., `amity_comments`, `amity_delete_comment`) for backward compatibility.

---

## Format strings

Some strings contain placeholders for dynamic values:

```kotlin
// strings.xml
// <string name="amity_common_be_first_to_react">Be the first to react to this %1$s!</string>

// Usage — the provider handles formatting automatically
amityCommonString("amity_common_be_first_to_react", "post")
// → "Be the first to react to this post!"
```

In your translations, keep the format placeholders in the same order:

```kotlin
"amity_common_be_first_to_react" to "เป็นคนแรกที่แสดงความรู้สึกต่อ %1\$s นี้!"
```

> **Important:** In Kotlin string literals, escape `$` as `\$` to prevent string interpolation. Write `%1\$s` not `%1$s`.

Common format patterns:
- `%s` or `%1$s` — string argument
- `%d` or `%1$d` — integer argument
- `%1$s ... %2$s` — multiple arguments in order

---

## Composable vs non-composable usage

If you're building custom UI on top of the UIKit, you may need to access localized strings in different contexts:

### Inside @Composable functions

```kotlin
@Composable
fun MyCustomPostHeader(post: AmityPost) {
    Text(text = amitySocialString("amity_social_post"))
    Text(text = amityCommonString("amity_common_delete"))
}
```

### Inside ViewModels, callbacks, or business logic

```kotlin
class MyViewModel : ViewModel() {
    fun getErrorMessage(): String {
        return DefaultAmitySocialStringProvider.getInstance()
            .getString("amity_social_something_went_wrong")
    }
}
```

### Inside remember/derivedStateOf blocks

`remember {}` and `derivedStateOf {}` blocks are **not** composable scope. Use the provider directly:

```kotlin
@Composable
fun MyComponent() {
    val provider = DefaultAmitySocialStringProvider.getInstance()
    val errorMessage by remember {
        derivedStateOf {
            provider.getString("amity_social_error_message")
        }
    }
}
```

---

## Testing your translations

### Verify resolution chain programmatically

```kotlin
// In a test or debug screen:
val provider = DefaultAmitySocialStringProvider.getInstance()

// Check a specific key
val result = provider.getString("amity_social_post")
Log.d("L10n", "amity_social_post = $result")

// Verify locale is active
provider.setLocale("th", myThaiStrings)
val thaiResult = provider.getString("amity_social_post")
Log.d("L10n", "Thai: amity_social_post = $thaiResult")
```

### Check for missing translations

If a string renders as its key name (e.g., you see `amity_social_create_post` in the UI), it means:
1. The key is missing from your locale bundle, AND
2. The key is missing from `strings.xml`

This should not happen with UIKit-shipped keys (they all have `strings.xml` defaults), but may occur if you reference a key that doesn't exist.

### Run the localization lint check

If you've forked the UIKit, run the bypass checker to ensure no hardcoded strings were introduced:

```bash
./gradlew checkLocalizationBypass
```

---

## FAQ

### Do I need to translate every string?

No. Only translate the strings you want to change. Missing keys fall through to the English default from `strings.xml`.

### Can I translate social-compose without translating common-compose?

Yes, but some UI elements (reactions, delete dialogs, ads) use common-compose strings. For a complete translation, cover both modules.

### What happens if I call setLocale() with a partial map?

It works fine. Keys in your map will use your translations. Keys not in your map will fall through to `strings.xml` English defaults.

### Can I use different languages for different modules?

Yes. Each module has an independent provider:

```kotlin
DefaultAmitySocialStringProvider.setLocale("ja", japaneseStrings)
DefaultAmityCommonStringProvider.setLocale("th", thaiStrings)
// Social UI shows Japanese, shared elements show Thai
```

(This is unusual but technically supported.)

### How do I update translations without an app release?

Use the config.json text override (level 1). Set the text value for the specific UI element in the Amity dashboard. This overrides everything else including locale bundles.

### Does this work with Jetpack Compose navigation and recomposition?

Yes. The string providers use Compose's `CompositionLocal` system. When you call `setLocale()`, subsequent recompositions will pick up the new strings. For an immediate full refresh, trigger a recomposition of the relevant screens.

### What about right-to-left (RTL) languages?

The localization system handles string content only. RTL layout support depends on your app's Android configuration (`android:supportsRtl="true"` in your manifest). The UIKit's Compose layouts generally support RTL through standard Compose RTL handling.

### Where can I find a complete example?

The sample app in this repository demonstrates both localization approaches:

- **Thai via Kotlin bundle:** `sample/src/main/java/.../localization/AmitySocialThaiStrings.kt`
- **Italian via Android overlay:** `sample/src/main/res/values-it/strings.xml`
- **Locale detection and application:** `sample/src/main/java/.../localization/AmityLocaleHelper.kt`
- **App bootstrap:** `sample/src/main/java/.../AmitySampleApp.kt`
