package com.amity.socialcloud.uikit.community.ui.view

import android.os.Bundle
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import com.amity.socialcloud.uikit.common.base.AmityBaseActivity
import com.amity.socialcloud.uikit.common.components.AmityToolBarClickListener
import com.amity.socialcloud.uikit.common.utils.AmityConstants
import com.amity.socialcloud.uikit.community.BR
import com.amity.socialcloud.uikit.community.R
import com.amity.socialcloud.uikit.community.data.AmitySelectMemberItem
import com.amity.socialcloud.uikit.community.databinding.AmityActivitySelectMembersListBinding
import com.amity.socialcloud.uikit.community.ui.viewModel.AmitySelectMembersViewModel
import kotlinx.android.synthetic.main.amity_activity_select_members_list.*

class AmityMemberPickerActivity : AmityBaseActivity<AmityActivitySelectMembersListBinding,
        AmitySelectMembersViewModel>(), AmityToolBarClickListener {

    private val mViewModel: AmitySelectMembersViewModel by viewModels()
    private lateinit var mFragment: AmityMemberPickerFragment


    override fun getLayoutId(): Int = R.layout.amity_activity_select_members_list

    override fun getViewModel(): AmitySelectMembersViewModel = mViewModel

    override fun getBindingVariable(): Int = BR.viewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setUpToolBar()
        loadFragment()

    }

    private fun loadFragment() {
        intent?.getParcelableArrayListExtra<AmitySelectMemberItem>(AmityConstants.MEMBERS_LIST)
                ?.let { list ->
                    val fragmentManager = supportFragmentManager
                    val fragmentTransaction = fragmentManager.beginTransaction()
                    mFragment = AmityMemberPickerFragment.newInstance()
                            .selectedMembers(list)
                            .build()
                    fragmentTransaction.replace(R.id.fragmentContainer, mFragment)
                    fragmentTransaction.commit()
                }
    }

    private fun setUpToolBar() {
        smToolBar.setLeftDrawable(
                ContextCompat.getDrawable(this, R.drawable.amity_ic_arrow_back)
        )
        smToolBar.setRightString(getString(R.string.amity_done))
        smToolBar.setClickListener(this)
        setSelectionCount()
    }

    private fun setSelectionCount() {
        mViewModel.leftString.observe(this, Observer {
            smToolBar.setLeftString(it)
        })
        mViewModel.rightStringActive.observe(this, Observer {
            smToolBar.setRightStringActive(it)
        })
    }

    override fun leftIconClick() {
        mFragment.finishActivity(true)
    }

    override fun onBackPressed() {
        mFragment.finishActivity(true)
    }

    override fun rightIconClick() {
        mFragment.finishActivity(false)
    }

}