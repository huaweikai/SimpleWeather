package com.hua.network.api

import com.hua.network.ApiResult
import com.hua.network.Contacts
import com.hua.network.bean.DailyResponse
import com.hua.network.bean.RealtimeResponse
import com.hua.model.weather.WeatherVO
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
    ):ApiResult<RealtimeResponse>

    @GET("v2.5/${Contacts.TOKEN}/{lng},{lat}/daily.json")
    suspend fun getDailyWeather(
        @Path("lng")lng:String,
        @Path("lat")lat:String
    ):ApiResult<DailyResponse>

    @GET("v2.5/${Contacts.TOKEN}/{lng},{lat}/weather.json?alert=true")
    suspend fun getWeather(
        @Path("lng")lng:String,
        @Path("lat")lat:String
    ):ApiResult<WeatherVO>
}