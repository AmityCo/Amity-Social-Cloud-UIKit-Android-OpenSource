package com.amity.socialcloud.uikit.community.newsfeed.viewmodel

import com.amity.socialcloud.sdk.AmityCoreClient
import com.amity.socialcloud.sdk.core.error.AmityError
import com.amity.socialcloud.sdk.core.mention.AmityMentionMetadata
import com.amity.socialcloud.sdk.core.mention.AmityMentionMetadataCreator
import com.amity.socialcloud.sdk.core.permission.AmityPermission
import com.amity.socialcloud.sdk.social.AmitySocialClient
import com.amity.socialcloud.sdk.social.comment.AmityComment
import com.amity.socialcloud.sdk.social.feed.AmityPost
import com.amity.socialcloud.uikit.common.common.views.dialog.bottomsheet.BottomSheetMenuItem
import com.amity.socialcloud.uikit.common.utils.AmityConstants
import com.amity.socialcloud.uikit.community.R
import io.reactivex.Completable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

interface CommentViewModel {

    fun addComment(
        parentId: String?,
        commentId: String,
        postId: String,
        message: String,
        userMentions: List<AmityMentionMetadata.USER>,
        onSuccess: () -> Unit,
        onError: () -> Unit,
        onBanned: () -> Unit
    ): Completable {
        val commentCreator = AmitySocialClient.newCommentRepository().createComment(commentId)
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
            .doOnSuccess {
                onSuccess.invoke()
            }
            .doOnError {
                if (AmityError.from(it) == AmityError.BAN_WORD_FOUND) {
                    onBanned.invoke()
                } else {
                    onError.invoke()
                }
            }.ignoreElement()
    }

    fun deleteComment(
        commentId: String,
        onSuccess: () -> Unit,
        onError: () -> Unit,
    ): Completable {
        return AmitySocialClient.newCommentRepository().deleteComment(commentId)
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
        return comment.react().addReaction(AmityConstants.POST_REACTION)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }

    fun removeCommentReaction(comment: AmityComment): Completable {
        return comment.react().removeReaction(AmityConstants.POST_REACTION)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }

    fun reportComment(
        comment: AmityComment,
        onSuccess: () -> Unit,
        onError: () -> Unit
    ): Completable {
        return comment.report()
            .flag()
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
        return comment.report()
            .unflag()
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
        val editCommentMenuItem = BottomSheetMenuItem(null, null, R.string.amity_edit_comment, editComment)
        val deleteCommentMenuItem = BottomSheetMenuItem(null, null, R.string.amity_delete_comment, deleteComment)
        val reportCommentMenuItem = BottomSheetMenuItem(null, null, R.string.amity_report, reportComment)
        val unReportCommentMenuItem = BottomSheetMenuItem(null, null, R.string.amity_undo_report, unReportComment)
        val editReplyMenuItem = BottomSheetMenuItem(null, null, R.string.amity_edit_reply, editReply)
        val deleteReplyMenuItem = BottomSheetMenuItem(null, null, R.string.amity_delete_reply, deleteReply)

        if (comment.getUserId() == AmityCoreClient.getUserId()) {
            if(comment.getParentId() == null) {
                items.add(editCommentMenuItem)
                items.add(deleteCommentMenuItem)
            } else {
                items.add(editReplyMenuItem)
                items.add(deleteReplyMenuItem)
            }
        } else {
            val reference: AmityComment.Reference = comment.getReference()
            when (reference) {
                is AmityComment.Reference.POST -> {
                    val post = AmitySocialClient.newFeedRepository().getPost(reference.getPostId())
                        .blockingFirst(null)
                    post?.let {
                        val target = post.getTarget()
                        when (target) {
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
                }
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
