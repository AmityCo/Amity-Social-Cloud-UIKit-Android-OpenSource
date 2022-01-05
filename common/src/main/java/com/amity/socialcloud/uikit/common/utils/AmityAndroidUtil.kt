package com.amity.socialcloud.uikit.common.utils

import android.app.Activity
import android.content.Context
import android.media.MediaMetadataRetriever
import android.net.Uri
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import java.io.File

object AmityAndroidUtil {
    
    fun hideKeyboard(view: View) {
        val imm = view.context.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }
    
    fun showKeyboard(view: View) {
        val imm = view.context.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT)
    }
    
    fun isSoftKeyboardOpen(view: View): Boolean {
        val insets = ViewCompat.getRootWindowInsets(view)
        return insets?.isVisible(WindowInsetsCompat.Type.ime()) ?: false
    }
    
    fun getKeyboardHeight(view: View): Int? {
        val insets = ViewCompat.getRootWindowInsets(view)
        return insets?.getInsets(WindowInsetsCompat.Type.ime())?.bottom
    }
    
    fun getMediaLength(context: Context?, fPath: String): Int {
        val fileUri: Uri = Uri.fromFile(File(fPath))
        val retriever = MediaMetadataRetriever()
        
        return try {
            retriever.setDataSource(context, fileUri)
            val time = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION)
            Integer.parseInt(time)
        } catch (ex: Exception) {
            Log.e("AndroidUtil", "getMediaLength: $fileUri -- ${ex.localizedMessage}")
            0
        } finally {
            retriever.release()
        }
    }
}