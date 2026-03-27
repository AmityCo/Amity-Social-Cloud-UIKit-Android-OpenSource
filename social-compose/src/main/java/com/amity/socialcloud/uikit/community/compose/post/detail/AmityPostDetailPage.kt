package com.amity.socialcloud.uikit.community.compose.post.detail

import android.util.Log
import androidx.compose.animation.core.animate
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rxjava3.subscribeAsState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.coerceAtMost
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.LocalViewModelStoreOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.paging.compose.collectAsLazyPagingItems
import com.amity.socialcloud.sdk.api.core.AmityCoreClient
import com.amity.socialcloud.sdk.model.core.error.AmityError
import com.amity.socialcloud.sdk.model.core.error.AmityException
import com.amity.socialcloud.sdk.core.session.model.NetworkConnectionEvent
import com.amity.socialcloud.sdk.model.social.comment.AmityComment
import com.amity.socialcloud.sdk.model.social.comment.AmityCommentReferenceType
import com.amity.socialcloud.sdk.model.social.post.AmityPost
import com.amity.socialcloud.uikit.common.ad.AmityListItem
import com.amity.socialcloud.uikit.common.eventbus.AmityUIKitSnackbar
import com.amity.socialcloud.uikit.common.ui.base.AmityBaseComponent
import com.amity.socialcloud.uikit.common.ui.base.AmityBaseElement
import com.amity.socialcloud.uikit.common.ui.base.AmityBasePage
import com.amity.socialcloud.uikit.common.ui.elements.AmityAlertDialog
import com.amity.socialcloud.uikit.common.ui.elements.DisposableEffectWithLifeCycle
import com.amity.socialcloud.uikit.common.ui.theme.AmityTheme
import com.amity.socialcloud.uikit.common.utils.clickableWithoutRipple
import com.amity.socialcloud.uikit.common.utils.closePage
import com.amity.socialcloud.uikit.common.utils.getIcon
import com.amity.socialcloud.uikit.common.utils.getKeyboardHeight
import com.amity.socialcloud.uikit.common.utils.isKeyboardVisible
import com.amity.socialcloud.uikit.common.utils.isSignedIn
import com.amity.socialcloud.uikit.common.utils.isVisitor
import com.amity.socialcloud.uikit.community.compose.AmitySocialBehaviorHelper
import com.amity.socialcloud.uikit.community.compose.R
import com.amity.socialcloud.uikit.community.compose.comment.AmityCommentTrayComponentViewModel
import com.amity.socialcloud.uikit.community.compose.comment.create.AmityCommentComposerBar
import com.amity.socialcloud.uikit.community.compose.livestream.errorhandling.AmityPostErrorPage
import com.amity.socialcloud.uikit.community.compose.comment.query.elements.AmityCommentItemShimmer
import com.amity.socialcloud.uikit.community.compose.paging.comment.amityCommentListLLS
import com.amity.socialcloud.uikit.community.compose.post.detail.components.AmityPostContentComponent
import com.amity.socialcloud.uikit.community.compose.post.detail.components.AmityPostContentComponentStyle
import com.amity.socialcloud.uikit.community.compose.post.detail.components.AmityPostShimmer
import com.amity.socialcloud.uikit.community.compose.post.detail.menu.AmityPostMenuSheetUIState
import com.amity.socialcloud.uikit.community.compose.post.detail.menu.AmityPostMenuViewModel
import kotlinx.coroutines.delay

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun AmityPostDetailPage(
    modifier: Modifier = Modifier,
    id: String,
    style: AmityPostContentComponentStyle,
    category: AmityPostCategory = AmityPostCategory.GENERAL,
    hideTarget: Boolean,
    showLivestreamPostExceeded: Boolean = false,
    commentId: String? = null,
    parentId: String? = null,
    rootId: String? = null,
    replyToCommentId: String? = null,
    eventHostId: String? = null,
    autoFocusCommentInput: Boolean = false,
) {
    val context = LocalContext.current

    val viewModelStoreOwner = checkNotNull(LocalViewModelStoreOwner.current) {
        "No ViewModelStoreOwner was provided via LocalViewModelStoreOwner"
    }
    val viewModel =
        viewModel<AmityPostDetailPageViewModel>(viewModelStoreOwner = viewModelStoreOwner)
    val sheetViewModel =
        viewModel<AmityPostMenuViewModel>(viewModelStoreOwner = viewModelStoreOwner)
    val commentViewModel =
        viewModel<AmityCommentTrayComponentViewModel>(viewModelStoreOwner = viewModelStoreOwner)

    val internetState by viewModel.internetState.collectAsState()

    val comments = remember(id) {
        commentViewModel.getComments(id, AmityCommentReferenceType.POST, null)
    }.collectAsLazyPagingItems()

    val commentListState by commentViewModel.commentListState.collectAsState()

    val post by remember(id) {
        viewModel.getPost(id)
    }.collectAsState(initial = null)

    val error by remember(id) {
        viewModel.getFetchErrorState()
    }.collectAsState()

    val currentUser by remember(viewModel) {
        viewModel.getCurrentUser()
    }.subscribeAsState(null)

    val postErrorState by viewModel.postErrorState.collectAsState()
    val isL2 = parentId != null && rootId != null && parentId != rootId
    val commentTarget by commentViewModel.commentTarget.collectAsState()
    val commentTargetUnavailableSet by commentViewModel.commentTargetUnavailable.collectAsState()
    val parentTargetUnavailable = parentId in commentTargetUnavailableSet
    val commentTargetUnavailable = commentId in commentTargetUnavailableSet

    LaunchedEffect(Unit) {
        val targetId = rootId ?: parentId ?: commentId
        targetId?.let { commentViewModel.getCommentTargetById(it) }
        // For L2: also verify the L1 parent and L2 itself
        if (isL2) {
            parentId.let { commentViewModel.verifyCommentExists(it) }
            commentId?.let { commentViewModel.verifyCommentExists(it) }
        } else if (parentId != null) {
            // For L1: verify the L1 comment itself
            commentId?.let { commentViewModel.verifyCommentExists(it) }
        }
    }

    LaunchedEffect(commentTargetUnavailable, parentTargetUnavailable, error) {
        if (error == null) {
            if (commentTargetUnavailable || parentTargetUnavailable) {
                AmityUIKitSnackbar.publishSnackbarErrorMessage(
                    context.getString(R.string.amity_reply_no_longer_available_error_message)
                )
            }
        }
    }

    val replyContext by commentViewModel.replyContext.collectAsState()

    var editingCommentId by remember { mutableStateOf<String?>(null) }

    var showLivestreamLimitExceededDialog by remember { mutableStateOf(showLivestreamPostExceeded) }

    LaunchedEffect(post) {
        commentViewModel.setCommunity((post?.getTarget() as? AmityPost.Target.COMMUNITY)?.getCommunity())
    }

    val isKeyboardOpen by isKeyboardVisible()
    val keyboardHeight by getKeyboardHeight()
    val systemBarPadding = WindowInsets.systemBars.asPaddingValues().calculateBottomPadding()

    val commentComposeBarBottomOffset by remember(systemBarPadding) {
        derivedStateOf {
            if (isKeyboardOpen) {
                2.dp.minus(keyboardHeight).plus(systemBarPadding).coerceAtMost(0.dp)
            } else {
                0.dp
            }
        }
    }

    val scrollState = rememberLazyListState()
    val hasScrolled by remember {
        derivedStateOf { scrollState.firstVisibleItemScrollOffset > 0 }
    }

    DisposableEffectWithLifeCycle(
        onDestroy = {
            post?.let {
                viewModel.unSubscribePostRT(it)
            }
        }
    )
    post?.let {
        viewModel.subscribePostRT(it)
    }

    var stickyHeaderHeight by remember { mutableIntStateOf(0) }
    var scrollTriggered by remember { mutableStateOf(false) }
    var scrollAnimationComplete by remember { mutableStateOf(false) }

    //LaunchedEffect that triggers the scroll with the measured offset
    LaunchedEffect(commentTarget) {
        if (commentTarget != null && !scrollTriggered) {
            scrollAnimationComplete = false
            delay(300L)
            scrollState.animateScrollToItem(
                index = 2,
                scrollOffset = -stickyHeaderHeight
            )
            scrollTriggered = true
            // Wait a bit to ensure scroll completes
            delay(100L)
            scrollAnimationComplete = true
            // If opened with a replyToCommentId, set reply context via ViewModel
            replyToCommentId?.let { targetId ->
                val targetComment = comments.itemSnapshotList
                    .filterIsInstance<AmityListItem.CommentItem>()
                    .flatMap { item ->
                        listOf(item.comment) +
                            item.comment.getLatestReplies() +
                            item.comment.getLatestReplies().flatMap { it.getLatestReplies() }
                    }
                    .firstOrNull { it.getCommentId() == targetId }
                targetComment?.let { comment ->
                    commentViewModel.setReplyContext(comment, comment.getCommentId())
                }
            }
        }
    }

    AmityBasePage(pageId = "post_detail_page") {
        AmityBaseComponent(
            pageScope = getPageScope(),
            componentId = "comment_tray_component"
        ) {
            if ((post != null && ((post?.isDeleted() == true
                        || !AmitySocialBehaviorHelper.supportedStructureTypes.contains(post?.getStructureType())) || postErrorState) || error != null)
                ) {
                AmityPostErrorPage()
            } else {
                Column(modifier = modifier.fillMaxSize()) {
                    LazyColumn(
                        state = scrollState,
                        verticalArrangement = Arrangement.Top,
                        modifier = modifier.weight(1f)
                    ) {
                        AmityCommentTrayComponentViewModel.CommentListState.from(
                            loadState = comments.loadState.refresh,
                            itemCount = comments.itemCount,
                        ).let(commentViewModel::setCommentListState)

                        stickyHeader {
                            Box(
                                modifier = modifier
                                    .height(58.dp)
                                    .fillMaxWidth()
                                    .background(AmityTheme.colors.background)
                                    .padding(horizontal = 16.dp)
                                    .onGloballyPositioned { coordinates ->
                                        stickyHeaderHeight = coordinates.size.height
                                    }
                            ) {
                                AmityBaseElement(
                                    pageScope = getPageScope(),
                                    elementId = "back_button"
                                ) {
                                    Icon(
                                        painter = painterResource(id = getConfig().getIcon()),
                                        contentDescription = null,
                                        tint = AmityTheme.colors.base,
                                        modifier = modifier
                                            .size(24.dp)
                                            .align(Alignment.CenterStart)
                                            .clickableWithoutRipple {
                                                context.closePage()
                                            }
                                            .testTag(getAccessibilityId()),
                                    )
                                }

                                Text(
                                    text = "Post",
                                    style = AmityTheme.typography.titleLegacy,
                                    modifier = modifier
                                        .padding(horizontal = 48.dp)
                                        .align(Alignment.CenterStart)
                                )

                                AmityBaseElement(
                                        pageScope = getPageScope(),
                                        elementId = "menu_button"
                                    ) {
                                    Icon(
                                        painter = painterResource(id = getConfig().getIcon()),
                                        contentDescription = null,
                                        tint = AmityTheme.colors.base,
                                        modifier = modifier
                                            .size(24.dp)
                                            .align(Alignment.CenterEnd)
                                            .clickableWithoutRipple {
                                                post?.let {
                                                    sheetViewModel.updateSheetUIState(
                                                        AmityPostMenuSheetUIState.OpenSheet(it.getPostId())
                                                    )
                                                }
                                            }
                                            .testTag(getAccessibilityId()),
                                    )
                                }
                            }
                            if (hasScrolled) {
                                HorizontalDivider(
                                    color = AmityTheme.colors.baseShade4,
                                )
                            }
                        }

                        item(key = "post_content") {
                            if (post != null && post?.isDeleted() == false) {
                                val isEventHost = eventHostId != null && post?.getCreator()?.getUserId() == eventHostId
                                AmityPostContentComponent(
                                    modifier = modifier,
                                    pageScope = getPageScope(),
                                    post = post ?: return@item,
                                    style = style,
                                    category = category,
                                    hideTarget = hideTarget,
                                    hideMenuButton = true,
                                    isEventHost = isEventHost,
                                )
                                HorizontalDivider(
                                    color = AmityTheme.colors.baseShade4,
                                    modifier = modifier
                                )
                            } else {
                                AmityPostShimmer()
                            }
                        }

                        item(key = "scroll_anchor") {
                            Spacer(
                                modifier = modifier
                                    .height(8.dp)
                            )
                        }

                        amityCommentListLLS(
                            modifier = modifier,
                            componentScope = getComponentScope(),
                            comments = comments,
                            commentTarget = commentTarget,
                            commentListState = commentListState,
                            referenceId = id,
                            referenceType = AmityCommentReferenceType.POST,
                            editingCommentId = editingCommentId,
                            shouldAllowInteraction = true,
                            showEngagementRow = true,
                            eventHostId = eventHostId,
                            onReply = { },
                            onEdit = {
                                editingCommentId = it
                            },
                            showBounceEffect = scrollAnimationComplete,
                            replyTargetId = when {
                                isL2 -> parentId    // L1 parent to sort to top and expand
                                parentId != null -> commentId  // L1 reply to bounce
                                else -> null
                            },
                            l2TargetId = if (isL2) commentId else null,
                            expandReplies = parentId != null,
                            fromNonMemberCommunity = sheetViewModel.isNotMember(post)
                        )

                        item {
                            Box(modifier.height(commentComposeBarBottomOffset.unaryMinus()))
                        }
                    }

                    if (!sheetViewModel.isNotMember(post) && editingCommentId == null && AmityCoreClient.isSignedIn()) {
                        AmityCommentComposerBar(
                            modifier = modifier.offset(y = commentComposeBarBottomOffset),
                            componentScope = getComponentScope(),
                            referenceId = id,
                            referenceType = AmityCommentReferenceType.POST,
                            shouldFocusKeyboard = replyToCommentId != null || autoFocusCommentInput,
                            currentUser = currentUser,
                            replyComment = replyContext?.first,
                            replyCommentId = replyContext?.second
                        ) {
                            commentViewModel.clearReplyContext()
                        }
                    }

                    if (showLivestreamLimitExceededDialog) {
                        AmityAlertDialog(
                            dialogTitle = "Live stream ended",
                            dialogText = "Your live stream has been automatically terminated since you reached 4-hour limit.",
                            dismissText = "OK",
                            onDismissRequest = {
                                showLivestreamLimitExceededDialog = false
                            }
                        )
                    }

                    if (internetState is NetworkConnectionEvent.Disconnected) {
                        AmityUIKitSnackbar.publishSnackbarErrorMessage(
                            message = "No internet connection.",
                            offsetFromBottom = 70
                        )
                    }
                }
            }
        }
    }
}

