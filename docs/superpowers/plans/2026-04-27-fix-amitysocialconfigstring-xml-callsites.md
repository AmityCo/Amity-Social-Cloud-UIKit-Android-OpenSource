# Fix amitySocialConfigString XML-key Callsites Implementation Plan

> **For agentic workers:** REQUIRED SUB-SKILL: Use superpowers:subagent-driven-development (recommended) or superpowers:executing-plans to implement this plan task-by-task. Steps use checkbox (`- [ ]`) syntax for tracking.

**Goal:** Fix 66 callsites across 26 files that pass XML-style keys (`amity_v4_*`, `amity_social_*`) to `amitySocialConfigString()` / `amitySocialString()`, replacing each with its correct dot-notation master key already in the registry.

**Architecture:** Callsite-only changes — no new strings.xml entries needed, no registry changes. The dot-notation equivalents already exist and have correct string values. The bug is purely that callsites use wrong key format which misses the registry lookup and falls back to displaying the raw key string.

**Tech Stack:** Kotlin, social-compose module, `AmitySocialStrings.kt` registry, `amitySocialConfigString()` / `amitySocialString()` accessor functions.

---

## Complete Key Mapping Reference

| XML key (old) | Dot-notation key (correct) |
|---|---|
| `amity_close_community` | `social.button.close.community` |
| `amity_close_community_description` | `social.label.close.community.description` |
| `amity_leave_community` | `social.button.leave.community` |
| `amity_social_edit_profile` | `social.button.edit.profile` |
| `amity_social_members` | `social.button.members` |
| `amity_v4_change_thumbnail` | `social.button.change.thumbnail` |
| `amity_v4_community_add_member_button` | `social.button.invite` |
| `amity_v4_community_setting_notifications` | `social.notification.title.notifications` |
| `amity_v4_community_setting_pending_invitations` | `social.label.community.pending.invitations.title` |
| `amity_v4_community_setting_post_permission` | `social.permission.title.post.permissions` |
| `amity_v4_community_setting_story_setting` | `social.label.title.story.comments` |
| `amity_v4_community_setup_about_title` | `social.placeholder.user.profile.about.placeholder` |
| `amity_v4_community_setup_camera_button` | `social.button.community.setup.camera.button` |
| `amity_v4_community_setup_categories_title` | `social.label.community.setup.categories.title` |
| `amity_v4_community_setup_create_button` | `social.button.setup.create.button` |
| `amity_v4_community_setup_image_button` | `social.button.community.setup.image.button` |
| `amity_v4_community_setup_invite_members_description` | `social.label.community.setup.invite.members.description` |
| `amity_v4_community_setup_invite_members_title` | `social.label.community.setup.invite.members.title` |
| `amity_v4_community_setup_membership_desc` | `social.label.community.setup.membership.desc` |
| `amity_v4_community_setup_membership_sub_desc` | `social.label.community.setup.membership.sub.desc` |
| `amity_v4_community_setup_name_title` | `social.label.community.setup.name.title` |
| `amity_v4_community_setup_privacy_private_and_hidden_description` | `social.label.community.setup.privacy.private.and.hidden.description` |
| `amity_v4_community_setup_privacy_private_and_hidden_title` | `social.label.community.setup.privacy.private.and.hidden.title` |
| `amity_v4_community_setup_privacy_private_and_visible_description` | `social.label.community.setup.privacy.private.and.visible.description` |
| `amity_v4_community_setup_privacy_private_and_visible_title` | `social.label.community.setup.privacy.private.and.visible.title` |
| `amity_v4_community_setup_privacy_public_description` | `social.label.community.setup.privacy.public.description` |
| `amity_v4_community_setup_privacy_public_title` | `social.label.community.setup.privacy.public.title` |
| `amity_v4_community_setup_privacy_title` | `social.label.community.setup.privacy.title` |
| `amity_v4_delete_thumbnail` | `social.button.delete.thumbnail` |
| `amity_v4_edit_user_about_hint` | `social.placeholder.edit.user.about.hint` |
| `amity_v4_edit_user_about_title` | `social.placeholder.user.profile.about.placeholder` |
| `amity_v4_edit_user_display_name_hint` | `social.label.edit.user.display.name.title` |
| `amity_v4_edit_user_display_name_title` | `social.label.edit.user.display.name.title` |
| `amity_v4_edit_user_save_button` | `social.button.edit.user.save.button` |
| `amity_v4_livestream_add_thumbnail` | `social.button.livestream.add.thumbnail` |
| `amity_v4_livestream_end_live` | `social.status.livestream.end.live` |
| `amity_v4_livestream_terminated_ok` | `social.button.livestream.terminated.ok` |
| `amity_v4_pending_join_accept_button` | `social.button.accept.button` |
| `amity_v4_pending_join_decline_button` | `social.button.pending.post.decline.button` |
| `amity_v4_pending_post_accept_button` | `social.button.accept.button` |
| `amity_v4_pending_post_decline_button` | `social.button.pending.post.decline.button` |
| `amity_v4_post_composer_create_button` | `social.button.post.composer.create.button` |
| `amity_v4_post_composer_edit_title` | `social.label.post.composer.edit.title` |
| `amity_v4_post_content_comment_button` | `social.button.post.content.comment.button` |
| `amity_v4_post_content_moderator_badge` | `social.status.post.content.moderator.badge` |
| `amity_v4_select_event_target_title` | `social.label.select.event.target.title` |
| `amity_v4_select_livestream_target_my_timeline` | `social.button.select.poll.target.my.timeline` |
| `amity_v4_select_livestream_target_title` | `social.status.select.livestream.target.title` |
| `amity_v4_select_poll_target_my_timeline` | `social.button.select.poll.target.my.timeline` |
| `amity_v4_select_poll_target_title` | `social.label.select.poll.target.title` |
| `amity_v4_select_post_target_my_timeline` | `social.button.select.poll.target.my.timeline` |
| `amity_v4_select_post_target_title` | `social.label.select.poll.target.title` |
| `amity_v4_select_story_target_title` | `social.label.select.story.target.title` |
| `amity_v4_social_home_clips_button` | `social.button.social.home.clips.button` |
| `amity_v4_social_home_communities_button` | `social.button.social.home.communities.button` |
| `amity_v4_social_home_create_community` | `social.button.setup.create.button` |
| `amity_v4_social_home_create_post_button` | `social.button.post.composer.create.button` |
| `amity_v4_social_home_create_story_button` | `social.button.story` |
| `amity_v4_social_home_empty_description` | `social.label.find.or.create.community` |
| `amity_v4_social_home_empty_title` | `social.empty_state.social.home.empty.title` |
| `amity_v4_social_home_events_button` | `social.button.social.home.events.button` |
| `amity_v4_social_home_explore_button` | `social.button.explore.community.button` |
| `amity_v4_social_home_header_label` | `social.label.social.home.header.label` |
| `amity_v4_social_home_newsfeed_button` | `social.button.social.home.newsfeed.button` |
| `amity_v4_user_profile_follower` | `social.button.user.profile.follower` |
| `amity_v4_user_profile_following` | `social.button.user.profile.following` |
| `amity_social_notification_everyone` | `social.notification.notification.everyone` |
| `amity_social_notification_only_moderator` | `social.notification.notification.only.moderator` |
| `amity_social_notification_off` | `social.notification.notification.off` |

