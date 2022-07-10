package com.hua.model

import android.graphics.drawable.Drawable

data class AppInfoVo(
    val icon: Drawable,
    val appName:String,
    val packageName:String,
    val launcherName:String
)