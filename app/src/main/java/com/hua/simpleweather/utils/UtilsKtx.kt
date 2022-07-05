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


fun View.disableAutoFill() = run {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        this.importantForAutofill = View.IMPORTANT_FOR_AUTOFILL_NO_EXCLUDE_DESCENDANTS
    }
}

fun Activity.fullScreen() {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
        window.setDecorFitsSystemWindows(true)
    }
    @Suppress("DEPRECATION")
    window.decorView.systemUiVisibility =
        View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
    @Suppress("DEPRECATION")
    window.clearFlags(
        WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS
                or WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION
    )
    window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
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

fun Activity.setLightStatusBar(isLightBar: Boolean) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
        window.insetsController?.let {
            if (isLightBar) {
                it.setSystemBarsAppearance(
                    WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS,
                    WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS
                )
            } else {
                it.setSystemBarsAppearance(
                    0,
                    WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS
                )
            }
        }
    }
    @Suppress("DEPRECATION")
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        val decorView = window.decorView
        val systemUiVisibility = decorView.systemUiVisibility
        if (isLightBar) {
            decorView.systemUiVisibility =
                systemUiVisibility or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        } else {
            decorView.systemUiVisibility =
                systemUiVisibility and View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR.inv()
        }
    }
}