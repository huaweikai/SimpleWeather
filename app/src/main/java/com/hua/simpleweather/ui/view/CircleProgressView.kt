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
import com.hua.simpleweather.utils.isDarkMode
import com.hua.simpleweather.utils.sp
import kotlin.math.min

/**
 * 自定义圆形的进度条
 */

class CircleProgressView @JvmOverloads constructor(
    context:Context,
    attributeSet: AttributeSet ? =null,
    defStyle:Int = 0
) :View(context,attributeSet,defStyle){
    //画笔宽度
    private var circleWidth = 8.dp.toFloat()
    //内圆的画笔
    private val innerPaint = Paint().apply {
        strokeCap = Paint.Cap.ROUND
        strokeWidth = circleWidth
        color = Color.BLUE
        style = Paint.Style.STROKE
    }
    //外圆的画笔
    private val outPaint = Paint().apply {
        strokeCap = Paint.Cap.ROUND
        strokeWidth = circleWidth
        color = Color.GREEN
        style = Paint.Style.STROKE
    }
    //字体所占的宽高
    private val textBounds = Rect()

    //字体的画笔
    private val textPaint = Paint().apply {
        textSize = 16.sp
        //对字体颜色进行设置
        color = if(context.isDarkMode) Color.WHITE else Color.BLACK
    }
    private var text = "1"

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val widthMode = MeasureSpec.getMode(widthMeasureSpec)
        val heightMode = MeasureSpec.getMode(heightMeasureSpec)
        val widthSize = if(widthMode == MeasureSpec.EXACTLY) MeasureSpec.getSize(widthMeasureSpec) else 100.dp
        val heightSize = if(heightMode == MeasureSpec.EXACTLY) MeasureSpec.getSize(heightMeasureSpec) else 100.dp
        //选取最小值，保证一定是所在区域最大的正方形
        val size = min(widthSize,heightSize)
        setMeasuredDimension(size,size)
    }

    private var max = 1
    private var progress = 0f
    //设置最大值
    fun setMax(max:Int){
        this.max = max
    }
    private val CRICLE_START = 135F
    //设置当前的进度值
    fun setProgress(value:Int){
        val percent = (value.toFloat() / max)
        text = value.toString()
        //并计算出内圆需要画的度数
        progress = percent * 270f
        invalidate()
    }

    //设置画笔的颜色
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
            //计算baseLine
            val dy = (fontMetrics.bottom - fontMetrics.top) / 2 - fontMetrics.bottom
            val baseLineY = height / 2f + dy

            textPaint.getTextBounds(text,0,text.length,textBounds)
            //计算文字最中心的位置
            val start = width / 2 - textBounds.width() / 2f
            it.drawText(text,start,baseLineY,textPaint)
        }
    }
}