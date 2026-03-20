# Post Composer Architecture

> Read this document when working on post creation, post editing, product tagging in posts, or debugging composer page behavior.

## Key files

| File | Role |
|------|------|
| `social-compose/.../post/composer/AmityPostComposerPageViewModel.kt` | Core ViewModel — create/edit logic, media uploads, product tags state |
| `social-compose/.../post/composer/AmityPostComposerPage.kt` | Compose UI — observes ViewModel events, handles user input, triggers create/edit |
| `social-compose/.../post/composer/AmityPostComposerHelper.kt` | Global singleton — holds newly created/updated posts in memory for feed pages |
| `social-compose/.../post/composer/AmityPostComposerOptions.kt` | Sealed class — defines create/edit/clip launcher options |
| `social-compose/.../post/composer/AmityPostComposerPageActivity.kt` | Activity wrapper hosting the composer page |

## Create paths

The `createPost()` function branches based on media type. Each path accepts different parameters:

| Path | Function | `productTags` | `attachmentProductTags` |
|------|----------|:---:|:---:|
| Text only | `createPostText()` | Yes | No |
| Text + Images | `createPostTextAndImages()` | Yes | Yes |
| Text + Videos | `createPostTextAndVideos()` | Yes | Yes |
| Text + Clip | `createPostTextAndClip()` | No | No |

All paths return `Single<AmityPost>` and share a common `.doOnSuccess` / `.doOnError` handler that emits `AmityPostCreationEvent`.

## Edit paths

The `updatePost()` function has two distinct branches:

### Clip post edit
- Only updates text, metadata, mentions, and links via `editPost(postId).text(...)`
- Does **not** pass product tags or modify attachments
- Re-fetches the post afterward to check review status

### Non-clip post edit
1. Calls `deleteImageOrFileInPost()` — hard-deletes any child posts whose media file ID is in `deletedImageIds`
2. Calls `updateParentPost()` — rebuilds the edit request with remaining + new attachments
3. Sets `.productTags(productTags).taggedProducts(attachmentProductTags)` on the editor
4. Re-fetches the post afterward to check review status

## Product tags architecture

### Two levels of product tags

| Level | Type | Where stored | Purpose |
|-------|------|-------------|---------|
| **Text-level** | `AmityProductTag.Text` | Parent post | Products mentioned in the post text body (with `index` and `length` for positioning) |
| **Media-level** | `AmityProductTag.Media` | Child posts (one per image/video) | Products tagged on specific media attachments |

### ViewModel state for product tags

```kotlin
// Media product tags: maps fileId -> list of AmityProduct
private val _mediaProductTags: MutableStateFlow<Map<String, List<AmityProduct>>>

// Text product tags: stores tags with their text indices
private val _textProductTags: MutableStateFlow<List<AmityProductTag.Text>>
private val _textProductTagsWithIndex: MutableStateFlow<List<Pair<Int, AmityProduct>>>

// Combined flow of all distinct products (text + media, deduplicated by product ID)
val allDistinctProductTags: Flow<List<AmityProduct>>
```

### Key functions

| Function | Purpose |
|----------|---------|
| `setProductTagsForMedia(mediaId, products)` | Store product tags for a media file |
| `getProductTagsForMedia(mediaId)` | Retrieve product tags for a media file |
| `removeProductTagsForMedia(mediaId)` | Remove product tags when media is deleted |
| `clearAllProductTags()` | Reset all product tags |
| `buildAttachmentProductTags()` | Convert `_mediaProductTags` into `AmityAttachmentProductTags` via Builder |
| `prepareProductTags(post)` | In edit mode — extract existing product tags from the post into ViewModel state |
| `checkAndNotifyMissingProductTags(post, sentProductTags)` | Compare sent tags vs returned post; show error snackbar if any were removed by the server |

### How `prepareProductTags(post)` works (edit mode)

1. Extracts text product tags from `post.getProductTags().filterIsInstance<AmityProductTag.Text>()`
2. Maps product IDs to full `AmityProduct` objects via `post.getProducts()`
3. For each child post, extracts the media file ID and its `AmityProductTag.Media` list
4. Populates `_textProductTags`, `_textProductTagsWithIndex`, and `_mediaProductTags`

## Event flow

### `AmityPostCreationEvent` sealed class

```
Initial -> Creating/Updating -> Success/Pending/Failed -> Initial (after 500ms delay)
```

| Event | Meaning |
|-------|---------|
| `Initial` | Idle state |
| `Creating` | Post creation in progress |
| `Updating` | Post update in progress |
| `Success` | Operation succeeded — composer closes |
| `Pending` | Post is under review — shows pending dialog |
| `Failed(throwable)` | Operation failed — shows error snackbar |

### Page observation flow

1. **Creating/Updating** -> `getPageScope().showProgressSnackbar("Posting..."/"Updating...")`
2. **Success** -> `context.closePageWithResult(Activity.RESULT_OK)`
3. **Pending** -> dismiss snackbar, show pending post dialog, then close on dismiss
4. **Failed** -> dismiss snackbar, publish error via `AmityUIKitSnackbar.publishSnackbarErrorMessage()`

### Success path detail

```
SDK returns AmityPost
  -> AmityPostComposerHelper.addNewPost(post) / .updatePost(postId)
  -> checkAndNotifyMissingProductTags(post, productTags)  // snackbar if tags missing
  -> setPostCreationEvent(Success)
  -> Page observes Success -> closePageWithResult(RESULT_OK)
  -> Feed page reads AmityPostComposerHelper.getCreatedPosts()
  -> Feed page shows any pending AmityUIKitSnackbar messages
```

## Media upload flow

1. User picks media -> stored in `mediaMap: LinkedHashMap<String, AmityPostMedia>`
2. Each media is uploaded via `AmityFileService().uploadImage/uploadVideo/uploadClip`
3. Upload progress tracked via `AmityUploadResult` (progress/complete/error/cancelled)
4. Completed uploads stored in `uploadedMediaMap: ConcurrentHashMap<String, AmityFileInfo>`
5. `_isAllMediaSuccessfullyUploaded` StateFlow gates the post button
6. On `createPost`, images/videos are sorted by original order and passed to the SDK
