package com.hua.simpleweather.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.hua.model.weather.WeatherVO
import com.hua.resource.getWarnColor
import com.hua.resource.getWarnDec
import com.hua.resource.getWarnIcon
import com.hua.simpleweather.R
import com.hua.simpleweather.databinding.ItemAlertBinding
import com.hua.simpleweather.utils.calendar
import com.hua.simpleweather.utils.getString
import com.hua.simpleweather.utils.toTime
import java.util.*

class AlertAdapter:ListAdapter<WeatherVO.Result.Content,AlertAdapter.VHolder>(
    object :DiffUtil.ItemCallback<WeatherVO.Result.Content>(){
        override fun areItemsTheSame(
            oldItem: WeatherVO.Result.Content,
            newItem: WeatherVO.Result.Content
        ): Boolean {
            return oldItem.code == newItem.code
        }

        override fun areContentsTheSame(
            oldItem: WeatherVO.Result.Content,
            newItem: WeatherVO.Result.Content
        ): Boolean {
            return oldItem == oldItem
        }
    }
) {
    class VHolder(val itemAlertBinding: ItemAlertBinding):RecyclerView.ViewHolder(itemAlertBinding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VHolder {
        return VHolder(
            ItemAlertBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        )
    }

    override fun onBindViewHolder(holder: VHolder, position: Int) {
        val data = currentList[position]
        holder.itemAlertBinding.apply {
            itemAlertIcon.setImageResource(getWarnIcon(data.code))
            itemAlertIcon.setColorFilter(getWarnColor(data.code))
            itemAlertLevel.text = getWarnDec(data.code)
            calendar.time = Date(data.publishTime * 1000)
            itemAlertTime.text = getString(
                R.string.alert_time,
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH),
                calendar.get(Calendar.HOUR_OF_DAY).toTime(),
                calendar.get(Calendar.MINUTE).toTime()
            )
            itemAlertDesc.text = data.description
        }
    }
}