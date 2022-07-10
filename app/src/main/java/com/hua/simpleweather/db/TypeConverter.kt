package com.hua.simpleweather.db

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.hua.model.weather.WeatherVO

val gson = Gson()

private val temperatureType = object : TypeToken<List<WeatherVO.Result.Temperature>>(){}.type
private val skyIconType = object :TypeToken<List<WeatherVO.Result.Skycon>>(){}.type
private val lifeIndexType = object :TypeToken<List<String>>(){}.type
private val astroType = object :TypeToken<List<WeatherVO.Result.Astro>>(){}.type
private val lifeDescType = object :TypeToken<List<WeatherVO.Result.LifeDescription>>(){}.type
private val lifeContentType = object :TypeToken<List<WeatherVO.Result.Content>>(){}.type
private val dataType = object :TypeToken<List<WeatherVO.Result.Data>>(){}.type
private val windType = object :TypeToken<List<WeatherVO.Result.Wind>>(){}.type
private val aqiType = object :TypeToken<List<WeatherVO.Result.HourAQI>>(){}.type
private val dailyAqi = object :TypeToken<List<WeatherVO.Result.DailyAQI>>(){}.type
private val dailyPM25 = object :TypeToken<List<WeatherVO.Result.DailyPM25>>(){}.type
/**
 * 对数据库列表进行转换的转换类
 */

class TypeConverter {

    @TypeConverter
    fun temperatureToString(list:List<WeatherVO.Result.Temperature>):String{
        return gson.toJson(list)
    }

    @TypeConverter
    fun stringToTemperature(json:String):List<WeatherVO.Result.Temperature>{
        return gson.fromJson(json,temperatureType)
    }

    @TypeConverter
    fun skyIconToString(list:List<WeatherVO.Result.Skycon>):String{
        return gson.toJson(list)
    }

    @TypeConverter
    fun stringToSkyIcon(skyIcon:String):List<WeatherVO.Result.Skycon>{
        return gson.fromJson(skyIcon,skyIconType)
    }

    @TypeConverter
    fun lifeIndexToString(list:List<String>):String{
        return gson.toJson(list)
    }
    @TypeConverter
    fun stringToLifeIndex(skyIcon:String):List<String>{
        return gson.fromJson(skyIcon,lifeIndexType)
    }

    @TypeConverter
    fun astroToString(list:List<WeatherVO.Result.Astro>):String{
        return gson.toJson(list)
    }
    @TypeConverter
    fun stringToAstro(skyIcon:String):List<WeatherVO.Result.Astro>{
        return gson.fromJson(skyIcon, astroType)
    }

    @TypeConverter
    fun lifeDescriptionToString(list:List<WeatherVO.Result.LifeDescription>):String{
        return gson.toJson(list)
    }
    @TypeConverter
    fun stringToLifeDescription(skyIcon:String):List<WeatherVO.Result.LifeDescription>{
        return gson.fromJson(skyIcon,lifeDescType)
    }

    @TypeConverter
    fun contentToString(list:List<WeatherVO.Result.Content>):String{
        return gson.toJson(list)
    }
    @TypeConverter
    fun stringToLifeContent(skyIcon:String):List<WeatherVO.Result.Content>{
        return gson.fromJson(skyIcon, lifeContentType)
    }

    @TypeConverter
    fun dataToString(list:List<WeatherVO.Result.Data>):String{
        return gson.toJson(list)
    }

    @TypeConverter
    fun stringToData(skyIcon:String):List<WeatherVO.Result.Data>{
        return gson.fromJson(skyIcon, dataType)
    }

    @TypeConverter
    fun windToString(list:List<WeatherVO.Result.Wind>):String{
        return gson.toJson(list)
    }

    @TypeConverter
    fun stringToWind(skyIcon:String):List<WeatherVO.Result.Wind>{
        return gson.fromJson(skyIcon, windType)
    }

    @TypeConverter
    fun hourAQIToString(list:List<WeatherVO.Result.HourAQI>):String{
        return gson.toJson(list)
    }
    @TypeConverter
    fun stringToHourAQI(skyIcon:String):List<WeatherVO.Result.HourAQI>{
        return gson.fromJson(skyIcon, aqiType)
    }

    @TypeConverter
    fun dailyAQIToString(list:List<WeatherVO.Result.DailyAQI>):String{
        return gson.toJson(list)
    }
    @TypeConverter
    fun stringToDailyAQI(skyIcon:String):List<WeatherVO.Result.DailyAQI>{
        return gson.fromJson(skyIcon, dailyAqi)
    }

    @TypeConverter
    fun dailyPM25ToString(list:List<WeatherVO.Result.DailyPM25>):String{
        return gson.toJson(list)
    }
    @TypeConverter
    fun stringToDailyPM25(skyIcon:String):List<WeatherVO.Result.DailyPM25>{
        return gson.fromJson(skyIcon, dailyPM25)
    }

}