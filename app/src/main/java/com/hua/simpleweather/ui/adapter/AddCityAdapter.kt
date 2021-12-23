package com.hua.simpleweather.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.hua.simpleweather.R
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
            return oldItem.address == newItem.address
        }

        override fun areContentsTheSame(oldItem: LocalCity, newItem: LocalCity): Boolean {
            return oldItem.cityName == newItem.cityName
        }
    }
) {
    private var localList = listOf<String>()

    //当新的地址添加后，更新到这，并更新列表
    fun updateLocalList(list:List<String>){
        this.localList = list
        notifyItemRangeChanged(0,itemCount)
    }


    class VHolder(val bind:ItemAddCityBinding):RecyclerView.ViewHolder(bind.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VHolder {
        val holder = VHolder(
            ItemAddCityBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        ).apply {
            bind.addcityAddIv.setOnClickListener {
                onclick(getItem(adapterPosition))
            }
        }
        return holder
    }

    override fun onBindViewHolder(holder: VHolder, position: Int) {
        val data = getItem(position)
        holder.bind.apply {
            val address = data.cityName
            addcityName.text = address
            val lngLat = data.lng + data.lat
            if(lngLat in localList){
                addcityAddIv.setImageResource(R.drawable.ic_added)
                addcityAddIv.isClickable = false
            }
        }
    }
}