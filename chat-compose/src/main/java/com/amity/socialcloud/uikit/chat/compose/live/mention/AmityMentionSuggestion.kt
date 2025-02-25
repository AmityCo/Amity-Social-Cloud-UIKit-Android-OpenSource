package com.amity.socialcloud.uikit.chat.compose.live.mention

import com.amity.socialcloud.sdk.model.core.user.AmityUser

sealed class AmityMentionSuggestion {

    class USER(
        val user: AmityUser
    ) : AmityMentionSuggestion()

    class CHANNEL(
        val channelId: String
    ) : AmityMentionSuggestion()


}