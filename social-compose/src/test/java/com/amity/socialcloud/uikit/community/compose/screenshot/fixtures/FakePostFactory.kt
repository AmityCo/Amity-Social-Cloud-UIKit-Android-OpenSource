package com.amity.socialcloud.uikit.community.compose.screenshot.fixtures

import com.amity.socialcloud.sdk.helper.core.mention.AmityMentionee
import com.amity.socialcloud.sdk.model.core.producttag.AmityProductTag
import com.amity.socialcloud.sdk.model.social.poll.AmityPoll
import com.amity.socialcloud.sdk.model.social.poll.AmityPollAnswer
import com.amity.socialcloud.sdk.model.social.post.AmityPost
import com.amity.socialcloud.sdk.model.video.room.AmityRoom
import com.amity.socialcloud.sdk.model.video.room.AmityRoomModeration
import com.amity.socialcloud.sdk.model.video.room.AmityRoomModerationLabel
import com.amity.socialcloud.sdk.model.video.room.AmityRoomStatus
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import io.mockk.every
import io.mockk.mockk
import io.reactivex.rxjava3.core.Flowable
import org.joda.time.DateTime

object FakePostFactory {

    private fun basePost(postId: String): AmityPost = mockk(relaxed = true) {
        every { getPostId() } returns postId
        every { getCreatorId() } returns "user-001"
        every { getCreator() } returns FakeUserFactory.currentUser()
        every { getCreatedAt() } returns DateTime.now()
        every { getUpdatedAt() } returns DateTime.now()
        every { getEditedAt() } returns null
        every { isEdited() } returns false
        every { isDeleted() } returns false
        // Must be in AmitySocialBehaviorHelper.supportedStructureTypes or the page
        // routes to AmityPostErrorPage instead of rendering the post.
        every { getStructureType() } returns AmityPost.StructureType.TEXT
        // getType() is a sealed class (AmityPost.DataType), not an enum — relaxed mock
        // returns null by default. AmityPostPreviewLinkView guards on
        // post.getType() == AmityPost.DataType.TEXT, so null causes an early return
        // and the preview card is never rendered.
        every { getType() } returns AmityPost.DataType.TEXT
        every { isFlaggedByMe() } returns false
        every { getCommentCount() } returns 0
        every { getReactionCount() } returns 0
        every { getMyReactions() } returns emptyList()
        every { getChildren() } returns emptyList()
        every { getLatestComments() } returns emptyList()
        every { getMetadata() } returns null
        every { getMentionees() } returns emptyList()
        every { getHashtags() } returns emptyList()
    }

    /** Plain short text post — base case */
    fun textPost(
        postId: String = "post-text-001",
        text: String = "Hello, this is a test post content.",
    ): AmityPost = basePost(postId).also { post ->
        val textData = mockk<AmityPost.Data.TEXT>(relaxed = true) {
            every { getText() } returns text
        }
        every { post.getData() } returns textData
    }

    /** Long text — tests "See more" expand behavior */
    fun longTextPost(postId: String = "post-long-001"): AmityPost = textPost(
        postId = postId,
        text = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. ".repeat(8).trim(),
    )

    /**
     * Text post with @mention.
     *
     * getMetadata() MUST return a JsonObject with a "mentioned" array for
     * AmityMentionMetadataGetter.getMentionedUsers() to produce non-empty results.
     * Without it, AmityExpandableText iterates over an emptyList and no highlight
     * SpanStyle is ever applied — the @ token renders as plain text.
     */
    fun mentionPost(
        postId: String = "post-mention-001",
        mentionedUserId: String = "user-002",
        mentionedDisplayName: String = "MentionedUser",
    ): AmityPost {
        val postText = "Hello @$mentionedDisplayName, how are you?"
        val atIndex = postText.indexOf('@')
        val nameLength = mentionedDisplayName.length

        val mentionedEntry = JsonObject().apply {
            addProperty("userId", mentionedUserId)
            addProperty("index", atIndex)
            addProperty("length", nameLength)
            addProperty("type", "user")
        }
        val metadata = JsonObject().apply {
            add("mentioned", JsonArray().apply { add(mentionedEntry) })
        }

        return textPost(postId = postId, text = postText).also { post ->
            val mentionee = mockk<AmityMentionee.USER>(relaxed = true) {
                every { getUserId() } returns mentionedUserId
            }
            every { post.getMentionees() } returns listOf(mentionee)
            every { post.getMetadata() } returns metadata
        }
    }

