package com.hua.simpleweather.utils

import android.content.Context
import android.widget.Toast

/**
 * @author : huaweikai
 * @Date   : 2021/12/23
 * @Desc   : ktx
 */
fun Any.toast(context: Context){
    Toast.makeText(context,this.toString(),Toast.LENGTH_SHORT).show()
}