package com.hua.simpleweather.ui.adapter.holder

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.hua.material.materialcolor.ColorContainerData
import com.hua.model.weather.WeatherVO
import com.hua.resource.getSkyIcon
import com.hua.resource.getSkyName
import com.hua.simpleweather.R
import com.hua.simpleweather.base.BaseViewHolder
import com.hua.simpleweather.databinding.ItemDailyCardBinding
import com.hua.simpleweather.databinding.ItemDailyWeatherBinding
import com.hua.simpleweather.utils.calendar
import com.hua.simpleweather.utils.getString
import com.hua.simpleweather.utils.resultDate
import com.hua.simpleweather.utils.toTime
import java.util.*
import kotlin.math.roundToInt

class DailyDayHolder(
    private val bind: ItemDailyCardBinding,
    color: ColorContainerData
) : AbstractMainHolder(bind, color) {
    override fun onBindView(data: WeatherVO) {
        bind.apply {
            itemDailyTitle.text = "七日预报"
            itemDailyTitle.setTextColor(colorData.primaryColor)
            itemDailyRv.adapter = DailyAdapter(data)
        }
    }
}

class DailyAdapter(
    private val data: WeatherVO
) : RecyclerView.Adapter<BaseViewHolder<ItemDailyWeatherBinding>>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): BaseViewHolder<ItemDailyWeatherBinding> {
        return BaseViewHolder(
            ItemDailyWeatherBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: BaseViewHolder<ItemDailyWeatherBinding>, position: Int) {
        holder.bind.apply {
            calendar.time = resultDate.parse(data.result.daily.skycon[position].date) as Date
            itemDailyDate.text = getString(
                R.string.item_daily_date,
                calendar.get(Calendar.YEAR),
                (calendar.get(Calendar.MONTH) + 1).toTime(),
                calendar[Calendar.DAY_OF_MONTH] + 1
            )

            itemDailySkyDesc.text = getSkyName(data.result.daily.skycon[position].value)

            itemDailyTemp.text = getString(
                R.string.item_daily_temp,
                data.result.daily.temperature[position].min.roundToInt(),
                data.result.daily.temperature[position].max.roundToInt()
            )

            itemDailySkyIcon.setImageResource(getSkyIcon(data.result.daily.skycon[position].value))
        }
    }

    override fun getItemCount() = data.result.daily.temperature.size
}