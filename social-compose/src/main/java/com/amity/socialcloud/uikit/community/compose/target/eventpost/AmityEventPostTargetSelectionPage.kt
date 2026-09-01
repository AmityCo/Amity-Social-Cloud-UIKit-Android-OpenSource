package com.amity.socialcloud.uikit.community.compose.target.eventpost

import android.app.Activity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rxjava3.subscribeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.LocalViewModelStoreOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import com.amity.socialcloud.sdk.api.core.AmityCoreClient
import com.amity.socialcloud.sdk.model.social.community.AmityCommunity
import com.amity.socialcloud.sdk.model.social.event.AmityEvent
import com.amity.socialcloud.sdk.model.social.event.AmityEventOriginType
import com.amity.socialcloud.uikit.common.ui.base.AmityBaseElement
import com.amity.socialcloud.uikit.common.ui.base.AmityBasePage
import com.amity.socialcloud.uikit.common.ui.elements.AmityCommunityAvatarView
import com.amity.socialcloud.uikit.common.ui.elements.AmityUserAvatarView
import com.amity.socialcloud.uikit.common.ui.theme.AmityTheme
import com.amity.socialcloud.uikit.common.utils.clickableWithoutRipple
import com.amity.socialcloud.uikit.common.utils.closePage
import com.amity.socialcloud.uikit.common.utils.closePageWithResult
import com.amity.socialcloud.uikit.common.utils.getIcon
import com.amity.socialcloud.uikit.community.compose.AmitySocialBehaviorHelper
import com.amity.socialcloud.uikit.common.R as CommonR
import com.amity.socialcloud.uikit.community.compose.localization.amitySocialConfigString
import com.amity.socialcloud.uikit.community.compose.localization.amitySocialString
import com.amity.socialcloud.uikit.community.compose.post.composer.AmityPostTargetType
import com.amity.socialcloud.uikit.community.compose.target.AmityTargetSelectionPageViewModel
import com.amity.socialcloud.uikit.community.compose.target.components.AmityTargetContentType
import com.amity.socialcloud.uikit.community.compose.target.components.AmityTargetSelectionMyCommunitiesView

/**
 * Picks where a post referencing [event] is published, before the composer opens.
 *
 * The page is presented for every event-post create flow and renders one of two variants, decided
 * by where the event was created:
 *
 * - **Full** — public-community or user-hosted event. "My Timeline" plus the paginated list of
 *   communities the user may post in.
 * - **Restricted** — the event was created in a private community, so the post can only go back
 *   into that community. A banner explains the constraint, "My Timeline" is hidden, and the list is
 *   reduced to that single community with a Private lock badge.
 *
 * The restricted variant is guidance, not the enforcement point — the server rejects a disallowed
 * target with a 400 regardless. When the origin signal cannot be read the full variant renders
 * defensively rather than blocking the flow.
 */
