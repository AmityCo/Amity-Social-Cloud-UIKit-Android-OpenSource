package com.amity.snipet.verifier.chat.livechat

import androidx.compose.runtime.Composable
import com.amity.socialcloud.uikit.chat.compose.live.component.AmityLiveChatMessageList
import com.amity.socialcloud.uikit.chat.compose.live.AmityLiveChatPageViewModel


class AmityLiveChatMessageList {
    /* begin_sample_code
       gist_id: 5c3b9401734f0666a1ce03e91c141e6a
       filename: AmityLiveChatMessageList.kt
       asc_page: https://docs.amity.co/amity-uikit/uikit-v4-beta/chat/
       description: initialize live chat message list
       */
    @Composable
    fun composeLiveChatMessageList(
        viewModel: AmityLiveChatPageViewModel
    ) {
        AmityLiveChatMessageList(
            viewModel = viewModel
        )
    }
    /* end_sample_code */
}