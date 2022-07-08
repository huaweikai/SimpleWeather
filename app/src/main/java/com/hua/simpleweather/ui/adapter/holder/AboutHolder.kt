package com.hua.simpleweather.ui.adapter.holder

import com.hua.material.materialcolor.ColorContainerData
import com.hua.model.weather.WeatherVO
import com.hua.simpleweather.databinding.ItemAboutBinding

class AboutHolder(
    private val bind:ItemAboutBinding
):AbstractMainHolder(bind) {
    override fun onBindView(data: WeatherVO,colorData: ColorContainerData) {

    }
}