package com.amity.socialcloud.uikit.community.compose.community.pending.elements


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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.amity.socialcloud.sdk.model.social.post.AmityPost
import com.amity.socialcloud.uikit.common.common.readableSocialTimeDiff
import com.amity.socialcloud.uikit.common.ui.base.AmityBaseElement
import com.amity.socialcloud.uikit.common.ui.elements.AmityUserAvatarView
import com.amity.socialcloud.uikit.common.ui.scope.AmityComposeComponentScope
import com.amity.socialcloud.uikit.common.ui.scope.AmityComposePageScope
import com.amity.socialcloud.uikit.common.ui.theme.AmityTheme
import com.amity.socialcloud.uikit.common.utils.clickableWithoutRipple
import com.amity.socialcloud.uikit.community.compose.AmitySocialBehaviorHelper
import com.amity.socialcloud.uikit.community.compose.R

@Composable
fun AmityPendingPostHeaderElement(
    modifier: Modifier = Modifier,
    pageScope: AmityComposePageScope? = null,
    componentScope: AmityComposeComponentScope? = null,
    post: AmityPost,
    hideMenuButton: Boolean,
    onMenuClick: (AmityPost) -> Unit = {},
) {
    val context = LocalContext.current
    val behavior by lazy {
        AmitySocialBehaviorHelper.pendingPostContentComponentBehavior
    }

    Column(
        modifier = modifier
            .fillMaxWidth()
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = modifier
                .fillMaxWidth()
                .padding(start = 12.dp, end = 16.dp, top = 12.dp, bottom = 4.dp)
        ) {
            AmityUserAvatarView(
                user = post.getCreator(),
                modifier = modifier.clickableWithoutRipple {
                    post.getCreator()?.let {
                        behavior.goToUserProfilePage(
                            context = context,
                            userId = it.getUserId(),
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
                        style = AmityTheme.typography.bodyLegacy.copy(
                            fontWeight = FontWeight.SemiBold
                        ),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = modifier
                            .weight(1f, fill = false)
                            .clickableWithoutRipple {
                                post
                                    .getCreator()
                                    ?.let {
                                        behavior.goToUserProfilePage(
                                            context = context,
                                            userId = it.getUserId(),
                                        )
                                    }
                            }
                    )

                    val isBrandCreator = post.getCreator()?.isBrand() == true
                    if (isBrandCreator) {
                        val badge = R.drawable.amity_ic_brand_badge
                        Image(
                            painter = painterResource(id = badge),
                            contentDescription = "",
                            modifier = Modifier
                                .size(18.dp)
                                .testTag("post_header/brand_user_icon")
                        )
                    }
                }

                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    AmityBaseElement(
                        pageScope = pageScope,
                        componentScope = componentScope,
                        elementId = "timestamp"
                    ) {
                        Text(
                            text = (post.getCreatedAt()?.readableSocialTimeDiff() ?: "")
                                    + if (post.isEdited()) " (edited)" else "",
                            style = AmityTheme.typography.captionLegacy.copy(
                                fontWeight = FontWeight.Normal,
                                color = AmityTheme.colors.baseShade2
                            ),
                            modifier = modifier.testTag(getAccessibilityId()),
                        )
                    }
                }
            }

            if (!hideMenuButton) {
                Icon(
                    painter = painterResource(id = R.drawable.amity_ic_more_horiz),
                    contentDescription = null,
                    tint = AmityTheme.colors.base,
                    modifier = modifier
                        .size(24.dp)
                        .align(Alignment.CenterVertically)
                        .clickableWithoutRipple { onMenuClick(post) }
                )
            }
        }
    }
}