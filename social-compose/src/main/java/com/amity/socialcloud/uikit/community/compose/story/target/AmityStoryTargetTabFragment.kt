package com.amity.socialcloud.uikit.community.compose.story.target

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.media3.common.util.UnstableApi
import com.amity.socialcloud.sdk.model.social.community.AmityCommunity
import com.amity.socialcloud.uikit.common.base.AmityBaseFragment
import com.amity.socialcloud.uikit.community.compose.databinding.AmityFragmentStoryTargetTabBinding

@UnstableApi
class AmityStoryTargetTabFragment(val community: AmityCommunity) : AmityBaseFragment() {

    private lateinit var binding: AmityFragmentStoryTargetTabBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = AmityFragmentStoryTargetTabBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.composeView.setContent {
            AmityStoryTargetTabComponent(community = community)
        }
    }

    companion object {
        fun newInstance(): Builder {
            return Builder()
        }
    }

    class Builder internal constructor() {

        private var community: AmityCommunity? = null

        fun build(): AmityStoryTargetTabFragment {
            return AmityStoryTargetTabFragment(community!!)
        }

        fun community(community: AmityCommunity): Builder {
            this.community = community
            return this
        }
    }
}