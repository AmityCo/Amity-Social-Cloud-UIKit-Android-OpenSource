package com.amity.socialcloud.uikit.community.compose.visitor

import com.amity.socialcloud.sdk.api.core.AmityCoreClient
import com.amity.socialcloud.sdk.core.session.model.SessionState
import com.amity.socialcloud.uikit.common.utils.isVisitor
import com.amity.socialcloud.uikit.community.compose.AmitySocialBehaviorHelper
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.schedulers.Schedulers

object AmityVisitorUsageLimitObserver {

    private val disposable = CompositeDisposable()
    private var eventDisposable: Disposable? = null

    fun init() {
        disposable.clear()
        disposable.add(
            AmityCoreClient.observeSessionState()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnNext {
                    when (it) {
                        is SessionState.Established -> {
                            if (AmityCoreClient.isVisitor()) {
                                subscribeToEvents()
                            }
                        }

                        is SessionState.Terminated, SessionState.NotLoggedIn -> {
                            clearSubscription()
                        }

                        else -> {}
                    }
                }
                .doOnError { it.printStackTrace() }
                .subscribe()
        )
    }

    private fun subscribeToEvents() {
        clearSubscription()
        eventDisposable = AmityCoreClient.getVisitorUsageLimitEvents()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnNext {
                AmitySocialBehaviorHelper.globalBehavior.handleVisitorUsageLimitReached()
            }
            .doOnError { it.printStackTrace() }
            .subscribe()
    }

    private fun clearSubscription() {
        eventDisposable?.dispose()
        eventDisposable = null
    }
}
