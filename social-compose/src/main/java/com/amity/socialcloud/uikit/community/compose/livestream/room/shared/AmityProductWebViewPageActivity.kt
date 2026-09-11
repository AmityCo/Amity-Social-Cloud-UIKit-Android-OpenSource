package com.amity.socialcloud.uikit.community.compose.livestream.room.shared

import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.ui.Modifier
import com.amity.socialcloud.sdk.model.core.product.AmityProduct
import com.amity.socialcloud.uikit.common.ui.theme.AmityTheme
import com.amity.socialcloud.uikit.community.compose.livestream.room.util.AmityPipSessionRegistry

/**
 * Hosts the product web view as a page of its own, launched from a livestream player.
 *
 * Being a separate Activity is the point. Android system Picture-in-Picture only engages when
 * an Activity loses the foreground, so a product page presented *inside* the player — as the
 * modal bottom sheet still does everywhere else — could never leave the stream floating; it
 * needed a hand-drawn mini-player that only worked in that one window. Opening the product as
 * its own Activity makes the tap an ordinary navigation, so the OS floats the stream exactly
 * as it does when the viewer leaves for any other page.
 *
 * Only the two PiP-capable players use this. Every other product surface (feeds, carousels,
 * post details, room creation) keeps [AmityProductWebViewBottomSheet], where there is no
 * playback to preserve and a sheet is the lighter presentation.
 */
class AmityProductWebViewPageActivity : AppCompatActivity() {

    /** The livestream post whose floating window should be restored when this page closes. */
    private var ownerPostId: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val product = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getParcelableExtra(EXTRA_PRODUCT, AmityProduct::class.java)
        } else {
            @Suppress("DEPRECATION")
            intent.getParcelableExtra(EXTRA_PRODUCT) as? AmityProduct
        }
        ownerPostId = intent.getStringExtra(EXTRA_OWNER_POST_ID)

        if (product == null || product.getProductUrl().isBlank()) {
            // Nothing to show; leaving the viewer on a blank page would be worse than not
            // having navigated at all.
            finish()
            return
        }

        setContent {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(AmityTheme.colors.background)
                    // The host draws edge to edge and the web view has no inset handling of
                    // its own, so without this its header sits under the status bar.
                    .statusBarsPadding()
                    .navigationBarsPadding()
            ) {
                AmityProductWebViewComponent(
                    product = product,
                    onDismiss = { finish() },
                    modifier = Modifier.fillMaxSize(),
                )
            }
        }
    }

    /**
     * Leaving this page returns the viewer to the stream at full size rather than to whatever
     * sits behind it. Back out of a product page means "I'm done shopping, back to the show",
     * and the floating window is the thing they came from.
     */
    override fun finish() {
        AmityPipSessionRegistry.expandExisting(this, ownerPostId)
        super.finish()
    }

    companion object {
        private const val EXTRA_PRODUCT = "EXTRA_PRODUCT"
        private const val EXTRA_OWNER_POST_ID = "EXTRA_OWNER_POST_ID"

        fun newIntent(
            context: Context,
            product: AmityProduct,
            ownerPostId: String?,
        ): Intent {
            return Intent(context, AmityProductWebViewPageActivity::class.java).apply {
                putExtra(EXTRA_PRODUCT, product)
                putExtra(EXTRA_OWNER_POST_ID, ownerPostId)
            }
        }
    }
}
