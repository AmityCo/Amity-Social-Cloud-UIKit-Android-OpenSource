package com.amity.socialcloud.uikit.community.newsfeed.fragment

import android.view.LayoutInflater
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.amity.socialcloud.sdk.core.user.AmityUser
import com.amity.socialcloud.sdk.social.community.AmityCommunity
import com.amity.socialcloud.sdk.social.feed.AmityPost
import com.amity.socialcloud.uikit.community.newsfeed.events.AmityFeedRefreshEvent
import com.amity.socialcloud.uikit.community.newsfeed.events.PostEngagementClickEvent
import com.amity.socialcloud.uikit.community.newsfeed.listener.AmityCommunityClickListener
import com.amity.socialcloud.uikit.community.newsfeed.listener.AmityPostItemListener
import com.amity.socialcloud.uikit.community.newsfeed.listener.AmityUserClickListener
import com.amity.socialcloud.uikit.community.newsfeed.viewmodel.AmitySinglePostViewModel
import com.amity.socialcloud.uikit.feed.settings.AmityPostShareClickListener
import com.amity.socialcloud.uikit.social.AmitySocialUISettings
import com.ekoapp.rxlifecycle.extension.untilLifecycleEnd
import io.reactivex.Flowable


class AmitySinglePostFragment : AmityFeedFragment() {

    override val showProgressBarOnLaunched = false

    override fun getViewModel(): AmitySinglePostViewModel {
        return ViewModelProvider(requireActivity()).get(AmitySinglePostViewModel::class.java)
    }

    override fun getEmptyView(inflater: LayoutInflater): View {
        return View(requireContext())
    }

    override fun getItemDecorations(): List<RecyclerView.ItemDecoration> {
        return listOf()
    }

    override fun navigateToPostDetails(postId: String) {
        // do not navigate
    }

    override fun deletePost(post: AmityPost) {
        getViewModel().deletePost(
            post = post,
            onSuccess = {
                backPressFragment()
            },
            onError = {
                // display error
            }
        )
            .untilLifecycleEnd(this)
            .subscribe()
    }

    override fun approvePost(post: AmityPost) {
        super.approvePost(post)
        backPressFragment()
    }

    override fun declinePost(post: AmityPost) {
        super.declinePost(post)
        backPressFragment()
    }

    override fun observePostEngagementClickEvents() {
        getViewModel().getPostEngagementClickEvents(
            onReceivedEvent = {
                when (it) {
                    is PostEngagementClickEvent.Reaction -> {
                        getViewModel().postReactionEventMap.put(it.post.getPostId(), it)
                    }
                    is PostEngagementClickEvent.Comment -> {
                        getViewModel().postItemListener.onClickComment(it.post, this)
                    }
                    is PostEngagementClickEvent.Sharing -> {
                        showSharingOptions(it.post)
                    }
                }
            }
        )
            .untilLifecycleEnd(this)
            .subscribe()
    }

    class Builder internal constructor() {

        lateinit var postId: String
        private var userClickListener: AmityUserClickListener? = null
        private var communityClickListener: AmityCommunityClickListener? = null
        private var postShareClickListener: AmityPostShareClickListener = AmitySocialUISettings.postShareClickListener
        private var postItemListener: AmityPostItemListener = object : AmityPostItemListener {
            override fun onClickComment(post: AmityPost, fragment: Fragment) {

            }
        }
        private var feedRefreshEvents = Flowable.never<AmityFeedRefreshEvent>()

        fun build(activity: AppCompatActivity): AmitySinglePostFragment {
            val fragment = AmitySinglePostFragment()
            val viewModel = ViewModelProvider(activity).get(AmitySinglePostViewModel::class.java)
            if (postId.isNotEmpty()) {
                viewModel.postId = postId
            }
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
            viewModel.postItemListener = postItemListener
            viewModel.feedRefreshEvents = feedRefreshEvents
            return fragment
        }

        fun userClickListener(userClickListener: AmityUserClickListener): Builder {
            return apply { this.userClickListener = userClickListener }
        }

        fun postShareClickListener(postShareClickListener: AmityPostShareClickListener): Builder {
            return apply { this.postShareClickListener = postShareClickListener }
        }

        internal fun postId(postId : String) : Builder {
            return apply {
                this.postId = postId
            }
        }

        internal fun postItemListener(postItemListener: AmityPostItemListener) : Builder {
            return apply {  this.postItemListener = postItemListener }
        }

        internal fun feedRefreshEvents(feedRefreshEvents: Flowable<AmityFeedRefreshEvent>): Builder {
            return apply {  this.feedRefreshEvents = feedRefreshEvents }
        }

    }

    companion object {

        fun newInstance(postId: String): Builder {
            return Builder().postId(postId)
        }

    }

}