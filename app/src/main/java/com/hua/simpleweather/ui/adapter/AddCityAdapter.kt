package com.hua.simpleweather.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.hua.simpleweather.db.dao.bean.LocalCity
import com.hua.simpleweather.databinding.ItemAddCityBinding

/**
 * @author : huaweikai
 * @Date   : 2021/12/22
 * @Desc   : adapter
 */
class AddCityAdapter(
    private val onclick:(LocalCity)->Unit
):ListAdapter<LocalCity,AddCityAdapter.VHolder>(
    object :DiffUtil.ItemCallback<LocalCity>(){
        override fun areItemsTheSame(oldItem: LocalCity, newItem: LocalCity): Boolean {
            return oldItem.code == newItem.code
        }

        override fun areContentsTheSame(oldItem: LocalCity, newItem: LocalCity): Boolean {
            return oldItem.cityName == newItem.cityName
        }
    }
) {

    class VHolder(val bind:ItemAddCityBinding):RecyclerView.ViewHolder(bind.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VHolder {
        val holder = VHolder(
            ItemAddCityBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        ).apply {
            itemView.setOnClickListener {
                onclick(getItem(adapterPosition))
            }
        }
        return holder
    }

    override fun onBindViewHolder(holder: VHolder, position: Int) {
        val data = getItem(position)
        holder.bind.apply {
            addcityPlaceName.text = data.cityName
            val address = "经度${data.lng} 维度${data.lat}"
            addcityPlaceAddress.text = address
        }

    }
}