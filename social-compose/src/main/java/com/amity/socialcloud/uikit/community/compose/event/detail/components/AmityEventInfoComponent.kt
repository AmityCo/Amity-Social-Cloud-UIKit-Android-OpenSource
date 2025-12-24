package com.amity.socialcloud.uikit.community.compose.event.detail.components

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.platform.LocalContext
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.amity.socialcloud.sdk.model.core.file.AmityImage
import com.amity.socialcloud.sdk.model.social.event.AmityEvent
import com.amity.socialcloud.sdk.model.social.event.AmityEventType
import com.amity.socialcloud.uikit.common.extionsions.extractUrls
import com.amity.socialcloud.uikit.common.ui.elements.AmityExpandableText
import com.amity.socialcloud.uikit.common.ui.theme.AmityTheme
import com.amity.socialcloud.uikit.community.compose.R
import org.joda.time.DateTime
import org.joda.time.Minutes

@Composable
private fun CopyButton(
    text: String,
    onCopy: () -> Unit,
    modifier: Modifier = Modifier
) {
    Button(
        onClick = {
            onCopy()
        },
        modifier = modifier
            .fillMaxWidth()
            .height(48.dp)
            .border(
                width = 1.dp,
                color = AmityTheme.colors.secondaryShade3,
                shape = RoundedCornerShape(8.dp)
            ),
        colors = ButtonDefaults.buttonColors(
            containerColor = AmityTheme.colors.background,
            contentColor = AmityTheme.colors.base
        ),
        shape = RoundedCornerShape(4.dp)
    ) {
        Icon(
            painter = painterResource(R.drawable.amity_ic_event_copy_badge),
            contentDescription = "Copy",
            tint = AmityTheme.colors.secondary,
            modifier = Modifier
                .size(20.dp)
                .padding(end = 8.dp)
        )
        Text(
            text = "Copy",
            style = AmityTheme.typography.body.copy(
                fontWeight = FontWeight.SemiBold
            ),
            color = AmityTheme.colors.secondary
        )
    }
}

