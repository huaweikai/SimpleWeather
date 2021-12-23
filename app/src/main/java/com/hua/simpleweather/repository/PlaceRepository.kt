package com.hua.simpleweather.repository


import com.hua.simpleweather.db.dao.CityDao
import com.hua.simpleweather.db.dao.WeatherDao
import com.hua.simpleweather.db.dao.bean.CityBean
import com.hua.simpleweather.db.dao.bean.LocalCity
import javax.inject.Inject

/**
 * @author : huaweikai
 * @Date   : 2021/12/22
 * @Desc   : repository
 */
class PlaceRepository @Inject constructor(
    private val cityDao: CityDao,
    private val weatherDao: WeatherDao
) {
    //模糊检索地址
    suspend fun selectCity(query: String): List<LocalCity> {
        return cityDao.selectCity("%$query%")
    }
    //第一次进入软件刷新
    suspend fun insertCity(localCity: LocalCity){
        cityDao.insertCity(localCity)
    }

    //检索都有什么城市，批量更新天气
    suspend fun selectCities():List<CityBean>{
        return weatherDao.selectAllCities()
    }
}