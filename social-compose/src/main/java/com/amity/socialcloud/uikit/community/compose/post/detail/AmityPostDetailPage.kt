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
import com.amity.socialcloud.sdk.model.social.comment.AmityComment
import com.amity.socialcloud.sdk.model.social.comment.AmityCommentReferenceType
import com.amity.socialcloud.sdk.model.social.post.AmityPost
import com.amity.socialcloud.uikit.common.ui.base.AmityBaseComponent
import com.amity.socialcloud.uikit.common.ui.base.AmityBaseElement
import com.amity.socialcloud.uikit.common.ui.base.AmityBasePage
import com.amity.socialcloud.uikit.common.ui.theme.AmityTheme
import com.amity.socialcloud.uikit.common.utils.clickableWithoutRipple
import com.amity.socialcloud.uikit.common.utils.closePage
import com.amity.socialcloud.uikit.common.utils.getIcon
import com.amity.socialcloud.uikit.common.utils.getKeyboardHeight
import com.amity.socialcloud.uikit.common.utils.isKeyboardVisible
import com.amity.socialcloud.uikit.community.compose.comment.amityCommentListComponent
import com.amity.socialcloud.uikit.community.compose.comment.create.AmityCommentComposerBar
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

    val post by remember(id) {
        viewModel.getPost(id)
    }.collectAsState(initial = null)

    val currentUser by remember(viewModel) {
        viewModel.getCurrentUser()
    }.subscribeAsState(null)

    var replyComment by remember { mutableStateOf<AmityComment?>(null) }

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
            val commentListComposables = amityCommentListComponent(
                modifier = modifier,
                componentScope = getComponentScope(),
                referenceId = id,
                referenceType = AmityCommentReferenceType.POST,
                community = (post?.getTarget() as? AmityPost.Target.COMMUNITY)?.getCommunity(),
                shouldAllowInteraction = !sheetViewModel.isNotMember(post),
                onReply = {
                    replyComment = it
                }
            )

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

                    items(
                        count = commentListComposables.size,
                        key = { it }
                    ) { index ->
                        commentListComposables[index]()
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
    ANNOUNCEMENT;

    companion object {
        fun fromString(category: String): AmityPostCategory {
            return when (category) {
                "ANNOUNCEMENT" -> ANNOUNCEMENT
                "PIN" -> PIN
                else -> GENERAL
            }
        }
    }
}