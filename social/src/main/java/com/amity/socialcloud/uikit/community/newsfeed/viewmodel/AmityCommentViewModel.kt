package com.amity.socialcloud.uikit.community.newsfeed.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.paging.ExperimentalPagingApi
import androidx.paging.PagedList
import androidx.paging.PagingData
import com.amity.socialcloud.sdk.AmityCoreClient
import com.amity.socialcloud.sdk.core.mention.AmityMentionMetadata
import com.amity.socialcloud.sdk.core.mention.AmityMentionMetadataCreator
import com.amity.socialcloud.sdk.core.user.AmityUser
import com.amity.socialcloud.sdk.core.user.AmityUserRepository
import com.amity.socialcloud.sdk.core.user.AmityUserSortOption
import com.amity.socialcloud.sdk.social.AmitySocialClient
import com.amity.socialcloud.sdk.social.comment.AmityComment
import com.amity.socialcloud.sdk.social.comment.AmityCommentRepository
import com.amity.socialcloud.sdk.social.community.AmityCommunityMember
import com.amity.socialcloud.sdk.social.feed.AmityPost
import com.amity.socialcloud.sdk.social.post.AmityPostRepository
import com.amity.socialcloud.uikit.common.base.AmityBaseViewModel
import com.ekoapp.ekosdk.community.membership.query.AmityCommunityMembership
import io.reactivex.Completable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.TimeUnit

class AmityCommentViewModel : AmityBaseViewModel() {
    private var comment: AmityComment? = null
    private var reply: AmityComment? = null
    private var postId: String = ""

    var post: AmityPost? = null

    val commentText = MutableLiveData<String>().apply { value = "" }
    val hasCommentUpdate = MutableLiveData<Boolean>(false)

    private val commentRepository: AmityCommentRepository = AmitySocialClient.newCommentRepository()
    private val userRepository: AmityUserRepository = AmityCoreClient.newUserRepository()
    private val postRepository: AmityPostRepository = AmitySocialClient.newPostRepository()

    fun updateComment(
        commentText: String,
        userMentions: List<AmityMentionMetadata.USER>,
        onSuccess: (AmityComment) -> Unit,
        onError: (Throwable) -> Unit
    ): Completable {
        return (comment?.getData() as? AmityComment.Data.TEXT)?.let { textComment ->
            val textCommentEditor = textComment.edit().text(commentText)
            textCommentEditor
                .metadata(AmityMentionMetadataCreator(userMentions).create())
                .mentionUsers(userMentions.map { it.getUserId() })
            textCommentEditor.build().apply()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSuccess(onSuccess)
                .doOnError(onError)
                .ignoreElement()
        } ?: kotlin.run {
            Completable.complete()
        }
    }

    fun addComment(
        commentId: String,
        commentText: String,
        userMentions: List<AmityMentionMetadata.USER>,
        onSuccess: (AmityComment) -> Unit,
        onError: (Throwable) -> Unit
    ): Completable {
        val commentCreator = AmitySocialClient.newCommentRepository().createComment(commentId)
            .post(postId)
        if (reply != null) {
            commentCreator.parentId(reply?.getCommentId())
        }
        val textCommentCreator = commentCreator.with().text(commentText)
        if (userMentions.isNotEmpty()) {
            textCommentCreator
                .metadata(AmityMentionMetadataCreator(userMentions).create())
                .mentionUsers(userMentions.map { it.getUserId() })
        }
        return textCommentCreator.build().send()
            .map {
                AmitySocialClient.newFeedRepository().getPost(postId).ignoreElements()
                    .onErrorComplete()
                    .subscribe()
                it
            }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSuccess(onSuccess)
            .doOnError(onError)
            .ignoreElement()
    }

    fun deleteComment(commentId: String): Completable {
        return commentRepository.deleteComment(commentId)
    }

    fun checkForCommentUpdate() {
        val commentData = (comment?.getData() as? AmityComment.Data.TEXT)?.getText()
        val updateAvailable = !commentText.value.isNullOrBlank() && commentData != commentText.value
        hasCommentUpdate.value = updateAvailable
    }

    fun setPost(postId: String) {
        this.postId = postId
    }

    fun setComment(comment: AmityComment?) {
        this.comment = comment
        if (editMode()) {
            val commentData = (comment?.getData() as? AmityComment.Data.TEXT)?.getText()
            if (commentData != null)
                commentText.value = commentData
        }
        val commentPostId = (comment?.getReference() as? AmityComment.Reference.POST)?.getPostId()
        if (commentPostId?.isNotEmpty() == true) {
            setPost(commentPostId)
        }
    }

    fun setReplyTo(reply: AmityComment?) {
        this.reply = reply
        val replyPostId = (reply?.getReference() as? AmityComment.Reference.POST)?.getPostId()
        if (replyPostId?.isNotEmpty() == true) {
            setPost(replyPostId)
        }
    }

    fun searchUsersMention(
        keyword: String,
        onResult: (users: PagedList<AmityUser>) -> Unit
    ): Completable {
        return userRepository.searchUserByDisplayName(keyword)
            .sortBy(AmityUserSortOption.DISPLAYNAME)
            .build()
            .query()
            .throttleLatest(1, TimeUnit.SECONDS, true)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnNext {
                onResult.invoke(it)
            }
            .ignoreElements()
    }

    @ExperimentalPagingApi
    fun searchCommunityUsersMention(
        communityId: String,
        keyword: String,
        onResult: (users: PagingData<AmityCommunityMember>) -> Unit
    ): Completable {
        return AmitySocialClient.newCommunityRepository()
            .membership(communityId)
            .searchMembers(keyword)
            .membershipFilter(listOf(AmityCommunityMembership.MEMBER))
            .build()
            .getPagingData()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnNext {
                onResult.invoke(it)
            }
            .ignoreElements()
    }

    private fun editMode(): Boolean {
        return comment != null
    }

    fun getPost(onResult: (post: AmityPost) -> Unit): Completable {
        return postRepository
            .getPost(postId)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnNext {
                onResult.invoke(it)
            }
            .ignoreElements()
    }

    fun observeComment(onResult: (comment: AmityComment) -> Unit): Completable? {
        return comment?.getCommentId()?.let { commentId ->
            commentRepository
                .observeComment(commentId)
                .firstOrError()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSuccess {
                    onResult.invoke(it)
                }
                .ignoreElement()
        }
    }

    fun getComment(): AmityComment? {
        return comment
    }

    fun getReply(): AmityComment? {
        return reply
    }

    fun setCommentData(commentText: String) {
        this.commentText.value = commentText
    }

}
