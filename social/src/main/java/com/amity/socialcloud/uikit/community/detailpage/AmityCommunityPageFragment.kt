package com.amity.socialcloud.uikit.community.detailpage

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.paging.ExperimentalPagingApi
import com.amity.socialcloud.sdk.model.social.community.AmityCommunity
import com.amity.socialcloud.sdk.model.social.community.AmityCommunityPostSettings
import com.amity.socialcloud.sdk.model.social.story.AmityStory
import com.amity.socialcloud.uikit.common.base.AmityFragmentStateAdapter
import com.amity.socialcloud.uikit.common.common.setSafeOnClickListener
import com.amity.socialcloud.uikit.common.common.views.dialog.bottomsheet.AmityBottomSheetDialog
import com.amity.socialcloud.uikit.common.common.views.dialog.bottomsheet.BottomSheetMenuItem
import com.amity.socialcloud.uikit.community.R
import com.amity.socialcloud.uikit.community.compose.AmitySocialBehaviorHelper
import com.amity.socialcloud.uikit.community.databinding.AmityFragmentCommunityPageBinding
import com.amity.socialcloud.uikit.community.newsfeed.activity.AmityLiveStreamPostCreatorActivity
import com.amity.socialcloud.uikit.community.newsfeed.activity.AmityPollPostCreatorActivity
import com.amity.socialcloud.uikit.community.newsfeed.activity.AmityPostCreatorActivity
import com.amity.socialcloud.uikit.community.newsfeed.activity.AmityPostDetailsActivity
import com.amity.socialcloud.uikit.community.newsfeed.events.AmityFeedRefreshEvent
import com.amity.socialcloud.uikit.community.newsfeed.fragment.AmityCommunityFeedFragment
import com.amity.socialcloud.uikit.community.newsfeed.fragment.AmityMediaGalleryFragment
import com.amity.socialcloud.uikit.community.newsfeed.model.AmityMediaGalleryTarget
import com.amity.socialcloud.uikit.community.setting.AmityCommunitySettingsActivity
import com.amity.socialcloud.uikit.community.ui.view.AmityCommunityCustomizeDialogFragment
import com.ekoapp.rxlifecycle.extension.untilLifecycleEnd
import com.google.android.material.appbar.AppBarLayout
import com.trello.rxlifecycle4.components.support.RxFragment
import io.reactivex.rxjava3.core.BackpressureStrategy
import io.reactivex.rxjava3.subjects.PublishSubject

