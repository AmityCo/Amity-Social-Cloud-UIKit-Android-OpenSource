package com.amity.snipet.verifier.social.socialhome

import android.content.Context
import com.amity.socialcloud.uikit.community.compose.socialhome.AmitySocialV4CompatibleFragment
import com.amity.socialcloud.uikit.community.home.activity.AmityCommunityHomePageActivity

class AmitySocialV4CompatibleSample {
    /* begin_sample_code
    gist_id: 7bf4cc7ae486b91de5b029ea632d476d
    filename: AmitySocialV4CompatibleSample.kt
    asc_page: https://docs.amity.co/amity-uikit/uikit-v4-beta/
    description: Amity Social V4 compatible sample
    */
    fun createSocialV4CompatibleFragment(): AmitySocialV4CompatibleFragment {
        return AmitySocialV4CompatibleFragment.newInstance().build()
    }

    fun startAnActivity(context: Context) {
        val intent = AmityCommunityHomePageActivity.newIntent(
            context = context,
            useNewsFeedV4 = true
        )
        context.startActivity(intent)
    }
    /* end_sample_code */
}