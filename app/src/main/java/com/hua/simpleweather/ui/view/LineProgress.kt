package com.hua.simpleweather.ui.view

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
import android.view.View
import com.hua.simpleweather.utils.dp

class LineProgress @JvmOverloads constructor(
    context: Context,
    attributeSet: AttributeSet ?= null,
    defStyle:Int = 0
):View(context,attributeSet,defStyle) {
    private val linePaint = Paint().apply {
        style = Paint.Style.FILL
        isAntiAlias = true
        color = Color.GREEN
    }

    private val progressPaint = Paint().apply {
        style = Paint.Style.FILL
        isAntiAlias = true
        color = Color.RED
    }
    private val rectf = RectF()

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        //无论如何 高始终为2.dp
        setMeasuredDimension(widthMeasureSpec,2.dp)
    }
    private var max :Int = 1
    fun setMax(max: Int){
        this.max = max
    }
    private var percent = 0.3f

    fun setPercent(value:Int){
        percent = value.toFloat() / max
        invalidate()
    }

    fun setColor(innerColor:Int = Color.BLUE,outerColor:Int = Color.GREEN){
        linePaint.color = outerColor
        progressPaint.color = innerColor
        invalidate()
    }


    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        canvas?.let {
            rectf.set(0f,0f,width.toFloat(),height.toFloat())
            it.drawRoundRect(rectf,8f,8f,linePaint)
            rectf.set(0f,0f,width * percent,height.toFloat())
            it.drawRoundRect(rectf,8f,8f,progressPaint)
        }
    }
}