package com.amity.socialcloud.uikit.community.newsfeed.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import com.amity.socialcloud.sdk.model.core.reaction.AmityReaction
import com.amity.socialcloud.sdk.model.core.user.AmityUser
import com.amity.socialcloud.uikit.community.databinding.AmityItemReactionBinding
import com.amity.socialcloud.uikit.community.newsfeed.diffutil.ReactionDiffUtil
import io.reactivex.rxjava3.subjects.PublishSubject

class AmityReactionAdapter(
    private val userClickPublisher: PublishSubject<AmityUser>
) : PagingDataAdapter<AmityReaction, AmityReactionViewHolder>(ReactionDiffUtil()) {

    override fun onBindViewHolder(holder: AmityReactionViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AmityReactionViewHolder {
        val itemBinding = AmityItemReactionBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return AmityReactionViewHolder(itemBinding, userClickPublisher)
    }
}