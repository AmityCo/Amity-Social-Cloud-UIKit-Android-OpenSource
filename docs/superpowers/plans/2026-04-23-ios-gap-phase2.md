# iOS Gap Phase 2 — Localization Registration Implementation Plan

> **For agentic workers:** REQUIRED SUB-SKILL: Use superpowers:subagent-driven-development (recommended) or superpowers:executing-plans to implement this plan task-by-task. Steps use checkbox (`- [ ]`) syntax for tracking.

**Goal:** Register all 74 actionable iOS-only gap-needs-review keys in Android (`AmitySocialStrings.kt` / `AmityCommonStrings.kt` + `strings.xml`), wire 3 missing call sites (close/leave/unjoin success toasts), and keep the branch building clean.

**Architecture:** Two registry files (`AmitySocialStrings.kt`, `AmityCommonStrings.kt`) map dot-notation keys → R.string IDs. New R.string names must be the master key with dots replaced by underscores (no abbreviation). Type-b entries re-use existing R.string values. Format-string keys with `%@` use `%s` / `%d` in Android R.string. Platform-specific keys (iOS-Settings references) are skipped. The 3 call-site changes use the existing `DefaultAmitySocialStringProvider.getInstance().getString(key)` + `AmityUIKitSnackbar.publishSnackbarMessage(message)` pattern already present in both files.

**Tech Stack:** Kotlin, Jetpack Compose, Android string resources (R.string), `amitySocialString()` / `amityCommonString()` @Composable helpers, `DefaultAmitySocialStringProvider` for non-composable contexts.

**Branch / PR:** `ws/feat/localization-alignment` → PR #624

**Worktree:** `/Users/warakorn/Documents/GitHub/cleverden/frontend-agentic/worktree/localization-alignment`

---

## Task 1 — Add 2 common keys (register only)

**Files:**
- Modify: `common-compose/src/main/res/values/strings.xml`
- Modify: `common-compose/src/main/java/com/amity/socialcloud/uikit/common/localization/AmityCommonStrings.kt`

- [ ] **Step 1: Add 2 string resources to common strings.xml**

  Insert before the closing `</resources>` tag:
  ```xml
  <string name="common_button_required_indicator"> *</string>
  <string name="common_button_see_less">See less</string>
  ```

- [ ] **Step 2: Register the 2 keys in AmityCommonStrings.kt**

  Add inside `buildStringKeyMap()` (after existing entries):
  ```kotlin
  "common.button.required.indicator" to R.string.common_button_required_indicator,
  "common.button.see.less" to R.string.common_button_see_less,
  ```

- [ ] **Step 3: Verify build**

  ```
  ./gradlew :common-compose:compileDebugKotlin
  ```
  Expected: `BUILD SUCCESSFUL`

- [ ] **Step 4: Commit**

  ```bash
  git add common-compose/src/main/res/values/strings.xml \
          common-compose/src/main/java/com/amity/socialcloud/uikit/common/localization/AmityCommonStrings.kt
  git commit -m "feat: add common.button.required.indicator and see.less to common registry"
  ```

---

## Task 2 — Add ~74 social strings (strings.xml + registry)

All new R.string names = master key with dots replaced by underscores.

**Files:**
- Modify: `social-compose/src/main/res/values/strings.xml`
- Modify: `social-compose/src/main/java/com/amity/socialcloud/uikit/community/compose/localization/AmitySocialStrings.kt`

