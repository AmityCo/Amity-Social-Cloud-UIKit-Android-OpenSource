package com.amity.socialcloud.uikit.common.memberpicker.fragment


import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.SimpleItemAnimator
import com.amity.socialcloud.sdk.model.core.file.AmityImage
import com.amity.socialcloud.sdk.model.core.user.AmityUser
import com.amity.socialcloud.uikit.common.R
import com.amity.socialcloud.uikit.common.common.setShape
import com.amity.socialcloud.uikit.common.common.views.AmityColorShade
import com.amity.socialcloud.uikit.common.databinding.AmityFragmentSelectMembersListBinding
import com.amity.socialcloud.uikit.common.memberpicker.AmitySelectMembersItemDecoration
import com.amity.socialcloud.uikit.common.memberpicker.adapter.AmityMembersListAdapter
import com.amity.socialcloud.uikit.common.memberpicker.adapter.AmitySearchResultAdapter
import com.amity.socialcloud.uikit.common.memberpicker.adapter.AmitySelectedMemberAdapter
import com.amity.socialcloud.uikit.common.memberpicker.listener.AmitySelectMemberListener
import com.amity.socialcloud.uikit.common.memberpicker.listener.AmitySelectedMemberListener
import com.amity.socialcloud.uikit.common.memberpicker.viewmodel.AmityMemberPickerViewModel
import com.amity.socialcloud.uikit.common.model.AmityEventIdentifier
import com.amity.socialcloud.uikit.common.model.AmitySelectMemberItem
import com.amity.socialcloud.uikit.common.utils.AmityConstants
import com.amity.socialcloud.uikit.common.utils.AmityRecyclerViewItemDecoration
import com.trello.rxlifecycle4.components.support.RxFragment
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers

private const val ARG_MEMBERS_LIST = "ARG_MEMBERS_LIST"

