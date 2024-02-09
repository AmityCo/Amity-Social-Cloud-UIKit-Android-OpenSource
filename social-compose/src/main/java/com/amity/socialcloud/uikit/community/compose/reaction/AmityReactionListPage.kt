package com.amity.socialcloud.uikit.community.compose.reaction

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.LocalViewModelStoreOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.paging.compose.collectAsLazyPagingItems
import com.amity.socialcloud.sdk.model.core.reaction.AmityReactionReferenceType
import com.amity.socialcloud.uikit.community.compose.reaction.components.AmityReactionTab
import com.amity.socialcloud.uikit.community.compose.reaction.elements.AmityReactionListItem
import com.amity.socialcloud.uikit.community.compose.ui.elements.AmityPagingErrorItem
import com.amity.socialcloud.uikit.community.compose.ui.elements.AmityPagingLoadingItem
import com.amity.socialcloud.uikit.community.compose.utils.pagingLoadStateItem

@Composable
fun AmityReactionListPage(
    modifier: Modifier = Modifier,
    referenceType: AmityReactionReferenceType,
    referenceId: String
) {
    val viewModelStoreOwner = checkNotNull(LocalViewModelStoreOwner.current) {
        "No ViewModelStoreOwner was provided via LocalViewModelStoreOwner"
    }
    val viewModel =
        viewModel<AmityReactionListPageViewModel>(viewModelStoreOwner = viewModelStoreOwner)

    val reactions = remember(viewModel, referenceType, referenceId) {
        viewModel.getReactions(referenceType, referenceId)
    }.collectAsLazyPagingItems()

    Column(
        modifier = modifier.fillMaxSize()
    ) {
        AmityReactionTab(
            reactionCount = reactions.itemCount
        )
        LazyColumn(
            modifier = modifier.fillMaxSize()
        ) {
            pagingLoadStateItem(
                loadState = reactions.loadState.prepend,
                loading = { AmityPagingLoadingItem(modifier) },
                error = { AmityPagingErrorItem(modifier) },
            )

            items(
                count = reactions.itemCount,
                key = { index -> reactions[index]?.getReactionId() ?: index },
            ) { index ->
                val reaction = reactions[index] ?: return@items

                AmityReactionListItem(
                    modifier = modifier,
                    avatarUrl = reaction.getCreator()?.getAvatar()?.getUrl(),
                    displayName = reaction.getCreatorId()
                )
            }

            pagingLoadStateItem(
                loadState = reactions.loadState.append,
                loading = { AmityPagingLoadingItem(modifier) },
                error = { AmityPagingErrorItem(modifier) },
            )
        }
    }
}

@Preview
@Composable
fun AmityReactionListPagePreview() {
    AmityReactionListPage(
        referenceType = AmityReactionReferenceType.COMMENT,
        referenceId = "",
    )
}