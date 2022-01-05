package com.amity.socialcloud.uikit.community.followers

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.amity.socialcloud.uikit.common.base.AmityFragmentStateAdapter
import com.amity.socialcloud.uikit.community.R
import com.amity.socialcloud.uikit.community.following.AmityUserFollowingFragment
import com.trello.rxlifecycle3.components.support.RxFragment
import kotlinx.android.synthetic.main.amity_user_follower_home_fragment.*

class AmityUserFollowerHomeFragment :
    RxFragment(R.layout.amity_user_follower_home_fragment) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initTabLayout()
    }

    private fun initTabLayout() {
        arguments?.getString(ARG_USER_ID)?.let { nonNullUserId ->
            val fragmentStateAdapter = AmityFragmentStateAdapter(childFragmentManager, requireActivity().lifecycle)
            fragmentStateAdapter.setFragmentList(
                arrayListOf(
                    AmityFragmentStateAdapter.AmityPagerModel(
                        getString(R.string.amity_following),
                        AmityUserFollowingFragment.newInstance(nonNullUserId)
                            .build(requireActivity() as AppCompatActivity)
                    ),
                    AmityFragmentStateAdapter.AmityPagerModel(
                        getString(R.string.amity_followers),
                        AmityUserFollowerFragment.newInstance(nonNullUserId)
                            .build(requireActivity() as AppCompatActivity)
                    )
                )
            )
            followersTabLayout.setAdapter(fragmentStateAdapter)
        }
    }

    class Builder internal constructor() {
        var userId: String = ""

        fun build(): AmityUserFollowerHomeFragment {
            return AmityUserFollowerHomeFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_USER_ID, userId)
                }
            }
        }

        internal fun setUserId(userId: String): Builder {
            this.userId = userId
            return this
        }
    }

    companion object {
        private const val ARG_USER_ID = "ARG_USER_ID"

        fun newInstance(userId: String): Builder {
            return Builder().setUserId(userId)
        }
    }
}