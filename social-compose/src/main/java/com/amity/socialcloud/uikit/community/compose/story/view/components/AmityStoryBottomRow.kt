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
import com.amity.socialcloud.uikit.community.compose.R
import com.amity.socialcloud.uikit.community.compose.story.view.AmityViewStoryPageViewModel
import com.amity.socialcloud.uikit.community.compose.story.view.elements.AmityStoryCommentCountElement
import com.amity.socialcloud.uikit.community.compose.story.view.elements.AmityStoryReactionCountElement
import com.amity.socialcloud.uikit.community.compose.story.view.elements.AmityStoryViewCountElement
import com.amity.socialcloud.uikit.community.compose.ui.elements.AmityAlertDialogWithThreeActions

@Composable
fun AmityStoryBottomRow(
    modifier: Modifier = Modifier,
    story: AmityStory,
    onDeleteClicked: (String) -> Unit
) {
    Box(
        modifier = modifier.height(56.dp)
    ) {
        when (story.getState()) {
            AmityStory.State.SYNCED -> AmityStoryEngagementRow(
                modifier = modifier,
                reachCount = story.getReach().toString(),
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
    reachCount: String = "0",
    commentCount: String = "0",
    reactionCount: String = "0",
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .background(Color.Black)
            .padding(horizontal = 12.dp, vertical = 8.dp)
    ) {
        AmityStoryViewCountElement(
            count = reachCount,
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

            }

            AmityStoryReactionCountElement(
                count = reactionCount,
                isSelected = false,
                modifier = modifier,
            ) {

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
    AmityStoryEngagementRow()
}