package com.amity.socialcloud.uikit.community.compose.screenshot.fakes

import androidx.paging.PagingData
import com.amity.socialcloud.sdk.model.core.user.AmityUser
import com.amity.socialcloud.sdk.model.social.comment.AmityComment
import com.amity.socialcloud.sdk.model.social.comment.AmityCommentReferenceType
import com.amity.socialcloud.uikit.common.ad.AmityListItem
import com.amity.socialcloud.uikit.community.compose.comment.AmityCommentTrayComponentViewModel
import com.amity.socialcloud.uikit.community.compose.screenshot.fixtures.FakeUserFactory
import io.reactivex.rxjava3.core.Flowable
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flowOf

/**
 * Fake [AmityCommentTrayComponentViewModel] — shows empty comment tray.
 *
 * TODO: Add FakeCommentFactory and a populated constructor overload when
 *       screenshot tests for the comment tray UI are needed.
 *       Use PagingData.from(listOf(...)) to supply fake comments via [getComments].
 */
class FakeAmityCommentTrayComponentViewModel(
    commentListStateOverride: CommentListState = CommentListState.EMPTY,
    private val fakeUser: AmityUser = FakeUserFactory.currentUser(),
) : AmityCommentTrayComponentViewModel() {

    override val commentListState: MutableStateFlow<CommentListState> =
        MutableStateFlow(commentListStateOverride)

    override val sheetUiState: StateFlow<CommentBottomSheetState> =
        MutableStateFlow(CommentBottomSheetState.CloseSheet).asStateFlow()

    override val replyContext: StateFlow<Pair<AmityComment, String>?> =
        MutableStateFlow(null).asStateFlow()

    override val replyComments: StateFlow<Map<String, List<AmityComment>>> =
        MutableStateFlow(emptyMap<String, List<AmityComment>>()).asStateFlow()

    override val replyLoading: StateFlow<Map<String, Boolean>> =
        MutableStateFlow(emptyMap<String, Boolean>()).asStateFlow()

    override fun getCurrentUser(): Flowable<AmityUser> = Flowable.just(fakeUser)

    override fun getComments(
        referenceId: String,
        referenceType: AmityCommentReferenceType,
        communityId: String?,
        includeDeleted: Boolean,
    ): Flow<PagingData<AmityListItem>> = flowOf(PagingData.empty())
}
