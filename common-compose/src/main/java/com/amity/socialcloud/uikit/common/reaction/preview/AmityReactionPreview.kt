package com.amity.socialcloud.uikit.common.reaction.preview

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import com.amity.socialcloud.sdk.model.chat.message.AmityMessage
import com.amity.socialcloud.uikit.common.compose.R
import com.amity.socialcloud.uikit.common.model.AmityMessageReactions
import com.amity.socialcloud.uikit.common.model.AmitySocialReactions
import com.amity.socialcloud.uikit.common.ui.base.AmityBaseElement
import com.amity.socialcloud.uikit.common.ui.scope.AmityComposeComponentScope
import com.amity.socialcloud.uikit.common.ui.scope.AmityComposePageScope
import com.amity.socialcloud.uikit.common.ui.theme.AmityTheme
import com.amity.socialcloud.uikit.common.utils.AmityNumberUtil

@Composable
fun AmityReactionPreview(
	modifier: Modifier = Modifier,
	pageScope: AmityComposePageScope? = null,
	componentScope: AmityComposeComponentScope? = null,
	myReaction: String? = null,
	reactions: Map<String, Int> = emptyMap(),
	reactionCount: Int = 0,
	showContainer: Boolean = false,
) {
	AmityBaseElement(
		pageScope = pageScope,
		componentScope = componentScope,
		elementId = "reaction_preview"
	) {
		var countOffset = 0
		val hasMyReaction = myReaction?.isNotEmpty() == true
		Box(
			modifier = modifier
				.wrapContentWidth()
				.let {
					if (showContainer) {
						it.border(
							width = 1.dp,
							color = AmityTheme.colors.baseShade4,
							shape = RoundedCornerShape(size = 16.dp)
						)
							.background(
								color = AmityTheme.colors.background,
								shape = RoundedCornerShape(size = 16.dp)
							)
					} else {
						it
					}
				}
				.padding(start = 6.dp, top = 4.dp, end = 6.dp, bottom = 4.dp),
		) {
			Layout(
				content = {
					Row(
						verticalAlignment = Alignment.CenterVertically,
					) {
						reactions
							.entries
							.toList()
							.filter { it.value > 0 }
							.sortedByDescending { it.value }
							.take(5)
							.apply {
								countOffset = if (isNotEmpty()) {
									(size.dec() * -8)
								} else {
									0
								}
							}
							.mapIndexed { index, it ->
								AmityReactionPreviewIcon(
									reaction = it.key,
									modifier = Modifier
										.offset(
											x = (index * -8).dp
										)
										.zIndex(1f - (0.1f * index))
										.border(
											width = 1.dp,
											color = AmityTheme.colors.background,
											shape = RoundedCornerShape(size = 16.dp)
										)
								)
							}
						Spacer(modifier = Modifier.width(4.dp))
						Text(
							text = reactionCount
								.let(AmityNumberUtil::getNumberAbbreveation),
							style = AmityTheme.typography.caption.copy(
								color = if (showContainer) AmityTheme.colors.base else AmityTheme.colors.baseShade2
							),
							modifier = Modifier
								.offset(
									x = countOffset.dp
								)
								.wrapContentWidth()
						)
					}
				},
				measurePolicy = { measurables, constraints ->
					// Measure each child
					val placeables = measurables.map { measurable ->
						measurable.measure(constraints)
					}

					// Determine the width of the Row
					val width = placeables.foldIndexed(0) { index, acc, placeable ->
						if (index < placeables.lastIndex)
							acc + placeable.width + (index * -8).dp.roundToPx() // Calculate width for the icon
						else
							acc + placeable.width + countOffset.dp.roundToPx() // Calculate width for the count
					}

					// Set the size of the Row
					layout(width, placeables.maxOfOrNull { it.height } ?: 0) {
						// Place each child
						var xPosition = 0
						placeables.forEach { placeable ->
							placeable.placeRelative(IntOffset(xPosition, 0))
							xPosition += placeable.width
						}
					}
				})
		}
	}
}

@Composable
fun AmityReactionPreviewIcon(
	modifier: Modifier = Modifier,
	reaction: String
) {
	AmitySocialReactions.getList().firstOrNull { it.name == reaction }?.let {
		Image(
			imageVector = ImageVector.vectorResource(id = it.icon),
			contentDescription = reaction,
			modifier = modifier
				.size(20.dp)
		)
	} ?: Image(
		imageVector = ImageVector.vectorResource(id = R.drawable.amity_ic_message_reaction_missing),
		contentDescription = reaction,
		modifier = modifier
			.size(20.dp)
	)
}