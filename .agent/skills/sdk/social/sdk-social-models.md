# SDK Social Models Reference

> Read this document when you need to understand SDK data structures, access patterns, or how to read/write post data and product tags.

## Important note

All SDK classes below come from **external dependencies** (`com.amity.socialcloud.sdk.*`). There is no source code in this project. You must rely on the public API methods documented here and usage patterns observed in the codebase.

## AmityPost

The core post model. A post has a parent post (the main content) and zero or more child posts (one per image/video attachment).

### Key methods

| Method | Return type | Description |
|--------|-------------|-------------|
| `getPostId()` | `String` | Unique post identifier |
| `getData()` | `AmityPost.Data` | Sealed type — `TEXT`, `IMAGE`, `VIDEO`, `CLIP`, `LIVESTREAM`, `POLL`, etc. |
| `getChildren()` | `List<AmityPost>` | Child posts (images, videos). Each child has its own `getData()`, `getProductTags()`, `getProducts()` |
| `getProductTags()` | `List<AmityProductTag>` | Both text and media product tags on this post |
| `getProducts()` | `List<AmityProduct>?` | Full product objects associated with this post |
| `getReviewStatus()` | `AmityReviewStatus` | `UNDER_REVIEW`, `APPROVED`, etc. |
| `getEditedAt()` | `DateTime?` | Last edit timestamp |
| `getUpdatedAt()` | `DateTime?` | Last update timestamp |

### AmityPost.Data sealed types

| Type | How to get the file ID |
|------|----------------------|
| `AmityPost.Data.TEXT` | N/A — text-only post |
| `AmityPost.Data.IMAGE` | `data.getImage()?.getFileId()` |
| `AmityPost.Data.VIDEO` | `data.getVideo().blockingGet().getFileId()` (note: `blockingGet()` because it returns `Single`) |
| `AmityPost.Data.CLIP` | Through clip-specific APIs |
| `AmityPost.Data.LIVESTREAM` | Through livestream-specific APIs |
| `AmityPost.Data.POLL` | Through poll-specific APIs |

### Parent-child relationship

```
AmityPost (parent)
+-- getData() = AmityPost.Data.TEXT (or IMAGE/VIDEO for single-attachment posts)
+-- getProductTags() -> text-level AmityProductTag.Text items
+-- getProducts() -> full AmityProduct list for text tags
+-- getChildren()
    +-- AmityPost (child 1: image)
    |   +-- getData() = AmityPost.Data.IMAGE
    |   +-- getProductTags() -> media-level AmityProductTag.Media items
    |   +-- getProducts() -> full AmityProduct list for this child's tags
    +-- AmityPost (child 2: image)
    |   +-- ...
    +-- AmityPost (child 3: video)
        +-- ...
```

## AmityProductTag

Sealed class with two subtypes representing product tags at different levels.

### AmityProductTag.Text

Product mentioned in the post text body.

| Property | Type | Description |
|----------|------|-------------|
| `productId` | `String` | Product identifier |
| `text` | `String` | Display text in the post body |
| `index` | `Int` | Start position in the text |
| `length` | `Int` | Length of the tagged text |
| `product` | `AmityProduct?` | Full product object (may be null if not resolved) |

Constructor:
```kotlin
AmityProductTag.Text(
    productId = "prod_123",
    text = "Serum CitrusGlow",
    index = 42,
    length = 16,
    product = amityProduct  // optional
)
```

### AmityProductTag.Media

Product tagged on a media attachment (image/video).

| Property | Type | Description |
|----------|------|-------------|
| `productId` | `String` | Product identifier |
| `product` | `AmityProduct?` | Full product object (may be null if not resolved) |

Constructor:
```kotlin
AmityProductTag.Media(
    productId = "prod_456",
    product = amityProduct  // optional
)
```

## AmityProduct

Full product model from the SDK.

### Key methods

| Method | Return type | Description |
|--------|-------------|-------------|
| `getProductId()` | `String` | Unique product identifier |
| `getProductName()` | `String?` | Display name |
| `getThumbnailUrl()` | `String?` | Product image URL |
| `getCurrency()` | `String?` | Currency code (e.g. "USD") |
| `getPrice()` | `Double?` | Price value |
| `getStatus()` | `AmityProductStatus` | Product availability status |
| `isDeleted()` | `Boolean` | Whether the product has been deleted |

### AmityProductStatus

| Status | Meaning |
|--------|---------|
| `ARCHIVED` | Product is unlisted / no longer available |
| Other values | Active / available |

## AmityAttachmentProductTags

Container that maps media file IDs to their product tags. Used when creating/editing posts with media-level product tags.

### Construction (Builder pattern)

```kotlin
val builder = AmityAttachmentProductTags.Builder()

// For each media file, set its product tags
builder.set("fileId_abc", listOf(
    AmityProductTag.Media(productId = "prod_1"),
    AmityProductTag.Media(productId = "prod_2"),
))
builder.set("fileId_def", listOf(
    AmityProductTag.Media(productId = "prod_3"),
))

val attachmentProductTags: AmityAttachmentProductTags = builder.build()
```

### Usage in SDK API

```kotlin
// Create post with attachment product tags
AmitySocialClient.newPostRepository()
    .createImagePost(
        ...,
        productTags = textProductTags,                    // List<AmityProductTag.Text>
        attachmentProductTags = attachmentProductTags,     // AmityAttachmentProductTags
    )

// Edit post with attachment product tags
AmitySocialClient.newPostRepository()
    .editPost(postId)
    .productTags(textProductTags)              // List<AmityProductTag.Text>
    .taggedProducts(attachmentProductTags)     // AmityAttachmentProductTags
    .build()
    .apply()
```

## File info classes

| Class | Description | Key method |
|-------|-------------|------------|
| `AmityImage` | Uploaded image file info | `getFileId()` |
| `AmityVideo` | Uploaded video file info | `getFileId()` |
| `AmityClip` | Uploaded clip file info | `getFileId()` |
| `AmityFileInfo` | Base class for all uploaded files | `getFileId()` |
| `AmityRawFile` | Raw file upload | `getFileId()` |

## Common patterns in the codebase

### Reading product tags from a post for display

```kotlin
// Text tags from parent post
val textTags = post.getProductTags().filterIsInstance<AmityProductTag.Text>()

// Media tags from child posts
post.getChildren().forEach { childPost ->
    val mediaTags = childPost.getProductTags().filterIsInstance<AmityProductTag.Media>()
    val products = mediaTags.mapNotNull { it.product }
}
```

### Collecting all unique products from a post (for product carousel)

```kotlin
val products = buildSet {
    post.getProductTags().forEach { tag -> tag.product?.let(::add) }
    post.getChildren().forEach { childPost ->
        childPost.getProductTags().forEach { tag -> tag.product?.let(::add) }
    }
}.toList()
```

### Getting the file ID from a child post

```kotlin
val fileId = when (val childData = childPost.getData()) {
    is AmityPost.Data.IMAGE -> childData.getImage()?.getFileId()
    is AmityPost.Data.VIDEO -> childData.getVideo().blockingGet().getFileId()
    else -> null
}
```
