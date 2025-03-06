package com.amity.socialcloud.uikit.community.ui.viewModel

import android.net.Uri
import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableField
import androidx.databinding.ObservableParcelable
import androidx.lifecycle.SavedStateHandle
import com.amity.socialcloud.sdk.api.core.AmityCoreClient
import com.amity.socialcloud.sdk.api.social.AmitySocialClient
import com.amity.socialcloud.sdk.api.social.community.AmityCommunityRepository
import com.amity.socialcloud.sdk.model.core.file.AmityImage
import com.amity.socialcloud.sdk.model.core.file.upload.AmityUploadResult
import com.amity.socialcloud.sdk.model.social.community.AmityCommunity
import com.amity.socialcloud.uikit.common.base.AmityBaseViewModel
import com.amity.socialcloud.uikit.common.model.AmitySelectMemberItem
import com.amity.socialcloud.uikit.common.service.AmityFileService
import com.amity.socialcloud.uikit.community.data.AmitySelectCategoryItem
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.core.Single

private const val SAVED_COMMUNITY_ID = "SAVED_COMMUNITY_ID"

class AmityCreateCommunityViewModel(private val savedState: SavedStateHandle) :
    AmityBaseViewModel() {

    val initialStateChanged = ObservableBoolean(false)
    private var initialCommunityName: String = ""
    private var initialCommunityDescription = ""
    private var initialIsPublic = true
    var initialCategory = ""
    val avatarUrl = ObservableField("")
    val communityId = ObservableField("")
    val communityName = ObservableField(initialCommunityName)
    val description = ObservableField("")
    val isPublic = ObservableBoolean(true)
    val isAdmin = ObservableBoolean(false)
    val addMemberVisible = ObservableBoolean(true)
    val category = ObservableParcelable(AmitySelectCategoryItem())
    val nameError = ObservableBoolean(false)
    val selectedMembersList = ArrayList<AmitySelectMemberItem>()
    private val userIdList = ArrayList<String>()
    var amityImage: AmityImage? = null

    var savedCommunityId: String? = null
        set(value) {
            savedState.set(SAVED_COMMUNITY_ID, value)
            field = value
        }

    init {
        savedState.get<String>(SAVED_COMMUNITY_ID)?.let {
            communityId.set(it)
        }
    }

    fun changePostType(value: Boolean) {
        isPublic.set(value)
        initialStateChanged.set(isPublic.get() != initialIsPublic)
    }

    fun changeAdminPost() {
        isAdmin.set(!isAdmin.get())
    }

    fun uploadProfilePicture(uri: Uri): Flowable<AmityUploadResult<AmityImage>> {
        return AmityFileService().uploadImage(uri = uri)
    }

    fun setCategory(categoryAmity: AmitySelectCategoryItem) {
        this.category.set(categoryAmity)
        initialStateChanged.set(categoryAmity.name != initialCategory)
    }

    fun createCommunity(): Single<AmityCommunity> {
        resetError()
        val communityRepository: AmityCommunityRepository =
            AmitySocialClient.newCommunityRepository()
        return if (isAdmin.get()) {
            val builder = communityRepository.createCommunity(communityName.get()!!.trim())
            if (amityImage != null) {
                builder.avatar(amityImage!!)
            }
            if (category.get()?.categoryId?.isNotEmpty() == true) {
                builder.categoryIds(listOf(category.get()?.categoryId!!))
            }
            builder.isPublic(isPublic.get())
                .description(description.get()?.trim() ?: "")
                .userIds(userIdList)
                .build()
                .create()
        } else {
            val builder = communityRepository.createCommunity(communityName.get()!!.trim())
            if (amityImage != null) {
                builder.avatar(amityImage!!)
            }
            if (category.get()?.categoryId?.isNotEmpty() == true) {
                builder.categoryIds(listOf(category.get()?.categoryId!!))
            }
            builder.isPublic(isPublic.get())
                .description(description.get()?.trim() ?: "")
                .userIds(userIdList)
                .build()
                .create()
        }
    }

    fun editCommunity(): Single<AmityCommunity> {
        val communityRepository: AmityCommunityRepository =
            AmitySocialClient.newCommunityRepository()
        val builder = communityRepository.editCommunity(communityId.get()!!)
        if (amityImage != null) {
            builder.avatar(amityImage!!)
        }
        if (category.get()?.categoryId?.isNotEmpty() == true) {
            builder.categoryIds(listOf(category.get()?.categoryId!!))
        }
        return builder.displayName(communityName.get()!!.trim())
            .isPublic(isPublic.get())
            .description(description.get()?.trim() ?: "")
            .build()
            .apply()
    }

    fun getCommunityDetail(): Flowable<AmityCommunity> {
        val communityRepository: AmityCommunityRepository =
            AmitySocialClient.newCommunityRepository()
        return communityRepository.getCommunity(communityId.get()!!)
    }

    fun setCommunityDetails(amityCommunity: AmityCommunity) {
        savedState.set(SAVED_COMMUNITY_ID, amityCommunity.getCommunityId())
        initialCommunityName = amityCommunity.getDisplayName()
        initialCommunityDescription = amityCommunity.getDescription()
        initialIsPublic = amityCommunity.isPublic()
        communityId.set(amityCommunity.getCommunityId())
        communityName.set(amityCommunity.getDisplayName())
        avatarUrl.set(amityCommunity.getAvatar()?.getUrl(AmityImage.Size.LARGE) ?: "")
        description.set(amityCommunity.getDescription())
        isPublic.set(amityCommunity.isPublic())
        category.set(
            AmitySelectCategoryItem(
                name = amityCommunity.getCategories()
                    .joinToString(separator = " ") { it.getName() })
        )
    }

    fun createIdList() {
        userIdList.clear()
        if (!isPublic.get()) {
            for (i in 0 until selectedMembersList.size - 1) {
                userIdList.add(selectedMembersList[i].id)
            }
        }
    }

    private fun resetError() {
        nameError.set(false)
    }

    fun setPropertyChangeCallback() {
        communityName.addOnPropertyChanged {
            initialStateChanged.set(communityName.get() != initialCommunityName)
        }

        description.addOnPropertyChanged {
            initialStateChanged.set(description.get() != initialCommunityDescription)
        }
    }
}