package com.amity.socialcloud.uikit.community.compose.event.setup.elements

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import com.amity.socialcloud.sdk.model.social.event.AmityEventType
import com.amity.socialcloud.uikit.common.common.views.AmityColorShade
import com.amity.socialcloud.uikit.common.ui.base.AmityBaseElement
import com.amity.socialcloud.uikit.common.ui.elements.AmityBasicTextField
import com.amity.socialcloud.uikit.common.ui.elements.AmityTextField
import com.amity.socialcloud.uikit.common.ui.scope.AmityComposePageScope
import com.amity.socialcloud.uikit.common.ui.theme.AmityTheme
import com.amity.socialcloud.uikit.common.utils.clickableWithoutRipple
import com.amity.socialcloud.uikit.common.utils.shade
import com.amity.socialcloud.uikit.community.compose.R
import kotlinx.coroutines.launch

enum class EventPlatform {
    LIVE_STREAM,
    EXTERNAL_PLATFORM
}

data class AmityLocationData(
    val eventType: AmityEventType? = null,
    val platform: EventPlatform? = null,
    val address: String = "",
    val eventLink: String = ""
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AmityLocationBottomSheet(
    modifier: Modifier = Modifier,
    pageScope: AmityComposePageScope? = null,
    shouldShow: Boolean,
    initialData: AmityLocationData = AmityLocationData(),
    onDismiss: () -> Unit,
    onDone: (AmityLocationData) -> Unit
) {
    val sheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true
    )
    val scope = rememberCoroutineScope()

    var selectedEventType by remember(initialData) {
        mutableStateOf(initialData.eventType ?: AmityEventType.VIRTUAL)
    }
    var selectedPlatform by remember(initialData) {
        mutableStateOf(initialData.platform ?: EventPlatform.LIVE_STREAM)
    }
    var address by remember(initialData) { mutableStateOf(initialData.address) }
    var eventLink by remember(initialData) { mutableStateOf(initialData.eventLink) }
    var showEventTypeSelectionSheet by remember { mutableStateOf(false) }
    val hapticFeedback = LocalHapticFeedback.current

    if (shouldShow) {
        ModalBottomSheet(
            onDismissRequest = onDismiss,
            sheetState = sheetState,
            containerColor = AmityTheme.colors.background,
            dragHandle = {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 12.dp, bottom = 34.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Box(
                        modifier = Modifier
                            .width(36.dp)
                            .height(4.dp)
                            .background(
                                color = AmityTheme.colors.baseShade3,
                                shape = RoundedCornerShape(6.dp)
                            )
                    )
                }
            }
        ) {
            AmityBaseElement(
                pageScope = pageScope,
                elementId = "location_bottom_sheet"
            ) {
                val scrollState = rememberScrollState()

                Column(
                    modifier = modifier
                        .fillMaxWidth()
                        .fillMaxHeight(0.92f)
                        .verticalScroll(scrollState)
                        .padding(horizontal = 16.dp)
                ) {
                    // Header
                    // Check if required fields are filled
                    val areRequiredFieldsFilled = when {
                        // In-person requires address
                        selectedEventType == AmityEventType.IN_PERSON -> address.isNotEmpty()
                        // External platform requires event link
                        selectedEventType == AmityEventType.VIRTUAL &&
                        selectedPlatform == EventPlatform.EXTERNAL_PLATFORM -> eventLink.isNotEmpty()
                        // No event type selected
                        selectedEventType == null -> false
                        // Live stream is always valid
                        else -> true
                    }

                    // Check if current state differs from initial data
                    val currentData = when (selectedEventType) {
                        AmityEventType.IN_PERSON -> {
                            AmityLocationData(
                                eventType = selectedEventType,
                                platform = EventPlatform.LIVE_STREAM,
                                address = address,
                                eventLink = ""
                            )
                        }
                        AmityEventType.VIRTUAL -> {
                            when (selectedPlatform) {
                                EventPlatform.EXTERNAL_PLATFORM -> {
                                    AmityLocationData(
                                        eventType = selectedEventType,
                                        platform = selectedPlatform,
                                        address = "",
                                        eventLink = eventLink
                                    )
                                }
                                EventPlatform.LIVE_STREAM -> {
                                    AmityLocationData(
                                        eventType = selectedEventType,
                                        platform = selectedPlatform,
                                        address = "",
                                        eventLink = ""
                                    )
                                }
                                null -> {
                                    AmityLocationData(
                                        eventType = selectedEventType,
                                        platform = null,
                                        address = "",
                                        eventLink = ""
                                    )
                                }
                            }
                        }
                        null -> {
                            AmityLocationData(
                                eventType = null,
                                platform = null,
                                address = address,
                                eventLink = eventLink
                            )
                        }
                        AmityEventType.UNKNOWN -> AmityLocationData()
                    }
                    val hasChanges = currentData != initialData

                    val isDoneEnabled = areRequiredFieldsFilled && hasChanges

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Cancel",
                            style = AmityTheme.typography.body,
                            color = AmityTheme.colors.base,
                            modifier = Modifier.clickableWithoutRipple {
                                scope.launch {
                                    sheetState.hide()
                                }.invokeOnCompletion {
                                    if (!sheetState.isVisible) {
                                        onDismiss()
                                    }
                                }
                            }
                        )

                        Text(
                            text = "Location",
                            style = AmityTheme.typography.titleBold,
                            color = AmityTheme.colors.base,
                        )

                        Text(
                            text = "Done",
                            style = AmityTheme.typography.body,
                            color = if (isDoneEnabled) AmityTheme.colors.primary else AmityTheme.colors.primary.shade(AmityColorShade.SHADE2),
                            modifier = Modifier.clickableWithoutRipple {
                                if (isDoneEnabled) {
                                    // Clear irrelevant fields based on event type and platform
                                    val locationData = when (selectedEventType) {
                                        AmityEventType.IN_PERSON -> {
                                            // In-person: only address is relevant
                                            AmityLocationData(
                                                eventType = selectedEventType,
                                                platform = EventPlatform.LIVE_STREAM, // Default, not used for in-person
                                                address = address,
                                                eventLink = ""
                                            )
                                        }
                                        AmityEventType.VIRTUAL -> {
                                            when (selectedPlatform) {
                                                EventPlatform.EXTERNAL_PLATFORM -> {
                                                    // External platform: only eventLink is relevant
                                                    AmityLocationData(
                                                        eventType = selectedEventType,
                                                        platform = selectedPlatform,
                                                        address = "",
                                                        eventLink = eventLink
                                                    )
                                                }
                                                EventPlatform.LIVE_STREAM -> {
                                                    // Live stream: no address or link needed
                                                    AmityLocationData(
                                                        eventType = selectedEventType,
                                                        platform = selectedPlatform,
                                                        address = "",
                                                        eventLink = ""
                                                    )
                                                }
                                                null -> {
                                                    AmityLocationData(
                                                        eventType = selectedEventType,
                                                        platform = null,
                                                        address = "",
                                                        eventLink = ""
                                                    )
                                                }
                                            }
                                        }
                                        null -> {
                                            AmityLocationData(
                                                eventType = null,
                                                platform = null,
                                                address = address,
                                                eventLink = eventLink
                                            )
                                        }
                                        AmityEventType.UNKNOWN -> AmityLocationData()
                                    }
                                    onDone(locationData)
                                    scope.launch {
                                        sheetState.hide()
                                    }.invokeOnCompletion {
                                        if (!sheetState.isVisible) {
                                            onDismiss()
                                        }
                                    }
                                }
                            }
                        )
                    }

                    HorizontalDivider(
                        color = AmityTheme.colors.baseShade4,
                        thickness = 1.dp
                    )

                    Spacer(modifier = Modifier.height(34.dp))

                    // Event Type Section
                    Text(
                        text = "Event type",
                        style = AmityTheme.typography.titleBold,
                        color = AmityTheme.colors.base,
                        modifier = Modifier.padding(bottom = 12.dp)
                    )

                    // Event Type Selection
                    Column(
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickableWithoutRipple {
                                    showEventTypeSelectionSheet = true
                                }
                                .padding(vertical = 12.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = when (selectedEventType) {
                                    AmityEventType.VIRTUAL -> "Virtual"
                                    AmityEventType.IN_PERSON -> "In-person"
                                    AmityEventType.UNKNOWN -> ""
                                    null -> ""
                                },
                                style = AmityTheme.typography.body,
                                color = AmityTheme.colors.base
                            )
                            Icon(
                                painter = painterResource(com.amity.socialcloud.uikit.common.R.drawable.amity_arrow_down),
                                contentDescription = "Select event type",
                                tint = AmityTheme.colors.baseShade2,
                                modifier = Modifier.size(16.dp)
                            )
                        }

                        HorizontalDivider(
                            color = AmityTheme.colors.baseShade4,
                            thickness = 1.dp
                        )
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    // Address Section (only visible for In-person events)
                    if (selectedEventType == AmityEventType.IN_PERSON) {
                        Column(
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                text = "Address",
                                style = AmityTheme.typography.body,
                                color = AmityTheme.colors.base,
                            )

                            Spacer(modifier = Modifier.height(4.dp))

                            var isAddressFocused by remember { mutableStateOf(false) }
                            var addressFieldValue by remember { mutableStateOf(TextFieldValue(initialData.address)) }

                            val isAddressNotEmpty = address.trim().isNotEmpty()
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .heightIn(min = 112.dp)
                                    .background(
                                        AmityTheme.colors.baseShade4,
                                        RoundedCornerShape(8.dp)
                                    )
                                    .padding(vertical = 4.dp)
                            ) {
                                AmityBasicTextField(
                                    value = addressFieldValue,
                                    maxChar = 180,
                                    onValueChange = { newValue: TextFieldValue ->
                                        if (newValue.text.length > 180) {
                                            hapticFeedback.performHapticFeedback(HapticFeedbackType.LongPress)
                                        } else {
                                            addressFieldValue = newValue
                                            address = newValue.text
                                        }
                                    },
                                    textStyle = AmityTheme.typography.bodyLegacy.copy(
                                        color = AmityTheme.colors.base
                                    ),
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(horizontal = 12.dp)
                                        .onFocusChanged { focus ->
                                            isAddressFocused = focus.isFocused
                                        },
                                    placeholder = {
                                        Text(
                                            text = "Enter address of where this event will be happening",
                                            style = AmityTheme.typography.bodyLegacy,
                                            color = Color(0xFF6E6E6E)
                                        )
                                    },
                                    colors = TextFieldDefaults.colors(
                                        unfocusedContainerColor = Color.Transparent,
                                        focusedContainerColor = Color.Transparent,
                                        unfocusedIndicatorColor = Color.Transparent,
                                        focusedIndicatorColor = Color.Transparent,
                                    ),
                                    singleLine = false,
                                    minHeight = 40.dp,
                                    contentPadding = PaddingValues(
                                        start = 0.dp,
                                        end = 0.dp,
                                        top = 12.dp,
                                        bottom = 12.dp
                                    ),
                                    maxLines = 10,
                                )
                            }

                            Spacer(modifier = Modifier.height(8.dp))

                            Text(
                                text = "${address.length}/180",
                                style = AmityTheme.typography.caption.copy(
                                    fontWeight = FontWeight.Normal,
                                    color = AmityTheme.colors.baseShade1,
                                )
                            )
                        }

                        Spacer(modifier = Modifier.height(24.dp))
                    }

                    // Platform Section (only visible for Virtual events)
                    if (selectedEventType == AmityEventType.VIRTUAL) {
                        Text(
                            text = "How to join",
                            style = AmityTheme.typography.titleBold,
                            color = AmityTheme.colors.base,
                            modifier = Modifier.padding(bottom = 16.dp)
                        )

                        // Live Stream Option
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickableWithoutRipple {
                                    selectedPlatform = EventPlatform.LIVE_STREAM
                                }
                                .padding(vertical = 12.dp),
                            verticalAlignment = Alignment.Top
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(40.dp)
                                    .background(
                                        AmityTheme.colors.baseShade4,
                                        CircleShape
                                    ),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    painter = painterResource(R.drawable.amity_ic_event_livestream),
                                    contentDescription = "Live stream",
                                    tint = AmityTheme.colors.base,
                                    modifier = Modifier.size(24.dp)
                                )
                            }

                            Spacer(modifier = Modifier.width(12.dp))

                            Column(
                                modifier = Modifier
                                    .weight(1f)
                                    .padding(end = 8.dp)
                            ) {
                                Text(
                                    text = "Live stream",
                                    style = AmityTheme.typography.body.copy(
                                        fontWeight = FontWeight.SemiBold
                                    ),
                                    color = AmityTheme.colors.base
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = "Attendees join the live stream directly on the app or website.",
                                    style = AmityTheme.typography.caption,
                                    color = AmityTheme.colors.baseShade1
                                )
                            }

                            // Radio Button
                            val strokeColor = AmityTheme.colors.baseShade3
                            Box(
                                modifier = Modifier
                                    .size(24.dp)
                                    .background(
                                        if (selectedPlatform == EventPlatform.LIVE_STREAM)
                                            AmityTheme.colors.primary
                                        else
                                            Color.Transparent,
                                        CircleShape
                                    ),
                                contentAlignment = Alignment.Center
                            ) {
                                if (selectedPlatform == EventPlatform.LIVE_STREAM) {
                                    Box(
                                        modifier = Modifier
                                            .size(8.dp)
                                            .background(Color.White, CircleShape)
                                    )
                                } else {
                                    androidx.compose.foundation.Canvas(
                                        modifier = Modifier.fillMaxSize()
                                    ) {
                                        drawCircle(
                                            color = strokeColor,
                                            radius = size.minDimension / 2,
                                            style = androidx.compose.ui.graphics.drawscope.Stroke(width = 2.dp.toPx())
                                        )
                                    }
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        // External Platform Option
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickableWithoutRipple {
                                    selectedPlatform = EventPlatform.EXTERNAL_PLATFORM
                                }
                                .padding(vertical = 12.dp),
                            verticalAlignment = Alignment.Top
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(40.dp)
                                    .background(
                                        AmityTheme.colors.baseShade4,
                                        CircleShape
                                    ),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    painter = painterResource(R.drawable.amity_ic_event_external),
                                    contentDescription = "Event link",
                                    tint = AmityTheme.colors.base,
                                    modifier = Modifier.size(24.dp)
                                )
                            }

                            Spacer(modifier = Modifier.width(12.dp))

                            Column(
                                modifier = Modifier
                                    .weight(1f)
                                    .padding(end = 8.dp)
                            ) {
                                Text(
                                    text = "Event link",
                                    style = AmityTheme.typography.body.copy(
                                        fontWeight = FontWeight.SemiBold
                                    ),
                                    color = AmityTheme.colors.base
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = "Attendees click on an external link to join the event.",
                                    style = AmityTheme.typography.caption,
                                    color = AmityTheme.colors.baseShade1
                                )
                            }

                            // Radio Button
                            val strokeColor = AmityTheme.colors.baseShade3
                            Box(
                                modifier = Modifier
                                    .size(24.dp)
                                    .background(
                                        if (selectedPlatform == EventPlatform.EXTERNAL_PLATFORM)
                                            AmityTheme.colors.primary
                                        else
                                            Color.Transparent,
                                        CircleShape
                                    ),
                                contentAlignment = Alignment.Center
                            ) {
                                if (selectedPlatform == EventPlatform.EXTERNAL_PLATFORM) {
                                    Box(
                                        modifier = Modifier
                                            .size(8.dp)
                                            .background(Color.White, CircleShape)
                                    )
                                } else {
                                    androidx.compose.foundation.Canvas(
                                        modifier = Modifier.fillMaxSize()
                                    ) {
                                        drawCircle(
                                            color = strokeColor,
                                            radius = size.minDimension / 2,
                                            style = androidx.compose.ui.graphics.drawscope.Stroke(width = 2.dp.toPx())
                                        )
                                    }
                                }
                            }
                        }

                        // Event Link field (only visible when External Platform is selected)
                        if (selectedPlatform == EventPlatform.EXTERNAL_PLATFORM) {
                            Spacer(modifier = Modifier.height(16.dp))

                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(start = 52.dp)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .background(
                                            AmityTheme.colors.baseShade4,
                                            RoundedCornerShape(8.dp)
                                        )
                                        .padding(horizontal = 12.dp, vertical = 12.dp)
                                ) {
                                    AmityTextField(
                                        maxCharacters = 200,
                                        maxLines = Int.MAX_VALUE,
                                        text = eventLink,
                                        hint = "Event link",
                                        onValueChange = { newValue ->
                                            val filteredValue = newValue.replace(Regex("[ \\n]"), "")
                                            if (filteredValue.length > 200) {
                                                hapticFeedback.performHapticFeedback(HapticFeedbackType.LongPress)
                                            } else {
                                                eventLink = filteredValue
                                            }
                                        },
                                        textStyle = AmityTheme.typography.body.copy(
                                            color = AmityTheme.colors.base
                                        ),
                                    )
                                }

                                Spacer(modifier = Modifier.height(8.dp))

                                Box(
                                    modifier = Modifier.fillMaxWidth(),
                                    contentAlignment = Alignment.CenterEnd
                                ) {
                                    Text(
                                        text = "${eventLink.length}/200",
                                        style = AmityTheme.typography.caption,
                                        color = AmityTheme.colors.baseShade1
                                    )
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(32.dp))
                }
            }
        }

        // Event Type Selection Bottom Sheet
        if (showEventTypeSelectionSheet) {
            AmityEventTypeSelectionSheet(
                modifier = modifier,
                pageScope = pageScope,
                shouldShow = showEventTypeSelectionSheet,
                onDismiss = { showEventTypeSelectionSheet = false },
                onTypeSelected = { type ->
                    selectedEventType = type
                    showEventTypeSelectionSheet = false
                }
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AmityEventTypeSelectionSheet(
    modifier: Modifier = Modifier,
    pageScope: AmityComposePageScope? = null,
    shouldShow: Boolean,
    onDismiss: () -> Unit,
    onTypeSelected: (AmityEventType) -> Unit
) {
    val sheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true
    )
    val scope = rememberCoroutineScope()

    if (shouldShow) {
        ModalBottomSheet(
            onDismissRequest = onDismiss,
            sheetState = sheetState,
            containerColor = AmityTheme.colors.background,
            dragHandle = {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 12.dp, bottom = 20.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Box(
                        modifier = Modifier
                            .width(36.dp)
                            .height(4.dp)
                            .background(
                                color = AmityTheme.colors.baseShade3,
                                shape = RoundedCornerShape(6.dp)
                            )
                    )
                }
            }
        ) {
            AmityBaseElement(
                pageScope = pageScope,
                elementId = "event_type_selection"
            ) {
                Column(
                    modifier = modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                        .padding(bottom = 32.dp)
                ) {
                    // In-person option
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickableWithoutRipple {
                                onTypeSelected(AmityEventType.IN_PERSON)
                                scope.launch {
                                    sheetState.hide()
                                }.invokeOnCompletion {
                                    if (!sheetState.isVisible) {
                                        onDismiss()
                                    }
                                }
                            }
                            .padding(vertical = 16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "In-person",
                            style = AmityTheme.typography.bodyLegacy.copy(
                                color = AmityTheme.colors.base
                            )
                        )

                    }

                    // Virtual option
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickableWithoutRipple {
                                onTypeSelected(AmityEventType.VIRTUAL)
                                scope.launch {
                                    sheetState.hide()
                                }.invokeOnCompletion {
                                    if (!sheetState.isVisible) {
                                        onDismiss()
                                    }
                                }
                            }
                            .padding(vertical = 16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Virtual",
                            style = AmityTheme.typography.bodyLegacy.copy(
                                color = AmityTheme.colors.base
                            )
                        )
                    }
                }
            }
        }
    }
}