> **Note on `amity_v4_edit_user_display_name_title`:** Value "Display name" matches `social.label.edit.user.display.name.title`. This is correct — both are "Display name" but semantically title vs hint. Use `social.label.edit.user.display.name.title` (confirm it exists in registry, or use `social.label.edit.user.display.name.title` as fallback).

---

## Files to Change

| File | XML keys to fix | Count |
|---|---|---|
| `social-compose/.../community/setting/AmityCommunitySettingPage.kt` | close_community, leave_community, amity_social_edit_profile, amity_social_members, amity_v4_community_setting_* | 9 |
| `social-compose/.../community/setting/notifications/AmityCommunityNotificationSettingDataType.kt` | amity_social_notification_* (enum values) | 3 |
| `social-compose/.../community/setup/AmityCommunitySetupPage.kt` | amity_v4_community_setup_* | ~15 |
| `social-compose/.../community/membership/element/AmityCommunityAddMemberElement.kt` | amity_v4_community_add_member_button | 1 |
| `social-compose/.../community/pending/elements/AmityPendingJoinRequestComponent.kt` | amity_v4_pending_join_* | 2 |
| `social-compose/.../community/pending/elements/AmityPendingPostActionRow.kt` | amity_v4_pending_post_* | 2 |
| `social-compose/.../user/edit/AmityEditUserProfilePage.kt` | amity_v4_edit_user_* | 5 |
| `social-compose/.../user/profile/components/AmityUserProfileHeaderComponent.kt` | amity_v4_user_profile_* | 2 |
| `social-compose/.../post/composer/AmityPostComposerPage.kt` | amity_v4_post_composer_* | 2 |
| `social-compose/.../post/composer/components/AmityDetailedMediaAttachmentComponent.kt` | amity_v4_post_composer_camera/image/video/file_button (via switch) | 4 |
| `social-compose/.../post/detail/elements/AmityPostModeratorBadge.kt` | amity_v4_post_content_moderator_badge | 1 |
| `social-compose/.../post/detail/elements/AmityPostEngagementView.kt` | amity_v4_post_content_comment_button | 1 |
| `social-compose/.../target/post/AmityPostTargetSelectionPage.kt` | amity_v4_select_post_target_* | 2 |
| `social-compose/.../target/livestream/AmityLivestreamPostTargetSelectionPage.kt` | amity_v4_select_livestream_target_* | 2 |
| `social-compose/.../target/event/AmityEventTargetSelectionPage.kt` | amity_v4_select_event_target_title | 1 |
| `social-compose/.../target/story/AmityStoryTargetSelectionPage.kt` | amity_v4_select_story_target_title | 1 |
| `social-compose/.../target/poll/AmityPollTargetSelectionPage.kt` | amity_v4_select_poll_target_* | 2 |
| `social-compose/.../livestream/errorhandling/AmityLivestreamTerminatedPage.kt` | amity_v4_livestream_terminated_ok | 1 |
| `social-compose/.../livestream/create/element/AmityEditThumbnailSheet.kt` | amity_v4_change_thumbnail, amity_v4_delete_thumbnail | 2 |
| `social-compose/.../livestream/create/AmityCreateLivestreamPage.kt` | amity_v4_livestream_add_thumbnail, amity_v4_livestream_end_live | 2 |
| `social-compose/.../socialhome/AmitySocialHomePage.kt` | amity_v4_social_home_* | ~6 |
| `social-compose/.../socialhome/components/AmitySocialHomeTopNavigationComponent.kt` | amity_v4_social_home_header_label | 1 |
| `social-compose/.../socialhome/components/AmityCreatePostMenuComponent.kt` | amity_v4_social_home_create_* | 2 |
| `social-compose/.../socialhome/elements/AmityCreateCommunityCard.kt` | amity_v4_social_home_create_community, amity_v4_social_home_empty_* | 3 |
| `social-compose/.../community/setup/elements/AmityMediaImageSelectionSheet.kt` | amity_v4_community_setup_camera_button, amity_v4_community_setup_image_button | 2 |
| `social-compose/.../socialhome/components/AmityEmptyNewsFeedComponent.kt` | amity_v4_social_home_explore_button, amity_v4_social_home_empty_* | 2 |

