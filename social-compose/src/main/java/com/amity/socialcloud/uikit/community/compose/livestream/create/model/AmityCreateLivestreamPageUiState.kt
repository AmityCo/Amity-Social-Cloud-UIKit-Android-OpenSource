package com.amity.socialcloud.uikit.community.compose.livestream.create.model

import android.net.Uri
import com.amity.socialcloud.sdk.core.session.model.NetworkConnectionEvent
import com.amity.socialcloud.sdk.model.social.post.AmityPost
import com.amity.socialcloud.sdk.model.video.stream.AmityStream
import com.amity.socialcloud.sdk.model.video.stream.AmityStreamModeration
import com.amity.socialcloud.sdk.video.model.AmityStreamBroadcasterState

data class AmityCreateLivestreamPageUiState(
    val isLive: Boolean = false,
    val targetName: String? = null,
    val communityId: String? = null,
    val userId: String? = null,
    val liveTitle: String? = null,
    val liveDesc: String? = null,
    val createPostId: String? = null,
    val thumbnailId: String? = null,
    val thumbnailUri: Uri? = null,
    val streamObj: AmityStream? = null,
    val streamModeration: AmityStreamModeration? = null,
    val networkConnection: NetworkConnectionEvent = NetworkConnectionEvent.Connected,
    val broadcasterState: AmityStreamBroadcasterState = AmityStreamBroadcasterState.IDLE(),
    val thumbnailUploadUiState: LivestreamThumbnailUploadUiState = LivestreamThumbnailUploadUiState.Idle,
    val amityPost: AmityPost? = null,
    val error: Throwable? = null,
)

sealed class LivestreamThumbnailUploadUiState {
    object Idle : LivestreamThumbnailUploadUiState()
    object Uploading : LivestreamThumbnailUploadUiState()
    data class Success(val thumbnailId: String) : LivestreamThumbnailUploadUiState()
    data class Error(val message: String) : LivestreamThumbnailUploadUiState()
}