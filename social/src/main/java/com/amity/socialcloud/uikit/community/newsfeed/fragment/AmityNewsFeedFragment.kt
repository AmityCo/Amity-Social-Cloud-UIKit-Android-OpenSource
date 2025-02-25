package com.amity.socialcloud.uikit.community.newsfeed.fragment

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.amity.socialcloud.uikit.common.base.AmityBaseFragment
import com.amity.socialcloud.uikit.common.common.setSafeOnClickListener
import com.amity.socialcloud.uikit.common.common.views.dialog.bottomsheet.AmityBottomSheetDialog
import com.amity.socialcloud.uikit.common.common.views.dialog.bottomsheet.BottomSheetMenuItem
import com.amity.socialcloud.uikit.community.R
import com.amity.socialcloud.uikit.community.compose.story.target.global.AmityStoryGlobalFeedFragment
import com.amity.socialcloud.uikit.community.databinding.AmityFragmentNewsFeedBinding
import com.amity.socialcloud.uikit.community.newsfeed.activity.AmityTargetSelectionPageActivity
import com.amity.socialcloud.uikit.community.newsfeed.activity.AmityTargetSelectionPageType
import com.amity.socialcloud.uikit.community.newsfeed.events.AmityFeedRefreshEvent
import com.google.android.material.appbar.AppBarLayout
import io.reactivex.rxjava3.core.BackpressureStrategy
import io.reactivex.rxjava3.subjects.BehaviorSubject

class AmityNewsFeedFragment : AmityBaseFragment(),
    AppBarLayout.OnOffsetChangedListener {

    private lateinit var binding: AmityFragmentNewsFeedBinding
    private var refreshEventPublisher = BehaviorSubject.create<AmityFeedRefreshEvent>()

    private val creationTargetSelection =
        registerForActivityResult<AmityTargetSelectionPageType, String>(
            AmityTargetSelectionPageActivity.AmityTargetSelectionPageActivityContract()
        ) {
            refreshFeed()
        }

    companion object {
        fun newInstance(): Builder {
            return Builder()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding =
            DataBindingUtil.inflate(inflater, R.layout.amity_fragment_news_feed, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val fragmentTransaction = childFragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.storyFeedContainer, getStoryFeed())
        fragmentTransaction.replace(R.id.globalFeedContainer, getGlobalFeed())
        fragmentTransaction.commit()

        binding.fabCreatePost.setSafeOnClickListener {
            activity?.let { navigateToCreatePost() }
        }

        binding.refreshLayout.setColorSchemeResources(R.color.amityColorPrimary)
        binding.refreshLayout.setOnRefreshListener {
            refreshFeed()
            Handler(Looper.getMainLooper()).postDelayed({
                binding.refreshLayout.isRefreshing = false
            }, 1000)
        }
    }

    override fun onOffsetChanged(appBarLayout: AppBarLayout?, verticalOffset: Int) {
        binding.refreshLayout.isEnabled = (verticalOffset == 0)
    }

    override fun onResume() {
        super.onResume()
        binding.appBar.addOnOffsetChangedListener(this)
    }

    override fun onPause() {
        super.onPause()
        binding.appBar.removeOnOffsetChangedListener(this)
    }

    private fun refreshFeed() {
        childFragmentManager.fragments.forEach { fragment ->
            when (fragment) {
                is AmityGlobalFeedFragment -> {
                    refreshEventPublisher.onNext(AmityFeedRefreshEvent())
                }
            }
        }
    }

    private fun navigateToCreatePost() {
        val bottomSheet = AmityBottomSheetDialog(requireContext())
        val postCreationOptions =
            arrayListOf(
                BottomSheetMenuItem(
                    iconResId = R.drawable.amity_ic_post_create,
                    titleResId = R.string.amity_post,
                    action = {
                        creationTargetSelection.launch(AmityTargetSelectionPageType.POST)
                        bottomSheet.dismiss()
                    }
                ),
                BottomSheetMenuItem(
                    iconResId = R.drawable.amity_ic_story_create,
                    titleResId = R.string.amity_story,
                    action = {
                        creationTargetSelection.launch(AmityTargetSelectionPageType.STORY)
                        bottomSheet.dismiss()
                    }
                ),
                BottomSheetMenuItem(
                    iconResId = R.drawable.ic_amity_ic_live_stream_create,
                    titleResId = R.string.amity_video_stream_title,
                    action = {
                        creationTargetSelection.launch(AmityTargetSelectionPageType.LIVESTREAM)
                        bottomSheet.dismiss()
                    }
                ),
                BottomSheetMenuItem(
                    iconResId = R.drawable.ic_amity_ic_poll_create,
                    titleResId = R.string.amity_general_poll,
                    action = {
                        creationTargetSelection.launch(AmityTargetSelectionPageType.POLL)
                        bottomSheet.dismiss()
                    }
                )
            )
        bottomSheet.show(postCreationOptions)
    }

    private fun getStoryFeed(): Fragment {
        return AmityStoryGlobalFeedFragment.newInstance().build()
    }

    private fun getGlobalFeed(): Fragment {
        return AmityGlobalFeedFragment.Builder()
            .feedRefreshEvents(refreshEventPublisher.toFlowable(BackpressureStrategy.BUFFER))
            .build(activity as AppCompatActivity)
    }

    class Builder internal constructor() {

        fun build(): AmityNewsFeedFragment {
            return AmityNewsFeedFragment()
        }
    }
}