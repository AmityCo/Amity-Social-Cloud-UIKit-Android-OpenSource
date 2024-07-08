package com.amity.socialcloud.uikit.community.compose.target.story

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.ui.Modifier
import com.amity.socialcloud.uikit.community.compose.target.AmityTargetSelectionPageType

class AmityStoryTargetSelectionPageActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val type =
            intent?.getSerializableExtra(EXTRA_PARAM_SELECTION_TYPE) as AmityTargetSelectionPageType

        setContent {
            AmityStoryTargetSelectionPage(
                modifier = Modifier
                    .statusBarsPadding()
                    .systemBarsPadding(),
                type = type,
            )
        }
    }

    companion object {
        private const val EXTRA_PARAM_SELECTION_TYPE = "selection_type"

        fun newIntent(
            context: Context,
            type: AmityTargetSelectionPageType,
        ): Intent {

            return Intent(
                context,
                AmityStoryTargetSelectionPageActivity::class.java
            ).apply {
                putExtra(EXTRA_PARAM_SELECTION_TYPE, type)
            }
        }
    }
}