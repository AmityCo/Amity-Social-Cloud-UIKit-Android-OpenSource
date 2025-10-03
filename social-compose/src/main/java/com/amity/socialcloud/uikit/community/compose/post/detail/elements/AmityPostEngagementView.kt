package com.amity.socialcloud.uikit.community.compose.post.detail.elements

import android.content.res.Resources
import androidx.compose.foundation.Image
import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.awaitLongPressOrCancellation
import androidx.compose.foundation.gestures.drag
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.input.pointer.PointerId
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.boundsInWindow
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntRect
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Popup
import androidx.compose.ui.window.PopupPositionProvider
import androidx.compose.ui.window.PopupProperties
import androidx.lifecycle.viewmodel.compose.LocalViewModelStoreOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import com.amity.socialcloud.sdk.api.core.AmityCoreClient
import com.amity.socialcloud.sdk.model.core.reaction.AmityReactionReferenceType
import com.amity.socialcloud.sdk.model.social.post.AmityPost
import com.amity.socialcloud.uikit.common.common.readableNumber
import com.amity.socialcloud.uikit.common.model.AmitySocialReactions
import com.amity.socialcloud.uikit.common.reaction.AmityReactionList
import com.amity.socialcloud.uikit.common.reaction.picker.AmityReactionPicker
import com.amity.socialcloud.uikit.common.reaction.picker.getReactionIndexByX
import com.amity.socialcloud.uikit.common.reaction.preview.AmityReactionPreview
import com.amity.socialcloud.uikit.common.ui.base.AmityBaseElement
import com.amity.socialcloud.uikit.common.ui.scope.AmityComposeComponentScope
import com.amity.socialcloud.uikit.common.ui.scope.AmityComposePageScope
import com.amity.socialcloud.uikit.common.ui.theme.AmityTheme
import com.amity.socialcloud.uikit.common.utils.AmityConstants.POST_REACTION
import com.amity.socialcloud.uikit.common.utils.clickableWithoutRipple
import com.amity.socialcloud.uikit.common.utils.getIcon
import com.amity.socialcloud.uikit.common.utils.getText
import com.amity.socialcloud.uikit.common.utils.isVisitor
import com.amity.socialcloud.uikit.community.compose.AmitySocialBehaviorHelper
import com.amity.socialcloud.uikit.community.compose.R
import com.amity.socialcloud.uikit.community.compose.post.detail.AmityPostDetailPageViewModel
import kotlinx.coroutines.TimeoutCancellationException
import kotlin.math.roundToInt

