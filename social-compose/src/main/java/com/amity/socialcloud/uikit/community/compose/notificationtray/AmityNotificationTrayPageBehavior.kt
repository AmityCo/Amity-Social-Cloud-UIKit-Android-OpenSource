package com.amity.socialcloud.uikit.community.compose.notificationtray

import android.content.Context
import com.amity.socialcloud.sdk.api.video.AmityVideoClient
import com.amity.socialcloud.uikit.community.compose.community.profile.AmityCommunityProfilePageActivity
import com.amity.socialcloud.uikit.community.compose.event.detail.AmityEventDetailPageActivity
import com.amity.socialcloud.uikit.community.compose.livestream.room.view.AmityRoomPlayerPageActivity
import com.amity.socialcloud.uikit.community.compose.post.detail.AmityPostCategory
import com.amity.socialcloud.uikit.community.compose.post.detail.AmityPostDetailPageActivity
import com.amity.socialcloud.uikit.community.compose.user.profile.AmityUserProfilePageActivity
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers

open class AmityNotificationTrayPageBehavior {

    open fun goToCommunityProfilePage(
        context: Context,
        communityId: String,
    ) {
        val intent = AmityCommunityProfilePageActivity.newIntent(
            context = context,
            communityId = communityId
        )
        context.startActivity(intent)
    }

    open fun goToUserProfilePage(context: Context, userId: String) {
        val intent = AmityUserProfilePageActivity.newIntent(
            context = context,
            userId = userId,
        )
        context.startActivity(intent)
    }

    open fun goToPostDetailPage(
        context: Context,
        postId: String,
        category: AmityPostCategory = AmityPostCategory.GENERAL,
        commentId: String? = null,
        parentId: String? = null,
        replyTo: String? = null,
        eventHostId: String? = null,
    ) {
        val intent = AmityPostDetailPageActivity.newIntent(
            context = context,
            id = postId,
            category = category,
            commentId = commentId,
            parentId = parentId,
            replyTo = replyTo,
            eventHostId = eventHostId,
        )
        context.startActivity(intent)
    }

    open fun goToEventDetailPage(
        context: Context,
        eventId: String,
    ) {
        val intent = AmityEventDetailPageActivity.newIntent(
            context = context,
            eventId = eventId
        )
        context.startActivity(intent)
    }

    open fun goToLiveRoomDetailPage(
        context: Context,
        roomId: String,
    ) {
        AmityVideoClient.newRoomRepository()
            .getRoom(roomId = roomId)
            .distinctUntilChanged { old, new -> old.getRoomId() == new.getRoomId() }
            .firstOrError()
            .doOnSuccess { room ->
                room.getPost()
                    ?.let {
                        AmityRoomPlayerPageActivity.newIntent(
                            context = context,
                            post = it,
                            fromInvitation = true,
                        )
                    }
                    ?.let { intent ->
                        context.startActivity(intent)
                    }
            }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe()
    }
}