@Composable
fun AmityEventInfoComponent(
    event: AmityEvent,
    modifier: Modifier = Modifier,
    onAddressCopied: () -> Unit = {},
    onLinkCopied: () -> Unit = {}
) {
    val clipboardManager = LocalClipboardManager.current
    
    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(AmityTheme.colors.background)
            .padding(16.dp)
    ) {
        // About the event section
        Text(
            text = "About the event",
            style = AmityTheme.typography.title.copy(
                fontWeight = FontWeight.Bold,
                fontSize = 17.sp
            ),
            color = AmityTheme.colors.base,
            modifier = Modifier.padding(bottom = 12.dp)
        )

        // Event description
        event.getDescription()?.let { description ->
            if (description.isNotEmpty()) {
                AmityExpandableText(
                    text = description,
                    style = AmityTheme.typography.body.copy(
                        color = AmityTheme.colors.base
                    ),
                    previewLines = 10,
                    intialExpand = false,
                    modifier = Modifier.padding(bottom = 24.dp)
                )
            }
        }

        // Event address section (only for in-person events)
        if (event.getType() == AmityEventType.IN_PERSON) {
            Text(
                text = "Location",
                style = AmityTheme.typography.title.copy(
                    fontWeight = FontWeight.Bold,
                    fontSize = 17.sp
                ),
                color = AmityTheme.colors.base,
                modifier = Modifier.padding(bottom = 12.dp)
            )
            
            // Address text
            event.getLocation()?.let { location ->
                Text(
                    text = location.toString(),
                    style = AmityTheme.typography.body,
                    color = AmityTheme.colors.base,
                    modifier = Modifier.padding(bottom = 16.dp)
                )
            }
            
            // Copy button
            CopyButton(
                text = "Address copied.",
                onCopy = {
                    event.getLocation()?.let { location ->
                        clipboardManager.setText(AnnotatedString(location.toString()))
                        onAddressCopied()
                    }
                }
            )
        }
        
        // Virtual event section (Live stream or Event link)
        if (event.getType() == AmityEventType.VIRTUAL) {
            val isExternalLink = !event.getExternalUrl().isNullOrBlank()

            if (isExternalLink) {
                // Show Event link section with copy button
                Text(
                    text = "Event link",
                    style = AmityTheme.typography.title.copy(
                        fontWeight = FontWeight.Bold,
                    ),
                    color = AmityTheme.colors.base,
                    modifier = Modifier.padding(bottom = 12.dp)
                )
                
                // Event link text with URL highlighting
                val linkText = event.getExternalUrl()!!
                val styledText = buildAnnotatedString {
                    append(linkText)
                    // Extract and highlight URLs
                    linkText.extractUrls().forEach { urlPosition ->
                        addStyle(
                            style = SpanStyle(color = AmityTheme.colors.highlight),
                            start = urlPosition.start,
                            end = urlPosition.end
                        )
                    }
                }
                
                Text(
                    text = styledText,
                    style = AmityTheme.typography.body,
                    color = AmityTheme.colors.base,
                    modifier = Modifier.padding(bottom = 16.dp)
                )
                
                // Copy button
                CopyButton(
                    text = "Link copied.",
                    onCopy = {
                        clipboardManager.setText(AnnotatedString(event.getExternalUrl()!!))
                        onLinkCopied()
                    }
                )
            } else {
                // Show Live stream section
                // Calculate minutes until start
                val startTime = event.getStartTime()
                val now = DateTime.now()
                val minutesUntilStart = startTime?.let { 
                    Minutes.minutesBetween(now, it).minutes
                } ?: Int.MAX_VALUE
                
                // Only show description if more than 15 minutes before start
                val shouldShowDescription = minutesUntilStart > 15
                
                Text(
                    text = "Live stream",
                    style = AmityTheme.typography.title.copy(
                        fontWeight = FontWeight.Bold,
                        fontSize = 17.sp
                    ),
                    color = AmityTheme.colors.base,
                    modifier = Modifier.padding(bottom = 12.dp)
                )
                
                // Live stream description - only show if more than 15 minutes before event
                if (shouldShowDescription) {
                    Text(
                        text = "You can start setting up live 15 minutes before the event starts.",
                        style = AmityTheme.typography.body,
                        color = AmityTheme.colors.baseShade1,
                        modifier = Modifier.padding(bottom = 16.dp)
                    )
                }
                
                // Live stream cover image with badge
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(186.dp)
                        .clip(RoundedCornerShape(8.dp))
                ) {
                    // Use cover image if available, otherwise use livestream placeholder
                    val imageModel = event.getCoverImage()?.getUrl(AmityImage.Size.MEDIUM) ?: R.drawable.amity_v4_ic_default_stream_thumbnail
                    val context = LocalContext.current
                    
                    AsyncImage(
                        model = ImageRequest.Builder(context)
                            .data(imageModel)
                            .crossfade(true)
                            .build(),
                        contentDescription = "Live stream cover",
                        contentScale = ContentScale.Crop,
                        placeholder = painterResource(R.drawable.amity_v4_ic_default_stream_thumbnail),
                        error = painterResource(R.drawable.amity_v4_ic_default_stream_thumbnail),
                        modifier = Modifier.fillMaxSize()
                    )
                    
                    // UPCOMING LIVE badge with black 50% background
                    Box(
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                            .padding(top = 12.dp, end = 12.dp)
                            .background(
                                color = androidx.compose.ui.graphics.Color.Black.copy(alpha = 0.5f),
                                shape = RoundedCornerShape(4.dp)
                            )
                            .padding(horizontal = 8.dp, vertical = 4.dp)
                    ) {
                        Text(
                            text = "UPCOMING LIVE",
                            style = AmityTheme.typography.caption.copy(
                                fontWeight = FontWeight.Bold,
                                fontSize = 11.sp
                            ),
                            color = androidx.compose.ui.graphics.Color.White
                        )
                    }
                }
            }
        }
    }
}
