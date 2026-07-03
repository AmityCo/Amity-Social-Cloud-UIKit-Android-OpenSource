# Android Chat-Compose — Missing Features vs Flutter

## Status Legend
- ✅ Fully implemented
- ⚠️ Partial / API only (no UI)
- ❌ Missing

---

## 1. Chat Home Page

| # | Feature | Status | Notes |
|---|---------|--------|-------|
| 1.1 | Three-Tab Layout (All/Direct/Groups) | ✅ | |
| 1.2 | App Bar (search, create, options menu) | ✅ | |
| 1.3 | Channel List with pagination | ✅ | |
| 1.4 | Channel List Item layout | ✅ | |
| 1.5 | Avatar Display (group/direct) | ✅ | |
| 1.6 | Display Name (group with count / direct other user) | ✅ | |
| 1.7 | Last Message Preview (text/image/video/deleted) | ✅ | |
| 1.8 | Timestamp (relative format) | ✅ | |
| 1.9 | Unread Count Badge | ✅ | Shows "99+" for >99 |
| 1.9 | Mention Badge (@) | ✅ | 24dp circle with @ icon, shown left of unread badge |
| 1.10 | Swipe-to-Archive | ⚠️ | UI gesture ready; SDK `archiveChannel()` not yet in Android SDK 7.19.0 |
| 1.11 | Tap → open chat | ✅ | |
| 1.12 | Push Notification Disabled Banner | ❌ | No inline yellow banner |
| 1.13 | Loading skeleton | ✅ | |
| 1.13 | Empty state | ✅ | |

---

## 2. Direct (1:1) Chat Page

| # | Feature | Status | Notes |
|---|---------|--------|-------|
| 2.1 | Page with channelId parameter | ✅ | |
| 2.2 | Header with other user's name & avatar | ✅ | |
| 2.3 | Reverse layout message list with pagination | ✅ | |
| 2.4 | New Message Floating Notification (when scrolled up) | ✅ | Shows sender avatar + preview pill |
| 2.5 | Scroll-to-Latest FAB | ✅ | 40×40 circle with down arrow |
| 2.6 | Mark as Read | ✅ | |
| 2.7 | Block/Unblock User | ⚠️ | `_isUserBlocked` state exists, no UI to block/unblock |
| 2.8 | Per-channel Notification Toggle | ✅ | |

---

## 3. Group Chat Page

| # | Feature | Status | Notes |
|---|---------|--------|-------|
| 3.1 | Page with channelId | ✅ | |
| 3.2 | Header with group name + settings icon | ✅ | |
| 3.3 | Message list (same as direct) | ✅ | |
| 3.4 | Moderator detection | ✅ | Checks permission for mute/settings |

---

## 4. Message Bubbles

| # | Feature | Status | Notes |
|---|---------|--------|-------|
| 4.1 | Text Messages (own/other alignment + colors) | ✅ | |
| 4.1 | Mention display (bold primary text) | ✅ | Live chat + conversation/group |
| 4.1 | Link detection & preview | ✅ | URL detection + OGP link preview card |
| 4.1 | See More / Full Text expand | ✅ | Inline expand with "See More" divider row |
| 4.1 | Edited indicator "(Edited)" | ✅ | |
| 4.2 | Image Messages (render + upload progress) | ✅ | Renders images; no upload progress/cancel UI |
| 4.3 | Video Messages (thumbnail + play) | ✅ | Renders placeholder; sending via gallery picker |
| 4.4 | Deleted Messages | ✅ | Shows "Message deleted" |
| 4.5 | Custom/System Messages | ❌ | Not handled |

---

## 5. Date Separators

| # | Feature | Status | Notes |
|---|---------|--------|-------|
| 5 | Inline date separator between messages | ✅ | Same format as Flutter |

---

## 6. Sender Info (Group Chat)

| # | Feature | Status | Notes |
|---|---------|--------|-------|
| 6.1 | Avatar (32dp) | ✅ | |
| 6.2 | Display name above bubble | ✅ | |
| 6.1 | Moderator badge on avatar | ✅ | 16dp primary circle + shield icon on avatar in group chat |

---

## 7. Reply / Quote Messages

