package com.amity.socialcloud.uikit.sample

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import com.amity.socialcloud.uikit.common.ui.base.AmityBasePage
import com.amity.socialcloud.uikit.community.compose.community.category.component.AmityCommunityCategoriesComponent
import com.amity.socialcloud.uikit.community.compose.community.trending.AmityTrendingCommunitiesComponent


class AmityPlaygroundActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //enableEdgeToEdge()
        setContent {
            //
            AmityBasePage(pageId = "community_add_category_page") {
                Column(modifier = Modifier.fillMaxSize()) {
                    AmityCommunityCategoriesComponent()
                    AmityTrendingCommunitiesComponent()

                }
            }


        }
    }
}
