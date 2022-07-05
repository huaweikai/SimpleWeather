package com.hua.simpleweather.network.interfaces

import com.hua.simpleweather.network.bean.DailyResponse
import com.hua.simpleweather.network.bean.RealtimeResponse
import com.hua.simpleweather.network.bean.WeatherVO
import com.hua.simpleweather.other.Contacts
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

/**
 * @author : huaweikai
 * @Date   : 2021/12/22
 * @Desc   : wearther
 */
interface WeatherService {
    @GET("v2.5/${Contacts.TOKEN}/{lng},{lat}/realtime.json")
    suspend fun getRealTimeWeather(
        @Path("lng")lng:String,
        @Path("lat")lat:String
    ):RealtimeResponse

    @GET("v2.5/${Contacts.TOKEN}/{lng},{lat}/daily.json")
    suspend fun getDailyWeather(
        @Path("lng")lng:String,
        @Path("lat")lat:String
    ):DailyResponse

    @GET("v2.5/${Contacts.TOKEN}/{lng},{lat}/weather.json")
    suspend fun getWeather(
        @Path("lng")lng:String,
        @Path("lat")lat:String
    ):WeatherVO
}