    /**
     * Text post with #hashtags.
     *
     * getMetadata() MUST return a JsonObject with a "hashtags" array for
     * AmityHashtagMetadataGetter.getHashtags() to produce non-empty results.
     * getHashtags() alone is NOT read by the metadata getter — only metadata JSON is.
     */
    fun hashtagPost(postId: String = "post-hashtag-001"): AmityPost {
        val postText = "Exploring #Android and #JetpackCompose today!"

        val hashtag1 = JsonObject().apply {
            addProperty("text", "Android")
            addProperty("index", postText.indexOf("#Android"))
            addProperty("length", "Android".length)
        }
        val hashtag2 = JsonObject().apply {
            addProperty("text", "JetpackCompose")
            addProperty("index", postText.indexOf("#JetpackCompose"))
            addProperty("length", "JetpackCompose".length)
        }
        val metadata = JsonObject().apply {
            add("hashtags", JsonArray().apply { add(hashtag1); add(hashtag2) })
        }

        return textPost(postId = postId, text = postText).also { post ->
            every { post.getHashtags() } returns listOf("Android", "JetpackCompose")
            every { post.getMetadata() } returns metadata
        }
    }

    /**
     * Text post with product tag highlights.
     *
     * AmityExpandableText reads productTags directly from the list (not from metadata JSON).
     * AmityPostContentElement calls post.getProductTags().filterIsInstance<AmityProductTag.Text>()
     * and passes the result straight to AmityExpandableText.
     *
     * Tag positions use property access (index, length, productId) not getter methods.
     */
    fun productTagPost(postId: String = "post-producttag-001"): AmityPost {
        val postText = "Check out the Nike Sneakers and Adidas Hoodie!"

        val tag1 = mockk<AmityProductTag.Text>(relaxed = true).also { tag ->
            every { tag.productId } returns "product-001"
            every { tag.text } returns "Nike Sneakers"
            every { tag.index } returns postText.indexOf("Nike Sneakers")
            every { tag.length } returns "Nike Sneakers".length
            every { tag.product } returns null  // null product → not ARCHIVED → highlight shown
        }
        val tag2 = mockk<AmityProductTag.Text>(relaxed = true).also { tag ->
            every { tag.productId } returns "product-002"
            every { tag.text } returns "Adidas Hoodie"
            every { tag.index } returns postText.indexOf("Adidas Hoodie")
            every { tag.length } returns "Adidas Hoodie".length
            every { tag.product } returns null
        }

        return textPost(postId = postId, text = postText).also { post ->
            every { post.getProductTags() } returns listOf(tag1, tag2)
        }
    }

    /** Edited post — shows "(Edited)" badge */
    fun editedPost(postId: String = "post-edited-001"): AmityPost = textPost(postId).also { post ->
        every { post.isEdited() } returns true
        every { post.getEditedAt() } returns DateTime.now().minusMinutes(5)
    }

    /**
     * Post whose structure type is not in the supported list.
     * AmityPostDetailPage should render AmityPostErrorPage for this.
     */
    fun unsupportedTypePost(postId: String = "post-unsupported-001"): AmityPost =
        basePost(postId).also { post ->
            every { post.getStructureType() } returns AmityPost.StructureType.UNKNOWN
            every { post.getData() } returns mockk<AmityPost.Data.TEXT>(relaxed = true) {
                every { getText() } returns "unsupported"
            }
        }

