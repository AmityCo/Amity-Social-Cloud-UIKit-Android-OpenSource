package com.amity.socialcloud.uikit.common.model

object AmityMessageReactions {
	private val reactions = mutableListOf<AmityReactionType>()
	
	fun addReaction(reaction: AmityReactionType) {
		if (reactions.none { it.name == reaction.name }) {
			reactions.add(reaction)
		}
	}
	
	fun getList(): List<AmityReactionType> {
		return reactions.toList()
	}
}