package com.amity.socialcloud.uikit.community.compose.story.view.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.lifecycle.viewmodel.compose.LocalViewModelStoreOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.media3.common.util.UnstableApi
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.amity.socialcloud.sdk.api.core.AmityCoreClient
import com.amity.socialcloud.sdk.model.social.story.AmityStory
import com.amity.socialcloud.uikit.common.common.readableTimeDiff
import com.amity.socialcloud.uikit.common.common.views.AmityColorPaletteUtil
import com.amity.socialcloud.uikit.common.common.views.AmityColorShade
import com.amity.socialcloud.uikit.common.utils.AmityConstants
import com.amity.socialcloud.uikit.community.compose.R
import com.amity.socialcloud.uikit.community.compose.story.view.AmityViewStoryPageViewModel
import com.amity.socialcloud.uikit.community.compose.story.view.elements.AmityStorySegmentTimerElement
import com.amity.socialcloud.uikit.community.compose.ui.base.AmityBaseElement
import com.amity.socialcloud.uikit.community.compose.ui.scope.AmityComposePageScope
import com.amity.socialcloud.uikit.community.compose.utils.AmityStoryVideoPlayerHelper
import com.amity.socialcloud.uikit.community.compose.utils.asDrawableRes
import com.amity.socialcloud.uikit.community.compose.utils.getValue