class AmityCommunityPageFragment : RxFragment(),
    AppBarLayout.OnOffsetChangedListener {

    private var isCreateCommunity: Boolean = false

    private lateinit var binding: AmityFragmentCommunityPageBinding
    private lateinit var viewModel: AmityCommunityDetailViewModel
    private lateinit var fragmentStateAdapter: AmityFragmentStateAdapter
    private var refreshEventPublisher = PublishSubject.create<AmityFeedRefreshEvent>()

    private val behavior by lazy {
        AmitySocialBehaviorHelper.storyTabComponentBehavior
    }

    private val createGenericPost =
        registerForActivityResult(AmityPostCreatorActivity.AmityCreateCommunityPostActivityContract()) {
            refreshFeed()
        }

    private val createLiveStreamPost =
        registerForActivityResult(AmityLiveStreamPostCreatorActivity.AmityCreateLiveStreamPostActivityContract()) {
            it?.let {
                val intent = AmityPostDetailsActivity.newIntent(requireContext(), it, null, null)
                startActivity(intent)
            }
            refreshFeed()
        }

    private val createPollPost =
        registerForActivityResult(AmityPollPostCreatorActivity.AmityPollCreatorActivityContract()) {
            refreshFeed()
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel =
            ViewModelProvider(requireActivity()).get(AmityCommunityDetailViewModel::class.java)
        arguments?.let {
            isCreateCommunity = it.getBoolean(ARG_IS_CREATE_COMMUNITY)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.amity_fragment_community_page,
            container,
            false
        )

        return binding.root
    }

    @ExperimentalPagingApi
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpView()
        observeCommunity()
        // TODO move to community creationn flow
        if (isCreateCommunity) {
            showCommunityCustomizeDialog()
        }

    }

    private fun observeCommunity() {
        viewModel.getCommunity {
            if ((it.isJoined() && it.getPostSettings() != AmityCommunityPostSettings.ADMIN_CAN_POST_ONLY)
                || viewModel.hasManageStoryPermission) {
                binding.fabCreatePost.visibility = View.VISIBLE
            } else {
                binding.fabCreatePost.visibility = View.GONE
            }
        }.untilLifecycleEnd(this)
            .subscribe()
    }

    override fun onPause() {
        super.onPause()
        binding.appBar.removeOnOffsetChangedListener(this)
    }

    override fun onResume() {
        super.onResume()
        binding.appBar.addOnOffsetChangedListener(this)
    }

    override fun onOffsetChanged(appBarLayout: AppBarLayout?, verticalOffset: Int) {
        binding.refreshLayout.isEnabled = (verticalOffset == 0)
    }

    private fun setUpView() {
        setUpProfile()
        setUpTabLayout()
        setUpViewListeners()
    }

    private fun setUpProfile() {
        binding.appBar.setExpanded(true)
        val communityProfileFragment =
            AmityCommunityProfileFragment.newInstance(viewModel.communityId!!)
                .build(requireActivity() as AppCompatActivity)
        childFragmentManager.beginTransaction()
            .replace(R.id.profile_container, communityProfileFragment)
            .commit()
    }

    private fun setUpTabLayout() {
        fragmentStateAdapter = AmityFragmentStateAdapter(childFragmentManager, this.lifecycle)
        fragmentStateAdapter.setFragmentList(
            arrayListOf(
                AmityFragmentStateAdapter.AmityPagerModel(
                    requireActivity().getString(R.string.amity_timeline),
                    getFeedFragment(viewModel.communityId!!)
                ),
                AmityFragmentStateAdapter.AmityPagerModel(
                    getString(R.string.amity_gallery_title),
                    getPostGalleryFragment()
                )
            )
        )

        binding.ccDetailTab.setAdapter(fragmentStateAdapter)
        binding.ccDetailTab.disableSwipe()
    }

    private fun setUpViewListeners() {
        binding.refreshLayout.setColorSchemeResources(R.color.amityColorPrimary)
        binding.refreshLayout.setOnRefreshListener {
            refreshCommunity()
        }

        binding.fabCreatePost.setSafeOnClickListener {
            navigateToCreatePost()
        }
    }

    private fun showCommunityCustomizeDialog() {
        val customizeDialog = AmityCommunityCustomizeDialogFragment.newInstance()
        customizeDialog.show(childFragmentManager, AmityCommunityCustomizeDialogFragment.TAG)
        customizeDialog.setDialogListener(object :
            AmityCommunityCustomizeDialogFragment.AmityCommunityCustomizeDialogFragmentListener {
            override fun onClickCommunitySettingsButton() {
                navigateToCommunitySettings()
            }

            override fun onClickSkipButton() {
                //do nothing
            }
        })
    }

    private fun navigateToCommunitySettings() {
        val intent = AmityCommunitySettingsActivity.newIntent(
            requireContext(),
            viewModel.communityId
        )
        startActivity(intent)
    }

    private fun navigateToCreatePost() {
        val bottomSheet = AmityBottomSheetDialog(requireContext())
        val postCreationOptions =
            mutableListOf(
                BottomSheetMenuItem(
                    iconResId = R.drawable.amity_ic_post_create,
                    titleResId = R.string.amity_post,
                    action = {
                        createGenericPost.launch(viewModel.communityId)
                        bottomSheet.dismiss()
                    }
                ),
                BottomSheetMenuItem(
                    iconResId = R.drawable.ic_amity_ic_live_stream_create,
                    titleResId = R.string.amity_video_stream_title,
                    action = {
                        createLiveStreamPost.launch(viewModel.communityId)
                        bottomSheet.dismiss()
                    }
                ),
                BottomSheetMenuItem(
                    iconResId = R.drawable.ic_amity_ic_poll_create,
                    titleResId = R.string.amity_general_poll,
                    action = {
                        createPollPost.launch(viewModel.communityId)
                        bottomSheet.dismiss()
                    }
                )
            )

        if (viewModel.hasManageStoryPermission) {
            postCreationOptions.add(
                index = 1,
                BottomSheetMenuItem(
                    iconResId = R.drawable.amity_ic_story_create,
                    titleResId = R.string.amity_story,
                    action = {
                        behavior.goToCreateStoryPage(
                            context = requireContext(),
                            targetId = viewModel.communityId ?: "",
                            targetType = AmityStory.TargetType.COMMUNITY,
                        )
                        bottomSheet.dismiss()
                    }
                )
            )
        }
        bottomSheet.show(postCreationOptions)
    }


    private fun refreshCommunity() {
        binding.refreshLayout.isRefreshing = true
        refreshProfile()
        refreshFeed()
        Handler(Looper.getMainLooper()).postDelayed({
            binding.refreshLayout.isRefreshing = false
        }, 1000)
    }

    private fun refreshProfile() {
        childFragmentManager.fragments.forEach { fragment ->
            when (fragment) {
                is AmityCommunityProfileFragment -> {
                    fragment.refresh()
                }
            }
        }
    }

    private fun refreshFeed() {
        childFragmentManager.fragments.forEach { fragment ->
            when (fragment) {
                is AmityCommunityFeedFragment -> {
                    refreshEventPublisher.onNext(AmityFeedRefreshEvent())
                }
            }
        }
    }

    private fun getFeedFragment(communityId: String): Fragment {
        return AmityCommunityFeedFragment.newInstance(communityId)
            .feedRefreshEvents(refreshEventPublisher.toFlowable(BackpressureStrategy.BUFFER))
            .build(activity as AppCompatActivity)
    }

    private fun getPostGalleryFragment(): Fragment {
        return AmityMediaGalleryFragment.newInstance()
            .build(AmityMediaGalleryTarget.COMMUNITY(viewModel.communityId ?: ""))
    }

    class Builder internal constructor() {
        private lateinit var communityId: String
        private var communityCreated = false

        fun build(activity: AppCompatActivity): AmityCommunityPageFragment {
            val fragment = AmityCommunityPageFragment().apply {
                arguments = Bundle().apply {
                    putBoolean(ARG_IS_CREATE_COMMUNITY, communityCreated)
                }
            }
            val viewModel =
                ViewModelProvider(activity).get(AmityCommunityDetailViewModel::class.java)
            viewModel.communityId = communityId
            return fragment
        }

        internal fun createCommunitySuccess(value: Boolean): Builder {
            communityCreated = value
            return this
        }

        internal fun communityId(communityId: String): Builder {
            this.communityId = communityId
            return this
        }

    }

    companion object {

        @Deprecated("Use communityId instead")
        fun newInstance(community: AmityCommunity): Builder {
            return Builder().communityId(community.getCommunityId())
        }

        fun newInstance(communityId: String): Builder {
            return Builder().communityId(communityId)
        }
    }

}

private const val ARG_IS_CREATE_COMMUNITY = "ARG_IS_CREATE_COMMUNITY"
