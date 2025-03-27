package com.amity.socialcloud.uikit.community.compose.post.detail.elements

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.LocalViewModelStoreOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import com.amity.socialcloud.sdk.model.core.reaction.AmityReactionReferenceType
import com.amity.socialcloud.sdk.model.social.post.AmityPost
import com.amity.socialcloud.uikit.common.common.readableNumber
import com.amity.socialcloud.uikit.common.reaction.AmityReactionList
import com.amity.socialcloud.uikit.common.ui.base.AmityBaseElement
import com.amity.socialcloud.uikit.common.ui.scope.AmityComposeComponentScope
import com.amity.socialcloud.uikit.common.ui.theme.AmityTheme
import com.amity.socialcloud.uikit.common.utils.clickableWithoutRipple
import com.amity.socialcloud.uikit.common.utils.getIcon
import com.amity.socialcloud.uikit.common.utils.getText
import com.amity.socialcloud.uikit.community.compose.AmitySocialBehaviorHelper
import com.amity.socialcloud.uikit.community.compose.R
import com.amity.socialcloud.uikit.community.compose.post.detail.AmityPostDetailPageViewModel

@Composable
fun AmityPostEngagementView(
    modifier: Modifier = Modifier,
    componentScope: AmityComposeComponentScope? = null,
    post: AmityPost,
    isPostDetailPage: Boolean,
) {
    val context = LocalContext.current
    val behavior by lazy {
        AmitySocialBehaviorHelper.postContentComponentBehavior
    }
    var isReacted by remember(post.getUpdatedAt(), post.getMyReactions()) {
        mutableStateOf(post.getMyReactions().isNotEmpty())
    }
    var localReactionCount by remember(post.getUpdatedAt(), post.getReactionCount()) {
        mutableIntStateOf(post.getReactionCount())
    }

    val reactionCount = if(localReactionCount == 0) "0" else pluralStringResource(
        id = R.plurals.amity_feed_reaction_count,
        count = localReactionCount,
        localReactionCount.readableNumber()
    )

    val commentCount = pluralStringResource(
        id = R.plurals.amity_feed_comment_count,
        count = post.getCommentCount(),
        post.getCommentCount().readableNumber()
    )

    var showReactionListSheet by remember { mutableStateOf(false) }

    val viewModelStoreOwner = checkNotNull(LocalViewModelStoreOwner.current) {
        "No ViewModelStoreOwner was provided via LocalViewModelStoreOwner"
    }
    val viewModel =
        viewModel<AmityPostDetailPageViewModel>(viewModelStoreOwner = viewModelStoreOwner)

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(start = 16.dp, end = 16.dp, bottom = 0.dp, top = if (isPostDetailPage) 8.dp else 0.dp)
    ) {
        if (isPostDetailPage) {
            Box(
                modifier = modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .align(Alignment.CenterStart)
                        .clickableWithoutRipple {
                            showReactionListSheet = true
                        }
                ) {
                    Icon(
                        imageVector = ImageVector.vectorResource(id = R.drawable.amity_ic_story_liked_pressed),
                        contentDescription = "Post Reaction Count",
                        tint = Color.Unspecified,
                        modifier = Modifier.size(16.dp),
                    )
                    Spacer(modifier = modifier.width(4.dp))
                    Text(
                        text = reactionCount,
                        style = AmityTheme.typography.captionLegacy.copy(
                            fontWeight = FontWeight.Normal,
                            color = AmityTheme.colors.baseShade2
                        )
                    )
                }

                Text(
                    text = commentCount,
                    style = AmityTheme.typography.captionLegacy.copy(
                        fontWeight = FontWeight.Normal,
                        color = AmityTheme.colors.baseShade2
                    ),
                    modifier = Modifier.align(Alignment.CenterEnd)
                )
            }
        }

        HorizontalDivider(
            color = AmityTheme.colors.divider,
        )

        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
            modifier = modifier
                .fillMaxWidth()
                .padding(vertical = 12.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                AmityBaseElement(
                    componentScope = componentScope,
                    elementId = "reaction_button"
                ) {
                    Icon(
                        imageVector = if (isReacted) ImageVector.vectorResource(id = R.drawable.amity_ic_story_liked_pressed)
                        else ImageVector.vectorResource(id = R.drawable.amity_ic_story_like_normal),
                        contentDescription = "Post Reaction",
                        tint = Color.Unspecified,
                        modifier = Modifier
                            .size(20.dp)
                            .clickableWithoutRipple {
                                isReacted = !isReacted

                                if (isReacted) {
                                    localReactionCount += 1
                                } else {
                                    localReactionCount -= 1
                                }

                                viewModel.changeReaction(
                                    postId = post.getPostId(),
                                    isReacted = isReacted
                                )
                            }
                            .testTag(getAccessibilityId()),
                    )
                    Text(
                        text = if (isPostDetailPage) getConfig().getText()
                        else localReactionCount.readableNumber(),
                        style = AmityTheme.typography.bodyLegacy.copy(
                            fontWeight = FontWeight.SemiBold,
                            color = if (isReacted) AmityTheme.colors.primary
                            else AmityTheme.colors.baseShade2
                        ),
                    )
                }

                Spacer(modifier = modifier.width(8.dp))
                AmityBaseElement(
                    componentScope = componentScope,
                    elementId = "comment_button"
                ) {
                    Image(
                        painter = painterResource(id = getConfig().getIcon()),
                        contentDescription = null,
                        modifier = modifier
                            .size(20.dp)
                            .testTag(getAccessibilityId())
                    )
                    Text(
                        text = if (isPostDetailPage) getConfig().getText()
                        else post.getCommentCount().readableNumber(),
                        style = AmityTheme.typography.bodyLegacy.copy(
                            fontWeight = FontWeight.SemiBold,
                            color = AmityTheme.colors.baseShade2
                        ),
                    )
                }
            }

            /*
                AmityBaseElement(
                    componentScope = componentScope,
                    elementId = "share_button"
                ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.amity_ic_reply),
                        contentDescription = null,
                        modifier = modifier.size(20.dp)
                    )
                    Text(
                        text = getConfig().getText(),
                        style = AmityTheme.typography.body.copy(
                            fontWeight = FontWeight.SemiBold,
                            color = AmityTheme.colors.baseShade2
                        )
                    )
                }
                }

                AmityBaseElement(
                    componentScope = componentScope,
                    elementId = "post_content_view_count"
                ) { }
             */
        }

        if (showReactionListSheet) {
            AmityReactionList(
                modifier = modifier,
                referenceType = AmityReactionReferenceType.POST,
                referenceId = post.getPostId(),
                onClose = {
                    showReactionListSheet = false
                },
                onUserClick = {
                    behavior.goToUserProfilePage(
                        context = context,
                        userId = it,
                    )
                }
            )
        }
    }
}