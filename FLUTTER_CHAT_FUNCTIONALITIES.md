# Flutter Chat UIKit — Complete Functionality Reference

## 1. Chat Home Page

### 1.1 Three-Tab Layout
- **Tabs**: "All" | "Direct" | "Groups" (animated tab bar, 200ms transition)
- **All tab**: Shows both CONVERSATION (1:1) and COMMUNITY (group) channels
- **Direct tab**: CONVERSATION channels only
- **Groups tab**: COMMUNITY channels only
- Each tab backed by separate BLoC with channel type filter

### 1.2 App Bar
- **Title**: "Chat" (left-aligned)
- **Network Status**: "Waiting for network..." with spinner when offline (via `NetworkConnectivityBloc`)
- **Search Button**: Opens `AmitySearchChannelPage`
- **Create Menu (+)**: Popup with "Direct Chat" → create conversation; "Group Chat" → select group members
- **Options Menu (⋮)**: Popup with "Archived" → archived chats page

### 1.3 Channel List
- **Data Source**: `AmityChatClient.newChannelRepository().getChannels().types([...]).filter(AmityChannelFilter.MEMBER).excludeArchives(true).getLiveCollection()`
- **Pagination**: Infinite scroll — loads next page when scrolled to bottom
- **Item Height**: 82px per channel item
- **Real-time Updates**: Live collection stream

### 1.4 Channel List Item Layout
```
┌──────────────────────────────────────────────────┐
│ [Avatar]  [Display Name]           [Timestamp]   │
│  40x40    [Last message preview]   [Unread badge] │
│           [Preview line 2]         [Mention badge] │
└──────────────────────────────────────────────────┘
```

### 1.5 Avatar Display
- **Group (COMMUNITY)**: Channel avatar image (40x40, rounded corners), private lock badge if private, placeholder = colored background + group icon SVG
- **Direct (CONVERSATION)**: Other user's avatar OR first character colored circle, deleted user = special gray icon
- **SDK**: `channel.avatar?.getUrl(AmityImageSize.MEDIUM)`

### 1.6 Display Name
- **Group**: `"Group Name (member_count)"` — member count formatted compactly (e.g., "2.1K")
- **Direct**: Other user's `displayName`, fallback to "Unknown User Name"

### 1.7 Last Message Preview
| Message Type | Display |
|---|---|
| Text | Truncated text (max 2 lines) |
| Image | 🖼️ "Photo sent" + image icon |
| Video | 🎬 "Video" + video icon |
| File/Audio | "No preview" |
| Deleted | 🗑️ "Message deleted" + icon |
| No messages | "No messages yet" |
| Custom data | Raw JSON string |

### 1.8 Timestamp
- Relative format: "Today", "Yesterday", "Monday", or full date
- Right-aligned, light gray color

### 1.9 Unread Indicators (right side, priority order)
1. **Unread Count Badge**: Red pill, "99+" if > 99, otherwise raw count
2. **Mention Badge**: 24x24 circular @ icon when user is mentioned
3. Hidden when unreadCount == 0 and not mentioned
- **SDK**: `channel.unreadCount`, `channel.isMentioned`

### 1.10 Swipe Actions
- **Swipe left**: Archive/Unarchive toggle
- Archive confirmation toast: "Chat archived" / "Chat unarchived"
- Archive limit error: Dialog with "Archive Limit Reached"
- **SDK**: `.archiveChannel(channelId)` / `.unarchiveChannel(channelId)`

### 1.11 Tap Action
- CONVERSATION → opens `AmityChatPage(channelId, userId, userDisplayName, avatarUrl)`
- COMMUNITY → opens `AmityGroupChatPage(channelId)`

### 1.12 Push Notification Banner
- Yellow banner above list: "Chat notifications are disabled"
- Shows when global or chat-module notifications are disabled
- **SDK**: `AmityNotification().user().getSettings()`

### 1.13 Loading / Empty / Error States
- **Loading**: Skeleton shimmer view (6 items)
- **Empty**: "No chats yet" icon + "+ Create New" button
- **Error**: Standard error display

---

## 2. Direct (1:1) Chat Page

### 2.1 Page Parameters
- `channelId: String?` — channel ID
- `userId: String?` — other user's ID
- `userDisplayName: String?` — display name
- `avatarUrl: String?` — avatar URL
- `jumpToMessageId: String?` — jump to specific message
- `isJustCreated: bool` — newly created flag

