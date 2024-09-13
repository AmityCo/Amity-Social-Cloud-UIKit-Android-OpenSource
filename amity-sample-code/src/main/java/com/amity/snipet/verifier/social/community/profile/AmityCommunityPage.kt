package com.amity.snipet.verifier.social.community.profile

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import com.amity.socialcloud.uikit.community.detailpage.AmityCommunityPageActivity
import com.amity.socialcloud.uikit.community.detailpage.AmityCommunityPageFragment

class AmityCommunityPage {
    /* begin_sample_code
     gist_id: 2f0b741d98d0cd1dd6a83bb3f1479a95
     filename: AmityCommunityPage.kt
     asc_page: https://docs.amity.co/amity-uikit/uikit-v4-beta/social/community-profile-page
     description: Navigate to community page
     */
    fun startAnActivity(context: Context) {
        val intent = Intent(context, AmityCommunityPageActivity::class.java)
        context.startActivity(intent)
    }

    fun createFragment(communityId: String, activity: AppCompatActivity) {
        AmityCommunityPageFragment
            .newInstance(communityId = communityId)
            .build(activity)
    }
    /* end_sample_code */
}