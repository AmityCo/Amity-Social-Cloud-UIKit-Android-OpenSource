package com.amity.socialcloud.uikit.community.compose.target.poll


import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.ui.Modifier
import com.amity.socialcloud.uikit.community.compose.target.AmityPostTargetSelectionPageType

class AmityPollTargetSelectionPageActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            AmityPollTargetSelectionPage(
                modifier = Modifier
                    .statusBarsPadding()
                    .systemBarsPadding(),
            )
        }
    }

    companion object {
        private const val EXTRA_PARAM_SELECTION_TYPE = "selection_type"

        fun newIntent(
            context: Context,
        ): Intent {

            return Intent(
                context,
                AmityPollTargetSelectionPageActivity::class.java
            ).apply {
                putExtra(EXTRA_PARAM_SELECTION_TYPE, type)
            }
        }
    }
}