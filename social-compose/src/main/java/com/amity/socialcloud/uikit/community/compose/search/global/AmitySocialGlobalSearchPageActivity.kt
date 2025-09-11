package com.amity.socialcloud.uikit.community.compose.search.global

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.ui.Modifier

class AmitySocialGlobalSearchPageActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val prefilledText = intent.getStringExtra(EXTRA_PARAM_PREFILLED_TEXT)

        setContent {
            AmitySocialGlobalSearchPage(
                modifier = Modifier
                    .statusBarsPadding()
                    .systemBarsPadding(),
                prefilledText = prefilledText
            )
        }
    }

    companion object {

        private const val EXTRA_PARAM_PREFILLED_TEXT = "prefilled_text"

        fun newIntent(context: Context, prefilledText: String? = null): Intent {
            return Intent(
                context,
                AmitySocialGlobalSearchPageActivity::class.java
            ).apply {
                if(prefilledText != null) {
                    putExtra(EXTRA_PARAM_PREFILLED_TEXT, prefilledText)
                }
            }
        }
    }
}