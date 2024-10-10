package com.amity.socialcloud.uikit.community.compose.post.detail

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
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rxjava3.subscribeAsState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.coerceAtMost
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.LocalViewModelStoreOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.paging.compose.collectAsLazyPagingItems
import com.amity.socialcloud.sdk.model.social.comment.AmityComment
import com.amity.socialcloud.sdk.model.social.comment.AmityCommentReferenceType
import com.amity.socialcloud.sdk.model.social.post.AmityPost
import com.amity.socialcloud.uikit.common.ad.AmityListItem
import com.amity.socialcloud.uikit.common.ui.base.AmityBaseComponent
import com.amity.socialcloud.uikit.common.ui.base.AmityBaseElement
import com.amity.socialcloud.uikit.common.ui.base.AmityBasePage
import com.amity.socialcloud.uikit.common.ui.theme.AmityTheme
import com.amity.socialcloud.uikit.common.utils.clickableWithoutRipple
import com.amity.socialcloud.uikit.common.utils.closePage
import com.amity.socialcloud.uikit.common.utils.getIcon
import com.amity.socialcloud.uikit.common.utils.getKeyboardHeight
import com.amity.socialcloud.uikit.common.utils.isKeyboardVisible
import com.amity.socialcloud.uikit.community.compose.comment.AmityCommentTrayComponentViewModel
import com.amity.socialcloud.uikit.community.compose.comment.create.AmityCommentComposerBar
import com.amity.socialcloud.uikit.community.compose.paging.comment.amityCommentListLLS
import com.amity.socialcloud.uikit.community.compose.post.detail.components.AmityPostContentComponent
import com.amity.socialcloud.uikit.community.compose.post.detail.components.AmityPostContentComponentStyle
import com.amity.socialcloud.uikit.community.compose.post.detail.menu.AmityPostMenuSheetUIState
import com.amity.socialcloud.uikit.community.compose.post.detail.menu.AmityPostMenuViewModel

@Composable
fun AmityPostDetailPage(
    modifier: Modifier = Modifier,
    id: String,
    style: AmityPostContentComponentStyle,
    category: AmityPostCategory = AmityPostCategory.GENERAL,
    hideTarget: Boolean,
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

    var replyComment by remember { mutableStateOf<AmityComment?>(null) }
    var replyCommentId by remember { mutableStateOf("") }
    var editingCommentId by remember { mutableStateOf<String?>(null) }

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

    AmityBasePage(pageId = "post_detail_page") {
        AmityBaseComponent(
            pageScope = getPageScope(),
            componentId = "comment_tray_component"
        ) {
            Column(modifier = modifier.fillMaxSize()) {
                Box(
                    modifier = modifier
                        .height(58.dp)
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
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
                        style = AmityTheme.typography.title,
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
                LazyColumn(
                    verticalArrangement = Arrangement.Top,
                    modifier = modifier.weight(1f)
                ) {
                    AmityCommentTrayComponentViewModel.CommentListState.from(
                        loadState = comments.loadState.refresh,
                        itemCount = comments.itemCount,
                    ).let(commentViewModel::setCommentListState)

                    item {
                        AmityPostContentComponent(
                            modifier = modifier,
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
                    }

                    item {
                        Spacer(modifier = modifier.height(8.dp))
                    }

                    amityCommentListLLS(
                        modifier = modifier,
                        componentScope = getComponentScope(),
                        comments = comments,
                        commentListState = commentListState,
                        referenceId = id,
                        referenceType = AmityCommentReferenceType.POST,
                        editingCommentId = editingCommentId,
                        shouldAllowInteraction = true,
                        onReply = {
                            replyCommentId = it
                        },
                        onEdit = {
                            editingCommentId = it
                        }
                    )

                    item {
                        Box(modifier.height(commentComposeBarBottomOffset.unaryMinus()))
                    }
                }

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
        }
    }
}

enum class AmityPostCategory {
    GENERAL,
    PIN,
    ANNOUNCEMENT,
    PIN_AND_ANNOUNCEMENT;

    companion object {
        fun fromString(category: String): AmityPostCategory {
            return when (category) {
                "PIN_AND_ANNOUNCEMENT" -> PIN_AND_ANNOUNCEMENT
                "ANNOUNCEMENT" -> ANNOUNCEMENT
                "PIN" -> PIN
                else -> GENERAL
            }
        }
    }
}