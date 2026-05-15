# Localization Key Deduplication Implementation Plan

> **For agentic workers:** REQUIRED SUB-SKILL: Use superpowers:subagent-driven-development (recommended) or superpowers:executing-plans to implement this plan task-by-task. Steps use checkbox (`- [ ]`) syntax for tracking.

**Goal:** Align Android localization keys with iOS master keys from `gap-01.csv` by adding alias registry entries, switching call sites to canonical iOS master keys, fixing broken call sites, and consolidating redundant feed config element IDs.

**Architecture:** Add iOS master keys as aliases (pointing to existing R.string values) in `AmitySocialStrings.kt` / `AmityCommonStrings.kt`, then update call sites to use the new canonical keys. No user-visible behavior changes — only which localization key is used internally.

**Tech Stack:** Kotlin, Jetpack Compose, Android string resources (R.string), `amitySocialString()` / `amityCommonString()` @Composable helpers, `DefaultAmitySocialStringProvider.getInstance().getString()` for non-composable contexts.

**Worktree:** `/Users/warakorn/Documents/GitHub/cleverden/frontend-agentic/worktree/localization-alignment`
**Branch:** `ws/feat/localization-alignment`
**PR:** https://github.com/AmityCo/Amity-Social-Cloud-UIKit-Android/pull/623

---

## Key Architecture Facts

- `amitySocialString("dot.key")` and `amityCommonString("dot.key")` are `@Composable` functions — must be called at composable scope only.
- `DefaultAmitySocialStringProvider.getInstance().getString("dot.key")` — non-composable variant, used in callbacks.
- `amitySocialConfigString("element_id")` — config-first: checks config.json, then falls back to `amitySocialString(key)`. Config element IDs use underscore format (e.g. `amity_v4_blocked_user_feed`), not dot-notation.
- Registry: `AmitySocialStrings.kt` → `buildStringKeyMap(): Map<String, Int>` mapping dot-notation keys to `R.string` IDs. `AmityCommonStrings.kt` is identical pattern for common module.
- Key naming: dots → underscores for R.string name (e.g. `social.button.done` → `R.string.social_button_done`).
- **Alias pattern**: multiple dot-notation keys can map to the same `R.string` (e.g. two keys both → `R.string.social_button_done`).

---

## Task 1: Add common.button.join and common.button.remove to Common Registry

**Files:**
- Modify: `common-compose/src/main/res/values/strings.xml`
- Modify: `common-compose/src/main/java/com/amity/socialcloud/uikit/common/localization/AmityCommonStrings.kt`

- [ ] **Step 1: Add new R.string entries to common strings.xml**

  Open `common-compose/src/main/res/values/strings.xml`. Add before `</resources>`:

  ```xml
  <string name="common_button_join">Join</string>
  <string name="common_button_remove">Remove</string>
  ```

- [ ] **Step 2: Register new keys in AmityCommonStrings.kt**

  Open `common-compose/src/main/java/com/amity/socialcloud/uikit/common/localization/AmityCommonStrings.kt`. Add to `buildStringKeyMap()` after the last existing entry:

  ```kotlin
  "common.button.join" to R.string.common_button_join,
  "common.button.remove" to R.string.common_button_remove,
  ```

- [ ] **Step 3: Verify build compiles**

  Run: `./gradlew :common-compose:compileDebugKotlin`
  Expected: BUILD SUCCESSFUL

- [ ] **Step 4: Commit**

  ```bash
  git add common-compose/src/main/res/values/strings.xml \
          common-compose/src/main/java/com/amity/socialcloud/uikit/common/localization/AmityCommonStrings.kt
  git commit -m "feat: add common.button.join and common.button.remove to common registry"
  ```

---

## Task 2: Add iOS Master Key Aliases to Social Registry

**Files:**
- Modify: `social-compose/src/main/java/com/amity/socialcloud/uikit/community/compose/localization/AmitySocialStrings.kt`

All entries are **aliases** to existing R.string values — no new XML strings needed.

