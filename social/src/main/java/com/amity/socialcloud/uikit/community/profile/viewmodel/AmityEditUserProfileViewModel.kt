package com.amity.socialcloud.uikit.community.profile.viewmodel

import android.net.Uri
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import com.amity.socialcloud.sdk.api.core.AmityCoreClient
import com.amity.socialcloud.sdk.model.core.file.AmityImage
import com.amity.socialcloud.sdk.model.core.file.upload.AmityUploadResult
import com.amity.socialcloud.sdk.model.core.user.AmityUser
import com.amity.socialcloud.uikit.common.base.AmityBaseViewModel
import com.amity.socialcloud.uikit.common.model.AmityEventIdentifier
import com.amity.socialcloud.uikit.common.service.AmityFileService
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.core.Single

class AmityEditUserProfileViewModel : AmityBaseViewModel() {
    var profileImage: AmityImage? = null
    var profileUri: Uri? = null
    var updating: Boolean = false
    var user: AmityUser? = null

    var userNameMaxTextLength = 100
    var aboutMaxTextLength = 180

    val displayName = MutableLiveData<String>().apply { value = "" }
    val about = MutableLiveData<String>().apply { value = "" }
    val hasProfileUpdate = MutableLiveData(false)
    val mediatorLiveData = MediatorLiveData<String>().apply {
        addSource(displayName) { value ->
            setValue(value)
        }
        addSource(about) { value -> setValue(value) }
    }


    fun updateUser(): Single<AmityUser> {
        val updateUserBuilder = AmityCoreClient.editUser()
            .apply {
                if (displayName.value != user?.getDisplayName()) {
                    displayName(displayName.value!!)
                }
            }
            .description(about.value!!)

        if (profileImage != null) {
            updateUserBuilder.avatar(profileImage!!)
        }
        return updateUserBuilder.build().apply()
    }

    fun uploadProfilePicture(uri: Uri): Flowable<AmityUploadResult<AmityImage>> {
        updating = true
        checkProfileUpdate()
        return AmityFileService().uploadImage(uri)
    }

    fun updateImageUploadStatus(ekoImageUpload: AmityUploadResult<AmityImage>) {
        when (ekoImageUpload) {
            is AmityUploadResult.COMPLETE -> {
                profileImage = ekoImageUpload.getFile()
                triggerEvent(AmityEventIdentifier.PROFILE_PICTURE_UPLOAD_SUCCESS)
            }
            is AmityUploadResult.ERROR, AmityUploadResult.CANCELLED -> {
                updating = false
                checkProfileUpdate()
                triggerEvent(AmityEventIdentifier.PROFILE_PICTURE_UPLOAD_FAILED)
            }
            else -> {
            }
        }
    }

    fun getUser(): Single<AmityUser>? {
        return AmityCoreClient.getCurrentUser().firstOrError()
    }


    fun updateProfileUri(profileUri: Uri?) {
        this.profileUri = profileUri
    }

    private fun hasDraft(): Boolean {
        return user != null && (displayName.value != user!!.getDisplayName() || about.value != user!!.getDescription() || profileUri != null && profileUri.toString() != getCurrentProfileUrl())
    }

    private fun getCurrentProfileUrl(): String {
        return user!!.getAvatar()
            ?.getUrl(AmityImage.Size.SMALL) ?: ""
    }

    fun checkProfileUpdate() {
        val updateAvailable = hasDraft() && !displayName.value.isNullOrEmpty() && !updating
        hasProfileUpdate.value = updateAvailable
    }

    fun errorOnUpdate() {
        updating = false
        checkProfileUpdate()
    }

}