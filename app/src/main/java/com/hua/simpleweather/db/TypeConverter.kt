package com.hua.simpleweather.db

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.hua.model.weather.WeatherVO

val gson = Gson()

class TypeConverter {

    @TypeConverter
    fun temperatureToString(list:List<WeatherVO.Result.Temperature>):String{
        return gson.toJson(list)
    }

    @TypeConverter
    fun stringToTemperature(json:String):List<WeatherVO.Result.Temperature>{
        val type = object : TypeToken<List<WeatherVO.Result.Temperature>>(){}.type
        return gson.fromJson(json,type)
    }

    @TypeConverter
    fun skyIconToString(list:List<WeatherVO.Result.Skycon>):String{
        return gson.toJson(list)
    }

    @TypeConverter
    fun stringToSkyIcon(skyIcon:String):List<WeatherVO.Result.Skycon>{
        val typeToken = object :TypeToken<List<WeatherVO.Result.Skycon>>(){}.type
        return gson.fromJson(skyIcon,typeToken)
    }

    @TypeConverter
    fun lifeIndexToString(list:List<String>):String{
        return gson.toJson(list)
    }
    @TypeConverter
    fun stringToLifeIndex(skyIcon:String):List<String>{
        val typeToken = object :TypeToken<List<String>>(){}.type
        return gson.fromJson(skyIcon,typeToken)
    }

    @TypeConverter
    fun astroToString(list:List<WeatherVO.Result.Astro>):String{
        return gson.toJson(list)
    }
    @TypeConverter
    fun stringToAstro(skyIcon:String):List<WeatherVO.Result.Astro>{
        val typeToken = object :TypeToken<List<WeatherVO.Result.Astro>>(){}.type
        return gson.fromJson(skyIcon,typeToken)
    }

    @TypeConverter
    fun lifeDescriptionToString(list:List<WeatherVO.Result.LifeDescription>):String{
        return gson.toJson(list)
    }
    @TypeConverter
    fun stringToLifeDescription(skyIcon:String):List<WeatherVO.Result.LifeDescription>{
        val typeToken = object :TypeToken<List<WeatherVO.Result.LifeDescription>>(){}.type
        return gson.fromJson(skyIcon,typeToken)
    }

    @TypeConverter
    fun contentToString(list:List<WeatherVO.Result.Content>):String{
        return gson.toJson(list)
    }
    @TypeConverter
    fun stringToLifeContent(skyIcon:String):List<WeatherVO.Result.Content>{
        val typeToken = object :TypeToken<List<WeatherVO.Result.Content>>(){}.type
        return gson.fromJson(skyIcon,typeToken)
    }

    @TypeConverter
    fun dataToString(list:List<WeatherVO.Result.Data>):String{
        return gson.toJson(list)
    }

    @TypeConverter
    fun stringToData(skyIcon:String):List<WeatherVO.Result.Data>{
        val typeToken = object :TypeToken<List<WeatherVO.Result.Data>>(){}.type
        return gson.fromJson(skyIcon,typeToken)
    }

    @TypeConverter
    fun windToString(list:List<WeatherVO.Result.Wind>):String{
        return gson.toJson(list)
    }

    @TypeConverter
    fun stringToWind(skyIcon:String):List<WeatherVO.Result.Wind>{
        val typeToken = object :TypeToken<List<WeatherVO.Result.Wind>>(){}.type
        return gson.fromJson(skyIcon,typeToken)
    }

    @TypeConverter
    fun hourAQIToString(list:List<WeatherVO.Result.HourAQI>):String{
        return gson.toJson(list)
    }
    @TypeConverter
    fun stringToHourAQI(skyIcon:String):List<WeatherVO.Result.HourAQI>{
        val typeToken = object :TypeToken<List<WeatherVO.Result.HourAQI>>(){}.type
        return gson.fromJson(skyIcon,typeToken)
    }


}