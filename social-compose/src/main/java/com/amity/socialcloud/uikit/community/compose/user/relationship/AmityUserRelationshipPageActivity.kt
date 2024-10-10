package com.amity.socialcloud.uikit.community.compose.user.relationship

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity

class AmityUserRelationshipPageActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val userId = intent.getStringExtra(EXTRA_PARAM_USER_ID) ?: ""
        val tab = intent.getSerializableExtra(EXTRA_PARAM_TAB) as AmityUserRelationshipPageTab

        setContent {
            AmityUserRelationshipPage(
                userId = userId,
                selectedTab = tab,
            )
        }
    }

    companion object {
        private const val EXTRA_PARAM_USER_ID = "user_id"
        private const val EXTRA_PARAM_TAB = "tab"

        fun newIntent(
            context: Context,
            userId: String,
            selectedTab: AmityUserRelationshipPageTab,
        ): Intent {
            return Intent(
                context,
                AmityUserRelationshipPageActivity::class.java
            ).apply {
                putExtra(EXTRA_PARAM_USER_ID, userId)
                putExtra(EXTRA_PARAM_TAB, selectedTab)
            }
        }
    }
}