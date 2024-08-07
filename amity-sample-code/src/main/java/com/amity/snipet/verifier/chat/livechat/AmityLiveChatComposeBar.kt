package com.amity.snipet.verifier.chat.livechat

import androidx.compose.runtime.Composable
import com.amity.socialcloud.uikit.chat.compose.live.composer.AmityLiveChatMessageComposeBar
import com.amity.socialcloud.uikit.chat.compose.live.AmityLiveChatPageViewModel


class AmityLiveChatComposeBar {
    /* begin_sample_code
       gist_id: ad90cc62e19eeea14892fd8fb2d75da4
       filename: AmityLiveChatComposeBar.kt
       asc_page: https://docs.amity.co/amity-uikit/uikit-v4-beta/chat/
       description: initialize live chat message compose bar
       */
    @Composable
    fun composeLiveChatMessageComposeBar(
        viewModel: AmityLiveChatPageViewModel
    ) {
        AmityLiveChatMessageComposeBar(
            viewModel = viewModel
        )
    }
    /* end_sample_code */
}