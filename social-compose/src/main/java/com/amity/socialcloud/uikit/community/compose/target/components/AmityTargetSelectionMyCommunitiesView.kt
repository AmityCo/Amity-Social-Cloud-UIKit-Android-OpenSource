package com.amity.socialcloud.uikit.community.compose.target.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import com.amity.socialcloud.sdk.model.social.community.AmityCommunity
import com.amity.socialcloud.uikit.common.ui.base.AmityBaseElement
import com.amity.socialcloud.uikit.common.ui.elements.AmityCommunityAvatarView
import com.amity.socialcloud.uikit.common.ui.theme.AmityTheme
import com.amity.socialcloud.uikit.common.utils.clickableWithoutRipple
import com.amity.socialcloud.uikit.common.utils.getIcon
import com.amity.socialcloud.uikit.community.compose.target.AmityTargetSelectionPageViewModel
import com.amity.socialcloud.uikit.community.compose.ui.shimmer.AmityCommunityTargetListShimmer


@Composable
fun AmityTargetSelectionMyCommunitiesView(
    modifier: Modifier = Modifier,
    contentType: AmityTargetContentType,
    onClick: (AmityCommunity) -> Unit,
) {
    val viewModel = viewModel<AmityTargetSelectionPageViewModel>()

    LaunchedEffect(contentType) {
        viewModel.setContentType(contentType)
    }

    val communities = remember(viewModel, contentType) {
        viewModel.getCommunities()
    }.collectAsLazyPagingItems()

    val allowToCreateMap = remember(viewModel) {
        viewModel.allowToCreateMap
    }.collectAsState()

    Text(
        text = "My Communities",
        style = AmityTheme.typography.bodyLegacy.copy(
            color = AmityTheme.colors.base.copy(alpha = 0.4f),
        ),
        modifier = modifier.padding(horizontal = 16.dp, vertical = 8.dp),
    )

    LazyColumn(
        modifier = modifier.fillMaxWidth(),
        state = rememberLazyListState()
    ) {
        when {
            communities.loadState.refresh is LoadState.Loading -> {
                item {
                    AmityCommunityTargetListShimmer(
                        modifier = modifier.padding(horizontal = 16.dp)
                    )
                }
            }
            communities.loadState.refresh is LoadState.Error -> {
            }
            else -> {
                items(
                    count = communities.itemCount,
                    key = { communities[it]?.getCommunityId() ?: it }
                ) { index ->
                    val community = communities[index] ?: return@items

                    LaunchedEffect(community.getCommunityId()) {
                        viewModel.checkPermissionIfNeeded(community)
                    }

                    if (allowToCreateMap.value[community.getCommunityId()] == true) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(12.dp),
                            modifier = modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp, vertical = 8.dp)
                                .clickableWithoutRipple { onClick(community) }
                        ) {
                            AmityCommunityAvatarView(
                                modifier = modifier,
                                size = 40.dp,
                                community = community,
                            )

                            Row(
                                horizontalArrangement = Arrangement.spacedBy(4.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                if (!community.isPublic()) {
                                    AmityBaseElement(elementId = "community_private_badge") {
                                        Icon(
                                            painter = painterResource(id = getConfig().getIcon()),
                                            tint = AmityTheme.colors.baseShade1,
                                            contentDescription = "Private Community",
                                            modifier = Modifier
                                                .size(16.dp)
                                                .testTag(getAccessibilityId()),
                                        )
                                    }
                                }

                                Text(
                                    text = community.getDisplayName(),
                                    style = AmityTheme.typography.bodyLegacy.copy(
                                        fontWeight = FontWeight.SemiBold
                                    ),
                                    modifier = modifier,
                                )

                                if (community.isOfficial()) {
                                    AmityBaseElement(elementId = "community_official_badge") {
                                        Image(
                                            painter = painterResource(id = getConfig().getIcon()),
                                            contentDescription = "Verified Community",
                                            modifier = Modifier
                                                .size(16.dp)
                                                .testTag(getAccessibilityId()),
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
enum class AmityTargetContentType {
    POST,
    STORY
}