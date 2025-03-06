package com.amity.socialcloud.uikit.community.compose.post.composer

import com.amity.socialcloud.sdk.api.social.AmitySocialClient
import com.amity.socialcloud.sdk.model.social.post.AmityPost
import io.reactivex.rxjava3.schedulers.Schedulers

object AmityPostComposerHelper {

    private val createdPosts: MutableList<AmityPost> = mutableListOf()

    fun addNewPost(post: AmityPost) {
        // Ensure that the post is not already added
        if (createdPosts.filter { it.getPostId() == post.getPostId() }.isEmpty()) {
            createdPosts.add(0, post)
        }
    }

    fun updatePost(postId: String) {
        AmitySocialClient.newPostRepository()
            .getPost(postId)
            .firstOrError()
            .observeOn(Schedulers.io())
            .doOnSuccess { post ->
                val updateIndex = createdPosts.indexOfFirst { it.getPostId() == post.getPostId() }
                if (updateIndex != -1) {
                    createdPosts[updateIndex] = post
                }
            }
            .doOnError {
                // ignore
            }
            .subscribe()

    }

    fun deletePost(postId: String) {
        val deleteIndex = createdPosts.indexOfFirst { it.getPostId() == postId }
        if (deleteIndex != -1) {
            createdPosts.removeAt(deleteIndex)
        }
    }

    fun getCreatedPosts(): List<AmityPost> {
        return createdPosts.distinctBy { it.getPostId() }
    }

    fun clear() {
        createdPosts.clear()
    }
}