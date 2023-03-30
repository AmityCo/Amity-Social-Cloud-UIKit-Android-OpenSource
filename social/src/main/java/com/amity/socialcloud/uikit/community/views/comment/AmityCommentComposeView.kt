package com.amity.socialcloud.uikit.community.views.comment

import android.content.Context
import android.graphics.Rect
import android.util.AttributeSet
import androidx.core.content.ContextCompat
import androidx.core.widget.doAfterTextChanged
import com.amity.socialcloud.sdk.helper.core.mention.AmityMentionMetadata
import com.amity.socialcloud.uikit.common.utils.AmityAlertDialogUtil
import com.amity.socialcloud.uikit.community.R
import com.amity.socialcloud.uikit.community.newsfeed.model.AmityUserMention
import com.amity.socialcloud.uikit.community.views.mention.MentionViewWordTokenizer
import com.linkedin.android.spyglass.mentions.MentionSpan
import com.linkedin.android.spyglass.mentions.MentionSpanConfig
import com.linkedin.android.spyglass.mentions.Mentionable
import com.linkedin.android.spyglass.ui.MentionsEditText

private const val MENTIONS_LIMIT: Int = 30
private const val CHARACTERS_LIMIT: Int = 50000

class AmityCommentComposeView : MentionsEditText {

    lateinit var style: AmityCommentComposeViewStyle

    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(
        context,
        attrs,
        defStyle
    ) {
        parseStyle(attrs)
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        parseStyle(attrs)
    }

    override fun insertMention(mention: Mentionable) {
        if (getUserMentions().size >= MENTIONS_LIMIT) {
            showErrorDialog(
                context.resources.getString(R.string.amity_mention_error_title),
                context.resources.getString(R.string.amity_mention_error_msg)
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

    private fun parseStyle(attrs: AttributeSet) {
        tokenizer = null
        style = AmityCommentComposeViewStyle(context, attrs)
        applyStyle()
        setMentionConfig()

        this.doAfterTextChanged { text ->
            if (!text.isNullOrEmpty() && text.length > CHARACTERS_LIMIT) {
                getText().delete(CHARACTERS_LIMIT, getText().length)
                showErrorDialog(
                    context.resources.getString(R.string.amity_comment_characters_limit_error_title),
                    context.resources.getString(R.string.amity_characters_limit_error_msg)
                )
            }
        }
    }

    private fun showErrorDialog(title: String, message: String) {
        AmityAlertDialogUtil.showDialog(
            context, title, message,
            context.resources.getString(R.string.amity_done),
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
                    ContextCompat.getColor(context, R.color.upstraColorPrimary)
                ).build()
        )
    }

    fun setViewStyle(style: AmityCommentComposeViewStyle) {
        this.style = style
        applyStyle()
    }

    fun getTextCompose(): String {
        return text.toString()
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

    private fun applyStyle() {
        setBackgroundColor(style.backgroundColor)
        setTextColor(style.textColor)
        setHintTextColor(style.hintTextColor)
        setDefaultPostHint()
    }

    fun setDefaultPostHint() {
        setHint(style.hint)
    }

}