package com.amity.socialcloud.uikit.community.compose.livestream.chat

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.lifecycle.viewmodel.compose.LocalViewModelStoreOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import com.amity.socialcloud.sdk.api.core.AmityCoreClient
import com.amity.socialcloud.sdk.model.core.error.AmityError
import com.amity.socialcloud.sdk.model.core.error.AmityException
import com.amity.socialcloud.uikit.common.model.AmityMessageReactions
import com.amity.socialcloud.uikit.common.ui.base.AmityBaseComponent
import com.amity.socialcloud.uikit.common.ui.base.AmityBaseElement
import com.amity.socialcloud.uikit.common.ui.elements.AmityTextField
import com.amity.socialcloud.uikit.common.ui.scope.AmityComposePageScope
import com.amity.socialcloud.uikit.common.ui.theme.AmityTheme
import com.amity.socialcloud.uikit.common.utils.clickableWithoutRipple
import com.amity.socialcloud.uikit.common.utils.isSignedIn
import com.amity.socialcloud.uikit.common.utils.isVisitor
import com.amity.socialcloud.uikit.community.compose.AmitySocialBehaviorHelper
import com.amity.socialcloud.uikit.community.compose.R
import com.amity.socialcloud.uikit.community.compose.livestream.room.shared.AmityProductTaggingButton
import com.amity.socialcloud.uikit.community.compose.localization.DefaultAmitySocialStringProvider
import com.amity.socialcloud.uikit.community.compose.localization.amitySocialString
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun AmityLivestreamMessageComposeBar(
    modifier: Modifier = Modifier,
    pageScope: AmityComposePageScope? = null,
    channelId: String,
    value: String,
    isPendingApproval: Boolean = false,
    isNonMember: Boolean = false,
    streamHostUserId: String? = null,
    onValueChange: (String) -> Unit,
    onSend: () -> Unit,
    onReactionClick: () -> Unit,
    onReactionLongClick: () -> Unit,
    isMicrophoneMute: Boolean = false,
    onToggleMicrophone: (() -> Unit)? = null,
    onSwitchCamera: (() -> Unit)? = null,
    onOpenInviteSheet: (() -> Unit)? = null,
    isProductSettingsEnabled: Boolean = false,
    taggedProductsCount: Int = 0,
    canManageProducts: Boolean = false,
    onTaggedProductClick: (() -> Unit)? = null,
) {
    val viewModelStoreOwner = checkNotNull(LocalViewModelStoreOwner.current) {
        "No ViewModelStoreOwner was provided via LocalViewModelStoreOwner"
    }
    val viewModel = viewModel<AmityLivestreamChatViewModel>(
        factory = AmityLivestreamChatViewModel.create(channelId),
        viewModelStoreOwner = viewModelStoreOwner
    )

    val behavior = remember {
        AmitySocialBehaviorHelper.createLivestreamPageBehavior
    }

    val keyboardController = LocalSoftwareKeyboardController.current
    val focusRequester = remember { FocusRequester() }
    val focusManager = LocalFocusManager.current
    var isFocused by remember { mutableStateOf(false) }

    var messageText by remember { mutableStateOf(value) }
    val isTextValid by remember {
        derivedStateOf { messageText.isNotEmpty() }
    }
    var shouldClearText by remember { mutableStateOf(false) }

    val isChannelMuted by remember {
        derivedStateOf { }
        viewModel.getChannelFlow().map {
            it.isMuted()
        }.distinctUntilChanged()
    }.collectAsState(initial = false)

    val isChannelModerator by remember {
        derivedStateOf { }
        viewModel.isChannelModerator().distinctUntilChanged()
    }.collectAsState(initial = false)

    val membership by remember {
        viewModel.observeMembership()
            .distinctUntilChanged { old, new ->
                old.isMuted() == new.isMuted()
            }
    }.collectAsState(initial = null)

    val isCurrentUserMutedInMetadata by remember {
        viewModel.isUserMuted(com.amity.socialcloud.sdk.api.core.AmityCoreClient.getUserId())
    }.collectAsState(initial = false)

    val isCurrentUserStreamHost by remember {
        derivedStateOf {
            streamHostUserId != null && AmityCoreClient.getUserId() == streamHostUserId
        }
    }

    var showComposeErrorDialog = remember { mutableStateOf(false) }

    AmityBaseComponent(
        pageScope = pageScope,
        componentId = "message_composer",
    ) {
        Column(
            modifier = modifier
                .fillMaxWidth()
                .background(
                    if (isFocused) AmityTheme.colors.background
                    else Color.Transparent
                )
        ) {

            if (showComposeErrorDialog.value) {
                MessageComposeErrorPopup(
                    onDismiss = { showComposeErrorDialog.value = false }
                )
            }

            val isUserMuted = membership?.isMuted() == true || isCurrentUserMutedInMetadata

            // Show compose bar if user is not pending approval and either:
            // 1. Channel is not muted and user is not muted, OR
            // 2. User is the stream host (can always send messages)
            val shouldShowComposeBar = !isPendingApproval && (!isChannelMuted && !isUserMuted || isCurrentUserStreamHost)

            if (shouldShowComposeBar) {
                HorizontalDivider(
                    color = AmityTheme.colors.baseShade4,
                )
            }

            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Host can always see product tagging button
                if (isProductSettingsEnabled && (canManageProducts || taggedProductsCount > 0) && (isCurrentUserStreamHost || !isPendingApproval)) {
                    AmityProductTaggingButton(
                        taggedProductsCount = taggedProductsCount,
                        onClick = { onTaggedProductClick?.invoke() }
                    )
                }
                if (isPendingApproval) {
                    AmityLivestreamPendingApprovalComposeBar()
                } else if (isChannelMuted && !isCurrentUserStreamHost) {
                    // Read-only mode applies to everyone (members, non-members, visitors):
                    // show "This live stream is now read-only." before the non-member message.
                    AmityLivestreamReadOnlyComposeBar(
                        modifier = Modifier.weight(1f),
                    )
                } else if (isNonMember) {
                    AmityLivestreamNonMemberComposeBar(
                        modifier = Modifier.weight(1f)
                    )
                } else if (isUserMuted && !isCurrentUserStreamHost) {
                    AmityLivestreamReadOnlyComposeBar(
                        modifier = Modifier.weight(1f),
                        isMemberMuted = true,
                    )
                } else {
                    AmityBaseElement(
                        elementId = "message_composer_text_field",
                        componentScope = getComponentScope(),
                    ) {
                        AmityTextField(
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(20.dp))
                                .background(
                                    if (isFocused) AmityTheme.colors.baseShade4
                                    else Color.Transparent
                                )
                                .border(
                                    width = if (isFocused) 0.dp else 1.dp,
                                    color = AmityTheme.colors.secondaryShade1,
                                    shape = RoundedCornerShape(20.dp)
                                )
                                .focusRequester(focusRequester)
                                .onFocusChanged { focusState ->
                                    isFocused = focusState.isFocused
                                }
                                .let {
                                    if (AmityCoreClient.isVisitor()) {
                                        it.clickable {
                                            behavior.handleVisitorUserAction()
                                        }
                                    } else if (isNonMember) {
                                        it.clickable {
                                            behavior.handleNonMemberAction()
                                        }
                                    } else {
                                        it
                                    }
                                }
                                .testTag("message_composer_text_field"),
                            text = messageText,
                            textStyle = AmityTheme.typography.body.copy(color = AmityTheme.colors.base),
                            maxLines = 1,
                            hint = amitySocialString("amity_social_placeholder_chat_hint"),
                            hintColor = AmityTheme.colors.secondaryShade2,
                            enabled = (isChannelModerator || isCurrentUserStreamHost || (!isUserMuted && !isChannelMuted)) && AmityCoreClient.isSignedIn() && !isNonMember,
                            shape = RoundedCornerShape(20.dp),
                            innerPadding = PaddingValues(horizontal = 12.dp, vertical = 10.dp),
                            onValueChange = {
                                messageText = it
                                onValueChange(it)
                            },
                        )
                    }
                }
                val maxChar: Int = 200
                val defaultReaction =
                    AmityMessageReactions.getList().getOrNull(0) ?: AmityMessageReactions.getList()
                        .firstOrNull()
                if (isPendingApproval || isNonMember) {
                    Box {}
                } else if (messageText.isNotEmpty() || defaultReaction == null) {
                    Button(
                        onClick = {
                            if (messageText.isEmpty()) {
                                onReactionClick()
                                return@Button
                            }
                            if (messageText.length > maxChar) {
                                showComposeErrorDialog.value = true
                                return@Button
                            }
                            shouldClearText = true
                            val sendingMessage = messageText.trim()
                            messageText = ""
                            keyboardController?.hide()
                            focusManager.clearFocus()
                            viewModel.createMessage(
                                text = sendingMessage,
                                onSuccess = {
                                    onSend()
                                    shouldClearText = true
                                },
                                onError = { exception ->
                                    shouldClearText = false

                                    val stringProvider = DefaultAmitySocialStringProvider.getInstance()
                                    val errorMessage: String = if (exception is AmityException) {
                                        when (AmityError.from(exception.code)) {
                                            AmityError.BAN_WORD_FOUND -> stringProvider.getString("amity_social_label_msg_blocked_word")
                                            AmityError.LINK_NOT_ALLOWED -> stringProvider.getString("amity_social_label_msg_link_not_allowed")
                                            else -> exception.message
                                                ?: stringProvider.getString("amity_social_toast_message_send_failed")
                                        }
                                    } else {
                                        exception.message
                                            ?: stringProvider.getString("amity_social_toast_message_send_failed")
                                    }
                                    pageScope?.showSnackbar(
                                        message = errorMessage
                                    )
                                }
                            )
                        },
                        shape = CircleShape,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = AmityTheme.colors.primary,
                            contentColor = AmityTheme.colors.primaryShade4,
                            disabledContainerColor = AmityTheme.colors.primaryShade2,
                            disabledContentColor = AmityTheme.colors.primaryShade4,
                        ),
                        modifier = Modifier
                            .size(32.dp)
                            .align(Alignment.CenterVertically)
                            .clickable(enabled = isTextValid) {},
                        contentPadding = PaddingValues(1.dp)
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.amity_arrow_upward),
                            contentDescription = "Send",
                            modifier = Modifier.size(16.dp)
                        )
                    }
                } else if (onSwitchCamera != null || onToggleMicrophone != null || onOpenInviteSheet != null) {
                    if (onOpenInviteSheet != null) {
                        Box(
                            modifier = Modifier
                                .size(36.dp)
                                .align(Alignment.CenterVertically)
                                .clickableWithoutRipple {
                                    onOpenInviteSheet()
                                    keyboardController?.hide()
                                    focusManager.clearFocus()
                                }
                                .testTag("invite_sheet_button")
                        ) {
                            Image(
                                painter = painterResource(R.drawable.amity_ic_room_invite_button),
                                contentDescription = "",
                                modifier = Modifier
                                    .size(36.dp)
                            )
                        }
                    }
                    if (onToggleMicrophone != null) {
                        Box(
                            modifier = Modifier
                                .size(36.dp)
                                .align(Alignment.CenterVertically)
                                .clickableWithoutRipple {
                                    onToggleMicrophone()
                                    keyboardController?.hide()
                                    focusManager.clearFocus()
                                }
                                .testTag("toggle_microphone_button")
                        ) {
                            Image(
                                painter = if (isMicrophoneMute) { painterResource(R.drawable.amity_ic_room_unmute_button) } else {
                                    painterResource(R.drawable.amity_ic_room_mute_button)
                                },
                                contentDescription = "",
                                modifier = Modifier
                                    .size(36.dp)
                            )
                        }
                    }
                    if (onSwitchCamera != null) {
                        Box(
                            modifier = Modifier
                                .size(36.dp)
                                .align(Alignment.CenterVertically)
                                .clickableWithoutRipple {
                                    onSwitchCamera()
                                    keyboardController?.hide()
                                    focusManager.clearFocus()
                                }
                                .testTag("switch_camera_button")
                        ) {
                            Image(
                                painter = painterResource(R.drawable.amity_ic_room_switch_camera),
                                contentDescription = "",
                                modifier = Modifier
                                    .size(36.dp)
                            )
                        }
                    }
                } else {
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .align(Alignment.CenterVertically)
                            .pointerInput(onReactionLongClick) {
                                detectTapGestures(
                                    onTap = {
                                        onReactionClick()
                                        keyboardController?.hide()
                                        focusManager.clearFocus()
                                    },
                                    onLongPress = {
                                        onReactionLongClick()
                                        keyboardController?.hide()
                                        focusManager.clearFocus()
                                    }
                                )
                            }
                            .testTag("reaction_button")
                    ) {
                        Image(
                            imageVector = ImageVector.vectorResource(id = defaultReaction.icon),
                            contentDescription = "",
                            modifier = Modifier
                                .size(36.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun MessageComposeErrorPopup(
    onDismiss: () -> Unit,
) {

    Dialog(
        onDismissRequest = {},
        DialogProperties(
            usePlatformDefaultWidth = false
        )
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(16.dp))
                    .background(AmityTheme.colors.baseShade4)
                    .width(270.dp)
            ) {
                Column {
                    Column(
                        modifier = Modifier.padding(19.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = amitySocialString("amity_social_label_unable_to_send_message"),
                            fontSize = 17.sp,
                            lineHeight = 22.sp,
                            fontWeight = FontWeight(600),
                            textAlign = TextAlign.Center,
                            modifier = Modifier.fillMaxWidth(),
                            color = AmityTheme.colors.baseInverse,
                        )
                        Text(
                            text = amitySocialString("amity_social_label_your_message_is_too_long_please_shorten_your_message_an"),
                            fontSize = 13.sp,
                            lineHeight = 16.sp,
                            fontWeight = FontWeight(400),
                            textAlign = TextAlign.Center,
                            modifier = Modifier.fillMaxWidth(),
                            color = AmityTheme.colors.baseInverse,
                        )
                    }
                    HorizontalDivider(
                        thickness = 1.dp,
                        color = AmityTheme.colors.secondaryShade1
                    )
                    Row(
                        modifier = Modifier
                            .height(41.dp)
                            .fillMaxWidth()
                    ) {
                        TextButton(
                            onClick = { onDismiss.invoke() },
                            modifier = Modifier.weight(1f)
                        ) {
                            Text(
                                text = amitySocialString("amity_social_button_ok"),
                                fontSize = 17.sp,
                                lineHeight = 22.sp,
                                fontWeight = FontWeight(600),
                                color = AmityTheme.colors.primary,
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun AmityLivestreamNonMemberComposeBar(
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .height(40.dp)
            .fillMaxWidth(),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = amitySocialString("amity_social_status_join_community_to_interact_with_live_stream"),
            style = AmityTheme.typography.body,
            color = AmityTheme.colors.secondaryShade2,
            textAlign = TextAlign.Center,
        )
    }
}

@Composable
fun AmityLivestreamReadOnlyComposeBar(
    modifier: Modifier = Modifier,
    isMemberMuted: Boolean = false
) {
    Row(
        modifier = modifier
            .height(40.dp)
            .padding(start = 12.dp)
    ) {
        Icon(
            imageVector = ImageVector.vectorResource(id = R.drawable.amity_ic_livestream_chat_read_only),
            contentDescription = "Read Only Icon",
            modifier = Modifier
                .size(20.dp)
                .align(Alignment.CenterVertically),
            tint = AmityTheme.colors.baseShade1,
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = if (isMemberMuted) amitySocialString("amity_social_label_you_have_been_muted") else amitySocialString("amity_social_status_livestream_read_only"),
            fontSize = 14.sp,
            lineHeight = 20.sp,
            fontWeight = FontWeight(400),
            color = AmityTheme.colors.baseShade1,
            modifier = Modifier
                .align(Alignment.CenterVertically)
                .weight(1f)
        )
        Spacer(modifier = Modifier.width(8.dp))
    }
}

@Composable
fun AmityLivestreamPendingApprovalComposeBar(
    modifier: Modifier = Modifier,
    isMemberMuted: Boolean = false
) {
    Row(
        modifier = modifier
            .height(40.dp)
            .padding(start = 12.dp)
    ) {
        Text(
            text = amitySocialString("amity_social_status_this_live_stream_has_started_but_with_limited_visibilit"),
            fontSize = 14.sp,
            lineHeight = 20.sp,
            fontWeight = FontWeight(400),
            color = AmityTheme.colors.baseShade1,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .align(Alignment.CenterVertically)
                .weight(1f)
        )
        Spacer(modifier = Modifier.width(8.dp))
    }
}