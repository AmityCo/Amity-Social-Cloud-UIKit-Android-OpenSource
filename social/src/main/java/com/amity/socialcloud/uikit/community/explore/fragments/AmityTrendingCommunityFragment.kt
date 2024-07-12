package com.amity.socialcloud.uikit.community.explore.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.amity.socialcloud.sdk.model.social.community.AmityCommunity
import com.amity.socialcloud.uikit.common.base.AmityBaseFragment
import com.amity.socialcloud.uikit.common.utils.AmityExceptionCatchLinearLayoutManager
import com.amity.socialcloud.uikit.common.utils.AmityRecyclerViewItemDecoration
import com.amity.socialcloud.uikit.community.R
import com.amity.socialcloud.uikit.community.compose.community.profile.AmityCommunityProfilePageActivity
import com.amity.socialcloud.uikit.community.databinding.AmityFragmentTrendingCommunityBinding
import com.amity.socialcloud.uikit.community.detailpage.AmityCommunityPageActivity
import com.amity.socialcloud.uikit.community.explore.adapter.AmityTrendingCommunityAdapter
import com.amity.socialcloud.uikit.community.explore.viewmodel.AmityExploreCommunityViewModel
import com.amity.socialcloud.uikit.community.mycommunity.listener.AmityMyCommunityItemClickListener
import com.ekoapp.rxlifecycle.extension.untilLifecycleEnd

class AmityTrendingCommunityFragment : AmityBaseFragment(),
    AmityMyCommunityItemClickListener {

    private lateinit var adapter: AmityTrendingCommunityAdapter
    private lateinit var viewModel: AmityExploreCommunityViewModel
    private lateinit var binding: AmityFragmentTrendingCommunityBinding


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewModel =
            ViewModelProvider(requireActivity()).get(AmityExploreCommunityViewModel::class.java)
        binding = AmityFragmentTrendingCommunityBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initializeRecyclerView()
    }

    internal fun refresh() {
        getTrendingCommunity()
    }

    private fun initializeRecyclerView() {
        adapter = AmityTrendingCommunityAdapter(this)
        binding.rvTrendingCommunity.layoutManager =
            AmityExceptionCatchLinearLayoutManager(requireContext())
        binding.rvTrendingCommunity.adapter = adapter
        binding.rvTrendingCommunity.addItemDecoration(
            AmityRecyclerViewItemDecoration(
                requireContext().resources.getDimensionPixelSize(R.dimen.amity_padding_m1),
                requireContext().resources.getDimensionPixelSize(R.dimen.amity_zero),
                requireContext().resources.getDimensionPixelSize(R.dimen.amity_padding_xs)
            )
        )
        getTrendingCommunity()
    }

    private fun getTrendingCommunity() {
        viewModel.getTrendingCommunity {
            viewModel.emptyTrendingList.set(it.isEmpty())
            adapter.submitList(it)
        }.untilLifecycleEnd(this)
            .subscribe()
    }

    override fun onCommunitySelected(community: AmityCommunity?) {
        if (community != null) {
            val intent = AmityCommunityProfilePageActivity.newIntent(
                context = requireContext(),
                communityId = community.getCommunityId(),
            )
            requireContext().startActivity(intent)
        }
    }

    class Builder internal constructor() {
        fun build(): AmityTrendingCommunityFragment {
            return AmityTrendingCommunityFragment()
        }
    }

    companion object {
        fun newInstance(): Builder {
            return Builder()
        }
    }

}
