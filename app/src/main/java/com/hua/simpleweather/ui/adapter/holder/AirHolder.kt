package com.hua.simpleweather.ui.adapter.holder

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.hua.material.materialcolor.ColorContainerData
import com.hua.model.weather.WeatherVO
import com.hua.resource.AQIDetailViewType
import com.hua.resource.getAQIDesc
import com.hua.resource.getAQIValue
import com.hua.simpleweather.R
import com.hua.simpleweather.base.BaseViewHolder
import com.hua.simpleweather.databinding.CardDetailItemBinding
import com.hua.simpleweather.databinding.ItemAirCardBinding
import com.hua.simpleweather.utils.getString

class AirHolder(
    private val bind:ItemAirCardBinding,
    colorData:ColorContainerData
) :AbstractMainHolder(bind,colorData){
    override fun onBindView(data: WeatherVO) {
        bind.apply {
            itemAirCardTitle.setTextColor(colorData.primaryColor)

            itemAirCardCricleProgress.setColor(colorData.primaryColor,colorData.containerColor)
            itemAirCardCricleProgress.setMax(500)
            itemAirCardCricleProgress.setProgress(data.result.realtime.airQuality.aqi.chn)

            itemAirDesc.text = data.result.realtime.airQuality.description.chn

            itemAirCardRv.adapter = ItemDetailCardAdapter(data.result.realtime.airQuality,colorData)
        }
    }
}
class ItemDetailCardAdapter(
    private val data:WeatherVO.Result.AirQuality,
    private val colorData: ColorContainerData
):RecyclerView.Adapter<BaseViewHolder<CardDetailItemBinding>>(){
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): BaseViewHolder<CardDetailItemBinding> {
       return BaseViewHolder(
           CardDetailItemBinding.inflate(
               LayoutInflater.from(parent.context),
               parent,
               false
           )
       )
    }

    override fun onBindViewHolder(holder: BaseViewHolder<CardDetailItemBinding>, position: Int) {
       holder.bind.apply {
           cardItemDetailTitle.text = listAqi[position].desc
           val value = getAQIValue(listAqi[position],data)
           cardItemDetailValue.text = getString(
               R.string.item_detail_value,
               value
           )
           cardItemDetailProgress.setColor(colorData.primaryColor,colorData.containerColor)
           cardItemDetailProgress.setMax(500)
           cardItemDetailProgress.setPercent(value)
       }
    }

    override fun getItemCount() = listAqi.size

    private val listAqi = listOf(
        AQIDetailViewType.PM25,
        AQIDetailViewType.PM10,
        AQIDetailViewType.CO,
        AQIDetailViewType.SO2,
        AQIDetailViewType.NO2,
        AQIDetailViewType.O3
    )
}