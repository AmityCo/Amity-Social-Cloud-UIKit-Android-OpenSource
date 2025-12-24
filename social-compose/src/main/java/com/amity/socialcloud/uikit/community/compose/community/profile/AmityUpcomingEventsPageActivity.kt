package com.amity.socialcloud.uikit.community.compose.community.profile

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.amity.socialcloud.uikit.community.compose.event.detail.AmityEventDetailPageActivity

/**
 * Activity for displaying all upcoming events.
 * Use the companion object's newIntent() method to start this activity.
 */
class AmityUpcomingEventsPageActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val showAllEvents = intent.getBooleanExtra(EXTRA_SHOW_ALL_EVENTS, true)

        setContent {
            AmityUpcomingEventsPage(
                showAllEvents = showAllEvents,
                onBackClick = { finish() },
                onEventClick = { event ->
                    startActivity(AmityEventDetailPageActivity.newIntent(this, event.getEventId()))
                }
            )
        }
    }

    companion object {
        private const val EXTRA_SHOW_ALL_EVENTS = "extra_show_all_events"

        /**
         * Creates an intent to launch AmityUpcomingEventsPageActivity
         *
         * @param context The context to create the intent from
         * @param showAllEvents If true, shows all upcoming events. If false, shows only user's events.
         * @return Intent to start AmityUpcomingEventsPageActivity
         */
        fun newIntent(context: Context, showAllEvents: Boolean = true): Intent {
            return Intent(context, AmityUpcomingEventsPageActivity::class.java).apply {
                putExtra(EXTRA_SHOW_ALL_EVENTS, showAllEvents)
            }
        }
    }
}
