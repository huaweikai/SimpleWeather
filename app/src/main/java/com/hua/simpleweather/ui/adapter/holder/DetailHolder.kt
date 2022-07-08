package com.hua.simpleweather.ui.adapter.holder

import android.media.Image
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.hua.material.materialcolor.ColorContainerData
import com.hua.model.weather.WeatherVO
import com.hua.resource.DetailViewType
import com.hua.resource.getDetailIcon
import com.hua.resource.getDetailSubTile
import com.hua.resource.getDetailTile
import com.hua.simpleweather.R
import com.hua.simpleweather.base.BaseViewHolder
import com.hua.simpleweather.databinding.ItemDetailBinding
import com.hua.simpleweather.databinding.ItemRealtimeDetailCardBinding

class DetailHolder(
    private val bind:ItemRealtimeDetailCardBinding,
    colorData:ColorContainerData
):AbstractMainHolder(bind,colorData) {
    private val listType = listOf(
        DetailViewType.ViewTypeWind,
        DetailViewType.ViewTypeWet,
        DetailViewType.ViewTypeUltraviolet,
        DetailViewType.ViewTypePressure,
        DetailViewType.ViewTypeVisibility,
        DetailViewType.ViewTypeCloudRate
    )
    override fun onBindView(data: WeatherVO) {
        bind.itemRealtimeDetailTitle.setTextColor(colorData.primaryColor)
//        listType.forEach {
//            bind.itemRealtimeDetailRv.addView(
//                ItemDetailBinding.inflate(LayoutInflater.from(bind.root.context),bind.root,false).root.apply {
//                    findViewById<ImageView>(R.id.item_detail_icon).setImageResource(getDetailIcon(it))
//                    findViewById<TextView>(R.id.item_detail_title).text = getDetailTile(it)
//                    findViewById<TextView>(R.id.item_detail_desc).setTextColor(colorData.containerColor)
//                    findViewById<TextView>(R.id.item_detail_desc).text = getDetailSubTile(it,data.result)
//                }
//            )
//        }
        bind.itemRealtimeDetailRv.adapter = DetailDescAdapter(
            data.result,
            colorData
        )
    }
}
class DetailDescAdapter(
    private val data:WeatherVO.Result,
    private val colorData: ColorContainerData
):RecyclerView.Adapter<BaseViewHolder<ItemDetailBinding>>(){
    private val listType = listOf(
        DetailViewType.ViewTypeWind,
        DetailViewType.ViewTypeWet,
        DetailViewType.ViewTypeUltraviolet,
        DetailViewType.ViewTypePressure,
        DetailViewType.ViewTypeVisibility,
        DetailViewType.ViewTypeCloudRate
    )
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): BaseViewHolder<ItemDetailBinding>  {
        return BaseViewHolder(
            ItemDetailBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: BaseViewHolder<ItemDetailBinding>, position: Int) {
        holder.bind.apply {
            itemDetailIcon.setImageResource(getDetailIcon(listType[position]))
            itemDetailTitle.text = getDetailTile(listType[position])
            itemDetailDesc.setTextColor(colorData.containerColor)
            itemDetailDesc.text = getDetailSubTile(listType[position],data)
        }
    }

    override fun getItemCount() = listType.size


}