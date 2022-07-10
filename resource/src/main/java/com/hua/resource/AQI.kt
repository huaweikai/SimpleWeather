package com.hua.resource

import com.hua.model.weather.WeatherVO

/**
 * 获取AQI的具体数值或介绍的工具类
 */

fun getAQIDesc(aqi: String): String {
    val aqiInt = aqi.toIntOrNull()
    if (aqiInt == null || aqiInt < 0) return "未知"
    return when (aqiInt) {
        in 0..50 -> "优"
        in 51..100 -> "良"
        in 101..150 -> "轻度污染"
        in 151..200 -> "中度污染"
        in 201..300 -> "重度污染"
        else -> "严重污染"
    }
}

fun getAQIValue(aqiDetailViewType: AQIDetailViewType,data: WeatherVO.Result.AirQuality):Int{
   return when(aqiDetailViewType){
        is AQIDetailViewType.CO->data.co.toInt()
        is AQIDetailViewType.NO2->data.no2
        is AQIDetailViewType.O3->data.o3
        is AQIDetailViewType.PM10->data.pm10
        is AQIDetailViewType.PM25->data.pm25
        is AQIDetailViewType.SO2->data.so2
    }
}

sealed class AQIDetailViewType(val desc:String){
    object PM25:AQIDetailViewType("PM2.5")
    object PM10:AQIDetailViewType("PM10")
    object CO:AQIDetailViewType("CO")
    object SO2:AQIDetailViewType("SO₂")
    object NO2:AQIDetailViewType("NO₂")
    object O3:AQIDetailViewType("O₃")
}