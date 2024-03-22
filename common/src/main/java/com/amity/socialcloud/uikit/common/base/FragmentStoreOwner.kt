package com.amity.socialcloud.uikit.common.base

import androidx.lifecycle.ViewModelStore
import androidx.lifecycle.ViewModelStoreOwner

class FragmentStoreOwner: ViewModelStoreOwner {
    private val store = ViewModelStore()

    override val viewModelStore: ViewModelStore
        get() = store
}