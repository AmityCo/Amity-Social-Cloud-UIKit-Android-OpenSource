package com.amity.socialcloud.uikit.community.explore.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import com.amity.socialcloud.sdk.model.social.category.AmityCommunityCategory
import com.amity.socialcloud.sdk.model.social.community.AmityCommunity
import com.amity.socialcloud.uikit.common.base.AmityBaseFragment
import com.amity.socialcloud.uikit.common.common.showSnackBar
import com.amity.socialcloud.uikit.common.utils.AmityRecyclerViewItemDecoration
import com.amity.socialcloud.uikit.community.R
import com.amity.socialcloud.uikit.community.compose.community.profile.AmityCommunityProfilePageActivity
import com.amity.socialcloud.uikit.community.databinding.AmityFragmentCategoryCommunityListBinding
import com.amity.socialcloud.uikit.community.detailpage.AmityCommunityPageActivity
import com.amity.socialcloud.uikit.community.explore.adapter.AmityCategoryCommunityListAdapter
import com.amity.socialcloud.uikit.community.explore.listener.AmityCommunityItemClickListener
import com.amity.socialcloud.uikit.community.explore.viewmodel.AmityCategoryCommunityListViewModel
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers

const val ARG_CATEGORY_ID = "Category_id"
const val ARG_CATEGORY_NAME = "Category_name"

class AmityCategoryCommunityListFragment : AmityBaseFragment(),
    AmityCommunityItemClickListener {
    private val viewModel: AmityCategoryCommunityListViewModel by viewModels()
    private lateinit var adapter: AmityCategoryCommunityListAdapter
    private var categoryId: String? = null
    private var categoryName: String? = null
    lateinit var binding: AmityFragmentCategoryCommunityListBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        categoryId = requireArguments().getString(ARG_CATEGORY_ID)
        categoryName = requireArguments().getString(ARG_CATEGORY_NAME)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.amity_fragment_category_community_list,
            container,
            false
        )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        adapter = AmityCategoryCommunityListAdapter(
            AmityCategoryCommunityListAdapter.AmityCommunityDiffUtil(),
            this
        )
        setupToolBar()
        initView()
        getCategories()
        adapter.addLoadStateListener { loadState ->
            if (loadState.source.refresh is LoadState.NotLoading) {
                handleListVisibility()
            }
        }
    }

    private fun setupToolBar() {
        categoryName?.let {
            (activity as AppCompatActivity).title = it
        }
    }

    private fun initView() {
        val itemDecorSpace =
            AmityRecyclerViewItemDecoration(resources.getDimensionPixelSize(R.dimen.amity_padding_xs))
        binding.rvCommunity.layoutManager = LinearLayoutManager(requireContext())
        binding.rvCommunity.adapter = adapter
        binding.rvCommunity.addItemDecoration(itemDecorSpace)
    }

    private fun getCategories() {
        disposable.add(viewModel.getCommunityByCategory(categoryId!!)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnNext {
                adapter.submitData(lifecycle, it)
            }
            .doOnError {
                it.message?.let { message -> view?.showSnackBar(message) }
            }
            .subscribe()
        )
    }

    private fun handleListVisibility() {
        binding.rvCommunity.visibility = if (adapter.itemCount > 0) View.VISIBLE else View.GONE
        binding.emptyView.visibility = if (adapter.itemCount > 0) View.GONE else View.VISIBLE
    }


    override fun onClick(community: AmityCommunity, position: Int) {
        if (viewModel.communityItemClickListener != null) {
            viewModel.communityItemClickListener?.onClick(community, position)
        } else {
            val detailIntent = AmityCommunityProfilePageActivity
                .newIntent(requireContext(), community.getCommunityId())
            startActivity(detailIntent)
        }
    }

    class Builder internal constructor() {
        private var categoryId: String? = null
        private var category: AmityCommunityCategory? = null


        internal fun categoryId(categoryId: String): Builder {
            this.categoryId = categoryId
            return this
        }

        internal fun category(category: AmityCommunityCategory): Builder {
            this.category = category
            return this
        }

        fun build(): AmityCategoryCommunityListFragment {
            val fragment = AmityCategoryCommunityListFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_CATEGORY_ID, categoryId ?: category!!.getCategoryId())
                    putString(ARG_CATEGORY_NAME, category?.getName())
                }
            }
            return fragment
        }

    }

    companion object {
        fun newInstance(categoryId: String): Builder {
            return Builder().categoryId(categoryId)
        }

        fun newInstance(category: AmityCommunityCategory): Builder {
            return Builder().category(category)
        }
    }

}
