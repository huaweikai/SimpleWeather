@file:Suppress("unused")

package com.hua.simpleweather.utils

/**
 * 对activity进行扩展，借鉴阅读APP的
 */
import android.app.Activity
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.Rect
import android.os.Build
import android.util.DisplayMetrics
import android.util.Log
import android.view.*
import androidx.annotation.ColorInt
import androidx.annotation.ColorRes
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.drawToBitmap
import androidx.palette.graphics.Palette
import com.hua.simpleweather.R

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

val Context.isDarkMode get() = resources.configuration.uiMode == 0x21

val WindowManager.windowSize: DisplayMetrics
    get() {
        val displayMetrics = DisplayMetrics()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            val windowMetrics: WindowMetrics = currentWindowMetrics
            val insets = windowMetrics.windowInsets
                .getInsetsIgnoringVisibility(WindowInsets.Type.systemBars())
            displayMetrics.widthPixels = windowMetrics.bounds.width() - insets.left - insets.right
            displayMetrics.heightPixels = windowMetrics.bounds.height() - insets.top - insets.bottom
        } else {
            @Suppress("DEPRECATION")
            defaultDisplay.getMetrics(displayMetrics)
        }
        return displayMetrics
    }

/**
 * 设置状态栏颜色
 */
fun Activity.setStatusBarColorAuto(
    @ColorInt color: Int,
    isTransparent: Boolean,
    fullScreen: Boolean
) {
    val isLightBar = ColorUtils.isColorLight(color)
    if (fullScreen) {
        if (isTransparent) {
            window.statusBarColor = Color.TRANSPARENT
        } else {
            window.statusBarColor = getCompatColor(R.color.purple_700)
        }
    } else {
        window.statusBarColor = color
    }
    setLightStatusBar(isLightBar)
}

/**
 * 自动截图，然后解析状态栏是否亮色
 */
fun AppCompatActivity.fixStatusBarColor() {
    val bitmap = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        screenShotWithO()
    } else {
        screenShotMinO()
    }
    Palette.from(bitmap)
        .setRegion(
            0,
            0,
            resources.configuration.screenWidthDp.dpToPx(this).toInt(),
            statusHeight
        )
        .generate {
            it?.let { palette ->
                var mostPopularSwatch: Palette.Swatch? = null
                for (swatch in palette.swatches) {
                    if (mostPopularSwatch == null || swatch.population > mostPopularSwatch.population) {
                        mostPopularSwatch = swatch
                    }
                }
                mostPopularSwatch?.let { swatch ->
                    val luminance = androidx.core.graphics.ColorUtils.calculateLuminance(swatch.rgb)
                    setLightStatusBar(luminance > 0.5)
                }
            }
            bitmap.recycle()
        }
}

@RequiresApi(Build.VERSION_CODES.O)
private fun AppCompatActivity.screenShotWithO(): Bitmap {
    val view = window.decorView
    val bitmap = Bitmap.createBitmap(view.width, view.height, Bitmap.Config.ARGB_8888)
    val rect = Rect(0, 0, view.height, view.bottom)
    PixelCopy.request(window,
        rect ,
        bitmap, {
            if (it == PixelCopy.SUCCESS) {
                Log.d("TAG", "fixStatusBarColor: 截图正确")
            }
        }, mainHandler
    )
    return bitmap
}

private fun AppCompatActivity.screenShotMinO(): Bitmap {
    return window.decorView.drawToBitmap()
}

fun Context.getCompatColor(@ColorRes id: Int): Int = ContextCompat.getColor(this, id)