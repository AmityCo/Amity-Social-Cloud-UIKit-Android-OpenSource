package com.amity.socialcloud.uikit.community.compose.event.detail

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.LocalViewModelStoreOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import coil3.compose.AsyncImage
import coil3.compose.rememberAsyncImagePainter
import coil3.request.ImageRequest
import coil3.request.crossfade
import coil3.disk.DiskCache
import coil3.memory.MemoryCache
import com.amity.socialcloud.sdk.model.core.file.AmityImage
import com.amity.socialcloud.sdk.model.social.event.AmityEvent
import com.amity.socialcloud.sdk.model.social.community.AmityCommunity
import com.amity.socialcloud.uikit.common.ui.base.AmityBaseElement
import com.amity.socialcloud.uikit.common.ui.base.AmityBasePage
import com.amity.socialcloud.uikit.common.ui.elements.AmityAlertDialog
import com.amity.socialcloud.uikit.common.ui.elements.AmityMenuButton
import com.amity.socialcloud.uikit.common.ui.scope.AmityComposePageScope
import com.amity.socialcloud.uikit.common.ui.theme.AmityTheme
import com.amity.socialcloud.uikit.community.compose.R
import com.amity.socialcloud.uikit.community.compose.event.detail.components.AmityEventInfoComponent
import com.amity.socialcloud.uikit.community.compose.event.detail.components.amityEventDiscussionFeedItems
import com.amity.socialcloud.uikit.community.compose.event.detail.elements.AmityEventDiscussionActionsBottomSheet
import com.amity.socialcloud.uikit.community.compose.event.detail.elements.AmityEventMenuBottomSheet
import com.amity.socialcloud.uikit.community.compose.post.composer.poll.AmityPollPostTypeSelectionBottomSheet
import com.amity.socialcloud.uikit.community.compose.ui.shimmer.AmityEventDetailShimmer
import com.amity.socialcloud.uikit.community.compose.ui.shimmer.AmityEventAboutTabShimmer
import android.app.Activity
import android.content.Context
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import kotlinx.coroutines.flow.catch
import org.joda.time.format.DateTimeFormat
import com.amity.socialcloud.sdk.api.social.AmitySocialClient
import com.amity.socialcloud.sdk.api.core.AmityCoreClient
import com.amity.socialcloud.sdk.model.core.permission.AmityPermission
import com.amity.socialcloud.sdk.model.social.post.AmityPost
import com.amity.socialcloud.sdk.model.core.pin.AmityPinnedPost
import com.amity.socialcloud.uikit.community.compose.post.detail.AmityPostCategory
import com.amity.socialcloud.uikit.common.ad.AmityListItem
import com.amity.socialcloud.uikit.common.ad.AmityAdInjector
import com.amity.socialcloud.sdk.model.core.ad.AmityAdPlacement
import com.amity.socialcloud.uikit.community.compose.AmitySocialBehaviorHelper
import com.amity.socialcloud.uikit.community.compose.event.detail.AmityEventDetailPageBehavior
import androidx.paging.compose.collectAsLazyPagingItems
import io.reactivex.rxjava3.schedulers.Schedulers
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import java.util.concurrent.TimeUnit
import com.amity.socialcloud.sdk.helper.core.coroutines.asFlow
import com.amity.socialcloud.sdk.model.social.event.AmityEventResponseStatus
import com.amity.socialcloud.sdk.model.social.event.AmityEventStatus
import com.amity.socialcloud.sdk.model.social.event.AmityEventType
import com.amity.socialcloud.uikit.common.eventbus.AmityUIKitSnackbar
import com.amity.socialcloud.uikit.common.ui.elements.AmityAvatarView
import com.amity.socialcloud.uikit.common.ui.elements.AmityUserAvatarView
import com.amity.socialcloud.uikit.common.utils.clickableWithoutRipple
import com.amity.socialcloud.uikit.common.utils.closePage
import com.amity.socialcloud.uikit.common.common.readableNumber
import com.amity.socialcloud.uikit.common.utils.isVisitor
import com.amity.socialcloud.uikit.community.compose.event.attendees.AmityEventAttendeesPageActivity
import com.amity.socialcloud.uikit.community.compose.event.detail.components.AmityEventDiscussionFeedComponent
import com.amity.socialcloud.uikit.community.compose.livestream.room.create.AmityCreateRoomPageActivity
import kotlinx.coroutines.launch
import com.amity.socialcloud.uikit.community.compose.event.formatEventTimestamp
import org.joda.time.DateTime

private fun android.content.Context.closePage() {
    (this as? Activity)?.finish()
}

/**
 * Event detail page with sticky header behavior similar to community profile
 */