    /** Deleted post — AmityPostDetailPage should render AmityPostErrorPage */
    fun deletedPost(postId: String = "post-deleted-001"): AmityPost =
        basePost(postId).also { post ->
            every { post.isDeleted() } returns true
            every { post.getData() } returns mockk<AmityPost.Data.TEXT>(relaxed = true) {
                every { getText() } returns "deleted"
            }
        }

    // ── Media post types ──────────────────────────────────────────────────────

    /**
     * Image post — structureType IMAGE, children each have Data.IMAGE.
     * AmityPostContentComponent routes to AmityPostMediaElement for images.
     */
    fun imagePost(postId: String = "post-image-001"): AmityPost =
        basePost(postId).also { post ->
            every { post.getStructureType() } returns AmityPost.StructureType.IMAGE
            val imageData = mockk<AmityPost.Data.IMAGE>(relaxed = true) {
                every { getPostId() } returns "${postId}-child"
                every { getImage() } returns null
            }
            every { post.getData() } returns imageData
            // Children carry the actual IMAGE data so AmityPostContentComponent
            // picks it up via getChildren()
            val child = mockk<AmityPost>(relaxed = true) {
                every { getPostId() } returns "${postId}-child"
                every { getData() } returns imageData
                every { getStructureType() } returns AmityPost.StructureType.IMAGE
                every { getChildren() } returns emptyList()
            }
            every { post.getChildren() } returns listOf(child)
        }

    /**
     * Video post — structureType VIDEO, children each have Data.VIDEO.
     */
    fun videoPost(postId: String = "post-video-001"): AmityPost =
        basePost(postId).also { post ->
            every { post.getStructureType() } returns AmityPost.StructureType.VIDEO
            val videoData = mockk<AmityPost.Data.VIDEO>(relaxed = true) {
                every { getPostId() } returns "${postId}-child"
                every { getThumbnailImage() } returns null
            }
            every { post.getData() } returns videoData
            val child = mockk<AmityPost>(relaxed = true) {
                every { getPostId() } returns "${postId}-child"
                every { getData() } returns videoData
                every { getStructureType() } returns AmityPost.StructureType.VIDEO
                every { getChildren() } returns emptyList()
            }
            every { post.getChildren() } returns listOf(child)
        }

    /**
     * Poll post — structureType POLL, child has Data.POLL.
     * AmityPostContentComponent routes to AmityPostPollElement.
     *
     * getPoll() returns Flowable.just(fakePoll) so the composable's pollData becomes non-null
     * and the poll UI is actually rendered. Without this, pollData stays null and the element
     * returns early (renders nothing).
     *
     * The fake poll is OPEN, single-choice, not yet voted, closes 7 days from now, with
     * 3 text-type answers.
     */
    fun pollPost(postId: String = "post-poll-001"): AmityPost =
        basePost(postId).also { post ->
            every { post.getStructureType() } returns AmityPost.StructureType.POLL

            val answer1 = mockk<AmityPollAnswer>(relaxed = true) {
                every { data } returns "Option A — The first choice"
                every { dataType } returns "text"
                every { voteCount } returns 10
                every { isVotedByUser } returns false
                every { id } returns "answer-001"
            }
            val answer2 = mockk<AmityPollAnswer>(relaxed = true) {
                every { data } returns "Option B — The second choice"
                every { dataType } returns "text"
                every { voteCount } returns 5
                every { isVotedByUser } returns false
                every { id } returns "answer-002"
            }
            val answer3 = mockk<AmityPollAnswer>(relaxed = true) {
                every { data } returns "Option C — The third choice"
                every { dataType } returns "text"
                every { voteCount } returns 0
                every { isVotedByUser } returns false
                every { id } returns "answer-003"
            }

            val fakePoll = mockk<AmityPoll>(relaxed = true) {
                every { getPollId() } returns "poll-001"
                every { getTitle() } returns "Which option do you prefer?"
                every { getQuestion() } returns "Cast your vote below."
                every { getAnswers() } returns listOf(answer1, answer2, answer3)
                every { getAnswerType() } returns AmityPoll.AnswerType.SINGLE
                every { getStatus() } returns AmityPoll.Status.OPEN
                every { isVoted() } returns false
                every { getClosedAt() } returns DateTime.now().plusDays(7)
            }

            val pollData = mockk<AmityPost.Data.POLL>(relaxed = true) {
                every { getPostId() } returns "${postId}-child"
                every { getPollId() } returns "poll-001"
                every { getPoll() } returns Flowable.just(fakePoll)
            }
            every { post.getData() } returns pollData
            val child = mockk<AmityPost>(relaxed = true) {
                every { getPostId() } returns "${postId}-child"
                every { getData() } returns pollData
                every { getStructureType() } returns AmityPost.StructureType.POLL
                every { getChildren() } returns emptyList()
            }
            every { post.getChildren() } returns listOf(child)
        }

