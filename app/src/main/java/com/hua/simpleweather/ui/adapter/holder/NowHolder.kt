package com.hua.simpleweather.ui.adapter.holder

import android.animation.Animator
import android.animation.ObjectAnimator
import android.util.Log
import android.view.animation.DecelerateInterpolator
import android.view.animation.LinearInterpolator
import androidx.viewbinding.ViewBinding
import androidx.viewbinding.ViewBindings
import com.hua.material.materialcolor.ColorContainerData
import com.hua.model.weather.WeatherVO
import com.hua.resource.getSkyName
import com.hua.simpleweather.R
import com.hua.simpleweather.databinding.ItemNowBinding
import com.hua.simpleweather.db.dao.bean.WeatherPO
import com.hua.simpleweather.utils.getString
import kotlin.math.roundToInt

class NowHolder(
    val bind: ItemNowBinding
) : AbstractMainHolder(bind) {
    override fun onBindView(data: WeatherVO,colorData:ColorContainerData) {
        val value = getSkyName(data.result.realtime.skycon)
        bind.apply {
            val temp = data.result.realtime.temperature.roundToInt()
            val animate = ObjectAnimator.ofInt(temp - 10, temp).apply {
                duration = 500
                interpolator = DecelerateInterpolator()
            }
            itemNowSky.text = getString(
                R.string.sky_text,
                value,
                data.result.realtime.parentTemperature.roundToInt()
            )
            animate.addUpdateListener {
                itemNowTemp.text = getString(
                    R.string.sky_temp_now,
                    it.animatedValue as Int
                )
            }
            animate.start()
            itemPm25.text = getString(
                R.string.sky_aqi_desc,
                data.result.realtime.airQuality.description.chn
            )
            itemNowCity.text = data.cityName
        }
    }

}