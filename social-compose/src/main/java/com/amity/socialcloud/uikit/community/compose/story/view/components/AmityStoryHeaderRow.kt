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
import androidx.compose.foundation.layout.width
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.LocalViewModelStoreOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import com.amity.socialcloud.sdk.api.core.AmityCoreClient
import com.amity.socialcloud.sdk.api.social.AmitySocialClient
import com.amity.socialcloud.sdk.helper.core.coroutines.asFlow
import com.amity.socialcloud.sdk.model.core.ad.AmityAd
import com.amity.socialcloud.sdk.model.social.community.AmityCommunity
import com.amity.socialcloud.sdk.model.social.story.AmityStory
import com.amity.socialcloud.sdk.model.social.story.AmityStoryTarget
import com.amity.socialcloud.uikit.common.ad.AmityAdBadge
import com.amity.socialcloud.uikit.common.common.readableTimeDiff
import com.amity.socialcloud.uikit.common.ui.base.AmityBaseElement
import com.amity.socialcloud.uikit.common.ui.elements.AmityAvatarView
import com.amity.socialcloud.uikit.common.ui.elements.AmityCommunityAvatarView
import com.amity.socialcloud.uikit.common.ui.scope.AmityComposePageScope
import com.amity.socialcloud.uikit.common.ui.theme.AmityTheme
import com.amity.socialcloud.uikit.common.utils.AmityConstants
import com.amity.socialcloud.uikit.common.utils.asDrawableRes
import com.amity.socialcloud.uikit.common.utils.clickableWithoutRipple
import com.amity.socialcloud.uikit.common.utils.closePage
import com.amity.socialcloud.uikit.common.utils.getValue
import com.amity.socialcloud.uikit.community.compose.R
import com.amity.socialcloud.uikit.community.compose.story.view.AmityStoryModalSheetUIState
import com.amity.socialcloud.uikit.community.compose.story.view.AmityViewStoryPageViewModel
import com.amity.socialcloud.uikit.community.compose.story.view.elements.AmityStorySegmentTimerElement
import com.amity.socialcloud.uikit.community.compose.utils.AmityStoryVideoPlayerHelper
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map

@Composable
fun AmityStoryHeaderRow(
    modifier: Modifier = Modifier,
    pageScope: AmityComposePageScope? = null,
    story: AmityStory?,
    ad: AmityAd?,
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

    val context = LocalContext.current

    val isAd = remember(story, ad) {
        story == null && ad != null
    }

    var timerDuration by remember {
        mutableLongStateOf(AmityConstants.STORY_DURATION)
    }
    val videoDuration by AmityStoryVideoPlayerHelper.duration.collectAsState()

    val viewModelStoreOwner = checkNotNull(LocalViewModelStoreOwner.current) {
        "No ViewModelStoreOwner was provided via LocalViewModelStoreOwner"
    }
    val viewModel =
        viewModel<AmityViewStoryPageViewModel>(viewModelStoreOwner = viewModelStoreOwner)

    val hasManageStoryPermission by remember(story?.getTargetId()) {
        viewModel.checkMangeStoryPermission(
            communityId = story?.getTargetId() ?: ""
        )
    }.subscribeAsState(initial = false)


    val allowAllUserStoryCreation by AmitySocialClient.getSettings()
        .asFlow()
        .map { it.getStorySettings().isAllowAllUserToCreateStory() }
        .catch {
            emit(false)
        }
        .collectAsState(initial = false)

    val shouldShowStoryCreationButton by remember(
        allowAllUserStoryCreation,
        hasManageStoryPermission,
        viewModel.community?.isJoined()
    ) {
        derivedStateOf {
            (allowAllUserStoryCreation || hasManageStoryPermission)
                    && (viewModel.community?.isJoined() == true)
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
                        Color.Black.copy(alpha = 0.5f),
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
                verticalAlignment = Alignment.CenterVertically,
                modifier = modifier
            ) {
                Box(
                    modifier = modifier
                        .size(40.dp)
                        .clickableWithoutRipple {
                            if (isAd) return@clickableWithoutRipple
                            if (hasManageStoryPermission) {
                                navigateToCreatePage()
                            } else {
                                if (isSingleTarget) {
                                    onCloseClicked()
                                } else {
                                    viewModel.community?.let {
                                        context.closePage() // Avoid multiple ExoPlayer instances
                                        navigateToCommunityProfilePage(it)
                                    }
                                }
                            }
                        }
                ) {
                    if (isAd) {
                        AmityAvatarView(
                            image = ad?.getAdvertiser()?.getAvatar(),
                            placeholder = R.drawable.amity_ic_default_advertiser,
                            iconPadding = 8.dp,
                            modifier = modifier.padding(vertical = 8.dp)
                        )
                    } else {
                        val community = (story?.getTarget() as? AmityStoryTarget.COMMUNITY)?.getCommunity()
                        AmityCommunityAvatarView(
                            community = community,
                            size = 40.dp,
                            modifier = modifier
                                .clip(CircleShape)
                                .testTag("community_avatar")
                        )
                    }
                    val canCreate = !isAd && shouldShowStoryCreationButton
                    if (canCreate) {
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
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        val displayName = if (isAd) {
                            ad?.getAdvertiser()?.getName() ?: ""
                        } else {
                            (story?.getTarget() as? AmityStoryTarget.COMMUNITY)?.getCommunity()?.getDisplayName() ?: ""
                        }
                        Text(
                            text = displayName,
                            style = AmityTheme.typography.bodyLegacy.copy(
                                color = Color.White,
                                fontWeight = FontWeight.SemiBold
                            ),
                            modifier = Modifier
                                .clickableWithoutRipple {
                                    if (isAd) return@clickableWithoutRipple
                                    if (isSingleTarget) {
                                        onCloseClicked()
                                    } else {
                                        if (viewModel.community != null) {
                                            context.closePage() // Avoid multiple ExoPlayer instances
                                            navigateToCommunityProfilePage(viewModel.community!!)
                                        }
                                    }
                                }
                                .testTag("community_display_name")
                        )

                        val isOfficialCommunity = if (isAd) {
                            false
                        } else {
                            (story?.getTarget() as? AmityStoryTarget.COMMUNITY)?.getCommunity()?.isOfficial() ?: false
                        }
                        if (isOfficialCommunity) {
                            Spacer(modifier.width(2.dp))
                            Icon(
                                painter = painterResource(R.drawable.amity_ic_verified_community),
                                tint = Color.White,
                                contentDescription = "Community Official Icon",
                                modifier = modifier.size(20.dp)
                            )
                        }
                    }

                    if (isAd) {
                        AmityAdBadge()
                    } else {
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
                }

                if (
                    (story?.getCreatorId() == AmityCoreClient.getUserId() || hasManageStoryPermission)
                    && !isAd
                ) {
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
                                            story?.getStoryId() ?: ""
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