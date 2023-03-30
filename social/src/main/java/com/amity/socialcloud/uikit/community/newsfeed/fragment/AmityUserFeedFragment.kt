package com.amity.socialcloud.uikit.community.newsfeed.fragment

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.amity.socialcloud.sdk.model.core.error.AmityError
import com.amity.socialcloud.sdk.model.core.user.AmityUser
import com.amity.socialcloud.sdk.model.social.community.AmityCommunity
import com.amity.socialcloud.uikit.community.databinding.AmityViewOtherUserTimelineEmptyBinding
import com.amity.socialcloud.uikit.community.databinding.AmityViewPrivateUserProfileBinding
import com.amity.socialcloud.uikit.community.newsfeed.events.AmityFeedRefreshEvent
import com.amity.socialcloud.uikit.community.newsfeed.listener.AmityCommunityClickListener
import com.amity.socialcloud.uikit.community.newsfeed.listener.AmityUserClickListener
import com.amity.socialcloud.uikit.community.newsfeed.viewmodel.AmityUserFeedViewModel
import com.amity.socialcloud.uikit.feed.settings.AmityPostShareClickListener
import com.amity.socialcloud.uikit.social.AmitySocialUISettings
import io.reactivex.rxjava3.core.Flowable

class AmityUserFeedFragment : AmityFeedFragment() {

    override fun getViewModel(): AmityUserFeedViewModel {
        return ViewModelProvider(requireActivity()).get(AmityUserFeedViewModel::class.java)
    }

    override fun getEmptyView(inflater: LayoutInflater): View {
        val binding = AmityViewOtherUserTimelineEmptyBinding.inflate(
            inflater,
            requireView().parent as ViewGroup,
            false
        )
        return binding.root
    }

    private fun getPrivateProfileView(inflater: LayoutInflater): View {
        val binding = AmityViewPrivateUserProfileBinding.inflate(
            inflater,
            requireView().parent as ViewGroup,
            false
        )
        return binding.root
    }

    override fun handleErrorState(error: AmityError) {
        if (error == AmityError.PERMISSION_DENIED) {
            val inflater =
                requireContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            handleEmptyState(getPrivateProfileView(inflater))
        } else {
            super.handleErrorState(error)
        }
    }

    class Builder internal constructor() {
        lateinit var userId: String
        private var userClickListener: AmityUserClickListener? = null
        private var communityClickListener: AmityCommunityClickListener? = null
        private var postShareClickListener: AmityPostShareClickListener =
            AmitySocialUISettings.postShareClickListener
        private var feedRefreshEvents = Flowable.never<AmityFeedRefreshEvent>()

        fun build(activity: AppCompatActivity): AmityUserFeedFragment {
            val fragment = AmityUserFeedFragment()
            val viewModel = ViewModelProvider(activity).get(AmityUserFeedViewModel::class.java)
            viewModel.userId = userId
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

        internal fun userId(userId: String): Builder {
            this.userId = userId
            return this
        }

        internal fun user(user: AmityUser): Builder {
            this.userId = user.getUserId()
            return this
        }
    }

    companion object {
        fun newInstance(userId: String): Builder {
            return Builder().userId(userId)
        }

        fun newInstance(user: AmityUser): Builder {
            return Builder().user(user)
        }
    }

}