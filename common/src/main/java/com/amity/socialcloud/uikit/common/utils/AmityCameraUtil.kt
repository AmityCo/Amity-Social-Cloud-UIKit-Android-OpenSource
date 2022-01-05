package com.amity.socialcloud.uikit.common.utils

import android.content.Context
import android.net.Uri
import android.os.Environment
import androidx.core.content.FileProvider
import com.amity.socialcloud.uikit.common.common.AmityFileManager
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

class AmityCameraUtil {

    companion object {
        fun createPhotoUri(context: Context, file: File): Uri? {
            return FileProvider.getUriForFile(
                context,
                context.applicationContext.packageName + ".UikitCommonProvider",
                file
            )
        }

        fun createVideoUri(context: Context, file: File): Uri? {
            return FileProvider.getUriForFile(
                context,
                context.applicationContext.packageName + ".UikitCommonProvider",
                file
            )
        }


        fun createImageFile(context: Context): File? {

            return try {
                val timeStamp: String =
                    SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())

                val storageDir: File? = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
                
                return File.createTempFile(
                    "JPEG_${timeStamp}_", /* prefix */
                    ".jpg", /* suffix */
                    storageDir /* directory */
                )
            } catch (ex: IOException) {
                null
            }
        }

        fun createVideoFile(context: Context): File? {
            return try {
                val timeStamp: String =
                    SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())

                val storageDir: File? = context.getExternalFilesDir(Environment.DIRECTORY_MOVIES)

                return File.createTempFile(
                    "VIDEO_${timeStamp}_", /* prefix */
                    ".mp4", /* suffix */
                    storageDir /* directory */
                )
            } catch (ex: IOException) {
                null
            }
        }
    }
}