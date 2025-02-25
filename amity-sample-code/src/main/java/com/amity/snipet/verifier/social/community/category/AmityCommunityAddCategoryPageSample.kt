package com.amity.snipet.verifier.social.community.category

import androidx.compose.runtime.Composable
import com.amity.socialcloud.sdk.model.social.category.AmityCommunityCategory
import com.amity.socialcloud.uikit.community.compose.community.category.AmityCommunityAddCategoryPage

class AmityCommunityAddCategoryPageSample {
    /* begin_sample_code
    gist_id: 34b598fb4fef625eff8f95e14d9bff10
    filename: AmityCommunityAddCategoryPageSample.kt
    asc_page: https://docs.amity.co/amity-uikit/uikit-v4-beta/
    description: Community Add Category Page sample
    */
    @Composable
    fun composeCommunityAddCategoryPage(
        categories: List<AmityCommunityCategory>,
    ) {
        AmityCommunityAddCategoryPage(
            categories = categories,
            onAddedAction = {

            }
        )
    }
    /* end_sample_code */
}