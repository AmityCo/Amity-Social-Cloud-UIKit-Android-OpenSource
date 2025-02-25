package com.amity.socialcloud.uikit.community.explore.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.amity.socialcloud.sdk.model.social.community.AmityCommunity
import com.amity.socialcloud.uikit.common.base.AmityBaseFragment
import com.amity.socialcloud.uikit.common.utils.AmityExceptionCatchLinearLayoutManager
import com.amity.socialcloud.uikit.community.R
import com.amity.socialcloud.uikit.community.compose.community.profile.AmityCommunityProfilePageActivity
import com.amity.socialcloud.uikit.community.databinding.AmityFragmentRecommendedCommunityBinding
import com.amity.socialcloud.uikit.community.explore.adapter.AmityCommunityItemDecoration
import com.amity.socialcloud.uikit.community.explore.adapter.AmityRecommendedCommunitiesAdapter
import com.amity.socialcloud.uikit.community.explore.viewmodel.AmityExploreCommunityViewModel
import com.amity.socialcloud.uikit.community.mycommunity.listener.AmityMyCommunityItemClickListener
import com.ekoapp.rxlifecycle.extension.untilLifecycleEnd

class AmityRecommendedCommunityFragment : AmityBaseFragment(),
    AmityMyCommunityItemClickListener {

    private lateinit var viewModel: AmityExploreCommunityViewModel
    private lateinit var adapter: AmityRecommendedCommunitiesAdapter
    private lateinit var binding: AmityFragmentRecommendedCommunityBinding


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewModel =
            ViewModelProvider(requireActivity()).get(AmityExploreCommunityViewModel::class.java)
        binding = AmityFragmentRecommendedCommunityBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initializeRecyclerView()
    }

    override fun onResume() {
        super.onResume()
        addItemTouchListener()
    }

    internal fun refresh() {
        getRecommendedCommunity()
    }

    private fun initializeRecyclerView() {
        adapter = AmityRecommendedCommunitiesAdapter(this)
        binding.rvRecommCommunity.layoutManager = AmityExceptionCatchLinearLayoutManager(
            requireContext(), LinearLayoutManager.HORIZONTAL, false
        )
        binding.rvRecommCommunity.adapter = adapter
        binding.rvRecommCommunity.addItemDecoration(
            AmityCommunityItemDecoration(
                requireContext().resources.getDimensionPixelSize(R.dimen.amity_ten),
                requireContext().resources.getDimensionPixelSize(R.dimen.amity_padding_xs),
                requireContext().resources.getDimensionPixelSize(R.dimen.amity_eighteen),
                requireContext().resources.getDimensionPixelSize(R.dimen.amity_padding_xs)
            )
        )
        getRecommendedCommunity()
    }

    private fun getRecommendedCommunity() {
        viewModel.getRecommendedCommunity {
            viewModel.emptyRecommendedList.set(it.isEmpty())
            adapter.submitList(it)
        }.untilLifecycleEnd(this)
            .subscribe()
    }

    private fun addItemTouchListener() {
        val touchListener = object : RecyclerView.OnItemTouchListener {
            override fun onInterceptTouchEvent(rv: RecyclerView, e: MotionEvent): Boolean {
                val action = e.action
                return if (binding.rvRecommCommunity.canScrollHorizontally(RecyclerView.FOCUS_FORWARD)) {
                    when (action) {
                        MotionEvent.ACTION_MOVE -> rv.parent.requestDisallowInterceptTouchEvent(true)
                    }
                    false
                } else {
                    when (action) {
                        MotionEvent.ACTION_MOVE -> rv.parent.requestDisallowInterceptTouchEvent(
                            false
                        )
                    }
                    false
                }
            }

            override fun onTouchEvent(rv: RecyclerView, e: MotionEvent) {}

            override fun onRequestDisallowInterceptTouchEvent(disallowIntercept: Boolean) {}

        }
        binding.rvRecommCommunity.addOnItemTouchListener(touchListener)
    }

    override fun onCommunitySelected(community: AmityCommunity?) {
        navigateToCommunityDetails(community)
    }

    private fun navigateToCommunityDetails(community: AmityCommunity?) {
        if (community != null) {
            val intent = AmityCommunityProfilePageActivity.newIntent(
                context = requireContext(),
                communityId = community.getCommunityId(),
            )
            requireContext().startActivity(intent)
        }
    }

    class Builder internal constructor() {
        fun build(): AmityRecommendedCommunityFragment {
            return AmityRecommendedCommunityFragment()
        }
    }

    companion object {
        fun newInstance(): Builder {
            return Builder()
        }
    }
}
