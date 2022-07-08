package com.hua.simpleweather.ui.view

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
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
    }
    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        //无论如何 高始终为2.dp
        setMeasuredDimension(widthMeasureSpec,2.dp)
    }


    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        canvas?.let {

        }
    }
}