    /**
     * Poll post where the current user already voted — isVoted() = true.
     * The composable enters result-mode immediately so vote-count bars are rendered
     * instead of radio buttons.
     *
     * answer1 has the highest vote count and is voted by the user — it appears highlighted.
     */
    fun pollPostVoted(postId: String = "post-poll-voted-001"): AmityPost =
        basePost(postId).also { post ->
            every { post.getStructureType() } returns AmityPost.StructureType.POLL

            val answer1 = mockk<AmityPollAnswer>(relaxed = true) {
                every { data } returns "Option A — The first choice"
                every { dataType } returns "text"
                every { voteCount } returns 12
                every { isVotedByUser } returns true
                every { id } returns "answer-001"
            }
            val answer2 = mockk<AmityPollAnswer>(relaxed = true) {
                every { data } returns "Option B — The second choice"
                every { dataType } returns "text"
                every { voteCount } returns 5
                every { isVotedByUser } returns false
                every { id } returns "answer-002"
            }
            val answer3 = mockk<AmityPollAnswer>(relaxed = true) {
                every { data } returns "Option C — The third choice"
                every { dataType } returns "text"
                every { voteCount } returns 0
                every { isVotedByUser } returns false
                every { id } returns "answer-003"
            }

            val fakePoll = mockk<AmityPoll>(relaxed = true) {
                every { getPollId() } returns "poll-voted-001"
                every { getTitle() } returns "Which option do you prefer?"
                every { getQuestion() } returns "Cast your vote below."
                every { getAnswers() } returns listOf(answer1, answer2, answer3)
                every { getAnswerType() } returns AmityPoll.AnswerType.SINGLE
                every { getStatus() } returns AmityPoll.Status.OPEN
                every { isVoted() } returns true
                every { getClosedAt() } returns DateTime.now().plusDays(7)
            }

            val pollData = mockk<AmityPost.Data.POLL>(relaxed = true) {
                every { getPostId() } returns "${postId}-child"
                every { getPollId() } returns "poll-voted-001"
                every { getPoll() } returns Flowable.just(fakePoll)
            }
            every { post.getData() } returns pollData
            val child = mockk<AmityPost>(relaxed = true) {
                every { getPostId() } returns "${postId}-child"
                every { getData() } returns pollData
                every { getStructureType() } returns AmityPost.StructureType.POLL
                every { getChildren() } returns emptyList()
            }
            every { post.getChildren() } returns listOf(child)
        }

    /**
     * AmityPostContentComponent routes to AmityPostLivestreamElement.
     * getStream() returns Flowable.never() to prevent real SDK use case from executing.
     */
    fun livestreamPost(postId: String = "post-livestream-001"): AmityPost =
        basePost(postId).also { post ->
            every { post.getStructureType() } returns AmityPost.StructureType.LIVESTREAM
            val liveData = mockk<AmityPost.Data.LIVE_STREAM>(relaxed = true) {
                every { getPostId() } returns "${postId}-child"
                every { getStream() } returns Flowable.never()
            }
            every { post.getData() } returns liveData
            val child = mockk<AmityPost>(relaxed = true) {
                every { getPostId() } returns "${postId}-child"
                every { getData() } returns liveData
                every { getStructureType() } returns AmityPost.StructureType.LIVESTREAM
                every { getChildren() } returns emptyList()
            }
            every { post.getChildren() } returns listOf(child)
        }

