package com.hua.simpleweather.repository

import com.hua.simpleweather.db.dao.WeatherDao
import com.hua.network.api.WeatherService
import com.hua.model.weather.WeatherVO
import com.hua.network.getOrError
import kotlinx.coroutines.flow.Flow
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
    suspend fun getWeather(lng: String, lat: String, cityName: String,id:Int
    ): WeatherVO? {
        val weatherVO = weatherService.getWeather(lng, lat).getOrError { null }
        weatherVO?.let {
            return it.copy(cityName = cityName, id = id)
        }
        return null
    }


    fun getAllWeather(): Flow<List<WeatherVO>> {
        return dao.getAllWeather()
    }

    suspend fun selectCityWeather(): List<WeatherVO> {
        return dao.selectCityWeather()
    }

    suspend fun selectLocationWeather(): WeatherVO? {
        return dao.selectLocationWeather()
    }

    //添加城市
    suspend fun insertWeather(weather: WeatherVO?) {
        if(weather == null) return
        dao.insertWeather(weather)
    }

    suspend fun updateWeather(weatherPO: WeatherVO){
        dao.insertWeather(weatherPO)
    }


    //判断要添加的城市是否存在
    suspend fun cityExist(primary: String): Int {
        return dao.cityExist(primary)
    }

    //删除城市
    suspend fun deleteCity(weatherPO: WeatherVO){
        return dao.deleteCity(weatherPO)
    }
    //获取城市数量
    suspend fun getCityCount():Int{
        return dao.getCityCount()
    }

    suspend fun selectNoLocationCount():Int{
        return dao.selectNoLocationCount()
    }

    suspend fun updateCityCount(list: List<WeatherVO>){
        dao.updateCity(
            list.filter { it.id!=0 }.mapIndexed { index, weatherVO ->
                weatherVO.copy(id = index + 1)
            }
        )
    }

    suspend fun selectWeather(lng: String,lat: String):WeatherVO?{
        return weatherService.getWeather(lng, lat).getOrError { null }
    }
}