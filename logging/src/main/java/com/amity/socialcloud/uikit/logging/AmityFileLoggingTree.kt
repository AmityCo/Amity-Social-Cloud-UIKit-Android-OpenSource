package com.amity.socialcloud.uikit.logging

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.os.Environment
import android.util.Log
import timber.log.Timber
import java.io.File
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

class AmityFileLoggingTree(private val context: Context) : Timber.Tree() {
    private val fileNameTag = AmityFileLoggingTree::class.java.simpleName

    companion object {
        var logFile: File? = null

        fun getLogFilePath(): String = logFile?.absolutePath ?: "Log File not created yet"
    }

    init {
        if (Thread.getDefaultUncaughtExceptionHandler() !is AmityExceptionHandler) {
            Thread.setDefaultUncaughtExceptionHandler(AmityExceptionHandler())
        }
    }

    @SuppressLint("LogNotTimber")
    override fun log(priority: Int, tag: String?, message: String, t: Throwable?) {
        try {
            logFile = createLogFile()

            val logTimeStamp = SimpleDateFormat(
                "E MMM dd yyyy 'at' hh:mm:ss:SSS aaa",
                Locale.getDefault()
            ).format(Date())
            val priorityString: String = if (t != null) {
                "EXCEPTION"
            } else {
                priorityString(priority)
            }
            writeLogsToFile(logFile, logTimeStamp, tag, priorityString, message)
            showLogsOnTerminal(logTimeStamp, tag, priority, message)
        } catch (e: FileNotFoundException) {
            Log.e(fileNameTag, "File not Found Exception : $e")
        } catch (e: IOException) {
            Log.e(fileNameTag, "IO Exception : $e")
        } catch (e: IllegalArgumentException) {
            Log.e(fileNameTag, "permission error : $e")
        }
    }

    @SuppressLint("LogNotTimber")
    fun createLogFile(): File {
        val separator = File.separator
        val logFolderName = AmityLoggingConfig.LOG_FOLDER_NAME
        val folder: File? = getLogFolder()

        val direct = File(folder?.absolutePath, logFolderName)
        if (!direct.exists()) {
            if (direct.mkdirs())
                Log.d(fileNameTag, "Directory created successfully")
            else
                Log.d(fileNameTag, "error while creating Directory")
        }

        val fileNameTimeStamp = SimpleDateFormat(
            "dd-MM-yyyy",
            Locale.getDefault()
        ).format(Date())

        val fileName = "$fileNameTimeStamp.txt"
        val file = File("$direct$separator$fileName")

        file.createNewFile()
        return file
    }

    private fun getLogFolder(): File? {
        return if (isStoragePermissionGranted()) {
            context.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS)
        } else {
            context.cacheDir
        }
    }

    private fun isStoragePermissionGranted(): Boolean =
        context.checkCallingOrSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED


    @SuppressLint("LogNotTimber")
    fun showLogsOnTerminal(logTimeStamp: String, tag: String?, priority: Int, message: String) {
        if (BuildConfig.DEBUG) {
            val msgStr = "$logTimeStamp - $message"
            when (priority) {
                Log.VERBOSE -> Log.v(tag, msgStr)
                Log.DEBUG -> Log.d(tag, msgStr)
                Log.INFO -> Log.i(tag, msgStr)
                Log.WARN -> Log.w(tag, msgStr)
                Log.ERROR -> Log.e(tag, msgStr)
                else -> Log.e(tag, msgStr)
            }
        }
    }

    private fun writeLogsToFile(
        file: File?,
        logTimeStamp: String,
        tag: String?,
        priorityString: String,
        message: String
    ) {
        if (file?.exists() == true) {

            val fileOutputStream = FileOutputStream(file, true)
            val formattedMessage = "$priorityString: $logTimeStamp: $tag: $message\n"
            fileOutputStream.write(formattedMessage.toByteArray())
            fileOutputStream.close()
        }
    }

    fun priorityString(priority: Int): String = when (priority) {
        Log.VERBOSE -> AmityLoggingConfig.VERBOSE
        Log.DEBUG -> AmityLoggingConfig.DEBUG
        Log.INFO -> AmityLoggingConfig.INFO
        Log.WARN -> AmityLoggingConfig.WARN
        Log.ERROR -> AmityLoggingConfig.ERROR
        else -> AmityLoggingConfig.ERROR
    }

}