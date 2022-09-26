package com.hua.simpleweather.db

import androidx.room.TypeConverter
import com.hua.model.weather.WeatherVO
import com.hua.network.utils.globalJson
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
/**
 * 对数据库列表进行转换的转换类
 */

class TypeConverter {

    @TypeConverter
    fun temperatureToString(list:List<WeatherVO.Result.Temperature>):String{
        return globalJson.encodeToString(list)
    }

    @TypeConverter
    fun stringToTemperature(json:String):List<WeatherVO.Result.Temperature>{
        return globalJson.decodeFromString(json)
    }

    @TypeConverter
    fun skyIconToString(list:List<WeatherVO.Result.Skycon>):String{
        return globalJson.encodeToString(list)
    }

    @TypeConverter
    fun stringToSkyIcon(json: String):List<WeatherVO.Result.Skycon>{
        return globalJson.decodeFromString(json)
    }

    @TypeConverter
    fun lifeIndexToString(list:List<String>):String{
        return globalJson.encodeToString(list)
    }
    @TypeConverter
    fun stringToLifeIndex(json: String):List<String>{
        return globalJson.decodeFromString(json)
    }

    @TypeConverter
    fun astroToString(list:List<WeatherVO.Result.Astro>):String{
        return globalJson.encodeToString(list)
    }
    @TypeConverter
    fun stringToAstro(json: String):List<WeatherVO.Result.Astro>{
        return globalJson.decodeFromString(json)
    }

    @TypeConverter
    fun lifeDescriptionToString(list:List<WeatherVO.Result.LifeDescription>):String{
        return globalJson.encodeToString(list)
    }
    @TypeConverter
    fun stringToLifeDescription(json: String):List<WeatherVO.Result.LifeDescription>{
        return globalJson.decodeFromString(json)
    }

    @TypeConverter
    fun contentToString(list:List<WeatherVO.Result.Content>):String{
        return globalJson.encodeToString(list)
    }
    @TypeConverter
    fun stringToLifeContent(json: String):List<WeatherVO.Result.Content>{
        return globalJson.decodeFromString(json)
    }

    @TypeConverter
    fun dataToString(list:List<WeatherVO.Result.Data>):String{
        return globalJson.encodeToString(list)
    }

    @TypeConverter
    fun stringToData(json: String):List<WeatherVO.Result.Data>{
        return globalJson.decodeFromString(json)
    }

    @TypeConverter
    fun windToString(list:List<WeatherVO.Result.Wind>):String{
        return globalJson.encodeToString(list)
    }

    @TypeConverter
    fun stringToWind(json: String):List<WeatherVO.Result.Wind>{
        return globalJson.decodeFromString(json)
    }

    @TypeConverter
    fun hourAQIToString(list:List<WeatherVO.Result.HourAQI>):String{
        return globalJson.encodeToString(list)
    }
    @TypeConverter
    fun stringToHourAQI(json: String):List<WeatherVO.Result.HourAQI>{
        return globalJson.decodeFromString(json)
    }

    @TypeConverter
    fun dailyAQIToString(list:List<WeatherVO.Result.DailyAQI>):String{
        return globalJson.encodeToString(list)
    }
    @TypeConverter
    fun stringToDailyAQI(json: String):List<WeatherVO.Result.DailyAQI>{
        return globalJson.decodeFromString(json)
    }

    @TypeConverter
    fun dailyPM25ToString(list:List<WeatherVO.Result.DailyPM25>):String{
        return globalJson.encodeToString(list)
    }
    @TypeConverter
    fun stringToDailyPM25(json: String):List<WeatherVO.Result.DailyPM25>{
        return globalJson.decodeFromString(json)
    }

}