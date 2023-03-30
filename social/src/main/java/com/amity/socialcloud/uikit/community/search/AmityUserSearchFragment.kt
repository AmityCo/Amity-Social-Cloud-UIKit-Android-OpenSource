package com.amity.socialcloud.uikit.community.search

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
import com.amity.socialcloud.sdk.model.core.user.AmityUser
import com.amity.socialcloud.uikit.common.common.views.AmityColorPaletteUtil
import com.amity.socialcloud.uikit.common.common.views.AmityColorShade
import com.amity.socialcloud.uikit.common.model.AmityEventIdentifier
import com.amity.socialcloud.uikit.common.utils.AmityRecyclerViewItemDecoration
import com.amity.socialcloud.uikit.community.R
import com.amity.socialcloud.uikit.community.databinding.AmityFragmentUsersGlobalSearchBinding
import com.amity.socialcloud.uikit.community.newsfeed.listener.AmityUserClickListener
import com.amity.socialcloud.uikit.social.AmitySocialUISettings
import com.ekoapp.rxlifecycle.extension.untilLifecycleEnd
import com.trello.rxlifecycle4.components.support.RxFragment
import io.reactivex.rxjava3.disposables.Disposable
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class AmityUserSearchFragment : RxFragment(R.layout.amity_fragment_users_global_search) {

    private var searchDisposable: Disposable? = null
    private lateinit var usersAdapter: AmityUsersAdapter
    private var isEmptyList = false
    private lateinit var viewModel: AmityUsersGlobalSearchViewModel
    private lateinit var binding: AmityFragmentUsersGlobalSearchBinding


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = AmityFragmentUsersGlobalSearchBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel =
            ViewModelProvider(requireActivity()).get(AmityUsersGlobalSearchViewModel::class.java)
        viewModel.setPropertyChangeCallback()
        initSearchRecyclerView()
        observeSearchEvent()
    }

    private fun initSearchRecyclerView() {
        val userClickListener = object : AmityUserClickListener {
            override fun onClickUser(user: AmityUser) {
                AmitySocialUISettings.globalUserClickListener.onClickUser(
                    this@AmityUserSearchFragment,
                    user
                )
            }
        }
        usersAdapter = AmityUsersAdapter(requireContext(), userClickListener)
        binding.rvUsersSearch.apply {
            adapter = usersAdapter
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
        usersAdapter.addLoadStateListener { loadState ->
            if (loadState.source.refresh is LoadState.NotLoading && loadState.append.endOfPaginationReached && usersAdapter.itemCount < 1) {
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
    }

    override fun onResume() {
        super.onResume()
        searchUsers()
    }

    private fun observeSearchEvent() {
        viewModel.onAmityEventReceived += { event ->
            if (event.type == AmityEventIdentifier.SEARCH_USERS) {
                searchUsers()
            }
        }
    }

    private fun searchUsers() {
        searchDisposable?.dispose()
        lifecycleScope.launch(Dispatchers.Main) {
            whenStarted {
                binding.progressBar.show()
                binding.tvNoResults.visibility = View.GONE
            }
        }
        isEmptyList = false
        searchDisposable = viewModel.searchUsers {
            onSearchUserResult(it)
        }.untilLifecycleEnd(this)
            .subscribe()
    }

    private fun onSearchUserResult(list: PagingData<AmityUser>) {
        usersAdapter.submitData(lifecycle,list)
    }

    override fun onDestroy() {
        super.onDestroy()
        searchDisposable?.dispose()
    }

    class Builder internal constructor() {
        private var input = ObservableField("")

        fun build(activity: AppCompatActivity): AmityUserSearchFragment {
            val fragment = AmityUserSearchFragment()
            fragment.viewModel =
                ViewModelProvider(activity).get(AmityUsersGlobalSearchViewModel::class.java)
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