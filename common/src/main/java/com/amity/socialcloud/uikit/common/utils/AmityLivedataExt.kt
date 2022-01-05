package com.amity.socialcloud.uikit.common.utils

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import timber.log.Timber

fun <T> LiveData<T>.observeNotNull(lifecycleOwner: LifecycleOwner, render: (T) -> Unit) {
    observe(lifecycleOwner, Observer {
        if (it != null) render(it)
        else Timber.w("observeNotNull is null")
    })
}

fun <T> LiveData<T>.observeOnce(observer: (T) -> Unit) {
    observeForever(object : Observer<T> {
        override fun onChanged(value: T) {
            removeObserver(this)
            observer(value)
        }
    })
}

fun <T> LiveData<T>.observeOnce(owner: LifecycleOwner, observer: (T) -> Unit) {
    observe(owner, Observer<T> { value ->
        removeObservers(owner)
        observer(value)
    })
}

fun <T> MutableLiveData<T>.notifyObserver() {
    this.value = this.value
}