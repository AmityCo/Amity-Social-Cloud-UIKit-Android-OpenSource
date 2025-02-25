package com.amity.socialcloud.uikit.community.compose.story.create

import androidx.lifecycle.viewModelScope
import com.amity.socialcloud.sdk.api.core.AmityCoreClient
import com.amity.socialcloud.uikit.common.base.AmityBaseViewModel
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class AmityCreateStoryPageViewModel : AmityBaseViewModel() {

    init {
        contentCheck()
    }

    private val _maxRecordDurationSeconds by lazy {
        MutableStateFlow(90)
    }
    val maxRecordDurationSeconds get() = _maxRecordDurationSeconds

    private fun contentCheck() {
        AmityCoreClient.getContentCheck()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSuccess { contentCheck ->
                viewModelScope.launch {
                    val duration = contentCheck.story()?.video?.maxDurationSeconds ?: 90
                    _maxRecordDurationSeconds.value = duration
                }
            }
            .subscribe()
    }
}