@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
@Composable
fun AmityEventDetailPage(
    modifier: Modifier = Modifier,
    eventId: String,
    showSuccessToast: Boolean = false,
    pageScope: AmityComposePageScope? = null,
    onBackClick: () -> Unit = {}
) {
    val viewModelStoreOwner = checkNotNull(LocalViewModelStoreOwner.current) {
        "No ViewModelStoreOwner was provided via LocalViewModelStoreOwner"
    }

    val viewModel = viewModel<AmityEventDetailViewModel>(
        viewModelStoreOwner = viewModelStoreOwner,
        factory = object : ViewModelProvider.Factory {
            override fun <VM : androidx.lifecycle.ViewModel> create(modelClass: Class<VM>): VM {
                return AmityEventDetailViewModel(eventId) as VM
            }
        }
    )

    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    val lazyListState = rememberLazyListState()
    var isHeaderSticky by remember { mutableStateOf(false) }
    var selectedTabIndex by remember { mutableStateOf(0) }
    var showCreatePostBottomSheet by remember { mutableStateOf(false) }
    var showPollSelectionBottomSheet by remember { mutableStateOf(false) }
    var showEventMenuBottomSheet by remember { mutableStateOf(false) }
    var showDeleteConfirmDialog by remember { mutableStateOf(false) }
    var showRsvpBottomSheet by remember { mutableStateOf(false) }
    var showChangeRsvpBottomSheet by remember { mutableStateOf(false) }
    var showJoinCommunityBottomSheet by remember { mutableStateOf(false) }
    var showPendingApprovalDialog by remember { mutableStateOf(false) }
    var showEditingNotPossibleDialog by remember { mutableStateOf(false) }
    var showPendingJoinDialog by remember { mutableStateOf(false) }

    // Get error state from ViewModel
    val eventDetailState by viewModel.eventDetailState.collectAsState()
    val hasError = eventDetailState is AmityEventDetailViewModel.EventDetailState.Error

    val event by viewModel.getEvent()
        .collectAsState(initial = null)

    // Get RSVP status from event: null = no RSVP, true = going, false = not going
    var isGoing by remember { mutableStateOf<Boolean?>(null) }
    var isRsvpLoading by remember { mutableStateOf(false) }
    var hasLoadedRsvp by remember { mutableStateOf(false) }

    // Observe RSVP status - only fetch once when event is loaded
    LaunchedEffect(event, eventDetailState) {
        if (event != null && !hasLoadedRsvp && !hasError) {
            hasLoadedRsvp = true
            isRsvpLoading = true
            event!!.getMyRSVP()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    { response ->
                        isGoing = when (response.getStatus()) {
                            AmityEventResponseStatus.GOING -> true
                            AmityEventResponseStatus.NOT_GOING -> false
                            else -> null
                        }
                        isRsvpLoading = false
                    },
                    { error ->
                        isGoing = null
                        isRsvpLoading = false
                    }
                )
        } else if (hasError) {
            // If there's an error, stop RSVP loading immediately
            isRsvpLoading = false
        }
    }

    // Show success toast when coming from event creation
    LaunchedEffect(showSuccessToast) {
        if (showSuccessToast) {
            AmityUIKitSnackbar.publishSnackbarMessage("Successfully created event.")
        }
    }

    // Update ViewModel with event data
    LaunchedEffect(event, isGoing) {
        viewModel.updateEventData(event, isGoing)
    }

    // Collect ViewModel states
    val communityId by viewModel.communityId.collectAsState()
    val excludedPostIds by viewModel.excludedPostIds.collectAsState()
    val community by remember(communityId) {
        if (!communityId.isNullOrEmpty()) {
            viewModel.getCommunity(communityId!!)
        } else {
            kotlinx.coroutines.flow.flowOf(null)
        }
    }.collectAsState(initial = null)

    // Get target community for membership check
    val targetCommunityId = event?.getTargetCommunity()?.getCommunityId() ?: ""
    val targetCommunity by remember(targetCommunityId) {
        if (targetCommunityId.isNotEmpty()) {
            viewModel.getCommunity(targetCommunityId)
        } else {
            kotlinx.coroutines.flow.flowOf(null)
        }
    }.collectAsState(initial = null)

    // Check if user is a member of the target community
    val isMember = targetCommunity?.isJoined() == true

    // Check if user has a pending join request
    val hasPendingJoinRequest = targetCommunity?.getLocalJoinRequest()?.find {
        it.getRequestorPublicId() == AmityCoreClient.getUserId() &&
                it.getStatus() == com.amity.socialcloud.sdk.model.social.community.AmityJoinRequestStatus.PENDING
    } != null


    // Get permissions from ViewModel
    val hasDeleteEventPermission by viewModel.hasDeleteEventPermission.collectAsState()

    // Setup paging data for discussion feed
    val announcementPosts = remember(communityId) {
        if (!communityId.isNullOrEmpty()) {
            AmitySocialClient.newPostRepository()
                .getPinnedPosts(
                    communityId = communityId!!,
                    placement = AmityPinnedPost.PinPlacement.ANNOUNCEMENT.value
                )
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .asFlow()
                .catch { }
        } else {
            kotlinx.coroutines.flow.flowOf(androidx.paging.PagingData.empty())
        }
    }.collectAsLazyPagingItems()

    val pinPosts = remember(communityId) {
        if (!communityId.isNullOrEmpty()) {
            AmitySocialClient.newPostRepository()
                .getPinnedPosts(
                    communityId = communityId!!,
                    placement = AmityPinnedPost.PinPlacement.DEFAULT.value
                )
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .asFlow()
                .catch { }
        } else {
            kotlinx.coroutines.flow.flowOf(androidx.paging.PagingData.empty())
        }
    }.collectAsLazyPagingItems()

    val communityPosts = remember(communityId) {
        if (!communityId.isNullOrEmpty()) {
            val injector = AmityAdInjector<AmityPost>(
                placement = AmityAdPlacement.FEED,
                communityId = communityId!!,
            )

            AmitySocialClient.newFeedRepository()
                .getCommunityFeed(communityId!!)
                .includeDeleted(false)
                .dataTypes(AmitySocialBehaviorHelper.supportedPostTypes)
                .matchingOnlyParentPosts(true)
                .build()
                .query()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .onBackpressureBuffer()
                .throttleLatest(2000, TimeUnit.MILLISECONDS)
                .map { injector.inject(it) }
                .asFlow()
                .catch {}
        } else {
            kotlinx.coroutines.flow.flowOf(androidx.paging.PagingData.empty())
        }
    }.collectAsLazyPagingItems()

    // Sticky header logic - moved outside to work for both tabs
    LaunchedEffect(lazyListState) {
        snapshotFlow { lazyListState.firstVisibleItemIndex }
            .collect { index ->
                if (index > 3 && !isHeaderSticky) {
                    isHeaderSticky = true
                } else if (index <= 3 && isHeaderSticky) {
                    isHeaderSticky = false
                }
            }
    }

    AmityBasePage(pageId = "event_detail_page") {
        Scaffold(
            modifier = modifier.fillMaxSize(),
            containerColor = AmityTheme.colors.background
        ) { paddingValues ->
            Box(
                modifier = Modifier
                    .padding(PaddingValues(bottom = paddingValues.calculateBottomPadding()))
                    .fillMaxSize()
            ) {
                // Show error state if loading failed
                if (hasError) {
                    // Center content
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center,
                            modifier = Modifier.padding(horizontal = 32.dp)
                        ) {
                            Icon(
                                painter = painterResource(R.drawable.amity_ic_unable_to_load),
                                contentDescription = "Error",
                                tint = AmityTheme.colors.baseShade4,
                                modifier = Modifier.size(60.dp)
                            )

                            Spacer(modifier = Modifier.height(16.dp))

                            Text(
                                text = "Something went wrong",
                                style = AmityTheme.typography.headLine.copy(
                                    fontWeight = FontWeight.Bold
                                ),
                                color = AmityTheme.colors.baseShade3
                            )

                            Spacer(modifier = Modifier.height(8.dp))

                            Text(
                                text = "The content you're looking for is unavailable.",
                                style = AmityTheme.typography.body,
                                color = AmityTheme.colors.baseShade3,
                                textAlign = TextAlign.Center
                            )

                            Spacer(modifier = Modifier.height(24.dp))

                            // Go back button
                            Button(
                                onClick = onBackClick,
                                modifier = Modifier.height(48.dp),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = AmityTheme.colors.primary
                                ),
                                shape = RoundedCornerShape(8.dp),
                                contentPadding = PaddingValues(
                                    horizontal = 24.dp,
                                    vertical = 10.dp
                                )
                            ) {
                                Text(
                                    text = "Go back",
                                    style = AmityTheme.typography.body.copy(
                                        fontWeight = FontWeight.Bold
                                    ),
                                    color = Color.White
                                )
                            }
                        }
                    }
                } else {
                    // Single LazyColumn for both tabs
                    LazyColumn(
                        state = lazyListState,
                        modifier = Modifier
                            .fillMaxSize()
                            .background(AmityTheme.colors.background)
                    ) {
                        stickyHeader {
                            if (isHeaderSticky && event != null) {
                                val showMenu =
                                    viewModel.shouldShowMenu(isGoing)
                                Column(
                                    modifier = Modifier.background(AmityTheme.colors.background)
                                ) {
                                    EventCollapsedHeader(
                                        event = event!!,
                                        onBackClick = onBackClick,
                                        onMenuClick = if (showMenu) {
                                            { showEventMenuBottomSheet = true }
                                        } else null
                                    )
                                    EventTabRow(
                                        selectedIndex = selectedTabIndex,
                                        onTabSelected = { selectedTabIndex = it }
                                    )
                                }
                            }
                        }

                        item {
                            if (event != null && !isRsvpLoading) {
                                val showMenu =
                                    viewModel.shouldShowMenu(isGoing)
                                EventExpandedHeader(
                                    event = event!!,
                                    onBackClick = onBackClick,
                                    onMenuClick = if (showMenu) {
                                        { showEventMenuBottomSheet = true }
                                    } else null
                                )
                                EventTitleSection(event = event!!)
                                EventDetailsSection(
                                    event = event!!,
                                    isGoing = isGoing,
                                    showRsvpBottomSheet = showRsvpBottomSheet,
                                    onRsvpBottomSheetChange = { showRsvpBottomSheet = it },
                                    showChangeRsvpBottomSheet = showChangeRsvpBottomSheet,
                                    onChangeRsvpBottomSheetChange = {
                                        showChangeRsvpBottomSheet = it
                                    },
                                    onJoinCommunityBottomSheetChange = {
                                        showJoinCommunityBottomSheet = it
                                    },
                                    isMember = isMember,
                                    hasPendingJoinRequest = hasPendingJoinRequest,
                                    showPendingJoinDialog = showPendingJoinDialog,
                                    onShowPendingJoinDialogChange = { showPendingJoinDialog = it },
                                    onIsGoingChange = { isGoing = it },
                                    viewModel = viewModel,
                                    community = community
                                )
                            } else {
                                AmityEventDetailShimmer()
                            }
                        }

                        item {
                            if (event != null) {
                                EventTabRow(
                                    selectedIndex = selectedTabIndex,
                                    onTabSelected = { selectedTabIndex = it }
                                )
                            }
                        }

                        // Conditional content based on selected tab
                        when (selectedTabIndex) {
                            0 -> {
                                // About tab
                                item {
                                    if (event != null) {
                                        AmityEventInfoComponent(
                                            event = event!!,
                                            onAddressCopied = {
                                                AmityUIKitSnackbar.publishSnackbarMessage("Address copied")
                                            },
                                            onLinkCopied = {
                                                AmityUIKitSnackbar.publishSnackbarMessage("Link copied.")
                                            }
                                        )
                                    } else {
                                        AmityEventAboutTabShimmer()
                                    }
                                }
                            }

                            1 -> {
                                // Discussion tab
                                if (event != null && !communityId.isNullOrEmpty()) {
                                    amityEventDiscussionFeedItems(
                                        modifier = Modifier,
                                        pageScope = pageScope,
                                        announcementPosts = announcementPosts,
                                        pinPosts = pinPosts,
                                        communityPosts = communityPosts,
                                        excludedPostIds = excludedPostIds,
                                        eventHostId = null,
                                        onClick = { post, _ ->
                                            val behavior =
                                                AmitySocialBehaviorHelper.eventDetailPageBehavior
                                            behavior.goToPostDetailPage(
                                                AmityEventDetailPageBehavior.Context(
                                                    pageContext = context,
                                                ),
                                                postId = post.getPostId(),
                                                category = AmityPostCategory.GENERAL
                                            )
                                        }
                                    )
                                }
                            }
                        }
                    } // Close LazyColumn
                } // Close else block

                // FAB for creating posts (only show in Discussion tab)
                if (selectedTabIndex == 1 && event != null && !hasError) {
                    AmityBaseElement(
                        pageScope = pageScope,
                        elementId = "event_discussion_create_post_button",
                    ) {
                        if (community != null) {
                            FloatingActionButton(
                                onClick = {
                                    showCreatePostBottomSheet = true
                                },
                                shape = RoundedCornerShape(size = 32.dp),
                                containerColor = AmityTheme.colors.primary,
                                modifier = Modifier
                                    .padding(16.dp)
                                    .size(64.dp)
                                    .align(Alignment.BottomEnd)
                            ) {
                                Icon(
                                    painter = painterResource(id = R.drawable.amity_ic_plus),
                                    contentDescription = "create post",
                                    tint = Color.White,
                                    modifier = Modifier.size(32.dp)
                                )
                            }
                        }
                    }
                }

                // Bottom sheet for post creation actions
                if (community != null && !hasError) {
                    AmityEventDiscussionActionsBottomSheet(
                        community = community!!,
                        shouldShow = showCreatePostBottomSheet,
                        showPollTypeSelectionSheet = {
                            showCreatePostBottomSheet = false
                            showPollSelectionBottomSheet = true
                        },
                        onDismiss = { showCreatePostBottomSheet = false }
                    )
                }

                // Poll type selection bottom sheet
                if (showPollSelectionBottomSheet && community != null && !hasError) {
                    val context = LocalContext.current
                    val behavior by lazy {
                        AmitySocialBehaviorHelper.eventDetailPageBehavior
                    }
                    ModalBottomSheet(
                        onDismissRequest = {
                            showPollSelectionBottomSheet = false
                        },
                        sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true),
                        containerColor = AmityTheme.colors.background,
                        contentWindowInsets = { WindowInsets.waterfall },
                        modifier = Modifier
                            .navigationBarsPadding()
                            .statusBarsPadding()
                    ) {
                        AmityPollPostTypeSelectionBottomSheet(
                            onCloseSheet = {
                                showPollSelectionBottomSheet = false
                            },
                            onNextClicked = { pollType ->
                                showPollSelectionBottomSheet = false
                                behavior.goToCreatePollPage(
                                    AmityEventDetailPageBehavior.Context(
                                        pageContext = context,
                                        community = community,
                                    ),
                                    pollType = pollType
                                )
                            }
                        )
                    }
                }
            }

            // Event menu bottom sheet - placed at Box level to work on both tabs
            if (event != null && !hasError) {
                val context = LocalContext.current
                val behavior by lazy {
                    AmitySocialBehaviorHelper.eventDetailPageBehavior
                }
                val currentUserId = AmityCoreClient.getUserId()
                val isEventCreator = event!!.getCreator()?.getUserId() == currentUserId

                AmityEventMenuBottomSheet(
                    shouldShow = showEventMenuBottomSheet,
                    onDismiss = { showEventMenuBottomSheet = false },
                    onEditClick = {
                        behavior.goToEditEventPage(
                            AmityEventDetailPageBehavior.Context(pageContext = context),
                            eventId = event!!.getEventId()
                        )
                    },
                    onDeleteClick = {
                        showDeleteConfirmDialog = true
                    },
                    onAddToCalendarClick = {
                        val intent =
                            android.content.Intent(android.content.Intent.ACTION_INSERT).apply {
                                data = android.provider.CalendarContract.Events.CONTENT_URI
                                putExtra(
                                    android.provider.CalendarContract.Events.TITLE,
                                    event!!.getTitle()
                                )
                                putExtra(
                                    android.provider.CalendarContract.Events.DESCRIPTION,
                                    event!!.getDescription()
                                )
                                putExtra(
                                    android.provider.CalendarContract.EXTRA_EVENT_BEGIN_TIME,
                                    event!!.getStartTime()?.millis
                                )
                                putExtra(
                                    android.provider.CalendarContract.EXTRA_EVENT_END_TIME,
                                    event!!.getEndTime()?.millis
                                )

                                if (event!!.getType() == AmityEventType.IN_PERSON) {
                                    event!!.getLocation()?.let { location ->
                                        putExtra(
                                            android.provider.CalendarContract.Events.EVENT_LOCATION,
                                            location.toString()
                                        )
                                    }
                                }
                            }
                        try {
                            context.startActivity(intent)
                            AmityUIKitSnackbar.publishSnackbarMessage("Event added to your calendar.")
                        } catch (e: Exception) {
                        }
                    },
                    eventStartTime = event!!.getStartTime(),
                    eventEndTime = event!!.getEndTime(),
                    isEventCreator = isEventCreator,
                    hasDeletePermission = hasDeleteEventPermission,
                    hasRsvpd = isGoing == true
                )
            }

            // RSVP bottom sheet
            if (showRsvpBottomSheet && event != null && !hasError) {
                val context = LocalContext.current
                ModalBottomSheet(
                    onDismissRequest = { showRsvpBottomSheet = false },
                    sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true),
                    containerColor = AmityTheme.colors.background
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .navigationBarsPadding(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Spacer(modifier = Modifier.height(20.dp))
                            // Calendar icon
                            Image(
                                painter = painterResource(R.drawable.amity_ic_event_add_to_calendar),
                                contentDescription = "Calendar",
                                modifier = Modifier.size(120.dp)
                            )

                            Spacer(modifier = Modifier.height(36.dp))

                            // Title
                            Text(
                                text = "You're going to this event!",
                                style = AmityTheme.typography.headLine.copy(fontWeight = FontWeight.Bold),
                                color = AmityTheme.colors.base
                            )

                            Spacer(modifier = Modifier.height(8.dp))

                            // Description
                            Text(
                                text = "We'll notify you about the event. Add it to your calendar to track event details.",
                                style = AmityTheme.typography.body,
                                color = AmityTheme.colors.baseShade1,
                                textAlign = TextAlign.Center
                            )

                            Spacer(modifier = Modifier.height(24.dp))
                        }

                        // Divider (full width)
                        HorizontalDivider(
                            thickness = 1.dp,
                            color = AmityTheme.colors.baseShade4,
                            modifier = Modifier.fillMaxWidth()
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        // Add to calendar button
                        Button(
                            onClick = {
                                // Add event to calendar (RSVP already created)
                                val intent =
                                    android.content.Intent(android.content.Intent.ACTION_INSERT)
                                        .apply {
                                            data =
                                                android.provider.CalendarContract.Events.CONTENT_URI
                                            putExtra(
                                                android.provider.CalendarContract.Events.TITLE,
                                                event!!.getTitle()
                                            )
                                            putExtra(
                                                android.provider.CalendarContract.Events.DESCRIPTION,
                                                event!!.getDescription()
                                            )
                                            putExtra(
                                                android.provider.CalendarContract.EXTRA_EVENT_BEGIN_TIME,
                                                event!!.getStartTime()?.millis
                                            )
                                            putExtra(
                                                android.provider.CalendarContract.EXTRA_EVENT_END_TIME,
                                                event!!.getEndTime()?.millis
                                            )

                                            // Add location if it's an in-person event
                                            if (event!!.getType() == com.amity.socialcloud.sdk.model.social.event.AmityEventType.IN_PERSON) {
                                                event!!.getLocation()?.let { location ->
                                                    putExtra(
                                                        android.provider.CalendarContract.Events.EVENT_LOCATION,
                                                        location.toString()
                                                    )
                                                }
                                            }
                                        }

                                try {
                                    context.startActivity(intent)
                                    showRsvpBottomSheet = false
                                    AmityUIKitSnackbar.publishSnackbarMessage("Event added to your calendar.")
                                } catch (e: Exception) {
                                    showRsvpBottomSheet = false
                                }
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp)
                                .height(48.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = AmityTheme.colors.primary
                            ),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Icon(
                                painter = painterResource(R.drawable.amity_ic_event_add_to_calendar_button),
                                contentDescription = "Add to calendar",
                                tint = Color.White,
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Add to calendar",
                                style = AmityTheme.typography.body.copy(
                                    fontWeight = FontWeight.Bold
                                ),
                                color = Color.White
                            )
                        }
                    }
                }
            }

            // Join community bottom sheet
            if (showJoinCommunityBottomSheet && targetCommunity != null && !hasError) {
                // Get current user
                val currentUserId = AmityCoreClient.getUserId()
                val currentUser by remember {
                    AmityCoreClient.newUserRepository().getUser(currentUserId)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .asFlow()
                        .catch { }
                }.collectAsState(initial = null)

                ModalBottomSheet(
                    onDismissRequest = { showJoinCommunityBottomSheet = false },
                    sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true),
                    containerColor = AmityTheme.colors.background,
                    dragHandle = {
                        Box(
                            modifier = Modifier
                                .padding(top = 12.dp, bottom = 8.dp)
                                .width(36.dp)
                                .height(4.dp)
                                .background(
                                    color = AmityTheme.colors.baseShade3,
                                    shape = RoundedCornerShape(2.dp)
                                )
                        )
                    }
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .navigationBarsPadding()
                            .padding(horizontal = 16.dp, vertical = 24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        // Community avatar with current user avatar badge
                        Box(
                            modifier = Modifier.size(120.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            // Main community avatar (120x120)
                            AmityAvatarView(
                                image = targetCommunity!!.getAvatar(),
                                size = 120.dp,
                                roundedCornerShape = RoundedCornerShape(24.dp),
                                placeholder = R.drawable.amity_ic_community_placeholder,
                                placeholderTint = Color.White,
                                placeholderBackground = AmityTheme.colors.primaryShade2,
                                iconPadding = 24.dp,
                                modifier = Modifier.align(Alignment.Center)
                            )

                            // Current user avatar with display name on bottom right
                            Column(
                                horizontalAlignment = Alignment.End,
                                modifier = Modifier
                                    .align(Alignment.BottomEnd)
                                    .offset(x = 20.dp, y = 8.dp)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(52.dp)
                                        .border(
                                            width = 4.dp,
                                            color = AmityTheme.colors.background,
                                            shape = CircleShape
                                        )
                                        .background(
                                            color = AmityTheme.colors.background,
                                            shape = CircleShape
                                        )
                                        .padding(4.dp)
                                ) {
                                    AmityUserAvatarView(
                                        user = currentUser,
                                        size = 56.dp,
                                        modifier = Modifier.align(Alignment.Center)
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(36.dp))

                        // Title
                        Text(
                            text = "Join group to continue",
                            style = AmityTheme.typography.headLine.copy(
                                fontWeight = FontWeight.Bold
                            ),
                            color = AmityTheme.colors.base,
                            textAlign = TextAlign.Center
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        // Description
                        Text(
                            text = "Become a member of ${targetCommunity!!.getDisplayName()} to attend events and join the conversation.",
                            style = AmityTheme.typography.body,
                            color = AmityTheme.colors.baseShade1,
                            textAlign = TextAlign.Center
                        )

                        Spacer(modifier = Modifier.height(16.dp))
                    }

                    // Divider (full width, outside the column padding)
                    HorizontalDivider(
                        thickness = 1.dp,
                        color = AmityTheme.colors.baseShade4,
                        modifier = Modifier.fillMaxWidth()
                    )

                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .navigationBarsPadding()
                            .padding(horizontal = 16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Spacer(modifier = Modifier.height(16.dp))

                        // Join community button
                        val requiresApproval = targetCommunity!!.requiresJoinApproval()
                        Button(
                            onClick = {
                                viewModel.joinCommunity(
                                    communityId = targetCommunity!!.getCommunityId(),
                                    onSuccess = {
                                        showJoinCommunityBottomSheet = false
                                        if (requiresApproval) {
                                            // Show pending approval dialog
                                            showPendingApprovalDialog = true
                                        } else {
                                            // Show RSVP bottom sheet to add to calendar
                                            showRsvpBottomSheet = true
                                        }
                                    },
                                    onError = {
                                        showJoinCommunityBottomSheet = false
                                        AmityUIKitSnackbar.publishSnackbarErrorMessage("Failed to join the community. Please try again.")
                                    }
                                )
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(48.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = AmityTheme.colors.primary
                            ),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Text(
                                text = if (requiresApproval) "Join group" else "Join group and RSVP",
                                style = AmityTheme.typography.body.copy(
                                    fontWeight = FontWeight.SemiBold
                                ),
                                color = Color.White
                            )
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        // Cancel button
                        TextButton(
                            onClick = { showJoinCommunityBottomSheet = false },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(48.dp)
                                .border(
                                    width = 1.dp,
                                    color = AmityTheme.colors.secondaryShade3,
                                    shape = RoundedCornerShape(8.dp)
                                ),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Text(
                                text = "Cancel",
                                style = AmityTheme.typography.body.copy(
                                    fontWeight = FontWeight.SemiBold
                                ),
                                color = AmityTheme.colors.secondary
                            )
                        }
                    }
                }
            }

            // Pending approval dialog
            if (showPendingApprovalDialog && !hasError) {
                AmityAlertDialog(
                    dialogTitle = "You'll be able to RSVP once your join request is accepted",
                    dialogText = "Requested to join the community. You'll be notified once your request is accepted.",
                    dismissText = "OK",
                    onDismissRequest = { showPendingApprovalDialog = false }
                )
            }

            // Delete confirmation dialog - placed at Box level to work on both tabs
            if (showDeleteConfirmDialog && !hasError) {
                val context = LocalContext.current
                AmityAlertDialog(
                    dialogTitle = "Delete event?",
                    dialogText = "This event will be permanently deleted. You and others will no longer see and find this event.",
                    confirmText = "Delete",
                    dismissText = "Cancel",
                    confirmTextColor = AmityTheme.colors.alert,
                    onConfirmation = {
                        viewModel.deleteEvent(
                            onSuccess = {
                                showDeleteConfirmDialog = false
                                context.closePage()
                                coroutineScope.launch {
                                    kotlinx.coroutines.delay(200)
                                    AmityUIKitSnackbar.publishSnackbarMessage("Event deleted.")
                                }
                            },
                            onError = {
                                showDeleteConfirmDialog = false
                                AmityUIKitSnackbar.publishSnackbarMessage("Failed to delete event. Please try again.")
                            }
                        )
                    },
                    onDismissRequest = { showDeleteConfirmDialog = false }
                )
            }

            // Editing not possible dialog - shown when trying to edit within 15 minutes of start
            if (showEditingNotPossibleDialog && !hasError) {
                AmityAlertDialog(
                    dialogTitle = "Editing is not possible",
                    dialogText = "You can no longer edit this event. Changes are restricted 15 minutes before the start time.",
                    dismissText = "OK",
                    onDismissRequest = { showEditingNotPossibleDialog = false }
                )
            }

            // Pending join request dialog - shown when user has pending join request and tries to RSVP
            if (showPendingJoinDialog && !hasError) {
                AmityAlertDialog(
                    dialogTitle = "You'll be able to RSVP once your join request is accepted",
                    dialogText = "Requested to join the community. You'll be notified once your request is accepted.",
                    dismissText = "OK",
                    onDismissRequest = { showPendingJoinDialog = false }
                )
            }
        }
    }
}



@Composable
private fun EventCollapsedHeader(
    event: AmityEvent,
    onBackClick: () -> Unit,
    onMenuClick: (() -> Unit)? = null
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp)
    ) {

        // Background with event cover image
        val coverUrl = event.getCoverImage()?.getUrl(AmityImage.Size.MEDIUM)

        if (coverUrl != null) {
            AsyncImage(
                model = coverUrl,
                contentDescription = "Event background",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .matchParentSize()
                    .clip(RoundedCornerShape(0.dp)),
                alpha = 0.15f
            )
        } else {
            Image(
                painter = painterResource(R.drawable.amity_ic_event_list_placeholder),
                contentDescription = "Event background",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .matchParentSize()
                    .clip(RoundedCornerShape(0.dp)),
                alpha = 0.15f
            )
        }

        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(start = 16.dp, top = 60.dp, end = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onBackClick) {
                Icon(
                    painter = painterResource(R.drawable.amity_ic_back),
                    contentDescription = "Back",
                    tint = AmityTheme.colors.base
                )
            }

            Text(
                text = event.getTitle() ?: "Event",
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                style = AmityTheme.typography.title.copy(
                    fontWeight = FontWeight.Bold,
                    fontSize = 17.sp
                ),
                color = AmityTheme.colors.base,
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 12.dp)
            )

            onMenuClick?.let {
                AmityMenuButton(
                    icon = R.drawable.amity_ic_more_horiz,
                    size = 32.dp,
                    iconPadding = 4.dp,
                    onClick = it
                )
            }
        }
    }
    Divider(color = AmityTheme.colors.baseShade4)
}

