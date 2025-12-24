package com.amity.socialcloud.uikit.community.compose.event.attendees

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import com.amity.socialcloud.uikit.common.ui.theme.AmityComposeTheme

class AmityEventAttendeesPageActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val eventId = intent.getStringExtra(EXTRA_EVENT_ID) ?: return finish()

        setContent {
            AmityComposeTheme {
                AmityEventAttendeesPage(
                    eventId = eventId
                )
            }
        }
    }

    companion object {
        private const val EXTRA_EVENT_ID = "EXTRA_EVENT_ID"

        /**
         * Creates an intent to launch AmityEventAttendeesPageActivity
         * @param context The context to create the intent from
         * @param eventId The ID of the event to show attendees for
         * @return Intent to start AmityEventAttendeesPageActivity
         */
        fun newIntent(context: Context, eventId: String): Intent {
            return Intent(context, AmityEventAttendeesPageActivity::class.java).apply {
                putExtra(EXTRA_EVENT_ID, eventId)
            }
        }
    }
}
