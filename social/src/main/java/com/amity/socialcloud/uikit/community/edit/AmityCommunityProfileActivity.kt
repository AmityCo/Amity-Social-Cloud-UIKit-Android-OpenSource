package com.amity.socialcloud.uikit.community.edit

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.amity.socialcloud.uikit.common.components.AmityToolBarClickListener
import com.amity.socialcloud.uikit.community.R
import com.amity.socialcloud.uikit.community.databinding.AmityActivityEditCommunityBinding
import com.amity.socialcloud.uikit.community.ui.viewModel.AmityCreateCommunityViewModel

class AmityCommunityProfileActivity : AppCompatActivity(), AmityToolBarClickListener {

    private val binding: AmityActivityEditCommunityBinding by lazy {
        AmityActivityEditCommunityBinding.inflate(layoutInflater)
    }
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
        setContentView(binding.root)

        communityId = intent?.getStringExtra(COMMUNITY_ID) ?: ""
        setUpToolbar()
        if (savedInstanceState == null) {
            loadFragment()
        }

    }

    private fun setUpToolbar() {
        binding.editCommunityToolbar.apply {
            setLeftDrawable(
                    ContextCompat.getDrawable(this@AmityCommunityProfileActivity, R.drawable.amity_ic_arrow_back)
            )
            setLeftString(getString(R.string.amity_edit_profile))

            setClickListener(this@AmityCommunityProfileActivity)
            supportActionBar?.displayOptions = ActionBar.DISPLAY_SHOW_CUSTOM
            setSupportActionBar(this)
        }
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