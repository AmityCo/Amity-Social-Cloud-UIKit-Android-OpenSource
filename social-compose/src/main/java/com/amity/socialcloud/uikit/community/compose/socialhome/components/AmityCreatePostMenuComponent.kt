package com.amity.socialcloud.uikit.community.compose.socialhome.components

import com.amity.socialcloud.uikit.community.compose.localization.amitySocialConfigString
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
import androidx.compose.material3.ExperimentalMaterial3Api
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
import com.amity.socialcloud.uikit.common.config.AmityUIKitConfigController
import com.amity.socialcloud.uikit.common.ui.base.AmityBaseComponent
import com.amity.socialcloud.uikit.common.ui.base.AmityBaseElement
import com.amity.socialcloud.uikit.common.ui.scope.AmityComposePageScope
import com.amity.socialcloud.uikit.common.ui.theme.AmityTheme
import com.amity.socialcloud.uikit.common.utils.closePageWithResult
import com.amity.socialcloud.uikit.common.utils.getIcon
import com.amity.socialcloud.uikit.common.utils.getText
import com.amity.socialcloud.uikit.community.compose.AmitySocialBehaviorHelper
import com.amity.socialcloud.uikit.common.R as CommonR
import com.amity.socialcloud.uikit.community.compose.post.composer.AmityPostTargetType
import com.amity.socialcloud.uikit.community.compose.target.AmityPostTargetSelectionPageType
import com.amity.socialcloud.uikit.community.compose.localization.amitySocialString

