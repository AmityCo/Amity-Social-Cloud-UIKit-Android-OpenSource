package com.amity.socialcloud.uikit.common.utils

import com.amity.socialcloud.sdk.model.core.file.AmityImage
import com.amity.socialcloud.sdk.model.core.user.AmityUser

/**
 * Returns the best available avatar URL string for this user.
 *
 * Priority order:
 *   1. avatarCustomUrl — external URL set by the operator (e.g. BNI's CDN).
 *      The [size] parameter has NO effect here — CDN URLs are returned as-is.
 *   2. Amity-hosted file — uploaded via AmityFileRepository, at [size] resolution.
 *   3. null — caller should display a placeholder.
 *
 * The [size] parameter defaults to MEDIUM. Pass Size.SMALL or Size.LARGE
 * at call sites that previously used those sizes directly.
 *
 * Related: SLE-566 / iOS equivalent SLE-565
 */
fun AmityUser.resolvedAvatarUrl(
    size: AmityImage.Size = AmityImage.Size.MEDIUM
): String? {
    // 1. Operator-supplied external URL — size is irrelevant
    val customUrl = getAvatarCustomUrl()
    if (!customUrl.isNullOrBlank()) return customUrl
    // 2. Amity-hosted file — use requested size
    val amityUrl = getAvatar()?.getUrl(size)
    if (!amityUrl.isNullOrBlank()) return amityUrl
    // 3. No avatar set
    return null
}
