package com.amity.socialcloud.uikit.community.compose.ui.components.mentions

import android.content.Context
import android.graphics.Rect
import android.util.TypedValue
import androidx.annotation.ColorInt
import androidx.core.content.ContextCompat
import androidx.core.widget.doAfterTextChanged
import com.amity.socialcloud.sdk.helper.core.mention.AmityMentionMetadata
import com.amity.socialcloud.sdk.helper.core.mention.AmityMentionee
import com.amity.socialcloud.sdk.model.core.user.AmityUser
import com.amity.socialcloud.uikit.common.utils.AmityAlertDialogUtil
import com.amity.socialcloud.uikit.community.compose.R
import com.linkedin.android.spyglass.mentions.MentionSpan
import com.linkedin.android.spyglass.mentions.MentionSpanConfig
import com.linkedin.android.spyglass.mentions.Mentionable
import com.linkedin.android.spyglass.ui.MentionsEditText

class AmityCommentComposeView(context: Context) : MentionsEditText(context) {

    init {
        parseStyle()
    }

    override fun insertMention(mention: Mentionable) {
        if (getUserMentions().size >= MENTIONS_LIMIT) {
            showErrorDialog(
                "Unable to mention user",
                "You have reached maximum 30 mentioned users in a post."
            )
        } else {
            super.insertMention(mention)
            append(" ")
        }
    }

    override fun onFocusChanged(focused: Boolean, direction: Int, previouslyFocusedRect: Rect?) {
        super.onFocusChanged(focused, direction, previouslyFocusedRect)
        tokenizer = MentionViewWordTokenizer().create()
    }

    private fun parseStyle() {
        tokenizer = null

        applyStyle()
        setMentionConfig()

        this.doAfterTextChanged { text ->
            if (!text.isNullOrEmpty() && text.length > CHARACTERS_LIMIT) {
                getText().delete(CHARACTERS_LIMIT, getText().length)
                showErrorDialog(
                    "Unable to type text into comment",
                    "You have reached 50,000 characters limit."
                )
            }
        }
    }

    private fun showErrorDialog(title: String, message: String) {
        AmityAlertDialogUtil.showDialog(
            context, title, message,
            "DONE",
            null
        ) { dialog, which ->
            AmityAlertDialogUtil.checkConfirmDialog(
                isPositive = which,
                confirmed = dialog::cancel
            )
        }
    }

    private fun setMentionConfig() {
        setMentionSpanConfig(
            MentionSpanConfig.Builder()
                .setMentionTextColor(
                    ContextCompat.getColor(
                        context,
                        R.color.amityColorPrimary
                    )
                ).build()
        )
    }

    fun setTextCompose(text: String) {
        setText(text)
    }

    fun setMentionMetadata(
        mentionMetadata: List<AmityMentionMetadata.USER>,
        mentionees: List<AmityMentionee>
    ) {
        mentionMetadata.forEach { mentionItem ->
            val mentionStart = mentionItem.getIndex()
            val mentionEnd = mentionItem.getIndex().plus(mentionItem.getLength()).inc()

            text.delete(mentionStart, mentionEnd)
            setSelection(mentionStart)
            getMentionedUser(mentionItem.getUserId(), mentionees)?.let {
                insertMentionWithoutToken(AmityUserMention(it))
            }
        }
    }

    fun getUserMentions(): List<AmityMentionMetadata.USER> {
        val mentionSpan = mutableListOf<MentionSpan>()
        val userMentions = mutableListOf<AmityMentionMetadata.USER>()
        for (index in 0 until mentionsText.length) {
            val mentionSpanItem = mentionsText.getMentionSpanStartingAt(index)
            if (mentionSpanItem != null && !mentionSpan.contains(mentionSpanItem)) {
                mentionSpan.add(mentionSpanItem)
                (mentionSpanItem.mention as? AmityUserMention?)?.let { user ->
                    userMentions.add(
                        AmityMentionMetadata.USER(
                            user.getUserId(),
                            index, user.getDisplayName().length
                        )
                    )
                }
            }
        }
        return userMentions
    }

    private fun getMentionedUser(
        userId: String,
        mentionees: List<AmityMentionee>
    ): AmityUser? {
        return mentionees.map { (it as? AmityMentionee.USER)?.getUser() }
            .find { it?.getUserId() == userId }
    }

    private fun applyStyle() {
        setBackgroundColor(ContextCompat.getColor(context, android.R.color.transparent))
        setHint("Say something nice...")
        setTextSize(TypedValue.COMPLEX_UNIT_SP, 15f)
    }

    fun setStyle(@ColorInt textColor: Int, @ColorInt hintColor: Int) {
        setTextColor(textColor)
        setHintTextColor(hintColor)
    }
}

const val MENTIONS_LIMIT: Int = 30
const val CHARACTERS_LIMIT: Int = 50000
