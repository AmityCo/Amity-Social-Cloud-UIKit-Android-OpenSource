package com.amity.socialcloud.uikit.common.utils

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer

class AmitySingleLiveData<T> : MutableLiveData<T?>() {

    override fun observe(owner: LifecycleOwner, observer: Observer<in T?>) {
        super.observe(owner, Observer { t ->
            if (t != null) {
                observer.onChanged(t)
                postValue(null)
            }
        })
    }
}