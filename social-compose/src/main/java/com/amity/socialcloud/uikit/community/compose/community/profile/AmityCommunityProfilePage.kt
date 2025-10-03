import android.annotation.SuppressLint
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.waterfall
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.LocalViewModelStoreOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import com.amity.socialcloud.sdk.api.core.AmityCoreClient
import com.amity.socialcloud.sdk.api.social.AmitySocialClient
import com.amity.socialcloud.sdk.helper.core.coroutines.asFlow
import com.amity.socialcloud.sdk.model.core.permission.AmityPermission
import com.amity.socialcloud.sdk.model.social.community.AmityCommunityPostSettings
import com.amity.socialcloud.sdk.model.social.post.AmityPost
import com.amity.socialcloud.uikit.common.eventbus.AmityUIKitSnackbar
import com.amity.socialcloud.uikit.common.ui.base.AmityBaseElement
import com.amity.socialcloud.uikit.common.ui.base.AmityBasePage
import com.amity.socialcloud.uikit.common.ui.theme.AmityTheme
import com.amity.socialcloud.uikit.common.utils.getIcon
import com.amity.socialcloud.uikit.common.utils.isSignedIn
import com.amity.socialcloud.uikit.common.utils.isVisitor
import com.amity.socialcloud.uikit.community.compose.AmitySocialBehaviorHelper
import com.amity.socialcloud.uikit.community.compose.R
import com.amity.socialcloud.uikit.community.compose.clip.view.AmityClipFeedPageType
import com.amity.socialcloud.uikit.community.compose.community.profile.AmityCommunityModalSheetUIState
import com.amity.socialcloud.uikit.community.compose.community.profile.AmityCommunityProfilePageBehavior
import com.amity.socialcloud.uikit.community.compose.community.profile.AmityCommunityProfileViewModel
import com.amity.socialcloud.uikit.community.compose.community.profile.component.AmityCommunityHeaderComponent
import com.amity.socialcloud.uikit.community.compose.community.profile.component.AmityCommunityHeaderStyle
import com.amity.socialcloud.uikit.community.compose.community.profile.element.AmityCommunityModalBottomSheet
import com.amity.socialcloud.uikit.community.compose.community.profile.element.AmityCommunityProfileActionsBottomSheet
import com.amity.socialcloud.uikit.community.compose.community.profile.element.AmityCommunityProfileShimmer
import com.amity.socialcloud.uikit.community.compose.community.profile.element.AmityCommunityProfileTabRow
import com.amity.socialcloud.uikit.community.compose.community.profile.element.AmityVideoAndClipChipSelector
import com.amity.socialcloud.uikit.community.compose.livestream.errorhandling.AmityPostErrorPage
import com.amity.socialcloud.uikit.community.compose.paging.feed.community.amityCommunityAnnouncementFeedLLS
import com.amity.socialcloud.uikit.community.compose.paging.feed.community.amityCommunityClipFeedLLS
import com.amity.socialcloud.uikit.community.compose.paging.feed.community.amityCommunityFeedLLS
import com.amity.socialcloud.uikit.community.compose.paging.feed.community.amityCommunityImageFeedLLS
import com.amity.socialcloud.uikit.community.compose.paging.feed.community.amityCommunityPinnedFeedLLS
import com.amity.socialcloud.uikit.community.compose.paging.feed.community.amityCommunityVideoFeedLLS
import com.amity.socialcloud.uikit.community.compose.post.composer.poll.AmityPollPostTypeSelectionBottomSheet
import com.amity.socialcloud.uikit.community.compose.post.detail.AmityPostCategory
import com.amity.socialcloud.uikit.community.compose.post.detail.components.AmityPostShimmer
import com.amity.socialcloud.uikit.community.compose.ui.components.feed.community.AmityCommunityPrivateView
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlin.compareTo
import kotlin.div
import kotlin.text.compareTo
import kotlin.text.get

