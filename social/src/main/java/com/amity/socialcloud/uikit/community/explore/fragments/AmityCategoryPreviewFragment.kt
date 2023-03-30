package com.amity.socialcloud.uikit.community.explore.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.paging.LoadState
import com.amity.socialcloud.sdk.model.social.category.AmityCommunityCategory
import com.amity.socialcloud.uikit.common.base.AmityBaseFragment
import com.amity.socialcloud.uikit.common.common.expandViewHitArea
import com.amity.socialcloud.uikit.common.utils.AmityExceptionCatchGridLayoutManager
import com.amity.socialcloud.uikit.common.utils.AmityRecyclerViewItemDecoration
import com.amity.socialcloud.uikit.community.R
import com.amity.socialcloud.uikit.community.databinding.AmityFragmentCategoryPreviewBinding
import com.amity.socialcloud.uikit.community.explore.activity.AmityCategoryCommunityListActivity
import com.amity.socialcloud.uikit.community.explore.activity.AmityCategoryListActivity
import com.amity.socialcloud.uikit.community.explore.adapter.AmityCommunityCategoryAdapter
import com.amity.socialcloud.uikit.community.explore.listener.AmityCategoryItemClickListener
import com.amity.socialcloud.uikit.community.explore.viewmodel.AmityExploreCommunityViewModel
import com.ekoapp.rxlifecycle.extension.untilLifecycleEnd

class AmityCategoryPreviewFragment : AmityBaseFragment(),
    AmityCategoryItemClickListener {

    private lateinit var viewModel: AmityExploreCommunityViewModel
    private lateinit var communityCategoryAdapter: AmityCommunityCategoryAdapter
    private lateinit var binding: AmityFragmentCategoryPreviewBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewModel =
            ViewModelProvider(requireActivity()).get(AmityExploreCommunityViewModel::class.java)
        binding = DataBindingUtil.inflate(
            inflater, R.layout.amity_fragment_category_preview, container, false
        )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initializeRecyclerView()
        initListener()

        binding.ivMore.expandViewHitArea()?.setOnClickListener {
            val intent = Intent(requireContext(), AmityCategoryListActivity::class.java)
            startActivity(intent)
        }
    }

    internal fun refresh() {
        getCategories()
    }

    private fun initializeRecyclerView() {
        communityCategoryAdapter = AmityCommunityCategoryAdapter(this)
        binding.rvCommunityCategory.layoutManager =
            AmityExceptionCatchGridLayoutManager(requireContext(), 2)
        binding.rvCommunityCategory.adapter = communityCategoryAdapter
        binding.rvCommunityCategory.addItemDecoration(
            AmityRecyclerViewItemDecoration(
                requireContext().resources.getDimensionPixelSize(R.dimen.amity_padding_s)
            )
        )
        getCategories()
    }

    private fun getCategories() {
        viewModel.getCommunityCategory {
            communityCategoryAdapter.submitData(lifecycle, it)
        }
            .untilLifecycleEnd(this)
            .subscribe()
    }

    private fun initListener() {
        communityCategoryAdapter.addLoadStateListener { loadState ->
            loadState.run {
                if (source.append is LoadState.NotLoading
                    && source.append.endOfPaginationReached
                ) {
                    viewModel.emptyCategoryList.set(communityCategoryAdapter.itemCount == 0)
                }
            }
        }
    }

    override fun onCategorySelected(category: AmityCommunityCategory) {
        val intent = AmityCategoryCommunityListActivity.newIntent(requireContext(), category)
        startActivity(intent)
    }

    class Builder internal constructor() {
        fun build(): AmityCategoryPreviewFragment {
            return AmityCategoryPreviewFragment()
        }
    }

    companion object {
        fun newInstance(): Builder {
            return Builder()
        }
    }

}
