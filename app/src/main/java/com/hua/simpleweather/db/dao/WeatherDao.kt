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
    suspend fun insertWeather(vararg weatherBean: WeatherBean)

    @Query("select count(*) from weatherbean")
    suspend fun getCityCount():Int

    @Query("select * from weatherbean order by id")
    fun getAllWeather(): Flow<List<WeatherBean>>
//    fun getAllWeather():LiveData<List<WeatherBean>>

    @Query("select * from weatherbean order by id")
    suspend fun selectCityWeather(): List<WeatherBean>
//    fun getAllWeather():LiveData<List<WeatherBean>>

    @Query("select count(*) from weatherbean where :address = address")
    suspend fun cityExist(address:String):Int

    @Query("select lng,lat,cityName from weatherbean order by id")
    suspend fun selectAllCities():List<CityBean>

    @Query("select address from weatherbean")
    fun selectLocalCity():Flow<List<String>>

    @Delete
    suspend fun deleteCity(weatherBean: WeatherBean)

    @Query("delete from weatherbean")
    suspend fun deleteAllWeather()



    @Transaction
    suspend fun updateCity(list:List<WeatherBean>){
        deleteAllWeather()
        insertWeather(*list.toTypedArray())
    }
}