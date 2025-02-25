package com.amity.snipet.verifier.social.socialhome

import androidx.compose.runtime.Composable
import com.amity.socialcloud.uikit.community.compose.socialhome.AmitySocialHomePageTab
import com.amity.socialcloud.uikit.community.compose.socialhome.components.AmitySocialHomeTopNavigationComponent

class AmitySocialHomeTopNavigationComponent {
    /* begin_sample_code
    gist_id: cbb8964c6c2236427f47792df81dbf14
    filename: AmitySocialHomeTopNavigationComponent.kt
    asc_page: https://docs.amity.co/amity-uikit/uikit-v4-beta/
    description: Amity Social Home Top Navigation component
    */
    @Composable
    fun composeSocialHomeTopNavigationComponent() {
        AmitySocialHomeTopNavigationComponent(
            selectedTab = AmitySocialHomePageTab.NEWSFEED,
            searchButtonAction = {},
        )
    }
    /* end_sample_code */
}