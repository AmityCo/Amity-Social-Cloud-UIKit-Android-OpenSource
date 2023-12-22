package com.amity.socialcloud.uikit.common.base

import android.Manifest
import android.net.Uri
import android.os.Build
import androidx.activity.result.contract.ActivityResultContracts
import com.amity.socialcloud.uikit.common.R
import com.amity.socialcloud.uikit.common.common.showSnackBar
import com.amity.socialcloud.uikit.common.contract.AmityPickFileContract
import com.amity.socialcloud.uikit.common.contract.AmityPickImageContract
import com.amity.socialcloud.uikit.common.utils.AmityCameraUtil
import com.google.android.material.snackbar.Snackbar
import java.io.File

abstract class AmityPickerFragment : AmityBaseFragment() {

    private var photoFile: File? = null

    private val pickImage = registerForActivityResult(AmityPickImageContract()) { data ->
        onImagePicked(data)
    }

    private val pickImagePermission =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) {
            if (it) {
                pickImage.launch(getString(R.string.amity_choose_image))
            } else {
                view?.showSnackBar("Permission denied", Snackbar.LENGTH_SHORT)
            }
        }

    private val pickFile = registerForActivityResult(AmityPickFileContract()) { data ->
        onFilePicked(data)
    }

    private val pickFilePermission =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) {
            if (it) {
                pickFile.launch("")
            } else {
                view?.showSnackBar("Permission denied", Snackbar.LENGTH_SHORT)
            }
        }

    private val cameraPermission =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
            var permissionGranted = true
            permissions.entries.forEach {
                if(it.value == false) {
                    permissionGranted = false
                }
            }
            if (permissionGranted) {
                photoFile = AmityCameraUtil.createImageFile(requireContext())
                photoFile?.let {
                    val uri = AmityCameraUtil.createPhotoUri(requireContext(), it)
                    takePhoto.launch(uri)
                }
            }
        }

    private val takePhoto = registerForActivityResult(ActivityResultContracts.TakePicture()) {
        if(it) {
            onPhotoClicked(photoFile)
        }
    }

    abstract fun onImagePicked(data: Uri?)
    abstract fun onFilePicked(data: Uri?)
    abstract fun onPhotoClicked(file: File?)

    fun pickImage() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU) {
            pickImagePermission.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            pickImagePermission.launch(Manifest.permission.READ_MEDIA_IMAGES)
        }
    }

    fun pickFile() {

        //pickFilePermission.launch(Manifest.permission.WRITE_EXTERNAL_STORAGE)
    }

    fun takePicture() {
        val permissions = if (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU) {
            arrayOf(
                Manifest.permission.CAMERA,
            )
        } else {
            arrayOf(
                Manifest.permission.CAMERA
            )
        }
        cameraPermission.launch(permissions)
    }

}