package com.amity.socialcloud.uikit.community.compose.story.target.global

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity

class AmityStoryGlobalFeedActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            AmityStoryGlobalTargetTabComponent()
        }
    }
}