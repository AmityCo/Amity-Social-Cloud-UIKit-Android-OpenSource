package com.amity.socialcloud.uikit.community.compose.community.profile.component

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.platform.LocalContext
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.amity.socialcloud.sdk.api.core.AmityCoreClient
import com.amity.socialcloud.sdk.model.core.file.AmityImage
import com.amity.socialcloud.sdk.model.social.event.AmityEvent
import com.amity.socialcloud.sdk.model.social.event.AmityEventType
import com.amity.socialcloud.uikit.common.ui.theme.AmityTheme
import com.amity.socialcloud.uikit.common.utils.clickableWithoutRipple
import com.amity.socialcloud.uikit.community.compose.R
import com.amity.socialcloud.uikit.community.compose.event.formatEventTimestamp
import org.joda.time.DateTime

/**
 * Composable that wraps the event host badge with a circular background
 */
@Composable
private fun EventHostBadge(
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .background(
                color = Color(0xFFEAE2FF),
                shape = CircleShape
            )
            .padding(3.dp),
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(id = R.drawable.amity_ic_event_host_badge),
            contentDescription = "Host badge"
        )
    }
}


/**
 * EventCardItem displays an event in different layout styles.
 * 
 * @param event The event to display
 * @param style The layout style for the card (Large, Medium, or List)
 * @param onClick Callback when the card is clicked
 */
@Composable
fun EventCardItem(
    event: AmityEvent?,
    style: EventCardStyle,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    when (style) {
        EventCardStyle.Large -> EventCardLarge(
            event = event,
            onClick = onClick,
            modifier = modifier
        )
        EventCardStyle.Medium -> EventCardMedium(
            event = event,
            onClick = onClick,
            modifier = modifier
        )
        EventCardStyle.List -> EventCardList(
            event = event,
            onClick = onClick,
            modifier = modifier
        )
    }
}

/**
 * Defines the available layout styles for event cards
 */
enum class EventCardStyle {
    /** Large card format - 280x280dp with full cover image */
    Large,
    /** Medium card format - similar to Large but potentially with different dimensions */
    Medium,
    /** List item format - horizontal layout with 142x80dp thumbnail */
    List
}

/**
 * Large event card layout (full width with 141dp image)
 * Used for featured/hero sections
 */
@Composable
private fun EventCardLarge(
    event: AmityEvent?,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(
                color = AmityTheme.colors.background,
                shape = RoundedCornerShape(12.dp)
            )
            .clickableWithoutRipple { onClick() }
    ) {
        // Image Section with Badge
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(193.dp)
        ) {
            // Cover Image - use getCoverImage() which handles the coverImageFileId internally
            val coverImageUrl = event?.getCoverImage()?.getUrl(AmityImage.Size.MEDIUM)
            val context = LocalContext.current
            AsyncImage(
                model = ImageRequest.Builder(context)
                    .data(coverImageUrl)
                    .crossfade(true)
                    .build(),
                contentDescription = "Event cover",
                contentScale = ContentScale.Crop,
                placeholder = painterResource(R.drawable.amity_ic_event_list_placeholder),
                error = painterResource(R.drawable.amity_ic_event_list_placeholder),
                modifier = Modifier.fillMaxSize()
            )
            
            // Event Type Badge
            Box(
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .padding(top = 8.dp, start = 8.dp)
                    .background(
                        color = androidx.compose.ui.graphics.Color.Black.copy(alpha = 0.5f),
                        shape = RoundedCornerShape(4.dp)
                    )
                    .padding(horizontal = 6.dp, vertical = 2.dp)
            ) {
                Text(
                    text = when (event?.getType()) {
                        AmityEventType.IN_PERSON -> "In-person"
                        AmityEventType.VIRTUAL -> "Virtual"
                        else -> "Virtual"
                    },
                    style = AmityTheme.typography.caption.copy(
                        fontSize = 12.sp
                    ),
                    color = androidx.compose.ui.graphics.Color.White
                )
            }
            
            // Host Badge
            val currentUserId = AmityCoreClient.getUserId()
            val isHost = event?.getCreator()?.getUserId() == currentUserId
            if (isHost) {
                EventHostBadge(
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(top = 8.dp, end = 8.dp)
                )
            }
        }
        // Event Info Section
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(color = AmityTheme.colors.background)
                .padding(16.dp)
        ) {
            val startTime = event?.getStartTime()
            val endTime = event?.getEndTime()
            
            startTime?.let {
                Text(
                    text = formatEventTimestamp(it, endTime),
                    style = AmityTheme.typography.caption,
                    color = AmityTheme.colors.baseShade1
                )
            }
            
            Spacer(modifier = Modifier.height(6.dp))
            
            Text(
                text = event?.getTitle() ?: "Make Every Minute Count",
                style = AmityTheme.typography.title.copy(fontWeight = FontWeight.Bold),
                color = AmityTheme.colors.base,
                maxLines = 2
            )
            
            Spacer(modifier = Modifier.height(6.dp))
            
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = "By ",
                    style = AmityTheme.typography.body,
                    color = AmityTheme.colors.baseShade1
                )
                Text(
                    text = event?.getCreator()?.getDisplayName() ?: "Community Name",
                    style = AmityTheme.typography.body.copy(fontWeight = FontWeight.Medium),
                    color = AmityTheme.colors.baseShade1
                )
                // TODO: Replace with actual logic - val isBrandCreator = event?.getCreator()?.isBrand() == true
                val isBrandCreator = true // Force true for testing
                if (isBrandCreator) {
                    Image(
                        painter = painterResource(id = R.drawable.amity_ic_brand_badge),
                        contentDescription = "Brand badge",
                        modifier = Modifier.size(18.dp)
                    )
                }
            }
        }
    }
}

