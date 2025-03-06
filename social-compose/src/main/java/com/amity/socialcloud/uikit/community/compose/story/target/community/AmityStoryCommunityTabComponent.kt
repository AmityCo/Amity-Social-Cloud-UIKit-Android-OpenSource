package com.amity.socialcloud.uikit.community.compose.story.target.community

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rxjava3.subscribeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.LocalViewModelStoreOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.paging.compose.collectAsLazyPagingItems
import com.amity.socialcloud.sdk.api.social.AmitySocialClient
import com.amity.socialcloud.sdk.helper.core.coroutines.asFlow
import com.amity.socialcloud.sdk.model.social.story.AmityStory
import com.amity.socialcloud.sdk.model.social.story.AmityStoryTarget
import com.amity.socialcloud.uikit.common.ui.base.AmityBaseComponent
import com.amity.socialcloud.uikit.community.compose.AmitySocialBehaviorHelper
import com.amity.socialcloud.uikit.community.compose.story.target.elements.AmityStoryTargetElement
import com.amity.socialcloud.uikit.community.compose.story.target.utils.AmityStoryTargetRingUiState
import com.amity.socialcloud.uikit.community.compose.story.target.utils.toRingUiState
import com.amity.socialcloud.uikit.community.compose.story.view.AmityViewStoryPageType
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map

@Composable
fun AmityStoryCommunityTabComponent(
    modifier: Modifier = Modifier,
    communityId: String,
) {
    val context = LocalContext.current
    val behavior by lazy {
        AmitySocialBehaviorHelper.storyTabComponentBehavior
    }

    val viewModelStoreOwner = checkNotNull(LocalViewModelStoreOwner.current) {
        "No ViewModelStoreOwner was provided via LocalViewModelStoreOwner"
    }
    val viewModel =
        viewModel<AmityStoryCommunityTabViewModel>(viewModelStoreOwner = viewModelStoreOwner)

    val hasManageStoryPermission by remember(viewModel, communityId) {
        viewModel.checkMangeStoryPermission(communityId)
    }.subscribeAsState(initial = false)

    val stories = remember(viewModel, communityId) {
        viewModel.getStories(communityId)
    }.collectAsLazyPagingItems()

    val target by remember(viewModel, communityId) {
        viewModel.observeStoryTarget(communityId)
    }.subscribeAsState(initial = null)

    val storyTargetRingUiState by remember {
        derivedStateOf {
            target?.toRingUiState() ?: AmityStoryTargetRingUiState.SEEN
        }
    }

    val community by remember(target) {
        derivedStateOf {
            (target as? AmityStoryTarget.COMMUNITY)?.getCommunity()
        }
    }

    val isStoryEmpty by remember {
        derivedStateOf {
            stories.itemCount == 0
        }
    }

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
        community?.isJoined()
    ) {
        derivedStateOf {
            (allowAllUserStoryCreation || hasManageStoryPermission)
                    && (community?.isJoined() == true)
        }
    }

    if (community == null) return
    if (!shouldShowStoryCreationButton && isStoryEmpty) return

    AmityBaseComponent(componentId = "story_tab_component") {
        AmityStoryTargetElement(
            modifier = modifier,
            componentScope = getComponentScope(),
            community = community,
            ringUiState = storyTargetRingUiState,
            isCommunityTarget = true,
            hasManageStoryPermission = shouldShowStoryCreationButton,
        ) {
            if (shouldShowStoryCreationButton && isStoryEmpty) {
                behavior.goToCreateStoryPage(
                    context = context,
                    targetId = communityId,
                    targetType = AmityStory.TargetType.COMMUNITY,
                )
            } else {
                behavior.goToViewStoryPage(
                    context = context,
                    type = AmityViewStoryPageType.CommunityFeed(
                        communityId = target?.getTargetId() ?: ""
                    )
                )
            }
        }
    }
}

@Preview
@Composable
fun AmityStoryCommunityTargetTabComponentPreview() {
    AmityStoryCommunityTabComponent(
        communityId = ""
    )
}