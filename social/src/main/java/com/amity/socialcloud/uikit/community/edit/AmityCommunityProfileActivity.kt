package com.amity.socialcloud.uikit.community.edit

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import com.amity.socialcloud.uikit.common.components.AmityToolBarClickListener
import com.amity.socialcloud.uikit.community.R
import com.amity.socialcloud.uikit.community.ui.viewModel.AmityCreateCommunityViewModel
import kotlinx.android.synthetic.main.amity_activity_create_community.*
import kotlinx.android.synthetic.main.amity_activity_edit_community.*

class AmityCommunityProfileActivity : AppCompatActivity(), AmityToolBarClickListener {

    private lateinit var mFragment: AmityCommunityEditorFragment
    private val mViewModel: AmityCreateCommunityViewModel by viewModels()
    private var communityId = ""

    companion object {
        private const val COMMUNITY_ID = "COMMUNITY_ID"

        fun newIntent(context: Context, communityId: String = "") =
            Intent(context, AmityCommunityProfileActivity::class.java).apply {
                putExtra(COMMUNITY_ID, communityId)
            }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.amity_activity_edit_community)

        communityId = intent?.getStringExtra(COMMUNITY_ID) ?: ""
        setUpToolbar()
        if (savedInstanceState == null) {
            loadFragment()
        }

    }

    private fun setUpToolbar() {
        editCommunityToolbar.setLeftDrawable(
            ContextCompat.getDrawable(this, R.drawable.amity_ic_arrow_back)
        )
        editCommunityToolbar.setLeftString(getString(R.string.amity_edit_profile))

        editCommunityToolbar.setClickListener(this)
        supportActionBar?.displayOptions = ActionBar.DISPLAY_SHOW_CUSTOM
        setSupportActionBar(communityToolbar)
    }

    private fun loadFragment() {
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        mFragment = AmityCommunityEditorFragment.newInstance(communityId).build(this)
        fragmentTransaction.replace(R.id.fragmentContainer, mFragment)
        fragmentTransaction.commit()
    }

    override fun leftIconClick() {
        mFragment.onLeftIconClick()
    }

    override fun rightIconClick() {
    }

}