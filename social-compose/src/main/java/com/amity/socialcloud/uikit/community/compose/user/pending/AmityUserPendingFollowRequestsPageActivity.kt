package com.amity.socialcloud.uikit.community.compose.user.pending

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity

class AmityUserPendingFollowRequestsPageActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            AmityUserPendingFollowRequestsPage()
        }
    }

    companion object {
        fun newIntent(
            context: Context,
        ): Intent {
            return Intent(
                context,
                AmityUserPendingFollowRequestsPageActivity::class.java
            )
        }
    }
}