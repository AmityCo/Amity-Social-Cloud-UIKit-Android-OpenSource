package com.amity.socialcloud.uikit.community.compose.livestream.room.create.model

import android.net.Uri
import com.amity.socialcloud.sdk.core.session.model.NetworkConnectionEvent
import com.amity.socialcloud.sdk.model.core.file.AmityImage
import com.amity.socialcloud.sdk.model.core.invitation.AmityInvitation
import com.amity.socialcloud.sdk.model.core.user.AmityUser
import com.amity.socialcloud.sdk.model.social.post.AmityPost
import com.amity.socialcloud.sdk.model.video.room.AmityRoom
import com.amity.socialcloud.sdk.model.video.room.AmityRoomBroadcastData
import com.amity.socialcloud.sdk.model.video.room.AmityRoomModeration
import com.amity.socialcloud.uikit.community.compose.livestream.create.model.LivestreamThumbnailUploadUiState
import io.livekit.android.room.Room
import io.livekit.android.room.track.CameraPosition

data class AmityCreateRoomPageUiState(
    val isLive: Boolean = false,
    val targetName: String? = null,
    val communityId: String? = null,
    val userId: String? = null,
    val liveTitle: String? = null,
    val liveDesc: String? = null,
    val createPostId: String? = null,
    val thumbnailId: String? = null,
    val thumbnailUri: Uri? = null,
    val thumbnailImage: AmityImage? = null,
    val room: AmityRoom? = null,
    val liveKitRoom: Room? = null,
    val networkConnection: NetworkConnectionEvent = NetworkConnectionEvent.Connected,
    val thumbnailUploadUiState: LivestreamThumbnailUploadUiState = LivestreamThumbnailUploadUiState.Idle,
    val postId: String? = null,
    val post: AmityPost? = null,
    val channelId: String? = null,
    val isPendingApproval: Boolean? = null,
    val error: Throwable? = null,
    val broadcasterData: AmityRoomBroadcastData? = null,
    val roomModeration: AmityRoomModeration? = null,
    val hostUserId: String? = null,
    val hostUser: AmityUser? = null,
    val cohostUserId: String? = null,
    val cohostUser: AmityUser? = null,
    val viewers: List<AmityUser> = emptyList(),
    val invitation: AmityInvitation? = null,
    val cameraPosition: CameraPosition = CameraPosition.FRONT,
    val isPreparingInitialData: Boolean = false,
    val viewerCount: Int? = null,
)
