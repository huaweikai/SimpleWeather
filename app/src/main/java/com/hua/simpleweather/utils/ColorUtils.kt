package com.hua.simpleweather.utils

import androidx.annotation.ColorInt
import androidx.core.graphics.ColorUtils

object ColorUtils {
    fun isColorLight(@ColorInt color:Int):Boolean{
        return ColorUtils.calculateLuminance(color) >= 0.5
    }
}