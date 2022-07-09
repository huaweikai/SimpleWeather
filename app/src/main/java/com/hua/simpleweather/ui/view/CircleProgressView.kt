package com.hua.simpleweather.ui.view

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.util.AttributeSet
import android.view.View
import androidx.annotation.ColorInt
import com.hua.simpleweather.utils.dp
import com.hua.simpleweather.utils.sp
import kotlin.math.max
import kotlin.math.min


class CircleProgressView @JvmOverloads constructor(
    context:Context,
    attributeSet: AttributeSet ? =null,
    defStyle:Int = 0
) :View(context,attributeSet,defStyle){
    private var circleWidth = 8.dp.toFloat()
    private val innerPaint = Paint().apply {
        strokeCap = Paint.Cap.ROUND
        strokeWidth = circleWidth
        color = Color.BLUE
        style = Paint.Style.STROKE
    }
    private val outPaint = Paint().apply {
        strokeCap = Paint.Cap.ROUND
        strokeWidth = circleWidth
        color = Color.GREEN
        style = Paint.Style.STROKE
    }

    private val textBounds = Rect()

    private val textPaint = Paint().apply {
        textSize = 16.sp
    }
    private var text = "1"

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val widthMode = MeasureSpec.getMode(widthMeasureSpec)
        val heightMode = MeasureSpec.getMode(heightMeasureSpec)
//        var widthSize = MeasureSpec.getSize(widthMeasureSpec)
//        var heightSize = MeasureSpec.getSize(heightMeasureSpec)
//        if (widthMode == MeasureSpec.AT_MOST){
//            widthSize = 150
//        }
//        if (heightMode== MeasureSpec.AT_MOST){
//            heightSize = 150
//        }
//        val size = min(widthSize,heightSize)
        val widthSize = if(widthMode == MeasureSpec.EXACTLY) MeasureSpec.getSize(widthMeasureSpec) else 100.dp
        val heightSize = if(heightMode == MeasureSpec.EXACTLY) MeasureSpec.getSize(heightMeasureSpec) else 100.dp
        val size = min(widthSize,heightSize)
        setMeasuredDimension(size,size)
    }

    private var max = 1
    private var progress = 0f
    fun setMax(max:Int){
        this.max = max
    }
    private val CRICLE_START = 135F
    fun setProgress(value:Int){
        val percent = (value.toFloat() / max)
        text = value.toString()
        progress = percent * 270f
        invalidate()
    }

    fun setColor( @ColorInt  innerColor:Int?=null,  @ColorInt  outColor: Int?=null){
        innerPaint.color = innerColor?: Color.BLUE
        outPaint.color = outColor?:Color.GREEN
        invalidate()
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        canvas?.let {
            val padding = circleWidth / 2
            it.drawArc(padding,padding,width - padding,height - padding,CRICLE_START,270f,false,outPaint)
            it.drawArc(padding,padding,width - padding,height - padding,CRICLE_START,progress,false,innerPaint)
            val fontMetrics = textPaint.fontMetricsInt
            val dy = (fontMetrics.bottom - fontMetrics.top) / 2 - fontMetrics.bottom
            val baseLineY = height / 2f + dy

            textPaint.getTextBounds(text,0,text.length,textBounds)
            val start = width / 2 - textBounds.width() / 2f
            it.drawText(text,start,baseLineY,textPaint)
        }
    }
}