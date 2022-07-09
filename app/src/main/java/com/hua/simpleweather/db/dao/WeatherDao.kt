package com.hua.simpleweather.db.dao

import androidx.room.*
import com.hua.model.weather.WeatherVO
import com.hua.simpleweather.db.dao.bean.CityBean
import kotlinx.coroutines.flow.Flow

/**
 * @author : huaweikai
 * @Date   : 2021/12/22
 * @Desc   : dao
 */
@Dao
interface WeatherDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWeather(vararg weatherPO: WeatherVO)

    @Query("select count(*) from weather")
    suspend fun getCityCount():Int

    @Query("select count(*) from weather where id > 0")
    suspend fun selectNoLocationCount():Int

    @Query("select * from weather order by id")
    fun getAllWeather(): Flow<List<WeatherVO>>
//    fun getAllWeather():LiveData<List<WeatherBean>>

    @Query("select * from weather order by id")
    suspend fun selectCityWeather(): List<WeatherVO>
//    fun getAllWeather():LiveData<List<WeatherBean>>

    @Query("select * from weather where id = 0")
    suspend fun selectLocationWeather():WeatherVO?

    @Query("select count(*) from weather where :address = address")
    suspend fun cityExist(address: String):Int

    @Query("select id,lng,lat,cityName,updateTime from weather order by id")
    suspend fun selectAllCities():List<CityBean>

    @Query("select address from weather")
    fun selectLocalCity():Flow<List<String>>

    @Delete
    suspend fun deleteCity(weatherVO:WeatherVO)

    @Query("delete from weather where id>0")
    suspend fun deleteAllWeather()



    @Transaction
    suspend fun updateCity(list:List<WeatherVO>){
        deleteAllWeather()
        insertWeather(*list.toTypedArray())
    }
}