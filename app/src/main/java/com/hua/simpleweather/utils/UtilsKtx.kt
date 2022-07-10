package com.hua.simpleweather.utils

import android.app.Activity
import android.content.Context
import android.content.res.Resources
import android.graphics.Color
import android.os.Build
import android.util.TypedValue
import android.view.View
import android.view.WindowInsetsController
import android.view.WindowManager
import android.widget.Toast
import androidx.annotation.ColorInt
import androidx.viewbinding.ViewBinding
import java.text.SimpleDateFormat
import java.util.*

/**
 * @author : huaweikai
 * @Date   : 2021/12/23
 * @Desc   : ktx
 */
//toast发送ktx
fun Any.toast(context: Context){
    Toast.makeText(context,this.toString(),Toast.LENGTH_SHORT).show()
}
//将int值转为dp值
val Int.dp: Int
    get() {
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            this.toFloat(),
            Resources.getSystem().displayMetrics
        ).toInt()
    }
//将dp值转为px值
fun Int.dpToPx(context:Context) = (this * context.resources.displayMetrics.density) + 0.5f
//将int值直接转为px值
fun Int.valueToPx(context: Context) = this * (context.resources.displayMetrics.densityDpi / 160f)

//将Int值转为sp值
val Int.sp: Float
    get() {
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_SP,
            this.toFloat(),
            Resources.getSystem().displayMetrics
        )
    }


fun View.disableAutoFill() = run {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        this.importantForAutofill = View.IMPORTANT_FOR_AUTOFILL_NO_EXCLUDE_DESCENDANTS
    }
}

//viewBind中直接获取资源文字，用于显示
fun ViewBinding.getString(resId: Int, vararg formatString: Any): String {
    return this.root.resources.getString(resId, *formatString)
}
//日历单例
val calendar = Calendar.getInstance()

val simpleDateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm",Locale.CHINA)

//对彩云天气的接口时间进行解析
val resultDate = SimpleDateFormat("yyyy-MM-dd'T'HH:mm+08:00", Locale.CHINA)

//由日历转来的时间在小于10时，只有一位
fun Int.toTime():String{
    return if(this < 10){
        "0$this"
    }else{
        this.toString()
    }
}