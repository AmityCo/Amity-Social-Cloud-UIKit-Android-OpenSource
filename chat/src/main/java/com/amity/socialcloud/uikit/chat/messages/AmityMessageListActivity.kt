package com.amity.socialcloud.uikit.chat.messages

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updatePadding
import com.amity.socialcloud.uikit.chat.R
import com.amity.socialcloud.uikit.chat.databinding.AmityActivityChatBinding
import com.amity.socialcloud.uikit.chat.messages.composebar.AmityChatRoomComposeBar
import com.amity.socialcloud.uikit.chat.messages.fragment.AmityChatRoomFragment
import com.amity.socialcloud.uikit.common.utils.AmityThemeUtil

class AmityMessageListActivity : AppCompatActivity() {
    private lateinit var channelId: String
    private lateinit var binding: AmityActivityChatBinding

    companion object {
        private const val INTENT_CHANNEL_ID = "channelID"
        private const val INTENT_IS_TEXT_ONLY = "isTextOnly"


        fun newIntent(context: Context, channelId: String, isTextOnly: Boolean = false): Intent {
            return Intent(context, AmityMessageListActivity::class.java).apply {
                putExtra(INTENT_CHANNEL_ID, channelId)
                putExtra(INTENT_IS_TEXT_ONLY, isTextOnly)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        AmityThemeUtil.setCurrentTheme(this)
        super.onCreate(savedInstanceState)
        binding = AmityActivityChatBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { view, windowInsets ->
            val imeInsets = windowInsets.getInsets(WindowInsetsCompat.Type.ime())
            val systemBarInsets = windowInsets.getInsets(WindowInsetsCompat.Type.systemBars())
            view.updatePadding(
                left = systemBarInsets.left,
                top = systemBarInsets.top,
                right = systemBarInsets.right,
                bottom = maxOf(imeInsets.bottom, systemBarInsets.bottom)
            )
            WindowInsetsCompat.CONSUMED
        }
        channelId = intent.getStringExtra(INTENT_CHANNEL_ID) ?: ""
        initializeFragment()
    }

    private fun initializeFragment() {
        val isTextOnly = intent.getBooleanExtra(INTENT_IS_TEXT_ONLY, false)
        val composebar = if (isTextOnly) AmityChatRoomComposeBar.TEXT else AmityChatRoomComposeBar.DEFAULT
        val messageListFragment = AmityChatRoomFragment.newInstance(channelId)
            .enableChatToolbar(true)
            .enableConnectionBar(true)
            .composeBar(composebar)
            .build(this)
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.messageListContainer, messageListFragment)
        transaction.addToBackStack(null)
        transaction.commit()
    }
}