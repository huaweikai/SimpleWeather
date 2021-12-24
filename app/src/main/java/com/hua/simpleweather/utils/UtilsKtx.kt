package com.hua.simpleweather.utils

import android.content.Context
import android.content.res.Resources
import android.util.TypedValue
import android.widget.Toast

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