### 2.2 Header
- Other user's avatar (tappable → user profile via behavior callback)
- Other user's display name
- Back button

### 2.3 Message List
- **Reverse layout**: Newest at bottom, reversed `ListView`
- **Pagination**: Load older messages when scroll reaches top (pixels ≤ -50), load newer when near bottom (pixels ≥ maxScrollExtent - 100)
- **Auto-scroll**: Scrolls to latest on new incoming message
- **Content overflow detection**: Toggles between forward/reverse UI on first overflow

### 2.4 New Message Notification
- Floating bubble when new message arrives while scrolled up
- Shows sender avatar + message preview
- Tappable to scroll to latest
- Auto-dismisses after timeout

### 2.5 Scroll-to-Latest Button
- Floating 40x40 circular button, bottom-right
- Only visible when scrolled up from bottom

### 2.6 Mark as Read
- `message.markRead()` on newest visible message

### 2.7 Block/Unblock User
- **SDK**: `AmityCoreClient.newUserRepository().relationship().blockUser(userId)` / `.unblockUser(userId)`

### 2.8 Notification Toggle (per channel)
- **SDK**: `AmityCoreClient().notifications().channel(channelId).enable()` / `.disable()` / `.getSettings()`

---

## 3. Group Chat Page

### 3.1 Page Parameters
- `channelId: String` (required)
- `jumpToMessageId: String?` — jump to specific message
- `isJustCreated: bool` — newly created flag

### 3.2 Header
- Group avatar + group name
- Settings icon → navigates to `AmityGroupSettingPage`
- Back button

### 3.3 Message List
- Same features as direct chat message list (section 2.3–2.6)
- Additionally shows sender display name above bubbles (for non-own messages)
- Moderator badge on moderator user avatars

### 3.4 Moderator Detection
- Checks `channel.getCurentUserRoles()` for moderator status

---

## 4. Message Bubbles

### 4.1 Text Messages
- **Own messages**: Right-aligned, primary color background
- **Other messages**: Left-aligned, light gray background
- **Link detection**: Auto-extracts URLs via `linkify` library
- **Link preview**: Thumbnail (40% width) + title/domain (60%) below text, tappable → opens URL
- **Mentions**: Bold primary-color text, extracted from `message.metadata['mentioned']`
- **See More**: Truncated at 5 lines (with links) or 10 lines (without), tappable → full text screen
- **Edited indicator**: "(Edited)" text if `message.editedAt != null`
- **Timestamp**: "HH:mm" format, positioned left of own / right of others

### 4.2 Image Messages
- Max 240px width/height, aspect ratio preserved
- Upload progress: Circular indicator (0-100%) + cancel button
- Sync states: SYNCED (normal), UPLOADING (progress + cancel), FAILED (red error, tappable to resend)
- Tap to view: Full-screen viewer with delete/save options
- Image dimension caching via `ImageInfoManager`

### 4.3 Video Messages
- 240x240 thumbnail with play button overlay
- Tap → full-screen `VideoMessagePlayer`
- Upload progress same as images
- Thumbnail: Remote `thumbnailImageFile?.fileUrl` or local via `FlutterVideoThumbnail`

### 4.4 Deleted Messages
- "🗑 This message was deleted" in bordered container
- No original content shown

### 4.5 Custom/System Messages
- Yellow background bubble
- Used for system notifications or special message types

---

## 5. Date Separators

- **Type**: Inline `ChatItem.date()` objects inserted into message list
- **Format**: Same year → "EEE, d MMM" (e.g., "Mon, 5 Apr"); Different year → "EEE, d MMM yyyy"
- **Logic**: Inserted when day boundary detected between consecutive messages (> 0 days difference)

---

## 6. Sender Info (Group Chat Only)

### 6.1 Avatar
- 32x32px (36x36 if moderator)
- Fallback: First character colored circle
- Deleted users: Special icon
- Moderator badge: Green circle in bottom-right corner
- Tappable via `AmityUIKit4Manager.behavior.messageBubbleBehavior.onAvatarTap()`

### 6.2 Display Name
- Shown above bubble (group chat, non-reply messages only)
- Max 10 chars with ellipsis
- Bold caption style

---

## 7. Reply / Quote Messages

