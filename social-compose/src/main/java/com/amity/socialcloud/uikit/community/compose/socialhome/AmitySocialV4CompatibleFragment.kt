package com.amity.socialcloud.uikit.community.compose.socialhome

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.platform.ComposeView
import com.amity.socialcloud.uikit.common.base.AmityBaseFragment
import com.amity.socialcloud.uikit.common.ui.base.AmityBasePage
import com.amity.socialcloud.uikit.community.compose.socialhome.components.AmityNewsFeedComponent

class AmitySocialV4CompatibleFragment : AmityBaseFragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            setContent {
                AmityBasePage(
                    pageId = "social_home_page"
                ) {
                    AmityNewsFeedComponent(
                        pageScope = getPageScope()
                    )
                }
            }
        }
    }

    companion object {
        fun newInstance(): Builder {
            return Builder()
        }
    }

    class Builder internal constructor() {

        fun build(): AmitySocialV4CompatibleFragment {
            return AmitySocialV4CompatibleFragment()
        }
    }
}