    /**
     * Clip post — structureType CLIP, child has Data.CLIP.
     * AmityPostContentComponent routes to AmityPostContentElement + AmityPostMediaElement.
     */
    fun clipPost(postId: String = "post-clip-001"): AmityPost =
        basePost(postId).also { post ->
            every { post.getStructureType() } returns AmityPost.StructureType.CLIP
            val clipData = mockk<AmityPost.Data.CLIP>(relaxed = true) {
                every { getPostId() } returns "${postId}-child"
                every { getThumbnailImage() } returns null
                every { getFileId() } returns null
                every { getIsMuted() } returns false
            }
            every { post.getData() } returns clipData
            val child = mockk<AmityPost>(relaxed = true) {
                every { getPostId() } returns "${postId}-child"
                every { getData() } returns clipData
                every { getStructureType() } returns AmityPost.StructureType.CLIP
                every { getChildren() } returns emptyList()
            }
            every { post.getChildren() } returns listOf(child)
        }

    /**
     * Room post — structureType ROOM, child has Data.ROOM.
     * AmityPostContentComponent routes to AmityPostLivestreamElement (same branch as LIVE_STREAM).
     * Base variant: getRoom() returns null (loading / null state).
     */
    fun roomPost(postId: String = "post-room-001"): AmityPost =
        basePost(postId).also { post ->
            every { post.getStructureType() } returns AmityPost.StructureType.ROOM
            val roomData = mockk<AmityPost.Data.ROOM>(relaxed = true) {
                every { getPostId() } returns "${postId}-child"
                every { getRoomId() } returns "room-001"
                every { getRoom() } returns null
            }
            every { post.getData() } returns roomData
            val child = mockk<AmityPost>(relaxed = true) {
                every { getPostId() } returns "${postId}-child"
                every { getData() } returns roomData
                every { getStructureType() } returns AmityPost.StructureType.ROOM
                every { getChildren() } returns emptyList()
            }
            every { post.getChildren() } returns listOf(child)
        }

    // ── Room post state variants ───────────────────────────────────────────────

    /**
     * Builds a base room post where the child carries a live [AmityRoom] mock.
     * All room-state factory methods delegate to this helper and override
     * the fields they need.
     */
    private fun roomPostWithRoom(
        postId: String,
        roomId: String = "room-001",
        configure: (AmityRoom) -> Unit = {},
    ): AmityPost = basePost(postId).also { post ->
        every { post.getStructureType() } returns AmityPost.StructureType.ROOM
        val room = mockk<AmityRoom>(relaxed = true) {
            every { getRoomId() } returns roomId
            every { isDeleted() } returns false
            // getModeration() returns a relaxed mock with terminateLabels = emptyList() by default
            every { getStatus() } returns AmityRoomStatus.IDLE
            every { getThumbnail() } returns null
            every { getLiveThumbnailUrl() } returns null
            every { getRecordedPlaybackInfos() } returns emptyList()
            every { getLiveResolution() } returns null
            every { getRecordedResolution() } returns null
        }
        configure(room)
        val roomData = mockk<AmityPost.Data.ROOM>(relaxed = true) {
            every { getPostId() } returns "${postId}-child"
            every { getRoomId() } returns roomId
            every { getRoom() } returns room
        }
        every { post.getData() } returns roomData
        val child = mockk<AmityPost>(relaxed = true) {
            every { getPostId() } returns "${postId}-child"
            every { getData() } returns roomData
            every { getStructureType() } returns AmityPost.StructureType.ROOM
            every { getChildren() } returns emptyList()
            every { getProducts() } returns emptyList()
        }
        every { post.getChildren() } returns listOf(child)
    }

