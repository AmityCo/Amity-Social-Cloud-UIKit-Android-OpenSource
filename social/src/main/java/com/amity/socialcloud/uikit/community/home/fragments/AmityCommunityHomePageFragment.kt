package com.amity.socialcloud.uikit.community.home.fragments

import android.app.SearchManager
import android.content.Context
import android.os.Bundle
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.databinding.ObservableField
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.amity.socialcloud.uikit.common.base.AmityFragmentStateAdapter
import com.amity.socialcloud.uikit.common.common.views.AmityColorPaletteUtil
import com.amity.socialcloud.uikit.common.common.views.AmityColorShade
import com.amity.socialcloud.uikit.common.model.AmityEventIdentifier
import com.amity.socialcloud.uikit.common.utils.AmityAndroidUtil
import com.amity.socialcloud.uikit.community.R
import com.amity.socialcloud.uikit.community.databinding.AmityFragmentCommunityHomePageBinding
import com.amity.socialcloud.uikit.community.explore.fragments.AmityCommunityExplorerFragment
import com.amity.socialcloud.uikit.community.mycommunity.fragment.AmityMyCommunityFragment
import com.amity.socialcloud.uikit.community.newsfeed.fragment.AmityNewsFeedFragment
import com.amity.socialcloud.uikit.community.newsfeed.fragment.AmityNewsFeedV4Fragment
import com.amity.socialcloud.uikit.community.search.AmityUserSearchFragment
import com.amity.socialcloud.uikit.community.setting.AmityCommunitySearchFragment
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.subjects.PublishSubject
import java.util.concurrent.TimeUnit


class AmityCommunityHomePageFragment : Fragment() {

