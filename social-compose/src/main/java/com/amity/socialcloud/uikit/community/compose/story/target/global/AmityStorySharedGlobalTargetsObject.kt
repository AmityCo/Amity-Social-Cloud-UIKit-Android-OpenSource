package com.amity.socialcloud.uikit.community.compose.story.target.global

import com.amity.socialcloud.sdk.model.social.story.AmityStoryTarget

object AmityStorySharedGlobalTargetsObject {

    private var selectedTarget: Pair<Int, AmityStoryTarget?> = -1 to null

    private var targets: List<AmityStoryTarget> = emptyList()

    fun setTargets(targets: List<AmityStoryTarget>) {
        this.targets = targets
    }

    fun getTargets(): List<AmityStoryTarget> {
        return targets
    }

    fun setSelectedTarget(index: Int, target: AmityStoryTarget) {
        this.selectedTarget = index to target
    }

    fun getSelectedTarget(): Pair<Int, AmityStoryTarget?> {
        return selectedTarget
    }

    fun clear() {
        targets = emptyList()
        selectedTarget = -1 to null
    }
}