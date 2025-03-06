package com.amity.socialcloud.uikit.common.common

import android.content.Context
import android.content.res.Resources
import android.graphics.Rect
import android.graphics.drawable.Drawable
import android.os.AsyncTask
import android.view.TouchDelegate
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.ColorRes
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.paging.PagedList
import com.amity.socialcloud.sdk.model.core.content.AmityContentFeedType
import com.amity.socialcloud.sdk.model.core.error.AmityError
import com.amity.socialcloud.uikit.common.R
import com.amity.socialcloud.uikit.common.common.views.AmityColorPaletteUtil
import com.amity.socialcloud.uikit.common.common.views.AmityColorShade
import com.amity.socialcloud.uikit.common.utils.AmityListDataSource
import com.amity.socialcloud.uikit.common.utils.AmitySafeClickListener
import com.amity.socialcloud.uikit.common.utils.AmityScaleErrorImageViewTarget
import com.bumptech.glide.Glide
import com.google.android.material.shape.CornerFamily
import com.google.android.material.shape.MaterialShapeDrawable
import com.google.android.material.shape.ShapeAppearanceModel
import com.google.android.material.snackbar.BaseTransientBottomBar
import com.google.android.material.snackbar.Snackbar
import org.joda.time.DateTime
import org.joda.time.Duration
import java.math.RoundingMode
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.concurrent.TimeUnit
import kotlin.math.ln
import kotlin.math.pow

fun String.isNotEmptyOrBlank(): Boolean {
    return this.isNotEmpty() && this.isNotBlank()
}

fun TextView.leftDrawable(drawable: Drawable) {
    this.setCompoundDrawablesRelativeWithIntrinsicBounds(drawable, null, null, null)
}

fun String.changeValue(default: String, previous: String): String {
    return if (this == default) {
        previous
    } else {
        this
    }
}

fun Boolean.changeValue(default: Boolean, previous: Boolean): Boolean {
    return if (this == default) {
        previous
    } else {
        this
    }
}

fun ImageView.loadImage(url: String?, id: Int? = null) {
    if (id != null) {
        Glide.with(this)
            .load(url)
            .centerCrop()
            .placeholder(id)
            .into(AmityScaleErrorImageViewTarget(this).error())
    } else {
        Glide.with(this)
            .load(url)
            .centerCrop()
            .placeholder(R.drawable.amity_ic_user)
            .into(AmityScaleErrorImageViewTarget(this).error())
    }

}

fun Long.readableFeedPostTime(context: Context): String {
    val diff = System.currentTimeMillis() - this
    val days = TimeUnit.MILLISECONDS.toDays(diff)
    val hours = TimeUnit.MILLISECONDS.toHours(diff)
    val minutes = TimeUnit.MILLISECONDS.toMinutes(diff)

    return when {
        days > 0 -> context.resources.getQuantityString(
            R.plurals.amity_number_of_days,
            days.toInt(),
            days
        )

        hours > 0 -> context.resources.getQuantityString(
            R.plurals.amity_number_of_hours,
            hours.toInt(),
            hours
        )

        minutes > 0 -> context.resources.getQuantityString(
            R.plurals.amity_number_of_mins,
            minutes.toInt(),
            minutes
        )

        else -> context.getString(R.string.amity_just_now)
    }
}

fun isPlurals(number: Long): Boolean {
    return number > 1
}

fun Int.readableNumber(): String {
    if (this < 1000) return "" + this
    val exp = (ln(this.toDouble()) / ln(1000.0)).toInt()
    val format = DecimalFormat("0.#")
    val value: String = format.format(this / 1000.0.pow(exp.toDouble()))
    return String.format("%s%c", value, "KMBTPE"[exp - 1])
}

fun DateTime.readableTimeDiff(): String {
    val diff = DateTime.now().millis - this.millis
    diff.let {
        val days = TimeUnit.MILLISECONDS.toDays(diff)
        val hours = TimeUnit.MILLISECONDS.toHours(diff)
        val minutes = TimeUnit.MILLISECONDS.toMinutes(diff)
        val seconds = TimeUnit.MILLISECONDS.toSeconds(diff)

        return when {
            days > 0 -> days.toString() + "d"
            hours > 0 -> hours.toString() + "h"
            minutes > 0 -> minutes.toString() + "m"
            seconds > 1 -> seconds.toString() + "s"
            else -> "Just now"
        }
    }
}

