# Chat-Compose Customization & Localization Guide

## Overview

The chat-compose module supports config-driven customization and string localization.
All customization is optional — the module works out of the box with sensible defaults.

## Config-Driven Customization

### Bubble Colors

Customize message bubble colors via `config.json` in your app's `assets/` folder:

```json
{
  "customizations": {
    "chat_page/message_bubble/*": {
      "right_bubble_color": "#FF6B00",
      "right_bubble_text_color": "#FFFFFF",
      "left_bubble_color": "#F0F0F0",
      "left_bubble_text_color": "#333333"
    },
    "group_chat_page/message_bubble/*": {
      "right_bubble_color": "#FF6B00",
      "right_bubble_text_color": "#FFFFFF"
    }
  }
}
```

Available bubble color properties:

| Property | Default | Description |
|---|---|---|
| `left_bubble_color` | `#EBECEF` | Background color for received messages |
| `left_bubble_pressed_color` | `#A5A9B5` | Pressed state for received messages |
| `left_bubble_text_color` | `#292B32` | Text color for received messages |
| `left_bubble_subtle_text_color` | `#636878` | Secondary text in received messages |
| `left_bubble_preview_link_color` | `#FFFFFF` | Link preview background in received |
| `right_bubble_color` | `#1054DE` | Background color for sent messages |
| `right_bubble_pressed_color` | `#1A4499` | Pressed state for sent messages |
| `right_bubble_text_color` | `#FFFFFF` | Text color for sent messages |
| `right_bubble_subtle_text_color` | `#A9C4F9` | Secondary text in sent messages |
| `right_bubble_preview_link_color` | `#FFFFFF` | Link preview background in sent |
| `bubble_divider_color` | `#C1C1C1` | Divider inside bubble content |

Or override programmatically at runtime:

```kotlin
AmityChatConfigHelper.overrideBubbleColors(
    rightBubbleColor = "#FF6B00",
    rightBubbleTextColor = "#FFFFFF",
)
```

### Feature Toggles (Conversation Chat)

Control which actions appear in the conversation chat action sheet:

```json
{
  "conversation_chat_user_actions": [
    { "name": "mute", "enabled": true },
    { "name": "report", "enabled": false },
    { "name": "block", "enabled": true }
  ]
}
```

Setting `"enabled": false` hides the action from the user action sheet.
Actions not listed in the array default to enabled.

### Composer Settings

```json
{
  "customizations": {
    "chat_page/message_composer/*": {
      "message_limit": 3000,
      "placeholder_text": "Say something..."
    }
  }
}
```

## Localization

All user-facing strings in the chat module can be overridden by consuming developers.

### Override specific strings

```kotlin
// After AmityUIKitConfigController.setup(context)
DefaultAmityChatStringProvider.initialize(context)
DefaultAmityChatStringProvider.setOverrides(mapOf(
    "amity_chat_home_title" to "Messaging",
    "amity_chat_composer_placeholder" to "Write something...",
))
```

### Provide a full locale bundle

```kotlin
DefaultAmityChatStringProvider.initialize(context)
DefaultAmityChatStringProvider.setLocale("ja", mapOf(
    "amity_chat_home_title" to "チャット",
    "amity_chat_tab_all" to "すべて",
    "amity_chat_tab_direct" to "ダイレクト",
    "amity_chat_tab_groups" to "グループ",
    // See AmityChatStrings.kt for all available keys
))
```

### String resolution priority

1. **Programmatic overrides** (highest) — set via `setOverrides()`
2. **Locale bundles** — set via `setLocale()`
3. **Android string resources** — from `strings.xml` in the library
4. **Key name fallback** — returns the key itself if nothing else matches

### Available string keys

See `AmityChatStrings.kt` for the complete list of overridable string keys.
All keys follow the `amity_chat_*` prefix convention. Key categories include:

- **Chat home:** `amity_chat_home_title`, `amity_chat_tab_all`, `amity_chat_tab_direct`, `amity_chat_tab_groups`
- **Conversation:** `amity_chat_composer_placeholder`, `amity_chat_replying_to`
- **Message options:** `amity_chat_option_reply`, `amity_chat_option_copy`, `amity_chat_option_edit`, `amity_chat_option_delete`
- **User actions:** `amity_chat_action_mute`, `amity_chat_action_unmute`, `amity_chat_option_report_user`
- **Status:** `amity_chat_status_edited`, `amity_chat_sending_status`, `amity_chat_status_failed`
- **Errors/success:** `amity_chat_error_report_user`, `amity_chat_success_user_reported`, etc.
- **Group chat:** `amity_chat_group_settings_title`, `amity_chat_group_leave`, etc.
