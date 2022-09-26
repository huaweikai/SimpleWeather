package com.hua.simpleweather.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.graphics.ColorUtils
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.hua.simpleweather.R
import com.hua.simpleweather.databinding.ItemAddCityBinding
import com.hua.simpleweather.db.dao.bean.LocalCity
import com.hua.simpleweather.utils.getString

class PagingCityAdapter(
    private val onclick: (LocalCity) -> Unit
): PagingDataAdapter<LocalCity,PagingCityAdapter.VHolder>(
    object :DiffUtil.ItemCallback<LocalCity>(){
        override fun areItemsTheSame(oldItem: LocalCity, newItem: LocalCity): Boolean {
            return oldItem.address == newItem.address
        }

        override fun areContentsTheSame(oldItem: LocalCity, newItem: LocalCity): Boolean {
            return oldItem == newItem
        }
    }
) {
    class VHolder(val bind: ItemAddCityBinding): RecyclerView.ViewHolder(bind.root)

    private var localList = listOf<String>()

    //当新的地址添加后，更新到这，并更新列表
    fun updateLocalList(list:List<String>){
        this.localList = list
        notifyItemRangeChanged(0,itemCount)
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VHolder {
        val holder = VHolder(
            ItemAddCityBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        ).apply {
            bind.root.setOnClickListener {
                getItem(bindingAdapterPosition)?.let { it1 -> onclick(it1) }
            }
        }
        return holder
    }

    override fun onBindViewHolder(holder: VHolder, position: Int) {
        val data = getItem(position) ?: return
//        val address = LocationUtils.getAddress(holder.bind.root.context,data.lng.toDouble(),data.lat.toDouble())
        holder.bind.apply {
            addCityName.text = data.cityName
            addCityLatLng.text = getString(
                R.string.addcity_location,
                data.lng,
                data.lat
            )
            val lngLat = data.lng + data.lat
            if(lngLat in localList){
                val color = ColorUtils.setAlphaComponent(
                    addCityName.currentTextColor,
                    (0.3 * 255).toInt()
                )
                citycard.isClickable = false
                addCityName.setTextColor(color)
                addCityLatLng.setTextColor(color)
            }
        }
    }
}