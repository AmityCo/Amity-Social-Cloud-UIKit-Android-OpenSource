package com.amity.socialcloud.uikit.community.compose.community.profile.element

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.testTagsAsResourceId
import androidx.compose.ui.unit.dp
import com.amity.socialcloud.sdk.model.social.community.AmityCommunity
import com.amity.socialcloud.uikit.common.ui.base.AmityBaseElement
import com.amity.socialcloud.uikit.common.ui.elements.AmityBottomSheetActionItem
import com.amity.socialcloud.uikit.common.ui.scope.AmityComposeComponentScope
import com.amity.socialcloud.uikit.common.ui.theme.AmityTheme
import com.amity.socialcloud.uikit.community.compose.AmitySocialBehaviorHelper
import com.amity.socialcloud.uikit.community.compose.R
import com.amity.socialcloud.uikit.community.compose.community.profile.AmityCommunityProfilePageBehavior
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class)
@Composable
fun AmityCommunityProfileActionsBottomSheet(
    modifier: Modifier = Modifier,
    componentScope: AmityComposeComponentScope? = null,
    community: AmityCommunity,
    shouldShow: Boolean,
    shouldShowPostCreationButton: Boolean,
    shouldShowStoryCreationButton: Boolean,
    onDismiss: () -> Unit,
) {
    val sheetState = rememberModalBottomSheetState()
    val scope = rememberCoroutineScope()

    if (shouldShow) {
        ModalBottomSheet(
            onDismissRequest = {
                onDismiss()
            },
            sheetState = sheetState,
            containerColor = AmityTheme.colors.background,
            modifier = modifier.semantics {
                testTagsAsResourceId = true
            }
        ) {
            AmityCommunityProfileActionsContainer(
                modifier = modifier,
                componentScope = componentScope,
                community = community,
                shouldShowPostCreationButton = shouldShowPostCreationButton,
                shouldShowStoryCreationButton = shouldShowStoryCreationButton,
            ) {
                scope.launch {
                    sheetState.hide()
                }.invokeOnCompletion {
                    if (!sheetState.isVisible) {
                        onDismiss()
                    }
                }
            }
        }
    }
}

@Composable
fun AmityCommunityProfileActionsContainer(
    modifier: Modifier = Modifier,
    componentScope: AmityComposeComponentScope? = null,
    community: AmityCommunity,
    shouldShowPostCreationButton: Boolean,
    shouldShowStoryCreationButton: Boolean,
    onDismiss: () -> Unit,
) {
    val context = LocalContext.current

    AmityBaseElement(
        componentScope = componentScope,
        elementId = "community_profile_actions"
    ) {
        Column(
            modifier = modifier
                .background(AmityTheme.colors.background)
                .padding(start = 16.dp, end = 16.dp, bottom = 20.dp)
        ) {
            val behavior by lazy {
                AmitySocialBehaviorHelper.communityProfilePageBehavior
            }

            val launcher = rememberLauncherForActivityResult(
                contract = ActivityResultContracts.StartActivityForResult()
            ) {}

            if (shouldShowPostCreationButton) {
                AmityBottomSheetActionItem(
                    icon = R.drawable.amity_ic_post_create,
                    text = "Post",
                    modifier = modifier,
                ) {
                    onDismiss()
                    behavior.goToPostComposerPage(
                        AmityCommunityProfilePageBehavior.Context(
                            pageContext = context,
                            activityLauncher = launcher,
                            community = community,
                        )
                    )
                }
            }

            if (shouldShowStoryCreationButton) {
                AmityBottomSheetActionItem(
                    icon = R.drawable.amity_ic_create_story_social,
                    text = "Story",
                    modifier = modifier,
                ) {
                    onDismiss()
                    behavior.goToCreateStoryPage(
                        AmityCommunityProfilePageBehavior.Context(
                            pageContext = context,
                            activityLauncher = launcher,
                            community = community,
                        )
                    )
                }
            }

            if (shouldShowPostCreationButton) {
                AmityBottomSheetActionItem(
                    icon = R.drawable.ic_amity_ic_poll_create,
                    text = "Poll",
                    modifier = modifier,
                ) {
                    onDismiss()
                    behavior.goToCreatePollPage(
                        AmityCommunityProfilePageBehavior.Context(
                            pageContext = context,
                            activityLauncher = launcher,
                            community = community,
                        )
                    )
                }
            }

            if (shouldShowPostCreationButton) {
                AmityBottomSheetActionItem(
                    icon = R.drawable.ic_amity_ic_live_stream_create,
                    text = "Live stream",
                    modifier = modifier,
                ) {
                    onDismiss()
                    behavior.goToCreateLivestreamPage(
                        AmityCommunityProfilePageBehavior.Context(
                            pageContext = context,
                            activityLauncher = launcher,
                            community = community,
                        )
                    )
                }
            }
        }
    }
}

enum class CommunityCreatePostTargetType {
    COMMUNITY,
    USER,
}