---

## Task 1: Fix AmityCommunitySettingPage.kt

**Files:**
- Modify: `social-compose/src/main/java/com/amity/socialcloud/uikit/community/compose/community/setting/AmityCommunitySettingPage.kt`

- [ ] **Step 1: Apply replacements**

  Replace each callsite per mapping table:
  ```kotlin
  // line ~243
  title = amitySocialConfigString("amity_social_edit_profile")
  // → 
  title = amitySocialConfigString("social.button.edit.profile")

  // line ~279
  title = amitySocialConfigString("amity_social_members")
  // →
  title = amitySocialConfigString("social.button.members")

  // line ~314
  title = amitySocialConfigString("amity_v4_community_setting_pending_invitations")
  // →
  title = amitySocialConfigString("social.label.community.pending.invitations.title")

  // line ~363
  title = amitySocialConfigString("amity_v4_community_setting_post_permission")
  // →
  title = amitySocialConfigString("social.permission.title.post.permissions")

  // line ~400
  title = amitySocialConfigString("amity_v4_community_setting_story_setting")
  // →
  title = amitySocialConfigString("social.label.title.story.comments")

  // line ~454
  title = amitySocialConfigString("amity_v4_community_setting_notifications")
  // →
  title = amitySocialConfigString("social.notification.title.notifications")

  // line ~505
  text = amitySocialConfigString("amity_leave_community")
  // →
  text = amitySocialConfigString("social.button.leave.community")

  // line ~541
  text = amitySocialConfigString("amity_close_community")
  // →
  text = amitySocialConfigString("social.button.close.community")

  // line ~559
  text = amitySocialConfigString("amity_close_community_description")
  // →
  text = amitySocialConfigString("social.label.close.community.description")
  ```