- **Display**: Above message bubble with reply icon + parent message preview
- **Loading state**: Spinner while fetching parent
- **Deleted parent**: "You replied to deleted message"
- **Cache**: `ParentMessageCache()` singleton
- **Localized strings**: "You replied to yourself", "X replied to you", "You replied to X", etc.
- **Tap**: Opens parent in viewer/player

---

## 8. Message Reactions

### 8.1 Reaction Selection (Long Press)
- 6 emoji reactions in a row
- Drag-to-select with hover preview and tooltip
- Scale animation: 30px normal → 36px on hover
- Haptic feedback on long press

### 8.2 Reaction Display
- ReactionBubble at bottom of message
- Up to 3 reaction icons with 13px overlap + total count
- Border highlight if current user reacted
- Tappable to open full reaction list

### 8.3 SDK
- `message.react().addReaction(reactionName)`
- `message.react().removeReaction(reactionName)`
- `message.myReactions` for current user's reactions

---

## 9. Message Options (Long Press Menu)

Available actions (contextual):

| Action | Condition | Behavior |
|---|---|---|
| **Reply** | Any non-deleted message | Sets reply-to in composer |
| **Edit** | Own text messages, SYNCED state | Opens composer in edit mode |
| **Copy** | Text or custom messages | Copies to clipboard, shows "Message copied" toast |
| **Translate** | If enabled in config | Translates message text |
| **Share** | Any message | Shares content externally |
| **Delete** | Own messages | Confirmation dialog → `message.delete()` |
| **Report** | Others' messages | Opens report bottom sheet |
| **Unreport** | Already reported messages | Removes flag |
| **Save Image** | Image messages | Downloads to device gallery |
| **Save Video** | Video messages | Downloads to device storage |

---

## 10. Message Composer

### 10.1 Text Input
- Multiline (min 1 line, max 120px height)
- Character limit enforced by SDK (error 400000 = "message too long")
- Draft persistence: `MessageComposerCache()` singleton saves text between instances
- Auto-focus when replying

### 10.2 Send Button
- Disabled (grayed) when text is empty or editing with unchanged text
- Icons: `amity_ic_sent_message_button.svg` (enabled) / `_disable.svg` (disabled)

### 10.3 Media Toggle
- Button to show/hide media attachment picker
- Options: Image/Video picker, Camera capture

### 10.4 @Mention System
- Triggered by typing `@` character
- Suggestion overlay: shows channel members (max 2 rows)
- Stored as `AmityUserMentionMetadata` with userId, index, length
- Regex: `@[a-zA-Z0-9-\s]+`
- Max 30 mentions per message
- **@All mention**: Special `"all"` ID, uses `.mentionChannel()` SDK, gated by `mentionConfigurations?.isMentionAllEnabled`
- **SDK**: `AmityMentionMetadataCreator(mentionList).create()` → generates metadata

### 10.5 Reply Mode
- Panel above composer: "Replying to [UserName]" + text/image/video preview
- Close button to cancel
- Height: 62px
- Sets `parentId` on message creation

### 10.6 Edit Mode
- Panel: "Editing message" with close button (48px)
- Loads existing text + mention metadata into composer
- Preserves channel mentions from original
- **SDK**: `.editTextMessage(messageId).text(newText).metadata(preservedMetadata).update()`

### 10.7 Send Text Message
```
AmityChatClient.newMessageRepository()
  .createMessage(channelId)
  .text(text.trim())
  .parentId(replyId)           // if replying
  .metadata(mentionMetadata)   // if mentions
  .mentionUsers([userIds])     // if @user mentions
  .mentionChannel()            // if @all mention
  .send()
```

### 10.8 Send Image/Video
- Uses `file_picker` library, `FilePicker.platform.pickFiles()`
- File size limit: < 1GB
- MIME validation via `lookupMimeType()`
- **SDK**: `.image(uri).send()` or `.video(uri).send()`

### 10.9 Camera Capture
- Mode toggle: Image ↔ Video
- Flash toggle, camera switch (front/back)
- Video recording with elapsed timer "00:00:00"
- Image/video preview before sending
- Resolution: `ResolutionPreset.high`
- Permission: `MediaPermissionHandler().handleMediaPermissions()`

### 10.10 Error Handling
| Error | Display |
|---|---|
| `BAN_WORD_FOUND` | "Your message contains inappropriate word" |
| `LINK_NOT_IN_WHITELIST` | "Message wasn't sent as it contains a link that's not allowed" |
| `400000` (too long) | Alert dialog with title/description |

---

