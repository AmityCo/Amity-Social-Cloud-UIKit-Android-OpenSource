package com.amity.socialcloud.uikit.community.compose.user.profile.elements

import android.app.Activity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.waterfall
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.testTagsAsResourceId
import androidx.compose.ui.unit.dp
import com.amity.socialcloud.sdk.model.core.user.AmityUser
import com.amity.socialcloud.sdk.model.social.post.AmityPost
import com.amity.socialcloud.uikit.common.ui.elements.AmityBottomSheetActionItem
import com.amity.socialcloud.uikit.common.ui.theme.AmityTheme
import com.amity.socialcloud.uikit.common.utils.closePageWithResult
import com.amity.socialcloud.uikit.community.compose.AmitySocialBehaviorHelper
import com.amity.socialcloud.uikit.community.compose.R
import com.amity.socialcloud.uikit.community.compose.community.profile.AmityCommunityProfilePageBehavior
import com.amity.socialcloud.uikit.community.compose.post.composer.AmityPostTargetType

@OptIn(ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class)
@Composable
fun AmityUserActionsBottomSheet(
    modifier: Modifier = Modifier,
    user: AmityUser,
    showPollTypeSelectionSheet: () -> Unit,
    onDismiss: () -> Unit,
) {
    val context = LocalContext.current
    val behavior by lazy {
        AmitySocialBehaviorHelper.userProfilePageBehavior
    }

    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) {
        context.closePageWithResult(Activity.RESULT_OK)
    }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = AmityTheme.colors.background,
        contentWindowInsets = { WindowInsets.waterfall },
        modifier = modifier
            .semantics {
                testTagsAsResourceId = true
            },
    ) {
        Column(
            modifier = modifier
                .background(AmityTheme.colors.background)
                .padding(start = 16.dp, end = 16.dp, bottom = 64.dp)
        ) {
            AmityBottomSheetActionItem(
                icon = R.drawable.amity_ic_post_create,
                text = "Post",
                modifier = modifier,
            ) {
                onDismiss()
                behavior.goToPostComposerPage(
                    context = context,
                    userId = user.getUserId(),
                )
            }

            AmityBottomSheetActionItem(
                icon = R.drawable.ic_amity_ic_poll_create,
                text = "Poll",
                modifier = modifier,
            ) {
                onDismiss()
                showPollTypeSelectionSheet()
//                behavior.goToPollComposerPage(
//                    context = context,
//                    userId = user.getUserId(),
//                )
            }

            AmityBottomSheetActionItem(
                icon = R.drawable.ic_amity_ic_live_stream_create,
                text = "Live stream",
                modifier = modifier,
            ) {
                onDismiss()
                behavior.goToLivestreamPostComposerPage(
                    context = context,
                    targetId = user.getUserId(),
                    targetType = AmityPost.TargetType.USER,
                    community = null
                )
            }

            AmityBottomSheetActionItem(
                icon = R.drawable.amity_ic_create_clip,
                text = "Clip",
                modifier = modifier,
            ) {
                onDismiss()
                behavior.goToClipPostComposerPage(
                    context = context,
                    targetId = user.getUserId(),
                    launcher = launcher,
                    targetType = AmityPostTargetType.USER
                )
            }
        }
    }
}