| # | Feature | Status | Notes |
|---|---------|--------|-------|
| 7 | Reply display above bubble | ✅ | |
| 7 | Reply mode in composer | ✅ | |
| 7 | Localized reply strings | ✅ | |

---

## 8. Message Reactions

| # | Feature | Status | Notes |
|---|---------|--------|-------|
| 8.1 | Reaction selection (long press) | ✅ | Live chat + conversation/group |
| 8.2 | Reaction display (bubble with count) | ✅ | Live chat + conversation/group |
| 8 | Reactions in conversation/group chat | ✅ | Reaction picker, preview, and reaction list all wired |

---

## 9. Message Options (Long Press)

| # | Feature | Status | Notes |
|---|---------|--------|-------|
| 9 | Reply | ✅ | |
| 9 | Edit (own text messages) | ✅ | |
| 9 | Copy | ✅ | |
| 9 | Delete (own messages) | ✅ | |
| 9 | Report/Flag | ✅ | |
| 9 | Unreport | ✅ | |
| 9 | Save Image to gallery | ✅ | Downloads LARGE URL to MediaStore |
| 9 | Save Video to storage | ⚠️ | Image save implemented; video save needs streaming download |

---

## 10. Message Composer

| # | Feature | Status | Notes |
|---|---------|--------|-------|
| 10.1 | Text input (multiline) | ✅ | |
| 10.2 | Send button (enabled/disabled) | ✅ | |
| 10.3 | Image/Video attachment picker | ✅ | Media toggle + gallery picker |
| 10.4 | @Mention system | ✅ | Live chat + group chat; conversation composers simpler |
| 10.5 | Reply mode panel | ✅ | |
| 10.6 | Edit mode panel | ✅ | |
| 10.7 | Send text message | ✅ | |
| 10.8 | Send image/video message | ✅ | Image + video sending via gallery picker with mime detection |
| 10.9 | Camera capture | ✅ | Camera integration with permission handling |
| 10.10 | Error handling (ban word, link whitelist) | ❌ | No error-specific UI |

---

## 11. Message Report / Flag

| # | Feature | Status | Notes |
|---|---------|--------|-------|
| 11.1 | Report bottom sheet with reasons | ✅ | Full page with radio buttons |
| 11.2 | All 9 report reasons | ✅ | |
| 11.3 | Flag/Unflag SDK | ✅ | |

---

## 12. Message Delivery States

| # | Feature | Status | Notes |
|---|---------|--------|-------|
| 12 | SYNCED (normal) | ✅ | |
| 12 | SYNCING ("Sending") | ✅ | Shows "Sending..." text |
| 12 | UPLOADING (progress + cancel) | ⚠️ | Shows "Sending..." text; no progress bar or cancel |
| 12 | FAILED (red error, tap to resend) | ✅ | Red icon + "Failed" text; onResend callback ready |

---

## 13. Create Direct Conversation

| # | Feature | Status | Notes |
|---|---------|--------|-------|
| 13.1 | Full-screen search page | ✅ | |
| 13.2 | User search with debounce | ✅ | |
| 13.3 | Tap user → open chat | ✅ | |

---

## 14. Create Group Chat

| # | Feature | Status | Notes |
|---|---------|--------|-------|
| 14.1 | Select Members (multi-select + search) | ✅ | |
| 14.2 | Configure Group (name, avatar, privacy) | ✅ | |
| 14.3 | Create via SDK | ✅ | |

---

## 15. Group Settings

| # | Feature | Status | Notes |
|---|---------|--------|-------|
| 15.1 | Settings page with tiles | ✅ | |
| 15.2 | Edit Group Profile | ✅ | |
| 15.2 | Members list | ✅ | |
| 15.2 | Add Members | ✅ | |
| 15.2 | Notifications | ✅ | Generic on/off only |
| 15.2 | Member Permissions (mute channel) | ✅ | Moderator UI to set Everyone/Mods Only |
| 15.2 | Banned Members | ✅ | Full banned members page with unban |
| 15.3 | Leave Group | ✅ | |

---

## 16. Edit Group Profile

