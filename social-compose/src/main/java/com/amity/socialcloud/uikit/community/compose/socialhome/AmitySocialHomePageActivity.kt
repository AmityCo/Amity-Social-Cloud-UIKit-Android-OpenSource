package com.amity.socialcloud.uikit.community.compose.socialhome

import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.ui.Modifier
import com.amity.socialcloud.uikit.community.compose.livestream.errorhandling.AmityLivestreamDeletedPageActivity
import com.amity.socialcloud.uikit.community.compose.livestream.errorhandling.AmityLivestreamTerminatedPageActivity
import com.amity.socialcloud.uikit.community.compose.livestream.util.LivestreamErrorScreenType
import com.amity.socialcloud.uikit.community.compose.livestream.util.LivestreamScreenType
import com.amity.socialcloud.uikit.community.compose.post.detail.AmityPostDetailPageActivity.Companion.EXTRA_PARAM_LIVESTREAM_ERROR_TYPE
import com.amity.socialcloud.uikit.community.compose.post.detail.AmityPostDetailPageActivity.Companion.REQUEST_CODE_VIEW_LIVESTREAM

class AmitySocialHomePageActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            AmitySocialHomePage(
                modifier = Modifier
                    .statusBarsPadding()
                    .systemBarsPadding(),
            )
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE_VIEW_LIVESTREAM && resultCode == RESULT_OK) {
            val data = data?.getStringExtra(EXTRA_PARAM_LIVESTREAM_ERROR_TYPE) ?: ""
            val errorType = LivestreamErrorScreenType.fromString(data)

            when (errorType) {
                LivestreamErrorScreenType.TERMINATED -> {
                    startActivity(
                        AmityLivestreamTerminatedPageActivity.newIntent(
                            context = this,
                            screenType = LivestreamScreenType.WATCH
                        )
                    )
                }

                LivestreamErrorScreenType.DELETED -> {
                    startActivity(AmityLivestreamDeletedPageActivity.newIntent(context = this))
                }

                else -> {}
            }
        }
    }
}