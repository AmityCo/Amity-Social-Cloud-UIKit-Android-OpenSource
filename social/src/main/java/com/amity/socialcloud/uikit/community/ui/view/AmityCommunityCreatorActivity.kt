package com.amity.socialcloud.uikit.community.ui.view

import android.os.Bundle
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.amity.socialcloud.uikit.common.components.AmityToolBarClickListener
import com.amity.socialcloud.uikit.community.R
import kotlinx.android.synthetic.main.amity_activity_create_community.*

class AmityCommunityCreatorActivity : AppCompatActivity(), AmityToolBarClickListener {

    private lateinit var mFragment: AmityCommunityCreatorFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.amity_activity_create_community)
        setUpToolBar()
        loadFragment()
    }

    private fun setUpToolBar() {
        communityToolbar.setLeftDrawable(
            ContextCompat.getDrawable(this, R.drawable.amity_ic_cross)
        )
        communityToolbar.setLeftString(getString(R.string.amity_create_community))

        communityToolbar.setClickListener(this)

        supportActionBar?.displayOptions = ActionBar.DISPLAY_SHOW_CUSTOM
        setSupportActionBar(communityToolbar)
    }

    private fun loadFragment() {
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        mFragment = AmityCommunityCreatorFragment.newInstance().build()
        fragmentTransaction.replace(R.id.fragmentContainer, mFragment)
        fragmentTransaction.commit()
    }

    override fun leftIconClick() {
        mFragment.onLeftIconClick()
    }

    override fun rightIconClick() {
    }
}