    /** Room post where the room object is null — element renders nothing (loading state). */
    fun roomPost_nullRoom(postId: String = "post-room-null-001"): AmityPost = roomPost(postId)

    /** Room post where room.isDeleted() = true → AmityLivestreamUnavailableView. */
    fun roomPost_deleted(postId: String = "post-room-deleted-001"): AmityPost =
        roomPostWithRoom(postId) { room ->
            every { room.isDeleted() } returns true
        }

    /** Room post where terminateLabels is non-empty → AmityLivestreamTerminatedView. */
    fun roomPost_terminated(postId: String = "post-room-terminated-001"): AmityPost =
        roomPostWithRoom(postId) { room ->
            every { room.isDeleted() } returns false
            val moderation = mockk<AmityRoomModeration>(relaxed = true) {
                every { terminateLabels } returns listOf(
                    mockk<AmityRoomModerationLabel>(relaxed = true)
                )
            }
            every { room.getModeration() } returns moderation
        }

    /** Room post with status ENDED → AmityLivestreamEndedView ("processing"). */
    fun roomPost_ended(postId: String = "post-room-ended-001"): AmityPost =
        roomPostWithRoom(postId) { room ->
            every { room.getStatus() } returns AmityRoomStatus.ENDED
        }

    /** Room post with status ERROR → AmityLivestreamReplayUnavailableView. */
    fun roomPost_error(postId: String = "post-room-error-001"): AmityPost =
        roomPostWithRoom(postId) { room ->
            every { room.getStatus() } returns AmityRoomStatus.ERROR
        }

    /** Room post with status IDLE — "UPCOMING LIVE" badge, no play button, default thumbnail. */
    fun roomPost_idle(postId: String = "post-room-idle-001"): AmityPost =
        roomPostWithRoom(postId) { room ->
            every { room.getStatus() } returns AmityRoomStatus.IDLE
        }

    /** Room post with status LIVE — "LIVE" badge, play button, no thumbnail → placeholder. */
    fun roomPost_live(postId: String = "post-room-live-001"): AmityPost =
        roomPostWithRoom(postId) { room ->
            every { room.getStatus() } returns AmityRoomStatus.LIVE
        }

    /** Room post with status LIVE and a liveThumbnailUrl set — triggers Mux live thumbnail. */
    fun roomPost_liveWithThumbnail(
        postId: String = "post-room-live-thumb-001",
        liveThumbnailUrl: String = "https://image.mux.com/live-abc123/thumbnail.jpg",
    ): AmityPost = roomPostWithRoom(postId) { room ->
        every { room.getStatus() } returns AmityRoomStatus.LIVE
        every { room.getLiveThumbnailUrl() } returns liveThumbnailUrl
    }

    /** Room post with status WAITING_RECONNECT — same as LIVE badge. */
    fun roomPost_waitingReconnect(postId: String = "post-room-reconnect-001"): AmityPost =
        roomPostWithRoom(postId) { room ->
            every { room.getStatus() } returns AmityRoomStatus.WAITING_RECONNECT
        }

    /** Room post with status RECORDED — "RECORDED" badge, play button, no thumbnail → placeholder. */
    fun roomPost_recorded(postId: String = "post-room-recorded-001"): AmityPost =
        roomPostWithRoom(postId) { room ->
            every { room.getStatus() } returns AmityRoomStatus.RECORDED
        }

    /**
     * Room post with a creator-uploaded thumbnail (AmityImage non-null).
     * Uses IDLE status so other thumbnail paths stay inactive.
     * The thumbnail URL passes the "not null and not starting with 'null'" guard in
     * AmityChildRoomPostElement.
     */
    fun roomPost_withCreatorThumbnail(
        postId: String = "post-room-creator-thumb-001",
        thumbnailUrl: String = "https://example.com/creator-thumbnail.jpg",
    ): AmityPost = roomPostWithRoom(postId) { room ->
        every { room.getStatus() } returns AmityRoomStatus.IDLE
        val image = mockk<com.amity.socialcloud.sdk.model.core.file.AmityImage>(relaxed = true) {
            every {
                getUrl(com.amity.socialcloud.sdk.model.core.file.AmityImage.Size.MEDIUM)
            } returns thumbnailUrl
        }
        every { room.getThumbnail() } returns image
    }

