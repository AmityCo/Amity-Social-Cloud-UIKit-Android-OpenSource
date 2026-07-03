package com.amity.socialcloud.uikit.chat.compose.setting

import android.net.Uri
import com.amity.socialcloud.sdk.api.chat.AmityChatClient
import com.amity.socialcloud.sdk.helper.core.coroutines.asFlow
import com.amity.socialcloud.sdk.model.chat.channel.AmityChannel
import com.amity.socialcloud.sdk.model.core.file.AmityImage
import com.amity.socialcloud.sdk.model.core.file.upload.AmityUploadResult
import com.amity.socialcloud.uikit.common.base.AmityBaseViewModel
import com.amity.socialcloud.uikit.common.service.AmityFileService
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch

class AmityEditGroupProfilePageViewModel(
    private val channelId: String,
) : AmityBaseViewModel() {

    private val _avatarUploadState = MutableStateFlow<AvatarUploadState>(AvatarUploadState.Idle)
    val avatarUploadState: StateFlow<AvatarUploadState> = _avatarUploadState

    private var uploadedAvatar: AmityImage? = null

    fun getChannelFlow(): Flow<AmityChannel> {
        return AmityChatClient.newChannelRepository()
            .getChannel(channelId)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .asFlow()
            .catch { }
    }

    fun uploadAvatar(
        uri: Uri,
        onInappropriateImageError: () -> Unit,
    ) {
        _avatarUploadState.value = AvatarUploadState.Uploading(uri)
        uploadedAvatar = null

        AmityFileService().uploadImage(uri)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { uploadStatus ->
                    when (uploadStatus) {
                        is AmityUploadResult.COMPLETE -> {
                            uploadedAvatar = uploadStatus.getFile()
                            _avatarUploadState.value = AvatarUploadState.Success(uri)
                        }
                        is AmityUploadResult.ERROR -> {
                            val error = uploadStatus.getError()
                            _avatarUploadState.value = AvatarUploadState.Failed
                            if (error.code == 400314) {
                                onInappropriateImageError()
                            }
                        }
                        is AmityUploadResult.CANCELLED -> {
                            _avatarUploadState.value = AvatarUploadState.Failed
                        }
                        else -> {}
                    }
                },
                { _avatarUploadState.value = AvatarUploadState.Failed },
            )
    }

    fun updateProfile(
        displayName: String,
        onSuccess: () -> Unit = {},
        onError: (Throwable) -> Unit = {},
    ) {
        editChannel(
            displayName = displayName,
            avatar = uploadedAvatar,
            onSuccess = onSuccess,
            onError = onError,
        )
    }

    private fun editChannel(
        displayName: String,
        avatar: AmityImage?,
        onSuccess: () -> Unit,
        onError: (Throwable) -> Unit,
    ) {
        AmityChatClient.newChannelRepository()
            .editChannel(channelId)
            .displayName(displayName)
            .apply {
                if (avatar != null) avatar(avatar)
            }
            .build()
            .apply()
            .ignoreElement()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { onSuccess() },
                { error -> onError(error) },
            )
    }

    sealed class AvatarUploadState {
        object Idle : AvatarUploadState()
        data class Uploading(val uri: Uri) : AvatarUploadState()
        data class Success(val uri: Uri) : AvatarUploadState()
        object Failed : AvatarUploadState()
    }
}
