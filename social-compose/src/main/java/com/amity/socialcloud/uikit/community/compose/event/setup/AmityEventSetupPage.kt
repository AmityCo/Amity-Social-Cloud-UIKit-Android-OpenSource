package com.amity.socialcloud.uikit.community.compose.event.setup

import android.app.Activity
import android.net.Uri
import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.SelectableDates
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import android.text.format.DateFormat
import androidx.compose.foundation.layout.heightIn
import androidx.compose.ui.text.style.TextOverflow
import coil3.compose.AsyncImage
import com.amity.socialcloud.sdk.api.core.AmityCoreClient
import com.amity.socialcloud.sdk.api.social.AmitySocialClient
import com.amity.socialcloud.sdk.model.core.error.AmityError
import com.amity.socialcloud.sdk.model.core.file.upload.AmityUploadResult
import com.amity.socialcloud.sdk.model.social.event.AmityEvent
import com.amity.socialcloud.sdk.model.social.event.AmityEventType
import com.amity.socialcloud.uikit.common.config.AmityUIKitConfigController
import com.amity.socialcloud.uikit.common.eventbus.AmityUIKitSnackbar
import com.amity.socialcloud.uikit.common.ui.base.AmityBaseElement
import com.amity.socialcloud.uikit.common.ui.base.AmityBasePage
import com.amity.socialcloud.uikit.common.ui.elements.AmityAlertDialog
import com.amity.socialcloud.uikit.common.ui.elements.AmityBasicTextField
import com.amity.socialcloud.uikit.common.ui.elements.AmityDatePickerDialog
import com.amity.socialcloud.uikit.common.ui.elements.AmityTextField
import com.amity.socialcloud.uikit.common.ui.elements.AmityTimePickerDialog
import com.amity.socialcloud.uikit.common.ui.theme.AmityTheme
import com.amity.socialcloud.uikit.common.utils.AmityCameraUtil
import com.amity.socialcloud.uikit.common.utils.clickableWithoutRipple
import com.amity.socialcloud.uikit.common.utils.closePage
import com.amity.socialcloud.uikit.common.utils.closePageWithResult
import com.amity.socialcloud.uikit.community.compose.R
import com.amity.socialcloud.uikit.community.compose.community.setup.elements.AmityMediaImageSelectionSheet
import com.amity.socialcloud.uikit.community.compose.community.setup.elements.AmityMediaImageSelectionType
import com.amity.socialcloud.uikit.community.compose.event.detail.AmityEventDetailPageActivity
import com.amity.socialcloud.uikit.community.compose.event.setup.elements.AmityLocationBottomSheet
import com.amity.socialcloud.uikit.community.compose.event.setup.elements.AmityLocationData
import com.amity.socialcloud.uikit.community.compose.event.setup.elements.AmityTimezoneListBottomSheet
import com.amity.socialcloud.uikit.community.compose.event.setup.elements.EventPlatform
import com.amity.socialcloud.uikit.community.compose.event.setup.elements.TimezoneFormatter
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers
import org.joda.time.DateTime
import org.joda.time.DateTimeZone
import org.joda.time.format.DateTimeFormat
import java.util.TimeZone

@OptIn(ExperimentalMaterial3Api::class)

