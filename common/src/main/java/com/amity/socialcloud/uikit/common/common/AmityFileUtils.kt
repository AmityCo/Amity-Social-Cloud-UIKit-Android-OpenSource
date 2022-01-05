package com.amity.socialcloud.uikit.common.common

import android.content.ContentResolver
import android.content.Context
import android.database.CursorIndexOutOfBoundsException
import android.net.Uri
import android.provider.DocumentsContract
import android.provider.MediaStore
import android.provider.OpenableColumns
import android.webkit.MimeTypeMap
import com.amity.socialcloud.uikit.common.R
import org.apache.commons.io.FilenameUtils
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream

class AmityFileUtils {

    companion object {
        fun getFileName(uri: Uri, context: Context): String {
            var result: String? = null
            if (uri.scheme == "content") {
                val cursor =
                    context.contentResolver.query(uri, null, null, null, null)
                try {
                    if (cursor != null && cursor.moveToFirst()) {
                        result =
                            cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME))
                    }
                } finally {
                    cursor!!.close()
                }
            }
            if (result == null) {
                result = uri.path
                val cut = result!!.lastIndexOf('/')
                if (cut != -1) {
                    result = result.substring(cut + 1)
                }
            }
            return result
        }

        fun getPathFromUri(
            context: Context,
            uri: Uri
        ): String? {
            return if (isFile(uri)) {
                getPathFromFile(context, uri)
            } else if (isDocument(context, uri)) {
                getPathFromDocument(context, uri)
            } else {
                try {
                    getPathFromContent(context, uri)
                } catch (e: CursorIndexOutOfBoundsException) {
                    uri.toString()
                }
            }
        }

        private fun isFile(uri: Uri): Boolean {
            return (uri.scheme == null
                    || ContentResolver.SCHEME_FILE == uri.scheme)
        }

        private fun getPathFromFile(
            context: Context,
            uri: Uri
        ): String? {
            return uri.path
        }

        private fun isDocument(
            context: Context,
            uri: Uri
        ): Boolean {
            return DocumentsContract.isDocumentUri(context, uri)
        }

        private fun getPathFromContent(
            context: Context,
            uri: Uri
        ): String? {
            val projection =
                arrayOf(MediaStore.Images.Media.DATA)
            val cursor =
                context.contentResolver.query(uri, projection, null, null, null)
            val index = cursor!!.getColumnIndex(MediaStore.Images.Media.DATA)
            cursor.moveToFirst()
            val path = cursor.getString(index)
            cursor.close()
            return path
        }

        private fun getPathFromDocument(
            context: Context,
            uri: Uri
        ): String? {
            val fileName = getName(context.contentResolver, uri)
            var inputStream: InputStream? = null
            var output: FileOutputStream? = null
            try {
                inputStream = context.contentResolver.openInputStream(uri)
                val file = File(context.cacheDir, fileName)
                output = FileOutputStream(file)
                val bufferSize = 1024
                val buffer = ByteArray(bufferSize)
                var len: Int
                while (inputStream!!.read(buffer).also { len = it } != -1) {
                    output.write(buffer, 0, len)
                }
                output.flush()
                return file.absolutePath
            } catch (e: OutOfMemoryError) {
            } catch (e: Exception) {
            } finally {
                try {
                    inputStream?.close()
                    output?.close()
                } catch (e: Exception) {
                }
            }
            return ""
        }

        fun getName(contentResolver: ContentResolver, uri: Uri): String? {
            if (uri.scheme == null || ContentResolver.SCHEME_FILE == uri.scheme) {
                val file = File(uri.path)
                return file.name
            }
            val cursor =
                contentResolver.query(uri, null, null, null, null, null)
            try {
                if (cursor != null && cursor.moveToFirst()) {
                    return cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME))
                }
            } finally {
                cursor?.close()
            }
            return null
        }

        fun getMimeType(contentResolver: ContentResolver, uri: Uri): String? {
            if (uri.scheme == null || ContentResolver.SCHEME_FILE == uri.scheme) {
                val extension = FilenameUtils.getExtension(uri.path)
                return MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension)
            }
            return contentResolver.getType(uri)
        }

        fun getSize(contentResolver: ContentResolver, uri: Uri): Int {
            if (uri.scheme == null || ContentResolver.SCHEME_FILE == uri.scheme) {
                val file = File(uri.path)
                return file.length().toInt()
            }
            val cursor =
                contentResolver.query(uri, null, null, null, null, null)
            try {
                if (cursor != null && cursor.moveToFirst()) {
                    val sizeIndex = cursor.getColumnIndex(OpenableColumns.SIZE)
                    return if (!cursor.isNull(sizeIndex)) {
                        cursor.getInt(sizeIndex)
                    } else {
                        0
                    }
                }
            } finally {
                cursor?.close()
            }
            return 0
        }

        fun humanReadableByteCount(bytes: Long, si: Boolean): String? {
            val unit = if (si) 1000 else 1024
            if (bytes < unit) {
                return "$bytes B"
            }
            val exp =
                (Math.log(bytes.toDouble()) / Math.log(unit.toDouble())).toInt()
            val pre =
                (if (si) "kMGTPE" else "KMGTPE")[exp - 1].toString() + if (si) "" else "i"
            return String.format(
                "%.2f %sB",
                bytes / Math.pow(unit.toDouble(), exp.toDouble()),
                pre
            )
        }

        fun isFileTypeDoc(fileExtension: String): Boolean {
            return fileExtension.equals("doc", ignoreCase = true) || fileExtension.equals(
                "docx",
                ignoreCase = true
            )
        }

        fun isFileTypePdf(fileExtension: String): Boolean {
            return fileExtension.equals("pdf", ignoreCase = true)
        }

        fun getFileIcon(mimeType: String): Int {
            return (when (mimeType) {
                "application/x-msdos-program",
                "application/vnd.microsoft.portable-executable",
                "application/octet-stream" -> R.drawable.amity_ic_exe_large
                "application/rar" -> R.drawable.amity_ic_rar_large
                "application/pdf" -> R.drawable.amity_ic_pdf_large
                "application/msword",
                "application/vnd.openxmlformats-officedocument.wordprocessingml.document" -> R.drawable.amity_ic_doc_large
                "application/vnd.ms-excel",
                "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet" -> R.drawable.amity_ic_xls_large
                "text/html" -> R.drawable.amity_ic_html_large
                "video/mp4" -> R.drawable.amity_ic_mp4_large
                "video/quicktime" -> R.drawable.amity_ic_mov_large
                "application/vnd.ms-powerpoint" -> R.drawable.amity_ic_ppt_large
                "application/vnd.openxmlformats-officedocument.presentationml.presentation" -> R.drawable.amity_ic_ppx_large
                "application/zip" -> R.drawable.amity_ic_zip_large
                "audio/mpeg" -> R.drawable.amity_ic_mp3_large
                "text/plain" -> R.drawable.amity_ic_txt_large
                "text/comma-separated-values" -> R.drawable.amity_ic_csv_large
                "video/mpeg" -> R.drawable.amity_ic_mpeg_large
                "video/x-msvideo" -> R.drawable.amity_ic_avi_large
                else -> {
                    if (mimeType.startsWith("audio")) R.drawable.amity_ic_audio_large
                    else if (mimeType.startsWith("image")) R.drawable.amity_ic_img_large
                    else R.drawable.amity_ic_file_type_unknown
                }
            })

        }
    }
}