fun DateTime.readableSocialTimeDiff(): String {
    val now = DateTime.now()

    if (now.year().get() > this.year().get()) {
        val formatter = SimpleDateFormat("d MMM yyyy", Locale.getDefault())
        val formattedDateTime: String = formatter.format(this.toDate())
        return formattedDateTime
    } else {
        if (now.dayOfYear - this.dayOfYear <= 7) {
            //  within 7 days
            val diff = now.millis - this.millis
            diff.let {
                val days = TimeUnit.MILLISECONDS.toDays(diff)
                val hours = TimeUnit.MILLISECONDS.toHours(diff)
                val minutes = TimeUnit.MILLISECONDS.toMinutes(diff)

                return when {
                    days > 0 -> days.toString() + "d"
                    hours > 0 -> hours.toString() + "h"
                    minutes > 0 -> minutes.toString() + "m"
                    else -> "Just now"
                }
            }
        } else {
            val formatter = SimpleDateFormat("d MMM", Locale.getDefault())
            val formattedDateTime: String = formatter.format(this.toDate())
            return formattedDateTime
        }
    }
}

fun Int.readableMinuteSeconds(): String {
    val minutes = this / 60
    val seconds = this % 60

    return "%02d:%02d".format(minutes, seconds)
}

fun <T : Any> List<T>.toPagedList(pageSize: Int): PagedList<T> {
    val validPageSize = Math.max(pageSize, 1)
    val config = PagedList.Config.Builder()
        .setEnablePlaceholders(false)
        .setPageSize(validPageSize)
        .build()

    return PagedList.Builder(AmityListDataSource(this), config)
        .setNotifyExecutor(AmityListDataSource.AmityUiThreadExecutor())
        .setFetchExecutor(AsyncTask.THREAD_POOL_EXECUTOR)
        .build()

}

fun View.setShape(
    topLeft: Float?, bottomLeft: Float?,
    topRight: Float?, bottomRight: Float?, @ColorRes fillColor: Int?,
    @ColorRes strokeColor: Int?, colorShade: AmityColorShade?
) {

    val modal = ShapeAppearanceModel()
        .toBuilder()
    val cornerRadius = this.context.resources.getDimensionPixelSize(R.dimen.amity_eight).toFloat()
    if (topLeft == null) {
        modal.setTopLeftCorner(CornerFamily.ROUNDED, cornerRadius)
    } else {
        modal.setTopLeftCorner(CornerFamily.ROUNDED, topLeft)
    }

    if (bottomLeft == null) {
        modal.setBottomLeftCorner(CornerFamily.ROUNDED, cornerRadius)
    } else {
        modal.setBottomLeftCorner(CornerFamily.ROUNDED, bottomLeft)
    }

    if (topRight == null) {
        modal.setTopRightCorner(CornerFamily.ROUNDED, cornerRadius)
    } else {
        modal.setTopRightCorner(CornerFamily.ROUNDED, topRight)
    }

    if (bottomRight == null) {
        modal.setBottomRightCorner(CornerFamily.ROUNDED, cornerRadius)
    } else {
        modal.setBottomRightCorner(CornerFamily.ROUNDED, bottomRight)
    }

    val shapeDrawable = MaterialShapeDrawable(modal.build())

    if (fillColor == null) {
        shapeDrawable.fillColor =
            ContextCompat.getColorStateList(this.context, R.color.amityColorWhite)
    } else {
        if (colorShade == null)
            shapeDrawable.fillColor = ContextCompat.getColorStateList(this.context, fillColor)
        else {
            shapeDrawable.setTint(
                AmityColorPaletteUtil.getColor(
                    ContextCompat.getColor(
                        this.context,
                        fillColor
                    ), colorShade
                )
            )
        }


        if (strokeColor == null) {
            if (colorShade == null) {
                shapeDrawable.setStroke(2F, ContextCompat.getColor(this.context, fillColor))
            } else {
                shapeDrawable.setStroke(
                    2F, AmityColorPaletteUtil.getColor(
                        ContextCompat.getColor(
                            this.context,
                            fillColor
                        ), colorShade
                    )
                )
            }

        } else {
            if (colorShade == null) {
                shapeDrawable.setStroke(2F, ContextCompat.getColor(this.context, strokeColor))
            } else {
                shapeDrawable.setStroke(
                    2F, AmityColorPaletteUtil.getColor(
                        ContextCompat.getColor(
                            this.context,
                            strokeColor
                        ), colorShade
                    )
                )
            }

        }

        ViewCompat.setBackground(this, shapeDrawable)

    }
}

fun View.toCircularShape(fillColor: Int, strokeWidth: Float? = null) {
    val modal = ShapeAppearanceModel()
        .toBuilder()
        .setAllCorners(
            CornerFamily.ROUNDED,
            this.context.resources.getDimensionPixelSize(R.dimen.amity_thirty_two).toFloat()
        )
    val shapeDrawable = MaterialShapeDrawable(modal.build())
    shapeDrawable.setTint(fillColor)
    if (strokeWidth != null) {
        shapeDrawable.setStroke(
            strokeWidth,
            ContextCompat.getColor(this.context, R.color.amityColorWhite)
        )
    }
    ViewCompat.setBackground(this, shapeDrawable)
}