- [ ] **Step 1: Add all new string resources to social strings.xml**

  Insert before the closing `</resources>` tag:

  ```xml
  <!-- Community member counts / pending -->
  <string name="social_button_member_count_plural">%s members</string>
  <string name="social_button_member_count_singular">%s member</string>
  <string name="social_button_pending_request_singular">Pending request</string>
  <string name="social_button_setup_add_member_button">Add</string>
  <string name="social_label_invite_member_description">Invite people to join your community</string>
  <string name="social_label_no_post_to_review">No post to review</string>
  <string name="social_label_page_pending_post_title">Pending post</string>
  <string name="social_label_setup_privacy_public_description">Community is discoverable by anyone. Content is visible to anyone.</string>
  <string name="social_setting_setting_edit_profile">Community profile</string>

  <!-- Community success/progress toasts -->
  <string name="social_toast_member_promote_success_toast">Successfully promoted to moderator!</string>
  <string name="social_toast_member_reported_toast">Member reported.</string>
  <string name="social_toast_member_unreported_toast">Member unreported.</string>
  <string name="social_toast_setting_close_success">Successfully closed community!</string>
  <string name="social_toast_setting_closing_toast">Closing the community.</string>
  <string name="social_toast_setting_leave_success">Successfully left community!</string>
  <string name="social_toast_setting_leaving_toast">Leaving the community.</string>
  <string name="social_toast_unjoined_toast">You unjoined %s!</string>

  <!-- Community post review -->
  <string name="social_modal_post_sent_for_review_alert_title">Post sent for review</string>
  <string name="social_label_post_composer_post_update_sent_for_review_message">Your post update has been submitted to the pending list. It will be published once approved by the community moderator</string>
  <string name="social_label_post_composer_post_updates_sent_for_review_title">Post updates sent for review</string>

  <!-- Livestream / Co-host -->
  <string name="social_button_confirm_invite_cohost_title">Confirm invite co-host</string>
  <string name="social_modal_alert_cohost_leave_title">Leave live stream</string>
  <string name="social_status_live_stream_duration_label">LIVE %s</string>
  <string name="social_status_live_stream_ending_stream_title">Ending live stream</string>
  <string name="social_status_no_viewers_message">Viewers who join your livestream will appear here.</string>
  <string name="social_toast_co_host_left_stage_toast">Co-host left the stage.</string>
  <string name="social_toast_invitation_send_failed_toast">Failed to send invitation.</string>
  <string name="social_toast_left_backstage_toast">You have left the backstage.</string>
  <string name="social_toast_remove_co_host_failed_toast">Failed to remove co-host.</string>
  <string name="social_label_player_unavailable_message">Please try again later.</string>
  <string name="social_label_banned_message">Based on your previous activities, your account has been banned from all feeds.</string>

  <!-- Events -->
  <string name="social_button_event_detail_header_status_cancelled">Cancelled</string>
  <string name="social_button_event_info_event_address">Event address</string>
  <string name="social_button_event_info_see_more">...See more</string>
  <string name="social_button_event_platform">Platform</string>
  <string name="social_error_event_setup_create_ban_word_error">Your event wasn\'t created as it contains an inappropriate word.</string>
  <string name="social_error_event_setup_update_ban_word_error">Your event wasn\'t updated as it contains an inappropriate word.</string>
  <string name="social_modal_add_calendar_sheet_title">You\'ll be notified</string>
  <string name="social_toast_event_detail_header_update_attending_status_failed">Failed to update your attending status. Please try again.</string>

  <!-- Poll -->
  <string name="social_button_poll_duration_plural_days">%d days</string>
  <string name="social_button_poll_duration_singular_day">%d day</string>
  <string name="social_error_poll_option_char_limit_error">Poll option cannot exceed %d characters.</string>
  <string name="social_error_poll_post_create_ban_word_error">Your post wasn\'t posted as it contains an inappropriate word.</string>
  <string name="social_error_poll_question_char_limit_error">Poll question cannot exceed %d characters.</string>
  <string name="social_label_poll_duration_pick_date_and_time">Pick date and time</string>
  <string name="social_label_poll_post_title">Post title</string>

  <!-- Story / Clip -->
  <string name="social_button_delete_story_message">This story will be permanently deleted. You\'ll no longer be able to see and find this story</string>
  <string name="social_modal_clip_alert_unsupported_video_title">Unsupported video type</string>
  <string name="social_toast_crated_story_failed">Failed to share story</string>

  <!-- User Profile / Follow / Block -->
  <string name="social_button_user_profile_followings">Followings</string>
  <string name="social_label_user_block_message_format">%s won\'t be able to see posts and comments that you\'ve created. They won\'t be notified.</string>
  <string name="social_label_user_unblock_message_format">%s will now be able to see posts and comments that you\'ve created. They won\'t be notified.</string>

  <!-- Copy / Share -->
  <string name="social_button_copy_link">Copy link</string>
  <string name="social_label_copy_clip_link">Copy clip link</string>

  <!-- Report / Moderation -->
  <string name="social_label_report_thanks_message">Our moderators will review this content and take action if it violates our guidelines.</string>
  <string name="social_label_report_thanks_title">Thanks for your report.</string>
  <string name="social_toast_report_reason_success_toast_message">%s reported.</string>

  <!-- Product Tags -->
  <string name="social_label_product_tag_unavailable_message">Your post can still be published, but product tags will be removed.</string>
  <string name="social_label_product_tags_updated">Product tags have been updated.</string>

  <!-- Misc / General -->
  <string name="social_button_edit_tags">Edit tags</string>
  <string name="social_button_general_anonymous">Anonymous</string>
  <string name="social_button_media_processing">Processing media...</string>
  <string name="social_button_replying_to">Replying to</string>
  <string name="social_button_see_less">See less</string>
  <string name="social_label_max_upload_limit_message">You\'ve reached the upload limit of 10 %s</string>
  <string name="social_label_reach_mention_limit_title">Unable to mention user</string>
  <string name="social_notification_notification_tray_invited_to_join_separator"> invited you to join </string>
  <string name="social_permission_permission_required">Permission Required!!</string>
  <string name="social_permission_post_permission_admin_review_setting">Admin review post</string>
  <string name="social_placeholder_comment_text_field_placeholder">Say something nice...</string>
  <string name="social_placeholder_event_setup_event_details_placeholder">Share what this event is all about</string>
  <string name="social_placeholder_post_composer_body_clip_placeholder">What\'s going on? (optional)</string>
  <string name="social_placeholder_post_composer_body_placeholder">What\'s going on...</string>
  <string name="social_placeholder_search_community_user_placeholder">Search community and user</string>
  ```

