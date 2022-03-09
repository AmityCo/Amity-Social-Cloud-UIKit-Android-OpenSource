package com.amity.socialcloud.uikit.community.ui.view

import android.os.Bundle
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.amity.socialcloud.uikit.common.components.AmityToolBarClickListener
import com.amity.socialcloud.uikit.community.R
import com.amity.socialcloud.uikit.community.databinding.AmityActivityCreateCommunityBinding

class AmityCommunityCreatorActivity : AppCompatActivity(), AmityToolBarClickListener {

    private val binding : AmityActivityCreateCommunityBinding by lazy {
        AmityActivityCreateCommunityBinding.inflate(layoutInflater)
    }

    private lateinit var mFragment: AmityCommunityCreatorFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        setUpToolBar()
        loadFragment()
    }

    private fun setUpToolBar() {
        binding.communityToolbar.setLeftDrawable(
            ContextCompat.getDrawable(this, R.drawable.amity_ic_cross)
        )
        binding.communityToolbar.setLeftString(getString(R.string.amity_create_community))

        binding.communityToolbar.setClickListener(this)

        supportActionBar?.displayOptions = ActionBar.DISPLAY_SHOW_CUSTOM
        setSupportActionBar(binding.communityToolbar)
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