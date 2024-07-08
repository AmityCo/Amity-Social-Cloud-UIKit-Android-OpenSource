package com.amity.socialcloud.uikit.community.compose.post.detail.elements

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.LocalViewModelStoreOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import com.amity.socialcloud.sdk.model.social.post.AmityPost
import com.amity.socialcloud.uikit.common.common.readableSocialTimeDiff
import com.amity.socialcloud.uikit.common.ui.base.AmityBaseElement
import com.amity.socialcloud.uikit.common.ui.elements.AmityAvatarView
import com.amity.socialcloud.uikit.common.ui.scope.AmityComposeComponentScope
import com.amity.socialcloud.uikit.common.ui.theme.AmityTheme
import com.amity.socialcloud.uikit.common.utils.clickableWithoutRipple
import com.amity.socialcloud.uikit.common.utils.getIcon
import com.amity.socialcloud.uikit.community.compose.AmitySocialBehaviorHelper
import com.amity.socialcloud.uikit.community.compose.R
import com.amity.socialcloud.uikit.community.compose.post.detail.AmityPostDetailPageViewModel

@Composable
fun AmityPostHeaderElement(
    modifier: Modifier = Modifier,
    componentScope: AmityComposeComponentScope? = null,
    post: AmityPost,
    isPostDetailPage: Boolean,
    onMenuClick: (AmityPost) -> Unit = {}
) {
    val context = LocalContext.current
    val behavior by lazy {
        AmitySocialBehaviorHelper.postContentComponentBehavior
    }

    val viewModelStoreOwner = checkNotNull(LocalViewModelStoreOwner.current) {
        "No ViewModelStoreOwner was provided via LocalViewModelStoreOwner"
    }
    val viewModel =
        viewModel<AmityPostDetailPageViewModel>(viewModelStoreOwner = viewModelStoreOwner)

    val isCommunityModerator = remember(post) {
        viewModel.isCommunityModerator(post)
    }

    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        modifier = modifier
            .fillMaxWidth()
            .padding(start = 12.dp, end = 16.dp, top = 12.dp, bottom = 4.dp)
    ) {
        AmityAvatarView(
            size = 32.dp,
            avatarUrl = post.getCreator()?.getAvatar()?.getUrl(),
            modifier = modifier.clickableWithoutRipple {
                post.getCreator()?.let {
                    behavior.goToUserProfilePage(
                        context = context,
                        user = it
                    )
                }
            }
        )

        Column(
            modifier = modifier
                .weight(1f)
                .padding(start = 4.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = post.getCreator()?.getDisplayName() ?: "",
                    style = AmityTheme.typography.body.copy(
                        fontWeight = FontWeight.SemiBold
                    ),
                    modifier = modifier.clickableWithoutRipple {
                        post
                            .getCreator()
                            ?.let {
                                behavior.goToUserProfilePage(
                                    context = context,
                                    user = it
                                )
                            }
                    }
                )

                when (val target = post.getTarget()) {
                    is AmityPost.Target.COMMUNITY -> {
                        Icon(
                            painter = painterResource(id = R.drawable.amity_ic_post_target),
                            contentDescription = null,
                            tint = AmityTheme.colors.baseShade1,
                            modifier = modifier.size(16.dp),
                        )
                        Text(
                            text = target.getCommunity()?.getDisplayName() ?: "",
                            style = AmityTheme.typography.body.copy(
                                fontWeight = FontWeight.SemiBold
                            ),
                            modifier = modifier.clickableWithoutRipple {
                                target.getCommunity()?.let {
                                    behavior.goToCommunityProfilePage(
                                        context = context,
                                        community = it
                                    )
                                }
                            }
                        )

                        if (target.getCommunity()?.isOfficial() == true) {
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

                    is AmityPost.Target.USER -> {
                        if (target.getUser()?.getUserId() != post.getCreatorId()) {
                            Icon(
                                painter = painterResource(id = R.drawable.amity_ic_post_target),
                                contentDescription = null,
                                tint = AmityTheme.colors.baseShade1,
                                modifier = modifier.size(16.dp),
                            )
                            Text(
                                text = target.getUser()?.getDisplayName() ?: "",
                                style = AmityTheme.typography.body.copy(
                                    fontWeight = FontWeight.SemiBold
                                ),
                                modifier = modifier.clickableWithoutRipple {
                                    target.getUser()?.let {
                                        behavior.goToUserProfilePage(
                                            context = context,
                                            user = it
                                        )
                                    }
                                }
                            )
                        }
                    }

                    AmityPost.Target.UNKNOWN -> {

                    }
                }
            }

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (isCommunityModerator) {
                    AmityPostModeratorBadge(
                        modifier = modifier,
                        componentScope = componentScope,
                    )

                    Text(
                        text = " • ",
                        style = AmityTheme.typography.caption.copy(
                            fontWeight = FontWeight.Normal,
                            color = AmityTheme.colors.baseShade2
                        )
                    )
                }

                AmityBaseElement(
                    componentScope = componentScope,
                    elementId = "timestamp"
                ) {
                    Text(
                        text = (post.getCreatedAt()?.readableSocialTimeDiff() ?: "")
                                + if (post.isEdited()) " (edited)" else "",
                        style = AmityTheme.typography.caption.copy(
                            fontWeight = FontWeight.Normal,
                            color = AmityTheme.colors.baseShade2
                        ),
                        modifier = modifier.testTag(getAccessibilityId()),
                    )
                }
            }
        }

        if (!isPostDetailPage) {
            AmityBaseElement(
                componentScope = componentScope,
                elementId = "menu_button"
            ) {
                Icon(
                    painter = painterResource(id = getConfig().getIcon()),
                    contentDescription = null,
                    tint = AmityTheme.colors.base,
                    modifier = modifier
                        .size(24.dp)
                        .align(Alignment.CenterVertically)
                        .clickableWithoutRipple { onMenuClick(post) }
                        .testTag(getAccessibilityId()),
                )
            }
        }
    }
}