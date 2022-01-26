package com.amity.socialcloud.uikit.community.newsfeed.fragment

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.amity.socialcloud.uikit.community.databinding.AmityViewCustomPostRankingFeedEmptyBinding
import com.amity.socialcloud.uikit.community.databinding.AmityViewGlobalFeedEmptyBinding
import com.amity.socialcloud.uikit.community.newsfeed.events.AmityFeedRefreshEvent
import com.amity.socialcloud.uikit.community.newsfeed.viewmodel.AmityCustomPostRankingFeedViewModel
import com.amity.socialcloud.uikit.community.newsfeed.viewmodel.AmityGlobalFeedViewModel
import com.amity.socialcloud.uikit.feed.settings.AmityPostShareClickListener
import com.amity.socialcloud.uikit.social.AmitySocialUISettings
import io.reactivex.Flowable

class AmityCustomPostRankingFeedFragment : AmityFeedFragment() {

    override fun getViewModel(): AmityCustomPostRankingFeedViewModel {
        return ViewModelProvider(requireActivity()).get(AmityCustomPostRankingFeedViewModel::class.java)
    }

    override fun getEmptyView(inflater: LayoutInflater): View {
        val binding = AmityViewCustomPostRankingFeedEmptyBinding.inflate(
            inflater,
            requireView().parent as ViewGroup,
            false
        )
        return binding.root
    }

    class Builder internal constructor() {
        private var postShareClickListener: AmityPostShareClickListener =
            AmitySocialUISettings.postShareClickListener
        private var feedRefreshEvents = Flowable.never<AmityFeedRefreshEvent>()

        fun build(activity: AppCompatActivity): AmityCustomPostRankingFeedFragment {
            val fragment = AmityCustomPostRankingFeedFragment()
            val viewModel = ViewModelProvider(activity).get(AmityGlobalFeedViewModel::class.java)
            viewModel.postShareClickListener = postShareClickListener
            viewModel.feedRefreshEvents = feedRefreshEvents
            return fragment
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