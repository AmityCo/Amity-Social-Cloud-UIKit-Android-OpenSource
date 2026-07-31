package com.amity.socialcloud.uikit.chat.compose.archive

import android.app.Activity
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
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
import androidx.compose.ui.platform.LocalContext
import com.amity.socialcloud.uikit.chat.compose.localization.amityChatString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.amity.socialcloud.sdk.model.chat.channel.AmityChannel
import com.amity.socialcloud.uikit.chat.compose.home.AmityChatHomePageBehavior
import com.amity.socialcloud.uikit.chat.compose.home.component.AmityChatListComponent
import com.amity.socialcloud.uikit.chat.compose.home.component.SwipeAction
import com.amity.socialcloud.uikit.common.compose.R as CommonR
import com.amity.socialcloud.uikit.common.eventbus.AmityUIKitSnackbar
import com.amity.socialcloud.uikit.common.ui.atoms.AmityButton
import com.amity.socialcloud.uikit.common.ui.atoms.AmityButtonHierarchy
import com.amity.socialcloud.uikit.common.ui.atoms.AmityButtonVariant
import com.amity.socialcloud.uikit.common.ui.atoms.AmityButtonStyle
import com.amity.socialcloud.uikit.common.ui.atoms.AmityEmptyState
import com.amity.socialcloud.uikit.common.ui.atoms.AmityEmptyStateVariant
import com.amity.socialcloud.uikit.common.ui.atoms.AmityIconButtonSize
import com.amity.socialcloud.uikit.common.ui.base.AmityBasePage
import com.amity.socialcloud.uikit.common.ui.theme.AmityTheme
import com.amity.socialcloud.uikit.common.ui.theme.AmityColorToken

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

    AmityBasePage(pageId = "archived_chat_page", useAmityToast = true) {
        Box(
            modifier = modifier
                .fillMaxSize()
                .background(AmityTheme.token(AmityColorToken.SurfacePageBackgroundDefault)),
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
                    AmityButton(
                        variant = AmityButtonVariant.ICON,
                        style = AmityButtonStyle.GHOST,
                        hierarchy = AmityButtonHierarchy.SECONDARY,
                        iconSize = AmityIconButtonSize.SIZE32,
                        icon = CommonR.drawable.amity_ic_chevron_left,
                        onClick = { (context as? Activity)?.finish() },
                        modifier = Modifier.align(Alignment.CenterStart),
                    )

                    Text(
                        text = amityChatString("chat.archived.navbar.title"),
                        style = AmityTheme.typography.titleLegacy.copy(
                            fontWeight = FontWeight.SemiBold,
                            color = AmityTheme.token(AmityColorToken.TextSheetsHeaderTitleDefault),
                        ),
                        modifier = Modifier
                            .padding(vertical = 17.dp)
                            .align(Alignment.Center),
                    )
                }

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
    AmityEmptyState(
        modifier = modifier.fillMaxSize(),
        variant = AmityEmptyStateVariant.ICON,
        icon = CommonR.drawable.amity_ic_inbox_l,
        title = amityChatString("chat.archived.empty.title"),
    )
}
