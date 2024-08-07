package com.amity.snipet.verifier.chat.livechat

import androidx.compose.runtime.Composable
import com.amity.socialcloud.uikit.chat.compose.live.component.AmityLiveChatHeader
import com.amity.socialcloud.uikit.chat.compose.live.AmityLiveChatPageViewModel


class AmityLiveChatHeader {
    /* begin_sample_code
       gist_id: 38c1960c262a167d9ae57eb1be59e007
       filename: AmityLiveChatHeader.kt
       asc_page: https://docs.amity.co/amity-uikit/uikit-v4-beta/chat/
       description: initialize live chat header
       */
    @Composable
    fun composeLiveChatHeader(
        viewModel: AmityLiveChatPageViewModel
    ) {
        AmityLiveChatHeader(
            viewModel = viewModel
        )
    }
    /* end_sample_code */
}