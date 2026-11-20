package com.amity.socialcloud.uikit.community.compose.ui.components.feed

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.amity.socialcloud.sdk.model.core.file.AmityImage
import com.amity.socialcloud.uikit.common.ui.elements.AmityMenuButton
import com.amity.socialcloud.uikit.common.R as CommonR
import com.amity.socialcloud.uikit.community.compose.localization.amitySocialString
import com.amity.socialcloud.uikit.common.ui.theme.amityMediaSurface
import com.amity.socialcloud.uikit.common.ui.theme.amityColorWhite
import com.amity.socialcloud.uikit.common.ui.theme.amityColorBlack

/**
 * One image in a profile or community Images tab grid. The grid items register these with
 * [AmityImageFeedContainer], so the viewer can swipe through every loaded image (PDT-7237).
 */
class AmityImageFeedViewerEntry(
    val image: AmityImage?,
    val postId: String?,
    val productTagCount: Int,
    val onBottomSheetRequest: (() -> Unit)?,
)

/**
 * The grid images in the order the grid composes them. Grid items join on composition and
 * leave on disposal, so the list follows the loaded paging items.
 */
class AmityImageFeedViewerRegistry {
    private val keys = androidx.compose.runtime.mutableStateListOf<Any>()
    private val entries = androidx.compose.runtime.mutableStateMapOf<Any, AmityImageFeedViewerEntry>()

    fun register(key: Any, entry: AmityImageFeedViewerEntry) {
        if (!entries.containsKey(key)) keys.add(key)
        entries[key] = entry
    }

    fun unregister(key: Any) {
        keys.remove(key)
        entries.remove(key)
    }

    val orderedEntries: List<AmityImageFeedViewerEntry>
        get() = keys.mapNotNull { entries[it] }
}

val LocalAmityImageFeedViewerRegistry = staticCompositionLocalOf<AmityImageFeedViewerRegistry?> { null }

@Composable
fun AmityImageFeedContainer(
    availablePostIds: Set<String> = emptySet(), // Pass current available post IDs
    onProductTagClick: ((String) -> Unit)? = null, // Pass postId when product tag clicked
    content: @Composable (
        openDialog: (AmityImage?, String?, Int, onBottomSheetRequest: (() -> Unit)?) -> Unit
    ) -> Unit
) {
    // Tuple4: image, postId, productTagCount, onBottomSheetRequest
    var dialogData by remember { mutableStateOf<Array<Any?>?>(null) }
    val registry = remember { AmityImageFeedViewerRegistry() }

    CompositionLocalProvider(LocalAmityImageFeedViewerRegistry provides registry) {
        content { image, postId, productTagCount, onBottomSheetRequest ->
            dialogData = arrayOf(image, postId, productTagCount, onBottomSheetRequest)
        }
    }

    dialogData?.let { data ->
        val image = data[0] as? AmityImage
        val postId = data[1] as? String
        val productTagCount = data[2] as? Int ?: 0
        val onBottomSheetRequest = data[3] as? (() -> Unit)

        // Check if the current dialog's post still exists in the available post IDs
        val isItemDeleted = postId != null &&
                           availablePostIds.isNotEmpty() &&
                           !availablePostIds.contains(postId)

        if (isItemDeleted) {
            ImageNotAvailableDialog(
                onDismiss = {
                    dialogData = null
                }
            )
        } else {
            // Every loaded grid image, so the viewer shows "n / total" and swipes through the
            // grid. A deleted post leaves the grid, so its image leaves the viewer too.
            val entries = registry.orderedEntries
                .filter { it.postId == null || availablePostIds.isEmpty() || availablePostIds.contains(it.postId) }
                .ifEmpty { listOf(AmityImageFeedViewerEntry(image, postId, productTagCount, onBottomSheetRequest)) }
            val initialIndex = remember(dialogData) {
                entries.indexOfFirst {
                    it.postId == postId && it.image?.getFileId() == image?.getFileId()
                }.coerceAtLeast(0)
            }
            AmityProfileImageFeedPagerPreviewDialog(
                entries = entries,
                initialIndex = initialIndex,
                onProductTagClick = if (onProductTagClick != null) {
                    { entry -> entry.postId?.let(onProductTagClick) }
                } else null,
            ) {
                dialogData = null
            }
        }
    }
}

@Composable
private fun ImageNotAvailableDialog(
    onDismiss: () -> Unit
) {
    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(
            usePlatformDefaultWidth = false
        ),
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(amityMediaSurface)
        ) {
            Column(
                modifier = Modifier
                    .align(Alignment.Center)
                    .padding(32.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Icon(
                    painter = painterResource(id = CommonR.drawable.amity_ic_image_not_available),
                    contentDescription = "Image Not Available",
                    tint = amityColorWhite,
                    modifier = Modifier.size(60.dp)
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = amitySocialString("amity_social_label_this_photo_is_no_longer_available"),
                    style = TextStyle(
                        fontSize = 15.sp,
                        lineHeight = 20.sp,
                        fontWeight = FontWeight(400),
                        color = amityColorWhite,
                        textAlign = TextAlign.Center,
                    )
                )
            }

            AmityMenuButton(
                icon = CommonR.drawable.amity_ic_close2,
                size = 32.dp,
                iconPadding = 10.dp,
                tint = amityColorBlack.copy(0.5f),
                background = amityColorWhite.copy(0.8f),
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .padding(16.dp),
                onClick = onDismiss
            )
        }
    }
}
