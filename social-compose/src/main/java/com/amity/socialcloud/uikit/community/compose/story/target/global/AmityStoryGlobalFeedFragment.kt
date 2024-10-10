package com.amity.socialcloud.uikit.community.compose.story.target.global

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.platform.ComposeView
import com.amity.socialcloud.uikit.common.base.AmityBaseFragment
import com.amity.socialcloud.uikit.community.compose.story.target.AmityStoryTabComponent
import com.amity.socialcloud.uikit.community.compose.story.target.AmityStoryTabComponentType

class AmityStoryGlobalFeedFragment : AmityBaseFragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            setContent {
                AmityStoryTabComponent(
                    type = AmityStoryTabComponentType.GlobalFeed()
                )
            }
        }
    }

    companion object {
        fun newInstance(): Builder {
            return Builder()
        }
    }

    class Builder internal constructor() {

        fun build(): AmityStoryGlobalFeedFragment {
            return AmityStoryGlobalFeedFragment()
        }
    }
}