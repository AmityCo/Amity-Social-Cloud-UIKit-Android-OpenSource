package com.amity.socialcloud.uikit.community.ui.view

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import com.amity.socialcloud.uikit.common.contract.AmityPickMemberContract
import com.amity.socialcloud.uikit.common.memberpicker.adapter.AmityAddedMembersAdapter
import com.amity.socialcloud.uikit.common.memberpicker.listener.AmityAddedMemberClickListener
import com.amity.socialcloud.uikit.common.model.AmitySelectMemberItem
import com.amity.socialcloud.uikit.community.R
import com.amity.socialcloud.uikit.community.utils.AmityAddedMemberItemDecoration


class AmityCommunityCreatorFragment : AmityCommunityCreateBaseFragment(),
    AmityAddedMemberClickListener {

    private lateinit var addedMembersAdapter: AmityAddedMembersAdapter
    private val selectMembers = registerForActivityResult(AmityPickMemberContract()) { list ->
        viewModel.selectedMembersList.clear()
        viewModel.selectedMembersList.addAll(list ?: arrayListOf())
        setAddMemberVisibility()
        setCount()
        addedMembersAdapter.submitList(viewModel.selectedMembersList)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        addMembers()
        setAddedMemberRecyclerView()
    }

    private fun addMembers() {
        getBindingVariable().ivAdd.setOnClickListener {
            goToAddMembersActivity()
        }
    }

    private fun setAddedMemberRecyclerView() {
        addedMembersAdapter = AmityAddedMembersAdapter(this)
        getBindingVariable().rvAddedMembers.layoutManager = GridLayoutManager(requireContext(), 2)
        getBindingVariable().rvAddedMembers.adapter = addedMembersAdapter
        getBindingVariable().rvAddedMembers.addItemDecoration(
            AmityAddedMemberItemDecoration(
                resources.getDimensionPixelSize(R.dimen.amity_padding_xs),
                resources.getDimensionPixelSize(R.dimen.amity_padding_s)
            )
        )
        addedMembersAdapter.submitList(viewModel.selectedMembersList)
    }

    override fun onAddButtonClicked() {
        goToAddMembersActivity()
    }

    override fun onMemberCountClicked() {
        goToAddMembersActivity()
    }

    override fun onMemberRemoved(itemAmity: AmitySelectMemberItem) {
        val lastItem = viewModel.selectedMembersList[viewModel.selectedMembersList.lastIndex]
        if (lastItem.name == "ADD") {
            viewModel.selectedMembersList.remove(lastItem)
        }
        viewModel.selectedMembersList.remove(itemAmity)
        setAddMemberVisibility()
        setCount()
        addedMembersAdapter.submitList(viewModel.selectedMembersList)
    }

    private fun setCount() {
        if (viewModel.selectedMembersList.size < 8) {
            if (viewModel.selectedMembersList.isNotEmpty()) {
                viewModel.selectedMembersList.add(AmitySelectMemberItem("", "", "ADD"))
            }
        } else {
            viewModel.selectedMembersList[7].subTitle =
                "${viewModel.selectedMembersList.size - 8}"
        }
    }

    private fun goToAddMembersActivity() {

        selectMembers.launch(viewModel.selectedMembersList)
    }

    private fun setAddMemberVisibility() {
        viewModel.addMemberVisible.set(viewModel.selectedMembersList.isEmpty())
    }

    class Builder internal constructor() {
        fun build(): AmityCommunityCreatorFragment {
            return AmityCommunityCreatorFragment()
        }
    }

    companion object {

        fun newInstance(): Builder {
            return Builder()
        }
    }
}