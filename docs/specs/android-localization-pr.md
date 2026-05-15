# Localization: Extend to social-compose and common-compose

## Summary

This PR adds full localization support to the `social-compose` and `common-compose` modules of the Android UIKit, following the exact pattern already established in `chat-compose`. Every user-facing string in these modules now flows through a localization provider with a well-defined resolution chain, enabling third-party developers to translate the UIKit into any language without modifying SDK source code.

**Branch:** `feature/social-common-localization`
**239 files changed**, **7,866 insertions**, **1,878 deletions** across 18 commits.

---

## What changed

### New localization infrastructure

| Component | Module | File | Purpose |
|-----------|--------|------|---------|
| `AmitySocialStringProvider` | social-compose | `localization/AmitySocialStringProvider.kt` | String resolution interface + singleton |
| `AmitySocialStrings` | social-compose | `localization/AmitySocialStrings.kt` | 949 string key → resource ID mappings |
| `AmitySocialConfigString` | social-compose | `localization/AmitySocialConfigString.kt` | Config.json + localization fallback |
| `AmityCommonStringProvider` | common-compose | `localization/AmityCommonStringProvider.kt` | String resolution interface + singleton |
| `AmityCommonStrings` | common-compose | `localization/AmityCommonStrings.kt` | 39 string key → resource ID mappings |
| `AmityCommonConfigString` | common-compose | `localization/AmityCommonConfigString.kt` | Config.json + localization fallback |

### String resolution priority chain

Every string resolves through this priority order (highest to lowest):

```
1. Config.json text         → Remote override (on-the-fly updates)
2. Programmatic overrides   → setOverrides(mapOf("key" to "value"))
3. Locale bundle            → setLocale("th", thaiStrings)
4. strings.xml              → Android resource default (library-shipped English)
5. Key name fallback        → Returns "amity_social_cancel" as-is
```

The `amitySocialConfigString()` / `amityCommonConfigString()` composables implement the full chain including config.json. The `amitySocialString()` / `amityCommonString()` composables implement levels 2–5 (for strings that have no config.json entry).

### Externalized strings

| Module | String keys | strings.xml entries |
|--------|-------------|---------------------|
| social-compose | 949 | 952 |
| common-compose | 39 | 40 |
| **Total** | **988** | **992** |

All hardcoded English strings across both modules have been replaced:
- Composable functions use `amitySocialString("key")` or `amityCommonString("key")`
- ViewModels and non-composable code use `DefaultAmitySocialStringProvider.getInstance().getString("key")`
- Config-aware UI elements use `amitySocialConfigString("fallback_key")`

### Reaction display name localization

Added `amityReactionDisplayName(reactionKey)` composable that localizes reaction labels (the tooltip shown when hovering over a reaction emoji). It resolves `amity_common_reaction_{key}` through the localization chain and falls back to title-cased key name for unknown/custom reactions. Seven default reaction names are provided: like, love, fire, happy, sad, heart, grinning.

### Lint enforcement

`checkLocalizationBypass.gradle` (a Gradle verification task) scans source files for hardcoded strings and enforces 24 detection patterns across both modules:

- Direct `stringResource(R.string.amity...)` calls
- `context.getString()` bypasses
- `getConfig().getText()` without localization fallback
- Hardcoded string literals in `text =`, `description =`, `buttonText =`, `message =`, `title =`, `confirmText =`, `dismissText =`, `hintText =`, `placeHolderText =` parameters
- Hardcoded strings in `if/else` branches, `when` results, `mapOf` values
- `showSnackbar()` / `showProgressSnackbar()` with hardcoded text
- `tabList.add()` and `listOf()` with hardcoded labels
- **Thai translation coverage** — every key in `strings.xml` must have a corresponding entry in `AmitySocialThaiStrings.kt` / `AmityCommonThaiStrings.kt`

The task outputs file paths and line numbers for violations and fails the build if any are found.

**Exclusions** (intentionally allowed): `contentDescription` accessibility strings, `@Preview` functions, date/time format patterns, string comparisons, constants, error/throw/require/check messages, comments, imports, annotations, shimmer/skeleton UI files.

### Config.json cleanup