## 11. Message Report / Flag

### 11.1 UI Flow
- Bottom sheet (90% screen height)
- PageView: Page 1 = category selection, Page 2 = custom reason input (for "Others")

### 11.2 Report Reasons (`AmityContentFlagReasonType`)
- Community Guidelines
- Harassment or Bullying
- Self Harm or Suicide
- Violence or Threatening Content
- Selling Restricted Items
- Sexual Content or Nudity
- Spam or Scams
- False Information
- Others (custom text input)

### 11.3 SDK
- **Flag**: `AmityChatClient.newMessageRepository().flagMessage(messageId: messageId, reason: reason)`
- **Unflag**: `AmityChatClient.newMessageRepository().unflag(messageId)`
- **Check**: `message.isFlaggedByMe == true`

---

## 12. Message Delivery States

| State | Display | Behavior |
|---|---|---|
| `SYNCED` | Normal bubble | Delivered successfully |
| `SYNCING` | "Sending" text | In progress |
| `UPLOADING` | Progress indicator + cancel (X) button | Media upload |
| `FAILED` | Red error icon | Tappable to resend |

- Cancel upload: `AmityCoreClient.newFileRepository().cancelUpload(uploadId)`
- Failed resend: Delete + re-create message

---

## 13. Create Direct Conversation

### 13.1 UI
- Full-screen page: "New Conversation" header + close button
- Search input (min 3 chars, 300ms debounce)
- User list with pagination

### 13.2 Flow
1. Search for user by display name
2. Tap user → navigate to `AmityChatPage(channelId, userId, displayName, avatarUrl, isJustCreated: true)`
3. SDK creates conversation automatically if none exists

### 13.3 SDK
- **Search**: `AmityCoreClient.newUserRepository().searchUserByDisplayName(query).matchType(PARTIAL).sortBy(DISPLAY).getLiveCollection()`

---

## 14. Create Group Chat (2-Step Flow)

### 14.1 Step 1 — Select Members
- Page: "Select Members" header + close + "Next" button
- Horizontal scroll of selected member chips at top
- Search input + user list with checkboxes
- "Next" enabled only when users selected
- Can also be used for modifying existing group members (`isModifyMember: true`)

### 14.2 Step 2 — Configure Group
- Group name input (optional, auto-generated if empty; char count tracking)
- Avatar picker (camera / file picker)
- Privacy toggle: Public / Private (radio buttons)
- Selected users display with removal capability

### 14.3 SDK
- **Upload Avatar**: `AmityCoreClient.newFileRepository().uploadImage(file).stream.listen()` — progress callback, error 403 = inappropriate image
- **Create Group**: `AmityChatClient.newChannelRepository().createChannel().communityType().withDisplayName(name).isPublic(isPublic).userIds([...]).avatar(file).create()`

---

## 15. Group Settings Page

### 15.1 Layout
- Group avatar + name + member info
- Navigation tiles to sub-pages (moderator-only tiles gated by role)

### 15.2 Available Tiles (Moderator)
| Tile | Destination |
|---|---|
| Edit Group Profile | `AmityEditGroupProfilePage` |
| Members | `AmityGroupMemberListPage` |
| Add Members | `AmityAddGroupMemberPage` |
| Notifications | `AmityEditGroupNotificationPage` |
| Member Permissions | `AmityEditGroupMemberPermissionsPage` |
| Banned Members | `AmityBannedGroupMemberListPage` |

### 15.3 Leave Group
- Available to all members
- Confirmation dialog → `channel.leave()`

---

## 16. Edit Group Profile

- Edit group name (max 100 chars, real-time count display)
- Change group avatar (image picker: camera / gallery)
- Save button enabled only when changes detected
- **SDK**: File upload → `AmityChatClient.newChannelRepository().updateChannel(channelId).displayName(name).avatar(file).create()`

---

## 17. Group Member Management

### 17.1 Member List Page
- Two tabs: "All Members" | "Moderators"
- Search with 300ms debounce
- Pagination
- **SDK**: `AmityChatClient.newChannelRepository().membership(channelId).searchMembers(query).membershipFilter([MEMBER, MUTED]).getLiveCollection()`
- Moderator filter: `.role("channel-moderator")`

