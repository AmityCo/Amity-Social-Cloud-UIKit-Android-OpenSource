package com.amity.socialcloud.uikit.sample

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.amity.socialcloud.uikit.chat.home.callback.AmityRecentChatFragmentDelegate
import com.amity.socialcloud.uikit.chat.home.callback.AmityRecentChatItemClickListener
import com.amity.socialcloud.uikit.chat.home.fragment.AmityChatHomePageFragment
import com.amity.socialcloud.uikit.chat.recent.fragment.AmityRecentChatFragment

class AmityRecentMessageListActivity : AppCompatActivity(), AmityRecentChatItemClickListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.amity_activity_recent_message_list)

        val chatHomeFragment = AmityChatHomePageFragment.newInstance()
                .recentChatFragmentDelegate(object: AmityRecentChatFragmentDelegate {
                    override fun recentChatFragment(): AmityRecentChatFragment {
                        return AmityRecentChatFragment.newInstance()
                                .recentChatItemClickListener(this@AmityRecentMessageListActivity)
                                .build(this@AmityRecentMessageListActivity)
                    }
                })
            .build(this)
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.fragmentContainer, chatHomeFragment)
        transaction.addToBackStack(null)
        transaction.commit()
    }

    override fun onRecentChatItemClick(channelId: String) {
        val intent = Intent(this, AmityMessageListWithCustomUi::class.java).apply {
            putExtra("CHANNEL_ID", channelId)
        }
        startActivity(intent)
    }
}