- [ ] **Step 1: Add alias entries to AmitySocialStrings.kt**

  Open the file. Add these entries to `buildStringKeyMap()` after the last existing entry:

  ```kotlin
  // iOS master key aliases — Event
  "social.label.event.attendees.page.title" to R.string.social_button_attendees,
  "social.label.event.setup.edit.event.title" to R.string.social_button_edit_event,
  "social.modal.add.calendar.sheet.add.button" to R.string.social_label_add_to_calendar,

  // iOS master key aliases — Live stream
  "social.status.banned.page.title" to R.string.social_status_live_stream,

  // iOS master key aliases — Product tag
  "social.button.report.reason.done.button" to R.string.social_button_done,
  "social.modal.alert.remove.button" to R.string.social_button_remove,
  "social.toast.waiting.network.toast" to R.string.social_toast_snackbar_waiting_for_network,

  // iOS master key aliases — Event feed tabs
  "social.button.event.feed.past" to R.string.social_tab_tab_past,
  "social.status.event.feed.upcoming" to R.string.social_tab_tab_upcoming,
  "social.status.event.detail.header.status.upcoming" to R.string.social_tab_tab_upcoming,
  ```

- [ ] **Step 2: Verify build compiles**

  Run: `./gradlew :social-compose:compileDebugKotlin`
  Expected: BUILD SUCCESSFUL

- [ ] **Step 3: Commit**

  ```bash
  git add social-compose/src/main/java/com/amity/socialcloud/uikit/community/compose/localization/AmitySocialStrings.kt
  git commit -m "feat: add iOS master key aliases to social registry"
  ```

---

## Task 3: Switch ProductTagSelectionComponent Call Sites to iOS Master Keys

**Files:**
- Modify: `social-compose/src/main/java/com/amity/socialcloud/uikit/community/compose/product/AmityProductTagSelectionComponent.kt`

- [ ] **Step 1: Switch three string keys**

  Line ~172 — change:
  ```kotlin
  actionText = amitySocialString("social.button.done"),
  ```
  to:
  ```kotlin
  actionText = amitySocialString("social.button.report.reason.done.button"),
  ```

  Line ~319 — change:
  ```kotlin
  getComponentScope().showProgressSnackbar(amitySocialString("social.toast.snackbar.waiting.for.network"))
  ```
  to:
  ```kotlin
  getComponentScope().showProgressSnackbar(amitySocialString("social.toast.waiting.network.toast"))
  ```

  Line ~495 — change:
  ```kotlin
  contentDescription = amitySocialString("social.button.remove"),
  ```
  to:
  ```kotlin
  contentDescription = amitySocialString("social.modal.alert.remove.button"),
  ```

- [ ] **Step 2: Verify build compiles**

  Run: `./gradlew :social-compose:compileDebugKotlin`
  Expected: BUILD SUCCESSFUL

- [ ] **Step 3: Commit**

  ```bash
  git add social-compose/src/main/java/com/amity/socialcloud/uikit/community/compose/product/AmityProductTagSelectionComponent.kt
  git commit -m "feat: switch ProductTagSelection to iOS master localization keys"
  ```

---

## Task 4: Switch CommunityEventFeed Call Sites to iOS Master Keys

**Files:**
- Modify: `social-compose/src/main/java/com/amity/socialcloud/uikit/community/compose/community/profile/component/AmityCommunityEventFeed.kt`

- [ ] **Step 1: Switch two tab label keys**

  Line ~120 — change:
  ```kotlin
  label = amitySocialString("social.tab.tab.upcoming"),
  ```
  to:
  ```kotlin
  label = amitySocialString("social.status.event.feed.upcoming"),
  ```

  Line ~125 — change:
  ```kotlin
  label = amitySocialString("social.tab.tab.past"),
  ```
  to:
  ```kotlin
  label = amitySocialString("social.button.event.feed.past"),
  ```

- [ ] **Step 2: Verify build compiles**

  Run: `./gradlew :social-compose:compileDebugKotlin`
  Expected: BUILD SUCCESSFUL

- [ ] **Step 3: Commit**

  ```bash
  git add social-compose/src/main/java/com/amity/socialcloud/uikit/community/compose/community/profile/component/AmityCommunityEventFeed.kt
  git commit -m "feat: switch CommunityEventFeed tabs to iOS master localization keys"
  ```

---

## Task 5: Switch EventDetailPage and EventMenuBottomSheet Call Sites

