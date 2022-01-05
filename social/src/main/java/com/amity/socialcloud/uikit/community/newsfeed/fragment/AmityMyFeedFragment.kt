package com.amity.socialcloud.uikit.community.newsfeed.fragment

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.amity.socialcloud.sdk.core.user.AmityUser
import com.amity.socialcloud.sdk.social.community.AmityCommunity
import com.amity.socialcloud.uikit.community.databinding.AmityViewMyTimelineFeedEmptyBinding
import com.amity.socialcloud.uikit.community.newsfeed.events.AmityFeedRefreshEvent
import com.amity.socialcloud.uikit.community.newsfeed.listener.AmityCommunityClickListener
import com.amity.socialcloud.uikit.community.newsfeed.listener.AmityUserClickListener
import com.amity.socialcloud.uikit.community.newsfeed.viewmodel.AmityMyTimelineViewModel
import com.amity.socialcloud.uikit.feed.settings.AmityPostShareClickListener
import com.amity.socialcloud.uikit.social.AmitySocialUISettings
import io.reactivex.Flowable

class AmityMyFeedFragment : AmityFeedFragment() {


    override fun getViewModel(): AmityMyTimelineViewModel {
        return ViewModelProvider(requireActivity()).get(AmityMyTimelineViewModel::class.java)
    }

    override fun getEmptyView(inflater: LayoutInflater): View {
        val binding = AmityViewMyTimelineFeedEmptyBinding.inflate(inflater, requireView().parent as ViewGroup, false)
        return binding.root
    }


    class Builder internal constructor(){
        private var userClickListener: AmityUserClickListener? = null
        private var communityClickListener: AmityCommunityClickListener? = null
        private var postShareClickListener: AmityPostShareClickListener = AmitySocialUISettings.postShareClickListener
        private var feedRefreshEvents = Flowable.never<AmityFeedRefreshEvent>()

        fun build(activity: AppCompatActivity): AmityMyFeedFragment {
            val fragment = AmityMyFeedFragment()
            val viewModel = ViewModelProvider(activity).get(AmityMyTimelineViewModel::class.java)
            if(userClickListener == null) {
                userClickListener = object : AmityUserClickListener {
                    override fun onClickUser(user: AmityUser) {
                        AmitySocialUISettings.globalUserClickListener.onClickUser(fragment, user)
                    }
                }
            }
            viewModel.userClickListener = userClickListener!!

            if(communityClickListener == null) {
                communityClickListener = object : AmityCommunityClickListener {
                    override fun onClickCommunity(community: AmityCommunity) {
                        AmitySocialUISettings.globalCommunityClickListener.onClickCommunity(fragment, community)
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
            return apply {  this.feedRefreshEvents = feedRefreshEvents }
        }

    }

    companion object {

        fun newInstance(): Builder {
            return Builder()
        }
    }
}