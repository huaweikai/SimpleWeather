package com.hua.simpleweather.utils

import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import android.view.WindowInsets
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity

/**
 * created by william
 * @Date Created in 2023/3/13
 */
/**
 * 状态栏高度
 */
val AppCompatActivity.statusHeight: Int
    @SuppressLint("InternalInsetResource", "DiscouragedApi", "PrivateApi")
    get() {
        val identifier: Int = resources.getIdentifier("status_bar_height", "dimen", "android")
        if (identifier > 0) {
            return resources.getDimensionPixelSize(identifier)
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            val wm = getSystemService(Context.WINDOW_SERVICE) as WindowManager
            val windowMetrics = wm.currentWindowMetrics
            val windowInsets = windowMetrics.windowInsets
            val insets =
                windowInsets.getInsetsIgnoringVisibility(WindowInsets.Type.navigationBars() or WindowInsets.Type.displayCutout())
            if (insets.top > 0) {
                return insets.top
            }
        } else {
            try {
                val c = Class.forName("com.android.internal.R\$dimen")
                val obj = c.newInstance()
                val field = c.getField("status_bar_height")
                val x = field[obj]?.toString()?.toInt()
                return if (x == null) {
                    0
                } else {
                    resources.getDimensionPixelSize(x)
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        return 0
    }