    // ── Image poll post types ─────────────────────────────────────────────────

    /**
     * Internal builder for image-poll posts.
     *
     * All answers have [dataType] = "image" and [getImage] = null (no Coil request).
     * The number of vote counts is set to produce realistic percentage bars in result mode.
     *
<<<<<<< HEAD
     * @param answerCount          Number of poll answers (2–10).
     * @param answerType           SINGLE or MULTIPLE.
     * @param isVoted              Whether the current user has already voted.
     * @param status               OPEN or CLOSED.
     * @param closedAt             Poll expiry DateTime. Use DateTime.now().minusDays(1) for expired.
     * @param votedIndices         Indices of answers where isVotedByUser = true.
     * @param reviewStatus         PUBLISHED or UNDER_REVIEW.
     * @param forceZeroVoteCounts  When true, all answers return voteCount = 0 regardless of
     *                             [isVoted] or [status]. Use for the divide-by-zero safety scenario
     *                             where the user has voted but every answer has 0 votes.
     */
    private fun imagePollBase(
        postId: String,
        answerCount: Int = 3,
        answerType: AmityPoll.AnswerType = AmityPoll.AnswerType.SINGLE,
        isVoted: Boolean = false,
        status: AmityPoll.Status = AmityPoll.Status.OPEN,
        closedAt: DateTime = DateTime.now().plusDays(7),
        votedIndices: List<Int> = emptyList(),
        reviewStatus: com.amity.socialcloud.sdk.api.social.post.review.AmityReviewStatus =
            com.amity.socialcloud.sdk.api.social.post.review.AmityReviewStatus.PUBLISHED,
        forceZeroVoteCounts: Boolean = false,
    ): AmityPost = basePost(postId).also { post ->
        every { post.getStructureType() } returns AmityPost.StructureType.POLL
        every { post.getReviewStatus() } returns reviewStatus

        val voteCounts = listOf(12, 7, 3, 5, 2, 8, 1, 4, 6, 9)
        val answers = (0 until answerCount).map { i ->
            mockk<AmityPollAnswer>(relaxed = true) {
                every { data } returns "Image Option ${i + 1}"
                every { dataType } returns "image"
                every { voteCount } returns when {
                    forceZeroVoteCounts -> 0
                    isVoted || status == AmityPoll.Status.CLOSED -> voteCounts[i % voteCounts.size]
                    else -> 0
                }
                every { isVotedByUser } returns (i in votedIndices)
                every { id } returns "answer-img-${postId}-$i"
                every { getImage() } returns null
            }
        }

        val fakePoll = mockk<AmityPoll>(relaxed = true) {
            every { getPollId() } returns "poll-img-$postId"
            every { getTitle() } returns "Which image do you prefer?"
            every { getQuestion() } returns "Cast your vote below."
            every { getAnswers() } returns answers
            every { getAnswerType() } returns answerType
            every { getStatus() } returns status
            every { isVoted() } returns isVoted
            every { getClosedAt() } returns closedAt
        }

        val pollData = mockk<AmityPost.Data.POLL>(relaxed = true) {
            every { getPostId() } returns "${postId}-child"
            every { getPollId() } returns "poll-img-$postId"
            every { getPoll() } returns Flowable.just(fakePoll)
        }
        every { post.getData() } returns pollData
        val child = mockk<AmityPost>(relaxed = true) {
            every { getPostId() } returns "${postId}-child"
            every { getData() } returns pollData
            every { getStructureType() } returns AmityPost.StructureType.POLL
            every { getChildren() } returns emptyList()
        }
        every { post.getChildren() } returns listOf(child)
    }

