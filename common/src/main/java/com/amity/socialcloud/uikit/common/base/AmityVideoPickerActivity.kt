package com.amity.socialcloud.uikit.common.base

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import com.trello.rxlifecycle4.components.support.RxAppCompatActivity

class AmityVideoPickerActivity : RxAppCompatActivity() {
    private var maxItems: Int = 1
    private lateinit var pickMedia: ActivityResultLauncher<PickVisualMediaRequest>
    
    override fun onStart() {
        maxItems = intent.getIntExtra(MAX_SELECTION_COUNT, maxItems)
        pickMedia = if (maxItems > 1) {
            registerForActivityResult(PickMultipleVideos(maxItems = maxItems)) { uris ->
                if (uris != null && uris.isNotEmpty()) {
                    val result = Intent().apply { putExtra("data", uris.toTypedArray()) }
                    setResult(Activity.RESULT_OK, result)
                    finish()
                } else {
                    setResult(Activity.RESULT_CANCELED)
                    finish()
                }
            }
        } else {
            registerForActivityResult(PickVideo()) { uri ->
                if (uri != null) {
                    val result = Intent().apply { putExtra("data", arrayOf(uri)) }
                    setResult(Activity.RESULT_OK, result)
                    finish()
                } else {
                    setResult(Activity.RESULT_CANCELED)
                    finish()
                }
            }
        }
        pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.SingleMimeType("*/*")))
        super.onStart()
    }
    
    class PickVideo() : ActivityResultContracts.PickVisualMedia() {
        override fun createIntent(context: Context, input: PickVisualMediaRequest): Intent {
            val intent = super.createIntent(context, input)
            intent.putExtra(Intent.EXTRA_MIME_TYPES, SUPPORT_MIME_TYPES)
            return intent
        }
    }
    
    class PickMultipleVideos(maxItems: Int) : ActivityResultContracts.PickMultipleVisualMedia(maxItems) {
        override fun createIntent(context: Context, input: PickVisualMediaRequest): Intent {
            val intent = super.createIntent(context, input)
            intent.putExtra(Intent.EXTRA_MIME_TYPES, SUPPORT_MIME_TYPES)
            return intent
        }
    }
    
    companion object {
        private const val MAX_SELECTION_COUNT = "MAX_SELECTION_COUNT"
        val SUPPORT_MIME_TYPES = arrayOf(
            "video/3gp",
            "video/x-msvideo",
            "video/x-f4v",
            "video/x-flv",
            "video/mp4",
            "video/quicktime",
            "video/mp4",
            "video/ogg",
            "video/3gpp2",
            "video/x-ms-wmv",
            "video/mpeg",
            "video/webm",
            "video/x-matroska"
        )
       
        fun newIntent(
            context: Context,
            maxItems: Int = 1,
        ): Intent {
            val intent = Intent(context, AmityVideoPickerActivity::class.java)
            intent.putExtra(MAX_SELECTION_COUNT, maxItems)
            return intent
        }
        
        fun getUris(intent: Intent): Array<Uri>? {
            return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                intent.getParcelableArrayExtra("data", Uri::class.java)
            } else {
                intent.getParcelableArrayExtra("data")?.mapNotNull { it as? Uri }?.toTypedArray()
            }
        }
    }
}