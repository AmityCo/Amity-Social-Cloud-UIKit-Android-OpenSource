package com.amity.socialcloud.uikit.chat.compose.create

import android.net.Uri
import com.amity.socialcloud.sdk.api.chat.AmityChatClient
import com.amity.socialcloud.sdk.api.core.AmityCoreClient
import com.amity.socialcloud.sdk.model.core.file.AmityImage
import com.amity.socialcloud.sdk.model.core.file.upload.AmityUploadResult
import com.amity.socialcloud.sdk.model.core.user.AmityUser
import com.amity.socialcloud.uikit.common.base.AmityBaseViewModel
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class AmityCreateGroupChatPageViewModel : AmityBaseViewModel() {

    private val _creationState = MutableStateFlow<CreationState>(CreationState.Idle)
    val creationState: StateFlow<CreationState> = _creationState

    private val _avatarUploadState = MutableStateFlow<AvatarUploadState>(AvatarUploadState.Idle)
    val avatarUploadState: StateFlow<AvatarUploadState> = _avatarUploadState

    private var uploadedAvatar: AmityImage? = null

    fun uploadAvatar(
        uri: Uri,
        onInappropriateImageError: () -> Unit,
    ) {
        _avatarUploadState.value = AvatarUploadState.Uploading(uri)
        uploadedAvatar = null

        AmityCoreClient.newFileRepository()
            .uploadImage(uri)
            .doOnNext { uploadResult ->
                when (uploadResult) {
                    is AmityUploadResult.COMPLETE -> {
                        uploadedAvatar = uploadResult.getFile()
                        _avatarUploadState.value = AvatarUploadState.Success(uri)
                    }
                    is AmityUploadResult.ERROR -> {
                        val error = uploadResult.getError()
                        _avatarUploadState.value = AvatarUploadState.Failed
                        if (error.code == 400314) {
                            onInappropriateImageError()
                        }
                    }
                    else -> {}
                }
            }
            .doOnError {
                _avatarUploadState.value = AvatarUploadState.Failed
            }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe()
    }

    fun createGroup(
        displayName: String,
        members: List<AmityUser>,
        isPublic: Boolean,
        onSuccess: (String) -> Unit,
        onError: (Throwable) -> Unit = {},
    ) {
        if (displayName.isBlank() && members.isEmpty()) return

        _creationState.value = CreationState.Loading

        val name = displayName.ifBlank {
            members.joinToString { member ->
                member.getDisplayName()
                    .orEmpty()
                    .ifBlank {
                        member.getUserId()
                    }
            }
        }
        val userIds = members.map { it.getUserId() }
        doCreateGroup(
            name = name,
            userIds = userIds,
            avatar = uploadedAvatar,
            isPublic = isPublic,
            onSuccess = onSuccess,
            onError = onError,
        )
    }

    private fun doCreateGroup(
        name: String,
        userIds: List<String>,
        avatar: AmityImage?,
        isPublic: Boolean,
        onSuccess: (String) -> Unit,
        onError: (Throwable) -> Unit,
    ) {
        val builder = AmityChatClient.newChannelRepository()
            .createChannel(displayName = name)
            .community()
            .userIds(userIds)
            .isPublic(isPublic)

        if (avatar != null) {
            builder.avatar(avatar)
        }

        builder.build()
            .create()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSuccess { channel ->
                _creationState.value = CreationState.Success(channel.getChannelId())
                onSuccess(channel.getChannelId())
            }
            .doOnError { error ->
                _creationState.value = CreationState.Error(error)
                onError(error)
            }
            .subscribe()
    }

    sealed class CreationState {
        object Idle : CreationState()
        object Loading : CreationState()
        data class Success(val channelId: String) : CreationState()
        data class Error(val throwable: Throwable) : CreationState()
    }

    sealed class AvatarUploadState {
        object Idle : AvatarUploadState()
        data class Uploading(val uri: Uri) : AvatarUploadState()
        data class Success(val uri: Uri) : AvatarUploadState()
        object Failed : AvatarUploadState()
    }
}
