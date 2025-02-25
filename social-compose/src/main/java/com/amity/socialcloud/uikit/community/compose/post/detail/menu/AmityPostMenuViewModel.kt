package com.amity.socialcloud.uikit.community.compose.post.detail.menu

import androidx.lifecycle.viewModelScope
import com.amity.socialcloud.sdk.api.core.AmityCoreClient
import com.amity.socialcloud.sdk.api.social.AmitySocialClient
import com.amity.socialcloud.sdk.model.core.permission.AmityPermission
import com.amity.socialcloud.sdk.model.social.post.AmityPost
import com.amity.socialcloud.uikit.common.base.AmityBaseViewModel
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.schedulers.Schedulers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class AmityPostMenuViewModel : AmityBaseViewModel() {

    private val _sheetUIState by lazy {
        MutableStateFlow<AmityPostMenuSheetUIState>(AmityPostMenuSheetUIState.CloseSheet)
    }
    val sheetUIState get() = _sheetUIState

    private val _dialogUIState by lazy {
        MutableStateFlow<AmityPostMenuDialogUIState>(AmityPostMenuDialogUIState.CloseDialog)
    }
    val dialogUIState get() = _dialogUIState

    fun updateSheetUIState(uiState: AmityPostMenuSheetUIState) {
        viewModelScope.launch {
            _sheetUIState.value = uiState
        }
    }

    fun updateDialogUIState(uiState: AmityPostMenuDialogUIState) {
        viewModelScope.launch {
            _dialogUIState.value = uiState
        }
    }

    fun deletePost(
        postId: String,
        onSuccess: () -> Unit,
        onError: (Throwable) -> Unit,
    ) {
        AmitySocialClient.newPostRepository()
            .softDeletePost(postId)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnComplete(onSuccess)
            .doOnError(onError)
            .subscribe()
    }

    fun flagPost(
        postId: String,
        onSuccess: () -> Unit,
        onError: (Throwable) -> Unit,
    ) {
        AmitySocialClient.newPostRepository()
            .flagPost(postId)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnComplete(onSuccess)
            .doOnError(onError)
            .subscribe()
    }

    fun unflagPost(
        postId: String,
        onSuccess: () -> Unit,
        onError: (Throwable) -> Unit,
    ) {
        AmitySocialClient.newPostRepository()
            .unflagPost(postId)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnComplete(onSuccess)
            .doOnError(onError)
            .subscribe()
    }

    fun checkDeleteCommunityPostPermission(communityId: String): Flowable<Boolean> {
        return AmityCoreClient.hasPermission(AmityPermission.DELETE_COMMUNITY_POST)
            .atCommunity(communityId)
            .check()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }

    fun checkDeleteUserFeedPostPermission(): Flowable<Boolean> {
        return AmityCoreClient.hasPermission(AmityPermission.DELETE_USER_FEED_POST)
            .atGlobal()
            .check()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }
    
    fun isNotMember(post: AmityPost?): Boolean {
        val isNotMember = !((post?.getTarget() as? AmityPost.Target.COMMUNITY)?.getCommunity()?.isJoined() ?: true)
        return isNotMember
    }
}

sealed class AmityPostMenuSheetUIState(val postId: String) {

    data class OpenSheet(val id: String) : AmityPostMenuSheetUIState(id)

    object CloseSheet : AmityPostMenuSheetUIState("")
}

sealed class AmityPostMenuDialogUIState {

    data class OpenConfirmDeleteDialog(val postId: String) : AmityPostMenuDialogUIState()

    data class OpenConfirmEditDialog(val postId: String) : AmityPostMenuDialogUIState()

    data class OpenConfirmClosePollDialog(val pollId: String) : AmityPostMenuDialogUIState()

    object CloseDialog : AmityPostMenuDialogUIState()
}