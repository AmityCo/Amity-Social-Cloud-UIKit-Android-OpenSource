package com.amity.socialcloud.uikit.community.compose.livestream.chat

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
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
import com.amity.socialcloud.sdk.model.chat.member.AmityMembershipType
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
    onValueChange: (String) -> Unit,
    onSend: () -> Unit,
    onReactionClick: () -> Unit,
    onReactionLongClick: () -> Unit,
    onSwitchCamera: (() -> Unit)? = null,
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

    var showComposeErrorDialog = remember { mutableStateOf(false) }

    AmityBaseComponent(
        pageScope = pageScope,
        componentId = "message_composer",
    ) {
        Column(
            modifier = modifier
                .fillMaxWidth()
                .background(
                    if (isFocused) Color(0xFF191919)
                    else Color.Transparent
                )
        ) {

            if (showComposeErrorDialog.value) {
                MessageComposeErrorPopup(
                    onDismiss = { showComposeErrorDialog.value = false }
                )
            }

            if (!isChannelMuted && membership?.isMuted() != true && !isPendingApproval) {
                HorizontalDivider(
                    color = Color(0xFF292B32),
                )
            }

            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp, vertical = 8.dp)
            ) {
                if (isPendingApproval) {
                    AmityLivestreamPendingApprovalComposeBar()
                } else if (isChannelMuted) {
                    AmityLivestreamReadOnlyComposeBar(
                        modifier = Modifier.weight(1f),
                    )
                } else if (membership?.isMuted() == true) {
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
                                    if (isFocused) Color(0xFF292B32)
                                    else Color.Transparent
                                )
                                .border(
                                    width = if (isFocused) 0.dp else 1.dp,
                                    color = Color(0xFFA5A9B5),
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
                            textStyle = AmityTheme.typography.body.copy(color = Color(0xFFEBECEF)),
                            maxLines = 1,
                            hint = "Chat...",
                            enabled = (isChannelModerator || ((membership?.isMuted() != true) && !isChannelMuted)) && AmityCoreClient.isSignedIn() && !isNonMember,
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
                    AmityMessageReactions.getList().getOrNull(1) ?: AmityMessageReactions.getList()
                        .firstOrNull()
                if (isPendingApproval) {
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
                            keyboardController?.hide()
                            focusManager.clearFocus()
                            viewModel.createMessage(
                                text = messageText.trim(),
                                onSuccess = {
                                    messageText = ""
                                    shouldClearText = true
                                },
                                onError = { exception ->
                                    messageText = ""
                                    shouldClearText = false

                                    val errorMessage: String = if (exception is AmityException) {
                                        when (AmityError.from(exception.code)) {
                                            AmityError.BAN_WORD_FOUND -> "Your message wasn't sent as it contained a blocked word."
                                            AmityError.LINK_NOT_ALLOWED -> "Your message wasn't sent as it contained a link that's not allowed."
                                            else -> exception.message
                                                ?: "This message failed to be sent. Please try again."
                                        }
                                    } else {
                                        exception.message
                                            ?: "This message failed to be sent. Please try again."
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
                } else if (onSwitchCamera != null) {
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
                            painter = painterResource(R.drawable.amity_v4_switch_camera_button),
                            contentDescription = "",
                            modifier = Modifier
                                .size(36.dp)
                        )
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
                            text = "Unable to send message",
                            fontSize = 17.sp,
                            lineHeight = 22.sp,
                            fontWeight = FontWeight(600),
                            textAlign = TextAlign.Center,
                            modifier = Modifier.fillMaxWidth(),
                            color = AmityTheme.colors.baseInverse,
                        )
                        Text(
                            text = "Your message is too long. Please shorten your message and try again.",
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
                                text = "OK",
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
            tint = Color(0xFF898E9E),
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = if (isMemberMuted) "You have been muted." else "This live stream is now read-only.",
            fontSize = 14.sp,
            lineHeight = 20.sp,
            fontWeight = FontWeight(400),
            color = Color(0xFF898E9E),
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
            text = "This live stream has started, but with limited visibility until the post has been approved.",
            fontSize = 14.sp,
            lineHeight = 20.sp,
            fontWeight = FontWeight(400),
            color = Color(0xFF898E9E),
            textAlign = TextAlign.Center,
            modifier = Modifier
                .align(Alignment.CenterVertically)
                .weight(1f)
        )
        Spacer(modifier = Modifier.width(8.dp))
    }
}