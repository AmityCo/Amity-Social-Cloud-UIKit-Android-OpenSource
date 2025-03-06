package com.amity.socialcloud.uikit.community.newsfeed.viewmodel


import androidx.lifecycle.MutableLiveData
import androidx.paging.ExperimentalPagingApi
import androidx.paging.PagingData
import com.amity.socialcloud.sdk.api.core.AmityCoreClient
import com.amity.socialcloud.sdk.api.core.user.AmityUserRepository
import com.amity.socialcloud.sdk.api.core.user.search.AmityUserSortOption
import com.amity.socialcloud.sdk.api.social.AmitySocialClient
import com.amity.socialcloud.sdk.api.social.comment.AmityCommentRepository
import com.amity.socialcloud.sdk.api.social.post.AmityPostRepository
import com.amity.socialcloud.sdk.helper.core.mention.AmityMentionMetadata
import com.amity.socialcloud.sdk.helper.core.mention.AmityMentionMetadataCreator
import com.amity.socialcloud.sdk.model.core.user.AmityUser
import com.amity.socialcloud.sdk.model.social.comment.AmityComment
import com.amity.socialcloud.sdk.model.social.member.AmityCommunityMember
import com.amity.socialcloud.sdk.model.social.member.AmityCommunityMembership
import com.amity.socialcloud.sdk.model.social.post.AmityPost
import com.amity.socialcloud.uikit.common.base.AmityBaseViewModel
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.schedulers.Schedulers
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

            val textCommentEditor = AmitySocialClient.newCommentRepository()
                .editComment(textComment.getCommentId())
                .text(commentText)
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
        commentText: String,
        userMentions: List<AmityMentionMetadata.USER>,
        onSuccess: (AmityComment) -> Unit,
        onError: (Throwable) -> Unit
    ): Completable {
        val commentCreator = AmitySocialClient.newCommentRepository().createComment()
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
                AmitySocialClient.newPostRepository()
                    .getPost(postId)
                    .ignoreElements()
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

    @OptIn(ExperimentalPagingApi::class)
    fun searchUsersMention(
        keyword: String,
        onResult: (users: PagingData<AmityUser>) -> Unit
    ): Completable {
        return userRepository.searchUsers(keyword)
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
            .includeDeleted(false)
            .build()
            .query()
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
                .getComment(commentId)
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
