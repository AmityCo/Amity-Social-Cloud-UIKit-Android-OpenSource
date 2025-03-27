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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.LocalViewModelStoreOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import com.amity.socialcloud.sdk.model.social.story.AmityStory
import com.amity.socialcloud.sdk.model.social.story.AmityStoryTarget
import com.amity.socialcloud.uikit.common.common.readableNumber
import com.amity.socialcloud.uikit.common.ui.elements.AmityAlertDialog
import com.amity.socialcloud.uikit.common.ui.scope.AmityComposePageScope
import com.amity.socialcloud.uikit.common.ui.theme.AmityTheme
import com.amity.socialcloud.uikit.community.compose.R
import com.amity.socialcloud.uikit.community.compose.story.view.AmityStoryModalDialogUIState
import com.amity.socialcloud.uikit.community.compose.story.view.AmityStoryModalSheetUIState
import com.amity.socialcloud.uikit.community.compose.story.view.AmityViewStoryPageViewModel
import com.amity.socialcloud.uikit.community.compose.story.view.elements.AmityStoryCommentCountElement
import com.amity.socialcloud.uikit.community.compose.story.view.elements.AmityStoryReactionCountElement
import com.amity.socialcloud.uikit.community.compose.story.view.elements.AmityStoryViewCountElement
import com.amity.socialcloud.uikit.common.compose.R as CommonR

@Composable
fun AmityStoryBottomRow(
    modifier: Modifier = Modifier,
    pageScope: AmityComposePageScope,
    storyId: String,
    story: AmityStory,
    target: AmityStoryTarget?,
    state: AmityStory.State,
    reachCount: Int,
    commentCount: Int,
    reactionCount: Int,
    isReactedByMe: Boolean,
    isCreatedByMe: Boolean,
    hasModeratorRole: Boolean,
) {
    val viewModelStoreOwner = checkNotNull(LocalViewModelStoreOwner.current) {
        "No ViewModelStoreOwner was provided via LocalViewModelStoreOwner"
    }
    val viewModel =
        viewModel<AmityViewStoryPageViewModel>(viewModelStoreOwner = viewModelStoreOwner)

    val community = remember(viewModel, target?.getTargetId()) {
        if (target is AmityStoryTarget.COMMUNITY) {
            target.getCommunity()
        } else {
            null
        }
    }

    val isCommunityJoined = remember(viewModel, community?.getCommunityId()) {
        community?.isJoined() == true
    }

    val isAllowedComment = remember(viewModel, community?.getCommunityId()) {
        community?.getStorySettings()?.allowComment == true
    }

    Box(
        modifier = modifier.height(56.dp)
    ) {
        when (state) {
            AmityStory.State.SYNCED -> AmityStoryEngagementRow(
                modifier = modifier,
                pageScope = pageScope,
                storyId = storyId,
                isCommunityJoined = isCommunityJoined,
                isAllowedComment = isAllowedComment,
                reachCount = reachCount,
                commentCount = commentCount,
                reactionCount = reactionCount,
                isReactedByMe = isReactedByMe,
                isCreatedByMe = isCreatedByMe,
                hasModeratorRole = hasModeratorRole,
            )

            AmityStory.State.SYNCING -> AmityStoryUploadProgressRow(modifier)
            AmityStory.State.FAILED -> AmityStoryUploadFailedRow(
                modifier = modifier,
                pageScope = pageScope,
                storyId = storyId,
                story = story,
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
    isCreatedByMe: Boolean,
    hasModeratorRole: Boolean,
) {
    val viewModelStoreOwner = checkNotNull(LocalViewModelStoreOwner.current) {
        "No ViewModelStoreOwner was provided via LocalViewModelStoreOwner"
    }
    val viewModel =
        viewModel<AmityViewStoryPageViewModel>(viewModelStoreOwner = viewModelStoreOwner)

    Box(
        modifier = modifier
            .fillMaxWidth()
            .background(Color.Black)
            .padding(horizontal = 12.dp, vertical = 8.dp)
    ) {

        if (hasModeratorRole || isCreatedByMe) {
            AmityStoryViewCountElement(
                count = reachCount.readableNumber(),
                modifier = Modifier.align(Alignment.CenterStart)
            )
        }

        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.align(Alignment.CenterEnd)
        ) {
            AmityStoryCommentCountElement(
                count = commentCount,
                modifier = modifier,
            ) {
                viewModel.updateSheetUIState(
                    AmityStoryModalSheetUIState.OpenCommentTraySheet(
                        storyId = storyId,
                        community = viewModel.community,
                        shouldAllowInteraction = isCommunityJoined,
                        shouldAllowComment = isAllowedComment,
                    )
                )
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
            color = AmityTheme.colors.primary,
            trackColor = Color.White,
            modifier = modifier.size(20.dp),
            strokeWidth = 2.dp
        )

        Text(
            text = "Uploading...",
            color = Color.White
        )
    }
}

@Composable
fun AmityStoryUploadFailedRow(
    modifier: Modifier = Modifier,
    pageScope: AmityComposePageScope? = null,
    storyId: String,
    story: AmityStory,
) {
    val viewModelStoreOwner = checkNotNull(LocalViewModelStoreOwner.current) {
        "No ViewModelStoreOwner was provided via LocalViewModelStoreOwner"
    }
    val viewModel =
        viewModel<AmityViewStoryPageViewModel>(viewModelStoreOwner = viewModelStoreOwner)

    val openAlertDialog = remember { mutableStateOf(false) }

    if (openAlertDialog.value) {
        viewModel.handleSegmentTimer(shouldPause = true)

        AmityAlertDialog(
            dialogTitle = "Failed to upload story",
            dialogText = "Would you like to discard or retry uploading?",
            dismissText = "Cancel",
            action1Text = "Discard",
            action2Text = "Retry",
            onAction1 = {
                openAlertDialog.value = false
                viewModel.updateDialogUIState(
                    AmityStoryModalDialogUIState.OpenConfirmDeleteDialog(storyId)
                )
            },
            onAction2 = {
                openAlertDialog.value = false
                viewModel.handleSegmentTimer(shouldPause = false)
                viewModel.reUploadStory(
                    storyId = storyId,
                    story = story,
                    onSuccess = {
                        pageScope?.showSnackbar(
                            drawableRes = CommonR.drawable.amity_ic_check_circle,
                            message = "Successfully shared story"
                        )
                    },
                    onError = {
                        pageScope?.showSnackbar(
                            message = it.message ?: "Failed to reupload story",
                        )
                    }
                )
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
            .background(AmityTheme.colors.alert)
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
        isCreatedByMe = true,
        hasModeratorRole = false,
    )
}