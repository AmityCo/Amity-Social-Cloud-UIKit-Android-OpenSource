package com.amity.socialcloud.uikit.community.compose.community.setting.story

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.amity.socialcloud.sdk.model.social.community.AmityCommunity
import com.amity.socialcloud.uikit.common.utils.getCommunity

class AmityCommunityStorySettingPageActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val community = intent.getCommunity(EXTRA_PARAM_COMMUNITY) ?: return

        setContent {
            AmityCommunityStorySettingPage(
                community = community,
            )
        }
    }

    companion object {
        private const val EXTRA_PARAM_COMMUNITY = "community"

        fun newIntent(
            context: Context,
            community: AmityCommunity,
        ): Intent {
            return Intent(
                context,
                AmityCommunityStorySettingPageActivity::class.java
            ).apply {
                putExtra(EXTRA_PARAM_COMMUNITY, community)
            }
        }
    }
}