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
fun Any.toast(context: Context){
    Toast.makeText(context,this.toString(),Toast.LENGTH_SHORT).show()
}
val Int.dp: Int
    get() {
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            this.toFloat(),
            Resources.getSystem().displayMetrics
        ).toInt()
    }
fun Int.dpToPx(context:Context) = (this * context.resources.displayMetrics.density) + 0.5f

fun Int.valueToPx(context: Context) = this * (context.resources.displayMetrics.densityDpi / 160f)


fun View.disableAutoFill() = run {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        this.importantForAutofill = View.IMPORTANT_FOR_AUTOFILL_NO_EXCLUDE_DESCENDANTS
    }
}



///**
// * 设置状态栏颜色
// */
//fun Activity.setStatusBarColorAuto(
//    @ColorInt color: Int,
//    isTransparent: Boolean,
//    fullScreen: Boolean
//) {
//    val isLightBar = ColorUtils.isColorLight(color)
//    if (fullScreen) {
//        if (isTransparent) {
//            window.statusBarColor = Color.TRANSPARENT
//        } else {
//            window.statusBarColor = getCompatColor(R.color.status_bar_bag)
//        }
//    } else {
//        window.statusBarColor = color
//    }
//    setLightStatusBar(isLightBar)
//}


fun ViewBinding.getString(resId: Int, vararg formatString: Any): String {
    return this.root.resources.getString(resId, *formatString)
}

val calendar = Calendar.getInstance()

val simpleDateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm",Locale.CHINA)

val resultDate = SimpleDateFormat("yyyy-MM-dd'T'HH:mm+08:00", Locale.CHINA)

fun Int.toTime():String{
    return if(this < 10){
        "0$this"
    }else{
        this.toString()
    }
}