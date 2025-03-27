package com.amity.socialcloud.uikit.community.compose.livestream.create.nopermission

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity

class AmityNoPermissionPageActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            AmityNoPermissionPage()
        }
    }

    companion object {

        fun newIntent(
            context: Context,
        ): Intent {
            return Intent(
                context,
                AmityNoPermissionPageActivity::class.java
            )
        }
    }
}