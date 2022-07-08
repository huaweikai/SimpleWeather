package com.hua.simpleweather.ui.view

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.util.Log
import android.view.View
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.ViewCompat
import com.hua.material.materialcolor.ColorContainerData
import com.hua.simpleweather.R
import com.hua.simpleweather.utils.valueToPx
import kotlin.math.abs
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.tan

class SunView @JvmOverloads constructor(
    context: Context,
    attributeSet: AttributeSet? = null,
    defStyle: Int = 0
) : View(context, attributeSet, defStyle) {

    private val mMargin = 16.valueToPx(context)
    private val SUN_ANGLE = 135f

    private val iconSize = 24.valueToPx(context)

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val width: Int = (MeasureSpec.getSize(widthMeasureSpec) - 2 * mMargin).toInt()
        val deltaRadians =
            Math.toRadians((180 - SUN_ANGLE) / 2.0)
        val radius = (width / 2 / cos(deltaRadians)).toInt()
        val height = (radius - width / 2 * tan(deltaRadians)).toInt()
        setMeasuredDimension(
            MeasureSpec.makeMeasureSpec((width + 2 * mMargin).toInt(), MeasureSpec.EXACTLY),
            MeasureSpec.makeMeasureSpec((height + 2 * mMargin).toInt(), MeasureSpec.EXACTLY)
        )
        val centerX = measuredWidth / 2
        val centerY = (mMargin + radius).toInt()
        mRectF.set(
            (centerX - radius).toFloat(),
            (centerY - radius).toFloat(),
            (centerX + radius).toFloat(),
            (centerY + radius).toFloat()
        )
    }

    private val mEffect = DashPathEffect(
        floatArrayOf(
            3.valueToPx(context),
            2 * 3.valueToPx(context)
        ), 0f
    )

    private val linePaint = Paint().apply {
        style = Paint.Style.STROKE
        strokeWidth = 1.valueToPx(context)
        pathEffect = mEffect
    }

    private val pathPaint = Paint().apply {
        style = Paint.Style.STROKE
        strokeWidth = 5.valueToPx(context)
        strokeCap = Paint.Cap.ROUND
    }

    private val mRectF: RectF = RectF()

    fun setColor(colorData: ColorContainerData) {
        pathPaint.color = colorData.primaryColor
        linePaint.color = colorData.containerColor
        ViewCompat.postInvalidateOnAnimation(this)
    }


    private var progresses = -1L

    private var max = 100L

    private var currentTime = 0L

    private var endTime = 1L

    private var startTime = 1L

    private val iconPosition = floatArrayOf(0f, 0f)

    private val iconRotation: FloatArray = floatArrayOf(0f, 0f)


    fun setTime(
        startTime: Long,
        endTime: Long,
        currentTime: Long
    ) {
        this.startTime = startTime
        this.endTime = endTime
        this.currentTime = currentTime
        ViewCompat.postInvalidateOnAnimation(this)
    }

    private fun ensureProgress() {
        //计算进度
        max = endTime - startTime
        progresses = currentTime - startTime
        progresses = progresses.coerceAtLeast(0)
        progresses = progresses.coerceAtMost(max)
    }

    private val drawable = ResourcesCompat.getDrawable(resources, R.drawable.ic_sun, null)?.apply {
        bounds = Rect(0, 0, iconSize.toInt(), iconSize.toInt())
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        canvas?.let {
            countAngle()
            val startAngle: Float = 270 - SUN_ANGLE / 2f
            val sweepAngle = (1f * progresses / max) * SUN_ANGLE
            it.drawArc(mRectF, 270 - SUN_ANGLE / 2f, SUN_ANGLE, false, linePaint)
            it.drawLine(
                mMargin,
                measuredHeight - mMargin,
                measuredWidth - mMargin,
                measuredHeight - mMargin,
                linePaint
            )
            it.drawArc(mRectF, startAngle, sweepAngle, false, pathPaint)

            it.save()
            it.translate(iconPosition[0], iconPosition[1])
            it.rotate(iconRotation[0], iconSize / 2f, iconSize / 2f)
            drawable?.draw(it)
            it.restore()
        }
    }

    private fun countAngle() {
        ensureProgress()
        val startAngle: Float = 270 - SUN_ANGLE / 2f
        val progressSweepAngle = 1f * progresses / max * SUN_ANGLE
        val progressEndAngle = startAngle + progressSweepAngle
        val deltaAngle = progressEndAngle - 180
        val deltaWidth =
            abs(mRectF.width() / 2 * cos(Math.toRadians(deltaAngle.toDouble()))).toFloat()
        val deltaHeight =
            abs(mRectF.width() / 2 * sin(Math.toRadians(deltaAngle.toDouble()))).toFloat()

        if (progressEndAngle < 270) {
            iconPosition[0] = mRectF.centerX() - deltaWidth - iconSize / 2f
        } else {
            iconPosition[0] = mRectF.centerX() + deltaWidth - iconSize / 2f
        }
        iconPosition[1] = mRectF.centerY() - deltaHeight - iconSize / 2f
    }

}