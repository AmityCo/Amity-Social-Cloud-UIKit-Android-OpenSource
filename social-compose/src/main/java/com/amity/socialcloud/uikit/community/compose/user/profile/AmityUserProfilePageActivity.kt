package com.amity.socialcloud.uikit.community.compose.user.profile

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity

class AmityUserProfilePageActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val userId = intent.getStringExtra(EXTRA_PARAM_USER_ID) ?: ""

        setContent {
            AmityUserProfilePage(userId = userId)
        }
    }

    companion object {
        private const val EXTRA_PARAM_USER_ID = "user_id"

        fun newIntent(
            context: Context,
            userId: String
        ): Intent {
            return Intent(
                context,
                AmityUserProfilePageActivity::class.java
            ).apply {
                putExtra(EXTRA_PARAM_USER_ID, userId)
            }
        }
    }
}