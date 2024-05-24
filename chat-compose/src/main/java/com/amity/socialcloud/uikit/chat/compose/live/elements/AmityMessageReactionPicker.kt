package com.amity.socialcloud.uikit.chat.compose.live.elements

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Popup
import com.amity.socialcloud.sdk.model.chat.message.AmityMessage
import com.amity.socialcloud.uikit.common.model.AmityMessageReactions
import com.amity.socialcloud.uikit.common.ui.base.AmityBaseElement
import com.amity.socialcloud.uikit.common.ui.scope.AmityComposeComponentScope
import com.amity.socialcloud.uikit.common.ui.scope.AmityComposePageScope
import com.amity.socialcloud.uikit.common.ui.theme.AmityTheme

@Composable
fun AmityMessageReactionPicker(
	modifier: Modifier = Modifier,
	pageScope: AmityComposePageScope? = null,
	componentScope: AmityComposeComponentScope? = null,
	message: AmityMessage,
	show: Boolean,
	onAddReaction: (AmityMessage, String) -> Unit,
	onRemoveReaction: (AmityMessage, String) -> Unit,
	onDismiss: () -> Unit,
) {
	if (show) {
		AmityBaseElement(
			pageScope = pageScope,
			componentScope = componentScope,
			elementId = "message_reaction"
		) {
			Popup(
				alignment = Alignment.BottomStart,
				onDismissRequest = onDismiss,
			) {
				Row {
					Row(
						modifier = Modifier
							.shadow(
								shape = RoundedCornerShape(32.dp),
								elevation = 4.dp,
								clip = true
							)
							.clip(RoundedCornerShape(28.dp))
							.background(color = AmityTheme.colors.baseShade4)
							.padding(vertical = 4.dp, horizontal = 6.dp)
					) {
						val myReaction = message.getMyReactions().firstOrNull()
						AmityMessageReactions
							.getList()
							.mapIndexed { index, reaction ->
								val isSelected = myReaction == reaction.name
								if (index > 0) {
									Spacer(modifier = Modifier.width(6.dp))
								}
								AmityMessageReactionItem(
									icon = ImageVector.vectorResource(id = reaction.icon),
									isSelected = isSelected,
									onDismiss = onDismiss,
									action = {
										if (isSelected) {
											onRemoveReaction.invoke(message, reaction.name)
										} else {
											onAddReaction.invoke(message, reaction.name)
										}
									}
								)
							}
					}
				}
			}
		}
	}
}

@Composable
fun AmityMessageReactionItem(
	modifier: Modifier = Modifier,
	icon: ImageVector,
	isSelected: Boolean = false,
	onDismiss: () -> Unit,
	action: () -> Unit,
) {
	Row(
		modifier = modifier
			.clickable {
				action.invoke()
				onDismiss()
			}
			.clip(RoundedCornerShape(28.dp))
			.background(
				color = if (isSelected) {
					AmityTheme.colors.baseShade1
				} else {
					Color.Transparent
				}
			)
			.padding(5.dp)
	){
		Column {
			Image(
				imageVector = icon,
				contentDescription = "",
				modifier = Modifier
					.size(32.dp)
			)
		}
	}
	
}
