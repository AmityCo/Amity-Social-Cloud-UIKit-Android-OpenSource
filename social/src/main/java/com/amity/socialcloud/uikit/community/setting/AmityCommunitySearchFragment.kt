package com.amity.socialcloud.uikit.community.setting

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.databinding.ObservableField
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.whenStarted
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.recyclerview.widget.LinearLayoutManager
import com.amity.socialcloud.sdk.model.social.community.AmityCommunity
import com.amity.socialcloud.uikit.common.common.views.AmityColorPaletteUtil
import com.amity.socialcloud.uikit.common.common.views.AmityColorShade
import com.amity.socialcloud.uikit.common.model.AmityEventIdentifier
import com.amity.socialcloud.uikit.common.utils.AmityRecyclerViewItemDecoration
import com.amity.socialcloud.uikit.community.R
import com.amity.socialcloud.uikit.community.compose.community.profile.AmityCommunityProfilePageActivity
import com.amity.socialcloud.uikit.community.databinding.AmityFragmentCommunityGlobalSearchBinding
import com.amity.socialcloud.uikit.community.detailpage.AmityCommunityPageActivity
import com.amity.socialcloud.uikit.community.mycommunity.adapter.AmityMyCommunitiesAdapter
import com.amity.socialcloud.uikit.community.mycommunity.listener.AmityMyCommunityItemClickListener
import com.amity.socialcloud.uikit.community.search.AmityCommunityGlobalSearchViewModel
import com.ekoapp.rxlifecycle.extension.untilLifecycleEnd
import com.trello.rxlifecycle4.components.support.RxFragment
import io.reactivex.rxjava3.disposables.Disposable
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class AmityCommunitySearchFragment :
    RxFragment(),
    AmityMyCommunityItemClickListener {

    private var searchDisposable: Disposable? = null
    private var isEmptyList = false
    private lateinit var viewModel: AmityCommunityGlobalSearchViewModel
    private lateinit var adapter: AmityMyCommunitiesAdapter
    private lateinit var binding: AmityFragmentCommunityGlobalSearchBinding


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = AmityFragmentCommunityGlobalSearchBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(requireActivity()).get(
            AmityCommunityGlobalSearchViewModel::class.java
        )
        initSearchRecyclerView()
        viewModel.setPropertyChangeCallback()
        observeSearchEvent()
    }

    private fun initSearchRecyclerView() {
        adapter = AmityMyCommunitiesAdapter(this)
        adapter.addLoadStateListener { loadState ->
            if (loadState.source.refresh is LoadState.NotLoading && adapter.itemCount == 0) {
                if (isEmptyList) {
                    binding.progressBar.hide()
                    binding.tvNoResults.visibility = View.VISIBLE
                } else {
                    isEmptyList = true
                }
            } else {
                binding.progressBar.hide()
                binding.tvNoResults.visibility = View.GONE
            }
        }
        binding.rvCommunitySearch.apply {
            adapter = this@AmityCommunitySearchFragment.adapter
            layoutManager = LinearLayoutManager(requireContext())
            addItemDecoration(
                AmityRecyclerViewItemDecoration(
                    resources.getDimensionPixelSize(R.dimen.amity_padding_m1)
                )
            )
            setHasFixedSize(true)
        }
        binding.tvNoResults.setTextColor(
            AmityColorPaletteUtil.getColor(
                ContextCompat.getColor(requireContext(), R.color.amityColorBase),
                AmityColorShade.SHADE3
            )
        )
    }

    private fun observeSearchEvent() {
        viewModel.onAmityEventReceived += { event ->
            if (event.type == AmityEventIdentifier.SEARCH_COMMUNITY) {
                searchCommunity()
            }
        }
    }

    override fun onResume() {
        super.onResume()
        if (viewModel.searchString.get()?.length != 0) {
            searchCommunity()
        }
    }

    private fun searchCommunity() {
        searchDisposable?.dispose()
        lifecycleScope.launch(Dispatchers.Main) {
            whenStarted {
                binding.progressBar.show()
                binding.tvNoResults.visibility = View.GONE
            }
        }
        isEmptyList = false
        searchDisposable = viewModel.searchCommunity(onResult = this::onSearchCommunityResult)
            .untilLifecycleEnd(this)
            .subscribe()
    }

    private fun onSearchCommunityResult(list: PagingData<AmityCommunity>) {
        adapter.submitData(lifecycle, list)
    }

    override fun onCommunitySelected(ekoCommunity: AmityCommunity?) {
        if (ekoCommunity != null) {
            val detailIntent = AmityCommunityProfilePageActivity.newIntent(
                requireContext(),
                ekoCommunity.getCommunityId()
            )
            startActivity(detailIntent)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        searchDisposable?.dispose()
    }

    class Builder internal constructor() {
        private var input = ObservableField("")

        fun build(activity: AppCompatActivity): AmityCommunitySearchFragment {
            val fragment = AmityCommunitySearchFragment()
            fragment.viewModel = ViewModelProvider(activity).get(
                AmityCommunityGlobalSearchViewModel::class.java
            )
            fragment.viewModel.searchString = input
            return fragment
        }

        fun setInputSource(searchSource: ObservableField<String>): Builder {
            input = searchSource
            return this
        }
    }

    companion object {

        fun newInstance(source: ObservableField<String>): Builder {
            return Builder().setInputSource(source)
        }
    }
}