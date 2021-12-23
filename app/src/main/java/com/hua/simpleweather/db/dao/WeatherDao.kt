package com.hua.simpleweather.db.dao

import androidx.room.*
import com.hua.simpleweather.db.dao.bean.CityBean
import com.hua.simpleweather.db.dao.bean.WeatherBean
import kotlinx.coroutines.flow.Flow

/**
 * @author : huaweikai
 * @Date   : 2021/12/22
 * @Desc   : dao
 */
@Dao
interface WeatherDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWeather(weatherBean: WeatherBean)

    @Query("select count(*) from weatherbean")
    suspend fun getCityCount():Int

    @Query("select * from weatherbean")
    fun getAllWeather(): Flow<List<WeatherBean>>
//    fun getAllWeather():LiveData<List<WeatherBean>>

    @Query("select count(*) from weatherbean where :address = address")
    suspend fun cityExist(address:String):Int

    @Query("select lng,lat,cityName from weatherbean")
    suspend fun selectAllCities():List<CityBean>

    @Delete
    suspend fun deleteCity(weatherBean: WeatherBean)
}