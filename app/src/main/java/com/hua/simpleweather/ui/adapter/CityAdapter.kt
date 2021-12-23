package com.hua.simpleweather.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.hua.simpleweather.db.dao.bean.WeatherBean
import com.hua.simpleweather.databinding.ItemCityBinding
import com.hua.simpleweather.other.getSky
import kotlin.math.roundToInt

/**
 * @author : huaweikai
 * @Date   : 2021/12/22
 * @Desc   : adapter
 */
class CityAdapter(
    private val onDeleteClick:(weatherBean: WeatherBean)->Unit,
    private val onclick:(Int)->Unit,
    private val onLongClick:()->Unit
):ListAdapter<WeatherBean,CityAdapter.VHolder>(
    object :DiffUtil.ItemCallback<WeatherBean>(){
        override fun areItemsTheSame(oldItem: WeatherBean, newItem: WeatherBean): Boolean {
            return oldItem.address == newItem.address
        }

        override fun areContentsTheSame(oldItem: WeatherBean, newItem: WeatherBean): Boolean {
            return oldItem == newItem
        }
    }
) {
     var isDeleteTime = false
    class VHolder(val bind:ItemCityBinding):RecyclerView.ViewHolder(bind.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VHolder {
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
            cityBg.setBackgroundResource(
                getSky(data.realtime_skyIcon).bg
            )
            cityName.text = data.cityName
            val temp = "${data.realtime_temperature.roundToInt()} °C"
            cityTemp.text = temp
            cityWeatherDesc.text = getSky(data.realtime_skyIcon).info

            cityDelete.isVisible = isDeleteTime

            root.isLongClickable = isDeleteTime

        }
    }
}