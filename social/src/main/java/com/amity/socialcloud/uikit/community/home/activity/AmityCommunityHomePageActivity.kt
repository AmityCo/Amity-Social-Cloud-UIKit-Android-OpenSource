package com.amity.socialcloud.uikit.community.home.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import com.amity.socialcloud.uikit.community.R
import com.amity.socialcloud.uikit.community.databinding.AmityActivityCommunityHomeBinding
import com.amity.socialcloud.uikit.community.home.fragments.AmityCommunityHomePageFragment

class AmityCommunityHomePageActivity : AppCompatActivity() {

    private val binding: AmityActivityCommunityHomeBinding by lazy {
        AmityActivityCommunityHomeBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        initToolbar()

        val useNewsFeedV4 = intent?.getBooleanExtra(EXTRA_USE_NEWS_FEED_V4, false) ?: false
        loadFragment(useNewsFeedV4)
    }

    private fun initToolbar() {
        supportActionBar?.displayOptions = ActionBar.DISPLAY_SHOW_CUSTOM
        setSupportActionBar(binding.communityHomeToolbar)
        binding.communityHomeToolbar.setLeftString(getString(R.string.amity_community))
    }

    private fun loadFragment(useNewsFeedV4: Boolean) {
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        val fragment = AmityCommunityHomePageFragment.newInstance(useNewsFeedV4).build()
        fragmentTransaction.replace(R.id.fragmentContainer, fragment)
        fragmentTransaction.commit()
    }


    companion object {
        private const val EXTRA_USE_NEWS_FEED_V4 = "EXTRA_USE_NEWS_FEED_V4"

        fun newIntent(context: Context, useNewsFeedV4: Boolean = false) =
            Intent(context, AmityCommunityHomePageActivity::class.java).apply {
                putExtra(EXTRA_USE_NEWS_FEED_V4, useNewsFeedV4)
            }
    }
}