@Composable
private fun EventExpandedHeader(
    event: AmityEvent,
    onBackClick: () -> Unit,
    onMenuClick: (() -> Unit)? = null
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(187.dp)
    ) {
        // Cover image fills entire box
        val coverUrl = event.getCoverImage()?.getUrl(AmityImage.Size.MEDIUM)

        val context = LocalContext.current
        if (coverUrl != null) {
            Box(modifier = Modifier.matchParentSize()) {
                AsyncImage(
                    model = ImageRequest.Builder(context)
                        .data(coverUrl)
                        .crossfade(true)
                        .build(),
                    contentDescription = "Event cover",
                    contentScale = ContentScale.Crop,
                    placeholder = painterResource(R.drawable.amity_ic_event_list_placeholder),
                    error = painterResource(R.drawable.amity_ic_event_list_placeholder),
                    modifier = Modifier.fillMaxSize()
                )
            }
        } else {
            Box(modifier = Modifier.matchParentSize()) {
                Image(
                    painter = painterResource(R.drawable.amity_ic_event_list_placeholder),
                    contentDescription = "Event cover",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
            }
        }

        // Top navigation bar overlays the cover image
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 16.dp, end = 16.dp, bottom = 12.dp, top = 60.dp)
                .align(Alignment.TopCenter),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Surface(
                shape = RoundedCornerShape(50),
                color = Color.Black.copy(alpha = 0.3f),
                modifier = Modifier.size(32.dp)
            ) {
                IconButton(onClick = onBackClick) {
                    Icon(
                        painter = painterResource(R.drawable.amity_ic_back),
                        contentDescription = "Back",
                        tint = Color.White
                    )
                }
            }

            Spacer(modifier = Modifier.weight(1f))

            onMenuClick?.let {
                AmityMenuButton(
                    icon = R.drawable.amity_ic_more_horiz,
                    size = 32.dp,
                    iconPadding = 4.dp,
                    tint = Color.White,
                    onClick = it
                )
            }
        }
    }
}

