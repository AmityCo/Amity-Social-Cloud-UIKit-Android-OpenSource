package com.amity.socialcloud.uikit.community.newsfeed.adapter

import android.content.Context
import android.content.res.ColorStateList
import androidx.core.content.ContextCompat
import com.amity.socialcloud.sdk.model.social.poll.AmityPollAnswer
import com.amity.socialcloud.uikit.community.R
import com.amity.socialcloud.uikit.community.databinding.AmityItemPollSingleAnswerBinding
import com.google.android.material.card.MaterialCardView

class AmityPollSingleAnswerViewHolder(
    context: Context,
    isEnabled: Boolean,
    voteCallback: (answerId: String, isSelected: Boolean, holder: MaterialCardView?) -> Unit
) : AmityPollAnswerViewHolder(context, R.layout.amity_item_poll_single_answer, isEnabled, voteCallback) {

    override fun bind(data: AmityPollAnswer) {
        val binding = AmityItemPollSingleAnswerBinding.bind(itemView)

        binding.voteRadioButton.isChecked = false
        binding.voteRadioButton.text = data.data
        binding.voteRadioButton.setTextColor(
            when (isEnabled) {
                true -> ContextCompat.getColor(context, R.color.amityColorBlack)
                false -> ContextCompat.getColor(context, R.color.amityPlaceHolderDarkColor)
            }
        )

        binding.voteRadioButton.buttonTintList = ColorStateList.valueOf(
            ContextCompat.getColor(
                context, when (isEnabled) {
                    true -> R.color.amityColorPrimary
                    false -> R.color.amityPlaceHolderDarkColor
                }
            )
        )

        binding.voteCardView.isEnabled = isEnabled
        binding.voteCardView.strokeColor = ContextCompat.getColor(context, R.color.upstraMessageBubbleInverse)
        binding.voteCardView.setOnClickListener {
            binding.voteRadioButton.isChecked = !binding.voteRadioButton.isChecked
            binding.voteCardView.strokeColor = when (binding.voteRadioButton.isChecked) {
                true -> ContextCompat.getColor(context, R.color.amityColorPrimary)
                false -> ContextCompat.getColor(context, R.color.upstraMessageBubbleInverse)
            }
            voteCallback.invoke(data.id, binding.voteRadioButton.isChecked, binding.voteCardView)
        }
    }
}