**Files:**
- Modify: `social-compose/src/main/java/com/amity/socialcloud/uikit/community/compose/event/detail/AmityEventDetailPage.kt`
- Modify: `social-compose/src/main/java/com/amity/socialcloud/uikit/community/compose/event/detail/elements/AmityEventMenuBottomSheet.kt`

- [ ] **Step 1: Switch keys in AmityEventDetailPage.kt**

  Lines ~769, 775 — change both occurrences of:
  ```kotlin
  amitySocialString("social.label.add.to.calendar")
  ```
  to:
  ```kotlin
  amitySocialString("social.modal.add.calendar.sheet.add.button")
  ```

  Lines ~1579, 1590 — change both occurrences of:
  ```kotlin
  amitySocialString("social.button.attendees")
  ```
  to:
  ```kotlin
  amitySocialString("social.label.event.attendees.page.title")
  ```

- [ ] **Step 2: Switch keys in AmityEventMenuBottomSheet.kt**

  Lines ~90, 96 — change both occurrences of:
  ```kotlin
  amitySocialString("social.button.edit.event")
  ```
  to:
  ```kotlin
  amitySocialString("social.label.event.setup.edit.event.title")
  ```

  Lines ~121, 127 — change both occurrences of:
  ```kotlin
  amitySocialString("social.label.add.to.calendar")
  ```
  to:
  ```kotlin
  amitySocialString("social.modal.add.calendar.sheet.add.button")
  ```

- [ ] **Step 3: Verify build compiles**

  Run: `./gradlew :social-compose:compileDebugKotlin`
  Expected: BUILD SUCCESSFUL

- [ ] **Step 4: Commit**

  ```bash
  git add social-compose/src/main/java/com/amity/socialcloud/uikit/community/compose/event/detail/AmityEventDetailPage.kt \
          social-compose/src/main/java/com/amity/socialcloud/uikit/community/compose/event/detail/elements/AmityEventMenuBottomSheet.kt
  git commit -m "feat: switch EventDetail and EventMenu to iOS master localization keys"
  ```

---

## Task 6: Switch CommunityJoinButton to common.button.join

**Files:**
- Modify: `social-compose/src/main/java/com/amity/socialcloud/uikit/community/compose/community/membership/element/AmityCommunityJoinButton.kt`

- [ ] **Step 1: Add import for amityCommonString if not already present**

  At the top of the file, add if missing:
  ```kotlin
  import com.amity.socialcloud.uikit.common.localization.amityCommonString
  ```

- [ ] **Step 2: Switch two call sites from social to common key**

  Lines ~160, 168 — change both occurrences of:
  ```kotlin
  amitySocialString("social.button.community.invitation.accept.button")
  ```
  to:
  ```kotlin
  amityCommonString("common.button.join")
  ```

- [ ] **Step 3: Verify build compiles**

  Run: `./gradlew :social-compose:compileDebugKotlin`
  Expected: BUILD SUCCESSFUL

- [ ] **Step 4: Commit**

  ```bash
  git add social-compose/src/main/java/com/amity/socialcloud/uikit/community/compose/community/membership/element/AmityCommunityJoinButton.kt
  git commit -m "feat: switch CommunityJoinButton to common.button.join iOS master key"
  ```

---

## Task 7: Fix Broken Leave Dialog Keys in AmityCommunitySetupPage.kt

**Files:**
- Modify: `social-compose/src/main/java/com/amity/socialcloud/uikit/community/compose/community/setup/AmityCommunitySetupPage.kt`

**Problem:** Lines 1219–1232 use `DefaultAmitySocialStringProvider.getInstance().getString(...)` with old-format keys (`amity_v4_community_setup_dialog_leave_*`) that are NOT registered. These return the key string itself as fallback (broken UI). The correct registered dot-notation keys are in `AmitySocialStrings.kt` lines 284–287.

