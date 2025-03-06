package com.amity.socialcloud.uikit.common.service

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.media.ExifInterface
import android.media.MediaExtractor
import android.media.MediaFormat
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.util.Log
import androidx.core.net.toUri
import com.amity.socialcloud.sdk.api.core.AmityCoreClient
import com.amity.socialcloud.sdk.model.core.content.AmityContentFeedType
import com.amity.socialcloud.sdk.model.core.error.AmityError
import com.amity.socialcloud.sdk.model.core.error.AmityException
import com.amity.socialcloud.sdk.model.core.file.AmityFile
import com.amity.socialcloud.sdk.model.core.file.AmityImage
import com.amity.socialcloud.sdk.model.core.file.AmityVideo
import com.amity.socialcloud.sdk.model.core.file.upload.AmityUploadInfo
import com.amity.socialcloud.sdk.model.core.file.upload.AmityUploadResult
import com.amity.socialcloud.uikit.common.infra.transcoder.MediaTranscoder
import com.amity.socialcloud.uikit.common.infra.transcoder.format.MediaFormatStrategyPresets
import com.ekoapp.ekosdk.internal.util.AppContext
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.core.Single
import java.io.File
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.InputStream
import java.util.UUID

class AmityFileService {

    fun cancelUpload(uploadId: String) {
        return AmityCoreClient.newFileRepository().cancelUpload(uploadId)
    }

    fun getUploadInfo(uploadId: String): Flowable<AmityUploadInfo> {
        return AmityCoreClient.newFileRepository().getUploadInfo(uploadId)
    }

    fun uploadFile(
        uri: Uri,
        uploadId: String = UUID.randomUUID().toString()
    ): Flowable<AmityUploadResult<AmityFile>> {
        return AmityCoreClient.newFileRepository().uploadFile(uri = uri, uploadId = uploadId)
    }

    fun uploadImage(
        uri: Uri,
        uploadId: String = UUID.randomUUID().toString()
    ): Flowable<AmityUploadResult<AmityImage>> {
        return rewriteImageFile(AppContext.get(), uri)
            .flatMapPublisher { file ->
                val bitmapUri = Uri.fromFile(file)
                AmityCoreClient.newFileRepository()
                    .uploadImage(uri = bitmapUri, uploadId = uploadId)
                    .doOnTerminate {
                        deleteFile(file)
                    }
            }.onErrorReturn {
                val exception = AmityException.create(it.message, it, AmityError.UNKNOWN)
                AmityUploadResult.ERROR(exception)
            }
    }

    fun uploadVideo(
        uri: Uri,
        contentFeedType: AmityContentFeedType,
        uploadId: String = UUID.randomUUID().toString()
    ): Flowable<AmityUploadResult<AmityVideo>> {
        return Single.create<Uri> { emitter ->
            if (!isHevcVideo(uri)) {
                // skip transcoding if the video is not HEVC
                emitter.onSuccess(uri)
            } else {
                val context = AppContext.get()

                val inputFile = copyUriToFile(context, uri)
                val storageDir: File? = context.getExternalFilesDir(Environment.DIRECTORY_MOVIES)

                val outputFile = File.createTempFile(
                    uploadId, /* prefix */
                    ".mp4", /* suffix */
                    storageDir /* directory */
                )
                val outputFilePath = outputFile.absolutePath

                MediaTranscoder.getInstance().transcodeVideo(
                    inputFile.absolutePath,
                    outputFilePath,
                    MediaFormatStrategyPresets.createExportPreset960x540Strategy(),
                    object : MediaTranscoder.Listener {
                        override fun onTranscodeProgress(progress: Double) {
                            // Update UI with progress if needed
                        }

                        override fun onTranscodeCompleted() {
                            emitter.onSuccess(outputFile.toUri())
                        }

                        override fun onTranscodeCanceled() {
                            emitter.onSuccess(uri)
                        }

                        override fun onTranscodeFailed(exception: Exception?) {
                            emitter.onSuccess(uri)
                        }

                    }
                )
            }
        }.flatMapPublisher {
            AmityCoreClient.newFileRepository().uploadVideo(
                uri = it,
                contentFeedType = contentFeedType,
                uploadId = uploadId
            )

        }
    }

    private fun isHevcVideo(uri: Uri): Boolean {
        val mediaExtractor = MediaExtractor()
        mediaExtractor.setDataSource(AppContext.get(), uri, null)
        for (i in 0 until mediaExtractor.trackCount) {
            val format = mediaExtractor.getTrackFormat(i)
            val mimeType = format.getString(MediaFormat.KEY_MIME)
            if (mimeType == "video/hevc") {
                return true
            }
        }
        return false
    }

    private fun copyUriToFile(context: Context, uri: Uri): File {
        val inputStream = context.contentResolver.openInputStream(uri)
            ?: throw FileNotFoundException("Could not open URI: $uri")
        val tempFile = File(context.cacheDir, System.currentTimeMillis().toString())
        tempFile.outputStream().use { outputStream ->
            inputStream.copyTo(outputStream)
        }
        return tempFile
    }

