package com.amity.snipet.verifier.social.community

import androidx.compose.runtime.Composable
import com.amity.socialcloud.uikit.community.compose.community.bycategory.AmityCommunitiesByCategoryPage


class AmityCommunitiesByCategoryPageSample {
    /* begin_sample_code
    gist_id: 61a5adb590f9d3f74b301a77dacd717a
    filename: AmityCommunitiesByCategoryPageSample.kt
    asc_page: https://docs.amity.co/amity-uikit/uikit-v4-beta/
    description: Communities By Category Page sample
    */
    @Composable
    fun composeCommunitiesByCatgoryPage(
        categoryId: String
    ) {
        AmityCommunitiesByCategoryPage(categoryId = categoryId)
    }

}
