package com.amity.socialcloud.uikit.common.memberpicker

import android.os.Bundle
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import com.amity.socialcloud.uikit.common.BR
import com.amity.socialcloud.uikit.common.R
import com.amity.socialcloud.uikit.common.base.AmityBaseActivity
import com.amity.socialcloud.uikit.common.components.AmityToolBarClickListener
import com.amity.socialcloud.uikit.common.databinding.AmityActivityPickMemberListBinding
import com.amity.socialcloud.uikit.common.memberpicker.fragment.AmityMemberPickerFragment
import com.amity.socialcloud.uikit.common.memberpicker.viewmodel.AmityMemberPickerViewModel
import com.amity.socialcloud.uikit.common.model.AmitySelectMemberItem
import com.amity.socialcloud.uikit.common.utils.AmityConstants

class AmityMemberPickerActivity : AmityBaseActivity<AmityActivityPickMemberListBinding,
        AmityMemberPickerViewModel>(), AmityToolBarClickListener {


    private val mViewModel: AmityMemberPickerViewModel by viewModels()
    private lateinit var mFragment: AmityMemberPickerFragment


    override fun getLayoutId(): Int = R.layout.amity_activity_pick_member_list

    override fun getViewModel(): AmityMemberPickerViewModel = mViewModel

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
        setSupportActionBar(getViewDataBinding().smToolBar)
        getViewDataBinding().smToolBar.setLeftDrawable(
            ContextCompat.getDrawable(
                this@AmityMemberPickerActivity,
                R.drawable.amity_ic_arrow_back
            )
        )
        getViewDataBinding().smToolBar.setRightString(getString(R.string.amity_done))
        getViewDataBinding().smToolBar.setClickListener(this@AmityMemberPickerActivity)
        setSelectionCount()
    }

    private fun setSelectionCount() {
        mViewModel.rightStringActive.observe(this, Observer {
            getViewDataBinding().smToolBar.setRightStringActive(it)
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