package com.amity.socialcloud.uikit.chat.compose.live

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.media3.common.util.UnstableApi
import com.amity.socialcloud.uikit.common.base.AmityBaseFragment

@UnstableApi
class AmityLiveChatFragment private constructor(
    private val channelId: String,
) : AmityBaseFragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = ComposeView(requireContext()).apply {
        setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
        setContent {
            AmityLiveChatPage(
                channelId = channelId,
                modifier = Modifier.fillMaxWidth(),
            )
        }
    }

    companion object {
        fun newInstance(
            channelId: String
        ): Builder {
            return Builder(
                channelId = channelId,
            )
        }
    }

    class Builder internal constructor(
        private val channelId: String,
    ) {
        fun build(): AmityLiveChatFragment {
            return AmityLiveChatFragment(
                channelId = channelId,
            )
        }
    }
}