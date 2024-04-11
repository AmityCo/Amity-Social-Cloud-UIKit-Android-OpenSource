package com.amity.socialcloud.uikit.chat.compose.live.elements

import com.linkedin.android.spyglass.mentions.Mentionable
import kotlinx.parcelize.Parcelize
import java.util.UUID

@Parcelize
class AmityChannelMention : Mentionable {

    val CHAR_MENTION = "@"

    override fun getSuggestibleId(): Int {
        return "All".hashCode()
    }


    override fun getSuggestiblePrimaryText(): String {
        return "All" + UUID.randomUUID()
    }

    override fun getTextForDisplayMode(mode: Mentionable.MentionDisplayMode): String {
        return "$CHAR_MENTION"+"All"
    }

    override fun getDeleteStyle(): Mentionable.MentionDeleteStyle {
        return Mentionable.MentionDeleteStyle.PARTIAL_NAME_DELETE
    }


}