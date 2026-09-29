package com.amity.socialcloud.uikit.community.compose

import androidx.paging.PagingData
import androidx.paging.filter
import com.amity.socialcloud.sdk.model.core.pin.AmityPinnedPost
import com.amity.socialcloud.sdk.model.social.post.AmityPost
import com.amity.socialcloud.uikit.common.config.AmityUIKitDataGate
import com.amity.socialcloud.uikit.common.config.AmityUIKitInternalApi
import com.amity.socialcloud.uikit.common.config.AmityUIKitFeature
import io.reactivex.rxjava3.core.Flowable

/**
 * Which module owns a post, or null for one every build can draw. Clip is not
 * a module; [isClip] answers for it.
 *
 * Read from the children, the way `AmityPostContentComponent` picks the element
 * to draw with: a poll, livestream, clip or event post is a parent post of
 * dataType `text` carrying one child of that type. `dataTypes()` matches the
 * parent, so it cannot tell those apart from a plain text post — a feed that
 * asks for text posts is handed polls too.
 */
private fun AmityPost.owningFeature(): AmityUIKitFeature? {
    // Structure type first: in a live collection the children can be empty on the
    // first emission and resolve later, while the parent carries its structure
    // type immediately — the same ordering iOS's AmityPostModel documents.
    when (getStructureType()) {
        AmityPost.StructureType.POLL -> return AmityUIKitFeature.POLL
        AmityPost.StructureType.LIVESTREAM, AmityPost.StructureType.ROOM -> return AmityUIKitFeature.LIVE
        AmityPost.StructureType.EVENT -> return AmityUIKitFeature.EVENTS
        else -> Unit
    }
    val data = getChildren().map { it.getData() } + getData()
    return when {
        data.any { it is AmityPost.Data.LIVE_STREAM || it is AmityPost.Data.ROOM } ->
            AmityUIKitFeature.LIVE

        data.any { it is AmityPost.Data.POLL } -> AmityUIKitFeature.POLL
        data.any { it is AmityPost.Data.EVENT } -> AmityUIKitFeature.EVENTS
        else -> null
    }
}

/** A clip post, by structure type first and then by its children, as above. */
private fun AmityPost.isClip(): Boolean =
    getStructureType() == AmityPost.StructureType.CLIP ||
        (getChildren().map { it.getData() } + getData()).any { it is AmityPost.Data.CLIP }

/**
 * Whether the module this post belongs to is available. A clip is Post's, and
 * answers to the internal clip tier rather than to a module of its own (§6.2).
 */
@OptIn(AmityUIKitInternalApi::class)
internal fun AmityPost.isTypeAvailable(): Boolean {
    if (isClip()) return AmityUIKitDataGate.isClipOn()
    val feature = owningFeature() ?: return true
    return AmityUIKitDataGate.isOn(feature)
}

/**
 * Drops posts belonging to a switched-off module, at the query.
 *
 * The elements already refuse to draw those posts, but refusing at the element
 * leaves the header, the menu and the reaction bar around a body that renders
 * nothing — an empty post. Dropping the row here means no feed ever hands one to
 * the view, and no surface has to remember to hide it.
 */
internal fun Flowable<PagingData<AmityPost>>.dropGatedPostTypes(): Flowable<PagingData<AmityPost>> =
    map { paging -> paging.filter { it.isTypeAvailable() } }

/** As [dropGatedPostTypes], for the pinned and announcement queries. */
internal fun Flowable<PagingData<AmityPinnedPost>>.dropGatedPinnedPostTypes(): Flowable<PagingData<AmityPinnedPost>> =
    map { paging -> paging.filter { it.post?.isTypeAvailable() ?: true } }

/** As [dropGatedPostTypes], for the global pinned query, which is not paginated. */
internal fun Flowable<List<AmityPinnedPost>>.dropGatedPinnedPosts(): Flowable<List<AmityPinnedPost>> =
    map { pinned -> pinned.filter { it.post?.isTypeAvailable() ?: true } }