class AmityMemberPickerFragment : RxFragment(), AmitySelectMemberListener,
    AmitySelectedMemberListener {

    private val mViewModel: AmityMemberPickerViewModel by activityViewModels()
    private lateinit var mSelectedMembersAdapter: AmitySelectedMemberAdapter
    private lateinit var mMemberListAdapter: AmityMembersListAdapter
    private lateinit var mSearchResultAdapter: AmitySearchResultAdapter
    private val receivedMembersList = arrayListOf<AmitySelectMemberItem>()
    private val disposable: CompositeDisposable by lazy {
        CompositeDisposable()
    }
    private val searchDisposable: CompositeDisposable by lazy {
        CompositeDisposable()
    }

    private lateinit var binding: AmityFragmentSelectMembersListBinding

    companion object {
        fun newInstance(): Builder {
            return Builder()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.amity_fragment_select_members_list, container, false
        )
        binding.viewModel = mViewModel
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        subscribeObservers()
        setUpSelectedMemberRecyclerView()
        setUpSearchRecyclerView()

        handleSelectedMembers()
        setUpMembersListRecyclerView()

        binding.etSearch.setShape(
            null, null, null, null,
            R.color.amityColorBase, null, AmityColorShade.SHADE4
        )

    }


    private fun handleSelectedMembers() {
        val list = arguments?.getParcelableArrayList<AmitySelectMemberItem>(ARG_MEMBERS_LIST)
        if (list != null && list.isNotEmpty()) {
            if (list[list.size - 1].name == "ADD") {
                list.removeAt(list.size - 1)
            }
            mViewModel.selectedMembersList.clear()
            receivedMembersList.addAll(list)
            for (item in list) {
                mViewModel.selectedMemberSet.add(item.id)
                mViewModel.prepareSelectedMembersList(item, true)
            }
            mSelectedMembersAdapter.submitList(mViewModel.selectedMembersList)
        }
        setToolBarState()
    }

    private fun setToolBarState() {
        if (mViewModel.selectedMembersList.size != 0) {
            mViewModel.leftString.value =
                "${mViewModel.selectedMembersList.size} ${getString(R.string.amity_selected)}"
            mViewModel.rightStringActive.value = true
        } else {
            mViewModel.leftString.value = getString(R.string.amity_select_members)
            mViewModel.rightStringActive.value = false
        }
    }

    private fun subscribeObservers() {
        mViewModel.setPropertyChangeCallback()

        mViewModel.onAmityEventReceived += { event ->
            when (event.type) {
                AmityEventIdentifier.SEARCH_STRING_CHANGED -> loadFilteredList()
                else -> {

                }
            }
        }
    }

    private fun setUpSelectedMemberRecyclerView() {
        mSelectedMembersAdapter = AmitySelectedMemberAdapter(this)
        val layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        binding.rvSelectedMembers.apply {
            this.layoutManager = layoutManager
            adapter = mSelectedMembersAdapter
            addItemDecoration(
                AmityRecyclerViewItemDecoration(
                    0,
                    resources.getDimensionPixelSize(R.dimen.amity_padding_m2),
                    0,
                    resources.getDimensionPixelSize(R.dimen.amity_padding_xxs)
                )
            )
        }
    }

    private fun setUpMembersListRecyclerView() {
        mMemberListAdapter = AmityMembersListAdapter(this, mViewModel)
        binding.rvMembersList.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = mMemberListAdapter
            addItemDecoration(
                AmitySelectMembersItemDecoration(
                    resources.getDimensionPixelSize(R.dimen.amity_eighteen),
                    resources.getDimensionPixelSize(R.dimen.amity_padding_m1)
                )
            )
        }
        disposable.add(mViewModel.getAllUsers().doOnError {

        }.subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { list ->
                mMemberListAdapter.submitData(lifecycle, list, mViewModel.selectedMemberSet)
            })
    }

    private fun setUpSearchRecyclerView() {
        mSearchResultAdapter = AmitySearchResultAdapter(this)
        binding.rvSearchResults.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = mSearchResultAdapter
            addItemDecoration(
                AmityRecyclerViewItemDecoration(
                    resources.getDimensionPixelSize(R.dimen.amity_padding_m1)
                )
            )
            (itemAnimator as SimpleItemAnimator).supportsChangeAnimations = false
        }
        mSearchResultAdapter.addLoadStateListener { loadState ->
            if (loadState.source.refresh is LoadState.NotLoading) {
                prepareSearchMap()
                mViewModel.isSearchUser.set(mSearchResultAdapter.itemCount > 0)
            }
        }
    }

    override fun onMemberClicked(member: AmityUser, position: Int) {
        val selectMemberItem = AmitySelectMemberItem(
            member.getUserId(),
            member.getAvatar()?.getUrl(AmityImage.Size.MEDIUM) ?: "",
            member.getDisplayName()
                ?: getString(R.string.amity_anonymous), member.getDescription(), false
        )
        if (mViewModel.selectedMemberSet.contains(member.getUserId())) {
            mViewModel.prepareSelectedMembersList(selectMemberItem, false)
            mViewModel.selectedMemberSet.remove(member.getUserId())
            updateListOnSelection(member.getUserId())
        } else {
            mViewModel.selectedMemberSet.add(member.getUserId())
            mViewModel.prepareSelectedMembersList(selectMemberItem, true)
            updateListOnSelection(member.getUserId())
            binding.rvSelectedMembers.scrollToPosition(mViewModel.selectedMembersList.size - 1)
        }
    }

    override fun onMemberRemoved(memberAmity: AmitySelectMemberItem) {
        mViewModel.prepareSelectedMembersList(memberAmity, false)
        mViewModel.selectedMemberSet.remove(memberAmity.id)
        updateListOnSelection(memberAmity.id)
    }

    private fun updateListOnSelection(id: String) {
        mSelectedMembersAdapter.submitList(mViewModel.selectedMembersList)
        val position = mViewModel.memberMap[id]
        if (position != null) {
            mMemberListAdapter.notifyChange(position, mViewModel.selectedMemberSet)
        }

        if (mSearchResultAdapter.itemCount > 0 && mViewModel.searchMemberMap[id] != null) {
            mSearchResultAdapter.notifyChange(
                mViewModel.searchMemberMap[id]!!,
                mViewModel.selectedMemberSet
            )
        }
        setToolBarState()
    }

    private fun loadFilteredList() {
        searchDisposable.clear()
        searchDisposable.add(
            mViewModel.searchUser { list ->
                mSearchResultAdapter.submitData(lifecycle, list, mViewModel.selectedMemberSet)
            }.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe()
        )
    }

    private fun prepareSearchMap() {
        mSearchResultAdapter.snapshot().forEachIndexed { index, user ->
            user?.getUserId()?.let { userId ->
                mViewModel.searchMemberMap[userId] = index
            }
        }
    }

    fun finishActivity(isCancel: Boolean) {
        val finishIntent = Intent()
        if (isCancel) {
            finishIntent.putParcelableArrayListExtra(
                AmityConstants.MEMBERS_LIST,
                receivedMembersList
            )
            requireActivity().setResult(Activity.RESULT_OK, finishIntent)
        } else {
            finishIntent.putParcelableArrayListExtra(
                AmityConstants.MEMBERS_LIST,
                mViewModel.selectedMembersList
            )
            requireActivity().setResult(Activity.RESULT_OK, finishIntent)
        }
        requireActivity().finish()
    }

    override fun onDestroy() {
        if (!disposable.isDisposed) {
            disposable.dispose()
        }
        super.onDestroy()
    }

    class Builder internal constructor() {
        private var selectedMembersList: ArrayList<AmitySelectMemberItem>? = null

        fun build(): AmityMemberPickerFragment {
            return AmityMemberPickerFragment().apply {
                arguments = Bundle().apply {
                    putParcelableArrayList(ARG_MEMBERS_LIST, selectedMembersList)
                }
            }
        }

        fun selectedMembers(list: ArrayList<AmitySelectMemberItem>): Builder {
            selectedMembersList = list
            return this
        }
    }
}