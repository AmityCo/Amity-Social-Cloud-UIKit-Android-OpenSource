package com.amity.socialcloud.uikit.community.compose.community.profile

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.amity.socialcloud.uikit.community.compose.event.detail.AmityEventDetailPageActivity

/**
 * Activity for displaying all past events.
 * Use the companion object's newIntent() method to start this activity.
 */
class AmityPastEventsPageActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            AmityPastEventsPage(
                onBackClick = { finish() },
                onEventClick = { event ->
                    startActivity(AmityEventDetailPageActivity.newIntent(this, event.getEventId()))
                }
            )
        }
    }

    companion object {
        /**
         * Creates an intent to launch AmityPastEventsPageActivity
         *
         * @param context The context to create the intent from
         * @return Intent to start AmityPastEventsPageActivity
         */
        fun newIntent(context: Context): Intent {
            return Intent(context, AmityPastEventsPageActivity::class.java)
        }
    }
}