@OptIn(ExperimentalComposeUiApi::class, ExperimentalMaterial3Api::class)
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
    val targetClipBehavior by lazy {
        AmitySocialBehaviorHelper.postTargetSelectionPageBehavior
    }

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) {
        context.closePageWithResult(Activity.RESULT_OK)
    }

    val items = rememberCreatePostMenuItems(pageScope)

    AmityBaseComponent(
        pageScope = pageScope,
        componentId = "create_post_menu"
    ) {
        // Every item here belongs to a module. Gating the items alone left the
        // menu opening on nothing when they were all switched off — a popup with
        // no rows, which reads as a broken button rather than an absent feature.
        // The "+" that opens this menu reads the same list, so it is not drawn
        // when this would be empty.
        if (items.isEmpty()) {
            return@AmityBaseComponent
        }
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
                if (AmityCreatePostMenuItem.POST in items) {
                    AmityBaseElement(
                        pageScope = pageScope,
                        componentScope = getComponentScope(),
                        elementId = "create_post_button"
                    ) {
                        DropdownMenuItem(
                            text = {
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
                                        text = amitySocialConfigString("amity_social_button_post_composer_create_button"),
                                        style = AmityTheme.typography.bodyLegacy.copy(
                                            fontWeight = FontWeight.SemiBold
                                        )
                                    )
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
                    }
                }

                if (AmityCreatePostMenuItem.POLL in items) {
                    AmityBaseElement(
                        pageScope = pageScope,
                        elementId = "create_poll_button",
                        componentScope = getComponentScope()
                    ) {
                        DropdownMenuItem(
                            text = {
                                Row(
                                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = modifier.padding(horizontal = 8.dp),
                                ) {
                                    Icon(
                                        painter = painterResource(CommonR.drawable.ic_amity_ic_poll_create),
                                        contentDescription = "Create Poll Post",
                                        tint = AmityTheme.colors.base,
                                        modifier = modifier.size(20.dp)
                                    )
                                    Text(
                                        amitySocialString("amity_social_button_poll"),
                                        style = AmityTheme.typography.bodyLegacy.copy(
                                            fontWeight = FontWeight.SemiBold
                                        )
                                    )
                                }
                            },
                            onClick = {
                                onDismiss()
                                if (targetType == CreatePostTargetType.COMMUNITY && targetId?.isNotEmptyOrBlank() == true) {
                                    behavior.goToSelectPollTargetPage(context)
                                } else {
                                    behavior.goToSelectPollTargetPage(context)
                                }
                            }
                        )
                    }
                }

                if (AmityCreatePostMenuItem.LIVESTREAM in items) {
                    AmityBaseElement(
                        pageScope = pageScope,
                        elementId = "create_livestream_button",
                        componentScope = getComponentScope()
                    ) {
                        DropdownMenuItem(
                            text = {
                                Row(
                                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = modifier.padding(horizontal = 8.dp),
                                ) {
                                    Icon(
                                        painter = painterResource(id = CommonR.drawable.ic_amity_ic_live_stream_create),
                                        contentDescription = "Create Livestream Post",
                                        tint = AmityTheme.colors.base,
                                        modifier = modifier.size(20.dp)
                                    )
                                    Text(
                                        amitySocialString("amity_social_status_live_stream"),
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

                if (AmityCreatePostMenuItem.STORY in items) {
                    AmityBaseElement(
                        pageScope = pageScope,
                        componentScope = getComponentScope(),
                        elementId = "create_story_button"
                    ) {
                        DropdownMenuItem(
                            text = {
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
                                        text = amitySocialConfigString("amity_social_button_story"),
                                        style = AmityTheme.typography.bodyLegacy.copy(
                                            fontWeight = FontWeight.SemiBold
                                        )
                                    )
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
                }

                if (AmityCreatePostMenuItem.CLIP in items) {
                    AmityBaseElement(
                        pageScope = pageScope,
                        elementId = "create_clip_button",
                        componentScope = getComponentScope()
                    ) {
                        DropdownMenuItem(
                            text = {
                                Row(
                                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = modifier.padding(horizontal = 8.dp),
                                ) {
                                    Icon(
                                        painter = painterResource(id = CommonR.drawable.amity_ic_create_clip),
                                        contentDescription = "Create clip post",
                                        tint = AmityTheme.colors.base,
                                        modifier = modifier.size(20.dp)
                                    )
                                    Text(
                                        amitySocialString("amity_social_button_clip"),
                                        style = AmityTheme.typography.bodyLegacy.copy(
                                            fontWeight = FontWeight.SemiBold
                                        )
                                    )
                                }
                            },
                            onClick = {
                                onDismiss()
                                if (targetType == CreatePostTargetType.COMMUNITY && targetId?.isNotEmptyOrBlank() == true) {
                                    targetClipBehavior.goToClipPostComposerPage(
                                        context = context,
                                        launcher = launcher,
                                        targetId = targetId,
                                        targetType = AmityPostTargetType.COMMUNITY,
                                    )
                                } else {
                                    behavior.goToSelectPostTargetPage(
                                        context = context,
                                        type = AmityPostTargetSelectionPageType.CLIP
                                    )
                                }
                            }
                        )
                    }
                }


                if (AmityCreatePostMenuItem.EVENT in items) {
                    // The element wraps the whole item, the way the five above do.
                    // Nested inside `text` it only emptied the row: the menu still
                    // measured a full-height — and still clickable — item for a module
                    // that is switched off, which is the blank strip under the last entry.
                    AmityBaseElement(
                        pageScope = pageScope,
                        componentScope = getComponentScope(),
                        elementId = "create_event_button"
                    ) {
                        DropdownMenuItem(
                            text = {
                                Row(
                                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = modifier
                                        .padding(horizontal = 8.dp)
                                        .testTag(getAccessibilityId()),
                                ) {
                                    Icon(
                                        painter = painterResource(id = com.amity.socialcloud.uikit.common.R.drawable.amity_ic_create_event),
                                        contentDescription = "Create Event",
                                        tint = AmityTheme.colors.base,
                                        modifier = modifier.size(20.dp)
                                    )
                                    Text(
                                        text = amitySocialString("amity_social_button_event"),
                                        style = AmityTheme.typography.bodyLegacy.copy(
                                            fontWeight = FontWeight.SemiBold
                                        )
                                    )
                                }
                            },
                            onClick = {
                                onDismiss()
                                behavior.goToSelectEventTargetPage(context = context)
                            }
                        )
                    }
                }
            }
        }
    }
}

/**
 * The rows [AmityCreatePostMenuComponent] can draw, in order, by element id.
 *
 * The menu and the "+" that opens it (`post_creation_button`) belong to no
 * module (module-availability-spec §10, PDT-5867); each row keeps its own owner.
 */
internal enum class AmityCreatePostMenuItem(val elementId: String) {
    POST("create_post_button"),
    POLL("create_poll_button"),
    LIVESTREAM("create_livestream_button"),
    STORY("create_story_button"),
    CLIP("create_clip_button"),
    EVENT("create_event_button"),
}

/**
 * The rows the create menu shows: the one list both the menu and its "+" read,
 * so the "+" is drawn exactly when the menu would have something in it.
 *
 * A row shows when the gate leaves it — its module, the customer's excludes, and
 * the menu's own id — and when the per-user check it carries passes: the story
 * setting or permission for Story, the create-event permission for Event. Those
 * two start false and resolve asynchronously, so a "+" whose only rows wait on
 * them stays hidden until they answer rather than appearing and vanishing.
 */
internal fun createPostMenuItems(
    pageScope: AmityComposePageScope?,
    canCreateStory: Boolean,
    canCreateEvent: Boolean,
): List<AmityCreatePostMenuItem> {
    val page = pageScope?.getId() ?: "*"
    if (AmityUIKitConfigController.isExcluded("$page/create_post_menu/*")) return emptyList()
    return AmityCreatePostMenuItem.entries.filter { item ->
        val allowed = when (item) {
            AmityCreatePostMenuItem.STORY -> canCreateStory
            AmityCreatePostMenuItem.EVENT -> canCreateEvent
            else -> true
        }
        allowed && !AmityUIKitConfigController.isExcluded("$page/create_post_menu/${item.elementId}")
    }
}

/**
 * [createPostMenuItems] for the signed-in user, read from the menu's view model.
 *
 * Both callers share the view model — it is keyed by class under the same
 * owner — so the "+" and the menu see the same permission state.
 */
@Composable
internal fun rememberCreatePostMenuItems(
    pageScope: AmityComposePageScope?,
): List<AmityCreatePostMenuItem> {
    val viewModel = viewModel<AmityCreatePostMenuComponentViewModel>()
    val uiState by viewModel.uiState.collectAsState()
    return createPostMenuItems(pageScope, uiState.canCreateStory, uiState.canCreateEvent)
}

enum class CreatePostTargetType {
    COMMUNITY,
    USER,
}