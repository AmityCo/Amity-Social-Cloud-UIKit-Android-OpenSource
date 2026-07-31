package com.amity.socialcloud.uikit.chat.compose.live.elements

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.amity.socialcloud.sdk.model.chat.message.AmityMessage
import com.amity.socialcloud.uikit.common.model.AmityMessageReactions
import com.amity.socialcloud.uikit.common.ui.atoms.AmityReaction
import com.amity.socialcloud.uikit.common.ui.atoms.AmityReactionCountContext
import com.amity.socialcloud.uikit.common.ui.atoms.AmityReactionItem
import com.amity.socialcloud.uikit.common.ui.atoms.AmityReactionVariant
import com.amity.socialcloud.uikit.common.ui.base.AmityBaseElement
import com.amity.socialcloud.uikit.common.ui.scope.AmityComposeComponentScope
import com.amity.socialcloud.uikit.common.ui.scope.AmityComposePageScope
import com.amity.socialcloud.uikit.common.utils.AmityNumberUtil

@Composable
fun AmityMessageReactionPreview(
	modifier: Modifier = Modifier,
	pageScope: AmityComposePageScope? = null,
	componentScope: AmityComposeComponentScope? = null,
	message: AmityMessage,
	// Optimistic display overrides — all null means "use live message data".
	overrideReactionMap: Map<String, Int>? = null,
	overrideReactionCount: Int? = null,
	overrideHasMyReaction: Boolean? = null,
) {
	AmityBaseElement(
		pageScope = pageScope,
		componentScope = componentScope,
		elementId = "message_reaction_preview"
	) {
		val reactionMap = overrideReactionMap ?: message.getReactionMap()
		val reactionCount = overrideReactionCount ?: message.getReactionCount()
		val hasMyReaction = overrideHasMyReaction ?: message.getMyReactions().isNotEmpty()
		if (!message.isDeleted()
			&& message.getState() == AmityMessage.State.SYNCED
			&& reactionCount > 0) {
			val topReactions = reactionMap.entries
				.filter { it.value > 0 }
				.sortedByDescending { it.value }
				.take(3)
				.map { resolveReactionItem(it.key) }

			AmityReaction(
				variant = AmityReactionVariant.COUNT,
				modifier = modifier,
				reactions = topReactions,
				displayCount = reactionCount.let(AmityNumberUtil::getNumberAbbreveation),
				countContext = AmityReactionCountContext.CHAT,
				active = hasMyReaction,
			)
		}
	}
}

/**
 * Resolves a reaction key to its currently-configured drawable. A `null` icon (no config match —
 * e.g. an admin removed this reaction after members already reacted with it) falls back to
 * [AmityReaction]'s separator-ring + missing glyph.
 */
private fun resolveReactionItem(reactionKey: String): AmityReactionItem {
	val icon = AmityMessageReactions.getList()
		.firstOrNull { it.name.equals(reactionKey, ignoreCase = true) }
		?.icon
	return AmityReactionItem(name = reactionKey, icon = icon)
}
