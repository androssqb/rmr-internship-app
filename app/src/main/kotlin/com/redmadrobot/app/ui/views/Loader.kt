package com.redmadrobot.app.ui.views

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
import android.view.View
import android.view.animation.LinearInterpolator
import androidx.core.content.ContextCompat
import androidx.core.graphics.ColorUtils
import com.redmadrobot.app.R
import kotlin.properties.Delegates

class Loader @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
) : View(context, attrs, defStyleAttr) {

    private companion object {
        const val DEFAULT_FIRST_COLOR: Int = R.color.light_grey_blue
        const val DEFAULT_SECOND_COLOR: Int = R.color.grey
        const val DEFAULT_THIRD_COLOR: Int = R.color.middle_grey
        const val DEFAULT_FOURTH_COLOR: Int = R.color.dark_grey
        const val DEFAULT_ANIM_DURATION: Long = 1_200L
        const val MAX_OFFSET_DENOMINATOR: Float = 4f
        const val FRACTION_OF_ANIMATION_CYCLE: Float = 0.5f
    }

    private val defaultViewSide = resources.getDimensionPixelSize(R.dimen.loader_default_side)

    private val innerSpace = resources.getDimension(R.dimen.loader_default_inner_space)
    private val maxRectOffset = innerSpace / MAX_OFFSET_DENOMINATOR

    private val radius = resources.getDimension(R.dimen.loader_default_rect_corner_radius)

    private val rectList = mutableListOf<LoaderRect>()

    private val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.FILL
    }

    private val loaderFirstColor: Int = ContextCompat.getColor(context, DEFAULT_FIRST_COLOR)
    private val loaderSecondColor: Int = ContextCompat.getColor(context, DEFAULT_SECOND_COLOR)
    private val loaderThirdColor: Int = ContextCompat.getColor(context, DEFAULT_THIRD_COLOR)
    private val loaderFourthColor: Int = ContextCompat.getColor(context, DEFAULT_FOURTH_COLOR)

    private val animator = ValueAnimator.ofFloat(0f, 1f).apply {
        addUpdateListener {
            val fraction = it.animatedFraction
            val rectOffset = calculateRectOffset(fraction)
            rectList.forEach { rect ->
                rect.color = blendCurrentColors(rect.colorQueue, fraction)
                rect.moveRect(rectOffset)
            }
            invalidate()
        }
        duration = DEFAULT_ANIM_DURATION
        repeatCount = ValueAnimator.INFINITE
        interpolator = LinearInterpolator()
    }

    init {
        initRectList()
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        animator.start()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val width = getSize(widthMeasureSpec)
        val height = getSize(heightMeasureSpec)

        val measureViewSide = minOf(width, height)
        setMeasuredDimension(measureViewSide, measureViewSide)

        rectList.forEach { loaderRect ->
            with(loaderRect) {
                rectOffsetCoordinate = measureViewSide / 2f + maxRectOffset
                setDimensions(measureViewSide.toFloat(), (measureViewSide - innerSpace) / 2f)
            }
        }
    }

    override fun onDetachedFromWindow() {
        animator.cancel()
        super.onDetachedFromWindow()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        rectList.forEach {
            paint.color = it.color
            canvas.drawRoundRect(it, radius, radius, paint)
        }
    }

    private fun blendCurrentColors(colorQueue: List<Int>, fraction: Float): Int {
        val animationStepSize = 1f / colorQueue.size
        val animationStep =
            (fraction / animationStepSize).toInt().coerceAtMost(colorQueue.lastIndex)

        val startColor = colorQueue[animationStep]
        val endColor = colorQueue[(animationStep + 1) % colorQueue.size]
        val ratio = if (fraction < 1f) fraction % animationStepSize / animationStepSize else 1f
        return ColorUtils.blendARGB(startColor, endColor, ratio)
    }

    private fun calculateRectOffset(fraction: Float): Float {
        val coordinateMultiplier = if (fraction < FRACTION_OF_ANIMATION_CYCLE) fraction * 2f else (1 - fraction) * 2f
        return maxRectOffset * coordinateMultiplier
    }

    private fun initRectList() {
        RectPosition.values().forEach { rectList.add(LoaderRect(it)) }
    }

    private fun getSize(measureSpec: Int): Int {
        val mode = MeasureSpec.getMode(measureSpec)
        val size = MeasureSpec.getSize(measureSpec)
        return when (mode) {
            MeasureSpec.UNSPECIFIED -> defaultViewSide
            MeasureSpec.EXACTLY -> size
            else -> minOf(defaultViewSide, size)
        }
    }

    enum class RectPosition {
        TOP_LEFT, TOP_RIGHT, BOTTOM_RIGHT, BOTTOM_LEFT
    }

    inner class LoaderRect(private val position: RectPosition) : RectF() {
        var color by Delegates.notNull<Int>()
        var rectOffsetCoordinate: Float = 0f

        val colorQueue: List<Int> = when (position) {
            RectPosition.TOP_LEFT ->
                listOf(loaderFirstColor, loaderFourthColor, loaderThirdColor, loaderSecondColor)

            RectPosition.TOP_RIGHT ->
                listOf(loaderSecondColor, loaderFirstColor, loaderFourthColor, loaderThirdColor)

            RectPosition.BOTTOM_RIGHT ->
                listOf(loaderThirdColor, loaderSecondColor, loaderFirstColor, loaderFourthColor)

            RectPosition.BOTTOM_LEFT ->
                listOf(loaderFourthColor, loaderThirdColor, loaderSecondColor, loaderFirstColor)
        }

        fun setDimensions(viewSide: Float, rectSide: Float) {
            when (position) {
                RectPosition.TOP_LEFT -> set(0f, 0f, rectSide, rectSide)
                RectPosition.TOP_RIGHT -> set(viewSide - rectSide, 0f, viewSide, rectSide)
                RectPosition.BOTTOM_RIGHT ->
                    set(viewSide - rectSide, viewSide - rectSide, viewSide, viewSide)
                RectPosition.BOTTOM_LEFT -> set(0f, viewSide - rectSide, rectSide, viewSide)
            }
        }

        fun moveRect(rectOffset: Float) {
            when (position) {
                RectPosition.TOP_LEFT -> offsetTo(rectOffset, rectOffset)
                RectPosition.TOP_RIGHT -> offsetTo(rectOffsetCoordinate - rectOffset, rectOffset)
                RectPosition.BOTTOM_RIGHT -> offsetTo(
                    rectOffsetCoordinate - rectOffset,
                    rectOffsetCoordinate - rectOffset
                )
                RectPosition.BOTTOM_LEFT -> offsetTo(rectOffset, rectOffsetCoordinate - rectOffset)
            }
        }
    }
}
