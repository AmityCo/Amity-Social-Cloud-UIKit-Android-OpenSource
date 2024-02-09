package com.amity.socialcloud.uikit.community.compose.story.view.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.LocalViewModelStoreOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import com.amity.socialcloud.sdk.model.social.story.AmityStory
import com.amity.socialcloud.uikit.common.common.readableNumber
import com.amity.socialcloud.uikit.community.compose.R
import com.amity.socialcloud.uikit.community.compose.comment.AmityStoryCommentBottomSheet
import com.amity.socialcloud.uikit.community.compose.story.view.AmityViewStoryPageViewModel
import com.amity.socialcloud.uikit.community.compose.story.view.elements.AmityStoryCommentCountElement
import com.amity.socialcloud.uikit.community.compose.story.view.elements.AmityStoryReactionCountElement
import com.amity.socialcloud.uikit.community.compose.story.view.elements.AmityStoryViewCountElement
import com.amity.socialcloud.uikit.community.compose.ui.elements.AmityAlertDialogWithThreeActions
import com.amity.socialcloud.uikit.community.compose.ui.scope.AmityComposePageScope

@Composable
fun AmityStoryBottomRow(
    modifier: Modifier = Modifier,
    pageScope: AmityComposePageScope,
    story: AmityStory,
    onDeleteClicked: (String) -> Unit
) {
    val viewModelStoreOwner = checkNotNull(LocalViewModelStoreOwner.current) {
        "No ViewModelStoreOwner was provided via LocalViewModelStoreOwner"
    }
    val viewModel =
        viewModel<AmityViewStoryPageViewModel>(viewModelStoreOwner = viewModelStoreOwner)

    val community = remember(viewModel, story.getStoryId()) {
        viewModel.getPostedCommunity(story)
    }

    val isCommunityJoined = remember(viewModel, community) {
        community?.isJoined() == true
    }

    val isAllowedComment = remember(viewModel, community) {
        community?.getStorySettings()?.allowComment == true
    }

    Box(
        modifier = modifier.height(56.dp)
    ) {
        when (story.getState()) {
            AmityStory.State.SYNCED -> AmityStoryEngagementRow(
                modifier = modifier,
                pageScope = pageScope,
                storyId = story.getStoryId(),
                isCommunityJoined = isCommunityJoined,
                isAllowedComment = isAllowedComment,
                reachCount = story.getReach(),
                commentCount = story.getCommentCount(),
                reactionCount = story.getReactionCount(),
                isReactedByMe = story.getMyReactions().isNotEmpty()
            )

            AmityStory.State.SYNCING -> AmityStoryUploadProgressRow(modifier)
            AmityStory.State.FAILED -> AmityStoryUploadFailedRow(
                modifier = modifier,
                storyId = story.getStoryId(),
                onDeleteClicked = onDeleteClicked
            )
        }
    }
}

@Composable
fun AmityStoryEngagementRow(
    modifier: Modifier = Modifier,
    pageScope: AmityComposePageScope? = null,
    storyId: String,
    isCommunityJoined: Boolean,
    isAllowedComment: Boolean,
    reachCount: Int = 0,
    commentCount: Int = 0,
    reactionCount: Int = 0,
    isReactedByMe: Boolean = false,
) {
    val viewModelStoreOwner = checkNotNull(LocalViewModelStoreOwner.current) {
        "No ViewModelStoreOwner was provided via LocalViewModelStoreOwner"
    }
    val viewModel =
        viewModel<AmityViewStoryPageViewModel>(viewModelStoreOwner = viewModelStoreOwner)
    var showCommentSheet by remember { mutableStateOf(false) }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .background(Color.Black)
            .padding(horizontal = 12.dp, vertical = 8.dp)
    ) {
        AmityStoryViewCountElement(
            count = reachCount.readableNumber(),
            modifier = Modifier.align(Alignment.CenterStart)
        ) {

        }

        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.align(Alignment.CenterEnd)
        ) {
            AmityStoryCommentCountElement(
                count = commentCount,
                modifier = modifier,
            ) {
                showCommentSheet = true
                viewModel.handleSegmentTimer(shouldPause = true)
            }

            AmityStoryReactionCountElement(
                pageScope = pageScope,
                count = reactionCount,
                isCommunityJoined = isCommunityJoined,
                isReactedByMe = isReactedByMe,
                modifier = modifier,
            ) { isReacted ->
                if (isReacted) {
                    viewModel.addReaction(storyId)
                } else {
                    viewModel.removeReaction(storyId)
                }
            }
        }

        if (showCommentSheet) {
            AmityStoryCommentBottomSheet(
                modifier = modifier,
                storyId = storyId,
                shouldAllowInteraction = isCommunityJoined,
                shouldAllowComment = isAllowedComment,
            ) {
                showCommentSheet = false
                viewModel.handleSegmentTimer(shouldPause = false)
            }
        }
    }
}

