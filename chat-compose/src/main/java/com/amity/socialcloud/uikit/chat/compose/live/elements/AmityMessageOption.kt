package com.amity.socialcloud.uikit.chat.compose.live.elements

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.compose.ui.window.Popup
import com.amity.socialcloud.sdk.model.chat.message.AmityMessage
import com.amity.socialcloud.uikit.chat.compose.R
import com.amity.socialcloud.uikit.chat.compose.common.AmityChatConfirmDialog
import com.amity.socialcloud.uikit.chat.compose.live.AmityLiveChatPageViewModel
import com.amity.socialcloud.uikit.chat.compose.localization.DefaultAmityChatStringProvider
import com.amity.socialcloud.uikit.chat.compose.localization.amityChatString
import com.amity.socialcloud.uikit.common.compose.R as CommonR
import com.amity.socialcloud.uikit.common.ui.atoms.AmityDivider
import com.amity.socialcloud.uikit.common.ui.atoms.AmityDividerVariant
import com.amity.socialcloud.uikit.common.ui.base.AmityBaseElement
import com.amity.socialcloud.uikit.common.ui.elements.BottomConfirmDeletePopup
import com.amity.socialcloud.uikit.common.ui.scope.AmityComposeComponentScope
import com.amity.socialcloud.uikit.common.ui.scope.AmityComposePageScope
import com.amity.socialcloud.uikit.common.ui.theme.AmityColorToken
import com.amity.socialcloud.uikit.common.ui.theme.AmityTheme
import com.amity.socialcloud.uikit.common.localization.amityCommonString

@Composable
fun AmityMessageOption(
	modifier: Modifier = Modifier,
	pageScope: AmityComposePageScope? = null,
	componentScope: AmityComposeComponentScope? = null,
	show: Boolean,
	onMessageAction: AmityMessageAction = AmityMessageAction(),
	onDismiss: () -> Unit,
) {
	var callCopyAction by remember { mutableStateOf(false) }
	if (callCopyAction) {
		onMessageAction.onCopy?.invoke()
		callCopyAction = false
	}
	val onDelete = onMessageAction.onDelete
	val onReply = onMessageAction.onReply
	val onFlag = onMessageAction.onFlag
	val onUnFlag = onMessageAction.onUnFlag
	if (show) {
		AmityBaseElement(
			pageScope = pageScope,
			componentScope = componentScope,
			elementId = "message_option"
		) {
			Popup(
				onDismissRequest = onDismiss,
			) {
				Column(
					modifier = modifier
						.shadow(
							shape = RoundedCornerShape(10.dp),
							elevation = 4.dp,
							clip = true,
						)
						.clip(RoundedCornerShape(10.dp))
						.background(color = AmityTheme.colors.baseShade4)
				) {
					Column {
						if (onReply != null) {
							AmityMessageOptionItem(
								option = amityChatString("chat.option.reply"),
								icon = ImageVector.vectorResource(id = R.drawable.amity_ic_reply_message),
								tint = AmityTheme.colors.baseInverse,
								onDismiss = onDismiss,
								action = onReply
							)
							// Resolved to Content: intra-menu row separator (matches AmityMessageActionMenu).
							AmityDivider(variant = AmityDividerVariant.Content, inset = false)
						}
						AmityMessageOptionItem(
							option = amityChatString("chat.option.copy"),
							icon = ImageVector.vectorResource(id = R.drawable.amity_ic_copy_message),
							tint = AmityTheme.colors.baseInverse,
							onDismiss = onDismiss,
							action = {
								callCopyAction = true
							},
						)
						if (onFlag != null) {
							AmityDivider(variant = AmityDividerVariant.Content, inset = false)
							AmityMessageOptionItem(
								option = amityChatString("chat.option.report"),
								icon = ImageVector.vectorResource(id = R.drawable.amity_ic_flag_message),
								tint = AmityTheme.colors.alert,
								onDismiss = onDismiss,
								action = onFlag
							)
						}
						if (onUnFlag != null) {
							AmityDivider(variant = AmityDividerVariant.Content, inset = false)
							AmityMessageOptionItem(
								option = amityChatString("chat.option.unreport"),
								icon = ImageVector.vectorResource(id = CommonR.drawable.amity_ic_flag_slash_r),
								tint = AmityTheme.colors.alert,
								onDismiss = onDismiss,
								action = onUnFlag
							)
						}
						if (onDelete != null) {
							AmityDivider(variant = AmityDividerVariant.Content, inset = false)
							AmityMessageOptionItem(
								option = amityChatString("chat.option.delete"),
								icon = ImageVector.vectorResource(id = R.drawable.amity_ic_delete_message),
								tint = AmityTheme.colors.alert,
								onDismiss = onDismiss,
								action = onDelete
							)
						}
					}

				}
			}
		}
	}
}

@Composable
fun AmityMessageOptionItem(
	option: String,
	icon: ImageVector,
	tint: Color = AmityTheme.typography.bodyLegacy.color,
	onDismiss: () -> Unit,
	action: () -> Unit,
) {
	Row(
		modifier = Modifier
			.clickable {
				onDismiss()
				action()

			}
			.padding(24.dp, 12.dp)
	) {
		Text(text = option,
			style = AmityTheme.typography.body,
			color = tint,
			modifier = Modifier.wrapContentWidth()
		)
		Spacer(modifier = Modifier.weight(1.0f))
		Icon(
			imageVector = icon,
			contentDescription = option,
			tint = tint,
			modifier = Modifier.size(20.dp))
	}
}

@Composable
fun ConfirmDeletePopup(
	pageScope: AmityComposePageScope? = null,
	componentScope: AmityComposeComponentScope? = null,
	viewModel: AmityLiveChatPageViewModel,
) {

	AmityBaseElement(
		pageScope = pageScope,
		componentScope = componentScope,
		elementId = "confirm_delete_message"
	) {
		val targetMessage = viewModel.targetDeletedMessage.value
		if (viewModel.showDeleteDialog.value && targetMessage != null) {
			if (targetMessage.getState() == AmityMessage.State.SYNCED) {
				CenterConfirmDeletePopup(
					pageScope = pageScope,
					componentScope = componentScope,
					onCancel = {
						viewModel.dismissDeleteConfirmation()
					},
					onDelete = {
						viewModel.deleteMessage(
							onError = {
								pageScope?.showSnackbar(
									message = DefaultAmityChatStringProvider.getInstance().getString("chat.toast.delete.error")
								)
							}
						)
					}
				)
			} else {
				BottomConfirmDeletePopup(
					pageScope = pageScope,
					componentScope = componentScope,
					onDelete = {
						viewModel.deleteMessage(
							onError = {
								pageScope?.showSnackbar(
									message = DefaultAmityChatStringProvider.getInstance().getString("chat.toast.delete.error")
								)
							}
						)
					},
					onDismiss = {
						viewModel.dismissDeleteConfirmation()
					},
				)
			}
		}
	}
}

@Composable
fun CenterConfirmDeletePopup(
	pageScope: AmityComposePageScope? = null,
	componentScope: AmityComposeComponentScope? = null,
	onCancel: (() -> Unit)? = null,
	onDelete: (() -> Unit)? = null
) {
	// Delegates to chat's shared native confirm dialog — same title/body/actions, token-bound.
	// pageScope/componentScope kept for caller API stability.
	AmityChatConfirmDialog(
		title = amityChatString("chat.delete.alert.title"),
		message = amityChatString("chat.delete.alert.message"),
		confirmLabel = amityCommonString("amity_common_button_delete"),
		onConfirm = { onDelete?.invoke() },
		onDismiss = { onCancel?.invoke() },
	)
}