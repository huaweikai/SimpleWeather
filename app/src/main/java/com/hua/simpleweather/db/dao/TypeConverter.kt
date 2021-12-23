package com.hua.simpleweather.db.dao

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.hua.simpleweather.network.bean.DailyResponse

/**
 * @author : huaweikai
 * @Date   : 2021/12/22
 * @Desc   :转换类
 */

class TypeConverter {
    @TypeConverter
    fun temperatureToString(list: List<DailyResponse.Temperature>):String{
        return Gson().toJson(list)
    }
    @TypeConverter
    fun stringToTemperature(json:String):List<DailyResponse.Temperature>{
        val type = object :TypeToken<List<DailyResponse.Temperature>>(){}.type
        return Gson().fromJson(json,type)
    }

    @TypeConverter
    fun skyIconToString(list:List<DailyResponse.Skycon>):String{
        return Gson().toJson(list)
    }
    @TypeConverter
    fun stringToSkyIcon(skyIcon:String):List<DailyResponse.Skycon>{
        val typeToken = object :TypeToken<List<DailyResponse.Skycon>>(){}.type
        return Gson().fromJson(skyIcon,typeToken)
    }

    @TypeConverter
    fun lifeIndexToString(list:List<String>):String{
        return Gson().toJson(list)
    }
    @TypeConverter
    fun stringToLifeIndex(skyIcon:String):List<String>{
        val typeToken = object :TypeToken<List<String>>(){}.type
        return Gson().fromJson(skyIcon,typeToken)
    }
}