import android.annotation.SuppressLint
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.paging.compose.collectAsLazyPagingItems
import com.amity.socialcloud.sdk.model.social.post.AmityPost
import com.amity.socialcloud.uikit.common.ad.AmityListItem
import com.amity.socialcloud.uikit.common.ui.base.AmityBaseElement
import com.amity.socialcloud.uikit.common.ui.base.AmityBasePage
import com.amity.socialcloud.uikit.common.ui.elements.AmityNewsFeedDivider
import com.amity.socialcloud.uikit.common.ui.theme.AmityTheme
import com.amity.socialcloud.uikit.common.utils.getIcon
import com.amity.socialcloud.uikit.community.compose.AmitySocialBehaviorHelper
import com.amity.socialcloud.uikit.community.compose.community.profile.AmityCommunityProfilePageBehavior
import com.amity.socialcloud.uikit.community.compose.community.profile.AmityCommunityProfileViewModel
import com.amity.socialcloud.uikit.community.compose.community.profile.component.AmityCommunityHeaderComponent
import com.amity.socialcloud.uikit.community.compose.community.profile.component.AmityCommunityHeaderStyle
import com.amity.socialcloud.uikit.community.compose.community.profile.element.AmityCommunityEmptyView
import com.amity.socialcloud.uikit.community.compose.community.profile.element.AmityCommunityProfileActionsBottomSheet
import com.amity.socialcloud.uikit.community.compose.community.profile.element.AmityCommunityProfileShimmer
import com.amity.socialcloud.uikit.community.compose.community.profile.element.AmityCommunityProfileTabComponent
import com.amity.socialcloud.uikit.community.compose.post.detail.AmityPostCategory
import com.amity.socialcloud.uikit.community.compose.post.detail.components.AmityPostContentComponent
import com.amity.socialcloud.uikit.community.compose.post.detail.components.AmityPostContentComponentStyle
import com.amity.socialcloud.uikit.community.compose.socialhome.components.AmityPostAdView