Removed text overrides from `common/src/main/assets/config.json`. Previously, config.json contained hardcoded English text that shadowed the localization chain. With the unified resolution (config → overrides → locale → strings.xml → key), these redundant text values were removed so that locale bundles and strings.xml properly take effect. Non-text config (colors, icons, feature flags) is unchanged.

### Sample app translations

| Locale | Social keys | Common keys | Method |
|--------|-------------|-------------|--------|
| Thai (th) | 949 | 39 | Kotlin locale bundle (`AmitySocialThaiStrings`, `AmityCommonThaiStrings`) |
| Italian (it) | 952 | 40 | Android resource overlay (`values-it/strings.xml`) |

Both methods are demonstrated to show third-party developers the two supported approaches for adding translations.

The sample app includes `AmityLocaleHelper` — a utility that detects the device locale and applies the corresponding translation bundle at startup.

### Unit tests

| Test file | Test cases | Coverage |
|-----------|------------|----------|
| `DefaultAmitySocialStringProviderTest` | 22 | Full resolution chain priority, format args, override/locale interactions, clear/reset behavior |
| `DefaultAmityCommonStringProviderTest` | 8 | Resolution chain, format args, clear/reset |

Key test scenarios:
- Override takes priority over locale bundle
- Locale bundle takes priority over strings.xml
- Key name returned as fallback when nothing matches
- `clearOverrides()` / `clearLocale()` properly fall through
- Format string arguments (`%s`, `%d`) work at every priority level
- `setLocale()` replaces entire bundle (not incremental)

---


## How to verify

### Build
```bash
./gradlew :social-compose:compileDebugKotlin :common-compose:compileDebugKotlin :sample:compileDebugKotlin
```

### Lint check
```bash
./gradlew checkLocalizationBypass
# Expected:
# checkLocalizationBypass: No localization bypasses found. ✓
# checkLocalizationBypass: Thai translation coverage complete. ✓
```

### Unit tests
```bash
./gradlew :social-compose:testDebugUnitTest :common-compose:testDebugUnitTest
# Expected: All 30 tests pass
```

### Manual verification
1. Run the sample app with device locale set to Thai → all UI text should render in Thai
2. Run with device locale set to Italian → all UI text should render in Italian
3. Run with device locale set to English (or any unsupported locale) → English defaults from strings.xml
4. Verify reactions: long-press a reaction emoji → tooltip shows localized name

---

## Post-review hardcoded string fixes

After the initial PR, additional hardcoded strings were identified and fixed during QA:

| String(s) | Location | Key(s) added |
|-----------|----------|--------------|
| "Upload image" (baked into vector drawable path data) | `AmityPollTypeImageView` | `amity_social_poll_upload_image` |
| "Name your link", "Search category" | Link preview / category search | `amity_social_name_your_link`, `amity_common_search_category_hint` |
| "posts", "members" (community profile info row) | `AmityCommunityInfoView` | `amity_social_community_posts_label`, `amity_social_community_members_label` |
| "post/posts", "join request/s", "requires/require approval", "and", "Your posts are pending for review" | `AmityCommunityProfileActionView` banner | `amity_social_community_post_label`, `amity_social_community_join_request_label`, `amity_social_community_requires_approval`, `amity_social_community_require_approval`, `amity_social_community_posts_pending_review`, `amity_social_community_and` |
| "Everyone", "Only moderator", "Off" (notification settings) | `AmityCommunityNotificationSettingDataType` enum | `amity_social_notification_everyone`, `amity_social_notification_only_moderator`, `amity_social_notification_off` |
| "members" (explore / recommended community cards) | `AmityCommunityView`, `AmityRecommendedCommunities` | Reuses `amity_social_community_members_label` |

`contentDescription = "…"` lines (TalkBack accessibility metadata) were removed from enforcement — they do not require localization.

---

## Breaking changes

**None.** This is fully backward-compatible:

- Zero-config works out of the box — English strings.xml defaults render without any developer action
- No public API changes — existing integrations continue working unchanged
- New localization API is opt-in — developers only call `setLocale()` if they want translations
- Config.json text overrides still work for remote string updates (highest priority in the chain)
