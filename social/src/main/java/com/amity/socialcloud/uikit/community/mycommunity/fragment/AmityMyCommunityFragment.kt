package com.amity.socialcloud.uikit.community.mycommunity.fragment

import android.content.Intent
import android.os.Bundle
import android.view.*
import android.view.inputmethod.EditorInfo
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.amity.socialcloud.sdk.social.community.AmityCommunity
import com.amity.socialcloud.uikit.common.base.AmityBaseFragment
import com.amity.socialcloud.uikit.common.common.setShape
import com.amity.socialcloud.uikit.common.common.views.AmityColorShade
import com.amity.socialcloud.uikit.common.model.AmityEventIdentifier
import com.amity.socialcloud.uikit.common.utils.AmityAndroidUtil
import com.amity.socialcloud.uikit.common.utils.AmityRecyclerViewItemDecoration
import com.amity.socialcloud.uikit.community.R
import com.amity.socialcloud.uikit.community.databinding.AmityFragmentMyCommunityBinding
import com.amity.socialcloud.uikit.community.detailpage.AmityCommunityPageActivity
import com.amity.socialcloud.uikit.community.mycommunity.adapter.AmityMyCommunityListAdapter
import com.amity.socialcloud.uikit.community.mycommunity.listener.AmityMyCommunityItemClickListener
import com.amity.socialcloud.uikit.community.mycommunity.viewmodel.AmityMyCommunityListViewModel
import com.amity.socialcloud.uikit.community.ui.view.AmityCommunityCreatorActivity
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

private const val ARG_SHOW_SEARCH = "ARG_SHOW_SEARCH"
private const val ARG_SHOW_OPTIONS_MENU = "ARG_SHOW_OPTIONS_MENU"

class AmityMyCommunityFragment : AmityBaseFragment(),
    AmityMyCommunityItemClickListener {

    private val viewModel: AmityMyCommunityListViewModel by viewModels()
    lateinit var binding: AmityFragmentMyCommunityBinding
    private lateinit var mAdapter: AmityMyCommunityListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (arguments?.getBoolean(ARG_SHOW_OPTIONS_MENU) != false) {
            setHasOptionsMenu(true)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding =
            DataBindingUtil.inflate(
                inflater,
                R.layout.amity_fragment_my_community,
                container,
                false
            )
        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpToolBar()
        subscribeObservers()
        initRecyclerView()
        handleEditTextInput()
        if (arguments?.getBoolean(ARG_SHOW_SEARCH) != false) {
            binding.etSearch.visibility = View.VISIBLE
        } else {
            binding.etSearch.visibility = View.GONE
        }
    }

    private fun handleEditTextInput() {
        binding.etSearch.setShape(
            null, null, null, null,
            R.color.amityColorBase, null, AmityColorShade.SHADE4
        )
        binding.etSearch.setOnEditorActionListener(object : TextView.OnEditorActionListener {
            override fun onEditorAction(v: TextView?, actionId: Int, event: KeyEvent?): Boolean {
                if (actionId == EditorInfo.IME_ACTION_NEXT) {
                    AmityAndroidUtil.hideKeyboard(binding.etSearch)
                    return true
                }
                return false
            }
        })
    }

    private fun setUpToolBar() {
        (activity as AppCompatActivity).supportActionBar?.title =
            getString(R.string.amity_my_community)
    }

    private fun subscribeObservers() {
        viewModel.setPropertyChangeCallback()
        viewModel.onAmityEventReceived += { event ->
            when (event.type) {
                AmityEventIdentifier.SEARCH_STRING_CHANGED -> searchCommunity()
                else -> {
                }
            }
        }
    }

    private fun searchCommunity() {
        disposable.clear()
        disposable.add(viewModel.getCommunityList().subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnNext { list ->
                viewModel.emptyCommunity.set(list.size == 0)
                mAdapter.submitList(list)
            }.doOnError {

            }.subscribe()
        )
    }

    private fun initRecyclerView() {
        mAdapter = AmityMyCommunityListAdapter(this, false)
        binding.rvMyCommunities.apply {
            layoutManager = LinearLayoutManager(requireContext())
            this.adapter = mAdapter
            addItemDecoration(
                    AmityRecyclerViewItemDecoration(
                            resources.getDimensionPixelSize(R.dimen.amity_padding_xs),
                            0, resources.getDimensionPixelSize(R.dimen.amity_padding_xs), 0
                    )
            )
            setHasFixedSize(true)
        }

        disposable.add(viewModel.getCommunityList().subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnNext { list ->
                viewModel.emptyCommunity.set(list.size == 0)
                mAdapter.submitList(list)
            }.doOnError {

            }.subscribe()
        )
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        val shouldShowAddButton =
            resources.getBoolean(R.bool.amity_uikit_social_community_creation_button_visible)
        if (shouldShowAddButton) {
            val drawable = ContextCompat.getDrawable(requireContext(), R.drawable.amity_ic_add)
            menu.add(Menu.NONE, 1, Menu.NONE, getString(R.string.amity_add))
                ?.setIcon(drawable)
                ?.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS)
        }
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val createCommunityIntent =
            Intent(requireActivity(), AmityCommunityCreatorActivity::class.java)
        startActivity(createCommunityIntent)
        return super.onOptionsItemSelected(item)
    }

    override fun onCommunitySelected(ekoCommunity: AmityCommunity?) {
        if (viewModel.myCommunityItemClickListener != null) {
            viewModel.myCommunityItemClickListener?.onCommunitySelected(ekoCommunity)
        } else {
            navigateToCommunityDetails(ekoCommunity)
        }
    }

    private fun navigateToCommunityDetails(ekoCommunity: AmityCommunity?) {
        if (ekoCommunity != null) {
            val detailIntent = AmityCommunityPageActivity.newIntent(
                requireContext(),
                ekoCommunity
            )
            startActivity(detailIntent)
        }

    }

    class Builder internal constructor() {
        private var myCommunityItemClickListener: AmityMyCommunityItemClickListener? = null
        private var showSearch = true
        private var showOptionsMenu = true

        fun build(): AmityMyCommunityFragment {
            val fragment = AmityMyCommunityFragment().apply {
                arguments = Bundle().apply {
                    putBoolean(ARG_SHOW_SEARCH, showSearch)
                    putBoolean(ARG_SHOW_OPTIONS_MENU, showOptionsMenu)
                }
            }
            return fragment
        }

        fun enableSearch(isEnable: Boolean): Builder {
            showSearch = isEnable
            return this
        }

        private fun showOptionsMenu(value: Boolean): Builder {
            showOptionsMenu = value
            return this
        }
    }

    companion object {
        fun newInstance(): Builder {
            return Builder()
        }
    }

}