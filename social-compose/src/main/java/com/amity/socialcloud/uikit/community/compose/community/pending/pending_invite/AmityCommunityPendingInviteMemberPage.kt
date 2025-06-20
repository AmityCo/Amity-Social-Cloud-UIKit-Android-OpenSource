package com.amity.socialcloud.uikit.community.compose.community.pending.pending_invite

import android.app.Activity
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.LocalViewModelStoreOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.paging.compose.collectAsLazyPagingItems
import com.amity.socialcloud.sdk.model.core.user.AmityUser
import com.amity.socialcloud.sdk.model.social.community.AmityCommunity
import com.amity.socialcloud.uikit.common.common.views.AmityColorShade
import com.amity.socialcloud.uikit.common.ui.base.AmityBasePage
import com.amity.socialcloud.uikit.common.ui.elements.AmitySearchBarView
import com.amity.socialcloud.uikit.common.ui.elements.AmityUserAvatarView
import com.amity.socialcloud.uikit.common.ui.theme.AmityTheme
import com.amity.socialcloud.uikit.common.utils.clickableWithoutRipple
import com.amity.socialcloud.uikit.common.utils.closePageWithResult
import com.amity.socialcloud.uikit.common.utils.shade
import com.amity.socialcloud.uikit.community.compose.R
import com.amity.socialcloud.uikit.community.compose.community.membership.element.AmityCommunityInviteMemberItem
import com.amity.socialcloud.uikit.community.compose.community.membership.element.AmityCommunityInviteMemberRowList
import com.amity.socialcloud.uikit.community.compose.community.membership.invite.AmityCommunityInviteMemberPageViewModel
import com.amity.socialcloud.uikit.community.compose.search.components.AmityEmptySearchResultComponent
import com.amity.socialcloud.uikit.community.compose.search.components.AmityEmptyUserListComponent
import com.amity.socialcloud.uikit.community.compose.ui.shimmer.AmityUserListShimmer

@Composable
fun AmityCommunityPendingInviteMemberPage(
    modifier: Modifier = Modifier,
    community: AmityCommunity,
) {
    val context = LocalContext.current

    var keyword by remember {
        mutableStateOf<String?>(null)
    }

    val viewModelStoreOwner = checkNotNull(LocalViewModelStoreOwner.current) {
        "No ViewModelStoreOwner was provided via LocalViewModelStoreOwner"
    }
    val viewModel =
        viewModel<AmityCommunityPendingInviteMemberPageViewModel>(viewModelStoreOwner = viewModelStoreOwner)

    val loadState by viewModel.userListState.collectAsState()

    val lazyPagingItems = remember() {
        viewModel.getInvitations(community)
    }.collectAsLazyPagingItems()

    AmityBasePage(pageId = "community_invite_member_page") {
        Box(
            modifier = modifier
                .fillMaxSize()
                .windowInsetsPadding(WindowInsets.safeDrawing)
        ) {
            Column(modifier.fillMaxSize()) {
                Box(
                    modifier = modifier
                        .fillMaxWidth()
                        .padding(start = 12.dp, end = 16.dp)
                ) {
                    Icon(
                        painter = painterResource(R.drawable.amity_ic_close),
                        contentDescription = "Close",
                        modifier = modifier
                            .size(16.dp)
                            .align(Alignment.CenterStart)
                            .clickableWithoutRipple {
                                context.closePageWithResult(Activity.RESULT_CANCELED)
                            }
                    )

                    Text(
                        text = "Pending invitations",
                        style = AmityTheme.typography.titleLegacy,
                        modifier = modifier
                            .padding(vertical = 17.dp)
                            .align(Alignment.Center)
                    )
                }

                HorizontalDivider(
                    color = AmityTheme.colors.baseShade4,
                )

                LazyColumn {
                    AmityCommunityPendingInviteMemberPageViewModel.UserListState.from(
                        loadState = lazyPagingItems.loadState.refresh,
                        itemCount = lazyPagingItems.itemCount,
                    ).let(viewModel::setUserListState)

                    when (loadState) {
                        AmityCommunityPendingInviteMemberPageViewModel.UserListState.EMPTY -> {
                            item {
                                if (keyword == null) {
                                    AmityEmptyUserListComponent(modifier)
                                } else {
                                    AmityEmptySearchResultComponent(modifier)
                                }
                            }
                        }

                        AmityCommunityPendingInviteMemberPageViewModel.UserListState.LOADING -> {
                            item {
                                if (keyword == null) {
                                    AmityEmptyUserListComponent(modifier)
                                } else {
                                    AmityUserListShimmer(
                                        modifier = modifier.padding(
                                            horizontal = 16.dp,
                                            vertical = 8.dp
                                        )
                                    )
                                }
                            }
                        }

                        AmityCommunityPendingInviteMemberPageViewModel.UserListState.SUCCESS -> {
                            items(
                                count = lazyPagingItems.itemCount,
                                key = { index -> index }
                            ) { index ->

                                val user = lazyPagingItems[index]?.getInvitedUser() ?: return@items

                                AmityCommunityInvitationItem(
                                    user = user,
                                )
                            }
                            item {
                                Spacer(modifier.height(80.dp))
                            }
                        }

                        else -> {}
                    }
                }
            }
        }
    }
}


@Composable
fun AmityCommunityInvitationItem(
    modifier: Modifier = Modifier,
    user: AmityUser,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .fillMaxWidth()
            .background(color = Color.Transparent)
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        AmityUserAvatarView(
            user = user,
            size = 40.dp,
            modifier = modifier,
        )

        Spacer(modifier.width(12.dp))
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = modifier.weight(1f)
        ) {
            Text(
                text = user.getDisplayName() ?: user.getUserId(),
                overflow = TextOverflow.Ellipsis,
                maxLines = 1,
                style = AmityTheme.typography.bodyLegacy.copy(
                    fontWeight = FontWeight.SemiBold,
                ),
                modifier = Modifier.weight(1f, fill = false)
            )
            val isBrandUser = user.isBrand()
            if (isBrandUser) {
                val badge = R.drawable.amity_ic_brand_badge
                Image(
                    painter = painterResource(id = badge),
                    contentDescription = "",
                    modifier = Modifier
                        .size(20.dp)
                        .padding(start = 4.dp)
                        .testTag("user_view/brand_user_icon")
                )
            }
        }
    }
}