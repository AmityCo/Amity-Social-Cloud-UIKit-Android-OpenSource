package com.amity.socialcloud.uikit.chat.home.groupchat

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.amity.socialcloud.uikit.chat.R


class AmityCreateGroupChatFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.amity_fragment_create_group_chat, container, false)
    }

}