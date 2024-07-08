package com.amity.socialcloud.uikit.common.infra.initializer

import android.annotation.SuppressLint
import android.content.Context


@SuppressLint("StaticFieldLeak")
object AmityAppContext {
    private var context: Context? = null

    fun init(context: Context) {
        if (this.context == null) {
            this.context = context.applicationContext
        }
    }

    fun getContext(): Context {
        return context ?: throw IllegalStateException("Context is not initialized")
    }
}