package com.amity.socialcloud.uikit.community.compose.event.detail.elements

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.amity.socialcloud.uikit.common.ui.theme.AmityTheme
import com.amity.socialcloud.uikit.community.compose.R
import com.amity.socialcloud.uikit.community.compose.localization.amitySocialString

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AmityEventMenuBottomSheet(
    shouldShow: Boolean,
    onDismiss: () -> Unit,
    onEditClick: () -> Unit,
    onDeleteClick: () -> Unit,
    onAddToCalendarClick: () -> Unit = {},
    onCopyLinkClick: () -> Unit = {},
    onShareClick: () -> Unit = {},
    eventStartTime: org.joda.time.DateTime? = null,
    eventEndTime: org.joda.time.DateTime? = null,
    isEventCreator: Boolean = false,
    hasDeletePermission: Boolean = false,
    hasRsvpd: Boolean = false,
    showShareActions: Boolean = false
) {
    var showEditingNotPossibleDialog by remember { mutableStateOf(false) }

    // Check if event has ended
    val now = org.joda.time.DateTime.now()
    val hasEventEnded = eventEndTime?.let { now.isAfter(it) } ?: false

    // Determine who can see "Add to calendar":
    // - If event ended: no one can see
    // - Host: always see (if not ended)
    // - Moderator/User: only if they RSVP'd (and not ended)
    val showAddToCalendar = if (hasEventEnded) {
        false
    } else {
        isEventCreator || hasRsvpd
    }

    if (shouldShow) {
        ModalBottomSheet(
            onDismissRequest = onDismiss,
            sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true),
            containerColor = AmityTheme.colors.background,
            contentWindowInsets = { WindowInsets.waterfall },
            modifier = Modifier
                .navigationBarsPadding()
                .statusBarsPadding()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp)
            ) {
                // Edit option - only show for host (event creator), regardless of event status
                // This appears first in the menu per design requirements
                if (isEventCreator) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                // Check if event starts within 15 minutes
                                if (eventStartTime != null) {
                                    val now = org.joda.time.DateTime.now()
                                    val minutesUntilStart = org.joda.time.Minutes.minutesBetween(now, eventStartTime).minutes

                                    if (minutesUntilStart < 15) {
                                        showEditingNotPossibleDialog = true
                                        onDismiss()
                                        return@clickable
                                    }
                                }

                                onEditClick()
                                onDismiss()
                            }
                            .padding(horizontal = 16.dp, vertical = 16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.amity_ic_edit_profile),
                            contentDescription = amitySocialString("amity_social_label_edit_event"),
                            tint = AmityTheme.colors.base,
                            modifier = Modifier.size(24.dp)
                        )
                        Spacer(modifier = Modifier.width(16.dp))
                        Text(
                            text = amitySocialString("amity_social_label_edit_event"),
                            style = AmityTheme.typography.body.copy(
                                fontWeight = FontWeight.Medium,
                                fontSize = 15.sp
                            ),
                            color = AmityTheme.colors.base
                        )
                    }
                }

                // Add to calendar option
                // This appears after Edit event for event creators
                if (showAddToCalendar) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                onAddToCalendarClick()
                                onDismiss()
                            }
                            .padding(horizontal = 16.dp, vertical = 16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.amity_ic_event_add_to_calendar_button),
                            contentDescription = amitySocialString("amity_social_label_add_to_calendar"),
                            tint = AmityTheme.colors.base,
                            modifier = Modifier.size(24.dp)
                        )
                        Spacer(modifier = Modifier.width(16.dp))
                        Text(
                            text = amitySocialString("amity_social_label_add_to_calendar"),
                            style = AmityTheme.typography.body.copy(
                                fontWeight = FontWeight.Medium,
                                fontSize = 15.sp
                            ),
                            color = AmityTheme.colors.base
                        )
                    }
                }

                // Copy event link option - shown when the network deep-link config, community
                // type and event status allow sharing (see AmityEventDetailViewModel.eventShareUrl).
                // Appears after Add to calendar per design.
                if (showShareActions) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                onCopyLinkClick()
                                onDismiss()
                            }
                            .padding(horizontal = 16.dp, vertical = 16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.amity_v4_link_icon),
                            contentDescription = amitySocialString("amity_social_button_copy_event_link"),
                            tint = AmityTheme.colors.base,
                            modifier = Modifier.size(24.dp)
                        )
                        Spacer(modifier = Modifier.width(16.dp))
                        Text(
                            text = amitySocialString("amity_social_button_copy_event_link"),
                            style = AmityTheme.typography.body.copy(
                                fontWeight = FontWeight.Medium,
                                fontSize = 15.sp
                            ),
                            color = AmityTheme.colors.base
                        )
                    }

                    // Share to option - native OS share sheet (mobile only). Appears after Copy event link.
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                onShareClick()
                                onDismiss()
                            }
                            .padding(horizontal = 16.dp, vertical = 16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.amity_v4_share_icon),
                            contentDescription = amitySocialString("amity_social_button_share_to"),
                            tint = AmityTheme.colors.base,
                            modifier = Modifier.size(24.dp)
                        )
                        Spacer(modifier = Modifier.width(16.dp))
                        Text(
                            text = amitySocialString("amity_social_button_share_to"),
                            style = AmityTheme.typography.body.copy(
                                fontWeight = FontWeight.Medium,
                                fontSize = 15.sp
                            ),
                            color = AmityTheme.colors.base
                        )
                    }
                }

                // Delete option - show for host or users with delete permission, regardless of event status
                if (isEventCreator || hasDeletePermission) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                onDeleteClick()
                                onDismiss()
                            }
                            .padding(horizontal = 16.dp, vertical = 16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.amity_ic_delete1),
                            contentDescription = "Delete event",
                            tint = AmityTheme.colors.alert,
                            modifier = Modifier.size(24.dp)
                        )
                        Spacer(modifier = Modifier.width(16.dp))
                        Text(
                            text = amitySocialString("amity_social_button_delete_event"),
                            style = AmityTheme.typography.body.copy(
                                fontWeight = FontWeight.Medium,
                                fontSize = 15.sp
                            ),
                            color = AmityTheme.colors.alert
                        )
                    }
                }
            }
        }
    }

    // Dialog for editing not possible
    if (showEditingNotPossibleDialog) {
        AlertDialog(
            onDismissRequest = { showEditingNotPossibleDialog = false },
            title = {
                Text(
                    text = amitySocialString("amity_social_label_editing_is_not_possible"),
                    style = AmityTheme.typography.title.copy(
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 18.sp
                    ),
                    color = AmityTheme.colors.base
                )
            },
            text = {
                Text(
                    text = amitySocialString("amity_social_label_you_can_no_longer_edit_this_event_changes_are_restricte"),
                    style = AmityTheme.typography.body.copy(
                        fontSize = 15.sp
                    ),
                    color = AmityTheme.colors.base
                )
            },
            confirmButton = {
                TextButton(
                    onClick = { showEditingNotPossibleDialog = false }
                ) {
                    Text(
                        text = amitySocialString("amity_social_button_ok"),
                        style = AmityTheme.typography.body.copy(
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 15.sp
                        ),
                        color = AmityTheme.colors.primary
                    )
                }
            },
            containerColor = AmityTheme.colors.background
        )
    }
}
