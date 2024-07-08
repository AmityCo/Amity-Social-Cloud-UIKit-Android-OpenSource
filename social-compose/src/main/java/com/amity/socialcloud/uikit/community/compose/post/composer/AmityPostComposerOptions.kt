package com.amity.socialcloud.uikit.community.compose.post.composer

import android.os.Parcelable
import com.amity.socialcloud.sdk.model.social.community.AmityCommunity
import com.amity.socialcloud.sdk.model.social.post.AmityPost
import kotlinx.parcelize.Parcelize

@Parcelize
sealed class AmityPostComposerOptions : Parcelable {
    enum class Mode {
        CREATE,
        EDIT;
    }

    class AmityPostComposerEditOptions(
        val mode: Mode = Mode.EDIT,
        val post: AmityPost,
    ) : AmityPostComposerOptions()

    class AmityPostComposerCreateOptions(
        val mode: Mode = Mode.CREATE,
        val targetId: String? = null,
        val targetType: AmityPostTargetType,
        val community: AmityCommunity? = null,
    ) : AmityPostComposerOptions()
}

enum class AmityPostTargetType {
    USER,
    COMMUNITY;
}