- [ ] **Step 2: Register all keys in AmitySocialStrings.kt**

  Add inside `buildStringKeyMap()` after existing entries:

  ```kotlin
  // Community member counts / pending
  "social.button.member.count.plural" to R.string.social_button_member_count_plural,
  "social.button.member.count.singular" to R.string.social_button_member_count_singular,
  "social.button.pending.request.singular" to R.string.social_button_pending_request_singular,
  "social.button.setup.add.member.button" to R.string.social_button_setup_add_member_button,
  "social.label.invite.member.description" to R.string.social_label_invite_member_description,
  "social.label.no.post.to.review" to R.string.social_label_no_post_to_review,
  "social.label.page.pending.post.title" to R.string.social_label_page_pending_post_title,
  "social.label.setup.privacy.public.description" to R.string.social_label_setup_privacy_public_description,
  "social.setting.setting.edit.profile" to R.string.social_setting_setting_edit_profile,

  // Community success/progress toasts
  "social.toast.member.promote.success.toast" to R.string.social_toast_member_promote_success_toast,
  "social.toast.member.reported.toast" to R.string.social_toast_member_reported_toast,
  "social.toast.member.unreported.toast" to R.string.social_toast_member_unreported_toast,
  "social.toast.setting.close.success" to R.string.social_toast_setting_close_success,
  "social.toast.setting.closing.toast" to R.string.social_toast_setting_closing_toast,
  "social.toast.setting.leave.success" to R.string.social_toast_setting_leave_success,
  "social.toast.setting.leaving.toast" to R.string.social_toast_setting_leaving_toast,
  "social.toast.unjoined.toast" to R.string.social_toast_unjoined_toast,

  // Community post review
  "social.modal.post.sent.for.review.alert.title" to R.string.social_modal_post_sent_for_review_alert_title,
  "social.label.post.composer.post.update.sent.for.review_message" to R.string.social_label_post_composer_post_update_sent_for_review_message,
  "social.label.post.composer.post.updates.sent.for.review_title" to R.string.social_label_post_composer_post_updates_sent_for_review_title,

  // Community — type-b (point to existing R.string, no new XML needed)
  "social.label.invitation.no.longer.valid" to R.string.social_label_invitation_unavailable,
  "social.label.player.banned.title" to R.string.social_label_banned_title,
  "social.modal.join.community.sheet.join" to R.string.social_button_community_invitation_accept_button,
  "social.modal.post.sent.for.review.alert.message" to R.string.social_modal_dialog_post_pending_approval,
  "social.toast.accept.invitation.failed" to R.string.social_toast_community_invitation_fail_to_accept,
  "social.toast.decline.invitation.failed" to R.string.social_toast_community_invitation_fail_to_reject,    
  "social.toast.live.chat.demote.to.member.success.toast" to R.string.social_label_user_demoted,

  // Livestream / Co-host
  "social.button.confirm.invite.cohost.title" to R.string.social_button_confirm_invite_cohost_title,
  "social.modal.alert.cohost.leave.title" to R.string.social_modal_alert_cohost_leave_title,
  "social.status.live.stream.duration.label" to R.string.social_status_live_stream_duration_label,
  "social.status.live.stream.ending.stream.title" to R.string.social_status_live_stream_ending_stream_title,
  "social.status.no.viewers.message" to R.string.social_status_no_viewers_message,
  "social.toast.co.host.left.stage.toast" to R.string.social_toast_co_host_left_stage_toast,
  "social.toast.invitation.send.failed.toast" to R.string.social_toast_invitation_send_failed_toast,
  "social.toast.left.backstage.toast" to R.string.social_toast_left_backstage_toast,
  "social.toast.remove.co.host.failed.toast" to R.string.social_toast_remove_co_host_failed_toast,
  "social.label.player.unavailable.message" to R.string.social_label_player_unavailable_message,
  "social.label.banned.message" to R.string.social_label_banned_message,

  // Events
  "social.button.event.detail.header.status.cancelled" to R.string.social_button_event_detail_header_status_cancelled,
  "social.button.event.info.event.address" to R.string.social_button_event_info_event_address,
  "social.button.event.info.see.more" to R.string.social_button_event_info_see_more,
  "social.button.event.platform" to R.string.social_button_event_platform,
  "social.error.event.setup.create.ban.word.error" to R.string.social_error_event_setup_create_ban_word_error,
  "social.error.event.setup.update.ban.word.error" to R.string.social_error_event_setup_update_ban_word_error,
  "social.modal.add.calendar.sheet.title" to R.string.social_modal_add_calendar_sheet_title,
  "social.toast.event.detail.header.update.attending.status.failed" to R.string.social_toast_event_detail_header_update_attending_status_failed,

  // Poll
  "social.button.poll.duration.plural.days" to R.string.social_button_poll_duration_plural_days,
  "social.button.poll.duration.singular.day" to R.string.social_button_poll_duration_singular_day,
  "social.error.poll.option.char.limit.error" to R.string.social_error_poll_option_char_limit_error,
  "social.error.poll.post.create.ban.word.error" to R.string.social_error_poll_post_create_ban_word_error,
  "social.error.poll.question.char.limit.error" to R.string.social_error_poll_question_char_limit_error,
  "social.label.poll.duration.pick.date.and.time" to R.string.social_label_poll_duration_pick_date_and_time,
  "social.label.poll.post.title" to R.string.social_label_poll_post_title,

  // Story / Clip
  "social.button.delete.story.message" to R.string.social_button_delete_story_message,
  "social.modal.clip.alert.unsupported.video.title" to R.string.social_modal_clip_alert_unsupported_video_title,
  "social.toast.crated.story.failed" to R.string.social_toast_crated_story_failed,

  // User Profile / Follow / Block
  "social.button.user.profile.followings" to R.string.social_button_user_profile_followings,
  "social.label.user.block.message.format" to R.string.social_label_user_block_message_format,
  "social.label.user.unblock.message.format" to R.string.social_label_user_unblock_message_format,

  // Copy / Share
  "social.button.copy.link" to R.string.social_button_copy_link,
  "social.label.copy.clip.link" to R.string.social_label_copy_clip_link,

  // Report / Moderation
  "social.label.report.thanks.message" to R.string.social_label_report_thanks_message,
  "social.label.report.thanks.title" to R.string.social_label_report_thanks_title,
  "social.toast.report.reason.success.toast.message" to R.string.social_toast_report_reason_success_toast_message,

  // Product Tags
  "social.label.product.tags.updated" to R.string.social_label_product_tags_updated,

  // Misc / General
  "social.button.edit.tags" to R.string.social_button_edit_tags,
  "social.button.general.anonymous" to R.string.social_button_general_anonymous,
  "social.button.media.processing" to R.string.social_button_media_processing,
  "social.button.replying.to" to R.string.social_button_replying_to,
  "social.button.see.less" to R.string.social_button_see_less,
  "social.label.max.upload.limit.message" to R.string.social_label_max_upload_limit_message,
  "social.label.reach.mention.limit.title" to R.string.social_label_reach_mention_limit_title,
  "social.notification.notification.tray.invited.to.join.separator" to R.string.social_notification_notification_tray_invited_to_join_separator,
  "social.permission.permission.required" to R.string.social_permission_permission_required,
  "social.permission.post.permission.admin.review.setting" to R.string.social_permission_post_permission_admin_review_setting,
  "social.placeholder.comment.text.field.placeholder" to R.string.social_placeholder_comment_text_field_placeholder,
  "social.placeholder.event.setup.event.details.placeholder" to R.string.social_placeholder_event_setup_event_details_placeholder,
  "social.placeholder.post.composer.body.clip.placeholder" to R.string.social_placeholder_post_composer_body_clip_placeholder,
  "social.placeholder.post.composer.body.placeholder" to R.string.social_placeholder_post_composer_body_placeholder,
  "social.placeholder.search.community.user.placeholder" to R.string.social_placeholder_search_community_user_placeholder,
  ```

