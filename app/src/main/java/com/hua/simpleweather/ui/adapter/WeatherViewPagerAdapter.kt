package com.hua.simpleweather.ui.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.hua.simpleweather.R
import com.hua.simpleweather.db.dao.bean.WeatherBean
import com.hua.simpleweather.databinding.ItemMainViewpagerBinding
import com.hua.simpleweather.other.getSky
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.roundToInt

/**
 * @author : huaweikai
 * @Date   : 2021/12/22
 * @Desc   : adapter
 */
class WeatherViewPagerAdapter(
    private val onclick: () -> Unit,
    private val onRefresh: (SwipeRefreshLayout) -> Unit,
    private val attachToWindow: (Int) -> Unit
) : ListAdapter<WeatherBean, WeatherViewPagerAdapter.VHolder>(
    object : DiffUtil.ItemCallback<WeatherBean>() {
        override fun areItemsTheSame(oldItem: WeatherBean, newItem: WeatherBean): Boolean {
            return oldItem.address == newItem.address
        }

        override fun areContentsTheSame(oldItem: WeatherBean, newItem: WeatherBean): Boolean {
            return oldItem == newItem
        }
    }
) {
    inner class VHolder(val bind: ItemMainViewpagerBinding) : RecyclerView.ViewHolder(bind.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VHolder {
        val holder = VHolder(
            ItemMainViewpagerBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        ).apply {
            this.bind.now.mainAdd.setOnClickListener {
                //点到添加按钮，无须传东西
                onclick()
            }
        }
        return holder
    }

    override fun onViewAttachedToWindow(holder: VHolder) {
        super.onViewAttachedToWindow(holder)
        attachToWindow(
            getSky(currentList[holder.adapterPosition].realtime_skyIcon).bg
        )
    }

    override fun onBindViewHolder(holder: VHolder, position: Int) {
        val data = getItem(position)
        holder.bind.root
        holder.bind.apply {
            val currentTempText = "${data.realtime_temperature.roundToInt()}°C"
            now.currentTemp.text = currentTempText
            now.cityName.text = getItem(position).cityName
            now.currentSky.text = getSky(data.realtime_skyIcon).info
            val pm25Text = "PM2.5 ${data.realtime_airQuality_aqi.toInt()}"
            now.currentAQI.text = pm25Text
            now.nowLayout.setBackgroundResource(getSky(data.realtime_skyIcon).bg)
            swipeRefresh.setOnRefreshListener {
                onRefresh(swipeRefresh)
            }
            forecast.forecastLayout.removeAllViews()
            val days = data.daily_temperature.size
            for (i in 0 until days) {
                val skycon = data.daily_skyIcon[i]
                val temperature = data.daily_temperature[i]
                val view = LayoutInflater.from(holder.itemView.context)
                    .inflate(R.layout.forecast_item, holder.bind.forecast.forecastLayout, false)
                val dateInfo = view.findViewById<TextView>(R.id.dateInfo)
                val skyIcon = view.findViewById<ImageView>(R.id.skyIcon)
                val skyInfo = view.findViewById<TextView>(R.id.skyInfo)
                val temperatureInfo = view.findViewById<TextView>(R.id.temperatureInfo)
                //设定时间格式
                val simpleDateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                dateInfo.text = simpleDateFormat.format(skycon.date)
                val sky = getSky(skycon.value)
                skyIcon.setImageResource(sky.icon)
                skyInfo.text = sky.info
                val tempText = "${temperature.min.toInt()} - ${temperature.max.toInt()} °C"
                temperatureInfo.text = tempText
                holder.bind.forecast.forecastLayout.addView(view)
            }
            lifeIndex.coldRiskText.text = data.life_index[0]
            lifeIndex.dressingText.text = data.life_index[1]
            lifeIndex.ultravioletText.text = data.life_index[2]
            lifeIndex.carWashingText.text = data.life_index[3]
        }
    }
}