@UnstableApi
@Composable
fun AmityStoryHeaderRow(
    modifier: Modifier = Modifier,
    pageScope: AmityComposePageScope? = null,
    communityId: String,
    communityDisplayName: String,
    avatarUrl: String,
    stories: List<AmityStory>,
    totalSegments: Int,
    currentSegment: Int,
    isVisible: Boolean,
    shouldPauseTimer: Boolean,
    shouldRestartTimer: Boolean,
    moveToNextSegment: () -> Unit,
    onCloseClicked: () -> Unit,
    navigateToCreatePage: () -> Unit,
    onDeleteClicked: (String) -> Unit,
) {
    if (isVisible) {
        if (stories.isEmpty()) return

        val story by lazy { stories.getOrNull(currentSegment) }
        var timerDuration by remember {
            mutableStateOf(AmityConstants.STORY_DURATION)
        }
        val videoDuration by AmityStoryVideoPlayerHelper.duration.collectAsState()

        val viewModelStoreOwner = checkNotNull(LocalViewModelStoreOwner.current) {
            "No ViewModelStoreOwner was provided via LocalViewModelStoreOwner"
        }
        val viewModel =
            viewModel<AmityViewStoryPageViewModel>(viewModelStoreOwner = viewModelStoreOwner)

        LaunchedEffect(communityId) {
            viewModel.checkMangeStoryPermission(communityId = communityId)
        }
        val hasManageStoryPermission by viewModel.hasManageStoryPermission.collectAsState()

        LaunchedEffect(currentSegment, videoDuration) {
            timerDuration = if (story?.getDataType() == AmityStory.DataType.VIDEO) {
                videoDuration
            } else {
                AmityConstants.STORY_DURATION
            }
        }

        Box(
            modifier = modifier
                .fillMaxWidth()
                .background(
                    brush = Brush.verticalGradient(
                        listOf(
                            Color.Black.copy(alpha = 0.2f),
                            Color.Transparent
                        )
                    )
                )
                .padding(horizontal = 16.dp, vertical = 16.dp)
        ) {
            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                AmityStorySegmentTimerElement(
                    pageScope = pageScope,
                    shouldPauseTimer = shouldPauseTimer,
                    shouldRestart = shouldRestartTimer,
                    totalSegments = totalSegments,
                    currentSegment = currentSegment,
                    duration = timerDuration,
                ) {
                    moveToNextSegment()
                }

                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = modifier
                ) {
                    Box(
                        modifier = modifier
                            .size(40.dp)
                            .clickable {
                                if (hasManageStoryPermission) {
                                    navigateToCreatePage()
                                } else {
                                    onCloseClicked()
                                }
                            }
                    ) {
                        if (avatarUrl.isEmpty()) {
                            Image(
                                painter = painterResource(id = R.drawable.amity_ic_default_community_avatar_circular),
                                contentScale = ContentScale.Fit,
                                contentDescription = "Avatar Image",
                                modifier = Modifier
                                    .size(40.dp)
                                    .clip(CircleShape)
                                    .background(
                                        Color(
                                            AmityColorPaletteUtil.getColor(
                                                ContextCompat.getColor(
                                                    LocalContext.current,
                                                    R.color.amityColorPrimary
                                                ),
                                                AmityColorShade.SHADE3
                                            )
                                        )
                                    )
                            )
                        } else {
                            AsyncImage(
                                model = ImageRequest
                                    .Builder(LocalContext.current)
                                    .data(avatarUrl)
                                    .crossfade(true)
                                    .build(),
                                contentScale = ContentScale.Crop,
                                contentDescription = "Avatar Image",
                                modifier = modifier
                                    .size(40.dp)
                                    .clip(CircleShape)
                            )
                        }
                        if (hasManageStoryPermission) {
                            Image(
                                painter = painterResource(id = R.drawable.amity_ic_plus_circle),
                                contentDescription = "",
                                modifier = Modifier
                                    .size(16.dp)
                                    .align(Alignment.BottomEnd)
                            )
                        }
                    }

                    Column(
                        modifier = Modifier.weight(1f),
                    ) {
                        Text(
                            text = communityDisplayName,
                            color = Color.White,
                            style = TextStyle(fontWeight = FontWeight.Bold, fontSize = 15.sp),
                            modifier = Modifier.clickable {
                                onCloseClicked()
                            }
                        )
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Text(
                                text = story?.getCreatedAt()?.readableTimeDiff() ?: "",
                                color = Color.White,
                                style = TextStyle(fontSize = 13.sp)
                            )
                            Text(
                                text = "•",
                                color = Color.White,
                                style = TextStyle(fontSize = 13.sp)
                            )
                            Text(
                                text = "By ${story?.getCreator()?.getDisplayName() ?: ""}",
                                color = Color.White,
                                style = TextStyle(fontSize = 13.sp)
                            )
                        }
                    }

                    if (story?.getCreatorId() == AmityCoreClient.getUserId()) {
                        AmityBaseElement(
                            pageScope = pageScope,
                            elementId = "overflow_menu"
                        ) {
                            Icon(
                                painter = painterResource(
                                    id = getConfig().getValue("overflow_menu_icon").asDrawableRes()
                                ),
                                contentDescription = "More",
                                tint = Color.White,
                                modifier = Modifier
                                    .size(24.dp)
                                    .align(Alignment.CenterVertically)
                                    .clickable {
                                        story
                                            ?.getStoryId()
                                            ?.let {
                                                onDeleteClicked(it)
                                            }
                                    }
                            )
                        }
                        Spacer(modifier = modifier.size(8.dp))
                    }

                    AmityBaseElement(
                        pageScope = pageScope,
                        elementId = "close_button"
                    ) {
                        Icon(
                            painter = painterResource(
                                id = getConfig().getValue("close_icon").asDrawableRes()
                            ),
                            contentDescription = "Close",
                            tint = Color.White,
                            modifier = Modifier
                                .size(24.dp)
                                .align(Alignment.CenterVertically)
                                .padding(4.dp)
                                .clickable {
                                    onCloseClicked()
                                }
                        )

                    }
                }
            }
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFABEF)
@Composable
fun AmityStoryHeaderRowPreview() {
//    AmityStoryHeaderRow(
//        storyTarget = FAKE_STORY_DATA.first(),
//        storySegment = FAKE_STORY_DATA.first().segments.first(),
//        totalSegments = FAKE_STORY_DATA.first().segments.size,
//        currentSegment = 0,
//        isVisible = true,
//        shouldPauseTimer = false,
//        moveToNextSegment = {},
//        onCloseClicked = {}
//    )
}