| # | Feature | Status | Notes |
|---|---------|--------|-------|
| 16 | Edit group name | ✅ | |
| 16 | Change group avatar | ⚠️ | Avatar upload may be partial |

---

## 17. Group Member Management

| # | Feature | Status | Notes |
|---|---------|--------|-------|
| 17.1 | Member list (All Members + Moderators tabs) | ✅ | |
| 17.1 | Search members | ✅ | |
| 17.2 | Remove member | ✅ | |
| 17.2 | Promote to moderator | ✅ | |
| 17.2 | Demote moderator | ✅ | |
| 17.2 | Ban member | ✅ | With confirmation dialog |
| 17.2 | Unban member | ✅ | Via banned members page |
| 17.2 | Mute/Unmute member | ✅ | Toggle in member action bottom sheet |
| 17.2 | Report/Unreport user | ✅ | Toggle in member action bottom sheet |
| 17.3 | Add Members page | ✅ | |
| 17.4 | Banned Members page | ✅ | Full page with unban flow |

---

## 18. Notification Settings

| # | Feature | Status | Notes |
|---|---------|--------|-------|
| 18.1 | Group Notification Mode (Default/Silent/Subscribe) | ✅ | Radio button UI with three modes; SDK: `AmityCoreClient().notifications().channel(channelId)` |
| 18.2 | Member Messaging Permissions (Everyone/Mods Only) | ✅ | Via Member Permissions page |
| 18.3 | Per-channel user notification preference | ✅ | Enable/Disable toggle |

---

## 19. Search

| # | Feature | Status | Notes |
|---|---------|--------|-------|
| 19.1 | Search page | ✅ | Channel search only |
| 19.2 | Chats tab (channel name search) | ✅ | |
| 19.3 | Messages tab (message content search) | ❌ | |
| 19.4 | Jump to specific message from search | ❌ | |

---

## 20. Archive

| # | Feature | Status | Notes |
|---|---------|--------|-------|
| 20 | Archive page with archived channel list | ✅ | |
| 20 | Unarchive action | ✅ | |
| 20 | Swipe-to-archive on list items | ⚠️ | Swipe UI ready; blocked on SDK `archiveChannel()` |

---

## Summary of Missing Features

### High Priority (Core Chat UX)
1. **~~Image/Video sending~~** — ✅ Implemented: media toggle, gallery picker, camera capture
2. **~~Link preview~~** — ✅ Implemented: URL detection, clickable links, OGP preview card
3. **~~Message delivery states~~** — ✅ Implemented: Sending.../Failed indicators with resend
4. **~~Reactions in conversation/group~~** — ✅ Implemented: picker, preview, reaction list
5. **Message search** — can only search channel names, not message content
6. **~~Scroll-to-latest FAB~~** — ✅ Implemented: 40×40 circle with down arrow

### Medium Priority (Group Management)
7. **~~Banned members page~~** — ✅ Implemented: full page with unban flow
8. **~~Member permissions page~~** — ✅ Implemented: Everyone/Mods Only with muteChannel
9. **Group notification modes** — only on/off, not Default/Silent/Subscribe
10. **~~Ban/Mute/Report member actions~~** — ✅ Implemented: all 6 actions in bottom sheet
11. **~~Moderator badge on message bubbles~~** — ✅ Implemented: 16dp primary badge + shield on avatar

### Lower Priority (Polish & UX)
12. **~~Mention badge on list item~~** — ✅ Implemented: 24dp circle with @ icon from SDK `isMentioned()`
13. **Swipe-to-archive on list items** — ⚠️ Swipe gesture UI implemented; blocked on SDK `archiveChannel()`
14. **~~New message floating notification~~** — ✅ Implemented: sender avatar + preview pill
15. **Push notification disabled banner** — no inline warning
16. **~~See More / full text expand~~** — ✅ Implemented: inline expand with divider row
17. **~~Save image to device~~** — ✅ Implemented: downloads to gallery via MediaStore
18. **Block/unblock user UI** — state tracked but no UI
19. **~~Camera capture for messages~~** — ✅ Implemented: camera integration with permission handling
20. **Composer error handling UI** — no ban word / link whitelist feedback
21. **Jump to message from search** — no deep link to specific message