/**
 * Medium event card layout (280x280dp)
 * Used for horizontal scrolling sections like "Happening Now"
 */
@Composable
private fun EventCardMedium(
    event: AmityEvent?,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .width(251.dp)
            .height(221.dp)
            .border(
                width = 1.dp,
                color = AmityTheme.colors.baseShade4,
                shape = RoundedCornerShape(8.dp)
            )
            .clip(RoundedCornerShape(8.dp))
            .background(
                color = AmityTheme.colors.background,
                shape = RoundedCornerShape(8.dp)
            )
            .clickableWithoutRipple { onClick() }
    ) {
        // Cover Image - use getCoverImage() which handles the coverImageFileId internally
        val coverImageUrl = event?.getCoverImage()?.getUrl(AmityImage.Size.MEDIUM)
        val context = LocalContext.current
        AsyncImage(
            model = ImageRequest.Builder(context)
                .data(coverImageUrl)
                .crossfade(true)
                .build(),
            contentDescription = "Event cover",
            contentScale = ContentScale.Crop,
            placeholder = painterResource(R.drawable.amity_ic_event_list_placeholder),
            error = painterResource(R.drawable.amity_ic_event_list_placeholder),
            modifier = Modifier
                .fillMaxWidth()
                .height(180.dp)
        )
        
        // Event Type Badge (Top Left)
        Box(
            modifier = Modifier
                .align(Alignment.TopStart)
                .padding(top = 8.dp, start = 8.dp)
                .background(
                    color = androidx.compose.ui.graphics.Color.Black.copy(alpha = 0.5f),
                    shape = RoundedCornerShape(4.dp)
                )
                .padding(horizontal = 6.dp, vertical = 2.dp)
        ) {
            Text(
                text = when (event?.getType()) {
                    AmityEventType.IN_PERSON -> "In-person"
                    AmityEventType.VIRTUAL -> "Virtual"
                    else -> "Virtual"
                },
                style = AmityTheme.typography.body.copy(
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Medium
                ),
                color = androidx.compose.ui.graphics.Color.White
            )
        }
        
        // Host Badge
        val currentUserId = AmityCoreClient.getUserId()
        val isHost = event?.getCreator()?.getUserId() == currentUserId
                if (isHost) {
                    EventHostBadge(
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                            .padding(top = 8.dp, end = 8.dp)
                    )
                }        // Event Info (Bottom)
        Column(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .fillMaxWidth()
                .background(
                    color = AmityTheme.colors.background
                )
                .padding(16.dp)
        ) {
            val startTime = event?.getStartTime()
            val endTime = event?.getEndTime()

            // Time text
            startTime?.let {
                Text(
                    text = formatEventTimestamp(it, endTime),
                    style = AmityTheme.typography.caption,
                    color = AmityTheme.colors.baseShade1
                )
            }
            
            Spacer(modifier = Modifier.height(8.dp))
            
            // Event Title
            Text(
                text = event?.getTitle() ?: "Meet Your Perfect Match with...",
                style = AmityTheme.typography.body.copy(
                    fontSize = 15.sp,
                    fontWeight = FontWeight.SemiBold
                ),
                color = AmityTheme.colors.base,
                maxLines = 2
            )
            
            Spacer(modifier = Modifier.height(8.dp))
            
            // Creator/Community Name
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = "By ",
                    style = AmityTheme.typography.caption,
                    color = AmityTheme.colors.baseShade1
                )
                Text(
                    text = event?.getCreator()?.getDisplayName() ?: "Benefit Cosmetics",
                    style = AmityTheme.typography.caption,
                    color = AmityTheme.colors.baseShade1
                )
                val isBrandCreator = event?.getCreator()?.isBrand() == true
                if (isBrandCreator) {
                    Image(
                        painter = painterResource(id = com.amity.socialcloud.uikit.common.compose.R.drawable.amity_ic_brand_badge),
                        contentDescription = "Brand badge",
                        modifier = Modifier.size(16.dp)
                    )
                }
            }
        }
    }
}