@Composable
fun AmityEventPostTargetSelectionPage(
    modifier: Modifier = Modifier,
    event: AmityEvent,
) {
    val context = LocalContext.current
    val behavior by lazy {
        AmitySocialBehaviorHelper.eventPostTargetSelectionPageBehavior
    }

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) {
        context.closePageWithResult(Activity.RESULT_OK)
    }

    val viewModelStoreOwner = checkNotNull(LocalViewModelStoreOwner.current) {
        "No ViewModelStoreOwner was provided via LocalViewModelStoreOwner"
    }
    val viewModel =
        viewModel<AmityTargetSelectionPageViewModel>(viewModelStoreOwner = viewModelStoreOwner)

    val currentUser by remember(viewModel) {
        viewModel.getCurrentUser()
    }.subscribeAsState(null)

    // "Created in a private community" is read straight off the event object. A user-hosted event
    // has no origin community and therefore never restricts.
    val isPrivateCommunityEvent =
        event.getOriginType() == AmityEventOriginType.COMMUNITY && !event.isOriginPublic()
    val originCommunityId = event.getTargetCommunity()?.getCommunityId()

    val selectMyTimeline: () -> Unit = {
        behavior.goToPostComposerPage(
            context = context,
            launcher = launcher,
            event = event,
            targetId = AmityCoreClient.getUserId(),
            targetType = AmityPostTargetType.USER,
            community = null,
        )
    }
    val selectCommunity: (AmityCommunity) -> Unit = { community ->
        behavior.goToPostComposerPage(
            context = context,
            launcher = launcher,
            event = event,
            targetId = community.getCommunityId(),
            targetType = AmityPostTargetType.COMMUNITY,
            community = community,
        )
    }

    AmityBasePage(
        "event_post_target_selection_page"
    ) {
        Column(
            modifier = modifier.fillMaxSize()
        ) {
            Box(
                modifier = modifier
                    .height(58.dp)
                    .fillMaxWidth()
                    .padding(start = 12.dp),
            ) {
                AmityBaseElement(
                    pageScope = getPageScope(),
                    elementId = "close_button"
                ) {
                    Icon(
                        painter = painterResource(getConfig().getIcon()),
                        contentDescription = "Close Button",
                        tint = AmityTheme.colors.base,
                        modifier = modifier
                            .size(16.dp)
                            .align(Alignment.CenterStart)
                            .clickableWithoutRipple {
                                context.closePage()
                            }
                            .testTag(getAccessibilityId()),
                    )
                }
                AmityBaseElement(
                    pageScope = getPageScope(),
                    elementId = "title"
                ) {
                    Text(
                        text = amitySocialConfigString("amity_social_button_post_to"),
                        style = AmityTheme.typography.titleLegacy,
                        modifier = modifier
                            .align(Alignment.Center)
                            .testTag(getAccessibilityId()),
                    )
                }
            }

            if (isPrivateCommunityEvent) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = modifier
                        .fillMaxWidth()
                        .background(AmityTheme.colors.baseShade4)
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                ) {
                    Text(
                        text = amitySocialString("amity_social_label_private_event_target_banner"),
                        style = AmityTheme.typography.captionLegacy,
                        color = AmityTheme.colors.baseShade1,
                    )
                }

                val lockedCommunity by remember(originCommunityId) {
                    if (originCommunityId != null) {
                        viewModel.getCommunity(originCommunityId)
                    } else {
                        io.reactivex.rxjava3.core.Flowable.empty()
                    }
                }.subscribeAsState(null)

                Text(
                    text = amitySocialString("amity_social_button_my_communities"),
                    style = AmityTheme.typography.bodyLegacy.copy(
                        color = AmityTheme.colors.base.copy(alpha = 0.4f),
                    ),
                    modifier = modifier.padding(start = 16.dp, end = 16.dp, bottom = 8.dp, top = 16.dp),
                )

                lockedCommunity?.let { community ->
                    AmityEventLockedCommunityRow(
                        modifier = modifier,
                        community = community,
                        onClick = { selectCommunity(community) },
                    )
                }
            } else {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp)
                        .clickableWithoutRipple { selectMyTimeline() },
                ) {
                    AmityBaseElement(
                        pageScope = getPageScope(),
                        elementId = "my_timeline_avatar"
                    ) {
                        AmityUserAvatarView(
                            size = 40.dp,
                            modifier = modifier.testTag(getAccessibilityId()),
                            user = currentUser,
                        )
                    }

                    AmityBaseElement(
                        pageScope = getPageScope(),
                        elementId = "my_timeline_text"
                    ) {
                        Text(
                            text = amitySocialConfigString("amity_social_button_select_poll_target_my_timeline"),
                            style = AmityTheme.typography.bodyLegacy.copy(
                                fontWeight = FontWeight.SemiBold
                            ),
                            modifier = modifier.testTag(getAccessibilityId()),
                        )
                    }
                }

                HorizontalDivider(
                    color = AmityTheme.colors.divider,
                    modifier = modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                )

                AmityTargetSelectionMyCommunitiesView(
                    modifier = modifier,
                    contentType = AmityTargetContentType.POST,
                    onClick = selectCommunity,
                )
            }
        }
    }
}

/**
 * The single community row rendered by the restricted variant, where the event's origin community
 * is the only valid target. Mirrors the row from [AmityTargetSelectionMyCommunitiesView].
 */
@Composable
private fun AmityEventLockedCommunityRow(
    modifier: Modifier = Modifier,
    community: AmityCommunity,
    onClick: () -> Unit,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .clickableWithoutRipple { onClick() },
    ) {
        AmityCommunityAvatarView(
            modifier = modifier,
            size = 40.dp,
            community = community,
        )
        Row(
            horizontalArrangement = Arrangement.spacedBy(4.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            if (!community.isPublic()) {
                Icon(
                    painter = painterResource(CommonR.drawable.amity_ic_lock1),
                    tint = AmityTheme.colors.base,
                    contentDescription = "Private Community",
                    modifier = Modifier.size(20.dp),
                )
            }
            Text(
                text = community.getDisplayName(),
                style = AmityTheme.typography.bodyLegacy.copy(
                    fontWeight = FontWeight.SemiBold
                ),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
        }
    }
}