- [ ] **Step 3: Verify build**

  ```
  ./gradlew :social-compose:compileDebugKotlin
  ```
  Expected: `BUILD SUCCESSFUL`

- [ ] **Step 4: Commit**

  ```bash
  git add social-compose/src/main/res/values/strings.xml \
          social-compose/src/main/java/com/amity/socialcloud/uikit/community/compose/localization/AmitySocialStrings.kt
  git commit -m "feat: register 74 iOS gap-needs-review keys in social registry"
  ```

---

## Task 3 — Add 3 missing success toast call sites

**Files:**
- Modify: `social-compose/.../community/setting/AmityCommunitySettingPage.kt`
- Modify: `social-compose/.../community/membership/element/AmityCommunityJoinButton.kt`

### 3a — Close community success toast (`AmityCommunitySettingPage.kt` ~line 139)

- [ ] **Step 1: Find `CloseCommunitySuccess` block and add toast before `closePage()`**

  Before:
  ```kotlin
  AmityCommunitySettingUIState.CloseCommunitySuccess -> {
      context.closePage()
  }
  ```

  After:
  ```kotlin
  AmityCommunitySettingUIState.CloseCommunitySuccess -> {
      AmityUIKitSnackbar.publishSnackbarMessage(
          message = DefaultAmitySocialStringProvider.getInstance()
              .getString("social.toast.setting.close.success")
      )
      context.closePage()
  }
  ```