/**
 * List item event card layout (horizontal with thumbnail)
 * Used for scrollable event lists
 */
@Composable
private fun EventCardList(
    event: AmityEvent?,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clickableWithoutRipple { onClick() }
            .padding(horizontal = 16.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // Event Cover Image with Badge Overlay
        Box(
            modifier = Modifier
                .width(142.dp)
                .height(80.dp)
                .clip(RoundedCornerShape(8.dp))
        ) {
            // Cover Image - use getCoverImage() which handles the coverImageFileId internally
            val coverImageUrl = event?.getCoverImage()?.getUrl(AmityImage.Size.MEDIUM)
            val context = LocalContext.current
            AsyncImage(
                model = ImageRequest.Builder(context)
                    .data(coverImageUrl)
                    .crossfade(true)
                    .build(),
                contentDescription = "Event cover",
                contentScale = ContentScale.Crop,
                placeholder = painterResource(R.drawable.amity_ic_event_list_placeholder),
                error = painterResource(R.drawable.amity_ic_event_list_placeholder),
                modifier = Modifier.fillMaxSize()
            )
            
            // Event Type Badge on top left of image
            Box(
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .padding(top = 8.dp, start = 8.dp)
                    .background(
                        color = androidx.compose.ui.graphics.Color.Black.copy(alpha = 0.5f),
                        shape = RoundedCornerShape(4.dp)
                    )
                    .padding(horizontal = 6.dp, vertical = 2.dp)
            ) {
                Text(
                    text = when (event?.getType()) {
                        AmityEventType.IN_PERSON -> "In-person"
                        AmityEventType.VIRTUAL -> "Virtual"
                        else -> "Virtual"
                    },
                    style = AmityTheme.typography.caption,
                    color = androidx.compose.ui.graphics.Color.White
                )
            }
            
            // Host Badge
            val currentUserId = AmityCoreClient.getUserId()
            val isHost = event?.getCreator()?.getUserId() == currentUserId
            if (isHost) {
                EventHostBadge(
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(top = 4.dp, end = 4.dp)
                )
            }
        }
        
        Column(
            modifier = Modifier
                .weight(1f)
                .height(80.dp),
            verticalArrangement = Arrangement.Center
        ) {
            // Event Time
            val startTime = event?.getStartTime()
            val endTime = event?.getEndTime()

            startTime?.let {
                Text(
                    text = formatEventTimestamp(it, endTime),
                    style = AmityTheme.typography.captionBold,
                    color = AmityTheme.colors.base,
                    maxLines = 2
                )
            }
            
            Spacer(modifier = Modifier.height(4.dp))
            
            // Event Title
            Text(
                text = event?.getTitle() ?: "Sample Event Title",
                style = AmityTheme.typography.body.copy(fontWeight = FontWeight.SemiBold),
                color = AmityTheme.colors.base,
                maxLines = 1
            )
            
            Spacer(modifier = Modifier.height(4.dp))
            
            // Community Name
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = "By ",
                    style = AmityTheme.typography.caption,
                    color = AmityTheme.colors.baseShade1,
                    maxLines = 1
                )
                Text(
                    text = event?.getCreator()?.getDisplayName() ?: "Community Name",
                    style = AmityTheme.typography.caption,
                    color = AmityTheme.colors.baseShade1,
                    maxLines = 1
                )
                val isBrandCreator = event?.getCreator()?.isBrand() == true
                if (isBrandCreator) {
                    Image(
                        painter = painterResource(id = com.amity.socialcloud.uikit.common.compose.R.drawable.amity_ic_brand_badge),
                        contentDescription = "Brand badge",
                        modifier = Modifier.size(16.dp)
                    )
                }
            }
        }
    }
}