    /** Image-poll: SINGLE-select, OPEN, not yet voted. Voting mode. */
    fun imagePollPost(postId: String = "post-imgpoll-001"): AmityPost =
        imagePollBase(postId = postId)

    /** Image-poll: SINGLE-select, OPEN, user voted answer[0]. Enters result mode. */
    fun imagePollPostVotedSingle(postId: String = "post-imgpoll-voted-s-001"): AmityPost =
        imagePollBase(postId = postId, isVoted = true, votedIndices = listOf(0))

    /** Image-poll: MULTIPLE-select, OPEN, not yet voted. Voting mode. */
    fun imagePollPostMulti(postId: String = "post-imgpoll-multi-001"): AmityPost =
        imagePollBase(postId = postId, answerType = AmityPoll.AnswerType.MULTIPLE)

    /** Image-poll: MULTIPLE-select, OPEN, user voted answers[0] and [1]. Result mode. */
    fun imagePollPostVotedMulti(postId: String = "post-imgpoll-voted-m-001"): AmityPost =
        imagePollBase(
            postId = postId,
            answerType = AmityPoll.AnswerType.MULTIPLE,
            isVoted = true,
            votedIndices = listOf(0, 1),
        )

    /** Image-poll: status = CLOSED. Enters result mode regardless of isVoted. */
    fun imagePollPostClosed(postId: String = "post-imgpoll-closed-001"): AmityPost =
        imagePollBase(postId = postId, status = AmityPoll.Status.CLOSED, isVoted = false)

    /** Image-poll: closedAt in the past (expired by time). Enters result mode via the
     *  `poll.getClosedAt().isBefore(DateTime.now())` branch. Includes realistic vote counts
     *  (isVoted = true) so percentage bars are visible — distinguishable from imagePollPostZeroVotes.
     */
    fun imagePollPostExpired(postId: String = "post-imgpoll-expired-001"): AmityPost =
        imagePollBase(
            postId = postId,
            closedAt = DateTime.now().minusDays(1),
            isVoted = true,
            votedIndices = listOf(0),
        )

    /**
     * Image-poll: all answers have voteCount = 0, but the user has voted (isVoted = true).
     * Use in result mode — percentage bars should all render at 0%.
     * Specifically tests the divide-by-zero safety guard when totalVoteCount = 0.
     */
    fun imagePollPostZeroVotes(postId: String = "post-imgpoll-zero-001"): AmityPost =
        imagePollBase(
            postId = postId,
            isVoted = true,
            votedIndices = listOf(0),
            forceZeroVoteCounts = true,
        )

    /** Image-poll: 10 answers (maximum). Initial view shows 4; rest collapsed. */
    fun imagePollPostMaxOptions(postId: String = "post-imgpoll-max-001"): AmityPost =
        imagePollBase(postId = postId, answerCount = 10)

    /**
     * Image-poll: reviewStatus = UNDER_REVIEW.
     * The composable blocks result mode — shows voting UI even if isVoted = false.
     */
    fun imagePollPostUnderReview(postId: String = "post-imgpoll-review-001"): AmityPost =
        imagePollBase(
            postId = postId,
            reviewStatus = com.amity.socialcloud.sdk.api.social.post.review.AmityReviewStatus.UNDER_REVIEW,
        )

    // ── Link preview post types ───────────────────────────────────────────────

    /**
     * Text post whose body contains a URL.
     *
     * AmityPostPreviewLinkView reads post text via getData().getText(), extracts URLs,
     * and calls AmityPreviewUrl.getPostPreviewUrl(). The caller must mock
     * AmityPreviewUrl via mockkObject to control what metadata is returned.
     *
     * @param url  The URL embedded in the post text (e.g. "https://www.youtube.com/watch?v=abc").
     */
    fun textPostWithUrl(
        postId: String = "post-link-001",
        url: String = "https://www.youtube.com/watch?v=dQw4w9WgXcQ",
    ): AmityPost {
        val postText = "Check out this link: $url"
        return textPost(postId = postId, text = postText)
    }
}
