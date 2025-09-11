package com.amity.socialcloud.uikit.community.compose.post.composer

import android.net.Uri
import android.os.Parcelable
import androidx.camera.core.AspectRatio
import com.amity.socialcloud.sdk.model.core.file.AmityClip
import com.amity.socialcloud.sdk.model.social.community.AmityCommunity
import com.amity.socialcloud.sdk.model.social.post.AmityPost
import kotlinx.parcelize.Parcelize

@Parcelize
sealed class AmityPostComposerOptions : Parcelable {
    enum class Mode {
        CREATE,
        CREATE_CLIP,
        EDIT_CLIP,
        EDIT;
    }

    data class AmityPostComposerEditOptions(
        val mode: Mode = Mode.EDIT,
        val post: AmityPost,
    ) : AmityPostComposerOptions()

    data class AmityPostComposerCreateOptions(
        val mode: Mode = Mode.CREATE,
        val targetId: String? = null,
        val targetType: AmityPostTargetType,
        val community: AmityCommunity? = null,
    ) : AmityPostComposerOptions()

    data class AmityPostComposerEditClipOptions(
        val mode: Mode = Mode.EDIT_CLIP,
        val post: AmityPost,
    ) : AmityPostComposerOptions()

    data class AmityPostComposerCreateClipOptions(
        val mode: Mode = Mode.CREATE_CLIP,
        val targetId: String? = null,
        val targetType: AmityPostTargetType,
        val community: AmityCommunity? = null,
        val clip : AmityClip,
        val uri: Uri? = null,
        val isMute: Boolean? = null,
        val aspectRatio: AmityClip.DisplayMode? = null
    ) : AmityPostComposerOptions()
}

enum class AmityPostTargetType {
    USER,
    COMMUNITY;
}