### 3b — Leave community success toast (`AmityCommunitySettingPage.kt` ~line 149)

- [ ] **Step 2: Find `LeaveCommunitySuccess` block and add toast before `closePage()`**

  Before:
  ```kotlin
  AmityCommunitySettingUIState.LeaveCommunitySuccess -> {
      context.closePage()
  }
  ```

  After:
  ```kotlin
  AmityCommunitySettingUIState.LeaveCommunitySuccess -> {
      AmityUIKitSnackbar.publishSnackbarMessage(
          message = DefaultAmitySocialStringProvider.getInstance()
              .getString("social.toast.setting.leave.success")
      )
      context.closePage()
  }
  ```

### 3c — Unjoin success toast (`AmityCommunityJoinButton.kt` — 2 spots)

Both call sites are where `val isSuccess = leaveCommunity(community)` is called and `!isSuccess` currently shows a failure toast. Add a success branch.

- [ ] **Step 3: Fix call site 1 (~line 245, inside coroutine in the "no approval required" leave flow)**

  Before:
  ```kotlin
  val isSuccess = leaveCommunity(community)
  isInProgress = false
  if (!isSuccess) {
      isJoined = true
      AmityUIKitSnackbar.publishSnackbarMessage(
          message = DefaultAmitySocialStringProvider.getInstance().getString("amity_social_leave_community_failed"),
      )
  }
  ```

  After:
  ```kotlin
  val isSuccess = leaveCommunity(community)
  isInProgress = false
  if (isSuccess) {
      AmityUIKitSnackbar.publishSnackbarMessage(
          message = DefaultAmitySocialStringProvider.getInstance()
              .getString("social.toast.unjoined.toast")
              .format(community.getDisplayName())
      )
  } else {
      isJoined = true
      AmityUIKitSnackbar.publishSnackbarMessage(
          message = DefaultAmitySocialStringProvider.getInstance().getString("amity_social_leave_community_failed"),
      )
  }
  ```

