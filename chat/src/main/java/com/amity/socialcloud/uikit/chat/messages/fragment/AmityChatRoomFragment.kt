package com.amity.socialcloud.uikit.chat.messages.fragment

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.amity.socialcloud.uikit.chat.R
import com.amity.socialcloud.uikit.chat.messages.adapter.AmityMessageListAdapter
import com.amity.socialcloud.uikit.chat.messages.composebar.AmityChatRoomComposeBar
import com.amity.socialcloud.uikit.chat.messages.viewModel.AmityChatRoomEssentialViewModel

class AmityChatRoomFragment : Fragment(R.layout.amity_fragment_chat_room) {

    private lateinit var essentialViewModel: AmityChatRoomEssentialViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        essentialViewModel = ViewModelProvider(requireActivity()).get(AmityChatRoomEssentialViewModel::class.java)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpChatRoom()
    }

    private fun setUpChatRoom() {
        childFragmentManager.beginTransaction()
            .replace(R.id.chat_room_container, getFragment())
            .commit()
    }

    private fun getFragment(): Fragment {
        return when (essentialViewModel.composeBar) {
            is AmityChatRoomComposeBar.DEFAULT -> {
                val builder =
                    AmityChatRoomWithDefaultComposeBarFragment.newInstance(essentialViewModel.channelId)
                        .enableChatToolbar(essentialViewModel.enableChatToolbar)
                        .enableConnectionBar(essentialViewModel.enableConnectionBar)

                essentialViewModel.customViewHolder?.let {
                    builder.customViewHolder(it)
                }
                builder.build(requireActivity() as AppCompatActivity)
            }
            is AmityChatRoomComposeBar.TEXT -> {
                val builder =
                    AmityChatRoomWithTextComposeBarFragment.newInstance(essentialViewModel.channelId)
                        .enableChatToolbar(essentialViewModel.enableChatToolbar)
                        .enableConnectionBar(essentialViewModel.enableConnectionBar)

                essentialViewModel.customViewHolder?.let {
                    builder.customViewHolder(it)
                }
                builder.build(requireActivity() as AppCompatActivity)
            }
        }
    }

    class Builder internal constructor(private val channelId: String) {

        private var enableChatToolbar = true
        private var enableConnectionBar = true
        private var composeBar: AmityChatRoomComposeBar = AmityChatRoomComposeBar.DEFAULT
        private var customViewHolder: AmityMessageListAdapter.CustomViewHolderListener? = null

        fun enableChatToolbar(enable: Boolean): Builder {
            this.enableChatToolbar = enable
            return this
        }

        fun enableConnectionBar(enable: Boolean): Builder {
            this.enableConnectionBar = enable
            return this
        }

        fun composeBar(composeBar: AmityChatRoomComposeBar): Builder {
            this.composeBar = composeBar
            return this
        }

        fun customViewHolder(customViewHolder: AmityMessageListAdapter.CustomViewHolderListener): Builder {
            this.customViewHolder = customViewHolder
            return this
        }

        fun build(activity: AppCompatActivity): AmityChatRoomFragment {
            val essentialViewModel = ViewModelProvider(activity).get(AmityChatRoomEssentialViewModel::class.java)
            essentialViewModel.channelId = channelId
            essentialViewModel.enableChatToolbar = enableChatToolbar
            essentialViewModel.enableConnectionBar = enableConnectionBar
            essentialViewModel.composeBar = composeBar
            essentialViewModel.customViewHolder = customViewHolder
            return AmityChatRoomFragment()
        }
    }

    companion object {

        fun newInstance(channelId: String): Builder {
            return Builder(channelId)
        }
    }

}