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
import androidx.compose.material3.HorizontalDivider
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
import com.amity.socialcloud.uikit.chat.compose.live.AmityLiveChatPageViewModel
import com.amity.socialcloud.uikit.common.ui.base.AmityBaseElement
import com.amity.socialcloud.uikit.common.ui.scope.AmityComposeComponentScope
import com.amity.socialcloud.uikit.common.ui.scope.AmityComposePageScope
import com.amity.socialcloud.uikit.common.ui.theme.AmityTheme

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
								option = "Reply",
								icon = ImageVector.vectorResource(id = R.drawable.amity_ic_reply_message),
								tint = AmityTheme.colors.baseInverse,
								onDismiss = onDismiss,
								action = onReply
							)
							HorizontalDivider(color = AmityTheme.colors.secondaryShade1)
						}
						AmityMessageOptionItem(
							option = "Copy",
							icon = ImageVector.vectorResource(id = R.drawable.amity_ic_copy_message),
							tint = AmityTheme.colors.baseInverse,
							onDismiss = onDismiss,
							action = {
								callCopyAction = true
							},
						)
						if (onFlag != null) {
							HorizontalDivider(color = AmityTheme.colors.secondaryShade1)
							AmityMessageOptionItem(
								option = "Report",
								icon = ImageVector.vectorResource(id = R.drawable.amity_ic_flag_message),
								tint = AmityTheme.colors.alert,
								onDismiss = onDismiss,
								action = onFlag
							)
						}
						if (onUnFlag != null) {
							HorizontalDivider(color = AmityTheme.colors.secondaryShade1)
							AmityMessageOptionItem(
								option = "Unreport",
								icon = ImageVector.vectorResource(id = R.drawable.amity_ic_flag_message),
								tint = AmityTheme.colors.alert,
								onDismiss = onDismiss,
								action = onUnFlag
							)
						}
						if (onDelete != null) {
							HorizontalDivider(color = AmityTheme.colors.secondaryShade1)
							AmityMessageOptionItem(
								option = "Delete",
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
			.padding(24.dp, 13.dp)
	) {
		Text(text = option,
			fontSize = 17.sp,
			lineHeight = 22.sp,
			fontWeight = FontWeight(400),
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
					viewModel = viewModel
				)
			} else {
				BottomConfirmDeletePopup(
					pageScope = pageScope,
					componentScope = componentScope,
					viewModel = viewModel
				)
			}
		}
	}
}

@Composable
fun CenterConfirmDeletePopup(
	pageScope: AmityComposePageScope? = null,
	componentScope: AmityComposeComponentScope? = null,
	viewModel: AmityLiveChatPageViewModel,
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
							text = "Delete this message?",
							fontSize = 17.sp,
							lineHeight = 22.sp,
							fontWeight = FontWeight(600),
							textAlign = TextAlign.Center,
							modifier = Modifier.fillMaxWidth(),
							color = AmityTheme.colors.baseInverse,
						)
						Text(
							text = "This message will also be removed from your friend’s devices.",
							fontSize = 13.sp,
							lineHeight = 16.sp,
							fontWeight = FontWeight(400),
							textAlign = TextAlign.Center,
							modifier = Modifier.fillMaxWidth(),
							color = AmityTheme.colors.baseInverse,
						)
					}
					HorizontalDivider(thickness = 1.dp, color = AmityTheme.colors.secondaryShade1)
					Row(
						modifier = Modifier
							.height(41.dp)
							.fillMaxWidth()
					) {
						TextButton(
							onClick = { viewModel.dismissDeleteConfirmation() },
							modifier = Modifier.weight(0.5f)
						) {
							Text(
								text = "Cancel",
								fontSize = 17.sp,
								lineHeight = 22.sp,
								fontWeight = FontWeight(600),
								color = AmityTheme.colors.primary,
							)
						}
						VerticalDivider(thickness = 1.dp, color = AmityTheme.colors.secondaryShade1)
						TextButton(
							onClick = {
								viewModel.deleteMessage(
									onError = {
										pageScope?.showSnackbar(
											message = "Unable to delete message. Please try again."
										)
									}
								)
							},
							modifier = Modifier.weight(0.5f)
						) {
							Text(
								text = "Delete",
								fontSize = 17.sp,
								lineHeight = 22.sp,
								fontWeight = FontWeight(600),
								color = AmityTheme.colors.alert,
							)
						}
					}
				}
			}
		}
	}
}


@Composable
fun BottomConfirmDeletePopup(
	pageScope: AmityComposePageScope? = null,
	componentScope: AmityComposeComponentScope? = null,
	viewModel: AmityLiveChatPageViewModel,
) {
	Dialog(
		onDismissRequest = {
			viewModel.dismissDeleteConfirmation()
		},
		DialogProperties(
			usePlatformDefaultWidth = false
		)
	) {
		Box(
			modifier = Modifier
				.fillMaxSize(),
			contentAlignment = Alignment.BottomCenter
		) {
			Column {
				Column(
					modifier = Modifier
						.padding(horizontal = 8.dp)
						.clip(RoundedCornerShape(14.dp))
						.background(AmityTheme.colors.baseShade4)
						.fillMaxWidth()
				) {
					Column(
						modifier = Modifier.padding(vertical = 13.dp),
						horizontalAlignment = Alignment.CenterHorizontally
					) {
						Text(
							text = "Your message wasn’t sent",
							fontSize = 13.sp,
							lineHeight = 18.sp,
							fontWeight = FontWeight(600),
							textAlign = TextAlign.Center,
							modifier = Modifier.fillMaxWidth(),
							color = AmityTheme.colors.baseInverse,
						)
					}
					HorizontalDivider(thickness = 1.dp, color = AmityTheme.colors.secondaryShade1)
					Row(
						modifier = Modifier
							.fillMaxWidth()
					) {
						TextButton(
							onClick = {
								viewModel.deleteMessage(
									onError = {
										pageScope?.showSnackbar(
											message = "Unable to delete message. Please try again."
										)
									}
								)
							},
							modifier = Modifier.fillMaxWidth()
						) {
							Text(
								text = "Delete",
								fontSize = 17.sp,
								lineHeight = 22.sp,
								fontWeight = FontWeight(600),
								color = AmityTheme.colors.alert,
							)
						}
					}
				}
				Spacer(modifier = Modifier.height(8.dp))
				Column(
					modifier = Modifier
						.padding(horizontal = 8.dp)
						.clip(RoundedCornerShape(14.dp))
						.background(AmityTheme.colors.baseShade4)
						.fillMaxWidth()
				) {
					Row(
						modifier = Modifier
							.fillMaxWidth()
					) {
						TextButton(
							onClick = {
								viewModel.dismissDeleteConfirmation()
							},
							modifier = Modifier.fillMaxWidth()
						) {
							Text(
								text = "Cancel",
								fontSize = 17.sp,
								lineHeight = 22.sp,
								fontWeight = FontWeight(600),
								color = AmityTheme.colors.primary,
							)
						}
					}
				}
				Spacer(modifier = Modifier.height(30.dp))
			}
		}
	}
}

@Preview(showBackground = true)
@Composable
fun BottomConfirmDeletePopupPreview() {
	BottomConfirmDeletePopup(
		viewModel = AmityLiveChatPageViewModel("")
	)
}