- [ ] **Step 1: Fix the four broken getString calls**

  Line 1219 — change:
  ```kotlin
  dialogTitle = DefaultAmitySocialStringProvider.getInstance().getString("amity_v4_community_setup_dialog_leave_edit_title"),
  ```
  to:
  ```kotlin
  dialogTitle = DefaultAmitySocialStringProvider.getInstance().getString("social.modal.community.setup.dialog.leave.edit.title"),
  ```

  Line 1220 — change:
  ```kotlin
  dialogText = DefaultAmitySocialStringProvider.getInstance().getString("amity_v4_community_setup_dialog_leave_edit_description"),
  ```
  to:
  ```kotlin
  dialogText = DefaultAmitySocialStringProvider.getInstance().getString("social.modal.community.setup.dialog.leave.edit.description"),
  ```

  Line 1231 — change:
  ```kotlin
  dialogTitle = DefaultAmitySocialStringProvider.getInstance().getString("amity_v4_community_setup_dialog_leave_title"),
  ```
  to:
  ```kotlin
  dialogTitle = DefaultAmitySocialStringProvider.getInstance().getString("social.modal.community.setup.dialog.leave.title"),
  ```

  Line 1232 — change:
  ```kotlin
  dialogText = DefaultAmitySocialStringProvider.getInstance().getString("amity_v4_community_setup_dialog_leave_description"),
  ```
  to:
  ```kotlin
  dialogText = DefaultAmitySocialStringProvider.getInstance().getString("social.modal.community.setup.dialog.leave.description"),
  ```

- [ ] **Step 2: Verify build compiles**

  Run: `./gradlew :social-compose:compileDebugKotlin`
  Expected: BUILD SUCCESSFUL

- [ ] **Step 3: Commit**

  ```bash
  git add social-compose/src/main/java/com/amity/socialcloud/uikit/community/compose/community/setup/AmityCommunitySetupPage.kt
  git commit -m "fix: correct broken leave dialog localization keys in CommunitySetupPage"
  ```

---

## Task 8: Consolidate Feed Config Element IDs

**Files:**
- Modify: `social-compose/src/main/java/com/amity/socialcloud/uikit/community/compose/ui/components/feed/user/AmityBlockedUserFeed.kt`
- Modify: `social-compose/src/main/java/com/amity/socialcloud/uikit/community/compose/ui/components/feed/user/AmityPrivateUserFeed.kt`

**Context:** Both files use `amitySocialConfigString(fallbackKey)` where `fallbackKey` is selected by `when(feedType)` yielding IMAGE/VIDEO/CLIP-specific config element IDs. Consolidate so all feed types use the generic element ID.

- [ ] **Step 1: Consolidate AmityBlockedUserFeed.kt**

  Find the `when(feedType)` block building `fallbackKey` (around lines 41–47). Replace the entire `remember` block with:
  ```kotlin
  val fallbackKey = remember(feedType) {
      "amity_v4_blocked_user_feed"
  }
  ```

  Find the `when(feedType)` block building the info key (around lines 48–55). Replace with:
  ```kotlin
  val fallbackKeyInfo = remember(feedType) {
      "amity_v4_blocked_user_feed_info"
  }
  ```

- [ ] **Step 2: Consolidate AmityPrivateUserFeed.kt**

  Apply the same pattern:
  ```kotlin
  val fallbackKey = remember(feedType) {
      "amity_v4_private_user_feed"
  }

  val fallbackKeyInfo = remember(feedType) {
      "amity_v4_private_user_feed_info"
  }
  ```

- [ ] **Step 3: Verify build compiles**

  Run: `./gradlew :social-compose:compileDebugKotlin`
  Expected: BUILD SUCCESSFUL

- [ ] **Step 4: Commit**

  ```bash
  git add social-compose/src/main/java/com/amity/socialcloud/uikit/community/compose/ui/components/feed/user/AmityBlockedUserFeed.kt \
          social-compose/src/main/java/com/amity/socialcloud/uikit/community/compose/ui/components/feed/user/AmityPrivateUserFeed.kt
  git commit -m "refactor: consolidate blocked/private user feed config element IDs"
  ```

---

## Task 9: Final Build Verification and Push

- [ ] **Step 1: Full build verification**

  Run: `./gradlew :social-compose:compileDebugKotlin :common-compose:compileDebugKotlin`
  Expected: BUILD SUCCESSFUL

- [ ] **Step 2: Push to remote**

  ```bash
  git push origin ws/feat/localization-alignment
  ```
  Expected: pushed successfully

- [ ] **Step 3: Confirm PR #623 is up-to-date**

  The PR at https://github.com/AmityCo/Amity-Social-Cloud-UIKit-Android/pull/623 should show all new commits.
