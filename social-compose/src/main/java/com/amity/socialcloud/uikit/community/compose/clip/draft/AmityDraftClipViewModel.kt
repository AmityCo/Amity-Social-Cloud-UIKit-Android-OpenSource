package com.amity.socialcloud.uikit.community.compose.clip.draft

import android.net.Uri
import com.amity.socialcloud.sdk.api.core.AmityCoreClient
import com.amity.socialcloud.sdk.api.social.AmitySocialClient
import com.amity.socialcloud.sdk.model.core.content.AmityContentFeedType
import com.amity.socialcloud.sdk.model.core.file.AmityClip
import com.amity.socialcloud.sdk.model.core.file.upload.AmityUploadResult
import com.amity.socialcloud.sdk.model.social.community.AmityCommunity
import com.amity.socialcloud.uikit.common.base.AmityBaseViewModel
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class AmityDraftClipViewModel : AmityBaseViewModel() {

    private val _clipUploadState by lazy {
        MutableStateFlow<ClipUploadState>(ClipUploadState.Idle)
    }
    val clipUploadState get() = _clipUploadState.asStateFlow()

    private val _community = MutableStateFlow<AmityCommunity?>(null)
    val community get() = _community.asStateFlow()

    fun uploadClip(uri: Uri) {
        _clipUploadState.update { ClipUploadState.Uploading(0) }

        AmityCoreClient.newFileRepository()
            .uploadClip(
                uri = uri,
                contentFeedType = AmityContentFeedType.POST
            )
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnNext { uploadResult ->
                when (uploadResult) {
                    is AmityUploadResult.COMPLETE -> {
                        val file = uploadResult.getFile()
                        _clipUploadState.update { ClipUploadState.Success(file) }
                    }

                    is AmityUploadResult.ERROR -> {
                        val error = uploadResult.getError()
                        _clipUploadState.update {
                            ClipUploadState.Error(error.message ?: "Unknown error")
                        }
                    }

                    is AmityUploadResult.PROGRESS -> {
                        val progress = uploadResult.getUploadInfo().getProgressPercentage()
                        _clipUploadState.update {
                            ClipUploadState.Uploading(progress)
                        }
                    }

                    is AmityUploadResult.CANCELLED -> {
                        _clipUploadState.update { ClipUploadState.Cancelled }
                    }
                }
            }
            .subscribe()
    }


    fun getCommunity(
        targetId: String,
    ) {
        addDisposable(
            AmitySocialClient.newCommunityRepository()
                .getCommunity(targetId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnNext { result ->
                    _community.update { result }
                }
                .subscribe()
        )
    }


    sealed class ClipUploadState {
        object Idle : ClipUploadState()
        data class Uploading(val progressPercentage: Int = 0) : ClipUploadState()
        data class Success(val clip: AmityClip) : ClipUploadState()
        data class Error(val message: String) : ClipUploadState()
        object Cancelled : ClipUploadState()
    }

}
