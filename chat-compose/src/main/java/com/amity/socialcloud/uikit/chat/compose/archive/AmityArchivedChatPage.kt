package com.amity.socialcloud.uikit.chat.compose.archive

import android.app.Activity
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import com.amity.socialcloud.uikit.chat.compose.localization.amityChatString
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.amity.socialcloud.sdk.model.chat.channel.AmityChannel
import com.amity.socialcloud.uikit.chat.compose.R
import com.amity.socialcloud.uikit.chat.compose.home.AmityChatHomePageBehavior
import com.amity.socialcloud.uikit.chat.compose.home.component.AmityChatListComponent
import com.amity.socialcloud.uikit.chat.compose.home.component.SwipeAction
import com.amity.socialcloud.uikit.common.eventbus.AmityUIKitSnackbar
import com.amity.socialcloud.uikit.common.ui.base.AmityBasePage
import com.amity.socialcloud.uikit.common.ui.theme.AmityTheme
import com.amity.socialcloud.uikit.common.utils.clickableWithoutRipple
import kotlinx.coroutines.launch

@Composable
fun AmityArchivedChatPage(
    modifier: Modifier = Modifier,
) {
    val viewModel: AmityArchivedChatPageViewModel = viewModel()
    val context = LocalContext.current
    val behavior = remember { AmityChatHomePageBehavior() }
    val scrollScope = rememberCoroutineScope()
    val unarchivedMessage = amityChatString("chat.unarchived.toast")
    val unarchiveErrorMessage = amityChatString("chat.unarchive.error.toast")
    val otherMembersMap by viewModel.otherMembers.collectAsState()

    AmityBasePage(pageId = "archived_chat_page") {
        Box(
            modifier = modifier
                .fillMaxSize()
                .background(AmityTheme.colors.background),
        ) {
            Column(
                modifier = Modifier.fillMaxSize(),
            ) {
                // Header
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 12.dp),
                ) {
                    Icon(
                        imageVector = ImageVector.vectorResource(id = R.drawable.amity_ic_chat_back),
                        contentDescription = "Back",
                        modifier = Modifier
                            .size(24.dp)
                            .align(Alignment.CenterStart)
                            .clickableWithoutRipple {
                                (context as? Activity)?.finish()
                            },
                        tint = AmityTheme.colors.base,
                    )

                    Text(
                        text = amityChatString("chat.archived.navbar.title"),
                        style = AmityTheme.typography.titleLegacy.copy(
                            fontWeight = FontWeight.SemiBold,
                        ),
                        modifier = Modifier
                            .padding(vertical = 17.dp)
                            .align(Alignment.Center),
                    )
                }

                HorizontalDivider(color = AmityTheme.colors.baseShade4)

                AmityChatListComponent(
                    modifier = Modifier.fillMaxSize(),
                    componentId = "archived_chat_list",
                    channelsFlow = viewModel.archivedChannels,
                    isLoadingOverride = viewModel.isLoading,
                    emptyContent = { ArchivedChatEmptyState() },
                    otherMembersMap = otherMembersMap,
                    onFetchOtherMember = { viewModel.fetchOtherMember(it) },
                    onChannelClick = { channel ->
                        when (channel.getChannelType()) {
                            AmityChannel.Type.CONVERSATION -> {
                                behavior.goToConversationChatPage(context, channel.getChannelId())
                            }
                            AmityChannel.Type.COMMUNITY -> {
                                behavior.goToGroupChatPage(context, channel.getChannelId())
                            }
                            else -> {}
                        }
                    },
                    swipeAction = SwipeAction.UNARCHIVE,
                    onSwipeAction = { channel ->
                        viewModel.unarchiveChannel(
                            channelId = channel.getChannelId(),
                            onSuccess = {
                                AmityUIKitSnackbar.publishSnackbarMessage(
                                    message = unarchivedMessage,
                                )
                            },
                            onError = {
                                AmityUIKitSnackbar.publishSnackbarMessage(
                                    message = unarchiveErrorMessage,
                                )
                            },
                        )
                    },
                )
            }
        }
    }
}

@Composable
private fun ArchivedChatEmptyState(
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Icon(
            imageVector = ImageVector.vectorResource(id = R.drawable.amity_ic_no_archive_chat),
            contentDescription = null,
            modifier = Modifier.size(60.dp),
            tint = AmityTheme.colors.baseShade3,
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = amityChatString("chat.archived.empty.title"),
            style = AmityTheme.typography.bodyLegacy.copy(
                fontWeight = FontWeight.SemiBold,
                fontSize = 17.sp,
                color = AmityTheme.colors.baseShade3,
            ),
        )
    }
}
