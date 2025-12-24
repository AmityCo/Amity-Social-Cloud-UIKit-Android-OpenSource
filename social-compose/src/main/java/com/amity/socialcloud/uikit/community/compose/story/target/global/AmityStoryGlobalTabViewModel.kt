package com.amity.socialcloud.uikit.community.compose.story.target.global

import androidx.lifecycle.viewModelScope
import androidx.paging.CombinedLoadStates
import androidx.paging.LoadState
import androidx.paging.PagingData
import com.amity.socialcloud.sdk.api.social.AmitySocialClient
import com.amity.socialcloud.sdk.api.social.storytarget.AmityGlobalStoryTargetsQueryOption
import com.amity.socialcloud.sdk.helper.core.coroutines.asFlow
import com.amity.socialcloud.sdk.model.social.post.AmityPost
import com.amity.socialcloud.sdk.model.social.story.AmityStoryTarget
import com.amity.socialcloud.uikit.common.base.AmityBaseViewModel
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.schedulers.Schedulers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

class AmityStoryGlobalTabViewModel : AmityBaseViewModel() {

    private val _targetListState by lazy {
        MutableStateFlow<TargetListState>(TargetListState.LOADING)
    }
    private val _livePosts = MutableStateFlow<List<AmityPost>>(emptyList())
    val livePosts = _livePosts.asStateFlow()
    private var livePostDisposable: Disposable? = null

    val targetListState get() = _targetListState

    fun setTargetListState(state: TargetListState) {
        _targetListState.value = state
    }

    fun getTargets(): Flow<PagingData<AmityStoryTarget>> {
        return AmitySocialClient.newStoryRepository()
            .getGlobalStoryTargets(
                queryOption = AmityGlobalStoryTargetsQueryOption.SMART
            )
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .asFlow()
            .catch {

            }
    }

    fun getLives() {
        livePostDisposable?.dispose()
        livePostDisposable = AmitySocialClient.newPostRepository()
            .getLiveRoomPosts()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                _livePosts.value = it
            }, {
                _livePosts.value = emptyList()
            })
    }

    fun prefetchStoriesFromTargets(targets: List<AmityStoryTarget>) {
        compositeDisposable.clear()
        targets.chunked(10)
            .map {
                AmitySocialClient.newStoryRepository()
                    .getStoriesByTargets(
                        targets = it.map { target ->
                            target.getTargetType() to target.getTargetId()
                        }
                    )
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe()
                    .also(compositeDisposable::add)
            }
    }

    override fun onCleared() {
        super.onCleared()
        livePostDisposable?.dispose()
    }

    sealed class TargetListState {
        object LOADING : TargetListState()
        object SUCCESS : TargetListState()
        object EMPTY : TargetListState()

        companion object {
            fun from(
                loadState: CombinedLoadStates,
                itemCount: Int,
                livePostCount: Int
            ): TargetListState {
                return if (loadState.refresh is LoadState.Loading) {
                    LOADING
                } else if (itemCount > 0 || livePostCount > 0) {
                    SUCCESS
                } else {
                    EMPTY
                }
            }
        }
    }
}