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
        //获取需要画的园的半径
        val radius = (width / 2 / cos(deltaRadians)).toInt()
        //对半径计算出，最终view的高度
        val height = (radius - width / 2 * tan(deltaRadians)).toInt()
        setMeasuredDimension(
            MeasureSpec.makeMeasureSpec((width + 2 * mMargin).toInt(), MeasureSpec.EXACTLY),
            MeasureSpec.makeMeasureSpec((height + 2 * mMargin).toInt(), MeasureSpec.EXACTLY)
        )
        //计算画圆所需要的rect
        val centerX = measuredWidth / 2
        val centerY = (mMargin + radius).toInt()
        mRectF.set(
            (centerX - radius).toFloat(),
            (centerY - radius).toFloat(),
            (centerX + radius).toFloat(),
            (centerY + radius).toFloat()
        )
    }
    //虚线
    private val mEffect = DashPathEffect(
        floatArrayOf(
            3.valueToPx(context),
            2 * 3.valueToPx(context)
        ), 0f
    )
    //虚线的paint
    private val linePaint = Paint().apply {
        style = Paint.Style.STROKE
        strokeWidth = 1.valueToPx(context)
        pathEffect = mEffect
    }
    //实线的paint
    private val pathPaint = Paint().apply {
        style = Paint.Style.STROKE
        strokeWidth = 5.valueToPx(context)
        strokeCap = Paint.Cap.ROUND
    }

    private val mRectF: RectF = RectF()
    //设置实线虚线的颜色
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



    fun setTime(
        startTime: Long,
        endTime: Long,
        currentTime: Long
    ) {
        //设置时间
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

    //太阳的drawable，并设置其所占大小
    private val drawable = ResourcesCompat.getDrawable(resources, R.drawable.ic_sun, null)?.apply {
        bounds = Rect(0, 0, iconSize.toInt(), iconSize.toInt())
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        canvas?.let {
            countAngle()
            val startAngle: Float = 270 - SUN_ANGLE / 2f
            val sweepAngle = (1f * progresses / max) * SUN_ANGLE
            //半圆的虚线
            it.drawArc(mRectF, startAngle, SUN_ANGLE, false, linePaint)
            //下面的虚线
            it.drawLine(
                mMargin,
                measuredHeight - mMargin,
                measuredWidth - mMargin,
                measuredHeight - mMargin,
                linePaint
            )
            //画圆
            it.drawArc(mRectF, startAngle, sweepAngle, false, pathPaint)

            it.save()
            it.translate(iconPosition[0], iconPosition[1])
            drawable?.draw(it)
            it.restore()
        }
    }

    //计算太阳的坐标
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