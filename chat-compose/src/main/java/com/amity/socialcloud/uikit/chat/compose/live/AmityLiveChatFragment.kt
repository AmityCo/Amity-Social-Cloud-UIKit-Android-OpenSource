package com.amity.socialcloud.uikit.chat.compose.live

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.ui.Modifier
import androidx.media3.common.util.UnstableApi
import com.amity.socialcloud.uikit.chat.compose.databinding.AmityFragmentLiveChatBinding
import com.amity.socialcloud.uikit.common.base.AmityBaseFragment

@UnstableApi
class AmityLiveChatFragment private constructor(
    private val channelId: String,
) : AmityBaseFragment() {

    lateinit var binding: AmityFragmentLiveChatBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = AmityFragmentLiveChatBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.composeView.setContent {
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