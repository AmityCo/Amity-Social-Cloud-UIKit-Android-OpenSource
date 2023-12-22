package com.amity.socialcloud.uikit.community.compose.story.create

import android.content.Context
import android.content.Intent
import androidx.activity.result.ActivityResultLauncher
import androidx.media3.common.util.UnstableApi
import com.amity.socialcloud.sdk.model.social.community.AmityCommunity
import com.amity.socialcloud.uikit.community.compose.story.draft.AmityDraftStoryPageActivity

@UnstableApi
class AmityCreateStoryPageBehavior(
    private val context: Context
) {
    fun goToDraftStoryPage(
        launcher: ActivityResultLauncher<Intent>,
        community: AmityCommunity,
        isImage: Boolean,
        fileUri: String,
    ) {
        val intent = AmityDraftStoryPageActivity.newIntent(
            context = context,
            community = community,
            isImage = isImage,
            fileUri = fileUri
        )
        launcher.launch(intent)
    }
}