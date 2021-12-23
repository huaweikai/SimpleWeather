package com.hua.simpleweather.other

import android.util.Log
import com.hua.simpleweather.db.dao.bean.WeatherBean
import com.hua.simpleweather.network.bean.Weather

/**
 * @author : huaweikai
 * @Date   : 2021/12/22
 * @Desc   : 网络数据转本次数据库可存数据
 */
object WeatherToWeatherBean {
    fun change(weather: Weather,id:Int): WeatherBean {
        val realtime = weather.realtimeresponse!!.result.realtime
        val daily = weather.dailyresponse!!.result.daily
        val life = listOf(
            daily.index.coldRisk[0].desc,
            daily.index.dressing[0].desc,
            daily.index.ultraviolet[0].desc,
            daily.index.carWashing[0].desc
        )
        return WeatherBean(
            weather.realtimeresponse.location[1],
            weather.realtimeresponse.location[0],
            realtime.skycon,
            realtime.temperature,
            realtime.airQuality.aqi.chn,
            daily.temperature,
            daily.skycon,
            life,
            weather.cityName,
            id
        )
    }
}