package com.hua.simpleweather.repository

import android.util.Log
import com.hua.simpleweather.db.dao.bean.WeatherBean
import com.hua.simpleweather.db.dao.WeatherDao
import com.hua.simpleweather.network.bean.Weather
import com.hua.simpleweather.network.interfaces.WeatherService
import com.hua.simpleweather.other.WeatherToWeatherBean
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import java.lang.Exception
import javax.inject.Inject

/**
 * @author : huaweikai
 * @Date   : 2021/12/22
 * @Desc   : net
 */
class NetRepository @Inject constructor(
    private val weatherService: WeatherService,
    private val dao: WeatherDao
) {

    //获取天气
    suspend fun getWeather(lng: String, lat: String, cityName: String
    ): Weather? {
        var weather: Weather?
        withContext(Dispatchers.IO) {
            weather = try {
                val realtimeTry = weatherService.runCatching {
                    return@runCatching this.getRealTimeWeather(lng, lat)
                }
                val dailyTry = weatherService.runCatching {
                    return@runCatching this.getDailyWeather(lng, lat)
                }
                val realtimeResponse = realtimeTry.getOrNull()
                val dailyResponse = dailyTry.getOrNull()
                Log.d("TAG", "getWeather: $dailyResponse $realtimeResponse")
                if(realtimeResponse?.status == "ok" && dailyResponse?.status == "ok"){
                    Weather(realtimeResponse, dailyResponse, cityName).apply {
                        Log.d("TAG", "getWeather: $this")
                    }
                }else{
                    null
                }
            } catch (e: Exception) {
                null
            }
        }
        return weather
    }


    fun getAllWeather(): Flow<List<WeatherBean>> {
        return dao.getAllWeather()
    }

    //添加城市
    suspend fun insertWeather(weather: Weather,id:Int) {
        val dailyResponse = weather.dailyresponse
        val realtimeResponse = weather.realtimeresponse
        if (dailyResponse != null && realtimeResponse != null) {
            dao.insertWeather(WeatherToWeatherBean.change(weather,id))
        }
    }

    suspend fun updateWeather(weatherBean: WeatherBean){
        dao.insertWeather(weatherBean)
    }

    //判断要添加的城市是否存在
    suspend fun cityExist(primary: String): Int {
        return dao.cityExist(primary)
    }

    //删除城市
    suspend fun deleteCity(weatherBean: WeatherBean){
        return dao.deleteCity(weatherBean)
    }
}