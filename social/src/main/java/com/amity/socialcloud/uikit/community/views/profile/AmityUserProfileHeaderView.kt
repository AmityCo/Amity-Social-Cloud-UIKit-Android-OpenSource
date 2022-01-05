package com.amity.socialcloud.uikit.community.views.profile

import android.content.Context
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.core.text.bold
import androidx.databinding.DataBindingUtil
import com.amity.socialcloud.sdk.core.user.AmityFollowStatus
import com.amity.socialcloud.sdk.core.user.AmityMyFollowInfo
import com.amity.socialcloud.sdk.core.user.AmityUser
import com.amity.socialcloud.sdk.core.user.AmityUserFollowInfo
import com.amity.socialcloud.uikit.common.common.views.AmityColorPaletteUtil
import com.amity.socialcloud.uikit.common.common.views.AmityColorShade
import com.amity.socialcloud.uikit.community.R
import com.amity.socialcloud.uikit.community.databinding.AmityViewUserProfileHeaderBinding

class AmityUserProfileHeaderView : ConstraintLayout {
    private lateinit var headerBinding: AmityViewUserProfileHeaderBinding

    constructor(context: Context) : super(context) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        init()
    }

    fun setUserData(user: AmityUser) {
        headerBinding.amityUser = user
        headerBinding.postCount = "10"
        headerBinding.tvPostCount.setText(
            getStylisedText("10", context.getString(R.string.amity_posts)),
            TextView.BufferType.SPANNABLE
        )

        val banIcon = if (user.isGlobalBan()) {
            ContextCompat.getDrawable(context, R.drawable.amity_ic_ban)
        } else {
            null
        }
        headerBinding.tvName.setCompoundDrawablesRelativeWithIntrinsicBounds(null, null, banIcon, null)
    }

    fun setMyFollowInfo(followInfo: AmityMyFollowInfo) {
        headerBinding.tvFollowersCount.setText(
            getStylisedText(
                followInfo.getFollowerCount().toString(),
                context.getString(R.string.amity_followers)
            ),
            TextView.BufferType.SPANNABLE
        )
        headerBinding.tvFollowingCount.setText(
            getStylisedText(
                followInfo.getFollowingCount().toString(),
                context.getString(R.string.amity_following_count)
            ),
            TextView.BufferType.SPANNABLE
        )
        headerBinding.isSelf = true
        headerBinding.connectionState = AmityFollowStatus.ACCEPTED

        if (followInfo.getPendingRequestCount() > 0) {
            headerBinding.layoutPendingRequests.visibility = View.VISIBLE
        } else {
            headerBinding.layoutPendingRequests.visibility = View.GONE
        }
    }

    fun setUserFollowInfo(userFollowInfo: AmityUserFollowInfo) {
        headerBinding.tvFollowersCount.setText(
            getStylisedText(
                userFollowInfo.getFollowerCount().toString(),
                context.getString(R.string.amity_followers)
            ),
            TextView.BufferType.SPANNABLE
        )
        headerBinding.tvFollowingCount.setText(
            getStylisedText(
                userFollowInfo.getFollowingCount().toString(),
                context.getString(R.string.amity_following_count)
            ),
            TextView.BufferType.SPANNABLE
        )
        updateState(userFollowInfo.getStatus())

    }

    private fun setTextColor(followStatus: AmityFollowStatus) {
        val textColor = when (followStatus) {
            AmityFollowStatus.NONE, AmityFollowStatus.PENDING -> {
                AmityColorPaletteUtil.getColor(
                    ContextCompat.getColor(context, R.color.amityColorBase), AmityColorShade.SHADE2
                )
            }
            else -> {
                ContextCompat.getColor(context, R.color.amityColorBase)
            }
        }
        headerBinding.tvPostCount.setTextColor(textColor)
        headerBinding.tvFollowingCount.setTextColor(textColor)
        headerBinding.tvFollowersCount.setTextColor(textColor)
    }

    fun getHeaderBinding(): AmityViewUserProfileHeaderBinding = headerBinding

    fun updateState(newState: AmityFollowStatus) {
        headerBinding.connectionState = newState
        setTextColor(newState)
    }

    private fun init() {
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        headerBinding =
            DataBindingUtil.inflate(inflater, R.layout.amity_view_user_profile_header, this, true)
    }

    private fun getStylisedText(s1: String, s2: String): Spannable {
        return SpannableStringBuilder()
            .bold { append(s1) }
            .append(' ')
            .append(s2)
    }
}