package com.amity.socialcloud.uikit.community.explore.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.amity.socialcloud.uikit.community.R
import com.amity.socialcloud.uikit.community.databinding.AmityFragmentExploreBinding
import com.amity.socialcloud.uikit.community.explore.viewmodel.AmityExploreCommunityViewModel
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.TimeUnit

class AmityCommunityExplorerFragment : Fragment() {

    private val viewModel: AmityExploreCommunityViewModel by activityViewModels()
    private lateinit var binding: AmityFragmentExploreBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(
            inflater, R.layout.amity_fragment_explore, container, false
        )
        binding.viewModel = viewModel
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initListener()

        if (savedInstanceState == null) {
            val fragmentManager = childFragmentManager
            val fragmentTransaction = fragmentManager.beginTransaction()
            fragmentTransaction.add(R.id.recommendedContainer, getRecommendedFragment())
            fragmentTransaction.add(R.id.trendingContainer, getTrendingFragment())
            fragmentTransaction.add(R.id.categoryContainer, getCategoryPreviewFragment())
            fragmentTransaction.commit()
            fragmentManager.executePendingTransactions()
        }
    }

    private fun initListener() {
        binding.refreshLayout.setColorSchemeResources(R.color.amityColorPrimary)
        binding.refreshLayout.setOnRefreshListener {
            childFragmentManager.fragments.forEach { fragment ->
                when (fragment) {
                    is AmityCategoryPreviewFragment -> {
                        fragment.refresh()
                    }
                    is AmityTrendingCommunityFragment -> {
                        fragment.refresh()
                    }
                    is AmityRecommendedCommunityFragment -> {
                        fragment.refresh()
                    }
                }
            }
            Single.timer(1, TimeUnit.SECONDS, Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSuccess { binding.refreshLayout.isRefreshing = false  }
                .doOnError {binding.refreshLayout.isRefreshing = false }
                .subscribe()
        }
    }

    private fun getCategoryPreviewFragment(): Fragment {
        return AmityCategoryPreviewFragment.newInstance().build()
    }

    private fun getTrendingFragment(): Fragment {
        return AmityTrendingCommunityFragment.newInstance().build()
    }

    private fun getRecommendedFragment(): Fragment {
        return AmityRecommendedCommunityFragment.newInstance().build()
    }

    class Builder internal constructor(){
        fun build(): AmityCommunityExplorerFragment {
            return AmityCommunityExplorerFragment()
        }
    }

    companion object {
        fun newInstance(): Builder {
            return Builder()
        }
    }
}