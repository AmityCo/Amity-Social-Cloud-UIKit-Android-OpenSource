package com.amity.socialcloud.uikit.common.utils

import android.Manifest
import android.content.pm.ActivityInfo
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.amity.socialcloud.uikit.common.R
import com.amity.socialcloud.uikit.common.common.showSnackBar
import com.google.android.material.snackbar.Snackbar
import com.zhihu.matisse.Matisse
import com.zhihu.matisse.MimeType
import com.zhihu.matisse.engine.impl.GlideEngine

object AmityAlbumUtil {

    private const val MAX_SELECTION_COUNT = 20

    fun pickMultipleImage(activity: AppCompatActivity, currentCount: Int, resultCode: Int) {
        val pickImagePermission =
            activity.registerForActivityResult(ActivityResultContracts.RequestPermission()) {
                if (it) {
                    if (currentCount == MAX_SELECTION_COUNT) {
                        activity.findViewById<View>(android.R.id.content).showSnackBar(activity.getString(R.string.amity_max_image_selected))
                    } else {
                        Matisse.from(activity)
                            .choose(MimeType.of(MimeType.JPEG, MimeType.PNG, MimeType.GIF))
                            .countable(true)
                            .maxSelectable(MAX_SELECTION_COUNT - currentCount)
                            .restrictOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED)
                            .imageEngine(GlideEngine())
                            .theme(R.style.AmityImagePickerTheme)
                            .forResult(resultCode)
                    }
                } else {
                    activity.findViewById<View>(android.R.id.content).showSnackBar("Permission denied", Snackbar.LENGTH_SHORT)
                }
            }
        pickImagePermission.launch(Manifest.permission.WRITE_EXTERNAL_STORAGE)
    }

    fun pickMultipleImage(fragment: Fragment, currentCount: Int, resultCode: Int) {
        fragment.view?.showSnackBar("Hello Test")
        val pickImagePermission = fragment.registerForActivityResult(ActivityResultContracts.RequestPermission()) {
                if (it) {
                    if (currentCount == MAX_SELECTION_COUNT) {
                        fragment.view?.showSnackBar(fragment.getString(R.string.amity_max_image_selected))
                    } else {
                        Matisse.from(fragment)
                            .choose(MimeType.of(MimeType.JPEG, MimeType.PNG, MimeType.GIF))
                            .countable(true)
                            .maxSelectable(MAX_SELECTION_COUNT - currentCount)
                            .restrictOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED)
                            .imageEngine(GlideEngine())
                            .theme(R.style.AmityImagePickerTheme)
                            .forResult(resultCode)
                    }
                } else {
                    fragment.view?.showSnackBar("Permission denied", Snackbar.LENGTH_SHORT)
                }
            }
        pickImagePermission.launch(Manifest.permission.WRITE_EXTERNAL_STORAGE)
    }
}