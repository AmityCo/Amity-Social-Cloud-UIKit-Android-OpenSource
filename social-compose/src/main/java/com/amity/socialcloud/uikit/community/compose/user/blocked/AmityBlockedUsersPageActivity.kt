package com.amity.socialcloud.uikit.community.compose.user.blocked

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity

class AmityBlockedUsersPageActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            AmityBlockedUsersPage()
        }
    }

    companion object {
        fun newIntent(
            context: Context,
        ): Intent {
            return Intent(
                context,
                AmityBlockedUsersPageActivity::class.java
            )
        }
    }
}