package com.amity.socialcloud.uikit.common.components

import java.io.File

interface AmityAudioRecorderListener {

    fun onFileRecorded(audioFile: File?)

    fun showMessage()
}