package com.hua.simpleweather.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import coil.size.Scale
import com.hua.model.weather.WeatherVO
import com.hua.resource.getSkyBg
import com.hua.resource.getSkyName
import com.hua.simpleweather.databinding.ItemCityBinding
import com.hua.simpleweather.utils.dp
import com.hua.simpleweather.utils.isDarkMode
import kotlin.math.roundToInt

/**
 * @author : huaweikai
 * @Date   : 2021/12/22
 * @Desc   : adapter
 */
class CityAdapter(
    private val onDeleteClick:(weatherPO: WeatherVO)->Unit,
    private val onclick:(Int)->Unit,
    private val onLongClick:()->Unit
):ListAdapter<WeatherVO,CityAdapter.VHolder>(
    object :DiffUtil.ItemCallback<WeatherVO>(){
        override fun areItemsTheSame(oldItem: WeatherVO, newItem: WeatherVO): Boolean {
            return oldItem.address == newItem.address
        }

        override fun areContentsTheSame(oldItem: WeatherVO, newItem: WeatherVO): Boolean {
            return oldItem == newItem
        }
    }
) {
    var isDeleteTime = false
        set(value) {
            field = value
            notifyItemRangeChanged(0,itemCount)
        }
    class VHolder(val bind:ItemCityBinding):RecyclerView.ViewHolder(bind.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VHolder {
        val width = parent.context.resources.displayMetrics.widthPixels
        val holder = VHolder(
            ItemCityBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        ).apply {
            bind.root.isClickable = !isDeleteTime
            bind.cityDelete.setOnClickListener {
                onDeleteClick(getItem(adapterPosition))
            }
            bind.root.setOnClickListener {
                onclick(adapterPosition)
            }
//            bind.root.setOnLongClickListener {
//                isDeleteTime = true
//                notifyItemRangeChanged(0,currentList.size)
//                onLongClick()
//                return@setOnLongClickListener true
//            }
        }
        return holder
    }

    override fun onBindViewHolder(holder: VHolder, position: Int) {
        val data = getItem(position)
        holder.bind.apply {
            cityBg.load(getSkyBg(this.root.context.isDarkMode(),data.result.realtime.skycon))
            cityName.text = data.cityName
            val temp = "${data.result.realtime.temperature.roundToInt()}°"
            cityTemp.text = temp
            cityWeatherDesc.text = getSkyName(data.result.realtime.skycon)
        }
//        holder.bind.apply {
//            cityBg.setBackgroundResource(
//                getSky(data.realtime_skyIcon).bg
//            )
//            cityName.text = data.cityName
//            val temp = "${data.realtime_temperature.roundToInt()} °C"
//            cityTemp.text = temp
//            cityWeatherDesc.text = getSky(data.realtime_skyIcon).info
//
//            cityDelete.isVisible = isDeleteTime
//
//
//        }
    }
}