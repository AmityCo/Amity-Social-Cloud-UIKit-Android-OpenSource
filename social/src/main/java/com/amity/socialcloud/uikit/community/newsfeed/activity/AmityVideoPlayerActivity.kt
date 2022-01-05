package com.amity.socialcloud.uikit.community.newsfeed.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.amity.socialcloud.uikit.community.R
import com.amity.socialcloud.uikit.community.newsfeed.fragment.AmitySimpleVideoPlayerFragment

internal class AmityVideoPlayerActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.amity_activity_social_video_player)
        intent.getStringExtra(KEY_VIDEO_URL)?.let { nonNullVideoUrl ->
            val fragmentManager = supportFragmentManager
            val fragmentTransaction = fragmentManager.beginTransaction()
            val fragment = AmitySimpleVideoPlayerFragment.newInstance(nonNullVideoUrl).build(this)
            fragmentTransaction.replace(R.id.video_container, fragment)
            fragmentTransaction.commit()
        }
    }

    companion object {
        fun newIntent(context: Context, videoUrl: String): Intent {
            return Intent(
                context,
                AmityVideoPlayerActivity::class.java
            ).apply {
                putExtra(KEY_VIDEO_URL, videoUrl)
            }
        }
    }
}

private const val KEY_VIDEO_URL = "AMITY_KEY_VIDEO_URL"
