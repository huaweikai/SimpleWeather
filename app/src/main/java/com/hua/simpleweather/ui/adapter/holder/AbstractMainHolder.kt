package com.hua.simpleweather.ui.adapter.holder

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.hua.material.materialcolor.ColorContainerData
import com.hua.model.weather.WeatherVO
import com.hua.simpleweather.db.dao.bean.WeatherPO

//将卡片的holder抽取出来，然后只需要在Module中调用onBindView即可调用对应的holder
abstract class AbstractMainHolder(
    viewBind:ViewBinding,
    val colorData: ColorContainerData
):RecyclerView.ViewHolder(viewBind.root) {

    abstract fun onBindView(data:WeatherVO)

}