package com.amity.socialcloud.uikit.community.newsfeed.adapter

import android.content.Context
import android.view.View
import com.amity.socialcloud.sdk.model.social.poll.AmityPollAnswer
import com.amity.socialcloud.uikit.common.base.AmityViewHolder
import com.google.android.material.card.MaterialCardView

abstract class AmityPollAnswerViewHolder(
    val context: Context,
    val resource: Int,
    val isEnabled: Boolean,
    val voteCallback: (answerId: String, isSelected: Boolean, holder: MaterialCardView?) -> Unit
) : AmityViewHolder<AmityPollAnswer>(View.inflate(context, resource, null))