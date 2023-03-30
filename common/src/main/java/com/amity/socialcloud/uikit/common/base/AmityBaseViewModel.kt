package com.amity.socialcloud.uikit.common.base

import androidx.databinding.Observable
import androidx.lifecycle.ViewModel
import com.amity.socialcloud.sdk.api.core.AmityCoreClient
import com.amity.socialcloud.sdk.model.core.permission.AmityPermission
import com.amity.socialcloud.uikit.common.model.AmityEventIdentifier
import com.amity.socialcloud.uikit.common.model.AmityEventType
import com.amity.socialcloud.uikit.common.utils.AmityEvent
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.disposables.Disposable

/**
 * Base viewModel to be extended by all viewModels of application.
 * @author sumitlakra
 * @date 06/01/2020
 */
open class AmityBaseViewModel : ViewModel() {

    protected val compositeDisposable: CompositeDisposable by lazy {
        CompositeDisposable()
    }

    val onAmityEventReceived: AmityEvent<AmityEventType> = AmityEvent()

    //TODO Refactor permission function move to other class
    fun hasPermissionAtCommunity(
        permission: AmityPermission,
        communityId: String
    ): Flowable<Boolean> {
        return AmityCoreClient.hasPermission(permission).atCommunity(communityId).check()
    }

    fun checkModeratorPermissionAtChannel(
        permission: AmityPermission,
        channelId: String
    ): Flowable<Boolean> {
        return AmityCoreClient.hasPermission(permission).atChannel(channelId)
            .check()
    }

    fun checkPermissionAtGlobal(permission: AmityPermission): Flowable<Boolean> {
        return AmityCoreClient.hasPermission(permission).atGlobal().check()
    }

    /**
     * Function to be used by child view models to trigger any event
     * @author sumitlakra
     * @date 06/01/2020
     */
    fun triggerEvent(type: AmityEventIdentifier, dataObj: Any = "") {
        val eventType = AmityEventType(type, dataObj)
        onAmityEventReceived(eventType)
    }

    /**
     * define a property change callback which calls "callback " on change
     * @return Unit
     * @author sumitlakra
     * @date 06/01/2020
     */
    @Suppress("UNCHECKED_CAST")
    fun <T : Observable> T.addOnPropertyChanged(callback: (T) -> Unit) =
        object : Observable.OnPropertyChangedCallback() {
            @Suppress("UNCHECKED_CAST")
            override fun onPropertyChanged(observable: Observable?, i: Int) =
                callback(observable as T)
        }.also { addOnPropertyChangedCallback(it) }

    /**
     * add disposable to [compositeDisposable] to dispose later
     * @param [disposable]
     * @author sumitlakra
     * @date 08/11/2020
     */
    fun addDisposable(disposable: Disposable) {
        compositeDisposable.add(disposable)
    }

    override fun onCleared() {
        compositeDisposable.dispose()
        super.onCleared()
    }
}