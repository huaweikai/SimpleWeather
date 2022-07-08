package com.hua.simpleweather.db.dao.bean

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.hua.model.weather.WeatherVO
import java.util.*

/**
 * @author : huaweikai
 * @Date   : 2021/12/22
 * @Desc   : bean
 */
@Entity
data class WeatherPO(
    val lng: String,
    val lat: String,
    val realtime_skyIcon: String,
    val realtime_temperature: Float,
    val realtime_airQuality_aqi: Float,
    val daily_temperature:List<WeatherVO.Result.Temperature>,
    val daily_skyIcon: List<WeatherVO.Result.Skycon>,
    val life_index:List<String>,
    val cityName:String,
    val id:Int,
    val date: Long = System.currentTimeMillis(),
    @PrimaryKey
    val address :String = lng + lat
)