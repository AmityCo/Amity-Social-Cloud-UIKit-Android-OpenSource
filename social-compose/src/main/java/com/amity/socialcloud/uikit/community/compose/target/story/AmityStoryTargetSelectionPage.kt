package com.amity.socialcloud.uikit.community.compose.target.story


import android.app.Activity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.amity.socialcloud.sdk.model.social.story.AmityStory
import com.amity.socialcloud.uikit.common.ui.base.AmityBaseElement
import com.amity.socialcloud.uikit.common.ui.base.AmityBasePage
import com.amity.socialcloud.uikit.common.ui.theme.AmityTheme
import com.amity.socialcloud.uikit.common.utils.clickableWithoutRipple
import com.amity.socialcloud.uikit.common.utils.closePage
import com.amity.socialcloud.uikit.common.utils.closePageWithResult
import com.amity.socialcloud.uikit.common.utils.getIcon
import com.amity.socialcloud.uikit.common.utils.getText
import com.amity.socialcloud.uikit.community.compose.AmitySocialBehaviorHelper
import com.amity.socialcloud.uikit.community.compose.target.components.AmityTargetContentType
import com.amity.socialcloud.uikit.community.compose.target.components.AmityTargetSelectionMyCommunitiesView

@Composable
fun AmityStoryTargetSelectionPage(
    modifier: Modifier = Modifier,
) {
    val context = LocalContext.current
    val behavior by lazy {
        AmitySocialBehaviorHelper.storyTargetSelectionPageBehavior
    }

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            context.closePageWithResult(Activity.RESULT_OK)
        }
    }

    AmityBasePage(
        "select_story_target_page"
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
                    )
                }

                AmityBaseElement(
                    pageScope = getPageScope(),
                    elementId = "title"
                ) {
                    Text(
                        text = getConfig().getText(),
                        style = AmityTheme.typography.titleLegacy,
                        modifier = modifier.align(Alignment.Center)
                    )
                }
            }

            AmityTargetSelectionMyCommunitiesView(
                modifier = modifier,
                contentType = AmityTargetContentType.STORY,
            ) { community ->
                behavior.goToStoryCreationPage(
                    context = context,
                    launcher = launcher,
                    targetId = community.getCommunityId(),
                    targetType = AmityStory.TargetType.COMMUNITY,
                )
            }
        }
    }
}