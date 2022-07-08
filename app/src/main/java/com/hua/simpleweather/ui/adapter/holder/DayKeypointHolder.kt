package com.hua.simpleweather.ui.adapter.holder

import androidx.core.graphics.ColorUtils
import com.hua.material.materialcolor.ColorContainerData
import com.hua.model.weather.WeatherVO
import com.hua.simpleweather.R
import com.hua.simpleweather.databinding.ItemRealtimeSurveyCardBinding
import com.hua.simpleweather.utils.calendar
import com.hua.simpleweather.utils.getString
import com.hua.simpleweather.utils.simpleDateFormat
import com.hua.simpleweather.utils.toTime
import java.util.*

class DayKeypointHolder(
   private val itemRealtimeSurveyBinding: ItemRealtimeSurveyCardBinding
):AbstractMainHolder(itemRealtimeSurveyBinding) {
    override fun onBindView(data: WeatherVO,colorData: ColorContainerData) {
        itemRealtimeSurveyBinding.apply {
            itemSurveyTitle.setTextColor(colorData.primaryColor)
            itemRealtimeSurvey.text = data.result.survey
            calendar.time = Date(data.updateTime)
            downTime.setTextColor(
                colorData.containerColor
            )
            downTime.text = getString(R.string.update_time, calendar.get(Calendar.HOUR_OF_DAY).toTime(),
                calendar.get(Calendar.MINUTE).toTime())
        }
    }
}