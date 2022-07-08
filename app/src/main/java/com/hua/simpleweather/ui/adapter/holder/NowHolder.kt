package com.hua.simpleweather.ui.adapter.holder

import com.hua.material.materialcolor.ColorContainerData
import com.hua.model.weather.WeatherVO
import com.hua.resource.getSkyIcon
import com.hua.resource.getSkyName
import com.hua.simpleweather.R
import com.hua.simpleweather.databinding.ItemNowCardBinding
import com.hua.simpleweather.utils.getString
import kotlin.math.roundToInt

class NowHolder(
    val bind: ItemNowCardBinding,
    colorData: ColorContainerData
) : AbstractMainHolder(bind,colorData) {
    override fun onBindView(data: WeatherVO) {
        val value = getSkyName(data.result.realtime.skycon)
        bind.apply {
            val temp = data.result.realtime.temperature.roundToInt()
//            val animate = ObjectAnimator.ofInt(temp - 10, temp).apply {
//                duration = 500
//                interpolator = DecelerateInterpolator()
//            }
            itemNowSky.text = getString(
                R.string.sky_text,
                value,
                data.result.realtime.parentTemperature.roundToInt()
            )
            itemNowTemp.text = getString(
                R.string.sky_temp_now,
               temp
            )
//            animate.addUpdateListener {
//
//            }
//            animate.start()
            itemPm25.text = getString(
                R.string.sky_aqi_desc,
                data.result.realtime.airQuality.description.chn
            )
            itemNowCity.text = data.cityName
            itemNowSkyIcon.setImageResource(getSkyIcon(data.result.realtime.skycon))
        }
    }

}