    private lateinit var fragmentStateAdapter: AmityFragmentStateAdapter
    private lateinit var globalSearchStateAdapter: AmityFragmentStateAdapter
    private lateinit var searchMenuItem: MenuItem
    private lateinit var binding: AmityFragmentCommunityHomePageBinding
    private val viewModel: AmityCommunityHomeViewModel by viewModels()
    private var textChangeDisposable: Disposable? = null
    private val textChangeSubject: PublishSubject<String> = PublishSubject.create()
    private val searchString = ObservableField("")
    private var useNewsFeedV4: Boolean = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.amity_fragment_community_home_page,
            container,
            false
        )
        binding.viewModel = viewModel
        binding.tabLayout.disableSwipe()
        binding.tabLayout.setOffscreenPageLimit(3)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(true)

        useNewsFeedV4 = requireArguments().getBoolean(EXTRA_USE_NEWS_FEED_V4, false)

        initTabLayout()
        setUpSearchTabLayout()
        addViewModelListeners()
        subscribeTextChangeEvents()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        if (textChangeDisposable?.isDisposed == false) {
            textChangeDisposable?.dispose()
        }
    }

    private fun initTabLayout() {
        fragmentStateAdapter = AmityFragmentStateAdapter(
            childFragmentManager,
            requireActivity().lifecycle
        )
        globalSearchStateAdapter = AmityFragmentStateAdapter(
            childFragmentManager,
            requireActivity().lifecycle
        )
        fragmentStateAdapter.setFragmentList(
            arrayListOf(
                AmityFragmentStateAdapter.AmityPagerModel(
                    getString(R.string.amity_title_news_feed),
                    getNewsFeedFragment()
                ),
                AmityFragmentStateAdapter.AmityPagerModel(
                    getString(R.string.amity_title_explore),
                    getExploreFragment()
                ),
                AmityFragmentStateAdapter.AmityPagerModel(
                    getString(R.string.amity_title_my_communities),
                    getMyCommunityFragment()
                )
            )
        )
        binding.tabLayout.setAdapter(fragmentStateAdapter)
    }

    private fun getExploreFragment(): Fragment {
        return AmityCommunityExplorerFragment.newInstance().build()
    }


    private fun getNewsFeedFragment(): Fragment {
        return if (useNewsFeedV4) {
            AmityNewsFeedV4Fragment.newInstance().build()
        } else {
            AmityNewsFeedFragment.newInstance().build()
        }
    }

    private fun getMyCommunityFragment(): Fragment {
        return AmityMyCommunityFragment.newInstance().build()
    }

    private fun addViewModelListeners() {
        viewModel.onAmityEventReceived += { event ->
            when (event.type) {
                AmityEventIdentifier.EXPLORE_COMMUNITY -> {
                    //searchMenuItem.expandActionView()
                    binding.tabLayout.switchTab(1)
                }

                else -> {

                }
            }
        }
    }

    private fun setUpSearchTabLayout() {
        globalSearchStateAdapter.setFragmentList(
            arrayListOf(
                AmityFragmentStateAdapter.AmityPagerModel(
                    getString(R.string.amity_communities),
                    AmityCommunitySearchFragment.newInstance(searchString)
                        .build(requireActivity() as AppCompatActivity)
                ),
                AmityFragmentStateAdapter.AmityPagerModel(
                    getString(R.string.amity_accounts),
                    AmityUserSearchFragment.newInstance(searchString)
                        .build(requireActivity() as AppCompatActivity)
                )
            )
        )
        binding.globalSearchTabLayout.setAdapter(globalSearchStateAdapter)
    }

    private fun subscribeTextChangeEvents() {
        textChangeDisposable = textChangeSubject.debounce(300, TimeUnit.MILLISECONDS)
            .map {
                if (searchString.get() != it) {
                    searchString.set(it)
                }
                viewModel.emptySearchString.set(it.isEmpty())
            }
            .subscribe()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        val searchManager =
            requireActivity().getSystemService(Context.SEARCH_SERVICE) as SearchManager
        val searchView =
            SearchView((activity as AppCompatActivity).supportActionBar!!.themedContext)
        searchView.queryHint = getString(R.string.amity_search)
        searchView.maxWidth = Int.MAX_VALUE

        val searchEditText =
            searchView.findViewById<EditText>(androidx.appcompat.R.id.search_src_text)
        searchEditText.setTextColor(
            ContextCompat.getColor(
                requireContext(),
                R.color.amityColorBase
            )
        )
        searchEditText.setHintTextColor(
            AmityColorPaletteUtil.getColor(
                ContextCompat.getColor(requireContext(), R.color.amityColorBase),
                AmityColorShade.SHADE2
            )
        )
        searchEditText.imeOptions = EditorInfo.IME_ACTION_NEXT
        searchEditText.setOnEditorActionListener(object : TextView.OnEditorActionListener {
            override fun onEditorAction(v: TextView?, actionId: Int, event: KeyEvent?): Boolean {
                if (actionId == EditorInfo.IME_ACTION_NEXT) {
                    AmityAndroidUtil.hideKeyboard(searchEditText)
                    return true
                }
                return false
            }
        })

        searchMenuItem = menu.add("SearchMenu").setVisible(true).setActionView(searchView)
            .setIcon(R.drawable.amity_ic_search)
        searchMenuItem.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM or MenuItem.SHOW_AS_ACTION_COLLAPSE_ACTION_VIEW)

        searchView.setSearchableInfo(searchManager.getSearchableInfo(requireActivity().componentName))
        val queryTextListener = object : SearchView.OnQueryTextListener {
            override fun onQueryTextChange(newText: String?): Boolean {
                newText?.let { textChangeSubject.onNext(it) }
                return true
            }

            override fun onQueryTextSubmit(query: String?): Boolean {
                query?.let { textChangeSubject.onNext(it) }
                searchMenuItem.collapseActionView()
                return true
            }
        }

        searchView.setOnQueryTextListener(queryTextListener)

        searchMenuItem.setOnActionExpandListener(object : MenuItem.OnActionExpandListener {
            override fun onMenuItemActionExpand(item: MenuItem): Boolean {
                viewModel.isSearchMode.set(true)
                return true
            }

            override fun onMenuItemActionCollapse(item: MenuItem): Boolean {
                viewModel.isSearchMode.set(false)
                return true
            }
        })

    }

    class Builder internal constructor() {
        private var mUseNewsFeedV4: Boolean = false

        fun build(): AmityCommunityHomePageFragment {
            return AmityCommunityHomePageFragment().apply {
                arguments = Bundle().apply {
                    putBoolean(EXTRA_USE_NEWS_FEED_V4, mUseNewsFeedV4)
                }
            }
        }

        internal fun useNewsFeedV4(): Builder {
            mUseNewsFeedV4 = true
            return this
        }
    }

    companion object {
        private const val EXTRA_USE_NEWS_FEED_V4 = "EXTRA_USE_NEWS_FEED_V4"

        fun newInstance(useNewsFeedV4: Boolean? = false): Builder {
            return if (useNewsFeedV4 == true) {
                Builder().useNewsFeedV4()
            } else {
                Builder()
            }
        }
    }
}