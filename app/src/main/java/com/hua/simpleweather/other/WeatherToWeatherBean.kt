package com.hua.simpleweather.other

import com.hua.simpleweather.db.dao.bean.WeatherPO
import com.hua.model.weather.WeatherVO

/**
 * @author : huaweikai
 * @Date   : 2021/12/22
 * @Desc   : 网络数据转本次数据库可存数据
 */
object WeatherToWeatherBean {
    fun change(weather: WeatherVO, id: Int): WeatherPO {
        val realtime = weather.result.realtime
        val daily = weather.result.daily
        val life = listOf(
            daily.index.coldRisk[0].desc,
            daily.index.dressing[0].desc,
            daily.index.ultraviolet[0].desc,
            daily.index.carWashing[0].desc
        )
        return WeatherPO(
            weather.location[1],
            weather.location[0],
            realtime.skycon,
            realtime.temperature,
            realtime.airQuality.pm25.toFloat(),
            daily.temperature,
            daily.skycon,
            life,
            "",
            id
        )
    }
}