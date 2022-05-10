package com.amity.socialcloud.uikit.community.members

import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.activityViewModels
import androidx.viewpager2.widget.ViewPager2
import com.amity.socialcloud.sdk.core.error.AmityException
import com.amity.socialcloud.sdk.social.community.AmityCommunity
import com.amity.socialcloud.uikit.common.base.AmityBaseFragment
import com.amity.socialcloud.uikit.common.base.AmityFragmentStateAdapter
import com.amity.socialcloud.uikit.common.contract.AmityPickMemberContract
import com.amity.socialcloud.uikit.common.utils.AmityAlertDialogUtil
import com.amity.socialcloud.uikit.common.utils.AmityConstants
import com.amity.socialcloud.uikit.community.R
import com.amity.socialcloud.uikit.community.databinding.AmityFragmentCommunityMemberSettingsBinding
import com.ekoapp.rxlifecycle.extension.untilLifecycleEnd
import timber.log.Timber

private const val ARG_COMMUNITY_ID = "ARG_COMMUNITY_ID"
private const val ARG_IS_MEMBER = "ARG_IS_MEMBER"
private const val ARG_IS_COMMUNITY = "ARG_COMMUNITY"

class AmityCommunityMemberSettingsFragment : AmityBaseFragment() {

    private lateinit var fragmentStateAdapter: AmityFragmentStateAdapter
    private lateinit var binding: AmityFragmentCommunityMemberSettingsBinding
    private val viewModel: AmityCommunityMembersViewModel by activityViewModels()
    private lateinit var memberFragment: AmityMembersFragment
    private lateinit var modFragment: AmityModeratorsFragment
    private val selectMembers = registerForActivityResult(AmityPickMemberContract()) {
        viewModel.handleAddRemoveMembers(it, onMembersAdded = {
            viewModel.getCommunityMembers({}, {}, {})
                .subscribe()
        }, onFailed = {

        })
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.communityId = arguments?.getString(ARG_COMMUNITY_ID) ?: ""
        viewModel.isJoined.set(arguments?.getBoolean(ARG_IS_MEMBER) ?: false)
        fragmentStateAdapter = AmityFragmentStateAdapter(
            childFragmentManager,
            requireActivity().lifecycle
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = AmityFragmentCommunityMemberSettingsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.addRemoveErrorData.observe(requireActivity(), {
            handleNoPermissionError(it)
        })
        setUpToolbar()
        setUpTabLayout()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val pageChangeCallback = object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                if (position == 0) {
                    memberFragment.refresh()
                } else {
                    modFragment.refresh()
                }
            }
        }
        binding.membersTabLayout.setPageChangeListener(pageChangeCallback)
    }

    private fun setUpToolbar() {
        (activity as AppCompatActivity).supportActionBar?.title =
            getString(R.string.amity_members_capital)
        viewModel.checkModeratorPermission { granted ->
            setHasOptionsMenu(granted)
            viewModel.isModerator.set(granted)
        }
            .untilLifecycleEnd(this)
            .subscribe()
    }

    private fun setUpTabLayout() {
        memberFragment = AmityMembersFragment.newInstance()
        modFragment = AmityModeratorsFragment.newInstance()
        fragmentStateAdapter.setFragmentList(
            arrayListOf(
                AmityFragmentStateAdapter.AmityPagerModel(
                    getString(R.string.amity_members_capital),
                    memberFragment
                ),
                AmityFragmentStateAdapter.AmityPagerModel(
                    getString(R.string.amity_moderators),
                    modFragment
                )
            )
        )

        binding.membersTabLayout.setAdapter(fragmentStateAdapter)
    }

    private fun handleNoPermissionError(exception: Throwable) {
        if (exception is AmityException) {
            if (exception.code == AmityConstants.NO_PERMISSION_ERROR_CODE) {
                AmityAlertDialogUtil.showNoPermissionDialog(requireContext()) { dialog, _ ->
                    dialog?.dismiss()
                    requireActivity().finish()
                }
            } else {
                Timber.e(exception)
            }
        } else {
            Timber.e(exception)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        val drawable = ContextCompat.getDrawable(requireContext(), R.drawable.amity_ic_add)
        menu.add(Menu.NONE, 1, Menu.NONE, getString(R.string.amity_add))
            ?.setIcon(drawable)
            ?.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        selectMembers.launch(viewModel.selectMembersList)
        return super.onOptionsItemSelected(item)
    }

    class Builder internal constructor() {
        private var communityId = ""
        private var isMember = false
        private var community: AmityCommunity? = null

        fun build(): AmityCommunityMemberSettingsFragment {
            return AmityCommunityMemberSettingsFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_COMMUNITY_ID, communityId)
                    putBoolean(ARG_IS_MEMBER, isMember)
                    putParcelable(ARG_IS_COMMUNITY, community)
                }
            }
        }

        internal fun communityId(id: String): Builder {
            communityId = id
            return this
        }

        fun isMember(value: Boolean): Builder {
            isMember = value
            return this
        }
    }

    companion object {
        fun newInstance(communityId: String): Builder {
            return Builder().communityId(communityId)
        }
    }
}