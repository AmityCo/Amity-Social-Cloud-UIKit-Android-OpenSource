package com.amity.socialcloud.uikit.community.compose.comment

import androidx.paging.PagingData
import com.amity.socialcloud.sdk.api.core.AmityCoreClient
import com.amity.socialcloud.sdk.api.core.reaction.reference.AmityReactionReference
import com.amity.socialcloud.sdk.api.social.AmitySocialClient
import com.amity.socialcloud.sdk.api.social.comment.query.AmityCommentQuery
import com.amity.socialcloud.sdk.helper.core.coroutines.asFlow
import com.amity.socialcloud.sdk.helper.core.mention.AmityMentionMetadata
import com.amity.socialcloud.sdk.helper.core.mention.AmityMentionMetadataCreator
import com.amity.socialcloud.sdk.model.core.user.AmityUser
import com.amity.socialcloud.sdk.model.social.comment.AmityComment
import com.amity.socialcloud.sdk.model.social.community.AmityCommunity
import com.amity.socialcloud.uikit.common.base.AmityBaseViewModel
import com.amity.socialcloud.uikit.common.utils.AmityConstants
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.schedulers.Schedulers
import kotlinx.coroutines.flow.Flow

class AmityCommentTrayComponentViewModel : AmityBaseViewModel() {

    var community: AmityCommunity? = null
        private set

    fun setCommunity(community: AmityCommunity?) {
        this.community = community
    }

    fun getCurrentUser(): Flowable<AmityUser> {
        return AmityCoreClient.getCurrentUser()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }

    fun getComments(reference: AmityComment.Reference): Flow<PagingData<AmityComment>> {
        return getCommentQuery(reference = reference)
            .build()
            .query()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .asFlow()
    }

    fun addComment(
        reference: AmityComment.Reference,
        replyCommentId: String?,
        commentText: String,
        mentionedUsers: List<AmityMentionMetadata.USER> = emptyList(),
        onSuccess: (AmityComment) -> Unit,
        onError: (Throwable) -> Unit
    ) {
        val commentCreator = AmitySocialClient.newCommentRepository()
            .createComment()
            .run {
                when (reference) {
                    is AmityComment.Reference.STORY -> story(reference.getStoryId())
                    is AmityComment.Reference.POST -> post(reference.getPostId())
                    is AmityComment.Reference.CONTENT -> content(reference.getContentId())
                    else -> content("")
                }
            }

        if (!replyCommentId.isNullOrEmpty()) {
            commentCreator.parentId(replyCommentId)
        }

        val textCommentCreator = commentCreator.with().text(commentText)
        if (mentionedUsers.isNotEmpty()) {
            textCommentCreator
                .metadata(AmityMentionMetadataCreator(mentionedUsers).create())
                .mentionUsers(mentionedUsers.map { it.getUserId() })
        }

        textCommentCreator.build().send()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSuccess(onSuccess)
            .doOnError(onError)
            .ignoreElement()
            .subscribe()
    }

    fun addReaction(
        commentId: String
    ) {
        AmityCoreClient.newReactionRepository()
            .addReaction(
                reactionReference = AmityReactionReference.COMMENT(commentId),
                reactionName = AmityConstants.POST_REACTION
            )
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe()
    }

    fun removeReaction(
        commentId: String
    ) {
        AmityCoreClient.newReactionRepository()
            .removeReaction(
                reactionReference = AmityReactionReference.COMMENT(commentId),
                reactionName = AmityConstants.POST_REACTION
            )
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe()
    }

    fun editComment(
        commentId: String,
        commentText: String,
        userMentions: List<AmityMentionMetadata.USER> = emptyList(),
        onSuccess: (AmityComment) -> Unit,
        onError: (Throwable) -> Unit
    ) {
        val textCommentEditor = AmitySocialClient.newCommentRepository()
            .editComment(commentId)
            .text(commentText)

        if (userMentions.isNotEmpty()) {
            textCommentEditor
                .metadata(AmityMentionMetadataCreator(userMentions).create())
                .mentionUsers(userMentions.map { it.getUserId() })
        }

        textCommentEditor
            .build()
            .apply()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSuccess(onSuccess)
            .doOnError(onError)
            .ignoreElement()
            .subscribe()
    }

    fun deleteComment(
        commentId: String,
        onSuccess: () -> Unit,
        onError: (Throwable) -> Unit,
    ) {
        AmitySocialClient.newCommentRepository()
            .softDeleteComment(commentId)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnComplete(onSuccess)
            .doOnError(onError)
            .subscribe()
    }

    fun flagComment(
        commentId: String,
        onSuccess: () -> Unit,
        onError: (Throwable) -> Unit,
    ) {
        AmitySocialClient.newCommentRepository()
            .flagComment(commentId)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnComplete(onSuccess)
            .doOnError(onError)
            .subscribe()
    }

    fun unflagComment(
        commentId: String,
        onSuccess: () -> Unit,
        onError: (Throwable) -> Unit,
    ) {
        AmitySocialClient.newCommentRepository()
            .unflagComment(commentId)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnComplete(onSuccess)
            .doOnError(onError)
            .subscribe()
    }

    private fun getCommentQuery(
        reference: AmityComment.Reference,
    ): AmityCommentQuery.Builder {
        return AmitySocialClient.newCommentRepository()
            .getComments()
            .run {
                when (reference) {
                    is AmityComment.Reference.STORY -> story(reference.getStoryId())
                    is AmityComment.Reference.POST -> post(reference.getPostId())
                    is AmityComment.Reference.CONTENT -> content(reference.getContentId())
                    else -> content("")
                }
            }
    }
}