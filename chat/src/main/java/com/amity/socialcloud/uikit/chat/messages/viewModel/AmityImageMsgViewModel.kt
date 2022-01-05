package com.amity.socialcloud.uikit.chat.messages.viewModel

import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableField
import com.amity.socialcloud.sdk.AmityCoreClient
import com.amity.socialcloud.sdk.chat.message.AmityMessage
import com.amity.socialcloud.sdk.core.file.AmityFileRepository
import com.amity.socialcloud.sdk.core.file.AmityImage
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.io.File

class AmityImageMsgViewModel : AmitySelectableMessageViewModel() {

    val imageUrl = ObservableField<String>("")
    val uploading = ObservableBoolean(false)
    val uploadProgress = ObservableField(0)

    init {
        uploadProgress.addOnPropertyChanged {
            if (uploadProgress.get() == 100) {
                uploading.set(false)
            } else {
                uploading.set(true)
            }
        }
    }

    fun getImageUploadProgress(message: AmityMessage) {
        val imageData = message.getData() as AmityMessage.Data.IMAGE
        val localPath = imageData.getImage()?.getFilePath()
        if (!localPath.isNullOrEmpty()) {
            val file = File(localPath)
            if (file.exists() && imageUrl.get() != localPath) {
                imageUrl.set(localPath)
            }
        } else {
            if (message.getState() == AmityMessage.State.SYNCED) {
                if (imageUrl.get() != imageData.getImage()?.getUrl(AmityImage.Size.MEDIUM)) {
                    imageUrl.set(imageData.getImage()?.getUrl(AmityImage.Size.MEDIUM))
                    uploading.set(false)
                }
            } else {
                if (message.getState() == AmityMessage.State.UPLOADING) {
                    val fileRepository: AmityFileRepository = AmityCoreClient.newFileRepository()
                    addDisposable(fileRepository.getUploadInfo(message.getMessageId())
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .doOnNext { uploadInfo ->
                            if (imageUrl.get().isNullOrEmpty()
                                && !uploadInfo.getFilePath().isNullOrEmpty()
                            ) {
                                imageUrl.set(uploadInfo.getFilePath())
                            }
                            uploadProgress.set(uploadInfo.getProgressPercentage())
                        }.doOnError {

                        }.subscribe()
                    )
                }
            }
        }
    }

}