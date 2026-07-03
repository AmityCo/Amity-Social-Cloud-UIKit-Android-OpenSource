package com.amity.socialcloud.uikit.chat.compose.message.element

import android.content.ContentValues
import android.content.Context
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.widget.Toast
import com.amity.socialcloud.sdk.model.chat.message.AmityMessage
import com.amity.socialcloud.sdk.model.core.file.AmityImage
import com.amity.socialcloud.sdk.model.core.file.AmityVideo
import com.amity.socialcloud.uikit.chat.compose.R
import com.amity.socialcloud.uikit.common.utils.getVideoUrlWithFallbackQuality
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.net.URL

internal suspend fun saveImageToGallery(context: Context, message: AmityMessage) {
    val data = message.getData()
    if (data !is AmityMessage.Data.IMAGE) return
    val image = data.getImage() ?: return
    saveImageToGallery(context, image, messageId = message.getMessageId())
}

internal suspend fun saveVideoToGallery(context: Context, message: AmityMessage) {
    val data = message.getData()
    if (data !is AmityMessage.Data.VIDEO) return
    val video = data.getVideo() ?: return
    saveVideoToGallery(context, video, messageId = message.getMessageId())
}

internal suspend fun saveImageToGallery(
    context: Context,
    image: AmityImage,
    messageId: String = System.currentTimeMillis().toString(),
) {
    val imageUrl = image.getUrl(AmityImage.Size.LARGE) ?: return

    withContext(Dispatchers.IO) {
        try {
            val url = URL(imageUrl)
            val connection = url.openConnection()
            connection.connectTimeout = 15_000
            connection.readTimeout = 15_000
            val inputStream = connection.getInputStream()
            val bytes = inputStream.readBytes()
            inputStream.close()

            val fileName = "amity_${messageId}_${System.currentTimeMillis()}.jpg"
            val contentValues = ContentValues().apply {
                put(MediaStore.Images.Media.DISPLAY_NAME, fileName)
                put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg")
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    put(MediaStore.Images.Media.RELATIVE_PATH, Environment.DIRECTORY_PICTURES)
                    put(MediaStore.Images.Media.IS_PENDING, 1)
                }
            }

            val resolver = context.contentResolver
            val uri = resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)
            uri?.let {
                resolver.openOutputStream(it)?.use { outputStream ->
                    outputStream.write(bytes)
                }
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    contentValues.clear()
                    contentValues.put(MediaStore.Images.Media.IS_PENDING, 0)
                    resolver.update(it, contentValues, null, null)
                }
            }

            withContext(Dispatchers.Main) {
                Toast.makeText(context, context.getString(R.string.amity_chat_save_photo_success), Toast.LENGTH_SHORT).show()
            }
        } catch (e: Exception) {
            withContext(Dispatchers.Main) {
                Toast.makeText(context, context.getString(R.string.amity_chat_save_photo_failed), Toast.LENGTH_SHORT).show()
            }
        }
    }
}

internal suspend fun saveVideoToGallery(
    context: Context,
    video: AmityVideo,
    messageId: String = System.currentTimeMillis().toString(),
) {
    val videoUrl = video.getVideoUrlWithFallbackQuality() ?: return

    withContext(Dispatchers.IO) {
        try {
            val url = URL(videoUrl)
            val connection = url.openConnection()
            connection.connectTimeout = 15_000
            connection.readTimeout = 60_000
            val inputStream = connection.getInputStream()

            val fileName = "amity_${messageId}_${System.currentTimeMillis()}.mp4"
            val contentValues = ContentValues().apply {
                put(MediaStore.Video.Media.DISPLAY_NAME, fileName)
                put(MediaStore.Video.Media.MIME_TYPE, "video/mp4")
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    put(MediaStore.Video.Media.RELATIVE_PATH, Environment.DIRECTORY_MOVIES)
                    put(MediaStore.Video.Media.IS_PENDING, 1)
                }
            }

            val resolver = context.contentResolver
            val uri = resolver.insert(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, contentValues)
            uri?.let {
                resolver.openOutputStream(it)?.use { outputStream ->
                    inputStream.copyTo(outputStream)
                }
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    contentValues.clear()
                    contentValues.put(MediaStore.Video.Media.IS_PENDING, 0)
                    resolver.update(it, contentValues, null, null)
                }
            }
            inputStream.close()

            withContext(Dispatchers.Main) {
                Toast.makeText(context, context.getString(R.string.amity_chat_save_video_success), Toast.LENGTH_SHORT).show()
            }
        } catch (e: Exception) {
            withContext(Dispatchers.Main) {
                Toast.makeText(context, context.getString(R.string.amity_chat_save_video_failed), Toast.LENGTH_SHORT).show()
            }
        }
    }
}

