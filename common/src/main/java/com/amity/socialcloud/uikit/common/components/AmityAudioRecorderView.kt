package com.amity.socialcloud.uikit.common.components

import android.content.Context
import android.graphics.Point
import android.media.MediaRecorder
import android.os.Environment
import android.os.SystemClock
import android.util.AttributeSet
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewAnimationUtils
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.databinding.DataBindingUtil
import com.amity.socialcloud.uikit.common.R
import com.amity.socialcloud.uikit.common.databinding.AmityViewAudioRecorderBinding
import org.joda.time.DateTime
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

class AmityAudioRecorderView : ConstraintLayout {

    private lateinit var mBinding: AmityViewAudioRecorderBinding
    private var initialRecordBtnPos = 0
    private var initialDltBtnPos = 0
    private var recordButtonHeight = 0
    private var mediaRecorder: MediaRecorder? = null
    private var timer: Timer? = null
    private var audioFile: File? = null
    private var mListener: AmityAudioRecorderListener? = null
    private var startTime = 0L
    private var fileSent = false

    constructor(context: Context) : super(context) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        init()
    }

    private fun init() {
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        mBinding = DataBindingUtil.inflate(inflater, R.layout.amity_view_audio_recorder, this, true)
    }

    fun setAudioRecorderListener(listener: AmityAudioRecorderListener) {
        mListener = listener
    }

    fun onTouch(event: MotionEvent?) {
        if (initialRecordBtnPos == 0 || initialDltBtnPos == 0) {
            val recordButtonPosition = getCenterPointOfView(mBinding.btnRecordAudio)
            initialRecordBtnPos = recordButtonPosition.x
            recordButtonHeight = recordButtonPosition.y

            val deleteButtonPosition = getCenterPointOfView(mBinding.btnDelete)
            initialDltBtnPos = deleteButtonPosition.x
        }
        val xPos = event?.rawX
        when (event?.action) {
            MotionEvent.ACTION_DOWN -> {
                startTime = System.currentTimeMillis()
                //stopRecording()
                startRecording()
                startChronometer()
            }
            MotionEvent.ACTION_MOVE -> {
                val currentPos = xPos?.toInt() ?: 0
                val currentHeight = event.rawY.toInt()
                val outsideVerticalBound =
                    currentHeight < recordButtonHeight - mBinding.btnRecordAudio.height / 2
                when {
                    currentPos <= initialDltBtnPos + (mBinding.btnDelete.width / 2) && !outsideVerticalBound -> {
                        mBinding.delete = true
                        mBinding.disable = true
                    }
                    else -> {
                        mBinding.delete = false
                        mBinding.disable = false
                    }
                }

                if (mBinding.delete == true) {
                    mBinding.btnDelete.layoutParams.width =
                        resources.getDimensionPixelSize(R.dimen.amity_sixty_four)
                    mBinding.btnDelete.layoutParams.height =
                        resources.getDimensionPixelSize(R.dimen.amity_sixty_four)
                    mBinding.btnDelete.requestLayout()
                } else {
                    resetDeleteButton()
                }

            }
            MotionEvent.ACTION_UP -> {
                dismissRecordingUi()
            }
        }
    }

    private fun dismissRecordingUi() {
        val recordedTime = System.currentTimeMillis() - startTime
        stopRecording(recordedTime)
        if (mBinding.delete == true) {
            audioFile?.delete()
        } else {
            sendFile(recordedTime)
        }
        mBinding.delete = false
        mBinding.disable = false
        mBinding.chronometer.stop()
        mBinding.rippleBackGround.stopRippleAnimation()
        resetDeleteButton()
    }


    private fun startChronometer() {
        var i = 0
        mBinding.chronometer.base = SystemClock.elapsedRealtime()
        mBinding.chronometer.setOnChronometerTickListener { chronometer ->
            if (chronometer.text == "0$i:00" && chronometer.text != "00:00") {
                dismissRecordingUi()
                //stopRecording()
                //sendFile()
                //startRecording()
            }
            val time = SystemClock.elapsedRealtime() - chronometer.base
            val dateTime = DateTime(time)
            val seconds = String.format("%02d", dateTime.secondOfMinute().get() % 60)
            if (seconds == "00") {
                i++
            }
            chronometer.text = context.getString(R.string.amity_time, seconds)
        }
        mBinding.chronometer.start()
    }


    private fun resetDeleteButton() {
        mBinding.btnDelete.layoutParams.width = resources.getDimensionPixelSize(R.dimen.amity_forty_eight)
        mBinding.btnDelete.layoutParams.height =
            resources.getDimensionPixelSize(R.dimen.amity_forty_eight)
        mBinding.btnDelete.requestLayout()
    }

    private fun getCenterPointOfView(view: View): Point {
        val location = IntArray(2)
        view.getLocationInWindow(location)
        val x = location[0] + view.width / 2
        val y = location[1] + view.height / 2
        return Point(x, y)
    }

    fun circularReveal() {
        val radius = mBinding.layoutRecorder.width.coerceAtLeast(mBinding.layoutRecorder.height)
        val circularReveal = ViewAnimationUtils.createCircularReveal(
            mBinding.layoutRecorder,
            mBinding.layoutRecorder.width / 2, mBinding.layoutRecorder.height, 0F, radius * 1.1F
        )
        circularReveal.duration = 1500
        circularReveal.start()
    }

    private fun startRecording() {
        fileSent = false
        mediaRecorder = MediaRecorder()
        audioFile = try {
            createAudioFile()
        } catch (ex: IOException) {
            Log.e("EkoAudioRecorderView", "createAudioFile Exception ${ex.localizedMessage}")
            null
        }
        mediaRecorder?.apply {
            setAudioSource(MediaRecorder.AudioSource.MIC)
            setOutputFormat(MediaRecorder.OutputFormat.MPEG_4)
            setAudioEncoder(MediaRecorder.AudioEncoder.AAC)
            setOutputFile(audioFile?.absolutePath)
            setAudioSamplingRate(8000)
            setAudioEncodingBitRate(8000)
            prepare()
            start()
        }
        timer = Timer()
        timer?.schedule(object : TimerTask() {
            override fun run() {
                val currentMaxAmplitude = mediaRecorder?.maxAmplitude ?: 0
                post {
                    if (currentMaxAmplitude >= 1000) {
                        mBinding.rippleBackGround.startRippleAnimation()
                    } else {
                        mBinding.rippleBackGround.stopRippleAnimation()
                    }
                }
            }
        }, 0, 500)
    }

    private fun stopRecording(recordedTime: Long) {
        if (recordedTime > 500) {
            try {
                mediaRecorder?.apply {
                    stop()
                    reset()
                    release()
                }
            } catch (ex: Exception) {
                Log.e("EkoAudioRecorderView", "stopRecording: ${ex.printStackTrace()}")
            }

        }
        mediaRecorder = null
        timer?.cancel()
        timer = null
    }

    private fun sendFile(recordedTime: Long) {

        if (recordedTime < 1000) {
            audioFile?.delete()
            try {
                mListener?.showMessage()
            } catch(e: Exception) {
                Log.e("EkoAudioRecorderView", "stopRecording: Recorded time is less than 1 second")
            }
        } else {
            if (!fileSent) {
                mListener?.onFileRecorded(audioFile)
                fileSent = true
            }

        }
    }

    @Throws(IOException::class)
    private fun createAudioFile(): File {
        // Create an audio file name
        val timeStamp: String =
            SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
        val storageDir: File? = context.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS)
        return File.createTempFile(
            "Audio_${timeStamp}_", /* prefix */
            ".mp3", /* suffix */
            storageDir /* directory */
        )
    }

}