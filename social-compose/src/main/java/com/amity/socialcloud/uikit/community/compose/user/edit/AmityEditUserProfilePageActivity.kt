package com.amity.socialcloud.uikit.community.compose.user.edit

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity

class AmityEditUserProfilePageActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            AmityEditUserProfilePage()
        }
    }

    companion object {
        fun newIntent(
            context: Context,
        ): Intent {
            return Intent(
                context,
                AmityEditUserProfilePageActivity::class.java
            )
        }
    }
}