    private fun getInputStream(context: Context, uri: Uri): InputStream? {
        return if (uri.scheme != "content") {
            val file = File(uri.path)
            file.inputStream()
        } else {
            context.contentResolver.run {
                openInputStream(uri)
            }
        }
    }

    fun rewriteImageFile(context: Context, fileUri: Uri): Single<File> {
        return Single.create { emitter ->
            val filename = "img_cache_${UUID.randomUUID()}.png"
            val file = if (Build.VERSION.SDK_INT > Build.VERSION_CODES.P) {
                File("${context.cacheDir.absolutePath}${File.separator}$filename")
            } else {
                File(context.cacheDir, filename)
            }

            var inputStream: InputStream? = null
            var secondInputStream: InputStream? = null
            var outputStream: FileOutputStream? = null

            try {
                // Determine the orientation of the image
                val exifOrientation: Int? = try {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                        inputStream = getInputStream(context, fileUri)
                        inputStream?.let { inputStream ->
                            val exif = ExifInterface(inputStream).getAttributeInt(
                                ExifInterface.TAG_ORIENTATION,
                                ExifInterface.ORIENTATION_NORMAL
                            )
                            inputStream.close()
                            exif
                        }
                    } else {
                        ExifInterface(file.absolutePath).getAttributeInt(
                            ExifInterface.TAG_ORIENTATION,
                            ExifInterface.ORIENTATION_NORMAL
                        )
                    }
                } catch (e: Exception) {
                    null
                } finally {
                    inputStream?.close()
                }

                // Re-open input stream to decode the image with downsampling
                inputStream = getInputStream(context, fileUri)
                val options = BitmapFactory.Options().apply {
                    inJustDecodeBounds = true
                    BitmapFactory.decodeStream(inputStream, null, this)

                    // Calculate sample size (reduce resolution by a factor)
                    inSampleSize = calculateInSampleSize(this, 2048, 2048)
                    inJustDecodeBounds = false
                }

                // Re-open input stream for actual decoding
                secondInputStream = getInputStream(context, fileUri)
                val bitmap = secondInputStream?.let {
                    BitmapFactory.decodeStream(it, null, options)?.let { decodedBitmap ->
                        val matrix = Matrix()
                        when (exifOrientation) {
                            ExifInterface.ORIENTATION_FLIP_HORIZONTAL -> matrix.setScale(-1f, 1f)
                            ExifInterface.ORIENTATION_ROTATE_180 -> matrix.setRotate(180f)
                            ExifInterface.ORIENTATION_FLIP_VERTICAL -> {
                                matrix.setRotate(180f)
                                matrix.postScale(-1f, 1f)
                            }

                            ExifInterface.ORIENTATION_TRANSPOSE -> {
                                matrix.setRotate(90f)
                                matrix.postScale(-1f, 1f)
                            }

                            ExifInterface.ORIENTATION_ROTATE_90 -> matrix.setRotate(90f)
                            ExifInterface.ORIENTATION_TRANSVERSE -> {
                                matrix.setRotate(-90f)
                                matrix.postScale(-1f, 1f)
                            }

                            ExifInterface.ORIENTATION_ROTATE_270 -> matrix.setRotate(-90f)
                        }
                        // Apply transformation if needed
                        if (!matrix.isIdentity) {
                            Bitmap.createBitmap(
                                decodedBitmap,
                                0,
                                0,
                                decodedBitmap.width,
                                decodedBitmap.height,
                                matrix,
                                true
                            ).apply {
                                decodedBitmap.recycle()
                            }
                        } else {
                            decodedBitmap
                        }
                    }
                }

                // Compress and save the bitmap to reduce file size
                bitmap?.let {
                    outputStream = FileOutputStream(file)
                    it.compress(Bitmap.CompressFormat.PNG, 100, outputStream!!)
                    it.recycle()
                    emitter.onSuccess(file)
                } ?: run {
                    emitter.onError(Throwable("Failed to decode bitmap"))
                }
            } catch (e: Throwable) {
                emitter.onError(e)
            } finally {
                try {
                    inputStream?.close()
                    secondInputStream?.close()
                    outputStream?.close()
                } catch (closeException: Exception) {
                    Log.e("AmityFileService", "Failed to close streams", closeException)
                }
            }
        }
    }

    private fun calculateInSampleSize(
        options: BitmapFactory.Options,
        reqWidth: Int,
        reqHeight: Int
    ): Int {
        val (height: Int, width: Int) = options.run { outHeight to outWidth }
        var inSampleSize = 1

        if (height > reqHeight || width > reqWidth) {
            val halfHeight: Int = height / 2
            val halfWidth: Int = width / 2

            while (halfHeight / inSampleSize >= reqHeight && halfWidth / inSampleSize >= reqWidth) {
                inSampleSize *= 2
            }
        }
        return inSampleSize
    }

    private fun deleteFile(file: File) {
        if (file.exists()) {
            val success = file.delete()
        }
    }

}