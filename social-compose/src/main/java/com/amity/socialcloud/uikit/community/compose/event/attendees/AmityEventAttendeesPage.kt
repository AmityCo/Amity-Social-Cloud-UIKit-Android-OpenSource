package com.amity.socialcloud.uikit.community.compose.event.attendees

import android.app.Activity
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.LocalViewModelStoreOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.paging.PagingData
import androidx.paging.compose.collectAsLazyPagingItems
import com.amity.socialcloud.sdk.model.social.event.AmityEventResponse
import com.amity.socialcloud.uikit.common.compose.R
import com.amity.socialcloud.uikit.common.ui.base.AmityBasePage
import com.amity.socialcloud.uikit.common.ui.elements.AmityUserAvatarView
import com.amity.socialcloud.uikit.common.ui.elements.AmityToolBar
import com.amity.socialcloud.uikit.common.ui.theme.AmityTheme
import com.amity.socialcloud.uikit.common.utils.closePageWithResult
import com.amity.socialcloud.uikit.community.compose.ui.shimmer.AmityUserListShimmer
import com.amity.socialcloud.uikit.community.compose.search.components.AmityEmptySearchResultComponent

@Composable
fun AmityEventAttendeesPage(
    modifier: Modifier = Modifier,
    eventId: String,
) {
    val context = LocalContext.current

    val viewModelStoreOwner = checkNotNull(LocalViewModelStoreOwner.current) {
        "No ViewModelStoreOwner was provided via LocalViewModelStoreOwner"
    }

    val viewModel = viewModel<AmityEventAttendeesPageViewModel>(
        viewModelStoreOwner = viewModelStoreOwner,
        factory = object : ViewModelProvider.Factory {
            override fun <VM : androidx.lifecycle.ViewModel> create(modelClass: Class<VM>): VM {
                return AmityEventAttendeesPageViewModel(eventId) as VM
            }
        }
    )

    val loadState by viewModel.attendeeListState.collectAsState()
    val attendeesFlow by viewModel.attendees.collectAsState()

    val lazyPagingItems = (attendeesFlow ?: kotlinx.coroutines.flow.flowOf(PagingData.empty<AmityEventResponse>())).collectAsLazyPagingItems()

    AmityBasePage(pageId = "event_attendees_page") {
        Column(
            modifier = modifier
                .fillMaxSize()
                .windowInsetsPadding(WindowInsets.safeDrawing)
        ) {
            AmityToolBar(
                title = "Attendees",
                onBackClick = {
                    context.closePageWithResult(Activity.RESULT_CANCELED)
                }
            )

            LazyColumn {
                AmityEventAttendeesPageViewModel.AttendeeListState.values().find {
                    when (lazyPagingItems.loadState.refresh) {
                        is androidx.paging.LoadState.Loading -> it == AmityEventAttendeesPageViewModel.AttendeeListState.LOADING
                        is androidx.paging.LoadState.Error -> it == AmityEventAttendeesPageViewModel.AttendeeListState.ERROR
                        is androidx.paging.LoadState.NotLoading -> {
                            if (lazyPagingItems.itemCount == 0) {
                                it == AmityEventAttendeesPageViewModel.AttendeeListState.EMPTY
                            } else {
                                it == AmityEventAttendeesPageViewModel.AttendeeListState.SUCCESS
                            }
                        }
                    }
                }?.let(viewModel::setAttendeeListState)

                when (loadState) {
                    AmityEventAttendeesPageViewModel.AttendeeListState.EMPTY -> {
                    }

                    AmityEventAttendeesPageViewModel.AttendeeListState.LOADING -> {
                        item {
                            AmityUserListShimmer(
                                modifier = modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                            )
                        }
                    }

                    AmityEventAttendeesPageViewModel.AttendeeListState.SUCCESS -> {
                        items(
                            count = lazyPagingItems.itemCount,
                            key = { index -> lazyPagingItems[index]?.getUser()?.getUserId() ?: index }
                        ) { index ->
                            val attendee = lazyPagingItems[index] ?: return@items
                            val user = attendee.getUser()

                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(12.dp),
                                modifier = modifier
                                    .fillMaxSize()
                                    .padding(horizontal = 16.dp, vertical = 8.dp)
                            ) {
                                AmityUserAvatarView(
                                    user = user,
                                    size = 40.dp,
                                    modifier = modifier
                                )
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = modifier.weight(1f)
                                ) {
                                    Text(
                                        modifier = Modifier.weight(1f, fill = false),
                                        text = user?.getDisplayName() ?: "",
                                        overflow = TextOverflow.Ellipsis,
                                        maxLines = 1,
                                        style = AmityTheme.typography.bodyLegacy.copy(
                                            fontWeight = FontWeight.SemiBold,
                                        ),
                                    )
                                    val isBrandUser = user?.isBrand() ?: false
                                    if (isBrandUser) {
                                        val badge = R.drawable.amity_ic_brand_badge
                                        Image(
                                            painter = painterResource(id = badge),
                                            contentDescription = "",
                                            modifier = Modifier
                                                .size(16.dp)
                                                .padding(start = 4.dp)
                                                .testTag("user_view/brand_user_icon")
                                        )
                                    }
                                }
                            }
                        }
                    }

                    AmityEventAttendeesPageViewModel.AttendeeListState.ERROR -> {
                        item {
                            AmityEmptySearchResultComponent(modifier)
                        }
                    }
                }
            }
        }
    }
}
