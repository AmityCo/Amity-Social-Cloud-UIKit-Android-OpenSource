package com.amity.socialcloud.uikit.community.compose.event.setup

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity

class AmityEventSetupPageActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val mode: AmityEventSetupPageMode =
            intent?.extras?.getParcelable(EXTRA_MODE) ?: AmityEventSetupPageMode.Create()

        setContent {
            AmityEventSetupPage(
                mode = mode,
            )
        }
    }

    companion object {
        private const val EXTRA_MODE = "mode"

        fun newIntent(
            context: Context,
            mode: AmityEventSetupPageMode = AmityEventSetupPageMode.Create(),
        ): Intent {
            return Intent(
                context,
                AmityEventSetupPageActivity::class.java
            ).apply {
                putExtra(EXTRA_MODE, mode)
            }
        }
    }
}
