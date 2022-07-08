package com.hua.simpleweather.ui.adapter.holder

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.hua.material.materialcolor.ColorContainerData
import com.hua.model.weather.WeatherVO
import com.hua.simpleweather.db.dao.bean.WeatherPO

abstract class AbstractMainHolder(
    viewBind:ViewBinding
):RecyclerView.ViewHolder(viewBind.root) {

    abstract fun onBindView(data:WeatherVO,colorData:ColorContainerData)

}