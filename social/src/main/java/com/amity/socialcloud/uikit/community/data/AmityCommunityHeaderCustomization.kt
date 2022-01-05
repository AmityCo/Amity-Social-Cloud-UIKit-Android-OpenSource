package com.amity.socialcloud.uikit.community.data

import android.graphics.Color
import android.graphics.drawable.Drawable
import androidx.databinding.ObservableBoolean
import com.amity.socialcloud.uikit.common.utils.AmityConstants

data class AmityCommunityHeaderCustomization(
    var avatarUrl: String = AmityConstants.EMPTY_STRING,
    var avatarPlaceHolder: Drawable? = null,
    var avatarIsCircular: Boolean = true,
    var avatarSignature: String = AmityConstants.EMPTY_STRING,
    var chTitle: String = AmityConstants.EMPTY_STRING,
    var post: String = AmityConstants.EMPTY_STRING,
    var postBold: List<String> = arrayListOf(""),
    var postBoldRange: List<Pair<Int, Int>> = arrayListOf(),
    var followers: String = AmityConstants.EMPTY_STRING,
    var followersBold: List<String> = arrayListOf(),
    var followersBoldRange: List<Pair<Int, Int>> = arrayListOf(),
    var mutualFriends: String = AmityConstants.EMPTY_STRING,
    var mutualFriendsBold: List<String> = arrayListOf(),
    var mutualFriendsBoldRange: List<Pair<Int, Int>> = arrayListOf(),
    var boldTextColor: Int = Color.BLACK,
    var description: String = AmityConstants.EMPTY_STRING,
    var buttonFollowText: String = AmityConstants.EMPTY_STRING,
    var buttonFollowingText: String = AmityConstants.EMPTY_STRING,
    var buttonDrawable: Drawable? = null,
    var followingStatus: ObservableBoolean = ObservableBoolean(false)
)