@Composable
fun AmityEventSetupPage(
    modifier: Modifier = Modifier,
    mode: AmityEventSetupPageMode = AmityEventSetupPageMode.Create(),
) {
    val context = LocalContext.current

    val isInEditMode by remember(mode) {
        mutableStateOf(mode is AmityEventSetupPageMode.Edit)
    }

    // Load event data if in edit mode
    val existingEventState = remember { mutableStateOf<AmityEvent?>(null) }
    val existingEvent = existingEventState.value

    // Community display name for subtitle
    var communityDisplayName by remember { mutableStateOf("") }

    // Track if event data has been loaded in edit mode
    val isEventDataLoaded = remember { mutableStateOf(false) }

    // Track original data for comparison in edit mode
    val originalEventName = remember { mutableStateOf("") }
    val originalEventDetails = remember { mutableStateOf("") }
    val originalStartDateTime = remember { mutableStateOf<DateTime?>(null) }
    val originalEndDateTime = remember { mutableStateOf<DateTime?>(null) }
    val originalTimezone = remember { mutableStateOf<TimeZone?>(null) }
    val originalLocationData = remember { mutableStateOf<AmityLocationData?>(null) }
    val originalCoverImageUrl = remember { mutableStateOf<String?>(null) }

    // Load community display name in create mode
    LaunchedEffect(mode) {
        if (mode is AmityEventSetupPageMode.Create) {
            mode.communityId?.let { communityId ->
                AmitySocialClient.newCommunityRepository()
                    .getCommunity(communityId)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .doOnNext { community ->
                        communityDisplayName = community.getDisplayName() ?: ""
                    }
                    .subscribe()
            }
        }
    }

    LaunchedEffect(mode) {
        if (mode is AmityEventSetupPageMode.Edit) {
            AmitySocialClient.newEventRepository()
                .getEvent(mode.eventId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnNext { event ->
                    existingEventState.value = event
                    // Load community display name from event
                    val communityId = event.getOriginId()
                    if (communityId != null) {
                        AmitySocialClient.newCommunityRepository()
                            .getCommunity(communityId)
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .doOnNext { community ->
                                communityDisplayName = community.getDisplayName() ?: ""
                            }
                            .subscribe()
                    }
                }
                .subscribe()
        }
    }

    var eventName by remember { mutableStateOf("") }
    var eventDetails by remember { mutableStateOf("") }
    var selectedTimezone by remember { mutableStateOf(TimeZone.getDefault()) }

    // Date time handling
    var startDateTime by remember(isInEditMode) {
        mutableStateOf(
            if (isInEditMode) {
                // In edit mode, use a placeholder that will be replaced when data loads
                DateTime.now()
            } else {
                // In create mode, use default values
                DateTime.now()
                    .plusDays(1)
                    .withHourOfDay(9)
                    .withMinuteOfHour(0)
                    .withSecondOfMinute(0)
                    .withMillisOfSecond(0)
            }
        )
    }
    var endDateTime by remember(isInEditMode) {
        mutableStateOf<DateTime?>(
            if (isInEditMode) {
                // In edit mode, use a placeholder that will be replaced when data loads
                null
            } else {
                // In create mode, use default values
                DateTime.now()
                    .plusDays(1)
                    .withHourOfDay(10)
                    .withMinuteOfHour(0)
                    .withSecondOfMinute(0)
                    .withMillisOfSecond(0)
            }
        )
    }
    var hasEndDateTime by remember { mutableStateOf(true) }

    var locationData by remember { mutableStateOf(AmityLocationData()) }
    var hasConfiguredLocation by remember { mutableStateOf(false) }

    // Track if event is being created or saved
    var isCreatingOrSaving by remember { mutableStateOf(false) }

    // Populate fields when existing event loads (Edit mode)
    LaunchedEffect(existingEvent) {
        existingEvent?.let { event ->
            // Only populate fields once when data first loads
            if (!isEventDataLoaded.value) {
                // Store original values FIRST for comparison
                originalEventName.value = event.getTitle() ?: ""
                originalEventDetails.value = event.getDescription() ?: ""
                originalCoverImageUrl.value = event.getCoverImage()?.getUrl()

                // Set start and end times (which include timezone info)
                event.getStartTime()?.let { start ->
                    originalStartDateTime.value = start
                    // Extract timezone from DateTime
                    originalTimezone.value = TimeZone.getTimeZone(start.getZone().getID())
                }
                event.getEndTime()?.let { end ->
                    originalEndDateTime.value = end
                } ?: run {
                    originalEndDateTime.value = null
                }

                // Set location data based on event type
                val location = event.getLocation()
                val locationDataValue = when (event.getType()) {
                    AmityEventType.IN_PERSON -> {
                        AmityLocationData(
                            eventType = AmityEventType.IN_PERSON,
                            address = location.orEmpty()
                        )
                    }
                    AmityEventType.VIRTUAL -> {
                        // Check if it's a link (for external platform) or livestream
                        if (!location.isNullOrBlank() && (location.startsWith("http://") || location.startsWith("https://"))) {
                            AmityLocationData(
                                eventType = AmityEventType.VIRTUAL,
                                platform = EventPlatform.EXTERNAL_PLATFORM,
                                eventLink = location
                            )
                        } else {
                            // Default to livestream for virtual events
                            AmityLocationData(
                                eventType = AmityEventType.VIRTUAL,
                                platform = EventPlatform.LIVE_STREAM
                            )
                        }
                    }
                    else -> null
                }
                originalLocationData.value = locationDataValue

                // Now populate the UI fields with the same values
                eventName = originalEventName.value
                eventDetails = originalEventDetails.value

                originalStartDateTime.value?.let { start ->
                    startDateTime = start
                    selectedTimezone = TimeZone.getTimeZone(start.getZone().getID())
                }

                originalEndDateTime.value?.let { end ->
                    endDateTime = end
                    hasEndDateTime = true
                } ?: run {
                    hasEndDateTime = false
                    endDateTime = null
                }

                originalLocationData.value?.let { locData ->
                    locationData = locData
                    hasConfiguredLocation = true
                }

                // Mark data as loaded LAST, after everything is set
                isEventDataLoaded.value = true
            }
        }
    }

    var showLeaveConfirmDialog by remember { mutableStateOf(false) }
    var showMediaCameraSelectionSheet by remember { mutableStateOf(false) }
    var showTimezoneSelectionSheet by remember { mutableStateOf(false) }
    var showLocationBottomSheet by remember { mutableStateOf(false) }
    var showStartDatePicker by remember { mutableStateOf(false) }
    var showStartTimePicker by remember { mutableStateOf(false) }
    var showEndDatePicker by remember { mutableStateOf(false) }
    var showEndTimePicker by remember { mutableStateOf(false) }
    var isCameraPermissionGranted by remember { mutableStateOf(false) }
    var showCoverImageErrorUploadDialog by remember {
        mutableStateOf(
            Pair(
                first = false,
                second = 0
            )
        )
    }

    var isCapturedImageReady by remember { mutableStateOf(false) }
    var coverImageUri by remember { mutableStateOf(Uri.EMPTY) }

    // Date/Time Pickers State
    val startDatePickerState = rememberDatePickerState(
        initialSelectedDateMillis = startDateTime.millis,
        selectableDates = object : SelectableDates {
            override fun isSelectableDate(utcTimeMillis: Long): Boolean {
                val date = DateTime(utcTimeMillis, DateTimeZone.UTC)
                return !date.isBefore(DateTime.now().minusDays(1))
            }
        }
    )
    val startTimePickerState = rememberTimePickerState(
        initialHour = startDateTime.toLocalTime().hourOfDay,
        initialMinute = startDateTime.toLocalTime().minuteOfHour
    )

    val endDatePickerState = rememberDatePickerState(
        initialSelectedDateMillis = endDateTime?.millis ?: DateTime.now().plusHours(2).millis,
        selectableDates = object : SelectableDates {
            override fun isSelectableDate(utcTimeMillis: Long): Boolean {
                val date = DateTime(utcTimeMillis, DateTimeZone.UTC)
                return !date.isBefore(startDateTime.minusDays(1))
            }
        }
    )
    val endTimePickerState = rememberTimePickerState(
        initialHour = endDateTime?.toLocalTime()?.hourOfDay ?: DateTime.now().plusHours(2).toLocalTime().hourOfDay,
        initialMinute = endDateTime?.toLocalTime()?.minuteOfHour ?: DateTime.now().plusHours(2).toLocalTime().minuteOfHour
    )

    // Update end time picker state when endDateTime changes programmatically
    LaunchedEffect(endDateTime) {
        endDateTime?.let {
            endDatePickerState.selectedDateMillis = it.millis
            endTimePickerState.hour = it.toLocalTime().hourOfDay
            endTimePickerState.minute = it.toLocalTime().minuteOfHour
        }
    }

    val scrollState = rememberScrollState()
    val hasScrolled by remember {
        derivedStateOf { scrollState.value > 0 }
    }

    // Check if there are changes compared to original data
    val hasChanges by remember(
        eventName,
        eventDetails,
        selectedTimezone,
        startDateTime,
        endDateTime,
        locationData,
        coverImageUri,
        isCapturedImageReady,
        isInEditMode,
        isEventDataLoaded.value,
        originalEventName.value,
        originalEventDetails.value,
        originalTimezone.value,
        originalStartDateTime.value,
        originalEndDateTime.value,
        originalLocationData.value
    ) {
        derivedStateOf {
            if (!isInEditMode) {
                // In create mode, always consider as "has changes"
                true
            } else {
                // In edit mode, wait for original data to load first
                if (!isEventDataLoaded.value) {
                    // Data hasn't loaded yet, no changes
                    false
                } else {
                    // Check if any field has changed
                    val nameChanged = eventName != originalEventName.value
                    val detailsChanged = eventDetails != originalEventDetails.value
                    val timezoneChanged = selectedTimezone.id != originalTimezone.value?.id
                    val startDateTimeChanged = startDateTime != originalStartDateTime.value
                    val endDateTimeChanged = endDateTime != originalEndDateTime.value
                    val locationChanged = locationData != originalLocationData.value
                    val coverImageChanged = coverImageUri != Uri.EMPTY && isCapturedImageReady

                    nameChanged || detailsChanged || timezoneChanged || startDateTimeChanged ||
                    endDateTimeChanged || locationChanged || coverImageChanged
                }
            }
        }
    }

    val shouldActionButtonEnable by remember(eventName, eventDetails, hasConfiguredLocation, hasChanges) {
        derivedStateOf {
            val mandatoryFieldsFilled = eventName.isNotBlank() && eventDetails.isNotBlank() && hasConfiguredLocation
            mandatoryFieldsFilled && hasChanges
        }
    }

    val imagePickerLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
            uri?.let {
                coverImageUri = it
                isCapturedImageReady = true
            }
        }

    val imageCaptureLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.TakePicture()) { isSuccess ->
            isCapturedImageReady = isSuccess
        }

    val cameraPermissionLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
            isCapturedImageReady = false
            isCameraPermissionGranted = permissions.entries.all { it.value }
            if (!isCameraPermissionGranted) {
                AmityUIKitSnackbar.publishSnackbarErrorMessage("Camera permission not granted")
            } else {
                val imageFile = AmityCameraUtil.createImageFile(context)
                if (imageFile == null) {
                    AmityUIKitSnackbar.publishSnackbarErrorMessage("Failed to create image file")
                } else {
                    coverImageUri = AmityCameraUtil.createPhotoUri(context, imageFile)
                    imageCaptureLauncher.launch(coverImageUri)
                }
            }
        }

    BackHandler {
        if (isInEditMode) {
            // In edit mode, only show dialog if there are changes
            if (hasChanges) {
                showLeaveConfirmDialog = true
            } else {
                context.closePage()
            }
        } else {
            // In create mode, show dialog if any content entered
            val hasEnteredContent = eventName.isNotBlank() || eventDetails.isNotBlank()
            if (hasEnteredContent) {
                showLeaveConfirmDialog = true
            } else {
                context.closePage()
            }
        }
    }

    var stickyHeaderHeight by remember { mutableStateOf(0.dp) }
    val localDensity = LocalDensity.current

    AmityBasePage(pageId = "event_setup_page") {
        Box(
            modifier = modifier
                .fillMaxSize()
                .windowInsetsPadding(WindowInsets.safeDrawing)
        ) {
            // Fixed toolbar at the top
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.TopCenter)
                    .background(AmityTheme.colors.background)
                    .onGloballyPositioned { coordinates ->
                        stickyHeaderHeight = with(localDensity) {
                            coordinates.size.height.toDp()
                        }
                    }
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = modifier
                        .fillMaxWidth()
                        .padding(horizontal = 12.dp)
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.amity_ic_close),
                        contentDescription = "Close Button",
                        tint = AmityTheme.colors.base,
                        modifier = modifier
                            .size(12.dp)
                            .clickableWithoutRipple {
                                if (isInEditMode) {
                                    // In edit mode, only show dialog if there are changes
                                    if (hasChanges) {
                                        showLeaveConfirmDialog = true
                                    } else {
                                        context.closePage()
                                    }
                                } else {
                                    // In create mode, always show dialog if any content entered
                                    val hasEnteredContent = eventName.isNotBlank() || eventDetails.isNotBlank()
                                    if (hasEnteredContent) {
                                        showLeaveConfirmDialog = true
                                    } else {
                                        context.closePage()
                                    }
                                }
                            }
                    )
                    AmityBaseElement(
                        pageScope = getPageScope(),
                        elementId = "title"
                    ) {
                        Text(
                            text = if (isInEditMode) "Edit event" else "Create event",
                            style = AmityTheme.typography.titleBold,
                            color = AmityTheme.colors.base,
                            textAlign = TextAlign.Center,
                            modifier = modifier
                                .weight(1f)
                                .padding(vertical = 16.dp)
                                .testTag(getAccessibilityId())
                        )
                    }
                    Spacer(modifier = modifier.width(16.dp))
                }

                // Subtitle - Community name
                if (communityDisplayName.isNotEmpty()) {
                    Text(
                        text = communityDisplayName,
                        style = AmityTheme.typography.caption,
                        color = AmityTheme.colors.baseShade1,
                        textAlign = TextAlign.Center,
                        modifier = modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp)
                            .padding(bottom = 16.dp)
                    )
                }

                if (hasScrolled) {
                    HorizontalDivider(
                        color = AmityTheme.colors.baseShade4,
                    )
                }
            }

            // Content area with scroll
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = stickyHeaderHeight)
                    .verticalScroll(scrollState)
                    .background(AmityTheme.colors.background)
            ) {
                // Cover Image
                Box(
                    modifier = modifier
                        .aspectRatio(2f)
                        .background(AmityTheme.colors.primaryShade3.copy(alpha = 0.5f))
                        .clickableWithoutRipple {
                            showMediaCameraSelectionSheet = true
                        }
                ) {
                    val coverImage = if (coverImageUri != Uri.EMPTY && isCapturedImageReady) {
                        // User uploaded a new image
                        coverImageUri
                    } else if (isInEditMode && existingEvent != null) {
                        // In edit mode, show existing cover image
                        existingEvent.getCoverImage()?.getUrl()
                    } else {
                        null
                    }

                    AsyncImage(
                        model = coverImage,
                        contentDescription = "Cover Image",
                        contentScale = ContentScale.Crop,
                        modifier = modifier.fillMaxWidth(),
                    )
                    Box(
                        modifier = modifier
                            .fillMaxSize()
                            .background(Color.Black.copy(alpha = 0.4f))
                    )
                    Icon(
                        painter = painterResource(R.drawable.amity_ic_event_setup_camera),
                        contentDescription = "Upload cover image",
                        tint = Color.White,
                        modifier = modifier
                            .size(32.dp)
                            .align(Alignment.Center)
                    )
                }

                Spacer(modifier = modifier.height(24.dp))

                // Event Name
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                ) {
                    AmityBaseElement(
                        pageScope = getPageScope(),
                        elementId = "event_name_title"
                    ) {
                        Text(
                            text = "Event name",
                            style = AmityTheme.typography.body,
                            color = AmityTheme.colors.base,
                            modifier = modifier.testTag(getAccessibilityId())
                        )
                    }
                }

                Spacer(modifier = modifier.height(4.dp))

                var isEventNameFocused by remember { mutableStateOf(false) }
                var eventNameFieldValue by remember { mutableStateOf(TextFieldValue(eventName)) }
                val hapticFeedback = LocalHapticFeedback.current

                LaunchedEffect(eventName) {
                    if (eventNameFieldValue.text != eventName) {
                        eventNameFieldValue = TextFieldValue(eventName)
                    }
                }

                val isEventNameNotEmpty = eventName.trim().isNotEmpty()
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                        .background(
                            color = Color.Transparent,
                            shape = RoundedCornerShape(4.dp)
                        )
                        .padding(vertical = 4.dp)
                ) {
                    AmityBasicTextField(
                        value = eventNameFieldValue,
                        maxChar = 60,
                        onValueChange = { newValue: TextFieldValue ->
                            val filteredText = newValue.text.replace("\n", "")
                            if (filteredText.length > 60) {
                                hapticFeedback.performHapticFeedback(HapticFeedbackType.LongPress)
                            } else {
                                eventNameFieldValue = newValue.copy(text = filteredText)
                                eventName = filteredText
                            }
                        },
                        textStyle = AmityTheme.typography.bodyLegacy.copy(
                            color = AmityTheme.colors.base
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 12.dp)
                            .onFocusChanged { focus ->
                                isEventNameFocused = focus.isFocused
                            },
                        placeholder = {
                            Text(
                                text = "Name your event",
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
                        maxLines = 3,
                    )
                }

                Spacer(modifier = modifier.height(8.dp))

                Text(
                    modifier = Modifier.padding(horizontal = 16.dp),
                    text = "${eventName.length}/60",
                    style = AmityTheme.typography.caption.copy(
                        fontWeight = FontWeight.Normal,
                        color = AmityTheme.colors.baseShade1,
                    )
                )

                Spacer(modifier = modifier.height(24.dp))

                // Event Details
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                ) {
                    AmityBaseElement(
                        pageScope = getPageScope(),
                        elementId = "event_details_title"
                    ) {
                        Text(
                            text = "Event details",
                            style = AmityTheme.typography.body,
                            color = AmityTheme.colors.base,
                            modifier = modifier.testTag(getAccessibilityId())
                        )
                    }
                }
                Spacer(modifier = modifier.height(4.dp))

                var isEventDetailsFocused by remember { mutableStateOf(false) }
                var eventDetailsFieldValue by remember { mutableStateOf(TextFieldValue(eventDetails)) }

                LaunchedEffect(eventDetails) {
                    if (eventDetailsFieldValue.text != eventDetails) {
                        eventDetailsFieldValue = TextFieldValue(eventDetails)
                    }
            }

                val isEventDetailsNotEmpty = eventDetails.trim().isNotEmpty()
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(min = 112.dp)
                        .padding(horizontal = 16.dp)
                        .background(
                            AmityTheme.colors.baseShade4,
                            RoundedCornerShape(8.dp)
                        )
                        .padding(vertical = 4.dp)
                ) {
                    AmityBasicTextField(
                        value = eventDetailsFieldValue,
                        maxChar = 1000,
                        onValueChange = { newValue: TextFieldValue ->
                            val filteredText = newValue.text.replace("\n", "")
                            if (filteredText.length > 1000) {
                                hapticFeedback.performHapticFeedback(HapticFeedbackType.LongPress)
                            } else {
                                eventDetailsFieldValue = newValue.copy(text = filteredText)
                                eventDetails = filteredText
                            }
                        },
                        textStyle = AmityTheme.typography.bodyLegacy.copy(
                            color = AmityTheme.colors.base
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 12.dp)
                            .onFocusChanged { focus ->
                                isEventDetailsFocused = focus.isFocused
                            },
                        placeholder = {
                            Text(
                                text = "Share details about the event and what to expect",
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

                Spacer(modifier = modifier.height(8.dp))

                Text(
                    modifier = Modifier.padding(horizontal = 16.dp),
                    text = "${eventDetails.length}/1,000",
                    style = AmityTheme.typography.caption.copy(
                        fontWeight = FontWeight.Normal,
                        color = AmityTheme.colors.baseShade1,
                    )
                )

                Spacer(modifier = modifier.height(24.dp))

                // Date and Time Section
                AmityBaseElement(
                    pageScope = getPageScope(),
                    elementId = "date_time_title"
                ) {
                    Text(
                        text = "Date and time",
                        style = AmityTheme.typography.titleBold,
                        color = AmityTheme.colors.base,
                        modifier = modifier
                            .padding(horizontal = 16.dp)
                            .testTag(getAccessibilityId())
                    )
                }

                Spacer(modifier = modifier.height(20.dp))

                // Timezone
                Column(
                    modifier = modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                ) {
                    Text(
                        text = "Timezone",
                        style = AmityTheme.typography.body,
                        color = AmityTheme.colors.base,
                    )
                    Spacer(modifier = modifier.height(8.dp))
                    Box(
                        modifier = modifier
                            .fillMaxWidth()
                            .background(
                                AmityTheme.colors.baseShade4,
                                RoundedCornerShape(8.dp)
                            )
                            .clickableWithoutRipple {
                                showTimezoneSelectionSheet = true
                            }
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween,
                            modifier = modifier
                                .fillMaxWidth()
                                .padding(12.dp)
                        ) {
                            Text(
                                text = TimezoneFormatter.format(selectedTimezone),
                                style = AmityTheme.typography.body,
                                color = AmityTheme.colors.base,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis,
                                modifier = modifier.weight(1f)
                            )
                            Icon(
                                painter = painterResource(com.amity.socialcloud.uikit.common.R.drawable.amity_arrow_down),
                                contentDescription = "Select timezone",
                                tint = AmityTheme.colors.baseShade2,
                                modifier = modifier.size(16.dp)
                            )
                        }
                    }
                }

                Spacer(modifier = modifier.height(16.dp))

                // Starts
                Column(
                    modifier = modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                ) {
                    Text(
                        text = "Starts",
                        style = AmityTheme.typography.body,
                        color = AmityTheme.colors.base,
                    )

                    Spacer(modifier = modifier.height(8.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        val dayFormat = DateTimeFormat.forPattern("MMM dd yyyy")
                        Text(
                            text = startDateTime.toString(dayFormat),
                            style = AmityTheme.typography.body,
                            color = AmityTheme.colors.base,
                            modifier = Modifier
                                .weight(1f)
                                .background(
                                    AmityTheme.colors.baseShade4,
                                    RoundedCornerShape(8.dp)
                                )
                                .clickableWithoutRipple {
                                    showStartDatePicker = true
                                }
                                .padding(12.dp)
                        )

                        val is24HourFormat = DateFormat.is24HourFormat(context)
                        val timeFormat = DateTimeFormat.forPattern(if (is24HourFormat) "HH:mm" else "hh:mm a")
                        Text(
                            text = startDateTime.toString(timeFormat),
                            style = AmityTheme.typography.body,
                            color = AmityTheme.colors.base,
                            modifier = Modifier
                                .weight(1f)
                                .background(
                                    AmityTheme.colors.baseShade4,
                                    RoundedCornerShape(8.dp)
                                )
                                .clickableWithoutRipple {
                                    showStartTimePicker = true
                                }
                                .padding(12.dp)
                        )
                    }
                }

                Spacer(modifier = modifier.height(if (hasEndDateTime && endDateTime != null) 16.dp else 4.dp))

                // Ends on
                Column(
                    modifier = modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                ) {
                    endDateTime?.let { currentEndDateTime ->
                        if (hasEndDateTime) {
                            // Show end date fields with trash icon
                            Text(
                                text = "Ends on",
                                style = AmityTheme.typography.body,
                                color = AmityTheme.colors.base,
                            )

                            Spacer(modifier = modifier.height(8.dp))

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(12.dp)
                            ) {
                                // Date box - takes half width
                                val dayFormat = DateTimeFormat.forPattern("dd MMM yyyy")
                                Text(
                                    text = currentEndDateTime.toString(dayFormat),
                                style = AmityTheme.typography.body,
                                color = AmityTheme.colors.base,
                                modifier = Modifier
                                    .weight(1f)
                                    .background(
                                        AmityTheme.colors.baseShade4,
                                        RoundedCornerShape(8.dp)
                                    )
                                    .clickableWithoutRipple {
                                        showEndDatePicker = true
                                    }
                                    .padding(12.dp)
                            )

                            // Time + Trash button - together take half width
                            Row(
                                modifier = Modifier.weight(1f),
                                horizontalArrangement = Arrangement.spacedBy(12.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                val is24HourFormat = DateFormat.is24HourFormat(context)
                                val timeFormat = DateTimeFormat.forPattern(if (is24HourFormat) "HH:mm" else "hh:mm a")
                                Text(
                                    text = currentEndDateTime.toString(timeFormat),
                                    style = AmityTheme.typography.body,
                                    color = AmityTheme.colors.base,
                                    modifier = Modifier
                                        .weight(1f)
                                        .background(
                                            AmityTheme.colors.baseShade4,
                                            RoundedCornerShape(8.dp)
                                        )
                                        .clickableWithoutRipple {
                                            showEndTimePicker = true
                                        }
                                        .padding(12.dp)
                                )

                                Icon(
                                    painter = painterResource(R.drawable.amity_ic_delete_trash),
                                    contentDescription = "Remove end date",
                                    tint = AmityTheme.colors.base,
                                    modifier = modifier
                                        .size(24.dp)
                                        .clickableWithoutRipple {
                                            endDateTime = null
                                            hasEndDateTime = false
                                        }
                                )
                            }
                        }
                        }
                    }

                    if (!hasEndDateTime || endDateTime == null) {
                        // Show helper text and "Add end date and time" button
                        Text(
                            text = "Event without specified end time will end after 12 hours.",
                            style = AmityTheme.typography.caption,
                            color = AmityTheme.colors.baseShade1
                        )

                        Spacer(modifier = modifier.height(16.dp))

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .border(
                                    width = 1.dp,
                                    color = AmityTheme.colors.secondaryShade3,
                                    shape = RoundedCornerShape(8.dp)
                                )
                                .clickableWithoutRipple {
                                    endDateTime = startDateTime.plusHours(1)
                                    hasEndDateTime = true
                                }
                                .padding(vertical = 12.dp, horizontal = 16.dp),
                            horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Add end date and time",
                                style = AmityTheme.typography.body.copy(
                                    fontWeight = FontWeight.SemiBold
                                ),
                                color = AmityTheme.colors.secondary
                            )
                        }
                    }
                }

                Spacer(modifier = modifier.height(16.dp))

                HorizontalDivider(
                    color = AmityTheme.colors.divider,
                    modifier = modifier.padding(horizontal = 16.dp)
                )

                Spacer(modifier = modifier.height(24.dp))

                // Location
                AmityBaseElement(
                    pageScope = getPageScope(),
                    elementId = "location_title"
                ) {
                    Text(
                        text = "Location",
                        style = AmityTheme.typography.titleBold,
                        color = AmityTheme.colors.base,
                        modifier = modifier
                            .padding(horizontal = 16.dp)
                            .testTag(getAccessibilityId())
                    )
                }

                Spacer(modifier = modifier.height(18.dp))

                // Display location based on type
                Box(
                    modifier = modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                ) {
                    val displayText = when {
                        // Not configured yet - show placeholder
                        !hasConfiguredLocation -> "Select where this event will be happening"
                        // In-person with address
                        locationData.eventType == AmityEventType.IN_PERSON && locationData.address.isNotEmpty() ->
                            locationData.address
                        // Virtual with Live Stream
                        locationData.eventType == AmityEventType.VIRTUAL &&
                        locationData.platform == EventPlatform.LIVE_STREAM ->
                            "Live stream"
                        // Virtual with External Platform and link
                        locationData.eventType == AmityEventType.VIRTUAL &&
                        locationData.platform == EventPlatform.EXTERNAL_PLATFORM &&
                        locationData.eventLink.isNotEmpty() ->
                            locationData.eventLink
                        // Default placeholder for other cases
                        else -> "Select where this event will be happening"
                    }

                    val isPlaceholder = displayText == "Select where this event will be happening"

                    Text(
                        text = displayText,
                        style = AmityTheme.typography.body,
                        color = if (isPlaceholder) AmityTheme.colors.baseShade3 else AmityTheme.colors.base,
                        maxLines = Int.MAX_VALUE,
                        modifier = modifier
                            .clickableWithoutRipple {
                                showLocationBottomSheet = true
                            }
                            .padding(end = 24.dp)
                    )
                    Icon(
                        painter = painterResource(com.amity.socialcloud.uikit.common.R.drawable.amity_ic_chevron_right),
                        contentDescription = "Select location",
                        tint = AmityTheme.colors.baseShade2,
                        modifier = modifier
                            .size(16.dp)
                            .align(Alignment.TopEnd)
                            .clickableWithoutRipple {
                                showLocationBottomSheet = true
                            }
                    )
                }

                Spacer(modifier = modifier.height(16.dp))

                HorizontalDivider(
                    color = AmityTheme.colors.divider,
                    modifier = modifier.padding(horizontal = 16.dp)
                )

                Spacer(modifier.height(96.dp))

                Spacer(modifier.height(40.dp))
            }

            // Bottom Button
            Column(
                modifier = modifier
                    .align(Alignment.BottomCenter)
                    .background(AmityTheme.colors.background)
            ) {
                HorizontalDivider(
                    color = AmityTheme.colors.divider,
                    modifier = modifier
                )

                Spacer(modifier = modifier.height(16.dp))
                Button(
                    colors = ButtonDefaults.buttonColors(
                        containerColor = AmityTheme.colors.primary,
                        disabledContainerColor = AmityTheme.colors.primaryShade3,
                    ),
                    shape = RoundedCornerShape(8.dp),
                    contentPadding = ButtonDefaults.ButtonWithIconContentPadding,
                    enabled = shouldActionButtonEnable && !isCreatingOrSaving,
                    modifier = Modifier
                        .height(40.dp)
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    onClick = {
                        // Validate that start time is at least 15 minutes from now
                        val now = DateTime.now()
                        val minimumTime = now.plusMinutes(15)

                        if (startDateTime.isBefore(minimumTime)) {
                            val errorMessage = when (mode) {
                                is AmityEventSetupPageMode.Create -> "Your event wasn't created as it needs to start at least 15 minutes from now."
                                is AmityEventSetupPageMode.Edit -> "Event could not be updated. The event must start at least 15 minutes from now."
                            }
                            AmityUIKitSnackbar.publishSnackbarErrorMessage(errorMessage)
                            return@Button
                        }

                        // Function to create or update event with optional cover image file ID
                        fun saveEvent(coverImageFileId: String? = null) {
                            val eventRepository = AmitySocialClient.newEventRepository()

                            // Determine event type based on location data
                            val eventType = when {
                                !hasConfiguredLocation -> AmityEventType.VIRTUAL
                                locationData.eventType == AmityEventType.IN_PERSON ->
                                    AmityEventType.IN_PERSON
                                locationData.eventType == AmityEventType.VIRTUAL &&
                                locationData.platform == EventPlatform.LIVE_STREAM ->
                                    AmityEventType.VIRTUAL
                                else -> AmityEventType.VIRTUAL
                            }

                            when (mode) {
                                is AmityEventSetupPageMode.Create -> {
                                    // Prevent multiple submissions
                                    if (isCreatingOrSaving) return
                                    isCreatingOrSaving = true

                                    // Show "Creating..." toast
                                    getPageScope()?.showSnackbar("Creating...")

                                    val communityId = mode.communityId.orEmpty()

                                    // Ensure communityId is provided
                                    if (communityId.isBlank()) {
                                        AmityUIKitSnackbar.publishSnackbarErrorMessage("Community ID is required")
                                        isCreatingOrSaving = false
                                        return
                                    }

                                    val timezoneId = selectedTimezone.id.orEmpty()

                                    val builder = eventRepository.createEvent()
                                        .title(eventName.trim())
                                        .description(eventDetails.trim())
                                        .type(eventType)
                                        .isInviteOnly(false) // Default to public event
                                        .startTime(startDateTime)
                                        .timezone(timezoneId)
                                        .originType(com.amity.socialcloud.sdk.model.social.event.AmityEventOriginType.COMMUNITY)
                                        .originId(communityId)

                                    // Add cover image if uploaded
                                    coverImageFileId?.let {
                                        builder.coverImageFileId(it)
                                    }

                                    // End time is required - use user's end time or default to 12 hours from start
                                    val finalEndDateTime = endDateTime ?: startDateTime.plusHours(12)
                                    builder.endTime(finalEndDateTime)

                                    // Add location if configured
                                    if (hasConfiguredLocation) {
                                        when (locationData.eventType) {
                                            AmityEventType.IN_PERSON -> {
                                                if (locationData.address.isNotBlank()) {
                                                    builder.location(locationData.address.trim())
                                                }
                                            }
                                            AmityEventType.VIRTUAL -> {
                                                when (locationData.platform) {
                                                    EventPlatform.EXTERNAL_PLATFORM -> {
                                                        if (locationData.eventLink.isNotBlank()) {
                                                            builder.externalUrl(locationData.eventLink.trim())
                                                        }
                                                    }
                                                    EventPlatform.LIVE_STREAM -> {
                                                        // Live stream doesn't need additional URL
                                                    }
                                                    null -> {
                                                        // No platform selected
                                                    }
                                                }
                                            }
                                            AmityEventType.UNKNOWN -> {
                                                // Unknown event type
                                            }
                                            null -> {
                                                // No event type selected
                                            }
                                        }
                                    }

                                    builder.build()
                                        .create()
                                        .subscribeOn(Schedulers.io())
                                        .observeOn(AndroidSchedulers.mainThread())
                                        .doOnSuccess { event ->
                                            isCreatingOrSaving = false
                                            // Navigate to event detail page with success toast
                                            val intent = AmityEventDetailPageActivity.newIntent(
                                                context = context,
                                                eventId = event.getEventId(),
                                                showSuccessToast = true
                                            )
                                            context.startActivity(intent)
                                            context.closePageWithResult(Activity.RESULT_OK)
                                        }
                                        .doOnError { error ->
                                            isCreatingOrSaving = false
                                            getPageScope()?.showSnackbar("Failed to create event. Please try again.")
                                        }
                                        .subscribe()
                                }
                                is AmityEventSetupPageMode.Edit -> {
                                    // Prevent multiple submissions
                                    if (isCreatingOrSaving) return
                                    isCreatingOrSaving = true

                                    // Show "Saving..." toast
                                    getPageScope()?.showSnackbar("Saving...")

                                    val eventId = mode.eventId
                                    val builder = eventRepository.updateEvent(eventId)

                                    // Only update changed fields
                                    if (eventName != originalEventName.value) {
                                        builder.title(eventName.trim())
                                    }

                                    if (eventDetails != originalEventDetails.value) {
                                        builder.description(eventDetails.trim())
                                    }

                                    if (startDateTime != originalStartDateTime.value) {
                                        builder.startTime(startDateTime)
                                    }

                                    if (endDateTime != originalEndDateTime.value) {
                                        val finalEndDateTime = endDateTime ?: startDateTime.plusHours(12)
                                        builder.endTime(finalEndDateTime)
                                    }

                                    if (selectedTimezone.id != originalTimezone.value?.id) {
                                        val timezoneId = selectedTimezone.id.orEmpty()
                                        builder.timezone(timezoneId)
                                    }

                                    // Update cover image if new image uploaded
                                    coverImageFileId?.let {
                                        builder.coverImageFileId(it)
                                    }

                                    // Update location if changed
                                    if (locationData != originalLocationData.value) {
                                        // Determine event type based on location data
                                        val eventType = when {
                                            locationData.eventType == AmityEventType.IN_PERSON ->
                                                AmityEventType.IN_PERSON
                                            locationData.eventType == AmityEventType.VIRTUAL &&
                                            locationData.platform == EventPlatform.LIVE_STREAM ->
                                                AmityEventType.VIRTUAL
                                            else -> AmityEventType.VIRTUAL
                                        }

                                        builder.type(eventType)

                                        when (locationData.eventType) {
                                            AmityEventType.IN_PERSON -> {
                                                if (locationData.address.isNotBlank()) {
                                                    builder.location(locationData.address.trim())
                                                }
                                            }
                                            AmityEventType.VIRTUAL -> {
                                                when (locationData.platform) {
                                                    EventPlatform.EXTERNAL_PLATFORM -> {
                                                        if (locationData.eventLink.isNotBlank()) {
                                                            builder.externalUrl(locationData.eventLink.trim())
                                                        }
                                                    }
                                                    EventPlatform.LIVE_STREAM -> {
                                                        // Live stream doesn't need additional URL
                                                    }
                                                    null -> {
                                                        // No platform selected
                                                    }
                                                }
                                            }
                                            AmityEventType.UNKNOWN -> {
                                                // Unknown event type
                                            }
                                            null -> {
                                                // No event type selected
                                            }
                                        }
                                    }

                                    builder.build()
                                        .apply()
                                        .subscribeOn(Schedulers.io())
                                        .observeOn(AndroidSchedulers.mainThread())
                                        .doOnSuccess { event ->
                                            isCreatingOrSaving = false
                                            // Show success toast and pop back
                                            AmityUIKitSnackbar.publishSnackbarMessage("Successfully updated event.")
                                            context.closePageWithResult(Activity.RESULT_OK)
                                        }
                                        .doOnError { error ->
                                            isCreatingOrSaving = false
                                            getPageScope()?.showSnackbar("Failed to update event. Please try again.")
                                        }
                                        .subscribe()
                                }
                            }
                        }

                        // Upload cover image if present, then save event
                        if (coverImageUri != Uri.EMPTY && isCapturedImageReady) {
                            AmityCoreClient.newFileRepository()
                                .uploadImage(coverImageUri)
                                .subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .doOnNext { uploadResult ->
                                    when (uploadResult) {
                                        is AmityUploadResult.COMPLETE -> {
                                            val fileId = uploadResult.getFile().getFileId()
                                            saveEvent(fileId)
                                        }
                                        is AmityUploadResult.ERROR -> {
                                            isCreatingOrSaving = false
                                            val error = uploadResult.getError().code
                                            showCoverImageErrorUploadDialog = if (error == AmityError.BUSINESS_ERROR.code) {
                                                Pair(true, error)
                                            } else {
                                                Pair(true, 0)
                                            }
                                        }
                                        AmityUploadResult.CANCELLED -> {
                                            isCreatingOrSaving = false
                                            showCoverImageErrorUploadDialog = Pair(true, 0)
                                        }
                                        is AmityUploadResult.PROGRESS -> {
                                            Log.d("AmityEventSetupPage", "Cover image uploading: ${uploadResult.getUploadInfo().getProgressPercentage()}%")
                                        }
                                        else -> {
                                            Log.d("AmityEventSetupPage", "Cover image upload status: ${uploadResult.javaClass.simpleName}")
                                        }
                                    }
                                }
                                .doOnError { error ->
                                    isCreatingOrSaving = false
                                    showCoverImageErrorUploadDialog = Pair(true, 0)
                                }
                                .subscribe()
                        } else {
                            // No cover image, save event directly
                            saveEvent()
                        }
                    }
                ) {
                    AmityBaseElement(
                        pageScope = getPageScope(),
                        elementId = if (isInEditMode) "event_update_button" else "event_create_button"
                    ) {
                        if (isInEditMode) {
                            // Edit mode: No icon, just text
                            Text(
                                text = "Save",
                                style = AmityTheme.typography.body.copy(
                                    fontWeight = FontWeight.SemiBold,
                                    color = Color.White,
                                ),
                                textAlign = TextAlign.Center,
                                modifier = Modifier.testTag(getAccessibilityId())
                            )
                        } else {
                            // Create mode: Icon + text
                            Row(
                                horizontalArrangement = Arrangement.Center,
                                verticalAlignment = Alignment.CenterVertically,
                            ) {
                                Icon(
                                    painter = painterResource(com.amity.socialcloud.uikit.common.R.drawable.amity_ic_add),
                                    contentDescription = "Create",
                                    tint = Color.White,
                                    modifier = modifier.size(16.dp)
                                )
                                Spacer(modifier = modifier.width(8.dp))
                                Text(
                                    text = "Create event",
                                    style = AmityTheme.typography.body.copy(
                                        fontWeight = FontWeight.SemiBold,
                                        color = Color.White,
                                    ),
                                    textAlign = TextAlign.Center,
                                    modifier = Modifier.testTag(getAccessibilityId())
                                )
                            }
                        }
                    }
                }
                Spacer(modifier = modifier.height(40.dp))
            }
        }

        // Media Selection Sheet
        if (showMediaCameraSelectionSheet) {
            AmityMediaImageSelectionSheet(
                modifier = modifier,
                pageScope = getPageScope(),
            ) { type ->
                showMediaCameraSelectionSheet = false

                type?.let {
                    when (type) {
                        AmityMediaImageSelectionType.CAMERA -> {
                            val permissions = arrayOf(
                                android.Manifest.permission.CAMERA,
                            )
                            cameraPermissionLauncher.launch(permissions)
                        }

                        AmityMediaImageSelectionType.IMAGE -> {
                            imagePickerLauncher.launch(
                                PickVisualMediaRequest(
                                    mediaType = ActivityResultContracts.PickVisualMedia.ImageOnly
                                )
                            )
                        }
                    }
                }
            }
        }

        // Timezone Selection Sheet
        if (showTimezoneSelectionSheet) {
            AmityTimezoneListBottomSheet(
                modifier = modifier,
                pageScope = getPageScope(),
                shouldShow = showTimezoneSelectionSheet,
                onDismiss = { showTimezoneSelectionSheet = false },
                onTimezoneSelected = { timezone ->
                    selectedTimezone = timezone
                    showTimezoneSelectionSheet = false
                }
            )
        }

        // Location Bottom Sheet
        if (showLocationBottomSheet) {
            AmityLocationBottomSheet(
                modifier = modifier,
                pageScope = getPageScope(),
                shouldShow = showLocationBottomSheet,
                initialData = locationData,
                onDismiss = { showLocationBottomSheet = false },
                onDone = { data ->
                    locationData = data
                    hasConfiguredLocation = true
                    showLocationBottomSheet = false
                }
            )
        }

        // Start Date Picker
        if (showStartDatePicker) {
            AmityDatePickerDialog(
                datePickerState = startDatePickerState,
                onDismissRequest = { showStartDatePicker = false },
                confirmButton = {
                    TextButton(
                        onClick = {
                            showStartDatePicker = false
                            startDatePickerState.selectedDateMillis?.let { millis ->
                                val date = DateTime(millis)
                                var newStartDateTime = DateTime(
                                    date.year,
                                    date.monthOfYear,
                                    date.dayOfMonth,
                                    startTimePickerState.hour,
                                    startTimePickerState.minute
                                )

                                // If the selected time is less than 15 minutes from now, adjust it
                                val now = DateTime.now()
                                val minimumTime = now.plusMinutes(15)

                                if (newStartDateTime.isBefore(minimumTime)) {
                                    newStartDateTime = minimumTime
                                    // Update the time picker state to reflect the adjusted time
                                    startTimePickerState.hour = minimumTime.hourOfDay
                                    startTimePickerState.minute = minimumTime.minuteOfHour
                                }

                                startDateTime = newStartDateTime

                                // If end date is set and new start date is after or equal to it, update end date = start date + 1 hour
                                endDateTime?.let { currentEndDateTime ->
                                    if (!newStartDateTime.isBefore(currentEndDateTime)) {
                                        endDateTime = newStartDateTime.plusHours(1)
                                    }
                                }
                            }
                        }
                    ) {
                        Text("OK", color = AmityTheme.colors.primary)
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showStartDatePicker = false }) {
                        Text("CANCEL", color = AmityTheme.colors.primary)
                    }
                }
            )
        }

        // Start Time Picker
        if (showStartTimePicker) {
            AmityTimePickerDialog(
                timePickerState = startTimePickerState,
                onDismissRequest = { showStartTimePicker = false },
                confirmButton = {
                    TextButton(
                        onClick = {
                            showStartTimePicker = false
                            var newStartDateTime = DateTime(
                                startDateTime.year,
                                startDateTime.monthOfYear,
                                startDateTime.dayOfMonth,
                                startTimePickerState.hour,
                                startTimePickerState.minute
                            )

                            // If the selected time is less than 15 minutes from now, adjust it
                            val now = DateTime.now()
                            val minimumTime = now.plusMinutes(15)

                            if (newStartDateTime.isBefore(minimumTime)) {
                                newStartDateTime = minimumTime
                                // Update the time picker state to reflect the adjusted time
                                startTimePickerState.hour = minimumTime.hourOfDay
                                startTimePickerState.minute = minimumTime.minuteOfHour
                            }

                            startDateTime = newStartDateTime

                            // If end date is set and new start time is after or equal to it, update end date = start date + 1 hour
                            endDateTime?.let { currentEndDateTime ->
                                if (!newStartDateTime.isBefore(currentEndDateTime)) {
                                    endDateTime = newStartDateTime.plusHours(1)
                                }
                            }
                        }
                    ) {
                        Text("OK", color = AmityTheme.colors.primary)
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showStartTimePicker = false }) {
                        Text("CANCEL", color = AmityTheme.colors.primary)
                    }
                }
            )
        }

        // End Date Picker
        if (showEndDatePicker) {
            AmityDatePickerDialog(
                datePickerState = endDatePickerState,
                onDismissRequest = { showEndDatePicker = false },
                confirmButton = {
                    TextButton(
                        onClick = {
                            showEndDatePicker = false
                            endDatePickerState.selectedDateMillis?.let { millis ->
                                val date = DateTime(millis)
                                var newEndDateTime = DateTime(
                                    date.year,
                                    date.monthOfYear,
                                    date.dayOfMonth,
                                    endTimePickerState.hour,
                                    endTimePickerState.minute
                                )

                                // If end date/time is before or equal to start date/time, set to start + 1 hour
                                if (!newEndDateTime.isAfter(startDateTime)) {
                                    newEndDateTime = startDateTime.plusHours(1)
                                    // Update the time picker state to reflect the adjusted time
                                    endTimePickerState.hour = newEndDateTime.hourOfDay
                                    endTimePickerState.minute = newEndDateTime.minuteOfHour
                                }

                                endDateTime = newEndDateTime
                            }
                        }
                    ) {
                        Text("OK", color = AmityTheme.colors.primary)
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showEndDatePicker = false }) {
                        Text("CANCEL", color = AmityTheme.colors.primary)
                    }
                }
            )
        }

        // End Time Picker
        if (showEndTimePicker) {
            AmityTimePickerDialog(
                timePickerState = endTimePickerState,
                onDismissRequest = { showEndTimePicker = false },
                confirmButton = {
                    TextButton(
                        onClick = {
                            showEndTimePicker = false
                            endDateTime?.let { currentEndDateTime ->
                                var newEndDateTime = DateTime(
                                    currentEndDateTime.year,
                                    currentEndDateTime.monthOfYear,
                                    currentEndDateTime.dayOfMonth,
                                    endTimePickerState.hour,
                                    endTimePickerState.minute
                                )

                                // If end date/time is before or equal to start date/time, set to start + 1 hour
                                if (!newEndDateTime.isAfter(startDateTime)) {
                                    newEndDateTime = startDateTime.plusHours(1)
                                    // Update the time picker state to reflect the adjusted time
                                    endTimePickerState.hour = newEndDateTime.hourOfDay
                                    endTimePickerState.minute = newEndDateTime.minuteOfHour
                                }

                                endDateTime = newEndDateTime
                            }
                        }
                    ) {
                        Text("OK", color = AmityTheme.colors.primary)
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showEndTimePicker = false }) {
                        Text("CANCEL", color = AmityTheme.colors.primary)
                    }
                }
            )
        }

        // Leave Confirmation Dialog
        if (showLeaveConfirmDialog) {
            val dialogText = if (isInEditMode) {
                "Your changes that you made may not be saved."
            } else {
                "Your progress won't be saved and your event won't be created."
            }

            AmityAlertDialog(
                dialogTitle = "Leave without finishing?",
                dialogText = dialogText,
                confirmText = "Leave",
                dismissText = "Cancel",
                confirmTextColor = AmityTheme.colors.alert,
                onConfirmation = { context.closePage() },
                onDismissRequest = { showLeaveConfirmDialog = false }
            )
        }

        // Cover Image Upload Error Dialog
        if (showCoverImageErrorUploadDialog.first) {
            val isInappropriateImage = showCoverImageErrorUploadDialog.second == AmityError.BUSINESS_ERROR.code
            AmityAlertDialog(
                dialogTitle = if (isInappropriateImage) "Inappropriate image" else "Upload failed",
                dialogText = if (isInappropriateImage) "Please choose a different image." else "Please try again.",
                dismissText = "OK",
                onDismissRequest = {
                    showCoverImageErrorUploadDialog = Pair(false, 0)
                },
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun AmityEventSetupPagePreview() {
    AmityUIKitConfigController.setup(LocalContext.current)
    AmityEventSetupPage()
}
