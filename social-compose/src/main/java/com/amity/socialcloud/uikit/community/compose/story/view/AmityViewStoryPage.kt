package com.amity.socialcloud.uikit.community.compose.story.view

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.media3.exoplayer.ExoPlayer
import com.amity.socialcloud.sdk.model.social.story.AmityStory
import com.amity.socialcloud.uikit.common.utils.closePage
import com.amity.socialcloud.uikit.community.compose.utils.AmityStoryVideoPlayerHelper

@Composable
fun AmityViewStoryPage(
    modifier: Modifier = Modifier,
    type: AmityViewStoryPageType,
) {
    val context = LocalContext.current
    
    when (type) {
        is AmityViewStoryPageType.CommunityFeed -> {
            val exoPlayer = remember { ExoPlayer.Builder(context).build() }
            
            AmityViewCommunityStoryPage(
                modifier = modifier,
                targetId = type.communityId,
                targetType = AmityStory.TargetType.COMMUNITY,
                exoPlayer = exoPlayer,
                isSingleTarget = true,
                lastSegmentReached = {
                    context.closePage()
                }
            )
            
            DisposableEffect(Unit) {
                onDispose {
                    exoPlayer.release()
                    AmityStoryVideoPlayerHelper.clear()
                }
            }
        }
        
        is AmityViewStoryPageType.GlobalFeed -> {
            AmityViewGlobalStoryPage(
                modifier = modifier,
                targetId = type.communityId,
            )
        }
    }
}