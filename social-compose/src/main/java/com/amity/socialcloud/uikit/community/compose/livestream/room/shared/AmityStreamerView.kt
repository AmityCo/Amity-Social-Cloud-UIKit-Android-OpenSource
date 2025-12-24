package com.amity.socialcloud.uikit.community.compose.livestream.room.shared

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.amity.socialcloud.sdk.api.core.AmityCoreClient
import com.amity.socialcloud.sdk.model.core.user.AmityUser
import com.amity.socialcloud.sdk.model.video.room.AmityRoomBroadcastData
import io.livekit.android.RoomOptions
import io.livekit.android.compose.local.RoomScope
import io.livekit.android.compose.state.rememberTracks
import io.livekit.android.compose.types.TrackReference
import io.livekit.android.compose.ui.CameraPreview
import io.livekit.android.compose.ui.ScaleType
import io.livekit.android.room.Room
import io.livekit.android.room.track.CameraPosition
import io.livekit.android.room.track.LocalVideoTrack
import io.livekit.android.room.track.LocalVideoTrackOptions
import io.livekit.android.room.track.Track
import io.livekit.android.room.track.VideoCaptureParameter
import io.livekit.android.room.track.VideoPreset169

@Composable
fun AmityStreamerView(
    modifier: Modifier = Modifier,
    userEnabledCamera: Boolean,
    userEnabledMic: Boolean,
    cameraPosition: CameraPosition,
    broadcasterData: AmityRoomBroadcastData? = null,
    hostUserId: String,
    hostUser: AmityUser? = null,
    cohostUserId: String? = null,
    cohostUser: AmityUser? = null,
    onRoomChanged: (Room) -> Unit = {},
    onCoHostLeave: () -> Unit = {},
    onCancelInvite: () -> Unit = {},
    onRemoveCoHost: () -> Unit = {},
) {
    val enableCamera = rememberEnableCamera(enabled = userEnabledCamera)
    val enableMic = rememberEnableMic(enabled = userEnabledMic)

    // Extract URL and token from broadcaster data
    val broadcastDataCoHosts = broadcasterData as? AmityRoomBroadcastData.CoHosts
    val url = broadcastDataCoHosts?.getCoHostUrl() ?: ""
    val token = broadcastDataCoHosts?.getCoHostToken() ?: ""


    RoomScope(
        audio = enableMic,
        video = enableCamera,
        connect = false,
    ) { room ->
        onRoomChanged(room)
        val captureParam = if (cohostUserId == null) {
            VideoPreset169.H720.capture
        } else {
            VideoCaptureParameter(640, 720, 30)
        }
        room.setRoomOptions(
            RoomOptions(
                videoTrackCaptureDefaults = LocalVideoTrackOptions(
                    captureParams = captureParam
                ),
            )
        )

        if (url.isNotBlank() && token.isNotBlank()) {
            val isHost = AmityCoreClient.getUserId() == hostUserId
            val trackRefs = rememberTracks()
            val localParticipant = room.localParticipant
            val localTrackIndex = trackRefs
                .indexOfFirst { it.participant == localParticipant }
                .let { index ->
                    if (index == -1) 0 else index
                }
            val remoteTrackIndex = trackRefs
                .indexOfFirst { it.participant != localParticipant }

            val trackHeightRatio = if (cohostUserId == null) {
                1.0f
            } else {
                0.5f
            }
            val localTrackRef = trackRefs.getOrNull(localTrackIndex)
            val remoteTrackRef = if (remoteTrackIndex >= 0) {
                trackRefs.getOrNull(remoteTrackIndex)
            } else {
                null
            }

            localTrackRef
                ?.participant
                ?.getTrackPublication(Track.Source.CAMERA)
                ?.track
                ?.let { it as? LocalVideoTrack }
                ?.let { localVideoTrack ->
                    if (localVideoTrack.options.position != cameraPosition) {
                        localVideoTrack.switchCamera(position = cameraPosition)
                    }
                }

            if (broadcastDataCoHosts != null && room.state == Room.State.CONNECTED) {
                LazyColumn(modifier = Modifier.fillMaxSize()) {
                    item {
                        StreamerItem(
                            modifier = Modifier.fillParentMaxHeight(trackHeightRatio),
                            trackReference = if (isHost) {
                                localTrackRef
                            } else {
                                remoteTrackRef
                            },
                            isHost = isHost,
                            user = hostUser,
                            mirror = if (isHost) {
                                cameraPosition == CameraPosition.FRONT
                            } else {
                                false
                            },
                            isMute = if (isHost) !enableMic else false,
                            liveKitRoom = room,
                            onMenuClick = null // No menu click for upper section
                        )
                    }
                    if (cohostUserId != null) {
                        item {
                            StreamerItem(
                                modifier = Modifier.fillParentMaxHeight(trackHeightRatio),
                                trackReference = if (!isHost) {
                                    localTrackRef
                                } else {
                                    remoteTrackRef
                                },
                                isHost = isHost,
                                user = cohostUser,
                                mirror = if (!isHost) {
                                    cameraPosition == CameraPosition.FRONT
                                } else {
                                    false
                                },
                                isMute = if (!isHost) !enableMic else false,
                                liveKitRoom = room,
                                onMenuClick = if (isHost) {
                                    onRemoveCoHost
                                } else {
                                    onCoHostLeave
                                }
                            )
                        }
                    }
                }
            } else {
                CameraPreview(
                    cameraPosition = cameraPosition,
                    mirror = cameraPosition == CameraPosition.FRONT
                )
            }
        } else {
            CameraPreview(
                cameraPosition = cameraPosition,
                mirror = cameraPosition == CameraPosition.FRONT
            )
        }
    }
}

@Composable
fun StreamerItem(
    modifier: Modifier = Modifier,
    trackReference: TrackReference?,
    liveKitRoom: Room,
    isHost: Boolean,
    user: AmityUser?,
    mirror: Boolean = false,
    isMute: Boolean,
    onMenuClick: (() -> Unit)?,
) {
    Box(
        modifier = modifier
    ) {
        if (trackReference != null) {
            AmityCoHostVideo(
                modifier = modifier,
                user = user,
                trackReference = trackReference,
                room = liveKitRoom,
                isHost = isHost,
                mirror = mirror,
                isMute = isMute,
                scaleType = ScaleType.Fill,
                onMenuClick = onMenuClick,
            )
        } else {
            AmityWaitingForCoHost(
                modifier = modifier,
                isHost = isHost,
                user = user,
                onMenuClick = onMenuClick,
            )
        }
    }
}