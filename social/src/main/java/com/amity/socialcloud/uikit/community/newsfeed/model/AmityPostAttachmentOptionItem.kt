package com.amity.socialcloud.uikit.community.newsfeed.model

import android.os.Parcelable
import com.amity.socialcloud.uikit.community.R
import kotlinx.android.parcel.Parcelize

sealed class AmityPostAttachmentOptionItem(
    val activeIcon: Int,
    val inactiveIcon: Int,
    val optionName: Int,
) : Parcelable{

    var isEnable: Boolean = false

    @Parcelize
    internal object CAMERA : AmityPostAttachmentOptionItem(R.drawable.amity_ic_attachment_camera_enable, R.drawable.amity_ic_attachment_camera_disable, R.string.amity_post_attachment_option_camera)
    @Parcelize
    object PHOTO : AmityPostAttachmentOptionItem(R.drawable.amity_ic_attachment_photo_enable, R.drawable.amity_ic_attachment_photo_disable, R.string.amity_post_attachment_option_photo)
    @Parcelize
    object VIDEO : AmityPostAttachmentOptionItem(R.drawable.amity_ic_attachment_video_enable, R.drawable.amity_ic_attachment_video_disable, R.string.amity_post_attachment_option_video)
    @Parcelize
    object FILE : AmityPostAttachmentOptionItem(R.drawable.amity_ic_attachment_file_enable, R.drawable.amity_ic_attachment_file_disable, R.string.amity_post_attachment_option_file)
    @Parcelize
    internal object EXPAND : AmityPostAttachmentOptionItem(R.drawable.amity_ic_content_expand, R.drawable.amity_ic_content_expand, 0)
    @Parcelize
    internal object BLANK : AmityPostAttachmentOptionItem(0, 0, 0)
}