package com.hua.simpleweather.ui

import android.content.Context
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupWindow
import android.widget.TextView
import androidx.core.content.ContextCompat.getSystemService
import androidx.core.view.marginStart
import androidx.navigation.NavController
import com.hua.model.weather.WeatherVO
import com.hua.network.utils.globalJson
import com.hua.simpleweather.MainActivity
import com.hua.simpleweather.R
import com.hua.simpleweather.databinding.PopwindowMoreBinding
import com.hua.simpleweather.utils.dp
import com.hua.simpleweather.utils.toastOnUi
import kotlinx.serialization.encodeToString

private var morePopWindow :PopupWindow ?= null

fun View.showMorePopWindow(navController: NavController,weatherResult:WeatherVO){
    if(morePopWindow == null) morePopWindow = MorePopWindow(this.context,navController)
    morePopWindow?:return
    (morePopWindow!! as MorePopWindow).setWeather(weatherResult)
    morePopWindow?.showAsDropDown(this, 8.dp,8.dp)
}

class MorePopWindow(private val context: Context, private val navController: NavController) :
    PopupWindow(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT) {

    private var weatherResult:WeatherVO ?= null

    fun setWeather(weatherResult: WeatherVO){
        this.weatherResult = weatherResult
    }


    init {
        val bind = PopwindowMoreBinding.inflate(
            LayoutInflater.from(context)
        )
        contentView = bind.root
        bind.popwindowShare.setOnClickListener {
            weatherResult?.let {
                navController.navigate(R.id.shareFragment, Bundle().apply {
                    putString("weather", globalJson.encodeToString(it))
                })
            }?: context.toastOnUi("天气信息为空")

            this.dismiss()
        }
        isTouchable = true
        isOutsideTouchable = false
        isFocusable = true
    }
}