@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class, ExperimentalMaterialApi::class)
@SuppressLint("UnrememberedMutableState")
@Composable
fun AmityCommunityProfilePage(
	communityId: String,
) {
	AmityBasePage(pageId = "community_profile_page") {
		val viewModel = remember { AmityCommunityProfileViewModel(communityId) }
		val context = LocalContext.current
		val lazyListState = rememberLazyListState()
		val state by remember { viewModel.communityProfileState }.collectAsState()
		val community by remember { derivedStateOf { state.community } }
		var expanded by remember { mutableStateOf(false) }

		var isHeaderSticky by remember { mutableStateOf(false) }
		val pullRefreshState = rememberPullRefreshState(
			refreshing = state.isRefreshing,
			onRefresh = {
				viewModel.refresh()
			}
		)

		val behavior = remember {
			AmitySocialBehaviorHelper.communityProfilePageBehavior
		}
		val announcement = remember(state.isRefreshing) { viewModel.getAnnouncement() }.collectAsLazyPagingItems()
		val posts = remember(state.isRefreshing) { viewModel.getCommunityPosts() }.collectAsLazyPagingItems()
		val pin = remember(state.isRefreshing) { viewModel.getPin() }.collectAsLazyPagingItems()

		var selectedTabIndex by remember { mutableStateOf(0) }
		Scaffold { padding ->
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
					modifier = Modifier
                        .fillMaxSize()
                        .background(AmityTheme.colors.newsfeedDivider),
					state = lazyListState,
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
								AmityCommunityProfileTabComponent(
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
						AmityCommunityProfileTabComponent(
							pageScope = getPageScope(),
							selectedIndex = selectedTabIndex,
						) { index ->
							selectedTabIndex = index
						}
					}
					if (announcement.itemCount > 0) {
						items(
							count = announcement.itemCount,
							key = {
								"announcement_${announcement[it]?.post?.getPostId() ?: it}"
							}
						) { index ->
							announcement[index]?.post?.let { post ->
								// TODO: 3/6/24 currently only support text, image, and video post
								when (post.getData()) {
									is AmityPost.Data.TEXT,
									is AmityPost.Data.IMAGE,
									is AmityPost.Data.VIDEO -> {
									}

                                    else -> return@items
								}

                                if (post.isDeleted()) {
									return@items
								}

                                AmityPostContentComponent(
									post = post,
									style = AmityPostContentComponentStyle.FEED,
									category = AmityPostCategory.ANNOUNCEMENT,
									hideMenuButton = false,
									hideTarget = true,
								) {
                                    behavior.goToPostDetailPage(
                                        AmityCommunityProfilePageBehavior.Context(
                                            pageContext = context,
                                        ),
                                        postId = post.getPostId(),
                                        category = AmityPostCategory.ANNOUNCEMENT
                                    )
								}
								AmityNewsFeedDivider()
							}
						}
					}
					if (selectedTabIndex == 0) {
						items(
							count = posts.itemCount,
							key = {
								(posts[it] as? AmityListItem.PostItem)?.post?.getPostId() ?: it
							}
						) { index ->
							when (val data = posts[index]) {
								is AmityListItem.PostItem -> {
									val post = data.post
									val isAnnouncement = announcement.itemSnapshotList.items
										.map { it.postId }
										.contains(post.getPostId())

                                    if (isAnnouncement) {
										return@items
									}

                                    // TODO: 3/6/24 currently only support text, image, and video post
									when (post.getData()) {
										is AmityPost.Data.TEXT,
										is AmityPost.Data.IMAGE,
										is AmityPost.Data.VIDEO -> {
										}

                                        else -> return@items
									}
									val category = if(
										pin.itemSnapshotList.items.map { it.postId }.contains(post.getPostId())
									) {
										AmityPostCategory.PIN
									} else {
										AmityPostCategory.GENERAL
									}
									AmityPostContentComponent(
										post = post,
										style = AmityPostContentComponentStyle.FEED,
										category = category,
										hideMenuButton = false,
										hideTarget = true,
									) {
                                        behavior.goToPostDetailPage(
                                            AmityCommunityProfilePageBehavior.Context(
                                                pageContext = context,
                                            ),
                                            postId = post.getPostId(),
                                            category = category,
                                        )
									}
									AmityNewsFeedDivider()
								}

                                is AmityListItem.AdItem -> {
									val ad = data.ad

                                    AmityPostAdView(
										ad = ad,
									)
									AmityNewsFeedDivider()
								}

                                else -> {}
							}
						}

                        if (announcement.itemCount == 0 && posts.itemCount == 0) {
							item {
								Box(
									modifier = Modifier
										.height(480.dp),
								) {
									AmityCommunityEmptyView()
								}
							}
						}
					} else if (selectedTabIndex == 1) {
						items(
							count = pin.itemCount,
							key = {
								"pin_${pin[it]?.post?.getPostId() ?: it}"
							}
						) { index ->
							pin[index]?.post?.let { post ->
								val isAnnouncement = announcement.itemSnapshotList.items
									.map { it.postId }
									.contains(post.getPostId())

                                if (isAnnouncement) {
									return@items
								}

                                // TODO: 3/6/24 currently only support text, image, and video post
								when (post.getData()) {
									is AmityPost.Data.TEXT,
									is AmityPost.Data.IMAGE,
									is AmityPost.Data.VIDEO -> {
									}

                                    else -> return@items
								}

                                if (post.isDeleted()) {
									return@items
								}

                                AmityPostContentComponent(
									post = post,
									style = AmityPostContentComponentStyle.FEED,
									category = AmityPostCategory.PIN,
									hideMenuButton = false,
									hideTarget = true,
								) {
                                    behavior.goToPostDetailPage(
                                        AmityCommunityProfilePageBehavior.Context(
                                            pageContext = context,
                                        ),
                                        postId = post.getPostId(),
                                        category = AmityPostCategory.PIN
                                    )
								}
								AmityNewsFeedDivider()
							}
						}
						if (announcement.itemCount == 0 && pin.itemCount == 0) {
							item {
								Box(
									modifier = Modifier
										.height(480.dp),
								) {
									AmityCommunityEmptyView()
								}
							}
						}

                    }
				}
				AmityBaseElement(
					pageScope = getPageScope(),
					elementId = "community_create_post_button",
				) {
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
				if (community != null) {
					AmityCommunityProfileActionsBottomSheet(
						modifier = Modifier,
						community = community!!,
						shouldShow = expanded,
						onDismiss = { expanded = false },
					)
				}
				PullRefreshIndicator(
					refreshing = state.isRefreshing,
					state = pullRefreshState,
					modifier = Modifier.align(Alignment.TopCenter),
				)
			}
		}
	}

}