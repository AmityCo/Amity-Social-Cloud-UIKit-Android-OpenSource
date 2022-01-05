package com.amity.socialcloud.uikit.community.newsfeed.adapter

import android.content.Context
import android.view.View
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.core.widget.doAfterTextChanged
import com.amity.socialcloud.uikit.common.base.AmityViewHolder
import com.amity.socialcloud.uikit.common.utils.AmityAndroidUtil
import com.amity.socialcloud.uikit.community.R
import com.amity.socialcloud.uikit.community.databinding.AmityItemPollDraftAnswerBinding
import com.amity.socialcloud.uikit.community.newsfeed.fragment.MAX_ANSWER_LENGTH

class AmityPollDraftAnswerViewHolder(val context: Context, private val removeCallback: (answer: AmityPollDraftAnswerAdapter.DraftAnswer) -> Unit) :
    AmityViewHolder<AmityPollDraftAnswerAdapter.DraftAnswer>(View.inflate(context, R.layout.amity_item_poll_draft_answer, null)) {

    private var binding: AmityItemPollDraftAnswerBinding = AmityItemPollDraftAnswerBinding.bind(itemView)

    override fun bind(data: AmityPollDraftAnswerAdapter.DraftAnswer) {
        binding.answerEditText.setText(data.data)

        updateAnswer(data)

        binding.answerEditText.doAfterTextChanged {
            data.data = it.toString()
            data.allowEmpty = false
            updateAnswer(data)
        }

        binding.answerEditText.post {
            if (data.requestFocusOnce) {
                data.requestFocusOnce = false
                binding.answerEditText.requestFocus()
                AmityAndroidUtil.showKeyboard(binding.answerEditText)
            }
        }

        binding.removeImageView.setOnClickListener {
            removeCallback.invoke(data)
        }
    }

    private fun updateAnswer(data: AmityPollDraftAnswerAdapter.DraftAnswer) {
        binding.pollLinearLayout.background = ContextCompat.getDrawable(
            context,
            when ((data.data.isEmpty() && !data.allowEmpty) || data.data.length > MAX_ANSWER_LENGTH) {
                true -> R.drawable.amity_bg_poll_add_draft_answer_error
                false -> R.drawable.amity_bg_poll_draft_answer
            }
        )

        binding.errorTextView.text = when {
            data.data.isEmpty() && !data.allowEmpty -> context.getString(R.string.amity_poll_answer_error_empty)
            binding.answerEditText.length() > MAX_ANSWER_LENGTH -> context.getString(R.string.amity_poll_answer_error_exceed_limit)
            else -> ""
        }

        binding.errorTextView.isVisible = (data.data.isEmpty() && !data.allowEmpty)
                || binding.answerEditText.length() > MAX_ANSWER_LENGTH
    }
}