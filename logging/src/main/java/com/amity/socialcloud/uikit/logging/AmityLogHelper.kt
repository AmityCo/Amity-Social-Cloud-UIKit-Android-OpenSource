package com.amity.socialcloud.uikit.logging

import android.content.Context
import timber.log.Timber

interface ILogger {

    fun setupTimberLogging(context: Context)

    @Suppress("FunctionMinLength")
    fun d(msg: String)

    @Suppress("FunctionMinLength")
    fun i(msg: String)

    @Suppress("FunctionMinLength")
    fun w(msg: String)

    @Suppress("FunctionMinLength")
    fun e(msg: String)

    @Suppress("FunctionMinLength")
    fun ex(e: Throwable)

}

object LogHelper : ILogger {

    override fun setupTimberLogging(context: Context) = Timber.plant(AmityFileLoggingTree(context))

    @Suppress("FunctionMinLength")
    override fun d(msg: String) {
        log(LogType.DEBUG, msg)
    }

    @Suppress("FunctionMinLength")
    override fun i(msg: String) {
        log(LogType.INFO, msg)
    }

    @Suppress("FunctionMinLength")
    override fun w(msg: String) {
        log(LogType.WARN, msg)
    }

    @Suppress("FunctionMinLength")
    override fun e(msg: String) {
        log(LogType.ERROR, msg)
    }

    @Suppress("FunctionMinLength")
    override fun ex(e: Throwable) {
        Timber.e(e, "Exception")
    }

    private fun log(type: LogType, msg: String) {
        when (type) {
            LogType.DEBUG -> Timber.d(msg)
            LogType.INFO -> Timber.i(msg)
            LogType.WARN -> Timber.w(msg)
            LogType.ERROR -> Timber.e(msg)
        }
    }

    enum class LogType {
        DEBUG,
        INFO,
        WARN,
        ERROR
    }

    fun getCrashLogFilePath(): String = AmityFileLoggingTree.getLogFilePath()
}