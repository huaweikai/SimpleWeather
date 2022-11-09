package com.hua.simpleweather.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.hua.model.weather.WeatherVO
import com.hua.resource.getSkyBg
import com.hua.resource.getSkyName
import com.hua.simpleweather.R
import com.hua.simpleweather.databinding.ItemCityBinding
import com.hua.simpleweather.utils.getString
import com.hua.simpleweather.utils.isDarkMode
import kotlin.math.roundToInt

/**
 * @author : huaweikai
 * @Date   : 2021/12/22
 * @Desc   : adapter
 */
class CityAdapter(
    private val onDeleteClick:(weatherPO: WeatherVO)->Unit,
    private val onclick:(Int)->Unit
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
            notifyItemRangeChanged(1,itemCount)
        }
    class VHolder(val bind:ItemCityBinding):RecyclerView.ViewHolder(bind.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VHolder {
//        val width = parent.context.resources.displayMetrics.widthPixels
        val holder = VHolder(
            ItemCityBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        ).apply {
            bind.root.isClickable = !isDeleteTime
            bind.cityDelete.setOnClickListener {
                onDeleteClick(getItem(bindingAdapterPosition))
            }
            bind.root.setOnClickListener {
                onclick(bindingAdapterPosition)
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
            cityBg.load(getSkyBg(this.root.context.isDarkMode,data.result.realtime.skycon))
            cityName.text = data.cityName
            val temp = "${data.result.realtime.temperature.roundToInt()}°"
            cityTemp.text = temp
            cityWeatherDesc.text = getString(
                R.string.city_item_desc,
                getSkyName(data.result.realtime.skycon),
                data.result.daily.temperature[0].min.roundToInt(),
                data.result.daily.temperature[0].max.roundToInt()
            )
            if(data.id != 0){
                cityDelete.isVisible = isDeleteTime
                cityDrag.isVisible = isDeleteTime
            }
            cityIsLocation.isVisible = data.id == 0

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