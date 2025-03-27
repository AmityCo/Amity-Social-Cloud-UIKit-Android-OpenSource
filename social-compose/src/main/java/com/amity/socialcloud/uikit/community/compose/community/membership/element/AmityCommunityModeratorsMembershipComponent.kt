package com.amity.socialcloud.uikit.community.compose.community.membership.element

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.paging.compose.collectAsLazyPagingItems
import com.amity.socialcloud.sdk.api.core.AmityCoreClient
import com.amity.socialcloud.uikit.common.compose.R
import com.amity.socialcloud.uikit.common.ui.elements.AmityUserAvatarView
import com.amity.socialcloud.uikit.common.ui.theme.AmityTheme
import com.amity.socialcloud.uikit.common.utils.clickableWithoutRipple
import com.amity.socialcloud.uikit.community.compose.AmitySocialBehaviorHelper
import com.amity.socialcloud.uikit.community.compose.community.membership.list.AmityCommunityMembershipPageBehavior
import com.amity.socialcloud.uikit.community.compose.community.membership.list.AmityCommunityMembershipPageViewModel
import com.amity.socialcloud.uikit.community.compose.community.membership.list.AmityCommunityMembershipSheetUIState
import com.amity.socialcloud.uikit.community.compose.search.components.AmityEmptySearchResultComponent
import com.amity.socialcloud.uikit.community.compose.ui.shimmer.AmityUserListShimmer

@Composable
fun AmityCommunityModeratorsMembershipComponent(
    modifier: Modifier = Modifier,
    viewModel: AmityCommunityMembershipPageViewModel,
) {
    val context = LocalContext.current
    val behavior by lazy {
        AmitySocialBehaviorHelper.communityMembershipPageBehavior
    }

    val loadState by viewModel.moderatorListState.collectAsState()
    val lazyPagingItems = remember {
        viewModel.getModerators()
    }.collectAsLazyPagingItems()

    val triggerPagingRefresh by viewModel.triggerPagingRefresh.collectAsState()

    LaunchedEffect(triggerPagingRefresh) {
        lazyPagingItems.refresh()
    }

    Column(modifier.fillMaxSize()) {
        Spacer(modifier = modifier.height(12.dp))

        LazyColumn {
            AmityCommunityMembershipPageViewModel.MembershipListState.from(
                loadState = lazyPagingItems.loadState.refresh,
                itemCount = lazyPagingItems.itemCount,
            ).let(viewModel::setModeratorListState)

            when (loadState) {
                AmityCommunityMembershipPageViewModel.MembershipListState.EMPTY -> {
                    item {
                        AmityEmptySearchResultComponent(modifier)
                    }
                }

                AmityCommunityMembershipPageViewModel.MembershipListState.LOADING -> {
                    item {
                        AmityUserListShimmer(
                            modifier = modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                        )
                    }
                }

                AmityCommunityMembershipPageViewModel.MembershipListState.SUCCESS -> {
                    items(
                        count = lazyPagingItems.itemCount,
                        key = { index -> lazyPagingItems[index]?.getUser()?.getUserId() ?: index }
                    ) { index ->
                        val member = lazyPagingItems[index] ?: return@items

                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(12.dp),
                            modifier = modifier
                                .fillMaxSize()
                                .padding(horizontal = 16.dp, vertical = 8.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(40.dp)
                            ) {
                                AmityUserAvatarView(
                                    user = member.getUser(),
                                    size = 40.dp,
                                    modifier = modifier.clickableWithoutRipple {
                                        behavior.goToUserProfilePage(
                                            AmityCommunityMembershipPageBehavior.Context(
                                                pageContext = context,
                                                userId = member.getUserId(),
                                            )
                                        )
                                    }
                                )
                                Box(
                                    modifier = Modifier
                                        .size(16.dp)
                                        .offset(
                                            x = 4.dp,
                                        )
                                        .align(Alignment.BottomEnd)
                                        .background(
                                            color = AmityTheme.colors.primaryShade3,
                                            shape = CircleShape,
                                        )
                                ) {
                                    Icon(
                                        painter = painterResource(R.drawable.amity_ic_moderator_social),
                                        contentDescription = "Action",
                                        tint = AmityTheme.colors.primary,
                                        modifier = modifier
                                            .size(12.dp)
                                            .align(Alignment.Center)
                                    )
                                }
                            }

                            Text(
                                text = member.getUser()?.getDisplayName() ?: "",
                                style = AmityTheme.typography.bodyLegacy.copy(
                                    fontWeight = FontWeight.SemiBold,
                                ),
                                modifier = modifier.weight(1f, fill = false)
                            )

                            if (member.getUserId() != AmityCoreClient.getUserId()) {
                                Icon(
                                    painter = painterResource(R.drawable.amity_ic_more_horiz),
                                    contentDescription = "Action",
                                    modifier = modifier
                                        .size(24.dp)
                                        .clickableWithoutRipple {
                                            viewModel.updateSheetUIState(
                                                AmityCommunityMembershipSheetUIState.OpenSheet(
                                                    member = member,
                                                )
                                            )
                                        }
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}