- [ ] **Step 4: Fix call site 2 (~line 288, inside `onConfirmation` of `AmityAlertDialog`)**

  Before:
  ```kotlin
  val isSuccess = leaveCommunity(community)
  isInProgress = false
  if (!isSuccess) {
      isPending = true
      AmityUIKitSnackbar.publishSnackbarMessage(
          message = DefaultAmitySocialStringProvider.getInstance().getString("amity_social_leave_community_failed"),
      )
  }
  ```

  After:
  ```kotlin
  val isSuccess = leaveCommunity(community)
  isInProgress = false
  if (isSuccess) {
      AmityUIKitSnackbar.publishSnackbarMessage(
          message = DefaultAmitySocialStringProvider.getInstance()
              .getString("social.toast.unjoined.toast")
              .format(community.getDisplayName())
      )
  } else {
      isPending = true
      AmityUIKitSnackbar.publishSnackbarMessage(
          message = DefaultAmitySocialStringProvider.getInstance().getString("amity_social_leave_community_failed"),
      )
  }
  ```

- [ ] **Step 5: Verify build**

  ```
  ./gradlew :social-compose:compileDebugKotlin
  ```
  Expected: `BUILD SUCCESSFUL`

- [ ] **Step 6: Commit**

  ```bash
  git add social-compose/src/main/java/com/amity/socialcloud/uikit/community/compose/community/setting/AmityCommunitySettingPage.kt \
          social-compose/src/main/java/com/amity/socialcloud/uikit/community/compose/community/membership/element/AmityCommunityJoinButton.kt
  git commit -m "feat: add close/leave/unjoin success toasts using iOS master keys"
  ```

---

## Task 4 — Final build + push

- [ ] **Step 1: Run full compile across both modules**

  ```
  ./gradlew :social-compose:compileDebugKotlin :common-compose:compileDebugKotlin
  ```
  Expected: `BUILD SUCCESSFUL`

- [ ] **Step 2: Push to remote**

  ```bash
  git push origin ws/feat/localization-alignment
  ```

---

## Skipped / Out of Scope

| Key | Reason |
|---|---|
| `social.label.event.detail.header.no.calendar.access` | Platform-specific — references iOS Settings app |
| `social.permission.camera.access.denied` | Already registered (AmitySocialStrings.kt line 1088); value correction out of scope |
| See Less UI wiring in `AmityExpandableTextView` | Key registered only; collapse behavior not wired |
| `social.toast.setting.closing.toast` / `social.toast.setting.leaving.toast` | Registered only; no in-flight progress handler exists to attach call sites to |

---

## Notes

- **`social.modal.join.community.sheet.join`** uses `R.string.social_button_community_invitation_accept_button` ("Join") — avoids cross-module R reference issues.
- **Format-string keys**: all `%@` (iOS) written as `%s` / `%d` in Android R.string values.
- **`social.toast.crated.story.failed`** — "crated" typo is intentional; matches the DLS master key exactly.
- **22 already-registered keys** (done in previous commits on this branch) are not listed here.
