package com.amity.socialcloud.uikit.community.compose.socialhome.components

import com.amity.socialcloud.uikit.community.compose.community.setup.AmityCommunitySetupPageActivity
import com.amity.socialcloud.uikit.community.compose.community.setup.AmityCommunitySetupPageMode

open class AmitySocialHomeTopNavigationComponentBehavior {

    class Context(val componentContext: android.content.Context)

    open fun goToCreateCommunityPage(
        context: Context,
    ) {
        val intent = AmityCommunitySetupPageActivity.newIntent(
            context = context.componentContext,
            mode = AmityCommunitySetupPageMode.Create
        )
        context.componentContext.startActivity(intent)
    }
}