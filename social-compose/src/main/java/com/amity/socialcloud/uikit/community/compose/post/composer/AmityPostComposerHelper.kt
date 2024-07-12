package com.amity.socialcloud.uikit.community.compose.post.composer

import com.amity.socialcloud.sdk.model.social.post.AmityPost

object AmityPostComposerHelper {

    private val createdPosts: MutableList<AmityPost> = mutableListOf()

    fun addNewPost(post: AmityPost) {
        createdPosts.add(0, post)
    }

    fun deletePost(postId: String) {
        val deleteIndex = createdPosts.indexOfFirst { it.getPostId() == postId }
        if (deleteIndex != -1) {
            createdPosts.removeAt(deleteIndex)
        }
    }

    fun getCreatedPosts(): List<AmityPost> {
        return createdPosts
    }

    fun clear() {
        createdPosts.clear()
    }
}