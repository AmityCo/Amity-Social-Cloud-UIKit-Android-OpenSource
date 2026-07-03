# Chat Config & Localization Architecture

> Read this when working on customization, theming, string externalization, or feature toggles in `chat-compose`. Also read before adding new config properties or new user-facing strings to the module.

## Overview

The `chat-compose` module has two externalization systems wired in parallel:

1. **Config-driven customization** — bubble colors, feature toggles, composer settings via `config.json`
2. **String localization** — all user-facing strings overridable at runtime without recompiling

Both are backward-compatible: no config file needed, everything has sensible defaults.

---

## Config System

### Architecture

```
config.json (assets/)
    ↓ Gson parse
AmityUIKitConfig (data class, common-compose)
    ↓
AmityUIKitConfigController (singleton, common-compose)
    ↓
AmityChatConfigHelper (facade, chat-compose)
    ↓
Composables (AmityChatMessageBubble, AmityConversationChatUserActionSheet, etc.)
```

### Key files

| File | Role |
|---|---|
| `common/src/main/assets/config.json` | Source of truth for default config values |
| `common-compose/.../AmityUIKitConfig.kt` | Top-level data class; add new fields here |
| `common-compose/.../AmityUIKitConfigController.kt` | Singleton accessor; add typed getter methods here |
| `chat-compose/.../config/AmityChatBubbleColors.kt` | Bubble color data class with `fromConfig()` and `DEFAULTS` |
| `chat-compose/.../config/AmityChatComposerConfig.kt` | Composer settings data class |
| `chat-compose/.../config/AmityChatConfigHelper.kt` | **Public API** — this is what composables call |

### Adding a new config property

1. Add the field to `AmityUIKitConfig.kt` with a default value (backward compat)
2. Add an entry to `config.json` under `customizations` or top-level
3. Add a typed accessor to `AmityUIKitConfigController.kt`
4. Expose it via `AmityChatConfigHelper.kt`
5. Wire it in the relevant composable

### Customizations map pattern

The `customizations` map uses `page/component/element` wildcard keys — same pattern as Flutter:

```json
{
  "customizations": {
    "chat_page/message_bubble/*": {
      "right_bubble_color": "#1054DE",
      "left_bubble_color": "#EBECEF"
    }
  }
}
```

Read a value: `AmityUIKitConfigController.getCustomizationConfig("chat_page/message_bubble/*")`

### Feature toggles

Conversation chat user actions (mute, report, block) are controlled by:

```json
{
  "conversation_chat_user_actions": [
    { "name": "mute",   "enabled": true },
    { "name": "report", "enabled": false }
  ]
}
```

Read: `AmityChatConfigHelper.isConversationUserActionEnabled("report")`

---

## Localization System

### Architecture

```
strings.xml (library default)          ← lowest priority
    ↑
AmityChatStrings.buildStringKeyMap()   ← maps key → R.string.id (compile-time)
    ↑
DefaultAmityChatStringProvider         ← singleton, resolves strings
    ↑
Locale bundle (setLocale())            ← dev-provided per-language map
    ↑
Programmatic override (setOverrides()) ← highest priority
    ↑
amityChatString(key)                   ← helper called in composables
```

### Key files

| File | Role |
|---|---|
| `chat-compose/.../localization/AmityChatStrings.kt` | Compile-time map of 70+ string keys → `R.string` IDs |
| `chat-compose/.../localization/AmityChatStringProvider.kt` | Interface + `DefaultAmityChatStringProvider` + `LocalAmityChatStringProvider` + `amityChatString()` |
| `chat-compose/src/main/res/values/strings.xml` | Library default strings; ~220 entries total |

### Resolution priority (highest → lowest)

1. Programmatic overrides (`setOverrides()`)
2. Active locale bundle (`setLocale()`)
3. Android string resources (`strings.xml` in library)
4. Key name fallback (returns the key itself)

### Adding a new string

1. Add entry to `strings.xml`: `<string name="amity_chat_my_key">Default text</string>`
2. Add to `AmityChatStrings.buildStringKeyMap()`: `"amity_chat_my_key" to R.string.amity_chat_my_key`
3. Use in composable: `val label = amityChatString("amity_chat_my_key")`

**Never use a hardcoded string literal in a composable.** Always go through `amityChatString()`.

### ⚠️ Critical: Do NOT use `getIdentifier()` in library code

```kotlin
// ❌ WRONG — looks up by host app's package name, not library's
context.resources.getIdentifier("amity_chat_title", "string", context.packageName)

// ✅ CORRECT — compile-time Map<String, Int> keyed by library's R.string IDs
AmityChatStrings.buildStringKeyMap()["amity_chat_title"]?.let { context.getString(it) }
```

`getIdentifier()` uses `context.packageName` which in a library is the **host app's** package — string resources won't be found.

### ⚠️ Critical: Use pure JVM for color parsing in data classes

```kotlin
// ❌ WRONG — android.graphics.Color breaks JUnit unit tests
android.graphics.Color.parseColor("#1054DE")

// ✅ CORRECT — pure JVM, works in standard JUnit without Robolectric
val parsed = hexString.removePrefix("#").toLong(16)
val color = Color(0xFF000000L or parsed)
```

This keeps data class tests fast (no Robolectric needed).

---

## Scope Boundaries

- **In scope:** `conversation/`, `group/`, `setting/`, `message/`, `home/` packages
- **Out of scope:** `live_chat/` — separate code path, not yet covered

---

## Developer-facing API (for SDK consumers)

See `chat-compose/README_CUSTOMIZATION.md` for the complete developer guide with examples.