@OptIn(
    ExperimentalMaterialApi::class, ExperimentalFoundationApi::class,
    ExperimentalLayoutApi::class, ExperimentalMaterial3Api::class
)
@SuppressLint("UnrememberedMutableState")
@Composable
fun AmityCommunityProfilePage(
    modifier: Modifier = Modifier,
    communityId: String,
    isCommunityJustCreated: Boolean = false,
) {
    val viewModelStoreOwner = checkNotNull(LocalViewModelStoreOwner.current) {
        "No ViewModelStoreOwner was provided via LocalViewModelStoreOwner"
    }
    val viewModel =
        viewModel<AmityCommunityProfileViewModel>(
            viewModelStoreOwner = viewModelStoreOwner, factory =
                object : ViewModelProvider.Factory {
                    override fun <VM : androidx.lifecycle.ViewModel> create(
                        modelClass: Class<VM>,
                    ): VM {
                        return AmityCommunityProfileViewModel(communityId) as VM
                    }
                }
        )
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    val lazyListState = rememberLazyListState()
    val state by remember { viewModel.communityProfileState }.collectAsState()
    val community by remember { derivedStateOf { state.community } }
    var expanded by remember { mutableStateOf(false) }

    var showPollSelectionBottomSheet by remember { mutableStateOf(false) }

    var isHeaderSticky by remember { mutableStateOf(false) }
    val pullRefreshState = rememberPullRefreshState(
        refreshing = state.isRefreshing,
        onRefresh = {
            viewModel.refresh()
        }
    )

    val bottomSheetUiState by viewModel.sheetUIState.collectAsState()

    val behavior = remember {
        AmitySocialBehaviorHelper.communityProfilePageBehavior
    }
    val announcementPosts =
        remember(communityId) { viewModel.getAnnouncement() }.collectAsLazyPagingItems()
    val communityPosts =
        remember(communityId) { viewModel.getCommunityPosts() }.collectAsLazyPagingItems()
    val pinPosts =
        remember(communityId) { viewModel.getPin() }.collectAsLazyPagingItems()
    val imagePosts =
        remember(communityId) { viewModel.getCommunityImagePosts() }.collectAsLazyPagingItems()
    val videoPosts =
        remember(communityId) { viewModel.getCommunityVideoPosts() }.collectAsLazyPagingItems()
    val clipPosts =
        remember(communityId) { viewModel.getCommunityClipPosts() }.collectAsLazyPagingItems()

    var selectedTabIndex by remember { mutableIntStateOf(0) }

    val hasCreatePrivilege by if (!AmityCoreClient.isSignedIn()) {
        remember { mutableStateOf(false) }
    } else {
        AmityCoreClient.hasPermission(AmityPermission.CREATE_PRIVILEGED_POST)
            .atCommunity(communityId)
            .check()
            .asFlow()
            .catch {
                emit(false)
            }
            .collectAsState(initial = false)
    }

    val hasManageStoryPermission by if (!AmityCoreClient.isSignedIn()) {
        remember { mutableStateOf(false) }
    } else {
        AmityCoreClient.hasPermission(AmityPermission.MANAGE_COMMUNITY_STORY)
            .atCommunity(communityId)
            .check()
            .asFlow()
            .catch {
                emit(false)
            }
            .collectAsState(initial = false)
    }

    val allowAllUserStoryCreation by if (!AmityCoreClient.isSignedIn()) {
        remember { mutableStateOf(false) }
    } else {
        AmitySocialClient.getSettings()
            .asFlow()
            .map { it.getStorySettings().isAllowAllUserToCreateStory() }
            .catch {
                emit(false)
            }
            .collectAsState(initial = false)
    }

    val isOnlyAdminCanPost by remember(community?.getPostSettings()) {
        derivedStateOf {
            community?.getPostSettings() == AmityCommunityPostSettings.ADMIN_CAN_POST_ONLY
        }
    }

    val shouldShowPostCreationButton by remember(
        isOnlyAdminCanPost,
        hasCreatePrivilege,
        community?.isJoined()
    ) {
        derivedStateOf {
            (!isOnlyAdminCanPost || hasCreatePrivilege)
                    && (community?.isJoined() == true)
        }
    }

    val shouldShowStoryCreationButton by remember(
        allowAllUserStoryCreation,
        hasManageStoryPermission,
        community?.isJoined()
    ) {
        derivedStateOf {
            (allowAllUserStoryCreation || hasManageStoryPermission)
                    && (community?.isJoined() == true)
        }
    }

    LaunchedEffect(state.isRefreshing) {
        if (state.isRefreshing) {
            announcementPosts.refresh()
            communityPosts.refresh()
            pinPosts.refresh()
            imagePosts.refresh()
            videoPosts.refresh()
        }
    }

    var isShowCompleteCreatedCommunity by remember { mutableStateOf(false) }
    LaunchedEffect(isCommunityJustCreated) {
        isShowCompleteCreatedCommunity = isCommunityJustCreated
        if (isShowCompleteCreatedCommunity) {
            AmityUIKitSnackbar.publishSnackbarMessage(context.getString(R.string.amity_v4_community_setup_toast_create_success))
        }
        delay(200L)
        isShowCompleteCreatedCommunity = false
    }

    var selectedVideoClipsTabIndex by remember { mutableIntStateOf(0) }
    val videoClipTabsTitles = listOf("Videos", "Clips")

    AmityBasePage(pageId = "community_profile_page") {
        Scaffold { padding ->
            if (state.community?.isDeleted() == true || state.error != null) {
                AmityPostErrorPage()
            } else {
                Box(
                    modifier = Modifier
                        .padding(PaddingValues(bottom = padding.calculateBottomPadding()))
                        .fillMaxSize()
                        .pullRefresh(pullRefreshState)
                        .background(AmityTheme.colors.newsfeedDivider),
                ) {
                    LaunchedEffect(lazyListState) {
                        snapshotFlow { lazyListState.firstVisibleItemIndex }
                            .collect { index ->
                                if (index > 1 && !isHeaderSticky) {
                                    isHeaderSticky = true
                                } else if (index <= 1 && isHeaderSticky) {
                                    isHeaderSticky = false
                                }
                            }
                    }

                    LazyColumn(
                        state = lazyListState,
                        modifier = Modifier
                            .fillMaxSize()
                            .background(AmityTheme.colors.background),
                    ) {
                        if (community == null || state.isRefreshing) {
                            item {
                                AmityCommunityProfileShimmer()
                            }
                        } else {
                            stickyHeader {
                                if (isHeaderSticky) {
                                    AmityCommunityHeaderComponent(
                                        pageScope = getPageScope(),
                                        community = community!!,
                                        style = AmityCommunityHeaderStyle.COLLAPSE,
                                    )
                                    AmityCommunityProfileTabRow(
                                        pageScope = getPageScope(),
                                        selectedIndex = selectedTabIndex,
                                    ) { index ->
                                        selectedTabIndex = index
                                    }
                                }
                            }

                            item {
                                AmityCommunityHeaderComponent(
                                    pageScope = getPageScope(),
                                    community = community!!,
                                    style = AmityCommunityHeaderStyle.EXPANDED,
                                )
                            }
                        }
                        item {
                            AmityCommunityProfileTabRow(
                                pageScope = getPageScope(),
                                selectedIndex = selectedTabIndex,
                            ) { index ->
                                selectedTabIndex = index
                            }
                        }
                        val hasAnnouncementPin = announcementPosts
                            .itemSnapshotList
                            .items
                            .firstOrNull()
                            ?.postId
                            ?.let { announcementId ->
                                pinPosts.itemSnapshotList.items
                                    .map {
                                        it.postId
                                    }
                                    .contains(announcementId)
                            } ?: false
                        val shouldShowAnnouncement = announcementPosts.itemCount > 0
                                && (selectedTabIndex == 0 || (selectedTabIndex == 1 && hasAnnouncementPin))
                        if (community?.isJoined() == false && community?.isPublic() == false) {
                            item {
                                Box(
                                    modifier = Modifier
                                        .height(360.dp),
                                ) {
                                    AmityCommunityPrivateView()
                                }
                            }
                        } else {
                            if (shouldShowAnnouncement) {
                                amityCommunityAnnouncementFeedLLS(
                                    modifier = modifier,
                                    pageScope = getPageScope(),
                                    announcementPosts = announcementPosts,
                                    hasAnnouncementPin = hasAnnouncementPin,
                                    onClick = {
                                        behavior.goToPostDetailPage(
                                            AmityCommunityProfilePageBehavior.Context(
                                                pageContext = context,
                                            ),
                                            postId = it.getPostId(),
                                            category = if (hasAnnouncementPin) {
                                                AmityPostCategory.PIN_AND_ANNOUNCEMENT
                                            } else {
                                                AmityPostCategory.ANNOUNCEMENT
                                            }
                                        )
                                    }
                                )
                            }
                            when (selectedTabIndex) {
                                0 -> {
                                    if (communityPosts.loadState.refresh == LoadState.Loading) {
                                        repeat(4) {
                                            item {
                                                AmityPostShimmer()
                                            }
                                        }
                                    } else {
                                        amityCommunityFeedLLS(
                                            modifier = modifier,
                                            pageScope = getPageScope(),
                                            communityPosts = communityPosts,
                                            pinPosts = pinPosts,
                                            announcementPosts = announcementPosts,
                                            onClipClick = {
                                                behavior.goToClipFeedPage(
                                                    AmityCommunityProfilePageBehavior.Context(
                                                        pageContext = context,
                                                    ),
                                                    postId = it.getPostId(),
                                                    communityId = communityId,
                                                    type = AmityClipFeedPageType.CommunityFeed(
                                                        postId = it.getPostId(),
                                                        communityId = communityId
                                                    )
                                                )
                                            },
                                            onClick = { post, category ->
                                                behavior.goToPostDetailPage(
                                                    AmityCommunityProfilePageBehavior.Context(
                                                        pageContext = context,
                                                    ),
                                                    postId = post.getPostId(),
                                                    category = category,
                                                )
                                            }
                                        )
                                    }
                                }

                                1 -> {
                                    amityCommunityPinnedFeedLLS(
                                        modifier = modifier,
                                        pageScope = getPageScope(),
                                        pinPosts = pinPosts,
                                        announcementPosts = announcementPosts,
                                        onClick = {
                                            behavior.goToPostDetailPage(
                                                AmityCommunityProfilePageBehavior.Context(
                                                    pageContext = context,
                                                ),
                                                postId = it.getPostId(),
                                                category = AmityPostCategory.PIN
                                            )
                                        }
                                    )
                                }

                                2 -> {
                                    amityCommunityImageFeedLLS(
                                        modifier = modifier,
                                        imagePosts = imagePosts,
                                        onViewPost = { postId, category ->
                                            behavior.goToPostDetailPage(
                                                AmityCommunityProfilePageBehavior.Context(
                                                    pageContext = context,
                                                ),
                                                postId = postId,
                                                category = category,
                                            )
                                        }
                                    )
                                }

                                3 -> {
                                    item {
                                        AmityVideoAndClipChipSelector(
                                            tabTitles = videoClipTabsTitles,
                                            selectedTabIndex = selectedVideoClipsTabIndex,
                                            onTabSelected = { index ->
                                                selectedVideoClipsTabIndex = index
                                            },
                                        )
                                    }
                                    when (selectedVideoClipsTabIndex) {
                                        0 -> {
                                            amityCommunityVideoFeedLLS(
                                                modifier = modifier,
                                                videoPosts = videoPosts,
                                                onViewPost = { postId, category ->
                                                    behavior.goToPostDetailPage(
                                                        AmityCommunityProfilePageBehavior.Context(
                                                            pageContext = context,
                                                        ),
                                                        postId = postId,
                                                        category = category,
                                                    )
                                                }
                                            )
                                        }

                                        1 -> {
                                            amityCommunityClipFeedLLS(
                                                modifier = Modifier,
                                                clipPosts = clipPosts,
                                                onClipClicked = { postId ->
                                                    coroutineScope.launch {
                                                        val selectedIndex =
                                                            clipPosts.itemSnapshotList.items.indexOfFirst {
                                                                it.getPostId() == postId
                                                            }

                                                        if (selectedIndex >= clipPosts.itemCount && selectedIndex >= 0) {
                                                            // Wait for more data to load
                                                            val targetPage = selectedIndex / 20
                                                            val currentPage =
                                                                clipPosts.itemCount / 20

                                                            if (targetPage > currentPage) {
                                                                // Access items to trigger loading
                                                                repeat(targetPage - currentPage) {
                                                                    val triggerIndex =
                                                                        clipPosts.itemCount
                                                                    if (triggerIndex < selectedIndex) {
                                                                        clipPosts[triggerIndex] // Trigger next page load
                                                                        delay(500) // Wait for load
                                                                    }
                                                                }
                                                            }
                                                        }

                                                        behavior.goToClipFeedPage(
                                                            context = AmityCommunityProfilePageBehavior.Context(
                                                                pageContext = context,
                                                            ),
                                                            postId = postId,
                                                            communityId = communityId,
                                                            clipPagingData = viewModel.getCommunityClipPosts(),
                                                            selectedIndex = maxOf(
                                                                0,
                                                                minOf(
                                                                    selectedIndex,
                                                                    clipPosts.itemCount - 1
                                                                )
                                                            ),
                                                            type = AmityClipFeedPageType.CommunityClipFeed(
                                                                communityId
                                                            )
                                                        )
                                                    }
                                                }
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                    AmityBaseElement(
                        pageScope = getPageScope(),
                        elementId = "community_create_post_button",
                    ) {
                        if (shouldShowPostCreationButton || shouldShowStoryCreationButton) {

                            FloatingActionButton(
                                onClick = {
                                    expanded = true
                                },
                                shape = RoundedCornerShape(size = 32.dp),
                                containerColor = AmityTheme.colors.primary,
                                modifier = Modifier
                                    .padding(16.dp)
                                    .size(64.dp)
                                    .align(Alignment.BottomEnd)
                            ) {
                                Icon(
                                    painter = painterResource(id = getConfig().getIcon()),
                                    contentDescription = "create post",
                                    tint = Color.White,
                                    modifier = Modifier.size(32.dp)
                                )
                            }
                        }
                    }
                    if (community != null) {
                        AmityCommunityProfileActionsBottomSheet(
                            modifier = Modifier,
                            community = community!!,
                            shouldShow = expanded,
                            shouldShowPostCreationButton = shouldShowPostCreationButton,
                            shouldShowStoryCreationButton = shouldShowStoryCreationButton,
                            showPollTypeSelectionSheet = {
                                showPollSelectionBottomSheet = true
                            },
                            onDismiss = { expanded = false },
                        )
                    }
                    PullRefreshIndicator(
                        refreshing = state.isRefreshing,
                        state = pullRefreshState,
                        modifier = Modifier.align(Alignment.TopCenter),
                    )
                    if (bottomSheetUiState !is AmityCommunityModalSheetUIState.CloseSheet) {
                        community?.let {
                            AmityCommunityModalBottomSheet(
                                community = it,
                                behavior = behavior,
                                viewModel = viewModel,
                            )
                        }
                    }

                    if (showPollSelectionBottomSheet) {
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
                                onNextClicked = { selectedType ->
                                    behavior.goToCreatePollPage(
                                        AmityCommunityProfilePageBehavior.Context(
                                            pageContext = context,
                                            community = community,
                                        ),
                                        pollType = selectedType
                                    )
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}