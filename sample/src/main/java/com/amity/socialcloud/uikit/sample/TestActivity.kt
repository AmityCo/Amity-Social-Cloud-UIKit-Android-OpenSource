package com.amity.socialcloud.uikit.sample

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.amity.socialcloud.uikit.community.home.fragments.AmityCommunityHomePageFragment
import com.amity.socialcloud.uikit.sample.databinding.AmityActivityTestBinding

class TestActivity : AppCompatActivity() {

    private val binding: AmityActivityTestBinding by lazy {
        AmityActivityTestBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        setupToolbar()
        initializeFragment()
    }

    private fun setupToolbar() {
        binding.mToolbar.setLeftDrawable(
            ContextCompat.getDrawable(
                this,
                com.amity.socialcloud.uikit.community.R.drawable.amity_ic_arrow_back
            )
        )
        binding.mToolbar.setLeftString("Test Activity")
        supportActionBar?.displayOptions = ActionBar.DISPLAY_SHOW_CUSTOM
        setSupportActionBar(binding.mToolbar)
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