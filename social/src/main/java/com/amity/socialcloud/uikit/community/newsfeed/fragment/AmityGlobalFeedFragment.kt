package com.amity.socialcloud.uikit.community.newsfeed.fragment

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import com.amity.socialcloud.sdk.core.user.AmityUser
import com.amity.socialcloud.sdk.social.community.AmityCommunity
import com.amity.socialcloud.uikit.common.model.AmityEventIdentifier
import com.amity.socialcloud.uikit.community.databinding.AmityViewGlobalFeedEmptyBinding
import com.amity.socialcloud.uikit.community.home.fragments.AmityCommunityHomeViewModel
import com.amity.socialcloud.uikit.community.newsfeed.events.AmityFeedRefreshEvent
import com.amity.socialcloud.uikit.community.newsfeed.listener.AmityCommunityClickListener
import com.amity.socialcloud.uikit.community.newsfeed.listener.AmityUserClickListener
import com.amity.socialcloud.uikit.community.newsfeed.viewmodel.AmityGlobalFeedViewModel
import com.amity.socialcloud.uikit.community.ui.view.AmityCommunityCreatorActivity
import com.amity.socialcloud.uikit.feed.settings.AmityPostShareClickListener
import com.amity.socialcloud.uikit.social.AmitySocialUISettings
import io.reactivex.Flowable

class AmityGlobalFeedFragment : AmityFeedFragment() {

    private val communityHomeViewModel: AmityCommunityHomeViewModel by activityViewModels()

    override fun getViewModel(): AmityGlobalFeedViewModel {
        return ViewModelProvider(requireActivity()).get(AmityGlobalFeedViewModel::class.java)
    }

    override fun getEmptyView(inflater: LayoutInflater): View {
        val binding = AmityViewGlobalFeedEmptyBinding.inflate(
            inflater,
            requireView().parent as ViewGroup,
            false
        )
        binding.btnExplore.setOnClickListener {
            communityHomeViewModel.triggerEvent(AmityEventIdentifier.EXPLORE_COMMUNITY)
        }
        binding.tvCreateCommunity.setOnClickListener {
            val intent = Intent(requireContext(), AmityCommunityCreatorActivity::class.java)
            startActivity(intent)
        }
        return binding.root
    }

    class Builder internal constructor() {
        private var userClickListener: AmityUserClickListener? = null
        private var communityClickListener: AmityCommunityClickListener? = null
        private var postShareClickListener: AmityPostShareClickListener =
            AmitySocialUISettings.postShareClickListener
        private var feedRefreshEvents = Flowable.never<AmityFeedRefreshEvent>()

        fun build(activity: AppCompatActivity): AmityGlobalFeedFragment {
            val fragment = AmityGlobalFeedFragment()
            val viewModel = ViewModelProvider(activity).get(AmityGlobalFeedViewModel::class.java)
            if (userClickListener == null) {
                userClickListener = object : AmityUserClickListener {
                    override fun onClickUser(user: AmityUser) {
                        AmitySocialUISettings.globalUserClickListener.onClickUser(fragment, user)
                    }
                }
            }
            viewModel.userClickListener = userClickListener!!

            if (communityClickListener == null) {
                communityClickListener = object : AmityCommunityClickListener {
                    override fun onClickCommunity(community: AmityCommunity) {
                        AmitySocialUISettings.globalCommunityClickListener.onClickCommunity(
                            fragment,
                            community
                        )
                    }
                }
            }
            viewModel.communityClickListener = communityClickListener!!
            viewModel.postShareClickListener = postShareClickListener
            viewModel.feedRefreshEvents = feedRefreshEvents
            return fragment
        }

        fun userClickListener(userClickListener: AmityUserClickListener): Builder {
            return apply { this.userClickListener = userClickListener }
        }

        fun postShareClickListener(postShareClickListener: AmityPostShareClickListener): Builder {
            return apply { this.postShareClickListener = postShareClickListener }
        }

        fun feedRefreshEvents(feedRefreshEvents: Flowable<AmityFeedRefreshEvent>): Builder {
            return apply { this.feedRefreshEvents = feedRefreshEvents }
        }

    }

    companion object {

        fun newInstance(): Builder {
            return Builder()
        }
    }

}