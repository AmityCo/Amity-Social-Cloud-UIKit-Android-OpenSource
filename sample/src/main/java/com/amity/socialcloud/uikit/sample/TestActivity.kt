package com.amity.socialcloud.uikit.sample

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.amity.socialcloud.uikit.community.home.fragments.AmityCommunityHomePageFragment
import kotlinx.android.synthetic.main.amity_activity_test.*

class TestActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.amity_activity_test)
        setupToolbar()
        initializeFragment()
    }

    private fun setupToolbar() {
        mToolbar.setLeftDrawable(
            ContextCompat.getDrawable(
                this,
                com.amity.socialcloud.uikit.community.R.drawable.amity_ic_arrow_back
            )
        )
        mToolbar.setLeftString("Test Activity")
        supportActionBar?.displayOptions = ActionBar.DISPLAY_SHOW_CUSTOM
        setSupportActionBar(mToolbar)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val drawable = getDrawable(R.drawable.amity_ic_add)
        menu?.add(
            Menu.NONE,
            1,
            Menu.NONE,
            getString(R.string.amity_add)
        )
            ?.setIcon(drawable)
            ?.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS)
        return super.onCreateOptionsMenu(menu)
    }

    private fun initializeFragment() {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.mFrameLayout, AmityCommunityHomePageFragment.newInstance().build())
        transaction.commit()
    }
}