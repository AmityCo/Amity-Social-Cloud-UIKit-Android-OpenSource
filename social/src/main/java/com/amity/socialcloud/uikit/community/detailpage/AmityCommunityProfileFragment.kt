package com.amity.socialcloud.uikit.community.detailpage

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.graphics.BlendModeColorFilterCompat
import androidx.core.graphics.BlendModeCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.paging.ExperimentalPagingApi
import com.amity.socialcloud.sdk.model.core.file.AmityImage
import com.amity.socialcloud.sdk.model.social.community.AmityCommunity
import com.amity.socialcloud.sdk.model.social.feed.AmityFeedType
import com.amity.socialcloud.uikit.common.common.formatCount
import com.amity.socialcloud.uikit.common.common.readableNumber
import com.amity.socialcloud.uikit.community.R
import com.amity.socialcloud.uikit.community.compose.story.target.AmityStoryTabComponent
import com.amity.socialcloud.uikit.community.compose.story.target.AmityStoryTabComponentType
import com.amity.socialcloud.uikit.community.data.PostReviewBannerData
import com.amity.socialcloud.uikit.community.databinding.AmityFragmentCommunityProfileBinding
import com.amity.socialcloud.uikit.community.edit.AmityCommunityProfileActivity
import com.amity.socialcloud.uikit.community.members.AmityCommunityMemberSettingsActivity
import com.amity.socialcloud.uikit.community.newsfeed.activity.AmityCommunityReviewingFeedActivity
import com.amity.socialcloud.uikit.community.setting.AmityCommunitySettingsActivity
import com.amity.socialcloud.uikit.community.utils.EXTRA_PARAM_POST_ID
import com.bumptech.glide.Glide
import com.ekoapp.rxlifecycle.extension.untilLifecycleEnd
import com.trello.rxlifecycle4.components.support.RxFragment
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers
import timber.log.Timber
import java.util.UUID

class AmityCommunityProfileFragment : RxFragment() {