enum class AmityPostCategory {
    GENERAL,
    PIN,
    ANNOUNCEMENT,
    GLOBAL,
    PIN_AND_ANNOUNCEMENT;

    companion object {
        fun fromString(category: String): AmityPostCategory {
            return when (category) {
                "PIN_AND_ANNOUNCEMENT" -> PIN_AND_ANNOUNCEMENT
                "GLOBAL" -> GLOBAL
                "ANNOUNCEMENT" -> ANNOUNCEMENT
                "PIN" -> PIN
                else -> GENERAL
            }
        }
    }
}

@Composable
fun Modifier.bounceEffect(
    visible: Boolean = true,
    duration: Int = 300,
): Modifier {
    var offsetX by remember { mutableFloatStateOf(0f) }

    LaunchedEffect(visible) {
        if (visible) {
            // First shake to the right
            animate(
                initialValue = 0f,
                targetValue = 8f,
                animationSpec = tween(durationMillis = duration / 4)
            ) { value, _ ->
                offsetX = value
            }

            // Shake to the left
            animate(
                initialValue = 8f,
                targetValue = -8f,
                animationSpec = tween(durationMillis = duration / 3)
            ) { value, _ ->
                offsetX = value
            }

            // Shake back to the right
            animate(
                initialValue = -8f,
                targetValue = 5f,
                animationSpec = tween(durationMillis = duration / 4)
            ) { value, _ ->
                offsetX = value
            }

            // Final settle to center
            animate(
                initialValue = 5f,
                targetValue = 0f,
                animationSpec = tween(durationMillis = duration / 4)
            ) { value, _ ->
                offsetX = value
            }
        }
    }

    return this.then(
        Modifier.graphicsLayer {
            // Remove scale transformations, keep only translation
            translationX = offsetX
        }
    )
}