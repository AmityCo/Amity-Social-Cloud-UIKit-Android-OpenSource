package com.amity.socialcloud.uikit.community.newsfeed.viewmodel

import com.amity.socialcloud.sdk.api.core.AmityCoreClient
import com.amity.socialcloud.sdk.api.social.AmitySocialClient
import com.amity.socialcloud.sdk.helper.core.mention.AmityMentionMetadata
import com.amity.socialcloud.sdk.helper.core.mention.AmityMentionMetadataCreator
import com.amity.socialcloud.sdk.model.core.permission.AmityPermission
import com.amity.socialcloud.sdk.model.core.reaction.AmityReactionReferenceType
import com.amity.socialcloud.sdk.model.social.comment.AmityComment
import com.amity.socialcloud.sdk.model.social.post.AmityPost
import com.amity.socialcloud.uikit.common.common.views.dialog.bottomsheet.BottomSheetMenuItem
import com.amity.socialcloud.uikit.common.utils.AmityConstants
import com.amity.socialcloud.uikit.community.R
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.schedulers.Schedulers

interface CommentViewModel {

    fun addComment(
        parentId: String?,
        postId: String,
        message: String,
        userMentions: List<AmityMentionMetadata.USER>,
        onSuccess: (AmityComment) -> Unit,
        onError: (Throwable) -> Unit,
    ): Completable {
        val commentCreator = AmitySocialClient.newCommentRepository().createComment()
            .post(postId)
            .parentId(parentId)
            .with()
            .text(message)
        if (userMentions.isNotEmpty()) {
            commentCreator
                .metadata(AmityMentionMetadataCreator(userMentions).create())
                .mentionUsers(userMentions.map { it.getUserId() })
        }

        return commentCreator
            .build()
            .send()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSuccess(onSuccess)
            .doOnError(onError)
            .ignoreElement()
    }

    fun deleteComment(
        commentId: String,
        onSuccess: () -> Unit,
        onError: () -> Unit,
    ): Completable {
        return AmitySocialClient.newCommentRepository().softDeleteComment(commentId)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnComplete {
                onSuccess.invoke()
            }
            .doOnError {
                onError.invoke()
            }
    }

    fun addCommentReaction(comment: AmityComment): Completable {
        return AmityCoreClient.newReactionRepository()
                .addReaction(
                    referenceType = AmityReactionReferenceType.COMMENT,
                    referenceId = comment.getCommentId(),
                    reactionName = AmityConstants.POST_REACTION
                )
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }

    fun removeCommentReaction(comment: AmityComment): Completable {
        return AmityCoreClient.newReactionRepository()
            .removeReaction(
                referenceType = AmityReactionReferenceType.COMMENT,
                referenceId = comment.getCommentId(),
                reactionName = AmityConstants.POST_REACTION
            )
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }

    fun reportComment(
        comment: AmityComment,
        onSuccess: () -> Unit,
        onError: () -> Unit
    ): Completable {
        return AmitySocialClient.newCommentRepository()
            .flagComment(
               comment.getCommentId()
            )
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnComplete {
                onSuccess.invoke()
            }
            .doOnError {
                onError.invoke()
            }
    }

    fun unReportComment(
        comment: AmityComment,
        onSuccess: () -> Unit,
        onError: () -> Unit
    ): Completable {
        return AmitySocialClient.newCommentRepository()
            .unflagComment(
                comment.getCommentId()
            )
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnComplete {
                onSuccess.invoke()
            }
            .doOnError {
                onError.invoke()
            }
    }

    fun getCommentOptionMenuItems(
        comment: AmityComment,
        editComment: () -> Unit,
        deleteComment: () -> Unit,
        reportComment: () -> Unit,
        unReportComment: () -> Unit,
        editReply: () -> Unit,
        deleteReply: () -> Unit,
    ): List<BottomSheetMenuItem> {
        val items = arrayListOf<BottomSheetMenuItem>()
        val editCommentMenuItem =
            BottomSheetMenuItem(null, null, R.string.amity_edit_comment, editComment)
        val deleteCommentMenuItem =
            BottomSheetMenuItem(null, null, R.string.amity_delete_comment, deleteComment)
        val reportCommentMenuItem =
            BottomSheetMenuItem(null, null, R.string.amity_report, reportComment)
        val unReportCommentMenuItem =
            BottomSheetMenuItem(null, null, R.string.amity_undo_report, unReportComment)
        val editReplyMenuItem =
            BottomSheetMenuItem(null, null, R.string.amity_edit_reply, editReply)
        val deleteReplyMenuItem =
            BottomSheetMenuItem(null, null, R.string.amity_delete_reply, deleteReply)

        if (comment.getCreatorId() == AmityCoreClient.getUserId()) {
            if (comment.getParentId() == null) {
                if (comment.getState() != AmityComment.State.FAILED) {
                    items.add(editCommentMenuItem)
                }
                items.add(deleteCommentMenuItem)
            } else {
                if (comment.getState() != AmityComment.State.FAILED) {
                    items.add(editReplyMenuItem)
                }
                items.add(deleteReplyMenuItem)
            }
        } else {
            when (val reference: AmityComment.Reference = comment.getReference()) {
                is AmityComment.Reference.POST -> {
                    val post = AmitySocialClient.newPostRepository().getPost(reference.getPostId())
                        .blockingFirst()
                    when (val target = post.getTarget()) {
                        is AmityPost.Target.COMMUNITY -> {
                            val hasDeletePermission =
                                AmityCoreClient.hasPermission(AmityPermission.DELETE_COMMUNITY_COMMENT)
                                    .atCommunity(target.getCommunity()?.getCommunityId() ?: "")
                                    .check()
                                    .blockingFirst(false)
                            if (hasDeletePermission) {
                                items.add(deleteCommentMenuItem)
                            }
                        }
                        else -> {
                            val hasDeletePermission =
                                AmityCoreClient.hasPermission(AmityPermission.DELETE_USER_FEED_COMMENT)
                                    .atGlobal()
                                    .check()
                                    .blockingFirst(false)
                            if (hasDeletePermission) {
                                items.add(deleteCommentMenuItem)
                            }
                        }
                    }
                }
                else -> {}
            }
            if (comment.isFlaggedByMe()) {
                items.add(unReportCommentMenuItem)
            } else {
                items.add(reportCommentMenuItem)
            }

        }
        return items
    }

}
