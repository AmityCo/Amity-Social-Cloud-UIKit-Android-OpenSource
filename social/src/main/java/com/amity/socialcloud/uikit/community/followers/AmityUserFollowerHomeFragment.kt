package com.amity.socialcloud.uikit.community.followers

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import com.amity.socialcloud.uikit.common.base.AmityFragmentStateAdapter
import com.amity.socialcloud.uikit.community.R
import com.amity.socialcloud.uikit.community.databinding.AmityUserFollowerHomeFragmentBinding
import com.amity.socialcloud.uikit.community.following.AmityUserFollowingFragment
import com.trello.rxlifecycle3.components.support.RxFragment

class AmityUserFollowerHomeFragment : RxFragment() {

    private var _binding: AmityUserFollowerHomeFragmentBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = AmityUserFollowerHomeFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

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
            binding.followersTabLayout.setAdapter(fragmentStateAdapter)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
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