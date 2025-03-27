package com.amity.socialcloud.uikit.community.compose.target.post


import android.app.Activity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rxjava3.subscribeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.LocalViewModelStoreOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import com.amity.socialcloud.uikit.common.ui.base.AmityBaseElement
import com.amity.socialcloud.uikit.common.ui.base.AmityBasePage
import com.amity.socialcloud.uikit.common.ui.elements.AmityUserAvatarView
import com.amity.socialcloud.uikit.common.ui.theme.AmityTheme
import com.amity.socialcloud.uikit.common.utils.clickableWithoutRipple
import com.amity.socialcloud.uikit.common.utils.closePage
import com.amity.socialcloud.uikit.common.utils.closePageWithResult
import com.amity.socialcloud.uikit.common.utils.getIcon
import com.amity.socialcloud.uikit.common.utils.getText
import com.amity.socialcloud.uikit.community.compose.AmitySocialBehaviorHelper
import com.amity.socialcloud.uikit.community.compose.post.composer.AmityPostTargetType
import com.amity.socialcloud.uikit.community.compose.target.AmityPostTargetSelectionPageType
import com.amity.socialcloud.uikit.community.compose.target.AmityTargetSelectionPageViewModel
import com.amity.socialcloud.uikit.community.compose.target.components.AmityTargetContentType
import com.amity.socialcloud.uikit.community.compose.target.components.AmityTargetSelectionMyCommunitiesView

@Composable
fun AmityPostTargetSelectionPage(
    modifier: Modifier = Modifier,
    type: AmityPostTargetSelectionPageType,
) {
    val context = LocalContext.current
    val behavior by lazy {
        AmitySocialBehaviorHelper.postTargetSelectionPageBehavior
    }

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) {
        context.closePageWithResult(Activity.RESULT_OK)
    }

    val viewModelStoreOwner = checkNotNull(LocalViewModelStoreOwner.current) {
        "No ViewModelStoreOwner was provided via LocalViewModelStoreOwner"
    }
    val viewModel =
        viewModel<AmityTargetSelectionPageViewModel>(viewModelStoreOwner = viewModelStoreOwner)

    val currentUser by remember(viewModel) {
        viewModel.getCurrentUser()
    }.subscribeAsState(null)

    AmityBasePage(
        "select_post_target_page"
    ) {
        Column(
            modifier = modifier.fillMaxSize()
        ) {
            Box(
                modifier = modifier
                    .height(58.dp)
                    .fillMaxWidth()
                    .padding(start = 12.dp),
            ) {
                AmityBaseElement(
                    pageScope = getPageScope(),
                    elementId = "close_button"
                ) {
                    Icon(
                        painter = painterResource(getConfig().getIcon()),
                        contentDescription = "Close Button",
                        tint = AmityTheme.colors.base,
                        modifier = modifier
                            .size(16.dp)
                            .align(Alignment.CenterStart)
                            .clickableWithoutRipple {
                                context.closePage()
                            }
                            .testTag(getAccessibilityId()),
                    )
                }
                AmityBaseElement(
                    pageScope = getPageScope(),
                    elementId = "title"
                ) {
                    Text(
                        text = getConfig().getText(),
                        style = AmityTheme.typography.titleLegacy,
                        modifier = modifier
                            .align(Alignment.Center)
                            .testTag(getAccessibilityId()),
                    )
                }
            }

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                modifier = modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp)
                    .clickableWithoutRipple {
                        behavior.goToPostComposerPage(
                            context = context,
                            launcher = launcher,
                            targetType = AmityPostTargetType.USER,
                            community = null
                        )
                    },
            ) {
                AmityBaseElement(
                    pageScope = getPageScope(),
                    elementId = "my_timeline_avatar"
                ) {
                    AmityUserAvatarView(
                        size = 40.dp,
                        modifier = modifier.testTag(getAccessibilityId()),
                        user = currentUser,
                    )
                }

                AmityBaseElement(
                    pageScope = getPageScope(),
                    elementId = "my_timeline_text"
                ) {
                    Text(
                        text = getConfig().getText(),
                        style = AmityTheme.typography.bodyLegacy.copy(
                            fontWeight = FontWeight.SemiBold
                        ),
                        modifier = modifier.testTag(getAccessibilityId()),
                    )
                }
            }

            HorizontalDivider(
                color = AmityTheme.colors.divider,
                modifier = modifier.padding(horizontal = 16.dp, vertical = 8.dp),
            )

            AmityTargetSelectionMyCommunitiesView(
                modifier = modifier,
                contentType = AmityTargetContentType.POST
            ) {
                behavior.goToPostComposerPage(
                    context = context,
                    launcher = launcher,
                    targetId = it.getCommunityId(),
                    targetType = AmityPostTargetType.COMMUNITY,
                    community = it
                )
            }
        }
    }
}