- [ ] **Step 2: Verify no XML keys remain in this file**

  Run: `grep 'amity_v4_\|amity_social_\|amity_leave_\|amity_close_' social-compose/src/main/java/com/amity/socialcloud/uikit/community/compose/community/setting/AmityCommunitySettingPage.kt`
  
  Expected: empty output

- [ ] **Step 3: Compile check**

  Run: `./gradlew :social-compose:compileDebugKotlin 2>&1 | tail -5`
  
  Expected: `BUILD SUCCESSFUL`

---

## Task 2: Fix AmityCommunityNotificationSettingDataType.kt (enum)

**Files:**
- Modify: `social-compose/src/main/java/com/amity/socialcloud/uikit/community/compose/community/setting/notifications/AmityCommunityNotificationSettingDataType.kt`

- [ ] **Step 1: Apply replacements**

  ```kotlin
  // Current (broken)
  EVERYONE("amity_social_notification_everyone"),
  ONLY_MODERATOR("amity_social_notification_only_moderator"),
  OFF("amity_social_notification_off");

  // Fixed
  EVERYONE("social.notification.notification.everyone"),
  ONLY_MODERATOR("social.notification.notification.only.moderator"),
  OFF("social.notification.notification.off");
  ```

- [ ] **Step 2: Verify**

  Run: `grep 'amity_social_notification' social-compose/src/main/java/com/amity/socialcloud/uikit/community/compose/community/setting/notifications/AmityCommunityNotificationSettingDataType.kt`
  
  Expected: empty

- [ ] **Step 3: Compile check**

  Run: `./gradlew :social-compose:compileDebugKotlin 2>&1 | tail -5`
  
  Expected: `BUILD SUCCESSFUL`

---

## Task 3: Fix AmityCommunitySetupPage.kt

**Files:**
- Modify: `social-compose/src/main/java/com/amity/socialcloud/uikit/community/compose/community/setup/AmityCommunitySetupPage.kt`

- [ ] **Step 1: Find all XML-key callsites in this file**

  Run: `grep -n 'amitySocialConfigString\|amitySocialString' social-compose/src/main/java/com/amity/socialcloud/uikit/community/compose/community/setup/AmityCommunitySetupPage.kt | grep '"amity_'`

- [ ] **Step 2: Apply replacements per mapping table**

  Key replacements needed:
  - `"amity_v4_community_setup_create_title"` → `"social.label.community.setup.create.title"`
  - `"amity_v4_community_setup_create_button"` → `"social.button.setup.create.button"`
  - `"amity_v4_community_setup_edit_button"` (if present) → `"social.button.community.setup.edit.button"` (verify in registry first)
  - `"amity_v4_community_setup_name_title"` → `"social.label.community.setup.name.title"`
  - `"amity_v4_community_setup_about_title"` → `"social.placeholder.user.profile.about.placeholder"`
  - `"amity_v4_community_setup_categories_title"` → `"social.label.community.setup.categories.title"`
  - `"amity_v4_community_setup_privacy_title"` → `"social.label.community.setup.privacy.title"`
  - `"amity_v4_community_setup_privacy_public_title"` → `"social.label.community.setup.privacy.public.title"`
  - `"amity_v4_community_setup_privacy_public_description"` → `"social.label.community.setup.privacy.public.description"`
  - `"amity_v4_community_setup_privacy_private_and_visible_title"` → `"social.label.community.setup.privacy.private.and.visible.title"`
  - `"amity_v4_community_setup_privacy_private_and_visible_description"` → `"social.label.community.setup.privacy.private.and.visible.description"`
  - `"amity_v4_community_setup_privacy_private_and_hidden_title"` → `"social.label.community.setup.privacy.private.and.hidden.title"`
  - `"amity_v4_community_setup_privacy_private_and_hidden_description"` → `"social.label.community.setup.privacy.private.and.hidden.description"`
  - `"amity_v4_community_setup_membership_desc"` → `"social.label.community.setup.membership.desc"`
  - `"amity_v4_community_setup_membership_sub_desc"` → `"social.label.community.setup.membership.sub.desc"`
  - `"amity_v4_community_setup_invite_members_title"` → `"social.label.community.setup.invite.members.title"`
  - `"amity_v4_community_setup_invite_members_description"` → `"social.label.community.setup.invite.members.description"`
  - `"amity_v4_community_setup_camera_button"` → `"social.button.community.setup.camera.button"`
  - `"amity_v4_community_setup_image_button"` → `"social.button.community.setup.image.button"`
  - `"amity_v4_community_setup_about_optional_title"` → look up registry: `grep 'about_optional\|optional_title' AmitySocialStrings.kt`

  > For any key not in the mapping table: grep registry with the suffix portion, e.g. `grep 'setup_edit_button\|community.setup.edit' AmitySocialStrings.kt`

