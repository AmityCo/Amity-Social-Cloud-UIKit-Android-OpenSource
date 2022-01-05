package com.amity.socialcloud.uikit.community.newsfeed.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.amity.socialcloud.uikit.community.newsfeed.events.AmityCommentRefreshEvent
import com.amity.socialcloud.uikit.community.newsfeed.events.AmityCommentReplyIntentEvent
import com.amity.socialcloud.uikit.community.newsfeed.events.CommentEngagementClickEvent
import com.amity.socialcloud.uikit.community.newsfeed.listener.AmityCommentItemListener
import io.reactivex.Flowable
import io.reactivex.subjects.BehaviorSubject

private const val SAVED_POST_ID = "SAVED_POST_ID"
class AmityCommentListViewModel(private val savedState: SavedStateHandle) : ViewModel() , CommentViewModel {

    init {
        savedState.get<String>(SAVED_POST_ID)?.let { postId = it }
    }

    var postId: String = ""
        set(value) {
            savedState.set(SAVED_POST_ID, value)
            field = value
        }

    internal lateinit var commentItemListener : AmityCommentItemListener
    internal var refreshEvents = Flowable.never<AmityCommentRefreshEvent>()
    internal var isReadOnly: Boolean = false
    internal val commentReactionEventMap: HashMap<String, CommentEngagementClickEvent.Reaction> = hashMapOf()


}