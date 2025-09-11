package com.amity.socialcloud.uikit.community.compose.clip.view.element

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.waterfall
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.unit.dp
import com.amity.socialcloud.sdk.model.social.comment.AmityCommentReferenceType
import com.amity.socialcloud.uikit.common.common.isNotEmptyOrBlank
import com.amity.socialcloud.uikit.common.config.AmityUIKitConfigController
import com.amity.socialcloud.uikit.common.eventbus.AmityUIKitSnackbar
import com.amity.socialcloud.uikit.common.ui.elements.AmityBottomSheetActionItem
import com.amity.socialcloud.uikit.common.ui.theme.AmityTheme
import com.amity.socialcloud.uikit.community.compose.R
import com.amity.socialcloud.uikit.community.compose.clip.view.AmityClipFeedPageViewModel
import com.amity.socialcloud.uikit.community.compose.clip.view.AmityClipModalSheetUIState
import com.amity.socialcloud.uikit.community.compose.clip.view.AmityClipPageBehavior
import com.amity.socialcloud.uikit.community.compose.comment.AmityCommentTrayComponent
import com.amity.socialcloud.uikit.community.compose.utils.sharePost
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.text.replace


@OptIn(ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class)
@Composable
fun AmityClipModalBottomSheet(
    modifier: Modifier = Modifier,
    behavior: AmityClipPageBehavior,
    viewModel: AmityClipFeedPageViewModel,
) {
    val context = LocalContext.current

    val clipboardManager = LocalClipboardManager.current

    val scope = rememberCoroutineScope()

    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val sheetUIState by viewModel.sheetUIState.collectAsState()

    val showSheet by remember(viewModel) {
        derivedStateOf {
            sheetUIState != AmityClipModalSheetUIState.CloseSheet
        }
    }

    LaunchedEffect(showSheet) {
        delay(100)

        if (!showSheet) {
            scope.launch { sheetState.hide() }
        }
    }

    if (showSheet) {
        ModalBottomSheet(
            onDismissRequest = {
                viewModel.updateSheetUIState(AmityClipModalSheetUIState.CloseSheet)
            },
            sheetState = sheetState,
            containerColor = AmityTheme.colors.background,
            contentWindowInsets = { WindowInsets.waterfall },
            modifier = modifier.statusBarsPadding()
        ) {
            when (sheetUIState) {
                is AmityClipModalSheetUIState.OpenCommentTraySheet -> {
                    val data = sheetUIState as AmityClipModalSheetUIState.OpenCommentTraySheet
                    AmityCommentTrayComponent(
                        modifier = modifier,
                        referenceId = data.postId,
                        referenceType = AmityCommentReferenceType.POST,
                        community = data.community,
                        shouldAllowInteraction = data.shouldAllowInteraction,
                        shouldAllowCreation = data.shouldAllowComment,
                    )
                }

                is AmityClipModalSheetUIState.OpenClipMenuSheet -> {
                    val data = sheetUIState as AmityClipModalSheetUIState.OpenClipMenuSheet
                    Column(
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = modifier
                            .padding(start = 16.dp, end = 16.dp, bottom = 64.dp)
                    ) {
                        AmityBottomSheetActionItem(
                            icon = R.drawable.amity_v4_bottom_sheet_view_post,
                            text = "View post",
                            modifier = modifier,
                        ) {
                            viewModel.updateSheetUIState(AmityClipModalSheetUIState.CloseSheet)
                            behavior.goToPostDetailPage(
                                context = context,
                                postId = data.post.getPostId()
                            )
                        }

                        val postLink = AmityUIKitConfigController.getPostLink(data.post)
                        if (postLink.isNotEmptyOrBlank()) {
                            AmityBottomSheetActionItem(
                                icon = R.drawable.amity_v4_link_icon,
                                text = "Copy post link",
                                modifier = modifier.testTag("bottom_sheet_copy_link_button"),
                            ) {
                                viewModel.updateSheetUIState(AmityClipModalSheetUIState.CloseSheet)
                                // Generate the post link URL (adjust the URL format according to your app's deep linking structure)
                                // Copy to clipboard
                                clipboardManager.setText(AnnotatedString(postLink))
                                AmityUIKitSnackbar.publishSnackbarMessage("Link copied")
                            }

                            AmityBottomSheetActionItem(
                                icon = R.drawable.amity_v4_share_icon,
                                text = "Share to",
                                modifier = modifier.testTag("bottom_sheet_share_to_button"),
                            ) {
                                viewModel.updateSheetUIState(AmityClipModalSheetUIState.CloseSheet)
                                // Open native Android share sheet
                                sharePost(context, postLink)
                            }
                        }
                    }
                }

                AmityClipModalSheetUIState.CloseSheet -> {}
            }
        }
    }
}