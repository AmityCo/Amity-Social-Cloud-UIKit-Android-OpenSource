package com.amity.socialcloud.uikit.community.compose.event.detail

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity

/**
 * Activity for displaying event details.
 * Use the companion object's newIntent() method to start this activity.
 */
class AmityEventDetailPageActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val eventId = intent.getStringExtra(EXTRA_EVENT_ID) ?: run {
            finish()
            return
        }
        val showSuccessToast = intent.getBooleanExtra(EXTRA_SHOW_SUCCESS_TOAST, false)

        setContent {
            AmityEventDetailPage(
                eventId = eventId,
                showSuccessToast = showSuccessToast,
                onBackClick = { finish() }
            )
        }
    }

    companion object {
        private const val EXTRA_EVENT_ID = "extra_event_id"
        private const val EXTRA_SHOW_SUCCESS_TOAST = "extra_show_success_toast"

        /**
         * Creates an intent to launch AmityEventDetailPageActivity
         *
         * @param context The context to create the intent from
         * @param eventId The ID of the event to display
         * @param showSuccessToast Whether to show "Successfully created event" toast
         * @return Intent to start AmityEventDetailPageActivity
         */
        fun newIntent(context: Context, eventId: String, showSuccessToast: Boolean = false): Intent {
            return Intent(context, AmityEventDetailPageActivity::class.java).apply {
                putExtra(EXTRA_EVENT_ID, eventId)
                putExtra(EXTRA_SHOW_SUCCESS_TOAST, showSuccessToast)
            }
        }
    }
}