@Composable
fun AmityPostEngagementView(
    modifier: Modifier = Modifier,
    pageScope: AmityComposePageScope? = null,
    componentScope: AmityComposeComponentScope? = null,
    post: AmityPost,
    isPostDetailPage: Boolean,
    shareButtonClick: (postId:String) -> Unit = {},
) {
    val context = LocalContext.current
    val behavior by lazy {
        AmitySocialBehaviorHelper.postContentComponentBehavior
    }
    val myReactionState = remember {
        val firstReaction = post.getMyReactions().firstOrNull()
        mutableStateOf(firstReaction ?: "")
    }
    val myReaction by remember(post.getMyReactions()) { myReactionState }
    val reactingState = remember {
        mutableStateOf(Pair("",0))
    }
    val reacting by remember { reactingState }
    val localReactionCountState = remember {
        mutableStateOf(post.getReactionCount())
    }
    val localReactionCount by remember(post.getReactionCount()) {
        localReactionCountState
    }
    val localReactionsState = remember {
        val reactions = post.getReactionMap()
        mutableStateOf(reactions)
    }
    val localReactions by remember { localReactionsState }

    val isReacted = remember(myReaction, reacting) { myReaction.isNotEmpty() || reacting.first.isNotEmpty() }

    var reactionExpanded by remember { mutableStateOf(false) }

    val reactionCount = (if (reacting.first.isNotEmpty()) reacting.second else localReactionCount).let { count ->
        pluralStringResource(
            id = R.plurals.amity_feed_reaction_count,
            count = count,
            count.readableNumber()
        )
    }

    val commentCount by remember(post.getUpdatedAt(), post.getCommentCount()) {
        mutableStateOf(post.getCommentCount())
    }

    var showReactionListSheet by remember { mutableStateOf(false) }

    val viewModelStoreOwner = checkNotNull(LocalViewModelStoreOwner.current) {
        "No ViewModelStoreOwner was provided via LocalViewModelStoreOwner"
    }
    val viewModel =
        viewModel<AmityPostDetailPageViewModel>(viewModelStoreOwner = viewModelStoreOwner)
    var initialPointerId by remember { mutableStateOf<PointerId?>(null) }
    var initialDownPos by remember { mutableStateOf<Offset?>(null) }
    val density = LocalDensity.current
    val screenWidthPx = with(density) { Resources.getSystem().displayMetrics.widthPixels }
    var anchorInWindow by remember { mutableStateOf<Rect?>(null) }
    var popupWidthPx by remember { mutableStateOf(0) }
    var highlightedIndex by remember { mutableStateOf<Int?>(null) }
    var lastHapticIndex by remember { mutableStateOf<Int?>(null) }
    val haptics = LocalHapticFeedback.current
    val reactions = remember { AmitySocialReactions.getList() }
    val fromNonMemberCommunity = remember(post) {
        (post.getTarget() as? AmityPost.Target.COMMUNITY)?.getCommunity()?.isJoined() == false
    }
    val localFocus = LocalFocusManager.current

    // Update the reaction state when the post's reactions change
    LaunchedEffect(post.getMyReactions()) {
        if (myReactionState.value != post.getMyReactions().firstOrNull()) {
            myReactionState.value = post.getMyReactions().firstOrNull() ?: ""
        }
        localReactionsState.value = post.getReactionMap()
    }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(
                start = 16.dp,
                end = 16.dp,
                bottom = 0.dp,
                top = if (isPostDetailPage) 8.dp else 0.dp
            )
    ) {
        Row(
            modifier = modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            if (localReactionCount > 0) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .clickableWithoutRipple {
                            localFocus.clearFocus()
                            if (AmityCoreClient.isVisitor()) {
                                behavior.handleVisitorUserAction()
                            } else {
                                showReactionListSheet = true
                            }
                        }
                ) {
                    AmityReactionPreview(
                        pageScope = pageScope,
                        componentScope = componentScope,
                        myReaction = myReaction,
                        reactions = localReactions,
                        reactionCount = localReactionCount,
                    )
                }
            }

            if (commentCount > 0) {
                Text(
                    text = pluralStringResource(
                        id = R.plurals.amity_feed_comment_count,
                        count = commentCount,
                        commentCount.readableNumber()
                    ),
                    style = AmityTheme.typography.captionLegacy.copy(
                        fontWeight = FontWeight.Normal,
                        color = AmityTheme.colors.baseShade2
                    ),
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
                    Row(
                        modifier = Modifier
                            .testTag(getAccessibilityId())
                            .onGloballyPositioned { coords ->
                                anchorInWindow = coords.boundsInWindow()
                            }
                            .pointerInput(Unit) {
                                awaitEachGesture {
                                    // Record the initial pointer ID for future drag tracking
                                    val down = awaitFirstDown()
                                    down.consume()
                                    initialPointerId = down.id

                                    // Check if it's a long press (to show reactions) or a tap (to like/unlike)
                                    var longPressDetected = false

                                    val reactionMap = localReactionsState.value

                                    // Try to detect long press
                                    try {
                                        withTimeout(viewConfiguration.longPressTimeoutMillis) {
                                            // Wait a bit to see if this is a long press
                                            awaitLongPressOrCancellation(down.id)?.let { longPress ->
                                                // Long press detected, show reaction picker
                                                longPressDetected = true
                                                val anchor = anchorInWindow ?: return@let
                                                initialDownPos = Offset(
                                                    anchor.left.toFloat() + longPress.position.x,
                                                    anchor.top.toFloat() + longPress.position.y
                                                )
                                                reactionExpanded = true
                                                haptics.performHapticFeedback(HapticFeedbackType.LongPress)

                                                // Start drag tracking for the reaction selection
                                                var lastIdx: Int? = null
                                                drag(longPress.id) { change ->
                                                    change.consume()
                                                    if (!reactionExpanded) return@drag

                                                    val idx = getReactionIndexByX(change.position.x, popupWidthPx, reactions.size)
                                                    if (idx != highlightedIndex) {
                                                        highlightedIndex = idx
                                                        if (idx != null && lastHapticIndex != idx) {
                                                            haptics.performHapticFeedback(HapticFeedbackType.LongPress)
                                                            lastHapticIndex = idx
                                                        }
                                                    }
                                                    lastIdx = idx
                                                }

                                                // On drag end
                                                lastIdx?.let { chosen ->
                                                    localFocus.clearFocus()
                                                    if (AmityCoreClient.isVisitor()) {
                                                        behavior.handleVisitorUserAction()
                                                    } else if (fromNonMemberCommunity) {
                                                        behavior.handleNonMemberAction()
                                                    } else {
                                                        val chosenReaction = reactions[chosen]
                                                        if (myReaction == chosenReaction.name) {
                                                            localReactionCountState.value -= 1
                                                            myReactionState.value = ""
                                                            reactionMap[chosenReaction.name] =
                                                                (reactionMap[chosenReaction.name]
                                                                    ?: 1) - 1
                                                            localReactionsState.value = reactionMap
                                                            viewModel.changeReaction(
                                                                postId = post.getPostId(),
                                                                reactionName = chosenReaction.name,
                                                                isReacted = false,
                                                                onError = {
                                                                    localReactionsState.value =
                                                                        post.getReactionMap()
                                                                }
                                                            )
                                                        } else {
                                                            // If user already reacted with different reaction
                                                            if (myReaction.isNotEmpty() && myReaction != chosenReaction.name) {
                                                                reactingState.value = Pair(
                                                                    chosenReaction.name,
                                                                    localReactionCount
                                                                )
                                                                val previousReaction = myReaction
                                                                reactionMap[chosenReaction.name] =
                                                                    (reactionMap[chosenReaction.name]
                                                                        ?: 0) + 1
                                                                reactionMap[previousReaction] =
                                                                    (reactionMap[previousReaction]
                                                                        ?: 1) - 1
                                                                localReactionsState.value =
                                                                    reactionMap
                                                                viewModel.switchReaction(
                                                                    postId = post.getPostId(),
                                                                    reaction = chosenReaction.name,
                                                                    previousReaction = previousReaction,
                                                                    onSuccess = {
                                                                        reactingState.value =
                                                                            Pair("", 0)
                                                                    },
                                                                    onError = {
                                                                        reactingState.value =
                                                                            Pair("", 0)
                                                                        localReactionsState.value =
                                                                            post.getReactionMap()
                                                                    }
                                                                )
                                                            } else {
                                                                localReactionCountState.value += 1
                                                                myReactionState.value =
                                                                    chosenReaction.name
                                                                reactionMap[chosenReaction.name] =
                                                                    (reactionMap[chosenReaction.name]
                                                                        ?: 0) + 1
                                                                localReactionsState.value =
                                                                    reactionMap
                                                                viewModel.changeReaction(
                                                                    postId = post.getPostId(),
                                                                    reactionName = chosenReaction.name,
                                                                    isReacted = true,
                                                                    onError = {
                                                                        localReactionsState.value =
                                                                            post.getReactionMap()
                                                                    }
                                                                )
                                                            }
                                                        }
                                                    }
                                                }

                                                // Reset states
                                                highlightedIndex = null
                                                lastHapticIndex = null
                                                reactionExpanded = false
                                                initialPointerId = null
                                                initialDownPos = null
                                            }
                                        }
                                    } catch (e: TimeoutCancellationException) {
                                        // Timeout for long press detection exceeded
                                    }

                                    // If no long press was detected, check if this was a tap
                                    if (!longPressDetected) {
                                        localFocus.clearFocus()
                                        if (AmityCoreClient.isVisitor()) {
                                            behavior.handleVisitorUserAction()
                                        } else if (fromNonMemberCommunity) {
                                            behavior.handleNonMemberAction()
                                        } else if (reacting.first.isEmpty()) {
                                            // This was a tap, handle the like/unlike action
                                            val previousReaction = myReaction
                                            myReactionState.value = if (myReaction.isEmpty()) {
                                                POST_REACTION
                                            } else {
                                                ""
                                            }
                                            if (myReactionState.value.isEmpty()) {
                                                localReactionCountState.value -= 1
                                                reactionMap[previousReaction] =
                                                    (reactionMap[previousReaction]
                                                        ?: 1) - 1
                                                localReactionsState.value = reactionMap
                                            } else {
                                                localReactionCountState.value += 1
                                                reactionMap[POST_REACTION] =
                                                    (reactionMap[POST_REACTION] ?: 0) + 1
                                                localReactionsState.value = reactionMap
                                            }
                                            viewModel.changeReaction(
                                                postId = post.getPostId(),
                                                reactionName = if (myReactionState.value.isNotEmpty()) POST_REACTION else previousReaction,
                                                isReacted = myReactionState.value.isNotEmpty(),
                                                onError = {
                                                    localReactionsState.value = post.getReactionMap()
                                                }
                                            )
                                        }
                                    }
                                }
                            },
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        val reactionIcon = if (isReacted) {
                            reacting
                                .first
                                .ifEmpty { myReaction }
                                .let(AmitySocialReactions::toReaction)
                                .icon
                        } else {
                            R.drawable.amity_ic_story_like_normal
                        }.let { reactionRes ->
                            ImageVector.vectorResource(id = reactionRes)
                        }
                        Icon(
                            imageVector = reactionIcon,
                            contentDescription = "Post Reaction",
                            tint = Color.Unspecified,
                            modifier = Modifier
                                .size(20.dp)

                        )
                        Spacer(modifier = modifier.width(4.dp))
                        Text(
                            text = reacting
                                .first
                                .ifEmpty { myReaction }
                                .ifEmpty { AmitySocialReactions.toReaction(POST_REACTION).name }
                                .replaceFirstChar {
                                    if (it.isLowerCase()) it.titlecase() else it.toString()
                                },
                            style = AmityTheme.typography.bodyLegacy.copy(
                                fontWeight = FontWeight.SemiBold,
                                color = if (isReacted) AmityTheme.colors.primary
                                else AmityTheme.colors.baseShade2
                            ),
                        )
                    }
                }

                Spacer(modifier = modifier.width(8.dp))
                AmityBaseElement(
                    componentScope = componentScope,
                    elementId = "comment_button"
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp),
                        modifier = Modifier
                            .let {
                                if (AmityCoreClient.isVisitor()) {
                                    it.clickableWithoutRipple {
                                        localFocus.clearFocus()
                                        behavior.handleVisitorUserAction()
                                    }
                                } else if (fromNonMemberCommunity) {
                                    it.clickableWithoutRipple {

                                        localFocus.clearFocus()
                                        behavior.handleNonMemberAction()
                                    }
                                } else {
                                    it.clickableWithoutRipple {
                                        localFocus.clearFocus()
                                    }
                                }
                            }

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

                Spacer(Modifier.weight(1f))

                if (post.getTarget() is AmityPost.Target.COMMUNITY) {
                    val community = (post.getTarget() as AmityPost.Target.COMMUNITY).getCommunity()
                    if (community?.isPublic() == true) {
                        Icon(
                            imageVector = ImageVector.vectorResource(id = R.drawable.amity_v4_share_icon),
                            contentDescription = "Post Comment Count",
                            tint = Color.Unspecified,
                            modifier = Modifier
                                .size(20.dp)
                                .clickableWithoutRipple {
                                    shareButtonClick(post.getPostId())
                                }
                        )
                    }
                } else {
                    Icon(
                        imageVector = ImageVector.vectorResource(id = R.drawable.amity_v4_share_icon),
                        contentDescription = "Post Comment Count",
                        tint = Color.Unspecified,
                        modifier = Modifier
                            .size(20.dp)
                            .clickableWithoutRipple {
                                shareButtonClick(post.getPostId())
                            }
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

        if (reactionExpanded && anchorInWindow != null) {
            val anchor = anchorInWindow!!
            Popup(
                properties = PopupProperties(
                    focusable = true,
                    clippingEnabled = false
                ),
                popupPositionProvider = object : PopupPositionProvider {
                    override fun calculatePosition(
                        anchorBounds: IntRect,
                        windowSize: IntSize,
                        layoutDirection: LayoutDirection,
                        popupContentSize: IntSize
                    ): IntOffset {
                        // Save popup width for picker
                        popupWidthPx = popupContentSize.width
                        // Position the picker centered with 8dp gap; place below if button is in top 20% of screen, otherwise above
                        val spacingPx = with(density) { 8.dp.roundToPx() }
                        val x = (anchor.center.x - popupContentSize.width / 2f).roundToInt()

                        val likeIsInTop20 = anchor.top <= (windowSize.height * 0.2f)
                        val desiredY = if (likeIsInTop20) {
                            // show below the like button
                            (anchor.bottom + spacingPx)
                        } else {
                            // show above the like button
                            (anchor.top - popupContentSize.height - spacingPx)
                        }.roundToInt()

                        return IntOffset(
                            x.coerceIn(0, windowSize.width - popupContentSize.width),
                            desiredY.coerceIn(0, windowSize.height - popupContentSize.height)
                        )
                    }
                },
                onDismissRequest = {
                    reactionExpanded = false
                    highlightedIndex = null
                    lastHapticIndex = null
                }
            ) {
                Box(
                    modifier = Modifier
                        .padding(top = 32.dp)
                        .onGloballyPositioned { coords ->
                            popupWidthPx = coords.size.width
                        }
                ) {
                    AmityReactionPicker(
                        modifier = Modifier.padding(start = 16.dp),
                        pageScope = pageScope,
                        componentScope = componentScope,
                        selectedReaction = myReaction,
                        highlightedIndex = highlightedIndex,
                        onAddReaction = { },
                        onRemoveReaction = { },
                        onDismiss = {
                            reactionExpanded = false
                            initialPointerId = null
                            initialDownPos = null
                            highlightedIndex = null
                            lastHapticIndex = null
                        },
                    )
                }
            }
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
