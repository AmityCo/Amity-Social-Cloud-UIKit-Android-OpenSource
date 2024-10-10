package com.amity.socialcloud.uikit.common.base

import android.Manifest
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import com.amity.socialcloud.uikit.common.common.showSnackBar
import com.amity.socialcloud.uikit.common.contract.AmityPickFileContract
import com.amity.socialcloud.uikit.common.utils.AmityCameraUtil
import com.google.android.material.snackbar.Snackbar
import java.io.File

abstract class AmityPickerFragment : AmityBaseFragment() {

    private var photoFile: File? = null

    private lateinit var imagePickerLauncher: ActivityResultLauncher<PickVisualMediaRequest>

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
        imagePickerLauncher.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        imagePickerLauncher =
            registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
                onImagePicked(uri)
            }
    }

}