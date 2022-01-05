package com.amity.socialcloud.uikit.community.utils

import android.content.Context
import android.content.Intent
import com.amity.socialcloud.sdk.core.file.AmityImage
import com.amity.socialcloud.sdk.social.comment.AmityComment
import com.amity.socialcloud.sdk.social.community.AmityCommunity
import com.amity.socialcloud.sdk.social.feed.AmityPost
import com.amity.socialcloud.uikit.common.imagepreview.AmityImagePreviewActivity
import com.amity.socialcloud.uikit.common.imagepreview.AmityPreviewImage
import com.amity.socialcloud.uikit.community.detailpage.AmityCommunityPageActivity
import com.amity.socialcloud.uikit.community.explore.activity.EXTRA_PARAM_COMMUNITY
import com.amity.socialcloud.uikit.community.newsfeed.activity.*
import com.amity.socialcloud.uikit.community.newsfeed.util.AmityTimelineType
import com.amity.socialcloud.uikit.community.profile.activity.AmityEditUserProfileActivity
import com.amity.socialcloud.uikit.community.profile.activity.AmityUserProfileActivity

const val EXTRA_PARAM_NEWS_FEED = "news_feed"
const val EXTRA_PARAM_NEWS_FEED_ID = "news_feed_id"
const val EXTRA_PARAM_MENTION_METADATA_USERS = "mentioned_metadata_users"
const val EXTRA_PARAM_POST_ID = "news_feed_id"
const val EXTRA_PARAM_COMMUNITY_ID = "community_id"
const val EXTRA_PARAM_TIMELINE_TYPE = "timeline_type"
const val EXTRA_PARAM_POST_ATTACHMENT_OPTIONS = "post_attachment_options"
const val EXTRA_PARAM_POST_CREATION_TYPE = "post_creation_type"
const val EXTRA_PARAM_TARGET_TYPE = "target_type"
const val EXTRA_PARAM_TARGET_ID = "target_id"
const val EXTRA_PARAM_INCLUDE_DELETED = "include_deleted"
const val EXTRA_PARAM_NOTIFICATION_SETTING_TYPE = "noti_setting_type"


class AmityCommunityNavigation {

    companion object {
        fun navigateToCreatePost(context: Context) {
            val intent = Intent(context, AmityPostCreatorActivity::class.java)
            context.startActivity(intent)
        }

        fun navigateToCreatePost(context: Context, community: AmityCommunity) {
            val intent = Intent(context, AmityPostCreatorActivity::class.java).apply {
                putExtra(EXTRA_PARAM_COMMUNITY, community)
            }
            context.startActivity(intent)
        }

        fun navigateToEditPost(context: Context, post: AmityPost) {
            val intent = Intent(context, AmityPostEditorActivity::class.java)
            intent.putExtra(EXTRA_PARAM_POST_ID, post.getPostId())
            context.startActivity(intent)
        }

        fun navigateToCreatePostRoleSelection(context: Context) {
            val intent = Intent(context, AmityPostTargetPickerActivity::class.java)
            context.startActivity(intent)
        }

        fun navigateToPostDetails(context: Context, postId: String, timelineType: AmityTimelineType) {
            val intent = AmityPostDetailsActivity.newIntent(context, postId, timelineType)
            context.startActivity(intent)
        }

        fun navigateToPostDetails(
            context: Context,
            post: AmityPost,
            comment: AmityComment,
            timelineType: AmityTimelineType
        ) {
            val intent = AmityPostDetailsActivity.newIntent(context, post.getPostId(), timelineType, comment)
            context.startActivity(intent)
        }

        fun navigateToImagePreview(context: Context, images: List<AmityImage>, position: Int) {
            val previewImages = mutableListOf<AmityPreviewImage>()
            images.forEach {
                previewImages.add(AmityPreviewImage(it.getUrl(AmityImage.Size.LARGE)))
            }
            val intent =
                AmityImagePreviewActivity.newIntent(context, position, true, ArrayList(previewImages))
            context.startActivity(intent)
        }

        fun navigateToVideoPreview(context: Context, videoPostParentId: String, position: Int) {
            val intent = AmityVideoPostPlayerActivity.newIntent(context, videoPostParentId, position)
            context.startActivity(intent)
        }

        fun navigateToUserProfile(context: Context, userId: String) {
            val intent = AmityUserProfileActivity.newIntent(context, userId)
            context.startActivity(intent)
        }

        fun navigateToEditProfile(context: Context) {
            val intent = Intent(context, AmityEditUserProfileActivity::class.java)
            context.startActivity(intent)
        }

        fun navigateToCommunityDetails(context: Context, community: AmityCommunity) {
            val detailIntent = AmityCommunityPageActivity
                .newIntent(context, community)
            context.startActivity(detailIntent)
        }
    }
}