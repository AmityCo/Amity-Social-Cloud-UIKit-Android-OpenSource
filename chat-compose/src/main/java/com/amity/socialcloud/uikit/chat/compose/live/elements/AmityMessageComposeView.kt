package com.amity.socialcloud.uikit.chat.compose.live.elements

import android.content.Context
import android.graphics.Rect
import android.util.TypedValue
import androidx.annotation.ColorInt
import androidx.core.content.ContextCompat
import androidx.core.widget.doAfterTextChanged
import com.amity.socialcloud.sdk.helper.core.mention.AmityMentionMetadata
import com.amity.socialcloud.sdk.helper.core.mention.AmityMentionee
import com.amity.socialcloud.sdk.model.core.user.AmityUser
import com.amity.socialcloud.uikit.chat.compose.R
import com.amity.socialcloud.uikit.common.common.views.AmityColorPaletteUtil
import com.amity.socialcloud.uikit.common.common.views.AmityColorShade
import com.amity.socialcloud.uikit.common.localization.DefaultAmityCommonStringProvider
import com.linkedin.android.spyglass.mentions.MentionSpan
import com.linkedin.android.spyglass.mentions.MentionSpanConfig
import com.linkedin.android.spyglass.mentions.Mentionable
import com.linkedin.android.spyglass.ui.MentionsEditText


class AmityMessageComposeView(context: Context) : MentionsEditText(context) {

    init {
        parseStyle()
    }

    private var maxChars: Int = CHARACTERS_LIMIT
    var onMentionLimitReached: (() -> Unit)? = null
    var isApplyingMentionMetadata: Boolean = false

    override fun insertMention(mention: Mentionable) {
        if (getMentions().size >= MENTIONS_LIMIT) {
            onMentionLimitReached?.invoke()
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
            // Character limit is enforced by the compose bar's error dialog
        }
    }

    private fun setMentionConfig() {
        setMentionSpanConfig(
            MentionSpanConfig.Builder()
                .setMentionTextColor(
                    ContextCompat.getColor(
                        context,
                        R.color.amityColorHighlight
                    )
                ).build()
        )
    }

    fun setTextCompose(text: String) {
        setText(text)
    }

    fun setMentionMetadata(
        mentionMetadata: List<AmityMentionMetadata>,
        mentionees: List<AmityMentionee>
    ) {
        // Process mentions in reverse order (by index) to avoid index shifting
        val sortedMetadata = mentionMetadata.sortedByDescending { mention ->
            when (mention) {
                is AmityMentionMetadata.USER -> mention.getIndex()
                is AmityMentionMetadata.CHANNEL -> mention.getIndex()
                else -> 0
            }
        }
        sortedMetadata.forEach { mentionItem ->
            val mentionStart: Int
            val mentionEnd: Int
            if (mentionItem is AmityMentionMetadata.USER) {
                mentionStart = mentionItem.getIndex()
                mentionEnd = mentionItem.getIndex().plus(mentionItem.getLength()).inc()

                if (mentionEnd > text.length || mentionStart < 0) return@forEach
                text.delete(mentionStart, mentionEnd)
                setSelection(mentionStart)
                getMentionedUser(mentionItem.getUserId(), mentionees)?.let {
                    insertMentionWithoutToken(AmityUserMention(it))
                }
            }

            else if (mentionItem is AmityMentionMetadata.CHANNEL) {
                mentionStart = mentionItem.getIndex()
                mentionEnd = mentionItem.getIndex().plus(mentionItem.getLength()).inc()

                if (mentionEnd > text.length || mentionStart < 0) return@forEach
                text.delete(mentionStart, mentionEnd)
                setSelection(mentionStart)
                insertMentionWithoutToken(AmityChannelMention())
            }
        }
    }

    fun getMentions(): List<AmityMentionMetadata> {
        val mentionSpan = mutableListOf<MentionSpan>()
        val mentions = mutableListOf<AmityMentionMetadata>()
        if(mentionsText.startsWith(" ")) {
            mentionsText.trim()
        }
        for (index in 0 until mentionsText.length) {
            val mentionSpanItem = mentionsText.getMentionSpanStartingAt(index)
            if (mentionSpanItem != null && !mentionSpan.contains(mentionSpanItem)) {
                mentionSpan.add(mentionSpanItem)
                (mentionSpanItem.mention as? AmityUserMention?)?.let { user ->
                    mentions.add(
                        AmityMentionMetadata.USER(
                            user.getUserId(),
                            index, user.getDisplayName().length
                        )
                    )
                }
                (mentionSpanItem.mention as? AmityChannelMention?)?.let { channel ->
                    mentions.add(
                        AmityMentionMetadata.CHANNEL(
                            index, DefaultAmityCommonStringProvider.getInstance().getString("amity_common_button_all").length
                        )
                    )
                }
            }
        }
        return mentions
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
        setTextSize(TypedValue.COMPLEX_UNIT_SP, 15f)
    }

    fun setMaxChars(maxChars: Int) {
        this.maxChars = maxChars
    }

    fun setStyle(@ColorInt textColor: Int, @ColorInt hintColor: Int) {
        setTextColor(textColor)
        setHintTextColor(hintColor)
    }


}

const val MENTIONS_LIMIT: Int = 30
const val CHARACTERS_LIMIT: Int = 10000