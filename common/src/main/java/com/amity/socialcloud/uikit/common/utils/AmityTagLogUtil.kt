package com.amity.socialcloud.uikit.common.utils

import timber.log.Timber

fun getCurrentClassAndMethodNames(): String {
    val e = Thread.currentThread().stackTrace[3]
    val s = e.className
    return "<where> " + s.substring(s.lastIndexOf('.') + 1, s.length) + "." + e.methodName
}

fun Throwable.logThrowable() {
    Timber.d("${getCurrentClassAndMethodNames()}$this")
}