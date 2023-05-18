package com.amity.socialcloud.uikit.community.newsfeed.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.paging.PagingData
import com.amity.socialcloud.sdk.api.core.AmityCoreClient
import com.amity.socialcloud.sdk.api.core.reaction.reference.AmityReactionReference
import com.amity.socialcloud.sdk.model.core.reaction.AmityReaction
import com.amity.socialcloud.sdk.model.core.reaction.AmityReactionReferenceType
import com.amity.socialcloud.sdk.model.core.user.AmityUser
import com.amity.socialcloud.uikit.community.newsfeed.adapter.AmityReactionAdapter
import com.amity.socialcloud.uikit.community.newsfeed.listener.AmityUserClickListener
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.BackpressureStrategy
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.schedulers.Schedulers
import io.reactivex.rxjava3.subjects.PublishSubject

private const val SAVED_REFERENCE_ID = "SAVED_REFERENCE_ID"
private const val SAVED_REFERENCE_TYPE = "SAVED_REFERENCE_TYPE"

class AmitySingleReactionViewModel(private val savedState: SavedStateHandle) : ViewModel() {

    init {
        savedState.get<AmityReactionReferenceType>(SAVED_REFERENCE_TYPE)?.let { referenceType = it }
        savedState.get<String>(SAVED_REFERENCE_ID)?.let { referenceId = it }
    }

    var referenceId: String = ""
        set(value) {
            savedState.set(SAVED_REFERENCE_ID, value)
            field = value
        }

    var referenceType: AmityReactionReferenceType = AmityReactionReferenceType.POST
        set(value) {
            savedState.set(SAVED_REFERENCE_TYPE, value)
            field = value
        }

    lateinit var userClickListener: AmityUserClickListener

    private val userClickPublisher = PublishSubject.create<AmityUser>()

    fun createReactionAdapter(): AmityReactionAdapter {
        return AmityReactionAdapter(
            userClickPublisher
        )
    }

    fun getReactionPagingData(
        reactionName: String?,
        onPageLoaded: (reactions: PagingData<AmityReaction>) -> Unit
    ): Completable {
        return AmityCoreClient.newReactionRepository()
            .getReactions(getReference())
            .reactionName(reactionName)
            .build()
            .query()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnNext { onPageLoaded.invoke(it) }
            .ignoreElements()
    }

    fun getUserClickEvents(onReceivedEvent: (AmityUser) -> Unit): Completable {
        return userClickPublisher.toFlowable(BackpressureStrategy.BUFFER)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnNext {
                onReceivedEvent.invoke(it)
            }
            .doOnError {

            }
            .ignoreElements()
    }

    private fun getReference(): AmityReactionReference {
        return when (referenceType) {
            AmityReactionReferenceType.POST -> AmityReactionReference.POST(referenceId)
            AmityReactionReferenceType.COMMENT -> AmityReactionReference.COMMENT(referenceId)
            AmityReactionReferenceType.MESSAGE -> AmityReactionReference.MESSAGE(referenceId)
        }
    }
}