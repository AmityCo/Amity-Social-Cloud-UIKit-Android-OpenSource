package com.amity.socialcloud.uikit.community.compose.story.view.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rxjava3.subscribeAsState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.lifecycle.viewmodel.compose.LocalViewModelStoreOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import coil.request.CachePolicy
import coil.request.ImageRequest
import com.amity.socialcloud.sdk.api.core.AmityCoreClient
import com.amity.socialcloud.sdk.model.core.file.AmityImage
import com.amity.socialcloud.sdk.model.social.community.AmityCommunity
import com.amity.socialcloud.sdk.model.social.story.AmityStory
import com.amity.socialcloud.uikit.common.common.readableTimeDiff
import com.amity.socialcloud.uikit.common.common.views.AmityColorPaletteUtil
import com.amity.socialcloud.uikit.common.common.views.AmityColorShade
import com.amity.socialcloud.uikit.common.utils.AmityConstants
import com.amity.socialcloud.uikit.community.compose.R
import com.amity.socialcloud.uikit.community.compose.story.view.AmityStoryModalSheetUIState
import com.amity.socialcloud.uikit.community.compose.story.view.AmityViewStoryPageViewModel
import com.amity.socialcloud.uikit.community.compose.story.view.elements.AmityStorySegmentTimerElement
import com.amity.socialcloud.uikit.community.compose.ui.base.AmityBaseElement
import com.amity.socialcloud.uikit.community.compose.ui.scope.AmityComposePageScope
import com.amity.socialcloud.uikit.community.compose.ui.theme.AmityTheme
import com.amity.socialcloud.uikit.community.compose.utils.AmityStoryVideoPlayerHelper
import com.amity.socialcloud.uikit.community.compose.utils.asDrawableRes
import com.amity.socialcloud.uikit.community.compose.utils.clickableWithoutRipple
import com.amity.socialcloud.uikit.community.compose.utils.getValue
import kotlinx.coroutines.Dispatchers

@Composable
fun AmityStoryHeaderRow(
    modifier: Modifier = Modifier,
    pageScope: AmityComposePageScope? = null,
    story: AmityStory?,
    totalSegments: Int,
    currentSegment: Int,
    shouldPauseTimer: Boolean,
    shouldRestartTimer: Boolean,
    isSingleTarget: Boolean,
    moveToNextSegment: () -> Unit,
    onCloseClicked: () -> Unit,
    navigateToCreatePage: () -> Unit,
    navigateToCommunityProfilePage: (AmityCommunity) -> Unit,
) {
    var timerDuration by remember {
        mutableLongStateOf(AmityConstants.STORY_DURATION)
    }
    val videoDuration by AmityStoryVideoPlayerHelper.duration.collectAsState()

    val viewModelStoreOwner = checkNotNull(LocalViewModelStoreOwner.current) {
        "No ViewModelStoreOwner was provided via LocalViewModelStoreOwner"
    }
    val viewModel =
        viewModel<AmityViewStoryPageViewModel>(viewModelStoreOwner = viewModelStoreOwner)

    val hasManageStoryPermission by remember(viewModel.community?.getCommunityId()) {
        viewModel.checkMangeStoryPermission(
            communityId = viewModel.community?.getCommunityId() ?: ""
        )
    }.subscribeAsState(initial = false)

    val communityDisplayName by remember(viewModel.community?.getCommunityId()) {
        derivedStateOf {
            viewModel.community?.getDisplayName() ?: ""
        }
    }

    val communityAvatarUrl by remember(viewModel.community?.getCommunityId()) {
        derivedStateOf {
            viewModel.community?.getAvatar()?.getUrl(AmityImage.Size.LARGE) ?: ""
        }
    }

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
                        .clickableWithoutRipple {
                            if (hasManageStoryPermission) {
                                navigateToCreatePage()
                            } else {
                                if (isSingleTarget) {
                                    onCloseClicked()
                                } else {
                                    viewModel.community?.let {
                                        navigateToCommunityProfilePage(it)
                                    }
                                }
                            }
                        }
                ) {
                    if (communityAvatarUrl.isEmpty()) {
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
                                .testTag("community_avatar")
                        )
                    } else {
                        AsyncImage(
                            model = ImageRequest
                                .Builder(LocalContext.current)
                                .data(communityAvatarUrl)
                                .dispatcher(Dispatchers.IO)
                                .diskCachePolicy(CachePolicy.ENABLED)
                                .memoryCachePolicy(CachePolicy.ENABLED)
                                .build(),
                            contentScale = ContentScale.Crop,
                            contentDescription = "Avatar Image",
                            modifier = modifier
                                .size(40.dp)
                                .clip(CircleShape)
                                .testTag("community_avatar")
                        )
                    }
                    if (hasManageStoryPermission) {
                        Image(
                            painter = painterResource(id = R.drawable.amity_ic_plus_circle),
                            contentDescription = "",
                            modifier = Modifier
                                .size(16.dp)
                                .align(Alignment.BottomEnd)
                                .testTag("create_story_icon")
                        )
                    }
                }

                Column(
                    modifier = Modifier.weight(1f),
                ) {
                    Text(
                        text = communityDisplayName,
                        style = AmityTheme.typography.body.copy(
                            color = Color.White,
                            fontWeight = FontWeight.SemiBold
                        ),
                        modifier = Modifier
                            .clickableWithoutRipple {
                                if (isSingleTarget) {
                                    onCloseClicked()
                                } else {
                                    if (viewModel.community != null) {
                                        navigateToCommunityProfilePage(viewModel.community!!)
                                    }
                                }
                            }
                            .testTag("community_display_name")
                    )
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text(
                            text = story?.getCreatedAt()?.readableTimeDiff() ?: "",
                            color = Color.White,
                            fontSize = 13.sp,
                            modifier = modifier.testTag("created_at")
                        )
                        Text(
                            text = "• By",
                            color = Color.White,
                            fontSize = 13.sp,
                        )
                        Text(
                            text = story?.getCreator()?.getDisplayName() ?: "",
                            color = Color.White,
                            fontSize = 13.sp,
                            modifier = modifier.testTag("creator_display_name")
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
                                .clickableWithoutRipple {
                                    viewModel.updateSheetUIState(
                                        AmityStoryModalSheetUIState.OpenOverflowMenuSheet(
                                            story.getStoryId()
                                        )
                                    )
                                }
                                .testTag("overflow_menu_button"),
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
                            .clickableWithoutRipple {
                                onCloseClicked()
                            }
                            .testTag(getAccessibilityId()),
                    )
                }
            }
        }
    }
}