package com.amity.socialcloud.uikit.community.compose.user.edit

import android.net.Uri
import com.amity.socialcloud.sdk.api.core.AmityCoreClient
import com.amity.socialcloud.sdk.helper.core.coroutines.asFlow
import com.amity.socialcloud.sdk.model.core.file.AmityImage
import com.amity.socialcloud.sdk.model.core.file.upload.AmityUploadResult
import com.amity.socialcloud.sdk.model.core.user.AmityUser
import com.amity.socialcloud.uikit.common.base.AmityBaseViewModel
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers
import kotlinx.coroutines.flow.Flow

class AmityEditUserProfilePageViewModel : AmityBaseViewModel() {

    fun getUser(): Flow<AmityUser> {
        return AmityCoreClient
            .getCurrentUser()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .asFlow()
    }

    fun updateUser(
        displayName: String,
        description: String,
        avatarUri: Uri?,
        onSuccess: (AmityUser) -> Unit,
        onError: (String) -> Unit,
        onInappropriateImageError: () -> Unit,
    ) {
        if (avatarUri == null) {
            editUser(
                displayName = displayName,
                description = description,
                avatar = null,
                onSuccess = onSuccess,
                onError = onError,
            )
        } else {
            uploadAvatar(
                uri = avatarUri,
                onSuccess = { avatarImage ->
                    editUser(
                        avatar = avatarImage,
                        displayName = displayName,
                        description = description,
                        onSuccess = onSuccess,
                        onError = onError
                    )
                },
                onError = onError,
                onInappropriateImageError = onInappropriateImageError,
            )
        }
    }

    private fun uploadAvatar(
        uri: Uri,
        onSuccess: (AmityImage) -> Unit,
        onError: (String) -> Unit,
        onInappropriateImageError: () -> Unit,
    ) {
        AmityCoreClient.newFileRepository()
            .uploadImage(uri)
            .doOnNext { uploadStatus ->
                when (uploadStatus) {
                    is AmityUploadResult.COMPLETE -> onSuccess(uploadStatus.getFile())
                    is AmityUploadResult.ERROR -> {
                        val error = uploadStatus.getError()
                        if (error.code == 500000) {
                            onInappropriateImageError()
                        }
                    }

                    else -> {}
                }
            }
            .doOnError {
                onError(it.message ?: "Failed to upload image")
            }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe()
    }

    private fun editUser(
        displayName: String,
        description: String,
        avatar: AmityImage?,
        onSuccess: (AmityUser) -> Unit,
        onError: (String) -> Unit,
    ) {
        AmityCoreClient.editUser()
            .displayName(displayName)
            .description(description)
            .apply {
                if (avatar != null) avatar(avatar)
            }
            .build()
            .apply()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSuccess(onSuccess)
            .doOnError { onError(it.message ?: "Failed to update user.") }
            .subscribe()
    }
}