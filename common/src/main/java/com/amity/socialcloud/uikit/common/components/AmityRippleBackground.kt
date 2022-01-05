package com.amity.socialcloud.uikit.common.components

import android.animation.Animator
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
import android.widget.RelativeLayout
import com.amity.socialcloud.uikit.common.R
import java.util.*

class AmityRippleBackground : RelativeLayout {
    private var rippleStrokeWidth = 0f
    private lateinit var paint: Paint
    var isRippleAnimationRunning = false
    private lateinit var animatorSet: AnimatorSet
    private val rippleViewList: ArrayList<RippleView> = arrayListOf()

    constructor(context: Context?) : super(context) {}
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        init(context, attrs)
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        init(context, attrs)
    }

    companion object {
        private const val DEFAULT_RIPPLE_COUNT = 6
        private const val DEFAULT_DURATION_TIME = 3000
        private const val DEFAULT_SCALE = 6.0f
        private const val DEFAULT_FILL_TYPE = 0
    }

    private fun init(context: Context, attrs: AttributeSet?) {
        if (isInEditMode) return
        requireNotNull(attrs) { "Attributes should be provided to this view," }
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.AmityRippleBackground)
        val rippleColor = typedArray.getColor(
            R.styleable.AmityRippleBackground_amityRbColor,
            resources.getColor(R.color.amityColorBase)
        )
        rippleStrokeWidth = typedArray.getDimension(
            R.styleable.AmityRippleBackground_amityRbStrokeWidth,
            resources.getDimension(R.dimen.amity_two)
        )
        val rippleRadius = typedArray.getDimension(
            R.styleable.AmityRippleBackground_amityRbRadius,
            resources.getDimension(R.dimen.amity_sixty_four)
        )
        val rippleDurationTime =
            typedArray.getInt(R.styleable.AmityRippleBackground_amityRbDuration, DEFAULT_DURATION_TIME)
        val rippleAmount =
            typedArray.getInt(R.styleable.AmityRippleBackground_amityRbRippleAmount, DEFAULT_RIPPLE_COUNT)
        val rippleScale =
            typedArray.getFloat(R.styleable.AmityRippleBackground_amityRbScale, DEFAULT_SCALE)
        val rippleType =
            typedArray.getInt(R.styleable.AmityRippleBackground_amityRbType, DEFAULT_FILL_TYPE)
        typedArray.recycle()
        val rippleDelay = rippleDurationTime / rippleAmount

        paint = Paint()
        paint.isAntiAlias = true
        if (rippleType == DEFAULT_FILL_TYPE) {
            rippleStrokeWidth = 0f
            paint.style = Paint.Style.FILL
        } else paint.style = Paint.Style.STROKE
        paint.color = rippleColor

        val rippleParams = LayoutParams(
            (2 * (rippleRadius + rippleStrokeWidth)).toInt(),
            (2 * (rippleRadius + rippleStrokeWidth)).toInt()
        )

        rippleParams.addRule(ALIGN_PARENT_BOTTOM)
        rippleParams.addRule(CENTER_HORIZONTAL)
        animatorSet = AnimatorSet()
        animatorSet.interpolator = AccelerateDecelerateInterpolator()

        val animatorList = ArrayList<Animator>()
        for (i in 0 until rippleAmount) {
            val rippleView = RippleView(getContext())
            addView(rippleView, rippleParams)
            rippleViewList.add(rippleView)
            val scaleXAnimator = ObjectAnimator.ofFloat(rippleView, "ScaleX", 1.0f, rippleScale)
            scaleXAnimator.repeatCount = ObjectAnimator.INFINITE
            scaleXAnimator.repeatMode = ObjectAnimator.RESTART
            scaleXAnimator.startDelay = i * rippleDelay.toLong()
            scaleXAnimator.duration = rippleDurationTime.toLong()
            animatorList.add(scaleXAnimator)
            val scaleYAnimator = ObjectAnimator.ofFloat(rippleView, "ScaleY", 1.0f, rippleScale)
            scaleYAnimator.repeatCount = ObjectAnimator.INFINITE
            scaleYAnimator.repeatMode = ObjectAnimator.RESTART
            scaleYAnimator.startDelay = i * rippleDelay.toLong()
            scaleYAnimator.duration = rippleDurationTime.toLong()
            animatorList.add(scaleYAnimator)
            val alphaAnimator = ObjectAnimator.ofFloat(rippleView, "Alpha", 1.0f, 0f)
            alphaAnimator.repeatCount = ObjectAnimator.INFINITE
            alphaAnimator.repeatMode = ObjectAnimator.RESTART
            alphaAnimator.startDelay = i * rippleDelay.toLong()
            alphaAnimator.duration = rippleDurationTime.toLong()
            animatorList.add(alphaAnimator)
        }
        animatorSet.playTogether(animatorList)
    }

    private inner class RippleView(context: Context?) : View(context) {
        override fun onDraw(canvas: Canvas) {
            val radius = width.coerceAtMost(height) / 2
            canvas.drawCircle(
                radius.toFloat(),
                radius.toFloat(),
                100F,
                paint
            )
        }

        init {
            this.visibility = INVISIBLE
        }
    }

    fun startRippleAnimation() {
        if (!isRippleAnimationRunning) {
            for (rippleView in rippleViewList) {
                rippleView.visibility = VISIBLE
            }
            animatorSet.start()
            isRippleAnimationRunning = true
        }
    }

    fun stopRippleAnimation() {
        if (isRippleAnimationRunning) {
            animatorSet.end()
            isRippleAnimationRunning = false
        }
    }

}