package com.amity.socialcloud.uikit.community.compose.story.target

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.LocalViewModelStoreOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.media3.common.util.UnstableApi
import androidx.paging.compose.collectAsLazyPagingItems
import com.amity.socialcloud.sdk.model.core.file.AmityImage
import com.amity.socialcloud.sdk.model.social.community.AmityCommunity
import com.amity.socialcloud.uikit.common.config.AmityUIKitConfigController
import com.amity.socialcloud.uikit.community.compose.AmitySocialBehaviorHelper
import com.amity.socialcloud.uikit.community.compose.story.ui.elements.AmityStoryTargetElement
import com.amity.socialcloud.uikit.community.compose.ui.base.AmityBaseComponent

@UnstableApi
@Composable
fun AmityStoryTargetTabComponent(
    modifier: Modifier = Modifier,
    community: AmityCommunity,
) {
    val context = LocalContext.current
    val behavior by lazy {
        AmitySocialBehaviorHelper.storyTabComponentBehavior
    }

    val viewModelStoreOwner = checkNotNull(LocalViewModelStoreOwner.current) {
        "No ViewModelStoreOwner was provided via LocalViewModelStoreOwner"
    }
    val viewModel =
        viewModel<AmityStoryTargetTabViewModel>(viewModelStoreOwner = viewModelStoreOwner)

    LaunchedEffect(community.getCommunityId()) {
        viewModel.checkMangeStoryPermission(communityId = community.getCommunityId())
        viewModel.getStories(communityId = community.getCommunityId())
        viewModel.observeStory(communityId = community.getCommunityId())
    }

    val stories = viewModel.stories.collectAsLazyPagingItems()
    val storyTargetRingUiState by viewModel.storyTargetRingUiState.collectAsState()

    var isStoryEmpty by remember { mutableStateOf(true) }

    LaunchedEffect(stories.itemCount) {
        isStoryEmpty = stories.itemCount == 0
    }

    val hasManageStoryPermission by viewModel.hasManageStoryPermission.collectAsState()

    if (!hasManageStoryPermission && isStoryEmpty) return

    AmityBaseComponent(componentId = "story_tab_component") {
        AmityStoryTargetElement(
            modifier = modifier,
            componentScope = getComponentScope(),
            communityDisplayName = community.getDisplayName(),
            avatarUrl = community.getAvatar()?.getUrl(AmityImage.Size.LARGE) ?: "",
            storyTargetRingUiState = storyTargetRingUiState,
            isPublicCommunity = community.isPublic(),
            isOfficialCommunity = community.isOfficial(),
            isSingleCommunityTarget = true,
            hasManageStoryPermission = hasManageStoryPermission,
        ) {
            if (hasManageStoryPermission && isStoryEmpty) {
                behavior.goToCreateStoryPage(
                    context = context,
                    community = community
                )
            } else {
                behavior.goToViewStoryPage(
                    context = context,
                    community = community
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun AmityStoryTargetTabComponentPreview() {
    AmityUIKitConfigController.setup(LocalContext.current)
//    AmityStoryTargetTabComponent()
}