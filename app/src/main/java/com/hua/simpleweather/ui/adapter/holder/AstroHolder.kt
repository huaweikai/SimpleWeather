package com.hua.simpleweather.ui.adapter.holder

import com.hua.material.materialcolor.ColorContainerData
import com.hua.model.weather.WeatherVO
import com.hua.simpleweather.databinding.ItemAtrsoCardBinding
import com.hua.simpleweather.utils.calendar
import java.text.SimpleDateFormat
import java.util.*

class AstroHolder(
    private val bind: ItemAtrsoCardBinding
) : AbstractMainHolder(bind) {
    override fun onBindView(data: WeatherVO, colorData: ColorContainerData) {
        val day = data.result.daily.astro[0]
        bind.apply {
            sunView.setColor(colorData)
            sunView.setTime(
                day.sunrise.time.toHhMmTime(),
                day.sunset.time.toHhMmTime(),
                getNowHhMm(),
            )
            itemSunUpTime.text = day.sunrise.time
            itemSunDownTime.text = day.sunset.time
            itemAtrsoTitle.setTextColor(colorData.primaryColor)
        }
    }
}

val HhMmDateFormat = SimpleDateFormat("HH:mm", Locale.CHINA)

fun String.toHhMmTime(): Long {
    calendar.time = HhMmDateFormat.parse(this) as Date
    return (calendar.get(Calendar.HOUR_OF_DAY) * 60 + calendar.get(Calendar.MINUTE)) * 1000L
}

fun getNowHhMm(): Long {
    val calendar = Calendar.getInstance()
    val hour = calendar.get(Calendar.HOUR_OF_DAY)
    val minute = calendar.get(Calendar.MINUTE)
    return (hour * 60 + minute) * 1000L
}