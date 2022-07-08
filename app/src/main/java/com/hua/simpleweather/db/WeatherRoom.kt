package com.hua.simpleweather.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.hua.model.weather.WeatherVO
import com.hua.simpleweather.db.dao.CityDao
import com.hua.simpleweather.db.dao.WeatherDao
import com.hua.simpleweather.db.dao.bean.LocalCity

/**
 * @author : huaweikai
 * @Date   : 2021/12/22
 * @Desc   : room
 */
@Database(
    version = 1,
    entities = [WeatherVO::class, LocalCity::class],
    exportSchema = false,
)
@TypeConverters(TypeConverter::class)
abstract class WeatherRoom :RoomDatabase(){
    abstract val weatherDao: WeatherDao
    abstract val cityDao: CityDao
}