    private val TAG = AmityCommunityProfileFragment::class.java.canonicalName
    private lateinit var binding: AmityFragmentCommunityProfileBinding
    private lateinit var viewModel: AmityCommunityProfileViewModel
    private var menuItem: MenuItem? = null
    private var shouldShowOptionsMenu = false
    private var communityProfileComposer = UUID.randomUUID().toString()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.amity_fragment_community_profile,
            container,
            false
        )
        viewModel =
            ViewModelProvider(requireActivity()).get(AmityCommunityProfileViewModel::class.java)
        binding.viewModel = viewModel
        return binding.root
    }

    @ExperimentalPagingApi
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpViewListeners()
        observeCommunity()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        if (shouldShowOptionsMenu) {
            val drawable =
                ContextCompat.getDrawable(requireContext(), R.drawable.amity_ic_more_horiz)
            drawable?.mutate()
            drawable?.colorFilter = BlendModeColorFilterCompat.createBlendModeColorFilterCompat(
                R.color.amityColorBlack, BlendModeCompat.SRC_ATOP
            )
            menuItem = menu.add(Menu.NONE, 1, Menu.NONE, getString(R.string.amity_cancel))
            menuItem?.setIcon(drawable)?.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS)
        }

        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        navigateToCommunitySettings()
        return super.onOptionsItemSelected(item)
    }

    private fun navigateToCommunitySettings() {
        val intent = AmityCommunitySettingsActivity.newIntent(
            requireContext(),
            viewModel.communityId
        )
        startActivity(intent)
    }

    private fun setUpViewListeners() {
        binding.btnJoin.setOnClickListener {
            joinCommunity()
        }
        binding.buttonEditProfile.setOnClickListener {
            navigateToEditProfile()
        }
        binding.layoutMemberCount.setOnClickListener {
            navigateToMembersPage()
        }
    }

    private fun navigateToEditProfile() {
        val intent = AmityCommunityProfileActivity.newIntent(
            requireContext(),
            viewModel.communityId!!
        )
        startActivity(intent)
    }

    private fun navigateToMembersPage() {
        val intent = AmityCommunityMemberSettingsActivity.newIntent(
            requireContext(),
            viewModel.communityId!!,
            true
        )
        startActivity(intent)
    }

    @ExperimentalPagingApi
    private fun observeCommunity() {
        viewModel.observeCommunity(
            onLoaded = {
                val community = it.community
                if (community.isDeleted()) {
                    requireActivity().finish()
                }
                renderOptionsMenu(community)
                renderCommunityData(community)
                renderActionButton(community, it.canEditCommunity)
                renderPostReviewBanner(it.postReviewBanner)
                renderStory(community)
            }
        )
            .untilLifecycleEnd(this)
            .subscribe()


    }

    private fun renderOptionsMenu(community: AmityCommunity) {
        shouldShowOptionsMenu = community.isJoined()
        menuItem?.isVisible = shouldShowOptionsMenu
        (requireActivity() as AppCompatActivity).invalidateOptionsMenu()
    }

    private fun renderCommunityData(community: AmityCommunity) {

        Glide.with(this)
            .load(community.getAvatar()?.getUrl(AmityImage.Size.LARGE) ?: "")
            .centerCrop()
            .into(binding.ivAvatar)

        binding.tvName.text = community.getDisplayName().trim()
        val leftDrawable = if (community.isPublic()) {
            null
        } else {
            ContextCompat.getDrawable(requireContext(), R.drawable.amity_ic_lock1)
        }
        val rightDrawable = if (community.isOfficial()) {
            ContextCompat.getDrawable(requireContext(), R.drawable.amity_ic_verified)
        } else {
            null
        }
        binding.tvName.setCompoundDrawablesWithIntrinsicBounds(
            leftDrawable,
            null,
            rightDrawable,
            null
        )

        val category = community.getCategories().joinToString(separator = " ") { it.getName() }
            .takeIf { it.isNotEmpty() }

        binding.tvCategory.text = category ?: getString(R.string.amity_general)

        community.getPostCount(AmityFeedType.PUBLISHED)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnNext {
                binding.tvPostCount.text = it.toDouble().formatCount()
            }
            .doOnError {
                Timber.e(it)
            }
            .untilLifecycleEnd(this, communityProfileComposer)
            .subscribe()

        binding.tvMemberCount.text = community.getMemberCount().toDouble().formatCount()
        val description = community.getDescription().trim()
        if (description.isEmpty()) {
            binding.tvDescription.visibility = View.GONE
        } else {
            binding.tvDescription.text = description
            binding.tvDescription.visibility = View.VISIBLE
        }

    }

    private fun renderActionButton(community: AmityCommunity, canEditCommunity: Boolean) {
        if (community.isJoined()) {
            binding.btnJoin.visibility = View.GONE
        } else {
            binding.btnJoin.visibility = View.VISIBLE
        }
        if (canEditCommunity) {
            binding.buttonEditProfile.visibility = View.VISIBLE
        } else {
            binding.buttonEditProfile.visibility = View.GONE
        }
    }

    private fun renderStory(community: AmityCommunity) {
        binding.cvStoryTarget.setContent {
            AmityStoryTabComponent(
                type = AmityStoryTabComponentType.CommunityFeed(
                    communityId = community.getCommunityId()
                )
            )
        }
    }

    private fun joinCommunity() {
        viewModel.joinCommunity()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnError {
                Timber.e(TAG, it)
            }
            .untilLifecycleEnd(this)
            .subscribe()
    }

    private fun renderPostReviewBanner(data: PostReviewBannerData) {
        if (data.isVisible) {
            binding.layoutPendingPostBanner.visibility = View.VISIBLE
            if (data.isReviewer) {
                val reviewerMessage = binding.root.resources.getQuantityString(
                    R.plurals.amity_community_banner_number_of_pending_posts,
                    data.postCount,
                    data.postCount.readableNumber()
                )
                binding.textviewBannerDescription.text = reviewerMessage
            } else {
                binding.textviewBannerDescription.text =
                    getString(R.string.amity_pending_posts_banner_member_message)
            }
            binding.layoutPendingPostBanner.setOnClickListener {
                val intent =
                    Intent(requireContext(), AmityCommunityReviewingFeedActivity::class.java)
                intent.putExtra(EXTRA_PARAM_POST_ID, viewModel.communityId)
                startActivity(intent)
            }
        } else {
            binding.layoutPendingPostBanner.visibility = View.GONE
        }
    }

    internal fun refresh() {
        viewModel.refreshCommunity()
    }

    class Builder internal constructor() {
        private lateinit var communityId: String

        fun build(activity: AppCompatActivity): AmityCommunityProfileFragment {
            val viewModel =
                ViewModelProvider(activity).get(AmityCommunityProfileViewModel::class.java)
            viewModel.communityId = communityId
            return AmityCommunityProfileFragment()
        }

        internal fun communityId(communityId: String): Builder {
            this.communityId = communityId
            return this
        }

    }

    companion object {

        fun newInstance(communityId: String): Builder {
            return Builder().communityId(communityId)
        }
    }

}