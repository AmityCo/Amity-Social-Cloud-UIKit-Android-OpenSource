package com.amity.socialcloud.uikit.sample.liveChat

import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.media3.common.util.UnstableApi
import com.amity.socialcloud.sdk.model.chat.channel.AmityChannel
import com.amity.socialcloud.uikit.sample.R
import com.amity.socialcloud.uikit.sample.databinding.AmityActivityLiveChatListBinding

class AmityLiveChatListActivity : AppCompatActivity() {
    
    lateinit var binding: AmityActivityLiveChatListBinding
    @UnstableApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val channelId = intent.getStringExtra("CHANNEL_ID") ?: ""
        binding = AmityActivityLiveChatListBinding.inflate(this.layoutInflater, null, false)
        setContentView(binding.root)
        val nightModeFlags = this.resources.configuration.uiMode and
            Configuration.UI_MODE_NIGHT_MASK
        binding.root.background = when (nightModeFlags) {
            Configuration.UI_MODE_NIGHT_YES -> {
                ContextCompat.getDrawable(this, R.drawable.amity_live_chat_bottom_sheet_dark)
            }
            else -> {
                ContextCompat.getDrawable(this, R.drawable.amity_live_chat_bottom_sheet_light)
            }
        }
        binding.listContainer.setContent {
            AmityLiveChatList(onChannelClick = ::onChannelClick)
        }
    }

    private fun onChannelClick(channel: AmityChannel) {
        val intent = Intent(this, AmityLiveChatActivity::class.java).apply {
            putExtra("CHANNEL_ID", channel.getChannelId())
            putExtra("SUB_CHANNEL_ID", channel.getChannelId())
        }
        startActivity(intent)
    }
}