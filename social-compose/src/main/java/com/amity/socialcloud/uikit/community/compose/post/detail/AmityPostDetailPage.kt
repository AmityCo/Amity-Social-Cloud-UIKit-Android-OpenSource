package com.amity.socialcloud.uikit.community.compose.post.detail

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
import com.amity.socialcloud.uikit.community.compose.comment.AmityCommentTrayComponentViewModel
import com.amity.socialcloud.uikit.community.compose.comment.create.AmityCommentComposerBar
import com.amity.socialcloud.uikit.community.compose.livestream.errorhandling.AmityPostErrorPage
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

    val currentUser by remember(viewModel) {
        viewModel.getCurrentUser()
    }.subscribeAsState(null)

    val postErrorState by viewModel.postErrorState.collectAsState()

    LaunchedEffect(Unit) {
        if (parentId != null) {
            commentViewModel.getCommentById(parentId)
        } else if (commentId != null) {
            commentViewModel.getCommentById(commentId)
        }
    }

    val commentTarget by commentViewModel.comment.collectAsState()

    var replyComment by remember { mutableStateOf<AmityComment?>(null) }
    var replyCommentId by remember { mutableStateOf("") }
    var editingCommentId by remember { mutableStateOf<String?>(null) }

    var showLivestreamLimitExceededDialog by remember { mutableStateOf(showLivestreamPostExceeded) }

    LaunchedEffect(replyCommentId) {
        comments.itemSnapshotList.firstOrNull {
            it is AmityListItem.CommentItem &&
                    it.comment.getCommentId() == replyCommentId
        }?.let {
            replyComment = (it as AmityListItem.CommentItem).comment
            replyCommentId = ""
        }
    }

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
    LaunchedEffect(Unit) {
        // Ensure layout is complete
        scrollAnimationComplete = false
        delay(1000L)
        if (commentTarget != null && !scrollTriggered) {
            scrollState.animateScrollToItem(
                index = 2,
                scrollOffset = -stickyHeaderHeight
            )
            scrollTriggered = true
            // Wait a bit to ensure scroll completes
            delay(100L)
            scrollAnimationComplete = true
            delay(500L)
            scrollAnimationComplete = false
        }
    }

    AmityBasePage(pageId = "post_detail_page") {
        AmityBaseComponent(
            pageScope = getPageScope(),
            componentId = "comment_tray_component"
        ) {
            if (post?.isDeleted() == true || postErrorState == true) {
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
                                            .size(20.dp)
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
                                    modifier = modifier.align(Alignment.Center)
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
                                AmityPostContentComponent(
                                    modifier = modifier,
                                    pageScope = getPageScope(),
                                    post = post ?: return@item,
                                    style = style,
                                    category = category,
                                    hideTarget = hideTarget,
                                    hideMenuButton = true,
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
                            shouldAllowInteraction = !sheetViewModel.isNotMember(post),
                            onReply = {
                                replyCommentId = it
                            },
                            onEdit = {
                                editingCommentId = it
                            },
                            showBounceEffect = scrollAnimationComplete,
                            replyTargetId = if (parentId != null) commentId else null
                        )

                        item {
                            Box(modifier.height(commentComposeBarBottomOffset.unaryMinus()))
                        }
                    }

                    if (!sheetViewModel.isNotMember(post) && editingCommentId == null) {
                        AmityCommentComposerBar(
                            modifier = modifier.offset(y = commentComposeBarBottomOffset),
                            componentScope = getComponentScope(),
                            referenceId = id,
                            referenceType = AmityCommentReferenceType.POST,
                            currentUser = currentUser,
                            replyComment = replyComment,
                        ) {
                            replyComment = null
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