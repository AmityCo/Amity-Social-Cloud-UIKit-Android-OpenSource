package com.amity.socialcloud.uikit.chat.messages.viewModel

import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableField
import com.amity.socialcloud.sdk.model.chat.message.AmityMessage
import com.amity.socialcloud.sdk.model.core.file.AmityImage
import com.amity.socialcloud.uikit.common.service.AmityFileService
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers
import java.io.File

class AmityImageMsgViewModel : AmitySelectableMessageViewModel() {

    val imageUrl = ObservableField("")
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
                    addDisposable(AmityFileService().getUploadInfo(message.getMessageId())
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .doOnNext { uploadInfo ->
                            if (!uploadInfo.getFilePath().isNullOrEmpty()
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