@Composable
fun AmityStoryUploadProgressRow(
    modifier: Modifier = Modifier,
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .fillMaxWidth()
            .background(Color.Black)
            .padding(16.dp)
    ) {
        CircularProgressIndicator(
            color = Color(0xFF1054DE),
            trackColor = Color.White,
            modifier = modifier.size(20.dp),
            strokeWidth = 2.dp
        )

        Text(
            text = "Trying to reupload...",
            color = Color.White
        )
    }
}

@Composable
fun AmityStoryUploadFailedRow(
    modifier: Modifier = Modifier,
    storyId: String,
    onDeleteClicked: (String) -> Unit
) {
    val viewModelStoreOwner = checkNotNull(LocalViewModelStoreOwner.current) {
        "No ViewModelStoreOwner was provided via LocalViewModelStoreOwner"
    }
    val viewModel =
        viewModel<AmityViewStoryPageViewModel>(viewModelStoreOwner = viewModelStoreOwner)

    val openAlertDialog = remember { mutableStateOf(false) }

    if (openAlertDialog.value) {
        viewModel.handleSegmentTimer(shouldPause = true)

        AmityAlertDialogWithThreeActions(
            dialogTitle = "Failed to upload story",
            dialogText = "Would you like to discard or retry uploading?",
            dismissText = "CANCEL",
            action1Text = "DISCARD",
            action2Text = "RETRY",
            onAction1 = {
                openAlertDialog.value = false
                onDeleteClicked(storyId)
            },
            onAction2 = {
                openAlertDialog.value = false
                viewModel.handleSegmentTimer(shouldPause = false)
                viewModel.reuploadStory(storyId = storyId)
            },
            onDismissRequest = {
                openAlertDialog.value = false
                viewModel.handleSegmentTimer(shouldPause = false)
            }
        )
    }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .background(Color(0xFFFA4D30))
            .padding(horizontal = 16.dp, vertical = 12.dp)
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically,
            modifier = modifier.align(Alignment.CenterStart)
        ) {
            Icon(
                painter = painterResource(id = R.drawable.amity_ic_warning),
                tint = Color.White,
                contentDescription = null,
                modifier = modifier.size(16.dp)
            )

            Text(
                text = "Failed to upload",
                color = Color.White
            )
        }

        Icon(
            painter = painterResource(id = R.drawable.amity_ic_more_horiz),
            tint = Color.White,
            contentDescription = null,
            modifier = modifier
                .size(20.dp)
                .align(Alignment.CenterEnd)
                .clickable {
                    openAlertDialog.value = true
                }
        )
    }
}

@Preview
@Composable
fun AmityStoryBottomRowPreview() {
    AmityStoryEngagementRow(
        storyId = "",
        isCommunityJoined = false,
        isAllowedComment = false,
        reachCount = 1000,
        commentCount = 10000000,
        reactionCount = 1000000000,
    )
}