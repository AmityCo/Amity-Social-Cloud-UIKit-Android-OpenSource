package com.amity.socialcloud.uikit.community.compose.story.target.global

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade
import coil3.request.error
import coil3.request.fallback
import com.amity.socialcloud.sdk.api.social.AmitySocialClient
import com.amity.socialcloud.sdk.helper.core.coroutines.asFlow
import com.amity.socialcloud.sdk.model.core.file.AmityImage
import com.amity.socialcloud.sdk.model.social.event.AmityEvent
import com.amity.socialcloud.sdk.model.social.post.AmityPost
import com.amity.socialcloud.uikit.common.ui.elements.AmityCommunityAvatarView
import com.amity.socialcloud.uikit.common.ui.elements.AmityUserAvatarView
import com.amity.socialcloud.uikit.common.ui.theme.AmityTheme
import com.amity.socialcloud.uikit.common.ui.theme.amityColorWhite
import com.amity.socialcloud.uikit.common.ui.theme.amityLiveBadgeGradientEnd
import com.amity.socialcloud.uikit.common.ui.theme.amityLiveBadgeRed
import com.amity.socialcloud.uikit.common.utils.clickableWithoutRipple
import com.amity.socialcloud.uikit.community.compose.R
import com.amity.socialcloud.uikit.community.compose.story.target.elements.AmityStoryGradientRingElement
import com.amity.socialcloud.uikit.community.compose.localization.amitySocialString
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map

@Composable
fun AmityLiveRoomTarget(
    modifier: Modifier = Modifier,
    post: AmityPost? = null,
    onClick: () -> Unit,
) {
    val community = (post?.getTarget() as? AmityPost.Target.COMMUNITY)?.getCommunity()
    val context = LocalContext.current

    val eventId = remember(post) {
        post?.getEventId()?.takeIf { it.isNotEmpty() }
            ?: post?.getChildren()?.firstOrNull()?.getEventId()?.takeIf { it.isNotEmpty() }
    }

    val event by remember(eventId) {
        if (eventId.isNullOrEmpty()) {
            flowOf<AmityEvent?>(null)
        } else {
            AmitySocialClient.newEventRepository()
                .getEvent(eventId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .asFlow()
                .map<AmityEvent, AmityEvent?> { it }
                .catch { emit(null) }
        }
    }.collectAsState(initial = null)

    val eventCoverUrl = event?.getCoverImage()?.getUrl(AmityImage.Size.MEDIUM)
    val displayName = event?.getTitle()?.takeIf { it.isNotBlank() }
        ?: community?.getDisplayName()
        ?: ""

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(4.dp),
        modifier = modifier
            .padding(top = 16.dp, bottom = 16.dp)
            .width(72.dp)
            .clickableWithoutRipple {
                onClick()
            }
            .testTag("story_target_list/*")
    ) {
        Box(
            modifier = Modifier.size(72.dp)
        ) {

            if (eventCoverUrl != null) {
                AsyncImage(
                    model = ImageRequest.Builder(context)
                        .data(eventCoverUrl)
                        .crossfade(true)
                        .error(R.drawable.amity_v4_ic_default_stream_thumbnail)
                        .fallback(R.drawable.amity_v4_ic_default_stream_thumbnail)
                        .build(),
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(64.dp)
                        .clip(CircleShape)
                        .align(Alignment.Center)
                        .testTag("story_target_list/target_avatar")
                )
            } else {
                AmityCommunityAvatarView(
                    community = community,
                    size = 64.dp,
                    modifier = Modifier
                        .testTag("story_target_list/target_avatar")
                        .align(Alignment.Center)
                )
            }

            AmityStoryGradientRingElement(
                colors = listOf(amityLiveBadgeRed, amityLiveBadgeGradientEnd),
                isIndeterminate = false,
                modifier = Modifier
                    .fillMaxSize(),
            )

            Box(
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .size(28.dp)
                    .clip(CircleShape)
                    .background(AmityTheme.colors.baseInverse)
                    .padding(2.dp)
            ) {
                AmityUserAvatarView(
                    user = post?.getCreator(),
                    size = 24.dp
                )
            }


            Row(
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .border(1.5.dp, AmityTheme.colors.baseInverse, RoundedCornerShape(6.dp))
                    .background(
                        color = amityLiveBadgeRed,
                        shape = RoundedCornerShape(6.dp)
                    )
                    .padding(horizontal = 6.dp, vertical = 2.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = amitySocialString("amity_social_status_live"),
                    color = amityColorWhite,
                    style = AmityTheme.typography.caption.copy(fontWeight = FontWeight.Bold),
                )
            }
        }


        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(
                text = displayName,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                style = AmityTheme.typography.captionLegacy.copy(
                    fontWeight = FontWeight.Normal
                ),
                modifier = Modifier.testTag("story_target_list/target_display_name")
            )
        }
    }
}