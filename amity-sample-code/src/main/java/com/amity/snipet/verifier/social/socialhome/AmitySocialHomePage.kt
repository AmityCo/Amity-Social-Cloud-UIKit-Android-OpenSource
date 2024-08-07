package com.amity.snipet.verifier.social.socialhome

import android.content.Context
import android.content.Intent
import androidx.compose.runtime.Composable
import com.amity.socialcloud.uikit.community.compose.socialhome.AmitySocialHomePage
import com.amity.socialcloud.uikit.community.compose.socialhome.AmitySocialHomePageActivity

class AmitySocialHomePage {
    /* begin_sample_code
    gist_id: 8ff2b1bbde1ba699e8f78d77c5fb2e7a
    filename: AmitySocialHomePage.kt
    asc_page: https://docs.amity.co/amity-uikit/uikit-v4-beta/
    description: Amity Social Home Page
    */
    @Composable
    fun composeSocialHomePage() {
        AmitySocialHomePage()
    }

    fun startAnActivity(context: Context) {
        val intent = Intent(
            context,
            AmitySocialHomePageActivity::class.java
        )
        context.startActivity(intent)
    }
    /* end_sample_code */
}
