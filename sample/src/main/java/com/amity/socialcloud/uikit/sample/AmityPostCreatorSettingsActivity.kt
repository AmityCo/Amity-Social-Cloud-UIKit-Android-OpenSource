package com.amity.socialcloud.uikit.sample

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.amity.socialcloud.uikit.community.newsfeed.model.AmityPostAttachmentOptionItem
import com.amity.socialcloud.uikit.sample.databinding.AmityActivityPostCreationWithOptionsBinding

class AmityPostCreatorSettingsActivity : AppCompatActivity() {

    lateinit var binding: AmityActivityPostCreationWithOptionsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = AmityActivityPostCreationWithOptionsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.btnSubmit.setOnClickListener {
            AmityPostCreatorWithOptionsActivity.navigate(
                context = this,
                communityId = getSelectedTarget(),
                attachmentOptions = getSelectedAttachments()
            )
        }
    }

    private fun getSelectedAttachments(): ArrayList<AmityPostAttachmentOptionItem> {
        val selectedAttachments = mutableListOf<AmityPostAttachmentOptionItem>()
        if (binding.checkboxFile.isChecked) {
            selectedAttachments.add(AmityPostAttachmentOptionItem.FILE)
        }
        if (binding.checkboxPhoto.isChecked) {
            selectedAttachments.add(AmityPostAttachmentOptionItem.PHOTO)
        }
        if (binding.checkboxVideo.isChecked) {
            selectedAttachments.add(AmityPostAttachmentOptionItem.VIDEO)
        }
        return ArrayList(selectedAttachments)
    }


    private fun getSelectedTarget(): String? {
        if (binding.radioCommunityFeed.isChecked) {
            return binding.etTargetId.text.toString()
        }
        return null
    }

}