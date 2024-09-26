package com.amity.snipet.verifier.social.community

import androidx.compose.runtime.Composable
import com.amity.socialcloud.uikit.community.compose.community.bycategory.AmityCommunitiesByCategoryPage


class AmityCommunitiesByCategoryPageSample {
    /* begin_sample_code
    gist_id: 2237c9d04341d2cd6606c7f2bc2ba43d
    filename: AmityCommunitiesByCategoryPageSample.kt
    asc_page: https://docs.amity.co/amity-uikit/uikit-v4-beta/
    description: Communities by category sample
    */
    @Composable
    fun composeCommunitiesByCatgoryPage(
        categoryId: String
    ) {
        AmityCommunitiesByCategoryPage(categoryId = categoryId)
    }
    /* end_sample_code */
}