@Composable
private fun EventTabRow(
    selectedIndex: Int,
    onTabSelected: (Int) -> Unit
) {
    Column(
        modifier = Modifier
            .background(color = AmityTheme.colors.background)
            .padding(top = 16.dp)
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(20.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
        ) {
            // About tab
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .weight(1f)
                    .clickableWithoutRipple {
                        onTabSelected(0)
                    }
            ) {
                Box(
                    modifier = Modifier.padding(bottom = 12.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        painter = painterResource(R.drawable.amity_ic_event_detail_info_tab),
                        contentDescription = "About",
                        tint = if (selectedIndex == 0) AmityTheme.colors.base else AmityTheme.colors.secondaryShade3,
                        modifier = Modifier.size(24.dp)
                    )
                }
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(2.dp)
                        .background(
                            color = if (selectedIndex == 0) AmityTheme.colors.highlight else Color.Transparent,
                            shape = RoundedCornerShape(
                                topStart = 1.dp,
                                topEnd = 1.dp
                            )
                        )
                )
            }

            // Discussion tab
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .weight(1f)
                    .clickableWithoutRipple {
                        onTabSelected(1)
                    }
            ) {
                Box(
                    modifier = Modifier.padding(bottom = 12.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        painter = painterResource(R.drawable.amity_ic_event_detail_discussion_feed),
                        contentDescription = "Discussion",
                        tint = if (selectedIndex == 1) AmityTheme.colors.base else AmityTheme.colors.secondaryShade3,
                        modifier = Modifier.size(24.dp)
                    )
                }
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(2.dp)
                        .background(
                            color = if (selectedIndex == 1) AmityTheme.colors.highlight else Color.Transparent,
                            shape = RoundedCornerShape(
                                topStart = 1.dp,
                                topEnd = 1.dp
                            )
                        )
                )
            }
        }

        HorizontalDivider(
            thickness = 1.dp,
            color = AmityTheme.colors.divider
        )
    }
}

