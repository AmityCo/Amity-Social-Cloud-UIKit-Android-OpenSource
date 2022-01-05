package com.amity.socialcloud.uikit.community.home.activity

import android.os.Bundle
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import com.amity.socialcloud.uikit.community.R
import com.amity.socialcloud.uikit.community.home.fragments.AmityCommunityHomePageFragment
import kotlinx.android.synthetic.main.amity_activity_community_home.*

class AmityCommunityHomePageActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.amity_activity_community_home)
        initToolbar()
        loadFragment()
    }

    private fun initToolbar() {
        supportActionBar?.displayOptions = ActionBar.DISPLAY_SHOW_CUSTOM
        setSupportActionBar(communityHomeToolbar)
        communityHomeToolbar.setLeftString(getString(R.string.amity_community))
    }

    private fun loadFragment() {
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        val fragment = AmityCommunityHomePageFragment.newInstance().build()
        fragmentTransaction.replace(R.id.fragmentContainer, fragment)
        fragmentTransaction.commit()
    }
}