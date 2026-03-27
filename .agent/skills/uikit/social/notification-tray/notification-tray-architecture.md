# Notification Tray Architecture

> Read this document when working on the notification tray feature: adding new notification types, modifying item rendering, handling click navigation, or debugging notification display issues.

## File Map

| File | Purpose |
|------|---------|
| `social-compose/.../notificationtray/AmityNotificationTrayPage.kt` | Main page composable — LazyColumn, click handler routing, navigation dispatch |
| `social-compose/.../notificationtray/AmityNotificationTrayPageActivity.kt` | Activity wrapper hosting the page |
| `social-compose/.../notificationtray/AmityNotificationTrayPageBehavior.kt` | Navigation behavior (open community, user, post, event, live room) |
| `social-compose/.../notificationtray/NotificationTrayViewModel.kt` | ViewModel — paged notification items, mark seen, community invitations |
| `social-compose/.../notificationtray/component/AmityNotificationTrayItemView.kt` | Individual tray item rendering (avatar + text + timestamp) |
| `social-compose/.../notificationtray/component/AmityNotificationInvitationView.kt` | Community invitation item (accept/decline) |
| `social-compose/.../notificationtray/component/AmityNotificationTrayShimmer.kt` | Loading shimmer placeholder |
| `social-compose/.../notificationtray/component/AmityNotificationTrayEmptyState.kt` | Empty state composable |
| `common-compose/.../ui/elements/AmityAvatarView.kt` | All avatar composables used by the tray |

## SDK Model: `AmityNotificationTrayItem`

The SDK provides `AmityNotificationTrayItem` with the following key methods:

| Method | Returns | Description |
|--------|---------|-------------|
| `getActionType()` | `String` | Primary notification type: `"event"`, `"post"`, `"poll"`, `"comment"`, `"reply"`, `"reaction"`, `"mention"`, `"follow"`, `"join_request"`, `"invitation"`, `"user_profile_reset"` |
| `getTrayItemCategory()` | `String` | Sub-category for finer routing (e.g. `"reaction_on_comment"`, `"mention_in_post"`, `"respond_on_join_request"`, `"user_profile_reset"`) |
| `getTargetType()` | `String` | Target entity type: `"community"`, `"room"`, etc. |
| `getTargetId()` | `String` | ID of the target entity |
| `getActionReferenceId()` | `String` | ID of the referenced action entity (post, comment, event) |
| `getReferenceId()` | `String` | Parent reference ID (e.g. postId when action is on a comment) |
| `getParentId()` | `String?` | Parent comment ID (for replies) |
| `getUsers()` | `List<AmityUser>?` | Actor users associated with the notification |
| `getEvent()` | `AmityEvent?` | Event data (only for event notifications) |
| `getText()` | `String` | Fully rendered notification text |
| `getTemplatedText()` | `String` | Template with `{{key:value}}` placeholders for bold highlighting |
| `isSeen()` | `Boolean?` | Whether the notification has been seen |
| `getLastOccurredAt()` | `DateTime?` | Timestamp of the notification |
| `uniqueId()` | `String` | Unique identifier for LazyColumn keys |

## Avatar Rendering Pattern

In `AmityNotificationTrayItemView.kt`, the avatar is rendered using a priority-based conditional:

```kotlin
if (data?.getActionType() == "user_profile_reset") {
    // System avatar: generic person silhouette on colored circle
    // Prevents revealing admin identity
    AmityAvatarView(
        image = null,
        placeholder = R.drawable.amity_ic_default_profile1,
        placeholderTint = Color.White,
        placeholderBackground = AmityTheme.colors.primaryShade2,
    )
} else if (data?.getActionType() == "event") {
    // Event cover image (40dp, rounded corner 6dp)
    AmityEventAvatarView(eventCoverImage = event.getCoverImage())
} else {
    // User avatar (32dp, circle) from getUsers().firstOrNull()
    AmityUserAvatarView(user = it)
}
```

### Adding a new avatar type

1. Add a new `if`/`else if` branch **before** the `else` block
2. Check `getActionType()` (primary) for the new type
3. For system-generated notifications without a user actor, use `AmityAvatarView` with `image = null` and a custom `placeholder` drawable
4. Available avatar composables in `AmityAvatarView.kt`:
   - `AmityUserAvatarView` — circular, loads user avatar with initial-letter fallback
   - `AmityEventAvatarView` — rounded rectangle, loads event cover with placeholder fallback
   - `AmityCommunityAvatarView` — circular, loads community avatar
   - `AmityAvatarView` — generic, accepts custom `placeholder`, `placeholderTint`, `placeholderBackground`

### Key design rule

For moderation/system notifications: **never show admin avatar or display name**. Use the generic `AmityAvatarView` with `image = null` to force the placeholder icon. This ensures admin identity is not revealed.

## Click Navigation Pattern

In `AmityNotificationTrayPage.kt`, the click handler is a large `when (listItem.item.getActionType())` block that:

1. Extracts relevant IDs (`postId`, `commentId`, `parentId`, `communityId`, `userId`) based on action type and tray item category
2. Dispatches navigation via `behavior.*` methods

### Adding a new click handler

1. Add a new case in the `when` block
2. For early-return cases (navigation happens immediately), call `behavior.goTo*()` and `return@clickableWithoutRipple`
3. For cases that set IDs for deferred navigation, set the appropriate variables (`postId`, `communityId`, etc.) and let the downstream logic handle routing
4. Use `getTrayItemCategory()` for sub-routing within an action type

### Current navigation dispatch (after the `when` block):

```
if postId == null && communityId != null -> goToCommunityProfilePage
else if userId != null                  -> goToUserProfilePage
else if postId != null                  -> goToPostDetailPage (with optional commentId, parentId)
```

## HighlightText Composable

`HighlightText` renders notification text with bold highlights for dynamic values.

| Parameter | Description |
|-----------|-------------|
| `text` | Fully rendered text (e.g. `"Alice reacted to your post."`) |
| `templatedText` | Template with `{{placeholders}}` (e.g. `"{{ userId: abc123 }} reacted to your post."`) |

**Algorithm:**
1. Extract `{{...}}` patterns from `templatedText` via regex
2. Split template into literal parts
3. Diff against rendered `text` to find placeholder replacement values
4. Build `AnnotatedString` with `FontWeight.SemiBold` for placeholder values
5. If no placeholders found, render plain `Text`

**Key behavior:** If a notification type has no `{{ }}` placeholders in `templatedText`, the text renders with uniform weight. No code change is needed in `HighlightText` to support new notification types.

## R Class Cross-Module References

When referencing drawables from `common` module in `social-compose` code:

```kotlin
// In social-compose files, use the common-compose R alias
import com.amity.socialcloud.uikit.common.compose.R as CommonComposeR

// Then reference
CommonComposeR.drawable.amity_ic_default_profile1
```

The `common` module's resources are accessible transitively through `common-compose`'s merged R class.
