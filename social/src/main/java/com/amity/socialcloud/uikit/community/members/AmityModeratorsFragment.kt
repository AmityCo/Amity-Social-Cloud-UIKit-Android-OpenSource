package com.amity.socialcloud.uikit.community.members

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.paging.PagingData
import androidx.paging.map
import androidx.recyclerview.widget.LinearLayoutManager
import com.amity.socialcloud.sdk.model.core.file.AmityImage
import com.amity.socialcloud.sdk.model.social.member.AmityCommunityMember
import com.amity.socialcloud.uikit.common.base.AmityBaseFragment
import com.amity.socialcloud.uikit.common.common.setShape
import com.amity.socialcloud.uikit.common.common.views.AmityColorShade
import com.amity.socialcloud.uikit.common.model.AmitySelectMemberItem
import com.amity.socialcloud.uikit.common.utils.AmityRecyclerViewItemDecoration
import com.amity.socialcloud.uikit.community.R
import com.amity.socialcloud.uikit.community.databinding.AmityFragmentModeratorsBinding
import com.amity.socialcloud.uikit.community.profile.activity.AmityUserProfileActivity
import com.ekoapp.rxlifecycle.extension.untilLifecycleEnd
import java.util.*


class AmityModeratorsFragment : AmityBaseFragment(), AmityMemberClickListener {
    private val viewModel: AmityCommunityMembersViewModel by activityViewModels()
    private val memberDisposer = UUID.randomUUID().toString()
    private lateinit var moderatorAdapter: AmityCommunityModeratorAdapter
    private lateinit var binding: AmityFragmentModeratorsBinding

    companion object {
        @JvmStatic
        fun newInstance() = AmityModeratorsFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = AmityFragmentModeratorsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.membersSet.clear()
        viewModel.selectMembersList.clear()
        initRecyclerView()
        binding.etSearch.setShape(
            null, null, null, null,
            R.color.amityColorBase, null, AmityColorShade.SHADE4
        )
    }

    private fun initRecyclerView() {
        moderatorAdapter = AmityCommunityModeratorAdapter(requireContext(), this, viewModel)
        binding.rvCommunityModerators.layoutManager = LinearLayoutManager(requireContext())
        binding.rvCommunityModerators.adapter = moderatorAdapter
        binding.rvCommunityModerators.addItemDecoration(
            AmityRecyclerViewItemDecoration(
                requireContext().resources.getDimensionPixelSize(R.dimen.amity_padding_m1)
            )
        )

        getModerators()


    }

    private fun getModerators() {
        viewModel.getCommunityModerators {
            moderatorAdapter.submitData(lifecycle, it)
            prepareSelectedMembersList(it)
        }
            .untilLifecycleEnd(this, memberDisposer)
            .subscribe()
    }

    private fun prepareSelectedMembersList(list: PagingData<AmityCommunityMember>) {
        list.map {
            val ekoUser = it.getUser()
            if (ekoUser != null) {
                val selectMemberItem = AmitySelectMemberItem(
                    ekoUser.getUserId(),
                    ekoUser.getAvatar()?.getUrl(AmityImage.Size.MEDIUM) ?: "",
                    ekoUser.getDisplayName() ?: getString(R.string.amity_anonymous),
                    ekoUser.getDescription(),
                    false
                )
                if (!viewModel.membersSet.contains(selectMemberItem.id)) {
                    viewModel.selectMembersList.add(selectMemberItem)
                    viewModel.membersSet.add(selectMemberItem.id)
                }
            }

        }
    }

    override fun onCommunityMembershipSelected(membership: AmityCommunityMember) {
        val intent = AmityUserProfileActivity.newIntent(requireContext(), membership.getUserId())
        startActivity(intent)
    }

    internal fun refresh() {
        if (isAdded) {
            getModerators()
        }
    }

}