@Composable
private fun EventTitleSection(event: AmityEvent) {
    val context = LocalContext.current
    val behavior by lazy {
        AmitySocialBehaviorHelper.eventDetailPageBehavior
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(AmityTheme.colors.background)
            .padding(16.dp)
    ) {
        // Status and Community Name Row
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(bottom = 8.dp)
        ) {
            // Status text
            val statusText = when {
                event.getStatus() == AmityEventStatus.LIVE -> "HAPPENING NOW ·"
                event.getStatus() == AmityEventStatus.SCHEDULED -> "UPCOMING ·"
                else -> "ENDED ·"
            }

            Text(
                text = statusText,
                style = AmityTheme.typography.caption.copy(
                    fontWeight = FontWeight.Bold
                ),
                color = AmityTheme.colors.baseShade1
            )

            Spacer(modifier = Modifier.width(4.dp))

            // Community avatar and name
            event.getTargetCommunity()?.let { community ->
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .weight(1f, fill = false)
                        .clickableWithoutRipple {
                            behavior.goToCommunityProfilePage(
                                AmityEventDetailPageBehavior.Context(
                                    pageContext = context
                                ),
                                communityId = community.getCommunityId()
                            )
                        }
                ) {
                    // Lock icon - only show if community is private
                    if (!community.isPublic()) {
                        Icon(
                            painter = painterResource(R.drawable.amity_ic_lock1),
                            contentDescription = "Private",
                            tint = AmityTheme.colors.baseShade1,
                            modifier = Modifier
                                .padding(horizontal = 4.dp)
                                .size(16.dp)
                        )
                    }

                    // Community name (max 1 line)
                    Text(
                        text = community.getDisplayName() ?: "",
                        style = AmityTheme.typography.body.copy(
                            fontWeight = FontWeight.Bold
                        ),
                        color = AmityTheme.colors.base,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )

                    // Verified badge if official
                    if (community.isOfficial()) {
                        Image(
                            painter = painterResource(R.drawable.amity_ic_verified),
                            contentDescription = "Verified",
                            modifier = Modifier
                                .padding(start = 4.dp)
                                .size(16.dp)
                        )
                    }
                }
            }
        }

        // Event title (max 2 lines)
        Text(
            text = event.getTitle() ?: "",
            style = AmityTheme.typography.title.copy(
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp
            ),
            color = AmityTheme.colors.base,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis,
            textAlign = TextAlign.Start
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun EventDetailsSection(
    event: AmityEvent,
    isGoing: Boolean?,
    showRsvpBottomSheet: Boolean,
    onRsvpBottomSheetChange: (Boolean) -> Unit,
    showChangeRsvpBottomSheet: Boolean,
    onChangeRsvpBottomSheetChange: (Boolean) -> Unit,
    onJoinCommunityBottomSheetChange: (Boolean) -> Unit,
    isMember: Boolean,
    hasPendingJoinRequest: Boolean,
    showPendingJoinDialog: Boolean,
    onShowPendingJoinDialogChange: (Boolean) -> Unit,
    onIsGoingChange: (Boolean?) -> Unit,
    viewModel: AmityEventDetailViewModel,
    community: AmityCommunity?
) {
    val context = LocalContext.current
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(AmityTheme.colors.background)
            .padding(start = 16.dp, top = 16.dp, end = 16.dp, bottom = 16.dp)
    ) {

        Row(
            modifier = Modifier.padding(bottom = 10.dp),
            verticalAlignment = Alignment.Top
        ) {
            // Calendar-style date display
            event.getStartTime()?.let { startTime ->
                Column(
                    modifier = Modifier
                        .size(40.dp)
                        .border(
                            width = 1.dp,
                            color = AmityTheme.colors.baseShade4,
                            shape = RoundedCornerShape(8.dp)
                        )
                        .clip(RoundedCornerShape(8.dp)),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // Month section with divider color background (14px height)
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(14.dp)
                            .background(color = AmityTheme.colors.baseShade4),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = startTime.toString("MMM").uppercase(),
                            style = AmityTheme.typography.caption.copy(
                                fontSize = 8.sp,
                                fontWeight = FontWeight.SemiBold
                            ),
                            color = AmityTheme.colors.baseShade1
                        )
                    }
                    // Date section with transparent background
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = startTime.toString("d"),
                            style = AmityTheme.typography.body.copy(
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold
                            ),
                            color = AmityTheme.colors.base
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.width(12.dp))
            Column(
                modifier = Modifier.height(40.dp),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "When",
                    style = AmityTheme.typography.caption,
                    color = AmityTheme.colors.baseShade2,
                    modifier = Modifier.height(20.dp)
                )
                event.getStartTime()?.let { startTime ->
                    val endTime = event.getEndTime()
                    val dateText = formatEventTimestamp(startTime, endTime)
                    Text(
                        text = dateText,
                        style = AmityTheme.typography.body.copy(
                            fontWeight = FontWeight.Bold
                        ),
                        color = AmityTheme.colors.base,
                        modifier = Modifier.height(20.dp)
                    )
                }
            }
        }

        // Event type section
        Row(
            modifier = Modifier.padding(bottom = 10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Event type icon with border (similar to calendar style)
            val eventTypeIcon = when (event.getType()) {
                AmityEventType.IN_PERSON -> R.drawable.amity_ic_event_detail_location
                AmityEventType.VIRTUAL -> R.drawable.amity_ic_event_detail_video
                else -> R.drawable.amity_ic_event_detail_location
            }
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .border(
                        width = 1.dp,
                        color = AmityTheme.colors.baseShade4,
                        shape = RoundedCornerShape(8.dp)
                    )
                    .clip(RoundedCornerShape(8.dp)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = painterResource(eventTypeIcon),
                    contentDescription = "Event type",
                    tint = AmityTheme.colors.base,
                    modifier = Modifier.size(24.dp)
                )
            }
            Spacer(modifier = Modifier.width(12.dp))
            Column(
                modifier = Modifier.height(40.dp),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Event type",
                    style = AmityTheme.typography.caption,
                    color = AmityTheme.colors.baseShade2,
                    modifier = Modifier.height(20.dp)
                )
                Text(
                    text = when (event.getType()) {
                        AmityEventType.IN_PERSON -> "In-person"
                        AmityEventType.VIRTUAL -> "Virtual"
                        else -> "Unknown"
                    },
                    style = AmityTheme.typography.body.copy(
                        fontWeight = FontWeight.Bold
                    ),
                    color = AmityTheme.colors.base,
                    modifier = Modifier.height(20.dp)
                )
            }
        }

        // Attendees section - only show if there are RSVPs
        val rsvpCount = event.getRsvpCount()
        if (rsvpCount > 0) {
            val eventId = event.getEventId()
            Row(
                modifier = Modifier
                    .padding(bottom = 10.dp)
                    .clickableWithoutRipple {
                        AmitySocialBehaviorHelper.eventDetailPageBehavior.goToEventAttendeesPage(
                            context = AmityEventDetailPageBehavior.Context(
                                pageContext = context
                            ),
                            eventId = eventId
                        )
                    },
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Attendees icon with border (similar to event type style)
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .border(
                            width = 1.dp,
                            color = AmityTheme.colors.baseShade4,
                            shape = RoundedCornerShape(8.dp)
                        )
                        .clip(RoundedCornerShape(8.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        painter = painterResource(R.drawable.amity_ic_event_attendee),
                        contentDescription = "Attendees",
                        tint = AmityTheme.colors.base,
                        modifier = Modifier.size(20.dp)
                    )
                }
                Spacer(modifier = Modifier.width(12.dp))
                Column(
                    modifier = Modifier.height(40.dp),
                    verticalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "Attendees",
                        style = AmityTheme.typography.caption,
                        color = AmityTheme.colors.baseShade2,
                        modifier = Modifier.height(20.dp)
                    )
                    Text(
                        text = rsvpCount.readableNumber(),
                        style = AmityTheme.typography.body.copy(
                            fontWeight = FontWeight.Bold
                        ),
                        color = AmityTheme.colors.base,
                        modifier = Modifier.height(20.dp)
                    )
                }
            }
        }

        // Hosted by section
        Row(
            modifier = Modifier.padding(bottom = 0.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // User avatar
            event.getCreator()?.let { creator ->
                com.amity.socialcloud.uikit.common.ui.elements.AmityUserAvatarView(
                    user = creator,
                    size = 40.dp,
                    modifier = Modifier
                        .clickableWithoutRipple {
                            AmitySocialBehaviorHelper.eventDetailPageBehavior.goToUserProfilePage(
                                context = AmityEventDetailPageBehavior.Context(
                                    pageContext = context,
                                    community = community
                                ),
                                userId = creator.getUserId()
                            )
                        }
                )
            }
            Spacer(modifier = Modifier.width(12.dp))
            Column(
                modifier = Modifier.height(40.dp),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Hosted by",
                    style = AmityTheme.typography.caption,
                    color = AmityTheme.colors.baseShade2,
                    modifier = Modifier.height(20.dp)
                )
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                    modifier = Modifier
                        .height(20.dp)
                        .clickableWithoutRipple {
                            event.getCreator()?.let { creator ->
                                AmitySocialBehaviorHelper.eventDetailPageBehavior.goToUserProfilePage(
                                    context = AmityEventDetailPageBehavior.Context(
                                        pageContext = context,
                                        community = community
                                    ),
                                    userId = creator.getUserId()
                                )
                            }
                        }
                ) {
                    Text(
                        text = event.getCreator()?.getDisplayName() ?: "Unknown",
                        style = AmityTheme.typography.body.copy(
                            fontWeight = FontWeight.Bold
                        ),
                        color = AmityTheme.colors.base
                    )
                    val isBrandCreator = event.getCreator()?.isBrand() == true
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


        // Check if current user is the event creator
        val currentUserId = AmityCoreClient.getUserId()
        val isEventCreator = event.getCreator()?.getUserId() == currentUserId

        // Check if event has ended
        val endTime = event.getEndTime()
        val now = DateTime.now()
        val hasEventEnded = endTime?.let { now.isAfter(it) } ?: false

        // Only show buttons if event hasn't ended
            // Set up live stream button - show for creator if event is virtual and starts within 15 minutes
            val isVirtualEvent = event.getType() == com.amity.socialcloud.sdk.model.social.event.AmityEventType.VIRTUAL
            val startTime = event.getStartTime()
            val minutesUntilStart = startTime?.let {
                org.joda.time.Minutes.minutesBetween(now, it).minutes
            } ?: Int.MAX_VALUE
            // Show button if event starts within 15 minutes OR has already started
            val shouldShowLiveStreamButton = isEventCreator && isVirtualEvent && (minutesUntilStart <= 15 || minutesUntilStart < 0)

            if (shouldShowLiveStreamButton) {
                Spacer(modifier = Modifier.height(16.dp))
                // Show "Set up live stream" button for creator
                Button(
                    onClick = {
                        goToSetupLive(
                            context = context,
                            event = event,
                        )
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(40.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = AmityTheme.colors.primary
                    ),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Icon(
                        painter = painterResource(R.drawable.amity_ic_event_external),
                        contentDescription = "Set up live stream",
                        tint = Color.White,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Set up live stream",
                        style = AmityTheme.typography.body.copy(
                            fontWeight = FontWeight.Bold
                        ),
                        color = Color.White
                    )
                }
            } else if (!isEventCreator) {
                // Show "RSVP"/"Going"/"Not going" button for non-creators
                // Hide button if user never RSVP'd and event is live/ended
                val eventIsLiveOrEnded = event.getStatus() == AmityEventStatus.LIVE || event.getStatus() == AmityEventStatus.ENDED
                val shouldShowButton = isGoing != null || !eventIsLiveOrEnded
                if (shouldShowButton) {
                    Spacer(modifier = Modifier.height(16.dp))
                    Button(
                        onClick = {
                            // Check if user is a visitor
                            if (AmityCoreClient.isVisitor()) {
                                AmityUIKitSnackbar.publishSnackbarErrorMessage("Create an account or sign in to continue.")
                            } else if (!isMember) {
                                if (hasPendingJoinRequest) {
                                    // User has pending join request - show pending dialog
                                    onShowPendingJoinDialogChange(true)
                                } else {
                                    // User is not a member - show join community bottom sheet
                                    onJoinCommunityBottomSheetChange(true)
                                }
                            } else {
                                when (isGoing) {
                                    null -> {
                                        // No RSVP yet - check if event has started
                                        if (event.getStatus() != AmityEventStatus.SCHEDULED) {
                                            AmityUIKitSnackbar.publishSnackbarErrorMessage("Your RSVP cannot be changed once the event has started.")
                                        } else {
                                            // Create RSVP first, then show add to calendar bottom sheet
                                            viewModel.createRsvp(
                                                event = event,
                                                status = AmityEventResponseStatus.GOING,
                                                onSuccess = {
                                                    // Update local state
                                                    onIsGoingChange(true)
                                                    // Show bottom sheet to add to calendar
                                                    onRsvpBottomSheetChange(true)
                                                },
                                                onError = { error ->
                                                    AmityUIKitSnackbar.publishSnackbarErrorMessage("Your RSVP could not be updated. Please try again.")
                                                }
                                            )
                                        }
                                    }
                                    else -> {
                                        // Already has RSVP - show bottom sheet to change status
                                        onChangeRsvpBottomSheetChange(true)
                                    }
                                }
                            }
                        },
                        enabled = !eventIsLiveOrEnded || isGoing == null,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(40.dp)
                            .border(
                                width = if (isGoing != null) 1.dp else 0.dp,
                                color = if (isGoing != null) {
                                    if (eventIsLiveOrEnded) AmityTheme.colors.baseShade4 else AmityTheme.colors.secondaryShade3
                                } else Color.Transparent,
                                shape = RoundedCornerShape(8.dp)
                            ),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (isGoing != null) AmityTheme.colors.background else AmityTheme.colors.primary,
                            disabledContainerColor = if (isGoing != null) AmityTheme.colors.background else AmityTheme.colors.primary,
                            disabledContentColor = if (eventIsLiveOrEnded) AmityTheme.colors.baseShade4 else AmityTheme.colors.secondary
                        ),
                        shape = RoundedCornerShape(8.dp)
                ) {
                    Icon(
                        painter = painterResource(
                            when (isGoing) {
                                null -> R.drawable.amity_ic_event_rsvp
                                true -> R.drawable.amity_ic_event_going
                                false -> R.drawable.amity_ic_event_not_going
                            }
                        ),
                        contentDescription = when (isGoing) {
                            null -> "RSVP"
                            true -> "Going"
                            false -> "Not going"
                        },
                        tint = when (isGoing) {
                            null -> Color.White
                            else -> if (eventIsLiveOrEnded) AmityTheme.colors.baseShade3 else AmityTheme.colors.secondary
                        },
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = when (isGoing) {
                            null -> "RSVP"
                            true -> "Going"
                            false -> "Not going"
                        },
                        style = AmityTheme.typography.body.copy(
                            fontWeight = FontWeight.Bold
                        ),
                        color = when (isGoing) {
                            null -> Color.White
                            else -> if (eventIsLiveOrEnded) AmityTheme.colors.baseShade3 else AmityTheme.colors.secondary
                        }
                    )
                    }
                }
            }

        // Change RSVP status bottom sheet
        if (showChangeRsvpBottomSheet) {
            ModalBottomSheet(
                onDismissRequest = { onChangeRsvpBottomSheetChange(false) },
                sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true),
                containerColor = AmityTheme.colors.background
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .navigationBarsPadding()
                        .padding(horizontal = 16.dp, vertical = 24.dp)
                ) {
                    // Going option
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickableWithoutRipple {
                                // Check if event start time has passed or event is not scheduled
                                val startTime = event.getStartTime()
                                val now = DateTime.now()
                                val hasStarted = startTime?.let { now.isAfter(it) } ?: false
                                val isNotScheduled = event.getStatus() != AmityEventStatus.SCHEDULED

                                if (hasStarted || isNotScheduled) {
                                    AmityUIKitSnackbar.publishSnackbarErrorMessage("Your RSVP cannot be changed once the event has started.")
                                    onChangeRsvpBottomSheetChange(false)
                                    return@clickableWithoutRipple
                                }

                                val wasNotGoing = isGoing == false
                                viewModel.updateRsvp(
                                    event = event,
                                    status = AmityEventResponseStatus.GOING,
                                    onSuccess = {
                                        onIsGoingChange(true)
                                        onChangeRsvpBottomSheetChange(false)
                                        if (wasNotGoing) {
                                            // Show RSVP bottom sheet with Add to calendar option
                                            onRsvpBottomSheetChange(true)
                                        } else {
                                            AmityUIKitSnackbar.publishSnackbarMessage("Successfully updated your attending status.")
                                        }
                                    },
                                    onError = { error ->
                                        AmityUIKitSnackbar.publishSnackbarErrorMessage("Your RSVP could not be updated. Please try again.")
                                    }
                                )
                            }
                            .padding(vertical = 16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Going",
                            style = AmityTheme.typography.body.copy(
                                fontWeight = FontWeight.Bold
                            ),
                            color = AmityTheme.colors.base,
                            modifier = Modifier.weight(1f)
                        )

                        // Radio button for selected option
                        RadioButton(
                            selected = isGoing == true,
                            onClick = null,
                            colors = RadioButtonDefaults.colors(
                                selectedColor = AmityTheme.colors.primary,
                                unselectedColor = AmityTheme.colors.baseShade3
                            )
                        )
                    }

                    // Not going option
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickableWithoutRipple {
                                // Check if event start time has passed or event is not scheduled
                                val startTime = event.getStartTime()
                                val now = DateTime.now()
                                val hasStarted = startTime?.let { now.isAfter(it) } ?: false
                                val isNotScheduled = event.getStatus() != AmityEventStatus.SCHEDULED

                                if (hasStarted || isNotScheduled) {
                                    AmityUIKitSnackbar.publishSnackbarErrorMessage("Your RSVP cannot be changed once the event has started.")
                                    onChangeRsvpBottomSheetChange(false)
                                    return@clickableWithoutRipple
                                }

                                viewModel.updateRsvp(
                                    event = event,
                                    status = AmityEventResponseStatus.NOT_GOING,
                                    onSuccess = {
                                        onIsGoingChange(false)
                                        onChangeRsvpBottomSheetChange(false)
                                        AmityUIKitSnackbar.publishSnackbarMessage("Successfully updated your attending status.")
                                    },
                                    onError = { error ->
                                        AmityUIKitSnackbar.publishSnackbarErrorMessage("Your RSVP could not be updated. Please try again.")
                                    }
                                )
                            }
                            .padding(vertical = 16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Not going",
                            style = AmityTheme.typography.body.copy(
                                fontWeight = FontWeight.Bold
                            ),
                            color = AmityTheme.colors.base,
                            modifier = Modifier.weight(1f)
                        )

                        // Radio button for selected option
                        RadioButton(
                            selected = isGoing == false,
                            onClick = null,
                            colors = RadioButtonDefaults.colors(
                                selectedColor = AmityTheme.colors.primary,
                                unselectedColor = AmityTheme.colors.baseShade3
                            )
                        )
                    }
                }
            }
        }
    }
}

fun goToSetupLive(
    context: Context,
    event: AmityEvent,
) {
    val intent = AmityCreateRoomPageActivity.newIntentFromEvent(
        context = context,
        event = event,
    )
    context.startActivity(intent)
}


