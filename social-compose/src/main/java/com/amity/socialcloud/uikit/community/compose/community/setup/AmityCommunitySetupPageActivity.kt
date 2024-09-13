package com.amity.socialcloud.uikit.community.compose.community.setup

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity

class AmityCommunitySetupPageActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val mode: AmityCommunitySetupPageMode =
            intent?.extras?.getParcelable(EXTRA_MODE) ?: return

        setContent {
            AmityCommunitySetupPage(
                mode = mode,
            )
        }
    }

    companion object {
        private const val EXTRA_MODE = "mode"

        fun newIntent(
            context: Context,
            mode: AmityCommunitySetupPageMode,
        ): Intent {
            return Intent(
                context,
                AmityCommunitySetupPageActivity::class.java
            ).apply {
                putExtra(EXTRA_MODE, mode)
            }
        }
    }
}