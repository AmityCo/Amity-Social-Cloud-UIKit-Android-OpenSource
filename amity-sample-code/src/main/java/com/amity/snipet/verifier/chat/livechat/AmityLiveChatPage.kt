package com.amity.snipet.verifier.chat.livechat

import androidx.compose.runtime.Composable
import com.amity.socialcloud.uikit.chat.compose.live.AmityLiveChatPage

class AmityLiveChat {
    /* begin_sample_code
       gist_id: eeffb4ac4c8d216bde9eda314c71f7f3
       filename: AmityLiveChat.kt
       asc_page: https://docs.amity.co/amity-uikit/uikit-v4-beta/chat/
       description: initialize live chat page
       */
    @Composable
    fun composeLiveChatPage(
        channelId: String
    ) {
        AmityLiveChatPage(
            channelId = channelId
        )
    }
    /* end_sample_code */
}