fun View.setBackgroundColor(color: Int?, colorShade: AmityColorShade?) {
    val shade = colorShade ?: AmityColorShade.DEFAULT
    val backgroundColor = color ?: ContextCompat.getColor(this.context, R.color.amityColorBase)
    this.setBackgroundColor(AmityColorPaletteUtil.getColor(backgroundColor, shade))
}

fun Double.formatCount(): String {
    val suffixChars = "KMGTPE"
    val formatter = DecimalFormat("###.#")
    formatter.roundingMode = RoundingMode.DOWN
    return if (this < 1000.0) {
        formatter.format(this)
    } else {
        val exp = (ln(this) / ln(1000.0)).toInt()
        formatter.format(this / 1000.0.pow(exp.toDouble())) + suffixChars[exp - 1]
    }
}

fun DateTime.readableTimeLeft(startTime: DateTime = DateTime.now()): String {

    if (this.isBefore(startTime) || this == startTime) {
        return "0 m left"
    }

    val duration = Duration(startTime, this)

    // Calculate total hours and days
    val totalHours = duration.standardHours
    val totalMinutes = duration.standardMinutes
    val totalDays = duration.standardDays

    return when {
        totalDays > 0 -> {
            // Round up to the next day if hours are greater than 0
            val daysLeft = if (duration.standardHours % 24 > 0) totalDays + 1 else totalDays
            "${daysLeft}d left"
        }
        totalHours > 0 -> {
            "${totalHours}h left"
        }
        totalMinutes > 0 -> {
            "${totalMinutes}m left"
        }
        else -> {
            "0m left" // In case the target time is in the past or very close to now
        }
    }
}

fun View.expandViewHitArea(): View? {
    val parent = (this.parent as? View)
    parent?.post {
        val parentRect = Rect()
        val childRect = Rect()
        parent.getHitRect(parentRect)
        this.getHitRect(childRect)

        childRect.left -= 4
        childRect.top -= 4
        childRect.right += 4
        childRect.bottom += 4
        parent.touchDelegate = TouchDelegate(childRect, this)
    }
    return parent
}

fun Int.toDp(): Int = (this / Resources.getSystem().displayMetrics.density).toInt()

fun View.showSnackBar(
    msg: String,
    @BaseTransientBottomBar.Duration duration: Int = Snackbar.LENGTH_LONG
) {
    Snackbar.make(this, msg, duration).show()
}

fun View.setSafeOnClickListener(onSafeClick: (View) -> Unit) {
    val safeClickListener = AmitySafeClickListener {
        onSafeClick(it)
    }
    setOnClickListener(safeClickListener)
}

fun Throwable.toImageUploadError(): String {
    if(AmityError.from(this) == AmityError.FILE_SIZE_EXCEEDED) {
        return "Oops, photo can be maxed 30 MB"
    }

    val errorMessage = this.message ?: "Oops, photo upload failed"
    val localizedMessages = mapOf(
        Regex("""Image uploading failed: Image size in bytes: \d+ is more than the maximum limit""")
                to "Oops, photo can be maxed 30 MB",
        Regex("""Image uploading failed: Request has invalid image format""")
                to "Oops, photo formats accepted: JPG and PNG",
    )

    for ((pattern, localizedMessage) in localizedMessages) {
        if (pattern.matches(errorMessage)) {
            return localizedMessage
        }
    }

    return errorMessage
}

fun Throwable.toVideoUploadError(contentType: AmityContentFeedType): String {
    if(AmityError.from(this) == AmityError.FILE_SIZE_EXCEEDED) {
        return "Oops, video can be maxed 1 GB"
    }

    val maxDuration = if(contentType == AmityContentFeedType.STORY) 15 else 7200

    val errorMessage = this.message ?: "Oops, video upload failed"
    val localizedMessages = mapOf(
        Regex("""Failed to upload video. Video duration exceeds the maximum limit of \d+s\.""")
                to "Oops, video can be maxed $maxDuration seconds",
        Regex("""Video uploading failed: Video size in bytes: \d+ is more than the maximum limit""")
                to "Oops, video can be maxed 1 GB",
        Regex("""Video uploading failed: Request has invalid video format""")
                to "Oops, video formats accepted: 3gp, avi, f4v, flv, m4v, mov, mp4, ogv, 3g2, wmv, vob, webm, and mkv",
    )

    for ((pattern, localizedMessage) in localizedMessages) {
        if (pattern.matches(errorMessage)) {
            return localizedMessage
        }
    }
    return errorMessage
}