### 17.2 Member Actions (Moderator Only)
| Action | SDK Call |
|---|---|
| Add Members | `.addMembers(channelId, [userIds])` |
| Remove Member | `.removeMembers(channelId, [userIds])` |
| Promote to Moderator | `.moderation(channelId).addRole("channel-moderator", [userId])` |
| Demote Moderator | `.moderation(channelId).removeRole("channel-moderator", [userId])` |
| Ban Member | `.moderation(channelId).banMembers([userId])` |
| Unban Member | `.moderation(channelId).unbanMembers([userId])` |
| Mute Member | `.moderation(channelId).muteMembers([userId], millis: -1)` |
| Unmute Member | `.moderation(channelId).unmuteMembers([userId])` |
| Report User | `user.report().flag()` |
| Unreport User | `user.report().unflag()` |

### 17.3 Add Members Page
- Search users (display name, partial match)
- Multi-select with horizontal selected user chips
- Pagination
- **SDK**: `AmityCoreClient.newUserRepository().searchUserByDisplayName(query).matchType(PARTIAL).sortBy(DISPLAY).getLiveCollection()`

### 17.4 Banned Members Page
- View banned members with search
- Unban action
- **SDK**: `.membership(channelId).searchMembers(query).membershipFilter([BANNED]).getLiveCollection()`

---

## 18. Notification Settings

### 18.1 Group Notification Mode (Moderator — affects all members)
- Options: Default / Silent / Subscribe
- **SDK**: `AmityChatClient.newChannelRepository().updateChannel(channelId).notificationMode(mode).create()`

### 18.2 Member Messaging Permissions (Moderator)
- Options: Everyone / Moderators Only
- "Moderators Only" → mutes channel for all non-moderators
- **SDK**: `.muteChannel(channelId, millis: -1)` (mods only) / `.unMuteChannel(channelId)` (everyone)

### 18.3 Per-Channel User Notification Preference (Personal)
- Enable / Disable notifications for this specific channel
- Toast confirmation on save
- **SDK**: `AmityCoreClient().notifications().channel(channelId).enable()` / `.disable()` / `.getSettings()`

---

## 19. Search

### 19.1 Search Page
- Full-screen with two tabs: "Chats" | "Messages"
- Min 3 characters required, 300ms debounce
- Infinite scroll pagination (threshold: 200px before end)

### 19.2 Chats Tab
- Searches channel names/metadata
- Shows channel item with highlighted matching text (word boundaries)
- Swipe to archive/unarchive
- Tap → opens chat

### 19.3 Messages Tab
- Searches message content
- Shows channel containing matching message
- Tap → opens chat at specific message (`jumpToMessageId`)

### 19.4 States
| State | Display |
|---|---|
| Before 3 chars | "Type at least 3 characters" icon |
| Loading | Skeleton shimmer |
| No results | "No results found" icon |
| Results | Channel/message list |

### 19.5 SDK
- **Channel search**: `AmityChatClient.newChannelRepository().searchChannels().withQuery(query).types([CONVERSATION, COMMUNITY]).memberOnly(false).sortBy("lastActivity").getLiveCollection()`
- **Message search**: `AmityChatClient.newMessageRepository().searchMessage().withQuery(query).getLiveCollection()`
- **Archived IDs**: `AmityChatClient.newChannelRepository().getArchivedChannelIds()`

---

## 20. Archive

- **Archive**: Swipe left on channel in main list
- **Unarchive**: Swipe left on channel in archived list
- **Archived Page**: "Archived" title, back button, same list format
- **Empty state**: "No archived messages" icon
- **Limit**: Max ~30 archived channels (server enforced)
- **SDK**: `.archiveChannel(channelId)` / `.unarchiveChannel(channelId)`

---

## 21. Behavior Customization Points

| Behavior | File | Callback |
|---|---|---|
| Avatar tap (chat page) | `AmityChatPageBehavior` | `onAvatarTap(context, avatarUrl, userId)` |
| Message bubble avatar tap | `AmityUIKit4Manager.behavior.messageBubbleBehavior` | `onAvatarTap(context, avatarUrl, userId)` |
| Chat page behavior | `UIKitBehavior.instance.chatPageBehavior` | Various navigation overrides |

---

## 22. Features NOT Present in Flutter

- ❌ Typing indicators
- ❌ Audio messages
- ❌ `startMessageReceiptSync` / `stopMessageReceiptSync` (uses `message.markRead()` directly)
- ❌ File messages (only text, image, video, custom)
- ❌ Emoji picker (uses long-press reaction row)
- ❌ Forward message
