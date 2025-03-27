package com.amity.socialcloud.uikit.community.compose.socialhome.components

import android.app.Activity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.testTagsAsResourceId
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.amity.socialcloud.sdk.model.social.post.AmityPost
import com.amity.socialcloud.sdk.model.social.story.AmityStory
import com.amity.socialcloud.uikit.common.common.isNotEmptyOrBlank
import com.amity.socialcloud.uikit.common.ui.base.AmityBaseComponent
import com.amity.socialcloud.uikit.common.ui.base.AmityBaseElement
import com.amity.socialcloud.uikit.common.ui.scope.AmityComposePageScope
import com.amity.socialcloud.uikit.common.ui.theme.AmityTheme
import com.amity.socialcloud.uikit.common.utils.closePageWithResult
import com.amity.socialcloud.uikit.common.utils.getIcon
import com.amity.socialcloud.uikit.common.utils.getText
import com.amity.socialcloud.uikit.community.compose.AmitySocialBehaviorHelper
import com.amity.socialcloud.uikit.community.compose.R
import com.amity.socialcloud.uikit.community.compose.post.composer.AmityPostTargetType
import com.amity.socialcloud.uikit.community.compose.target.AmityPostTargetSelectionPageType

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun AmityCreatePostMenuComponent(
    modifier: Modifier = Modifier,
    pageScope: AmityComposePageScope? = null,
    targetType: CreatePostTargetType? = null,
    targetId: String? = null,
    expanded: Boolean = false,
    onDismiss: () -> Unit = {}
) {
    val context = LocalContext.current

    val behavior by lazy {
        AmitySocialBehaviorHelper.createPostMenuComponentBehavior
    }
    val targetPostBehavior by lazy {
        AmitySocialBehaviorHelper.postTargetSelectionPageBehavior
    }
    val targetStoryBehavior by lazy {
        AmitySocialBehaviorHelper.storyTargetSelectionPageBehavior
    }
    val targetPollBehavior by lazy {
        AmitySocialBehaviorHelper.pollTargetSelectionPageBehavior
    }
    val targetLivestreamBehavior by lazy {
        AmitySocialBehaviorHelper.livestreamTargetSelectionPageBehavior
    }


    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) {
        context.closePageWithResult(Activity.RESULT_OK)
    }

    val viewModel = viewModel<AmityCreatePostMenuComponentViewModel>()
    val showStoryAction by viewModel.showStoryAction.collectAsState()

    AmityBaseComponent(
        pageScope = pageScope,
        componentId = "create_post_menu"
    ) {
        MaterialTheme(
            shapes = MaterialTheme.shapes.copy(extraSmall = RoundedCornerShape(12.dp))
        ) {
            DropdownMenu(
                offset = DpOffset(x = 0.dp, y = 36.dp),
                expanded = expanded,
                onDismissRequest = onDismiss,
                modifier = Modifier
                    .width(180.dp)
                    .background(AmityTheme.colors.background)
                    .semantics {
                        testTagsAsResourceId = true
                    }
            ) {
                DropdownMenuItem(
                    text = {
                        AmityBaseElement(
                            pageScope = pageScope,
                            componentScope = getComponentScope(),
                            elementId = "create_post_button"
                        ) {
                            Row(
                                horizontalArrangement = Arrangement.spacedBy(8.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = modifier
                                    .padding(horizontal = 8.dp)
                                    .testTag(getAccessibilityId()),
                            ) {
                                Icon(
                                    painter = painterResource(id = getConfig().getIcon()),
                                    contentDescription = "Create Post",
                                    tint = AmityTheme.colors.base,
                                    modifier = modifier.size(20.dp)
                                )
                                Text(
                                    text = getConfig().getText(),
                                    style = AmityTheme.typography.bodyLegacy.copy(
                                        fontWeight = FontWeight.SemiBold
                                    )
                                )
                            }
                        }
                    },
                    onClick = {
                        onDismiss()
                        if (targetType == CreatePostTargetType.COMMUNITY && targetId?.isNotEmptyOrBlank() == true) {
                            targetPostBehavior.goToPostComposerPage(
                                context = context,
                                launcher = launcher,
                                targetId = targetId,
                                targetType = AmityPostTargetType.COMMUNITY,
                            )
                        } else {
                            behavior.goToSelectPostTargetPage(
                                context = context,
                                type = AmityPostTargetSelectionPageType.POST
                            )
                        }
                    },
                )

                if (showStoryAction) {
                    DropdownMenuItem(
                        text = {
                            AmityBaseElement(
                                pageScope = pageScope,
                                componentScope = getComponentScope(),
                                elementId = "create_story_button"
                            ) {
                                Row(
                                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = modifier
                                        .padding(horizontal = 8.dp)
                                        .testTag(getAccessibilityId()),
                                ) {
                                    Icon(
                                        painter = painterResource(id = getConfig().getIcon()),
                                        contentDescription = "Create Story",
                                        tint = AmityTheme.colors.base,
                                        modifier = modifier.size(20.dp)
                                    )
                                    Text(
                                        text = getConfig().getText(),
                                        style = AmityTheme.typography.bodyLegacy.copy(
                                            fontWeight = FontWeight.SemiBold
                                        )
                                    )
                                }
                            }
                        },
                        onClick = {
                            onDismiss()
                            if (targetType == CreatePostTargetType.COMMUNITY && targetId?.isNotEmptyOrBlank() == true) {
                                targetStoryBehavior.goToStoryCreationPage(
                                    context = context,
                                    launcher = launcher,
                                    targetId = targetId,
                                    targetType = AmityStory.TargetType.COMMUNITY,
                                )
                            } else {
                                behavior.goToSelectStoryTargetPage(context)
                            }
                        }
                    )
                }

                DropdownMenuItem(
                    text = {
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = modifier.padding(horizontal = 8.dp),
                        ) {
                            Icon(
                                painter = painterResource(R.drawable.ic_amity_ic_poll_create),
                                contentDescription = "Create Poll Post",
                                tint = AmityTheme.colors.base,
                                modifier = modifier.size(20.dp)
                            )
                            Text(
                                "Poll",
                                style = AmityTheme.typography.bodyLegacy.copy(
                                    fontWeight = FontWeight.SemiBold
                                )
                            )
                        }
                    },
                    onClick = {
                        onDismiss()
                        if (targetType == CreatePostTargetType.COMMUNITY && targetId?.isNotEmptyOrBlank() == true) {
                            targetPollBehavior.goToPollPostComposerPage(
                                context = context,
                                launcher = launcher,
                                targetId = targetId,
                                targetType = AmityPost.TargetType.COMMUNITY,
                            )
                        } else {
                            behavior.goToSelectPollTargetPage(context)
                        }
                    }
                )
                DropdownMenuItem(
                    text = {
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = modifier.padding(horizontal = 8.dp),
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_amity_ic_live_stream_create),
                                contentDescription = "Create Livestream Post",
                                tint = AmityTheme.colors.base,
                                modifier = modifier.size(20.dp)
                            )
                            Text(
                                "Live stream",
                                style = AmityTheme.typography.bodyLegacy.copy(
                                    fontWeight = FontWeight.SemiBold
                                )
                            )
                        }
                    },
                    onClick = {
                        onDismiss()
                        if (targetType == CreatePostTargetType.COMMUNITY && targetId?.isNotEmptyOrBlank() == true) {
                            targetLivestreamBehavior.goToLivestreamPostComposerPage(
                                context = context,
                                launcher = launcher,
                                targetId = targetId,
                                targetType = AmityPost.TargetType.COMMUNITY,
                            )
                        } else {
                            behavior.goToSelectLivestreamTargetPage(
                                context = context
                            )
                        }
                    }
                )
            }
        }
    }
}

enum class CreatePostTargetType {
    COMMUNITY,
    USER,
}