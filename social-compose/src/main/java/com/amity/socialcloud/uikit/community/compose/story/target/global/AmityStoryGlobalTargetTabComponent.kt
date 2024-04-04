package com.amity.socialcloud.uikit.community.compose.story.target.global

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.LocalViewModelStoreOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.paging.compose.collectAsLazyPagingItems
import com.amity.socialcloud.sdk.model.core.file.AmityImage
import com.amity.socialcloud.sdk.model.social.story.AmityStoryTarget
import com.amity.socialcloud.uikit.community.compose.AmitySocialBehaviorHelper
import com.amity.socialcloud.uikit.community.compose.story.target.elements.AmityStoryTargetElement
import com.amity.socialcloud.uikit.community.compose.story.target.utils.toRingUiState
import com.amity.socialcloud.uikit.community.compose.ui.base.AmityBaseComponent
import com.amity.socialcloud.uikit.community.compose.ui.elements.AmityGlobalFeedDivider

@Composable
fun AmityStoryGlobalTargetTabComponent(
    modifier: Modifier = Modifier,
) {
    val context = LocalContext.current
    val behavior by lazy {
        AmitySocialBehaviorHelper.storyTabComponentBehavior
    }

    val viewModelStoreOwner = checkNotNull(LocalViewModelStoreOwner.current) {
        "No ViewModelStoreOwner was provided via LocalViewModelStoreOwner"
    }
    val viewModel =
        viewModel<AmityStoryGlobalTargetTabViewModel>(viewModelStoreOwner = viewModelStoreOwner)

    val targets = remember(viewModel) {
        viewModel.getTargets()
    }.collectAsLazyPagingItems()

    val shouldShowDivider by remember(targets.itemCount) {
        derivedStateOf {
            targets.itemCount > 0
        }
    }

    LaunchedEffect(targets.itemSnapshotList.items) {
        AmityStorySharedGlobalTargetsObject.setTargets(targets.itemSnapshotList.items)
    }

    LaunchedEffect(targets.itemCount) {
        viewModel.prefetchStoriesFromTargets(targets.itemSnapshotList.items)
    }

    AmityBaseComponent(componentId = "story_tab_component") {
        Column(
            modifier = modifier.fillMaxWidth()
        ) {
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = modifier
                    .fillMaxWidth()
                    .padding(start = 16.dp)
            ) {
                items(
                    count = targets.itemCount,
                    key = { targets[it]?.getTargetId() ?: it }
                ) { index ->
                    val target = targets[index]
                    val community = if (target is AmityStoryTarget.COMMUNITY) {
                        target.getCommunity()
                    } else {
                        null
                    }
                    if (target == null || community == null) return@items

                    AmityStoryTargetElement(
                        modifier = modifier,
                        componentScope = getComponentScope(),
                        communityDisplayName = community.getDisplayName(),
                        avatarUrl = community.getAvatar()?.getUrl(AmityImage.Size.LARGE) ?: "",
                        ringUiState = target.toRingUiState(),
                        isPublicCommunity = community.isPublic(),
                        isOfficialCommunity = community.isOfficial(),
                        isCommunityTarget = false,
                        hasManageStoryPermission = false,
                    ) {
                        AmityStorySharedGlobalTargetsObject.setSelectedTarget(
                            index = index,
                            target = target
                        )

                        behavior.goToViewStoryPage(
                            context = context,
                            isGlobalFeed = true,
                            communityId = target.getTargetId()
                        )
                    }
                }
            }

            if (shouldShowDivider) {
                AmityGlobalFeedDivider(modifier)
            }
        }
    }
}

@Preview
@Composable
fun PreviewAmityStoryGlobalTargetTabComponent() {
    AmityStoryGlobalTargetTabComponent()
}