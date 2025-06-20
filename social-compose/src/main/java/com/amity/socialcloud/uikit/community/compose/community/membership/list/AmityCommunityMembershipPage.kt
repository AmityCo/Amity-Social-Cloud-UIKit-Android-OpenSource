package com.amity.socialcloud.uikit.community.compose.community.membership.list

import android.app.Activity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.rxjava3.subscribeAsState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.amity.socialcloud.sdk.model.chat.settings.AmityMembershipAcceptanceType
import com.amity.socialcloud.sdk.model.social.community.AmityCommunity
import com.amity.socialcloud.uikit.common.compose.R
import com.amity.socialcloud.uikit.community.compose.R as SocialR
import com.amity.socialcloud.uikit.common.ui.base.AmityBasePage
import com.amity.socialcloud.uikit.common.ui.elements.AmityTabRow
import com.amity.socialcloud.uikit.common.ui.elements.AmityTabRowItem
import com.amity.socialcloud.uikit.common.ui.elements.AmityToolBar
import com.amity.socialcloud.uikit.common.utils.clickableWithoutRipple
import com.amity.socialcloud.uikit.common.utils.closePageWithResult
import com.amity.socialcloud.uikit.community.compose.AmitySocialBehaviorHelper
import com.amity.socialcloud.uikit.community.compose.community.membership.add.AmityCommunityAddMemberPageActivity
import com.amity.socialcloud.uikit.community.compose.community.membership.element.AmityCommunityMembersMembershipComponent
import com.amity.socialcloud.uikit.community.compose.community.membership.element.AmityCommunityMembershipSheet
import com.amity.socialcloud.uikit.community.compose.community.membership.element.AmityCommunityModeratorsMembershipComponent
import com.amity.socialcloud.uikit.community.compose.community.membership.invite.AmityCommunityInviteMemberPageActivity
import kotlinx.coroutines.launch

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun AmityCommunityMembershipPage(
    modifier: Modifier = Modifier,
    community: AmityCommunity,
) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    val behavior by lazy {
        AmitySocialBehaviorHelper.communityMembershipPageBehavior
    }

    val viewModel = remember(community.getCommunityId()) {
        AmityCommunityMembershipPageViewModel(community.getCommunityId())
    }

    val tabs = remember {
        listOf(
            AmityTabRowItem(title = "Members"),
            AmityTabRowItem(title = "Moderators"),
        )
    }
    var selectedTabIndex by remember { mutableIntStateOf(0) }

    val pagerState = rememberPagerState { tabs.size }

    val membershipAcceptance = viewModel.getMembershipAcceptanceType()
        .subscribeAsState(initial = AmityMembershipAcceptanceType.AUTOMATIC)

    val hasEditPermission by viewModel.hasEditPermission().subscribeAsState(initial = false)

    val successMessage = stringResource(SocialR.string.amity_v4_community_add_member_success)
    val failedToAddMessage = stringResource(SocialR.string.amity_v4_community_add_member_failed)

    AmityBasePage(pageId = "community_membership_page") {
        val addMembersLauncher = rememberLauncherForActivityResult(
            contract = ActivityResultContracts.StartActivityForResult()
        ) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                result.data?.let {
                    val users = it.let(AmityCommunityAddMemberPageActivity::getUsers)
                    viewModel.addMembers(
                        userIds = users.map { user -> user.getUserId() },
                        onSuccess = {
                            getPageScope().showSnackbar(
                                message = successMessage,
                                drawableRes = R.drawable.amity_ic_snack_bar_success,
                            )
                        },
                        onError = {
                            getPageScope().showSnackbar(
                                message = failedToAddMessage,
                                drawableRes = R.drawable.amity_ic_snack_bar_warning,
                            )
                        }
                    )
                }
            }
        }

        val inviteMembersLauncher = rememberLauncherForActivityResult(
            contract = ActivityResultContracts.StartActivityForResult()
        ) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                result.data?.let { data ->
                    val users = data.let(AmityCommunityInviteMemberPageActivity::getUsers)
                    viewModel.inviteUsers(community, users.map { it.getUserId() })
                }
            }
        }

        Column(
            modifier = modifier
                .fillMaxSize()
                .windowInsetsPadding(WindowInsets.safeDrawing)
        ) {
            AmityToolBar(
                title = "All members",
                onBackClick = {
                    context.closePageWithResult(Activity.RESULT_CANCELED)
                }
            ) {
                if ((membershipAcceptance.value
                    == AmityMembershipAcceptanceType.INVITATION
                    && hasEditPermission)
                    || community.isJoined()
                ) {
                    Icon(
                        painter = painterResource(R.drawable.amity_ic_add),
                        contentDescription = "Close",
                        modifier = modifier
                            .size(24.dp)
                            .clickableWithoutRipple {
                                if (membershipAcceptance.value == AmityMembershipAcceptanceType.INVITATION) {
                                    behavior.goToInviteMemberPage(
                                        AmityCommunityMembershipPageBehavior.Context(
                                            pageContext = context,
                                            launcher = inviteMembersLauncher,
                                            community = community,
                                        )
                                    )
                                } else {
                                    behavior.goToAddMemberPage(
                                        AmityCommunityMembershipPageBehavior.Context(
                                            pageContext = context,
                                            launcher = addMembersLauncher,
                                        )
                                    )
                                }
                            }
                    )
                }
            }

            AmityTabRow(tabs = tabs, selectedIndex = selectedTabIndex) {
                selectedTabIndex = it
                coroutineScope.launch {
                    pagerState.scrollToPage(it)
                }
            }

            HorizontalPager(
                state = pagerState,
                userScrollEnabled = false,
                key = { index -> tabs[index].title ?: index },
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            ) { index ->
                when (index) {
                    0 -> AmityCommunityMembersMembershipComponent(viewModel = viewModel)
                    1 -> AmityCommunityModeratorsMembershipComponent(viewModel = viewModel)
                }
            }
        }

        AmityCommunityMembershipSheet(
            pageScope = getPageScope(),
            viewModel = viewModel
        )
    }
}