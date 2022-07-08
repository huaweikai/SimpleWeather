package com.hua.simpleweather.ui.adapter.holder

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.hua.material.materialcolor.ColorContainerData
import com.hua.model.weather.WeatherVO
import com.hua.resource.getAQIDesc
import com.hua.resource.getIcon
import com.hua.resource.getWind
import com.hua.resource.getWindDirection
import com.hua.simpleweather.R
import com.hua.simpleweather.databinding.ItemHourCardBinding
import com.hua.simpleweather.databinding.RvItemHourBinding
import com.hua.simpleweather.utils.calendar
import com.hua.simpleweather.utils.getString
import com.hua.simpleweather.utils.resultDate
import com.hua.simpleweather.utils.toTime
import java.util.*
import kotlin.math.roundToInt

class HourHolder(
    private val bind: ItemHourCardBinding
) : AbstractMainHolder(bind) {
    override fun onBindView(data: WeatherVO, colorData: ColorContainerData) {
        bind.apply {
            itemHourSubtitle.text = data.result.hourly.desc
            itemHourRv.adapter = HourAdapter(data.result.hourly, colorData)
            itemHourTitle.setTextColor(colorData.primaryColor)
        }
    }

    class HourAdapter(
        private val data: WeatherVO.Result.Hourly,
        private val colorData: ColorContainerData
    ) :
        RecyclerView.Adapter<HourAdapter.Holder>() {
        class Holder(val bind: RvItemHourBinding) : RecyclerView.ViewHolder(bind.root)

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
            return Holder(
                RvItemHourBinding.inflate(
                    LayoutInflater.from(parent.context), parent, false
                )
            )
        }

        override fun onBindViewHolder(holder: Holder, position: Int) {
            holder.bind.apply {
                calendar.time = resultDate.parse(data.temperature[position].datetime) as Date
                rvHourTime.setTextColor(colorData.containerColor)
                rvHourTime.text = getString(
                    R.string.hour_update_time, calendar.get(Calendar.HOUR_OF_DAY).toTime(),
                    calendar.get(Calendar.MINUTE).toTime()
                )
                rvHourSkyDetial.text = getString(
                    R.string.hour_rain_percent,
                    data.precipitation[position].value.toFloat().roundToInt()
                )
                getIcon(data.skyIcon[position].value)?.let { rvHourSkyIcon.setImageResource(it) }
                rvHourTemp.text = getString(
                    R.string.hour_sky_temp,
                    data.temperature[position].value.toFloat().roundToInt()
                )
                rvHourWindNav.rotation = data.wind[position].direction
                rvHourWindDetail.text = getString(
                    R.string.hour_wind_level,
                    getWind(data.wind[position].speed)
                )
            }
        }

        override fun getItemCount() = data.temperature.size
    }
}