package com.hua.simpleweather.db.dao.bean

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.hua.simpleweather.network.bean.DailyResponse

/**
 * @author : huaweikai
 * @Date   : 2021/12/22
 * @Desc   : bean
 */
@Entity
data class WeatherBean(
    val lng: String,
    val lat: String,
    val realtime_skyIcon: String,
    val realtime_temperature: Float,
    val realtime_airQuality_aqi: Float,
    val daily_temperature:List<DailyResponse.Temperature>,
    val daily_skyIcon: List<DailyResponse.Skycon>,
    val life_index:List<String>,
    val cityName:String,
    @PrimaryKey
    val address :String = lng + lat,
)