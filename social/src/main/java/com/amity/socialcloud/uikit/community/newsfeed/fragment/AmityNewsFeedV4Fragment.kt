package com.amity.socialcloud.uikit.community.newsfeed.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.amity.socialcloud.uikit.common.base.AmityBaseFragment
import com.amity.socialcloud.uikit.common.common.setSafeOnClickListener
import com.amity.socialcloud.uikit.common.common.views.dialog.bottomsheet.AmityBottomSheetDialog
import com.amity.socialcloud.uikit.common.common.views.dialog.bottomsheet.BottomSheetMenuItem
import com.amity.socialcloud.uikit.community.R
import com.amity.socialcloud.uikit.community.compose.socialhome.AmitySocialV4CompatibleFragment
import com.amity.socialcloud.uikit.community.databinding.AmityFragmentNewsFeedV4Binding
import com.amity.socialcloud.uikit.community.newsfeed.activity.AmityTargetSelectionPageActivity
import com.amity.socialcloud.uikit.community.newsfeed.activity.AmityTargetSelectionPageType
import com.amity.socialcloud.uikit.community.newsfeed.events.AmityFeedRefreshEvent
import io.reactivex.rxjava3.subjects.BehaviorSubject

class AmityNewsFeedV4Fragment : AmityBaseFragment() {

    private lateinit var binding: AmityFragmentNewsFeedV4Binding
    private var refreshEventPublisher = BehaviorSubject.create<AmityFeedRefreshEvent>()

    private val creationTargetSelection =
        registerForActivityResult<AmityTargetSelectionPageType, String>(
            AmityTargetSelectionPageActivity.AmityTargetSelectionPageActivityContract()
        ) {
            refreshFeed()
        }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding =
            DataBindingUtil.inflate(
                inflater,
                R.layout.amity_fragment_news_feed_v4,
                container,
                false
            )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val fragmentTransaction = childFragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.globalFeedContainer, getGlobalFeed())
        fragmentTransaction.commit()

        binding.fabCreatePost.setSafeOnClickListener {
            activity?.let { navigateToCreatePost() }
        }
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
            )
        bottomSheet.show(postCreationOptions)
    }

    private fun getGlobalFeed(): Fragment {
        return AmitySocialV4CompatibleFragment.newInstance().build()
    }

    class Builder internal constructor() {
        fun build(): AmityNewsFeedV4Fragment {
            return AmityNewsFeedV4Fragment()
        }
    }

    companion object {
        fun newInstance(): Builder {
            return Builder()
        }
    }
}