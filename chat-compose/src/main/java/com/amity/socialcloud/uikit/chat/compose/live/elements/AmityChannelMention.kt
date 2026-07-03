package com.amity.socialcloud.uikit.chat.compose.live.elements

import com.amity.socialcloud.uikit.chat.compose.localization.DefaultAmityChatStringProvider
import com.linkedin.android.spyglass.mentions.Mentionable
import kotlinx.parcelize.Parcelize
import java.util.UUID

@Parcelize
class AmityChannelMention : Mentionable {

    val CHAR_MENTION = "@"

    override fun getSuggestibleId(): Int {
        return DefaultAmityChatStringProvider.getInstance().getString("chat.tab.all").hashCode()
    }


    override fun getSuggestiblePrimaryText(): String {
        return DefaultAmityChatStringProvider.getInstance().getString("chat.tab.all") + UUID.randomUUID()
    }

    override fun getTextForDisplayMode(mode: Mentionable.MentionDisplayMode): String {
        return "$CHAR_MENTION"+ DefaultAmityChatStringProvider.getInstance().getString("chat.tab.all")
    }

    override fun getDeleteStyle(): Mentionable.MentionDeleteStyle {
        return Mentionable.MentionDeleteStyle.PARTIAL_NAME_DELETE
    }


}