- [ ] **Step 3: Verify no XML keys remain**

  Run: `grep -n '"amity_v4_\|"amity_social_' social-compose/src/main/java/com/amity/socialcloud/uikit/community/compose/community/setup/AmityCommunitySetupPage.kt`
  
  Expected: empty

- [ ] **Step 4: Compile check**

  Run: `./gradlew :social-compose:compileDebugKotlin 2>&1 | tail -5`
  
  Expected: `BUILD SUCCESSFUL`

---

## Task 4: Fix remaining 23 files (batch)

**Files:** All other 23 files listed in the Files to Change table above.

Apply the mapping table to each file. For each file:

1. Run `grep -n '"amity_v4_\|"amity_social_\|"amity_leave_\|"amity_close_' <file>` to see the exact callsites
2. Apply the replacement from the mapping table
3. Verify no XML keys remain in that file

**Specific notes per file:**

**`AmityDetailedMediaAttachmentComponent.kt`** — uses switch on component key, not direct string:
```kotlin
// Current
"camera_button" -> "amity_v4_post_composer_camera_button"
"image_button" -> "amity_v4_post_composer_image_button"
"video_button" -> "amity_v4_post_composer_video_button"
"file_button" -> "amity_v4_post_composer_file_button"
```
Find dot-notation equivalents for each:
- `amity_v4_post_composer_camera_button` → grep registry: `grep 'composer_camera\|composer.camera' AmitySocialStrings.kt`
- `amity_v4_post_composer_image_button` → grep registry similarly
- `amity_v4_post_composer_video_button` → grep registry
- `amity_v4_post_composer_file_button` → grep registry

**`AmityUserProfileHeaderComponent.kt`**:
- `"amity_v4_user_profile_following"` → `"social.button.user.profile.following"`
- `"amity_v4_user_profile_follower"` → `"social.button.user.profile.follower"`

After all 23 files are done:

- [ ] **Final verify: zero XML-style keys remain**

  Run:
  ```bash
  grep -rn 'amitySocialConfigString\|amitySocialString\b' --include="*.kt" social-compose/src/main/java \
    | grep '"amity_' \
    | grep -v 'import\|AmitySocialStrings\|AmitySocialThaiStrings\|fun amity'
  ```
  Expected: empty output

- [ ] **Compile check**

  Run: `./gradlew :social-compose:compileDebugKotlin 2>&1 | tail -5`
  
  Expected: `BUILD SUCCESSFUL`

---

## Task 5: Commit and push

- [ ] **Step 1: Commit**

  ```bash
  git add -A
  git commit -m "fix(localization): replace XML-style keys in amitySocialConfigString callsites with dot-notation master keys"
  ```

- [ ] **Step 2: Push**

  ```bash
  git push
  ```

- [ ] **Step 3: Open PR**

  ```bash
  gh pr create \
    --base feature/social-common-localization \
    --title "fix(localization): replace XML-style keys in amitySocialConfigString callsites with dot-notation master keys" \
    --body "Fix 66 callsites across 26 files showing raw key strings in UI instead of actual text. Root cause: amitySocialConfigString() uses dot-notation registry lookup but callsites were passing XML-style keys (amity_v4_*, amity_social_*) that were removed from strings.xml during the localization rename (5453ba39a). All dot-notation equivalents already exist in registry — callsite-only changes."
  ```
