package com.amity.socialcloud.uikit.community.compose.post.composer.poll

import android.net.Uri
import androidx.compose.ui.text.input.TextFieldValue
import androidx.lifecycle.viewModelScope
import com.amity.socialcloud.sdk.api.core.AmityCoreClient
import com.amity.socialcloud.sdk.api.social.AmitySocialClient
import com.amity.socialcloud.sdk.helper.core.asAmityImage
import com.amity.socialcloud.sdk.helper.core.hashtag.AmityHashtag
import com.amity.socialcloud.sdk.helper.core.mention.AmityMentionMetadata
import com.amity.socialcloud.sdk.helper.core.mention.AmityMentionMetadataCreator
import com.amity.socialcloud.sdk.model.core.file.AmityFileInfo
import com.amity.socialcloud.sdk.model.core.file.AmityFileType
import com.amity.socialcloud.sdk.model.core.file.AmityImage
import com.amity.socialcloud.sdk.model.core.file.AmityRawFile
import com.amity.socialcloud.sdk.model.core.file.upload.AmityUploadResult
import com.amity.socialcloud.sdk.model.social.poll.AmityPoll
import com.amity.socialcloud.sdk.model.social.poll.AmityPollAnswer
import com.amity.socialcloud.sdk.model.social.post.AmityPost
import com.amity.socialcloud.uikit.common.base.AmityBaseViewModel
import com.amity.socialcloud.uikit.common.service.AmityFileService
import com.amity.socialcloud.uikit.community.compose.post.composer.createMetadata
import com.amity.socialcloud.uikit.community.compose.post.composer.poll.model.ImagePollUiState
import com.amity.socialcloud.uikit.community.compose.post.model.AmityFileUploadState
import com.google.gson.JsonObject
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.schedulers.Schedulers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class AmityPollPostComposerViewModel : AmityBaseViewModel() {

    private val _imagePollUiState = MutableStateFlow(listOf(ImagePollUiState(), ImagePollUiState()))
    val imagePollUiState = _imagePollUiState.asStateFlow()

    fun createPost(
        question: String,
        postTitle: String?,
        options: List<String>,
        imagePoll: List<ImagePollUiState>? = null,
        isMultipleChoice: Boolean,
        duration: Long,
        targetId: String,
        targetType: AmityPost.TargetType,
        mentionedUsers: List<AmityMentionMetadata.USER>,
        hashtags: List<AmityHashtag> = emptyList(),
    ): Single<AmityPost> {
        val metadata = createMetadata(mentionedUsers, hashtags)
        val mentionUserIds = mentionedUsers.map { it.getUserId() }.toSet()

        return createPollPost(
            question = question,
            postTitle = postTitle,
            options = options,
            imagePoll = imagePoll,
            isMultipleChoice = isMultipleChoice,
            closedIn = duration,
            targetId = targetId,
            targetType = targetType,
            metadata = metadata,
            mentionUserIds = mentionUserIds,
            hashtags = hashtags,
        )
    }

    private fun createPollPost(
        question: String,
        postTitle: String?,
        options: List<String>,
        imagePoll: List<ImagePollUiState>? = null,
        isMultipleChoice: Boolean,
        closedIn: Long,
        targetId: String,
        targetType: AmityPost.TargetType,
        metadata: JsonObject?,
        mentionUserIds: Set<String>,
        hashtags: List<AmityHashtag>,
    ): Single<AmityPost> {
        val pollRepository = AmitySocialClient.newPollRepository()
        val postRepository = AmitySocialClient.newPostRepository()

        val answers = if (!imagePoll.isNullOrEmpty()) {
            // Create image-based answers, filtering out items without valid fileId
            imagePoll.mapNotNull { imageState ->
                if (imageState.image?.getFileId() != null && imageState.image.getFileId().isNotBlank()) {
                    AmityPollAnswer.Data.IMAGE(
                        imageState.answer.text,
                        imageState.image.getFileId(),
                    )
                } else {
                    null // This will be filtered out by mapNotNull
                }
            }
        } else {
            // Create text-based answers
            options.map { option -> AmityPollAnswer.Data.TEXT(option) }
        }

        return pollRepository.createPoll(
            question = question,
        )
            .answers(answers)
            .closedIn(closedIn)
            .apply {
                if (isMultipleChoice) {
                    answerType(AmityPoll.AnswerType.MULTIPLE)
                } else {
                    answerType(AmityPoll.AnswerType.SINGLE)
                }
            }
            .build()
            .create()
            .flatMap {
                postRepository
                    .createPollPost(
                        targetId = targetId,
                        targetType = targetType,
                        pollId = it,
                        text = question,
                        title = postTitle,
                        metadata = metadata,
                        mentionUserIds = mentionUserIds,
                        hashtags = hashtags.map { hashtag -> hashtag.getText() },
                    )
            }
    }

    fun uploadMediaForIndex(index: Int, imageUrl: Uri) {
        // Update UI state to show upload starting
        updateImagePollUiState(
            index, _imagePollUiState.value[index].copy(
                uploadState = AmityFileUploadState.UPLOADING,
                uploadProgress = 0
            )
        )

        AmityFileService()
            .uploadImage(imageUrl)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnNext { result ->
                updateMediaUploadStatusForIndex(index, result)
            }
            .ignoreElements()
            .subscribe()
    }

    private fun updateMediaUploadStatusForIndex(
        index: Int,
        result: AmityUploadResult<AmityFileInfo>,
    ) {
        val currentState = _imagePollUiState.value
        if (index >= 0 && index < currentState.size) {
            val currentItem = currentState[index]

            when (result) {
                is AmityUploadResult.PROGRESS -> {
                    updateImagePollUiState(
                        index, currentItem.copy(
                            uploadState = AmityFileUploadState.UPLOADING,
                            uploadProgress = result.getUploadInfo().getProgressPercentage()
                        )
                    )
                }

                is AmityUploadResult.COMPLETE -> {
                    val file = result.getFile()
                    getAmityImage(
                        fileId = file.getFileId(),
                        onSuccess = { amityImage ->
                            updateImagePollUiState(
                                index, currentItem.copy(
                                    uploadState = AmityFileUploadState.COMPLETE,
                                    uploadProgress = 100,
                                    image = amityImage,
                                    uploadError = null,
                                )
                            )
                        }
                    )
                }

                is AmityUploadResult.ERROR, AmityUploadResult.CANCELLED -> {
                    val error = (result as? AmityUploadResult.ERROR)?.getError()
                    updateImagePollUiState(
                        index, currentItem.copy(
                            uploadState = AmityFileUploadState.FAILED,
                            uploadProgress = 0,
                            uploadError = error
                        )
                    )
                }
            }
        }
    }

    fun updateImagePollUiState(index: Int, newState: ImagePollUiState) {
        viewModelScope.launch {
            val currentState = _imagePollUiState.value.toMutableList()
            if (index >= 0 && index < currentState.size) {
                currentState[index] = newState
                _imagePollUiState.update {
                    currentState
                }
            }
        }
    }

    fun addImagePollOption() {
        viewModelScope.launch {
            val currentState = _imagePollUiState.value.toMutableList()
            if (currentState.size <= 10) {
                currentState.add(ImagePollUiState())
                _imagePollUiState.update {
                    currentState
                }
            }
        }
    }

    fun removeImagePollOption(targetState: ImagePollUiState) {
        viewModelScope.launch {
            val currentState = _imagePollUiState.value.toMutableList()
            currentState.remove(targetState)
            _imagePollUiState.update {
                currentState
            }
        }
    }

    private fun getAmityImage(fileId: String, onSuccess: (AmityImage) -> Unit) {
        addDisposable(
            AmityCoreClient.newFileRepository().getFile(fileId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSuccess { rawFile: AmityRawFile ->
                    if (rawFile.getFileType() == AmityFileType.IMAGE) {
                        val imageFromRaw: AmityImage? =
                            rawFile.asAmityImage() // null for incorrect file type
                        imageFromRaw?.let { image ->
                            onSuccess.invoke(image)
                        }
                    }
                }
                .subscribe()
        )
    }

}